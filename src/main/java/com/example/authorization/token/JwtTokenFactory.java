package com.example.authorization.token;

import com.example.authorization.UserDatabase.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
@EnableConfigurationProperties(JwtSettings.class)
public class JwtTokenFactory {
    private final JwtSettings settings;

    @Autowired
    public JwtTokenFactory(JwtSettings settings){
        this.settings = settings;
    }

    public AccessJwtToken createAccessJwtToken(User user) {
        if (user.getEmail().isEmpty())
            throw new IllegalArgumentException("Cannot create JWT Token without email");

        if (user.getRole().isEmpty())
            throw new IllegalArgumentException("User doesn't have any privileges");

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("id", user.get_id());
        claims.put("role", user.getRole());

        LocalDateTime currentTime = LocalDateTime.now();

        System.out.println(settings.getTokenSigningKey());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusDays(settings.getTokenExpirationTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }

    public AccessJwtToken createChangePasswordToken(String email, ObjectId id){
        if (email.isEmpty())
            throw new IllegalArgumentException("Cannot create JWT Token without email");

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("id", id);

        LocalDateTime currentTime = LocalDateTime.now();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusDays(settings.getTokenExpirationTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(User user) {
        if(user.get_id() == null){
            throw new IllegalArgumentException("Cannot create JWT token");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("id", user.get_id());
        claims.put("role", user.getRole());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusDays(settings.getRefreshTokenExpTime())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }
}
