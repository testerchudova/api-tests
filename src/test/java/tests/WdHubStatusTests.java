package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class WdHubStatusTests extends tests.TestBase {


    @Test
    public void checkSelenoidVersionAndReadyStatus() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("value.message", containsString("Selenoid 1.11.3 built at"))
                .body("value.ready", is(true));
    }


    @Test
    public void checkStatusJsonSchema() {
        given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("status_response_schema.json"));
    }


    @Test
    public void loginWithWrongPassword() {
        given()
                .log().uri()
                .auth().basic("user1", "wrong_password") // Специально пишем неверный пароль
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    public void checkChromeIsAvailable() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("browsers.chrome", notNullValue());
    }

    @Test
    public void checkContentType() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .contentType(io.restassured.http.ContentType.JSON);
    }

    @Test
    public void checkUnauthorizedAccess() {
        given()
                .log().uri()
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .statusCode(401);
    }

    @Test
    public void checkResponseTime() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .time(lessThan(2000L));
    }
}