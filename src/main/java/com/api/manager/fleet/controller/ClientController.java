package com.api.manager.fleet.controller;

import com.api.manager.fleet.domain.client.Client;
import com.api.manager.fleet.service.IClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
@Tag(name = "Client", description = "Client management")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity listAll(){
        return service.getAll();
    }
}
