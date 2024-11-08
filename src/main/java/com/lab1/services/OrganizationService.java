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
    private final SecurityService securityService;

    public OrganizationService(OrganizationRepository organizationRepository, AddressRepository addressRepository, CoordinatesRepository coordinatesRepository, StaffRepository staffRepository, SecurityService securityService) {
        this.organizationRepository = organizationRepository;
        this.addressRepository = addressRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.securityService = securityService;
    }


    public Page<OrganizationResponseDTO> getAllOrganizations(Pageable pageable) {
        return organizationRepository.findAll(pageable).map(this::fromEntity);
    }


    public OrganizationResponseDTO getOrganizationById(int id) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            return fromEntity(organizationOptional.get());
        }
        throw new OrganizationAbsenceException("Organization с данным id не существует");
    }


    @Transactional
    public OrganizationResponseDTO createOrganization(OrganizationRequestDTO organizationRequestDTO) {
        Organization organization = toEntity(organizationRequestDTO);
        Organization savedOrganization = organizationRepository.save(organization);
        return fromEntity(savedOrganization);
    }


    @Transactional
    public OrganizationResponseDTO updateOrganization(int id, OrganizationRequestDTO organizationRequestDTO) {
        Optional<Organization> organizationOptional = organizationRepository.findByIdAndCreatedBy(id, securityService.findUserName());
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();

            Optional<Coordinates> coordinatesOptional = coordinatesRepository.findById(organizationRequestDTO.coordinatesId());
            if (coordinatesOptional.isPresent()) {
                organization.setCoordinates(coordinatesOptional.get());
            } else {
                throw new CoordinatesAbsenceException("Coordinates с данным id не существует");
            }

            Optional<Address> officialAddressOptional = addressRepository.findById(organizationRequestDTO.officialAddressId());
            if (officialAddressOptional.isPresent()) {
                organization.setOfficialAddress(officialAddressOptional.get());
            } else{
                throw new AddressAbsenceException("Address с данным id не существует");
            }

            Optional<Address> postalAddressOptional = addressRepository.findById(organizationRequestDTO.officialAddressId());
            if (postalAddressOptional.isPresent()) {
                organization.setPostalAddress(postalAddressOptional.get());
            } else{
                throw new AddressAbsenceException("Address с данным id не существует");
            }
            Organization updatedOrganization = organizationRepository.save(organization);
            return fromEntity(updatedOrganization);
        }
        throw new OrganizationUpdateException("Сущности Organization, принадлежащей Вам, с таким id не существует");
    }


    @Transactional
    public boolean deleteOrganization(int id) {
        Optional<Organization> organizationOptional = organizationRepository.findByIdAndCreatedBy(id, securityService.findUserName());
        if (organizationOptional.isPresent()) {
            organizationRepository.delete(organizationOptional.get());
            return true;
        }
        if (organizationRepository.findById(id).isEmpty()) {
            throw new OrganizationInaccessibleDeleteException("Organization с данным id не существует");
        }
        throw new OrganizationInaccessibleDeleteException("У Вас нет доступа к данному объекту Organization");
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