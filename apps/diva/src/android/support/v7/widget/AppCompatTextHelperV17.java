/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.TintInfo;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.widget.AppCompatTextHelper;
import android.util.AttributeSet;
import android.widget.TextView;

class AppCompatTextHelperV17
extends AppCompatTextHelper {
    private static final int[] VIEW_ATTRS_v17 = new int[]{16843666, 16843667};
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableStartTint;

    AppCompatTextHelperV17(TextView textView) {
        super(textView);
    }

    @Override
    void applyCompoundDrawablesTints() {
        super.applyCompoundDrawablesTints();
        if (this.mDrawableStartTint != null || this.mDrawableEndTint != null) {
            Drawable[] arrdrawable = this.mView.getCompoundDrawablesRelative();
            this.applyCompoundDrawableTint(arrdrawable[0], this.mDrawableStartTint);
            this.applyCompoundDrawableTint(arrdrawable[2], this.mDrawableEndTint);
        }
    }

    @Override
    void loadFromAttributes(AttributeSet attributeSet, int n) {
        super.loadFromAttributes(attributeSet, n);
        Context context = this.mView.getContext();
        TintManager tintManager = TintManager.get(context);
        attributeSet = context.obtainStyledAttributes(attributeSet, VIEW_ATTRS_v17, n, 0);
        if (attributeSet.hasValue(0)) {
            this.mDrawableStartTint = new TintInfo();
            this.mDrawableStartTint.mHasTintList = true;
            this.mDrawableStartTint.mTintList = tintManager.getTintList(attributeSet.getResourceId(0, 0));
        }
        if (attributeSet.hasValue(1)) {
            this.mDrawableEndTint = new TintInfo();
            this.mDrawableEndTint.mHasTintList = true;
            this.mDrawableEndTint.mTintList = tintManager.getTintList(attributeSet.getResourceId(1, 0));
        }
        attributeSet.recycle();
    }
}

