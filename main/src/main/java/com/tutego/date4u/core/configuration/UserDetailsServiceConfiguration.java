package com.tutego.date4u.core.configuration;

import com.tutego.date4u.core.unicorn.UnicornRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsServiceConfiguration {



 @Bean
 UserDetailsService userDetailsService(UnicornRepository unicorns ) {
  return email ->
      unicorns.findUnicornByEmail( email )
              .map( unicorn -> User.withUsername( unicorn.getEmail() )
                                   .password( unicorn.getPassword() )
                                   .roles( "USER" ).build() )
              .orElseThrow(() -> new UsernameNotFoundException(
                                   "Unicorn with " + email + " not found" ) );
  }
}