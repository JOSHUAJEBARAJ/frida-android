/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.method.PasswordTransformationMethod
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.AutoSizeableTextView;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextViewAutoSizeHelper;
import android.support.v7.widget.TintInfo;
import android.support.v7.widget.TintTypedArray;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import java.lang.ref.WeakReference;

class AppCompatTextHelper {
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private boolean mAsyncFontPending;
    @NonNull
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableStartTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mStyle = 0;
    private final TextView mView;

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
    }

    private void applyCompoundDrawableTint(Drawable drawable2, TintInfo tintInfo) {
        if (drawable2 != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
        }
    }

    private static TintInfo createTintInfo(Context context, AppCompatDrawableManager object, int n) {
        if ((context = object.getTintList(context, n)) != null) {
            object = new TintInfo();
            object.mHasTintList = true;
            object.mTintList = context;
            return object;
        }
        return null;
    }

    private void setTextSizeInternal(int n, float f) {
        this.mAutoSizeTextHelper.setTextSizeInternal(n, f);
    }

    private void updateTypefaceAndStyle(Context object, TintTypedArray tintTypedArray) {
        int n;
        this.mStyle = tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
        boolean bl = tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily);
        boolean bl2 = false;
        if (!bl && !tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_typeface)) {
                this.mAsyncFontPending = false;
                int n2 = tintTypedArray.getInt(R.styleable.TextAppearance_android_typeface, 1);
                if (n2 != 1) {
                    if (n2 != 2) {
                        if (n2 != 3) {
                            return;
                        }
                        this.mFontTypeface = Typeface.MONOSPACE;
                        return;
                    }
                    this.mFontTypeface = Typeface.SERIF;
                    return;
                }
                this.mFontTypeface = Typeface.SANS_SERIF;
            }
            return;
        }
        this.mFontTypeface = null;
        n = tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily) ? R.styleable.TextAppearance_fontFamily : R.styleable.TextAppearance_android_fontFamily;
        if (!object.isRestricted()) {
            block11 : {
                object = new ResourcesCompat.FontCallback(new WeakReference<TextView>(this.mView)){
                    final /* synthetic */ WeakReference val$textViewWeak;

                    @Override
                    public void onFontRetrievalFailed(int n) {
                    }

                    @Override
                    public void onFontRetrieved(@NonNull Typeface typeface) {
                        AppCompatTextHelper.this.onAsyncTypefaceReceived(this.val$textViewWeak, typeface);
                    }
                };
                this.mFontTypeface = tintTypedArray.getFont(n, this.mStyle, (ResourcesCompat.FontCallback)object);
                if (this.mFontTypeface != null) break block11;
                bl2 = true;
            }
            try {
                this.mAsyncFontPending = bl2;
            }
            catch (Resources.NotFoundException notFoundException) {
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                // empty catch block
            }
        }
        if (this.mFontTypeface == null && (object = tintTypedArray.getString(n)) != null) {
            this.mFontTypeface = Typeface.create((String)object, (int)this.mStyle);
        }
    }

    void applyCompoundDrawablesTints() {
        Drawable[] arrdrawable;
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            arrdrawable = this.mView.getCompoundDrawables();
            this.applyCompoundDrawableTint(arrdrawable[0], this.mDrawableLeftTint);
            this.applyCompoundDrawableTint(arrdrawable[1], this.mDrawableTopTint);
            this.applyCompoundDrawableTint(arrdrawable[2], this.mDrawableRightTint);
            this.applyCompoundDrawableTint(arrdrawable[3], this.mDrawableBottomTint);
        }
        if (Build.VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
            arrdrawable = this.mView.getCompoundDrawablesRelative();
            this.applyCompoundDrawableTint(arrdrawable[0], this.mDrawableStartTint);
            this.applyCompoundDrawableTint(arrdrawable[2], this.mDrawableEndTint);
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }

    int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    @SuppressLint(value={"NewApi"})
    void loadFromAttributes(AttributeSet object, int n) {
        Context context = this.mView.getContext();
        int[] arrn = AppCompatDrawableManager.get();
        Object object2 = TintTypedArray.obtainStyledAttributes(context, (AttributeSet)object, R.styleable.AppCompatTextHelper, n, 0);
        int n2 = object2.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)arrn, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)arrn, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)arrn, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)arrn, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        if (Build.VERSION.SDK_INT >= 17) {
            if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
                this.mDrawableStartTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)arrn, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
            }
            if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
                this.mDrawableEndTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)arrn, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
            }
        }
        object2.recycle();
        boolean bl = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean bl2 = false;
        boolean bl3 = false;
        int n3 = 0;
        int n4 = 0;
        Object var16_12 = null;
        object2 = null;
        TintTypedArray tintTypedArray = null;
        Object object3 = null;
        arrn = null;
        Object object4 = null;
        ColorStateList colorStateList = null;
        ColorStateList colorStateList2 = null;
        if (n2 != -1) {
            TintTypedArray tintTypedArray2 = TintTypedArray.obtainStyledAttributes(context, n2, R.styleable.TextAppearance);
            bl2 = bl3;
            n3 = n4;
            if (!bl) {
                bl2 = bl3;
                n3 = n4;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                    bl2 = tintTypedArray2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                    n3 = 1;
                }
            }
            this.updateTypefaceAndStyle(context, tintTypedArray2);
            object2 = var16_12;
            colorStateList = colorStateList2;
            if (Build.VERSION.SDK_INT < 23) {
                arrn = tintTypedArray;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColor)) {
                    arrn = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColor);
                }
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                    object4 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                }
                object2 = arrn;
                object3 = object4;
                colorStateList = colorStateList2;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                    colorStateList = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                    object3 = object4;
                    object2 = arrn;
                }
            }
            tintTypedArray2.recycle();
            arrn = object3;
        }
        tintTypedArray = TintTypedArray.obtainStyledAttributes(context, (AttributeSet)object, R.styleable.TextAppearance, n, 0);
        bl3 = bl2;
        n4 = n3;
        if (!bl) {
            bl3 = bl2;
            n4 = n3;
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                n4 = 1;
                bl3 = tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            }
        }
        object3 = object2;
        object4 = arrn;
        colorStateList2 = colorStateList;
        if (Build.VERSION.SDK_INT < 23) {
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
                object2 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                arrn = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
            object3 = object2;
            object4 = arrn;
            colorStateList2 = colorStateList;
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                colorStateList2 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                object4 = arrn;
                object3 = object2;
            }
        }
        if (Build.VERSION.SDK_INT >= 28 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle(context, tintTypedArray);
        tintTypedArray.recycle();
        if (object3 != null) {
            this.mView.setTextColor((ColorStateList)object3);
        }
        if (object4 != null) {
            this.mView.setHintTextColor((ColorStateList)object4);
        }
        if (colorStateList2 != null) {
            this.mView.setLinkTextColor(colorStateList2);
        }
        if (!bl && n4 != 0) {
            this.setAllCaps(bl3);
        }
        if ((arrn = this.mFontTypeface) != null) {
            this.mView.setTypeface((Typeface)arrn, this.mStyle);
        }
        this.mAutoSizeTextHelper.loadFromAttributes((AttributeSet)object, n);
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0 && (arrn = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes()).length > 0) {
            if ((float)this.mView.getAutoSizeStepGranularity() != -1.0f) {
                this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
                this.mView.setAutoSizeTextTypeUniformWithPresetSizes(arrn, 0);
            }
        }
        object = TintTypedArray.obtainStyledAttributes(context, (AttributeSet)object, R.styleable.AppCompatTextView);
        n = object.getDimensionPixelSize(R.styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
        n3 = object.getDimensionPixelSize(R.styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
        n4 = object.getDimensionPixelSize(R.styleable.AppCompatTextView_lineHeight, -1);
        object.recycle();
        if (n != -1) {
            TextViewCompat.setFirstBaselineToTopHeight(this.mView, n);
        }
        if (n3 != -1) {
            TextViewCompat.setLastBaselineToBottomHeight(this.mView, n3);
        }
        if (n4 != -1) {
            TextViewCompat.setLineHeight(this.mView, n4);
        }
    }

    void onAsyncTypefaceReceived(WeakReference<TextView> textView, Typeface typeface) {
        if (this.mAsyncFontPending) {
            this.mFontTypeface = typeface;
            if ((textView = textView.get()) != null) {
                textView.setTypeface(typeface, this.mStyle);
            }
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            this.autoSizeText();
        }
    }

    void onSetTextAppearance(Context context, int n) {
        ColorStateList colorStateList;
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, n, R.styleable.TextAppearance);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build.VERSION.SDK_INT < 23 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor) && (colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor)) != null) {
            this.mView.setTextColor(colorStateList);
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle(context, tintTypedArray);
        tintTypedArray.recycle();
        context = this.mFontTypeface;
        if (context != null) {
            this.mView.setTypeface((Typeface)context, this.mStyle);
        }
    }

    void setAllCaps(boolean bl) {
        this.mView.setAllCaps(bl);
    }

    void setAutoSizeTextTypeUniformWithConfiguration(int n, int n2, int n3, int n4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
    }

    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] arrn, int n) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(arrn, n);
    }

    void setAutoSizeTextTypeWithDefaults(int n) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(n);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void setTextSize(int n, float f) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !this.isAutoSizeEnabled()) {
            this.setTextSizeInternal(n, f);
        }
    }

}

