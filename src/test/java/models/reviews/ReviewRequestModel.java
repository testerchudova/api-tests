package models.reviews;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReviewRequestModel(
        Integer club,
        String review,
        Integer assessment,
        Integer readPages
) {}
