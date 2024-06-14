package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.dto.customer.CreateCustomerDTO;
import com.api.manager.fleet.dto.customer.CustomerDTO;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.exception.NotFoundException;
import com.api.manager.fleet.exception.AlreadyExistsException;
import com.api.manager.fleet.mapper.CustomerDTOMapper;
import com.api.manager.fleet.repository.CustomerRepository;
import com.api.manager.fleet.service.ICustomerService;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerRepository repository;
    private final CustomerDTOMapper customerDTOMapper;

    @Override
    public Optional<DefaultPaginatedListDTO<CustomerDTO>> getAll(
            @PositiveOrZero Integer startRow,
            @PositiveOrZero Integer maxResults
    ) {
        log.info("Fetching all customers from row {} to {}", startRow, maxResults);

        if (startRow > maxResults) {
            log.warn("Invalid pagination parameters: startRow {} is greater than maxResults {}", startRow, maxResults);
            throw new IllegalArgumentException("startRow can't be greater than maxResults");
        }

        Optional<DefaultPaginatedListDTO<Customer>> result = repository.findAll(startRow, maxResults);

        if (result.isPresent()) {
            log.info("Fetched {} customers", result.get().getItems().size());
        } else {
            log.info("No customers found for the given pagination parameters.");
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
        log.info("Fetching customer with ID {}", id);
        return customerDTOMapper.toDTO(findCustomerById(id));
    }

    @Transactional
    @Override
    public DefaultResponseDTO save(CreateCustomerDTO customer) {
        log.info("Saving new customer with CNPJ {}", customer.cnpj());
        checkIfCustomerExistsByCnpj(customer.cnpj());

        Customer instance = customerDTOMapper.toEntity(customer);
        repository.save(instance);
        log.info("Customer with CNPJ {} successfully saved", customer.cnpj());

        return new DefaultResponseDTO(true, "Customer successfully registered!");
    }

    @Transactional
    @Override
    public DefaultResponseDTO updateById(Long id, CreateCustomerDTO customerDTO) {
        log.info("Updating customer with ID {}", id);
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

        log.info("Customer with ID {} successfully updated", id);
        return new DefaultResponseDTO(true, "Customer successfully updated!");
    }

    @Transactional
    @Override
    public DefaultResponseDTO deleteById(Long id) {
        log.info("Deleting customer with ID {}", id);
        Customer customer = findCustomerById(id);
        repository.delete(customer);

        log.info("Customer with ID {} successfully deleted", id);
        return new DefaultResponseDTO(true, "Customer successfully deleted!");
    }

    public Customer findCustomerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    String message = "The Customer with ID " + id + " doesn't exist!";
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    private void checkIfCustomerExistsByCnpj(String cnpj) {
        if (repository.findByCnpj(cnpj).isPresent()) {
            String message = "A Customer with CNPJ " + cnpj + " already exists!";
            log.error(message);
            throw new AlreadyExistsException(message);
        }
    }
}
