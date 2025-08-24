package com.jeffyjamzhd.btj.util;

public class BTJMath {
    public static float easeOutCubic(float x) {
        return (float) (1F - Math.pow(1F - x, 3F));
    }

    public static float range(float x, float min, float max) {
        if (x < min)
            x = min;
        if (x > max)
            x = max;
        return x;
    }
}
