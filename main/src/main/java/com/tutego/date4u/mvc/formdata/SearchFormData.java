package com.tutego.date4u.mvc.formdata;

public class SearchFormData {
    private int minimumAge = 18;
    private int maximumAge = 99;
    private int minimumHornlength = 0;
    private int maximumHornlength = 100;
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

    @Override
    public String toString() {
        return "SearchFormData{" +
                "minimumAge=" + minimumAge +
                ", maximumAge=" + maximumAge +
                ", minimumHornlength=" + minimumHornlength +
                ", maximumHornlength=" + maximumHornlength +
                ", gender=" + gender +
                '}';
    }
}
