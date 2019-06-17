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
 *  android.graphics.Region
 *  android.graphics.Region$Op
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
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
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.coordinatorlayout.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.util.Pools;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.DirectedAcyclicGraph;
import android.support.v4.widget.ViewGroupUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorLayout
extends ViewGroup
implements NestedScrollingParent2 {
    static final Class<?>[] CONSTRUCTOR_PARAMS;
    static final int EVENT_NESTED_SCROLL = 1;
    static final int EVENT_PRE_DRAW = 0;
    static final int EVENT_VIEW_REMOVED = 2;
    static final String TAG = "CoordinatorLayout";
    static final Comparator<View> TOP_SORTED_CHILDREN_COMPARATOR;
    private static final int TYPE_ON_INTERCEPT = 0;
    private static final int TYPE_ON_TOUCH = 1;
    static final String WIDGET_PACKAGE_NAME;
    static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors;
    private static final Pools.Pool<Rect> sRectPool;
    private OnApplyWindowInsetsListener mApplyWindowInsetsListener;
    private View mBehaviorTouchView;
    private final DirectedAcyclicGraph<View> mChildDag = new DirectedAcyclicGraph();
    private final List<View> mDependencySortedChildren = new ArrayList<View>();
    private boolean mDisallowInterceptReset;
    private boolean mDrawStatusBarBackground;
    private boolean mIsAttachedToWindow;
    private int[] mKeylines;
    private WindowInsetsCompat mLastInsets;
    private boolean mNeedsPreDrawListener;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private View mNestedScrollingTarget;
    ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
    private OnPreDrawListener mOnPreDrawListener;
    private Paint mScrimPaint;
    private Drawable mStatusBarBackground;
    private final List<View> mTempDependenciesList = new ArrayList<View>();
    private final int[] mTempIntPair = new int[2];
    private final List<View> mTempList1 = new ArrayList<View>();

    static {
        Object object = CoordinatorLayout.class.getPackage();
        object = object != null ? object.getName() : null;
        WIDGET_PACKAGE_NAME = object;
        TOP_SORTED_CHILDREN_COMPARATOR = Build.VERSION.SDK_INT >= 21 ? new ViewElevationComparator() : null;
        CONSTRUCTOR_PARAMS = new Class[]{Context.class, AttributeSet.class};
        sConstructors = new ThreadLocal();
        sRectPool = new Pools.SynchronizedPool<Rect>(12);
    }

    public CoordinatorLayout(@NonNull Context context) {
        this(context, null);
    }

    public CoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.coordinatorLayoutStyle);
    }

    public CoordinatorLayout(@NonNull Context arrn, @Nullable AttributeSet attributeSet, @AttrRes int n) {
        super((Context)arrn, attributeSet, n);
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        attributeSet = n == 0 ? arrn.obtainStyledAttributes(attributeSet, R.styleable.CoordinatorLayout, 0, R.style.Widget_Support_CoordinatorLayout) : arrn.obtainStyledAttributes(attributeSet, R.styleable.CoordinatorLayout, n, 0);
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
        this.setupForInsets();
        super.setOnHierarchyChangeListener((ViewGroup.OnHierarchyChangeListener)new HierarchyChangeListener());
    }

    @NonNull
    private static Rect acquireTempRect() {
        Rect rect;
        Rect rect2 = rect = sRectPool.acquire();
        if (rect == null) {
            rect2 = new Rect();
        }
        return rect2;
    }

    private static int clamp(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }

    private void constrainChildRect(LayoutParams layoutParams, Rect rect, int n, int n2) {
        int n3 = this.getWidth();
        int n4 = this.getHeight();
        n3 = Math.max(this.getPaddingLeft() + layoutParams.leftMargin, Math.min(rect.left, n3 - this.getPaddingRight() - n - layoutParams.rightMargin));
        n4 = Math.max(this.getPaddingTop() + layoutParams.topMargin, Math.min(rect.top, n4 - this.getPaddingBottom() - n2 - layoutParams.bottomMargin));
        rect.set(n3, n4, n3 + n, n4 + n2);
    }

    private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat windowInsetsCompat) {
        if (windowInsetsCompat.isConsumed()) {
            return windowInsetsCompat;
        }
        int n = this.getChildCount();
        WindowInsetsCompat windowInsetsCompat2 = windowInsetsCompat;
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            windowInsetsCompat = windowInsetsCompat2;
            if (ViewCompat.getFitsSystemWindows(view)) {
                Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
                windowInsetsCompat = windowInsetsCompat2;
                if (behavior != null) {
                    windowInsetsCompat = windowInsetsCompat2 = behavior.onApplyWindowInsets(this, view, windowInsetsCompat2);
                    if (windowInsetsCompat2.isConsumed()) {
                        return windowInsetsCompat2;
                    }
                }
            }
            windowInsetsCompat2 = windowInsetsCompat;
        }
        return windowInsetsCompat2;
    }

    private void getDesiredAnchoredChildRectWithoutConstraints(View view, int n, Rect rect, Rect rect2, LayoutParams layoutParams, int n2, int n3) {
        int n4 = GravityCompat.getAbsoluteGravity(CoordinatorLayout.resolveAnchoredChildGravity(layoutParams.gravity), n);
        n = GravityCompat.getAbsoluteGravity(CoordinatorLayout.resolveGravity(layoutParams.anchorGravity), n);
        int n5 = n4 & 7;
        int n6 = n4 & 112;
        int n7 = n & 7;
        n4 = n & 112;
        n = n7 != 1 ? (n7 != 5 ? rect.left : rect.right) : rect.left + rect.width() / 2;
        n4 = n4 != 16 ? (n4 != 80 ? rect.top : rect.bottom) : rect.top + rect.height() / 2;
        if (n5 != 1) {
            if (n5 != 5) {
                n -= n2;
            }
        } else {
            n -= n2 / 2;
        }
        if (n6 != 16) {
            if (n6 != 80) {
                n4 -= n3;
            }
        } else {
            n4 -= n3 / 2;
        }
        rect2.set(n, n4, n + n2, n4 + n3);
    }

    private int getKeyline(int n) {
        int[] arrn = this.mKeylines;
        if (arrn == null) {
            arrn = new StringBuilder();
            arrn.append("No keylines defined for ");
            arrn.append(this);
            arrn.append(" - attempted index lookup ");
            arrn.append(n);
            Log.e((String)"CoordinatorLayout", (String)arrn.toString());
            return 0;
        }
        if (n >= 0 && n < arrn.length) {
            return (int)arrn[n];
        }
        arrn = new StringBuilder();
        arrn.append("Keyline index ");
        arrn.append(n);
        arrn.append(" out of range for ");
        arrn.append(this);
        Log.e((String)"CoordinatorLayout", (String)arrn.toString());
        return 0;
    }

    private void getTopSortedChildren(List<View> list) {
        list.clear();
        boolean bl = this.isChildrenDrawingOrderEnabled();
        int n = this.getChildCount();
        for (int i = n - 1; i >= 0; --i) {
            int n2 = bl ? this.getChildDrawingOrder(n, i) : i;
            list.add(this.getChildAt(n2));
        }
        Comparator<View> comparator = TOP_SORTED_CHILDREN_COMPARATOR;
        if (comparator != null) {
            Collections.sort(list, comparator);
        }
    }

    private boolean hasDependencies(View view) {
        return this.mChildDag.hasOutgoingEdges(view);
    }

    private void layoutChild(View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = CoordinatorLayout.acquireTempRect();
        rect.set(this.getPaddingLeft() + layoutParams.leftMargin, this.getPaddingTop() + layoutParams.topMargin, this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin, this.getHeight() - this.getPaddingBottom() - layoutParams.bottomMargin);
        if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this) && !ViewCompat.getFitsSystemWindows(view)) {
            rect.left += this.mLastInsets.getSystemWindowInsetLeft();
            rect.top += this.mLastInsets.getSystemWindowInsetTop();
            rect.right -= this.mLastInsets.getSystemWindowInsetRight();
            rect.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
        }
        Rect rect2 = CoordinatorLayout.acquireTempRect();
        GravityCompat.apply(CoordinatorLayout.resolveGravity(layoutParams.gravity), view.getMeasuredWidth(), view.getMeasuredHeight(), rect, rect2, n);
        view.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
        CoordinatorLayout.releaseTempRect(rect);
        CoordinatorLayout.releaseTempRect(rect2);
    }

    private void layoutChildWithAnchor(View view, View view2, int n) {
        Rect rect = CoordinatorLayout.acquireTempRect();
        Rect rect2 = CoordinatorLayout.acquireTempRect();
        try {
            this.getDescendantRect(view2, rect);
            this.getDesiredAnchoredChildRect(view, n, rect, rect2);
            view.layout(rect2.left, rect2.top, rect2.right, rect2.bottom);
            return;
        }
        finally {
            CoordinatorLayout.releaseTempRect(rect);
            CoordinatorLayout.releaseTempRect(rect2);
        }
    }

    private void layoutChildWithKeyline(View view, int n, int n2) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n3 = GravityCompat.getAbsoluteGravity(CoordinatorLayout.resolveKeylineGravity(layoutParams.gravity), n2);
        int n4 = n3 & 7;
        int n5 = n3 & 112;
        int n6 = this.getWidth();
        int n7 = this.getHeight();
        n3 = view.getMeasuredWidth();
        int n8 = view.getMeasuredHeight();
        if (n2 == 1) {
            n = n6 - n;
        }
        n = this.getKeyline(n) - n3;
        n2 = 0;
        if (n4 != 1) {
            if (n4 == 5) {
                n += n3;
            }
        } else {
            n += n3 / 2;
        }
        if (n5 != 16) {
            if (n5 == 80) {
                n2 = 0 + n8;
            }
        } else {
            n2 = 0 + n8 / 2;
        }
        n = Math.max(this.getPaddingLeft() + layoutParams.leftMargin, Math.min(n, n6 - this.getPaddingRight() - n3 - layoutParams.rightMargin));
        n2 = Math.max(this.getPaddingTop() + layoutParams.topMargin, Math.min(n2, n7 - this.getPaddingBottom() - n8 - layoutParams.bottomMargin));
        view.layout(n, n2, n + n3, n2 + n8);
    }

    private void offsetChildByInset(View object, Rect rect, int n) {
        if (!ViewCompat.isLaidOut((View)object)) {
            return;
        }
        if (object.getWidth() > 0) {
            int n2;
            int n3;
            if (object.getHeight() <= 0) {
                return;
            }
            LayoutParams layoutParams = (LayoutParams)object.getLayoutParams();
            Behavior behavior = layoutParams.getBehavior();
            Rect rect2 = CoordinatorLayout.acquireTempRect();
            Rect rect3 = CoordinatorLayout.acquireTempRect();
            rect3.set(object.getLeft(), object.getTop(), object.getRight(), object.getBottom());
            if (behavior != null && behavior.getInsetDodgeRect(this, object, rect2)) {
                if (!rect3.contains(rect2)) {
                    object = new StringBuilder();
                    object.append("Rect should be within the child's bounds. Rect:");
                    object.append(rect2.toShortString());
                    object.append(" | Bounds:");
                    object.append(rect3.toShortString());
                    throw new IllegalArgumentException(object.toString());
                }
            } else {
                rect2.set(rect3);
            }
            CoordinatorLayout.releaseTempRect(rect3);
            if (rect2.isEmpty()) {
                CoordinatorLayout.releaseTempRect(rect2);
                return;
            }
            int n4 = GravityCompat.getAbsoluteGravity(layoutParams.dodgeInsetEdges, n);
            n = n3 = 0;
            if ((n4 & 48) == 48) {
                n2 = rect2.top - layoutParams.topMargin - layoutParams.mInsetOffsetY;
                n = n3;
                if (n2 < rect.top) {
                    this.setInsetOffsetY((View)object, rect.top - n2);
                    n = 1;
                }
            }
            n3 = n;
            if ((n4 & 80) == 80) {
                n2 = this.getHeight() - rect2.bottom - layoutParams.bottomMargin + layoutParams.mInsetOffsetY;
                n3 = n;
                if (n2 < rect.bottom) {
                    this.setInsetOffsetY((View)object, n2 - rect.bottom);
                    n3 = 1;
                }
            }
            if (n3 == 0) {
                this.setInsetOffsetY((View)object, 0);
            }
            n = n3 = 0;
            if ((n4 & 3) == 3) {
                n2 = rect2.left - layoutParams.leftMargin - layoutParams.mInsetOffsetX;
                n = n3;
                if (n2 < rect.left) {
                    this.setInsetOffsetX((View)object, rect.left - n2);
                    n = 1;
                }
            }
            n3 = n;
            if ((n4 & 5) == 5) {
                n4 = this.getWidth() - rect2.right - layoutParams.rightMargin + layoutParams.mInsetOffsetX;
                n3 = n;
                if (n4 < rect.right) {
                    this.setInsetOffsetX((View)object, n4 - rect.right);
                    n3 = 1;
                }
            }
            if (n3 == 0) {
                this.setInsetOffsetX((View)object, 0);
            }
            CoordinatorLayout.releaseTempRect(rect2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static Behavior parseBehavior(Context object, AttributeSet object2, String string2) {
        HashMap hashMap;
        if (TextUtils.isEmpty((CharSequence)string2)) {
            return null;
        }
        if (string2.startsWith(".")) {
            hashMap = new StringBuilder();
            hashMap.append(object.getPackageName());
            hashMap.append(string2);
            string2 = hashMap.toString();
        } else if (string2.indexOf(46) < 0 && !TextUtils.isEmpty((CharSequence)WIDGET_PACKAGE_NAME)) {
            hashMap = new StringBuilder();
            hashMap.append(WIDGET_PACKAGE_NAME);
            hashMap.append('.');
            hashMap.append(string2);
            string2 = hashMap.toString();
        }
        try {
            Constructor constructor = sConstructors.get();
            hashMap = constructor;
            if (constructor == null) {
                hashMap = new HashMap();
                sConstructors.set(hashMap);
            }
            Constructor constructor2 = (Constructor)hashMap.get(string2);
            constructor = constructor2;
            if (constructor2 == null) {
                constructor = object.getClassLoader().loadClass(string2).getConstructor(CONSTRUCTOR_PARAMS);
                constructor.setAccessible(true);
                hashMap.put(string2, constructor);
            }
            return (Behavior)constructor.newInstance(object, object2);
        }
        catch (Exception exception) {
            object2 = new StringBuilder();
            object2.append("Could not inflate Behavior subclass ");
            object2.append(string2);
            throw new RuntimeException(object2.toString(), exception);
        }
    }

    private boolean performIntercept(MotionEvent motionEvent, int n) {
        boolean bl;
        boolean bl2 = false;
        boolean bl3 = false;
        LayoutParams layoutParams = null;
        int n2 = motionEvent.getActionMasked();
        List<View> list = this.mTempList1;
        this.getTopSortedChildren(list);
        int n3 = list.size();
        int n4 = 0;
        do {
            boolean bl4;
            bl = bl2;
            if (n4 >= n3) break;
            View view = list.get(n4);
            LayoutParams layoutParams2 = (LayoutParams)view.getLayoutParams();
            Behavior behavior = layoutParams2.getBehavior();
            boolean bl5 = true;
            if ((bl2 || bl3) && n2 != 0) {
                bl4 = bl2;
                bl5 = bl3;
                layoutParams2 = layoutParams;
                if (behavior != null) {
                    layoutParams2 = layoutParams;
                    if (layoutParams == null) {
                        long l = SystemClock.uptimeMillis();
                        layoutParams2 = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
                    }
                    if (n != 0) {
                        if (n == 1) {
                            behavior.onTouchEvent(this, view, (MotionEvent)layoutParams2);
                        }
                    } else {
                        behavior.onInterceptTouchEvent(this, view, (MotionEvent)layoutParams2);
                    }
                    bl4 = bl2;
                    bl5 = bl3;
                }
            } else {
                bl = bl2;
                if (!bl2) {
                    bl = bl2;
                    if (behavior != null) {
                        if (n != 0) {
                            if (n == 1) {
                                bl2 = behavior.onTouchEvent(this, view, motionEvent);
                            }
                        } else {
                            bl2 = behavior.onInterceptTouchEvent(this, view, motionEvent);
                        }
                        bl = bl2;
                        if (bl2) {
                            this.mBehaviorTouchView = view;
                            bl = bl2;
                        }
                    }
                }
                bl4 = layoutParams2.didBlockInteraction();
                bl2 = layoutParams2.isBlockingInteractionBelow(this, view);
                bl3 = bl2 && !bl4 ? bl5 : false;
                bl4 = bl;
                bl5 = bl3;
                layoutParams2 = layoutParams;
                if (bl2) {
                    bl4 = bl;
                    bl5 = bl3;
                    layoutParams2 = layoutParams;
                    if (!bl3) break;
                }
            }
            ++n4;
            bl2 = bl4;
            bl3 = bl5;
            layoutParams = layoutParams2;
        } while (true);
        list.clear();
        return bl;
    }

    private void prepareChildren() {
        this.mDependencySortedChildren.clear();
        this.mChildDag.clear();
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            LayoutParams layoutParams = this.getResolvedLayoutParams(view);
            layoutParams.findAnchorView(this, view);
            this.mChildDag.addNode(view);
            for (int j = 0; j < n; ++j) {
                View view2;
                if (j == i || !layoutParams.dependsOn(this, view, view2 = this.getChildAt(j))) continue;
                if (!this.mChildDag.contains(view2)) {
                    this.mChildDag.addNode(view2);
                }
                this.mChildDag.addEdge(view2, view);
            }
        }
        this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
        Collections.reverse(this.mDependencySortedChildren);
    }

    private static void releaseTempRect(@NonNull Rect rect) {
        rect.setEmpty();
        sRectPool.release(rect);
    }

    private void resetTouchBehaviors(boolean bl) {
        int n;
        int n2 = this.getChildCount();
        for (n = 0; n < n2; ++n) {
            View view = this.getChildAt(n);
            Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
            if (behavior == null) continue;
            long l = SystemClock.uptimeMillis();
            MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
            if (bl) {
                behavior.onInterceptTouchEvent(this, view, motionEvent);
            } else {
                behavior.onTouchEvent(this, view, motionEvent);
            }
            motionEvent.recycle();
        }
        for (n = 0; n < n2; ++n) {
            ((LayoutParams)this.getChildAt(n).getLayoutParams()).resetTouchBehaviorTracking();
        }
        this.mBehaviorTouchView = null;
        this.mDisallowInterceptReset = false;
    }

    private static int resolveAnchoredChildGravity(int n) {
        if (n == 0) {
            return 17;
        }
        return n;
    }

    private static int resolveGravity(int n) {
        int n2 = n;
        if ((n & 7) == 0) {
            n2 = n | 8388611;
        }
        n = n2;
        if ((n2 & 112) == 0) {
            n = n2 | 48;
        }
        return n;
    }

    private static int resolveKeylineGravity(int n) {
        if (n == 0) {
            return 8388661;
        }
        return n;
    }

    private void setInsetOffsetX(View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mInsetOffsetX != n) {
            ViewCompat.offsetLeftAndRight(view, n - layoutParams.mInsetOffsetX);
            layoutParams.mInsetOffsetX = n;
        }
    }

    private void setInsetOffsetY(View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mInsetOffsetY != n) {
            ViewCompat.offsetTopAndBottom(view, n - layoutParams.mInsetOffsetY);
            layoutParams.mInsetOffsetY = n;
        }
    }

    private void setupForInsets() {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (ViewCompat.getFitsSystemWindows((View)this)) {
            if (this.mApplyWindowInsetsListener == null) {
                this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener(){

                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                        return CoordinatorLayout.this.setWindowInsets(windowInsetsCompat);
                    }
                };
            }
            ViewCompat.setOnApplyWindowInsetsListener((View)this, this.mApplyWindowInsetsListener);
            this.setSystemUiVisibility(1280);
            return;
        }
        ViewCompat.setOnApplyWindowInsetsListener((View)this, null);
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

    public void dispatchDependentViewsChanged(@NonNull View view) {
        List list = this.mChildDag.getIncomingEdges(view);
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                View view2 = (View)list.get(i);
                Behavior behavior = ((LayoutParams)view2.getLayoutParams()).getBehavior();
                if (behavior == null) continue;
                behavior.onDependentViewChanged(this, view2, view);
            }
        }
    }

    public boolean doViewsOverlap(@NonNull View view, @NonNull View view2) {
        int n = view.getVisibility();
        boolean bl = false;
        if (n == 0 && view2.getVisibility() == 0) {
            Rect rect;
            boolean bl2;
            block5 : {
                rect = CoordinatorLayout.acquireTempRect();
                bl2 = view.getParent() != this;
                this.getChildRect(view, bl2, rect);
                view = CoordinatorLayout.acquireTempRect();
                bl2 = view2.getParent() != this;
                this.getChildRect(view2, bl2, (Rect)view);
                bl2 = bl;
                try {
                    if (rect.left > view.right) break block5;
                    bl2 = bl;
                }
                catch (Throwable throwable) {
                    CoordinatorLayout.releaseTempRect(rect);
                    CoordinatorLayout.releaseTempRect((Rect)view);
                    throw throwable;
                }
                if (rect.top > view.bottom) break block5;
                bl2 = bl;
                if (rect.right < view.left) break block5;
                n = rect.bottom;
                int n2 = view.top;
                bl2 = bl;
                if (n < n2) break block5;
                bl2 = true;
            }
            CoordinatorLayout.releaseTempRect(rect);
            CoordinatorLayout.releaseTempRect((Rect)view);
            return bl2;
        }
        return false;
    }

    protected boolean drawChild(Canvas canvas, View view, long l) {
        float f;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mBehavior != null && (f = layoutParams.mBehavior.getScrimOpacity(this, view)) > 0.0f) {
            if (this.mScrimPaint == null) {
                this.mScrimPaint = new Paint();
            }
            this.mScrimPaint.setColor(layoutParams.mBehavior.getScrimColor(this, view));
            this.mScrimPaint.setAlpha(CoordinatorLayout.clamp(Math.round(255.0f * f), 0, 255));
            int n = canvas.save();
            if (view.isOpaque()) {
                canvas.clipRect((float)view.getLeft(), (float)view.getTop(), (float)view.getRight(), (float)view.getBottom(), Region.Op.DIFFERENCE);
            }
            canvas.drawRect((float)this.getPaddingLeft(), (float)this.getPaddingTop(), (float)(this.getWidth() - this.getPaddingRight()), (float)(this.getHeight() - this.getPaddingBottom()), this.mScrimPaint);
            canvas.restoreToCount(n);
        }
        return super.drawChild(canvas, view, l);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] arrn = this.getDrawableState();
        boolean bl = false;
        Drawable drawable2 = this.mStatusBarBackground;
        boolean bl2 = bl;
        if (drawable2 != null) {
            bl2 = bl;
            if (drawable2.isStateful()) {
                bl2 = false | drawable2.setState(arrn);
            }
        }
        if (bl2) {
            this.invalidate();
        }
    }

    void ensurePreDrawListener() {
        boolean bl;
        boolean bl2 = false;
        int n = this.getChildCount();
        int n2 = 0;
        do {
            bl = bl2;
            if (n2 >= n) break;
            if (this.hasDependencies(this.getChildAt(n2))) {
                bl = true;
                break;
            }
            ++n2;
        } while (true);
        if (bl != this.mNeedsPreDrawListener) {
            if (bl) {
                this.addPreDrawListener();
                return;
            }
            this.removePreDrawListener();
        }
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
        if (!view.isLayoutRequested() && view.getVisibility() != 8) {
            if (bl) {
                this.getDescendantRect(view, rect);
                return;
            }
            rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            return;
        }
        rect.setEmpty();
    }

    @NonNull
    public List<View> getDependencies(@NonNull View object) {
        object = this.mChildDag.getOutgoingEdges((View)object);
        this.mTempDependenciesList.clear();
        if (object != null) {
            this.mTempDependenciesList.addAll((Collection<View>)object);
        }
        return this.mTempDependenciesList;
    }

    @VisibleForTesting
    final List<View> getDependencySortedChildren() {
        this.prepareChildren();
        return Collections.unmodifiableList(this.mDependencySortedChildren);
    }

    @NonNull
    public List<View> getDependents(@NonNull View object) {
        object = this.mChildDag.getIncomingEdges((View)object);
        this.mTempDependenciesList.clear();
        if (object != null) {
            this.mTempDependenciesList.addAll((Collection<View>)object);
        }
        return this.mTempDependenciesList;
    }

    void getDescendantRect(View view, Rect rect) {
        ViewGroupUtils.getDescendantRect(this, view, rect);
    }

    void getDesiredAnchoredChildRect(View view, int n, Rect rect, Rect rect2) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n2 = view.getMeasuredWidth();
        int n3 = view.getMeasuredHeight();
        this.getDesiredAnchoredChildRectWithoutConstraints(view, n, rect, rect2, layoutParams, n2, n3);
        this.constrainChildRect(layoutParams, rect2, n2, n3);
    }

    void getLastChildRect(View view, Rect rect) {
        rect.set(((LayoutParams)view.getLayoutParams()).getLastChildRect());
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public final WindowInsetsCompat getLastWindowInsets() {
        return this.mLastInsets;
    }

    @Override
    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    LayoutParams getResolvedLayoutParams(View object) {
        LayoutParams layoutParams = (LayoutParams)object.getLayoutParams();
        if (!layoutParams.mBehaviorResolved) {
            Object object2;
            if (object instanceof AttachedBehavior) {
                if ((object = ((AttachedBehavior)object).getBehavior()) == null) {
                    Log.e((String)"CoordinatorLayout", (String)"Attached behavior class is null");
                }
                layoutParams.setBehavior((Behavior)object);
                layoutParams.mBehaviorResolved = true;
                return layoutParams;
            }
            Class class_ = object.getClass();
            object = null;
            do {
                object2 = object;
                if (class_ == null) break;
                DefaultBehavior defaultBehavior = class_.getAnnotation(DefaultBehavior.class);
                object2 = object = defaultBehavior;
                if (defaultBehavior != null) break;
                class_ = class_.getSuperclass();
            } while (true);
            if (object2 != null) {
                try {
                    layoutParams.setBehavior(object2.value().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
                }
                catch (Exception exception) {
                    class_ = new StringBuilder();
                    class_.append("Default behavior class ");
                    class_.append(object2.value().getName());
                    class_.append(" could not be instantiated. Did you forget");
                    class_.append(" a default constructor?");
                    Log.e((String)"CoordinatorLayout", (String)class_.toString(), (Throwable)exception);
                }
            }
            layoutParams.mBehaviorResolved = true;
        }
        return layoutParams;
    }

    @Nullable
    public Drawable getStatusBarBackground() {
        return this.mStatusBarBackground;
    }

    protected int getSuggestedMinimumHeight() {
        return Math.max(super.getSuggestedMinimumHeight(), this.getPaddingTop() + this.getPaddingBottom());
    }

    protected int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(), this.getPaddingLeft() + this.getPaddingRight());
    }

    public boolean isPointInChildBounds(@NonNull View view, int n, int n2) {
        Rect rect = CoordinatorLayout.acquireTempRect();
        this.getDescendantRect(view, rect);
        try {
            boolean bl = rect.contains(n, n2);
            return bl;
        }
        finally {
            CoordinatorLayout.releaseTempRect(rect);
        }
    }

    void offsetChildToAnchor(View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mAnchorView != null) {
            Rect rect;
            int n2;
            Behavior behavior;
            int n3;
            Rect rect2;
            Rect rect3;
            int n4;
            block6 : {
                rect2 = CoordinatorLayout.acquireTempRect();
                rect3 = CoordinatorLayout.acquireTempRect();
                rect = CoordinatorLayout.acquireTempRect();
                this.getDescendantRect(layoutParams.mAnchorView, rect2);
                n2 = 0;
                this.getChildRect(view, false, rect3);
                n3 = view.getMeasuredWidth();
                n4 = view.getMeasuredHeight();
                this.getDesiredAnchoredChildRectWithoutConstraints(view, n, rect2, rect, layoutParams, n3, n4);
                if (rect.left == rect3.left) {
                    n = n2;
                    if (rect.top == rect3.top) break block6;
                }
                n = 1;
            }
            this.constrainChildRect(layoutParams, rect, n3, n4);
            n2 = rect.left - rect3.left;
            n3 = rect.top - rect3.top;
            if (n2 != 0) {
                ViewCompat.offsetLeftAndRight(view, n2);
            }
            if (n3 != 0) {
                ViewCompat.offsetTopAndBottom(view, n3);
            }
            if (n != 0 && (behavior = layoutParams.getBehavior()) != null) {
                behavior.onDependentViewChanged(this, view, layoutParams.mAnchorView);
            }
            CoordinatorLayout.releaseTempRect(rect2);
            CoordinatorLayout.releaseTempRect(rect3);
            CoordinatorLayout.releaseTempRect(rect);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.resetTouchBehaviors(false);
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

    final void onChildViewsChanged(int n) {
        int n2 = ViewCompat.getLayoutDirection((View)this);
        int n3 = this.mDependencySortedChildren.size();
        Rect rect = CoordinatorLayout.acquireTempRect();
        Rect rect2 = CoordinatorLayout.acquireTempRect();
        Rect rect3 = CoordinatorLayout.acquireTempRect();
        for (int i = 0; i < n3; ++i) {
            int n4;
            Object object;
            View view = this.mDependencySortedChildren.get(i);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (n == 0 && view.getVisibility() == 8) continue;
            for (n4 = 0; n4 < i; ++n4) {
                object = this.mDependencySortedChildren.get(n4);
                if (layoutParams.mAnchorDirectChild != object) continue;
                this.offsetChildToAnchor(view, n2);
            }
            this.getChildRect(view, true, rect2);
            if (layoutParams.insetEdge != 0 && !rect2.isEmpty()) {
                n4 = GravityCompat.getAbsoluteGravity(layoutParams.insetEdge, n2);
                int n5 = n4 & 112;
                if (n5 != 48) {
                    if (n5 == 80) {
                        rect.bottom = Math.max(rect.bottom, this.getHeight() - rect2.top);
                    }
                } else {
                    rect.top = Math.max(rect.top, rect2.bottom);
                }
                if ((n4 &= 7) != 3) {
                    if (n4 == 5) {
                        rect.right = Math.max(rect.right, this.getWidth() - rect2.left);
                    }
                } else {
                    rect.left = Math.max(rect.left, rect2.right);
                }
            }
            if (layoutParams.dodgeInsetEdges != 0 && view.getVisibility() == 0) {
                this.offsetChildByInset(view, rect, n2);
            }
            if (n != 2) {
                this.getLastChildRect(view, rect3);
                if (rect3.equals((Object)rect2)) continue;
                this.recordLastChildRect(view, rect2);
            }
            for (n4 = i + 1; n4 < n3; ++n4) {
                boolean bl;
                layoutParams = this.mDependencySortedChildren.get(n4);
                object = (LayoutParams)layoutParams.getLayoutParams();
                Behavior behavior = object.getBehavior();
                if (behavior == null || !behavior.layoutDependsOn(this, layoutParams, view)) continue;
                if (n == 0 && object.getChangedAfterNestedScroll()) {
                    object.resetChangedAfterNestedScroll();
                    continue;
                }
                if (n != 2) {
                    bl = behavior.onDependentViewChanged(this, layoutParams, view);
                } else {
                    behavior.onDependentViewRemoved(this, layoutParams, view);
                    bl = true;
                }
                if (n != 1) continue;
                object.setChangedAfterNestedScroll(bl);
            }
        }
        CoordinatorLayout.releaseTempRect(rect);
        CoordinatorLayout.releaseTempRect(rect2);
        CoordinatorLayout.releaseTempRect(rect3);
    }

    public void onDetachedFromWindow() {
        View view;
        super.onDetachedFromWindow();
        this.resetTouchBehaviors(false);
        if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null) {
            this.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this.mOnPreDrawListener);
        }
        if ((view = this.mNestedScrollingTarget) != null) {
            this.onStopNestedScroll(view);
        }
        this.mIsAttachedToWindow = false;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
            int n = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
            if (n > 0) {
                this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), n);
                this.mStatusBarBackground.draw(canvas);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        if (n == 0) {
            this.resetTouchBehaviors(true);
        }
        boolean bl = this.performIntercept(motionEvent, 0);
        if (n == 1 || n == 3) {
            this.resetTouchBehaviors(true);
        }
        return bl;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n2 = ViewCompat.getLayoutDirection((View)this);
        n3 = this.mDependencySortedChildren.size();
        for (n = 0; n < n3; ++n) {
            Behavior behavior;
            View view = this.mDependencySortedChildren.get(n);
            if (view.getVisibility() == 8 || (behavior = ((LayoutParams)view.getLayoutParams()).getBehavior()) != null && behavior.onLayoutChild(this, view, n2)) continue;
            this.onLayoutChild(view, n2);
        }
    }

    public void onLayoutChild(@NonNull View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.checkAnchorChanged()) {
            if (layoutParams.mAnchorView != null) {
                this.layoutChildWithAnchor(view, layoutParams.mAnchorView, n);
                return;
            }
            if (layoutParams.keyline >= 0) {
                this.layoutChildWithKeyline(view, layoutParams.keyline, n);
                return;
            }
            this.layoutChild(view, n);
            return;
        }
        throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
    }

    protected void onMeasure(int n, int n2) {
        this.prepareChildren();
        this.ensurePreDrawListener();
        int n3 = this.getPaddingLeft();
        int n4 = this.getPaddingTop();
        int n5 = this.getPaddingRight();
        int n6 = this.getPaddingBottom();
        int n7 = ViewCompat.getLayoutDirection((View)this);
        boolean bl = true;
        boolean bl2 = n7 == 1;
        int n8 = View.MeasureSpec.getMode((int)n);
        int n9 = View.MeasureSpec.getSize((int)n);
        int n10 = View.MeasureSpec.getMode((int)n2);
        int n11 = View.MeasureSpec.getSize((int)n2);
        int n12 = this.getSuggestedMinimumWidth();
        int n13 = this.getSuggestedMinimumHeight();
        if (this.mLastInsets == null || !ViewCompat.getFitsSystemWindows((View)this)) {
            bl = false;
        }
        int n14 = this.mDependencySortedChildren.size();
        int n15 = 0;
        for (int i = 0; i < n14; ++i) {
            int n16;
            int n17;
            View view = this.mDependencySortedChildren.get(i);
            if (view.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n18 = 0;
            if (layoutParams.keyline >= 0 && n8 != 0) {
                n17 = this.getKeyline(layoutParams.keyline);
                n16 = GravityCompat.getAbsoluteGravity(CoordinatorLayout.resolveKeylineGravity(layoutParams.gravity), n7) & 7;
                if (n16 == 3 && !bl2 || n16 == 5 && bl2) {
                    n18 = Math.max(0, n9 - n5 - n17);
                } else if (n16 == 5 && !bl2 || n16 == 3 && bl2) {
                    n18 = Math.max(0, n17 - n3);
                }
            }
            n17 = n12;
            n12 = n15;
            n16 = n13;
            if (bl && !ViewCompat.getFitsSystemWindows(view)) {
                n13 = this.mLastInsets.getSystemWindowInsetLeft();
                int n19 = this.mLastInsets.getSystemWindowInsetRight();
                n15 = this.mLastInsets.getSystemWindowInsetTop();
                int n20 = this.mLastInsets.getSystemWindowInsetBottom();
                n13 = View.MeasureSpec.makeMeasureSpec((int)(n9 - (n13 + n19)), (int)n8);
                n15 = View.MeasureSpec.makeMeasureSpec((int)(n11 - (n15 + n20)), (int)n10);
            } else {
                n13 = n;
                n15 = n2;
            }
            Behavior behavior = layoutParams.getBehavior();
            if (behavior == null || !behavior.onMeasureChild(this, view, n13, n18, n15, 0)) {
                this.onMeasureChild(view, n13, n18, n15, 0);
            }
            n18 = Math.max(n17, n3 + n5 + view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
            n13 = Math.max(n16, n4 + n6 + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
            n15 = View.combineMeasuredStates((int)n12, (int)view.getMeasuredState());
            n12 = n18;
        }
        this.setMeasuredDimension(View.resolveSizeAndState((int)n12, (int)n, (int)(-16777216 & n15)), View.resolveSizeAndState((int)n13, (int)n2, (int)(n15 << 16)));
    }

    public void onMeasureChild(View view, int n, int n2, int n3, int n4) {
        this.measureChildWithMargins(view, n, n2, n3, n4);
    }

    @Override
    public boolean onNestedFling(View view, float f, float f2, boolean bl) {
        int n = this.getChildCount();
        boolean bl2 = false;
        for (int i = 0; i < n; ++i) {
            boolean bl3;
            View view2 = this.getChildAt(i);
            if (view2.getVisibility() == 8) {
                bl3 = bl2;
            } else {
                Object object = (LayoutParams)view2.getLayoutParams();
                if (!object.isNestedScrollAccepted(0)) {
                    bl3 = bl2;
                } else {
                    object = object.getBehavior();
                    bl3 = bl2;
                    if (object != null) {
                        bl3 = object.onNestedFling(this, view2, view, f, f2, bl) | bl2;
                    }
                }
            }
            bl2 = bl3;
        }
        if (bl2) {
            this.onChildViewsChanged(1);
        }
        return bl2;
    }

    @Override
    public boolean onNestedPreFling(View view, float f, float f2) {
        boolean bl = false;
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            boolean bl2;
            View view2 = this.getChildAt(i);
            if (view2.getVisibility() == 8) {
                bl2 = bl;
            } else {
                Object object = (LayoutParams)view2.getLayoutParams();
                if (!object.isNestedScrollAccepted(0)) {
                    bl2 = bl;
                } else {
                    object = object.getBehavior();
                    bl2 = bl;
                    if (object != null) {
                        bl2 = bl | object.onNestedPreFling(this, view2, view, f, f2);
                    }
                }
            }
            bl = bl2;
        }
        return bl;
    }

    @Override
    public void onNestedPreScroll(View view, int n, int n2, int[] arrn) {
        this.onNestedPreScroll(view, n, n2, arrn, 0);
    }

    @Override
    public void onNestedPreScroll(View view, int n, int n2, int[] arrn, int n3) {
        int n4 = this.getChildCount();
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        for (int i = 0; i < n4; ++i) {
            Object object;
            View view2 = this.getChildAt(i);
            if (view2.getVisibility() == 8 || !(object = (LayoutParams)view2.getLayoutParams()).isNestedScrollAccepted(n3) || (object = object.getBehavior()) == null) continue;
            int[] arrn2 = this.mTempIntPair;
            arrn2[1] = 0;
            arrn2[0] = 0;
            object.onNestedPreScroll(this, view2, view, n, n2, arrn2, n3);
            n7 = n > 0 ? Math.max(n5, this.mTempIntPair[0]) : Math.min(n5, this.mTempIntPair[0]);
            n6 = n2 > 0 ? Math.max(n6, this.mTempIntPair[1]) : Math.min(n6, this.mTempIntPair[1]);
            int n8 = 1;
            n5 = n7;
            n7 = n8;
        }
        arrn[0] = n5;
        arrn[1] = n6;
        if (n7 != 0) {
            this.onChildViewsChanged(1);
        }
    }

    @Override
    public void onNestedScroll(View view, int n, int n2, int n3, int n4) {
        this.onNestedScroll(view, n, n2, n3, n4, 0);
    }

    @Override
    public void onNestedScroll(View view, int n, int n2, int n3, int n4, int n5) {
        int n6 = this.getChildCount();
        boolean bl = false;
        for (int i = 0; i < n6; ++i) {
            Object object;
            View view2 = this.getChildAt(i);
            if (view2.getVisibility() == 8 || !(object = (LayoutParams)view2.getLayoutParams()).isNestedScrollAccepted(n5) || (object = object.getBehavior()) == null) continue;
            object.onNestedScroll(this, view2, view, n, n2, n3, n4, n5);
            bl = true;
        }
        if (bl) {
            this.onChildViewsChanged(1);
        }
    }

    @Override
    public void onNestedScrollAccepted(View view, View view2, int n) {
        this.onNestedScrollAccepted(view, view2, n, 0);
    }

    @Override
    public void onNestedScrollAccepted(View view, View view2, int n, int n2) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, n, n2);
        this.mNestedScrollingTarget = view2;
        int n3 = this.getChildCount();
        for (int i = 0; i < n3; ++i) {
            View view3 = this.getChildAt(i);
            Object object = (LayoutParams)view3.getLayoutParams();
            if (!object.isNestedScrollAccepted(n2) || (object = object.getBehavior()) == null) continue;
            object.onNestedScrollAccepted(this, view3, view, view2, n, n2);
        }
    }

    protected void onRestoreInstanceState(Parcelable sparseArray) {
        if (!(sparseArray instanceof SavedState)) {
            super.onRestoreInstanceState((Parcelable)sparseArray);
            return;
        }
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

    @Override
    public boolean onStartNestedScroll(View view, View view2, int n) {
        return this.onStartNestedScroll(view, view2, n, 0);
    }

    @Override
    public boolean onStartNestedScroll(View view, View view2, int n, int n2) {
        int n3 = this.getChildCount();
        boolean bl = false;
        for (int i = 0; i < n3; ++i) {
            View view3 = this.getChildAt(i);
            if (view3.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view3.getLayoutParams();
            Behavior behavior = layoutParams.getBehavior();
            if (behavior != null) {
                boolean bl2 = behavior.onStartNestedScroll(this, view3, view, view2, n, n2);
                layoutParams.setNestedScrollAccepted(n2, bl2);
                bl |= bl2;
                continue;
            }
            layoutParams.setNestedScrollAccepted(n2, false);
        }
        return bl;
    }

    @Override
    public void onStopNestedScroll(View view) {
        this.onStopNestedScroll(view, 0);
    }

    @Override
    public void onStopNestedScroll(View view, int n) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view, n);
        int n2 = this.getChildCount();
        for (int i = 0; i < n2; ++i) {
            View view2 = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams)view2.getLayoutParams();
            if (!layoutParams.isNestedScrollAccepted(n)) continue;
            Behavior behavior = layoutParams.getBehavior();
            if (behavior != null) {
                behavior.onStopNestedScroll(this, view2, view, n);
            }
            layoutParams.resetNestedScroll(n);
            layoutParams.resetChangedAfterNestedScroll();
        }
        this.mNestedScrollingTarget = null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1_1) {
        var6_2 = false;
        var4_3 = false;
        var11_4 = null;
        var10_5 = null;
        var2_6 = var1_1.getActionMasked();
        if (this.mBehaviorTouchView != null) ** GOTO lbl-1000
        var4_3 = var7_7 = this.performIntercept(var1_1, 1);
        var3_8 = var6_2;
        var5_9 = var4_3;
        if (var7_7) lbl-1000: // 2 sources:
        {
            var12_10 = ((LayoutParams)this.mBehaviorTouchView.getLayoutParams()).getBehavior();
            var3_8 = var6_2;
            var5_9 = var4_3;
            if (var12_10 != null) {
                var3_8 = var12_10.onTouchEvent(this, this.mBehaviorTouchView, var1_1);
                var5_9 = var4_3;
            }
        }
        if (this.mBehaviorTouchView == null) {
            var4_3 = var3_8 | super.onTouchEvent(var1_1);
            var1_1 = var11_4;
        } else {
            var4_3 = var3_8;
            var1_1 = var11_4;
            if (var5_9) {
                var1_1 = var10_5;
                if (!false) {
                    var8_11 = SystemClock.uptimeMillis();
                    var1_1 = MotionEvent.obtain((long)var8_11, (long)var8_11, (int)3, (float)0.0f, (float)0.0f, (int)0);
                }
                super.onTouchEvent(var1_1);
                var4_3 = var3_8;
            }
        }
        if (var1_1 != null) {
            var1_1.recycle();
        }
        if (var2_6 != 1) {
            if (var2_6 != 3) return var4_3;
        }
        this.resetTouchBehaviors(false);
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

    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean bl) {
        Behavior behavior = ((LayoutParams)view.getLayoutParams()).getBehavior();
        if (behavior != null && behavior.onRequestChildRectangleOnScreen(this, view, rect, bl)) {
            return true;
        }
        return super.requestChildRectangleOnScreen(view, rect, bl);
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        super.requestDisallowInterceptTouchEvent(bl);
        if (bl && !this.mDisallowInterceptReset) {
            this.resetTouchBehaviors(false);
            this.mDisallowInterceptReset = true;
        }
    }

    public void setFitsSystemWindows(boolean bl) {
        super.setFitsSystemWindows(bl);
        this.setupForInsets();
    }

    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        this.mOnHierarchyChangeListener = onHierarchyChangeListener;
    }

    public void setStatusBarBackground(@Nullable Drawable drawable2) {
        Drawable drawable3 = this.mStatusBarBackground;
        if (drawable3 != drawable2) {
            Drawable drawable4 = null;
            if (drawable3 != null) {
                drawable3.setCallback(null);
            }
            if (drawable2 != null) {
                drawable4 = drawable2.mutate();
            }
            if ((drawable2 = (this.mStatusBarBackground = drawable4)) != null) {
                if (drawable2.isStateful()) {
                    this.mStatusBarBackground.setState(this.getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.mStatusBarBackground, ViewCompat.getLayoutDirection((View)this));
                drawable2 = this.mStatusBarBackground;
                boolean bl = this.getVisibility() == 0;
                drawable2.setVisible(bl, false);
                this.mStatusBarBackground.setCallback((Drawable.Callback)this);
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    public void setStatusBarBackgroundColor(@ColorInt int n) {
        this.setStatusBarBackground((Drawable)new ColorDrawable(n));
    }

    public void setStatusBarBackgroundResource(@DrawableRes int n) {
        Drawable drawable2 = n != 0 ? ContextCompat.getDrawable(this.getContext(), n) : null;
        this.setStatusBarBackground(drawable2);
    }

    public void setVisibility(int n) {
        super.setVisibility(n);
        boolean bl = n == 0;
        Drawable drawable2 = this.mStatusBarBackground;
        if (drawable2 != null && drawable2.isVisible() != bl) {
            this.mStatusBarBackground.setVisible(bl, false);
        }
    }

    final WindowInsetsCompat setWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        WindowInsetsCompat windowInsetsCompat2 = windowInsetsCompat;
        if (!ObjectsCompat.equals(this.mLastInsets, windowInsetsCompat)) {
            this.mLastInsets = windowInsetsCompat;
            boolean bl = true;
            boolean bl2 = windowInsetsCompat != null && windowInsetsCompat.getSystemWindowInsetTop() > 0;
            this.mDrawStatusBarBackground = bl2;
            bl2 = !this.mDrawStatusBarBackground && this.getBackground() == null ? bl : false;
            this.setWillNotDraw(bl2);
            windowInsetsCompat2 = this.dispatchApplyWindowInsetsToBehaviors(windowInsetsCompat);
            this.requestLayout();
        }
        return windowInsetsCompat2;
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        if (!super.verifyDrawable(drawable2) && drawable2 != this.mStatusBarBackground) {
            return false;
        }
        return true;
    }

    public static interface AttachedBehavior {
        @NonNull
        public Behavior getBehavior();
    }

    public static abstract class Behavior<V extends View> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
        }

        @Nullable
        public static Object getTag(@NonNull View view) {
            return ((LayoutParams)view.getLayoutParams()).mBehaviorTag;
        }

        public static void setTag(@NonNull View view, @Nullable Object object) {
            ((LayoutParams)view.getLayoutParams()).mBehaviorTag = object;
        }

        public boolean blocksInteractionBelow(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v) {
            if (this.getScrimOpacity(coordinatorLayout, v) > 0.0f) {
                return true;
            }
            return false;
        }

        public boolean getInsetDodgeRect(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull Rect rect) {
            return false;
        }

        @ColorInt
        public int getScrimColor(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v) {
            return -16777216;
        }

        @FloatRange(from=0.0, to=1.0)
        public float getScrimOpacity(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v) {
            return 0.0f;
        }

        public boolean layoutDependsOn(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view) {
            return false;
        }

        @NonNull
        public WindowInsetsCompat onApplyWindowInsets(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }

        public void onAttachedToLayoutParams(@NonNull LayoutParams layoutParams) {
        }

        public boolean onDependentViewChanged(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view) {
            return false;
        }

        public void onDependentViewRemoved(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view) {
        }

        public void onDetachedFromLayoutParams() {
        }

        public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull MotionEvent motionEvent) {
            return false;
        }

        public boolean onLayoutChild(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, int n) {
            return false;
        }

        public boolean onMeasureChild(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, int n, int n2, int n3, int n4) {
            return false;
        }

        public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, float f, float f2, boolean bl) {
            return false;
        }

        public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, float f, float f2) {
            return false;
        }

        @Deprecated
        public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, int n, int n2, @NonNull int[] arrn) {
        }

        public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, int n, int n2, @NonNull int[] arrn, int n3) {
            if (n3 == 0) {
                this.onNestedPreScroll(coordinatorLayout, v, view, n, n2, arrn);
            }
        }

        @Deprecated
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, int n, int n2, int n3, int n4) {
        }

        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, int n, int n2, int n3, int n4, int n5) {
            if (n5 == 0) {
                this.onNestedScroll(coordinatorLayout, v, view, n, n2, n3, n4);
            }
        }

        @Deprecated
        public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, @NonNull View view2, int n) {
        }

        public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, @NonNull View view2, int n, int n2) {
            if (n2 == 0) {
                this.onNestedScrollAccepted(coordinatorLayout, v, view, view2, n);
            }
        }

        public boolean onRequestChildRectangleOnScreen(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull Rect rect, boolean bl) {
            return false;
        }

        public void onRestoreInstanceState(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull Parcelable parcelable) {
        }

        @Nullable
        public Parcelable onSaveInstanceState(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v) {
            return View.BaseSavedState.EMPTY_STATE;
        }

        @Deprecated
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, @NonNull View view2, int n) {
            return false;
        }

        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, @NonNull View view2, int n, int n2) {
            if (n2 == 0) {
                return this.onStartNestedScroll(coordinatorLayout, v, view, view2, n);
            }
            return false;
        }

        @Deprecated
        public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view) {
        }

        public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull View view, int n) {
            if (n == 0) {
                this.onStopNestedScroll(coordinatorLayout, v, view);
            }
        }

        public boolean onTouchEvent(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V v, @NonNull MotionEvent motionEvent) {
            return false;
        }
    }

    @Deprecated
    @Retention(value=RetentionPolicy.RUNTIME)
    public static @interface DefaultBehavior {
        public Class<? extends Behavior> value();
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface DispatchChangeEvent {
    }

    private class HierarchyChangeListener
    implements ViewGroup.OnHierarchyChangeListener {
        HierarchyChangeListener() {
        }

        public void onChildViewAdded(View view, View view2) {
            if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
                CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        public void onChildViewRemoved(View view, View view2) {
            CoordinatorLayout.this.onChildViewsChanged(2);
            if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
                CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public int anchorGravity = 0;
        public int dodgeInsetEdges = 0;
        public int gravity = 0;
        public int insetEdge = 0;
        public int keyline = -1;
        View mAnchorDirectChild;
        int mAnchorId = -1;
        View mAnchorView;
        Behavior mBehavior;
        boolean mBehaviorResolved = false;
        Object mBehaviorTag;
        private boolean mDidAcceptNestedScrollNonTouch;
        private boolean mDidAcceptNestedScrollTouch;
        private boolean mDidBlockInteraction;
        private boolean mDidChangeAfterNestedScroll;
        int mInsetOffsetX;
        int mInsetOffsetY;
        final Rect mLastChildRect = new Rect();

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        LayoutParams(@NonNull Context object, @Nullable AttributeSet attributeSet) {
            super((Context)object, attributeSet);
            TypedArray typedArray = object.obtainStyledAttributes(attributeSet, R.styleable.CoordinatorLayout_Layout);
            this.gravity = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
            this.mAnchorId = typedArray.getResourceId(R.styleable.CoordinatorLayout_Layout_layout_anchor, -1);
            this.anchorGravity = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
            this.keyline = typedArray.getInteger(R.styleable.CoordinatorLayout_Layout_layout_keyline, -1);
            this.insetEdge = typedArray.getInt(R.styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
            this.dodgeInsetEdges = typedArray.getInt(R.styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
            this.mBehaviorResolved = typedArray.hasValue(R.styleable.CoordinatorLayout_Layout_layout_behavior);
            if (this.mBehaviorResolved) {
                this.mBehavior = CoordinatorLayout.parseBehavior((Context)object, attributeSet, typedArray.getString(R.styleable.CoordinatorLayout_Layout_layout_behavior));
            }
            typedArray.recycle();
            object = this.mBehavior;
            if (object != null) {
                object.onAttachedToLayoutParams(this);
            }
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

        private void resolveAnchorView(View object, CoordinatorLayout coordinatorLayout) {
            this.mAnchorView = coordinatorLayout.findViewById(this.mAnchorId);
            Object object2 = this.mAnchorView;
            if (object2 != null) {
                if (object2 == coordinatorLayout) {
                    if (coordinatorLayout.isInEditMode()) {
                        this.mAnchorDirectChild = null;
                        this.mAnchorView = null;
                        return;
                    }
                    throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
                }
                View view = this.mAnchorView;
                for (object2 = object2.getParent(); object2 != coordinatorLayout && object2 != null; object2 = object2.getParent()) {
                    if (object2 == object) {
                        if (coordinatorLayout.isInEditMode()) {
                            this.mAnchorDirectChild = null;
                            this.mAnchorView = null;
                            return;
                        }
                        throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
                    }
                    if (!(object2 instanceof View)) continue;
                    view = (View)object2;
                }
                this.mAnchorDirectChild = view;
                return;
            }
            if (coordinatorLayout.isInEditMode()) {
                this.mAnchorDirectChild = null;
                this.mAnchorView = null;
                return;
            }
            object2 = new StringBuilder();
            object2.append("Could not find CoordinatorLayout descendant view with id ");
            object2.append(coordinatorLayout.getResources().getResourceName(this.mAnchorId));
            object2.append(" to anchor view ");
            object2.append(object);
            object = new IllegalStateException(object2.toString());
            throw object;
        }

        private boolean shouldDodge(View view, int n) {
            int n2 = GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).insetEdge, n);
            if (n2 != 0 && (GravityCompat.getAbsoluteGravity(this.dodgeInsetEdges, n) & n2) == n2) {
                return true;
            }
            return false;
        }

        private boolean verifyAnchorView(View view, CoordinatorLayout coordinatorLayout) {
            if (this.mAnchorView.getId() != this.mAnchorId) {
                return false;
            }
            View view2 = this.mAnchorView;
            for (ViewParent viewParent = this.mAnchorView.getParent(); viewParent != coordinatorLayout; viewParent = viewParent.getParent()) {
                if (viewParent != null && viewParent != view) {
                    if (!(viewParent instanceof View)) continue;
                    view2 = (View)viewParent;
                    continue;
                }
                this.mAnchorDirectChild = null;
                this.mAnchorView = null;
                return false;
            }
            this.mAnchorDirectChild = view2;
            return true;
        }

        boolean checkAnchorChanged() {
            if (this.mAnchorView == null && this.mAnchorId != -1) {
                return true;
            }
            return false;
        }

        boolean dependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
            Behavior behavior;
            if (!(view2 == this.mAnchorDirectChild || this.shouldDodge(view2, ViewCompat.getLayoutDirection((View)coordinatorLayout)) || (behavior = this.mBehavior) != null && behavior.layoutDependsOn(coordinatorLayout, view, view2))) {
                return false;
            }
            return true;
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

        @IdRes
        public int getAnchorId() {
            return this.mAnchorId;
        }

        @Nullable
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

        boolean isBlockingInteractionBelow(CoordinatorLayout coordinatorLayout, View view) {
            boolean bl = this.mDidBlockInteraction;
            if (bl) {
                return true;
            }
            Behavior behavior = this.mBehavior;
            boolean bl2 = behavior != null ? behavior.blocksInteractionBelow(coordinatorLayout, view) : false;
            this.mDidBlockInteraction = bl2 = bl | bl2;
            return bl2;
        }

        boolean isNestedScrollAccepted(int n) {
            if (n != 0) {
                if (n != 1) {
                    return false;
                }
                return this.mDidAcceptNestedScrollNonTouch;
            }
            return this.mDidAcceptNestedScrollTouch;
        }

        void resetChangedAfterNestedScroll() {
            this.mDidChangeAfterNestedScroll = false;
        }

        void resetNestedScroll(int n) {
            this.setNestedScrollAccepted(n, false);
        }

        void resetTouchBehaviorTracking() {
            this.mDidBlockInteraction = false;
        }

        public void setAnchorId(@IdRes int n) {
            this.invalidateAnchor();
            this.mAnchorId = n;
        }

        public void setBehavior(@Nullable Behavior behavior) {
            Behavior behavior2 = this.mBehavior;
            if (behavior2 != behavior) {
                if (behavior2 != null) {
                    behavior2.onDetachedFromLayoutParams();
                }
                this.mBehavior = behavior;
                this.mBehaviorTag = null;
                this.mBehaviorResolved = true;
                if (behavior != null) {
                    behavior.onAttachedToLayoutParams(this);
                }
            }
        }

        void setChangedAfterNestedScroll(boolean bl) {
            this.mDidChangeAfterNestedScroll = bl;
        }

        void setLastChildRect(Rect rect) {
            this.mLastChildRect.set(rect);
        }

        void setNestedScrollAccepted(int n, boolean bl) {
            if (n != 0) {
                if (n != 1) {
                    return;
                }
                this.mDidAcceptNestedScrollNonTouch = bl;
                return;
            }
            this.mDidAcceptNestedScrollTouch = bl;
        }
    }

    class OnPreDrawListener
    implements ViewTreeObserver.OnPreDrawListener {
        OnPreDrawListener() {
        }

        public boolean onPreDraw() {
            CoordinatorLayout.this.onChildViewsChanged(0);
            return true;
        }
    }

    protected static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        SparseArray<Parcelable> behaviorStates;

        public SavedState(Parcel arrparcelable, ClassLoader classLoader) {
            super((Parcel)arrparcelable, classLoader);
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

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            int[] arrn = this.behaviorStates;
            int n2 = arrn != null ? arrn.size() : 0;
            parcel.writeInt(n2);
            arrn = new int[n2];
            Parcelable[] arrparcelable = new Parcelable[n2];
            for (int i = 0; i < n2; ++i) {
                arrn[i] = this.behaviorStates.keyAt(i);
                arrparcelable[i] = (Parcelable)this.behaviorStates.valueAt(i);
            }
            parcel.writeIntArray(arrn);
            parcel.writeParcelableArray(arrparcelable, n);
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

