package com.moyorak.infra.aws.s3;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
class S3Properties {

    private final String bucketName;

    private final String uri;

    private final String directory;

    S3Properties(
            @Value("${aws.s3.bucket}") final String bucketName,
            @Value("${aws.s3.uri}") final String uri,
            @Value("${aws.s3.directory}") final String directory) {
        this.bucketName = bucketName;
        this.uri = uri;
        this.directory = directory;
    }
}
