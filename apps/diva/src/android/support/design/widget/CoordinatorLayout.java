/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.AbsSavedState
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewGroup$OnHierarchyChangeListener
 *  android.view.ViewParent
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnPreDrawListener
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.design.R;
import android.support.design.widget.CoordinatorLayoutInsetsHelper;
import android.support.design.widget.CoordinatorLayoutInsetsHelperLollipop;
import android.support.design.widget.ThemeUtils;
import android.support.design.widget.ViewGroupUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.AbsSavedState;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorLayout
extends ViewGroup
implements NestedScrollingParent {
    static final Class<?>[] CONSTRUCTOR_PARAMS;
    static final CoordinatorLayoutInsetsHelper INSETS_HELPER;
    static final String TAG = "CoordinatorLayout";
    static final Comparator<View> TOP_SORTED_CHILDREN_COMPARATOR;
    private static final int TYPE_ON_INTERCEPT = 0;
    private static final int TYPE_ON_TOUCH = 1;
    static final String WIDGET_PACKAGE_NAME;
    static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors;
    private View mBehaviorTouchView;
    private final List<View> mDependencySortedChildren;
    private boolean mDrawStatusBarBackground;
    private boolean mIsAttachedToWindow;
    private int[] mKeylines;
    private WindowInsetsCompat mLastInsets;
    final Comparator<View> mLayoutDependencyComparator;
    private boolean mNeedsPreDrawListener;
    private View mNestedScrollingDirectChild;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private View mNestedScrollingTarget;
    private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
    private OnPreDrawListener mOnPreDrawListener;
    private Paint mScrimPaint;
    private Drawable mStatusBarBackground;
    private final List<View> mTempDependenciesList;
    private final int[] mTempIntPair;
    private final List<View> mTempList1;
    private final Rect mTempRect1;
    private final Rect mTempRect2;
    private final Rect mTempRect3;

    /*
     * Enabled aggressive block sorting
     */
    static {
        Object object = CoordinatorLayout.class.getPackage();
        object = object != null ? object.getName() : null;
        WIDGET_PACKAGE_NAME = object;
        if (Build.VERSION.SDK_INT >= 21) {
            TOP_SORTED_CHILDREN_COMPARATOR = new ViewElevationComparator();
            INSETS_HELPER = new CoordinatorLayoutInsetsHelperLollipop();
        } else {
            TOP_SORTED_CHILDREN_COMPARATOR = null;
            INSETS_HELPER = null;
        }
        CONSTRUCTOR_PARAMS = new Class[]{Context.class, AttributeSet.class};
        sConstructors = new ThreadLocal();
    }

    public CoordinatorLayout(Context context) {
        this(context, null);
    }

    public CoordinatorLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CoordinatorLayout(Context arrn, AttributeSet attributeSet, int n) {
        super((Context)arrn, attributeSet, n);
        this.mLayoutDependencyComparator = new Comparator<View>(){

            @Override
            public int compare(View view, View view2) {
                if (view == view2) {
                    return 0;
                }
                if (((LayoutParams)view.getLayoutParams()).dependsOn(CoordinatorLayout.this, view, view2)) {
                    return 1;
                }
                if (((LayoutParams)view2.getLayoutParams()).dependsOn(CoordinatorLayout.this, view2, view)) {
                    return -1;
                }
                return 0;
            }
        };
        this.mDependencySortedChildren = new ArrayList<View>();
        this.mTempList1 = new ArrayList<View>();
        this.mTempDependenciesList = new ArrayList<View>();
        this.mTempRect1 = new Rect();
        this.mTempRect2 = new Rect();
        this.mTempRect3 = new Rect();
        this.mTempIntPair = new int[2];
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        ThemeUtils.checkAppCompatTheme((Context)arrn);
        attributeSet = arrn.obtainStyledAttributes(attributeSet, R.styleable.CoordinatorLayout, n, R.style.Widget_Design_CoordinatorLayout);
        n = attributeSet.getResourceId(R.styleable.CoordinatorLayout_keylines, 0);
        if (n != 0) {
            arrn = arrn.getResources();
            this.mKeylines = arrn.getIntArray(n);
            float f = arrn.getDisplayMetrics().density;
            int n2 = this.mKeylines.length;
            for (n = 0; n < n2; ++n) {
                arrn = this.mKeylines;
                arrn[n] = (int)((float)arrn[n] * f);
            }
        }
        this.mStatusBarBackground = attributeSet.getDrawable(R.styleable.CoordinatorLayout_statusBarBackground);
        attributeSet.recycle();
        if (INSETS_HELPER != null) {
            INSETS_HELPER.setupForWindowInsets((View)this, new ApplyInsetsListener());
        }
        super.setOnHierarchyChangeListener((ViewGroup.OnHierarchyChangeListener)new HierarchyChangeListener());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void dispatchChildApplyWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        if (!windowInsetsCompat.isConsumed()) {
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view = this.getChildAt(i);
                WindowInsetsCompat windowInsetsCompat2 = windowInsetsCompat;
                if (ViewCompat.getFitsSystemWindows(view)) {
                    Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
                    windowInsetsCompat2 = windowInsetsCompat;
                    if (behavior != null && (windowInsetsCompat2 = behavior.onApplyWindowInsets(this, view, windowInsetsCompat)).isConsumed() || (windowInsetsCompat2 = ViewCompat.dispatchApplyWindowInsets(view, windowInsetsCompat2)).isConsumed()) break;
                }
                windowInsetsCompat = windowInsetsCompat2;
            }
        }
    }

    private int getKeyline(int n) {
        if (this.mKeylines == null) {
            Log.e((String)"CoordinatorLayout", (String)("No keylines defined for " + this + " - attempted index lookup " + n));
            return 0;
        }
        if (n < 0 || n >= this.mKeylines.length) {
            Log.e((String)"CoordinatorLayout", (String)("Keyline index " + n + " out of range for " + this));
            return 0;
        }
        return this.mKeylines[n];
    }

    /*
     * Enabled aggressive block sorting
     */
    private void getTopSortedChildren(List<View> list) {
        list.clear();
        boolean bl = this.isChildrenDrawingOrderEnabled();
        int n = this.getChildCount();
        for (int i = n - 1; i >= 0; --i) {
            int n2 = bl ? this.getChildDrawingOrder(n, i) : i;
            list.add(this.getChildAt(n2));
        }
        if (TOP_SORTED_CHILDREN_COMPARATOR != null) {
            Collections.sort(list, TOP_SORTED_CHILDREN_COMPARATOR);
        }
    }

    private void layoutChild(View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = this.mTempRect1;
        rect.set(this.getPaddingLeft() + layoutParams.leftMargin, this.getPaddingTop() + layoutParams.topMargin, this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin, this.getHeight() - this.getPaddingBottom() - layoutParams.bottomMargin);
        if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this) && !ViewCompat.getFitsSystemWindows(view)) {
            rect.left += this.mLastInsets.getSystemWindowInsetLeft();
            rect.top += this.mLastInsets.getSystemWindowInsetTop();
            rect.right -= this.mLastInsets.getSystemWindowInsetRight();
            rect.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
        }
        Rect rect2 = this.mTempRect2;
        GravityCompat.apply(CoordinatorLayout.resolveGravity(layoutParams.gravity), view.getMeasuredWidth(), view.getMeasuredHeight(), rect, rect2, n);
        view.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
    }

    private void layoutChildWithAnchor(View view, View view2, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        layoutParams = this.mTempRect1;
        Rect rect = this.mTempRect2;
        this.getDescendantRect(view2, (Rect)layoutParams);
        this.getDesiredAnchoredChildRect(view, n, (Rect)layoutParams, rect);
        view.layout(rect.left, rect.top, rect.right, rect.bottom);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void layoutChildWithKeyline(View view, int n, int n2) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n3 = GravityCompat.getAbsoluteGravity(CoordinatorLayout.resolveKeylineGravity(layoutParams.gravity), n2);
        int n4 = this.getWidth();
        int n5 = this.getHeight();
        int n6 = view.getMeasuredWidth();
        int n7 = view.getMeasuredHeight();
        int n8 = n;
        if (n2 == 1) {
            n8 = n4 - n;
        }
        n = this.getKeyline(n8) - n6;
        n2 = 0;
        switch (n3 & 7) {
            case 5: {
                n += n6;
            }
            default: {
                break;
            }
            case 1: {
                n += n6 / 2;
            }
        }
        switch (n3 & 112) {
            case 80: {
                n2 = 0 + n7;
            }
            default: {
                break;
            }
            case 16: {
                n2 = 0 + n7 / 2;
            }
        }
        n = Math.max(this.getPaddingLeft() + layoutParams.leftMargin, Math.min(n, n4 - this.getPaddingRight() - n6 - layoutParams.rightMargin));
        n2 = Math.max(this.getPaddingTop() + layoutParams.topMargin, Math.min(n2, n5 - this.getPaddingBottom() - n7 - layoutParams.bottomMargin));
        view.layout(n, n2, n + n6, n2 + n7);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static Behavior parseBehavior(Context object, AttributeSet attributeSet, String string2) {
        if (TextUtils.isEmpty((CharSequence)string2)) {
            return null;
        }
        if (string2.startsWith(".")) {
            string2 = object.getPackageName() + string2;
        } else if (string2.indexOf(46) < 0 && !TextUtils.isEmpty((CharSequence)WIDGET_PACKAGE_NAME)) {
            string2 = WIDGET_PACKAGE_NAME + '.' + string2;
        }
        try {
            Constructor constructor = sConstructors.get();
            Map<String, Constructor<Behavior>> map = constructor;
            if (constructor == null) {
                map = new HashMap<String, Constructor<Behavior>>();
                sConstructors.set(map);
            }
            Constructor<Behavior> constructor2 = map.get(string2);
            constructor = constructor2;
            if (constructor2 == null) {
                constructor = Class.forName(string2, true, object.getClassLoader()).getConstructor(CONSTRUCTOR_PARAMS);
                constructor.setAccessible(true);
                map.put(string2, constructor);
            }
            return (Behavior)constructor.newInstance(new Object[]{object, attributeSet});
        }
        catch (Exception exception) {
            throw new RuntimeException("Could not inflate Behavior subclass " + string2, exception);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean performIntercept(MotionEvent var1_1, int var2_2) {
        var8_3 = false;
        var3_4 = false;
        var13_5 = null;
        var6_6 = MotionEventCompat.getActionMasked(var1_1);
        var15_7 = this.mTempList1;
        this.getTopSortedChildren(var15_7);
        var7_8 = var15_7.size();
        var4_9 = 0;
        do {
            var9_11 = var8_3;
            if (var4_9 >= var7_8) break;
            var16_15 = var15_7.get(var4_9);
            var14_14 = (LayoutParams)var16_15.getLayoutParams();
            var17_16 = var14_14.getBehavior();
            if (!var8_3 && !var3_4 || var6_6 == 0) ** GOTO lbl40
            var14_14 = var13_5;
            var10_12 = var8_3;
            var5_10 = var3_4;
            if (var17_16 == null) ** GOTO lbl65
            var14_14 = var13_5;
            if (var13_5 == null) {
                var11_13 = SystemClock.uptimeMillis();
                var14_14 = MotionEvent.obtain((long)var11_13, (long)var11_13, (int)3, (float)0.0f, (float)0.0f, (int)0);
            }
            switch (var2_2) {
                default: {
                    var5_10 = var3_4;
                    var10_12 = var8_3;
                    ** GOTO lbl65
                }
                case 0: {
                    var17_16.onInterceptTouchEvent(this, var16_15, (MotionEvent)var14_14);
                    var10_12 = var8_3;
                    var5_10 = var3_4;
                    ** GOTO lbl65
                }
                case 1: 
            }
            var17_16.onTouchEvent(this, var16_15, (MotionEvent)var14_14);
            var10_12 = var8_3;
            var5_10 = var3_4;
            ** GOTO lbl65
lbl40: // 1 sources:
            var9_11 = var8_3;
            if (!var8_3) {
                var9_11 = var8_3;
                if (var17_16 != null) {
                    switch (var2_2) {
                        case 0: {
                            var8_3 = var17_16.onInterceptTouchEvent(this, var16_15, var1_1);
                            break;
                        }
                        case 1: {
                            var8_3 = var17_16.onTouchEvent(this, var16_15, var1_1);
                        }
                    }
                    var9_11 = var8_3;
                    if (var8_3) {
                        this.mBehaviorTouchView = var16_15;
                        var9_11 = var8_3;
                    }
                }
            }
            var10_12 = var14_14.didBlockInteraction();
            var8_3 = var14_14.isBlockingInteractionBelow(this, var16_15);
            var3_4 = var8_3 != false && var10_12 == false;
            var14_14 = var13_5;
            var10_12 = var9_11;
            var5_10 = var3_4;
            if (var8_3) {
                var14_14 = var13_5;
                var10_12 = var9_11;
                var5_10 = var3_4;
                if (!var3_4) break;
            }
lbl65: // 7 sources:
            ++var4_9;
            var13_5 = var14_14;
            var8_3 = var10_12;
            var3_4 = var5_10;
        } while (true);
        var15_7.clear();
        return var9_11;
    }

    private void prepareChildren() {
        this.mDependencySortedChildren.clear();
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            this.getResolvedLayoutParams(view).findAnchorView(this, view);
            this.mDependencySortedChildren.add(view);
        }
        CoordinatorLayout.selectionSort(this.mDependencySortedChildren, this.mLayoutDependencyComparator);
    }

    private void resetTouchBehaviors() {
        if (this.mBehaviorTouchView != null) {
            Behavior behavior = ((LayoutParams)this.mBehaviorTouchView.getLayoutParams()).getBehavior();
            if (behavior != null) {
                long l = SystemClock.uptimeMillis();
                MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
                behavior.onTouchEvent(this, this.mBehaviorTouchView, motionEvent);
                motionEvent.recycle();
            }
            this.mBehaviorTouchView = null;
        }
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            ((LayoutParams)this.getChildAt(i).getLayoutParams()).resetTouchBehaviorTracking();
        }
    }

    private static int resolveAnchoredChildGravity(int n) {
        int n2 = n;
        if (n == 0) {
            n2 = 17;
        }
        return n2;
    }

    private static int resolveGravity(int n) {
        int n2 = n;
        if (n == 0) {
            n2 = 8388659;
        }
        return n2;
    }

    private static int resolveKeylineGravity(int n) {
        int n2 = n;
        if (n == 0) {
            n2 = 8388661;
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void selectionSort(List<View> list, Comparator<View> comparator) {
        int n;
        if (list == null || list.size() < 2) {
            return;
        }
        View[] arrview = new View[list.size()];
        list.toArray((T[])arrview);
        int n2 = arrview.length;
        for (n = 0; n < n2; ++n) {
            int n3 = n;
            for (int i = n + 1; i < n2; ++i) {
                int n4 = n3;
                if (comparator.compare(arrview[i], arrview[n3]) < 0) {
                    n4 = i;
                }
                n3 = n4;
            }
            if (n == n3) continue;
            View view = arrview[n3];
            arrview[n3] = arrview[n];
            arrview[n] = view;
        }
        list.clear();
        n = 0;
        while (n < n2) {
            list.add(arrview[n]);
            ++n;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        boolean bl = true;
        if (this.mLastInsets == windowInsetsCompat) return;
        this.mLastInsets = windowInsetsCompat;
        boolean bl2 = windowInsetsCompat != null && windowInsetsCompat.getSystemWindowInsetTop() > 0;
        this.mDrawStatusBarBackground = bl2;
        bl2 = !this.mDrawStatusBarBackground && this.getBackground() == null ? bl : false;
        this.setWillNotDraw(bl2);
        this.dispatchChildApplyWindowInsets(windowInsetsCompat);
        this.requestLayout();
    }

    void addPreDrawListener() {
        if (this.mIsAttachedToWindow) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new OnPreDrawListener();
            }
            this.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this.mOnPreDrawListener);
        }
        this.mNeedsPreDrawListener = true;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams)) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    void dispatchDependentViewRemoved(View view) {
        int n = this.mDependencySortedChildren.size();
        boolean bl = false;
        int n2 = 0;
        while (n2 < n) {
            boolean bl2;
            View view2 = this.mDependencySortedChildren.get(n2);
            if (view2 == view) {
                bl2 = true;
            } else {
                bl2 = bl;
                if (bl) {
                    LayoutParams layoutParams = (LayoutParams)view2.getLayoutParams();
                    Behavior behavior = layoutParams.getBehavior();
                    bl2 = bl;
                    if (behavior != null) {
                        bl2 = bl;
                        if (layoutParams.dependsOn(this, view2, view)) {
                            behavior.onDependentViewRemoved(this, view2, view);
                            bl2 = bl;
                        }
                    }
                }
            }
            ++n2;
            bl = bl2;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void dispatchDependentViewsChanged(View view) {
        int n = this.mDependencySortedChildren.size();
        boolean bl = false;
        int n2 = 0;
        while (n2 < n) {
            boolean bl2;
            View view2 = this.mDependencySortedChildren.get(n2);
            if (view2 == view) {
                bl2 = true;
            } else {
                bl2 = bl;
                if (bl) {
                    LayoutParams layoutParams = (LayoutParams)view2.getLayoutParams();
                    Behavior behavior = layoutParams.getBehavior();
                    bl2 = bl;
                    if (behavior != null) {
                        bl2 = bl;
                        if (layoutParams.dependsOn(this, view2, view)) {
                            behavior.onDependentViewChanged(this, view2, view);
                            bl2 = bl;
                        }
                    }
                }
            }
            ++n2;
            bl = bl2;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void dispatchOnDependentViewChanged(boolean var1_1) {
        var4_2 = ViewCompat.getLayoutDirection((View)this);
        var5_3 = this.mDependencySortedChildren.size();
        var2_4 = 0;
        block0 : do {
            if (var2_4 >= var5_3) return;
            var7_7 = this.mDependencySortedChildren.get(var2_4);
            var8_8 = (LayoutParams)var7_7.getLayoutParams();
            for (var3_5 = 0; var3_5 < var2_4; ++var3_5) {
                var9_9 = this.mDependencySortedChildren.get(var3_5);
                if (var8_8.mAnchorDirectChild != var9_9) continue;
                this.offsetChildToAnchor(var7_7, var4_2);
            }
            var8_8 = this.mTempRect1;
            var9_9 = this.mTempRect2;
            this.getLastChildRect(var7_7, (Rect)var8_8);
            this.getChildRect(var7_7, true, (Rect)var9_9);
            if (var8_8.equals(var9_9)) ** GOTO lbl-1000
            this.recordLastChildRect(var7_7, (Rect)var9_9);
            var3_5 = var2_4 + 1;
            do {
                if (var3_5 >= var5_3) lbl-1000: // 2 sources:
                {
                    ++var2_4;
                    continue block0;
                }
                var8_8 = this.mDependencySortedChildren.get(var3_5);
                var9_9 = (LayoutParams)var8_8.getLayoutParams();
                var10_10 = var9_9.getBehavior();
                if (var10_10 != null && var10_10.layoutDependsOn(this, var8_8, var7_7)) {
                    if (!var1_1 && var9_9.getChangedAfterNestedScroll()) {
                        var9_9.resetChangedAfterNestedScroll();
                    } else {
                        var6_6 = var10_10.onDependentViewChanged(this, var8_8, var7_7);
                        if (var1_1) {
                            var9_9.setChangedAfterNestedScroll(var6_6);
                        }
                    }
                }
                ++var3_5;
            } while (true);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean doViewsOverlap(View view, View view2) {
        if (view.getVisibility() == 0 && view2.getVisibility() == 0) {
            Rect rect = this.mTempRect1;
            boolean bl = view.getParent() != this;
            this.getChildRect(view, bl, rect);
            view = this.mTempRect2;
            bl = view2.getParent() != this;
            this.getChildRect(view2, bl, (Rect)view);
            if (rect.left <= view.right && rect.top <= view.bottom && rect.right >= view.left && rect.bottom >= view.top) {
                return true;
            }
            return false;
        }
        return false;
    }

    protected boolean drawChild(Canvas canvas, View view, long l) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mBehavior != null && layoutParams.mBehavior.getScrimOpacity(this, view) > 0.0f) {
            if (this.mScrimPaint == null) {
                this.mScrimPaint = new Paint();
            }
            this.mScrimPaint.setColor(layoutParams.mBehavior.getScrimColor(this, view));
            canvas.drawRect((float)this.getPaddingLeft(), (float)this.getPaddingTop(), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - this.getPaddingBottom()), this.mScrimPaint);
        }
        return super.drawChild(canvas, view, l);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void ensurePreDrawListener() {
        var4_1 = false;
        var2_2 = this.getChildCount();
        var1_3 = 0;
        do {
            var3_4 = var4_1;
            if (var1_3 >= var2_2) ** GOTO lbl9
            if (this.hasDependencies(this.getChildAt(var1_3))) {
                var3_4 = true;
lbl9: // 2 sources:
                if (var3_4 == this.mNeedsPreDrawListener) return;
                if (!var3_4) break;
                this.addPreDrawListener();
                return;
            }
            ++var1_3;
        } while (true);
        this.removePreDrawListener();
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    void getChildRect(View view, boolean bl, Rect rect) {
        if (view.isLayoutRequested() || view.getVisibility() == 8) {
            rect.set(0, 0, 0, 0);
            return;
        }
        if (bl) {
            this.getDescendantRect(view, rect);
            return;
        }
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

    /*
     * Enabled aggressive block sorting
     */
    public List<View> getDependencies(View view) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        List<View> list = this.mTempDependenciesList;
        list.clear();
        int n = this.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            View view2 = this.getChildAt(n2);
            if (view2 != view && layoutParams.dependsOn(this, view, view2)) {
                list.add(view2);
            }
            ++n2;
        }
        return list;
    }

    void getDescendantRect(View view, Rect rect) {
        ViewGroupUtils.getDescendantRect(this, view, rect);
    }

    /*
     * Exception decompiling
     */
    void getDesiredAnchoredChildRect(View var1_1, int var2_2, Rect var3_3, Rect var4_4) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:486)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    void getLastChildRect(View view, Rect rect) {
        rect.set(((LayoutParams)view.getLayoutParams()).getLastChildRect());
    }

    @Override
    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    LayoutParams getResolvedLayoutParams(View object) {
        LayoutParams layoutParams = (LayoutParams)object.getLayoutParams();
        if (!layoutParams.mBehaviorResolved) {
            block5 : {
                Object object2;
                Class<? extends Object> class_ = object.getClass();
                object = null;
                do {
                    object2 = object;
                    if (class_ == null) break block5;
                    object2 = object = class_.getAnnotation(DefaultBehavior.class);
                    if (object != null) break;
                    class_ = class_.getSuperclass();
                } while (true);
                if (object2 != null) {
                    try {
                        layoutParams.setBehavior(object2.value().newInstance());
                    }
                    catch (Exception exception) {
                        Log.e((String)"CoordinatorLayout", (String)("Default behavior class " + object2.value().getName() + " could not be instantiated. Did you forget a default constructor?"), (Throwable)exception);
                    }
                }
            }
            layoutParams.mBehaviorResolved = true;
        }
        return layoutParams;
    }

    public Drawable getStatusBarBackground() {
        return this.mStatusBarBackground;
    }

    protected int getSuggestedMinimumHeight() {
        return Math.max(super.getSuggestedMinimumHeight(), this.getPaddingTop() + this.getPaddingBottom());
    }

    protected int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(), this.getPaddingLeft() + this.getPaddingRight());
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean hasDependencies(View view) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mAnchorView != null) {
            return true;
        }
        int n = this.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            View view2 = this.getChildAt(n2);
            if (view2 != view && layoutParams.dependsOn(this, view, view2)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public boolean isPointInChildBounds(View view, int n, int n2) {
        Rect rect = this.mTempRect1;
        this.getDescendantRect(view, rect);
        return rect.contains(n, n2);
    }

    void offsetChildToAnchor(View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mAnchorView != null) {
            Object object = this.mTempRect1;
            Rect rect = this.mTempRect2;
            Rect rect2 = this.mTempRect3;
            this.getDescendantRect(layoutParams.mAnchorView, (Rect)object);
            this.getChildRect(view, false, rect);
            this.getDesiredAnchoredChildRect(view, n, (Rect)object, rect2);
            n = rect2.left - rect.left;
            int n2 = rect2.top - rect.top;
            if (n != 0) {
                view.offsetLeftAndRight(n);
            }
            if (n2 != 0) {
                view.offsetTopAndBottom(n2);
            }
            if ((n != 0 || n2 != 0) && (object = layoutParams.getBehavior()) != null) {
                object.onDependentViewChanged(this, view, layoutParams.mAnchorView);
            }
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.resetTouchBehaviors();
        if (this.mNeedsPreDrawListener) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new OnPreDrawListener();
            }
            this.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this.mOnPreDrawListener);
        }
        if (this.mLastInsets == null && ViewCompat.getFitsSystemWindows((View)this)) {
            ViewCompat.requestApplyInsets((View)this);
        }
        this.mIsAttachedToWindow = true;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.resetTouchBehaviors();
        if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null) {
            this.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this.mOnPreDrawListener);
        }
        if (this.mNestedScrollingTarget != null) {
            this.onStopNestedScroll(this.mNestedScrollingTarget);
        }
        this.mIsAttachedToWindow = false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            int n = this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
            if (n <= 0) return;
            this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), n);
            this.mStatusBarBackground.draw(canvas);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int n = MotionEventCompat.getActionMasked(motionEvent);
        if (n == 0) {
            this.resetTouchBehaviors();
        }
        boolean bl = this.performIntercept(motionEvent, 0);
        if (false) {
            throw new NullPointerException();
        }
        if (n == 1 || n == 3) {
            this.resetTouchBehaviors();
        }
        return bl;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n2 = ViewCompat.getLayoutDirection((View)this);
        n3 = this.mDependencySortedChildren.size();
        for (n = 0; n < n3; ++n) {
            View view = this.mDependencySortedChildren.get(n);
            Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
            if (behavior != null && behavior.onLayoutChild(this, view, n2)) continue;
            this.onLayoutChild(view, n2);
        }
    }

    public void onLayoutChild(View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.checkAnchorChanged()) {
            throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
        }
        if (layoutParams.mAnchorView != null) {
            this.layoutChildWithAnchor(view, layoutParams.mAnchorView, n);
            return;
        }
        if (layoutParams.keyline >= 0) {
            this.layoutChildWithKeyline(view, layoutParams.keyline, n);
            return;
        }
        this.layoutChild(view, n);
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected void onMeasure(int var1_1, int var2_2) {
        block10 : {
            block11 : {
                block9 : {
                    this.prepareChildren();
                    this.ensurePreDrawListener();
                    var14_3 = this.getPaddingLeft();
                    var15_4 = this.getPaddingTop();
                    var16_5 = this.getPaddingRight();
                    var17_6 = this.getPaddingBottom();
                    var18_7 = ViewCompat.getLayoutDirection((View)this);
                    if (var18_7 != 1) ** GOTO lbl58
                    var4_8 = true;
lbl10: // 2 sources:
                    do {
                        var19_9 = View.MeasureSpec.getMode((int)var1_1);
                        var20_10 = View.MeasureSpec.getSize((int)var1_1);
                        var21_11 = View.MeasureSpec.getMode((int)var2_2);
                        var22_12 = View.MeasureSpec.getSize((int)var2_2);
                        var9_13 = this.getSuggestedMinimumWidth();
                        var8_14 = this.getSuggestedMinimumHeight();
                        var7_15 = 0;
                        if (this.mLastInsets == null || !ViewCompat.getFitsSystemWindows((View)this)) break block9;
                        var5_16 = true;
lbl20: // 2 sources:
                        do {
                            var23_17 = this.mDependencySortedChildren.size();
                            var6_18 = 0;
                            block3 : do {
                                if (var6_18 >= var23_17) break block10;
                                var24_24 = this.mDependencySortedChildren.get(var6_18);
                                var25_25 = (LayoutParams)var24_24.getLayoutParams();
                                var3_19 = var10_20 = 0;
                                if (var25_25.keyline < 0) ** GOTO lbl35
                                var3_19 = var10_20;
                                if (var19_9 == 0) ** GOTO lbl35
                                var11_21 = this.getKeyline(var25_25.keyline);
                                var12_22 = GravityCompat.getAbsoluteGravity(CoordinatorLayout.resolveKeylineGravity(var25_25.gravity), var18_7) & 7;
                                if (var12_22 == 3 && !var4_8 || var12_22 == 5 && var4_8) {
                                    var3_19 = Math.max(0, var20_10 - var16_5 - var11_21);
lbl35: // 6 sources:
                                    do {
                                        var11_21 = var1_1;
                                        var12_22 = var2_2;
                                        var13_23 = var11_21;
                                        var10_20 = var12_22;
                                        if (var5_16) {
                                            var13_23 = var11_21;
                                            var10_20 = var12_22;
                                            if (!ViewCompat.getFitsSystemWindows(var24_24)) {
                                                var12_22 = this.mLastInsets.getSystemWindowInsetLeft();
                                                var13_23 = this.mLastInsets.getSystemWindowInsetRight();
                                                var10_20 = this.mLastInsets.getSystemWindowInsetTop();
                                                var11_21 = this.mLastInsets.getSystemWindowInsetBottom();
                                                var13_23 = View.MeasureSpec.makeMeasureSpec((int)(var20_10 - (var12_22 + var13_23)), (int)var19_9);
                                                var10_20 = View.MeasureSpec.makeMeasureSpec((int)(var22_12 - (var10_20 + var11_21)), (int)var21_11);
                                            }
                                        }
                                        if ((var26_26 = var25_25.getBehavior()) == null || !var26_26.onMeasureChild(this, var24_24, var13_23, var3_19, var10_20, 0)) {
                                            this.onMeasureChild(var24_24, var13_23, var3_19, var10_20, 0);
                                        }
                                        var9_13 = Math.max(var9_13, var24_24.getMeasuredWidth() + (var14_3 + var16_5) + var25_25.leftMargin + var25_25.rightMargin);
                                        var8_14 = Math.max(var8_14, var24_24.getMeasuredHeight() + (var15_4 + var17_6) + var25_25.topMargin + var25_25.bottomMargin);
                                        var7_15 = ViewCompat.combineMeasuredStates(var7_15, ViewCompat.getMeasuredState(var24_24));
                                        ++var6_18;
                                        continue block3;
                                        break;
                                    } while (true);
                                }
                                break block11;
                                break;
                            } while (true);
                            break;
                        } while (true);
                        break;
                    } while (true);
lbl58: // 1 sources:
                    var4_8 = false;
                    ** while (true)
                }
                var5_16 = false;
                ** while (true)
            }
            if (var12_22 == 5 && !var4_8) ** GOTO lbl69
            var3_19 = var10_20;
            if (var12_22 != 3) ** GOTO lbl35
            var3_19 = var10_20;
            if (!var4_8) ** GOTO lbl35
lbl69: // 2 sources:
            var3_19 = Math.max(0, var11_21 - var14_3);
            ** while (true)
        }
        this.setMeasuredDimension(ViewCompat.resolveSizeAndState(var9_13, var1_1, -16777216 & var7_15), ViewCompat.resolveSizeAndState(var8_14, var2_2, var7_15 << 16));
    }

    public void onMeasureChild(View view, int n, int n2, int n3, int n4) {
        this.measureChildWithMargins(view, n, n2, n3, n4);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean onNestedFling(View view, float f, float f2, boolean bl) {
        boolean bl2 = false;
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            boolean bl3;
            View view2 = this.getChildAt(i);
            Object object = (LayoutParams)view2.getLayoutParams();
            if (!object.isNestedScrollAccepted()) {
                bl3 = bl2;
            } else {
                object = object.getBehavior();
                bl3 = bl2;
                if (object != null) {
                    bl3 = bl2 | object.onNestedFling(this, view2, view, f, f2, bl);
                }
            }
            bl2 = bl3;
        }
        if (bl2) {
            this.dispatchOnDependentViewChanged(true);
        }
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean onNestedPreFling(View view, float f, float f2) {
        boolean bl = false;
        int n = this.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            boolean bl2;
            View view2 = this.getChildAt(n2);
            Object object = (LayoutParams)view2.getLayoutParams();
            if (!object.isNestedScrollAccepted()) {
                bl2 = bl;
            } else {
                object = object.getBehavior();
                bl2 = bl;
                if (object != null) {
                    bl2 = bl | object.onNestedPreFling(this, view2, view, f, f2);
                }
            }
            ++n2;
            bl = bl2;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onNestedPreScroll(View view, int n, int n2, int[] arrn) {
        int n3 = 0;
        int n4 = 0;
        boolean bl = false;
        int n5 = this.getChildCount();
        for (int i = 0; i < n5; ++i) {
            int n6;
            int n7;
            View view2 = this.getChildAt(i);
            Object object = (LayoutParams)view2.getLayoutParams();
            if (!object.isNestedScrollAccepted()) {
                n7 = n4;
                n6 = n3;
            } else {
                object = object.getBehavior();
                n6 = n3;
                n7 = n4;
                if (object != null) {
                    int[] arrn2 = this.mTempIntPair;
                    this.mTempIntPair[1] = 0;
                    arrn2[0] = 0;
                    object.onNestedPreScroll(this, view2, view, n, n2, this.mTempIntPair);
                    n3 = n > 0 ? Math.max(n3, this.mTempIntPair[0]) : Math.min(n3, this.mTempIntPair[0]);
                    n4 = n2 > 0 ? Math.max(n4, this.mTempIntPair[1]) : Math.min(n4, this.mTempIntPair[1]);
                    bl = true;
                    n6 = n3;
                    n7 = n4;
                }
            }
            n3 = n6;
            n4 = n7;
        }
        arrn[0] = n3;
        arrn[1] = n4;
        if (bl) {
            this.dispatchOnDependentViewChanged(true);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onNestedScroll(View view, int n, int n2, int n3, int n4) {
        int n5 = this.getChildCount();
        boolean bl = false;
        int n6 = 0;
        while (n6 < n5) {
            View view2 = this.getChildAt(n6);
            Object object = (LayoutParams)view2.getLayoutParams();
            if (object.isNestedScrollAccepted() && (object = object.getBehavior()) != null) {
                object.onNestedScroll(this, view2, view, n, n2, n3, n4);
                bl = true;
            }
            ++n6;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onNestedScrollAccepted(View view, View view2, int n) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, n);
        this.mNestedScrollingDirectChild = view;
        this.mNestedScrollingTarget = view2;
        int n2 = this.getChildCount();
        int n3 = 0;
        while (n3 < n2) {
            View view3 = this.getChildAt(n3);
            Object object = (LayoutParams)view3.getLayoutParams();
            if (object.isNestedScrollAccepted() && (object = object.getBehavior()) != null) {
                object.onNestedScrollAccepted(this, view3, view, view2, n);
            }
            ++n3;
        }
    }

    protected void onRestoreInstanceState(Parcelable sparseArray) {
        sparseArray = (SavedState)sparseArray;
        super.onRestoreInstanceState(sparseArray.getSuperState());
        sparseArray = sparseArray.behaviorStates;
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            Parcelable parcelable;
            View view = this.getChildAt(i);
            int n2 = view.getId();
            Behavior behavior = this.getResolvedLayoutParams(view).getBehavior();
            if (n2 == -1 || behavior == null || (parcelable = (Parcelable)sparseArray.get(n2)) == null) continue;
            behavior.onRestoreInstanceState(this, view, parcelable);
        }
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SparseArray sparseArray = new SparseArray();
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            int n2 = view.getId();
            Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
            if (n2 == -1 || behavior == null || (view = behavior.onSaveInstanceState(this, view)) == null) continue;
            sparseArray.append(n2, (Object)view);
        }
        savedState.behaviorStates = sparseArray;
        return savedState;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean onStartNestedScroll(View view, View view2, int n) {
        boolean bl = false;
        int n2 = this.getChildCount();
        int n3 = 0;
        while (n3 < n2) {
            View view3 = this.getChildAt(n3);
            LayoutParams layoutParams = (LayoutParams)view3.getLayoutParams();
            Behavior behavior = layoutParams.getBehavior();
            if (behavior != null) {
                boolean bl2 = behavior.onStartNestedScroll(this, view3, view, view2, n);
                bl |= bl2;
                layoutParams.acceptNestedScroll(bl2);
            } else {
                layoutParams.acceptNestedScroll(false);
            }
            ++n3;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onStopNestedScroll(View view) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view);
        int n = this.getChildCount();
        int n2 = 0;
        do {
            if (n2 >= n) {
                this.mNestedScrollingDirectChild = null;
                this.mNestedScrollingTarget = null;
                return;
            }
            View view2 = this.getChildAt(n2);
            LayoutParams layoutParams = (LayoutParams)view2.getLayoutParams();
            if (layoutParams.isNestedScrollAccepted()) {
                Behavior behavior = layoutParams.getBehavior();
                if (behavior != null) {
                    behavior.onStopNestedScroll(this, view2, view);
                }
                layoutParams.resetNestedScroll();
                layoutParams.resetChangedAfterNestedScroll();
            }
            ++n2;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1_1) {
        var6_2 = false;
        var4_3 = false;
        var9_4 = null;
        var10_5 = null;
        var2_6 = MotionEventCompat.getActionMasked(var1_1);
        if (this.mBehaviorTouchView != null) ** GOTO lbl-1000
        var5_7 = var4_3 = this.performIntercept(var1_1, 1);
        var3_8 = var6_2;
        if (var4_3) lbl-1000: // 2 sources:
        {
            var11_9 = ((LayoutParams)this.mBehaviorTouchView.getLayoutParams()).getBehavior();
            var5_7 = var4_3;
            var3_8 = var6_2;
            if (var11_9 != null) {
                var3_8 = var11_9.onTouchEvent(this, this.mBehaviorTouchView, var1_1);
                var5_7 = var4_3;
            }
        }
        if (this.mBehaviorTouchView == null) {
            var4_3 = var3_8 | super.onTouchEvent(var1_1);
            var1_1 = var10_5;
        } else {
            var1_1 = var10_5;
            var4_3 = var3_8;
            if (var5_7) {
                var1_1 = var9_4;
                if (!false) {
                    var7_10 = SystemClock.uptimeMillis();
                    var1_1 = MotionEvent.obtain((long)var7_10, (long)var7_10, (int)3, (float)0.0f, (float)0.0f, (int)0);
                }
                super.onTouchEvent(var1_1);
                var4_3 = var3_8;
            }
        }
        if (var4_3 || var2_6 == 0) {
            // empty if block
        }
        if (var1_1 != null) {
            var1_1.recycle();
        }
        if (var2_6 != 1) {
            if (var2_6 != 3) return var4_3;
        }
        this.resetTouchBehaviors();
        return var4_3;
    }

    void recordLastChildRect(View view, Rect rect) {
        ((LayoutParams)view.getLayoutParams()).setLastChildRect(rect);
    }

    void removePreDrawListener() {
        if (this.mIsAttachedToWindow && this.mOnPreDrawListener != null) {
            this.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this.mOnPreDrawListener);
        }
        this.mNeedsPreDrawListener = false;
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        super.requestDisallowInterceptTouchEvent(bl);
        if (bl) {
            this.resetTouchBehaviors();
        }
    }

    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        this.mOnHierarchyChangeListener = onHierarchyChangeListener;
    }

    public void setStatusBarBackground(Drawable drawable2) {
        this.mStatusBarBackground = drawable2;
        this.invalidate();
    }

    public void setStatusBarBackgroundColor(int n) {
        this.setStatusBarBackground((Drawable)new ColorDrawable(n));
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setStatusBarBackgroundResource(int n) {
        Drawable drawable2 = n != 0 ? ContextCompat.getDrawable(this.getContext(), n) : null;
        this.setStatusBarBackground(drawable2);
    }

    final class ApplyInsetsListener
    implements OnApplyWindowInsetsListener {
        ApplyInsetsListener() {
        }

        @Override
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            CoordinatorLayout.this.setWindowInsets(windowInsetsCompat);
            return windowInsetsCompat.consumeSystemWindowInsets();
        }
    }

    public static abstract class Behavior<V extends View> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
        }

        public static Object getTag(View view) {
            return ((LayoutParams)view.getLayoutParams()).mBehaviorTag;
        }

        public static void setTag(View view, Object object) {
            ((LayoutParams)view.getLayoutParams()).mBehaviorTag = object;
        }

        public boolean blocksInteractionBelow(CoordinatorLayout coordinatorLayout, V v) {
            if (this.getScrimOpacity(coordinatorLayout, v) > 0.0f) {
                return true;
            }
            return false;
        }

        public final int getScrimColor(CoordinatorLayout coordinatorLayout, V v) {
            return -16777216;
        }

        public final float getScrimOpacity(CoordinatorLayout coordinatorLayout, V v) {
            return 0.0f;
        }

        public boolean isDirty(CoordinatorLayout coordinatorLayout, V v) {
            return false;
        }

        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, V v, View view) {
            return false;
        }

        public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout coordinatorLayout, V v, WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }

        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, V v, View view) {
            return false;
        }

        public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, V v, View view) {
        }

        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
            return false;
        }

        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int n) {
            return false;
        }

        public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, V v, int n, int n2, int n3, int n4) {
            return false;
        }

        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2, boolean bl) {
            return false;
        }

        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2) {
            return false;
        }

        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int n, int n2, int[] arrn) {
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int n, int n2, int n3, int n4) {
        }

        public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int n) {
        }

        public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v, Parcelable parcelable) {
        }

        public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v) {
            return View.BaseSavedState.EMPTY_STATE;
        }

        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int n) {
            return false;
        }

        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view) {
        }

        public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
            return false;
        }
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    public static @interface DefaultBehavior {
        public Class<? extends Behavior> value();
    }

    final class HierarchyChangeListener
    implements ViewGroup.OnHierarchyChangeListener {
        HierarchyChangeListener() {
        }

        public void onChildViewAdded(View view, View view2) {
            if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
                CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        public void onChildViewRemoved(View view, View view2) {
            CoordinatorLayout.this.dispatchDependentViewRemoved(view2);
            if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
                CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public int anchorGravity = 0;
        public int gravity = 0;
        public int keyline = -1;
        View mAnchorDirectChild;
        int mAnchorId = -1;
        View mAnchorView;
        Behavior mBehavior;
        boolean mBehaviorResolved = false;
        Object mBehaviorTag;
        private boolean mDidAcceptNestedScroll;
        private boolean mDidBlockInteraction;
        private boolean mDidChangeAfterNestedScroll;
        final Rect mLastChildRect = new Rect();

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CoordinatorLayout_LayoutParams);
            this.gravity = typedArray.getInteger(R.styleable.CoordinatorLayout_LayoutParams_android_layout_gravity, 0);
            this.mAnchorId = typedArray.getResourceId(R.styleable.CoordinatorLayout_LayoutParams_layout_anchor, -1);
            this.anchorGravity = typedArray.getInteger(R.styleable.CoordinatorLayout_LayoutParams_layout_anchorGravity, 0);
            this.keyline = typedArray.getInteger(R.styleable.CoordinatorLayout_LayoutParams_layout_keyline, -1);
            this.mBehaviorResolved = typedArray.hasValue(R.styleable.CoordinatorLayout_LayoutParams_layout_behavior);
            if (this.mBehaviorResolved) {
                this.mBehavior = CoordinatorLayout.parseBehavior(context, attributeSet, typedArray.getString(R.styleable.CoordinatorLayout_LayoutParams_layout_behavior));
            }
            typedArray.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        private void resolveAnchorView(View view, CoordinatorLayout coordinatorLayout) {
            this.mAnchorView = coordinatorLayout.findViewById(this.mAnchorId);
            if (this.mAnchorView != null) {
                View view2 = this.mAnchorView;
                for (ViewParent viewParent = this.mAnchorView.getParent(); viewParent != coordinatorLayout && viewParent != null; viewParent = viewParent.getParent()) {
                    if (viewParent == view) {
                        if (coordinatorLayout.isInEditMode()) {
                            this.mAnchorDirectChild = null;
                            this.mAnchorView = null;
                            return;
                        }
                        throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
                    }
                    if (!(viewParent instanceof View)) continue;
                    view2 = (View)viewParent;
                }
                this.mAnchorDirectChild = view2;
                return;
            }
            if (coordinatorLayout.isInEditMode()) {
                this.mAnchorDirectChild = null;
                this.mAnchorView = null;
                return;
            }
            throw new IllegalStateException("Could not find CoordinatorLayout descendant view with id " + coordinatorLayout.getResources().getResourceName(this.mAnchorId) + " to anchor view " + (Object)view);
        }

        private boolean verifyAnchorView(View view, CoordinatorLayout coordinatorLayout) {
            if (this.mAnchorView.getId() != this.mAnchorId) {
                return false;
            }
            View view2 = this.mAnchorView;
            for (ViewParent viewParent = this.mAnchorView.getParent(); viewParent != coordinatorLayout; viewParent = viewParent.getParent()) {
                if (viewParent == null || viewParent == view) {
                    this.mAnchorDirectChild = null;
                    this.mAnchorView = null;
                    return false;
                }
                if (!(viewParent instanceof View)) continue;
                view2 = (View)viewParent;
            }
            this.mAnchorDirectChild = view2;
            return true;
        }

        void acceptNestedScroll(boolean bl) {
            this.mDidAcceptNestedScroll = bl;
        }

        boolean checkAnchorChanged() {
            if (this.mAnchorView == null && this.mAnchorId != -1) {
                return true;
            }
            return false;
        }

        boolean dependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
            if (view2 == this.mAnchorDirectChild || this.mBehavior != null && this.mBehavior.layoutDependsOn(coordinatorLayout, view, view2)) {
                return true;
            }
            return false;
        }

        boolean didBlockInteraction() {
            if (this.mBehavior == null) {
                this.mDidBlockInteraction = false;
            }
            return this.mDidBlockInteraction;
        }

        View findAnchorView(CoordinatorLayout coordinatorLayout, View view) {
            if (this.mAnchorId == -1) {
                this.mAnchorDirectChild = null;
                this.mAnchorView = null;
                return null;
            }
            if (this.mAnchorView == null || !this.verifyAnchorView(view, coordinatorLayout)) {
                this.resolveAnchorView(view, coordinatorLayout);
            }
            return this.mAnchorView;
        }

        public int getAnchorId() {
            return this.mAnchorId;
        }

        public Behavior getBehavior() {
            return this.mBehavior;
        }

        boolean getChangedAfterNestedScroll() {
            return this.mDidChangeAfterNestedScroll;
        }

        Rect getLastChildRect() {
            return this.mLastChildRect;
        }

        void invalidateAnchor() {
            this.mAnchorDirectChild = null;
            this.mAnchorView = null;
        }

        /*
         * Enabled aggressive block sorting
         */
        boolean isBlockingInteractionBelow(CoordinatorLayout coordinatorLayout, View view) {
            if (this.mDidBlockInteraction) {
                return true;
            }
            boolean bl = this.mDidBlockInteraction;
            boolean bl2 = this.mBehavior != null ? this.mBehavior.blocksInteractionBelow(coordinatorLayout, view) : false;
            this.mDidBlockInteraction = bl2 |= bl;
            return bl2;
        }

        boolean isDirty(CoordinatorLayout coordinatorLayout, View view) {
            if (this.mBehavior != null && this.mBehavior.isDirty(coordinatorLayout, view)) {
                return true;
            }
            return false;
        }

        boolean isNestedScrollAccepted() {
            return this.mDidAcceptNestedScroll;
        }

        void resetChangedAfterNestedScroll() {
            this.mDidChangeAfterNestedScroll = false;
        }

        void resetNestedScroll() {
            this.mDidAcceptNestedScroll = false;
        }

        void resetTouchBehaviorTracking() {
            this.mDidBlockInteraction = false;
        }

        public void setAnchorId(int n) {
            this.invalidateAnchor();
            this.mAnchorId = n;
        }

        public void setBehavior(Behavior behavior) {
            if (this.mBehavior != behavior) {
                this.mBehavior = behavior;
                this.mBehaviorTag = null;
                this.mBehaviorResolved = true;
            }
        }

        void setChangedAfterNestedScroll(boolean bl) {
            this.mDidChangeAfterNestedScroll = bl;
        }

        void setLastChildRect(Rect rect) {
            this.mLastChildRect.set(rect);
        }
    }

    class OnPreDrawListener
    implements ViewTreeObserver.OnPreDrawListener {
        OnPreDrawListener() {
        }

        public boolean onPreDraw() {
            CoordinatorLayout.this.dispatchOnDependentViewChanged(false);
            return true;
        }
    }

    protected static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>(){

            @Override
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        });
        SparseArray<Parcelable> behaviorStates;

        public SavedState(Parcel arrparcelable, ClassLoader classLoader) {
            super((Parcel)arrparcelable);
            int n = arrparcelable.readInt();
            int[] arrn = new int[n];
            arrparcelable.readIntArray(arrn);
            arrparcelable = arrparcelable.readParcelableArray(classLoader);
            this.behaviorStates = new SparseArray(n);
            for (int i = 0; i < n; ++i) {
                this.behaviorStates.append(arrn[i], (Object)arrparcelable[i]);
            }
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            int n2 = this.behaviorStates != null ? this.behaviorStates.size() : 0;
            parcel.writeInt(n2);
            int[] arrn = new int[n2];
            Parcelable[] arrparcelable = new Parcelable[n2];
            int n3 = 0;
            do {
                if (n3 >= n2) {
                    parcel.writeIntArray(arrn);
                    parcel.writeParcelableArray(arrparcelable, n);
                    return;
                }
                arrn[n3] = this.behaviorStates.keyAt(n3);
                arrparcelable[n3] = (Parcelable)this.behaviorStates.valueAt(n3);
                ++n3;
            } while (true);
        }

    }

    static class ViewElevationComparator
    implements Comparator<View> {
        ViewElevationComparator() {
        }

        @Override
        public int compare(View view, View view2) {
            float f;
            float f2 = ViewCompat.getZ(view);
            if (f2 > (f = ViewCompat.getZ(view2))) {
                return -1;
            }
            if (f2 < f) {
                return 1;
            }
            return 0;
        }
    }

}

