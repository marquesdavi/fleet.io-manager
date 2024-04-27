package com.api.manager.fleet.dto.client;

import jakarta.validation.constraints.Size;

public record CreateClientDTO(
        @Size(max = 50)
        String name,
        @Size(min = 11, max = 11)
        String cpf,
        String address,
        String phone
) {
}
