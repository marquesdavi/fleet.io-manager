package com.api.manager.fleet.dto.vehicle;

public record VehicleDTO(
        String model,
        String brand,
        String plate,
        Integer year
) {
}
