package models.logout;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LogoutBodyModel(
        String refresh
) {}
