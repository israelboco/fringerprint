//package com.presence.testpresence.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http.csrf().disable()
//                .authorizeExchange()
//                .pathMatchers("/user/**").authenticated()
//                .anyExchange().permitAll()
//                .and()
//                .oauth2Login()
//                .and()
//                .oauth2ResourceServer()
//                .jwt();
//
//        return http.build();
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        String jwkSetUri = "https://your-auth-server/.well-known/jwks.json"; // Replace with your auth server's JWKS endpoint
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
//        return jwtDecoder;
//    }
//}