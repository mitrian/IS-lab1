package com.lab1.api;

import com.lab1.dto.OrganizationRequestDTO;
import com.lab1.dto.OrganizationResponseDTO;
import com.lab1.services.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }


    @GetMapping
    public Page<OrganizationResponseDTO> getAllOrganizations(Pageable pageable) {
        return organizationService.getAllOrganizations(pageable);
    }


    @GetMapping("/{id}")
    public OrganizationResponseDTO getOrganizationById(@PathVariable int id) {
        return organizationService.getOrganizationById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationResponseDTO createOrganization(@RequestBody OrganizationRequestDTO organizationRequestDTO) {
        return organizationService.createOrganization(organizationRequestDTO);
    }


    @PutMapping("/{id}")
    public OrganizationResponseDTO updateOrganization(@PathVariable int id, @RequestBody OrganizationRequestDTO organizationRequestDTO) {
        return organizationService.updateOrganization(id, organizationRequestDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable int id) {
        boolean successfulDelete = organizationService.deleteOrganization(id);
        if (successfulDelete) return ResponseEntity.ok().build();
        return null;
    }


    @GetMapping("/average-rating")
    public ResponseEntity<Double> getAverageRating() {
        return ResponseEntity.ok().body(organizationService.avgRatingOfOrganization());
    }


    @GetMapping("/count-by-greater-rating/{minRating}")
    public long countOrganizationsByMinRating(@PathVariable int minRating) {
        return organizationService.countAmountWithGreaterRating(minRating);
    }


    @GetMapping("/search-by-fullname-prefix/{prefix}")
    public List<OrganizationResponseDTO> getOrganizationsByFullNamePrefix(@PathVariable  String prefix) {
        return organizationService.getOrganizationsByFullNamePrefix(prefix);
    }
}
