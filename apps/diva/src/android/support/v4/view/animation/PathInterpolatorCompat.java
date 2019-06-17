/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Path
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.animation.Interpolator
 */
package android.support.v4.view.animation;

import android.graphics.Path;
import android.os.Build;
import android.support.v4.view.animation.PathInterpolatorCompatApi21;
import android.support.v4.view.animation.PathInterpolatorCompatBase;
import android.view.animation.Interpolator;

public class PathInterpolatorCompat {
    private PathInterpolatorCompat() {
    }

    public static Interpolator create(float f, float f2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(f, f2);
        }
        return PathInterpolatorCompatBase.create(f, f2);
    }

    public static Interpolator create(float f, float f2, float f3, float f4) {
        if (Build.VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(f, f2, f3, f4);
        }
        return PathInterpolatorCompatBase.create(f, f2, f3, f4);
    }

    public static Interpolator create(Path path) {
        if (Build.VERSION.SDK_INT >= 21) {
            return PathInterpolatorCompatApi21.create(path);
        }
        return PathInterpolatorCompatBase.create(path);
    }
}

