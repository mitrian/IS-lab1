package com.lab1.services;

import com.lab1.dao.entities.Address;
import com.lab1.dao.entities.Coordinates;
import com.lab1.dao.entities.Organization;
import com.lab1.dao.repositories.AddressRepository;
import com.lab1.dao.repositories.CoordinatesRepository;
import com.lab1.dao.repositories.OrganizationRepository;
import com.lab1.dao.repositories.StaffRepository;
import com.lab1.dto.*;
import com.lab1.exceptions.entity.impl.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final AddressRepository addressRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final StaffRepository staffRepository;
    private final SecurityService securityService;

    public OrganizationService(OrganizationRepository organizationRepository, AddressRepository addressRepository, CoordinatesRepository coordinatesRepository, StaffRepository staffRepository, SecurityService securityService) {
        this.organizationRepository = organizationRepository;
        this.addressRepository = addressRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.staffRepository = staffRepository;
        this.securityService = securityService;
    }


    public Page<OrganizationResponseDTO> getAllOrganizations(Pageable pageable) {
        return organizationRepository.findAll(pageable).map(this::fromEntity);
    }


    public OrganizationResponseDTO getOrganizationById(int id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationAbsenceException("Organization с данным id не существует"));

        return fromEntity(organization);
    }


    @Transactional
    public OrganizationResponseDTO createOrganization(OrganizationRequestDTO organizationRequestDTO) {
        Organization organization = toEntity(organizationRequestDTO);
        Organization savedOrganization = organizationRepository.save(organization);
        return fromEntity(savedOrganization);
    }


    @Transactional
    public OrganizationResponseDTO updateOrganization(int id, OrganizationRequestDTO organizationRequestDTO) {
        Organization organization = organizationRepository.findByIdAndCreatedBy(id, securityService.findUserName())
                .orElseThrow(() -> new OrganizationUpdateException("Сущности Organization, принадлежащей Вам, с таким id не существует"));

        Coordinates coordinates = coordinatesRepository.findById(organizationRequestDTO.coordinatesId())
                .orElseThrow(() -> new CoordinatesAbsenceException("Coordinates с данным id не существует"));
        organization.setCoordinates(coordinates);

        Address officialAddress = addressRepository.findById(organizationRequestDTO.officialAddressId())
                .orElseThrow(() -> new AddressAbsenceException("Address с данным id не существует"));
        organization.setOfficialAddress(officialAddress);

        Address postalAddress = addressRepository.findById(organizationRequestDTO.postalAddressId()) // fixed the second address id to be postalAddressId
                .orElseThrow(() -> new AddressAbsenceException("Address с данным id не существует"));
        organization.setPostalAddress(postalAddress);
        Organization updatedOrganization = organizationRepository.save(organization);

        return fromEntity(updatedOrganization);
    }


    @Transactional
    public boolean deleteOrganization(int id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationInaccessibleDeleteException("Organization с данным id не существует"));

        if (!organization.getCreatedBy().equals(securityService.findUserName()))
            throw new OrganizationInaccessibleDeleteException("У Вас нет доступа к данному объекту Organization");

        if (staffRepository.existsByOrganizationId(id))
            throw new OrganizationInaccessibleDeleteException("Этот объект Organization связан с другими. Удаление невозможно");

        organizationRepository.delete(organization);
        return true;
    }


    public Double avgRatingOfOrganization() {
        if (organizationRepository.count() == 0) {
            throw new OrganizationAbsenceException("У Вас нет доступа к данному объекту Coordinates");
        }
        return organizationRepository.findAverageRating();
    }


    public long countAmountWithGreaterRating(int rating) {
        return organizationRepository.countByRatingGreaterThan(rating);
    }




    public List<OrganizationResponseDTO> getOrganizationsByFullNamePrefix(String prefix) {
        List<Organization> organizations = organizationRepository.findByFullNameStartingWith(prefix);
        System.out.println(organizations);
        return organizations.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    private OrganizationResponseDTO fromEntity(Organization organization) {
        CoordinatesResponseDTO coordinatesResponseDTO = new CoordinatesResponseDTO(
                organization.getCoordinates().getId(),
                organization.getCoordinates().getX(),
                organization.getCoordinates().getY()
        );

        AddressResponseDTO officialAddressDTO = organization.getOfficialAddress() != null
                ? new AddressResponseDTO(
                organization.getOfficialAddress().getId(),
                organization.getOfficialAddress().getZipCode(),
                new LocationResponseDTO(
                        organization.getOfficialAddress().getTown().getId(),
                        organization.getOfficialAddress().getTown().getX(),
                        organization.getOfficialAddress().getTown().getY(),
                        organization.getOfficialAddress().getTown().getZ(),
                        organization.getOfficialAddress().getTown().getName()
                )
        ) : null;

        AddressResponseDTO postalAddressDTO = organization.getPostalAddress() != null
                ? new AddressResponseDTO(
                organization.getPostalAddress().getId(),
                organization.getPostalAddress().getZipCode(),
                new LocationResponseDTO(
                        organization.getPostalAddress().getTown().getId(),
                        organization.getPostalAddress().getTown().getX(),
                        organization.getPostalAddress().getTown().getY(),
                        organization.getPostalAddress().getTown().getZ(),
                        organization.getPostalAddress().getTown().getName()
                )
        ) : null;

        return new OrganizationResponseDTO(
                organization.getId(),
                organization.getName(),
                coordinatesResponseDTO,
                organization.getCreationDate(),
                officialAddressDTO,
                organization.getAnnualTurnover(),
                organization.getEmployeesCount(),
                organization.getRating(),
                organization.getFullName(),
                organization.getType(),
                postalAddressDTO
        );
    }


    private Organization toEntity(OrganizationRequestDTO organizationRequestDTO) {
        Organization organization = new Organization();
        organization.setName(organizationRequestDTO.name());
        organization.setAnnualTurnover(organizationRequestDTO.annualTurnover());
        organization.setEmployeesCount(organizationRequestDTO.employeesCount());
        organization.setRating(organizationRequestDTO.rating());
        organization.setFullName(organizationRequestDTO.fullName());
        organization.setType(organizationRequestDTO.type());

        Optional<Coordinates> coordinatesOptional = coordinatesRepository.findById(organizationRequestDTO.coordinatesId());
        if (coordinatesOptional.isEmpty()){
            throw new CoordinatesAbsenceException("Coordinates с таким id не существует");
        }
        coordinatesOptional.ifPresent(organization::setCoordinates);


        Optional<Address> officialAddressOptional = addressRepository.findById(organizationRequestDTO.officialAddressId());
        officialAddressOptional.ifPresent(organization::setOfficialAddress);

        Optional<Address> postalAddressOptional = addressRepository.findById(organizationRequestDTO.postalAddressId());
        postalAddressOptional.ifPresent(organization::setPostalAddress);

        organization.setCreationDate(ZonedDateTime.now());
        return organization;
    }
}