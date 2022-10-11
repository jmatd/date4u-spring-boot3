package com.tutego.date4u.core.profile;

import java.time.LocalDate;

public record SearchFilter(int minHornlength, int maxHornlength,
                           int gender, Integer attractedToGender, LocalDate minBirthdate, LocalDate maxBirthdate) {
}

