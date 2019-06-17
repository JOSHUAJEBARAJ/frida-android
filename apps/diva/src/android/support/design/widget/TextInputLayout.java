/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Typeface
 *  android.text.Editable
 *  android.text.TextUtils
 *  android.text.TextWatcher
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.widget.EditText
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.CollapsingTextHelper;
import android.support.design.widget.ThemeUtils;
import android.support.design.widget.ValueAnimatorCompat;
import android.support.design.widget.ViewUtils;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.internal.widget.TintManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class TextInputLayout
extends LinearLayout {
    private static final int ANIMATION_DURATION = 200;
    private static final int INVALID_MAX_LENGTH = -1;
    private ValueAnimatorCompat mAnimator;
    private final CollapsingTextHelper mCollapsingTextHelper;
    private boolean mCounterEnabled;
    private int mCounterMaxLength;
    private int mCounterOverflowTextAppearance;
    private boolean mCounterOverflowed;
    private int mCounterTextAppearance;
    private TextView mCounterView;
    private ColorStateList mDefaultTextColor;
    private EditText mEditText;
    private boolean mErrorEnabled;
    private boolean mErrorShown;
    private int mErrorTextAppearance;
    private TextView mErrorView;
    private ColorStateList mFocusedTextColor;
    private CharSequence mHint;
    private boolean mHintAnimationEnabled;
    private LinearLayout mIndicatorArea;
    private Paint mTmpPaint;

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet);
        this.mCollapsingTextHelper = new CollapsingTextHelper((View)this);
        ThemeUtils.checkAppCompatTheme(context);
        this.setOrientation(1);
        this.setWillNotDraw(false);
        this.setAddStatesFromChildren(true);
        this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        this.mCollapsingTextHelper.setPositionInterpolator((Interpolator)new AccelerateInterpolator());
        this.mCollapsingTextHelper.setCollapsedTextGravity(8388659);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.TextInputLayout, n, R.style.Widget_Design_TextInputLayout);
        this.setHint(context.getText(R.styleable.TextInputLayout_android_hint));
        this.mHintAnimationEnabled = context.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
        if (context.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
            attributeSet = context.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
            this.mFocusedTextColor = attributeSet;
            this.mDefaultTextColor = attributeSet;
        }
        if (context.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
            this.setHintTextAppearance(context.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
        }
        this.mErrorTextAppearance = context.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
        boolean bl = context.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
        boolean bl2 = context.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
        this.setCounterMaxLength(context.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
        this.mCounterTextAppearance = context.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
        this.mCounterOverflowTextAppearance = context.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
        context.recycle();
        this.setErrorEnabled(bl);
        this.setCounterEnabled(bl2);
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setAccessibilityDelegate((View)this, new TextInputAccessibilityDelegate());
    }

    private void addIndicator(TextView textView, int n, LinearLayout.LayoutParams layoutParams) {
        if (this.mIndicatorArea == null) {
            this.mIndicatorArea = new LinearLayout(this.getContext());
            this.mIndicatorArea.setOrientation(0);
            this.addView((View)this.mIndicatorArea);
            if (this.mEditText != null) {
                this.adjustIndicatorPadding();
            }
        }
        this.mIndicatorArea.addView((View)textView, n, (ViewGroup.LayoutParams)layoutParams);
    }

    private void adjustIndicatorPadding() {
        ViewCompat.setPaddingRelative((View)this.mIndicatorArea, ViewCompat.getPaddingStart((View)this.mEditText), 0, ViewCompat.getPaddingEnd((View)this.mEditText), this.mEditText.getPaddingBottom());
    }

    private void animateToExpansionFraction(float f) {
        if (this.mCollapsingTextHelper.getExpansionFraction() == f) {
            return;
        }
        if (this.mAnimator == null) {
            this.mAnimator = ViewUtils.createAnimator();
            this.mAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
            this.mAnimator.setDuration(200);
            this.mAnimator.setUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener(){

                @Override
                public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                    TextInputLayout.this.mCollapsingTextHelper.setExpansionFraction(valueAnimatorCompat.getAnimatedFloatValue());
                }
            });
        }
        this.mAnimator.setFloatValues(this.mCollapsingTextHelper.getExpansionFraction(), f);
        this.mAnimator.start();
    }

    private static boolean arrayContains(int[] arrn, int n) {
        int n2 = arrn.length;
        for (int i = 0; i < n2; ++i) {
            if (arrn[i] != n) continue;
            return true;
        }
        return false;
    }

    private void collapseHint(boolean bl) {
        if (this.mAnimator != null && this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (bl && this.mHintAnimationEnabled) {
            this.animateToExpansionFraction(1.0f);
            return;
        }
        this.mCollapsingTextHelper.setExpansionFraction(1.0f);
    }

    private void expandHint(boolean bl) {
        if (this.mAnimator != null && this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (bl && this.mHintAnimationEnabled) {
            this.animateToExpansionFraction(0.0f);
            return;
        }
        this.mCollapsingTextHelper.setExpansionFraction(0.0f);
    }

    private int getThemeAttrColor(int n) {
        TypedValue typedValue = new TypedValue();
        if (this.getContext().getTheme().resolveAttribute(n, typedValue, true)) {
            return typedValue.data;
        }
        return -65281;
    }

    private void removeIndicator(TextView textView) {
        this.mIndicatorArea.removeView((View)textView);
        if (this.mIndicatorArea.getChildCount() == 0) {
            this.removeView((View)this.mIndicatorArea);
        }
    }

    private void setEditText(EditText editText) {
        if (this.mEditText != null) {
            throw new IllegalArgumentException("We already have an EditText, can only have one");
        }
        this.mEditText = editText;
        this.mCollapsingTextHelper.setTypefaces(this.mEditText.getTypeface());
        this.mCollapsingTextHelper.setExpandedTextSize(this.mEditText.getTextSize());
        this.mCollapsingTextHelper.setExpandedTextGravity(this.mEditText.getGravity());
        this.mEditText.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable editable) {
                TextInputLayout.this.updateLabelVisibility(true);
                if (TextInputLayout.this.mCounterEnabled) {
                    TextInputLayout.this.updateCounter(editable.length());
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
            }

            public void onTextChanged(CharSequence charSequence, int n, int n2, int n3) {
            }
        });
        if (this.mDefaultTextColor == null) {
            this.mDefaultTextColor = this.mEditText.getHintTextColors();
        }
        if (TextUtils.isEmpty((CharSequence)this.mHint)) {
            this.setHint(this.mEditText.getHint());
            this.mEditText.setHint(null);
        }
        if (this.mCounterView != null) {
            this.updateCounter(this.mEditText.getText().length());
        }
        if (this.mIndicatorArea != null) {
            this.adjustIndicatorPadding();
        }
        this.updateLabelVisibility(false);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateCounter(int n) {
        boolean bl = this.mCounterOverflowed;
        if (this.mCounterMaxLength == -1) {
            this.mCounterView.setText((CharSequence)String.valueOf(n));
            this.mCounterOverflowed = false;
        } else {
            boolean bl2 = n > this.mCounterMaxLength;
            this.mCounterOverflowed = bl2;
            if (bl != this.mCounterOverflowed) {
                TextView textView = this.mCounterView;
                Context context = this.getContext();
                int n2 = this.mCounterOverflowed ? this.mCounterOverflowTextAppearance : this.mCounterTextAppearance;
                textView.setTextAppearance(context, n2);
            }
            this.mCounterView.setText((CharSequence)this.getContext().getString(R.string.character_counter_pattern, new Object[]{n, this.mCounterMaxLength}));
        }
        if (this.mEditText != null && bl != this.mCounterOverflowed) {
            this.updateEditTextBackground();
        }
    }

    private void updateEditTextBackground() {
        if (this.mErrorShown && this.mErrorView != null) {
            ViewCompat.setBackgroundTintList((View)this.mEditText, ColorStateList.valueOf((int)this.mErrorView.getCurrentTextColor()));
            return;
        }
        if (this.mCounterOverflowed && this.mCounterView != null) {
            ViewCompat.setBackgroundTintList((View)this.mEditText, ColorStateList.valueOf((int)this.mCounterView.getCurrentTextColor()));
            return;
        }
        TintManager tintManager = TintManager.get(this.getContext());
        ViewCompat.setBackgroundTintList((View)this.mEditText, tintManager.getTintList(R.drawable.abc_edit_text_material));
    }

    /*
     * Enabled aggressive block sorting
     */
    private LinearLayout.LayoutParams updateEditTextMargin(ViewGroup.LayoutParams layoutParams) {
        layoutParams = layoutParams instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams)layoutParams : new LinearLayout.LayoutParams(layoutParams);
        if (this.mTmpPaint == null) {
            this.mTmpPaint = new Paint();
        }
        this.mTmpPaint.setTypeface(this.mCollapsingTextHelper.getCollapsedTypeface());
        this.mTmpPaint.setTextSize(this.mCollapsingTextHelper.getCollapsedTextSize());
        layoutParams.topMargin = (int)(- this.mTmpPaint.ascent());
        return layoutParams;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLabelVisibility(boolean bl) {
        boolean bl2 = this.mEditText != null && !TextUtils.isEmpty((CharSequence)this.mEditText.getText());
        boolean bl3 = TextInputLayout.arrayContains(this.getDrawableState(), 16842908);
        boolean bl4 = !TextUtils.isEmpty((CharSequence)this.getError());
        if (this.mDefaultTextColor != null && this.mFocusedTextColor != null) {
            this.mCollapsingTextHelper.setExpandedTextColor(this.mDefaultTextColor.getDefaultColor());
            CollapsingTextHelper collapsingTextHelper = this.mCollapsingTextHelper;
            int n = bl3 ? this.mFocusedTextColor.getDefaultColor() : this.mDefaultTextColor.getDefaultColor();
            collapsingTextHelper.setCollapsedTextColor(n);
        }
        if (!(bl2 || bl3 || bl4)) {
            this.expandHint(bl);
            return;
        }
        this.collapseHint(bl);
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        if (view instanceof EditText) {
            this.setEditText((EditText)view);
            super.addView(view, 0, (ViewGroup.LayoutParams)this.updateEditTextMargin(layoutParams));
            return;
        }
        super.addView(view, n, layoutParams);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mCollapsingTextHelper.draw(canvas);
    }

    public int getCounterMaxLength() {
        return this.mCounterMaxLength;
    }

    @Nullable
    public EditText getEditText() {
        return this.mEditText;
    }

    @Nullable
    public CharSequence getError() {
        if (this.mErrorEnabled && this.mErrorView != null && this.mErrorView.getVisibility() == 0) {
            return this.mErrorView.getText();
        }
        return null;
    }

    @Nullable
    public CharSequence getHint() {
        return this.mHint;
    }

    @NonNull
    public Typeface getTypeface() {
        return this.mCollapsingTextHelper.getCollapsedTypeface();
    }

    public boolean isErrorEnabled() {
        return this.mErrorEnabled;
    }

    public boolean isHintAnimationEnabled() {
        return this.mHintAnimationEnabled;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        if (this.mEditText != null) {
            n = this.mEditText.getLeft() + this.mEditText.getCompoundPaddingLeft();
            n3 = this.mEditText.getRight() - this.mEditText.getCompoundPaddingRight();
            this.mCollapsingTextHelper.setExpandedBounds(n, this.mEditText.getTop() + this.mEditText.getCompoundPaddingTop(), n3, this.mEditText.getBottom() - this.mEditText.getCompoundPaddingBottom());
            this.mCollapsingTextHelper.setCollapsedBounds(n, this.getPaddingTop(), n3, n4 - n2 - this.getPaddingBottom());
            this.mCollapsingTextHelper.recalculate();
        }
    }

    public void refreshDrawableState() {
        super.refreshDrawableState();
        this.updateLabelVisibility(ViewCompat.isLaidOut((View)this));
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setCounterEnabled(boolean bl) {
        if (this.mCounterEnabled != bl) {
            if (bl) {
                this.mCounterView = new TextView(this.getContext());
                this.mCounterView.setMaxLines(1);
                this.mCounterView.setTextAppearance(this.getContext(), this.mCounterTextAppearance);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                layoutParams.gravity = layoutParams.gravity & 112 | 8388613;
                ViewCompat.setAccessibilityLiveRegion((View)this.mCounterView, 1);
                this.addIndicator(this.mCounterView, -1, layoutParams);
                if (this.mEditText == null) {
                    this.updateCounter(0);
                } else {
                    this.updateCounter(this.mEditText.getText().length());
                }
            } else {
                this.removeIndicator(this.mCounterView);
                this.mCounterView = null;
            }
            this.mCounterEnabled = bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setCounterMaxLength(int n) {
        if (this.mCounterMaxLength != n) {
            this.mCounterMaxLength = n > 0 ? n : -1;
            if (this.mCounterEnabled) {
                n = this.mEditText == null ? 0 : this.mEditText.getText().length();
                this.updateCounter(n);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void setError(@Nullable CharSequence charSequence) {
        if (!this.mErrorEnabled) {
            if (TextUtils.isEmpty((CharSequence)charSequence)) {
                return;
            }
            this.setErrorEnabled(true);
        }
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            ViewCompat.setAlpha((View)this.mErrorView, 0.0f);
            this.mErrorView.setText(charSequence);
            ViewCompat.animate((View)this.mErrorView).alpha(1.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new ViewPropertyAnimatorListenerAdapter(){

                @Override
                public void onAnimationStart(View view) {
                    view.setVisibility(0);
                }
            }).start();
            this.mErrorShown = true;
            this.updateEditTextBackground();
            this.updateLabelVisibility(true);
            return;
        }
        if (this.mErrorView.getVisibility() != 0) return;
        ViewCompat.animate((View)this.mErrorView).alpha(0.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(new ViewPropertyAnimatorListenerAdapter(){

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(4);
                TextInputLayout.this.updateLabelVisibility(true);
            }
        }).start();
        this.mErrorShown = false;
        this.updateEditTextBackground();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setErrorEnabled(boolean bl) {
        if (this.mErrorEnabled != bl) {
            if (this.mErrorView != null) {
                ViewCompat.animate((View)this.mErrorView).cancel();
            }
            if (bl) {
                this.mErrorView = new TextView(this.getContext());
                this.mErrorView.setTextAppearance(this.getContext(), this.mErrorTextAppearance);
                this.mErrorView.setVisibility(4);
                ViewCompat.setAccessibilityLiveRegion((View)this.mErrorView, 1);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2);
                layoutParams.weight = 1.0f;
                this.addIndicator(this.mErrorView, 0, layoutParams);
            } else {
                this.mErrorShown = false;
                this.updateEditTextBackground();
                this.removeIndicator(this.mErrorView);
                this.mErrorView = null;
            }
            this.mErrorEnabled = bl;
        }
    }

    public void setHint(@Nullable CharSequence charSequence) {
        this.mHint = charSequence;
        this.mCollapsingTextHelper.setText(charSequence);
        this.sendAccessibilityEvent(2048);
    }

    public void setHintAnimationEnabled(boolean bl) {
        this.mHintAnimationEnabled = bl;
    }

    public void setHintTextAppearance(@StyleRes int n) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(n);
        this.mFocusedTextColor = ColorStateList.valueOf((int)this.mCollapsingTextHelper.getCollapsedTextColor());
        if (this.mEditText != null) {
            this.updateLabelVisibility(false);
            LinearLayout.LayoutParams layoutParams = this.updateEditTextMargin(this.mEditText.getLayoutParams());
            this.mEditText.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
            this.mEditText.requestLayout();
        }
    }

    public void setTypeface(@Nullable Typeface typeface) {
        this.mCollapsingTextHelper.setTypefaces(typeface);
    }

    private class TextInputAccessibilityDelegate
    extends AccessibilityDelegateCompat {
        private TextInputAccessibilityDelegate() {
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)TextInputLayout.class.getSimpleName());
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onInitializeAccessibilityNodeInfo(View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo((View)object, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(TextInputLayout.class.getSimpleName());
            object = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty((CharSequence)object)) {
                accessibilityNodeInfoCompat.setText((CharSequence)object);
            }
            if (TextInputLayout.this.mEditText != null) {
                accessibilityNodeInfoCompat.setLabelFor((View)TextInputLayout.this.mEditText);
            }
            object = TextInputLayout.this.mErrorView != null ? TextInputLayout.this.mErrorView.getText() : null;
            if (TextUtils.isEmpty((CharSequence)object)) return;
            accessibilityNodeInfoCompat.setContentInvalid(true);
            accessibilityNodeInfoCompat.setError((CharSequence)object);
        }

        @Override
        public void onPopulateAccessibilityEvent(View object, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent((View)object, accessibilityEvent);
            object = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty((CharSequence)object)) {
                accessibilityEvent.getText().add(object);
            }
        }
    }

}

