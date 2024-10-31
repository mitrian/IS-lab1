package com.lab1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lab1.dao.enums.OrganizationType;

import java.time.ZonedDateTime;

public record OrganizationResponseDTO(int id, String name, CoordinatesResponseDTO coordinates,
                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
                                      ZonedDateTime creationDate,
                                      AddressResponseDTO officialAddress,
                                      int annualTurnover, int employeesCount, int rating,
                                      String fullName, OrganizationType type, AddressResponseDTO postalAddress) {
}
