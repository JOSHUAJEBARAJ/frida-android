/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompatApi21;
import android.support.v4.content.res.ResourcesCompatApi23;
import android.support.v4.content.res.ResourcesCompatIcsMr1;

public class ResourcesCompat {
    @Nullable
    public static Drawable getDrawable(@NonNull Resources resources, @DrawableRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return ResourcesCompatApi21.getDrawable(resources, n, theme);
        }
        return resources.getDrawable(n);
    }

    @Nullable
    public static Drawable getDrawableForDensity(@NonNull Resources resources, @DrawableRes int n, int n2, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return ResourcesCompatApi21.getDrawableForDensity(resources, n, n2, theme);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return ResourcesCompatIcsMr1.getDrawableForDensity(resources, n, n2);
        }
        return resources.getDrawable(n);
    }

    @ColorInt
    public int getColor(@NonNull Resources resources, @ColorRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return ResourcesCompatApi23.getColor(resources, n, theme);
        }
        return resources.getColor(n);
    }

    @Nullable
    public ColorStateList getColorStateList(@NonNull Resources resources, @ColorRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return ResourcesCompatApi23.getColorStateList(resources, n, theme);
        }
        return resources.getColorStateList(n);
    }
}

