package api;

import io.restassured.response.Response;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.LoginSpec.successfulLoginResponseSpec;

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

    public Response loginRaw(LoginBodyModel body) {
        return given(baseRequestSpec)
                .body(body)
                .when()
                .post("/auth/token/");
    }
}