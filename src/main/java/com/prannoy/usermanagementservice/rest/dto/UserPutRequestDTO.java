package com.prannoy.usermanagementservice.rest.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
public class UserPutRequestDTO extends UserRequestDTO {

    @NotNull
    private UUID userId;

    @Builder(builderMethodName = "userPutRequestDtoBuilder")
    public UserPutRequestDTO(@NotNull(message = "username cannot be empty") @Size(max = 50) String userName, @Size(max = 50) String firstName, @Size(max = 50) String lastName, String street, String city, String postalCode, @NotNull String countryCode, UUID userId) {
        super(userName, firstName, lastName, street, city, postalCode, countryCode);
        this.userId = userId;
    }
}
