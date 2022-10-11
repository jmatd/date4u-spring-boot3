package com.tutego.date4u.core.profile;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.mvc.dto.ProfileDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    final
    ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Gets the latest profile Image from Profile
     *
     * @param profile
     * @return Name of latest ProfileImage
     */
    public static String getProfilePictureNameFromProfile(Profile profile) {
        List<Photo> photos = profile.getPhotos();
        return photos.stream()
                .filter(Photo::isProfilePhoto)
                .max(Comparator.comparing(Photo::getCreated))
                .map(Photo::getName)
                .orElseThrow();
    }

    public ProfileDto getProfileDtoFromProfile(Profile profile) {
        return new ProfileDto(profile, getProfilePictureNameFromProfile(profile));
    }

    public List<ProfileDto> getProfileDtosFromProfiles(List<Profile> profiles) {
        return profiles.stream()
                .map(this::getProfileDtoFromProfile)
                .collect(Collectors.toList());
    }
}
