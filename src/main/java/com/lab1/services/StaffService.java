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
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            return fromEntity(staff.get());
        }
        throw new StaffAbsenceException("Staff с данным id не существует");
    }


    @Transactional
    public StaffResponseDTO createStaff(StaffRequestDTO staffRequestDTO) {
        Staff staff = toEntity(staffRequestDTO);
        Staff savedStaff = staffRepository.save(staff);
        return fromEntity(savedStaff);
    }


    public StaffResponseDTO updateStaff(Long id, StaffRequestDTO staffRequestDTO) {
        Optional<Staff> staffOptional = staffRepository.findByIdAndCreatedBy(id, securityService.findUserName());
        if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            staff.setName(staffRequestDTO.name());
            Optional<Organization> organizationOptional = organizationRepository.findByIdAndCreatedBy(
                    staffRequestDTO.organizationId(),
                    securityService.findUserName());
            if (organizationOptional.isPresent()) {
                staff.setOrganization(organizationOptional.get());
                Staff savedStaff = staffRepository.save(staff);
                return fromEntity(savedStaff);
            } else {
                throw new OrganizationAbsenceException("Organizateion с данным id, принадлежащей Вам не существует");
            }
        }
        throw new StaffUpdateException("Сущности Staff с таким id ,принадлежащей Вам, не существует");
    }


    @Transactional
    public boolean deleteStaff(Long id) {
        Optional<Staff> staffOptional = staffRepository.findByIdAndCreatedBy(id, securityService.findUserName());
        if (staffOptional.isPresent()) {
            staffRepository.delete(staffOptional.get());
            return true;
        }
        if (staffRepository.findById(id).isEmpty()) {
            throw new StaffInaccessibleDeleteException("Staff с данным id не существует");
        }
        throw new StaffInaccessibleDeleteException("У Вас нет доступа к данному объекту Staff");
    }


    public boolean dismissStaff(int id){
        System.out.println("AAAAAAA");
        if (organizationRepository.findByIdAndCreatedBy(id, securityService.findUserName()).isEmpty()) {
            throw new OrganizationAbsenceException("Такой Organization, принадлежащей вам не существует");
        }
        System.out.println("BBBBBBBB");
        staffRepository.setOrganizationToNullByOrganizationId(id);
        return true;
    }


    public StaffResponseDTO assignStaffToOrganization(Long staffId, int organizationId) {
        if (organizationRepository.findByIdAndCreatedBy(organizationId, securityService.findUserName()).isEmpty()) {
            throw new OrganizationAbsenceException("Такой Organization, принадлежащей вам не существует");
        }
        if (staffRepository.findByIdAndCreatedBy(staffId, securityService.findUserName()).isEmpty()) {
            throw new StaffAbsenceException("Такой Staff, принадлежащей вам не существует");
        }
        if (staffRepository.findById(staffId).get().getOrganization().getId() == organizationId) {
            throw new StaffUpdateException("Staff уже находится в данной организации");
        }
        staffRepository.updateOrganizationForStaff(staffId, organizationId);
        Optional<Staff> updatedStaff = staffRepository.findById(staffId);
        if (updatedStaff.isPresent()) {
            Staff staff = updatedStaff.get();
            staff.setOrganization(organizationRepository.findById(organizationId).get());
            if (staff.getOrganization() == null) {
                throw new OrganizationAbsenceException("Организация для сотрудника не была назначена");
            }
            return fromEntity(staff);
        }
        throw new StaffAbsenceException("Не удалось добавить нового сотрудника");
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
