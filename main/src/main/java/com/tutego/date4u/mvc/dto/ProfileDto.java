package com.tutego.date4u.mvc.dto;

import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileService;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDate;
import java.util.*;

public final class ProfileDto {

    private static final PrettyTime prettyTime = new PrettyTime(new Locale("de"));

    private final Profile profile;
    private final String profileImageName;
    private final int ageInYears;
    private final String humanReadablyLastSeen;

    private final List<String> photoNamesList;

    // Like eachother
    private final List<Profile> profilesOfMatches;


    public ProfileDto(Profile profile, String profileImageName) {
        this.profile = profile;
        this.profileImageName = profileImageName;
        this.ageInYears = calculateAgeInYears(profile.getBirthdate());
        this.humanReadablyLastSeen = prettyTime.format(profile.getLastseen());
        this.photoNamesList = profile.getPhotos().stream()
                .sorted(Comparator
                        //sorts photos by isProfilePhoto boolean and sets profilePhoto first
                        .comparing(Photo::isProfilePhoto, Comparator.reverseOrder()))
                .map(Photo::getName).toList();
        this.profilesOfMatches = ProfileService.getProfilesOfMatches(profile);
    }

    public List<Profile> getProfilesOfMatches() {
        return profilesOfMatches;
    }

    public List<String> getPhotoNamesList() {
        return photoNamesList;
    }

    public String getHumanReadablyLastSeen() {
        return humanReadablyLastSeen;
    }

    private int calculateAgeInYears(LocalDate birthdate) {
        return birthdate.until(LocalDate.now()).getYears();
    }

    public Profile getProfile() {
        return profile;
    }

    public String getProfileImageName() {
        return profileImageName;
    }

    public int getAgeInYears() {
        return ageInYears;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ProfileDto) obj;
        return Objects.equals(this.profile, that.profile) &&
                Objects.equals(this.profileImageName, that.profileImageName) &&
                this.ageInYears == that.ageInYears;
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, profileImageName, ageInYears);
    }

    @Override
    public String toString() {
        return "ProfileDto[" +
                "profile=" + profile + ", " +
                "profileImageName=" + profileImageName + ", " +
                "ageInYears=" + ageInYears + ']';
    }


}
