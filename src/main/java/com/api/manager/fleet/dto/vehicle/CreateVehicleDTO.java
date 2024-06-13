package com.api.manager.fleet.dto.vehicle;

public record CreateVehicleDTO(
        Long id,
        String model,
        String brand,
        String plate,
        Integer year
) {
}
