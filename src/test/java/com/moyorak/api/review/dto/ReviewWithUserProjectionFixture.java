package com.moyorak.api.review.dto;

public class ReviewWithUserProjectionFixture {

    public static ReviewWithUserProjection fixture(
            Long id,
            String extraText,
            Integer score,
            Integer servingTime,
            Integer waitingTime,
            String name,
            String profileImage) {
        return new ReviewWithUserProjection(
                id, extraText, score, servingTime, waitingTime, name, profileImage);
    }

    public static ReviewWithUserProjection defaultFixture() {
        return fixture(1L, "좋은식당", 5, 15, 2, "아무개", "/images/profile.png");
    }
}
