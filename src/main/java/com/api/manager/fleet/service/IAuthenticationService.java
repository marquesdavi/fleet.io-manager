package com.api.manager.fleet.service;

import com.api.manager.fleet.dto.user.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface IAuthenticationService {
    ResponseEntity<LoginResponseDTO> login(AuthenticationDTO authentication);

    ResponseEntity register(RegisterDTO registration);
}
