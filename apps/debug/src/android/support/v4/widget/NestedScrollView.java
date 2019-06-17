/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.FocusFinder
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.View$MeasureSpec
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityRecord
 *  android.view.animation.AnimationUtils
 *  android.widget.EdgeEffect
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.OverScroller
 *  android.widget.ScrollView
 */
package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityRecord;
import android.view.animation.AnimationUtils;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import java.util.ArrayList;

public class NestedScrollView
extends FrameLayout
implements NestedScrollingParent2,
NestedScrollingChild2,
ScrollingView {
    private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final int[] SCROLLVIEW_STYLEABLE = new int[]{16843130};
    private static final String TAG = "NestedScrollView";
    private int mActivePointerId = -1;
    private final NestedScrollingChildHelper mChildHelper;
    private View mChildToScrollTo = null;
    private EdgeEffect mEdgeGlowBottom;
    private EdgeEffect mEdgeGlowTop;
    private boolean mFillViewport;
    private boolean mIsBeingDragged = false;
    private boolean mIsLaidOut = false;
    private boolean mIsLayoutDirty = true;
    private int mLastMotionY;
    private long mLastScroll;
    private int mLastScrollerY;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private OnScrollChangeListener mOnScrollChangeListener;
    private final NestedScrollingParentHelper mParentHelper;
    private SavedState mSavedState;
    private final int[] mScrollConsumed = new int[2];
    private final int[] mScrollOffset = new int[2];
    private OverScroller mScroller;
    private boolean mSmoothScrollingEnabled = true;
    private final Rect mTempRect = new Rect();
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;

    public NestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public NestedScrollView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NestedScrollView(@NonNull Context context, @Nullable AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.initScrollView();
        context = context.obtainStyledAttributes(attributeSet, SCROLLVIEW_STYLEABLE, n, 0);
        this.setFillViewport(context.getBoolean(0, false));
        context.recycle();
        this.mParentHelper = new NestedScrollingParentHelper((ViewGroup)this);
        this.mChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
        ViewCompat.setAccessibilityDelegate((View)this, ACCESSIBILITY_DELEGATE);
    }

    private boolean canScroll() {
        int n = this.getChildCount();
        boolean bl = false;
        if (n > 0) {
            View view = this.getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
            if (view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin > this.getHeight() - this.getPaddingTop() - this.getPaddingBottom()) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    private static int clamp(int n, int n2, int n3) {
        if (n2 < n3 && n >= 0) {
            if (n2 + n > n3) {
                return n3 - n2;
            }
            return n;
        }
        return 0;
    }

    private void doScrollY(int n) {
        if (n != 0) {
            if (this.mSmoothScrollingEnabled) {
                this.smoothScrollBy(0, n);
                return;
            }
            this.scrollBy(0, n);
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.recycleVelocityTracker();
        this.stopNestedScroll(0);
        EdgeEffect edgeEffect = this.mEdgeGlowTop;
        if (edgeEffect != null) {
            edgeEffect.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
    }

    private void ensureGlows() {
        if (this.getOverScrollMode() != 2) {
            if (this.mEdgeGlowTop == null) {
                Context context = this.getContext();
                this.mEdgeGlowTop = new EdgeEffect(context);
                this.mEdgeGlowBottom = new EdgeEffect(context);
                return;
            }
        } else {
            this.mEdgeGlowTop = null;
            this.mEdgeGlowBottom = null;
        }
    }

    private View findFocusableViewInBounds(boolean bl, int n, int n2) {
        ArrayList arrayList = this.getFocusables(2);
        View view = null;
        boolean bl2 = false;
        int n3 = arrayList.size();
        for (int i = 0; i < n3; ++i) {
            View view2 = (View)arrayList.get(i);
            int n4 = view2.getTop();
            int n5 = view2.getBottom();
            View view3 = view;
            boolean bl3 = bl2;
            if (n < n5) {
                view3 = view;
                bl3 = bl2;
                if (n4 < n2) {
                    boolean bl4 = false;
                    boolean bl5 = n < n4 && n5 < n2;
                    if (view == null) {
                        view3 = view2;
                        bl3 = bl5;
                    } else {
                        if (bl && n4 < view.getTop() || !bl && n5 > view.getBottom()) {
                            bl4 = true;
                        }
                        if (bl2) {
                            view3 = view;
                            bl3 = bl2;
                            if (bl5) {
                                view3 = view;
                                bl3 = bl2;
                                if (bl4) {
                                    view3 = view2;
                                    bl3 = bl2;
                                }
                            }
                        } else if (bl5) {
                            view3 = view2;
                            bl3 = true;
                        } else {
                            view3 = view;
                            bl3 = bl2;
                            if (bl4) {
                                view3 = view2;
                                bl3 = bl2;
                            }
                        }
                    }
                }
            }
            view = view3;
            bl2 = bl3;
        }
        return view;
    }

    private void flingWithNestedDispatch(int n) {
        int n2 = this.getScrollY();
        boolean bl = !(n2 <= 0 && n <= 0 || n2 >= this.getScrollRange() && n >= 0);
        if (!this.dispatchNestedPreFling(0.0f, n)) {
            this.dispatchNestedFling(0.0f, n, bl);
            this.fling(n);
        }
    }

    private float getVerticalScrollFactorCompat() {
        if (this.mVerticalScrollFactor == 0.0f) {
            TypedValue typedValue = new TypedValue();
            Context context = this.getContext();
            if (context.getTheme().resolveAttribute(16842829, typedValue, true)) {
                this.mVerticalScrollFactor = typedValue.getDimension(context.getResources().getDisplayMetrics());
            } else {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
        }
        return this.mVerticalScrollFactor;
    }

    private boolean inChild(int n, int n2) {
        if (this.getChildCount() > 0) {
            int n3 = this.getScrollY();
            View view = this.getChildAt(0);
            if (n2 >= view.getTop() - n3 && n2 < view.getBottom() - n3 && n >= view.getLeft() && n < view.getRight()) {
                return true;
            }
            return false;
        }
        return false;
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
            return;
        }
        velocityTracker.clear();
    }

    private void initScrollView() {
        this.mScroller = new OverScroller(this.getContext());
        this.setFocusable(true);
        this.setDescendantFocusability(262144);
        this.setWillNotDraw(false);
        ViewConfiguration viewConfiguration = ViewConfiguration.get((Context)this.getContext());
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private boolean isOffScreen(View view) {
        return this.isWithinDeltaOfScreen(view, 0, this.getHeight()) ^ true;
    }

    private static boolean isViewDescendantOf(View view, View view2) {
        if (view == view2) {
            return true;
        }
        if ((view = view.getParent()) instanceof ViewGroup && NestedScrollView.isViewDescendantOf(view, view2)) {
            return true;
        }
        return false;
    }

    private boolean isWithinDeltaOfScreen(View view, int n, int n2) {
        view.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        if (this.mTempRect.bottom + n >= this.getScrollY() && this.mTempRect.top - n <= this.getScrollY() + n2) {
            return true;
        }
        return false;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int n = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(n) == this.mActivePointerId) {
            n = n == 0 ? 1 : 0;
            this.mLastMotionY = (int)motionEvent.getY(n);
            this.mActivePointerId = motionEvent.getPointerId(n);
            motionEvent = this.mVelocityTracker;
            if (motionEvent != null) {
                motionEvent.clear();
            }
        }
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private boolean scrollAndFocus(int n, int n2, int n3) {
        boolean bl = true;
        int n4 = this.getHeight();
        int n5 = this.getScrollY();
        n4 = n5 + n4;
        boolean bl2 = n == 33;
        View view = this.findFocusableViewInBounds(bl2, n2, n3);
        Object object = view;
        if (view == null) {
            object = this;
        }
        if (n2 >= n5 && n3 <= n4) {
            bl2 = false;
        } else {
            n2 = bl2 ? (n2 -= n5) : n3 - n4;
            this.doScrollY(n2);
            bl2 = bl;
        }
        if (object != this.findFocus()) {
            object.requestFocus(n);
        }
        return bl2;
    }

    private void scrollToChild(View view) {
        view.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        int n = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (n != 0) {
            this.scrollBy(0, n);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean bl) {
        int n = this.computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean bl2 = n != 0;
        if (bl2) {
            if (bl) {
                this.scrollBy(0, n);
                return bl2;
            }
            this.smoothScrollBy(0, n);
        }
        return bl2;
    }

    public void addView(View view) {
        if (this.getChildCount() <= 0) {
            super.addView(view);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View view, int n) {
        if (this.getChildCount() <= 0) {
            super.addView(view, n);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        if (this.getChildCount() <= 0) {
            super.addView(view, n, layoutParams);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        if (this.getChildCount() <= 0) {
            super.addView(view, layoutParams);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public boolean arrowScroll(int n) {
        View view;
        View view2 = view = this.findFocus();
        if (view == this) {
            view2 = null;
        }
        view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view2, n);
        int n2 = this.getMaxScrollAmount();
        if (view != null && this.isWithinDeltaOfScreen(view, n2, this.getHeight())) {
            view.getDrawingRect(this.mTempRect);
            this.offsetDescendantRectToMyCoords(view, this.mTempRect);
            this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            view.requestFocus(n);
        } else {
            int n3;
            int n4 = n2;
            if (n == 33 && this.getScrollY() < n4) {
                n3 = this.getScrollY();
            } else {
                n3 = n4;
                if (n == 130) {
                    n3 = n4;
                    if (this.getChildCount() > 0) {
                        view = this.getChildAt(0);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
                        n3 = Math.min(view.getBottom() + layoutParams.bottomMargin - (this.getScrollY() + this.getHeight() - this.getPaddingBottom()), n2);
                    }
                }
            }
            if (n3 == 0) {
                return false;
            }
            if (n != 130) {
                n3 = - n3;
            }
            this.doScrollY(n3);
        }
        if (view2 != null && view2.isFocused() && this.isOffScreen(view2)) {
            n = this.getDescendantFocusability();
            this.setDescendantFocusability(131072);
            this.requestFocus();
            this.setDescendantFocusability(n);
        }
        return true;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.mScroller.getCurrX();
            int n = this.mScroller.getCurrY();
            int n2 = n - this.mLastScrollerY;
            if (this.dispatchNestedPreScroll(0, n2, this.mScrollConsumed, null, 1)) {
                n2 -= this.mScrollConsumed[1];
            }
            if (n2 != 0) {
                int n3 = this.getScrollRange();
                int n4 = this.getScrollY();
                this.overScrollByCompat(0, n2, this.getScrollX(), n4, 0, n3, 0, 0, false);
                int n5 = this.getScrollY() - n4;
                if (!this.dispatchNestedScroll(0, n5, 0, n2 - n5, null, 1)) {
                    n2 = this.getOverScrollMode();
                    n2 = n2 != 0 && (n2 != 1 || n3 <= 0) ? 0 : 1;
                    if (n2 != 0) {
                        this.ensureGlows();
                        if (n <= 0 && n4 > 0) {
                            this.mEdgeGlowTop.onAbsorb((int)this.mScroller.getCurrVelocity());
                        } else if (n >= n3 && n4 < n3) {
                            this.mEdgeGlowBottom.onAbsorb((int)this.mScroller.getCurrVelocity());
                        }
                    }
                }
            }
            this.mLastScrollerY = n;
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
        if (this.hasNestedScrollingParent(1)) {
            this.stopNestedScroll(1);
        }
        this.mLastScrollerY = 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (this.getChildCount() == 0) {
            return 0;
        }
        int n = this.getHeight();
        int n2 = this.getScrollY();
        int n3 = n2 + n;
        int n4 = this.getVerticalFadingEdgeLength();
        int n5 = n2;
        if (rect.top > 0) {
            n5 = n2 + n4;
        }
        View view = this.getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        n2 = n3;
        if (rect.bottom < view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin) {
            n2 = n3 - n4;
        }
        n4 = n2;
        int n6 = 0;
        if (rect.bottom > n4 && rect.top > n5) {
            n2 = rect.height() > n ? 0 + (rect.top - n5) : 0 + (rect.bottom - n4);
            return Math.min(n2, view.getBottom() + layoutParams.bottomMargin - n3);
        }
        n2 = n6;
        if (rect.top >= n5) return n2;
        n2 = n6;
        if (rect.bottom >= n4) return n2;
        n2 = rect.height() > n ? 0 - (n4 - rect.bottom) : 0 - (n5 - rect.top);
        return Math.max(n2, - this.getScrollY());
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public int computeVerticalScrollRange() {
        int n = this.getChildCount();
        int n2 = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
        if (n == 0) {
            return n2;
        }
        View view = this.getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        n = view.getBottom() + layoutParams.bottomMargin;
        int n3 = this.getScrollY();
        int n4 = Math.max(0, n - n2);
        if (n3 < 0) {
            return n - n3;
        }
        n2 = n;
        if (n3 > n4) {
            n2 = n + (n3 - n4);
        }
        return n2;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!super.dispatchKeyEvent(keyEvent) && !this.executeKeyEvent(keyEvent)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean dispatchNestedFling(float f, float f2, boolean bl) {
        return this.mChildHelper.dispatchNestedFling(f, f2, bl);
    }

    @Override
    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mChildHelper.dispatchNestedPreFling(f, f2);
    }

    @Override
    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2) {
        return this.dispatchNestedPreScroll(n, n2, arrn, arrn2, 0);
    }

    @Override
    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2, int n3) {
        return this.mChildHelper.dispatchNestedPreScroll(n, n2, arrn, arrn2, n3);
    }

    @Override
    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn) {
        return this.dispatchNestedScroll(n, n2, n3, n4, arrn, 0);
    }

    @Override
    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn, int n5) {
        return this.mChildHelper.dispatchNestedScroll(n, n2, n3, n4, arrn, n5);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void draw(Canvas var1_1) {
        super.draw(var1_1);
        if (this.mEdgeGlowTop == null) return;
        var8_2 = this.getScrollY();
        if (this.mEdgeGlowTop.isFinished()) ** GOTO lbl28
        var9_3 = var1_1.save();
        var4_4 = this.getWidth();
        var7_5 = this.getHeight();
        var3_6 = 0;
        var6_7 = Math.min(0, var8_2);
        if (Build.VERSION.SDK_INT < 21) ** GOTO lbl-1000
        var2_8 = var4_4;
        if (this.getClipToPadding()) lbl-1000: // 2 sources:
        {
            var2_8 = var4_4 - (this.getPaddingLeft() + this.getPaddingRight());
            var3_6 = 0 + this.getPaddingLeft();
        }
        var5_9 = var7_5;
        var4_4 = var6_7;
        if (Build.VERSION.SDK_INT >= 21) {
            var5_9 = var7_5;
            var4_4 = var6_7;
            if (this.getClipToPadding()) {
                var5_9 = var7_5 - (this.getPaddingTop() + this.getPaddingBottom());
                var4_4 = var6_7 + this.getPaddingTop();
            }
        }
        var1_1.translate((float)var3_6, (float)var4_4);
        this.mEdgeGlowTop.setSize(var2_8, var5_9);
        if (this.mEdgeGlowTop.draw(var1_1)) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
        var1_1.restoreToCount(var9_3);
lbl28: // 2 sources:
        if (this.mEdgeGlowBottom.isFinished() != false) return;
        var9_3 = var1_1.save();
        var4_4 = this.getWidth();
        var6_7 = this.getHeight();
        var3_6 = 0;
        var7_5 = Math.max(this.getScrollRange(), var8_2) + var6_7;
        if (Build.VERSION.SDK_INT < 21) ** GOTO lbl-1000
        var2_8 = var4_4;
        if (this.getClipToPadding()) lbl-1000: // 2 sources:
        {
            var2_8 = var4_4 - (this.getPaddingLeft() + this.getPaddingRight());
            var3_6 = 0 + this.getPaddingLeft();
        }
        var5_9 = var6_7;
        var4_4 = var7_5;
        if (Build.VERSION.SDK_INT >= 21) {
            var5_9 = var6_7;
            var4_4 = var7_5;
            if (this.getClipToPadding()) {
                var5_9 = var6_7 - (this.getPaddingTop() + this.getPaddingBottom());
                var4_4 = var7_5 - this.getPaddingBottom();
            }
        }
        var1_1.translate((float)(var3_6 - var2_8), (float)var4_4);
        var1_1.rotate(180.0f, (float)var2_8, 0.0f);
        this.mEdgeGlowBottom.setSize(var2_8, var5_9);
        if (this.mEdgeGlowBottom.draw(var1_1)) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
        var1_1.restoreToCount(var9_3);
    }

    public boolean executeKeyEvent(@NonNull KeyEvent keyEvent) {
        this.mTempRect.setEmpty();
        boolean bl = this.canScroll();
        int n = 130;
        if (!bl) {
            if (this.isFocused() && keyEvent.getKeyCode() != 4) {
                View view = this.findFocus();
                keyEvent = view;
                if (view == this) {
                    keyEvent = null;
                }
                if ((keyEvent = FocusFinder.getInstance().findNextFocus((ViewGroup)this, (View)keyEvent, 130)) != null && keyEvent != this && keyEvent.requestFocus(130)) {
                    return true;
                }
                return false;
            }
            return false;
        }
        bl = false;
        if (keyEvent.getAction() == 0) {
            int n2 = keyEvent.getKeyCode();
            if (n2 != 19) {
                if (n2 != 20) {
                    if (n2 != 62) {
                        return false;
                    }
                    if (keyEvent.isShiftPressed()) {
                        n = 33;
                    }
                    this.pageScroll(n);
                    return false;
                }
                if (!keyEvent.isAltPressed()) {
                    return this.arrowScroll(130);
                }
                return this.fullScroll(130);
            }
            if (!keyEvent.isAltPressed()) {
                return this.arrowScroll(33);
            }
            bl = this.fullScroll(33);
        }
        return bl;
    }

    public void fling(int n) {
        if (this.getChildCount() > 0) {
            this.startNestedScroll(2, 1);
            this.mScroller.fling(this.getScrollX(), this.getScrollY(), 0, n, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            this.mLastScrollerY = this.getScrollY();
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    public boolean fullScroll(int n) {
        int n2 = n == 130 ? 1 : 0;
        int n3 = this.getHeight();
        Rect rect = this.mTempRect;
        rect.top = 0;
        rect.bottom = n3;
        if (n2 != 0 && (n2 = this.getChildCount()) > 0) {
            rect = this.getChildAt(n2 - 1);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)rect.getLayoutParams();
            this.mTempRect.bottom = rect.getBottom() + layoutParams.bottomMargin + this.getPaddingBottom();
            rect = this.mTempRect;
            rect.top = rect.bottom - n3;
        }
        return this.scrollAndFocus(n, this.mTempRect.top, this.mTempRect.bottom);
    }

    protected float getBottomFadingEdgeStrength() {
        if (this.getChildCount() == 0) {
            return 0.0f;
        }
        View view = this.getChildAt(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        int n = this.getVerticalFadingEdgeLength();
        int n2 = this.getHeight();
        int n3 = this.getPaddingBottom();
        n2 = view.getBottom() + layoutParams.bottomMargin - this.getScrollY() - (n2 - n3);
        if (n2 < n) {
            return (float)n2 / (float)n;
        }
        return 1.0f;
    }

    public int getMaxScrollAmount() {
        return (int)((float)this.getHeight() * 0.5f);
    }

    @Override
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    int getScrollRange() {
        int n = 0;
        if (this.getChildCount() > 0) {
            View view = this.getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
            n = Math.max(0, view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin - (this.getHeight() - this.getPaddingTop() - this.getPaddingBottom()));
        }
        return n;
    }

    protected float getTopFadingEdgeStrength() {
        if (this.getChildCount() == 0) {
            return 0.0f;
        }
        int n = this.getVerticalFadingEdgeLength();
        int n2 = this.getScrollY();
        if (n2 < n) {
            return (float)n2 / (float)n;
        }
        return 1.0f;
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return this.hasNestedScrollingParent(0);
    }

    @Override
    public boolean hasNestedScrollingParent(int n) {
        return this.mChildHelper.hasNestedScrollingParent(n);
    }

    public boolean isFillViewport() {
        return this.mFillViewport;
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }

    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }

    protected void measureChild(View view, int n, int n2) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        view.measure(NestedScrollView.getChildMeasureSpec((int)n, (int)(this.getPaddingLeft() + this.getPaddingRight()), (int)layoutParams.width), View.MeasureSpec.makeMeasureSpec((int)0, (int)0));
    }

    protected void measureChildWithMargins(View view, int n, int n2, int n3, int n4) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        view.measure(NestedScrollView.getChildMeasureSpec((int)n, (int)(this.getPaddingLeft() + this.getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + n2), (int)marginLayoutParams.width), View.MeasureSpec.makeMeasureSpec((int)(marginLayoutParams.topMargin + marginLayoutParams.bottomMargin), (int)0));
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsLaidOut = false;
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        float f;
        if ((motionEvent.getSource() & 2) != 0 && motionEvent.getAction() == 8 && !this.mIsBeingDragged && (f = motionEvent.getAxisValue(9)) != 0.0f) {
            int n = (int)(this.getVerticalScrollFactorCompat() * f);
            int n2 = this.getScrollRange();
            int n3 = this.getScrollY();
            int n4 = n3 - n;
            if (n4 < 0) {
                n = 0;
            } else {
                n = n4;
                if (n4 > n2) {
                    n = n2;
                }
            }
            if (n != n3) {
                super.scrollTo(this.getScrollX(), n);
                return true;
            }
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean onInterceptTouchEvent(MotionEvent object) {
        int n = object.getAction();
        if (n == 2 && this.mIsBeingDragged) {
            return true;
        }
        if ((n &= 255) != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        if (n != 6) return this.mIsBeingDragged;
                        this.onSecondaryPointerUp((MotionEvent)object);
                        return this.mIsBeingDragged;
                    }
                } else {
                    n = this.mActivePointerId;
                    if (n == -1) return this.mIsBeingDragged;
                    int n2 = object.findPointerIndex(n);
                    if (n2 == -1) {
                        object = new StringBuilder();
                        object.append("Invalid pointerId=");
                        object.append(n);
                        object.append(" in onInterceptTouchEvent");
                        Log.e((String)"NestedScrollView", (String)object.toString());
                        return this.mIsBeingDragged;
                    } else {
                        n = (int)object.getY(n2);
                        if (Math.abs(n - this.mLastMotionY) <= this.mTouchSlop || (2 & this.getNestedScrollAxes()) != 0) return this.mIsBeingDragged;
                        this.mIsBeingDragged = true;
                        this.mLastMotionY = n;
                        this.initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement((MotionEvent)object);
                        this.mNestedYOffset = 0;
                        object = this.getParent();
                        if (object == null) return this.mIsBeingDragged;
                        object.requestDisallowInterceptTouchEvent(true);
                    }
                    return this.mIsBeingDragged;
                }
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            this.recycleVelocityTracker();
            if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
            this.stopNestedScroll(0);
            return this.mIsBeingDragged;
        }
        n = (int)object.getY();
        if (!this.inChild((int)object.getX(), n)) {
            this.mIsBeingDragged = false;
            this.recycleVelocityTracker();
            return this.mIsBeingDragged;
        } else {
            this.mLastMotionY = n;
            this.mActivePointerId = object.getPointerId(0);
            this.initOrResetVelocityTracker();
            this.mVelocityTracker.addMovement((MotionEvent)object);
            this.mScroller.computeScrollOffset();
            this.mIsBeingDragged = true ^ this.mScroller.isFinished();
            this.startNestedScroll(2, 0);
        }
        return this.mIsBeingDragged;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.mIsLayoutDirty = false;
        View view = this.mChildToScrollTo;
        if (view != null && NestedScrollView.isViewDescendantOf(view, (View)this)) {
            this.scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!this.mIsLaidOut) {
            if (this.mSavedState != null) {
                this.scrollTo(this.getScrollX(), this.mSavedState.scrollPosition);
                this.mSavedState = null;
            }
            n = 0;
            if (this.getChildCount() > 0) {
                view = this.getChildAt(0);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
                n = view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }
            int n5 = this.getPaddingTop();
            int n6 = this.getPaddingBottom();
            n3 = this.getScrollY();
            n = NestedScrollView.clamp(n3, n4 - n2 - n5 - n6, n);
            if (n != n3) {
                this.scrollTo(this.getScrollX(), n);
            }
        }
        this.scrollTo(this.getScrollX(), this.getScrollY());
        this.mIsLaidOut = true;
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        if (!this.mFillViewport) {
            return;
        }
        if (View.MeasureSpec.getMode((int)n2) == 0) {
            return;
        }
        if (this.getChildCount() > 0) {
            int n3;
            View view = this.getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
            n2 = view.getMeasuredHeight();
            if (n2 < (n3 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom() - layoutParams.topMargin - layoutParams.bottomMargin)) {
                view.measure(NestedScrollView.getChildMeasureSpec((int)n, (int)(this.getPaddingLeft() + this.getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin), (int)layoutParams.width), View.MeasureSpec.makeMeasureSpec((int)n3, (int)1073741824));
            }
        }
    }

    @Override
    public boolean onNestedFling(View view, float f, float f2, boolean bl) {
        if (!bl) {
            this.flingWithNestedDispatch((int)f2);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View view, float f, float f2) {
        return this.dispatchNestedPreFling(f, f2);
    }

    @Override
    public void onNestedPreScroll(View view, int n, int n2, int[] arrn) {
        this.onNestedPreScroll(view, n, n2, arrn, 0);
    }

    @Override
    public void onNestedPreScroll(@NonNull View view, int n, int n2, @NonNull int[] arrn, int n3) {
        this.dispatchNestedPreScroll(n, n2, arrn, null, n3);
    }

    @Override
    public void onNestedScroll(View view, int n, int n2, int n3, int n4) {
        this.onNestedScroll(view, n, n2, n3, n4, 0);
    }

    @Override
    public void onNestedScroll(View view, int n, int n2, int n3, int n4, int n5) {
        n = this.getScrollY();
        this.scrollBy(0, n4);
        n = this.getScrollY() - n;
        this.dispatchNestedScroll(0, n, 0, n4 - n, null, n5);
    }

    @Override
    public void onNestedScrollAccepted(View view, View view2, int n) {
        this.onNestedScrollAccepted(view, view2, n, 0);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View view, @NonNull View view2, int n, int n2) {
        this.mParentHelper.onNestedScrollAccepted(view, view2, n, n2);
        this.startNestedScroll(2, n2);
    }

    protected void onOverScrolled(int n, int n2, boolean bl, boolean bl2) {
        super.scrollTo(n, n2);
    }

    protected boolean onRequestFocusInDescendants(int n, Rect rect) {
        int n2;
        if (n == 2) {
            n2 = 130;
        } else {
            n2 = n;
            if (n == 1) {
                n2 = 33;
            }
        }
        View view = rect == null ? FocusFinder.getInstance().findNextFocus((ViewGroup)this, null, n2) : FocusFinder.getInstance().findNextFocusFromRect((ViewGroup)this, rect, n2);
        if (view == null) {
            return false;
        }
        if (this.isOffScreen(view)) {
            return false;
        }
        return view.requestFocus(n2, rect);
    }

    protected void onRestoreInstanceState(Parcelable object) {
        if (!(object instanceof SavedState)) {
            super.onRestoreInstanceState((Parcelable)object);
            return;
        }
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        this.mSavedState = object;
        this.requestLayout();
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.scrollPosition = this.getScrollY();
        return savedState;
    }

    protected void onScrollChanged(int n, int n2, int n3, int n4) {
        super.onScrollChanged(n, n2, n3, n4);
        OnScrollChangeListener onScrollChangeListener = this.mOnScrollChangeListener;
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChange(this, n, n2, n3, n4);
        }
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        View view = this.findFocus();
        if (view != null) {
            if (this == view) {
                return;
            }
            if (this.isWithinDeltaOfScreen(view, 0, n4)) {
                view.getDrawingRect(this.mTempRect);
                this.offsetDescendantRectToMyCoords(view, this.mTempRect);
                this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            }
            return;
        }
    }

    @Override
    public boolean onStartNestedScroll(View view, View view2, int n) {
        return this.onStartNestedScroll(view, view2, n, 0);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View view, @NonNull View view2, int n, int n2) {
        if ((n & 2) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onStopNestedScroll(View view) {
        this.onStopNestedScroll(view, 0);
    }

    @Override
    public void onStopNestedScroll(@NonNull View view, int n) {
        this.mParentHelper.onStopNestedScroll(view, n);
        this.stopNestedScroll(n);
    }

    public boolean onTouchEvent(MotionEvent edgeEffect) {
        this.initVelocityTrackerIfNotExists();
        MotionEvent motionEvent = MotionEvent.obtain((MotionEvent)edgeEffect);
        int n = edgeEffect.getActionMasked();
        if (n == 0) {
            this.mNestedYOffset = 0;
        }
        motionEvent.offsetLocation(0.0f, (float)this.mNestedYOffset);
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        if (n != 5) {
                            if (n == 6) {
                                this.onSecondaryPointerUp((MotionEvent)edgeEffect);
                                this.mLastMotionY = (int)edgeEffect.getY(edgeEffect.findPointerIndex(this.mActivePointerId));
                            }
                        } else {
                            n = edgeEffect.getActionIndex();
                            this.mLastMotionY = (int)edgeEffect.getY(n);
                            this.mActivePointerId = edgeEffect.getPointerId(n);
                        }
                    } else {
                        if (this.mIsBeingDragged && this.getChildCount() > 0 && this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                            ViewCompat.postInvalidateOnAnimation((View)this);
                        }
                        this.mActivePointerId = -1;
                        this.endDrag();
                    }
                } else {
                    int n2 = edgeEffect.findPointerIndex(this.mActivePointerId);
                    if (n2 == -1) {
                        edgeEffect = new StringBuilder();
                        edgeEffect.append("Invalid pointerId=");
                        edgeEffect.append(this.mActivePointerId);
                        edgeEffect.append(" in onTouchEvent");
                        Log.e((String)"NestedScrollView", (String)edgeEffect.toString());
                    } else {
                        int n3 = (int)edgeEffect.getY(n2);
                        int n4 = n = this.mLastMotionY - n3;
                        if (this.dispatchNestedPreScroll(0, n, this.mScrollConsumed, this.mScrollOffset, 0)) {
                            n4 = n - this.mScrollConsumed[1];
                            motionEvent.offsetLocation(0.0f, (float)this.mScrollOffset[1]);
                            this.mNestedYOffset += this.mScrollOffset[1];
                        }
                        n = n4;
                        if (!this.mIsBeingDragged) {
                            n = n4;
                            if (Math.abs(n4) > this.mTouchSlop) {
                                ViewParent viewParent = this.getParent();
                                if (viewParent != null) {
                                    viewParent.requestDisallowInterceptTouchEvent(true);
                                }
                                this.mIsBeingDragged = true;
                                n = n4 > 0 ? n4 - this.mTouchSlop : n4 + this.mTouchSlop;
                            }
                        }
                        if (this.mIsBeingDragged) {
                            int n5;
                            this.mLastMotionY = n3 - this.mScrollOffset[1];
                            int n6 = this.getScrollY();
                            n3 = this.getScrollRange();
                            n4 = this.getOverScrollMode();
                            n4 = n4 != 0 && (n4 != 1 || n3 <= 0) ? 0 : 1;
                            if (this.overScrollByCompat(0, n, 0, this.getScrollY(), 0, n3, 0, 0, true) && !this.hasNestedScrollingParent(0)) {
                                this.mVelocityTracker.clear();
                            }
                            if (this.dispatchNestedScroll(0, n5 = this.getScrollY() - n6, 0, n - n5, this.mScrollOffset, 0)) {
                                n = this.mLastMotionY;
                                edgeEffect = this.mScrollOffset;
                                this.mLastMotionY = n - edgeEffect[1];
                                motionEvent.offsetLocation(0.0f, (float)((Object)edgeEffect[1]));
                                this.mNestedYOffset += this.mScrollOffset[1];
                            } else if (n4 != 0) {
                                this.ensureGlows();
                                n4 = n6 + n;
                                if (n4 < 0) {
                                    EdgeEffectCompat.onPull(this.mEdgeGlowTop, (float)n / (float)this.getHeight(), edgeEffect.getX(n2) / (float)this.getWidth());
                                    if (!this.mEdgeGlowBottom.isFinished()) {
                                        this.mEdgeGlowBottom.onRelease();
                                    }
                                } else if (n4 > n3) {
                                    EdgeEffectCompat.onPull(this.mEdgeGlowBottom, (float)n / (float)this.getHeight(), 1.0f - edgeEffect.getX(n2) / (float)this.getWidth());
                                    if (!this.mEdgeGlowTop.isFinished()) {
                                        this.mEdgeGlowTop.onRelease();
                                    }
                                }
                                edgeEffect = this.mEdgeGlowTop;
                                if (!(edgeEffect == null || edgeEffect.isFinished() && this.mEdgeGlowBottom.isFinished())) {
                                    ViewCompat.postInvalidateOnAnimation((View)this);
                                }
                            }
                        }
                    }
                }
            } else {
                edgeEffect = this.mVelocityTracker;
                edgeEffect.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                n = (int)edgeEffect.getYVelocity(this.mActivePointerId);
                if (Math.abs(n) > this.mMinimumVelocity) {
                    this.flingWithNestedDispatch(- n);
                } else if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                this.mActivePointerId = -1;
                this.endDrag();
            }
        } else {
            ViewParent viewParent;
            boolean bl;
            if (this.getChildCount() == 0) {
                return false;
            }
            this.mIsBeingDragged = bl = this.mScroller.isFinished() ^ true;
            if (bl && (viewParent = this.getParent()) != null) {
                viewParent.requestDisallowInterceptTouchEvent(true);
            }
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            this.mLastMotionY = (int)edgeEffect.getY();
            this.mActivePointerId = edgeEffect.getPointerId(0);
            this.startNestedScroll(2, 0);
        }
        edgeEffect = this.mVelocityTracker;
        if (edgeEffect != null) {
            edgeEffect.addMovement(motionEvent);
        }
        motionEvent.recycle();
        return true;
    }

    boolean overScrollByCompat(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        boolean bl2;
        int n9 = this.getOverScrollMode();
        boolean bl3 = this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent();
        boolean bl4 = this.computeVerticalScrollRange() > this.computeVerticalScrollExtent();
        bl3 = n9 == 0 || n9 == 1 && bl3;
        bl4 = n9 == 0 || n9 == 1 && bl4;
        n = !bl3 ? 0 : n7;
        n4 += n2;
        n2 = !bl4 ? 0 : n8;
        n7 = - n;
        n5 = - n2;
        n2 += n6;
        if ((n3 += n) > (n += n5)) {
            bl = true;
        } else if (n3 < n7) {
            n = n7;
            bl = true;
        } else {
            bl = false;
            n = n3;
        }
        if (n4 > n2) {
            bl2 = true;
        } else if (n4 < n5) {
            n2 = n5;
            bl2 = true;
        } else {
            n2 = n4;
            bl2 = false;
        }
        if (bl2 && !this.hasNestedScrollingParent(1)) {
            this.mScroller.springBack(n, n2, 0, 0, 0, this.getScrollRange());
        }
        this.onOverScrolled(n, n2, bl, bl2);
        if (!bl && !bl2) {
            return false;
        }
        return true;
    }

    public boolean pageScroll(int n) {
        Rect rect;
        int n2 = n == 130 ? 1 : 0;
        int n3 = this.getHeight();
        if (n2 != 0) {
            this.mTempRect.top = this.getScrollY() + n3;
            n2 = this.getChildCount();
            if (n2 > 0) {
                rect = this.getChildAt(n2 - 1);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)rect.getLayoutParams();
                n2 = rect.getBottom() + layoutParams.bottomMargin + this.getPaddingBottom();
                if (this.mTempRect.top + n3 > n2) {
                    this.mTempRect.top = n2 - n3;
                }
            }
        } else {
            this.mTempRect.top = this.getScrollY() - n3;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
        }
        rect = this.mTempRect;
        rect.bottom = rect.top + n3;
        return this.scrollAndFocus(n, this.mTempRect.top, this.mTempRect.bottom);
    }

    public void requestChildFocus(View view, View view2) {
        if (!this.mIsLayoutDirty) {
            this.scrollToChild(view2);
        } else {
            this.mChildToScrollTo = view2;
        }
        super.requestChildFocus(view, view2);
    }

    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean bl) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return this.scrollToChildRect(rect, bl);
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        if (bl) {
            this.recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(bl);
    }

    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    public void scrollTo(int n, int n2) {
        if (this.getChildCount() > 0) {
            View view = this.getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
            int n3 = this.getWidth();
            int n4 = this.getPaddingLeft();
            int n5 = this.getPaddingRight();
            int n6 = view.getWidth();
            int n7 = layoutParams.leftMargin;
            int n8 = layoutParams.rightMargin;
            int n9 = this.getHeight();
            int n10 = this.getPaddingTop();
            int n11 = this.getPaddingBottom();
            int n12 = view.getHeight();
            int n13 = layoutParams.topMargin;
            int n14 = layoutParams.bottomMargin;
            n = NestedScrollView.clamp(n, n3 - n4 - n5, n6 + n7 + n8);
            n2 = NestedScrollView.clamp(n2, n9 - n10 - n11, n12 + n13 + n14);
            if (n != this.getScrollX() || n2 != this.getScrollY()) {
                super.scrollTo(n, n2);
            }
        }
    }

    public void setFillViewport(boolean bl) {
        if (bl != this.mFillViewport) {
            this.mFillViewport = bl;
            this.requestLayout();
        }
    }

    @Override
    public void setNestedScrollingEnabled(boolean bl) {
        this.mChildHelper.setNestedScrollingEnabled(bl);
    }

    public void setOnScrollChangeListener(@Nullable OnScrollChangeListener onScrollChangeListener) {
        this.mOnScrollChangeListener = onScrollChangeListener;
    }

    public void setSmoothScrollingEnabled(boolean bl) {
        this.mSmoothScrollingEnabled = bl;
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public final void smoothScrollBy(int n, int n2) {
        if (this.getChildCount() == 0) {
            return;
        }
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
            View view = this.getChildAt(0);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
            int n3 = view.getHeight();
            int n4 = layoutParams.topMargin;
            int n5 = layoutParams.bottomMargin;
            int n6 = this.getHeight();
            int n7 = this.getPaddingTop();
            int n8 = this.getPaddingBottom();
            n = this.getScrollY();
            n2 = Math.max(0, Math.min(n + n2, Math.max(0, n3 + n4 + n5 - (n6 - n7 - n8))));
            this.mLastScrollerY = this.getScrollY();
            this.mScroller.startScroll(this.getScrollX(), n, 0, n2 - n);
            ViewCompat.postInvalidateOnAnimation((View)this);
        } else {
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            this.scrollBy(n, n2);
        }
        this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    }

    public final void smoothScrollTo(int n, int n2) {
        this.smoothScrollBy(n - this.getScrollX(), n2 - this.getScrollY());
    }

    @Override
    public boolean startNestedScroll(int n) {
        return this.startNestedScroll(n, 0);
    }

    @Override
    public boolean startNestedScroll(int n, int n2) {
        return this.mChildHelper.startNestedScroll(n, n2);
    }

    @Override
    public void stopNestedScroll() {
        this.stopNestedScroll(0);
    }

    @Override
    public void stopNestedScroll(int n) {
        this.mChildHelper.stopNestedScroll(n);
    }

    static class AccessibilityDelegate
    extends AccessibilityDelegateCompat {
        AccessibilityDelegate() {
        }

        @Override
        public void onInitializeAccessibilityEvent(View object, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent((View)object, accessibilityEvent);
            object = (NestedScrollView)object;
            accessibilityEvent.setClassName((CharSequence)ScrollView.class.getName());
            boolean bl = object.getScrollRange() > 0;
            accessibilityEvent.setScrollable(bl);
            accessibilityEvent.setScrollX(object.getScrollX());
            accessibilityEvent.setScrollY(object.getScrollY());
            AccessibilityRecordCompat.setMaxScrollX((AccessibilityRecord)accessibilityEvent, object.getScrollX());
            AccessibilityRecordCompat.setMaxScrollY((AccessibilityRecord)accessibilityEvent, object.getScrollRange());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            int n;
            super.onInitializeAccessibilityNodeInfo((View)object, accessibilityNodeInfoCompat);
            object = (NestedScrollView)object;
            accessibilityNodeInfoCompat.setClassName(ScrollView.class.getName());
            if (object.isEnabled() && (n = object.getScrollRange()) > 0) {
                accessibilityNodeInfoCompat.setScrollable(true);
                if (object.getScrollY() > 0) {
                    accessibilityNodeInfoCompat.addAction(8192);
                }
                if (object.getScrollY() < n) {
                    accessibilityNodeInfoCompat.addAction(4096);
                }
            }
        }

        @Override
        public boolean performAccessibilityAction(View object, int n, Bundle bundle) {
            if (super.performAccessibilityAction((View)object, n, bundle)) {
                return true;
            }
            if (!(object = (NestedScrollView)object).isEnabled()) {
                return false;
            }
            if (n != 4096) {
                if (n != 8192) {
                    return false;
                }
                n = object.getHeight();
                int n2 = object.getPaddingBottom();
                int n3 = object.getPaddingTop();
                n = Math.max(object.getScrollY() - (n - n2 - n3), 0);
                if (n != object.getScrollY()) {
                    object.smoothScrollTo(0, n);
                    return true;
                }
                return false;
            }
            n = object.getHeight();
            int n4 = object.getPaddingBottom();
            int n5 = object.getPaddingTop();
            n = Math.min(object.getScrollY() + (n - n4 - n5), object.getScrollRange());
            if (n != object.getScrollY()) {
                object.smoothScrollTo(0, n);
                return true;
            }
            return false;
        }
    }

    public static interface OnScrollChangeListener {
        public void onScrollChange(NestedScrollView var1, int var2, int var3, int var4, int var5);
    }

    static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        public int scrollPosition;

        SavedState(Parcel parcel) {
            super(parcel);
            this.scrollPosition = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HorizontalScrollView.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode((Object)this)));
            stringBuilder.append(" scrollPosition=");
            stringBuilder.append(this.scrollPosition);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.scrollPosition);
        }

    }

}

