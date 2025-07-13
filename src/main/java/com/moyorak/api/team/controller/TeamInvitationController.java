package com.moyorak.api.team.controller;

import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.team.dto.TeamInvitationCreateResponse;
import com.moyorak.api.team.service.TeamInvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "[팀 초대] 팀 초대 API", description = "팀 초대를 위한 API 입니다.")
class TeamInvitationController {

    private final TeamInvitationService teamInvitationService;

    @PostMapping("/teams/{teamId}/invitation")
    @Operation(summary = "팀 초대 링크 생성", description = "팀 초대를 위한 링크를 생성합니다.")
    public TeamInvitationCreateResponse createTeamInvitation(
            @PathVariable @Positive final Long teamId,
            @AuthenticationPrincipal final UserPrincipal userPrincipal) {
        return teamInvitationService.createTeamInvitation(teamId, userPrincipal.getId());
    }
}
