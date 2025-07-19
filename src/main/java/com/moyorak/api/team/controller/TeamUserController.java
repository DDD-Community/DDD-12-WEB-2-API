package com.moyorak.api.team.controller;

import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.team.service.TeamUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "[팀 멤버] 팀 멤버 API", description = "팀 멤버 관련 API입니다.")
class TeamUserController {

    private final TeamUserService teamUserService;

    @DeleteMapping("/teams/{teamId}/team-members/me")
    @Operation(summary = "팀 탈퇴", description = "팀을 탈퇴합니다.")
    public void withdraw(
            @PathVariable @Positive final Long teamId,
            @AuthenticationPrincipal final UserPrincipal userPrincipal) {
        teamUserService.withdraw(userPrincipal.getId(), teamId);
    }
}
