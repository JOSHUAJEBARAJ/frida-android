/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableWrapperKitKat;

class DrawableCompatKitKat {
    DrawableCompatKitKat() {
    }

    public static boolean isAutoMirrored(Drawable drawable2) {
        return drawable2.isAutoMirrored();
    }

    public static void setAutoMirrored(Drawable drawable2, boolean bl) {
        drawable2.setAutoMirrored(bl);
    }

    public static Drawable wrapForTinting(Drawable drawable2) {
        Drawable drawable3 = drawable2;
        if (!(drawable2 instanceof DrawableWrapperKitKat)) {
            drawable3 = new DrawableWrapperKitKat(drawable2);
        }
        return drawable3;
    }
}
