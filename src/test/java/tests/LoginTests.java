package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static specs.LoginSpec.badRequestLoginResponseSpec;
import static specs.LoginSpec.wrongCredentialsLoginResponseSpec;
import static data.TestData.LOGIN_PASSWORD;
import static data.TestData.LOGIN_TOKEN_PREFIX;
import static data.TestData.LOGIN_USERNAME;
import static data.TestData.LOGIN_WRONG_CREDENTIALS_ERROR;
import static data.TestData.LOGIN_WRONG_PASSWORD;

public class LoginTests extends TestBase {

    @Test
    void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        assertThat(loginResponse.access()).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(loginResponse.refresh()).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
    }

    @Test
    void unsuccessfulLoginWithWrongPasswordTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        api.auth.loginRaw(loginData)
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .body("detail", org.hamcrest.Matchers.equalTo(LOGIN_WRONG_CREDENTIALS_ERROR));
    }

    @Test
    void unsuccessfulLoginWithEmptyUsernameTest() {
        LoginBodyModel loginData = new LoginBodyModel("", LOGIN_PASSWORD);

        api.auth.loginRaw(loginData)
                .then()
                .spec(badRequestLoginResponseSpec)
                .body("username", notNullValue());
    }

    @Test
    void unsuccessfulLoginWithEmptyPasswordTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, "");

        api.auth.loginRaw(loginData)
                .then()
                .spec(badRequestLoginResponseSpec)
                .body("password", notNullValue());
    }
}
