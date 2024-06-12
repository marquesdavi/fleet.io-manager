package com.api.manager.fleet.controller;

import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController(value = "/customer")
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
@Tag(name = "Customer", description = "Customer management")
public class CustomerController {
    private final ICustomerService service;

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public Optional<DefaultPaginatedListDTO<CustomerDTO>> listAll(
            @RequestParam(defaultValue = "0") Integer startRow,
            @RequestParam(defaultValue = "24") Integer endRow
    ) {
        return service.getAll(startRow, endRow);
    }

    @GetMapping("/{id}")
    public CustomerDTO getById(
            @PathVariable() Long id
    ) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public DefaultResponseDTO updateById(
            @PathVariable() Long id,
            @RequestBody CreateCustomerDTO customerDTO
    ) {
        return service.updateById(id, customerDTO);
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
            @Validated @RequestBody CreateCustomerDTO customerDTO
    ) {
        return service.save(customerDTO);
    }
}
