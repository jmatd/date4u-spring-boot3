package com.tutego.date4u.mvc.controllers.profile;

import com.tutego.date4u.core.configuration.security.UnicornSecurityUser;
import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.unicorn.Unicorn;
import com.tutego.date4u.mvc.formdata.ProfileFormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileWebController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ProfileRepository profileRepository;

    public ProfileWebController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @PostMapping("/save")
    public String saveProfile(@ModelAttribute ProfileFormData profileFormData, Authentication authentication) {
        if(!userIdMatchesAuthorityId(profileFormData.getId(),authentication))
            throw new AccessDeniedException("User can edit only his own profile"); // Error code 403

        Optional<Profile> optionalProfile = profileRepository.findById(profileFormData.getId());
        //TODO error handling
        if (optionalProfile.isEmpty()) {
            return "redirect:/";
        }
        Profile profile = optionalProfile.get();

        profile.setNickname(profileFormData.getNickname());
        profile.setDescription(profileFormData.getDescription());
        profile.setAttractedToGender(profileFormData.getAttractedToGender());
        profile.setGender(profileFormData.getGender());
        profile.setBirthdate(profileFormData.getBirthdate());
        profile.setHornlength(profileFormData.getHornlength());

        profileRepository.save(profile);

        return "redirect:/profile/" + profileFormData.getId();
    }

    @RequestMapping("/profile")
    public String ownProfilePage(Authentication authentication){
        return "redirect:/profile/"+ ((UnicornSecurityUser)authentication.getPrincipal()).getId();
    }

    @RequestMapping("/profile/{id}")
    public String profilePage(@PathVariable long id, Model model, Authentication authentication) {
        System.out.println(id);

        Optional<Profile> optionalProfile = profileRepository.findById(id);

        if (optionalProfile.isEmpty()) return "redirect:/";

        Profile profile = optionalProfile.get();

        boolean isOwnProfile = userIdMatchesAuthorityId(id, authentication);

        log.info(authentication.toString()); //todo remove
        log.info("isOwnprofile" + isOwnProfile);

        model.addAttribute("profile",
                new ProfileFormData(
                        profile.getId(), profile.getNickname(), profile.getBirthdate(),
                        profile.getHornlength(), profile.getGender(),
                        profile.getAttractedToGender(), profile.getDescription(),
                        profile.getLastseen(),
                        profile.getPhotos().stream()
                                .sorted(Comparator
                                        //sorts photos by isProfilePhoto boolean and sets profilePhoto first
                                        .comparing(Photo::isProfilePhoto, Comparator.reverseOrder()))
                                .map(Photo::getName).toList()
                ))
                .addAttribute("isOwnProfile", isOwnProfile);
        return "profile";
    }

    private static boolean userIdMatchesAuthorityId(long id, Authentication authentication) {
        UnicornSecurityUser principal = (UnicornSecurityUser) authentication.getPrincipal();
        return principal.getId() == id;
    }


}
