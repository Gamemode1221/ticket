package com.example.ticket.auth;

public class TokenResponse {
    public String accessToken;
    public String tokenType = "Bearer";

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
