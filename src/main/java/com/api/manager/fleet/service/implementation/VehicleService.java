package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.vehicle.Vehicle;
import com.api.manager.fleet.dto.response.DefaultPaginatedListDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.dto.vehicle.VehicleRequestDTO;
import com.api.manager.fleet.dto.vehicle.VehicleResponseDTO;
import com.api.manager.fleet.exception.AlreadyExistsException;
import com.api.manager.fleet.exception.NotFoundException;
import com.api.manager.fleet.mapper.VehicleDTOMapper;
import com.api.manager.fleet.repository.VehicleRepository;
import com.api.manager.fleet.service.IVehicleService;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService implements IVehicleService {
    private final VehicleRepository repository;
    private final VehicleDTOMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<DefaultPaginatedListDTO<VehicleResponseDTO>> getAll(
            @PositiveOrZero Integer startRow,
            @PositiveOrZero Integer maxResults,
            Optional<String> plate,
            Optional<BigInteger> customerId) {
        log.info("Fetching all vehicles from row {} to {}, with plate {} and customerId {}", startRow, maxResults, plate.orElse("N/A"), customerId.orElse(null));

        Assert.isTrue(startRow < maxResults, "startRow can't be greater than maxResults");

        Optional<DefaultPaginatedListDTO<Vehicle>> result = repository.findAll(startRow, maxResults, plate, customerId);

        if (result.isPresent()) {
            List<VehicleResponseDTO> vehicles = result.get().getItems().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());

            long totalCount = result.get().getTotalCount();

            log.info("Fetched {} vehicles", vehicles.size());
            return Optional.of(new DefaultPaginatedListDTO<>(totalCount, vehicles));
        } else {
            log.info("No vehicles found for the given parameters.");
            return Optional.empty();
        }
    }

    @Override
    public VehicleResponseDTO getById(Long id) {
        log.info("Fetching vehicle with ID {}", id);
        return mapper.toDTO(findVehicleById(id));
    }

    @Override
    public Vehicle saveVehicle(VehicleRequestDTO vehicle) {
        log.info("Saving new vehicle with PLATE {}", vehicle.plate());
        if (repository.findByPlateAndCustomerId(vehicle.plate(), vehicle.customer()).isPresent()) {
            String message = "A vehicle already exists!";
            log.error(message);
            throw new AlreadyExistsException(message);
        }

        Vehicle instance = mapper.toEntity(vehicle);
        instance = repository.save(instance);
        log.info("Vehicle with PLATE {} successfully saved", vehicle.plate());

        return instance;
    }

    @Transactional
    @Override
    public DefaultResponseDTO updateById(Long id, VehicleRequestDTO vehicleDTO) {
        log.info("Updating vehicle with ID {}", id);
        Vehicle existingVehicle = findVehicleById(id);

        if (vehicleDTO.model() != null) {
            existingVehicle.setModel(vehicleDTO.model());
        }
        if (vehicleDTO.brand() != null) {
            existingVehicle.setBrand(vehicleDTO.brand());
        }
        if (vehicleDTO.plate() != null) {
            checkIfVehicleExistsByPlate(vehicleDTO.plate());
            existingVehicle.setPlate(vehicleDTO.plate());
        }
        if (vehicleDTO.year() != null) {
            existingVehicle.setYear(vehicleDTO.year());
        }
        if (vehicleDTO.customer() != null) {
            existingVehicle.setCustomer(vehicleDTO.customer());
        }

        repository.update(existingVehicle);

        log.info("Vehicle with ID {} successfully updated", id);
        return new DefaultResponseDTO(true, "Vehicle successfully updated!");
    }

    @Override
    public DefaultResponseDTO deleteById(Long id) {
        log.info("Deleting vehicle with ID {}", id);
        Vehicle vehicle = findVehicleById(id);
        repository.delete(vehicle);

        log.info("Vehicle with ID {} successfully deleted", id);
        return new DefaultResponseDTO(true, "Vehicle successfully deleted!");
    }

    private Vehicle findVehicleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    String message = "The Vehicle with ID " + id + " doesn't exist!";
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    private void checkIfVehicleExistsByPlate(String plate) {
        if (repository.findByPlate(plate).isPresent()) {
            throw new AlreadyExistsException("A vehicle with plate " + plate + " already exists!");
        }
    }
}
