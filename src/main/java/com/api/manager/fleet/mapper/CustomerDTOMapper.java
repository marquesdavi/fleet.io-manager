package com.api.manager.fleet.mapper;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerDTOMapper implements Function<Customer, CustomerDTO> {
    @Override
    public CustomerDTO apply(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCnpj(),
                customer.getPhone()
        );
    }
}
