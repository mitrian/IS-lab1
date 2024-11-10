package com.lab1.api;

import com.lab1.dto.LocationRequestDTO;
import com.lab1.dto.LocationResponseDTO;
import com.lab1.services.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping
    public Page<LocationResponseDTO> getAllLocations(Pageable pageable) {
        return locationService.getAllLocations(pageable);
    }


    @GetMapping("/{id}")
    public LocationResponseDTO getCoordinatesById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponseDTO createCoordinates(@RequestBody LocationRequestDTO locationRequestDTO) {
        return locationService.createLocation(locationRequestDTO);
    }


    @PutMapping("/{id}")
    public LocationResponseDTO updateCoordinates(@PathVariable Long id, @RequestBody LocationRequestDTO locationRequestDTO) {
        return locationService.updateLocation(id, locationRequestDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinates(@PathVariable Long id) {
        boolean successfulDelete = locationService.deleteLocation(id);
        if (successfulDelete) return ResponseEntity.ok().build();
        return null;
    }
}
