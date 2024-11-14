package com.lab1.services;

import com.lab1.dao.entities.Coordinates;
import com.lab1.dao.repositories.CoordinatesRepository;
import com.lab1.dao.repositories.OrganizationRepository;
import com.lab1.dto.CoordinatesRequestDTO;
import com.lab1.dto.CoordinatesResponseDTO;
import com.lab1.exceptions.entity.impl.CoordinatesAbsenceException;
import com.lab1.exceptions.entity.impl.CoordinatesInaccessibleDeleteException;
import com.lab1.exceptions.entity.impl.CoordinatesUpdateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    private final OrganizationRepository organizationRepository;
    private final SecurityService securityService;

    public CoordinatesService(CoordinatesRepository coordinatesRepository, OrganizationRepository organizationRepository, SecurityService securityService) {
        this.coordinatesRepository = coordinatesRepository;
        this.organizationRepository = organizationRepository;
        this.securityService = securityService;
    }


    public Page<CoordinatesResponseDTO> getAllCoordinates(Pageable pageable) {
        return coordinatesRepository.findAll(pageable).map(this::fromEntity);
    }


    public CoordinatesResponseDTO getCoordinatesById(Long id) {
        Coordinates coordinates = coordinatesRepository.findById(id)
                .orElseThrow(() -> new CoordinatesAbsenceException("Coordinates с данным id не существует"));

        return fromEntity(coordinates);
    }


    @Transactional
    public CoordinatesResponseDTO createCoordinates(CoordinatesRequestDTO coordinatesRequestDTO) {
        Coordinates coordinates = toEntity(coordinatesRequestDTO);
        Coordinates savedCoordinates = coordinatesRepository.save(coordinates);

        return fromEntity(savedCoordinates);
    }


    @Transactional
    public CoordinatesResponseDTO updateCoordinates(Long id, CoordinatesRequestDTO coordinatesRequestDTO) {
        Coordinates coordinates = coordinatesRepository.findByIdAndCreatedBy(id, securityService.findUserName())
                .orElseThrow(() -> new CoordinatesUpdateException("Сущности Coordinates, принадлежащей Вам, с таким id не существует"));

        coordinates.setX(coordinatesRequestDTO.x());
        coordinates.setY(coordinatesRequestDTO.y());
        Coordinates updatedCoordinates = coordinatesRepository.save(coordinates);

        return fromEntity(updatedCoordinates);
    }


    @Transactional
    public boolean deleteCoordinates(Long id) {
        Coordinates coordinates = coordinatesRepository.findById(id)
                .orElseThrow(() -> new CoordinatesInaccessibleDeleteException("Coordinates с данным id не существует"));

        if (!coordinates.getCreatedBy().equals(securityService.findUserName()))
            throw new CoordinatesInaccessibleDeleteException("У Вас нет доступа к данному объекту Coordinates");

        if (organizationRepository.existsByCoordinates(coordinates))
            throw new CoordinatesInaccessibleDeleteException("Этот объект Coordinates связан с другими. Удаление невозможно");

        coordinatesRepository.delete(coordinates);
        return true;
    }


    private CoordinatesResponseDTO fromEntity(Coordinates coordinates) {
        return new CoordinatesResponseDTO(
                coordinates.getId(),
                coordinates.getX(),
                coordinates.getY()
        );
    }


    private Coordinates toEntity(CoordinatesRequestDTO coordinatesRequestDTO) {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(coordinatesRequestDTO.x());
        coordinates.setY(coordinatesRequestDTO.y());
        return coordinates;
    }
}
