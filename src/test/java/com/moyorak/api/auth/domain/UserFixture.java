package com.moyorak.api.auth.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    public static User fixture(
            final Long id, final String email, final String name, final String profileImage) {
        User user = new User();

        ReflectionTestUtils.setField(user, "id", id);
        ReflectionTestUtils.setField(user, "email", email);
        ReflectionTestUtils.setField(user, "name", name);
        ReflectionTestUtils.setField(user, "profileImage", profileImage);

        return user;
    }
}
