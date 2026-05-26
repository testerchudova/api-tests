package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.clubs.ClubModel;
import models.clubs.ClubRequestModel;
import models.clubs.ClubsListResponseModel;

import static io.restassured.RestAssured.given;
import static specs.ClubsSpec.clubsRequestSpec;
import static specs.ClubsSpec.successfulClubResponseSpec;
import static specs.ClubsSpec.successfulClubsListResponseSpec;
import static specs.ClubsSpec.successfulCreateClubResponseSpec;
import static specs.ClubsSpec.successfulDeleteClubResponseSpec;

public class ClubsApiClient {

    private static final String CLUBS_PATH = "/clubs/";
    private static final String CLUB_BY_ID_PATH = "/clubs/{clubId}/";

    @Step("Получить список клубов")
    public ClubsListResponseModel getClubs() {
        return given(clubsRequestSpec)
                .when()
                .get(CLUBS_PATH)
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("Получить клуб по id: {clubId}")
    public ClubModel getClubById(Integer clubId) {
        return given(clubsRequestSpec)
                .when()
                .get(CLUB_BY_ID_PATH, clubId)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Получить клуб по id без success-spec: {clubId}")
    public Response getClubByIdRaw(Integer clubId) {
        return given(clubsRequestSpec)
                .when()
                .get(CLUB_BY_ID_PATH, clubId);
    }

    @Step("Создать клуб")
    public ClubModel createClub(ClubRequestModel body, String token) {
        return given(clubsRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .post(CLUBS_PATH)
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Обновить клуб через PUT: {clubId}")
    public ClubModel updateClub(Integer clubId, ClubRequestModel body, String token) {
        return given(clubsRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .put(CLUB_BY_ID_PATH, clubId)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Обновить клуб через PATCH: {clubId}")
    public ClubModel patchClub(Integer clubId, ClubRequestModel body, String token) {
        return given(clubsRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .patch(CLUB_BY_ID_PATH, clubId)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Удалить клуб: {clubId}")
    public void deleteClub(Integer clubId, String token) {
        given(clubsRequestSpec)
                .auth().oauth2(token)
                .when()
                .delete(CLUB_BY_ID_PATH, clubId)
                .then()
                .spec(successfulDeleteClubResponseSpec);
    }
}