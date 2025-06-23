package com.moyorak.api.auth.controller;

import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.auth.dto.FoodFlagSaveRequest;
import com.moyorak.api.auth.service.MyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "[회원] [토큰] 토큰 관리 API", description = "토큰 관리를 위한 API 입니다.")
class MyController {

    private final MyService myService;

    // 알러지, 비선호 음식을 `MyController` 내에서 관리하는게 맞을지 궁금합니다.

    @PostMapping("/food-flag")
    public void foodFlagRegister(
            @AuthenticationPrincipal final UserPrincipal userPrincipal,
            @RequestBody @Valid final FoodFlagSaveRequest request) {
        myService.foodFlagRegister(userPrincipal.getId(), request);
    }
}
