package com.moyorak.api.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.moyorak.api.auth.dto.FoodFlagTypeCount;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FoodFlagSummaryTest {

    @Nested
    @DisplayName("기존 항목과 갯수와, 더해질 항목의 갯수 값을 가져올 때,")
    class isWithinLimit {

        @Test
        @DisplayName("최대 값보다 넘어가면 false를 반환합니다.")
        void isFalse() {
            // given
            final List<FoodFlagTypeCount> counts =
                    List.of(new FoodFlagTypeCount(FoodFlagType.ALLERGY, 10L));

            final FoodFlagSummary summary = FoodFlagSummary.create(counts);

            // when
            final boolean result = summary.isWithinLimit(FoodFlagType.ALLERGY, 1L);

            // then
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 9})
        @DisplayName("최대 값과 같거나 적으면 true를 반환합니다.")
        void isTrue(final long size) {
            // given
            final List<FoodFlagTypeCount> counts =
                    List.of(new FoodFlagTypeCount(FoodFlagType.ALLERGY, size));

            final FoodFlagSummary summary = FoodFlagSummary.create(counts);

            // when
            final boolean result = summary.isWithinLimit(FoodFlagType.ALLERGY, 1L);

            // then
            assertThat(result).isTrue();
        }
    }
}
