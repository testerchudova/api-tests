package models.users;

public record UpdateUserResponse(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr
) {}