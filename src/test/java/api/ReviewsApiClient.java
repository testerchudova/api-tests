package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.reviews.ReviewModel;
import models.reviews.ReviewRequestModel;
import models.reviews.ReviewsListResponseModel;

import static io.restassured.RestAssured.given;
import static specs.ReviewsSpec.reviewsRequestSpec;
import static specs.ReviewsSpec.successfulCreateReviewResponseSpec;
import static specs.ReviewsSpec.successfulDeleteReviewResponseSpec;
import static specs.ReviewsSpec.successfulReviewResponseSpec;
import static specs.ReviewsSpec.successfulReviewsListResponseSpec;

public class ReviewsApiClient {

    private static final String REVIEWS_PATH = "/clubs/reviews/";
    private static final String REVIEW_BY_ID_PATH = "/clubs/reviews/{reviewId}/";

    @Step("Получить список reviews")
    public ReviewsListResponseModel getReviews() {
        return given(reviewsRequestSpec)
                .when()
                .get(REVIEWS_PATH)
                .then()
                .spec(successfulReviewsListResponseSpec)
                .extract()
                .as(ReviewsListResponseModel.class);
    }

    @Step("Получить review по id: {reviewId}")
    public ReviewModel getReviewById(Integer reviewId) {
        return given(reviewsRequestSpec)
                .when()
                .get(REVIEW_BY_ID_PATH, reviewId)
                .then()
                .spec(successfulReviewResponseSpec)
                .extract()
                .as(ReviewModel.class);
    }

    @Step("Получить review по id без success spec: {reviewId}")
    public Response getReviewByIdRaw(Integer reviewId) {
        return given(reviewsRequestSpec)
                .when()
                .get(REVIEW_BY_ID_PATH, reviewId);
    }

    @Step("Создать review")
    public ReviewModel createReview(ReviewRequestModel body, String token) {
        return given(reviewsRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .post(REVIEWS_PATH)
                .then()
                .spec(successfulCreateReviewResponseSpec)
                .extract()
                .as(ReviewModel.class);
    }

    @Step("Создать review без success spec")
    public Response createReviewRaw(ReviewRequestModel body, String token) {
        return given(reviewsRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .post(REVIEWS_PATH);
    }

    @Step("Создать review без токена")
    public Response createReviewWithoutToken(ReviewRequestModel body) {
        return given(reviewsRequestSpec)
                .body(body)
                .when()
                .post(REVIEWS_PATH);
    }

    @Step("Обновить review через PUT: {reviewId}")
    public ReviewModel updateReview(Integer reviewId, ReviewRequestModel body, String token) {
        return given(reviewsRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .put(REVIEW_BY_ID_PATH, reviewId)
                .then()
                .spec(successfulReviewResponseSpec)
                .extract()
                .as(ReviewModel.class);
    }

    @Step("Обновить review через PUT без success spec: {reviewId}")
    public Response updateReviewRaw(Integer reviewId, ReviewRequestModel body, String token) {
        return given(reviewsRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .put(REVIEW_BY_ID_PATH, reviewId);
    }

    @Step("Обновить review через PATCH: {reviewId}")
    public ReviewModel patchReview(Integer reviewId, ReviewRequestModel body, String token) {
        return given(reviewsRequestSpec)
                .auth().oauth2(token)
                .body(body)
                .when()
                .patch(REVIEW_BY_ID_PATH, reviewId)
                .then()
                .spec(successfulReviewResponseSpec)
                .extract()
                .as(ReviewModel.class);
    }

    @Step("Удалить review: {reviewId}")
    public void deleteReview(Integer reviewId, String token) {
        given(reviewsRequestSpec)
                .auth().oauth2(token)
                .when()
                .delete(REVIEW_BY_ID_PATH, reviewId)
                .then()
                .spec(successfulDeleteReviewResponseSpec);
    }

    @Step("Удалить review без success spec: {reviewId}")
    public Response deleteReviewRaw(Integer reviewId, String token) {
        return given(reviewsRequestSpec)
                .auth().oauth2(token)
                .when()
                .delete(REVIEW_BY_ID_PATH, reviewId);
    }
}
