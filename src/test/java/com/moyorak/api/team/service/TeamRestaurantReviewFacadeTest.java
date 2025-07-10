package com.moyorak.api.team.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.restaurant.domain.RestaurantCategory;
import com.moyorak.api.restaurant.domain.RestaurantFixture;
import com.moyorak.api.review.domain.ReviewPhotoPaths;
import com.moyorak.api.review.dto.ReviewPhotoPath;
import com.moyorak.api.review.dto.ReviewWithUserProjection;
import com.moyorak.api.review.dto.ReviewWithUserProjectionFixture;
import com.moyorak.api.review.service.ReviewPhotoService;
import com.moyorak.api.review.service.ReviewService;
import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.domain.TeamRestaurantFixture;
import com.moyorak.api.team.dto.TeamRestaurantReviewRequest;
import com.moyorak.api.team.dto.TeamRestaurantReviewRequestFixture;
import com.moyorak.api.team.dto.TeamRestaurantReviewResponse;
import com.moyorak.global.domain.ListResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class TeamRestaurantReviewFacadeTest {

    @InjectMocks private TeamRestaurantReviewFacade teamRestaurantReviewFacade;

    @Mock private ReviewService reviewService;

    @Mock private TeamRestaurantService teamRestaurantService;

    @Mock private ReviewPhotoService reviewPhotoService;

    @Nested
    @DisplayName("팀 맛집 리뷰 조회 시")
    class GetReview {
        @Test
        @DisplayName("성공 하면 리뷰를 반환한다")
        void getTeamRestaurantReviewsSuccess() {
            // given
            Long teamId = 1L;
            Long teamRestaurantId = 1L;
            TeamRestaurantReviewRequest request = TeamRestaurantReviewRequestFixture.fixture(1, 10);
            final Restaurant restaurant =
                    RestaurantFixture.fixture(
                            "http://place.map.kakao.com/000000",
                            "식당",
                            "서울시 어디구",
                            "서울로 456",
                            RestaurantCategory.KOREAN,
                            127.0,
                            37.0);
            final TeamRestaurant teamRestaurant =
                    TeamRestaurantFixture.fixture(
                            teamRestaurantId, "팀 식당", 4.0, 5, 5, 5.0, 5, true, teamId, restaurant);
            given(teamRestaurantService.getValidatedTeamRestaurant(teamId, teamRestaurantId))
                    .willReturn(teamRestaurant);

            final ReviewWithUserProjection review =
                    ReviewWithUserProjectionFixture.defaultFixture();

            final Page<ReviewWithUserProjection> reviewPage =
                    new PageImpl<>(List.of(review), PageRequest.of(0, 10), 1);

            given(
                            reviewService.getReviewWithUserByTeamRestaurantId(
                                    teamRestaurantId, request.toPageableAndDateSorted()))
                    .willReturn(reviewPage);

            final List<Long> reviewIds = List.of(1L);
            final List<ReviewPhotoPath> photoPaths =
                    List.of(new ReviewPhotoPath(1L, "s3://review1/photo1.jpg"));

            final ReviewPhotoPaths reviewPhotoPaths = ReviewPhotoPaths.create(photoPaths);

            given(reviewPhotoService.getReviewPhotoPaths(reviewIds)).willReturn(reviewPhotoPaths);

            // when
            final ListResponse<TeamRestaurantReviewResponse> result =
                    teamRestaurantReviewFacade.getTeamRestaurantReviews(
                            teamId, teamRestaurantId, request);

            // then
            assertThat(result.getData()).hasSize(1);

            final TeamRestaurantReviewResponse teamRestaurantReviewResponse =
                    result.getData().getFirst();
            assertThat(teamRestaurantReviewResponse.id()).isEqualTo(1L);
            assertThat(teamRestaurantReviewResponse.photoUrls())
                    .contains("s3://review1/photo1.jpg");
        }
    }
}
