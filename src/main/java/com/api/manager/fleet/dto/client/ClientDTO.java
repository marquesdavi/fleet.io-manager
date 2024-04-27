package com.api.manager.fleet.dto.client;

public record ClientDTO(
        Long id,
        String name,
        String cpf,
        String address,
        String phone
) {
}
