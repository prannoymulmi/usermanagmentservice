package com.prannoy.usermanagementservice.rest;

import com.prannoy.usermanagementservice.persistence.entity.UserEntity;
import com.prannoy.usermanagementservice.persistence.repository.UserRepository;
import com.prannoy.usermanagementservice.rest.dto.UserRequestDTO;
import com.prannoy.usermanagementservice.rest.exception.BadRequestException;
import com.prannoy.usermanagementservice.rest.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceControllerTest {

    private static final String ANY_USER_NAME = "any_user_name";
    private static final String ANY_CITY = "anyCity";
    private static final String VALID_COUNTRY_CODE = "de";
    private static final String INVALID_COUNTRY_CODE = "INVALID_COUNTRY_CODE";
    private static final String ANY_POSTAL_CODE = "any_postal_code";
    private static final String ANY_FIRST_NAME = "any_first_name";
    private static final String ANY_LAST_NAME = "any_last_name";
    private static final String ANY_STREET = "any_street";
    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setup() {

    }

    @Test
    void getUserOnCorrectUserIdAUserIsReturned() {
        //Given
        final var userEntity = UserEntity
                .builder()
                .userId(UUID.randomUUID())
                .userName(ANY_USER_NAME)
                .countryCode(VALID_COUNTRY_CODE)
                .city(ANY_CITY)
                .postalCode(ANY_POSTAL_CODE)
                .firstName(ANY_FIRST_NAME)
                .lastName(ANY_LAST_NAME)
                .street(ANY_STREET)
                .build();
        Mockito.when(this.mockUserRepository.findById(any())).thenReturn(Optional.of(userEntity));
        final var userManagementServiceController = new UserManagementServiceController(mockUserRepository);

        // when
        final var result = userManagementServiceController.getUser(UUID.randomUUID());
        // then
        assertNotNull(result);
        assertEquals(userEntity.getUserId(), result.getId());
        assertEquals(userEntity.getUserName(), result.getUserName());
        assertEquals(userEntity.getCountryCode(), result.getCountryCode());
        assertEquals(userEntity.getCity(), result.getCity());
        assertEquals(userEntity.getPostalCode(), result.getPostalCode());
        assertEquals(userEntity.getFirstName(), result.getFirstName());
        assertEquals(userEntity.getLastName(), result.getLastName());
        assertEquals(userEntity.getStreet(), result.getStreet());
    }

    @Test
    void getUserOnUnknownUserIdAResourceNotFoundExceptionIsReturned() {
        //Given
        final var userManagementServiceController = new UserManagementServiceController(mockUserRepository);

        // when
        assertThrows(ResourceNotFoundException.class, () -> userManagementServiceController.getUser(UUID.randomUUID()));
    }

    @Test
    void getUsersByUserNameOnUserNameFoundListOfUserAreReturned() {
        //Given
        List<UserEntity> users = new ArrayList<>();
        users.add(UserEntity
                .builder()
                .userId(UUID.randomUUID())
                .userName(ANY_USER_NAME)
                .build());
        users.add(UserEntity
                .builder()
                .userId(UUID.randomUUID())
                .userName(ANY_USER_NAME)
                .build());
        Mockito.when(this.mockUserRepository.findUserEntityByUserName(ANY_USER_NAME)).thenReturn(users);
        final var userManagementServiceController = new UserManagementServiceController(mockUserRepository);

        // when
        final var result = userManagementServiceController.getUsersByUserName(ANY_USER_NAME);
        // then
        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }


    @Test
    void getUsersByUserNameOnUserNameNotFoundResourceNotFoundExceptionIsReturned() {
        //Given
        List<UserEntity> users = new ArrayList<>();
        Mockito.when(this.mockUserRepository.findUserEntityByUserName(ANY_USER_NAME)).thenReturn(users);
        final var userManagementServiceController = new UserManagementServiceController(mockUserRepository);

        // when
        assertThrows(ResourceNotFoundException.class, () -> userManagementServiceController.getUsersByUserName(ANY_USER_NAME));
    }

    @Test
    void saveUserHasAllInputsCorrectNoErrorsOccur() {
        //given
        final var userManagementServiceController = new UserManagementServiceController(mockUserRepository);
        final var user = UserRequestDTO
                .builder()
                .city(ANY_CITY)
                .countryCode(VALID_COUNTRY_CODE)
                .userName(ANY_USER_NAME)
                .build();

        // when
        final var result = assertDoesNotThrow(() -> userManagementServiceController.saveUser(user));

        // save is carried out
        verify(this.mockUserRepository, times(1)).save(any(UserEntity.class));
        assertNotNull(result);
    }


    @Test
    void saveUserUnknownCountyCodeBadRequestExceptionIsThrown() {
        //given
        final var userManagementServiceController = new UserManagementServiceController(mockUserRepository);
        final var user = UserRequestDTO
                .builder()
                .city(ANY_CITY)
                .countryCode(INVALID_COUNTRY_CODE)
                .userName(ANY_USER_NAME)
                .build();

        // when
        assertThrows(BadRequestException.class, () -> userManagementServiceController.saveUser(user));
    }
}
