/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.DrawableContainer
 *  android.graphics.drawable.GradientDrawable
 */
package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.graphics.drawable.DrawableCompatBase;
import android.support.v4.graphics.drawable.DrawableWrapperLollipop;

class DrawableCompatLollipop {
    DrawableCompatLollipop() {
    }

    public static void setHotspot(Drawable drawable2, float f, float f2) {
        drawable2.setHotspot(f, f2);
    }

    public static void setHotspotBounds(Drawable drawable2, int n, int n2, int n3, int n4) {
        drawable2.setHotspotBounds(n, n2, n3, n4);
    }

    public static void setTint(Drawable drawable2, int n) {
        if (drawable2 instanceof DrawableWrapperLollipop) {
            DrawableCompatBase.setTint(drawable2, n);
            return;
        }
        drawable2.setTint(n);
    }

    public static void setTintList(Drawable drawable2, ColorStateList colorStateList) {
        if (drawable2 instanceof DrawableWrapperLollipop) {
            DrawableCompatBase.setTintList(drawable2, colorStateList);
            return;
        }
        drawable2.setTintList(colorStateList);
    }

    public static void setTintMode(Drawable drawable2, PorterDuff.Mode mode) {
        if (drawable2 instanceof DrawableWrapperLollipop) {
            DrawableCompatBase.setTintMode(drawable2, mode);
            return;
        }
        drawable2.setTintMode(mode);
    }

    public static Drawable wrapForTinting(Drawable drawable2) {
        Drawable drawable3;
        block2 : {
            if (!(drawable2 instanceof GradientDrawable)) {
                drawable3 = drawable2;
                if (!(drawable2 instanceof DrawableContainer)) break block2;
            }
            drawable3 = new DrawableWrapperLollipop(drawable2);
        }
        return drawable3;
    }
}

