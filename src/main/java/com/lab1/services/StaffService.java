package com.lab1.services;

import com.lab1.dao.entities.Organization;
import com.lab1.dao.entities.Staff;
import com.lab1.dao.repositories.OrganizationRepository;
import com.lab1.dao.repositories.StaffRepository;
import com.lab1.dto.*;
import com.lab1.exceptions.entity.impl.OrganizationAbsenceException;
import com.lab1.exceptions.entity.impl.StaffAbsenceException;
import com.lab1.exceptions.entity.impl.StaffInaccessibleDeleteException;
import com.lab1.exceptions.entity.impl.StaffUpdateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final OrganizationRepository organizationRepository;
    private final SecurityService securityService;

    public StaffService(StaffRepository staffRepository, OrganizationRepository organizationRepository, SecurityService securityService) {
        this.staffRepository = staffRepository;
        this.organizationRepository = organizationRepository;
        this.securityService = securityService;
    }


    public Page<StaffResponseDTO> findAllStaff(Pageable pageable) {
        return staffRepository.findAll(pageable).map(this::fromEntity);
    }


    public StaffResponseDTO findStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffAbsenceException("Staff с данным id не существует"));

        return fromEntity(staff);
    }


    @Transactional
    public StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO) {
        Staff staff = toEntity(staffRequestDTO);
        Staff savedStaff = staffRepository.save(staff);
        return fromEntity(savedStaff);
    }


    @Transactional
    public StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO) {
        Staff staff = staffRepository.findByIdAndCreatedBy(id, securityService.findUserName())
                .orElseThrow(() -> new StaffUpdateException("Сущности Staff с таким id, принадлежащей Вам, не существует"));

        Organization organization = organizationRepository.findByIdAndCreatedBy(
                        staffRequestDTO.organizationId(), securityService.findUserName())
                .orElseThrow(() -> new OrganizationAbsenceException("Organization с данным id, принадлежащей Вам не существует"));

        staff.setName(staffRequestDTO.name());
        staff.setOrganization(organization);

        Staff savedStaff = staffRepository.save(staff);

        return fromEntity(savedStaff);
    }


    @Transactional
    public boolean deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffInaccessibleDeleteException("Staff с данным id не существует"));

        if (!staff.getCreatedBy().equals(securityService.findUserName()))
            throw new StaffInaccessibleDeleteException("У Вас нет доступа к данному объекту Staff");

        staffRepository.delete(staff);
        return true;
    }


    @Transactional
    public boolean dismissStaff(int id){
        if (organizationRepository.findByIdAndCreatedBy(id, securityService.findUserName()).isEmpty())
            throw new OrganizationAbsenceException("Такой Organization, принадлежащей вам не существует");

        staffRepository.setOrganizationToNullByOrganizationId(id);
        return true;
    }


    @Transactional
    public StaffResponseDTO assignStaffToOrganization(Long staffId, int organizationId) {
        Organization organization = organizationRepository.findByIdAndCreatedBy(organizationId, securityService.findUserName())
                .orElseThrow(() -> new OrganizationAbsenceException("Такой Organization, принадлежащей вам не существует"));

        Staff staff = staffRepository.findByIdAndCreatedBy(staffId, securityService.findUserName())
                .orElseThrow(() -> new StaffAbsenceException("Такой Staff, принадлежащий вам не существует"));

        if (staff.getOrganization() != null && staff.getOrganization().getId() == organizationId) {
            throw new StaffUpdateException("Staff уже находится в данной организации");
        }


        staffRepository.updateOrganizationForStaff(staffId, organizationId);

        Staff updatedStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new StaffAbsenceException("Не удалось добавить нового сотрудника"));

        if (updatedStaff.getOrganization() == null) {
            throw new OrganizationAbsenceException("Организация для сотрудника не была назначена");
        }

        return fromEntity(updatedStaff);
    }


    private OrganizationResponseDTO organizationFromStaff(Organization organization) {
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


    private StaffResponseDTO fromEntity(Staff staff) {
        if (staff.getOrganization() != null){
            OrganizationResponseDTO organizationResponseDTO = organizationFromStaff(staff.getOrganization());
            return new StaffResponseDTO(
                    staff.getId(),
                    staff.getName(),
                    organizationResponseDTO
            );
        }
        return new StaffResponseDTO(
                staff.getId(),
                staff.getName(),
                null);

    }


    private Staff toEntity(StaffRequestDTO staffRequestDTO) {
        Staff staff = new Staff();
        staff.setName(staffRequestDTO.name());
        Optional<Organization> organization = organizationRepository.findById(staffRequestDTO.organizationId());
        if (organization.isEmpty()) {
            throw new OrganizationAbsenceException("Organization с таким id не существует");
        }
        staff.setOrganization(organization.get());
        return staff;
    }
}
