package com.api.manager.fleet.service;

import com.api.manager.fleet.dto.auth.LoginDTO;
import com.api.manager.fleet.dto.auth.TokenDTO;

public interface IAuthenticationService {
    TokenDTO login(LoginDTO request);
}
