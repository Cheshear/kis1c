package com.example.authorization.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class RefreshToken {
    private Jws<Claims> claims;

    private RefreshToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    public static Optional<RefreshToken> create(RawAccessJwtToken token, String signingKey) {
        Jws<Claims> claims = token.parseClaims(signingKey);

        String role = claims.getBody().get("role", String.class);
        if (role.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(claims));
    }
}
