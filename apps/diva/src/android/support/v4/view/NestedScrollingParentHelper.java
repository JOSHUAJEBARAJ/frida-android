/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.v4.view;

import android.view.View;
import android.view.ViewGroup;

public class NestedScrollingParentHelper {
    private int mNestedScrollAxes;
    private final ViewGroup mViewGroup;

    public NestedScrollingParentHelper(ViewGroup viewGroup) {
        this.mViewGroup = viewGroup;
    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollAxes;
    }

    public void onNestedScrollAccepted(View view, View view2, int n) {
        this.mNestedScrollAxes = n;
    }

    public void onStopNestedScroll(View view) {
        this.mNestedScrollAxes = 0;
    }
}

