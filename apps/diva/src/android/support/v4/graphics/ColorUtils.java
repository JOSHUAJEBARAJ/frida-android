/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Color
 */
package android.support.v4.graphics;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public class ColorUtils {
    private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
    private static final int MIN_ALPHA_SEARCH_PRECISION = 1;

    private ColorUtils() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @ColorInt
    public static int HSLToColor(@NonNull float[] var0) {
        var1_1 = var0[0];
        var2_2 = var0[1];
        var3_3 = var0[2];
        var2_2 = (1.0f - Math.abs(2.0f * var3_3 - 1.0f)) * var2_2;
        var3_3 -= 0.5f * var2_2;
        var4_4 = var2_2 * (1.0f - Math.abs(var1_1 / 60.0f % 2.0f - 1.0f));
        var8_5 = (int)var1_1 / 60;
        var7_6 = 0;
        var6_7 = 0;
        var5_8 = 0;
        switch (var8_5) {
            case 0: {
                var7_6 = Math.round(255.0f * (var2_2 + var3_3));
                var6_7 = Math.round(255.0f * (var4_4 + var3_3));
                var5_8 = Math.round(255.0f * var3_3);
                ** break;
            }
            case 1: {
                var7_6 = Math.round(255.0f * (var4_4 + var3_3));
                var6_7 = Math.round(255.0f * (var2_2 + var3_3));
                var5_8 = Math.round(255.0f * var3_3);
                ** break;
            }
            case 2: {
                var7_6 = Math.round(255.0f * var3_3);
                var6_7 = Math.round(255.0f * (var2_2 + var3_3));
                var5_8 = Math.round(255.0f * (var4_4 + var3_3));
                ** break;
            }
            case 3: {
                var7_6 = Math.round(255.0f * var3_3);
                var6_7 = Math.round(255.0f * (var4_4 + var3_3));
                var5_8 = Math.round(255.0f * (var2_2 + var3_3));
                ** break;
            }
            case 4: {
                var7_6 = Math.round(255.0f * (var4_4 + var3_3));
                var6_7 = Math.round(255.0f * var3_3);
                var5_8 = Math.round(255.0f * (var2_2 + var3_3));
            }
lbl36: // 6 sources:
            default: {
                return Color.rgb((int)ColorUtils.constrain(var7_6, 0, 255), (int)ColorUtils.constrain(var6_7, 0, 255), (int)ColorUtils.constrain(var5_8, 0, 255));
            }
            case 5: 
            case 6: 
        }
        var7_6 = Math.round(255.0f * (var2_2 + var3_3));
        var6_7 = Math.round(255.0f * var3_3);
        var5_8 = Math.round(255.0f * (var4_4 + var3_3));
        return Color.rgb((int)ColorUtils.constrain(var7_6, 0, 255), (int)ColorUtils.constrain(var6_7, 0, 255), (int)ColorUtils.constrain(var5_8, 0, 255));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void RGBToHSL(@IntRange(from=0, to=255) int n, @IntRange(from=0, to=255) int n2, @IntRange(from=0, to=255) int n3, @NonNull float[] arrf) {
        float f = (float)n / 255.0f;
        float f2 = (float)n2 / 255.0f;
        float f3 = (float)n3 / 255.0f;
        float f4 = Math.max(f, Math.max(f2, f3));
        float f5 = Math.min(f, Math.min(f2, f3));
        float f6 = f4 - f5;
        float f7 = (f4 + f5) / 2.0f;
        if (f4 == f5) {
            f = 0.0f;
            f6 = 0.0f;
        } else {
            f = f4 == f ? (f2 - f3) / f6 % 6.0f : (f4 == f2 ? (f3 - f) / f6 + 2.0f : (f - f2) / f6 + 4.0f);
            f2 = f6 / (1.0f - Math.abs(2.0f * f7 - 1.0f));
            f6 = f;
            f = f2;
        }
        f6 = f2 = 60.0f * f6 % 360.0f;
        if (f2 < 0.0f) {
            f6 = f2 + 360.0f;
        }
        arrf[0] = ColorUtils.constrain(f6, 0.0f, 360.0f);
        arrf[1] = ColorUtils.constrain(f, 0.0f, 1.0f);
        arrf[2] = ColorUtils.constrain(f7, 0.0f, 1.0f);
    }

    public static double calculateContrast(@ColorInt int n, @ColorInt int n2) {
        if (Color.alpha((int)n2) != 255) {
            throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(n2));
        }
        int n3 = n;
        if (Color.alpha((int)n) < 255) {
            n3 = ColorUtils.compositeColors(n, n2);
        }
        double d = ColorUtils.calculateLuminance(n3) + 0.05;
        double d2 = ColorUtils.calculateLuminance(n2) + 0.05;
        return Math.max(d, d2) / Math.min(d, d2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @FloatRange(from=0.0, to=1.0)
    public static double calculateLuminance(@ColorInt int n) {
        double d = (double)Color.red((int)n) / 255.0;
        d = d < 0.03928 ? (d /= 12.92) : Math.pow((0.055 + d) / 1.055, 2.4);
        double d2 = (double)Color.green((int)n) / 255.0;
        d2 = d2 < 0.03928 ? (d2 /= 12.92) : Math.pow((0.055 + d2) / 1.055, 2.4);
        double d3 = (double)Color.blue((int)n) / 255.0;
        if (d3 < 0.03928) {
            d3 /= 12.92;
            return 0.2126 * d + 0.7152 * d2 + 0.0722 * d3;
        }
        d3 = Math.pow((0.055 + d3) / 1.055, 2.4);
        return 0.2126 * d + 0.7152 * d2 + 0.0722 * d3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int calculateMinimumAlpha(@ColorInt int n, @ColorInt int n2, float f) {
        if (Color.alpha((int)n2) != 255) {
            throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(n2));
        }
        if (ColorUtils.calculateContrast(ColorUtils.setAlphaComponent(n, 255), n2) < (double)f) {
            return -1;
        }
        int n3 = 0;
        int n4 = 0;
        int n5 = 255;
        do {
            int n6 = n5;
            if (n3 > 10) return n6;
            n6 = n5;
            if (n5 - n4 <= 1) return n6;
            n6 = (n4 + n5) / 2;
            if (ColorUtils.calculateContrast(ColorUtils.setAlphaComponent(n, n6), n2) < (double)f) {
                n4 = n6;
            } else {
                n5 = n6;
            }
            ++n3;
        } while (true);
    }

    public static void colorToHSL(@ColorInt int n, @NonNull float[] arrf) {
        ColorUtils.RGBToHSL(Color.red((int)n), Color.green((int)n), Color.blue((int)n), arrf);
    }

    private static int compositeAlpha(int n, int n2) {
        return 255 - (255 - n2) * (255 - n) / 255;
    }

    public static int compositeColors(@ColorInt int n, @ColorInt int n2) {
        int n3 = Color.alpha((int)n2);
        int n4 = Color.alpha((int)n);
        int n5 = ColorUtils.compositeAlpha(n4, n3);
        return Color.argb((int)n5, (int)ColorUtils.compositeComponent(Color.red((int)n), n4, Color.red((int)n2), n3, n5), (int)ColorUtils.compositeComponent(Color.green((int)n), n4, Color.green((int)n2), n3, n5), (int)ColorUtils.compositeComponent(Color.blue((int)n), n4, Color.blue((int)n2), n3, n5));
    }

    private static int compositeComponent(int n, int n2, int n3, int n4, int n5) {
        if (n5 == 0) {
            return 0;
        }
        return (n * 255 * n2 + n3 * n4 * (255 - n2)) / (n5 * 255);
    }

    private static float constrain(float f, float f2, float f3) {
        if (f < f2) {
            return f2;
        }
        if (f > f3) {
            return f3;
        }
        return f;
    }

    private static int constrain(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }

    @ColorInt
    public static int setAlphaComponent(@ColorInt int n, @IntRange(from=0, to=255) int n2) {
        if (n2 < 0 || n2 > 255) {
            throw new IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return 16777215 & n | n2 << 24;
    }
}

