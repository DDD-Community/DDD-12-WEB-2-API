package com.moyorak.api.auth.domain;

import java.time.LocalDate;
import org.springframework.test.util.ReflectionTestUtils;

public class UserDailyStateFixture {

    public static UserDailyState fixture(
            final Long userId, final State state, final LocalDate recordDate) {
        UserDailyState dailyState = new UserDailyState();

        ReflectionTestUtils.setField(dailyState, "id", userId);
        ReflectionTestUtils.setField(dailyState, "state", state);
        ReflectionTestUtils.setField(dailyState, "recordDate", recordDate);

        return dailyState;
    }
}
