/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.view.View
 *  android.view.ViewPropertyAnimator
 *  android.view.animation.Interpolator
 */
package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.support.annotation.Nullable;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.FloatingActionButtonEclairMr1;
import android.support.design.widget.FloatingActionButtonImpl;
import android.support.design.widget.ShadowViewDelegate;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

class FloatingActionButtonHoneycombMr1
extends FloatingActionButtonEclairMr1 {
    private boolean mIsHiding;

    FloatingActionButtonHoneycombMr1(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void hide(final @Nullable FloatingActionButtonImpl.InternalVisibilityChangedListener internalVisibilityChangedListener) {
        if (this.mIsHiding || this.mView.getVisibility() != 0) {
            if (internalVisibilityChangedListener == null) return;
            {
                internalVisibilityChangedListener.onHidden();
                return;
            }
        } else {
            if (ViewCompat.isLaidOut(this.mView) && !this.mView.isInEditMode()) {
                this.mView.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                    public void onAnimationCancel(Animator animator) {
                        FloatingActionButtonHoneycombMr1.this.mIsHiding = false;
                    }

                    public void onAnimationEnd(Animator animator) {
                        FloatingActionButtonHoneycombMr1.this.mIsHiding = false;
                        FloatingActionButtonHoneycombMr1.this.mView.setVisibility(8);
                        if (internalVisibilityChangedListener != null) {
                            internalVisibilityChangedListener.onHidden();
                        }
                    }

                    public void onAnimationStart(Animator animator) {
                        FloatingActionButtonHoneycombMr1.this.mIsHiding = true;
                        FloatingActionButtonHoneycombMr1.this.mView.setVisibility(0);
                    }
                });
                return;
            }
            this.mView.setVisibility(8);
            if (internalVisibilityChangedListener == null) return;
            {
                internalVisibilityChangedListener.onHidden();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void show(final @Nullable FloatingActionButtonImpl.InternalVisibilityChangedListener internalVisibilityChangedListener) {
        if (this.mView.getVisibility() == 0) return;
        {
            if (ViewCompat.isLaidOut(this.mView) && !this.mView.isInEditMode()) {
                this.mView.setAlpha(0.0f);
                this.mView.setScaleY(0.0f);
                this.mView.setScaleX(0.0f);
                this.mView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200).setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                    public void onAnimationEnd(Animator animator) {
                        if (internalVisibilityChangedListener != null) {
                            internalVisibilityChangedListener.onShown();
                        }
                    }

                    public void onAnimationStart(Animator animator) {
                        FloatingActionButtonHoneycombMr1.this.mView.setVisibility(0);
                    }
                });
                return;
            } else {
                this.mView.setVisibility(0);
                this.mView.setAlpha(1.0f);
                this.mView.setScaleY(1.0f);
                this.mView.setScaleX(1.0f);
                if (internalVisibilityChangedListener == null) return;
                {
                    internalVisibilityChangedListener.onShown();
                    return;
                }
            }
        }
    }

}

