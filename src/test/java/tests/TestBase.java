package tests;

import api.ApiClient;
import io.restassured.RestAssured;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    protected static final ApiClient api = new ApiClient();

    protected String userToken;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";

        userToken = api.auth.loginAndGetAccessToken(
                new LoginBodyModel("user8", "user8")
        );
    }
}