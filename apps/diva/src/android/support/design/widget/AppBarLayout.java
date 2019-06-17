/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.HeaderBehavior;
import android.support.design.widget.HeaderScrollingViewBehavior;
import android.support.design.widget.MathUtils;
import android.support.design.widget.ThemeUtils;
import android.support.design.widget.ValueAnimatorCompat;
import android.support.design.widget.ViewUtils;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(value=Behavior.class)
public class AppBarLayout
extends LinearLayout {
    private static final int INVALID_SCROLL_RANGE = -1;
    private static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
    private static final int PENDING_ACTION_COLLAPSED = 2;
    private static final int PENDING_ACTION_EXPANDED = 1;
    private static final int PENDING_ACTION_NONE = 0;
    private int mDownPreScrollRange = -1;
    private int mDownScrollRange = -1;
    boolean mHaveChildWithInterpolator;
    private WindowInsetsCompat mLastInsets;
    private final List<OnOffsetChangedListener> mListeners;
    private int mPendingAction = 0;
    private float mTargetElevation;
    private int mTotalScrollRange = -1;

    public AppBarLayout(Context context) {
        this(context, null);
    }

    public AppBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setOrientation(1);
        ThemeUtils.checkAppCompatTheme(context);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.AppBarLayout, 0, R.style.Widget_Design_AppBarLayout);
        this.mTargetElevation = context.getDimensionPixelSize(R.styleable.AppBarLayout_elevation, 0);
        this.setBackgroundDrawable(context.getDrawable(R.styleable.AppBarLayout_android_background));
        if (context.hasValue(R.styleable.AppBarLayout_expanded)) {
            this.setExpanded(context.getBoolean(R.styleable.AppBarLayout_expanded, false));
        }
        context.recycle();
        ViewUtils.setBoundsViewOutlineProvider((View)this);
        this.mListeners = new ArrayList<OnOffsetChangedListener>();
        ViewCompat.setElevation((View)this, this.mTargetElevation);
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener(){

            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                AppBarLayout.this.setWindowInsets(windowInsetsCompat);
                return windowInsetsCompat.consumeSystemWindowInsets();
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getDownNestedPreScrollRange() {
        if (this.mDownPreScrollRange != -1) {
            return this.mDownPreScrollRange;
        }
        int n = 0;
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            View view = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n2 = view.getMeasuredHeight();
            int n3 = layoutParams.mScrollFlags;
            if ((n3 & 5) == 5) {
                n += layoutParams.topMargin + layoutParams.bottomMargin;
                n2 = (n3 & 8) != 0 ? n + ViewCompat.getMinimumHeight(view) : n + n2;
            } else {
                n2 = n;
                if (n > 0) break;
            }
            n = n2;
        }
        this.mDownPreScrollRange = n;
        return n;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int getDownNestedScrollRange() {
        if (this.mDownScrollRange != -1) {
            return this.mDownScrollRange;
        }
        var1_1 = 0;
        var2_2 = 0;
        var4_3 = this.getChildCount();
        do {
            var3_4 = var1_1;
            if (var2_2 >= var4_3) ** GOTO lbl20
            var9_9 = this.getChildAt(var2_2);
            var10_10 = (LayoutParams)var9_9.getLayoutParams();
            var6_6 = var9_9.getMeasuredHeight();
            var7_7 = var10_10.topMargin;
            var8_8 = var10_10.bottomMargin;
            var5_5 = var10_10.mScrollFlags;
            var3_4 = var1_1;
            if ((var5_5 & 1) == 0) ** GOTO lbl20
            var1_1 += var6_6 + (var7_7 + var8_8);
            if ((var5_5 & 2) != 0) {
                var3_4 = var1_1 - (ViewCompat.getMinimumHeight(var9_9) + this.getTopInset());
lbl20: // 3 sources:
                this.mDownScrollRange = var1_1 = Math.max(0, var3_4);
                return var1_1;
            }
            ++var2_2;
        } while (true);
    }

    private int getPendingAction() {
        return this.mPendingAction;
    }

    private int getTopInset() {
        if (this.mLastInsets != null) {
            return this.mLastInsets.getSystemWindowInsetTop();
        }
        return 0;
    }

    private int getUpNestedPreScrollRange() {
        return this.getTotalScrollRange();
    }

    private boolean hasChildWithInterpolator() {
        return this.mHaveChildWithInterpolator;
    }

    private boolean hasScrollableChildren() {
        if (this.getTotalScrollRange() != 0) {
            return true;
        }
        return false;
    }

    private void invalidateScrollRanges() {
        this.mTotalScrollRange = -1;
        this.mDownPreScrollRange = -1;
        this.mDownScrollRange = -1;
    }

    private void resetPendingAction() {
        this.mPendingAction = 0;
    }

    private void setWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        this.mTotalScrollRange = -1;
        this.mLastInsets = windowInsetsCompat;
        int n = 0;
        int n2 = this.getChildCount();
        while (n < n2 && !(windowInsetsCompat = ViewCompat.dispatchApplyWindowInsets(this.getChildAt(n), windowInsetsCompat)).isConsumed()) {
            ++n;
        }
        return;
    }

    public void addOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        if (onOffsetChangedListener != null && !this.mListeners.contains(onOffsetChangedListener)) {
            this.mListeners.add(onOffsetChangedListener);
        }
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            return new LayoutParams((LinearLayout.LayoutParams)layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    /*
     * Enabled aggressive block sorting
     */
    final int getMinimumHeightForVisibleOverlappingContent() {
        int n = 0;
        int n2 = this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
        int n3 = ViewCompat.getMinimumHeight((View)this);
        if (n3 != 0) {
            return n3 * 2 + n2;
        }
        n3 = this.getChildCount();
        if (n3 < 1) return n;
        return ViewCompat.getMinimumHeight(this.getChildAt(n3 - 1)) * 2 + n2;
    }

    public float getTargetElevation() {
        return this.mTargetElevation;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public final int getTotalScrollRange() {
        if (this.mTotalScrollRange != -1) {
            return this.mTotalScrollRange;
        }
        var1_1 = 0;
        var2_2 = 0;
        var4_3 = this.getChildCount();
        do {
            var3_4 = var1_1;
            if (var2_2 >= var4_3) ** GOTO lbl18
            var7_7 = this.getChildAt(var2_2);
            var8_8 = (LayoutParams)var7_7.getLayoutParams();
            var6_6 = var7_7.getMeasuredHeight();
            var5_5 = var8_8.mScrollFlags;
            var3_4 = var1_1;
            if ((var5_5 & 1) == 0) ** GOTO lbl18
            var1_1 += var8_8.topMargin + var6_6 + var8_8.bottomMargin;
            if ((var5_5 & 2) != 0) {
                var3_4 = var1_1 - ViewCompat.getMinimumHeight(var7_7);
lbl18: // 3 sources:
                this.mTotalScrollRange = var1_1 = Math.max(0, var3_4 - this.getTopInset());
                return var1_1;
            }
            ++var2_2;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.invalidateScrollRanges();
        this.mHaveChildWithInterpolator = false;
        n = 0;
        n2 = this.getChildCount();
        while (n < n2) {
            if (((LayoutParams)this.getChildAt(n).getLayoutParams()).getScrollInterpolator() != null) {
                this.mHaveChildWithInterpolator = true;
                return;
            }
            ++n;
        }
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        this.invalidateScrollRanges();
    }

    public void removeOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        if (onOffsetChangedListener != null) {
            this.mListeners.remove(onOffsetChangedListener);
        }
    }

    public void setExpanded(boolean bl) {
        this.setExpanded(bl, ViewCompat.isLaidOut((View)this));
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setExpanded(boolean bl, boolean bl2) {
        int n = bl ? 1 : 2;
        int n2 = bl2 ? 4 : 0;
        this.mPendingAction = n2 | n;
        this.requestLayout();
    }

    public void setOrientation(int n) {
        if (n != 1) {
            throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
        }
        super.setOrientation(n);
    }

    public void setTargetElevation(float f) {
        this.mTargetElevation = f;
    }

    public static class Behavior
    extends HeaderBehavior<AppBarLayout> {
        private static final int INVALID_POSITION = -1;
        private ValueAnimatorCompat mAnimator;
        private WeakReference<View> mLastNestedScrollingChildRef;
        private int mOffsetDelta;
        private int mOffsetToChildIndexOnLayout = -1;
        private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
        private float mOffsetToChildIndexOnLayoutPerc;
        private DragCallback mOnDragCallback;
        private boolean mSkipNestedPreScroll;
        private boolean mWasFlung;

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void animateOffsetTo(final CoordinatorLayout coordinatorLayout, final AppBarLayout appBarLayout, int n) {
            if (this.mAnimator == null) {
                this.mAnimator = ViewUtils.createAnimator();
                this.mAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
                this.mAnimator.setUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener(){

                    @Override
                    public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                        Behavior.this.setHeaderTopBottomOffset(coordinatorLayout, appBarLayout, valueAnimatorCompat.getAnimatedIntValue());
                    }
                });
            } else {
                this.mAnimator.cancel();
            }
            this.mAnimator.setIntValues(this.getTopBottomOffsetForScrollingSibling(), n);
            this.mAnimator.start();
        }

        private void dispatchOffsetUpdates(AppBarLayout appBarLayout) {
            List list = appBarLayout.mListeners;
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                OnOffsetChangedListener onOffsetChangedListener = (OnOffsetChangedListener)list.get(i);
                if (onOffsetChangedListener == null) continue;
                onOffsetChangedListener.onOffsetChanged(appBarLayout, this.getTopAndBottomOffset());
            }
        }

        private View getChildOnOffset(AppBarLayout appBarLayout, int n) {
            int n2 = appBarLayout.getChildCount();
            for (int i = 0; i < n2; ++i) {
                View view = appBarLayout.getChildAt(i);
                if (view.getTop() > - n || view.getBottom() < - n) continue;
                return view;
            }
            return null;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private int interpolateOffset(AppBarLayout appBarLayout, int n) {
            int n2 = Math.abs(n);
            int n3 = 0;
            int n4 = appBarLayout.getChildCount();
            do {
                int n5 = n;
                if (n3 >= n4) return n5;
                View view = appBarLayout.getChildAt(n3);
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                Interpolator interpolator = layoutParams.getScrollInterpolator();
                if (n2 >= view.getTop() && n2 <= view.getBottom()) {
                    n5 = n;
                    if (interpolator == null) return n5;
                    n5 = 0;
                    n4 = layoutParams.getScrollFlags();
                    if ((n4 & 1) != 0) {
                        n5 = n3 = 0 + (view.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                        if ((n4 & 2) != 0) {
                            n5 = n3 - ViewCompat.getMinimumHeight(view);
                        }
                    }
                    n3 = n5;
                    if (ViewCompat.getFitsSystemWindows(view)) {
                        n3 = n5 - appBarLayout.getTopInset();
                    }
                    n5 = n;
                    if (n3 <= 0) return n5;
                    n5 = view.getTop();
                    n5 = Math.round((float)n3 * interpolator.getInterpolation((float)(n2 - n5) / (float)n3));
                    return Integer.signum(n) * (view.getTop() + n5);
                }
                ++n3;
            } while (true);
        }

        private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
            int n = this.getTopBottomOffsetForScrollingSibling();
            View view = this.getChildOnOffset(appBarLayout, n);
            if (view != null && (((LayoutParams)view.getLayoutParams()).getScrollFlags() & 17) == 17) {
                int n2 = - view.getTop();
                int n3 = - view.getBottom();
                if (n < (n3 + n2) / 2) {
                    n2 = n3;
                }
                this.animateOffsetTo(coordinatorLayout, appBarLayout, n2);
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        boolean canDragView(AppBarLayout appBarLayout) {
            boolean bl = true;
            if (this.mOnDragCallback != null) {
                return this.mOnDragCallback.canDrag(appBarLayout);
            }
            boolean bl2 = bl;
            if (this.mLastNestedScrollingChildRef == null) return bl2;
            appBarLayout = this.mLastNestedScrollingChildRef.get();
            if (appBarLayout == null) return false;
            if (!appBarLayout.isShown()) return false;
            bl2 = bl;
            if (!ViewCompat.canScrollVertically((View)appBarLayout, -1)) return bl2;
            return false;
        }

        @Override
        int getMaxDragOffset(AppBarLayout appBarLayout) {
            return - appBarLayout.getDownNestedScrollRange();
        }

        @Override
        int getScrollRangeForDragFling(AppBarLayout appBarLayout) {
            return appBarLayout.getTotalScrollRange();
        }

        @Override
        int getTopBottomOffsetForScrollingSibling() {
            return this.getTopAndBottomOffset() + this.mOffsetDelta;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int n) {
            boolean bl = super.onLayoutChild(coordinatorLayout, appBarLayout, n);
            int n2 = appBarLayout.getPendingAction();
            if (n2 != 0) {
                n = (n2 & 4) != 0 ? 1 : 0;
                if ((n2 & 2) != 0) {
                    n2 = - appBarLayout.getUpNestedPreScrollRange();
                    if (n != 0) {
                        this.animateOffsetTo(coordinatorLayout, appBarLayout, n2);
                    } else {
                        this.setHeaderTopBottomOffset(coordinatorLayout, appBarLayout, n2);
                    }
                } else if ((n2 & 1) != 0) {
                    if (n != 0) {
                        this.animateOffsetTo(coordinatorLayout, appBarLayout, 0);
                    } else {
                        this.setHeaderTopBottomOffset(coordinatorLayout, appBarLayout, 0);
                    }
                }
            } else if (this.mOffsetToChildIndexOnLayout >= 0) {
                coordinatorLayout = appBarLayout.getChildAt(this.mOffsetToChildIndexOnLayout);
                n = - coordinatorLayout.getBottom();
                n = this.mOffsetToChildIndexOnLayoutIsMinHeight ? (n += ViewCompat.getMinimumHeight((View)coordinatorLayout)) : (n += Math.round((float)coordinatorLayout.getHeight() * this.mOffsetToChildIndexOnLayoutPerc));
                this.setTopAndBottomOffset(n);
            }
            appBarLayout.resetPendingAction();
            this.mOffsetToChildIndexOnLayout = -1;
            this.dispatchOffsetUpdates(appBarLayout);
            return bl;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, float f, float f2, boolean bl) {
            boolean bl2 = false;
            if (!bl) {
                bl = this.fling(coordinatorLayout, appBarLayout, - appBarLayout.getTotalScrollRange(), 0, - f2);
            } else if (f2 < 0.0f) {
                int n = - appBarLayout.getTotalScrollRange() + appBarLayout.getDownNestedPreScrollRange();
                bl = bl2;
                if (this.getTopBottomOffsetForScrollingSibling() < n) {
                    this.animateOffsetTo(coordinatorLayout, appBarLayout, n);
                    bl = true;
                }
            } else {
                int n = - appBarLayout.getUpNestedPreScrollRange();
                bl = bl2;
                if (this.getTopBottomOffsetForScrollingSibling() > n) {
                    this.animateOffsetTo(coordinatorLayout, appBarLayout, n);
                    bl = true;
                }
            }
            this.mWasFlung = bl;
            return bl;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int n, int n2, int[] arrn) {
            if (n2 != 0 && !this.mSkipNestedPreScroll) {
                int n3;
                if (n2 < 0) {
                    n = - appBarLayout.getTotalScrollRange();
                    n3 = n + appBarLayout.getDownNestedPreScrollRange();
                } else {
                    n = - appBarLayout.getUpNestedPreScrollRange();
                    n3 = 0;
                }
                arrn[1] = this.scroll(coordinatorLayout, appBarLayout, n2, n, n3);
            }
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int n, int n2, int n3, int n4) {
            if (n4 < 0) {
                this.scroll(coordinatorLayout, appBarLayout, n4, - appBarLayout.getDownNestedScrollRange(), 0);
                this.mSkipNestedPreScroll = true;
                return;
            }
            this.mSkipNestedPreScroll = false;
        }

        @Override
        public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, Parcelable object) {
            if (object instanceof SavedState) {
                object = (SavedState)((Object)object);
                super.onRestoreInstanceState(coordinatorLayout, appBarLayout, object.getSuperState());
                this.mOffsetToChildIndexOnLayout = object.firstVisibleChildIndex;
                this.mOffsetToChildIndexOnLayoutPerc = object.firstVisibileChildPercentageShown;
                this.mOffsetToChildIndexOnLayoutIsMinHeight = object.firstVisibileChildAtMinimumHeight;
                return;
            }
            super.onRestoreInstanceState(coordinatorLayout, appBarLayout, (Parcelable)object);
            this.mOffsetToChildIndexOnLayout = -1;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, AppBarLayout object) {
            Parcelable parcelable = super.onSaveInstanceState(coordinatorLayout, object);
            int n = this.getTopAndBottomOffset();
            int n2 = 0;
            int n3 = object.getChildCount();
            while (n2 < n3) {
                coordinatorLayout = object.getChildAt(n2);
                int n4 = coordinatorLayout.getBottom() + n;
                if (coordinatorLayout.getTop() + n <= 0 && n4 >= 0) {
                    object = new SavedState(parcelable);
                    object.firstVisibleChildIndex = n2;
                    boolean bl = n4 == ViewCompat.getMinimumHeight((View)coordinatorLayout);
                    object.firstVisibileChildAtMinimumHeight = bl;
                    object.firstVisibileChildPercentageShown = (float)n4 / (float)coordinatorLayout.getHeight();
                    return object;
                }
                ++n2;
            }
            return parcelable;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int n) {
            boolean bl = (n & 2) != 0 && appBarLayout.hasScrollableChildren() && coordinatorLayout.getHeight() - view.getHeight() <= appBarLayout.getHeight();
            if (bl && this.mAnimator != null) {
                this.mAnimator.cancel();
            }
            this.mLastNestedScrollingChildRef = null;
            return bl;
        }

        @Override
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view) {
            if (!this.mWasFlung) {
                this.snapToChildIfNeeded(coordinatorLayout, appBarLayout);
            }
            this.mSkipNestedPreScroll = false;
            this.mWasFlung = false;
            this.mLastNestedScrollingChildRef = new WeakReference<View>(view);
        }

        public void setDragCallback(@Nullable DragCallback dragCallback) {
            this.mOnDragCallback = dragCallback;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int n, int n2, int n3) {
            int n4;
            int n5 = this.getTopBottomOffsetForScrollingSibling();
            int n6 = n4 = 0;
            if (n2 != 0) {
                n6 = n4;
                if (n5 >= n2) {
                    n6 = n4;
                    if (n5 <= n3) {
                        n2 = MathUtils.constrain(n, n2, n3);
                        n6 = n4;
                        if (n5 != n2) {
                            n = appBarLayout.hasChildWithInterpolator() ? this.interpolateOffset(appBarLayout, n2) : n2;
                            boolean bl = this.setTopAndBottomOffset(n);
                            n6 = n5 - n2;
                            this.mOffsetDelta = n2 - n;
                            if (!bl && appBarLayout.hasChildWithInterpolator()) {
                                coordinatorLayout.dispatchDependentViewsChanged((View)appBarLayout);
                            }
                            this.dispatchOffsetUpdates(appBarLayout);
                        }
                    }
                }
            }
            return n6;
        }

        public static abstract class DragCallback {
            public abstract boolean canDrag(@NonNull AppBarLayout var1);
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
            boolean firstVisibileChildAtMinimumHeight;
            float firstVisibileChildPercentageShown;
            int firstVisibleChildIndex;

            /*
             * Enabled aggressive block sorting
             */
            public SavedState(Parcel parcel, ClassLoader classLoader) {
                super(parcel);
                this.firstVisibleChildIndex = parcel.readInt();
                this.firstVisibileChildPercentageShown = parcel.readFloat();
                boolean bl = parcel.readByte() != 0;
                this.firstVisibileChildAtMinimumHeight = bl;
            }

            public SavedState(Parcelable parcelable) {
                super(parcelable);
            }

            /*
             * Enabled aggressive block sorting
             */
            public void writeToParcel(Parcel parcel, int n) {
                super.writeToParcel(parcel, n);
                parcel.writeInt(this.firstVisibleChildIndex);
                parcel.writeFloat(this.firstVisibileChildPercentageShown);
                n = this.firstVisibileChildAtMinimumHeight ? 1 : 0;
                parcel.writeByte((byte)n);
            }

        }

    }

    public static class LayoutParams
    extends LinearLayout.LayoutParams {
        static final int FLAG_QUICK_RETURN = 5;
        static final int FLAG_SNAP = 17;
        public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
        public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
        public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
        public static final int SCROLL_FLAG_SCROLL = 1;
        public static final int SCROLL_FLAG_SNAP = 16;
        int mScrollFlags = 1;
        Interpolator mScrollInterpolator;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(int n, int n2, float f) {
            super(n, n2, f);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.AppBarLayout_LayoutParams);
            this.mScrollFlags = attributeSet.getInt(R.styleable.AppBarLayout_LayoutParams_layout_scrollFlags, 0);
            if (attributeSet.hasValue(R.styleable.AppBarLayout_LayoutParams_layout_scrollInterpolator)) {
                this.mScrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator((Context)context, (int)attributeSet.getResourceId(R.styleable.AppBarLayout_LayoutParams_layout_scrollInterpolator, 0));
            }
            attributeSet.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((LinearLayout.LayoutParams)layoutParams);
            this.mScrollFlags = layoutParams.mScrollFlags;
            this.mScrollInterpolator = layoutParams.mScrollInterpolator;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(LinearLayout.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public int getScrollFlags() {
            return this.mScrollFlags;
        }

        public Interpolator getScrollInterpolator() {
            return this.mScrollInterpolator;
        }

        public void setScrollFlags(int n) {
            this.mScrollFlags = n;
        }

        public void setScrollInterpolator(Interpolator interpolator) {
            this.mScrollInterpolator = interpolator;
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface ScrollFlags {
        }

    }

    public static interface OnOffsetChangedListener {
        public void onOffsetChanged(AppBarLayout var1, int var2);
    }

    public static class ScrollingViewBehavior
    extends HeaderScrollingViewBehavior {
        private int mOverlayTop;

        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, R.styleable.ScrollingViewBehavior_Params);
            this.mOverlayTop = context.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Params_behavior_overlapTop, 0);
            context.recycle();
        }

        private int getOverlapForOffset(View object, int n) {
            if (this.mOverlayTop != 0 && object instanceof AppBarLayout) {
                object = (AppBarLayout)((Object)object);
                int n2 = object.getTotalScrollRange();
                int n3 = ((AppBarLayout)((Object)object)).getDownNestedPreScrollRange();
                if (n3 != 0 && n2 + n <= n3) {
                    return 0;
                }
                if ((n2 -= n3) != 0) {
                    return MathUtils.constrain(Math.round((1.0f + (float)n / (float)n2) * (float)this.mOverlayTop), 0, this.mOverlayTop);
                }
            }
            return this.mOverlayTop;
        }

        private boolean updateOffset(CoordinatorLayout object, View view, View view2) {
            object = ((CoordinatorLayout.LayoutParams)view2.getLayoutParams()).getBehavior();
            if (object instanceof Behavior) {
                int n = ((Behavior)object).getTopBottomOffsetForScrollingSibling();
                this.setTopAndBottomOffset(view2.getHeight() + n - this.getOverlapForOffset(view2, n));
                return true;
            }
            return false;
        }

        @Override
        View findFirstDependency(List<View> list) {
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                View view = list.get(i);
                if (!(view instanceof AppBarLayout)) continue;
                return view;
            }
            return null;
        }

        public int getOverlayTop() {
            return this.mOverlayTop;
        }

        @Override
        int getScrollRange(View view) {
            if (view instanceof AppBarLayout) {
                return ((AppBarLayout)view).getTotalScrollRange();
            }
            return super.getScrollRange(view);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
            return view2 instanceof AppBarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
            this.updateOffset(coordinatorLayout, view, view2);
            return false;
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int n) {
            super.onLayoutChild(coordinatorLayout, view, n);
            List<View> list = coordinatorLayout.getDependencies(view);
            n = 0;
            int n2 = list.size();
            while (n < n2 && !this.updateOffset(coordinatorLayout, view, list.get(n))) {
                ++n;
            }
            return true;
        }

        public void setOverlayTop(int n) {
            this.mOverlayTop = n;
        }
    }

}

