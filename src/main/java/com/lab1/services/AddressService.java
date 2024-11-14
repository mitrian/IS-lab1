package com.lab1.services;

import com.lab1.dao.entities.Address;
import com.lab1.dao.entities.Location;
import com.lab1.dao.repositories.AddressRepository;
import com.lab1.dao.repositories.LocationRepository;
import com.lab1.dao.repositories.OrganizationRepository;
import com.lab1.dto.AddressRequestDTO;
import com.lab1.dto.AddressResponseDTO;
import com.lab1.dto.LocationResponseDTO;
import com.lab1.exceptions.entity.impl.AddressAbsenceException;
import com.lab1.exceptions.entity.impl.AddressInaccessibleDeleteDeleteException;
import com.lab1.exceptions.entity.impl.AddressUpdateException;
import com.lab1.exceptions.entity.impl.LocationAbsenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final LocationRepository locationRepository;
    private final OrganizationRepository organizationRepository;
    private final SecurityService securityService;

    public AddressService(AddressRepository addressRepository, LocationRepository locationRepository, OrganizationRepository organizationRepository, SecurityService securityService) {
        this.addressRepository = addressRepository;
        this.locationRepository = locationRepository;
        this.organizationRepository = organizationRepository;
        this.securityService = securityService;
    }


    public Page<AddressResponseDTO> getAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable).map(this::fromEntity);
    }


    public AddressResponseDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressAbsenceException("Address с данным id не существует"));

        return fromEntity(address);
    }


    @Transactional
    public AddressResponseDTO createAddress(AddressRequestDTO addressRequestDTO) {
        Location location = locationRepository.findById(addressRequestDTO.townId())
                .orElseThrow(() -> new LocationAbsenceException("Location с таким id не существует"));

        Address address = toEntity(addressRequestDTO);
        address.setTown(location);
        Address savedAddress = addressRepository.save(address);

        return fromEntity(savedAddress);
    }


    @Transactional
    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO addressRequestDTO) {
        Address address = addressRepository.findByIdAndCreatedBy(id, securityService.findUserName())
                .orElseThrow(() -> new AddressUpdateException("Сущности Address, принадлежащей Вам, с таким id не существует"));

        if (addressRequestDTO.townId() == null) {
            address.setTown(null);
        } else {
            Location location = locationRepository.findById(addressRequestDTO.townId())
                    .orElse(null);
            address.setTown(location);
        }
        Address updatedAddress = addressRepository.save(address);

        return fromEntity(updatedAddress);
    }


    @Transactional
    public boolean deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressInaccessibleDeleteDeleteException("Address с данным id не существует"));

        if (!address.getCreatedBy().equals(securityService.findUserName()))
            throw new AddressInaccessibleDeleteDeleteException("У Вас нет доступа к данному объекту Address");

        if (organizationRepository.existsByOfficialAddress(address) ||
                organizationRepository.existsByPostalAddress(address))
            throw new AddressInaccessibleDeleteDeleteException("Этот объект Address связан с другими. Удаление невозможно");

        addressRepository.delete(address);
        return true;
    }


    private AddressResponseDTO fromEntity(Address address) {
        LocationResponseDTO locationResponseDTO = new LocationResponseDTO(
                address.getTown().getId(),
                address.getTown().getX(),
                address.getTown().getY(),
                address.getTown().getZ(),
                address.getTown().getName()
        );
        return new AddressResponseDTO(
                address.getId(),
                address.getZipCode(),
                locationResponseDTO
        );
    }


    private Address toEntity(AddressRequestDTO addressRequestDTO) {
        Optional<Location> locationOptional = locationRepository.findById(addressRequestDTO.townId());
        Address address = new Address();
        address.setZipCode(addressRequestDTO.zipCode());
        if (locationOptional.isPresent()) {
            address.setTown(locationOptional.get());
        } else {
            address.setTown(null);
        }
        return address;
    }
}
