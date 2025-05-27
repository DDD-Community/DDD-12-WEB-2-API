package com.moyorak.api.sample.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SampleTest {

    @Nested
    @DisplayName("Sample을 수정할 때,")
    class modify {

        @Test
        @DisplayName("제목을 수정합니다.")
        void title() {
            // given
            final String input = "제목을 수정했습니다.";

            final String title = "제목";
            final String content = "내용";

            Sample sample = Sample.create(title, content);

            // when
            sample.modify(input, null);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(sample.getTitle()).isEqualTo(input);
                        it.assertThat(sample.getContent()).isEqualTo(content);
                    });
        }

        @Test
        @DisplayName("내용을 수정합니다.")
        void content() {
            // given
            final String input = "내용을 수정했습니다.";

            final String title = "제목";
            final String content = "내용";

            Sample sample = Sample.create(title, content);

            // when
            sample.modify(null, input);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(sample.getTitle()).isEqualTo(title);
                        it.assertThat(sample.getContent()).isEqualTo(input);
                    });
        }
    }
}
