/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package android.support.v4.text;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Locale;

public class TextUtilsCompatJellybeanMr1 {
    public static int getLayoutDirectionFromLocale(@Nullable Locale locale) {
        return TextUtils.getLayoutDirectionFromLocale((Locale)locale);
    }

    @NonNull
    public static String htmlEncode(@NonNull String string2) {
        return TextUtils.htmlEncode((String)string2);
    }
}

