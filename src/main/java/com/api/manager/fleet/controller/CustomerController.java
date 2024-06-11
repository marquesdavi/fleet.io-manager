package com.api.manager.fleet.controller;

import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.service.ICustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
@RequestMapping("/customer")
@Tag(name = "Customer", description = "Customer management")
@RequiredArgsConstructor
public class CustomerController {
    private final ICustomerService service;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Optional<DefaultPaginatedListDTO<CustomerDTO>> listAll(
            @RequestParam(defaultValue = "0") Integer startRow,
            @RequestParam(defaultValue = "24") Integer endRow
    ) {
        return service.getAll(startRow, endRow);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomerDTO getById(
            @PathVariable() Long id
    ) {
        return service.getById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public DefaultResponseDTO updateById(
            @PathVariable() Long id,
            @Valid @RequestBody CustomerDTO customerDTO
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
