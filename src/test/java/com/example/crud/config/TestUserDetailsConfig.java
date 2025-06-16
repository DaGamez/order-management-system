package com.example.crud.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class TestUserDetailsConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        
        UserDetails apiUser = User.builder()
            .username("api_user")
            .password(encoder.encode("password"))
            .roles("API_USER")
            .build();
            
        UserDetails adminUser = User.builder()
            .username("admin")
            .password(encoder.encode("admin"))
            .roles("ADMIN", "API_USER")
            .build();
            
        return new InMemoryUserDetailsManager(apiUser, adminUser);
    }
}
