package com.prannoy.usermanagementservice.rest;

import com.prannoy.usermanagementservice.config.EnableBasicErrorHandlingConfig;
import com.prannoy.usermanagementservice.persistence.entity.UserEntity;
import com.prannoy.usermanagementservice.persistence.repository.UserRepository;
import com.prannoy.usermanagementservice.rest.dto.ErrorDTO;
import com.prannoy.usermanagementservice.rest.dto.UserIdDTO;
import com.prannoy.usermanagementservice.rest.dto.UserRequestDTO;
import com.prannoy.usermanagementservice.rest.dto.UserResponseDTO;
import com.prannoy.usermanagementservice.rest.exception.BadRequestException;
import com.prannoy.usermanagementservice.rest.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@EnableBasicErrorHandlingConfig
@RequestMapping(
        value = "/v1/users",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserManagementServiceController {

    private final UserRepository userRepository;

    @Autowired
    public UserManagementServiceController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ApiResponse(
            responseCode = "404",
            description = "user with user Id not found",
            content = {@Content(schema = @Schema(implementation = ErrorDTO.class))}
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = {@Content(schema = @Schema(implementation = UserResponseDTO.class))}
    )
    @GetMapping("{userId}")
    public UserResponseDTO getUser(@PathVariable("userId") UUID userId) {
        final var byId = this.userRepository.findById(userId);
        final var modelMapper = new ModelMapper();
        return modelMapper.map(byId.orElseThrow(() -> new ResourceNotFoundException("The user with id is not found")), UserResponseDTO.class);
    }

    @ApiResponse(
            responseCode = "404",
            description = "users with username not found",
            content = {@Content(schema = @Schema(implementation = ErrorDTO.class))}
    )
    @ApiResponse(
            responseCode = "200",
            description = "Users found",
            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))}
    )
    @GetMapping("listUsersbyUsername/{userName}")
    public List<UserResponseDTO> getUsersByUserName(@PathVariable("userName") String userName) {
        final var userListEntity = this.userRepository.findUserEntityByUserName(userName);
        if (userListEntity.isEmpty()) {
            throw new ResourceNotFoundException(String.format("%s username is not found", userName));
        }
        final var modelMapper = new ModelMapper();
        return userListEntity.stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @ApiResponse(
            responseCode = "400",
            description = "Validation Error or incompatible input",
            content = {@Content(schema = @Schema(implementation = ErrorDTO.class))}
    )
    @ApiResponse(
            responseCode = "200",
            description = "User saved in database",
            content = {@Content(schema = @Schema(implementation = UserIdDTO.class))}
    )
    @PostMapping("/save")
    public UserIdDTO saveUser(@RequestBody @Valid final UserRequestDTO user) {
        getCountryCodeOrThrowException(user);
        final var modelMapper = new ModelMapper();
        final var userEntity = modelMapper.map(user, UserEntity.class);
        userRepository.save(userEntity);
        return UserIdDTO.builder().userId(userEntity.getUserId()).build();
    }

    private String getCountryCodeOrThrowException(UserRequestDTO user) {
        // Must be set to uppercase because The Country codes are stored in uppercase in the set
        final var countryCodeInput = user.getCountryCode().toUpperCase();

        return Arrays.stream(Locale.getISOCountries())
                .filter(code -> code.equals(countryCodeInput))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(String.format("%s as country code not valid", countryCodeInput)));
    }

}
