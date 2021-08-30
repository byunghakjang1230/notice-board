package com.rsupport.notice.auth.dto;

public class LoginResponse {
    private String token;

    protected LoginResponse() {
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
