package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static org.hamcrest.Matchers.notNullValue;

public class LoginSpec {

    public static final ResponseSpecification successfulLoginResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody("access", notNullValue())
            .expectBody("refresh", notNullValue())
            .build();
}