/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.v4.view;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.view.View;
import android.view.ViewParent;

public class NestedScrollingChildHelper {
    private boolean mIsNestedScrollingEnabled;
    private ViewParent mNestedScrollingParent;
    private int[] mTempNestedScrollConsumed;
    private final View mView;

    public NestedScrollingChildHelper(View view) {
        this.mView = view;
    }

    public boolean dispatchNestedFling(float f, float f2, boolean bl) {
        if (this.isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
            return ViewParentCompat.onNestedFling(this.mNestedScrollingParent, this.mView, f, f2, bl);
        }
        return false;
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        if (this.isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
            return ViewParentCompat.onNestedPreFling(this.mNestedScrollingParent, this.mView, f, f2);
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2) {
        boolean bl;
        boolean bl2 = bl = false;
        if (!this.isNestedScrollingEnabled()) return bl2;
        bl2 = bl;
        if (this.mNestedScrollingParent == null) return bl2;
        if (n != 0 || n2 != 0) {
            int n3 = 0;
            int n4 = 0;
            if (arrn2 != null) {
                this.mView.getLocationInWindow(arrn2);
                n3 = arrn2[0];
                n4 = arrn2[1];
            }
            int[] arrn3 = arrn;
            if (arrn == null) {
                if (this.mTempNestedScrollConsumed == null) {
                    this.mTempNestedScrollConsumed = new int[2];
                }
                arrn3 = this.mTempNestedScrollConsumed;
            }
            arrn3[0] = 0;
            arrn3[1] = 0;
            ViewParentCompat.onNestedPreScroll(this.mNestedScrollingParent, this.mView, n, n2, arrn3);
            if (arrn2 != null) {
                this.mView.getLocationInWindow(arrn2);
                arrn2[0] = arrn2[0] - n3;
                arrn2[1] = arrn2[1] - n4;
            }
            if (arrn3[0] != 0) return true;
            bl2 = bl;
            if (arrn3[1] == 0) return bl2;
            return true;
        }
        bl2 = bl;
        if (arrn2 == null) return bl2;
        arrn2[0] = 0;
        arrn2[1] = 0;
        return false;
    }

    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn) {
        if (this.isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
            if (n != 0 || n2 != 0 || n3 != 0 || n4 != 0) {
                int n5 = 0;
                int n6 = 0;
                if (arrn != null) {
                    this.mView.getLocationInWindow(arrn);
                    n5 = arrn[0];
                    n6 = arrn[1];
                }
                ViewParentCompat.onNestedScroll(this.mNestedScrollingParent, this.mView, n, n2, n3, n4);
                if (arrn != null) {
                    this.mView.getLocationInWindow(arrn);
                    arrn[0] = arrn[0] - n5;
                    arrn[1] = arrn[1] - n6;
                }
                return true;
            }
            if (arrn != null) {
                arrn[0] = 0;
                arrn[1] = 0;
            }
        }
        return false;
    }

    public boolean hasNestedScrollingParent() {
        if (this.mNestedScrollingParent != null) {
            return true;
        }
        return false;
    }

    public boolean isNestedScrollingEnabled() {
        return this.mIsNestedScrollingEnabled;
    }

    public void onDetachedFromWindow() {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void onStopNestedScroll(View view) {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void setNestedScrollingEnabled(boolean bl) {
        if (this.mIsNestedScrollingEnabled) {
            ViewCompat.stopNestedScroll(this.mView);
        }
        this.mIsNestedScrollingEnabled = bl;
    }

    public boolean startNestedScroll(int n) {
        if (this.hasNestedScrollingParent()) {
            return true;
        }
        if (this.isNestedScrollingEnabled()) {
            View view = this.mView;
            for (ViewParent viewParent = this.mView.getParent(); viewParent != null; viewParent = viewParent.getParent()) {
                if (ViewParentCompat.onStartNestedScroll(viewParent, view, this.mView, n)) {
                    this.mNestedScrollingParent = viewParent;
                    ViewParentCompat.onNestedScrollAccepted(viewParent, view, this.mView, n);
                    return true;
                }
                if (!(viewParent instanceof View)) continue;
                view = (View)viewParent;
            }
        }
        return false;
    }

    public void stopNestedScroll() {
        if (this.mNestedScrollingParent != null) {
            ViewParentCompat.onStopNestedScroll(this.mNestedScrollingParent, this.mView);
            this.mNestedScrollingParent = null;
        }
    }
}

