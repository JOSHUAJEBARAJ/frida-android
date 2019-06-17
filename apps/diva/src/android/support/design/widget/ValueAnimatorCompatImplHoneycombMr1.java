/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.view.animation.Interpolator
 */
package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.support.design.widget.ValueAnimatorCompat;
import android.view.animation.Interpolator;

class ValueAnimatorCompatImplHoneycombMr1
extends ValueAnimatorCompat.Impl {
    final ValueAnimator mValueAnimator = new ValueAnimator();

    ValueAnimatorCompatImplHoneycombMr1() {
    }

    @Override
    public void cancel() {
        this.mValueAnimator.cancel();
    }

    @Override
    public void end() {
        this.mValueAnimator.end();
    }

    @Override
    public float getAnimatedFloatValue() {
        return ((Float)this.mValueAnimator.getAnimatedValue()).floatValue();
    }

    @Override
    public float getAnimatedFraction() {
        return this.mValueAnimator.getAnimatedFraction();
    }

    @Override
    public int getAnimatedIntValue() {
        return (Integer)this.mValueAnimator.getAnimatedValue();
    }

    @Override
    public long getDuration() {
        return this.mValueAnimator.getDuration();
    }

    @Override
    public boolean isRunning() {
        return this.mValueAnimator.isRunning();
    }

    @Override
    public void setDuration(int n) {
        this.mValueAnimator.setDuration((long)n);
    }

    @Override
    public void setFloatValues(float f, float f2) {
        this.mValueAnimator.setFloatValues(new float[]{f, f2});
    }

    @Override
    public void setIntValues(int n, int n2) {
        this.mValueAnimator.setIntValues(new int[]{n, n2});
    }

    @Override
    public void setInterpolator(Interpolator interpolator) {
        this.mValueAnimator.setInterpolator((TimeInterpolator)interpolator);
    }

    @Override
    public void setListener(final ValueAnimatorCompat.Impl.AnimatorListenerProxy animatorListenerProxy) {
        this.mValueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationCancel(Animator animator) {
                animatorListenerProxy.onAnimationCancel();
            }

            public void onAnimationEnd(Animator animator) {
                animatorListenerProxy.onAnimationEnd();
            }

            public void onAnimationStart(Animator animator) {
                animatorListenerProxy.onAnimationStart();
            }
        });
    }

    @Override
    public void setUpdateListener(final ValueAnimatorCompat.Impl.AnimatorUpdateListenerProxy animatorUpdateListenerProxy) {
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatorUpdateListenerProxy.onAnimationUpdate();
            }
        });
    }

    @Override
    public void start() {
        this.mValueAnimator.start();
    }

}

