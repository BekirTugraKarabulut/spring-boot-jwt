package com.tugra.service.impl;

import com.tugra.dto.DtoUserUI;
import com.tugra.dto.DtoUsers;
import com.tugra.dto.RefreshTokenRequest;
import com.tugra.jwt.AuthRequest;
import com.tugra.jwt.AuthResponse;
import com.tugra.jwt.JwtService;
import com.tugra.model.RefreshToken;
import com.tugra.model.Role;
import com.tugra.model.Users;
import com.tugra.repository.RefreshTokenRepository;
import com.tugra.repository.UsersRepository;
import com.tugra.service.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtService jwtService;

    @Override
    public DtoUsers registerUser(DtoUserUI dtoUserUI) {

        Users users = new Users();

        users.setUsername(dtoUserUI.getUsername());
        users.setName(dtoUserUI.getName());
        users.setPassword(bCryptPasswordEncoder.encode(dtoUserUI.getPassword()));
        users.setRole(Role.KULLANICI);

        Users dbUsers = usersRepository.save(users);

        DtoUsers dtoUsers = new DtoUsers();
        BeanUtils.copyProperties(dbUsers, dtoUsers);

        return dtoUsers;
    }

    public RefreshToken createdRefreshToken(Users user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setCreatedDate(new Date());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 3600 * 1000));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        return refreshToken;
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        authenticationProvider.authenticate(token);

        Optional<Users> user = usersRepository.findByUsername(authRequest.getUsername());

        if (user.isEmpty()) {
            return null;
        }

        String accessToken = jwtService.generateToken(user.get());
        RefreshToken refreshToken = createdRefreshToken(user.get());
        RefreshToken dbRefreshToken = refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken , dbRefreshToken.getRefreshToken());
    }

    public boolean isValidToken(Date expiration) {
        return new Date().before(expiration);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenRequest.getToken());

        if(refreshToken == null) {
            return null;
        }

        if(!isValidToken(refreshToken.getExpiredDate())) {
            return null;
        }

        Users user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshTokenUser = createdRefreshToken(user);
        RefreshToken dbRefreshToken = refreshTokenRepository.save(refreshTokenUser);

        return new AuthResponse(accessToken , dbRefreshToken.getRefreshToken());
    }
}
