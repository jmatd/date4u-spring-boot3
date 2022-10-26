package com.tutego.date4u.core.profile;

import com.tutego.date4u.core.photo.Photo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

    public Profile findById(long id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isEmpty()) return null;
        return optionalProfile.get();
    }

    public void setProfileLastSeenToNowByProfileId(long id) {
        Profile profile = findById(id);
        profile.setLastseen(LocalDateTime.now());
        save(profile);
    }

    public long countProfiles() {
        return profileRepository.count();
    }

    /**
     * Get Profiles of Matches (both like each other)
     * @param profile input profile
     * @return profiles of matches
     */
    public static List<Profile> getProfilesOfMatches(Profile profile){
        List<Profile> list = new ArrayList<>();
        list.addAll(profile.getProfilesThatILike());
        list.addAll(profile.getProfilesThatLikeMe());

        Set<Profile> duplicatedProfilesRemovedSet = new HashSet<>();

        return list.stream()
                .filter(n -> !duplicatedProfilesRemovedSet.add(n))
                .toList();
    }


}
