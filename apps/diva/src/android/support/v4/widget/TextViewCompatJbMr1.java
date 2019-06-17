/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.widget.TextView
 */
package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

class TextViewCompatJbMr1 {
    TextViewCompatJbMr1() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
        boolean bl = true;
        if (textView.getLayoutDirection() != 1) {
            bl = false;
        }
        Drawable drawable6 = bl ? drawable4 : drawable2;
        if (!bl) {
            drawable2 = drawable4;
        }
        textView.setCompoundDrawables(drawable6, drawable3, drawable2, drawable5);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, int n, int n2, int n3, int n4) {
        boolean bl = true;
        if (textView.getLayoutDirection() != 1) {
            bl = false;
        }
        int n5 = bl ? n3 : n;
        if (!bl) {
            n = n3;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(n5, n2, n, n4);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
        boolean bl = true;
        if (textView.getLayoutDirection() != 1) {
            bl = false;
        }
        Drawable drawable6 = bl ? drawable4 : drawable2;
        if (!bl) {
            drawable2 = drawable4;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable6, drawable3, drawable2, drawable5);
    }
}

