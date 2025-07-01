package com.moyorak.api.team.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.restaurant.domain.RestaurantCategory;
import com.moyorak.api.restaurant.domain.RestaurantFixture;
import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.domain.TeamRestaurantFixture;
import com.moyorak.api.team.domain.TeamRestaurantNotFoundException;
import com.moyorak.api.team.dto.TeamRestaurantResponse;
import com.moyorak.api.team.repository.TeamRestaurantRepository;
import com.moyorak.config.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamRestaurantServiceTest {

    @InjectMocks private TeamRestaurantService teamRestaurantService;

    @Mock private TeamRestaurantRepository teamRestaurantRepository;

    final Long teamId = 1L;
    final Long teamRestaurantId = 1L;

    @Nested
    @DisplayName("팀 맛집 상세 조회 시,")
    class GetTeamRestaurant {

        @Test
        @DisplayName("정상적으로 조회되면 응답 DTO를 반환합니다.")
        void returnTeamRestaurantResponse() {
            // given
            final Restaurant restaurant =
                    RestaurantFixture.fixture(
                            "http://place.map.kakao.com/123456",
                            "우가우가 차차차",
                            "우가우가시 차차차동 24번길",
                            "우가우가 차차로 123",
                            RestaurantCategory.KOREAN,
                            127.043616,
                            37.503095);
            final TeamRestaurant teamRestaurant =
                    TeamRestaurantFixture.fixture(
                            teamRestaurantId, "맛있네요", 4.5, 5, 5, 5.5, 5, true, teamId, restaurant);
            given(teamRestaurantRepository.findByTeamIdAndIdAndUse(teamId, teamRestaurantId, true))
                    .willReturn(Optional.of(teamRestaurant));

            // when
            final TeamRestaurantResponse response =
                    teamRestaurantService.getTeamRestaurant(teamId, teamRestaurantId);

            // then
            assertThat(response.name()).isEqualTo(teamRestaurant.getRestaurant().getName());
        }

        @Test
        @DisplayName("팀 맛집이 존재하지 않으면 예외가 발생합니다.")
        void throwsWhenTeamRestaurantNotFound() {
            // given
            given(teamRestaurantRepository.findByTeamIdAndIdAndUse(teamId, teamRestaurantId, true))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(
                            () -> teamRestaurantService.getTeamRestaurant(teamId, teamRestaurantId))
                    .isInstanceOf(TeamRestaurantNotFoundException.class);
        }

        @Test
        @DisplayName("식당이 null이면 예외가 발생합니다.")
        void throwsWhenRestaurantIsNull() {
            // given
            final TeamRestaurant teamRestaurant =
                    TeamRestaurantFixture.fixture(
                            teamRestaurantId, "맛있네요", 4.5, 5, 5, 5.5, 5, true, teamId, null);

            given(teamRestaurantRepository.findByTeamIdAndIdAndUse(teamId, teamRestaurantId, true))
                    .willReturn(Optional.of(teamRestaurant));

            // when & then
            assertThatThrownBy(
                            () -> teamRestaurantService.getTeamRestaurant(teamId, teamRestaurantId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("연결된 식당 정보가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("use_yn이 'N'인 경우 조회되지 않아 예외가 발생합니다.")
        void throwsWhenUseIsFalse() {
            // given
            final Restaurant restaurant =
                    RestaurantFixture.fixture(
                            "http://place.map.kakao.com/000000",
                            "쓰지 않는 식당",
                            "서울시 어디구",
                            "서울로 456",
                            RestaurantCategory.KOREAN,
                            127.0,
                            37.0);
            final TeamRestaurant disabled =
                    TeamRestaurantFixture.fixture(
                            teamRestaurantId,
                            "쓰지 않는 식당",
                            4.0,
                            5,
                            5,
                            5.0,
                            5,
                            false,
                            teamId,
                            restaurant);
            given(teamRestaurantRepository.findByTeamIdAndIdAndUse(teamId, teamRestaurantId, true))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(
                            () -> teamRestaurantService.getTeamRestaurant(teamId, teamRestaurantId))
                    .isInstanceOf(TeamRestaurantNotFoundException.class);
        }
    }
}
