package com.expctrl.service;

import com.expctrl.dto.user.AuthenticationDTO;
import com.expctrl.dto.user.LoginResponseDTO;
import com.expctrl.dto.user.RegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface IAuthenticationService {
    ResponseEntity<LoginResponseDTO> login(AuthenticationDTO authentication);

    ResponseEntity register(RegisterDTO registration);
}
