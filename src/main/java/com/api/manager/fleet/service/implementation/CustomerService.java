package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.exception.CustomerAlreadyExistsException;
import com.api.manager.fleet.exception.CustomerNotFoundException;
import com.api.manager.fleet.mapper.CustomerDTOMapper;
import com.api.manager.fleet.repository.CustomerRepository;
import com.api.manager.fleet.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        logger.info("Fetching all customers from row {} to {}", startRow, maxResults);

        if (startRow > maxResults) {
            logger.warn("Invalid pagination parameters: startRow {} is greater than maxResults {}", startRow, maxResults);
            throw new IllegalArgumentException("startRow can't be greater than maxResults");
        }

        Optional<DefaultPaginatedListDTO<Customer>> result = repository.findAll(startRow, maxResults);

        if (result.isPresent()) {
            logger.info("Fetched {} customers", result.get().getItems().size());
        } else {
            logger.info("No customers found for the given pagination parameters.");
        }

        return result.map(paginatedList -> new DefaultPaginatedListDTO<>(
                paginatedList.getTotalCount(),
                paginatedList.getItems().stream()
                        .map(customerDTOMapper::toDTO)
                        .collect(Collectors.toList())
        ));
    }

    @Override
    public CustomerDTO getById(Long id) {
        logger.info("Fetching customer with ID {}", id);
        return customerDTOMapper.toDTO(findCustomerById(id));
    }

    @Transactional
    @Override
    public DefaultResponseDTO save(CreateCustomerDTO customer) {
        logger.info("Saving new customer with CNPJ {}", customer.cnpj());
        checkIfCustomerExistsByCnpj(customer.cnpj());

        Customer instance = customerDTOMapper.toEntity(customer);
        repository.save(instance);
        logger.info("Customer with CNPJ {} successfully saved", customer.cnpj());

        return new DefaultResponseDTO(true, "Customer successfully registered!");
    }

    @Transactional
    @Override
    public DefaultResponseDTO updateById(Long id, CreateCustomerDTO customerDTO) {
        logger.info("Updating customer with ID {}", id);
        Customer existingCustomer = findCustomerById(id);

        if (customerDTO.name() != null) {
            existingCustomer.setName(customerDTO.name());
        }
        if (customerDTO.email() != null) {
            existingCustomer.setEmail(customerDTO.email());
        }
        if (customerDTO.cnpj() != null) {
            checkIfCustomerExistsByCnpj(customerDTO.cnpj());
            existingCustomer.setCnpj(customerDTO.cnpj());
        }
        if (customerDTO.phone() != null) {
            existingCustomer.setPhone(customerDTO.phone());
        }

        repository.update(existingCustomer);

        logger.info("Customer with ID {} successfully updated", id);
        return new DefaultResponseDTO(true, "Customer successfully updated!");
    }


    @Transactional
    @Override
    public DefaultResponseDTO deleteById(Long id) {
        logger.info("Deleting customer with ID {}", id);
        Customer customer = findCustomerById(id);
        repository.delete(customer);

        logger.info("Customer with ID {} successfully deleted", id);
        return new DefaultResponseDTO(true, "Customer successfully deleted!");
    }

    private Customer findCustomerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    String message = "The Customer with ID " + id + " doesn't exist!";
                    logger.error(message);
                    throw new CustomerNotFoundException(message);
                });
    }

    private void checkIfCustomerExistsByCnpj(String cnpj) {
        if (repository.findByCnpj(cnpj).isPresent()) {
            String message = "A Customer with CNPJ " + cnpj + " already exists!";
            logger.error(message);
            throw new CustomerAlreadyExistsException(message, HttpStatus.CONFLICT);
        }
    }
}
