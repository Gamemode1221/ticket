package com.example.ticket.auth;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req.username, req.password);
        return new TokenResponse(token);
    }

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody SignupRequest req) {
        authService.signup(req.username, req.password);
    }
}
