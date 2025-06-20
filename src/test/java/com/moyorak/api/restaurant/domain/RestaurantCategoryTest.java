package com.moyorak.api.restaurant.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.moyorak.infra.orm.AbstractCommonEnumAttributeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantCategoryTest {

    private final AbstractCommonEnumAttributeConverter<RestaurantCategory> converter =
            new RestaurantCategory.Convert();

    @Test
    @DisplayName("문자열로 컨버팅을 성공합니다.")
    void convertToDatabaseColumn() {
        assertEquals("한식", converter.convertToDatabaseColumn(RestaurantCategory.KOREAN));
    }

    @Test
    @DisplayName("이넘으로 컨버팅을 성공합니다.")
    void convertToEntityAttribute() {
        assertEquals(RestaurantCategory.KOREAN, converter.convertToEntityAttribute("한식"));
    }
}
