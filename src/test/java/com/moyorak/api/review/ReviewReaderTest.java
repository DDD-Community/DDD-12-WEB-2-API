package com.moyorak.api.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.review.dto.FirstReviewPhotoId;
import com.moyorak.api.review.dto.FirstReviewPhotoPath;
import com.moyorak.api.review.repository.ReviewPhotoRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewReaderTest {

    @InjectMocks private ReviewReader reviewReader;

    @Mock private ReviewPhotoRepository reviewPhotoRepository;

    @Nested
    @DisplayName("첫번째 리뷰 이미지 조회시")
    class FindFirstReviewPhoto {

        private final Long teamRestaurantId = 1L;
        private final Long photoId = 100L;
        private final String photoPath = "s3://somepath/photo.jpg";

        @Test
        @DisplayName("이미지가 없는 경우 photoPath가 null인 결과를 반환한다")
        void noPhoto() {
            // given
            final List<Long> teamRestaurantIds = List.of(teamRestaurantId);

            given(
                            reviewPhotoRepository.findFirstReviewPhotoIdsByTeamRestaurantIds(
                                    teamRestaurantIds))
                    .willReturn(List.of());

            given(reviewPhotoRepository.findFirstReviewPhotoPathsByIdIn(List.of()))
                    .willReturn(List.of());

            // when
            final List<FirstReviewPhotoPath> result =
                    reviewReader.findFirstReviewSummaries(teamRestaurantIds);

            // then
            assertThat(result).hasSize(1);
            final FirstReviewPhotoPath photo = result.get(0);
            assertThat(photo.teamRestaurantId()).isEqualTo(teamRestaurantId);
            assertThat(photo.path()).isNull();
        }

        @Test
        @DisplayName("이미지가 존재하는 경우 photoPath를 포함한 결과를 반환한다")
        void hasPhoto() {
            // given
            final List<Long> teamRestaurantIds = List.of(teamRestaurantId);

            given(
                            reviewPhotoRepository.findFirstReviewPhotoIdsByTeamRestaurantIds(
                                    teamRestaurantIds))
                    .willReturn(List.of(new FirstReviewPhotoId(teamRestaurantId, photoId)));

            given(reviewPhotoRepository.findFirstReviewPhotoPathsByIdIn(List.of(photoId)))
                    .willReturn(List.of(new FirstReviewPhotoPath(teamRestaurantId, photoPath)));

            // when
            final List<FirstReviewPhotoPath> result =
                    reviewReader.findFirstReviewSummaries(teamRestaurantIds);

            // then
            assertThat(result).hasSize(1);
            final FirstReviewPhotoPath photo = result.get(0);
            assertThat(photo.teamRestaurantId()).isEqualTo(teamRestaurantId);
            assertThat(photo.path()).isEqualTo(photoPath);
        }
    }
}
