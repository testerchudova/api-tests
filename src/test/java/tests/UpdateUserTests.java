package tests;

import models.users.UpdateUserRequest;
import models.users.UpdateUserResponse;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.notNullValue;
import static specs.CommonSpec.unauthorizedResponseSpec;
import static specs.UpdateUserSpec.badRequestUpdateUserResponseSpec;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.BeforeEach;

import static data.TestData.LOGIN_PASSWORD;
import static data.TestData.LOGIN_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserTests extends TestBase {

    private String userToken;

    @BeforeEach
    void authorize() {
        userToken = api.auth.loginAndGetAccessToken(
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)
        );
    }
    @Test
    void successfulPutUpdateTest() {
        UpdateUserResponse currentUser = api.users.getCurrentUser(userToken);

        String email = "ivan" + System.currentTimeMillis() + "@test.com";

        UpdateUserRequest body = new UpdateUserRequest(
                currentUser.username(),
                "Ivan",
                "Ivanov",
                email
        );

        UpdateUserResponse response = api.users.putUpdateUser(body, userToken);

        assertThat(response.username()).isEqualTo(currentUser.username());
        assertThat(response.firstName()).isEqualTo("Ivan");
        assertThat(response.lastName()).isEqualTo("Ivanov");
        assertThat(response.email()).isEqualTo(email);
    }

    @Test
    void successfulPatchUpdateOnlyFirstNameTest() {
        UpdateUserResponse before = api.users.getCurrentUser(userToken);

        String newFirstName = "NewName" + System.currentTimeMillis();

        UpdateUserRequest body = new UpdateUserRequest(
                null,
                newFirstName,
                null,
                null
        );

        UpdateUserResponse response = api.users.patchUpdateUser(body, userToken);

        assertThat(response.firstName()).isEqualTo(newFirstName);
        assertThat(response.lastName()).isEqualTo(before.lastName());
        assertThat(response.email()).isEqualTo(before.email());
    }

    @Test
    void unsuccessfulPutUpdateWithoutRequiredFieldsTest() {
        UpdateUserRequest body = new UpdateUserRequest(
                null,
                "OnlyName",
                null,
                null
        );

        api.users.updateUser(body, "PUT", userToken)
                .then()
                .spec(badRequestUpdateUserResponseSpec)
                .body("username", notNullValue())
                .body("lastName", notNullValue())
                .body("email", notNullValue());
    }

    @Test
    void unsuccessfulPutUpdateWithoutTokenTest() {
        UpdateUserRequest body = new UpdateUserRequest(
                "user8",
                "Ivan",
                "Ivanov",
                "ivan" + System.currentTimeMillis() + "@test.com"
        );

        api.users.updateUserWithoutToken(body, "PUT")
                .then()
                .spec(unauthorizedResponseSpec);
    }

    @Test
    void unsuccessfulPatchUpdateWithoutTokenTest() {
        UpdateUserRequest body = new UpdateUserRequest(
                null,
                "NoAuthName",
                null,
                null
        );

        api.users.updateUserWithoutToken(body, "PATCH")
                .then()
                .spec(unauthorizedResponseSpec);
    }
}