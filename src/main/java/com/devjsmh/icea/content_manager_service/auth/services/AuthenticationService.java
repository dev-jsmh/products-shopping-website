package com.devjsmh.icea.content_manager_service.auth.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devjsmh.icea.content_manager_service.auth.dtos.AuthenticationRequest;
import com.devjsmh.icea.content_manager_service.auth.dtos.AuthenticationResponse;
import com.devjsmh.icea.content_manager_service.auth.entities.TokenEntity;
import com.devjsmh.icea.content_manager_service.auth.entities.UserEntity;
import com.devjsmh.icea.content_manager_service.auth.repositories.TokensRepository;
import com.devjsmh.icea.content_manager_service.auth.repositories.UsersRepository;

@Service
public class AuthenticationService {

    private final AuthenticationManager _authenticationManager;
    private final UsersRepository _usersRepository;
    private final TokensRepository _tokensRepository;
    private final JwtUtil _jwtUtil;
    private final PasswordEncoder _passwordEncoder;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UsersRepository usersRepository,
            TokensRepository tokensRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this._authenticationManager = authenticationManager;
        this._usersRepository = usersRepository;
        this._tokensRepository = tokensRepository;
        this._jwtUtil = jwtUtil;
        this._passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse logIn(AuthenticationRequest authRequest) {

        var authToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

        this._authenticationManager.authenticate(authToken);

        UserEntity user = this._usersRepository
                .findByUsername(authRequest.getUsername())
                .orElseThrow();

        this.revokeAllUserTokens(user);

        String accessToken = this._jwtUtil.createToken(user);
        String refreshToken = this._jwtUtil.createRefreshToken(user);

        this.saveUserToken(user, accessToken);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private void saveUserToken(UserEntity user, String jwtToken) {

        TokenEntity token = new TokenEntity();

        token.setToken(jwtToken);
        token.setRevoked(false);
        token.setUser(user);

        this._tokensRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {

        List<TokenEntity> validTokensList = this._tokensRepository
                .findAllByUserIdAndIsRevokedFalse(user.getId());

        validTokensList.forEach(token -> {
            token.setRevoked(true);
        });

        this._tokensRepository.saveAll(validTokensList);
    }

    public void logOut(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authHeader.substring(7);

        Optional<TokenEntity> optToken = this._tokensRepository.findByToken(jwt);

        if (optToken.isEmpty()) {
            return;
        }

        TokenEntity token = optToken.get();
        token.setRevoked(true);

        this._tokensRepository.save(token);
    }

    public UserEntity signUp(AuthenticationRequest authRequest) {

        String encodedPassword = this._passwordEncoder.encode(authRequest.getPassword());
        UserEntity user = new UserEntity(authRequest.getUsername(), encodedPassword);

        return this._usersRepository.save(user);
    }

}
