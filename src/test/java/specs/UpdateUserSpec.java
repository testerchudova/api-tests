package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateUserSpec {

    public static final ResponseSpecification successfulUpdateUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody("id", notNullValue())
            .expectBody("username", notNullValue())
            .expectBody("firstName", notNullValue())
            .expectBody("lastName", notNullValue())
            .expectBody("email", notNullValue())
            .build();

    public static final ResponseSpecification badRequestUpdateUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .build();
}
