package com.tutego.date4u.core.configuration.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UnicornSecurityUser implements UserDetails {

    private final long id;
    private final UserDetails user;

    public UnicornSecurityUser(UserDetails user, long id) {
        this.user = user;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String toString() {
        String sb = this.getClass().getName() + " [" +
                "Username=" + this.getUsername() + ", " +
                "Password=[PROTECTED], " +
                "ID=" + this.getId() + ", " +
                "Enabled=" + this.isEnabled() + ", " +
                "AccountNonExpired=" + this.isAccountNonExpired() + ", " +
                "credentialsNonExpired=" + this.isCredentialsNonExpired() + ", " +
                "AccountNonLocked=" + this.isAccountNonLocked() + ", " +
                "Granted Authorities=" + this.getAuthorities() + "]";
        return sb;
    }

}
