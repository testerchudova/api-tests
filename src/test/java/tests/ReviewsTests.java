package tests;

import models.clubs.ClubModel;
import models.clubs.ClubRequestModel;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.reviews.ReviewModel;
import models.reviews.ReviewRequestModel;
import models.reviews.ReviewsListResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static data.TestData.LOGIN_PASSWORD;
import static data.TestData.LOGIN_USERNAME;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.ReviewsSpec.badRequestReviewResponseSpec;
import static specs.ReviewsSpec.forbiddenReviewResponseSpec;
import static specs.ReviewsSpec.notFoundReviewResponseSpec;
import static specs.ReviewsSpec.unauthorizedReviewResponseSpec;

@Tag("reviews")
public class ReviewsTests extends TestBase {

    private static String userToken;
    private static String anotherUserToken;

    @BeforeAll
    static void authorize() {
        userToken = api.auth.loginAndGetAccessToken(
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)
        );

        String anotherUsername = "review_user_" + UUID.randomUUID().toString().substring(0, 8);
        String anotherPassword = "pass_" + UUID.randomUUID().toString().substring(0, 8);

        api.users.register(new RegistrationBodyModel(anotherUsername, anotherPassword));

        anotherUserToken = api.auth.loginAndGetAccessToken(
                new LoginBodyModel(anotherUsername, anotherPassword)
        );
    }

    @Test
    void successfulGetReviewsListTest() {
        ReviewsListResponseModel response = step("Получить список reviews", () ->
                api.reviews.getReviews()
        );

        step("Проверить структуру списка reviews", () -> {
            assertThat(response).isNotNull();
            assertThat(response.count()).isGreaterThanOrEqualTo(0);
            assertThat(response.results()).isNotNull();
        });
    }

    @Test
    void successfulCreateReviewTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("CreateReview"), userToken)
        );

        ReviewModel createdReview = null;

        try {
            ReviewRequestModel reviewRequest = step("Подготовить тело запроса для создания review", () ->
                    createReviewRequest(createdClub.id(), "Created review")
            );

            createdReview = step("Создать review", () ->
                    api.reviews.createReview(reviewRequest, userToken)
            );

            ReviewModel finalCreatedReview = createdReview;

            step("Проверить данные созданного review", () -> {
                assertThat(finalCreatedReview.id()).isNotNull().isPositive();
                assertThat(finalCreatedReview.club()).isEqualTo(createdClub.id());
                assertThat(finalCreatedReview.user()).isNotNull();
                assertThat(finalCreatedReview.user().username()).isEqualTo(LOGIN_USERNAME);
                assertThat(finalCreatedReview.review()).isEqualTo(reviewRequest.review());
                assertThat(finalCreatedReview.assessment()).isEqualTo(reviewRequest.assessment());
                assertThat(finalCreatedReview.readPages()).isEqualTo(reviewRequest.readPages());
                assertThat(finalCreatedReview.created()).isNotNull();
            });
        } finally {
            deleteReviewIfExists(createdReview);
            deleteClubIfExists(createdClub);
        }
    }

    @Test
    void successfulGetReviewByIdTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("GetReview"), userToken)
        );

        ReviewModel createdReview = null;

        try {
            ReviewRequestModel reviewRequest = step("Подготовить тело запроса для создания review", () ->
                    createReviewRequest(createdClub.id(), "Review for get by id")
            );

            createdReview = step("Создать review", () ->
                    api.reviews.createReview(reviewRequest, userToken)
            );

            ReviewModel finalCreatedReview = createdReview;

            ReviewModel receivedReview = step("Получить review по id", () ->
                    api.reviews.getReviewById(finalCreatedReview.id())
            );

            step("Проверить, что получен нужный review", () -> {
                assertThat(receivedReview.id()).isEqualTo(finalCreatedReview.id());
                assertThat(receivedReview.club()).isEqualTo(createdClub.id());
                assertThat(receivedReview.review()).isEqualTo(reviewRequest.review());
                assertThat(receivedReview.assessment()).isEqualTo(reviewRequest.assessment());
                assertThat(receivedReview.readPages()).isEqualTo(reviewRequest.readPages());
            });
        } finally {
            deleteReviewIfExists(createdReview);
            deleteClubIfExists(createdClub);
        }
    }

    @Test
    void successfulUpdateReviewTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("UpdateReview"), userToken)
        );

        ReviewModel createdReview = null;

        try {
            ReviewRequestModel createReviewRequest = step("Подготовить тело запроса для создания review", () ->
                    createReviewRequest(createdClub.id(), "Original review")
            );

            createdReview = step("Создать review", () ->
                    api.reviews.createReview(createReviewRequest, userToken)
            );

            ReviewModel finalCreatedReview = createdReview;

            ReviewRequestModel updateReviewRequest = step("Подготовить новое тело review для PUT", () ->
                    new ReviewRequestModel(
                            createdClub.id(),
                            "Updated review text " + UUID.randomUUID(),
                            4,
                            240
                    )
            );

            ReviewModel updatedReview = step("Обновить review через PUT", () ->
                    api.reviews.updateReview(finalCreatedReview.id(), updateReviewRequest, userToken)
            );

            step("Проверить обновленные данные review", () -> {
                assertThat(updatedReview.id()).isEqualTo(finalCreatedReview.id());
                assertThat(updatedReview.club()).isEqualTo(createdClub.id());
                assertThat(updatedReview.review()).isEqualTo(updateReviewRequest.review());
                assertThat(updatedReview.assessment()).isEqualTo(updateReviewRequest.assessment());
                assertThat(updatedReview.readPages()).isEqualTo(updateReviewRequest.readPages());
            });
        } finally {
            deleteReviewIfExists(createdReview);
            deleteClubIfExists(createdClub);
        }
    }

    @Test
    void successfulPatchReviewTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("PatchReview"), userToken)
        );

        ReviewModel createdReview = null;

        try {
            ReviewRequestModel createReviewRequest = step("Подготовить тело запроса для создания review", () ->
                    createReviewRequest(createdClub.id(), "Original patch review")
            );

            createdReview = step("Создать review", () ->
                    api.reviews.createReview(createReviewRequest, userToken)
            );

            ReviewModel finalCreatedReview = createdReview;

            String patchedReviewText = "Patched review text " + UUID.randomUUID();

            ReviewRequestModel patchReviewRequest = step("Подготовить тело PATCH-запроса только с review", () ->
                    new ReviewRequestModel(
                            null,
                            patchedReviewText,
                            null,
                            null
                    )
            );

            ReviewModel patchedReview = step("Обновить review через PATCH", () ->
                    api.reviews.patchReview(finalCreatedReview.id(), patchReviewRequest, userToken)
            );

            step("Проверить, что изменился только текст review", () -> {
                assertThat(patchedReview.id()).isEqualTo(finalCreatedReview.id());
                assertThat(patchedReview.club()).isEqualTo(createdClub.id());
                assertThat(patchedReview.review()).isEqualTo(patchedReviewText);
                assertThat(patchedReview.assessment()).isEqualTo(createReviewRequest.assessment());
                assertThat(patchedReview.readPages()).isEqualTo(createReviewRequest.readPages());
            });
        } finally {
            deleteReviewIfExists(createdReview);
            deleteClubIfExists(createdClub);
        }
    }

    @Test
    void successfulDeleteReviewTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("DeleteReview"), userToken)
        );

        ReviewModel createdReview = null;

        try {
            ReviewRequestModel reviewRequest = step("Подготовить тело запроса для создания review", () ->
                    createReviewRequest(createdClub.id(), "Review for delete")
            );

            createdReview = step("Создать review", () ->
                    api.reviews.createReview(reviewRequest, userToken)
            );

            ReviewModel finalCreatedReview = createdReview;

            step("Удалить review", () ->
                    api.reviews.deleteReview(finalCreatedReview.id(), userToken)
            );

            step("Проверить, что удаленный review больше не доступен", () ->
                    api.reviews.getReviewByIdRaw(finalCreatedReview.id())
                            .then()
                            .spec(notFoundReviewResponseSpec)
            );

            createdReview = null;
        } finally {
            deleteReviewIfExists(createdReview);
            deleteClubIfExists(createdClub);
        }
    }

    @Test
    void unsuccessfulCreateReviewWithoutTokenTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("ReviewWithoutToken"), userToken)
        );

        try {
            ReviewRequestModel reviewRequest = step("Подготовить тело запроса для создания review без токена", () ->
                    createReviewRequest(createdClub.id(), "Unauthorized review")
            );

            step("Отправить POST /clubs/reviews/ без токена и проверить ошибку авторизации", () ->
                    api.reviews.createReviewWithoutToken(reviewRequest)
                            .then()
                            .spec(unauthorizedReviewResponseSpec)
            );
        } finally {
            deleteClubIfExists(createdClub);
        }
    }

    @Test
    void unsuccessfulUpdateReviewByAnotherUserTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("ForbiddenUpdate"), userToken)
        );

        ReviewModel createdReview = null;

        try {
            ReviewRequestModel reviewRequest = step("Подготовить тело запроса для создания review", () ->
                    createReviewRequest(createdClub.id(), "Review owner text")
            );

            createdReview = step("Создать review основным пользователем", () ->
                    api.reviews.createReview(reviewRequest, userToken)
            );

            ReviewRequestModel updateRequest = step("Подготовить тело запроса для обновления чужого review", () ->
                    new ReviewRequestModel(
                            createdClub.id(),
                            "Another user tries to update review",
                            2,
                            50
                    )
            );

            ReviewModel finalCreatedReview = createdReview;

            step("Попробовать обновить чужой review и проверить 403", () ->
                    api.reviews.updateReviewRaw(finalCreatedReview.id(), updateRequest, anotherUserToken)
                            .then()
                            .spec(forbiddenReviewResponseSpec)
            );
        } finally {
            deleteReviewIfExists(createdReview);
            deleteClubIfExists(createdClub);
        }
    }

    @Test
    void unsuccessfulDeleteReviewByAnotherUserTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("ForbiddenDelete"), userToken)
        );

        ReviewModel createdReview = null;

        try {
            ReviewRequestModel reviewRequest = step("Подготовить тело запроса для создания review", () ->
                    createReviewRequest(createdClub.id(), "Review for forbidden delete")
            );

            createdReview = step("Создать review основным пользователем", () ->
                    api.reviews.createReview(reviewRequest, userToken)
            );

            ReviewModel finalCreatedReview = createdReview;

            step("Попробовать удалить чужой review и проверить 403", () ->
                    api.reviews.deleteReviewRaw(finalCreatedReview.id(), anotherUserToken)
                            .then()
                            .spec(forbiddenReviewResponseSpec)
            );
        } finally {
            deleteReviewIfExists(createdReview);
            deleteClubIfExists(createdClub);
        }
    }

    @Test
    void unsuccessfulCreateSecondReviewForSameClubTest() {
        ClubModel createdClub = step("Создать клуб для review", () ->
                api.clubs.createClub(createClubRequest("DuplicateReview"), userToken)
        );

        ReviewModel createdReview = null;

        try {
            ReviewRequestModel firstReviewRequest = step("Подготовить тело первого review", () ->
                    createReviewRequest(createdClub.id(), "First review")
            );

            createdReview = step("Создать первый review", () ->
                    api.reviews.createReview(firstReviewRequest, userToken)
            );

            ReviewRequestModel secondReviewRequest = step("Подготовить тело второго review для того же клуба и пользователя", () ->
                    createReviewRequest(createdClub.id(), "Second review")
            );

            step("Попробовать создать второй review на тот же клуб тем же пользователем и проверить 400", () ->
                    api.reviews.createReviewRaw(secondReviewRequest, userToken)
                            .then()
                            .spec(badRequestReviewResponseSpec)
            );
        } finally {
            deleteReviewIfExists(createdReview);
            deleteClubIfExists(createdClub);
        }
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

    private ReviewRequestModel createReviewRequest(Integer clubId, String marker) {
        return new ReviewRequestModel(
                clubId,
                marker + " " + UUID.randomUUID(),
                5,
                120
        );
    }

    private void deleteReviewIfExists(ReviewModel review) {
        if (review != null && review.id() != null) {
            step("Удалить тестовый review", () ->
                    api.reviews.deleteReviewRaw(review.id(), userToken)
            );
        }
    }

    private void deleteClubIfExists(ClubModel club) {
        if (club != null && club.id() != null) {
            step("Удалить тестовый клуб", () ->
                    api.clubs.deleteClub(club.id(), userToken)
            );
        }
    }
}