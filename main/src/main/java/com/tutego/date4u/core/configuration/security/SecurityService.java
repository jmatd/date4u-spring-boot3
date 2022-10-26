package com.tutego.date4u.core.configuration.security;

import com.tutego.date4u.core.profile.ProfileService;
import com.tutego.date4u.core.unicorn.Unicorn;
import com.tutego.date4u.core.unicorn.UnicornService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private UnicornService unicornService;
    private ProfileService profileService;

    public SecurityService(UnicornService unicornService, ProfileService profileService) {
        this.unicornService = unicornService;
        this.profileService = profileService;
    }

    @EventListener
    public void loginEvent(AuthenticationSuccessEvent event){
        UsernamePasswordAuthenticationToken source = (UsernamePasswordAuthenticationToken) event.getSource();

        UnicornSecurityUser unicornSecurityUser = (UnicornSecurityUser) source.getPrincipal();
        Unicorn unicorn = unicornService.findUnicornByUnicornSecurityUser(unicornSecurityUser);
        profileService.setProfileLastSeenToNowByProfileId(unicorn.getId());
    }
    @EventListener
    public void logoutEvent(LogoutSuccessEvent event){
        UsernamePasswordAuthenticationToken source = (UsernamePasswordAuthenticationToken) event.getSource();

        UnicornSecurityUser unicornSecurityUser = (UnicornSecurityUser) source.getPrincipal();
        Unicorn unicorn = unicornService.findUnicornByUnicornSecurityUser(unicornSecurityUser);
        profileService.setProfileLastSeenToNowByProfileId(unicorn.getId());
    }
}
