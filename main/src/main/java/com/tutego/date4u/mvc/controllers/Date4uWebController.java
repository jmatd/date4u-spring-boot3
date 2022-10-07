package com.tutego.date4u.mvc.controllers;

import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class Date4uWebController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ProfileRepository profileRepository;

    public Date4uWebController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @RequestMapping("/")
    public String indexPage(Model model) {
        long profileCount = profileRepository.count();
        model.addAttribute("totalProfiles", profileCount);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// todo remove
        log.info(auth.toString());  // TODO remove
        return "index";
    }

    @RequestMapping("/search")
    public String searchPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// todo remove
        log.info(auth.toString());  // TODO remove
        List<Profile> allProfiles = profileRepository.findAll();
        model.addAttribute("profiles", allProfiles);
        return "search";
    }
}