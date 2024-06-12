package com.api.manager.fleet.controller;

import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.repository.VehicleRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('SCOPE_USER')")
@RequestMapping("/vehicle")
@Tag(name = "Vehicle", description = "Vehicle management")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        return ResponseEntity.ok(repository.findById(id));
    }
}
