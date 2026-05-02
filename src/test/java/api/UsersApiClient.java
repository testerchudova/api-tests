package api;

import io.restassured.response.Response;
import models.users.UpdateUserRequest;
import models.users.UpdateUserResponse;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.UpdateUserSpec.successfulUpdateUserResponseSpec;

public class UsersApiClient {

    private static final String CURRENT_USER_PATH = "/users/me/";

    public Response updateUser(UpdateUserRequest body, String method, String token) {
        return given(baseRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .request(method, CURRENT_USER_PATH);
    }

    public Response updateUserWithoutToken(UpdateUserRequest body, String method) {
        return given(baseRequestSpec)
                .body(body)
                .when()
                .request(method, CURRENT_USER_PATH);
    }

    public UpdateUserResponse putUpdateUser(UpdateUserRequest body, String token) {
        return updateUser(body, "PUT", token)
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract().as(UpdateUserResponse.class);
    }

    public UpdateUserResponse patchUpdateUser(UpdateUserRequest body, String token) {
        return updateUser(body, "PATCH", token)
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract().as(UpdateUserResponse.class);
    }

    public UpdateUserResponse getCurrentUser(String token) {
        return given(baseRequestSpec)
                .auth().oauth2(token)
                .when()
                .get(CURRENT_USER_PATH)
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract().as(UpdateUserResponse.class);
    }
}