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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerRepository repository;
    private final CustomerDTOMapper customerDTOMapper;

    @Override
    public ResponseEntity getAll(Integer startRow, Integer endRow) {
        List<CustomerDTO> clients = repository.findAll(startRow, endRow)
                .orElseThrow(() -> new GenericException("", HttpStatus.NOT_FOUND))
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());

        Long lastRow = repository.countFindAll();

        return ResponseEntity.ok(new DefaultPaginatedListDTO<>(lastRow, clients).toJson());
    }

    @Override
    public ResponseEntity getById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(
                        () -> new CustomerNotFoundException("There is no Customer with id " + id)
                );

        return ResponseEntity.ok(new CustomerDTOMapper().apply(customer));
    }

    @Override
    public DefaultResponseDTO save(CreateCustomerDTO customer) {
        try {
            Customer instance = new Customer();
            instance.setName(customer.name());
            instance.setCnpj(customer.cnpj());
            instance.setEmail(customer.email());
            instance.setPhone(customer.phone());
            repository.save(instance);

            return new DefaultResponseDTO(true, "Customer successfully registered!");
        } catch (Exception e){
            throw new GenericException("Something went wrong: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity updateById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        return null;
    }


}
