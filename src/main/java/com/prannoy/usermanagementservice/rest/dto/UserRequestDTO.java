package com.prannoy.usermanagementservice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale.IsoCountryCode;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "username cannot be empty")
    @Size(max = 50)
    private String userName;


    @Size(max = 50)
    private String firstName;


    @Size(max = 50)
    private String lastName;


    private String street;

    private String city;

    private String postalCode;

    @NotNull
    private String countryCode;
}
