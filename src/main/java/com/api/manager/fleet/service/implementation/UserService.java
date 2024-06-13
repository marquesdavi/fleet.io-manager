package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.permission.Role;
import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.dto.user.RegisterDTO;
import com.api.manager.fleet.exception.NotFoundException;
import com.api.manager.fleet.exception.AlreadyExistsException;
import com.api.manager.fleet.mapper.UserDTOMapper;
import com.api.manager.fleet.repository.RoleRepository;
import com.api.manager.fleet.repository.UserRepository;
import com.api.manager.fleet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    @Override
    @Transactional
    public void create(RegisterDTO dto) {
        logger.info("Creating new user with email: {}", dto.email());

        Role userRole = roleRepository.findByName(Role.Values.USER.name())
                .orElseThrow(() -> {
                    logger.error("USER role not found!");
                    return new NotFoundException("USER role not found");
                });

        userRepository.findByEmail(dto.email()).ifPresent(existingUser -> {
            logger.warn("User with email {} already exists!", dto.email());
            throw new AlreadyExistsException("User already exists");
        });

        User user = userDTOMapper.toEntity(dto, Set.of(userRole));
        userRepository.save(user);

        logger.info("User with email {} successfully created.", dto.email());
    }

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }
}
