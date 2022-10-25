package com.tutego.date4u.mvc.controllers;

import com.tutego.date4u.core.configuration.security.UnicornSecurityUser;
import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileService;
import com.tutego.date4u.core.profile.SearchFilter;
import com.tutego.date4u.core.unicorn.UnicornService;
import com.tutego.date4u.mvc.dto.ProfileDto;
import com.tutego.date4u.mvc.formdata.ProfileFormData;
import com.tutego.date4u.mvc.formdata.SearchFormData;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

@Controller
public class ProfileWebController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ProfileService profileService;

    private final UnicornService unicornService;

    public ProfileWebController(ProfileService profileService, UnicornService unicornService) {
        this.profileService = profileService;
        this.unicornService = unicornService;
    }

    @PostMapping("/profile/{id}/save")
    public String saveProfile(@ModelAttribute @Valid ProfileFormData profileFormData,
                              Model model,
                              @AuthenticationPrincipal UnicornSecurityUser unicornSecurityUser,
                              BindingResult bindingResult) {
        boolean isOwnProfile = isOwnProfile(profileFormData.getId(), unicornSecurityUser);
        if (!isOwnProfile)
            throw new AccessDeniedException("User can only edit his own profile"); // Error code 403

        Profile profile = profileService.findById(profileFormData.getId());
        if (profile == null) return "redirect:/";

        createProfileModel(model, isOwnProfile, profile);
        if (isUnder18(profileFormData.getBirthdate())) {
            bindingResult.addError(
                    new FieldError("profileFormData", "birthdate", "Dating ist erst ab 18."));
        }
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        profileService.save(setProfileFromProfileFormData(profileFormData, profile));
        return "profile";
    }


    @RequestMapping("/profile")
    public String ownProfilePage(@AuthenticationPrincipal UnicornSecurityUser unicornSecurityUser) {

        return "redirect:/profile/" + unicornSecurityUser.getId();
    }

    @GetMapping("/profile/{id}")
    public String profilePage(@PathVariable long id,
                              Model model,
                              @AuthenticationPrincipal UnicornSecurityUser unicornSecurityUser) {

        Profile profile = profileService.findById(id);
        if (profile == null) return "redirect:/";

        createProfileModel(model, isOwnProfile(id, unicornSecurityUser), profile);
        //fill in formdata on get request
        if (isOwnProfile(id, unicornSecurityUser))
            model.addAttribute("profileFormData", getProfileFormDataFromProfile(profile));
        return "profile";
    }

    public void createProfileModel(Model model, boolean isOwnProfile, Profile profile) {
        model
                .addAttribute("profileDto", createProfileDtoFromProfile(profile))
                .addAttribute("isOwnProfile", isOwnProfile);
    }

    @RequestMapping("/search")
    public String searchPage(@AuthenticationPrincipal UnicornSecurityUser unicornSecurityUser,
                             Model model,
                             @ModelAttribute SearchFormData searchFormData) {

        SearchFilter filter = createSearchFilter(unicornSecurityUser, searchFormData);

        model.addAttribute("profiles", searchAndReturnProfileDtos(filter));
        model.addAttribute("search", searchFormData);
        return "search";
    }


    private SearchFilter createSearchFilter(@AuthenticationPrincipal UnicornSecurityUser unicornSecurityUser,
                                            SearchFormData searchFormData) {

        LocalDate minBirthdate = LocalDate.now().minusYears(searchFormData.getMinimumAge());
        LocalDate maxBirthdate = minBirthdate.plusYears((searchFormData.getMinimumAge() - searchFormData.getMaximumAge()));

        return new SearchFilter(
                searchFormData.getMinimumHornlength(),
                searchFormData.getMaximumHornlength(),
                profileService.getGenderById(unicornSecurityUser.getId()),//own
                searchFormData.getGender(),//attractedTo
                minBirthdate,
                maxBirthdate
        );
    }

    private boolean isOwnProfile(long id, @AuthenticationPrincipal UnicornSecurityUser unicornSecurityUser) {
        Profile profile = unicornService.findProfileByUnicornId(unicornSecurityUser.getId());
        return profile.getId() == id;
    }


    private boolean isUnder18(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears() < 18;
    }

    private ProfileFormData getProfileFormDataFromProfile(Profile profile) {
        return new ProfileFormData(
                profile.getId(), profile.getNickname(), profile.getBirthdate(),
                profile.getHornlength(), profile.getGender(),
                profile.getAttractedToGender(), profile.getDescription(),
                profile.getLastseen(),
                profile.getPhotos().stream()
                        .sorted(Comparator
                                //sorts photos by isProfilePhoto boolean and sets profilePhoto first
                                .comparing(Photo::isProfilePhoto, Comparator.reverseOrder()))
                        .map(Photo::getName).toList());
    }


    private Profile setProfileFromProfileFormData(ProfileFormData profileFormData, Profile profile) {

        profile.setNickname(profileFormData.getNickname());
        profile.setDescription(profileFormData.getDescription());
        profile.setAttractedToGender(profileFormData.getAttractedToGender());
        profile.setGender(profileFormData.getGender());
        profile.setBirthdate(profileFormData.getBirthdate());
        profile.setHornlength(profileFormData.getHornlength());
        return profile;
    }

    public ProfileDto createProfileDtoFromProfile(Profile profile) {
        return new ProfileDto(profile, ProfileService.getProfilePictureNameFromProfile(profile));
    }

    public List<ProfileDto> createProfileDtosFromProfileList(List<Profile> profiles) {
        return profiles.stream()
                .map(this::createProfileDtoFromProfile)
                .toList();
    }

    public List<ProfileDto> searchAndReturnProfileDtos(SearchFilter filter) {
        return createProfileDtosFromProfileList(profileService.search(filter));
    }
}
