/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.database.Observable
 *  android.graphics.Canvas
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.SparseArray
 *  android.util.SparseIntArray
 *  android.util.TypedValue
 *  android.view.FocusFinder
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
 *  android.view.accessibility.AccessibilityManager
 *  android.view.animation.Interpolator
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.TraceCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.AdapterHelper;
import android.support.v7.widget.ChildHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.support.v7.widget.ViewInfoStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView
extends ViewGroup
implements ScrollingView,
NestedScrollingChild {
    private static final boolean DEBUG = false;
    private static final boolean DISPATCH_TEMP_DETACH = false;
    private static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
    public static final int HORIZONTAL = 0;
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
    private static final int MAX_SCROLL_DURATION = 2000;
    public static final long NO_ID = -1;
    public static final int NO_POSITION = -1;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "RecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    private static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
    private static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
    private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
    private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
    private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
    private static final String TRACE_SCROLL_TAG = "RV Scroll";
    public static final int VERTICAL = 1;
    private static final Interpolator sQuinticInterpolator;
    private RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    private OnItemTouchListener mActiveOnItemTouchListener;
    private Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    private boolean mAdapterUpdateDuringMeasure;
    private EdgeEffectCompat mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    ChildHelper mChildHelper;
    private boolean mClipToPadding;
    private boolean mDataSetHasChangedAfterLayout;
    private boolean mEatRequestLayout;
    private int mEatenAccessibilityChangeFlags;
    private boolean mFirstLayoutComplete;
    private boolean mHasFixedSize;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private boolean mIsAttached;
    ItemAnimator mItemAnimator;
    private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    private final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    private int mLastTouchX;
    private int mLastTouchY;
    private LayoutManager mLayout;
    private boolean mLayoutFrozen;
    private int mLayoutOrScrollCounter;
    private boolean mLayoutRequestEaten;
    private EdgeEffectCompat mLeftGlow;
    private final int mMaxFlingVelocity;
    private final int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions;
    private final int[] mNestedOffsets;
    private final RecyclerViewDataObserver mObserver;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    private SavedState mPendingSavedState;
    private final boolean mPostUpdatesOnAnimation;
    private boolean mPostedAnimatorRunner;
    final Recycler mRecycler;
    private RecyclerListener mRecyclerListener;
    private EdgeEffectCompat mRightGlow;
    private final int[] mScrollConsumed;
    private float mScrollFactor;
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset;
    private int mScrollPointerId;
    private int mScrollState;
    private final NestedScrollingChildHelper mScrollingChildHelper;
    final State mState;
    private final Rect mTempRect;
    private EdgeEffectCompat mTopGlow;
    private int mTouchSlop;
    private final Runnable mUpdateChildViewsRunnable;
    private VelocityTracker mVelocityTracker;
    private final ViewFlinger mViewFlinger;
    private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
    final ViewInfoStore mViewInfoStore;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20;
        FORCE_INVALIDATE_DISPLAY_LIST = bl;
        LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE};
        sQuinticInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return f * f * f * f * (f -= 1.0f) + 1.0f;
            }
        };
    }

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    public RecyclerView(Context context, @Nullable AttributeSet attributeSet, int n) {
        boolean bl = false;
        super(context, attributeSet, n);
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new Runnable(){

            @Override
            public void run() {
                if (!RecyclerView.this.mFirstLayoutComplete || RecyclerView.this.isLayoutRequested()) {
                    return;
                }
                if (RecyclerView.this.mLayoutFrozen) {
                    RecyclerView.this.mLayoutRequestEaten = true;
                    return;
                }
                RecyclerView.this.consumePendingUpdateOperations();
            }
        };
        this.mTempRect = new Rect();
        this.mItemDecorations = new ArrayList();
        this.mOnItemTouchListeners = new ArrayList();
        this.mDataSetHasChangedAfterLayout = false;
        this.mLayoutOrScrollCounter = 0;
        this.mItemAnimator = new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScrollFactor = Float.MIN_VALUE;
        this.mViewFlinger = new ViewFlinger();
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mNestedOffsets = new int[2];
        this.mItemAnimatorRunner = new Runnable(){

            @Override
            public void run() {
                if (RecyclerView.this.mItemAnimator != null) {
                    RecyclerView.this.mItemAnimator.runPendingAnimations();
                }
                RecyclerView.this.mPostedAnimatorRunner = false;
            }
        };
        this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback(){

            @Override
            public void processAppeared(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo itemHolderInfo, ItemAnimator.ItemHolderInfo itemHolderInfo2) {
                RecyclerView.this.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }

            @Override
            public void processDisappeared(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo itemHolderInfo, ItemAnimator.ItemHolderInfo itemHolderInfo2) {
                RecyclerView.this.mRecycler.unscrapView(viewHolder);
                RecyclerView.this.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }

            /*
             * Enabled aggressive block sorting
             */
            @Override
            public void processPersistent(ViewHolder viewHolder, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo2) {
                viewHolder.setIsRecyclable(false);
                if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
                    if (!RecyclerView.this.mItemAnimator.animateChange(viewHolder, viewHolder, itemHolderInfo, itemHolderInfo2)) return;
                    {
                        RecyclerView.this.postAnimationRunner();
                        return;
                    }
                } else {
                    if (!RecyclerView.this.mItemAnimator.animatePersistence(viewHolder, itemHolderInfo, itemHolderInfo2)) return;
                    {
                        RecyclerView.this.postAnimationRunner();
                        return;
                    }
                }
            }

            @Override
            public void unused(ViewHolder viewHolder) {
                RecyclerView.this.mLayout.removeAndRecycleView(viewHolder.itemView, RecyclerView.this.mRecycler);
            }
        };
        this.setScrollContainer(true);
        this.setFocusableInTouchMode(true);
        boolean bl2 = Build.VERSION.SDK_INT >= 16;
        this.mPostUpdatesOnAnimation = bl2;
        ViewConfiguration viewConfiguration = ViewConfiguration.get((Context)context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        bl2 = bl;
        if (ViewCompat.getOverScrollMode((View)this) == 2) {
            bl2 = true;
        }
        this.setWillNotDraw(bl2);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        this.initAdapterManager();
        this.initChildrenHelper();
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager)this.getContext().getSystemService("accessibility");
        this.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        if (attributeSet != null) {
            viewConfiguration = context.obtainStyledAttributes(attributeSet, R.styleable.RecyclerView, n, 0);
            String string2 = viewConfiguration.getString(R.styleable.RecyclerView_layoutManager);
            viewConfiguration.recycle();
            this.createLayoutManager(context, string2, attributeSet, n, 0);
        }
        this.mScrollingChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addAnimatingView(ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        boolean bl = view.getParent() == this;
        this.mRecycler.unscrapView(this.getChildViewHolder(view));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
            return;
        }
        if (!bl) {
            this.mChildHelper.addView(view, true);
            return;
        }
        this.mChildHelper.hide(view);
    }

    private void animateAppearance(@NonNull ViewHolder viewHolder, @Nullable ItemAnimator.ItemHolderInfo itemHolderInfo, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo2) {
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }

    private void animateChange(@NonNull ViewHolder viewHolder, @NonNull ViewHolder viewHolder2, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo2) {
        viewHolder.setIsRecyclable(false);
        if (viewHolder != viewHolder2) {
            viewHolder.mShadowedHolder = viewHolder2;
            this.addAnimatingView(viewHolder);
            this.mRecycler.unscrapView(viewHolder);
            viewHolder2.setIsRecyclable(false);
            viewHolder2.mShadowingHolder = viewHolder;
        }
        if (this.mItemAnimator.animateChange(viewHolder, viewHolder2, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }

    private void animateDisappearance(@NonNull ViewHolder viewHolder, @NonNull ItemAnimator.ItemHolderInfo itemHolderInfo, @Nullable ItemAnimator.ItemHolderInfo itemHolderInfo2) {
        this.addAnimatingView(viewHolder);
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }

    private boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
        if (this.mItemAnimator == null || this.mItemAnimator.canReuseUpdatedViewHolder(viewHolder)) {
            return true;
        }
        return false;
    }

    private void cancelTouch() {
        this.resetTouch();
        this.setScrollState(0);
    }

    private void considerReleasingGlowsOnScroll(int n, int n2) {
        boolean bl;
        boolean bl2 = bl = false;
        if (this.mLeftGlow != null) {
            bl2 = bl;
            if (!this.mLeftGlow.isFinished()) {
                bl2 = bl;
                if (n > 0) {
                    bl2 = this.mLeftGlow.onRelease();
                }
            }
        }
        bl = bl2;
        if (this.mRightGlow != null) {
            bl = bl2;
            if (!this.mRightGlow.isFinished()) {
                bl = bl2;
                if (n < 0) {
                    bl = bl2 | this.mRightGlow.onRelease();
                }
            }
        }
        bl2 = bl;
        if (this.mTopGlow != null) {
            bl2 = bl;
            if (!this.mTopGlow.isFinished()) {
                bl2 = bl;
                if (n2 > 0) {
                    bl2 = bl | this.mTopGlow.onRelease();
                }
            }
        }
        bl = bl2;
        if (this.mBottomGlow != null) {
            bl = bl2;
            if (!this.mBottomGlow.isFinished()) {
                bl = bl2;
                if (n2 < 0) {
                    bl = bl2 | this.mBottomGlow.onRelease();
                }
            }
        }
        if (bl) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void consumePendingUpdateOperations() {
        if (!this.mFirstLayoutComplete) {
            return;
        }
        if (this.mDataSetHasChangedAfterLayout) {
            TraceCompat.beginSection("RV FullInvalidate");
            this.dispatchLayout();
            TraceCompat.endSection();
            return;
        }
        if (!this.mAdapterHelper.hasPendingUpdates()) return;
        if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
            TraceCompat.beginSection("RV PartialInvalidate");
            this.eatRequestLayout();
            this.mAdapterHelper.preProcess();
            if (!this.mLayoutRequestEaten) {
                if (this.hasUpdatedView()) {
                    this.dispatchLayout();
                } else {
                    this.mAdapterHelper.consumePostponedUpdates();
                }
            }
            this.resumeRequestLayout(true);
            TraceCompat.endSection();
            return;
        }
        if (!this.mAdapterHelper.hasPendingUpdates()) return;
        TraceCompat.beginSection("RV FullInvalidate");
        this.dispatchLayout();
        TraceCompat.endSection();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void createLayoutManager(Context var1_1, String var2_8, AttributeSet var3_9, int var4_10, int var5_11) {
        if (var2_8 == null) return;
        if ((var2_8 = var2_8.trim()).length() == 0) return;
        var7_12 = this.getFullClassName((Context)var1_1, (String)var2_8);
        try {
            var2_8 = this.isInEditMode() != false ? this.getClass().getClassLoader() : var1_1.getClassLoader();
            {
                var8_13 = var2_8.loadClass(var7_12).asSubclass(LayoutManager.class);
                var2_8 = null;
                try {
                    var6_14 = var8_13.getConstructor(RecyclerView.LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                }
                catch (NoSuchMethodException var6_15) {
                    try {
                        var1_1 = var8_13.getConstructor(new Class[0]);
                    }
                    catch (NoSuchMethodException var1_2) {
                        var1_2.initCause(var6_15);
                        throw new IllegalStateException(var3_9.getPositionDescription() + ": Error creating LayoutManager " + var7_12, var1_2);
                    }
lbl16: // 2 sources:
                    var1_1.setAccessible(true);
                    this.setLayoutManager(var1_1.newInstance((Object[])var2_8));
                    return;
                }
                var2_8 = new Object[]{var1_1, var3_9, var4_10, var5_11};
                var1_1 = var6_14;
                ** try [egrp 2[TRYBLOCK] [16, 17, 18, 19, 20 : 96->184)] { 
lbl22: // 1 sources:
                ** GOTO lbl16
            }
        }
lbl23: // 2 sources:
        catch (ClassNotFoundException var1_3) {
            throw new IllegalStateException(var3_9.getPositionDescription() + ": Unable to find LayoutManager " + var7_12, var1_3);
        }
lbl25: // 2 sources:
        catch (InvocationTargetException var1_4) {
            throw new IllegalStateException(var3_9.getPositionDescription() + ": Could not instantiate the LayoutManager: " + var7_12, var1_4);
        }
lbl27: // 2 sources:
        catch (InstantiationException var1_5) {
            throw new IllegalStateException(var3_9.getPositionDescription() + ": Could not instantiate the LayoutManager: " + var7_12, var1_5);
        }
lbl29: // 2 sources:
        catch (IllegalAccessException var1_6) {
            throw new IllegalStateException(var3_9.getPositionDescription() + ": Cannot access non-public constructor " + var7_12, var1_6);
        }
lbl31: // 2 sources:
        catch (ClassCastException var1_7) {
            throw new IllegalStateException(var3_9.getPositionDescription() + ": Class is not a LayoutManager " + var7_12, var1_7);
        }
    }

    /*
     * Exception decompiling
     */
    private void defaultOnMeasure(int var1_1, int var2_2) {
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

    /*
     * Enabled aggressive block sorting
     */
    private boolean didChildRangeChange(int n, int n2) {
        boolean bl;
        boolean bl2 = false;
        int n3 = this.mChildHelper.getChildCount();
        if (n3 == 0) {
            if (n != 0) return true;
            bl = bl2;
            if (n2 == 0) return bl;
            return true;
        }
        int n4 = 0;
        do {
            bl = bl2;
            if (n4 >= n3) return bl;
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(n4));
            if (!viewHolder.shouldIgnore()) {
                int n5 = viewHolder.getLayoutPosition();
                if (n5 < n) return true;
                if (n5 > n2) {
                    return true;
                }
            }
            ++n4;
        } while (true);
    }

    private void dispatchChildAttached(View view) {
        ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
        this.onChildAttachedToWindow(view);
        if (this.mAdapter != null && viewHolder != null) {
            this.mAdapter.onViewAttachedToWindow(viewHolder);
        }
        if (this.mOnChildAttachStateListeners != null) {
            for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewAttachedToWindow(view);
            }
        }
    }

    private void dispatchChildDetached(View view) {
        ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
        this.onChildDetachedFromWindow(view);
        if (this.mAdapter != null && viewHolder != null) {
            this.mAdapter.onViewDetachedFromWindow(viewHolder);
        }
        if (this.mOnChildAttachStateListeners != null) {
            for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewDetachedFromWindow(view);
            }
        }
    }

    private void dispatchContentChangedIfNecessary() {
        int n = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (n != 0 && this.isAccessibilityEnabled()) {
            AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain();
            accessibilityEvent.setEventType(2048);
            AccessibilityEventCompat.setContentChangeTypes(accessibilityEvent, n);
            this.sendAccessibilityEventUnchecked(accessibilityEvent);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean dispatchOnItemTouch(MotionEvent motionEvent) {
        int n = motionEvent.getAction();
        if (this.mActiveOnItemTouchListener != null) {
            if (n != 0) {
                this.mActiveOnItemTouchListener.onTouchEvent(this, motionEvent);
                if (n != 3 && n != 1) return true;
                {
                    this.mActiveOnItemTouchListener = null;
                    return true;
                }
            }
            this.mActiveOnItemTouchListener = null;
        }
        if (n == 0) {
            return false;
        }
        int n2 = this.mOnItemTouchListeners.size();
        n = 0;
        while (n < n2) {
            OnItemTouchListener onItemTouchListener = this.mOnItemTouchListeners.get(n);
            if (onItemTouchListener.onInterceptTouchEvent(this, motionEvent)) {
                this.mActiveOnItemTouchListener = onItemTouchListener;
                return true;
            }
            ++n;
        }
        return false;
    }

    private boolean dispatchOnItemTouchIntercept(MotionEvent motionEvent) {
        int n = motionEvent.getAction();
        if (n == 3 || n == 0) {
            this.mActiveOnItemTouchListener = null;
        }
        int n2 = this.mOnItemTouchListeners.size();
        for (int i = 0; i < n2; ++i) {
            OnItemTouchListener onItemTouchListener = this.mOnItemTouchListeners.get(i);
            if (!onItemTouchListener.onInterceptTouchEvent(this, motionEvent) || n == 3) continue;
            this.mActiveOnItemTouchListener = onItemTouchListener;
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void findMinMaxChildLayoutPositions(int[] arrn) {
        int n = this.mChildHelper.getChildCount();
        if (n == 0) {
            arrn[0] = 0;
            arrn[1] = 0;
            return;
        }
        int n2 = Integer.MAX_VALUE;
        int n3 = Integer.MIN_VALUE;
        int n4 = 0;
        do {
            int n5;
            if (n4 >= n) {
                arrn[0] = n2;
                arrn[1] = n3;
                return;
            }
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(n4));
            if (viewHolder.shouldIgnore()) {
                n5 = n2;
                n2 = n3;
            } else {
                int n6 = viewHolder.getLayoutPosition();
                int n7 = n2;
                if (n6 < n2) {
                    n7 = n6;
                }
                n2 = n3;
                n5 = n7;
                if (n6 > n3) {
                    n2 = n6;
                    n5 = n7;
                }
            }
            ++n4;
            n3 = n2;
            n2 = n5;
        } while (true);
    }

    private int getAdapterPositionFor(ViewHolder viewHolder) {
        if (viewHolder.hasAnyOfTheFlags(524) || !viewHolder.isBound()) {
            return -1;
        }
        return this.mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
    }

    static ViewHolder getChildViewHolderInt(View view) {
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).mViewHolder;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private String getFullClassName(Context object, String string2) {
        if (string2.charAt(0) == '.') {
            return object.getPackageName() + string2;
        }
        object = string2;
        if (string2.contains(".")) return object;
        return RecyclerView.class.getPackage().getName() + '.' + string2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private float getScrollFactor() {
        if (this.mScrollFactor != Float.MIN_VALUE) return this.mScrollFactor;
        TypedValue typedValue = new TypedValue();
        if (!this.getContext().getTheme().resolveAttribute(16842829, typedValue, true)) return 0.0f;
        this.mScrollFactor = typedValue.getDimension(this.getContext().getResources().getDisplayMetrics());
        return this.mScrollFactor;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean hasUpdatedView() {
        int n = this.mChildHelper.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(n2));
            if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.isUpdated()) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper(new ChildHelper.Callback(){

            @Override
            public void addView(View view, int n) {
                RecyclerView.this.addView(view, n);
                RecyclerView.this.dispatchChildAttached(view);
            }

            @Override
            public void attachViewToParent(View view, int n, ViewGroup.LayoutParams layoutParams) {
                ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
                if (viewHolder != null) {
                    if (!viewHolder.isTmpDetached() && !viewHolder.shouldIgnore()) {
                        throw new IllegalArgumentException("Called attach on a child which is not detached: " + viewHolder);
                    }
                    viewHolder.clearTmpDetachFlag();
                }
                RecyclerView.this.attachViewToParent(view, n, layoutParams);
            }

            @Override
            public void detachViewFromParent(int n) {
                Object object = this.getChildAt(n);
                if (object != null && (object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
                    if (object.isTmpDetached() && !object.shouldIgnore()) {
                        throw new IllegalArgumentException("called detach on an already detached child " + object);
                    }
                    object.addFlags(256);
                }
                RecyclerView.this.detachViewFromParent(n);
            }

            @Override
            public View getChildAt(int n) {
                return RecyclerView.this.getChildAt(n);
            }

            @Override
            public int getChildCount() {
                return RecyclerView.this.getChildCount();
            }

            @Override
            public ViewHolder getChildViewHolder(View view) {
                return RecyclerView.getChildViewHolderInt(view);
            }

            @Override
            public int indexOfChild(View view) {
                return RecyclerView.this.indexOfChild(view);
            }

            @Override
            public void onEnteredHiddenState(View object) {
                if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
                    ((ViewHolder)object).onEnteredHiddenState();
                }
            }

            @Override
            public void onLeftHiddenState(View object) {
                if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
                    ((ViewHolder)object).onLeftHiddenState();
                }
            }

            @Override
            public void removeAllViews() {
                int n = this.getChildCount();
                for (int i = 0; i < n; ++i) {
                    RecyclerView.this.dispatchChildDetached(this.getChildAt(i));
                }
                RecyclerView.this.removeAllViews();
            }

            @Override
            public void removeViewAt(int n) {
                View view = RecyclerView.this.getChildAt(n);
                if (view != null) {
                    RecyclerView.this.dispatchChildDetached(view);
                }
                RecyclerView.this.removeViewAt(n);
            }
        });
    }

    private void jumpToPositionForSmoothScroller(int n) {
        if (this.mLayout == null) {
            return;
        }
        this.mLayout.scrollToPosition(n);
        this.awakenScrollBars();
    }

    private void onEnterLayoutOrScroll() {
        ++this.mLayoutOrScrollCounter;
    }

    private void onExitLayoutOrScroll() {
        --this.mLayoutOrScrollCounter;
        if (this.mLayoutOrScrollCounter < 1) {
            this.mLayoutOrScrollCounter = 0;
            this.dispatchContentChangedIfNecessary();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onPointerUp(MotionEvent motionEvent) {
        int n = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, n) == this.mScrollPointerId) {
            int n2;
            n = n == 0 ? 1 : 0;
            this.mScrollPointerId = MotionEventCompat.getPointerId(motionEvent, n);
            this.mLastTouchX = n2 = (int)(MotionEventCompat.getX(motionEvent, n) + 0.5f);
            this.mInitialTouchX = n2;
            this.mLastTouchY = n = (int)(MotionEventCompat.getY(motionEvent, n) + 0.5f);
            this.mInitialTouchY = n;
        }
    }

    private void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation((View)this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
        }
    }

    private boolean predictiveItemAnimationsEnabled() {
        if (this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations()) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void processAdapterUpdatesAndSetAnimationFlags() {
        boolean bl = true;
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            this.markKnownViewsInvalid();
            this.mLayout.onItemsChanged(this);
        }
        if (this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations()) {
            this.mAdapterHelper.preProcess();
        } else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        boolean bl2 = this.mItemsAddedOrRemoved || this.mItemsChanged;
        State state = this.mState;
        boolean bl3 = !(!this.mFirstLayoutComplete || this.mItemAnimator == null || !this.mDataSetHasChangedAfterLayout && !bl2 && !this.mLayout.mRequestedSimpleAnimations || this.mDataSetHasChangedAfterLayout && !this.mAdapter.hasStableIds());
        state.mRunSimpleAnimations = bl3;
        state = this.mState;
        bl3 = this.mState.mRunSimpleAnimations && bl2 && !this.mDataSetHasChangedAfterLayout && this.predictiveItemAnimationsEnabled() ? bl : false;
        state.mRunPredictiveAnimations = bl3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void pullGlows(float f, float f2, float f3, float f4) {
        boolean bl;
        boolean bl2 = false;
        if (f2 < 0.0f) {
            this.ensureLeftGlow();
            bl = bl2;
            if (this.mLeftGlow.onPull((- f2) / (float)this.getWidth(), 1.0f - f3 / (float)this.getHeight())) {
                bl = true;
            }
        } else {
            bl = bl2;
            if (f2 > 0.0f) {
                this.ensureRightGlow();
                bl = bl2;
                if (this.mRightGlow.onPull(f2 / (float)this.getWidth(), f3 / (float)this.getHeight())) {
                    bl = true;
                }
            }
        }
        if (f4 < 0.0f) {
            this.ensureTopGlow();
            bl2 = bl;
            if (this.mTopGlow.onPull((- f4) / (float)this.getHeight(), f / (float)this.getWidth())) {
                bl2 = true;
            }
        } else {
            bl2 = bl;
            if (f4 > 0.0f) {
                this.ensureBottomGlow();
                bl2 = bl;
                if (this.mBottomGlow.onPull(f4 / (float)this.getHeight(), 1.0f - f / (float)this.getWidth())) {
                    bl2 = true;
                }
            }
        }
        if (bl2 || f2 != 0.0f || f4 != 0.0f) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    private void recordAnimationInfoIfBouncedHiddenView(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo itemHolderInfo) {
        viewHolder.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
            long l = this.getChangedHolderKey(viewHolder);
            this.mViewInfoStore.addToOldChangeHolders(l, viewHolder);
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
    }

    private void releaseGlows() {
        boolean bl = false;
        if (this.mLeftGlow != null) {
            bl = this.mLeftGlow.onRelease();
        }
        boolean bl2 = bl;
        if (this.mTopGlow != null) {
            bl2 = bl | this.mTopGlow.onRelease();
        }
        bl = bl2;
        if (this.mRightGlow != null) {
            bl = bl2 | this.mRightGlow.onRelease();
        }
        bl2 = bl;
        if (this.mBottomGlow != null) {
            bl2 = bl | this.mBottomGlow.onRelease();
        }
        if (bl2) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    private boolean removeAnimatingView(View object) {
        this.eatRequestLayout();
        boolean bl = this.mChildHelper.removeViewIfHidden((View)object);
        if (bl) {
            object = RecyclerView.getChildViewHolderInt((View)object);
            this.mRecycler.unscrapView((ViewHolder)object);
            this.mRecycler.recycleViewHolderInternal((ViewHolder)object);
        }
        this.resumeRequestLayout(false);
        return bl;
    }

    private void repositionShadowingViews() {
        int n = this.mChildHelper.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.mChildHelper.getChildAt(i);
            ViewHolder viewHolder = this.getChildViewHolder(view);
            if (viewHolder == null || viewHolder.mShadowingHolder == null) continue;
            viewHolder = viewHolder.mShadowingHolder.itemView;
            int n2 = view.getLeft();
            int n3 = view.getTop();
            if (n2 == viewHolder.getLeft() && n3 == viewHolder.getTop()) continue;
            viewHolder.layout(n2, n3, viewHolder.getWidth() + n2, viewHolder.getHeight() + n3);
        }
    }

    private void resetTouch() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
        }
        this.stopNestedScroll();
        this.releaseGlows();
    }

    private void setAdapterInternal(Adapter adapter, boolean bl, boolean bl2) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!bl || bl2) {
            if (this.mItemAnimator != null) {
                this.mItemAnimator.endAnimations();
            }
            if (this.mLayout != null) {
                this.mLayout.removeAndRecycleAllViews(this.mRecycler);
                this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            }
            this.mRecycler.clear();
        }
        this.mAdapterHelper.reset();
        Adapter adapter2 = this.mAdapter;
        this.mAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mObserver);
            adapter.onAttachedToRecyclerView(this);
        }
        if (this.mLayout != null) {
            this.mLayout.onAdapterChanged(adapter2, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(adapter2, this.mAdapter, bl);
        this.mState.mStructureChanged = true;
        this.markKnownViewsInvalid();
    }

    private void setDataSetChangedAfterLayout() {
        if (this.mDataSetHasChangedAfterLayout) {
            return;
        }
        this.mDataSetHasChangedAfterLayout = true;
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder == null || viewHolder.shouldIgnore()) continue;
            viewHolder.addFlags(512);
        }
        this.mRecycler.setAdapterPositionsAsUnknown();
    }

    private void setScrollState(int n) {
        if (n == this.mScrollState) {
            return;
        }
        this.mScrollState = n;
        if (n != 2) {
            this.stopScrollersInternal();
        }
        this.dispatchOnScrollStateChanged(n);
    }

    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        if (this.mLayout != null) {
            this.mLayout.stopSmoothScroller();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    void absorbGlows(int n, int n2) {
        if (n < 0) {
            this.ensureLeftGlow();
            this.mLeftGlow.onAbsorb(- n);
        } else if (n > 0) {
            this.ensureRightGlow();
            this.mRightGlow.onAbsorb(n);
        }
        if (n2 < 0) {
            this.ensureTopGlow();
            this.mTopGlow.onAbsorb(- n2);
        } else if (n2 > 0) {
            this.ensureBottomGlow();
            this.mBottomGlow.onAbsorb(n2);
        }
        if (n != 0 || n2 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    public void addFocusables(ArrayList<View> arrayList, int n, int n2) {
        if (this.mLayout == null || !this.mLayout.onAddFocusables(this, arrayList, n, n2)) {
            super.addFocusables(arrayList, n, n2);
        }
    }

    public void addItemDecoration(ItemDecoration itemDecoration) {
        this.addItemDecoration(itemDecoration, -1);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void addItemDecoration(ItemDecoration itemDecoration, int n) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            this.setWillNotDraw(false);
        }
        if (n < 0) {
            this.mItemDecorations.add(itemDecoration);
        } else {
            this.mItemDecorations.add(n, itemDecoration);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }

    public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        if (this.mOnChildAttachStateListeners == null) {
            this.mOnChildAttachStateListeners = new ArrayList<OnChildAttachStateChangeListener>();
        }
        this.mOnChildAttachStateListeners.add(onChildAttachStateChangeListener);
    }

    public void addOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.mOnItemTouchListeners.add(onItemTouchListener);
    }

    public void addOnScrollListener(OnScrollListener onScrollListener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList<OnScrollListener>();
        }
        this.mScrollListeners.add(onScrollListener);
    }

    void assertInLayoutOrScroll(String string2) {
        if (!this.isComputingLayout()) {
            if (string2 == null) {
                throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling");
            }
            throw new IllegalStateException(string2);
        }
    }

    void assertNotInLayoutOrScroll(String string2) {
        if (this.isComputingLayout()) {
            if (string2 == null) {
                throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling");
            }
            throw new IllegalStateException(string2);
        }
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams && this.mLayout.checkLayoutParams((LayoutParams)layoutParams)) {
            return true;
        }
        return false;
    }

    void clearOldPositions() {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder.shouldIgnore()) continue;
            viewHolder.clearOldPosition();
        }
        this.mRecycler.clearOldPositions();
    }

    public void clearOnChildAttachStateChangeListeners() {
        if (this.mOnChildAttachStateListeners != null) {
            this.mOnChildAttachStateListeners.clear();
        }
    }

    public void clearOnScrollListeners() {
        if (this.mScrollListeners != null) {
            this.mScrollListeners.clear();
        }
    }

    @Override
    public int computeHorizontalScrollExtent() {
        if (this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return 0;
    }

    @Override
    public int computeHorizontalScrollOffset() {
        if (this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return 0;
    }

    @Override
    public int computeHorizontalScrollRange() {
        if (this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return 0;
    }

    @Override
    public int computeVerticalScrollExtent() {
        if (this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return 0;
    }

    @Override
    public int computeVerticalScrollOffset() {
        if (this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return 0;
    }

    @Override
    public int computeVerticalScrollRange() {
        if (this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void dispatchLayout() {
        long l;
        int n;
        int n2;
        ItemAnimator.ItemHolderInfo itemHolderInfo;
        if (this.mAdapter == null) {
            Log.e((String)"RecyclerView", (String)"No adapter attached; skipping layout");
            return;
        }
        if (this.mLayout == null) {
            Log.e((String)"RecyclerView", (String)"No layout manager attached; skipping layout");
            return;
        }
        this.mViewInfoStore.clear();
        this.eatRequestLayout();
        this.onEnterLayoutOrScroll();
        this.processAdapterUpdatesAndSetAnimationFlags();
        Object object = this.mState;
        boolean bl = this.mState.mRunSimpleAnimations && this.mItemsChanged;
        ((State)object).mTrackOldChangeHolders = bl;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        this.mState.mInPreLayout = this.mState.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            n2 = this.mChildHelper.getChildCount();
            for (n = 0; n < n2; ++n) {
                object = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(n));
                if (object.shouldIgnore() || object.isInvalid() && !this.mAdapter.hasStableIds()) continue;
                itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, (ViewHolder)object, ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)object), object.getUnmodifiedPayloads());
                this.mViewInfoStore.addToPreLayout((ViewHolder)object, itemHolderInfo);
                if (!this.mState.mTrackOldChangeHolders || !object.isUpdated() || object.isRemoved() || object.shouldIgnore() || object.isInvalid()) continue;
                l = this.getChangedHolderKey((ViewHolder)object);
                this.mViewInfoStore.addToOldChangeHolders(l, (ViewHolder)object);
            }
        }
        if (!this.mState.mRunPredictiveAnimations) {
            this.clearOldPositions();
        } else {
            this.saveOldPositions();
            bl = this.mState.mStructureChanged;
            this.mState.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
            this.mState.mStructureChanged = bl;
            for (n = 0; n < this.mChildHelper.getChildCount(); ++n) {
                object = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(n));
                if (object.shouldIgnore() || this.mViewInfoStore.isInPreLayout((ViewHolder)object)) continue;
                int n3 = ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)object);
                bl = object.hasAnyOfTheFlags(8192);
                n2 = n3;
                if (!bl) {
                    n2 = n3 | 4096;
                }
                itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, (ViewHolder)object, n2, object.getUnmodifiedPayloads());
                if (bl) {
                    this.recordAnimationInfoIfBouncedHiddenView((ViewHolder)object, itemHolderInfo);
                    continue;
                }
                this.mViewInfoStore.addToAppearedInPreLayoutHolders((ViewHolder)object, itemHolderInfo);
            }
            this.clearOldPositions();
            this.mAdapterHelper.consumePostponedUpdates();
        }
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        this.mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = false;
        this.mPendingSavedState = null;
        object = this.mState;
        bl = this.mState.mRunSimpleAnimations && this.mItemAnimator != null;
        ((State)object).mRunSimpleAnimations = bl;
        if (this.mState.mRunSimpleAnimations) {
            n2 = this.mChildHelper.getChildCount();
            for (n = 0; n < n2; ++n) {
                object = RecyclerView.getChildViewHolderInt(this.mChildHelper.getChildAt(n));
                if (object.shouldIgnore()) continue;
                l = this.getChangedHolderKey((ViewHolder)object);
                itemHolderInfo = this.mItemAnimator.recordPostLayoutInformation(this.mState, (ViewHolder)object);
                ViewHolder viewHolder = this.mViewInfoStore.getFromOldChangeHolders(l);
                if (viewHolder != null && !viewHolder.shouldIgnore()) {
                    this.animateChange(viewHolder, (ViewHolder)object, this.mViewInfoStore.popFromPreLayout(viewHolder), itemHolderInfo);
                    continue;
                }
                this.mViewInfoStore.addToPostLayout((ViewHolder)object, itemHolderInfo);
            }
            this.mViewInfoStore.process(this.mViewInfoProcessCallback);
        }
        this.resumeRequestLayout(false);
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        this.mState.mPreviousLayoutItemCount = this.mState.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        this.mState.mRunSimpleAnimations = false;
        this.mState.mRunPredictiveAnimations = false;
        this.onExitLayoutOrScroll();
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        this.mViewInfoStore.clear();
        if (!this.didChildRangeChange(this.mMinMaxLayoutPositions[0], this.mMinMaxLayoutPositions[1])) return;
        this.dispatchOnScrolled(0, 0);
    }

    @Override
    public boolean dispatchNestedFling(float f, float f2, boolean bl) {
        return this.mScrollingChildHelper.dispatchNestedFling(f, f2, bl);
    }

    @Override
    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mScrollingChildHelper.dispatchNestedPreFling(f, f2);
    }

    @Override
    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2) {
        return this.mScrollingChildHelper.dispatchNestedPreScroll(n, n2, arrn, arrn2);
    }

    @Override
    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn) {
        return this.mScrollingChildHelper.dispatchNestedScroll(n, n2, n3, n4, arrn);
    }

    void dispatchOnScrollStateChanged(int n) {
        if (this.mLayout != null) {
            this.mLayout.onScrollStateChanged(n);
        }
        this.onScrollStateChanged(n);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(this, n);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; --i) {
                this.mScrollListeners.get(i).onScrollStateChanged(this, n);
            }
        }
    }

    void dispatchOnScrolled(int n, int n2) {
        int n3 = this.getScrollX();
        int n4 = this.getScrollY();
        this.onScrollChanged(n3, n4, n3, n4);
        this.onScrolled(n, n2);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrolled(this, n, n2);
        }
        if (this.mScrollListeners != null) {
            for (n3 = this.mScrollListeners.size() - 1; n3 >= 0; --n3) {
                this.mScrollListeners.get(n3).onScrolled(this, n, n2);
            }
        }
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        this.dispatchThawSelfOnly(sparseArray);
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> sparseArray) {
        this.dispatchFreezeSelfOnly(sparseArray);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void draw(Canvas canvas) {
        int n;
        int n2;
        int n3 = 1;
        super.draw(canvas);
        int n4 = this.mItemDecorations.size();
        for (n2 = 0; n2 < n4; ++n2) {
            this.mItemDecorations.get(n2).onDrawOver(canvas, this, this.mState);
        }
        n4 = n2 = 0;
        if (this.mLeftGlow != null) {
            n4 = n2;
            if (!this.mLeftGlow.isFinished()) {
                n = canvas.save();
                n2 = this.mClipToPadding ? this.getPaddingBottom() : 0;
                canvas.rotate(270.0f);
                canvas.translate((float)(- this.getHeight() + n2), 0.0f);
                n4 = this.mLeftGlow != null && this.mLeftGlow.draw(canvas) ? 1 : 0;
                canvas.restoreToCount(n);
            }
        }
        n2 = n4;
        if (this.mTopGlow != null) {
            n2 = n4;
            if (!this.mTopGlow.isFinished()) {
                n = canvas.save();
                if (this.mClipToPadding) {
                    canvas.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
                }
                n2 = this.mTopGlow != null && this.mTopGlow.draw(canvas) ? 1 : 0;
                n2 = n4 | n2;
                canvas.restoreToCount(n);
            }
        }
        n4 = n2;
        if (this.mRightGlow != null) {
            n4 = n2;
            if (!this.mRightGlow.isFinished()) {
                n = canvas.save();
                int n5 = this.getWidth();
                n4 = this.mClipToPadding ? this.getPaddingTop() : 0;
                canvas.rotate(90.0f);
                canvas.translate((float)(- n4), (float)(- n5));
                n4 = this.mRightGlow != null && this.mRightGlow.draw(canvas) ? 1 : 0;
                n4 = n2 | n4;
                canvas.restoreToCount(n);
            }
        }
        n2 = n4;
        if (this.mBottomGlow != null) {
            n2 = n4;
            if (!this.mBottomGlow.isFinished()) {
                n = canvas.save();
                canvas.rotate(180.0f);
                if (this.mClipToPadding) {
                    canvas.translate((float)(- this.getWidth() + this.getPaddingRight()), (float)(- this.getHeight() + this.getPaddingBottom()));
                } else {
                    canvas.translate((float)(- this.getWidth()), (float)(- this.getHeight()));
                }
                n2 = this.mBottomGlow != null && this.mBottomGlow.draw(canvas) ? n3 : 0;
                n2 = n4 | n2;
                canvas.restoreToCount(n);
            }
        }
        n4 = n2;
        if (n2 == 0) {
            n4 = n2;
            if (this.mItemAnimator != null) {
                n4 = n2;
                if (this.mItemDecorations.size() > 0) {
                    n4 = n2;
                    if (this.mItemAnimator.isRunning()) {
                        n4 = 1;
                    }
                }
            }
        }
        if (n4 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    public boolean drawChild(Canvas canvas, View view, long l) {
        return super.drawChild(canvas, view, l);
    }

    void eatRequestLayout() {
        if (!this.mEatRequestLayout) {
            this.mEatRequestLayout = true;
            if (!this.mLayoutFrozen) {
                this.mLayoutRequestEaten = false;
            }
        }
    }

    void ensureBottomGlow() {
        if (this.mBottomGlow != null) {
            return;
        }
        this.mBottomGlow = new EdgeEffectCompat(this.getContext());
        if (this.mClipToPadding) {
            this.mBottomGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
            return;
        }
        this.mBottomGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
    }

    void ensureLeftGlow() {
        if (this.mLeftGlow != null) {
            return;
        }
        this.mLeftGlow = new EdgeEffectCompat(this.getContext());
        if (this.mClipToPadding) {
            this.mLeftGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
            return;
        }
        this.mLeftGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
    }

    void ensureRightGlow() {
        if (this.mRightGlow != null) {
            return;
        }
        this.mRightGlow = new EdgeEffectCompat(this.getContext());
        if (this.mClipToPadding) {
            this.mRightGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
            return;
        }
        this.mRightGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
    }

    void ensureTopGlow() {
        if (this.mTopGlow != null) {
            return;
        }
        this.mTopGlow = new EdgeEffectCompat(this.getContext());
        if (this.mClipToPadding) {
            this.mTopGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
            return;
        }
        this.mTopGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
    }

    public View findChildViewUnder(float f, float f2) {
        for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
            View view = this.mChildHelper.getChildAt(i);
            float f3 = ViewCompat.getTranslationX(view);
            float f4 = ViewCompat.getTranslationY(view);
            if (f < (float)view.getLeft() + f3 || f > (float)view.getRight() + f3 || f2 < (float)view.getTop() + f4 || f2 > (float)view.getBottom() + f4) continue;
            return view;
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ViewHolder findViewHolderForAdapterPosition(int n) {
        if (this.mDataSetHasChangedAfterLayout) {
            return null;
        }
        int n2 = this.mChildHelper.getUnfilteredChildCount();
        int n3 = 0;
        while (n3 < n2) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(n3));
            if (viewHolder != null && !viewHolder.isRemoved()) {
                ViewHolder viewHolder2 = viewHolder;
                if (this.getAdapterPositionFor(viewHolder) == n) return viewHolder2;
            }
            ++n3;
        }
        return null;
    }

    public ViewHolder findViewHolderForItemId(long l) {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder == null || viewHolder.getItemId() != l) continue;
            return viewHolder;
        }
        return null;
    }

    public ViewHolder findViewHolderForLayoutPosition(int n) {
        return this.findViewHolderForPosition(n, false);
    }

    @Deprecated
    public ViewHolder findViewHolderForPosition(int n) {
        return this.findViewHolderForPosition(n, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    ViewHolder findViewHolderForPosition(int n, boolean bl) {
        int n2 = this.mChildHelper.getUnfilteredChildCount();
        int n3 = 0;
        while (n3 < n2) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(n3));
            if (viewHolder != null && !viewHolder.isRemoved() && (bl ? viewHolder.mPosition == n : viewHolder.getLayoutPosition() == n)) {
                return viewHolder;
            }
            ++n3;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean fling(int n, int n2) {
        if (this.mLayout == null) {
            Log.e((String)"RecyclerView", (String)"Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        } else {
            if (this.mLayoutFrozen) return false;
            {
                boolean bl;
                boolean bl2;
                int n3;
                block8 : {
                    bl = this.mLayout.canScrollHorizontally();
                    bl2 = this.mLayout.canScrollVertically();
                    if (bl) {
                        n3 = n;
                        if (Math.abs(n) >= this.mMinFlingVelocity) break block8;
                    }
                    n3 = 0;
                }
                if (!bl2) return false;
                n = n2;
                if (Math.abs(n2) < this.mMinFlingVelocity) {
                    n = 0;
                }
                if (n3 == 0 && n == 0 || this.dispatchNestedPreFling(n3, n)) return false;
                {
                    bl = bl || bl2;
                    this.dispatchNestedFling(n3, n, bl);
                    if (!bl) return false;
                    {
                        n2 = Math.max(- this.mMaxFlingVelocity, Math.min(n3, this.mMaxFlingVelocity));
                        n = Math.max(- this.mMaxFlingVelocity, Math.min(n, this.mMaxFlingVelocity));
                        this.mViewFlinger.fling(n2, n);
                        return true;
                    }
                }
            }
        }
    }

    public View focusSearch(View view, int n) {
        View view2;
        View view3 = this.mLayout.onInterceptFocusSearch(view, n);
        if (view3 != null) {
            return view3;
        }
        view3 = view2 = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view, n);
        if (view2 == null) {
            view3 = view2;
            if (this.mAdapter != null) {
                view3 = view2;
                if (this.mLayout != null) {
                    view3 = view2;
                    if (!this.isComputingLayout()) {
                        view3 = view2;
                        if (!this.mLayoutFrozen) {
                            this.eatRequestLayout();
                            view3 = this.mLayout.onFocusSearchFailed(view, n, this.mRecycler, this.mState);
                            this.resumeRequestLayout(false);
                        }
                    }
                }
            }
        }
        if (view3 != null) {
            return view3;
        }
        return super.focusSearch(view, n);
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateDefaultLayoutParams();
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateLayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateLayoutParams(layoutParams);
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    public int getBaseline() {
        if (this.mLayout != null) {
            return this.mLayout.getBaseline();
        }
        return super.getBaseline();
    }

    long getChangedHolderKey(ViewHolder viewHolder) {
        if (this.mAdapter.hasStableIds()) {
            return viewHolder.getItemId();
        }
        return viewHolder.mPosition;
    }

    public int getChildAdapterPosition(View object) {
        if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
            return object.getAdapterPosition();
        }
        return -1;
    }

    protected int getChildDrawingOrder(int n, int n2) {
        if (this.mChildDrawingOrderCallback == null) {
            return super.getChildDrawingOrder(n, n2);
        }
        return this.mChildDrawingOrderCallback.onGetChildDrawingOrder(n, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public long getChildItemId(View object) {
        if (this.mAdapter == null || !this.mAdapter.hasStableIds() || (object = RecyclerView.getChildViewHolderInt((View)object)) == null) {
            return -1;
        }
        return object.getItemId();
    }

    public int getChildLayoutPosition(View object) {
        if ((object = RecyclerView.getChildViewHolderInt((View)object)) != null) {
            return object.getLayoutPosition();
        }
        return -1;
    }

    @Deprecated
    public int getChildPosition(View view) {
        return this.getChildAdapterPosition(view);
    }

    public ViewHolder getChildViewHolder(View view) {
        ViewParent viewParent = view.getParent();
        if (viewParent != null && viewParent != this) {
            throw new IllegalArgumentException("View " + (Object)view + " is not a direct child of " + this);
        }
        return RecyclerView.getChildViewHolderInt(view);
    }

    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }

    Rect getItemDecorInsetsForChild(View view) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.mInsetsDirty) {
            return layoutParams.mDecorInsets;
        }
        Rect rect = layoutParams.mDecorInsets;
        rect.set(0, 0, 0, 0);
        int n = this.mItemDecorations.size();
        for (int i = 0; i < n; ++i) {
            this.mTempRect.set(0, 0, 0, 0);
            this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, view, this, this.mState);
            rect.left += this.mTempRect.left;
            rect.top += this.mTempRect.top;
            rect.right += this.mTempRect.right;
            rect.bottom += this.mTempRect.bottom;
        }
        layoutParams.mInsetsDirty = false;
        return rect;
    }

    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }

    public int getMaxFlingVelocity() {
        return this.mMaxFlingVelocity;
    }

    public int getMinFlingVelocity() {
        return this.mMinFlingVelocity;
    }

    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    public boolean hasFixedSize() {
        return this.mHasFixedSize;
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return this.mScrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean hasPendingAdapterUpdates() {
        if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates()) {
            return true;
        }
        return false;
    }

    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback(){

            void dispatchUpdate(AdapterHelper.UpdateOp updateOp) {
                switch (updateOp.cmd) {
                    default: {
                        return;
                    }
                    case 1: {
                        RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, updateOp.positionStart, updateOp.itemCount);
                        return;
                    }
                    case 2: {
                        RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, updateOp.positionStart, updateOp.itemCount);
                        return;
                    }
                    case 4: {
                        RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, updateOp.positionStart, updateOp.itemCount, updateOp.payload);
                        return;
                    }
                    case 8: 
                }
                RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, updateOp.positionStart, updateOp.itemCount, 1);
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public ViewHolder findViewHolder(int n) {
                ViewHolder viewHolder = RecyclerView.this.findViewHolderForPosition(n, true);
                if (viewHolder == null) {
                    return null;
                }
                ViewHolder viewHolder2 = viewHolder;
                if (!RecyclerView.this.mChildHelper.isHidden(viewHolder.itemView)) return viewHolder2;
                return null;
            }

            @Override
            public void markViewHoldersUpdated(int n, int n2, Object object) {
                RecyclerView.this.viewRangeUpdate(n, n2, object);
                RecyclerView.this.mItemsChanged = true;
            }

            @Override
            public void offsetPositionsForAdd(int n, int n2) {
                RecyclerView.this.offsetPositionRecordsForInsert(n, n2);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override
            public void offsetPositionsForMove(int n, int n2) {
                RecyclerView.this.offsetPositionRecordsForMove(n, n2);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override
            public void offsetPositionsForRemovingInvisible(int n, int n2) {
                RecyclerView.this.offsetPositionRecordsForRemove(n, n2, true);
                RecyclerView.this.mItemsAddedOrRemoved = true;
                State.access$1812(RecyclerView.this.mState, n2);
            }

            @Override
            public void offsetPositionsForRemovingLaidOutOrNewView(int n, int n2) {
                RecyclerView.this.offsetPositionRecordsForRemove(n, n2, false);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override
            public void onDispatchFirstPass(AdapterHelper.UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }

            @Override
            public void onDispatchSecondPass(AdapterHelper.UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }
        });
    }

    void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }

    public void invalidateItemDecorations() {
        if (this.mItemDecorations.size() == 0) {
            return;
        }
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }

    boolean isAccessibilityEnabled() {
        if (this.mAccessibilityManager != null && this.mAccessibilityManager.isEnabled()) {
            return true;
        }
        return false;
    }

    public boolean isAnimating() {
        if (this.mItemAnimator != null && this.mItemAnimator.isRunning()) {
            return true;
        }
        return false;
    }

    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }

    public boolean isComputingLayout() {
        if (this.mLayoutOrScrollCounter > 0) {
            return true;
        }
        return false;
    }

    public boolean isLayoutFrozen() {
        return this.mLayoutFrozen;
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return this.mScrollingChildHelper.isNestedScrollingEnabled();
    }

    void markItemDecorInsetsDirty() {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ((LayoutParams)this.mChildHelper.getUnfilteredChildAt((int)i).getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }

    void markKnownViewsInvalid() {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder == null || viewHolder.shouldIgnore()) continue;
            viewHolder.addFlags(6);
        }
        this.markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }

    public void offsetChildrenHorizontal(int n) {
        int n2 = this.mChildHelper.getChildCount();
        for (int i = 0; i < n2; ++i) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(n);
        }
    }

    public void offsetChildrenVertical(int n) {
        int n2 = this.mChildHelper.getChildCount();
        for (int i = 0; i < n2; ++i) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(n);
        }
    }

    void offsetPositionRecordsForInsert(int n, int n2) {
        int n3 = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n3; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder == null || viewHolder.shouldIgnore() || viewHolder.mPosition < n) continue;
            viewHolder.offsetPosition(n2, false);
            this.mState.mStructureChanged = true;
        }
        this.mRecycler.offsetPositionRecordsForInsert(n, n2);
        this.requestLayout();
    }

    /*
     * Enabled aggressive block sorting
     */
    void offsetPositionRecordsForMove(int n, int n2) {
        int n3;
        int n4;
        int n5;
        int n6 = this.mChildHelper.getUnfilteredChildCount();
        if (n < n2) {
            n3 = n;
            n5 = n2;
            n4 = -1;
        } else {
            n3 = n2;
            n5 = n;
            n4 = 1;
        }
        int n7 = 0;
        do {
            if (n7 >= n6) {
                this.mRecycler.offsetPositionRecordsForMove(n, n2);
                this.requestLayout();
                return;
            }
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(n7));
            if (viewHolder != null && viewHolder.mPosition >= n3 && viewHolder.mPosition <= n5) {
                if (viewHolder.mPosition == n) {
                    viewHolder.offsetPosition(n2 - n, false);
                } else {
                    viewHolder.offsetPosition(n4, false);
                }
                this.mState.mStructureChanged = true;
            }
            ++n7;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    void offsetPositionRecordsForRemove(int n, int n2, boolean bl) {
        int n3 = this.mChildHelper.getUnfilteredChildCount();
        int n4 = 0;
        do {
            if (n4 >= n3) {
                this.mRecycler.offsetPositionRecordsForRemove(n, n2, bl);
                this.requestLayout();
                return;
            }
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(n4));
            if (viewHolder != null && !viewHolder.shouldIgnore()) {
                if (viewHolder.mPosition >= n + n2) {
                    viewHolder.offsetPosition(- n2, bl);
                    this.mState.mStructureChanged = true;
                } else if (viewHolder.mPosition >= n) {
                    viewHolder.flagRemovedAndOffsetPosition(n - 1, - n2, bl);
                    this.mState.mStructureChanged = true;
                }
            }
            ++n4;
        } while (true);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        this.mIsAttached = true;
        this.mFirstLayoutComplete = false;
        if (this.mLayout != null) {
            this.mLayout.dispatchAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
    }

    public void onChildAttachedToWindow(View view) {
    }

    public void onChildDetachedFromWindow(View view) {
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
        }
        this.mFirstLayoutComplete = false;
        this.stopScroll();
        this.mIsAttached = false;
        if (this.mLayout != null) {
            this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
        }
        this.removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int n = this.mItemDecorations.size();
        for (int i = 0; i < n; ++i) {
            this.mItemDecorations.get(i).onDraw(canvas, this, this.mState);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if (this.mLayout == null) {
            return false;
        }
        if (this.mLayoutFrozen) return false;
        if ((MotionEventCompat.getSource(motionEvent) & 2) == 0) return false;
        if (motionEvent.getAction() != 8) return false;
        float f = this.mLayout.canScrollVertically() ? - MotionEventCompat.getAxisValue(motionEvent, 9) : 0.0f;
        if (!this.mLayout.canScrollHorizontally()) return false;
        float f2 = MotionEventCompat.getAxisValue(motionEvent, 10);
        if (f == 0.0f) {
            if (f2 == 0.0f) return false;
        }
        float f3 = this.getScrollFactor();
        this.scrollByInternal((int)(f2 * f3), (int)(f * f3), motionEvent);
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onInterceptTouchEvent(MotionEvent arrn) {
        if (this.mLayoutFrozen) {
            return false;
        }
        if (this.dispatchOnItemTouchIntercept((MotionEvent)arrn)) {
            this.cancelTouch();
            return true;
        }
        if (this.mLayout == null) {
            return false;
        }
        boolean bl = this.mLayout.canScrollHorizontally();
        boolean bl2 = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement((MotionEvent)arrn);
        int n = MotionEventCompat.getActionMasked((MotionEvent)arrn);
        int n2 = MotionEventCompat.getActionIndex((MotionEvent)arrn);
        switch (n) {
            case 0: {
                if (this.mIgnoreMotionEventTillDown) {
                    this.mIgnoreMotionEventTillDown = false;
                }
                this.mScrollPointerId = MotionEventCompat.getPointerId((MotionEvent)arrn, 0);
                this.mLastTouchX = n2 = (int)(arrn.getX() + 0.5f);
                this.mInitialTouchX = n2;
                this.mLastTouchY = n2 = (int)(arrn.getY() + 0.5f);
                this.mInitialTouchY = n2;
                if (this.mScrollState == 2) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    this.setScrollState(1);
                }
                arrn = this.mNestedOffsets;
                this.mNestedOffsets[1] = 0;
                arrn[0] = 0;
                n2 = 0;
                if (bl) {
                    n2 = false | true;
                }
                n = n2;
                if (bl2) {
                    n = n2 | 2;
                }
                this.startNestedScroll(n);
                break;
            }
            case 5: {
                this.mScrollPointerId = MotionEventCompat.getPointerId((MotionEvent)arrn, n2);
                this.mLastTouchX = n = (int)(MotionEventCompat.getX((MotionEvent)arrn, n2) + 0.5f);
                this.mInitialTouchX = n;
                this.mLastTouchY = n2 = (int)(MotionEventCompat.getY((MotionEvent)arrn, n2) + 0.5f);
                this.mInitialTouchY = n2;
                break;
            }
            case 2: {
                n = MotionEventCompat.findPointerIndex((MotionEvent)arrn, this.mScrollPointerId);
                if (n < 0) {
                    Log.e((String)"RecyclerView", (String)("Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?"));
                    return false;
                }
                n2 = (int)(MotionEventCompat.getX((MotionEvent)arrn, n) + 0.5f);
                n = (int)(MotionEventCompat.getY((MotionEvent)arrn, n) + 0.5f);
                if (this.mScrollState == 1) break;
                int n3 = n2 - this.mInitialTouchX;
                int n4 = n - this.mInitialTouchY;
                n2 = n = 0;
                if (bl) {
                    n2 = n;
                    if (Math.abs(n3) > this.mTouchSlop) {
                        n = this.mInitialTouchX;
                        int n5 = this.mTouchSlop;
                        n2 = n3 < 0 ? -1 : 1;
                        this.mLastTouchX = n2 * n5 + n;
                        n2 = 1;
                    }
                }
                n = n2;
                if (bl2) {
                    n = n2;
                    if (Math.abs(n4) > this.mTouchSlop) {
                        n = this.mInitialTouchY;
                        n3 = this.mTouchSlop;
                        n2 = n4 < 0 ? -1 : 1;
                        this.mLastTouchY = n2 * n3 + n;
                        n = 1;
                    }
                    if (n == 0) break;
                }
                this.setScrollState(1);
                break;
            }
            case 6: {
                this.onPointerUp((MotionEvent)arrn);
                break;
            }
            case 1: {
                this.mVelocityTracker.clear();
                this.stopNestedScroll();
                break;
            }
            case 3: {
                this.cancelTouch();
            }
        }
        if (this.mScrollState == 1) {
            return true;
        }
        return false;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        this.eatRequestLayout();
        TraceCompat.beginSection("RV OnLayout");
        this.dispatchLayout();
        TraceCompat.endSection();
        this.resumeRequestLayout(false);
        this.mFirstLayoutComplete = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        if (this.mAdapterUpdateDuringMeasure) {
            this.eatRequestLayout();
            this.processAdapterUpdatesAndSetAnimationFlags();
            if (this.mState.mRunPredictiveAnimations) {
                this.mState.mInPreLayout = true;
            } else {
                this.mAdapterHelper.consumeUpdatesInOnePass();
                this.mState.mInPreLayout = false;
            }
            this.mAdapterUpdateDuringMeasure = false;
            this.resumeRequestLayout(false);
        }
        this.mState.mItemCount = this.mAdapter != null ? this.mAdapter.getItemCount() : 0;
        if (this.mLayout == null) {
            this.defaultOnMeasure(n, n2);
        } else {
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
        }
        this.mState.mInPreLayout = false;
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        this.mPendingSavedState = (SavedState)parcelable;
        super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
        if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
        }
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.mPendingSavedState != null) {
            savedState.copyFrom(this.mPendingSavedState);
            return savedState;
        }
        if (this.mLayout != null) {
            savedState.mLayoutState = this.mLayout.onSaveInstanceState();
            return savedState;
        }
        savedState.mLayoutState = null;
        return savedState;
    }

    public void onScrollStateChanged(int n) {
    }

    public void onScrolled(int n, int n2) {
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3 || n2 != n4) {
            this.invalidateGlows();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1_1) {
        if (this.mLayoutFrozen != false) return false;
        if (this.mIgnoreMotionEventTillDown) {
            return false;
        }
        if (this.dispatchOnItemTouch((MotionEvent)var1_1)) {
            this.cancelTouch();
            return true;
        }
        if (this.mLayout == null) {
            return false;
        }
        var13_2 = this.mLayout.canScrollHorizontally();
        var14_3 = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        var10_4 = 0;
        var15_5 = MotionEvent.obtain((MotionEvent)var1_1);
        var6_6 = MotionEventCompat.getActionMasked((MotionEvent)var1_1);
        var5_7 = MotionEventCompat.getActionIndex((MotionEvent)var1_1);
        if (var6_6 == 0) {
            var16_8 = this.mNestedOffsets;
            this.mNestedOffsets[1] = 0;
            var16_8[0] = 0;
        }
        var15_5.offsetLocation((float)this.mNestedOffsets[0], (float)this.mNestedOffsets[1]);
        var4_9 = var10_4;
        switch (var6_6) {
            default: {
                var4_9 = var10_4;
                break;
            }
            case 0: {
                this.mScrollPointerId = MotionEventCompat.getPointerId((MotionEvent)var1_1, 0);
                this.mLastTouchX = var4_9 = (int)(var1_1.getX() + 0.5f);
                this.mInitialTouchX = var4_9;
                this.mLastTouchY = var4_9 = (int)(var1_1.getY() + 0.5f);
                this.mInitialTouchY = var4_9;
                var4_9 = 0;
                if (var13_2) {
                    var4_9 = false | true;
                }
                var5_7 = var4_9;
                if (var14_3) {
                    var5_7 = var4_9 | 2;
                }
                this.startNestedScroll(var5_7);
                var4_9 = var10_4;
                break;
            }
            case 5: {
                this.mScrollPointerId = MotionEventCompat.getPointerId((MotionEvent)var1_1, var5_7);
                this.mLastTouchX = var4_9 = (int)(MotionEventCompat.getX((MotionEvent)var1_1, var5_7) + 0.5f);
                this.mInitialTouchX = var4_9;
                this.mLastTouchY = var4_9 = (int)(MotionEventCompat.getY((MotionEvent)var1_1, var5_7) + 0.5f);
                this.mInitialTouchY = var4_9;
                var4_9 = var10_4;
                break;
            }
            case 2: {
                var4_9 = MotionEventCompat.findPointerIndex((MotionEvent)var1_1, this.mScrollPointerId);
                if (var4_9 < 0) {
                    Log.e((String)"RecyclerView", (String)("Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?"));
                    return false;
                }
                var11_10 = (int)(MotionEventCompat.getX((MotionEvent)var1_1, var4_9) + 0.5f);
                var12_11 = (int)(MotionEventCompat.getY((MotionEvent)var1_1, var4_9) + 0.5f);
                var7_12 = this.mLastTouchX - var11_10;
                var6_6 = this.mLastTouchY - var12_11;
                var5_7 = var7_12;
                var4_9 = var6_6;
                if (this.dispatchNestedPreScroll(var7_12, var6_6, this.mScrollConsumed, this.mScrollOffset)) {
                    var5_7 = var7_12 - this.mScrollConsumed[0];
                    var4_9 = var6_6 - this.mScrollConsumed[1];
                    var15_5.offsetLocation((float)this.mScrollOffset[0], (float)this.mScrollOffset[1]);
                    var1_1 = this.mNestedOffsets;
                    var1_1[0] = var1_1[0] + this.mScrollOffset[0];
                    var1_1 = this.mNestedOffsets;
                    var1_1[1] = var1_1[1] + this.mScrollOffset[1];
                }
                var8_13 = var5_7;
                var7_12 = var4_9;
                if (this.mScrollState != 1) {
                    var8_13 = 0;
                    var6_6 = var5_7;
                    var7_12 = var8_13;
                    if (var13_2) {
                        var6_6 = var5_7;
                        var7_12 = var8_13;
                        if (Math.abs(var5_7) > this.mTouchSlop) {
                            var6_6 = var5_7 > 0 ? var5_7 - this.mTouchSlop : var5_7 + this.mTouchSlop;
                            var7_12 = 1;
                        }
                    }
                    var5_7 = var4_9;
                    var9_14 = var7_12;
                    if (var14_3) {
                        var5_7 = var4_9;
                        var9_14 = var7_12;
                        if (Math.abs(var4_9) > this.mTouchSlop) {
                            var5_7 = var4_9 > 0 ? var4_9 - this.mTouchSlop : var4_9 + this.mTouchSlop;
                            var9_14 = 1;
                        }
                    }
                    var8_13 = var6_6;
                    var7_12 = var5_7;
                    if (var9_14 != 0) {
                        this.setScrollState(1);
                        var7_12 = var5_7;
                        var8_13 = var6_6;
                    }
                }
                var4_9 = var10_4;
                if (this.mScrollState == 1) {
                    this.mLastTouchX = var11_10 - this.mScrollOffset[0];
                    this.mLastTouchY = var12_11 - this.mScrollOffset[1];
                    if (!var13_2) {
                        var8_13 = 0;
                    }
                    if (!var14_3) {
                        var7_12 = 0;
                    }
                    var4_9 = var10_4;
                    if (this.scrollByInternal(var8_13, var7_12, var15_5)) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                        var4_9 = var10_4;
                        break;
                    }
                }
                ** GOTO lbl122
            }
            case 6: {
                this.onPointerUp((MotionEvent)var1_1);
                var4_9 = var10_4;
                break;
            }
            case 1: {
                this.mVelocityTracker.addMovement(var15_5);
                var4_9 = 1;
                this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaxFlingVelocity);
                var2_15 = var13_2 != false ? - VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mScrollPointerId) : 0.0f;
                var3_16 = var14_3 != false ? - VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mScrollPointerId) : 0.0f;
                if (var2_15 == 0.0f && var3_16 == 0.0f || !this.fling((int)var2_15, (int)var3_16)) {
                    this.setScrollState(0);
                }
                this.resetTouch();
            }
lbl122: // 3 sources:
            case 4: {
                break;
            }
            case 3: {
                this.cancelTouch();
                var4_9 = var10_4;
            }
        }
        if (var4_9 == 0) {
            this.mVelocityTracker.addMovement(var15_5);
        }
        var15_5.recycle();
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void removeDetachedView(View view, boolean bl) {
        ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
        if (viewHolder != null) {
            if (viewHolder.isTmpDetached()) {
                viewHolder.clearTmpDetachFlag();
            } else if (!viewHolder.shouldIgnore()) {
                throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + viewHolder);
            }
        }
        this.dispatchChildDetached(view);
        super.removeDetachedView(view, bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void removeItemDecoration(ItemDecoration itemDecoration) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(itemDecoration);
        if (this.mItemDecorations.isEmpty()) {
            boolean bl = ViewCompat.getOverScrollMode((View)this) == 2;
            this.setWillNotDraw(bl);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }

    public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        if (this.mOnChildAttachStateListeners == null) {
            return;
        }
        this.mOnChildAttachStateListeners.remove(onChildAttachStateChangeListener);
    }

    public void removeOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.mOnItemTouchListeners.remove(onItemTouchListener);
        if (this.mActiveOnItemTouchListener == onItemTouchListener) {
            this.mActiveOnItemTouchListener = null;
        }
    }

    public void removeOnScrollListener(OnScrollListener onScrollListener) {
        if (this.mScrollListeners != null) {
            this.mScrollListeners.remove(onScrollListener);
        }
    }

    public void requestChildFocus(View view, View view2) {
        boolean bl = false;
        if (!this.mLayout.onRequestChildFocus(this, this.mState, view, view2) && view2 != null) {
            this.mTempRect.set(0, 0, view2.getWidth(), view2.getHeight());
            Object object = view2.getLayoutParams();
            if (object instanceof LayoutParams) {
                object = (LayoutParams)((Object)object);
                if (!object.mInsetsDirty) {
                    object = object.mDecorInsets;
                    Rect rect = this.mTempRect;
                    rect.left -= object.left;
                    rect = this.mTempRect;
                    rect.right += object.right;
                    rect = this.mTempRect;
                    rect.top -= object.top;
                    rect = this.mTempRect;
                    rect.bottom += object.bottom;
                }
            }
            this.offsetDescendantRectToMyCoords(view2, this.mTempRect);
            this.offsetRectIntoDescendantCoords(view, this.mTempRect);
            object = this.mTempRect;
            if (!this.mFirstLayoutComplete) {
                bl = true;
            }
            this.requestChildRectangleOnScreen(view, (Rect)object, bl);
        }
        super.requestChildFocus(view, view2);
    }

    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean bl) {
        return this.mLayout.requestChildRectangleOnScreen(this, view, rect, bl);
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        int n = this.mOnItemTouchListeners.size();
        for (int i = 0; i < n; ++i) {
            this.mOnItemTouchListeners.get(i).onRequestDisallowInterceptTouchEvent(bl);
        }
        super.requestDisallowInterceptTouchEvent(bl);
    }

    public void requestLayout() {
        if (!this.mEatRequestLayout && !this.mLayoutFrozen) {
            super.requestLayout();
            return;
        }
        this.mLayoutRequestEaten = true;
    }

    void resumeRequestLayout(boolean bl) {
        if (this.mEatRequestLayout) {
            if (bl && this.mLayoutRequestEaten && !this.mLayoutFrozen && this.mLayout != null && this.mAdapter != null) {
                this.dispatchLayout();
            }
            this.mEatRequestLayout = false;
            if (!this.mLayoutFrozen) {
                this.mLayoutRequestEaten = false;
            }
        }
    }

    void saveOldPositions() {
        int n = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n; ++i) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder.shouldIgnore()) continue;
            viewHolder.saveOldPosition();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void scrollBy(int n, int n2) {
        if (this.mLayout == null) {
            Log.e((String)"RecyclerView", (String)"Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutFrozen) return;
        boolean bl = this.mLayout.canScrollHorizontally();
        boolean bl2 = this.mLayout.canScrollVertically();
        if (!bl && !bl2) return;
        if (bl) {
        } else {
            n = 0;
        }
        if (!bl2) {
            n2 = 0;
        }
        this.scrollByInternal(n, n2, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean scrollByInternal(int n, int n2, MotionEvent arrn) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        this.consumePendingUpdateOperations();
        if (this.mAdapter != null) {
            this.eatRequestLayout();
            this.onEnterLayoutOrScroll();
            TraceCompat.beginSection("RV Scroll");
            n7 = n8;
            n3 = n4;
            if (n != 0) {
                n7 = this.mLayout.scrollHorizontallyBy(n, this.mRecycler, this.mState);
                n3 = n - n7;
            }
            n9 = n10;
            n5 = n6;
            if (n2 != 0) {
                n9 = this.mLayout.scrollVerticallyBy(n2, this.mRecycler, this.mState);
                n5 = n2 - n9;
            }
            TraceCompat.endSection();
            this.repositionShadowingViews();
            this.onExitLayoutOrScroll();
            this.resumeRequestLayout(false);
        }
        if (!this.mItemDecorations.isEmpty()) {
            this.invalidate();
        }
        if (this.dispatchNestedScroll(n7, n9, n3, n5, this.mScrollOffset)) {
            this.mLastTouchX -= this.mScrollOffset[0];
            this.mLastTouchY -= this.mScrollOffset[1];
            if (arrn != null) {
                arrn.offsetLocation((float)this.mScrollOffset[0], (float)this.mScrollOffset[1]);
            }
            arrn = this.mNestedOffsets;
            arrn[0] = arrn[0] + this.mScrollOffset[0];
            arrn = this.mNestedOffsets;
            arrn[1] = arrn[1] + this.mScrollOffset[1];
        } else if (ViewCompat.getOverScrollMode((View)this) != 2) {
            if (arrn != null) {
                this.pullGlows(arrn.getX(), n3, arrn.getY(), n5);
            }
            this.considerReleasingGlowsOnScroll(n, n2);
        }
        if (n7 != 0 || n9 != 0) {
            this.dispatchOnScrolled(n7, n9);
        }
        if (!this.awakenScrollBars()) {
            this.invalidate();
        }
        if (n7 == 0 && n9 == 0) {
            return false;
        }
        return true;
    }

    public void scrollTo(int n, int n2) {
        Log.w((String)"RecyclerView", (String)"RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }

    public void scrollToPosition(int n) {
        if (this.mLayoutFrozen) {
            return;
        }
        this.stopScroll();
        if (this.mLayout == null) {
            Log.e((String)"RecyclerView", (String)"Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        this.mLayout.scrollToPosition(n);
        this.awakenScrollBars();
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent accessibilityEvent) {
        if (this.shouldDeferAccessibilityEvent(accessibilityEvent)) {
            return;
        }
        super.sendAccessibilityEventUnchecked(accessibilityEvent);
    }

    public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate) {
        this.mAccessibilityDelegate = recyclerViewAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate((View)this, this.mAccessibilityDelegate);
    }

    public void setAdapter(Adapter adapter) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, false, true);
        this.requestLayout();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback childDrawingOrderCallback) {
        if (childDrawingOrderCallback == this.mChildDrawingOrderCallback) {
            return;
        }
        this.mChildDrawingOrderCallback = childDrawingOrderCallback;
        boolean bl = this.mChildDrawingOrderCallback != null;
        this.setChildrenDrawingOrderEnabled(bl);
    }

    public void setClipToPadding(boolean bl) {
        if (bl != this.mClipToPadding) {
            this.invalidateGlows();
        }
        this.mClipToPadding = bl;
        super.setClipToPadding(bl);
        if (this.mFirstLayoutComplete) {
            this.requestLayout();
        }
    }

    public void setHasFixedSize(boolean bl) {
        this.mHasFixedSize = bl;
    }

    public void setItemAnimator(ItemAnimator itemAnimator) {
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
            this.mItemAnimator.setListener(null);
        }
        this.mItemAnimator = itemAnimator;
        if (this.mItemAnimator != null) {
            this.mItemAnimator.setListener(this.mItemAnimatorListener);
        }
    }

    public void setItemViewCacheSize(int n) {
        this.mRecycler.setViewCacheSize(n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setLayoutFrozen(boolean bl) {
        if (bl == this.mLayoutFrozen) return;
        this.assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
        if (!bl) {
            this.mLayoutFrozen = bl;
            if (this.mLayoutRequestEaten && this.mLayout != null && this.mAdapter != null) {
                this.requestLayout();
            }
            this.mLayoutRequestEaten = false;
            return;
        }
        long l = SystemClock.uptimeMillis();
        this.onTouchEvent(MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0));
        this.mLayoutFrozen = bl;
        this.mIgnoreMotionEventTillDown = true;
        this.stopScroll();
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager == this.mLayout) {
            return;
        }
        if (this.mLayout != null) {
            if (this.mIsAttached) {
                this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
            }
            this.mLayout.setRecyclerView(null);
        }
        this.mRecycler.clear();
        this.mChildHelper.removeAllViewsUnfiltered();
        this.mLayout = layoutManager;
        if (layoutManager != null) {
            if (layoutManager.mRecyclerView != null) {
                throw new IllegalArgumentException("LayoutManager " + layoutManager + " is already attached to a RecyclerView: " + layoutManager.mRecyclerView);
            }
            this.mLayout.setRecyclerView(this);
            if (this.mIsAttached) {
                this.mLayout.dispatchAttachedToWindow(this);
            }
        }
        this.requestLayout();
    }

    @Override
    public void setNestedScrollingEnabled(boolean bl) {
        this.mScrollingChildHelper.setNestedScrollingEnabled(bl);
    }

    @Deprecated
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mScrollListener = onScrollListener;
    }

    public void setRecycledViewPool(RecycledViewPool recycledViewPool) {
        this.mRecycler.setRecycledViewPool(recycledViewPool);
    }

    public void setRecyclerListener(RecyclerListener recyclerListener) {
        this.mRecyclerListener = recyclerListener;
    }

    public void setScrollingTouchSlop(int n) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get((Context)this.getContext());
        switch (n) {
            default: {
                Log.w((String)"RecyclerView", (String)("setScrollingTouchSlop(): bad argument constant " + n + "; using default value"));
            }
            case 0: {
                this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
                return;
            }
            case 1: 
        }
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
    }

    public void setViewCacheExtension(ViewCacheExtension viewCacheExtension) {
        this.mRecycler.setViewCacheExtension(viewCacheExtension);
    }

    boolean shouldDeferAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (this.isComputingLayout()) {
            int n = 0;
            if (accessibilityEvent != null) {
                n = AccessibilityEventCompat.getContentChangeTypes(accessibilityEvent);
            }
            int n2 = n;
            if (n == 0) {
                n2 = 0;
            }
            this.mEatenAccessibilityChangeFlags |= n2;
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void smoothScrollBy(int n, int n2) {
        block4 : {
            if (this.mLayout == null) {
                Log.e((String)"RecyclerView", (String)"Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
                return;
            }
            if (this.mLayoutFrozen) return;
            if (!this.mLayout.canScrollHorizontally()) {
                n = 0;
            }
            if (!this.mLayout.canScrollVertically()) {
                n2 = 0;
                if (n != 0) break block4;
            }
            if (n2 == 0) return;
        }
        this.mViewFlinger.smoothScrollBy(n, n2);
    }

    public void smoothScrollToPosition(int n) {
        if (this.mLayoutFrozen) {
            return;
        }
        if (this.mLayout == null) {
            Log.e((String)"RecyclerView", (String)"Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        this.mLayout.smoothScrollToPosition(this, this.mState, n);
    }

    @Override
    public boolean startNestedScroll(int n) {
        return this.mScrollingChildHelper.startNestedScroll(n);
    }

    @Override
    public void stopNestedScroll() {
        this.mScrollingChildHelper.stopNestedScroll();
    }

    public void stopScroll() {
        this.setScrollState(0);
        this.stopScrollersInternal();
    }

    public void swapAdapter(Adapter adapter, boolean bl) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, true, bl);
        this.setDataSetChangedAfterLayout();
        this.requestLayout();
    }

    /*
     * Enabled aggressive block sorting
     */
    void viewRangeUpdate(int n, int n2, Object object) {
        int n3 = this.mChildHelper.getUnfilteredChildCount();
        int n4 = 0;
        do {
            if (n4 >= n3) {
                this.mRecycler.viewRangeUpdate(n, n2);
                return;
            }
            View view = this.mChildHelper.getUnfilteredChildAt(n4);
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.mPosition >= n && viewHolder.mPosition < n + n2) {
                viewHolder.addFlags(2);
                viewHolder.addChangePayload(object);
                ((LayoutParams)view.getLayoutParams()).mInsetsDirty = true;
            }
            ++n4;
        } while (true);
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private boolean mHasStableIds = false;
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        public final void bindViewHolder(VH VH, int n) {
            VH.mPosition = n;
            if (this.hasStableIds()) {
                VH.mItemId = this.getItemId(n);
            }
            VH.setFlags(1, 519);
            TraceCompat.beginSection("RV OnBindView");
            this.onBindViewHolder(VH, n, VH.getUnmodifiedPayloads());
            VH.clearPayload();
            TraceCompat.endSection();
        }

        public final VH createViewHolder(ViewGroup object, int n) {
            TraceCompat.beginSection("RV CreateView");
            object = this.onCreateViewHolder((ViewGroup)object, n);
            object.mItemViewType = n;
            TraceCompat.endSection();
            return (VH)object;
        }

        public abstract int getItemCount();

        public long getItemId(int n) {
            return -1;
        }

        public int getItemViewType(int n) {
            return 0;
        }

        public final boolean hasObservers() {
            return this.mObservable.hasObservers();
        }

        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int n) {
            this.mObservable.notifyItemRangeChanged(n, 1);
        }

        public final void notifyItemChanged(int n, Object object) {
            this.mObservable.notifyItemRangeChanged(n, 1, object);
        }

        public final void notifyItemInserted(int n) {
            this.mObservable.notifyItemRangeInserted(n, 1);
        }

        public final void notifyItemMoved(int n, int n2) {
            this.mObservable.notifyItemMoved(n, n2);
        }

        public final void notifyItemRangeChanged(int n, int n2) {
            this.mObservable.notifyItemRangeChanged(n, n2);
        }

        public final void notifyItemRangeChanged(int n, int n2, Object object) {
            this.mObservable.notifyItemRangeChanged(n, n2, object);
        }

        public final void notifyItemRangeInserted(int n, int n2) {
            this.mObservable.notifyItemRangeInserted(n, n2);
        }

        public final void notifyItemRangeRemoved(int n, int n2) {
            this.mObservable.notifyItemRangeRemoved(n, n2);
        }

        public final void notifyItemRemoved(int n) {
            this.mObservable.notifyItemRangeRemoved(n, 1);
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        }

        public abstract void onBindViewHolder(VH var1, int var2);

        public void onBindViewHolder(VH VH, int n, List<Object> list) {
            this.onBindViewHolder(VH, n);
        }

        public abstract VH onCreateViewHolder(ViewGroup var1, int var2);

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        }

        public boolean onFailedToRecycleView(VH VH) {
            return false;
        }

        public void onViewAttachedToWindow(VH VH) {
        }

        public void onViewDetachedFromWindow(VH VH) {
        }

        public void onViewRecycled(VH VH) {
        }

        public void registerAdapterDataObserver(AdapterDataObserver adapterDataObserver) {
            this.mObservable.registerObserver((Object)adapterDataObserver);
        }

        public void setHasStableIds(boolean bl) {
            if (this.hasObservers()) {
                throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
            }
            this.mHasStableIds = bl;
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver adapterDataObserver) {
            this.mObservable.unregisterObserver((Object)adapterDataObserver);
        }
    }

    static class AdapterDataObservable
    extends Observable<AdapterDataObserver> {
        AdapterDataObservable() {
        }

        public boolean hasObservers() {
            if (!this.mObservers.isEmpty()) {
                return true;
            }
            return false;
        }

        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onChanged();
            }
        }

        public void notifyItemMoved(int n, int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(n, n2, 1);
            }
        }

        public void notifyItemRangeChanged(int n, int n2) {
            this.notifyItemRangeChanged(n, n2, null);
        }

        public void notifyItemRangeChanged(int n, int n2, Object object) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(n, n2, object);
            }
        }

        public void notifyItemRangeInserted(int n, int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(n, n2);
            }
        }

        public void notifyItemRangeRemoved(int n, int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(n, n2);
            }
        }
    }

    public static abstract class AdapterDataObserver {
        public void onChanged() {
        }

        public void onItemRangeChanged(int n, int n2) {
        }

        public void onItemRangeChanged(int n, int n2, Object object) {
            this.onItemRangeChanged(n, n2);
        }

        public void onItemRangeInserted(int n, int n2) {
        }

        public void onItemRangeMoved(int n, int n2, int n3) {
        }

        public void onItemRangeRemoved(int n, int n2) {
        }
    }

    public static interface ChildDrawingOrderCallback {
        public int onGetChildDrawingOrder(int var1, int var2);
    }

    public static abstract class ItemAnimator {
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration = 120;
        private long mChangeDuration = 250;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList();
        private ItemAnimatorListener mListener = null;
        private long mMoveDuration = 250;
        private long mRemoveDuration = 120;

        static int buildAdapterChangeFlagsForAnimations(ViewHolder viewHolder) {
            int n = viewHolder.mFlags & 14;
            if (viewHolder.isInvalid()) {
                return 4;
            }
            int n2 = n;
            if ((n & 4) == 0) {
                int n3 = viewHolder.getOldPosition();
                int n4 = viewHolder.getAdapterPosition();
                n2 = n;
                if (n3 != -1) {
                    n2 = n;
                    if (n4 != -1) {
                        n2 = n;
                        if (n3 != n4) {
                            n2 = n | 2048;
                        }
                    }
                }
            }
            return n2;
        }

        public abstract boolean animateAppearance(@NonNull ViewHolder var1, @Nullable ItemHolderInfo var2, @NonNull ItemHolderInfo var3);

        public abstract boolean animateChange(@NonNull ViewHolder var1, @NonNull ViewHolder var2, @NonNull ItemHolderInfo var3, @NonNull ItemHolderInfo var4);

        public abstract boolean animateDisappearance(@NonNull ViewHolder var1, @NonNull ItemHolderInfo var2, @Nullable ItemHolderInfo var3);

        public abstract boolean animatePersistence(@NonNull ViewHolder var1, @NonNull ItemHolderInfo var2, @NonNull ItemHolderInfo var3);

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
            return true;
        }

        public final void dispatchAnimationFinished(ViewHolder viewHolder) {
            this.onAnimationFinished(viewHolder);
            if (this.mListener != null) {
                this.mListener.onAnimationFinished(viewHolder);
            }
        }

        public final void dispatchAnimationStarted(ViewHolder viewHolder) {
            this.onAnimationStarted(viewHolder);
        }

        public final void dispatchAnimationsFinished() {
            int n = this.mFinishedListeners.size();
            for (int i = 0; i < n; ++i) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }

        public abstract void endAnimation(ViewHolder var1);

        public abstract void endAnimations();

        public long getAddDuration() {
            return this.mAddDuration;
        }

        public long getChangeDuration() {
            return this.mChangeDuration;
        }

        public long getMoveDuration() {
            return this.mMoveDuration;
        }

        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }

        public abstract boolean isRunning();

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public final boolean isRunning(ItemAnimatorFinishedListener itemAnimatorFinishedListener) {
            boolean bl = this.isRunning();
            if (itemAnimatorFinishedListener == null) return bl;
            if (!bl) {
                itemAnimatorFinishedListener.onAnimationsFinished();
                return bl;
            }
            this.mFinishedListeners.add(itemAnimatorFinishedListener);
            return bl;
        }

        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }

        public void onAnimationFinished(ViewHolder viewHolder) {
        }

        public void onAnimationStarted(ViewHolder viewHolder) {
        }

        @NonNull
        public ItemHolderInfo recordPostLayoutInformation(@NonNull State state, @NonNull ViewHolder viewHolder) {
            return this.obtainHolderInfo().setFrom(viewHolder);
        }

        @NonNull
        public ItemHolderInfo recordPreLayoutInformation(@NonNull State state, @NonNull ViewHolder viewHolder, int n, @NonNull List<Object> list) {
            return this.obtainHolderInfo().setFrom(viewHolder);
        }

        public abstract void runPendingAnimations();

        public void setAddDuration(long l) {
            this.mAddDuration = l;
        }

        public void setChangeDuration(long l) {
            this.mChangeDuration = l;
        }

        void setListener(ItemAnimatorListener itemAnimatorListener) {
            this.mListener = itemAnimatorListener;
        }

        public void setMoveDuration(long l) {
            this.mMoveDuration = l;
        }

        public void setRemoveDuration(long l) {
            this.mRemoveDuration = l;
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface AdapterChanges {
        }

        public static interface ItemAnimatorFinishedListener {
            public void onAnimationsFinished();
        }

        static interface ItemAnimatorListener {
            public void onAnimationFinished(ViewHolder var1);
        }

        public static class ItemHolderInfo {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;

            public ItemHolderInfo setFrom(ViewHolder viewHolder) {
                return this.setFrom(viewHolder, 0);
            }

            public ItemHolderInfo setFrom(ViewHolder viewHolder, int n) {
                viewHolder = viewHolder.itemView;
                this.left = viewHolder.getLeft();
                this.top = viewHolder.getTop();
                this.right = viewHolder.getRight();
                this.bottom = viewHolder.getBottom();
                return this;
            }
        }

    }

    private class ItemAnimatorRestoreListener
    implements ItemAnimator.ItemAnimatorListener {
        private ItemAnimatorRestoreListener() {
        }

        @Override
        public void onAnimationFinished(ViewHolder viewHolder) {
            viewHolder.setIsRecyclable(true);
            if (viewHolder.mShadowedHolder != null && viewHolder.mShadowingHolder == null) {
                viewHolder.mShadowedHolder = null;
            }
            viewHolder.mShadowingHolder = null;
            if (!viewHolder.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(viewHolder.itemView) && viewHolder.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
            }
        }
    }

    public static abstract class ItemDecoration {
        @Deprecated
        public void getItemOffsets(Rect rect, int n, RecyclerView recyclerView) {
            rect.set(0, 0, 0, 0);
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            this.getItemOffsets(rect, ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition(), recyclerView);
        }

        @Deprecated
        public void onDraw(Canvas canvas, RecyclerView recyclerView) {
        }

        public void onDraw(Canvas canvas, RecyclerView recyclerView, State state) {
            this.onDraw(canvas, recyclerView);
        }

        @Deprecated
        public void onDrawOver(Canvas canvas, RecyclerView recyclerView) {
        }

        public void onDrawOver(Canvas canvas, RecyclerView recyclerView, State state) {
            this.onDrawOver(canvas, recyclerView);
        }
    }

    public static abstract class LayoutManager {
        ChildHelper mChildHelper;
        private boolean mIsAttachedToWindow = false;
        RecyclerView mRecyclerView;
        private boolean mRequestedSimpleAnimations = false;
        @Nullable
        SmoothScroller mSmoothScroller;

        /*
         * Enabled aggressive block sorting
         */
        private void addViewInt(View view, int n, boolean bl) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (bl || viewHolder.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
            }
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (viewHolder.wasReturnedFromScrap() || viewHolder.isScrap()) {
                if (viewHolder.isScrap()) {
                    viewHolder.unScrap();
                } else {
                    viewHolder.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(view, n, view.getLayoutParams(), false);
            } else if (view.getParent() == this.mRecyclerView) {
                int n2 = this.mChildHelper.indexOfChild(view);
                int n3 = n;
                if (n == -1) {
                    n3 = this.mChildHelper.getChildCount();
                }
                if (n2 == -1) {
                    throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(view));
                }
                if (n2 != n3) {
                    this.mRecyclerView.mLayout.moveView(n2, n3);
                }
            } else {
                this.mChildHelper.addView(view, n, false);
                layoutParams.mInsetsDirty = true;
                if (this.mSmoothScroller != null && this.mSmoothScroller.isRunning()) {
                    this.mSmoothScroller.onChildAttachedToWindow(view);
                }
            }
            if (layoutParams.mPendingInvalidate) {
                viewHolder.itemView.invalidate();
                layoutParams.mPendingInvalidate = false;
            }
        }

        private void detachViewInternal(int n, View view) {
            this.mChildHelper.detachViewFromParent(n);
        }

        /*
         * Enabled aggressive block sorting
         */
        public static int getChildMeasureSpec(int n, int n2, int n3, boolean bl) {
            int n4 = Math.max(0, n - n2);
            n2 = 0;
            n = 0;
            if (bl) {
                if (n3 >= 0) {
                    n2 = n3;
                    n = 1073741824;
                    return View.MeasureSpec.makeMeasureSpec((int)n2, (int)n);
                }
                n2 = 0;
                n = 0;
                return View.MeasureSpec.makeMeasureSpec((int)n2, (int)n);
            }
            if (n3 >= 0) {
                n2 = n3;
                n = 1073741824;
                return View.MeasureSpec.makeMeasureSpec((int)n2, (int)n);
            }
            if (n3 == -1) {
                n2 = n4;
                n = 1073741824;
                return View.MeasureSpec.makeMeasureSpec((int)n2, (int)n);
            }
            if (n3 != -2) return View.MeasureSpec.makeMeasureSpec((int)n2, (int)n);
            n2 = n4;
            n = Integer.MIN_VALUE;
            return View.MeasureSpec.makeMeasureSpec((int)n2, (int)n);
        }

        public static Properties getProperties(Context context, AttributeSet attributeSet, int n, int n2) {
            Properties properties = new Properties();
            context = context.obtainStyledAttributes(attributeSet, R.styleable.RecyclerView, n, n2);
            properties.orientation = context.getInt(R.styleable.RecyclerView_android_orientation, 1);
            properties.spanCount = context.getInt(R.styleable.RecyclerView_spanCount, 1);
            properties.reverseLayout = context.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
            properties.stackFromEnd = context.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
            context.recycle();
            return properties;
        }

        private void onSmoothScrollerStopped(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
            }
        }

        private void scrapOrRecycleView(Recycler recycler, int n, View view) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder.shouldIgnore()) {
                return;
            }
            if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
                this.removeViewAt(n);
                recycler.recycleViewHolderInternal(viewHolder);
                return;
            }
            this.detachViewAt(n);
            recycler.scrapView(view);
        }

        public void addDisappearingView(View view) {
            this.addDisappearingView(view, -1);
        }

        public void addDisappearingView(View view, int n) {
            this.addViewInt(view, n, true);
        }

        public void addView(View view) {
            this.addView(view, -1);
        }

        public void addView(View view, int n) {
            this.addViewInt(view, n, false);
        }

        public void assertInLayoutOrScroll(String string2) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertInLayoutOrScroll(string2);
            }
        }

        public void assertNotInLayoutOrScroll(String string2) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertNotInLayoutOrScroll(string2);
            }
        }

        public void attachView(View view) {
            this.attachView(view, -1);
        }

        public void attachView(View view, int n) {
            this.attachView(view, n, (LayoutParams)view.getLayoutParams());
        }

        /*
         * Enabled aggressive block sorting
         */
        public void attachView(View view, int n, LayoutParams layoutParams) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
            }
            this.mChildHelper.attachViewToParent(view, n, (ViewGroup.LayoutParams)layoutParams, viewHolder.isRemoved());
        }

        public void calculateItemDecorationsForChild(View view, Rect rect) {
            if (this.mRecyclerView == null) {
                rect.set(0, 0, 0, 0);
                return;
            }
            rect.set(this.mRecyclerView.getItemDecorInsetsForChild(view));
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public boolean checkLayoutParams(LayoutParams layoutParams) {
            if (layoutParams != null) {
                return true;
            }
            return false;
        }

        public int computeHorizontalScrollExtent(State state) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State state) {
            return 0;
        }

        public int computeHorizontalScrollRange(State state) {
            return 0;
        }

        public int computeVerticalScrollExtent(State state) {
            return 0;
        }

        public int computeVerticalScrollOffset(State state) {
            return 0;
        }

        public int computeVerticalScrollRange(State state) {
            return 0;
        }

        public void detachAndScrapAttachedViews(Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.scrapOrRecycleView(recycler, i, this.getChildAt(i));
            }
        }

        public void detachAndScrapView(View view, Recycler recycler) {
            this.scrapOrRecycleView(recycler, this.mChildHelper.indexOfChild(view), view);
        }

        public void detachAndScrapViewAt(int n, Recycler recycler) {
            this.scrapOrRecycleView(recycler, n, this.getChildAt(n));
        }

        public void detachView(View view) {
            int n = this.mChildHelper.indexOfChild(view);
            if (n >= 0) {
                this.detachViewInternal(n, view);
            }
        }

        public void detachViewAt(int n) {
            this.detachViewInternal(n, this.getChildAt(n));
        }

        void dispatchAttachedToWindow(RecyclerView recyclerView) {
            this.mIsAttachedToWindow = true;
            this.onAttachedToWindow(recyclerView);
        }

        void dispatchDetachedFromWindow(RecyclerView recyclerView, Recycler recycler) {
            this.mIsAttachedToWindow = false;
            this.onDetachedFromWindow(recyclerView, recycler);
        }

        public void endAnimation(View view) {
            if (this.mRecyclerView.mItemAnimator != null) {
                this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(view));
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public View findViewByPosition(int n) {
            int n2 = this.getChildCount();
            int n3 = 0;
            while (n3 < n2) {
                View view = this.getChildAt(n3);
                ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
                if (!(viewHolder == null || viewHolder.getLayoutPosition() != n || viewHolder.shouldIgnore() || !this.mRecyclerView.mState.isPreLayout() && viewHolder.isRemoved())) {
                    return view;
                }
                ++n3;
            }
            return null;
        }

        public abstract LayoutParams generateDefaultLayoutParams();

        public LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
            return new LayoutParams(context, attributeSet);
        }

        public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
            if (layoutParams instanceof LayoutParams) {
                return new LayoutParams((LayoutParams)layoutParams);
            }
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
            }
            return new LayoutParams(layoutParams);
        }

        public int getBaseline() {
            return -1;
        }

        public int getBottomDecorationHeight(View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.bottom;
        }

        public View getChildAt(int n) {
            if (this.mChildHelper != null) {
                return this.mChildHelper.getChildAt(n);
            }
            return null;
        }

        public int getChildCount() {
            if (this.mChildHelper != null) {
                return this.mChildHelper.getChildCount();
            }
            return 0;
        }

        public boolean getClipToPadding() {
            if (this.mRecyclerView != null && this.mRecyclerView.mClipToPadding) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        public int getColumnCountForAccessibility(Recycler recycler, State state) {
            if (this.mRecyclerView == null || this.mRecyclerView.mAdapter == null || !this.canScrollHorizontally()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public int getDecoratedBottom(View view) {
            return view.getBottom() + this.getBottomDecorationHeight(view);
        }

        public int getDecoratedLeft(View view) {
            return view.getLeft() - this.getLeftDecorationWidth(view);
        }

        public int getDecoratedMeasuredHeight(View view) {
            Rect rect = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredHeight() + rect.top + rect.bottom;
        }

        public int getDecoratedMeasuredWidth(View view) {
            Rect rect = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredWidth() + rect.left + rect.right;
        }

        public int getDecoratedRight(View view) {
            return view.getRight() + this.getRightDecorationWidth(view);
        }

        public int getDecoratedTop(View view) {
            return view.getTop() - this.getTopDecorationHeight(view);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public View getFocusedChild() {
            if (this.mRecyclerView == null) {
                return null;
            }
            View view = this.mRecyclerView.getFocusedChild();
            if (view == null) return null;
            View view2 = view;
            if (!this.mChildHelper.isHidden(view)) return view2;
            return null;
        }

        public int getHeight() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getHeight();
            }
            return 0;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public int getItemCount() {
            if (this.mRecyclerView == null) return 0;
            Adapter adapter = this.mRecyclerView.getAdapter();
            if (adapter == null) return 0;
            return adapter.getItemCount();
        }

        public int getItemViewType(View view) {
            return RecyclerView.getChildViewHolderInt(view).getItemViewType();
        }

        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection((View)this.mRecyclerView);
        }

        public int getLeftDecorationWidth(View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.left;
        }

        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight((View)this.mRecyclerView);
        }

        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth((View)this.mRecyclerView);
        }

        public int getPaddingBottom() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingBottom();
            }
            return 0;
        }

        public int getPaddingEnd() {
            if (this.mRecyclerView != null) {
                return ViewCompat.getPaddingEnd((View)this.mRecyclerView);
            }
            return 0;
        }

        public int getPaddingLeft() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingLeft();
            }
            return 0;
        }

        public int getPaddingRight() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingRight();
            }
            return 0;
        }

        public int getPaddingStart() {
            if (this.mRecyclerView != null) {
                return ViewCompat.getPaddingStart((View)this.mRecyclerView);
            }
            return 0;
        }

        public int getPaddingTop() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingTop();
            }
            return 0;
        }

        public int getPosition(View view) {
            return ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        }

        public int getRightDecorationWidth(View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.right;
        }

        /*
         * Enabled aggressive block sorting
         */
        public int getRowCountForAccessibility(Recycler recycler, State state) {
            if (this.mRecyclerView == null || this.mRecyclerView.mAdapter == null || !this.canScrollVertically()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public int getSelectionModeForAccessibility(Recycler recycler, State state) {
            return 0;
        }

        public int getTopDecorationHeight(View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.top;
        }

        public int getWidth() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getWidth();
            }
            return 0;
        }

        public boolean hasFocus() {
            if (this.mRecyclerView != null && this.mRecyclerView.hasFocus()) {
                return true;
            }
            return false;
        }

        public void ignoreView(View object) {
            if (object.getParent() != this.mRecyclerView || this.mRecyclerView.indexOfChild((View)object) == -1) {
                throw new IllegalArgumentException("View should be fully attached to be ignored");
            }
            object = RecyclerView.getChildViewHolderInt((View)object);
            object.addFlags(128);
            this.mRecyclerView.mViewInfoStore.removeViewHolder((ViewHolder)object);
        }

        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }

        public boolean isFocused() {
            if (this.mRecyclerView != null && this.mRecyclerView.isFocused()) {
                return true;
            }
            return false;
        }

        public boolean isLayoutHierarchical(Recycler recycler, State state) {
            return false;
        }

        public boolean isSmoothScrolling() {
            if (this.mSmoothScroller != null && this.mSmoothScroller.isRunning()) {
                return true;
            }
            return false;
        }

        public void layoutDecorated(View view, int n, int n2, int n3, int n4) {
            Rect rect = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            view.layout(rect.left + n, rect.top + n2, n3 - rect.right, n4 - rect.bottom);
        }

        public void measureChild(View view, int n, int n2) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(view);
            int n3 = rect.left;
            int n4 = rect.right;
            int n5 = rect.top;
            int n6 = rect.bottom;
            view.measure(LayoutManager.getChildMeasureSpec(this.getWidth(), this.getPaddingLeft() + this.getPaddingRight() + (n + (n3 + n4)), layoutParams.width, this.canScrollHorizontally()), LayoutManager.getChildMeasureSpec(this.getHeight(), this.getPaddingTop() + this.getPaddingBottom() + (n2 + (n5 + n6)), layoutParams.height, this.canScrollVertically()));
        }

        public void measureChildWithMargins(View view, int n, int n2) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(view);
            int n3 = rect.left;
            int n4 = rect.right;
            int n5 = rect.top;
            int n6 = rect.bottom;
            view.measure(LayoutManager.getChildMeasureSpec(this.getWidth(), this.getPaddingLeft() + this.getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + (n + (n3 + n4)), layoutParams.width, this.canScrollHorizontally()), LayoutManager.getChildMeasureSpec(this.getHeight(), this.getPaddingTop() + this.getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + (n2 + (n5 + n6)), layoutParams.height, this.canScrollVertically()));
        }

        public void moveView(int n, int n2) {
            View view = this.getChildAt(n);
            if (view == null) {
                throw new IllegalArgumentException("Cannot move a child from non-existing index:" + n);
            }
            this.detachViewAt(n);
            this.attachView(view, n2);
        }

        public void offsetChildrenHorizontal(int n) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenHorizontal(n);
            }
        }

        public void offsetChildrenVertical(int n) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenVertical(n);
            }
        }

        public void onAdapterChanged(Adapter adapter, Adapter adapter2) {
        }

        public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> arrayList, int n, int n2) {
            return false;
        }

        @CallSuper
        public void onAttachedToWindow(RecyclerView recyclerView) {
        }

        @Deprecated
        public void onDetachedFromWindow(RecyclerView recyclerView) {
        }

        @CallSuper
        public void onDetachedFromWindow(RecyclerView recyclerView, Recycler recycler) {
            this.onDetachedFromWindow(recyclerView);
        }

        @Nullable
        public View onFocusSearchFailed(View view, int n, Recycler recycler, State state) {
            return null;
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void onInitializeAccessibilityEvent(Recycler object, State state, AccessibilityEvent accessibilityEvent) {
            boolean bl = true;
            object = AccessibilityEventCompat.asRecord(accessibilityEvent);
            if (this.mRecyclerView == null) return;
            if (object == null) {
                return;
            }
            boolean bl2 = bl;
            if (!ViewCompat.canScrollVertically((View)this.mRecyclerView, 1)) {
                bl2 = bl;
                if (!ViewCompat.canScrollVertically((View)this.mRecyclerView, -1)) {
                    bl2 = bl;
                    if (!ViewCompat.canScrollHorizontally((View)this.mRecyclerView, -1)) {
                        bl2 = ViewCompat.canScrollHorizontally((View)this.mRecyclerView, 1) ? bl : false;
                    }
                }
            }
            object.setScrollable(bl2);
            if (this.mRecyclerView.mAdapter == null) return;
            object.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            this.onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityEvent);
        }

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            this.onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityNodeInfoCompat);
        }

        public void onInitializeAccessibilityNodeInfo(Recycler recycler, State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (ViewCompat.canScrollVertically((View)this.mRecyclerView, -1) || ViewCompat.canScrollHorizontally((View)this.mRecyclerView, -1)) {
                accessibilityNodeInfoCompat.addAction(8192);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            if (ViewCompat.canScrollVertically((View)this.mRecyclerView, 1) || ViewCompat.canScrollHorizontally((View)this.mRecyclerView, 1)) {
                accessibilityNodeInfoCompat.addAction(4096);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(this.getRowCountForAccessibility(recycler, state), this.getColumnCountForAccessibility(recycler, state), this.isLayoutHierarchical(recycler, state), this.getSelectionModeForAccessibility(recycler, state)));
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            int n = this.canScrollVertically() ? this.getPosition(view) : 0;
            int n2 = this.canScrollHorizontally() ? this.getPosition(view) : 0;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n, 1, n2, 1, false, false));
        }

        void onInitializeAccessibilityNodeInfoForItem(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder != null && !viewHolder.isRemoved() && !this.mChildHelper.isHidden(viewHolder.itemView)) {
                this.onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, accessibilityNodeInfoCompat);
            }
        }

        public View onInterceptFocusSearch(View view, int n) {
            return null;
        }

        public void onItemsAdded(RecyclerView recyclerView, int n, int n2) {
        }

        public void onItemsChanged(RecyclerView recyclerView) {
        }

        public void onItemsMoved(RecyclerView recyclerView, int n, int n2, int n3) {
        }

        public void onItemsRemoved(RecyclerView recyclerView, int n, int n2) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int n, int n2) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int n, int n2, Object object) {
            this.onItemsUpdated(recyclerView, n, n2);
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            Log.e((String)"RecyclerView", (String)"You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public void onMeasure(Recycler recycler, State state, int n, int n2) {
            this.mRecyclerView.defaultOnMeasure(n, n2);
        }

        public boolean onRequestChildFocus(RecyclerView recyclerView, State state, View view, View view2) {
            return this.onRequestChildFocus(recyclerView, view, view2);
        }

        @Deprecated
        public boolean onRequestChildFocus(RecyclerView recyclerView, View view, View view2) {
            if (this.isSmoothScrolling() || recyclerView.isComputingLayout()) {
                return true;
            }
            return false;
        }

        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onScrollStateChanged(int n) {
        }

        boolean performAccessibilityAction(int n, Bundle bundle) {
            return this.performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, n, bundle);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public boolean performAccessibilityAction(Recycler recycler, State state, int n, Bundle bundle) {
            if (this.mRecyclerView == null) {
                return false;
            }
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            switch (n) {
                default: {
                    n = n4;
                    break;
                }
                case 8192: {
                    n4 = n2;
                    if (ViewCompat.canScrollVertically((View)this.mRecyclerView, -1)) {
                        n4 = - this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                    }
                    n = n4;
                    if (!ViewCompat.canScrollHorizontally((View)this.mRecyclerView, -1)) break;
                    n5 = - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                    n = n4;
                    break;
                }
                case 4096: {
                    n4 = n3;
                    if (ViewCompat.canScrollVertically((View)this.mRecyclerView, 1)) {
                        n4 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                    }
                    n = n4;
                    if (!ViewCompat.canScrollHorizontally((View)this.mRecyclerView, 1)) break;
                    n5 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                    n = n4;
                }
            }
            if (n == 0) {
                if (n5 == 0) return false;
            }
            this.mRecyclerView.scrollBy(n5, n);
            return true;
        }

        public boolean performAccessibilityActionForItem(Recycler recycler, State state, View view, int n, Bundle bundle) {
            return false;
        }

        boolean performAccessibilityActionForItem(View view, int n, Bundle bundle) {
            return this.performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, n, bundle);
        }

        public void postOnAnimation(Runnable runnable) {
            if (this.mRecyclerView != null) {
                ViewCompat.postOnAnimation((View)this.mRecyclerView, runnable);
            }
        }

        public void removeAllViews() {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.mChildHelper.removeViewAt(i);
            }
        }

        public void removeAndRecycleAllViews(Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                if (RecyclerView.getChildViewHolderInt(this.getChildAt(i)).shouldIgnore()) continue;
                this.removeAndRecycleViewAt(i, recycler);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        void removeAndRecycleScrapInt(Recycler recycler) {
            int n = recycler.getScrapCount();
            for (int i = n - 1; i >= 0; --i) {
                View view = recycler.getScrapViewAt(i);
                ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
                if (viewHolder.shouldIgnore()) continue;
                viewHolder.setIsRecyclable(false);
                if (viewHolder.isTmpDetached()) {
                    this.mRecyclerView.removeDetachedView(view, false);
                }
                if (this.mRecyclerView.mItemAnimator != null) {
                    this.mRecyclerView.mItemAnimator.endAnimation(viewHolder);
                }
                viewHolder.setIsRecyclable(true);
                recycler.quickRecycleScrapView(view);
            }
            recycler.clearScrap();
            if (n > 0) {
                this.mRecyclerView.invalidate();
            }
        }

        public void removeAndRecycleView(View view, Recycler recycler) {
            this.removeView(view);
            recycler.recycleView(view);
        }

        public void removeAndRecycleViewAt(int n, Recycler recycler) {
            View view = this.getChildAt(n);
            this.removeViewAt(n);
            recycler.recycleView(view);
        }

        public boolean removeCallbacks(Runnable runnable) {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.removeCallbacks(runnable);
            }
            return false;
        }

        public void removeDetachedView(View view) {
            this.mRecyclerView.removeDetachedView(view, false);
        }

        public void removeView(View view) {
            this.mChildHelper.removeView(view);
        }

        public void removeViewAt(int n) {
            if (this.getChildAt(n) != null) {
                this.mChildHelper.removeViewAt(n);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean requestChildRectangleOnScreen(RecyclerView recyclerView, View view, Rect rect, boolean bl) {
            int n = this.getPaddingLeft();
            int n2 = this.getPaddingTop();
            int n3 = this.getWidth() - this.getPaddingRight();
            int n4 = this.getHeight();
            int n5 = this.getPaddingBottom();
            int n6 = view.getLeft() + rect.left;
            int n7 = view.getTop() + rect.top;
            int n8 = n6 + rect.width();
            int n9 = rect.height();
            int n10 = Math.min(0, n6 - n);
            int n11 = Math.min(0, n7 - n2);
            int n12 = Math.max(0, n8 - n3);
            n4 = Math.max(0, n7 + n9 - (n4 - n5));
            if (this.getLayoutDirection() == 1) {
                n10 = n12 != 0 ? n12 : Math.max(n10, n8 - n3);
            } else if (n10 == 0) {
                n10 = Math.min(n6 - n, n12);
            }
            if (n11 == 0) {
                n11 = Math.min(n7 - n2, n4);
            }
            if (n10 == 0) {
                if (n11 == 0) return false;
            }
            if (bl) {
                recyclerView.scrollBy(n10, n11);
                return true;
            }
            recyclerView.smoothScrollBy(n10, n11);
            return true;
        }

        public void requestLayout() {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.requestLayout();
            }
        }

        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }

        public int scrollHorizontallyBy(int n, Recycler recycler, State state) {
            return 0;
        }

        public void scrollToPosition(int n) {
        }

        public int scrollVerticallyBy(int n, Recycler recycler, State state) {
            return 0;
        }

        public void setMeasuredDimension(int n, int n2) {
            this.mRecyclerView.setMeasuredDimension(n, n2);
        }

        void setRecyclerView(RecyclerView recyclerView) {
            if (recyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                return;
            }
            this.mRecyclerView = recyclerView;
            this.mChildHelper = recyclerView.mChildHelper;
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int n) {
            Log.e((String)"RecyclerView", (String)"You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller != null && smoothScroller != this.mSmoothScroller && this.mSmoothScroller.isRunning()) {
                this.mSmoothScroller.stop();
            }
            this.mSmoothScroller = smoothScroller;
            this.mSmoothScroller.start(this.mRecyclerView, this);
        }

        public void stopIgnoringView(View object) {
            object = RecyclerView.getChildViewHolderInt((View)object);
            object.stopIgnoring();
            object.resetInternal();
            object.addFlags(4);
        }

        void stopSmoothScroller() {
            if (this.mSmoothScroller != null) {
                this.mSmoothScroller.stop();
            }
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public static class Properties {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }

    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        final Rect mDecorInsets = new Rect();
        boolean mInsetsDirty = true;
        boolean mPendingInvalidate = false;
        ViewHolder mViewHolder;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.LayoutParams)layoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public int getViewAdapterPosition() {
            return this.mViewHolder.getAdapterPosition();
        }

        public int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }

        public int getViewPosition() {
            return this.mViewHolder.getPosition();
        }

        public boolean isItemChanged() {
            return this.mViewHolder.isUpdated();
        }

        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }

        public boolean isViewInvalid() {
            return this.mViewHolder.isInvalid();
        }

        public boolean viewNeedsUpdate() {
            return this.mViewHolder.needsUpdate();
        }
    }

    public static interface OnChildAttachStateChangeListener {
        public void onChildViewAttachedToWindow(View var1);

        public void onChildViewDetachedFromWindow(View var1);
    }

    public static interface OnItemTouchListener {
        public boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2);

        public void onRequestDisallowInterceptTouchEvent(boolean var1);

        public void onTouchEvent(RecyclerView var1, MotionEvent var2);
    }

    public static abstract class OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int n) {
        }

        public void onScrolled(RecyclerView recyclerView, int n, int n2) {
        }
    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private int mAttachCount = 0;
        private SparseIntArray mMaxScrap = new SparseIntArray();
        private SparseArray<ArrayList<ViewHolder>> mScrap = new SparseArray();

        private ArrayList<ViewHolder> getScrapHeapForType(int n) {
            ArrayList arrayList;
            ArrayList arrayList2 = arrayList = (ArrayList)this.mScrap.get(n);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.mScrap.put(n, arrayList);
                arrayList2 = arrayList;
                if (this.mMaxScrap.indexOfKey(n) < 0) {
                    this.mMaxScrap.put(n, 5);
                    arrayList2 = arrayList;
                }
            }
            return arrayList2;
        }

        void attach(Adapter adapter) {
            ++this.mAttachCount;
        }

        public void clear() {
            this.mScrap.clear();
        }

        void detach() {
            --this.mAttachCount;
        }

        public ViewHolder getRecycledView(int n) {
            ArrayList arrayList = (ArrayList)this.mScrap.get(n);
            if (arrayList != null && !arrayList.isEmpty()) {
                n = arrayList.size() - 1;
                ViewHolder viewHolder = (ViewHolder)arrayList.get(n);
                arrayList.remove(n);
                return viewHolder;
            }
            return null;
        }

        void onAdapterChanged(Adapter adapter, Adapter adapter2, boolean bl) {
            if (adapter != null) {
                this.detach();
            }
            if (!bl && this.mAttachCount == 0) {
                this.clear();
            }
            if (adapter2 != null) {
                this.attach(adapter2);
            }
        }

        public void putRecycledView(ViewHolder viewHolder) {
            int n = viewHolder.getItemViewType();
            ArrayList<ViewHolder> arrayList = this.getScrapHeapForType(n);
            if (this.mMaxScrap.get(n) <= arrayList.size()) {
                return;
            }
            viewHolder.resetInternal();
            arrayList.add(viewHolder);
        }

        public void setMaxRecycledViews(int n, int n2) {
            this.mMaxScrap.put(n, n2);
            ArrayList arrayList = (ArrayList)this.mScrap.get(n);
            if (arrayList != null) {
                while (arrayList.size() > n2) {
                    arrayList.remove(arrayList.size() - 1);
                }
            }
        }

        int size() {
            int n = 0;
            for (int i = 0; i < this.mScrap.size(); ++i) {
                ArrayList arrayList = (ArrayList)this.mScrap.valueAt(i);
                int n2 = n;
                if (arrayList != null) {
                    n2 = n + arrayList.size();
                }
                n = n2;
            }
            return n;
        }
    }

    public final class Recycler {
        private static final int DEFAULT_CACHE_SIZE = 2;
        final ArrayList<ViewHolder> mAttachedScrap;
        final ArrayList<ViewHolder> mCachedViews;
        private ArrayList<ViewHolder> mChangedScrap;
        private RecycledViewPool mRecyclerPool;
        private final List<ViewHolder> mUnmodifiableAttachedScrap;
        private ViewCacheExtension mViewCacheExtension;
        private int mViewCacheMax;

        public Recycler() {
            this.mAttachedScrap = new ArrayList();
            this.mChangedScrap = null;
            this.mCachedViews = new ArrayList();
            this.mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
            this.mViewCacheMax = 2;
        }

        private void attachAccessibilityDelegate(View view) {
            if (RecyclerView.this.isAccessibilityEnabled()) {
                if (ViewCompat.getImportantForAccessibility(view) == 0) {
                    ViewCompat.setImportantForAccessibility(view, 1);
                }
                if (!ViewCompat.hasAccessibilityDelegate(view)) {
                    ViewCompat.setAccessibilityDelegate(view, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
                }
            }
        }

        private void invalidateDisplayListInt(ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                this.invalidateDisplayListInt((ViewGroup)viewHolder.itemView, false);
            }
        }

        private void invalidateDisplayListInt(ViewGroup viewGroup, boolean bl) {
            int n;
            for (n = viewGroup.getChildCount() - 1; n >= 0; --n) {
                View view = viewGroup.getChildAt(n);
                if (!(view instanceof ViewGroup)) continue;
                this.invalidateDisplayListInt((ViewGroup)view, true);
            }
            if (!bl) {
                return;
            }
            if (viewGroup.getVisibility() == 4) {
                viewGroup.setVisibility(0);
                viewGroup.setVisibility(4);
                return;
            }
            n = viewGroup.getVisibility();
            viewGroup.setVisibility(4);
            viewGroup.setVisibility(n);
        }

        void addViewHolderToRecycledViewPool(ViewHolder viewHolder) {
            ViewCompat.setAccessibilityDelegate(viewHolder.itemView, null);
            this.dispatchViewRecycled(viewHolder);
            viewHolder.mOwnerRecyclerView = null;
            this.getRecycledViewPool().putRecycledView(viewHolder);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void bindViewToPosition(View object, int n) {
            boolean bl = true;
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt((View)object);
            if (viewHolder == null) {
                throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
            }
            int n2 = RecyclerView.this.mAdapterHelper.findPositionOffset(n);
            if (n2 < 0 || n2 >= RecyclerView.this.mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + n + "(offset:" + n2 + ")." + "state:" + RecyclerView.this.mState.getItemCount());
            }
            viewHolder.mOwnerRecyclerView = RecyclerView.this;
            RecyclerView.this.mAdapter.bindViewHolder(viewHolder, n2);
            this.attachAccessibilityDelegate((View)object);
            if (RecyclerView.this.mState.isPreLayout()) {
                viewHolder.mPreLayoutPosition = n;
            }
            if ((object = viewHolder.itemView.getLayoutParams()) == null) {
                object = (LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)object);
            } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)object)) {
                object = (LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)object);
                viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)object);
            } else {
                object = (LayoutParams)((Object)object);
            }
            object.mInsetsDirty = true;
            object.mViewHolder = viewHolder;
            if (viewHolder.itemView.getParent() != null) {
                bl = false;
            }
            object.mPendingInvalidate = bl;
        }

        public void clear() {
            this.mAttachedScrap.clear();
            this.recycleAndClearCachedViews();
        }

        void clearOldPositions() {
            int n;
            int n2 = this.mCachedViews.size();
            for (n = 0; n < n2; ++n) {
                this.mCachedViews.get(n).clearOldPosition();
            }
            n2 = this.mAttachedScrap.size();
            for (n = 0; n < n2; ++n) {
                this.mAttachedScrap.get(n).clearOldPosition();
            }
            if (this.mChangedScrap != null) {
                n2 = this.mChangedScrap.size();
                for (n = 0; n < n2; ++n) {
                    this.mChangedScrap.get(n).clearOldPosition();
                }
            }
        }

        void clearScrap() {
            this.mAttachedScrap.clear();
            if (this.mChangedScrap != null) {
                this.mChangedScrap.clear();
            }
        }

        public int convertPreLayoutPositionToPostLayout(int n) {
            if (n < 0 || n >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("invalid position " + n + ". State " + "item count is " + RecyclerView.this.mState.getItemCount());
            }
            if (!RecyclerView.this.mState.isPreLayout()) {
                return n;
            }
            return RecyclerView.this.mAdapterHelper.findPositionOffset(n);
        }

        void dispatchViewRecycled(ViewHolder viewHolder) {
            if (RecyclerView.this.mRecyclerListener != null) {
                RecyclerView.this.mRecyclerListener.onViewRecycled(viewHolder);
            }
            if (RecyclerView.this.mAdapter != null) {
                RecyclerView.this.mAdapter.onViewRecycled(viewHolder);
            }
            if (RecyclerView.this.mState != null) {
                RecyclerView.this.mViewInfoStore.removeViewHolder(viewHolder);
            }
        }

        ViewHolder getChangedScrapViewForPosition(int n) {
            ViewHolder viewHolder;
            int n2;
            if (this.mChangedScrap == null || (n2 = this.mChangedScrap.size()) == 0) {
                return null;
            }
            for (int i = 0; i < n2; ++i) {
                viewHolder = this.mChangedScrap.get(i);
                if (viewHolder.wasReturnedFromScrap() || viewHolder.getLayoutPosition() != n) continue;
                viewHolder.addFlags(32);
                return viewHolder;
            }
            if (RecyclerView.this.mAdapter.hasStableIds() && (n = RecyclerView.this.mAdapterHelper.findPositionOffset(n)) > 0 && n < RecyclerView.this.mAdapter.getItemCount()) {
                long l = RecyclerView.this.mAdapter.getItemId(n);
                for (n = 0; n < n2; ++n) {
                    viewHolder = this.mChangedScrap.get(n);
                    if (viewHolder.wasReturnedFromScrap() || viewHolder.getItemId() != l) continue;
                    viewHolder.addFlags(32);
                    return viewHolder;
                }
            }
            return null;
        }

        RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            return this.mRecyclerPool;
        }

        int getScrapCount() {
            return this.mAttachedScrap.size();
        }

        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }

        View getScrapViewAt(int n) {
            return this.mAttachedScrap.get((int)n).itemView;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        ViewHolder getScrapViewForId(long l, int n, boolean bl) {
            int n2;
            ViewHolder viewHolder;
            ViewHolder viewHolder2;
            for (n2 = this.mAttachedScrap.size() - 1; n2 >= 0; --n2) {
                viewHolder = this.mAttachedScrap.get(n2);
                if (viewHolder.getItemId() != l || viewHolder.wasReturnedFromScrap()) continue;
                if (n == viewHolder.getItemViewType()) {
                    viewHolder.addFlags(32);
                    viewHolder2 = viewHolder;
                    if (!viewHolder.isRemoved()) return viewHolder2;
                    viewHolder2 = viewHolder;
                    if (RecyclerView.this.mState.isPreLayout()) return viewHolder2;
                    viewHolder.setFlags(2, 14);
                    return viewHolder;
                }
                if (bl) continue;
                this.mAttachedScrap.remove(n2);
                RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
                this.quickRecycleScrapView(viewHolder.itemView);
            }
            n2 = this.mCachedViews.size() - 1;
            while (n2 >= 0) {
                viewHolder = this.mCachedViews.get(n2);
                if (viewHolder.getItemId() == l) {
                    if (n == viewHolder.getItemViewType()) {
                        viewHolder2 = viewHolder;
                        if (bl) return viewHolder2;
                        this.mCachedViews.remove(n2);
                        return viewHolder;
                    }
                    if (!bl) {
                        this.recycleCachedViewAt(n2);
                    }
                }
                --n2;
            }
            return null;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        ViewHolder getScrapViewForPosition(int var1_1, int var2_2, boolean var3_3) {
            block6 : {
                var5_4 = this.mAttachedScrap.size();
                var4_5 = 0;
                do {
                    if (var4_5 >= var5_4) ** GOTO lbl9
                    var6_6 = this.mAttachedScrap.get(var4_5);
                    if (!(var6_6.wasReturnedFromScrap() || var6_6.getLayoutPosition() != var1_1 || var6_6.isInvalid() || !State.access$2200(RecyclerView.this.mState) && var6_6.isRemoved())) {
                        if (var2_2 != -1 && var6_6.getItemViewType() != var2_2) {
                            Log.e((String)"RecyclerView", (String)("Scrap view for position " + var1_1 + " isn't dirty but has" + " wrong view type! (found " + var6_6.getItemViewType() + " but expected " + var2_2 + ")"));
lbl9: // 2 sources:
                            if (!var3_3 && (var6_6 = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(var1_1, var2_2)) != null) {
                                var7_7 = RecyclerView.getChildViewHolderInt((View)var6_6);
                                RecyclerView.this.mChildHelper.unhide((View)var6_6);
                                var1_1 = RecyclerView.this.mChildHelper.indexOfChild((View)var6_6);
                                if (var1_1 != -1) break;
                                throw new IllegalStateException("layout index should not be -1 after unhiding a view:" + var7_7);
                            }
                            break block6;
                        }
                        var6_6.addFlags(32);
                        return var6_6;
                    }
                    ++var4_5;
                } while (true);
                RecyclerView.this.mChildHelper.detachViewFromParent(var1_1);
                this.scrapView((View)var6_6);
                var7_7.addFlags(8224);
                return var7_7;
            }
            var4_5 = this.mCachedViews.size();
            var2_2 = 0;
            while (var2_2 < var4_5) {
                var7_8 = this.mCachedViews.get(var2_2);
                if (!var7_8.isInvalid() && var7_8.getLayoutPosition() == var1_1) {
                    var6_6 = var7_8;
                    if (var3_3 != false) return var6_6;
                    this.mCachedViews.remove(var2_2);
                    return var7_8;
                }
                ++var2_2;
            }
            return null;
        }

        public View getViewForPosition(int n) {
            return this.getViewForPosition(n, false);
        }

        /*
         * Enabled aggressive block sorting
         */
        View getViewForPosition(int n, boolean bl) {
            if (n < 0 || n >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("Invalid item position " + n + "(" + n + "). Item count:" + RecyclerView.this.mState.getItemCount());
            }
            int n2 = 0;
            Object object = null;
            if (RecyclerView.this.mState.isPreLayout()) {
                object = this.getChangedScrapViewForPosition(n);
                n2 = object != null ? 1 : 0;
            }
            int n3 = n2;
            Object object2 = object;
            if (object == null) {
                object = this.getScrapViewForPosition(n, -1, bl);
                n3 = n2;
                object2 = object;
                if (object != null) {
                    if (!this.validateViewHolderForOffsetPosition((ViewHolder)object)) {
                        if (!bl) {
                            object.addFlags(4);
                            if (object.isScrap()) {
                                RecyclerView.this.removeDetachedView(object.itemView, false);
                                object.unScrap();
                            } else if (object.wasReturnedFromScrap()) {
                                object.clearReturnedFromScrapFlag();
                            }
                            this.recycleViewHolderInternal((ViewHolder)object);
                        }
                        object2 = null;
                        n3 = n2;
                    } else {
                        n3 = 1;
                        object2 = object;
                    }
                }
            }
            int n4 = n3;
            object = object2;
            if (object2 == null) {
                Object object3;
                n4 = RecyclerView.this.mAdapterHelper.findPositionOffset(n);
                if (n4 < 0 || n4 >= RecyclerView.this.mAdapter.getItemCount()) {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + n + "(offset:" + n4 + ")." + "state:" + RecyclerView.this.mState.getItemCount());
                }
                int n5 = RecyclerView.this.mAdapter.getItemViewType(n4);
                n2 = n3;
                object = object2;
                if (RecyclerView.this.mAdapter.hasStableIds()) {
                    object2 = this.getScrapViewForId(RecyclerView.this.mAdapter.getItemId(n4), n5, bl);
                    n2 = n3;
                    object = object2;
                    if (object2 != null) {
                        object2.mPosition = n4;
                        n2 = 1;
                        object = object2;
                    }
                }
                object2 = object;
                if (object == null) {
                    object2 = object;
                    if (this.mViewCacheExtension != null) {
                        object3 = this.mViewCacheExtension.getViewForPositionAndType(this, n, n5);
                        object2 = object;
                        if (object3 != null) {
                            object = RecyclerView.this.getChildViewHolder((View)object3);
                            if (object == null) {
                                throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder");
                            }
                            object2 = object;
                            if (object.shouldIgnore()) {
                                throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                            }
                        }
                    }
                }
                object3 = object2;
                if (object2 == null) {
                    object3 = object2 = this.getRecycledViewPool().getRecycledView(n5);
                    if (object2 != null) {
                        object2.resetInternal();
                        object3 = object2;
                        if (FORCE_INVALIDATE_DISPLAY_LIST) {
                            this.invalidateDisplayListInt((ViewHolder)object2);
                            object3 = object2;
                        }
                    }
                }
                n4 = n2;
                object = object3;
                if (object3 == null) {
                    object = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, n5);
                    n4 = n2;
                }
            }
            if (n4 != 0 && !RecyclerView.this.mState.isPreLayout() && object.hasAnyOfTheFlags(8192)) {
                object.setFlags(0, 8192);
                if (RecyclerView.this.mState.mRunSimpleAnimations) {
                    n3 = ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)object);
                    object2 = RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, (ViewHolder)object, n3 | 4096, object.getUnmodifiedPayloads());
                    RecyclerView.this.recordAnimationInfoIfBouncedHiddenView((ViewHolder)object, (ItemAnimator.ItemHolderInfo)object2);
                }
            }
            n3 = 0;
            if (RecyclerView.this.mState.isPreLayout() && object.isBound()) {
                object.mPreLayoutPosition = n;
            } else if (!object.isBound() || object.needsUpdate() || object.isInvalid()) {
                n3 = RecyclerView.this.mAdapterHelper.findPositionOffset(n);
                object.mOwnerRecyclerView = RecyclerView.this;
                RecyclerView.this.mAdapter.bindViewHolder(object, n3);
                this.attachAccessibilityDelegate(object.itemView);
                n3 = n2 = 1;
                if (RecyclerView.this.mState.isPreLayout()) {
                    object.mPreLayoutPosition = n;
                    n3 = n2;
                }
            }
            if ((object2 = object.itemView.getLayoutParams()) == null) {
                object2 = (LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                object.itemView.setLayoutParams((ViewGroup.LayoutParams)object2);
            } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)object2)) {
                object2 = (LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)object2);
                object.itemView.setLayoutParams((ViewGroup.LayoutParams)object2);
            } else {
                object2 = (LayoutParams)((Object)object2);
            }
            object2.mViewHolder = object;
            bl = n4 != 0 && n3 != 0;
            object2.mPendingInvalidate = bl;
            return object.itemView;
        }

        void markItemDecorInsetsDirty() {
            int n = this.mCachedViews.size();
            for (int i = 0; i < n; ++i) {
                LayoutParams layoutParams = (LayoutParams)this.mCachedViews.get((int)i).itemView.getLayoutParams();
                if (layoutParams == null) continue;
                layoutParams.mInsetsDirty = true;
            }
        }

        void markKnownViewsInvalid() {
            if (RecyclerView.this.mAdapter != null && RecyclerView.this.mAdapter.hasStableIds()) {
                int n = this.mCachedViews.size();
                for (int i = 0; i < n; ++i) {
                    ViewHolder viewHolder = this.mCachedViews.get(i);
                    if (viewHolder == null) continue;
                    viewHolder.addFlags(6);
                    viewHolder.addChangePayload(null);
                }
            } else {
                this.recycleAndClearCachedViews();
            }
        }

        void offsetPositionRecordsForInsert(int n, int n2) {
            int n3 = this.mCachedViews.size();
            for (int i = 0; i < n3; ++i) {
                ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder == null || viewHolder.getLayoutPosition() < n) continue;
                viewHolder.offsetPosition(n2, true);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        void offsetPositionRecordsForMove(int n, int n2) {
            int n3;
            int n4;
            int n5;
            if (n < n2) {
                n4 = n;
                n3 = n2;
                n5 = -1;
            } else {
                n4 = n2;
                n3 = n;
                n5 = 1;
            }
            int n6 = this.mCachedViews.size();
            int n7 = 0;
            while (n7 < n6) {
                ViewHolder viewHolder = this.mCachedViews.get(n7);
                if (viewHolder != null && viewHolder.mPosition >= n4 && viewHolder.mPosition <= n3) {
                    if (viewHolder.mPosition == n) {
                        viewHolder.offsetPosition(n2 - n, false);
                    } else {
                        viewHolder.offsetPosition(n5, false);
                    }
                }
                ++n7;
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        void offsetPositionRecordsForRemove(int n, int n2, boolean bl) {
            int n3 = this.mCachedViews.size() - 1;
            while (n3 >= 0) {
                ViewHolder viewHolder = this.mCachedViews.get(n3);
                if (viewHolder != null) {
                    if (viewHolder.getLayoutPosition() >= n + n2) {
                        viewHolder.offsetPosition(- n2, bl);
                    } else if (viewHolder.getLayoutPosition() >= n) {
                        viewHolder.addFlags(8);
                        this.recycleCachedViewAt(n3);
                    }
                }
                --n3;
            }
            return;
        }

        void onAdapterChanged(Adapter adapter, Adapter adapter2, boolean bl) {
            this.clear();
            this.getRecycledViewPool().onAdapterChanged(adapter, adapter2, bl);
        }

        void quickRecycleScrapView(View object) {
            object = RecyclerView.getChildViewHolderInt((View)object);
            ((ViewHolder)object).mScrapContainer = null;
            ((ViewHolder)object).mInChangeScrap = false;
            object.clearReturnedFromScrapFlag();
            this.recycleViewHolderInternal((ViewHolder)object);
        }

        void recycleAndClearCachedViews() {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                this.recycleCachedViewAt(i);
            }
            this.mCachedViews.clear();
        }

        void recycleCachedViewAt(int n) {
            this.addViewHolderToRecycledViewPool(this.mCachedViews.get(n));
            this.mCachedViews.remove(n);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void recycleView(View view) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (viewHolder.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (viewHolder.isScrap()) {
                viewHolder.unScrap();
            } else if (viewHolder.wasReturnedFromScrap()) {
                viewHolder.clearReturnedFromScrapFlag();
            }
            this.recycleViewHolderInternal(viewHolder);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        void recycleViewHolderInternal(ViewHolder var1_1) {
            var7_2 = true;
            if (var1_1.isScrap() || var1_1.itemView.getParent() != null) {
                var8_3 = new StringBuilder().append("Scrapped or attached views may not be recycled. isScrap:").append(var1_1.isScrap()).append(" isAttached:");
                if (var1_1.itemView.getParent() != null) {
                    throw new IllegalArgumentException(var8_3.append(var7_2).toString());
                }
                var7_2 = false;
                throw new IllegalArgumentException(var8_3.append(var7_2).toString());
            }
            if (var1_1.isTmpDetached()) {
                throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + var1_1);
            }
            if (var1_1.shouldIgnore()) {
                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
            }
            var7_2 = ViewHolder.access$4700(var1_1);
            var2_4 = RecyclerView.access$2900(RecyclerView.this) != null && var7_2 != false && RecyclerView.access$2900(RecyclerView.this).onFailedToRecycleView(var1_1) != false ? 1 : 0;
            var3_5 = 0;
            var6_6 = 0;
            var5_7 = false;
            if (var2_4 != 0) ** GOTO lbl-1000
            var4_8 = var5_7;
            if (var1_1.isRecyclable()) lbl-1000: // 2 sources:
            {
                var2_4 = var6_6;
                if (!var1_1.hasAnyOfTheFlags(14)) {
                    var3_5 = this.mCachedViews.size();
                    if (var3_5 == this.mViewCacheMax && var3_5 > 0) {
                        this.recycleCachedViewAt(0);
                    }
                    var2_4 = var6_6;
                    if (var3_5 < this.mViewCacheMax) {
                        this.mCachedViews.add(var1_1);
                        var2_4 = 1;
                    }
                }
                var3_5 = var2_4;
                var4_8 = var5_7;
                if (var2_4 == 0) {
                    this.addViewHolderToRecycledViewPool(var1_1);
                    var4_8 = true;
                    var3_5 = var2_4;
                }
            }
            RecyclerView.this.mViewInfoStore.removeViewHolder(var1_1);
            if (var3_5 != 0) return;
            if (var4_8 != false) return;
            if (var7_2 == false) return;
            var1_1.mOwnerRecyclerView = null;
        }

        void recycleViewInternal(View view) {
            this.recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(view));
        }

        void scrapView(View object) {
            if (!(object = RecyclerView.getChildViewHolderInt((View)object)).isUpdated() || object.isInvalid() || RecyclerView.this.canReuseUpdatedViewHolder((ViewHolder)object)) {
                if (object.isInvalid() && !object.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds()) {
                    throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
                }
                object.setScrapContainer(this, false);
                this.mAttachedScrap.add((ViewHolder)object);
                return;
            }
            if (this.mChangedScrap == null) {
                this.mChangedScrap = new ArrayList();
            }
            object.setScrapContainer(this, true);
            this.mChangedScrap.add((ViewHolder)object);
        }

        void setAdapterPositionsAsUnknown() {
            int n = this.mCachedViews.size();
            for (int i = 0; i < n; ++i) {
                ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder == null) continue;
                viewHolder.addFlags(512);
            }
        }

        void setRecycledViewPool(RecycledViewPool recycledViewPool) {
            if (this.mRecyclerPool != null) {
                this.mRecyclerPool.detach();
            }
            this.mRecyclerPool = recycledViewPool;
            if (recycledViewPool != null) {
                this.mRecyclerPool.attach(RecyclerView.this.getAdapter());
            }
        }

        void setViewCacheExtension(ViewCacheExtension viewCacheExtension) {
            this.mViewCacheExtension = viewCacheExtension;
        }

        public void setViewCacheSize(int n) {
            this.mViewCacheMax = n;
            for (int i = this.mCachedViews.size() - 1; i >= 0 && this.mCachedViews.size() > n; --i) {
                this.recycleCachedViewAt(i);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        void unscrapView(ViewHolder viewHolder) {
            if (viewHolder.mInChangeScrap) {
                this.mChangedScrap.remove(viewHolder);
            } else {
                this.mAttachedScrap.remove(viewHolder);
            }
            viewHolder.mScrapContainer = null;
            viewHolder.mInChangeScrap = false;
            viewHolder.clearReturnedFromScrapFlag();
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        boolean validateViewHolderForOffsetPosition(ViewHolder viewHolder) {
            if (viewHolder.isRemoved()) {
                return true;
            }
            if (viewHolder.mPosition < 0) throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + viewHolder);
            if (viewHolder.mPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + viewHolder);
            }
            if (!RecyclerView.this.mState.isPreLayout() && RecyclerView.this.mAdapter.getItemViewType(viewHolder.mPosition) != viewHolder.getItemViewType()) {
                return false;
            }
            if (!RecyclerView.this.mAdapter.hasStableIds()) return true;
            if (viewHolder.getItemId() == RecyclerView.this.mAdapter.getItemId(viewHolder.mPosition)) return true;
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        void viewRangeUpdate(int n, int n2) {
            int n3 = this.mCachedViews.size() - 1;
            while (n3 >= 0) {
                int n4;
                ViewHolder viewHolder = this.mCachedViews.get(n3);
                if (viewHolder != null && (n4 = viewHolder.getLayoutPosition()) >= n && n4 < n + n2) {
                    viewHolder.addFlags(2);
                    this.recycleCachedViewAt(n3);
                }
                --n3;
            }
            return;
        }
    }

    public static interface RecyclerListener {
        public void onViewRecycled(ViewHolder var1);
    }

    private class RecyclerViewDataObserver
    extends AdapterDataObserver {
        private RecyclerViewDataObserver() {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapter.hasStableIds()) {
                RecyclerView.this.mState.mStructureChanged = true;
                RecyclerView.this.setDataSetChangedAfterLayout();
            } else {
                RecyclerView.this.mState.mStructureChanged = true;
                RecyclerView.this.setDataSetChangedAfterLayout();
            }
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
            }
        }

        @Override
        public void onItemRangeChanged(int n, int n2, Object object) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(n, n2, object)) {
                this.triggerUpdateProcessor();
            }
        }

        @Override
        public void onItemRangeInserted(int n, int n2) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(n, n2)) {
                this.triggerUpdateProcessor();
            }
        }

        @Override
        public void onItemRangeMoved(int n, int n2, int n3) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(n, n2, n3)) {
                this.triggerUpdateProcessor();
            }
        }

        @Override
        public void onItemRangeRemoved(int n, int n2) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(n, n2)) {
                this.triggerUpdateProcessor();
            }
        }

        void triggerUpdateProcessor() {
            if (RecyclerView.this.mPostUpdatesOnAnimation && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
                ViewCompat.postOnAnimation((View)RecyclerView.this, RecyclerView.this.mUpdateChildViewsRunnable);
                return;
            }
            RecyclerView.this.mAdapterUpdateDuringMeasure = true;
            RecyclerView.this.requestLayout();
        }
    }

    public static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        Parcelable mLayoutState;

        SavedState(Parcel parcel) {
            super(parcel);
            this.mLayoutState = parcel.readParcelable(LayoutManager.class.getClassLoader());
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void copyFrom(SavedState savedState) {
            this.mLayoutState = savedState.mLayoutState;
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeParcelable(this.mLayoutState, 0);
        }

    }

    public static class SimpleOnItemTouchListener
    implements OnItemTouchListener {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean bl) {
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        }
    }

    public static abstract class SmoothScroller {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private final Action mRecyclingAction = new Action(0, 0);
        private boolean mRunning;
        private int mTargetPosition = -1;
        private View mTargetView;

        /*
         * Enabled aggressive block sorting
         */
        private void onAnimation(int n, int n2) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (!this.mRunning || this.mTargetPosition == -1 || recyclerView == null) {
                this.stop();
            }
            this.mPendingInitialRun = false;
            if (this.mTargetView != null) {
                if (this.getChildPosition(this.mTargetView) == this.mTargetPosition) {
                    this.onTargetFound(this.mTargetView, recyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(recyclerView);
                    this.stop();
                } else {
                    Log.e((String)"RecyclerView", (String)"Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                this.onSeekTargetStep(n, n2, recyclerView.mState, this.mRecyclingAction);
                boolean bl = this.mRecyclingAction.hasJumpTarget();
                this.mRecyclingAction.runIfNecessary(recyclerView);
                if (bl) {
                    if (!this.mRunning) {
                        this.stop();
                        return;
                    }
                    this.mPendingInitialRun = true;
                    recyclerView.mViewFlinger.postOnAnimation();
                }
            }
        }

        public View findViewByPosition(int n) {
            return this.mRecyclerView.mLayout.findViewByPosition(n);
        }

        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }

        public int getChildPosition(View view) {
            return this.mRecyclerView.getChildLayoutPosition(view);
        }

        @Nullable
        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }

        public int getTargetPosition() {
            return this.mTargetPosition;
        }

        @Deprecated
        public void instantScrollToPosition(int n) {
            this.mRecyclerView.scrollToPosition(n);
        }

        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        protected void normalize(PointF pointF) {
            double d = Math.sqrt(pointF.x * pointF.x + pointF.y * pointF.y);
            pointF.x = (float)((double)pointF.x / d);
            pointF.y = (float)((double)pointF.y / d);
        }

        protected void onChildAttachedToWindow(View view) {
            if (this.getChildPosition(view) == this.getTargetPosition()) {
                this.mTargetView = view;
            }
        }

        protected abstract void onSeekTargetStep(int var1, int var2, State var3, Action var4);

        protected abstract void onStart();

        protected abstract void onStop();

        protected abstract void onTargetFound(View var1, State var2, Action var3);

        public void setTargetPosition(int n) {
            this.mTargetPosition = n;
        }

        void start(RecyclerView recyclerView, LayoutManager layoutManager) {
            this.mRecyclerView = recyclerView;
            this.mLayoutManager = layoutManager;
            if (this.mTargetPosition == -1) {
                throw new IllegalArgumentException("Invalid target position");
            }
            this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = this.findViewByPosition(this.getTargetPosition());
            this.onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
        }

        protected final void stop() {
            if (!this.mRunning) {
                return;
            }
            this.onStop();
            this.mRecyclerView.mState.mTargetPosition = -1;
            this.mTargetView = null;
            this.mTargetPosition = -1;
            this.mPendingInitialRun = false;
            this.mRunning = false;
            this.mLayoutManager.onSmoothScrollerStopped(this);
            this.mLayoutManager = null;
            this.mRecyclerView = null;
        }

        public static class Action {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean changed = false;
            private int consecutiveUpdates = 0;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition = -1;

            public Action(int n, int n2) {
                this(n, n2, Integer.MIN_VALUE, null);
            }

            public Action(int n, int n2, int n3) {
                this(n, n2, n3, null);
            }

            public Action(int n, int n2, int n3, Interpolator interpolator) {
                this.mDx = n;
                this.mDy = n2;
                this.mDuration = n3;
                this.mInterpolator = interpolator;
            }

            /*
             * Enabled aggressive block sorting
             */
            private void runIfNecessary(RecyclerView recyclerView) {
                if (this.mJumpToPosition >= 0) {
                    int n = this.mJumpToPosition;
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(n);
                    this.changed = false;
                    return;
                }
                if (!this.changed) {
                    this.consecutiveUpdates = 0;
                    return;
                }
                this.validate();
                if (this.mInterpolator == null) {
                    if (this.mDuration == Integer.MIN_VALUE) {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
                    } else {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
                    }
                } else {
                    recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                }
                ++this.consecutiveUpdates;
                if (this.consecutiveUpdates > 10) {
                    Log.e((String)"RecyclerView", (String)"Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                }
                this.changed = false;
            }

            private void validate() {
                if (this.mInterpolator != null && this.mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                }
                if (this.mDuration < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }

            public int getDuration() {
                return this.mDuration;
            }

            public int getDx() {
                return this.mDx;
            }

            public int getDy() {
                return this.mDy;
            }

            public Interpolator getInterpolator() {
                return this.mInterpolator;
            }

            boolean hasJumpTarget() {
                if (this.mJumpToPosition >= 0) {
                    return true;
                }
                return false;
            }

            public void jumpTo(int n) {
                this.mJumpToPosition = n;
            }

            public void setDuration(int n) {
                this.changed = true;
                this.mDuration = n;
            }

            public void setDx(int n) {
                this.changed = true;
                this.mDx = n;
            }

            public void setDy(int n) {
                this.changed = true;
                this.mDy = n;
            }

            public void setInterpolator(Interpolator interpolator) {
                this.changed = true;
                this.mInterpolator = interpolator;
            }

            public void update(int n, int n2, int n3, Interpolator interpolator) {
                this.mDx = n;
                this.mDy = n2;
                this.mDuration = n3;
                this.mInterpolator = interpolator;
                this.changed = true;
            }
        }

    }

    public static class State {
        private SparseArray<Object> mData;
        private int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        private boolean mInPreLayout = false;
        int mItemCount = 0;
        private int mPreviousLayoutItemCount = 0;
        private boolean mRunPredictiveAnimations = false;
        private boolean mRunSimpleAnimations = false;
        private boolean mStructureChanged = false;
        private int mTargetPosition = -1;
        private boolean mTrackOldChangeHolders = false;

        static /* synthetic */ int access$1812(State state, int n) {
            state.mDeletedInvisibleItemCountSincePreviousLayout = n = state.mDeletedInvisibleItemCountSincePreviousLayout + n;
            return n;
        }

        static /* synthetic */ boolean access$2200(State state) {
            return state.mInPreLayout;
        }

        public boolean didStructureChange() {
            return this.mStructureChanged;
        }

        public <T> T get(int n) {
            if (this.mData == null) {
                return null;
            }
            return (T)this.mData.get(n);
        }

        public int getItemCount() {
            if (this.mInPreLayout) {
                return this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
            }
            return this.mItemCount;
        }

        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            if (this.mTargetPosition != -1) {
                return true;
            }
            return false;
        }

        public boolean isPreLayout() {
            return this.mInPreLayout;
        }

        public void put(int n, Object object) {
            if (this.mData == null) {
                this.mData = new SparseArray();
            }
            this.mData.put(n, object);
        }

        public void remove(int n) {
            if (this.mData == null) {
                return;
            }
            this.mData.remove(n);
        }

        State reset() {
            this.mTargetPosition = -1;
            if (this.mData != null) {
                this.mData.clear();
            }
            this.mItemCount = 0;
            this.mStructureChanged = false;
            return this;
        }

        public String toString() {
            return "State{mTargetPosition=" + this.mTargetPosition + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
        }

        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }

        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }
    }

    public static abstract class ViewCacheExtension {
        public abstract View getViewForPositionAndType(Recycler var1, int var2, int var3);
    }

    private class ViewFlinger
    implements Runnable {
        private boolean mEatRunOnAnimationRequest;
        private Interpolator mInterpolator;
        private int mLastFlingX;
        private int mLastFlingY;
        private boolean mReSchedulePostAnimationCallback;
        private ScrollerCompat mScroller;

        public ViewFlinger() {
            this.mInterpolator = sQuinticInterpolator;
            this.mEatRunOnAnimationRequest = false;
            this.mReSchedulePostAnimationCallback = false;
            this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), sQuinticInterpolator);
        }

        /*
         * Enabled aggressive block sorting
         */
        private int computeScrollDuration(int n, int n2, int n3, int n4) {
            int n5;
            int n6 = Math.abs(n);
            boolean bl = n6 > (n5 = Math.abs(n2));
            n3 = (int)Math.sqrt(n3 * n3 + n4 * n4);
            n2 = (int)Math.sqrt(n * n + n2 * n2);
            n = bl ? RecyclerView.this.getWidth() : RecyclerView.this.getHeight();
            n4 = n / 2;
            float f = Math.min(1.0f, 1.0f * (float)n2 / (float)n);
            float f2 = n4;
            float f3 = n4;
            f = this.distanceInfluenceForSnapDuration(f);
            if (n3 > 0) {
                n = Math.round(1000.0f * Math.abs((f2 + f3 * f) / (float)n3)) * 4;
                return Math.min(n, 2000);
            }
            n2 = bl ? n6 : n5;
            n = (int)(((float)n2 / (float)n + 1.0f) * 300.0f);
            return Math.min(n, 2000);
        }

        private void disableRunOnAnimationRequests() {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
        }

        private float distanceInfluenceForSnapDuration(float f) {
            return (float)Math.sin((float)((double)(f - 0.5f) * 0.4712389167638204));
        }

        private void enableRunOnAnimationRequests() {
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                this.postOnAnimation();
            }
        }

        public void fling(int n, int n2) {
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.fling(0, 0, n, n2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.postOnAnimation();
        }

        void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
                return;
            }
            RecyclerView.this.removeCallbacks((Runnable)this);
            ViewCompat.postOnAnimation((View)RecyclerView.this, this);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void run() {
            this.disableRunOnAnimationRequests();
            RecyclerView.this.consumePendingUpdateOperations();
            ScrollerCompat scrollerCompat = this.mScroller;
            SmoothScroller smoothScroller = RecyclerView.access$1100((RecyclerView)RecyclerView.this).mSmoothScroller;
            if (scrollerCompat.computeScrollOffset()) {
                int n = scrollerCompat.getCurrX();
                int n2 = scrollerCompat.getCurrY();
                int n3 = n - this.mLastFlingX;
                int n4 = n2 - this.mLastFlingY;
                int n5 = 0;
                int n6 = 0;
                int n7 = 0;
                int n8 = 0;
                this.mLastFlingX = n;
                this.mLastFlingY = n2;
                int n9 = 0;
                int n10 = 0;
                int n11 = 0;
                int n12 = 0;
                if (RecyclerView.this.mAdapter != null) {
                    RecyclerView.this.eatRequestLayout();
                    RecyclerView.this.onEnterLayoutOrScroll();
                    TraceCompat.beginSection("RV Scroll");
                    if (n3 != 0) {
                        n6 = RecyclerView.this.mLayout.scrollHorizontallyBy(n3, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        n10 = n3 - n6;
                    }
                    if (n4 != 0) {
                        n8 = RecyclerView.this.mLayout.scrollVerticallyBy(n4, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        n12 = n4 - n8;
                    }
                    TraceCompat.endSection();
                    RecyclerView.this.repositionShadowingViews();
                    RecyclerView.this.onExitLayoutOrScroll();
                    RecyclerView.this.resumeRequestLayout(false);
                    n5 = n6;
                    n9 = n10;
                    n11 = n12;
                    n7 = n8;
                    if (smoothScroller != null) {
                        n5 = n6;
                        n9 = n10;
                        n11 = n12;
                        n7 = n8;
                        if (!smoothScroller.isPendingInitialRun()) {
                            n5 = n6;
                            n9 = n10;
                            n11 = n12;
                            n7 = n8;
                            if (smoothScroller.isRunning()) {
                                n5 = RecyclerView.this.mState.getItemCount();
                                if (n5 == 0) {
                                    smoothScroller.stop();
                                    n7 = n8;
                                    n11 = n12;
                                    n9 = n10;
                                    n5 = n6;
                                } else if (smoothScroller.getTargetPosition() >= n5) {
                                    smoothScroller.setTargetPosition(n5 - 1);
                                    smoothScroller.onAnimation(n3 - n10, n4 - n12);
                                    n5 = n6;
                                    n9 = n10;
                                    n11 = n12;
                                    n7 = n8;
                                } else {
                                    smoothScroller.onAnimation(n3 - n10, n4 - n12);
                                    n5 = n6;
                                    n9 = n10;
                                    n11 = n12;
                                    n7 = n8;
                                }
                            }
                        }
                    }
                }
                if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                if (ViewCompat.getOverScrollMode((View)RecyclerView.this) != 2) {
                    RecyclerView.this.considerReleasingGlowsOnScroll(n3, n4);
                }
                if (n9 != 0 || n11 != 0) {
                    n12 = (int)scrollerCompat.getCurrVelocity();
                    n6 = 0;
                    if (n9 != n) {
                        n6 = n9 < 0 ? - n12 : (n9 > 0 ? n12 : 0);
                    }
                    n10 = 0;
                    if (n11 != n2) {
                        n10 = n11 < 0 ? - n12 : (n11 > 0 ? n12 : 0);
                    }
                    if (ViewCompat.getOverScrollMode((View)RecyclerView.this) != 2) {
                        RecyclerView.this.absorbGlows(n6, n10);
                    }
                    if (!(n6 == 0 && n9 != n && scrollerCompat.getFinalX() != 0 || n10 == 0 && n11 != n2 && scrollerCompat.getFinalY() != 0)) {
                        scrollerCompat.abortAnimation();
                    }
                }
                if (n5 != 0 || n7 != 0) {
                    RecyclerView.this.dispatchOnScrolled(n5, n7);
                }
                if (!RecyclerView.this.awakenScrollBars()) {
                    RecyclerView.this.invalidate();
                }
                n6 = n4 != 0 && RecyclerView.this.mLayout.canScrollVertically() && n7 == n4 ? 1 : 0;
                n10 = n3 != 0 && RecyclerView.this.mLayout.canScrollHorizontally() && n5 == n3 ? 1 : 0;
                n6 = n3 == 0 && (n4 == 0 || n10 != 0) || n6 != 0 ? 1 : 0;
                if (scrollerCompat.isFinished() || n6 == 0) {
                    RecyclerView.this.setScrollState(0);
                } else {
                    this.postOnAnimation();
                }
            }
            if (smoothScroller != null) {
                if (smoothScroller.isPendingInitialRun()) {
                    smoothScroller.onAnimation(0, 0);
                }
                if (!this.mReSchedulePostAnimationCallback) {
                    smoothScroller.stop();
                }
            }
            this.enableRunOnAnimationRequests();
        }

        public void smoothScrollBy(int n, int n2) {
            this.smoothScrollBy(n, n2, 0, 0);
        }

        public void smoothScrollBy(int n, int n2, int n3) {
            this.smoothScrollBy(n, n2, n3, sQuinticInterpolator);
        }

        public void smoothScrollBy(int n, int n2, int n3, int n4) {
            this.smoothScrollBy(n, n2, this.computeScrollDuration(n, n2, n3, n4));
        }

        public void smoothScrollBy(int n, int n2, int n3, Interpolator interpolator) {
            if (this.mInterpolator != interpolator) {
                this.mInterpolator = interpolator;
                this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), interpolator);
            }
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.startScroll(0, 0, n, n2, n3);
            this.postOnAnimation();
        }

        public void stop() {
            RecyclerView.this.removeCallbacks((Runnable)this);
            this.mScroller.abortAnimation();
        }
    }

    public static abstract class ViewHolder {
        static final int FLAG_ADAPTER_FULLUPDATE = 1024;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
        static final int FLAG_BOUND = 1;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_MOVED = 2048;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
        public final View itemView;
        private int mFlags;
        private boolean mInChangeScrap = false;
        private int mIsRecyclableCount = 0;
        long mItemId = -1;
        int mItemViewType = -1;
        int mOldPosition = -1;
        RecyclerView mOwnerRecyclerView;
        List<Object> mPayloads = null;
        int mPosition = -1;
        int mPreLayoutPosition = -1;
        private Recycler mScrapContainer = null;
        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;
        List<Object> mUnmodifiedPayloads = null;
        private int mWasImportantForAccessibilityBeforeHidden = 0;

        public ViewHolder(View view) {
            if (view == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = view;
        }

        static /* synthetic */ boolean access$4700(ViewHolder viewHolder) {
            return viewHolder.doesTransientStatePreventRecycling();
        }

        private void createPayloadsIfNeeded() {
            if (this.mPayloads == null) {
                this.mPayloads = new ArrayList<Object>();
                this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
            }
        }

        private boolean doesTransientStatePreventRecycling() {
            if ((this.mFlags & 16) == 0 && ViewCompat.hasTransientState(this.itemView)) {
                return true;
            }
            return false;
        }

        private void onEnteredHiddenState() {
            this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            ViewCompat.setImportantForAccessibility(this.itemView, 4);
        }

        private void onLeftHiddenState() {
            ViewCompat.setImportantForAccessibility(this.itemView, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }

        private boolean shouldBeKeptAsChild() {
            if ((this.mFlags & 16) != 0) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        void addChangePayload(Object object) {
            if (object == null) {
                this.addFlags(1024);
                return;
            } else {
                if ((this.mFlags & 1024) != 0) return;
                {
                    this.createPayloadsIfNeeded();
                    this.mPayloads.add(object);
                    return;
                }
            }
        }

        void addFlags(int n) {
            this.mFlags |= n;
        }

        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }

        void clearPayload() {
            if (this.mPayloads != null) {
                this.mPayloads.clear();
            }
            this.mFlags &= -1025;
        }

        void clearReturnedFromScrapFlag() {
            this.mFlags &= -33;
        }

        void clearTmpDetachFlag() {
            this.mFlags &= -257;
        }

        void flagRemovedAndOffsetPosition(int n, int n2, boolean bl) {
            this.addFlags(8);
            this.offsetPosition(n2, bl);
            this.mPosition = n;
        }

        public final int getAdapterPosition() {
            if (this.mOwnerRecyclerView == null) {
                return -1;
            }
            return this.mOwnerRecyclerView.getAdapterPositionFor(this);
        }

        public final long getItemId() {
            return this.mItemId;
        }

        public final int getItemViewType() {
            return this.mItemViewType;
        }

        public final int getLayoutPosition() {
            if (this.mPreLayoutPosition == -1) {
                return this.mPosition;
            }
            return this.mPreLayoutPosition;
        }

        public final int getOldPosition() {
            return this.mOldPosition;
        }

        @Deprecated
        public final int getPosition() {
            if (this.mPreLayoutPosition == -1) {
                return this.mPosition;
            }
            return this.mPreLayoutPosition;
        }

        List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 1024) == 0) {
                if (this.mPayloads == null || this.mPayloads.size() == 0) {
                    return FULLUPDATE_PAYLOADS;
                }
                return this.mUnmodifiedPayloads;
            }
            return FULLUPDATE_PAYLOADS;
        }

        boolean hasAnyOfTheFlags(int n) {
            if ((this.mFlags & n) != 0) {
                return true;
            }
            return false;
        }

        boolean isAdapterPositionUnknown() {
            if ((this.mFlags & 512) != 0 || this.isInvalid()) {
                return true;
            }
            return false;
        }

        boolean isBound() {
            if ((this.mFlags & 1) != 0) {
                return true;
            }
            return false;
        }

        boolean isInvalid() {
            if ((this.mFlags & 4) != 0) {
                return true;
            }
            return false;
        }

        public final boolean isRecyclable() {
            if ((this.mFlags & 16) == 0 && !ViewCompat.hasTransientState(this.itemView)) {
                return true;
            }
            return false;
        }

        boolean isRemoved() {
            if ((this.mFlags & 8) != 0) {
                return true;
            }
            return false;
        }

        boolean isScrap() {
            if (this.mScrapContainer != null) {
                return true;
            }
            return false;
        }

        boolean isTmpDetached() {
            if ((this.mFlags & 256) != 0) {
                return true;
            }
            return false;
        }

        boolean isUpdated() {
            if ((this.mFlags & 2) != 0) {
                return true;
            }
            return false;
        }

        boolean needsUpdate() {
            if ((this.mFlags & 2) != 0) {
                return true;
            }
            return false;
        }

        void offsetPosition(int n, boolean bl) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (bl) {
                this.mPreLayoutPosition += n;
            }
            this.mPosition += n;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }

        void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            this.clearPayload();
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }

        void saveOldPosition() {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
        }

        void setFlags(int n, int n2) {
            this.mFlags = this.mFlags & ~ n2 | n & n2;
        }

        /*
         * Enabled aggressive block sorting
         */
        public final void setIsRecyclable(boolean bl) {
            int n = bl ? this.mIsRecyclableCount - 1 : this.mIsRecyclableCount + 1;
            this.mIsRecyclableCount = n;
            if (this.mIsRecyclableCount < 0) {
                this.mIsRecyclableCount = 0;
                Log.e((String)"View", (String)("isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this));
                return;
            } else {
                if (!bl && this.mIsRecyclableCount == 1) {
                    this.mFlags |= 16;
                    return;
                }
                if (!bl || this.mIsRecyclableCount != 0) return;
                {
                    this.mFlags &= -17;
                    return;
                }
            }
        }

        void setScrapContainer(Recycler recycler, boolean bl) {
            this.mScrapContainer = recycler;
            this.mInChangeScrap = bl;
        }

        boolean shouldIgnore() {
            if ((this.mFlags & 128) != 0) {
                return true;
            }
            return false;
        }

        void stopIgnoring() {
            this.mFlags &= -129;
        }

        /*
         * Enabled aggressive block sorting
         */
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("ViewHolder{" + Integer.toHexString(this.hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
            if (this.isScrap()) {
                StringBuilder stringBuilder2 = stringBuilder.append(" scrap ");
                String string2 = this.mInChangeScrap ? "[changeScrap]" : "[attachedScrap]";
                stringBuilder2.append(string2);
            }
            if (this.isInvalid()) {
                stringBuilder.append(" invalid");
            }
            if (!this.isBound()) {
                stringBuilder.append(" unbound");
            }
            if (this.needsUpdate()) {
                stringBuilder.append(" update");
            }
            if (this.isRemoved()) {
                stringBuilder.append(" removed");
            }
            if (this.shouldIgnore()) {
                stringBuilder.append(" ignored");
            }
            if (this.isTmpDetached()) {
                stringBuilder.append(" tmpDetached");
            }
            if (!this.isRecyclable()) {
                stringBuilder.append(" not recyclable(" + this.mIsRecyclableCount + ")");
            }
            if (this.isAdapterPositionUnknown()) {
                stringBuilder.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                stringBuilder.append(" no parent");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }

        boolean wasReturnedFromScrap() {
            if ((this.mFlags & 32) != 0) {
                return true;
            }
            return false;
        }
    }

}

