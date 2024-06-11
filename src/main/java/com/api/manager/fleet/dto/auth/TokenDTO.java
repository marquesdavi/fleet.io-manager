package com.api.manager.fleet.dto.auth;

public record TokenDTO(String accessToken, Long expiresIn) {
}
