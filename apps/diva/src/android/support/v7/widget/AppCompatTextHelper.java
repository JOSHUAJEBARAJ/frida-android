/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.appcompat.R;
import android.support.v7.internal.text.AllCapsTransformationMethod;
import android.support.v7.internal.widget.TintInfo;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.widget.AppCompatTextHelperV17;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

class AppCompatTextHelper {
    private static final int[] TEXT_APPEARANCE_ATTRS;
    private static final int[] VIEW_ATTRS;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    final TextView mView;

    static {
        VIEW_ATTRS = new int[]{16842804, 16843119, 16843117, 16843120, 16843118};
        TEXT_APPEARANCE_ATTRS = new int[]{R.attr.textAllCaps};
    }

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
    }

    static AppCompatTextHelper create(TextView textView) {
        if (Build.VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }

    final void applyCompoundDrawableTint(Drawable drawable2, TintInfo tintInfo) {
        if (drawable2 != null && tintInfo != null) {
            TintManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
        }
    }

    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            Drawable[] arrdrawable = this.mView.getCompoundDrawables();
            this.applyCompoundDrawableTint(arrdrawable[0], this.mDrawableLeftTint);
            this.applyCompoundDrawableTint(arrdrawable[1], this.mDrawableTopTint);
            this.applyCompoundDrawableTint(arrdrawable[2], this.mDrawableRightTint);
            this.applyCompoundDrawableTint(arrdrawable[3], this.mDrawableBottomTint);
        }
    }

    void loadFromAttributes(AttributeSet attributeSet, int n) {
        Context context = this.mView.getContext();
        TintManager tintManager = TintManager.get(context);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, VIEW_ATTRS, n, 0);
        int n2 = typedArray.getResourceId(0, -1);
        if (typedArray.hasValue(1)) {
            this.mDrawableLeftTint = new TintInfo();
            this.mDrawableLeftTint.mHasTintList = true;
            this.mDrawableLeftTint.mTintList = tintManager.getTintList(typedArray.getResourceId(1, 0));
        }
        if (typedArray.hasValue(2)) {
            this.mDrawableTopTint = new TintInfo();
            this.mDrawableTopTint.mHasTintList = true;
            this.mDrawableTopTint.mTintList = tintManager.getTintList(typedArray.getResourceId(2, 0));
        }
        if (typedArray.hasValue(3)) {
            this.mDrawableRightTint = new TintInfo();
            this.mDrawableRightTint.mHasTintList = true;
            this.mDrawableRightTint.mTintList = tintManager.getTintList(typedArray.getResourceId(3, 0));
        }
        if (typedArray.hasValue(4)) {
            this.mDrawableBottomTint = new TintInfo();
            this.mDrawableBottomTint.mHasTintList = true;
            this.mDrawableBottomTint.mTintList = tintManager.getTintList(typedArray.getResourceId(4, 0));
        }
        typedArray.recycle();
        if (n2 != -1) {
            tintManager = context.obtainStyledAttributes(n2, R.styleable.TextAppearance);
            if (tintManager.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                this.setAllCaps(tintManager.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
            }
            tintManager.recycle();
        }
        if ((attributeSet = context.obtainStyledAttributes(attributeSet, TEXT_APPEARANCE_ATTRS, n, 0)).getBoolean(0, false)) {
            this.setAllCaps(true);
        }
        attributeSet.recycle();
    }

    void onSetTextAppearance(Context context, int n) {
        if ((context = context.obtainStyledAttributes(n, TEXT_APPEARANCE_ATTRS)).hasValue(0)) {
            this.setAllCaps(context.getBoolean(0, false));
        }
        context.recycle();
    }

    /*
     * Enabled aggressive block sorting
     */
    void setAllCaps(boolean bl) {
        TextView textView = this.mView;
        AllCapsTransformationMethod allCapsTransformationMethod = bl ? new AllCapsTransformationMethod(this.mView.getContext()) : null;
        textView.setTransformationMethod((TransformationMethod)allCapsTransformationMethod);
    }
}

