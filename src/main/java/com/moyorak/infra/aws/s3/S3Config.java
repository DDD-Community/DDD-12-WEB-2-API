package com.moyorak.infra.aws.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
class S3Config {

    @Bean
    public S3Presigner s3PresignerConfig() {
        return S3Presigner.builder().region(Region.AP_NORTHEAST_2).build();
    }

    @Bean
    public S3Client s3ClientConfing() {
        return S3Client.builder().region(Region.AP_NORTHEAST_2).build();
    }
}
