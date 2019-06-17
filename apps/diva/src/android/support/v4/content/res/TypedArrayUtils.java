/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 */
package android.support.v4.content.res;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnyRes;
import android.support.annotation.StyleableRes;

public class TypedArrayUtils {
    public static boolean getBoolean(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2, boolean bl) {
        return typedArray.getBoolean(n, typedArray.getBoolean(n2, bl));
    }

    public static Drawable getDrawable(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2) {
        Drawable drawable2;
        Drawable drawable3 = drawable2 = typedArray.getDrawable(n);
        if (drawable2 == null) {
            drawable3 = typedArray.getDrawable(n2);
        }
        return drawable3;
    }

    public static int getInt(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2, int n3) {
        return typedArray.getInt(n, typedArray.getInt(n2, n3));
    }

    @AnyRes
    public static int getResourceId(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2, @AnyRes int n3) {
        return typedArray.getResourceId(n, typedArray.getResourceId(n2, n3));
    }

    public static String getString(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2) {
        String string2;
        String string3 = string2 = typedArray.getString(n);
        if (string2 == null) {
            string3 = typedArray.getString(n2);
        }
        return string3;
    }

    public static CharSequence[] getTextArray(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2) {
        CharSequence[] arrcharSequence;
        CharSequence[] arrcharSequence2 = arrcharSequence = typedArray.getTextArray(n);
        if (arrcharSequence == null) {
            arrcharSequence2 = typedArray.getTextArray(n2);
        }
        return arrcharSequence2;
    }
}

