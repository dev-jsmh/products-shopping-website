package com.devjsmh.icea.content_manager_service.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devjsmh.icea.content_manager_service.auth.dtos.AuthenticationRequest;
import com.devjsmh.icea.content_manager_service.auth.dtos.AuthenticationResponse;
import com.devjsmh.icea.content_manager_service.auth.entities.UserEntity;
import com.devjsmh.icea.content_manager_service.auth.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService _authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this._authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signUp(@RequestBody AuthenticationRequest authRequest) {

        var res = this._authenticationService.signUp(authRequest);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest request) {

        var res = this._authenticationService.logIn(request);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logOut(
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        this._authenticationService.logOut(authorizationHeader);

        Map<String, Object> logOutResponse = new HashMap<String, Object>();
        logOutResponse.put("message", "User loged out succesfully");

        return ResponseEntity.ok().body(logOutResponse);
    }
}
