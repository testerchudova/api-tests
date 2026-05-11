package tests;

import models.users.UpdateUserRequest;
import models.users.UpdateUserResponse;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.notNullValue;
import static specs.CommonSpec.unauthorizedResponseSpec;
import static specs.UpdateUserSpec.badRequestUpdateUserResponseSpec;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.BeforeEach;
import static io.qameta.allure.Allure.step;

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
        UpdateUserResponse currentUser = step("Получить текущего пользователя", () ->
                api.users.getCurrentUser(userToken)
        );

        String email = step("Подготовить новый email", () ->
                "ivan" + System.currentTimeMillis() + "@test.com"
        );

        UpdateUserRequest body = step("Подготовить тело PUT-запроса для обновления пользователя", () ->
                new UpdateUserRequest(
                        currentUser.username(),
                        "Ivan",
                        "Ivanov",
                        email
                )
        );

        UpdateUserResponse response = step("Выполнить PUT-запрос обновления пользователя", () ->
                api.users.putUpdateUser(body, userToken)
        );

        step("Проверить данные пользователя после PUT-обновления", () -> {
            assertThat(response.username()).isEqualTo(currentUser.username());
            assertThat(response.firstName()).isEqualTo("Ivan");
            assertThat(response.lastName()).isEqualTo("Ivanov");
            assertThat(response.email()).isEqualTo(email);
        });
    }

    @Test
    void successfulPatchUpdateOnlyFirstNameTest() {
        UpdateUserResponse before = step("Получить текущие данные пользователя", () ->
                api.users.getCurrentUser(userToken)
        );

        String newFirstName = step("Подготовить новое имя пользователя", () ->
                "NewName" + System.currentTimeMillis()
        );

        UpdateUserRequest body = step("Подготовить тело PATCH-запроса только с firstName", () ->
                new UpdateUserRequest(
                        null,
                        newFirstName,
                        null,
                        null
                )
        );

        UpdateUserResponse response = step("Выполнить PATCH-запрос обновления пользователя", () ->
                api.users.patchUpdateUser(body, userToken)
        );

        step("Проверить, что изменился только firstName", () -> {
            assertThat(response.firstName()).isEqualTo(newFirstName);
            assertThat(response.lastName()).isEqualTo(before.lastName());
            assertThat(response.email()).isEqualTo(before.email());
        });
    }

    @Test
    void unsuccessfulPutUpdateWithoutRequiredFieldsTest() {
        UpdateUserRequest body = step("Подготовить тело PUT-запроса без обязательных полей", () ->
                new UpdateUserRequest(
                        null,
                        "OnlyName",
                        null,
                        null
                )
        );

        step("Выполнить PUT-запрос и проверить ошибку по обязательным полям", () -> {
            api.users.updateUser(body, "PUT", userToken)
                    .then()
                    .spec(badRequestUpdateUserResponseSpec)
                    .body("username", notNullValue())
                    .body("lastName", notNullValue())
                    .body("email", notNullValue());
        });
    }

    @Test
    void unsuccessfulPutUpdateWithoutTokenTest() {
        UpdateUserRequest body = step("Подготовить тело PUT-запроса без авторизации", () ->
                new UpdateUserRequest(
                        LOGIN_USERNAME,
                        "Ivan",
                        "Ivanov",
                        "ivan" + System.currentTimeMillis() + "@test.com"
                )
        );

        step("Выполнить PUT-запрос без токена и проверить 401", () -> {
            api.users.updateUserWithoutToken(body, "PUT")
                    .then()
                    .spec(unauthorizedResponseSpec);
        });
    }

    @Test
    void unsuccessfulPatchUpdateWithoutTokenTest() {
        UpdateUserRequest body = step("Подготовить тело PATCH-запроса без авторизации", () ->
                new UpdateUserRequest(
                        null,
                        "NoAuthName",
                        null,
                        null
                )
        );

        step("Выполнить PATCH-запрос без токена и проверить 401", () -> {
            api.users.updateUserWithoutToken(body, "PATCH")
                    .then()
                    .spec(unauthorizedResponseSpec);
        });
    }}