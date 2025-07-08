package com.moyorak.api.team.service;

import com.moyorak.api.team.domain.TeamSearch;
import com.moyorak.api.team.dto.TeamSearchListResponse;
import com.moyorak.api.team.dto.TeamSearchRequest;
import com.moyorak.api.team.repository.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public TeamSearchListResponse search(final Long companyId, final TeamSearchRequest request) {
        final List<TeamSearch> teams =
                teamRepository.findByConditions(companyId, request.teamId(), request.name());

        return TeamSearchListResponse.from(teams);
    }
}
