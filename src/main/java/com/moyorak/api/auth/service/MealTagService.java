package com.moyorak.api.auth.service;

import com.moyorak.api.auth.domain.MealTagSummary;
import com.moyorak.api.auth.domain.MealTagType;
import com.moyorak.api.auth.dto.MealTagSaveRequest;
import com.moyorak.api.auth.repository.MealTagRepository;
import com.moyorak.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MealTagService {

    private final MealTagRepository mealTagRepository;

    @Transactional
    public void foodFlagRegister(final Long userId, final MealTagSaveRequest request) {
        // 1. 저장 된 항목 갯수와, 저장하고자 하는 갯수가 최대 값을 초과하는지 확인
        final MealTagSummary count =
                MealTagSummary.create(mealTagRepository.findTypeCountByUserId(userId));

        for (MealTagType type : MealTagType.values()) {
            validateWithinLimit(count, request, type);
        }

        // 2. 저장
        mealTagRepository.saveAll(request.toEntities());
    }

    private void validateWithinLimit(
        MealTagSummary count, MealTagSaveRequest request, MealTagType type) {
        if (!count.isWithinLimit(type, request.getCountByType(type))) {
            throw new BusinessException(
                    String.format(
                            "%s 타입은 최대 %d개까지만 등록 가능합니다.",
                            type.getDescription(), MealTagSummary.MAX_ITEMS_PER_TYPE));
        }
    }
}
