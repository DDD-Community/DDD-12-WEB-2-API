package com.moyorak.api.auth.dto;

import com.moyorak.api.auth.domain.Gender;
import com.moyorak.api.auth.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(title = "[회원] 회원가입 요청 DTO")
public record SignUpRequest(
        @NotBlank @Schema(description = "이메일", example = "gildong@gmail.com") String email,
        @NotBlank @Schema(description = "회원 이름", example = "홍길동") String name,
        @NotNull @Schema(description = "회원 성별", example = "FEMALE") Gender gender,
        @NotNull @Schema(description = "회원 생일", example = "1990-11-07") LocalDate birthday,
        @NotNull @Schema(description = "프로필 이미지 URL", example = "http://...") String profileImage

        // TODO: 알러지, 비선호, 회사 정보, 팀 정보가 개발되면 추가가 필요합니다.
        ) {

    public User toEntity() {
        return User.registeredUser(email, name, gender, birthday, profileImage);
    }
}
