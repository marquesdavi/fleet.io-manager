package com.expctrl.service.implementation;

import com.expctrl.domain.permission.Role;
import com.expctrl.dto.user.AuthenticationDTO;
import com.expctrl.dto.user.LoginResponseDTO;
import com.expctrl.dto.user.RegisterDTO;
import com.expctrl.domain.user.User;
import com.expctrl.infra.security.CustomUserPrincipal;
import com.expctrl.infra.security.TokenService;
import com.expctrl.repository.RoleRepository;
import com.expctrl.repository.UserRepository;
import com.expctrl.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImplementation implements IAuthenticationService {
    private AuthenticationManager authenticationManager;
    private UserRepository repository;
    private TokenService tokenService;
    private RoleRepository roleRepository;


    @Autowired
    public AuthenticationServiceImplementation(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            TokenService tokenService,
            RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.repository = userRepository;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    /**
     * @param data - Authentication data
     * @return - JWT Token
     */
    @Override
    public ResponseEntity<LoginResponseDTO> login(AuthenticationDTO data) {
        try{
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            User user = ((CustomUserPrincipal) auth.getPrincipal()).getUser();
            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new LoginResponseDTO(true, token));
        } catch (AuthenticationException e){
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
