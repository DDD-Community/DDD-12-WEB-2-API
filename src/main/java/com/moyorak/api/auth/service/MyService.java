package com.moyorak.api.auth.service;

import com.moyorak.api.auth.domain.FoodFlagSummary;
import com.moyorak.api.auth.domain.FoodFlagType;
import com.moyorak.api.auth.dto.FoodFlagSaveRequest;
import com.moyorak.api.auth.repository.FoodFlagRepository;
import com.moyorak.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyService {

    private final FoodFlagRepository foodFlagRepository;

    @Transactional
    public void foodFlagRegister(final Long userId, final FoodFlagSaveRequest request) {
        // 1. 각 항목들이 최대 값을 초과하는지 확인
        final FoodFlagSummary count =
                FoodFlagSummary.create(foodFlagRepository.findTypeCountByUserId(userId));

        // 2. 저장 된 항목 갯수와, 저장하고자 하는 갯수가 최대 값을 초과하는지 확인
        for (FoodFlagType type : FoodFlagType.values()) {
            validateWithinLimit(count, request, type);
        }

        // 3. 저장
        foodFlagRepository.saveAll(request.toEntities());
    }

    private void validateWithinLimit(
            FoodFlagSummary count, FoodFlagSaveRequest request, FoodFlagType type) {
        if (!count.isWithinLimit(type, request.getCountByType(type))) {
            throw new BusinessException(
                    String.format(
                            "%s 타입은 최대 %d개까지만 등록 가능합니다.",
                            type.getDescription(), FoodFlagSummary.MAX_ITEMS_PER_TYPE));
        }
    }
}
