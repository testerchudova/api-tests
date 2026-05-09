package tests;

import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static specs.RegistrationSpec.badRequestRegistrationResponseSpec;
import static data.TestData.REGISTRATION_EXISTING_USER_ERROR;
import static data.TestData.REGISTRATION_IP_REGEXP;

public class RegistrationTests extends TestBase {

    private String username;
    private String password;

    @BeforeEach
    void prepareTestData() {
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(registrationData);

        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.username()).isEqualTo(username);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");
        assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }

    @Test
    void unsuccessfulRegistrationExistingUserTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse = api.users.register(registrationData);
        assertThat(firstRegistrationResponse.username()).isEqualTo(username);

        ExistingUserResponseModel secondRegistrationResponse = api.users.registerExistingUser(registrationData);

        assertThat(secondRegistrationResponse.username().get(0))
                .isEqualTo(REGISTRATION_EXISTING_USER_ERROR);
    }

    @Test
    void unsuccessfulRegistrationWithEmptyUsernameTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", password);

        api.users.registerRaw(registrationData)
                .then()
                .spec(badRequestRegistrationResponseSpec)
                .body("username", notNullValue());
    }

    @Test
    void unsuccessfulRegistrationWithEmptyPasswordTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, "");

        api.users.registerRaw(registrationData)
                .then()
                .spec(badRequestRegistrationResponseSpec)
                .body("password", notNullValue());
    }
}
