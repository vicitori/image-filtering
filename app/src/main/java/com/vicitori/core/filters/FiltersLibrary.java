package com.vicitori.core.filters;

import com.vicitori.core.Filter;

import java.util.Map;
import java.util.Set;

public class FiltersLibrary {
    private static final Map<String, Filter> FILTERS = Map.of(
            "blur", new Filter(
                    new float[][]{
                            {0.0f, 0.0f, 1.0f, 0.0f, 0.0f},
                            {0.0f, 1.0f, 1.0f, 1.0f, 0.0f},
                            {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
                            {0.0f, 1.0f, 1.0f, 1.0f, 0.0f},
                            {0.0f, 0.0f, 1.0f, 0.0f, 0.0f}
                    },
                    1f / 13, 0.0f
            ), "motion_blur", new Filter(
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
            ), "find_edges", new Filter(
                    new float[][]{
                            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, -2.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 6.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, -2.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 0.0f, -1.0f},

                    }, 1.0f, 0.0f
            ), "sharpen", new Filter(
                    new float[][]{
                            {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f,},
                            {-1.0f, 2.0f, 2.0f, 2.0f, -1.0f,},
                            {-1.0f, 2.0f, 8.0f, 2.0f, -1.0f,},
                            {-1.0f, 2.0f, 2.0f, 2.0f, 0.0f},
                            {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f},

                    }, 1.0f / 8, 0.0f
            )
    );

    public static Filter get(String name) {
        return FILTERS.get(name.toLowerCase());
    }

    public static Set<String> getNames() {
        return FILTERS.keySet();
    }
}
