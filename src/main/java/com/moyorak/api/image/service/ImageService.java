package com.moyorak.api.image.service;

import com.moyorak.api.image.dto.ImageDeleteRequest;
import com.moyorak.api.image.dto.ImageSaveRequest;
import com.moyorak.api.image.dto.ImageSaveResponse;
import com.moyorak.config.exception.BusinessException;
import com.moyorak.infra.aws.s3.S3Adapter;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Adapter s3Adapter;

    public ImageSaveResponse generateUrl(final ImageSaveRequest request) {
        final String uuid = UUID.randomUUID().toString();
        final String path =
                s3Adapter.createFilePath(LocalDateTime.now(), uuid, request.extensionName());
        final String url = s3Adapter.createPreSignUrl(path, request.extensionName());

        return ImageSaveResponse.from(url, path);
    }

    public void remove(final Long userId, final ImageDeleteRequest request) {
        if (!userId.equals(request.userId())) {
            throw new BusinessException("사용자 ID가 일치하지 않습니다.");
        }

        s3Adapter.delete(request.path());
    }
}
