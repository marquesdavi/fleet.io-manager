package com.api.manager.fleet.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;

public record VehicleRequestDTO(
        String model,
        String brand,
        @NotBlank
        String plate,
        Integer year,
        @NotNull
        BigInteger customer
) {
}
