package com.example.ticket.auth;

import com.example.ticket.api.error.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expiresMinutes;

    // 하드코딩 계정
    private static final String DEMO_USER = "demo";
    private static final String DEMO_PASS = "demo1234";

    public AuthService(JwtEncoder jwtEncoder,
                       @Value("${app.jwt.issuer}") String issuer,
                       @Value("${app.jwt.expires-minutes}") long expiresMinutes
    ) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expiresMinutes = expiresMinutes;
    }

    public String login(String username, String password) {
        if (!DEMO_USER.equals(username) || !DEMO_PASS.equals(password)) {
            throw new UnauthorizedException("Invalid credentials");
        }

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresMinutes * 60))
                .subject(username)
                .claim("role", "USER")
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT")
                .keyId("hmac-key")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
//        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
