package com.tutego.date4u.mvc.formdata;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UnicornFormData {
    @NotNull
    @Size(max=64)
    @Email
    private String email;

    @NotNull
    @Size(min=8, max=128)
    private String password;

    public UnicornFormData() {
    }

    public UnicornFormData(String emailAddress, String password) {
        this.email = emailAddress;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UnicornFormData{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
