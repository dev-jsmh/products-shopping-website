package com.devjsmh.icea.content_manager_service.auth.dtos;

/**
 * This object DTO is return back to the client as a response when it
 * requests for a new token using a previously generated refresh token
 * 
 * @author Jhonatan Samuel Martinez Hernandez
 */
public class RefreshTokenRequest {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
