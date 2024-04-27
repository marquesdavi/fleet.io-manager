package com.api.manager.fleet.dto.response;

// Deve ser utilizada nos endpoints que precisarem de resposta de sucesso

public record DefaultResponseDTO(Boolean success, String message) {
}
