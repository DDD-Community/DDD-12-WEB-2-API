package com.moyorak.infra.orm;

import jakarta.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractCommonEnumAttributeConverter<T extends Enum<T> & CommonEnum>
        implements AttributeConverter<T, String> {

    private final Class<T> enumClass;
    private final Map<String, T> enumMap;

    protected AbstractCommonEnumAttributeConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
        this.enumMap =
                Arrays.stream(enumClass.getEnumConstants())
                        .collect(Collectors.toMap(CommonEnum::getDescription, e -> e));
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
        T value = enumMap.get(dbData);
        if (value == null) {
            throw new EnumConstantNotPresentException(enumClass, dbData);
        }
        return value;
    }
}
