package com.api.manager.fleet.controller;

import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.domain.permission.Role;
import com.api.manager.fleet.repository.UserRepository;
import com.api.manager.fleet.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.api.manager.fleet.dto.auth.*;



import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management")
public class AuthenticationController {
    private final IAuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }
}
