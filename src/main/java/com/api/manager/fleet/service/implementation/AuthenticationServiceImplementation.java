package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.permission.Role;
import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.dto.user.AuthenticationDTO;
import com.api.manager.fleet.dto.user.LoginResponseDTO;
import com.api.manager.fleet.dto.user.RegisterDTO;
import com.api.manager.fleet.infra.security.CustomUserPrincipal;
import com.api.manager.fleet.infra.security.TokenService;
import com.api.manager.fleet.repository.RoleRepository;
import com.api.manager.fleet.repository.UserRepository;
import com.api.manager.fleet.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements IAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    /**
     * @param data - Authentication data
     * @return - JWT Token
     */
    @Override
    public ResponseEntity<LoginResponseDTO> login(AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            User user = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
            String token = tokenService.generateToken(user.getEmail());

            return ResponseEntity.ok(new LoginResponseDTO(true, token));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Access denied!"));
        }
    }

    /**
     * @param data - Registration data
     * @return - Response Entity
     */
    @Override
    public ResponseEntity register(RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        Role userRole = this.roleRepository.findByName("USER");

        User newUser = new User(data.email(), encryptedPassword, data.firstName(), data.lastName(), userRole);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
