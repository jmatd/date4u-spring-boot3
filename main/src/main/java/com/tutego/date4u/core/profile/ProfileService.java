package com.tutego.date4u.core.profile;

import com.tutego.date4u.core.photo.Photo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

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
     * @param profile of unicorn
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

    public int getGenderById(long id) {
        return profileRepository.getGenderById(id);
    }


    public List<Profile> search(SearchFilter filter) {
        return profileRepository.search((byte) filter.gender(), filter.attractedToGender()
                        .byteValue(), filter.minBirthdate(), filter.maxBirthdate(),
                (short) filter.minHornlength(), (short) filter.maxHornlength());
    }


    public static Profile createProfileWithDummyData(String nickname) {

        Profile profile = new Profile(nickname, LocalDate.now(), 0, 0,
                null, "Erzähl was über dich", LocalDateTime.now());
        Photo photo = new Photo(null, profile, "dummyPhoto", true, LocalDateTime.now());
        profile.addPhoto(photo);
        return profile;
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }
}
