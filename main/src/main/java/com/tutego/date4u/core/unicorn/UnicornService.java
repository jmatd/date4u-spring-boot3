package com.tutego.date4u.core.unicorn;

import com.tutego.date4u.core.profile.ProfileService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tutego.date4u.core.profile.Profile;
import java.util.Optional;


@Service
public class UnicornService {


    private final PasswordEncoder passwordEncoder;

    private final UnicornRepository unicornRepository;

    public UnicornService(UnicornRepository unicornRepository, PasswordEncoder passwordEncoder) {
        this.unicornRepository = unicornRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean doesUnicornEmailAlreadyExist(String email) {
        return unicornRepository.existsUnicornByEmail(email);
    }

    public void createNewUnicorn(Unicorn unicorn) {
        String encodedPassword = passwordEncoder.encode(unicorn.getPassword());

        unicorn.setPassword(encodedPassword);
        unicorn.setProfile(ProfileService.createProfileWithDummyData());
        unicornRepository.save(unicorn);

    }

    public Profile findProfileByUnicornId(long id){
        Optional<Unicorn> unicornById = unicornRepository.findUnicornById(id);
        return unicornById.isEmpty() ? null : unicornById.get().getProfile();
    }
}
