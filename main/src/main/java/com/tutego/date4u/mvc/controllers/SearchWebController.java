package com.tutego.date4u.mvc.controllers;


import com.tutego.date4u.core.configuration.security.UnicornSecurityUser;
import com.tutego.date4u.core.profile.ProfileService;
import com.tutego.date4u.core.profile.SearchFilter;
import com.tutego.date4u.mvc.formdata.SearchFormData;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
public class SearchWebController {

    private final ProfileService profileService;

    public SearchWebController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @RequestMapping("/search")
    public String searchPage(Authentication authentication, Model model, @ModelAttribute SearchFormData searchFormData) {

        SearchFilter filter = createSearchFilter(authentication, searchFormData);

        model.addAttribute("profiles", profileService.searchAndReturnProfileDtos(filter));
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
}
