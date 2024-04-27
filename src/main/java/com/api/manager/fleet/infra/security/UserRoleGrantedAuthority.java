package com.api.manager.fleet.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class UserRoleGrantedAuthority implements GrantedAuthority {
    private final String role;

    @Override
    public String getAuthority() {
        return role;
    }
}

