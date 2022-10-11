package com.tutego.date4u.mvc.controllers;


import com.tutego.date4u.core.configuration.security.UnicornSecurityUser;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.profile.SearchFilter;
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

    public SearchWebController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @RequestMapping("/search")
    public String searchPage(Authentication authentication, Model model, @ModelAttribute SearchFormData searchFormData) {

        List<Profile> allProfiles = profileRepository.findAll();

        System.out.println("\n\n\n\n_____________________"+searchFormData.toString());

        Long id = ((UnicornSecurityUser) authentication.getPrincipal()).getId();

        LocalDate minBirthdate = LocalDate.now().minusYears(searchFormData.getMinimumAge());
        LocalDate maxBirthdate = minBirthdate.plusYears((searchFormData.getMinimumAge() - searchFormData.getMaximumAge()));
        allProfiles = profileRepository.search(new SearchFilter(
                searchFormData.getMinimumHornlength(),
                searchFormData.getMaximumHornlength(),
                profileRepository.getGenderById(id),//own
                searchFormData.getGender(),//attracted
                minBirthdate,
                maxBirthdate

        ));

        model.addAttribute("profiles", allProfiles);
        model.addAttribute("search", searchFormData);
        return "search";
    }

//    @PostMapping("/search")
//    public String search(Model model,@ModelAttribute SearchFormData searchFormData){
//
//    }
}
