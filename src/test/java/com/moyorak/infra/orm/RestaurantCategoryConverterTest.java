package com.moyorak.infra.orm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.moyorak.api.restaurant.domain.RestaurantCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RestaurantCategoryConverterTest {

    private final RestaurantCategoryConverter converter = new RestaurantCategoryConverter();

    @Nested
    @DisplayName("테이블 컬럼으로 변환 시")
    class CovertToTableColumn {

        @Test
        @DisplayName("enum을 문자열로 정상적으로 변환한다.")
        void success() {
            // Given
            RestaurantCategory category = RestaurantCategory.KOREAN;

            // When
            String dbValue = converter.convertToDatabaseColumn(category);

            // Then
            assertThat(dbValue).isEqualTo("한식");
        }
    }

    @Nested
    @DisplayName("entity 필드로 변환 시")
    class ConvertToEnum {

        @Test
        @DisplayName("문자열을 enum으로 정상적으로 변환한다.")
        void success() {
            // Given
            String dbData = "한식";

            // When
            RestaurantCategory result = converter.convertToEntityAttribute(dbData);

            // Then
            assertThat(result).isEqualTo(RestaurantCategory.KOREAN);
        }

        @Test
        @DisplayName("존재하지 않는 문자열이면 예외를 던진다.")
        void IsNotMatched() {
            // Given
            String dbData = "우가우가";

            // When & Then
            assertThatThrownBy(() -> converter.convertToEntityAttribute(dbData))
                    .isInstanceOf(EnumConstantNotPresentException.class);
        }
    }
}
