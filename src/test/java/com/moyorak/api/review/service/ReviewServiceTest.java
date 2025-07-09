package com.moyorak.api.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.review.dto.ReviewWithUserProjection;
import com.moyorak.api.review.dto.ReviewWithUserProjectionFixture;
import com.moyorak.api.review.repository.ReviewRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks private ReviewService reviewService;

    @Mock private ReviewRepository reviewRepository;

    @Test
    @DisplayName("팀 레스토랑 ID로 리뷰와 사용자 정보를 조회한다")
    void getReviewWithUserByTeamRestaurantId() {
        // given
        final Long teamRestaurantId = 1L;
        final Pageable pageable = PageRequest.of(0, 10);

        final ReviewWithUserProjection reviewWithUserProjection =
                ReviewWithUserProjectionFixture.defaultFixture();
        final Page<ReviewWithUserProjection> mockPage =
                new PageImpl<>(List.of(reviewWithUserProjection), pageable, 1);

        given(reviewRepository.findReviewWithUserByTeamRestaurantId(teamRestaurantId, pageable))
                .willReturn(mockPage);

        // when
        final Page<ReviewWithUserProjection> result =
                reviewService.getReviewWithUserByTeamRestaurantId(teamRestaurantId, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().name()).isEqualTo("아무개");
        assertThat(result.getContent().getFirst().extraText()).isEqualTo("좋은식당");
    }
}
