package models.users;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserRequest(
        String username,
        String firstName,
        String lastName,
        String email
) {}