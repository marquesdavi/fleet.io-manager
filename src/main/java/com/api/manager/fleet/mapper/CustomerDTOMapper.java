package com.api.manager.fleet.mapper;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerDTOMapper {
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCnpj(),
                customer.getPhone()
        );
    }

    public CreateCustomerDTO toCreateDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CreateCustomerDTO(
                customer.getName(),
                customer.getEmail(),
                customer.getCnpj(),
                customer.getPhone()
        );
    }


    public Customer toEntity(CreateCustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
}
