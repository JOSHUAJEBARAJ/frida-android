/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompatApi22;
import android.support.v4.graphics.drawable.DrawableCompatApi23;
import android.support.v4.graphics.drawable.DrawableCompatBase;
import android.support.v4.graphics.drawable.DrawableCompatHoneycomb;
import android.support.v4.graphics.drawable.DrawableCompatJellybeanMr1;
import android.support.v4.graphics.drawable.DrawableCompatKitKat;
import android.support.v4.graphics.drawable.DrawableCompatLollipop;
import android.support.v4.graphics.drawable.DrawableWrapper;

public class DrawableCompat {
    static final DrawableImpl IMPL;

    static {
        int n = Build.VERSION.SDK_INT;
        IMPL = n >= 23 ? new MDrawableImpl() : (n >= 22 ? new LollipopMr1DrawableImpl() : (n >= 21 ? new LollipopDrawableImpl() : (n >= 19 ? new KitKatDrawableImpl() : (n >= 17 ? new JellybeanMr1DrawableImpl() : (n >= 11 ? new HoneycombDrawableImpl() : new BaseDrawableImpl())))));
    }

    public static int getLayoutDirection(Drawable drawable2) {
        return IMPL.getLayoutDirection(drawable2);
    }

    public static boolean isAutoMirrored(Drawable drawable2) {
        return IMPL.isAutoMirrored(drawable2);
    }

    public static void jumpToCurrentState(Drawable drawable2) {
        IMPL.jumpToCurrentState(drawable2);
    }

    public static void setAutoMirrored(Drawable drawable2, boolean bl) {
        IMPL.setAutoMirrored(drawable2, bl);
    }

    public static void setHotspot(Drawable drawable2, float f, float f2) {
        IMPL.setHotspot(drawable2, f, f2);
    }

    public static void setHotspotBounds(Drawable drawable2, int n, int n2, int n3, int n4) {
        IMPL.setHotspotBounds(drawable2, n, n2, n3, n4);
    }

    public static void setLayoutDirection(Drawable drawable2, int n) {
        IMPL.setLayoutDirection(drawable2, n);
    }

    public static void setTint(Drawable drawable2, int n) {
        IMPL.setTint(drawable2, n);
    }

    public static void setTintList(Drawable drawable2, ColorStateList colorStateList) {
        IMPL.setTintList(drawable2, colorStateList);
    }

    public static void setTintMode(Drawable drawable2, PorterDuff.Mode mode) {
        IMPL.setTintMode(drawable2, mode);
    }

    public static <T extends Drawable> T unwrap(Drawable drawable2) {
        Drawable drawable3 = drawable2;
        if (drawable2 instanceof DrawableWrapper) {
            drawable3 = ((DrawableWrapper)drawable2).getWrappedDrawable();
        }
        return (T)drawable3;
    }

    public static Drawable wrap(Drawable drawable2) {
        return IMPL.wrap(drawable2);
    }

    static class BaseDrawableImpl
    implements DrawableImpl {
        BaseDrawableImpl() {
        }

        @Override
        public int getLayoutDirection(Drawable drawable2) {
            return 0;
        }

        @Override
        public boolean isAutoMirrored(Drawable drawable2) {
            return false;
        }

        @Override
        public void jumpToCurrentState(Drawable drawable2) {
        }

        @Override
        public void setAutoMirrored(Drawable drawable2, boolean bl) {
        }

        @Override
        public void setHotspot(Drawable drawable2, float f, float f2) {
        }

        @Override
        public void setHotspotBounds(Drawable drawable2, int n, int n2, int n3, int n4) {
        }

        @Override
        public void setLayoutDirection(Drawable drawable2, int n) {
        }

        @Override
        public void setTint(Drawable drawable2, int n) {
            DrawableCompatBase.setTint(drawable2, n);
        }

        @Override
        public void setTintList(Drawable drawable2, ColorStateList colorStateList) {
            DrawableCompatBase.setTintList(drawable2, colorStateList);
        }

        @Override
        public void setTintMode(Drawable drawable2, PorterDuff.Mode mode) {
            DrawableCompatBase.setTintMode(drawable2, mode);
        }

        @Override
        public Drawable wrap(Drawable drawable2) {
            return DrawableCompatBase.wrapForTinting(drawable2);
        }
    }

    static interface DrawableImpl {
        public int getLayoutDirection(Drawable var1);

        public boolean isAutoMirrored(Drawable var1);

        public void jumpToCurrentState(Drawable var1);

        public void setAutoMirrored(Drawable var1, boolean var2);

        public void setHotspot(Drawable var1, float var2, float var3);

        public void setHotspotBounds(Drawable var1, int var2, int var3, int var4, int var5);

        public void setLayoutDirection(Drawable var1, int var2);

        public void setTint(Drawable var1, int var2);

        public void setTintList(Drawable var1, ColorStateList var2);

        public void setTintMode(Drawable var1, PorterDuff.Mode var2);

        public Drawable wrap(Drawable var1);
    }

    static class HoneycombDrawableImpl
    extends BaseDrawableImpl {
        HoneycombDrawableImpl() {
        }

        @Override
        public void jumpToCurrentState(Drawable drawable2) {
            DrawableCompatHoneycomb.jumpToCurrentState(drawable2);
        }

        @Override
        public Drawable wrap(Drawable drawable2) {
            return DrawableCompatHoneycomb.wrapForTinting(drawable2);
        }
    }

    static class JellybeanMr1DrawableImpl
    extends HoneycombDrawableImpl {
        JellybeanMr1DrawableImpl() {
        }

        @Override
        public int getLayoutDirection(Drawable drawable2) {
            int n = DrawableCompatJellybeanMr1.getLayoutDirection(drawable2);
            if (n >= 0) {
                return n;
            }
            return 0;
        }

        @Override
        public void setLayoutDirection(Drawable drawable2, int n) {
            DrawableCompatJellybeanMr1.setLayoutDirection(drawable2, n);
        }
    }

    static class KitKatDrawableImpl
    extends JellybeanMr1DrawableImpl {
        KitKatDrawableImpl() {
        }

        @Override
        public boolean isAutoMirrored(Drawable drawable2) {
            return DrawableCompatKitKat.isAutoMirrored(drawable2);
        }

        @Override
        public void setAutoMirrored(Drawable drawable2, boolean bl) {
            DrawableCompatKitKat.setAutoMirrored(drawable2, bl);
        }

        @Override
        public Drawable wrap(Drawable drawable2) {
            return DrawableCompatKitKat.wrapForTinting(drawable2);
        }
    }

    static class LollipopDrawableImpl
    extends KitKatDrawableImpl {
        LollipopDrawableImpl() {
        }

        @Override
        public void setHotspot(Drawable drawable2, float f, float f2) {
            DrawableCompatLollipop.setHotspot(drawable2, f, f2);
        }

        @Override
        public void setHotspotBounds(Drawable drawable2, int n, int n2, int n3, int n4) {
            DrawableCompatLollipop.setHotspotBounds(drawable2, n, n2, n3, n4);
        }

        @Override
        public void setTint(Drawable drawable2, int n) {
            DrawableCompatLollipop.setTint(drawable2, n);
        }

        @Override
        public void setTintList(Drawable drawable2, ColorStateList colorStateList) {
            DrawableCompatLollipop.setTintList(drawable2, colorStateList);
        }

        @Override
        public void setTintMode(Drawable drawable2, PorterDuff.Mode mode) {
            DrawableCompatLollipop.setTintMode(drawable2, mode);
        }

        @Override
        public Drawable wrap(Drawable drawable2) {
            return DrawableCompatLollipop.wrapForTinting(drawable2);
        }
    }

    static class LollipopMr1DrawableImpl
    extends LollipopDrawableImpl {
        LollipopMr1DrawableImpl() {
        }

        @Override
        public Drawable wrap(Drawable drawable2) {
            return DrawableCompatApi22.wrapForTinting(drawable2);
        }
    }

    static class MDrawableImpl
    extends LollipopMr1DrawableImpl {
        MDrawableImpl() {
        }

        @Override
        public int getLayoutDirection(Drawable drawable2) {
            return DrawableCompatApi23.getLayoutDirection(drawable2);
        }

        @Override
        public void setLayoutDirection(Drawable drawable2, int n) {
            DrawableCompatApi23.setLayoutDirection(drawable2, n);
        }
    }

}

