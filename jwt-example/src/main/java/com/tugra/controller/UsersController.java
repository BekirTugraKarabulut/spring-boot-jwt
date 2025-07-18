package com.tugra.controller;

import com.tugra.dto.DtoUserUI;
import com.tugra.dto.DtoUsers;
import com.tugra.dto.RefreshTokenRequest;
import com.tugra.jwt.AuthRequest;
import com.tugra.jwt.AuthResponse;

public interface UsersController {

    public DtoUsers registerUser(DtoUserUI dtoUserUI);

    public AuthResponse authenticate(AuthRequest authRequest);

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
