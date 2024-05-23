package com.api.manager.fleet.dto.customer;

public record CustomerDTO(
        Long id,
        String name,
        String email,
        String cnpj,
        String phone
) {
}
