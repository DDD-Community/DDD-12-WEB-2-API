package com.moyorak.api.team.service;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.restaurant.repository.RestaurantRepository;
import com.moyorak.api.team.domain.GeoPoint;
import com.moyorak.api.team.domain.TeamPlace;
import com.moyorak.api.team.domain.TeamPlaceDistance;
import com.moyorak.api.team.domain.TeamRestaurantSearch;
import com.moyorak.api.team.domain.TeamUser;
import com.moyorak.api.team.dto.TeamPlaceSaveRequest;
import com.moyorak.api.team.repository.TeamPlaceRepository;
import com.moyorak.api.team.repository.TeamRestaurantSearchRepository;
import com.moyorak.api.team.repository.TeamUserRepository;
import com.moyorak.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamPlaceService {

    private final TeamPlaceRepository teamPlaceRepository;
    private final TeamUserRepository teamUserRepository;
    private final RestaurantRepository restaurantRepository;
    private final TeamRestaurantSearchRepository teamRestaurantSearchRepository;

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
                teamPlaceRepository
                        .findByTeamIdAndRestaurantIdAndUse(teamId, restaurantId, true)
                        .isPresent();
        if (isPresent) {
            throw new BusinessException("이미 등록된 팀 맛집입니다.");
        }

        // 거리 계산
        TeamPlaceDistance teamPlaceDistance = createTeamPlaceDistance(teamUser, restaurant);
        double distance = teamPlaceDistance.calculateDistance();

        // 팀 맛집 디비와 서치용 디비에 저장
        TeamPlace teamPlace =
                teamPlaceRepository.save(
                        teamPlaceSaveRequest.toTeamPlace(teamId, restaurantId, distance));
        teamRestaurantSearchRepository.save(TeamRestaurantSearch.from(teamPlace, restaurant));
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

    private TeamPlaceDistance createTeamPlaceDistance(TeamUser teamUser, Restaurant restaurant) {
        GeoPoint companyPoint =
                GeoPoint.of(
                        teamUser.getTeam().getCompany().getLongitude(),
                        teamUser.getTeam().getCompany().getLatitude());

        GeoPoint restaurantPoint = GeoPoint.of(restaurant.getLongitude(), restaurant.getLatitude());

        return TeamPlaceDistance.of(companyPoint, restaurantPoint);
    }
}
