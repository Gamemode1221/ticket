package com.example.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityErrorHandler securityErrorHandler;

    public SecurityConfig(SecurityErrorHandler securityErrorHandler) {
        this.securityErrorHandler = securityErrorHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    // Swagger / OpenAPI는 공개
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    // 로그인 공개
                    .requestMatchers("/auth/**").permitAll()
                    // GET은 공개
                    .requestMatchers(HttpMethod.GET, "/tickets/**").permitAll()
                    // POST/PATCH/DELETE만 인증 필요
                    .requestMatchers(HttpMethod.POST, "/tickets/**").hasRole("USER")
                    .requestMatchers(HttpMethod.PATCH, "/tickets/**").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasRole("USER")
                    // 나머지는 일단 공개
                    .anyRequest().permitAll()
                )
                // JWT 검증(Authorization: Bearer ...)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint((req, res, ex) ->
                                securityErrorHandler.write(req, res, UNAUTHORIZED.value(), "UNAUTHORIZED", "Missing or invalid token"))
                        .accessDeniedHandler((req, res, ex) ->
                                securityErrorHandler.write(req, res, FORBIDDEN.value(), "FORBIDDEN", "Access denied"))
                );

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
        gac.setAuthorityPrefix("ROLE_");
        gac.setAuthoritiesClaimName("role"); // "USER" -> "ROLE_USER"

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(gac);
        return converter;
    }
}
