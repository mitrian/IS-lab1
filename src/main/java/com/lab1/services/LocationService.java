package com.lab1.services;

import com.lab1.dao.entities.Location;
import com.lab1.dao.repositories.AddressRepository;
import com.lab1.dao.repositories.LocationRepository;
import com.lab1.dto.LocationRequestDTO;
import com.lab1.dto.LocationResponseDTO;
import com.lab1.exceptions.entity.impl.CoordinatesInaccessibleDeleteException;
import com.lab1.exceptions.entity.impl.LocationAbsenceException;
import com.lab1.exceptions.entity.impl.LocationInaccessibleDeleteException;
import com.lab1.exceptions.entity.impl.LocationUpdateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final AddressRepository addressRepository;
    private final SecurityService securityService;

    public LocationService(LocationRepository locationRepository, AddressRepository addressRepository, SecurityService securityService) {
        this.locationRepository = locationRepository;
        this.addressRepository = addressRepository;
        this.securityService = securityService;
    }


    public Page<LocationResponseDTO> getAllLocations(Pageable pageable) {
        return locationRepository.findAll(pageable).map(this::fromEntity);
    }


    public LocationResponseDTO getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationAbsenceException("Location с данным id не существует"));

        return fromEntity(location);
    }


    @Transactional
    public LocationResponseDTO createLocation(LocationRequestDTO locationRequestDTO) {
        Location location = toEntity(locationRequestDTO);
        Location savedLocation = locationRepository.save(location);

        return fromEntity(savedLocation);
    }


    @Transactional
    public LocationResponseDTO updateLocation(Long id, LocationRequestDTO locationRequestDTO) {
        Location location = locationRepository.findByIdAndCreatedBy(id, securityService.findUserName())
                .orElseThrow(() -> new LocationUpdateException("Сущности Location, принадлежащей Вам, с таким id не существует"));

        location.setX(locationRequestDTO.x());
        location.setY(locationRequestDTO.y());
        Location updatedLocation = locationRepository.save(location);

        return fromEntity(updatedLocation);
    }


    @Transactional
    public boolean deleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationInaccessibleDeleteException("Location с данным id не существует"));

        if (!location.getCreatedBy().equals(securityService.findUserName()))
            throw new LocationInaccessibleDeleteException("У Вас нет доступа к данному объекту Location");

        if (addressRepository.existsByTown(location))
            throw new CoordinatesInaccessibleDeleteException("Этот объект Location связан с другими. Удаление невозможно");

        locationRepository.delete(location);
        return true;
    }


    private LocationResponseDTO fromEntity(Location location) {
        return new LocationResponseDTO(
                location.getId(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getName()
        );
    }


    private Location toEntity(LocationRequestDTO locationRequestDTO) {
        Location location = new Location();
        location.setX(locationRequestDTO.x());
        location.setY(locationRequestDTO.y());
        location.setZ(locationRequestDTO.z());
        location.setName(locationRequestDTO.name());
        return location;
    }
}
