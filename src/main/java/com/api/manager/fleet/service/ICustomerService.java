package com.api.manager.fleet.service;

import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ICustomerService {
    Optional<DefaultPaginatedListDTO<CustomerDTO>> getAll(Integer startRow, Integer endRow);
    CustomerDTO getById(Long id);
    DefaultResponseDTO save(CreateCustomerDTO customer);
    DefaultResponseDTO updateById(Long id, CustomerDTO customerDTO);
    DefaultResponseDTO deleteById(Long id);
}
