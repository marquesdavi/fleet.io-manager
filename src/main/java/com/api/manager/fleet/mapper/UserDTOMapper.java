package com.api.manager.fleet.mapper;

import com.api.manager.fleet.domain.permission.Role;
import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.dto.user.RegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserDTOMapper {
    private final BCryptPasswordEncoder passwordEncoder;

    public User toEntity(RegisterDTO registerDTO, Set<Role> roles) {
        if (registerDTO == null) {
            return null;
        }

        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        user.setRole(roles);
        return user;
    }
}
