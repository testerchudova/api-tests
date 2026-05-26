package tests;

import models.clubs.ClubModel;
import models.clubs.ClubRequestModel;
import models.clubs.ClubsListResponseModel;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static data.TestData.LOGIN_PASSWORD;
import static data.TestData.LOGIN_USERNAME;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.ClubsSpec.notFoundClubResponseSpec;

public class ClubsTests extends TestBase {

    private static String userToken;

    @BeforeAll
    static void authorize() {
        userToken = api.auth.loginAndGetAccessToken(
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)
        );
    }

    @Test
    void successfulGetClubsListTest() {
        ClubsListResponseModel response = step("Получить список клубов", () ->
                api.clubs.getClubs()
        );

        step("Проверить структуру списка клубов", () -> {
            assertThat(response).isNotNull();
            assertThat(response.count()).isGreaterThanOrEqualTo(0);
            assertThat(response.results()).isNotNull();
        });
    }

    @Test
    void successfulCreateClubTest() {
        ClubRequestModel request = step("Подготовить данные нового клуба", () ->
                createClubRequest("Create")
        );

        ClubModel createdClub = step("Создать клуб", () ->
                api.clubs.createClub(request, userToken)
        );

        try {
            step("Проверить данные созданного клуба", () -> {
                assertThat(createdClub.id()).isNotNull().isPositive();
                assertThat(createdClub.bookTitle()).isEqualTo(request.bookTitle());
                assertThat(createdClub.bookAuthors()).isEqualTo(request.bookAuthors());
                assertThat(createdClub.publicationYear()).isEqualTo(request.publicationYear());
                assertThat(createdClub.description()).isEqualTo(request.description());
                assertThat(createdClub.telegramChatLink()).isEqualTo(request.telegramChatLink());
                assertThat(createdClub.owner()).isNotNull().isPositive();
                assertThat(createdClub.members()).isNotNull();
            });
        } finally {
            step("Удалить тестовый клуб", () ->
                    api.clubs.deleteClub(createdClub.id(), userToken)
            );
        }
    }

    @Test
    void successfulGetClubByIdTest() {
        ClubRequestModel request = step("Подготовить данные нового клуба", () ->
                createClubRequest("Read")
        );

        ClubModel createdClub = step("Создать клуб", () ->
                api.clubs.createClub(request, userToken)
        );

        try {
            ClubModel receivedClub = step("Получить созданный клуб по id", () ->
                    api.clubs.getClubById(createdClub.id())
            );

            step("Проверить, что получен нужный клуб", () -> {
                assertThat(receivedClub.id()).isEqualTo(createdClub.id());
                assertThat(receivedClub.bookTitle()).isEqualTo(request.bookTitle());
                assertThat(receivedClub.bookAuthors()).isEqualTo(request.bookAuthors());
                assertThat(receivedClub.publicationYear()).isEqualTo(request.publicationYear());
            });
        } finally {
            step("Удалить тестовый клуб", () ->
                    api.clubs.deleteClub(createdClub.id(), userToken)
            );
        }
    }

    @Test
    void successfulUpdateClubTest() {
        ClubRequestModel createRequest = step("Подготовить данные нового клуба", () ->
                createClubRequest("Update")
        );

        ClubModel createdClub = step("Создать клуб", () ->
                api.clubs.createClub(createRequest, userToken)
        );

        try {
            ClubRequestModel updateRequest = step("Подготовить новые данные клуба", () ->
                    createClubRequest("Updated")
            );

            ClubModel updatedClub = step("Обновить клуб через PUT", () ->
                    api.clubs.updateClub(createdClub.id(), updateRequest, userToken)
            );

            step("Проверить обновленные данные клуба", () -> {
                assertThat(updatedClub.id()).isEqualTo(createdClub.id());
                assertThat(updatedClub.bookTitle()).isEqualTo(updateRequest.bookTitle());
                assertThat(updatedClub.bookAuthors()).isEqualTo(updateRequest.bookAuthors());
                assertThat(updatedClub.publicationYear()).isEqualTo(updateRequest.publicationYear());
                assertThat(updatedClub.description()).isEqualTo(updateRequest.description());
                assertThat(updatedClub.telegramChatLink()).isEqualTo(updateRequest.telegramChatLink());
            });
        } finally {
            step("Удалить тестовый клуб", () ->
                    api.clubs.deleteClub(createdClub.id(), userToken)
            );
        }
    }

    @Test
    void successfulDeleteClubTest() {
        ClubRequestModel request = step("Подготовить данные нового клуба", () ->
                createClubRequest("Delete")
        );

        ClubModel createdClub = step("Создать клуб", () ->
                api.clubs.createClub(request, userToken)
        );

        step("Удалить клуб", () ->
                api.clubs.deleteClub(createdClub.id(), userToken)
        );

        step("Проверить, что удаленный клуб больше не доступен", () ->
                api.clubs.getClubByIdRaw(createdClub.id())
                        .then()
                        .spec(notFoundClubResponseSpec)
        );
    }

    private ClubRequestModel createClubRequest(String marker) {
        String uniqueSuffix = marker + "-" + UUID.randomUUID();

        return new ClubRequestModel(
                "Book Club " + uniqueSuffix,
                "Author " + uniqueSuffix,
                2024,
                "Description for " + uniqueSuffix,
                "https://t.me/bookclub" + uniqueSuffix.replace("-", "")
        );
    }
}
