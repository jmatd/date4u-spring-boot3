package com.tutego.date4u.mvc.controllers.profile;

import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.mvc.formdata.ProfileFormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class ProfileWebController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ProfileRepository profileRepository;

    public ProfileWebController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @PostMapping("/save")
    public String saveProfile(@ModelAttribute ProfileFormData profile) {
        log.info(profile.toString());
        return "redirect:/profile/" + profile.getId();
    }

    @RequestMapping("/profile/{id}")
    public String profilePage(@PathVariable long id, Model model) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isEmpty()) {
            return "redirect:/";
        }
        Profile profile = optionalProfile.get();
        model.addAttribute("profile",
                new ProfileFormData(
                        profile.getId(), profile.getNickname(), profile.getBirthdate(),
                        profile.getHornlength(), profile.getGender(),
                        profile.getAttractedToGender(), profile.getDescription(),
                        profile.getLastseen()
                ));

        return "profile";
    }
}
