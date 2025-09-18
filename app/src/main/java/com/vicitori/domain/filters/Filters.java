package com.vicitori.domain.filters;

import java.util.Map;
import java.util.Set;

public class Filters {
    private static final Map<String, FilterProfile> FILTERS = Map.of(
            "blur", new FilterProfile(
                    new float[][]{
                            {0.0f, 0.0f, 1.0f, 0.0f, 0.0f},
                            {0.0f, 1.0f, 1.0f, 1.0f, 0.0f},
                            {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
                            {0.0f, 1.0f, 1.0f, 1.0f, 0.0f},
                            {0.0f, 0.0f, 1.0f, 0.0f, 0.0f}
                    },
                    1f / 13,
                    0.0f
            )
    );

    public static FilterProfile getFilter(String name) {
        return FILTERS.get(name.toLowerCase());
    }

    public static float[][] getKernel(String name) {
        FilterProfile filter = FILTERS.get(name.toLowerCase());
        return filter != null ? filter.getKernel() : null;
    }

    public static Set<String> getFilterNames() {
        return FILTERS.keySet();
    }
}
