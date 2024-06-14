package com.api.manager.fleet.controller;


import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.dto.vehicle.VehicleRequestDTO;
import com.api.manager.fleet.dto.vehicle.VehicleResponseDTO;
import com.api.manager.fleet.service.IVehicleService;
import com.api.manager.fleet.util.uri.UriUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/vehicle")
@Tag(name = "Vehicle", description = "Vehicle management")
@RequiredArgsConstructor
public class VehicleController {
    private final IVehicleService service;

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    @Cacheable(value = "vehicle-find-all")
    public Optional<DefaultPaginatedListDTO<VehicleResponseDTO>> listAll(
            @RequestParam(defaultValue = "0") Integer startRow,
            @RequestParam(defaultValue = "24") Integer endRow,
            @RequestParam Optional<String> plate,
            @RequestParam Optional<BigInteger> customerId
    ) {
        return service.getAll(startRow, endRow, plate, customerId);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "vehicle-find-id")
    public VehicleResponseDTO getById(
            @PathVariable() Long id
    ) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public DefaultResponseDTO updateById(
            @PathVariable() Long id,
            @RequestBody VehicleRequestDTO vehicleDTO
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
    public ResponseEntity<Vehicle> create(
            @Valid @RequestBody VehicleRequestDTO vehicleDTO
    ) {
        Vehicle instance = service.saveVehicle(vehicleDTO);
        URI location = UriUtil.buildUriWithId("/vehicle", instance.getId());

        return ResponseEntity.created(location).body(instance);
    }
}
