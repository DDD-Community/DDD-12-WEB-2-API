package com.moyorak.api.image.controller;

import com.moyorak.api.image.dto.ImageSaveRequest;
import com.moyorak.api.image.dto.ImageSaveResponse;
import com.moyorak.api.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "[이미지] 이미지 관리 API", description = "이미지 관리를 위한 API 입니다.")
class ImageController {

    private final ImageService imageService;

    @PostMapping
    @Operation(summary = "[이미지] 이미지 저장 URL 발급", description = "이미지를 저장하기 위한 URL을 발급 받습니다.")
    public ImageSaveResponse generate(@RequestBody final ImageSaveRequest request) {
        return imageService.generateUrl(request);
    }
}
