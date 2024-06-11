package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.mapper.CustomerDTOMapper;
import com.api.manager.fleet.repository.CustomerRepository;
import com.api.manager.fleet.service.ICustomerService;
import com.api.manager.fleet.util.exception.CustomerNotFoundException;
import com.api.manager.fleet.util.exception.GenericException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerRepository repository;
    private final CustomerDTOMapper customerDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Override
    public Optional<DefaultPaginatedListDTO<CustomerDTO>> getAll(Integer startRow, Integer maxResults) {
        if(startRow > maxResults){
            throw new IllegalArgumentException("startRow can't be greater than maxResults");
        }

        Optional<DefaultPaginatedListDTO<Customer>> result = repository.findAll(startRow, maxResults);

        return result.map(paginatedList -> new DefaultPaginatedListDTO<>(
                paginatedList.getTotalCount(),
                paginatedList.getItems().stream()
                        .map(customerDTOMapper::toDTO)
                        .collect(Collectors.toList())
        ));
    }

    @Override
    public CustomerDTO getById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("There is no Customer with id " + id));

        return customerDTOMapper.toDTO(customer);
    }

    @Override
    public DefaultResponseDTO save(CreateCustomerDTO customer) {
        if (repository.findByCnpj(customer.cnpj()).isPresent()) {
            logger.error("A Customer with CNPJ " + customer.cnpj() + " already exists!");
            throw new GenericException("A Customer with CNPJ " + customer.cnpj() + " already exists!", HttpStatus.CONFLICT);
        }

        Customer instance = customerDTOMapper.toEntity(customer);
        repository.save(instance);

        return new DefaultResponseDTO(true, "Customer successfully registered!");
    }

    @Override
    public DefaultResponseDTO updateById(Long id, CustomerDTO customerDTO) {
        Customer instance = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("The Customer with ID " + id + " doesn't exists!");
                    return new GenericException("The Customer with ID " + id + " doesn't exists!", HttpStatus.NOT_FOUND);
                });


        repository.update(instance);

        return new DefaultResponseDTO(true, "Customer successfully deleted!");
    }

    @Override
    public DefaultResponseDTO deleteById(Long id) {
        Customer customer = repository.findById(id).orElseThrow(
                () -> {
                    logger.error("The Customer with ID " + id + " doesn't exists!");
                    return new GenericException("The Customer with ID " + id + " doesn't exists!", HttpStatus.NOT_FOUND);
                }
        );

        repository.delete(customer);

        return new DefaultResponseDTO(true, "Customer successfully deleted!");
        //return ResponseEntity.ok("");
    }


}
