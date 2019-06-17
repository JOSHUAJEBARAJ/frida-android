/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.ObjectAnimator
 *  android.animation.StateListAnimator
 *  android.animation.TimeInterpolator
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.view.View
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 */
package android.support.design.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.design.widget.CircularBorderDrawable;
import android.support.design.widget.CircularBorderDrawableLollipop;
import android.support.design.widget.FloatingActionButtonHoneycombMr1;
import android.support.design.widget.ShadowViewDelegate;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

@TargetApi(value=21)
class FloatingActionButtonLollipop
extends FloatingActionButtonHoneycombMr1 {
    private Drawable mBorderDrawable;
    private Interpolator mInterpolator;
    private RippleDrawable mRippleDrawable;
    private Drawable mShapeDrawable;

    FloatingActionButtonLollipop(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
        if (!view.isInEditMode()) {
            this.mInterpolator = AnimationUtils.loadInterpolator((Context)this.mView.getContext(), (int)17563661);
        }
    }

    private Animator setupAnimator(Animator animator) {
        animator.setInterpolator((TimeInterpolator)this.mInterpolator);
        return animator;
    }

    @Override
    void jumpDrawableToCurrentState() {
    }

    @Override
    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawableLollipop();
    }

    @Override
    void onDrawableStateChanged(int[] arrn) {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void setBackgroundDrawable(Drawable drawable2, ColorStateList colorStateList, PorterDuff.Mode mode, int n, int n2) {
        this.mShapeDrawable = DrawableCompat.wrap(drawable2.mutate());
        DrawableCompat.setTintList(this.mShapeDrawable, colorStateList);
        if (mode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, mode);
        }
        if (n2 > 0) {
            this.mBorderDrawable = this.createBorderDrawable(n2, colorStateList);
            drawable2 = new LayerDrawable(new Drawable[]{this.mBorderDrawable, this.mShapeDrawable});
        } else {
            this.mBorderDrawable = null;
            drawable2 = this.mShapeDrawable;
        }
        this.mRippleDrawable = new RippleDrawable(ColorStateList.valueOf((int)n), drawable2, null);
        this.mShadowViewDelegate.setBackgroundDrawable((Drawable)this.mRippleDrawable);
        this.mShadowViewDelegate.setShadowPadding(0, 0, 0, 0);
    }

    @Override
    void setBackgroundTintList(ColorStateList colorStateList) {
        DrawableCompat.setTintList(this.mShapeDrawable, colorStateList);
        if (this.mBorderDrawable != null) {
            DrawableCompat.setTintList(this.mBorderDrawable, colorStateList);
        }
    }

    @Override
    void setBackgroundTintMode(PorterDuff.Mode mode) {
        DrawableCompat.setTintMode(this.mShapeDrawable, mode);
    }

    @Override
    public void setElevation(float f) {
        ViewCompat.setElevation(this.mView, f);
    }

    @Override
    void setPressedTranslationZ(float f) {
        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, this.setupAnimator((Animator)ObjectAnimator.ofFloat((Object)this.mView, (String)"translationZ", (float[])new float[]{f})));
        stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.setupAnimator((Animator)ObjectAnimator.ofFloat((Object)this.mView, (String)"translationZ", (float[])new float[]{f})));
        stateListAnimator.addState(EMPTY_STATE_SET, this.setupAnimator((Animator)ObjectAnimator.ofFloat((Object)this.mView, (String)"translationZ", (float[])new float[]{0.0f})));
        this.mView.setStateListAnimator(stateListAnimator);
    }

    @Override
    void setRippleColor(int n) {
        this.mRippleDrawable.setColor(ColorStateList.valueOf((int)n));
    }
}

