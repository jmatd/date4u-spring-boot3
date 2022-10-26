package com.tutego.date4u.core.configuration.security;

import com.tutego.date4u.core.unicorn.UnicornRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDetailsServiceConfiguration {

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    UserDetailsService userDetailsService(UnicornRepository unicorns) {
        return email ->
                unicorns.findUnicornByEmail(email)
                        .map(unicorn ->new UnicornSecurityUser( User.withUsername(unicorn.getEmail())
                                .password(unicorn.getPassword())
                                .roles("USER")
                                .build(), unicorn.getId()))
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Unicorn with " + email + " not found"));
    }
}