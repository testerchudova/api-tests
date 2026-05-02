package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.filter.log.LogDetail.ALL;

public class RegistrationSpec {

    // Успешная регистрация (201 Created или 200 OK — зависит от твоего API)
    public static ResponseSpecification registrationSuccessSpec = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(ALL)
            .build();

    // Ошибка: такой пользователь уже есть (400 или 409)
    public static ResponseSpecification registrationErrorSpec = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .log(ALL)
            .build();
}
