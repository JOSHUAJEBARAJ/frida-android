/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Path
 *  android.graphics.PathMeasure
 *  android.view.animation.Interpolator
 */
package android.support.v4.view.animation;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.Interpolator;

class PathInterpolatorDonut
implements Interpolator {
    private static final float PRECISION = 0.002f;
    private final float[] mX;
    private final float[] mY;

    public PathInterpolatorDonut(float f, float f2) {
        this(PathInterpolatorDonut.createQuad(f, f2));
    }

    public PathInterpolatorDonut(float f, float f2, float f3, float f4) {
        this(PathInterpolatorDonut.createCubic(f, f2, f3, f4));
    }

    public PathInterpolatorDonut(Path path) {
        path = new PathMeasure(path, false);
        float f = path.getLength();
        int n = (int)(f / 0.002f) + 1;
        this.mX = new float[n];
        this.mY = new float[n];
        float[] arrf = new float[2];
        for (int i = 0; i < n; ++i) {
            path.getPosTan((float)i * f / (float)(n - 1), arrf, null);
            this.mX[i] = arrf[0];
            this.mY[i] = arrf[1];
        }
    }

    private static Path createCubic(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f, f2, f3, f4, 1.0f, 1.0f);
        return path;
    }

    private static Path createQuad(float f, float f2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f, f2, 1.0f, 1.0f);
        return path;
    }

    public float getInterpolation(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        int n = 0;
        int n2 = this.mX.length - 1;
        while (n2 - n > 1) {
            int n3 = (n + n2) / 2;
            if (f < this.mX[n3]) {
                n2 = n3;
                continue;
            }
            n = n3;
        }
        float f2 = this.mX[n2] - this.mX[n];
        if (f2 == 0.0f) {
            return this.mY[n];
        }
        f = (f - this.mX[n]) / f2;
        f2 = this.mY[n];
        return (this.mY[n2] - f2) * f + f2;
    }
}

