package com.moyorak.api.auth.controller;

import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.auth.dto.UserDailyStateRequest;
import com.moyorak.api.auth.dto.UserDailyStateResponse;
import com.moyorak.api.auth.service.UserDailyStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
@SecurityRequirement(name = "JWT")
@Tag(name = "[마이] 마이페이지 API", description = "마이페이지 API 입니다.")
public class UserDailyStateController {

    private final UserDailyStateService userDailyStateService;

    @GetMapping("/meal/alone")
    @Operation(summary = "혼밥 상태 조회", description = "혼밥 상태를 조회해 옵니다.")
    public UserDailyStateResponse getMealStatus(
            @AuthenticationPrincipal final UserPrincipal userPrincipal) {
        return userDailyStateService.getDailyState(userPrincipal.getId());
    }

    @PostMapping("/meal/alone")
    @Operation(summary = "혼밥 모드 변경(on/off)", description = "혼밥 모드를 변경 합니다(on/off).")
    public void changeMealStatus(
            @AuthenticationPrincipal final UserPrincipal userPrincipal,
            @RequestBody @Valid final UserDailyStateRequest request) {
        userDailyStateService.updateDailyState(userPrincipal.getId(), request);
    }
}
