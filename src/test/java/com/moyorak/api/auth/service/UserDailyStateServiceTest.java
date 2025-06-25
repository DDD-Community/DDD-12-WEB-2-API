package com.moyorak.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.moyorak.api.auth.domain.State;
import com.moyorak.api.auth.domain.UserDailyState;
import com.moyorak.api.auth.domain.UserDailyStateFixture;
import com.moyorak.api.auth.dto.UserDailyStateRequest;
import com.moyorak.api.auth.repository.UserDailyStateRepository;
import com.moyorak.config.exception.BusinessException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserDailyStateServiceTest {

    @InjectMocks private UserDailyStateService userDailyStateService;

    @Mock private UserDailyStateRepository userDailyStateRepository;

    final Long userId = 1L;
    final LocalDate today = LocalDate.now();

    @Nested
    @DisplayName("혼밥 상태 조회 시,")
    class GetDailyState {

        @Test
        @DisplayName("저장된 혼밥 상태가 있다면 해당 상태를 반환합니다.")
        void returnExistingState() {
            // given
            given(userDailyStateRepository.findByUserIdAndRecordDate(userId, today))
                    .willReturn(
                            Optional.of(UserDailyStateFixture.fixture(userId, State.ON, today)));

            // when & then
            assertThat(userDailyStateService.getDailyState(userId).state()).isEqualTo(State.ON);
        }

        @Test
        @DisplayName("저장된 혼밥 상태가가 없다면 OFF 상태를 반환합니다.")
        void returnDefaultOffState() {
            // given
            given(userDailyStateRepository.findByUserIdAndRecordDate(userId, today))
                    .willReturn(Optional.empty());

            // when & then
            assertThat(userDailyStateService.getDailyState(userId).state()).isEqualTo(State.OFF);
        }
    }

    @Nested
    @DisplayName("혼밥 상태를 변경할 때,")
    class UpdateDailyState {

        @Test
        @DisplayName("요청한 유저 ID와 실제 유저 ID가 다르면 예외가 발생합니다.")
        void userIdMismatchThrowsException() {
            // given
            final UserDailyStateRequest request = new UserDailyStateRequest(2L, State.ON);

            // when & then
            assertThatThrownBy(() -> userDailyStateService.updateDailyState(userId, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("유저 정보가 잘못됐습니다.");
        }

        @Test
        @DisplayName("혼밥 상태가 존재하면 상태를 업데이트하고 저장합니다.")
        void updateExistingStateAndSave() {
            // given
            final UserDailyState existing = UserDailyStateFixture.fixture(userId, State.OFF, today);
            final UserDailyStateRequest request = new UserDailyStateRequest(userId, State.ON);

            given(userDailyStateRepository.findByUserIdAndRecordDate(userId, today))
                    .willReturn(Optional.of(existing));

            // when
            userDailyStateService.updateDailyState(userId, request);

            // then
            assertThat(existing.getState()).isEqualTo(State.ON);
            then(userDailyStateRepository).should().save(existing);
        }

        @Test
        @DisplayName("혼밥 상태가 없다면 새로 저장합니다.")
        void saveNewState() {
            // given
            final UserDailyStateRequest request = new UserDailyStateRequest(userId, State.ON);

            given(userDailyStateRepository.findByUserIdAndRecordDate(userId, today))
                    .willReturn(Optional.empty());

            // when
            userDailyStateService.updateDailyState(userId, request);

            // then
            then(userDailyStateRepository).should().save(any(UserDailyState.class));
        }
    }
}
