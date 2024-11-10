package com.lab1.dto;

import com.lab1.dao.enums.OrganizationType;

public record OrganizationRequestDTO(String name, Long coordinatesId,
//                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
//                                     ZonedDateTime creationDate,
                                     Long officialAddressId,
                                     int annualTurnover, int employeesCount, int rating,
                                     String fullName, OrganizationType type, Long postalAddressId) {
}
