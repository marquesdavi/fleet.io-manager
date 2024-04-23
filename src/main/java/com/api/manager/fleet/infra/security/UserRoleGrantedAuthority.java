package com.api.manager.fleet.infra.security;

import org.springframework.security.core.GrantedAuthority;

public class UserRoleGrantedAuthority implements GrantedAuthority {
    private final String role;

    public UserRoleGrantedAuthority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}

