package com.lab1.api;

import com.lab1.dto.AddressRequestDTO;
import com.lab1.dto.AddressResponseDTO;
import com.lab1.services.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    @GetMapping
    public Page<AddressResponseDTO> getAllAddresses(Pageable pageable) {
        return addressService.getAllAddress(pageable);
    }

    @GetMapping("/{id}")
    public AddressResponseDTO getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponseDTO createAddress(@RequestBody AddressRequestDTO addressRequestDTO) {
        return addressService.createAddress(addressRequestDTO);
    }


    @PutMapping("/{id}")
    public AddressResponseDTO updateAddress(@PathVariable Long id, @RequestBody AddressRequestDTO addressRequestDTO) {
        return addressService.updateAddress(id, addressRequestDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        boolean successfulDelete = addressService.deleteAddress(id);
        if (successfulDelete){
            return ResponseEntity.ok().build();
        }
        return null;
    }


}
