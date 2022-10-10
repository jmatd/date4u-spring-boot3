package com.tutego.date4u.mvc.formdata;

public class SearchFormData {
    private int minimumAge;
    private int maximumAge;
    private int minimumHornlength;
    private int maximumHornlength;
    private int gender;


    public SearchFormData() {
    }

    public SearchFormData(int minimumAge, int maximumAge, int minimumHornlength, int maximumHornlength, int gender) {
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
        this.minimumHornlength = minimumHornlength;
        this.maximumHornlength = maximumHornlength;
        this.gender = gender;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public int getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        this.maximumAge = maximumAge;
    }

    public int getMinimumHornlength() {
        return minimumHornlength;
    }

    public void setMinimumHornlength(int minimumHornlength) {
        this.minimumHornlength = minimumHornlength;
    }

    public int getMaximumHornlength() {
        return maximumHornlength;
    }

    public void setMaximumHornlength(int maximumHornlength) {
        this.maximumHornlength = maximumHornlength;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
