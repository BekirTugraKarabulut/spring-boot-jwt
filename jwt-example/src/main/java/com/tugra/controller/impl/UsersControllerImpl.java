package com.tugra.controller.impl;

import com.tugra.controller.UsersController;
import com.tugra.dto.DtoUserUI;
import com.tugra.dto.DtoUsers;
import com.tugra.dto.RefreshTokenRequest;
import com.tugra.jwt.AuthRequest;
import com.tugra.jwt.AuthResponse;
import com.tugra.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersControllerImpl implements UsersController {

    @Autowired
    private UsersService usersService;

    @Override
    @PostMapping(path = "/register")
    public DtoUsers registerUser(@RequestBody DtoUserUI dtoUserUI) {
        return usersService.registerUser(dtoUserUI);
    }

    @Override
    @PostMapping(path = "/authenticate")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        return usersService.authenticate(authRequest);
    }

    @Override
    @PostMapping(path = "/refresh_token")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return usersService.refreshToken(refreshTokenRequest);
    }

}
