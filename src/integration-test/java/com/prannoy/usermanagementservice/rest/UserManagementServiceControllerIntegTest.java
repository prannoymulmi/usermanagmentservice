package com.prannoy.usermanagementservice.rest;

import com.prannoy.usermanagementservice.persistence.entity.UserEntity;
import com.prannoy.usermanagementservice.persistence.repository.UserRepository;
import com.prannoy.usermanagementservice.rest.dto.ErrorDTO;
import com.prannoy.usermanagementservice.rest.dto.UserIdDTO;
import com.prannoy.usermanagementservice.rest.dto.UserPutRequestDTO;
import com.prannoy.usermanagementservice.rest.dto.UserRequestDTO;
import com.prannoy.usermanagementservice.rest.dto.UserResponseDTO;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles(profiles = "test")
public class UserManagementServiceControllerIntegTest {

    private static final String ANY_USER_NAME = "any_user_name";
    private static final String ANY_POSTAL_CODE = "22014";
    private static final String COUNTRY_CODE = "de";
    private static final String ANY_STREET = "any_street";
    private static final String GP_COUNTRY_CODE = "gp";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Value("${local.server.port}")
    private int port;

    private UserEntity user;

    @BeforeEach
    void setup() {
        this.user = userRepository.save(UserEntity
                .builder()
                .userName(ANY_USER_NAME)
                .postalCode(ANY_POSTAL_CODE)
                .countryCode(COUNTRY_CODE)
                .street(ANY_STREET)
                .build());

        // user 2 with different ID
        userRepository.save(UserEntity
                .builder()
                .userName(ANY_USER_NAME)
                .postalCode(ANY_POSTAL_CODE)
                .countryCode(GP_COUNTRY_CODE)
                .street(ANY_STREET)
                .build());

    }

    @Test
    void saveAnyUserWithCorrectInputReturns200AndUserId() {
        var user = UserRequestDTO
                .builder()
                .userName(ANY_USER_NAME)
                .postalCode(ANY_POSTAL_CODE)
                .countryCode(COUNTRY_CODE)
                .street(ANY_STREET)
                .build();

        RequestEntity<UserRequestDTO> request = createPostRequest(user, "/v1/users/save");
        final var result = this.restTemplate.exchange(request, UserIdDTO.class);

        assertNotNull(result.getBody().getUserId());
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void saveAnyUserWithUnknownCountryCodeInputReturnsBadRequest() {
        // given
        var user = UserRequestDTO
                .builder()
                .userName(ANY_USER_NAME)
                .postalCode(ANY_POSTAL_CODE)
                .countryCode("COUNTRY_CODE_Unknown")
                .street(ANY_STREET)
                .build();

        final var expectedMsg = "COUNTRY_CODE_UNKNOWN as country code not valid";

        // when
        RequestEntity<UserRequestDTO> request = createPostRequest(user, "/v1/users/save");
        final var result = this.restTemplate.exchange(request, ErrorDTO.class);

        // then
        assertTrue(result.getStatusCode().is4xxClientError());
        assertEquals(expectedMsg, result.getBody().getErrorTxt());
    }

    @Test
    void saveAnyUserWithNoUsernameInputReturnsBadRequest() {
        //Given
        var user = UserRequestDTO
                .builder()
                .postalCode(ANY_POSTAL_CODE)
                .countryCode(COUNTRY_CODE)
                .street(ANY_STREET)
                .build();
        final var expectedMsg = "username cannot be empty";

        // When
        RequestEntity<UserRequestDTO> request = createPostRequest(user, "/v1/users/save");
        final var result = this.restTemplate.exchange(request, ErrorDTO.class);

        //Then
        assertTrue(result.getStatusCode().is4xxClientError());
        assertEquals(expectedMsg, result.getBody().getErrorTxt());
    }

    private RequestEntity<UserRequestDTO> createPostRequest(UserRequestDTO req, String url) {
        return RequestEntity
                .post(URI.create(String.format("http://localhost:%s%s", this.port, url)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(req, UserRequestDTO.class);
    }


    @Test
    void getUserWhenExistingUserIdIsSuppliedTheUserIsReturned() throws Exception {

        // given
        final var url = String.format("/v1/users/%s", this.user.getUserId());
        // When
        final var request = createGetRequest(url);
        final var result = this.restTemplate.exchange(request, UserResponseDTO.class);

        //Then
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(this.user.getUserId(), result.getBody().getUserId());
    }

    @Test
    void getUserWhenNoExistingUserIdIsSuppliedResourceNotFoundIsReturned() throws Exception {

        // given
        final var url = String.format("/v1/users/%s", UUID.randomUUID());
        var expectedMsg= "The user with id is not found";
        // When
        final var request = createGetRequest(url);
        final var result = this.restTemplate.exchange(request, ErrorDTO.class);

        //Then
        assertTrue(result.getStatusCode().is4xxClientError());
        assertEquals(expectedMsg, result.getBody().getErrorTxt());
    }

    @Test
    void getUsersWithUserNameWhenExistingUserNameIsSuppliedListOfUsersAreReturned() throws Exception {

        // given
        final var url = String.format("/v1/users/listUsersbyUsername/%s", ANY_USER_NAME);
        final var expectedListLength = 2;
        // When
        final var request = createGetRequest(url);
        final var result = this.restTemplate.exchange(request, UserResponseDTO[].class);

        //Then
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(expectedListLength, result.getBody().length, "length of the user list should be 2");
    }


    @Test
    void getUsersWithUserNameWhenNotExistingUserNameIsSuppliedResourceNotFoundIsReturned() throws Exception {

        // given
        final var url = String.format("/v1/users/listUsersbyUsername/%s", "random");
        final var expectedMsg = "random username is not found";
        // When
        final var request = createGetRequest(url);
        final var result = this.restTemplate.exchange(request, ErrorDTO.class);

        //Then
        assertTrue(result.getStatusCode().is4xxClientError());
        assertEquals(expectedMsg, result.getBody().getErrorTxt());
    }

    @Test
    void deleteUserWhenExistingUserIdIsSupplied200IsReturned() throws Exception {

        // given
        final var toBeDeletedUser = userRepository.save(UserEntity
                .builder()
                .userName("test_name")
                .postalCode(ANY_POSTAL_CODE)
                .countryCode(GP_COUNTRY_CODE)
                .street(ANY_STREET)
                .build());

        final var url = String.format("/v1/users/%s", toBeDeletedUser.getUserId());
        // When
        final var request = createDeleteRequest(url);
        final var result = this.restTemplate.exchange(request, String.class);

        //Then
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void deleteUserWhenNonExistingUserIdIsSuppliedResourceNotFoundIsReturned() throws Exception {

        // given
        final var url = String.format("/v1/users/%s", UUID.randomUUID());
        final var expectedMsg = "User cannot be found";
        // When
        final var request = createDeleteRequest(url);
        final var result = this.restTemplate.exchange(request, ErrorDTO.class);

        //Then
        assertTrue(result.getStatusCode().is4xxClientError());
        assertEquals(expectedMsg, result.getBody().getErrorTxt());
    }

    @Test
    void patchUserWithExistingUserInputReturns200AndUserId() {
        //given
        final var changedUserName = "changedUserName";
        var user = UserPutRequestDTO
                .userPutRequestDtoBuilder()
                .userId(this.user.getUserId())
                .userName(changedUserName)
                .postalCode(ANY_POSTAL_CODE)
                .countryCode(COUNTRY_CODE)
                .street(ANY_STREET)
                .build();

        //when
        RequestEntity<UserRequestDTO> request = createPutRequest(user, "/v1/users/update");
        final var result = this.restTemplate.exchange(request, Void.class);

        //then
        final var changedUser = this.userRepository.findById(this.user.getUserId()).get();
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(changedUserName, changedUser.getUserName());
    }

    @Test
    void patchUserWithExistingUserAndInvalidCountryCodeInputReturns200AndUserId() {
        //given
        final var changedUserName = "changedUserName";
        var user = UserPutRequestDTO
                .userPutRequestDtoBuilder()
                .userId(this.user.getUserId())
                .userName(changedUserName)
                .postalCode(ANY_POSTAL_CODE)
                .countryCode("INVALID")
                .street(ANY_STREET)
                .build();

        var expectedMsg= "INVALID as country code not valid";
        //when
        RequestEntity<UserRequestDTO> request = createPutRequest(user, "/v1/users/update");
        final var result = this.restTemplate.exchange(request, ErrorDTO.class);

        //then
        assertTrue(result.getStatusCode().is4xxClientError());
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode().value());
        assertEquals(expectedMsg, result.getBody().getErrorTxt());
    }


    @Test
    void patchUserWithNonExsistingUserReturnsResourceNotFoundAndUserId() {
        //given
        final var changedUserName = "changedUserName";
        var user = UserPutRequestDTO
                .userPutRequestDtoBuilder()
                .userId(UUID.randomUUID())
                .userName(changedUserName)
                .build();

        //when
        RequestEntity<UserRequestDTO> request = createPutRequest(user, "/v1/users/update");
        final var result = this.restTemplate.exchange(request, ErrorDTO.class);

        //then
        assertTrue(result.getStatusCode().is4xxClientError());
    }



    private RequestEntity<UserRequestDTO> createPutRequest(UserRequestDTO req, String url) {
        return RequestEntity
                .put(URI.create(String.format("http://localhost:%s%s", this.port, url)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(req, UserRequestDTO.class);
    }

    private RequestEntity<Void> createGetRequest(String url) throws URISyntaxException {
        final var uri = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(this.port)
                .setPath(url)
                .build();

        return RequestEntity.get(uri).build();
    }

    private RequestEntity<Void> createDeleteRequest(String url) throws URISyntaxException {
        final var uri = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(this.port)
                .setPath(url)
                .build();

        return RequestEntity.delete(uri).build();
    }
}
