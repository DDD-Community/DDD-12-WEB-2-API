package com.moyorak.api.auth.controller;

import com.moyorak.api.auth.dto.SignInRequest;
import com.moyorak.api.auth.dto.SignInResponse;
import com.moyorak.api.auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
@Tag(name = "[회원] [토큰] 토큰 관리 API", description = "토큰 관리를 위한 API 입니다.")
class TokenController {

    private final TokenService tokenService;

    @PostMapping
    @Operation(summary = "[로그인] 토큰 발급", description = "로그인을 하여 토큰을 발급 받습니다.")
    public SignInResponse generateToken(@RequestBody final SignInRequest request) {
        return tokenService.generate(request.userId());
    }
}
