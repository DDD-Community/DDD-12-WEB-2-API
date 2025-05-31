package com.moyorak.api.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.moyorak.global.domain.ListResponse;
import java.util.List;

public class ConverterHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T toDto(String request, Class<T> type) {
        try {
            return objectMapper.registerModule(new JavaTimeModule()).readValue(request, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toDtoList(String input, Class<T> type) throws Exception {
        return objectMapper.readValue(
                input, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }

    public static <T> ListResponse<T> toListResponse(String input, Class<T> type) throws Exception {
        JavaType listResponseType =
                objectMapper.getTypeFactory().constructParametricType(ListResponse.class, type);

        return objectMapper.readValue(input, listResponseType);
    }
}
