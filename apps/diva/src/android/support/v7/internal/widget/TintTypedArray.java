/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.TypedValue
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.TintManager;
import android.util.AttributeSet;
import android.util.TypedValue;

public class TintTypedArray {
    private final Context mContext;
    private TintManager mTintManager;
    private final TypedArray mWrapped;

    private TintTypedArray(Context context, TypedArray typedArray) {
        this.mContext = context;
        this.mWrapped = typedArray;
    }

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet attributeSet, int[] arrn) {
        return new TintTypedArray(context, context.obtainStyledAttributes(attributeSet, arrn));
    }

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet attributeSet, int[] arrn, int n, int n2) {
        return new TintTypedArray(context, context.obtainStyledAttributes(attributeSet, arrn, n, n2));
    }

    public boolean getBoolean(int n, boolean bl) {
        return this.mWrapped.getBoolean(n, bl);
    }

    public int getChangingConfigurations() {
        return this.mWrapped.getChangingConfigurations();
    }

    public int getColor(int n, int n2) {
        return this.mWrapped.getColor(n, n2);
    }

    public ColorStateList getColorStateList(int n) {
        return this.mWrapped.getColorStateList(n);
    }

    public float getDimension(int n, float f) {
        return this.mWrapped.getDimension(n, f);
    }

    public int getDimensionPixelOffset(int n, int n2) {
        return this.mWrapped.getDimensionPixelOffset(n, n2);
    }

    public int getDimensionPixelSize(int n, int n2) {
        return this.mWrapped.getDimensionPixelSize(n, n2);
    }

    public Drawable getDrawable(int n) {
        int n2;
        if (this.mWrapped.hasValue(n) && (n2 = this.mWrapped.getResourceId(n, 0)) != 0) {
            return this.getTintManager().getDrawable(n2);
        }
        return this.mWrapped.getDrawable(n);
    }

    public Drawable getDrawableIfKnown(int n) {
        if (this.mWrapped.hasValue(n) && (n = this.mWrapped.getResourceId(n, 0)) != 0) {
            return this.getTintManager().getDrawable(n, true);
        }
        return null;
    }

    public float getFloat(int n, float f) {
        return this.mWrapped.getFloat(n, f);
    }

    public float getFraction(int n, int n2, int n3, float f) {
        return this.mWrapped.getFraction(n, n2, n3, f);
    }

    public int getIndex(int n) {
        return this.mWrapped.getIndex(n);
    }

    public int getIndexCount() {
        return this.mWrapped.getIndexCount();
    }

    public int getInt(int n, int n2) {
        return this.mWrapped.getInt(n, n2);
    }

    public int getInteger(int n, int n2) {
        return this.mWrapped.getInteger(n, n2);
    }

    public int getLayoutDimension(int n, int n2) {
        return this.mWrapped.getLayoutDimension(n, n2);
    }

    public int getLayoutDimension(int n, String string2) {
        return this.mWrapped.getLayoutDimension(n, string2);
    }

    public String getNonResourceString(int n) {
        return this.mWrapped.getNonResourceString(n);
    }

    public String getPositionDescription() {
        return this.mWrapped.getPositionDescription();
    }

    public int getResourceId(int n, int n2) {
        return this.mWrapped.getResourceId(n, n2);
    }

    public Resources getResources() {
        return this.mWrapped.getResources();
    }

    public String getString(int n) {
        return this.mWrapped.getString(n);
    }

    public CharSequence getText(int n) {
        return this.mWrapped.getText(n);
    }

    public CharSequence[] getTextArray(int n) {
        return this.mWrapped.getTextArray(n);
    }

    public TintManager getTintManager() {
        if (this.mTintManager == null) {
            this.mTintManager = TintManager.get(this.mContext);
        }
        return this.mTintManager;
    }

    public int getType(int n) {
        return this.mWrapped.getType(n);
    }

    public boolean getValue(int n, TypedValue typedValue) {
        return this.mWrapped.getValue(n, typedValue);
    }

    public boolean hasValue(int n) {
        return this.mWrapped.hasValue(n);
    }

    public int length() {
        return this.mWrapped.length();
    }

    public TypedValue peekValue(int n) {
        return this.mWrapped.peekValue(n);
    }

    public void recycle() {
        this.mWrapped.recycle();
    }
}

