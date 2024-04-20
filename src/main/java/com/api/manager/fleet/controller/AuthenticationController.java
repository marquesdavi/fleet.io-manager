package com.expctrl.controller;

import com.expctrl.dto.user.AuthenticationDTO;
import com.expctrl.dto.user.RegisterDTO;
import com.expctrl.service.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private IAuthenticationService service;

    @Autowired
    public AuthenticationController(
            IAuthenticationService service
    ) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        return service.login(data);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        return service.register(data);
    }
}
