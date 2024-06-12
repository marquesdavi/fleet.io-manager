package com.api.manager.fleet.service;

import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.dto.user.RegisterDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    void create(RegisterDTO dto);
    List<User> list();
}
