package com.moyorak.api.team.service;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.restaurant.repository.RestaurantRepository;
import com.moyorak.api.team.domain.GeoPoint;
import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.domain.TeamRestaurantDistance;
import com.moyorak.api.team.domain.TeamRestaurantNotFoundException;
import com.moyorak.api.team.domain.TeamRestaurantSearch;
import com.moyorak.api.team.domain.TeamUser;
import com.moyorak.api.team.dto.TeamPlaceSaveRequest;
import com.moyorak.api.team.dto.TeamRestaurantResponse;
import com.moyorak.api.team.repository.TeamRestaurantRepository;
import com.moyorak.api.team.repository.TeamRestaurantSearchRepository;
import com.moyorak.api.team.repository.TeamUserRepository;
import com.moyorak.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamRestaurantService {
    private final TeamRestaurantRepository teamRestaurantRepository;
    private final TeamUserRepository teamUserRepository;
    private final RestaurantRepository restaurantRepository;
    private final TeamRestaurantSearchRepository teamRestaurantSearchRepository;

    @Transactional(readOnly = true)
    public TeamRestaurantResponse getTeamRestaurant(Long teamId, Long teamRestaurantId) {
        final TeamRestaurant teamRestaurant =
                teamRestaurantRepository
                        .findByTeamIdAndIdAndUse(teamId, teamRestaurantId, true)
                        .orElseThrow(TeamRestaurantNotFoundException::new);
        if (teamRestaurant.isRestaurantNull()) {
            throw new BusinessException("연결된 식당 정보가 존재하지 않습니다.");
        }

        return TeamRestaurantResponse.from(teamRestaurant);
    }

    @Transactional
    public void save(
            Long userId,
            Long teamId,
            Long restaurantId,
            TeamPlaceSaveRequest teamPlaceSaveRequest) {

        // 팀원인지 확인
        TeamUser teamUser = validateTeamUser(userId, teamId);

        // 식당 조회
        Restaurant restaurant =
                restaurantRepository
                        .findByIdAndUse(restaurantId, true)
                        .orElseThrow(() -> new BusinessException("식당이 존재하지 않습니다."));

        // 팀 맛집에 있는지 체크
        boolean isPresent =
                teamRestaurantRepository
                        .findByTeamIdAndRestaurantIdAndUse(teamId, restaurantId, true)
                        .isPresent();
        if (isPresent) {
            throw new BusinessException("이미 등록된 팀 맛집입니다.");
        }

        // 거리 계산
        TeamRestaurantDistance teamRestaurantDistance =
                createTeamPlaceDistance(teamUser, restaurant);
        double distance = teamRestaurantDistance.calculateDistance();

        // 팀 맛집 디비와 서치용 디비에 저장
        TeamRestaurant TeamRestaurant =
                teamRestaurantRepository.save(
                        teamPlaceSaveRequest.toTeamRestaurant(teamId, restaurant, distance));
        teamRestaurantSearchRepository.save(TeamRestaurantSearch.from(TeamRestaurant, restaurant));
    }

    private TeamUser validateTeamUser(Long userId, Long teamId) {
        TeamUser teamUser =
                teamUserRepository
                        .findWithTeamAndCompany(teamId, userId, true)
                        .orElseThrow(() -> new BusinessException("팀원이 아닙니다."));

        if (teamUser.isNotApproved()) {
            throw new BusinessException("팀원이 아닙니다.");
        }
        return teamUser;
    }

    private TeamRestaurantDistance createTeamPlaceDistance(
            TeamUser teamUser, Restaurant restaurant) {
        GeoPoint companyPoint =
                GeoPoint.of(
                        teamUser.getTeam().getCompany().getLongitude(),
                        teamUser.getTeam().getCompany().getLatitude());

        GeoPoint restaurantPoint = GeoPoint.of(restaurant.getLongitude(), restaurant.getLatitude());

        return TeamRestaurantDistance.of(companyPoint, restaurantPoint);
    }
}
