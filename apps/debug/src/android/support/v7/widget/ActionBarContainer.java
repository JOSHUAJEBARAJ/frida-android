/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.util.AttributeSet
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.ActionBarBackgroundDrawable;
import android.support.v7.widget.ScrollingTabContainerView;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class ActionBarContainer
extends FrameLayout {
    private View mActionBarView;
    Drawable mBackground;
    private View mContextView;
    private int mHeight;
    boolean mIsSplit;
    boolean mIsStacked;
    private boolean mIsTransitioning;
    Drawable mSplitBackground;
    Drawable mStackedBackground;
    private View mTabContainer;

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public ActionBarContainer(Context var1_1, AttributeSet var2_2) {
        super(var1_1, var2_2);
        ViewCompat.setBackground((View)this, new ActionBarBackgroundDrawable(this));
        var1_1 = var1_1.obtainStyledAttributes(var2_2, R.styleable.ActionBar);
        this.mBackground = var1_1.getDrawable(R.styleable.ActionBar_background);
        this.mStackedBackground = var1_1.getDrawable(R.styleable.ActionBar_backgroundStacked);
        this.mHeight = var1_1.getDimensionPixelSize(R.styleable.ActionBar_height, -1);
        if (this.getId() == R.id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = var1_1.getDrawable(R.styleable.ActionBar_backgroundSplit);
        }
        var1_1.recycle();
        var3_3 = this.mIsSplit;
        var4_4 = false;
        if (!var3_3) ** GOTO lbl17
        var3_3 = var4_4;
        if (this.mSplitBackground != null) ** GOTO lbl23
        ** GOTO lbl-1000
lbl17: // 1 sources:
        var3_3 = var4_4;
        if (this.mBackground == null) {
            var3_3 = var4_4;
            ** if (this.mStackedBackground != null) goto lbl23
        }
        ** GOTO lbl23
lbl-1000: // 2 sources:
        {
            var3_3 = true;
        }
lbl23: // 4 sources:
        this.setWillNotDraw(var3_3);
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        return view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    private boolean isCollapsed(View view) {
        if (view != null && view.getVisibility() != 8 && view.getMeasuredHeight() != 0) {
            return false;
        }
        return true;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable2 = this.mBackground;
        if (drawable2 != null && drawable2.isStateful()) {
            this.mBackground.setState(this.getDrawableState());
        }
        if ((drawable2 = this.mStackedBackground) != null && drawable2.isStateful()) {
            this.mStackedBackground.setState(this.getDrawableState());
        }
        if ((drawable2 = this.mSplitBackground) != null && drawable2.isStateful()) {
            this.mSplitBackground.setState(this.getDrawableState());
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable2 = this.mBackground;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        if ((drawable2 = this.mStackedBackground) != null) {
            drawable2.jumpToCurrentState();
        }
        if ((drawable2 = this.mSplitBackground) != null) {
            drawable2.jumpToCurrentState();
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = this.findViewById(R.id.action_bar);
        this.mContextView = this.findViewById(R.id.action_context_bar);
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        super.onHoverEvent(motionEvent);
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mIsTransitioning && !super.onInterceptTouchEvent(motionEvent)) {
            return false;
        }
        return true;
    }

    public void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        FrameLayout.LayoutParams layoutParams;
        super.onLayout(bl, n, n2, n3, n4);
        View view = this.mTabContainer;
        bl = view != null && view.getVisibility() != 8;
        if (view != null && view.getVisibility() != 8) {
            n2 = this.getMeasuredHeight();
            layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
            view.layout(n, n2 - view.getMeasuredHeight() - layoutParams.bottomMargin, n3, n2 - layoutParams.bottomMargin);
        }
        n = 0;
        n2 = 0;
        if (this.mIsSplit) {
            view = this.mSplitBackground;
            if (view != null) {
                view.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                n = 1;
            }
        } else {
            if (this.mBackground != null) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
                } else {
                    layoutParams = this.mContextView;
                    if (layoutParams != null && layoutParams.getVisibility() == 0) {
                        this.mBackground.setBounds(this.mContextView.getLeft(), this.mContextView.getTop(), this.mContextView.getRight(), this.mContextView.getBottom());
                    } else {
                        this.mBackground.setBounds(0, 0, 0, 0);
                    }
                }
                n2 = 1;
            }
            this.mIsStacked = bl;
            n = n2;
            if (bl) {
                layoutParams = this.mStackedBackground;
                n = n2;
                if (layoutParams != null) {
                    layoutParams.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    n = 1;
                }
            }
        }
        if (n != 0) {
            this.invalidate();
        }
    }

    public void onMeasure(int n, int n2) {
        int n3 = n2;
        if (this.mActionBarView == null) {
            n3 = n2;
            if (View.MeasureSpec.getMode((int)n2) == Integer.MIN_VALUE) {
                int n4 = this.mHeight;
                n3 = n2;
                if (n4 >= 0) {
                    n3 = View.MeasureSpec.makeMeasureSpec((int)Math.min(n4, View.MeasureSpec.getSize((int)n2)), (int)Integer.MIN_VALUE);
                }
            }
        }
        super.onMeasure(n, n3);
        if (this.mActionBarView == null) {
            return;
        }
        n2 = View.MeasureSpec.getMode((int)n3);
        View view = this.mTabContainer;
        if (view != null && view.getVisibility() != 8 && n2 != 1073741824) {
            n = !this.isCollapsed(this.mActionBarView) ? this.getMeasuredHeightWithMargins(this.mActionBarView) : (!this.isCollapsed(this.mContextView) ? this.getMeasuredHeightWithMargins(this.mContextView) : 0);
            n2 = n2 == Integer.MIN_VALUE ? View.MeasureSpec.getSize((int)n3) : Integer.MAX_VALUE;
            this.setMeasuredDimension(this.getMeasuredWidth(), Math.min(this.getMeasuredHeightWithMargins(this.mTabContainer) + n, n2));
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public void setPrimaryBackground(Drawable drawable2) {
        Drawable drawable3 = this.mBackground;
        if (drawable3 != null) {
            drawable3.setCallback(null);
            this.unscheduleDrawable(this.mBackground);
        }
        this.mBackground = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            drawable2 = this.mActionBarView;
            if (drawable2 != null) {
                this.mBackground.setBounds(drawable2.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        boolean bl = this.mIsSplit;
        boolean bl2 = true;
        if (!(bl ? this.mSplitBackground == null : this.mBackground == null && this.mStackedBackground == null)) {
            bl2 = false;
        }
        this.setWillNotDraw(bl2);
        this.invalidate();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void setSplitBackground(Drawable var1_1) {
        var4_2 = this.mSplitBackground;
        if (var4_2 != null) {
            var4_2.setCallback(null);
            this.unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = var1_1;
        var3_3 = false;
        if (var1_1 != null) {
            var1_1.setCallback((Drawable.Callback)this);
            if (this.mIsSplit && (var1_1 = this.mSplitBackground) != null) {
                var1_1.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            }
        }
        if (!this.mIsSplit) ** GOTO lbl15
        var2_4 = var3_3;
        if (this.mSplitBackground != null) ** GOTO lbl21
        ** GOTO lbl-1000
lbl15: // 1 sources:
        var2_4 = var3_3;
        if (this.mBackground == null) {
            var2_4 = var3_3;
            ** if (this.mStackedBackground != null) goto lbl21
        }
        ** GOTO lbl21
lbl-1000: // 2 sources:
        {
            var2_4 = true;
        }
lbl21: // 4 sources:
        this.setWillNotDraw(var2_4);
        this.invalidate();
    }

    public void setStackedBackground(Drawable drawable2) {
        Drawable drawable3 = this.mStackedBackground;
        if (drawable3 != null) {
            drawable3.setCallback(null);
            this.unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            if (this.mIsStacked && (drawable2 = this.mStackedBackground) != null) {
                drawable2.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
        }
        boolean bl = this.mIsSplit;
        boolean bl2 = true;
        if (!(bl ? this.mSplitBackground == null : this.mBackground == null && this.mStackedBackground == null)) {
            bl2 = false;
        }
        this.setWillNotDraw(bl2);
        this.invalidate();
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        View view = this.mTabContainer;
        if (view != null) {
            this.removeView(view);
        }
        this.mTabContainer = scrollingTabContainerView;
        if (scrollingTabContainerView != null) {
            this.addView((View)scrollingTabContainerView);
            view = scrollingTabContainerView.getLayoutParams();
            view.width = -1;
            view.height = -2;
            scrollingTabContainerView.setAllowCollapse(false);
        }
    }

    public void setTransitioning(boolean bl) {
        this.mIsTransitioning = bl;
        int n = bl ? 393216 : 262144;
        this.setDescendantFocusability(n);
    }

    public void setVisibility(int n) {
        super.setVisibility(n);
        boolean bl = n == 0;
        Drawable drawable2 = this.mBackground;
        if (drawable2 != null) {
            drawable2.setVisible(bl, false);
        }
        if ((drawable2 = this.mStackedBackground) != null) {
            drawable2.setVisible(bl, false);
        }
        if ((drawable2 = this.mSplitBackground) != null) {
            drawable2.setVisible(bl, false);
        }
    }

    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback, int n) {
        if (n != 0) {
            return super.startActionModeForChild(view, callback, n);
        }
        return null;
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        if (drawable2 == this.mBackground && !this.mIsSplit || drawable2 == this.mStackedBackground && this.mIsStacked || drawable2 == this.mSplitBackground && this.mIsSplit || super.verifyDrawable(drawable2)) {
            return true;
        }
        return false;
    }
}

