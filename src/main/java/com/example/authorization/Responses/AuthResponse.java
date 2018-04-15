package com.example.authorization.Responses;

import lombok.Getter;
import lombok.Setter;

public class AuthResponse {
    @Getter @Setter private boolean ok;
    @Getter @Setter private String token;
    @Getter @Setter private String refreshToken;

    public AuthResponse(boolean ok, String token, String refreshToken) {
        this.ok = ok;
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
