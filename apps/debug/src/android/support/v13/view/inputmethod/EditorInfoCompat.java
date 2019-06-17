/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.inputmethod.EditorInfo
 */
package android.support.v13.view.inputmethod;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.inputmethod.EditorInfo;

public final class EditorInfoCompat {
    private static final String CONTENT_MIME_TYPES_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;

    @NonNull
    public static String[] getContentMimeTypes(EditorInfo arrstring) {
        if (Build.VERSION.SDK_INT >= 25) {
            arrstring = arrstring.contentMimeTypes;
            if (arrstring != null) {
                return arrstring;
            }
            return EMPTY_STRING_ARRAY;
        }
        if (arrstring.extras == null) {
            return EMPTY_STRING_ARRAY;
        }
        arrstring = arrstring.extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
        if (arrstring != null) {
            return arrstring;
        }
        return EMPTY_STRING_ARRAY;
    }

    public static void setContentMimeTypes(@NonNull EditorInfo editorInfo, @Nullable String[] arrstring) {
        if (Build.VERSION.SDK_INT >= 25) {
            editorInfo.contentMimeTypes = arrstring;
            return;
        }
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        editorInfo.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", arrstring);
    }
}

