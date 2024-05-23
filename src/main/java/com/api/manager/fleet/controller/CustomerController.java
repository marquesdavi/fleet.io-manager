package com.api.manager.fleet.controller;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.service.ICustomerService;
import com.api.manager.fleet.util.exception.GenericException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer", description = "Customer management")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity listAll(
            @RequestParam(defaultValue = "0") Integer startRow,
            @RequestParam(defaultValue = "24") Integer endRow
    ) {
        return service.getAll(startRow, endRow);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(
            @PathVariable() Long id
    ) {
        return service.getById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity updateById(
            @PathVariable() Long id
    ) {
        return service.updateById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity deleteById(
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
