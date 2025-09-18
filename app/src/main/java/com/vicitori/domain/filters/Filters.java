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
                    1f / 13, 0.0f
            ), "motion_blur", new FilterProfile(
                    new float[][]{
                            {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f},
                    }, 1.0f / 9, 0.0f
            ), "find_edges", new FilterProfile(
                    new float[][]{
                            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, -2.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 6.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, -2.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 0.0f, -1.0f},

                    }, 1.0f, 0.0f
            ), "sharpen", new FilterProfile(
                    new float[][]{
                            {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f,},
                            {-1.0f, 2.0f, 2.0f, 2.0f, -1.0f,},
                            {-1.0f, 2.0f, 8.0f, 2.0f, -1.0f,},
                            {-1.0f, 2.0f, 2.0f, 2.0f, 0.0f},
                            {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f},

                    }, 1.0f / 8, 0.0f
            )
    );

    public static FilterProfile get(String name) {
        return FILTERS.get(name.toLowerCase());
    }

    public static float[][] getKernel(String name) {
        FilterProfile filter = FILTERS.get(name.toLowerCase());
        return filter != null ? filter.kernel() : null;
    }

    public static Set<String> getNames() {
        return FILTERS.keySet();
    }
}
