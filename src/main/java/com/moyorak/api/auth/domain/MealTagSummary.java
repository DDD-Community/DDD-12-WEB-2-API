package com.moyorak.api.auth.domain;

import com.moyorak.api.auth.dto.MealTagTypeCount;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealTagSummary {

    public static final long MAX_ITEMS_PER_TYPE = 10L;

    private final Map<MealTagType, Long> typeCounts;

    private MealTagSummary(final List<MealTagTypeCount> counts) {
        this.typeCounts =
                counts.stream()
                        .collect(Collectors.toMap(MealTagTypeCount::type, MealTagTypeCount::count));
    }

    public static MealTagSummary create(final List<MealTagTypeCount> counts) {
        return new MealTagSummary(counts);
    }

    public boolean isWithinLimit(final MealTagType type, long additional) {
        return (getCount(type) + additional) <= MAX_ITEMS_PER_TYPE;
    }

    private long getCount(final MealTagType type) {
        return typeCounts.getOrDefault(type, 0L);
    }
}
