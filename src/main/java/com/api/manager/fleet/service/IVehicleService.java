package com.api.manager.fleet.service;

import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.dto.vehicle.VehicleRequestDTO;
import com.api.manager.fleet.dto.vehicle.VehicleResponseDTO;

import java.math.BigInteger;
import java.util.Optional;

public interface IVehicleService {
    Optional<DefaultPaginatedListDTO<VehicleResponseDTO>> getAll(
            Integer startRow,
            Integer endRow,
            Optional<String> plate,
            Optional<BigInteger> customerId);

    VehicleResponseDTO getById(Long id);
    Vehicle saveVehicle(VehicleRequestDTO customer);
    DefaultResponseDTO updateById(Long id, VehicleRequestDTO customerDTO);
    DefaultResponseDTO deleteById(Long id);
}
