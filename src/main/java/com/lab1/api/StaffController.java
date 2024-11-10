package com.lab1.api;

import com.lab1.dto.StaffRequestDTO;
import com.lab1.dto.StaffResponseDTO;
import com.lab1.services.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public Page<StaffResponseDTO> getAllStaff(Pageable pageable) {
        return staffService.findAllStaff(pageable);
    }


    @GetMapping("/{id}")
    public StaffResponseDTO getStaff(@PathVariable Long id) {
        return staffService.findStaffById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StaffResponseDTO createStaff(@RequestBody StaffRequestDTO staffRequestDTO) {
        return staffService.createStaff(staffRequestDTO);
    }


    @PutMapping("/{id}")
    public StaffResponseDTO updateStaff(@PathVariable Long id, @RequestBody StaffRequestDTO staffRequestDTO) {
        return staffService.updateStaff(id, staffRequestDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        boolean successfulDelete = staffService.deleteStaff(id);
        if (successfulDelete) return ResponseEntity.ok().build();
        return null;
    }

    @PutMapping("/dismiss/{id}")
    public ResponseEntity<Void> dismissStaff(@PathVariable int id) {
        staffService.dismissStaff(id);
        return ResponseEntity.ok().build();
    }

    //id - staff, которого назнач
    @PutMapping("/assign/{staffId}/{organizationId}")
    public StaffResponseDTO assignStaff(@PathVariable Long staffId, @PathVariable int organizationId) {
        System.out.println(staffId);
        System.out.println(organizationId);
        return staffService.assignStaffToOrganization(staffId, organizationId);
    }


}
