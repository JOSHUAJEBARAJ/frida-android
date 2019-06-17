/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.graphics.drawable.DrawableUtils;
import android.support.v7.internal.widget.TintInfo;
import android.support.v7.internal.widget.TintManager;
import android.util.AttributeSet;
import android.view.View;

class AppCompatBackgroundHelper {
    private TintInfo mBackgroundTint;
    private TintInfo mInternalBackgroundTint;
    private final TintManager mTintManager;
    private final View mView;

    AppCompatBackgroundHelper(View view, TintManager tintManager) {
        this.mView = view;
        this.mTintManager = tintManager;
    }

    /*
     * Enabled aggressive block sorting
     */
    void applySupportBackgroundTint() {
        Drawable drawable2 = this.mView.getBackground();
        if (drawable2 == null) return;
        {
            if (this.mBackgroundTint != null) {
                TintManager.tintDrawable(drawable2, this.mBackgroundTint, this.mView.getDrawableState());
                return;
            } else {
                if (this.mInternalBackgroundTint == null) return;
                {
                    TintManager.tintDrawable(drawable2, this.mInternalBackgroundTint, this.mView.getDrawableState());
                    return;
                }
            }
        }
    }

    ColorStateList getSupportBackgroundTintList() {
        if (this.mBackgroundTint != null) {
            return this.mBackgroundTint.mTintList;
        }
        return null;
    }

    PorterDuff.Mode getSupportBackgroundTintMode() {
        if (this.mBackgroundTint != null) {
            return this.mBackgroundTint.mTintMode;
        }
        return null;
    }

    void loadFromAttributes(AttributeSet attributeSet, int n) {
        attributeSet = this.mView.getContext().obtainStyledAttributes(attributeSet, R.styleable.ViewBackgroundHelper, n, 0);
        try {
            ColorStateList colorStateList;
            if (attributeSet.hasValue(R.styleable.ViewBackgroundHelper_android_background) && (colorStateList = this.mTintManager.getTintList(attributeSet.getResourceId(R.styleable.ViewBackgroundHelper_android_background, -1))) != null) {
                this.setInternalBackgroundTint(colorStateList);
            }
            if (attributeSet.hasValue(R.styleable.ViewBackgroundHelper_backgroundTint)) {
                ViewCompat.setBackgroundTintList(this.mView, attributeSet.getColorStateList(R.styleable.ViewBackgroundHelper_backgroundTint));
            }
            if (attributeSet.hasValue(R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
                ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(attributeSet.getInt(R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), null));
            }
            return;
        }
        finally {
            attributeSet.recycle();
        }
    }

    void onSetBackgroundDrawable(Drawable drawable2) {
        this.setInternalBackgroundTint(null);
    }

    /*
     * Enabled aggressive block sorting
     */
    void onSetBackgroundResource(int n) {
        ColorStateList colorStateList = this.mTintManager != null ? this.mTintManager.getTintList(n) : null;
        this.setInternalBackgroundTint(colorStateList);
    }

    /*
     * Enabled aggressive block sorting
     */
    void setInternalBackgroundTint(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.mInternalBackgroundTint == null) {
                this.mInternalBackgroundTint = new TintInfo();
            }
            this.mInternalBackgroundTint.mTintList = colorStateList;
            this.mInternalBackgroundTint.mHasTintList = true;
        } else {
            this.mInternalBackgroundTint = null;
        }
        this.applySupportBackgroundTint();
    }

    void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintList = colorStateList;
        this.mBackgroundTint.mHasTintList = true;
        this.applySupportBackgroundTint();
    }

    void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintMode = mode;
        this.mBackgroundTint.mHasTintMode = true;
        this.applySupportBackgroundTint();
    }
}

