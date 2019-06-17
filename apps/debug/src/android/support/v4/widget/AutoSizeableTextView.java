/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.widget;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public interface AutoSizeableTextView {
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final boolean PLATFORM_SUPPORTS_AUTOSIZE;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 27;
        PLATFORM_SUPPORTS_AUTOSIZE = bl;
    }

    public int getAutoSizeMaxTextSize();

    public int getAutoSizeMinTextSize();

    public int getAutoSizeStepGranularity();

    public int[] getAutoSizeTextAvailableSizes();

    public int getAutoSizeTextType();

    public void setAutoSizeTextTypeUniformWithConfiguration(int var1, int var2, int var3, int var4) throws IllegalArgumentException;

    public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] var1, int var2) throws IllegalArgumentException;

    public void setAutoSizeTextTypeWithDefaults(int var1);
}

