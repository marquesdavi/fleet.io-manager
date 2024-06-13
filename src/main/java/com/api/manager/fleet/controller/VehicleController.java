package com.api.manager.fleet.controller;


import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.dto.vehicle.CreateVehicleDTO;
import com.api.manager.fleet.dto.vehicle.VehicleDTO;
import com.api.manager.fleet.service.IVehicleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/vehicle")
@Tag(name = "Vehicle", description = "Vehicle management")
@RequiredArgsConstructor
public class VehicleController {
    private final IVehicleService service;

    public Optional<DefaultPaginatedListDTO<VehicleDTO>> listAll(
            @RequestParam(defaultValue = "0") Integer startRow,
            @RequestParam(defaultValue = "24") Integer endRow
    ) {
        return service.getAll(startRow, endRow);
    }

    @GetMapping("/{id}")
    public VehicleDTO getById(
            @PathVariable() Long id
    ) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public DefaultResponseDTO updateById(
            @PathVariable() Long id,
            @RequestBody CreateVehicleDTO vehicleDTO
    ) {
        return service.updateById(id, vehicleDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public DefaultResponseDTO deleteById(
            @PathVariable() Long id
    ) {
        return service.deleteById(id);
    }

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public DefaultResponseDTO create(
            @Validated @RequestBody CreateVehicleDTO vehicleDTO
    ) {
        return service.save(vehicleDTO);
    }
}
