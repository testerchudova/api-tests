package tests;

import models.login.LoginBodyModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;
import static specs.LogoutSpec.badRequestLogoutResponseSpec;
import static specs.LogoutSpec.unauthorizedLogoutResponseSpec;
import static data.TestData.LOGIN_PASSWORD;
import static data.TestData.LOGIN_USERNAME;

public class LogoutTests extends TestBase {

    @Test
    void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);

        api.auth.logout(logoutData);
    }

    @Test
    void unsuccessfulLogoutWithInvalidRefreshTokenTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel("invalid-refresh-token");

        api.auth.logoutRaw(logoutData)
                .then()
                .spec(unauthorizedLogoutResponseSpec)
                .body("detail", notNullValue());
    }

    @Test
    void unsuccessfulLogoutWithEmptyRefreshTokenTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel("");

        api.auth.logoutRaw(logoutData)
                .then()
                .spec(badRequestLogoutResponseSpec)
                .body("refresh", notNullValue());
    }

    @Test
    void unsuccessfulLogoutWithoutRefreshTokenTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(null);

        api.auth.logoutRaw(logoutData)
                .then()
                .spec(badRequestLogoutResponseSpec)
                .body("refresh", notNullValue());
    }
}