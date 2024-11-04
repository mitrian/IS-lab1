package com.lab1.api;

import com.lab1.dto.CoordinatesRequestDTO;
import com.lab1.dto.CoordinatesResponseDTO;
import com.lab1.services.CoordinatesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }


    @GetMapping
    public Page<CoordinatesResponseDTO> getAllCoordinates(Pageable pageable) {
        return coordinatesService.getAllCoordinates(pageable);
    }


    @GetMapping("/{id}")
    public CoordinatesResponseDTO getCoordinatesById(@PathVariable Long id) {
        return coordinatesService.getCoordinatesById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoordinatesResponseDTO createCoordinates(@RequestBody CoordinatesRequestDTO coordinatesRequestDTO) {
        return coordinatesService.createCoordinates(coordinatesRequestDTO);
    }


    @PutMapping("/{id}")
    public CoordinatesResponseDTO updateCoordinates(@PathVariable Long id, @RequestBody CoordinatesRequestDTO coordinatesRequestDTO) {
        return coordinatesService.updateCoordinates(id, coordinatesRequestDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoordinates(@PathVariable Long id) {
        boolean successfulDelete = coordinatesService.deleteCoordinates(id);
        if (successfulDelete) return ResponseEntity.ok().build();
        return null;
    }
}
