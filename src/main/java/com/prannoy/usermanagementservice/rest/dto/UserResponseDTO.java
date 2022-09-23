package com.prannoy.usermanagementservice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private UUID userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String postalCode;
    private String countryCode;
}
