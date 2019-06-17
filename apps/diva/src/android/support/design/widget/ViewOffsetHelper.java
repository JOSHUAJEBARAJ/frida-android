/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.design.widget;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewParent;

class ViewOffsetHelper {
    private int mLayoutLeft;
    private int mLayoutTop;
    private int mOffsetLeft;
    private int mOffsetTop;
    private final View mView;

    public ViewOffsetHelper(View view) {
        this.mView = view;
    }

    private static void tickleInvalidationFlag(View view) {
        float f = ViewCompat.getTranslationX(view);
        ViewCompat.setTranslationY(view, 1.0f + f);
        ViewCompat.setTranslationY(view, f);
    }

    private void updateOffsets() {
        ViewCompat.offsetTopAndBottom(this.mView, this.mOffsetTop - (this.mView.getTop() - this.mLayoutTop));
        ViewCompat.offsetLeftAndRight(this.mView, this.mOffsetLeft - (this.mView.getLeft() - this.mLayoutLeft));
        if (Build.VERSION.SDK_INT < 23) {
            ViewOffsetHelper.tickleInvalidationFlag(this.mView);
            ViewParent viewParent = this.mView.getParent();
            if (viewParent instanceof View) {
                ViewOffsetHelper.tickleInvalidationFlag((View)viewParent);
            }
        }
    }

    public int getLeftAndRightOffset() {
        return this.mOffsetLeft;
    }

    public int getTopAndBottomOffset() {
        return this.mOffsetTop;
    }

    public void onViewLayout() {
        this.mLayoutTop = this.mView.getTop();
        this.mLayoutLeft = this.mView.getLeft();
        this.updateOffsets();
    }

    public boolean setLeftAndRightOffset(int n) {
        if (this.mOffsetLeft != n) {
            this.mOffsetLeft = n;
            this.updateOffsets();
            return true;
        }
        return false;
    }

    public boolean setTopAndBottomOffset(int n) {
        if (this.mOffsetTop != n) {
            this.mOffsetTop = n;
            this.updateOffsets();
            return true;
        }
        return false;
    }
}

