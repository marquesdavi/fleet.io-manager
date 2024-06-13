package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.customer.Customer;
import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.dto.vehicle.CreateVehicleDTO;
import com.api.manager.fleet.dto.vehicle.VehicleDTO;
import com.api.manager.fleet.exception.NotFoundException;
import com.api.manager.fleet.mapper.VehicleDTOMapper;
import com.api.manager.fleet.repository.VehicleRepository;
import com.api.manager.fleet.service.IVehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService implements IVehicleService {
    private final VehicleRepository repository;
    private final VehicleDTOMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);


    @Override
    public Optional<DefaultPaginatedListDTO<VehicleDTO>> getAll(Integer startRow, Integer maxResults) {
        logger.info("Fetching all customers from row {} to {}", startRow, maxResults);

        if (startRow > maxResults) {
            logger.warn("Invalid pagination parameters: startRow {} is greater than maxResults {}", startRow, maxResults);
            throw new IllegalArgumentException("startRow can't be greater than maxResults");
        }

        Optional<DefaultPaginatedListDTO<Vehicle>> result = repository.findAll(startRow, maxResults);

        if (result.isPresent()) {
            logger.info("Fetched {} customers", result.get().getItems().size());
        } else {
            logger.info("No customers found for the given pagination parameters.");
        }

        return result.map(paginatedList -> new DefaultPaginatedListDTO<>(
                paginatedList.getTotalCount(),
                paginatedList.getItems().stream()
                        .map(mapper::toDTO)
                        .collect(Collectors.toList())
        ));
    }

    @Override
    public VehicleDTO getById(Long id) {
        logger.info("Fetching vehicle with ID {}", id);
        return mapper.toDTO(findVehicleById(id));
    }

    @Override
    public DefaultResponseDTO save(CreateVehicleDTO customer) {
        return null;
    }

    @Override
    public DefaultResponseDTO updateById(Long id, CreateVehicleDTO customerDTO) {
        return null;
    }

    @Override
    public DefaultResponseDTO deleteById(Long id) {
        return null;
    }

    private Vehicle findVehicleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    String message = "The Vehicle with ID " + id + " doesn't exist!";
                    logger.error(message);
                    return new NotFoundException(message);
                });
    }
}
