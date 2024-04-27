package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.client.Client;
import com.api.manager.fleet.dto.client.ClientDTO;
import com.api.manager.fleet.dto.response.DefaultResponseDTO;
import com.api.manager.fleet.repository.ClientRepository;
import com.api.manager.fleet.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService implements IClientService {
    private ClientRepository repository;

    @Autowired
    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity getAll() {
        List<ClientDTO> clients = repository.findAll()
                .stream()
                .map(client -> new ClientDTO(client.getId(), client.getName(), client.getCpf(), client.getAddress(), client.getPhone()))
                .collect(Collectors.toList());

        if (clients.isEmpty()) {
            return ResponseEntity.status(
                            HttpStatus.NOT_FOUND)
                    .body(new DefaultResponseDTO(false, "There are no registered customers!"));
        }
        return ResponseEntity.ok(clients);

    }

    @Override
    public Client getById(Long id) {
        return null;
    }

    @Override
    public Client save(ClientDTO client) {
        return null;
    }
}
