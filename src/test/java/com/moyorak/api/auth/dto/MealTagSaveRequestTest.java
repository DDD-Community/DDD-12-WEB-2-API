package com.moyorak.api.auth.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.moyorak.api.auth.domain.MealTagType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class MealTagSaveRequestTest {

    @Test
    @DisplayName("음식 구분 객체 생성시, 중복 데이터가 제거 된 상태로 생성 됩니다.")
    void isNotDuplicate() {
        // given
        final MealTagDetailsSaveRequest detail =
                new MealTagDetailsSaveRequest(1L, MealTagType.ALLERGY, "우유");

        // when
        final MealTagSaveRequest result = new MealTagSaveRequest(List.of(detail, detail));

        // then
        assertThat(result.details()).size().isEqualTo(1);
    }

    @Nested
    @DisplayName("타입별 최대 아이템 입력 갯수를 체크할 때,")
    class isTypeItemCountValid {

        @Test
        @DisplayName("빈 값의 경우, true를 반환합니다.")
        void isNull() {
            // given
            final MealTagSaveRequest request = new MealTagSaveRequest(null);

            // when
            final boolean result = request.isTypeItemCountValid();

            // then
            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @EnumSource(MealTagType.class)
        @DisplayName("최대 입력 수인 10개를 초과하는 경우, false를 반환합니다.")
        void isInvalidItemSize(final MealTagType input) {
            // given
            final MealTagSaveRequest request =
                    new MealTagSaveRequest(
                            List.of(
                                    new MealTagDetailsSaveRequest(1L, input, "1"),
                                    new MealTagDetailsSaveRequest(1L, input, "2"),
                                    new MealTagDetailsSaveRequest(1L, input, "3"),
                                    new MealTagDetailsSaveRequest(1L, input, "4"),
                                    new MealTagDetailsSaveRequest(1L, input, "5"),
                                    new MealTagDetailsSaveRequest(1L, input, "6"),
                                    new MealTagDetailsSaveRequest(1L, input, "7"),
                                    new MealTagDetailsSaveRequest(1L, input, "8"),
                                    new MealTagDetailsSaveRequest(1L, input, "9"),
                                    new MealTagDetailsSaveRequest(1L, input, "10"),
                                    new MealTagDetailsSaveRequest(1L, input, "11")));

            // when
            final boolean result = request.isTypeItemCountValid();

            // then
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @CsvSource(value = {"3, DISLIKE", "9, ALLERGY"})
        @DisplayName("적거나 같은 경우에는 true를 반환합니다.")
        void success(final int size, final String inputType) {
            // given
            final MealTagType type = MealTagType.valueOf(MealTagType.class, inputType);

            List<MealTagDetailsSaveRequest> details = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                details.add(new MealTagDetailsSaveRequest(1L, type, String.valueOf(i)));
            }

            final MealTagSaveRequest request = new MealTagSaveRequest(details);

            // when
            final boolean result = request.isTypeItemCountValid();

            // then
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("입력 된 항목의 갯수를 성공적으로 가져옵니다.")
    void getCountByType() {
        // given
        final long expectedResult = 1L;

        final MealTagSaveRequest request =
                new MealTagSaveRequest(
                        List.of(new MealTagDetailsSaveRequest(1L, MealTagType.ALLERGY, "1")));

        // when
        final long result = request.getCountByType(MealTagType.ALLERGY);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }
}
