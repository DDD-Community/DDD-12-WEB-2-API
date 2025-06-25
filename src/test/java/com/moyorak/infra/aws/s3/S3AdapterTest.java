package com.moyorak.infra.aws.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

class S3AdapterTest {

    private final S3Properties s3Properties =
            new S3Properties("moyorak", "http://localhost/", "stg/");

    private final S3Adapter s3Adapter = new S3Adapter(mock(S3Presigner.class), s3Properties);

    @Nested
    @DisplayName("path 생성할 때,")
    class createFilePath {

        @Test
        @DisplayName("성공적으로 생성합니다.")
        void success() {
            // given
            final LocalDateTime now = LocalDateTime.of(2025, 6, 25, 11, 0, 0);
            final String extensionName = "jpg";
            final String uuid = "UUID";

            final String expectedResult = "stg/20250625/UUID-11_00_00.jpg";

            // when
            final String result = s3Adapter.createFilePath(now, uuid, extensionName);

            // then
            assertThat(result).isEqualTo(expectedResult);
        }
    }
}
