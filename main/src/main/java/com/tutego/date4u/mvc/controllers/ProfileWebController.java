package com.tutego.date4u.mvc.controllers;

import com.tutego.date4u.core.configuration.security.UnicornSecurityUser;
import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.ProfileService;
import com.tutego.date4u.core.unicorn.UnicornService;
import com.tutego.date4u.mvc.dto.ProfileDto;
import com.tutego.date4u.mvc.formdata.ProfileFormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.Optional;

@Controller
public class ProfileWebController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;

    private final UnicornService unicornService;

    public ProfileWebController(ProfileRepository profileRepository, ProfileService profileService, UnicornService unicornService) {
        this.profileRepository = profileRepository;
        this.profileService = profileService;
        this.unicornService = unicornService;
    }

    @PostMapping("/save")
    public String saveProfile(@ModelAttribute ProfileFormData profileFormData, Authentication authentication) {
        if (!userIdMatchesAuthorityId(profileFormData.getId(), authentication))
            throw new AccessDeniedException("User can only edit his own profile"); // Error code 403

        Optional<Profile> optionalProfile = profileRepository.findById(profileFormData.getId());
        //TODO error handling
        if (optionalProfile.isEmpty()) return "redirect:/";
        Profile profile = optionalProfile.get();

        profile.setNickname(profileFormData.getNickname());
        profile.setDescription(profileFormData.getDescription());
        profile.setAttractedToGender(profileFormData.getAttractedToGender());
        profile.setGender(profileFormData.getGender());
        profile.setBirthdate(profileFormData.getBirthdate());
        profile.setHornlength(profileFormData.getHornlength());

        profileRepository.save(profile);

        return "redirect:/profile";
    }

    @RequestMapping("/profile")
    public String ownProfilePage(Authentication authentication) {

        Profile profile = unicornService.findProfileByUnicornId(((UnicornSecurityUser) authentication.getPrincipal()).getId());
        if (profile == null) {

        }
        System.out.println(profile);
        return "redirect:/profile/" + profile.getId();
    }

    @RequestMapping("/profile/{id}")
    public String profilePage(@PathVariable long id, Model model, Authentication authentication) {

        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isEmpty()) return "redirect:/";
        Profile profile = optionalProfile.get();

        boolean isOwnProfile = userIdMatchesAuthorityId(id, authentication);

        ProfileDto profileDto = profileService.getProfileDtoFromProfile(profile);
        if (isOwnProfile) {
            model.addAttribute("profileForm",
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
                    ));
        }

        model
                .addAttribute("profileDto", profileDto)
                .addAttribute("isOwnProfile", isOwnProfile);
        return "profile";
    }

    private boolean userIdMatchesAuthorityId(long id, Authentication authentication) {
        Profile profile = unicornService.findProfileByUnicornId(((UnicornSecurityUser) authentication.getPrincipal()).getId());


        return profile.getId() == id;
    }


}
