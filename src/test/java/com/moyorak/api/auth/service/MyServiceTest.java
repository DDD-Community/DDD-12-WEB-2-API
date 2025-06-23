package com.moyorak.api.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.auth.domain.FoodFlagType;
import com.moyorak.api.auth.dto.FoodFlagDetailsSaveRequest;
import com.moyorak.api.auth.dto.FoodFlagSaveRequest;
import com.moyorak.api.auth.dto.FoodFlagTypeCount;
import com.moyorak.api.auth.repository.FoodFlagRepository;
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
class MyServiceTest {

    @InjectMocks private MyService myService;

    @Mock private FoodFlagRepository foodFlagRepository;

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
                final long maxSize = FoodFlagSaveRequest.MAX_ITEMS_PER_TYPE;

                final Long userId = 1L;

                List<FoodFlagDetailsSaveRequest> details = new ArrayList<>();
                for (int i = 0; i < maxSize; i++) {
                    details.add(
                            new FoodFlagDetailsSaveRequest(
                                    userId, FoodFlagType.ALLERGY, String.valueOf(i)));
                }

                final FoodFlagSaveRequest request = new FoodFlagSaveRequest(details);

                final List<FoodFlagTypeCount> counts =
                        List.of(new FoodFlagTypeCount(FoodFlagType.ALLERGY, maxSize));

                given(foodFlagRepository.findTypeCountByUserId(userId)).willReturn(counts);

                // when & then
                assertThatThrownBy(() -> myService.foodFlagRegister(userId, request))
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("알러지 타입은 최대 10개까지만 등록 가능합니다.");
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 5, 9})
        @DisplayName("성공적으로 저장합니다.")
        void success(final int size) {
            // given
            final Long userId = 1L;

            List<FoodFlagDetailsSaveRequest> details = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                details.add(
                        new FoodFlagDetailsSaveRequest(
                                userId, FoodFlagType.ALLERGY, String.valueOf(i)));
            }

            final FoodFlagSaveRequest request = new FoodFlagSaveRequest(details);

            final List<FoodFlagTypeCount> counts =
                    List.of(new FoodFlagTypeCount(FoodFlagType.ALLERGY, size));

            given(foodFlagRepository.findTypeCountByUserId(userId)).willReturn(counts);

            // when & then
            assertDoesNotThrow(() -> myService.foodFlagRegister(userId, request));
        }
    }
}
