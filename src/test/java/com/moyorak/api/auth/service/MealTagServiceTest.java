package com.moyorak.api.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.moyorak.api.auth.domain.MealTag;
import com.moyorak.api.auth.domain.MealTagType;
import com.moyorak.api.auth.dto.MealTagDetailsSaveRequest;
import com.moyorak.api.auth.dto.MealTagSaveRequest;
import com.moyorak.api.auth.dto.MealTagTypeCount;
import com.moyorak.api.auth.repository.MealTagRepository;
import com.moyorak.config.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MealTagServiceTest {

    @InjectMocks private MealTagService mealTagService;

    @Mock private MealTagRepository mealTagRepository;

    @Nested
    @DisplayName("음식 구분을 등록할 때,")
    class foodFlagRegister {

        @Nested
        @DisplayName("새로 저장하고자 할 때, 최대 값을 초과하는지 확인할 때,")
        class isMax {

            @Test
            @DisplayName("초과한다면 오류가 발생합니다.")
            void isMore() {
                // given
                final long maxSize = MealTagSaveRequest.MAX_ITEMS_PER_TYPE;

                final Long userId = 1L;

                List<MealTagDetailsSaveRequest> details = new ArrayList<>();
                for (int i = 0; i < maxSize; i++) {
                    details.add(
                            new MealTagDetailsSaveRequest(MealTagType.ALLERGY, String.valueOf(i)));
                }

                final MealTagSaveRequest request = new MealTagSaveRequest(1L, details);

                final List<MealTagTypeCount> counts =
                        List.of(new MealTagTypeCount(MealTagType.ALLERGY, maxSize));

                given(mealTagRepository.findTypeCountByUserId(userId)).willReturn(counts);

                // when & then
                assertThatThrownBy(() -> mealTagService.foodFlagRegister(userId, request))
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("알러지 타입은 최대 10개까지만 등록 가능합니다.");
            }
        }

        @Test
        @DisplayName("이미 등록된 항목을 중복 등록하려고 하면, 중복 제거가 됩니다.")
        void removeDuplicate() {
            // given
            final Long userId = 1L;
            final String item = "계란";

            List<MealTagDetailsSaveRequest> details = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                details.add(new MealTagDetailsSaveRequest(MealTagType.ALLERGY, item));
            }

            final MealTagSaveRequest request = new MealTagSaveRequest(1L, details);

            final List<MealTagTypeCount> counts =
                    List.of(new MealTagTypeCount(MealTagType.ALLERGY, 1));

            final List<MealTag> expectedMealTagList =
                    List.of(MealTag.create(userId, MealTagType.ALLERGY, item));

            given(mealTagRepository.findTypeCountByUserId(userId)).willReturn(counts);
            given(mealTagRepository.findByUserIdAndUse(userId, true))
                    .willReturn(expectedMealTagList);

            // when
            mealTagService.foodFlagRegister(userId, request);

            // then
            then(mealTagRepository)
                    .should()
                    .saveAll(argThat((List<MealTag> list) -> list.isEmpty()));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 5, 9})
        @DisplayName("성공적으로 저장합니다.")
        void success(final int size) {
            // given
            final Long userId = 1L;

            List<MealTagDetailsSaveRequest> details = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                details.add(new MealTagDetailsSaveRequest(MealTagType.ALLERGY, String.valueOf(i)));
            }

            final MealTagSaveRequest request = new MealTagSaveRequest(1L, details);

            final List<MealTagTypeCount> counts =
                    List.of(new MealTagTypeCount(MealTagType.ALLERGY, size));

            given(mealTagRepository.findTypeCountByUserId(userId)).willReturn(counts);
            given(mealTagRepository.findByUserIdAndUse(userId, true)).willReturn(List.of());

            // when & then
            assertDoesNotThrow(() -> mealTagService.foodFlagRegister(userId, request));
        }
    }
}
