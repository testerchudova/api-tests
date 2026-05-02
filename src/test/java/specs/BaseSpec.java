package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class BaseSpec {

    public static final RequestSpecification baseRequestSpec = new RequestSpecBuilder()
            .setBasePath("/api/v1")
            .setContentType(JSON)
            .log(ALL)
            .build();
}