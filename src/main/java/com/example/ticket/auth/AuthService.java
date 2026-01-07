package com.example.ticket.auth;

import com.example.ticket.api.error.ConflictException;
import com.example.ticket.api.error.UnauthorizedException;
import com.example.ticket.user.AppUser;
import com.example.ticket.user.AppUserRepository;
import com.example.ticket.user.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expiresMinutes;

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtEncoder jwtEncoder,
                       @Value("${app.jwt.issuer}") String issuer,
                       @Value("${app.jwt.expires-minutes}") long expiresMinutes,
                       AppUserRepository userRepository,
                       PasswordEncoder passwordEncoder
    ) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expiresMinutes = expiresMinutes;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(String username, String password) {
        // existsByUsername() 체크 후 저장 사이에 동시에 가입하면 DB unique 제약에서 터질 수 있음
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Username already exists");
        }
        String hash = passwordEncoder.encode(password);
        userRepository.save(new AppUser(username, hash, Role.USER));
    }

    public String login(String username, String password) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresMinutes * 60))
                .subject(username)
                .claim("roles", List.of(user.getRole().name())) // ["USER"]
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
