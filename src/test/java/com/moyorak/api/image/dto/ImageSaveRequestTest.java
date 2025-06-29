package com.moyorak.api.image.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ImageSaveRequestTest {

    @Nested
    @DisplayName("유효한 확장명인지 확인할 때,")
    class isInvalidExtensionName {

        @ParameterizedTest
        @ValueSource(strings = {" ", "gif"})
        @NullAndEmptySource
        @DisplayName("유효한 확장명이 아니면, false를 반환합니다.")
        void isFalse(final String input) {
            // given
            final ImageSaveRequest request = new ImageSaveRequest(input);

            // when
            final boolean result = request.isInvalidExtensionName(input);

            // then
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"jpg", "jpeg", "png"})
        @DisplayName("유효한 확장명이면 true를 반환합니다.")
        void isTrue(final String input) {
            // given
            final ImageSaveRequest request = new ImageSaveRequest(input);

            // when
            final boolean result = request.isInvalidExtensionName(input);

            // then
            assertThat(result).isTrue();
        }
    }
}
