package com.moyorak.api.auth.service;

import com.moyorak.api.auth.domain.State;
import com.moyorak.api.auth.dto.UserDailyStateRequest;
import com.moyorak.api.auth.dto.UserDailyStateResponse;
import com.moyorak.api.auth.repository.UserDailyStateRepository;
import com.moyorak.config.exception.BusinessException;
import java.time.LocalDate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDailyStateService {
    private final UserDailyStateRepository userDailyStateRepository;

    @Transactional(readOnly = true)
    public UserDailyStateResponse getDailyState(final Long userId) {
        final LocalDate today = LocalDate.now();
        return userDailyStateRepository
                .findByUserIdAndRecordDate(userId, today)
                .map(state -> UserDailyStateResponse.from(state.getState()))
                .orElseGet(() -> UserDailyStateResponse.from(State.OFF));
    }

    @Transactional
    public void updateDailyState(Long userId, UserDailyStateRequest request) {
        if (!Objects.equals(userId, request.userId())) {
            throw new BusinessException("유저 정보가 잘못됐습니다.");
        }
        final LocalDate today = LocalDate.now();
        userDailyStateRepository
                .findByUserIdAndRecordDate(userId, today)
                .ifPresentOrElse(
                        existing -> {
                            existing.changeState(request.state());
                            userDailyStateRepository.save(existing);
                        },
                        () -> userDailyStateRepository.save(request.toUserDailyState(today)));
    }
}
