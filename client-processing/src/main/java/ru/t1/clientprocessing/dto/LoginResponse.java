package ru.t1.clientprocessing.dto;

import java.util.List;

public record LoginResponse(
        String token,
        Long id,
        String type,
        String login,
        String email,
        List<String> roles
) {
    public LoginResponse(String token, Long id, String login, String email, List<String> roles) {
        this(token, id, "Bearer", login, email, roles);
    }
}