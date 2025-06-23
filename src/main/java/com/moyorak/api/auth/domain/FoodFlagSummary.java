package com.moyorak.api.auth.domain;

import com.moyorak.api.auth.dto.FoodFlagTypeCount;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FoodFlagSummary {

    public static final long MAX_ITEMS_PER_TYPE = 10L;

    private final Map<FoodFlagType, Long> typeCounts;

    private FoodFlagSummary(final List<FoodFlagTypeCount> counts) {
        this.typeCounts =
                counts.stream()
                        .collect(
                                Collectors.toMap(
                                        FoodFlagTypeCount::type, FoodFlagTypeCount::count));
    }

    public static FoodFlagSummary create(final List<FoodFlagTypeCount> counts) {
        return new FoodFlagSummary(counts);
    }

    public boolean isWithinLimit(final FoodFlagType type, long additional) {
        return (getCount(type) + additional) <= MAX_ITEMS_PER_TYPE;
    }

    private long getCount(final FoodFlagType type) {
        return typeCounts.getOrDefault(type, 0L);
    }
}
