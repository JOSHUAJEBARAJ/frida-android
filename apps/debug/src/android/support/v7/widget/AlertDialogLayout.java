/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class AlertDialogLayout
extends LinearLayoutCompat {
    public AlertDialogLayout(@Nullable Context context) {
        super(context);
    }

    public AlertDialogLayout(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void forceUniformWidth(int n, int n2) {
        int n3 = View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)1073741824);
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if (view.getVisibility() == 8) continue;
            LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
            if (layoutParams.width != -1) continue;
            int n4 = layoutParams.height;
            layoutParams.height = view.getMeasuredHeight();
            this.measureChildWithMargins(view, n3, 0, n2, 0);
            layoutParams.height = n4;
        }
    }

    private static int resolveMinimumHeight(View view) {
        int n = ViewCompat.getMinimumHeight(view);
        if (n > 0) {
            return n;
        }
        if (view instanceof ViewGroup && (view = (ViewGroup)view).getChildCount() == 1) {
            return AlertDialogLayout.resolveMinimumHeight(view.getChildAt(0));
        }
        return 0;
    }

    private void setChildFrame(View view, int n, int n2, int n3, int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }

    private boolean tryOnMeasure(int n, int n2) {
        int n3;
        int n4;
        View view;
        int n5;
        View view2 = null;
        View view3 = null;
        View view4 = null;
        int n6 = this.getChildCount();
        for (n3 = 0; n3 < n6; ++n3) {
            view = this.getChildAt(n3);
            if (view.getVisibility() == 8) continue;
            n5 = view.getId();
            if (n5 == R.id.topPanel) {
                view2 = view;
                continue;
            }
            if (n5 == R.id.buttonPanel) {
                view3 = view;
                continue;
            }
            if (n5 != R.id.contentPanel && n5 != R.id.customPanel) {
                return false;
            }
            if (view4 != null) {
                return false;
            }
            view4 = view;
        }
        int n7 = View.MeasureSpec.getMode((int)n2);
        int n8 = View.MeasureSpec.getSize((int)n2);
        int n9 = View.MeasureSpec.getMode((int)n);
        int n10 = 0;
        int n11 = n3 = this.getPaddingTop() + this.getPaddingBottom();
        if (view2 != null) {
            view2.measure(n, 0);
            n11 = n3 + view2.getMeasuredHeight();
            n10 = View.combineMeasuredStates((int)0, (int)view2.getMeasuredState());
        }
        n3 = 0;
        int n12 = 0;
        n5 = n10;
        int n13 = n11;
        if (view3 != null) {
            view3.measure(n, 0);
            n3 = AlertDialogLayout.resolveMinimumHeight(view3);
            n12 = view3.getMeasuredHeight() - n3;
            n13 = n11 + n3;
            n5 = View.combineMeasuredStates((int)n10, (int)view3.getMeasuredState());
        }
        int n14 = 0;
        if (view4 != null) {
            n10 = n7 == 0 ? 0 : View.MeasureSpec.makeMeasureSpec((int)Math.max(0, n8 - n13), (int)n7);
            view4.measure(n, n10);
            n14 = view4.getMeasuredHeight();
            n13 += n14;
            n5 = View.combineMeasuredStates((int)n5, (int)view4.getMeasuredState());
        }
        n10 = n4 = n8 - n13;
        n11 = n5;
        n8 = n13;
        if (view3 != null) {
            n8 = Math.min(n4, n12);
            n10 = n4;
            n11 = n3;
            if (n8 > 0) {
                n10 = n4 - n8;
                n11 = n3 + n8;
            }
            view3.measure(n, View.MeasureSpec.makeMeasureSpec((int)n11, (int)1073741824));
            n8 = n13 - n3 + view3.getMeasuredHeight();
            n11 = View.combineMeasuredStates((int)n5, (int)view3.getMeasuredState());
        }
        n13 = n10;
        n5 = n11;
        n3 = n8;
        if (view4 != null) {
            n13 = n10;
            n5 = n11;
            n3 = n8;
            if (n10 > 0) {
                view4.measure(n, View.MeasureSpec.makeMeasureSpec((int)(n14 + n10), (int)n7));
                n3 = n8 - n14 + view4.getMeasuredHeight();
                n5 = View.combineMeasuredStates((int)n11, (int)view4.getMeasuredState());
                n13 = n10 - n10;
            }
        }
        n10 = 0;
        for (n13 = 0; n13 < n6; ++n13) {
            view = this.getChildAt(n13);
            n11 = n10;
            if (view.getVisibility() != 8) {
                n11 = Math.max(n10, view.getMeasuredWidth());
            }
            n10 = n11;
        }
        this.setMeasuredDimension(View.resolveSizeAndState((int)(n10 + (this.getPaddingLeft() + this.getPaddingRight())), (int)n, (int)n5), View.resolveSizeAndState((int)n3, (int)n2, (int)0));
        if (n9 != 1073741824) {
            this.forceUniformWidth(n6, n2);
        }
        return true;
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        int n5 = this.getPaddingLeft();
        int n6 = n3 - n;
        int n7 = this.getPaddingRight();
        int n8 = this.getPaddingRight();
        n = this.getMeasuredHeight();
        int n9 = this.getChildCount();
        int n10 = this.getGravity();
        n3 = n10 & 112;
        n = n3 != 16 ? (n3 != 80 ? this.getPaddingTop() : this.getPaddingTop() + n4 - n2 - n) : this.getPaddingTop() + (n4 - n2 - n) / 2;
        Object object = this.getDividerDrawable();
        n3 = object == null ? 0 : object.getIntrinsicHeight();
        n4 = 0;
        do {
            object = this;
            if (n4 >= n9) break;
            View view = object.getChildAt(n4);
            if (view != null && view.getVisibility() != 8) {
                int n11 = view.getMeasuredWidth();
                int n12 = view.getMeasuredHeight();
                LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams)view.getLayoutParams();
                n2 = layoutParams.gravity;
                if (n2 < 0) {
                    n2 = n10 & 8388615;
                }
                n2 = (n2 = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this)) & 7) != 1 ? (n2 != 5 ? layoutParams.leftMargin + n5 : n6 - n7 - n11 - layoutParams.rightMargin) : (n6 - n5 - n8 - n11) / 2 + n5 + layoutParams.leftMargin - layoutParams.rightMargin;
                int n13 = n;
                if (object.hasDividerBeforeChildAt(n4)) {
                    n13 = n + n3;
                }
                n = n13 + layoutParams.topMargin;
                this.setChildFrame(view, n2, n, n11, n12);
                n += n12 + layoutParams.bottomMargin;
            }
            ++n4;
        } while (true);
    }

    @Override
    protected void onMeasure(int n, int n2) {
        if (!this.tryOnMeasure(n, n2)) {
            super.onMeasure(n, n2);
        }
    }
}

