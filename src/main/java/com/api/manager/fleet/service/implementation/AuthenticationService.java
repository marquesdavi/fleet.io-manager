package com.api.manager.fleet.service.implementation;

import com.api.manager.fleet.domain.permission.Role;
import com.api.manager.fleet.domain.user.User;
import com.api.manager.fleet.dto.auth.LoginDTO;
import com.api.manager.fleet.dto.auth.TokenDTO;
import com.api.manager.fleet.repository.UserRepository;
import com.api.manager.fleet.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.token.expires-in:3600}")
    private long expiresIn;

    @Override
    public TokenDTO login(LoginDTO request) {
        Optional<User> user = userRepository.findByEmail(request.email());

        if (user.isEmpty() || !user.get().isLoginCorrect(request, passwordEncoder)) {
            logger.warn("Failed login attempt for email: {}", request.email());
            throw new BadCredentialsException("User or password is invalid!");
        }

        var now = Instant.now();

        var scopes = user.get().getRole()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("fleetioauth")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .claim("roles", user.get().getRole().stream()
                        .map(Role::getName) // Ensure role prefix
                        .collect(Collectors.toList()))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new TokenDTO(jwtValue, expiresIn);
    }
}
