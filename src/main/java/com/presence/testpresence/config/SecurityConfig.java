//package com.presence.testpresence.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//@Order(1)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/oauth/authorize**", "/login**", "/error**")
//                .permitAll()
//                .and()
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("humptydumpty").password(passwordEncoder().encode("123456")).roles("USER");
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
////import org.springframework.security.config.web.server.ServerHttpSecurity;
////import org.springframework.security.oauth2.jwt.JwtDecoder;
////import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
////import org.springframework.security.web.server.SecurityWebFilterChain;
////
////@Configuration
//////@EnableWebFluxSecurity
////public class SecurityConfig {
////
////    @Bean
////    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
////        http.csrf().disable()
////                .authorizeExchange()
////                .pathMatchers("/user/**").permitAll()
////                .anyExchange().permitAll()
////                .and();
////                //.oauth2Login()
////                //.and()
////                //.oauth2ResourceServer()
////                //.jwt();
////
////        return http.build();
////    }
////
////    @Bean
////    public JwtDecoder jwtDecoder() {
////        String jwkSetUri = "http://localhost:9050/.well-known/jwks.json"; // Replace with your auth server's JWKS endpoint
////        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
////        return jwtDecoder;
////    }
////}
//
