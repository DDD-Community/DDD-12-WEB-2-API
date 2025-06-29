package com.moyorak.infra.aws.s3;

import com.moyorak.config.exception.BusinessException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Adapter {

    public static final Duration IMAGE_URL_EXPIRATION = Duration.ofMinutes(3);

    private final S3Presigner s3Presigner;
    private final S3Properties s3Properties;

    /**
     * S3를 이용하여 이미지 업로드를 위한 Presigned URL을 생성합니다. URL의 유효시간은 3분입니다.
     *
     * @param path S3에 저장될 파일의 경로
     * @param extensionName 파일의 확장자 이름 (예: "jpg", "png")
     * @return Presigned URL
     */
    public String createPreSignUrl(final String path, final String extensionName) {
        if (isValidName(path)) {
            throw new BusinessException("path 값이 유효하지 않습니다.");
        }

        final String contentType = String.format("image/%s", extensionName);

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(s3Properties.getBucketName())
                        .key(path)
                        .contentType(contentType)
                        .build();

        PutObjectPresignRequest putObjectPresignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(IMAGE_URL_EXPIRATION)
                        .putObjectRequest(request)
                        .build();

        try {
            PresignedPutObjectRequest presignedPutObjectRequest =
                    s3Presigner.presignPutObject(putObjectPresignRequest);

            return presignedPutObjectRequest.url().toExternalForm();
        } catch (RuntimeException e) {
            throw new BusinessException("PrisignedUrl 생성에 실패하였습니다.", e);
        }
    }

    /**
     * 파일이 저장 될 path를 생성합니다.
     *
     * @param now 파일명의 중복을 최소화 하기 위한 장치
     * @param extensionName 확장자명
     * @return path
     */
    public String createFilePath(
            final LocalDateTime now, final String uuid, final String extensionName) {
        final String date = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        final String time = now.format(DateTimeFormatter.ofPattern("HH_mm_ss"));

        return String.format(
                "%s%s/%s-%s.%s", s3Properties.getDirectory(), date, uuid, time, extensionName);
    }

    /** 입력값의 유효성 검증을 합니다. null, 빈 문자열(''), 공백 문자열(' ')의 경우 false를 반환합니다. */
    private boolean isValidName(final String value) {
        return value == null || ObjectUtils.isEmpty(value.trim());
    }
}
