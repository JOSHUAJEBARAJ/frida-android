/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.graphics;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;

public final class BitmapCompat {
    private BitmapCompat() {
    }

    public static int getAllocationByteCount(@NonNull Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount();
        }
        return bitmap.getByteCount();
    }

    public static boolean hasMipMap(@NonNull Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 18) {
            return bitmap.hasMipMap();
        }
        return false;
    }

    public static void setHasMipMap(@NonNull Bitmap bitmap, boolean bl) {
        if (Build.VERSION.SDK_INT >= 18) {
            bitmap.setHasMipMap(bl);
        }
    }
}

