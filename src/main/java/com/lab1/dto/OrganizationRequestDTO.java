package com.lab1.dto;

import com.lab1.dao.enums.OrganizationType;

public record OrganizationRequestDTO(String name, Long coordinatesId,
                                     Long officialAddressId,
                                     int annualTurnover, int employeesCount, int rating,
                                     String fullName, OrganizationType type, Long postalAddressId) {
}
