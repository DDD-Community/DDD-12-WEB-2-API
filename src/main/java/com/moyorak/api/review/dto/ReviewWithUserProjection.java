package com.moyorak.api.review.dto;

public record ReviewWithUserProjection(
        Long id,
        String extraText,
        Integer score,
        Integer servingTime,
        Integer waitingTime,
        String name,
        String profileImage) {}
