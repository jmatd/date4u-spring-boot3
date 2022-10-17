package com.tutego.date4u.mvc.dto;

import com.tutego.date4u.core.profile.Profile;

import java.time.LocalDate;
import java.util.Objects;

public final class ProfileDto {
    private final Profile profile;
    private final String profileImageName;
    private final int ageInYears;


    public ProfileDto(Profile profile, String profileImageName) {
        this.profile = profile;
        this.profileImageName = profileImageName;
        this.ageInYears = calculateAgeInYears(profile.getBirthdate());
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