package com.moyorak.api.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "[팀 초대] 팀 초대 링크 조회 응답 DTO")
public record TeamInvitationDetailResponse(
        @Schema(description = "회사 고유 ID", example = "1") Long companyId,
        @Schema(description = "회사 이름", example = "(주) 우가우가") String companyName,
        @Schema(description = "팀 고유 ID", example = "2") Long teamId,
        @Schema(description = "팀 이름", example = "차차차본부") String teamName) {}
