package com.moyorak.api.team.service;

import com.moyorak.api.team.dto.SearchResult;
import com.moyorak.api.team.repository.TeamRestaurantSearchNativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamRestaurantSearchService {

    private final TeamRestaurantSearchNativeRepository nativeRepository;

    @Transactional(readOnly = true)
    public SearchResult search(final Long teamId, final String keyword, Pageable pageable) {
        return nativeRepository.searchByTeamIdAndName(teamId, keyword, pageable);
    }
}
