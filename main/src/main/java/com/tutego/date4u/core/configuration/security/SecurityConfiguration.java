package com.tutego.date4u.core.configuration.security;

import jakarta.servlet.http.HttpSessionEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.Enumeration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().authenticated()
                                .and())

                .formLogin()
                    .loginPage("/login")
                     .defaultSuccessUrl("/", true)
                     .permitAll()
                .and()
                .sessionManagement(session -> session
                        .maximumSessions(1)
        );
        return http.build();
    }

//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher() {
//            @Override
//            public void sessionCreated(HttpSessionEvent event) {
//                System.out.println("removed TEST"); //todo remove
//                Enumeration<String> e = event.getSession().getAttributeNames();
//                while (e.hasMoreElements())
//                    System.out.println("Value is: " + e.nextElement());
//
//                super.sessionCreated(event);
//            }
//
//            @Override
//            public void sessionDestroyed(HttpSessionEvent event) {
//                System.out.println("removed TEST"); //todo remove
//                Enumeration<String> e = event.getSession().getAttributeNames();
//                while (e.hasMoreElements())
//                    System.out.println("Value is: " + e.nextElement());
//
//                super.sessionDestroyed(event);
//            }
//
//            @Override
//            public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
//
//                System.out.println("changed TEST"); //todo remove
//                Enumeration<String> e = event.getSession().getAttributeNames();
//                while (e.hasMoreElements())
//                    System.out.println("Value is: " + e.nextElement());
//                super.sessionIdChanged(event, oldSessionId);
//            }
//        };
//    }

}