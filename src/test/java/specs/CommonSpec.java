package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static org.hamcrest.Matchers.notNullValue;

public class CommonSpec {

    public static final ResponseSpecification badRequestResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .build();

    public static final ResponseSpecification unauthorizedResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody("detail", notNullValue())
            .build();
}