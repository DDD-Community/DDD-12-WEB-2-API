package com.moyorak.api.sample.controller;

import com.moyorak.api.sample.dto.SampleResponse;
import com.moyorak.api.sample.dto.SampleSaveRequest;
import com.moyorak.api.sample.dto.SampleSearchRequest;
import com.moyorak.api.sample.dto.SampleUpdateRequest;
import com.moyorak.api.sample.service.SampleService;
import com.moyorak.global.domain.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "샘플 API", description = "샘플용 API 입니다.")
class SampleController {

    private final SampleService sampleService;

    @GetMapping("/{id}")
    @Operation(summary = "샘플 데이터 단건 조회", description = "샘플 데이터 단건을 조회합니다.")
    public SampleResponse getDetail(@PathVariable @Positive final Long id) {
        return sampleService.getDetail(id);
    }

    @GetMapping
    @Operation(summary = "샘플 데이터 검색", description = "샘플 데이터 리스트를 검색합니다.")
    public ListResponse<SampleResponse> search(@Valid final SampleSearchRequest request) {
        return sampleService.search(request);
    }

    @PostMapping
    @Operation(summary = "샘플 데이터 저장", description = "샘플 데이터를 저장합니다.")
    public void register(@RequestBody @Valid final SampleSaveRequest request) {
        sampleService.register(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "샘플 데이터 수정", description = "샘플 데이터를 수정합니다.")
    public void modify(
            @PathVariable @Positive final Long id,
            @RequestBody @Valid final SampleUpdateRequest request) {
        sampleService.modify(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "샘플 데이터 삭제", description = "샘플 데이터를 삭제합니다.")
    public void remove(@PathVariable @Positive final Long id) {
        sampleService.remove(id);
    }
}
