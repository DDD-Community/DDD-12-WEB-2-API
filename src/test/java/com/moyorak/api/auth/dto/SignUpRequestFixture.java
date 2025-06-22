package com.moyorak.api.auth.dto;

import com.moyorak.api.auth.domain.Gender;
import java.time.LocalDate;

public class SignUpRequestFixture {

    public static SignUpRequest fixture(final String email) {
        return new SignUpRequest(email, "", Gender.MALE, LocalDate.now(), "");
    }
}
