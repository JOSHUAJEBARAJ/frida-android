/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.widget.ImageView
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.internal.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

class AppCompatImageHelper {
    private static final int[] VIEW_ATTRS = new int[]{16843033};
    private final TintManager mTintManager;
    private final ImageView mView;

    AppCompatImageHelper(ImageView imageView, TintManager tintManager) {
        this.mView = imageView;
        this.mTintManager = tintManager;
    }

    void loadFromAttributes(AttributeSet object, int n) {
        object = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), (AttributeSet)object, VIEW_ATTRS, n, 0);
        try {
            if (object.hasValue(0)) {
                this.mView.setImageDrawable(object.getDrawable(0));
            }
            return;
        }
        finally {
            object.recycle();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    void setImageResource(int n) {
        ImageView imageView = this.mView;
        Drawable drawable2 = this.mTintManager != null ? this.mTintManager.getDrawable(n) : ContextCompat.getDrawable(this.mView.getContext(), n);
        imageView.setImageDrawable(drawable2);
    }
}

