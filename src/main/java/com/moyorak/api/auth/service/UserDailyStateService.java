package com.moyorak.api.auth.service;

import com.moyorak.api.auth.domain.State;
import com.moyorak.api.auth.dto.UserDailyStateRequest;
import com.moyorak.api.auth.dto.UserDailyStateResponse;
import com.moyorak.api.auth.repository.UserDailyStateRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDailyStateService {
    private final UserDailyStateRepository userDailyStateRepository;

    @Transactional(readOnly = true)
    public UserDailyStateResponse getDailyState(final Long userId) {
        LocalDate today = LocalDate.now();

        return userDailyStateRepository
                .findByUserIdAndRecordDate(userId, today)
                .map(state -> UserDailyStateResponse.from(state.getState()))
                .orElseGet(() -> UserDailyStateResponse.from(State.OFF));
    }

    @Transactional
    public void updateDailyState(Long userId, UserDailyStateRequest request) {
        userDailyStateRepository
                .findByUserIdAndRecordDate(userId, request.recordDate())
                .ifPresentOrElse(
                        existing -> existing.changeState(request.state()),
                        () -> userDailyStateRepository.save(request.toUserDailyState()));
    }
}
