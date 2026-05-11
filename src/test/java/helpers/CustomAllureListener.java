package helpers;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomAllureListener {

    public static AllureRestAssured withCustomTemplates() {
        return new AllureRestAssured()
                .setRequestTemplate("custom-http-request.ftl")
                .setResponseTemplate("custom-http-response.ftl")
                .setRequestAttachmentName("Request")
                .setResponseAttachmentName("Response");
    }
}