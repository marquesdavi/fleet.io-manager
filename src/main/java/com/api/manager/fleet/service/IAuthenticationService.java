package com.api.manager.fleet.service;

import com.api.manager.fleet.dto.user.AuthenticationDTO;
import com.api.manager.fleet.dto.user.LoginResponseDTO;
import com.api.manager.fleet.dto.user.RegisterDTO;
import org.springframework.http.ResponseEntity;


public interface IAuthenticationService {
    ResponseEntity<LoginResponseDTO> login(AuthenticationDTO authentication);

    ResponseEntity register(RegisterDTO registration);
}
