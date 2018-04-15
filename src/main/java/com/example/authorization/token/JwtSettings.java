package com.example.authorization.token;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "demo.security.jwt")
public class JwtSettings {
    private Integer tokenExpirationTime;

    private Integer refreshTokenExpTime;

    private String tokenIssuer;

    private String tokenSigningKey;
}
