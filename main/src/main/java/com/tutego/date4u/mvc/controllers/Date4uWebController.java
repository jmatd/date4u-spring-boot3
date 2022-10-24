package com.tutego.date4u.mvc.controllers;

import com.tutego.date4u.core.profile.ProfileRepository;
import com.tutego.date4u.core.unicorn.Unicorn;
import com.tutego.date4u.core.unicorn.UnicornRepository;
import com.tutego.date4u.core.unicorn.UnicornService;
import com.tutego.date4u.mvc.formdata.UnicornFormData;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Date4uWebController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ProfileRepository profileRepository;

    private final UnicornRepository unicornRepository;
    private final UnicornService unicornService;

    public Date4uWebController(ProfileRepository profileRepository, UnicornRepository unicornRepository, UnicornService unicornService) {
        this.profileRepository = profileRepository;
        this.unicornRepository = unicornRepository;
        this.unicornService = unicornService;
    }


    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/")
    public String indexPage(Model model) {
        long profileCount = profileRepository.count();
        model.addAttribute("totalProfiles", profileCount);
        return "index";
    }

    @GetMapping("/register")
    public String registerPageGet(Model model, @ModelAttribute UnicornFormData unicornFormData) {
        return "register";
    }

    @PostMapping("/register")
    public String registerPagePost(Model model, @Valid UnicornFormData unicornFormData, BindingResult bindingResult) {
        if (unicornRepository.existsUnicornByEmail(unicornFormData.getEmail())) {
            bindingResult.addError(new FieldError("unicornFormData", "email","E-mail bereits in verwendung"));
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }

        Unicorn unicorn = new Unicorn(unicornFormData.getEmail(), unicornFormData.getPassword(), null);

        unicornService.createNewUnicorn(unicorn);
        return "redirect:/login?registered";
    }

}