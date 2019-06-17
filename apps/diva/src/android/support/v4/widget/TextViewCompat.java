/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.widget.TextView
 */
package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompatDonut;
import android.support.v4.widget.TextViewCompatJb;
import android.support.v4.widget.TextViewCompatJbMr1;
import android.support.v4.widget.TextViewCompatJbMr2;
import android.widget.TextView;

public class TextViewCompat {
    static final TextViewCompatImpl IMPL;

    static {
        int n = Build.VERSION.SDK_INT;
        IMPL = n >= 18 ? new JbMr2TextViewCompatImpl() : (n >= 17 ? new JbMr1TextViewCompatImpl() : (n >= 16 ? new JbTextViewCompatImpl() : new BaseTextViewCompatImpl()));
    }

    private TextViewCompat() {
    }

    public static int getMaxLines(@NonNull TextView textView) {
        return IMPL.getMaxLines(textView);
    }

    public static int getMinLines(@NonNull TextView textView) {
        return IMPL.getMinLines(textView);
    }

    public static void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
        IMPL.setCompoundDrawablesRelative(textView, drawable2, drawable3, drawable4, drawable5);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, int n, int n2, int n3, int n4) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, n, n2, n3, n4);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable2, drawable3, drawable4, drawable5);
    }

    static class BaseTextViewCompatImpl
    implements TextViewCompatImpl {
        BaseTextViewCompatImpl() {
        }

        @Override
        public int getMaxLines(TextView textView) {
            return TextViewCompatDonut.getMaxLines(textView);
        }

        @Override
        public int getMinLines(TextView textView) {
            return TextViewCompatDonut.getMinLines(textView);
        }

        @Override
        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            textView.setCompoundDrawables(drawable2, drawable3, drawable4, drawable5);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, int n, int n2, int n3, int n4) {
            textView.setCompoundDrawablesWithIntrinsicBounds(n, n2, n3, n4);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        }
    }

    static class JbMr1TextViewCompatImpl
    extends JbTextViewCompatImpl {
        JbMr1TextViewCompatImpl() {
        }

        @Override
        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            TextViewCompatJbMr1.setCompoundDrawablesRelative(textView, drawable2, drawable3, drawable4, drawable5);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, int n, int n2, int n3, int n4) {
            TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, n, n2, n3, n4);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable2, drawable3, drawable4, drawable5);
        }
    }

    static class JbMr2TextViewCompatImpl
    extends JbMr1TextViewCompatImpl {
        JbMr2TextViewCompatImpl() {
        }

        @Override
        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            TextViewCompatJbMr2.setCompoundDrawablesRelative(textView, drawable2, drawable3, drawable4, drawable5);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, int n, int n2, int n3, int n4) {
            TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, n, n2, n3, n4);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable2, drawable3, drawable4, drawable5);
        }
    }

    static class JbTextViewCompatImpl
    extends BaseTextViewCompatImpl {
        JbTextViewCompatImpl() {
        }

        @Override
        public int getMaxLines(TextView textView) {
            return TextViewCompatJb.getMaxLines(textView);
        }

        @Override
        public int getMinLines(TextView textView) {
            return TextViewCompatJb.getMinLines(textView);
        }
    }

    static interface TextViewCompatImpl {
        public int getMaxLines(TextView var1);

        public int getMinLines(TextView var1);

        public void setCompoundDrawablesRelative(@NonNull TextView var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4, @Nullable Drawable var5);

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var1, int var2, int var3, int var4, int var5);

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView var1, @Nullable Drawable var2, @Nullable Drawable var3, @Nullable Drawable var4, @Nullable Drawable var5);
    }

}

