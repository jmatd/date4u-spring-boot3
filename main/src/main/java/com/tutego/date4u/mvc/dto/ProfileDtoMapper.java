package com.tutego.date4u.mvc.dto;

import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileService;

import java.util.List;

public final class ProfileDtoMapper {

    private ProfileDtoMapper() {
    }

    public static ProfileDto createProfileDtoFromProfile(Profile profile) {
        return new ProfileDto(profile, ProfileService.getProfilePictureNameFromProfile(profile));
    }

    public static List<ProfileDto> createProfileDtosFromProfileList(List<Profile> profiles) {
        return profiles.stream()
                .map(ProfileDtoMapper::createProfileDtoFromProfile)
                .toList();
    }


}
