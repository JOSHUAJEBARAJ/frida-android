/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.design.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.ViewOffsetBehavior;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

abstract class HeaderScrollingViewBehavior
extends ViewOffsetBehavior<View> {
    public HeaderScrollingViewBehavior() {
    }

    public HeaderScrollingViewBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    abstract View findFirstDependency(List<View> var1);

    int getScrollRange(View view) {
        return view.getMeasuredHeight();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int n, int n2, int n3, int n4) {
        int n5 = view.getLayoutParams().height;
        if (n5 == -1 || n5 == -2) {
            View view2 = coordinatorLayout.getDependencies(view);
            if (view2.isEmpty()) {
                return false;
            }
            if ((view2 = this.findFirstDependency((List<View>)view2)) != null && ViewCompat.isLaidOut(view2)) {
                int n6;
                if (ViewCompat.getFitsSystemWindows(view2)) {
                    ViewCompat.setFitsSystemWindows(view, true);
                }
                n3 = n6 = View.MeasureSpec.getSize((int)n3);
                if (n6 == 0) {
                    n3 = coordinatorLayout.getHeight();
                }
                int n7 = view2.getMeasuredHeight();
                int n8 = this.getScrollRange(view2);
                n6 = n5 == -1 ? 1073741824 : Integer.MIN_VALUE;
                coordinatorLayout.onMeasureChild(view, n, n2, View.MeasureSpec.makeMeasureSpec((int)(n3 - n7 + n8), (int)n6), n4);
                return true;
            }
        }
        return false;
    }
}

