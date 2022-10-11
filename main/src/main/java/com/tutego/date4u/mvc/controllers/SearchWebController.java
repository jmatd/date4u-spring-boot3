package com.tutego.date4u.mvc.controllers;


import com.tutego.date4u.core.configuration.security.UnicornSecurityUser;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.ProfileService;
import com.tutego.date4u.core.profile.SearchFilter;
import com.tutego.date4u.mvc.dto.ProfileDto;
import com.tutego.date4u.mvc.formdata.SearchFormData;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SearchWebController {
    private final ProfileRepository profileRepository;

    private final ProfileService profileService;

    public SearchWebController(ProfileRepository profileRepository, ProfileService profileService) {
        this.profileRepository = profileRepository;
        this.profileService = profileService;
    }


    @RequestMapping("/search")
    public String searchPage(Authentication authentication, Model model, @ModelAttribute SearchFormData searchFormData) {

        SearchFilter filter = createSearchFilter(authentication, searchFormData);
        List<ProfileDto> searchedProfileDtoList = profileService.getProfileDtosFromProfiles(profileRepository.search(filter));


        model.addAttribute("profiles", searchedProfileDtoList);
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
                profileRepository.getGenderById(id),//own
                searchFormData.getGender(),//attractedTo
                minBirthdate,
                maxBirthdate
        );
    }

//    @PostMapping("/search")
//    public String search(Model model,@ModelAttribute SearchFormData searchFormData){
//
//    }
}
