/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 *  android.view.animation.Transformation
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.FloatingActionButtonImpl;
import android.support.design.widget.ShadowDrawableWrapper;
import android.support.design.widget.ShadowViewDelegate;
import android.support.design.widget.StateListAnimator;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

class FloatingActionButtonEclairMr1
extends FloatingActionButtonImpl {
    private int mAnimationDuration;
    private Drawable mBorderDrawable;
    private float mElevation;
    private boolean mIsHiding;
    private float mPressedTranslationZ;
    private Drawable mRippleDrawable;
    ShadowDrawableWrapper mShadowDrawable;
    private Drawable mShapeDrawable;
    private StateListAnimator mStateListAnimator;

    FloatingActionButtonEclairMr1(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
        this.mAnimationDuration = view.getResources().getInteger(17694720);
        this.mStateListAnimator = new StateListAnimator();
        this.mStateListAnimator.setTarget(view);
        this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, this.setupAnimation(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.setupAnimation(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(EMPTY_STATE_SET, this.setupAnimation(new ResetElevationAnimation()));
    }

    private static ColorStateList createColorStateList(int n) {
        int[][] arrarrn = new int[3][];
        int[] arrn = new int[3];
        arrarrn[0] = FOCUSED_ENABLED_STATE_SET;
        arrn[0] = n;
        int n2 = 0 + 1;
        arrarrn[n2] = PRESSED_ENABLED_STATE_SET;
        arrn[n2] = n;
        n = n2 + 1;
        arrarrn[n] = new int[0];
        arrn[n] = 0;
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private static Drawable mutateDrawable(Drawable drawable2) {
        if (Build.VERSION.SDK_INT < 14 && drawable2 instanceof GradientDrawable) {
            return drawable2;
        }
        return drawable2.mutate();
    }

    private Animation setupAnimation(Animation animation) {
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration((long)this.mAnimationDuration);
        return animation;
    }

    private void updatePadding() {
        Rect rect = new Rect();
        this.mShadowDrawable.getPadding(rect);
        this.mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    @Override
    void hide(final @Nullable FloatingActionButtonImpl.InternalVisibilityChangedListener internalVisibilityChangedListener) {
        if (this.mIsHiding || this.mView.getVisibility() != 0) {
            if (internalVisibilityChangedListener != null) {
                internalVisibilityChangedListener.onHidden();
            }
            return;
        }
        Animation animation = android.view.animation.AnimationUtils.loadAnimation((Context)this.mView.getContext(), (int)R.anim.design_fab_out);
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration(200);
        animation.setAnimationListener((Animation.AnimationListener)new AnimationUtils.AnimationListenerAdapter(){

            @Override
            public void onAnimationEnd(Animation animation) {
                FloatingActionButtonEclairMr1.this.mIsHiding = false;
                FloatingActionButtonEclairMr1.this.mView.setVisibility(8);
                if (internalVisibilityChangedListener != null) {
                    internalVisibilityChangedListener.onHidden();
                }
            }

            @Override
            public void onAnimationStart(Animation animation) {
                FloatingActionButtonEclairMr1.this.mIsHiding = true;
            }
        });
        this.mView.startAnimation(animation);
    }

    @Override
    void jumpDrawableToCurrentState() {
        this.mStateListAnimator.jumpToCurrentState();
    }

    @Override
    void onDrawableStateChanged(int[] arrn) {
        this.mStateListAnimator.setState(arrn);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void setBackgroundDrawable(Drawable arrdrawable, ColorStateList colorStateList, PorterDuff.Mode mode, int n, int n2) {
        this.mShapeDrawable = DrawableCompat.wrap(FloatingActionButtonEclairMr1.mutateDrawable((Drawable)arrdrawable));
        DrawableCompat.setTintList(this.mShapeDrawable, colorStateList);
        if (mode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, mode);
        }
        arrdrawable = new Drawable[]();
        arrdrawable.setShape(1);
        arrdrawable.setColor(-1);
        arrdrawable.setCornerRadius(this.mShadowViewDelegate.getRadius());
        this.mRippleDrawable = DrawableCompat.wrap((Drawable)arrdrawable);
        DrawableCompat.setTintList(this.mRippleDrawable, FloatingActionButtonEclairMr1.createColorStateList(n));
        DrawableCompat.setTintMode(this.mRippleDrawable, PorterDuff.Mode.MULTIPLY);
        if (n2 > 0) {
            this.mBorderDrawable = this.createBorderDrawable(n2, colorStateList);
            arrdrawable = new Drawable[]{this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable};
        } else {
            this.mBorderDrawable = null;
            arrdrawable = new Drawable[]{this.mShapeDrawable, this.mRippleDrawable};
        }
        this.mShadowDrawable = new ShadowDrawableWrapper(this.mView.getResources(), (Drawable)new LayerDrawable(arrdrawable), this.mShadowViewDelegate.getRadius(), this.mElevation, this.mElevation + this.mPressedTranslationZ);
        this.mShadowDrawable.setAddPaddingForCorners(false);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
        this.updatePadding();
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
    void setElevation(float f) {
        if (this.mElevation != f && this.mShadowDrawable != null) {
            this.mShadowDrawable.setShadowSize(f, this.mPressedTranslationZ + f);
            this.mElevation = f;
            this.updatePadding();
        }
    }

    @Override
    void setPressedTranslationZ(float f) {
        if (this.mPressedTranslationZ != f && this.mShadowDrawable != null) {
            this.mPressedTranslationZ = f;
            this.mShadowDrawable.setMaxShadowSize(this.mElevation + f);
            this.updatePadding();
        }
    }

    @Override
    void setRippleColor(int n) {
        DrawableCompat.setTintList(this.mRippleDrawable, FloatingActionButtonEclairMr1.createColorStateList(n));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void show(final @Nullable FloatingActionButtonImpl.InternalVisibilityChangedListener internalVisibilityChangedListener) {
        if (this.mView.getVisibility() != 0 || this.mIsHiding) {
            this.mView.clearAnimation();
            this.mView.setVisibility(0);
            Animation animation = android.view.animation.AnimationUtils.loadAnimation((Context)this.mView.getContext(), (int)R.anim.design_fab_in);
            animation.setDuration(200);
            animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            animation.setAnimationListener((Animation.AnimationListener)new AnimationUtils.AnimationListenerAdapter(){

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (internalVisibilityChangedListener != null) {
                        internalVisibilityChangedListener.onShown();
                    }
                }
            });
            this.mView.startAnimation(animation);
            return;
        } else {
            if (internalVisibilityChangedListener == null) return;
            {
                internalVisibilityChangedListener.onShown();
                return;
            }
        }
    }

    private abstract class BaseShadowAnimation
    extends Animation {
        private float mShadowSizeDiff;
        private float mShadowSizeStart;

        private BaseShadowAnimation() {
        }

        protected void applyTransformation(float f, Transformation transformation) {
            FloatingActionButtonEclairMr1.this.mShadowDrawable.setShadowSize(this.mShadowSizeStart + this.mShadowSizeDiff * f);
        }

        protected abstract float getTargetShadowSize();

        public void reset() {
            super.reset();
            this.mShadowSizeStart = FloatingActionButtonEclairMr1.this.mShadowDrawable.getShadowSize();
            this.mShadowSizeDiff = this.getTargetShadowSize() - this.mShadowSizeStart;
        }
    }

    private class ElevateToTranslationZAnimation
    extends BaseShadowAnimation {
        private ElevateToTranslationZAnimation() {
        }

        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation + FloatingActionButtonEclairMr1.this.mPressedTranslationZ;
        }
    }

    private class ResetElevationAnimation
    extends BaseShadowAnimation {
        private ResetElevationAnimation() {
        }

        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation;
        }
    }

}

