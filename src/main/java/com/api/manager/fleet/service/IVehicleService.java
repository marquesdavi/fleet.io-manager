package com.api.manager.fleet.service;

import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.dto.vehicle.CreateVehicleDTO;
import com.api.manager.fleet.dto.vehicle.VehicleDTO;

import java.util.Optional;

public interface IVehicleService {
    Optional<DefaultPaginatedListDTO<VehicleDTO>> getAll(Integer startRow, Integer endRow);
    VehicleDTO getById(Long id);
    DefaultResponseDTO save(CreateVehicleDTO customer);
    DefaultResponseDTO updateById(Long id, CreateVehicleDTO customerDTO);
    DefaultResponseDTO deleteById(Long id);
}
