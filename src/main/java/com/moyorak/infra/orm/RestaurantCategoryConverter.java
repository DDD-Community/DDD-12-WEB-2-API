package com.moyorak.infra.orm;

import com.moyorak.api.restaurant.domain.RestaurantCategory;
import jakarta.persistence.Converter;

@Converter
public class RestaurantCategoryConverter
        extends AbstractCommonEnumAttributeConverter<RestaurantCategory> {

    public RestaurantCategoryConverter() {
        super(RestaurantCategory.class);
    }
}
