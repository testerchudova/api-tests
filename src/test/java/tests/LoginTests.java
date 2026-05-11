package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static data.TestData.LOGIN_PASSWORD;
import static data.TestData.LOGIN_TOKEN_PREFIX;
import static data.TestData.LOGIN_USERNAME;
import static data.TestData.LOGIN_WRONG_CREDENTIALS_ERROR;
import static data.TestData.LOGIN_WRONG_PASSWORD;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static specs.LoginSpec.badRequestLoginResponseSpec;

public class LoginTests extends TestBase {

    @Test
    void successfulLoginTest() {
        LoginBodyModel loginData = step("Подготовить данные для успешной авторизации", () ->
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)
        );

        SuccessfulLoginResponseModel loginResponse = step("Выполнить авторизацию", () ->
                api.auth.login(loginData)
        );

        step("Проверить access и refresh токены", () -> {
            assertThat(loginResponse.access()).startsWith(LOGIN_TOKEN_PREFIX);
            assertThat(loginResponse.refresh()).startsWith(LOGIN_TOKEN_PREFIX);
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });
    }

    @Test
    void unsuccessfulLoginWithWrongPasswordTest() {
        LoginBodyModel loginData = step("Подготовить данные с неверным паролем", () ->
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD)
        );

        WrongCredentialsLoginResponseModel loginResponse = step("Выполнить авторизацию с неверным паролем", () ->
                api.auth.loginWrongCredentials(loginData)
        );

        step("Проверить текст ошибки", () ->
                assertThat(loginResponse.detail()).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR)
        );
    }

    @Test
    void unsuccessfulLoginWithEmptyUsernameTest() {
        LoginBodyModel loginData = step("Подготовить данные с пустым username", () ->
                new LoginBodyModel("", LOGIN_PASSWORD)
        );

        step("Выполнить авторизацию и проверить ошибку по username", () ->
                api.auth.loginRaw(loginData)
                        .then()
                        .spec(badRequestLoginResponseSpec)
                        .body("username", notNullValue())
        );
    }

    @Test
    void unsuccessfulLoginWithEmptyPasswordTest() {
        LoginBodyModel loginData = step("Подготовить данные с пустым password", () ->
                new LoginBodyModel(LOGIN_USERNAME, "")
        );

        step("Выполнить авторизацию и проверить ошибку по password", () ->
                api.auth.loginRaw(loginData)
                        .then()
                        .spec(badRequestLoginResponseSpec)
                        .body("password", notNullValue())
        );
    }
}