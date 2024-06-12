package com.api.manager.fleet.controller;

import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.dto.user.RegisterDTO;
import com.api.manager.fleet.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "User management")
public class UserController {

    private final IUserService service;

    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody RegisterDTO dto) {
        service.create(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> list() {
        List<User> users = service.list();
        return ResponseEntity.ok(users);
    }
}
