package api;

import io.restassured.response.Response;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.logout.LogoutBodyModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.LoginSpec.successfulLoginResponseSpec;
import static specs.LoginSpec.wrongCredentialsLoginResponseSpec;
import static specs.LogoutSpec.successfulLogoutResponseSpec;

public class AuthApiClient {

    public SuccessfulLoginResponseModel login(LoginBodyModel body) {
        return given(baseRequestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    public String loginAndGetAccessToken(LoginBodyModel body) {
        return login(body).access();
    }

    public String loginAndGetRefreshToken(LoginBodyModel body) {
        return login(body).refresh();
    }

    public WrongCredentialsLoginResponseModel loginWrongCredentials(LoginBodyModel body) {
        return given(baseRequestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);
    }

    public Response loginRaw(LoginBodyModel body) {
        return given(baseRequestSpec)
                .body(body)
                .when()
                .post("/auth/token/");
    }

    public void logout(LogoutBodyModel body) {
        given(baseRequestSpec)
                .body(body)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    public Response logoutRaw(LogoutBodyModel body) {
        return given(baseRequestSpec)
                .body(body)
                .when()
                .post("/auth/logout/");
    }
}