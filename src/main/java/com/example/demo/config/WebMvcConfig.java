package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.example.demo.mvc")
@EnableWebSecurity
public class WebMvcConfig {
    private final String jwkSetUri = "https://login.microsoftonline.com/common/discovery/keys";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .antMatchers("/jolokia/**").hasAuthority("SCOPE_jolokia_access"))
                .oauth2ResourceServer()
                .jwt(jwt -> jwt.jwkSetUri(jwkSetUri));

        return http.build();
    }
}