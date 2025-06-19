package com.moyorak.api.auth.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class UserTokenFixture {

    public static UserToken fixture(final Long userId, final String accessToken) {
        return fixture(null, userId, accessToken, null);
    }

    public static UserToken fixture(
            final Long id, final Long userId, final String accessToken, final String refreshToken) {
        UserToken userToken = new UserToken();

        ReflectionTestUtils.setField(userToken, "id", id);
        ReflectionTestUtils.setField(userToken, "userId", userId);
        ReflectionTestUtils.setField(userToken, "accessToken", accessToken);
        ReflectionTestUtils.setField(userToken, "refreshToken", refreshToken);

        return userToken;
    }
}
