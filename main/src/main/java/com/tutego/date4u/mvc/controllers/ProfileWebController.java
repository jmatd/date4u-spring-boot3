package com.tutego.date4u.mvc.controllers;

import com.tutego.date4u.core.configuration.security.UnicornSecurityUser;
import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public String saveProfile(@ModelAttribute @Valid ProfileFormData profileFormData, Authentication authentication, BindingResult bindingResult) {
        if (!userIdMatchesAuthorityId(profileFormData.getId(), authentication))
            throw new AccessDeniedException("User can only edit his own profile"); // Error code 403

        Optional<Profile> optionalProfile = profileRepository.findById(profileFormData.getId());
        //TODO error handling
        if (optionalProfile.isEmpty()) return "redirect:/";
        Profile profile = optionalProfile.get();

        if (isUnder18(profileFormData.getBirthdate())) {
            bindingResult.addError(new FieldError("unicornFormData", "birthdate", "Dating ist erst ab 18."));
        }
        if (bindingResult.hasErrors()) {
            return "redirect:/profile";
        }

        profileService.save(setProfileFromProfileFormData(profileFormData,profile));

        return "redirect:/profile";
    }



    @RequestMapping("/profile")
    public String ownProfilePage(Authentication authentication) {

        return "redirect:/profile/" + ((UnicornSecurityUser) authentication.getPrincipal()).getId();
    }

    @RequestMapping("/profile/{id}")
    public String profilePage(@PathVariable long id, Model model, Authentication authentication) {

        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isEmpty()) return "redirect:/";
        Profile profile = optionalProfile.get();
        boolean isOwnProfile = userIdMatchesAuthorityId(id, authentication);


        if (isOwnProfile) model.addAttribute("profileForm", getProfileFormDataFromProfile(profile));

        model
                .addAttribute("profileDto", createProfileDtoFromProfile(profile))
                .addAttribute("isOwnProfile", isOwnProfile);
        return "profile";
    }

    @RequestMapping("/search")
    public String searchPage(Authentication authentication, Model model, @ModelAttribute SearchFormData searchFormData) {

        SearchFilter filter = createSearchFilter(authentication, searchFormData);

        model.addAttribute("profiles", searchAndReturnProfileDtos(filter));
        model.addAttribute("search", searchFormData);
        return "search";
    }


    private SearchFilter createSearchFilter(Authentication authentication, SearchFormData searchFormData) {
        Long id = ((UnicornSecurityUser) authentication.getPrincipal()).getId();

        LocalDate minBirthdate = LocalDate.now().minusYears(searchFormData.getMinimumAge());
        LocalDate maxBirthdate = minBirthdate.plusYears((searchFormData.getMinimumAge() - searchFormData.getMaximumAge()));

        return new SearchFilter(
                searchFormData.getMinimumHornlength(),
                searchFormData.getMaximumHornlength(),
                profileService.getGenderById(id),//own
                searchFormData.getGender(),//attractedTo
                minBirthdate,
                maxBirthdate
        );
    }

    private boolean userIdMatchesAuthorityId(long id, Authentication authentication) {
        Profile profile = unicornService.findProfileByUnicornId(((UnicornSecurityUser) authentication.getPrincipal()).getId());

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



    private Profile setProfileFromProfileFormData(ProfileFormData profileFormData, Profile profile){

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

    public  List<ProfileDto> searchAndReturnProfileDtos(SearchFilter filter) {
        return createProfileDtosFromProfileList(profileService.search(filter));
    }
}
