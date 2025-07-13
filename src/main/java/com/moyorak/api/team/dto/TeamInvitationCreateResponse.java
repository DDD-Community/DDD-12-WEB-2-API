package com.moyorak.api.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "[팀 초대] 팀 초대 링크 생성 응답 DTO")
public record TeamInvitationCreateResponse(
        @Schema(description = "팀 초대 링크 URL 주소", example = "http://moyorak/invitation/abcdefg-asddf")
                String invitationUrl) {
    public static TeamInvitationCreateResponse of(String invitationUrl) {
        return new TeamInvitationCreateResponse(invitationUrl);
    }
}
