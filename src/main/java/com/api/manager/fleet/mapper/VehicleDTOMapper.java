package com.api.manager.fleet.mapper;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.vehicle.CreateVehicleDTO;
import com.api.manager.fleet.dto.vehicle.VehicleDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class VehicleDTOMapper {
    public VehicleDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return new VehicleDTO(
                vehicle.getModel(),
                vehicle.getBrand(),
                vehicle.getPlate(),
                vehicle.getYear()
        );
    }

//    public CreateCustomerDTO toCreateDTO(Customer customer) {
//        if (customer == null) {
//            return null;
//        }
//        return new CreateCustomerDTO(
//                customer.getName(),
//                customer.getEmail(),
//                customer.getCnpj(),
//                customer.getPhone()
//        );
//    }


    public Vehicle toEntity(CreateVehicleDTO vehicleDTO) {
        if (vehicleDTO == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleDTO, vehicle);
        return vehicle;
    }
}
