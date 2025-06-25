package com.moyorak.api.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ImageSaveRequest(
        @NotBlank @Schema(description = "파일 확장자명", example = "png") String extensionName) {

    private static final List<String> VALID_EXTENSION_NAMES = List.of("jpg", "png", "jpeg");

    @AssertTrue(message = "허용되지 않은 확장자명입니다.")
    public boolean isInvalidExtensionName(final String extensionName) {
        if (extensionName == null) {
            return false;
        }

        return VALID_EXTENSION_NAMES.contains(extensionName.toLowerCase());
    }
}
