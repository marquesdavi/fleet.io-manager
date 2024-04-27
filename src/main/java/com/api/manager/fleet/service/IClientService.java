package com.api.manager.fleet.service;

import com.api.manager.fleet.domain.client.Client;
import com.api.manager.fleet.dto.client.ClientDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IClientService {
    ResponseEntity getAll();
    Client getById(Long id);
    Client save(ClientDTO client);
}
