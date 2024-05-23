package com.api.manager.fleet.service;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import org.springframework.http.ResponseEntity;

public interface ICustomerService {
    ResponseEntity getAll(Integer startRow, Integer endRow);
    ResponseEntity getById(Long id);
    DefaultResponseDTO save(CreateCustomerDTO customer);
    ResponseEntity updateById(Long id);
    ResponseEntity deleteById(Long id);
}
