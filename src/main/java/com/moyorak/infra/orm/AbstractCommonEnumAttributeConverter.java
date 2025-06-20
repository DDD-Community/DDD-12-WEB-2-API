package com.moyorak.infra.orm;

import jakarta.persistence.AttributeConverter;
import java.util.Arrays;

public abstract class AbstractCommonEnumAttributeConverter<T extends Enum<T> & CommonEnum>
        implements AttributeConverter<T, String> {

    private final Class<T> enumClass;

    protected AbstractCommonEnumAttributeConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getDescription() : null;
    }

    @Override
    public T convertToEntityAttribute(String dbData) {

        if (dbData == null) {
            return null;
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(enumConstant -> enumConstant.getDescription().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid enum value: " + dbData));
    }
}
