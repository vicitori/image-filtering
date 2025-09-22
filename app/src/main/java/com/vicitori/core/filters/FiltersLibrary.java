package com.vicitori.core.filters;

import com.vicitori.core.Filter;

import java.util.Map;
import java.util.Set;

public class FiltersLibrary {
    private static final Map<String, Filter> FILTERS = Map.of(
            "motion_blur", new Filter(
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
            ), "radial_blur", new Filter(
                    new float[][]{
                            {0.0f, 0.1f, 0.0f},
                            {0.1f, 0.6f, 0.1f},
                            {0.0f, 0.1f, 0.0f}
                    }, 1.0f, 0.0f
            ),"blur", new Filter(
                    new float[][]{
                            {1.0f, 4.0f, 6.0f, 4.0f, 1.0f},
                            {4.0f, 16.0f, 24.0f, 16.0f, 4.0f},
                            {6.0f, 24.0f, 36.0f, 24.0f, 6.0f},
                            {4.0f, 16.0f, 24.0f, 16.0f, 4.0f},
                            {1.0f, 4.0f, 6.0f, 4.0f, 1.0f}
                    }, 1.0f / 256, 0.0f
            ),"emboss", new Filter(
                    new float[][]{
                            {-2.0f, -1.0f, 0.0f},
                            {-1.0f, 1.0f, 1.0f},
                            {0.0f, 1.0f, 2.0f}
                    }, 0.7f, 128.0f
            ),"find_edges", new Filter(
                    new float[][]{
                            {-1.0f, 0.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, -2.0f, 0.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 6.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, -2.0f, 0.0f},
                            {0.0f, 0.0f, 0.0f, 0.0f, -1.0f},

                    }, 1.0f, 0.0f
            ),"glass_distortion", new Filter(
                    new float[][]{
                            {0.0f, 1.0f, 0.0f},
                            {1.0f, -4.0f, 1.0f},
                            {0.0f, 1.0f, 0.0f}
                    }, 1.5f, 128.0f
            ),"negative", new Filter(
                    new float[][]{
                            {-1.0f}
                    }, 0.9f, 255.0f
            ),"pixelate", new Filter(
                    new float[][]{
                            {1.0f, 1.0f, 0.0f, 0.0f},
                            {1.0f, 1.0f, 0.0f, 0.0f},
                            {0.0f, 0.0f, 1.0f, 1.0f},
                            {0.0f, 0.0f, 1.0f, 1.0f}
                    }, 0.15f, 0.0f
            ));

    public static Filter get(String name) {
        return FILTERS.get(name.toLowerCase());
    }

    public static Set<String> getNames() {
        return FILTERS.keySet();
    }
}
