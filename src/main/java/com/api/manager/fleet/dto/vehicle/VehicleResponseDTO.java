package com.api.manager.fleet.dto.vehicle;

import java.math.BigInteger;

public record VehicleResponseDTO(
        Long id,
        String model,
        String brand,
        String plate,
        Integer year,
        BigInteger customer
) {
}
