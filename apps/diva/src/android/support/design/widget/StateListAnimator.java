/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.util.StateSet
 *  android.view.View
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 */
package android.support.design.widget;

import android.util.StateSet;
import android.view.View;
import android.view.animation.Animation;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

final class StateListAnimator {
    private Animation.AnimationListener mAnimationListener;
    private Tuple mLastMatch = null;
    private Animation mRunningAnimation = null;
    private final ArrayList<Tuple> mTuples = new ArrayList();
    private WeakReference<View> mViewRef;

    StateListAnimator() {
        this.mAnimationListener = new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                if (StateListAnimator.this.mRunningAnimation == animation) {
                    StateListAnimator.this.mRunningAnimation = null;
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        };
    }

    private void cancel() {
        if (this.mRunningAnimation != null) {
            View view = this.getTarget();
            if (view != null && view.getAnimation() == this.mRunningAnimation) {
                view.clearAnimation();
            }
            this.mRunningAnimation = null;
        }
    }

    private void clearTarget() {
        View view = this.getTarget();
        int n = this.mTuples.size();
        for (int i = 0; i < n; ++i) {
            Animation animation = this.mTuples.get((int)i).mAnimation;
            if (view.getAnimation() != animation) continue;
            view.clearAnimation();
        }
        this.mViewRef = null;
        this.mLastMatch = null;
        this.mRunningAnimation = null;
    }

    private void start(Tuple tuple) {
        this.mRunningAnimation = tuple.mAnimation;
        tuple = this.getTarget();
        if (tuple != null) {
            tuple.startAnimation(this.mRunningAnimation);
        }
    }

    public void addState(int[] object, Animation animation) {
        object = new Tuple((int[])object, animation);
        animation.setAnimationListener(this.mAnimationListener);
        this.mTuples.add((Tuple)object);
    }

    Animation getRunningAnimation() {
        return this.mRunningAnimation;
    }

    View getTarget() {
        if (this.mViewRef == null) {
            return null;
        }
        return this.mViewRef.get();
    }

    ArrayList<Tuple> getTuples() {
        return this.mTuples;
    }

    public void jumpToCurrentState() {
        View view;
        if (this.mRunningAnimation != null && (view = this.getTarget()) != null && view.getAnimation() == this.mRunningAnimation) {
            view.clearAnimation();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void setState(int[] var1_1) {
        var5_2 = null;
        var3_3 = this.mTuples.size();
        var2_4 = 0;
        do {
            var4_5 = var5_2;
            if (var2_4 >= var3_3) ** GOTO lbl-1000
            var4_5 = this.mTuples.get(var2_4);
            if (StateSet.stateSetMatches((int[])var4_5.mSpecs, (int[])var1_1)) lbl-1000: // 2 sources:
            {
                if (var4_5 != this.mLastMatch) break;
                return;
            }
            ++var2_4;
        } while (true);
        if (this.mLastMatch != null) {
            this.cancel();
        }
        this.mLastMatch = var4_5;
        var1_1 = this.mViewRef.get();
        if (var4_5 == null) return;
        if (var1_1 == null) return;
        if (var1_1.getVisibility() != 0) return;
        this.start(var4_5);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void setTarget(View view) {
        View view2 = this.getTarget();
        if (view2 == view) {
            return;
        }
        if (view2 != null) {
            this.clearTarget();
        }
        if (view == null) return;
        this.mViewRef = new WeakReference<View>(view);
    }

    static class Tuple {
        final Animation mAnimation;
        final int[] mSpecs;

        private Tuple(int[] arrn, Animation animation) {
            this.mSpecs = arrn;
            this.mAnimation = animation;
        }

        Animation getAnimation() {
            return this.mAnimation;
        }

        int[] getSpecs() {
            return this.mSpecs;
        }
    }

}

