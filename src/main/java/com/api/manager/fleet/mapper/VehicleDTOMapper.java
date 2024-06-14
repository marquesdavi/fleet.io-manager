package com.api.manager.fleet.mapper;

import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.vehicle.VehicleRequestDTO;
import com.api.manager.fleet.dto.vehicle.VehicleResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class VehicleDTOMapper {
    public VehicleResponseDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getModel(),
                vehicle.getBrand(),
                vehicle.getPlate(),
                vehicle.getYear(),
                vehicle.getCustomer()
        );
    }

    public Vehicle toEntity(VehicleRequestDTO vehicleDTO) {
        if (vehicleDTO == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDTO, vehicle);
        return vehicle;
    }
}
