/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.Transformation
 *  android.widget.AbsListView
 */
package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CircleImageView;
import android.support.v4.widget.MaterialProgressDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

public class SwipeRefreshLayout
extends ViewGroup
implements NestedScrollingParent,
NestedScrollingChild {
    private static final int ALPHA_ANIMATION_DURATION = 300;
    private static final int ANIMATE_TO_START_DURATION = 200;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int CIRCLE_BG_LIGHT = -328966;
    private static final int CIRCLE_DIAMETER = 40;
    private static final int CIRCLE_DIAMETER_LARGE = 56;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    public static final int DEFAULT = 1;
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    private static final float DRAG_RATE = 0.5f;
    private static final int INVALID_POINTER = -1;
    public static final int LARGE = 0;
    private static final int[] LAYOUT_ATTRS;
    private static final String LOG_TAG;
    private static final int MAX_ALPHA = 255;
    private static final float MAX_PROGRESS_ANGLE = 0.8f;
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int STARTING_PROGRESS_ALPHA = 76;
    private int mActivePointerId = -1;
    private Animation mAlphaMaxAnimation;
    private Animation mAlphaStartAnimation;
    private final Animation mAnimateToCorrectPosition;
    private final Animation mAnimateToStartPosition;
    private int mCircleHeight;
    private CircleImageView mCircleView;
    private int mCircleViewIndex = -1;
    private int mCircleWidth;
    private int mCurrentTargetOffsetTop;
    private final DecelerateInterpolator mDecelerateInterpolator;
    protected int mFrom;
    private float mInitialDownY;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private OnRefreshListener mListener;
    private int mMediumAnimationDuration;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private boolean mNotify;
    private boolean mOriginalOffsetCalculated = false;
    protected int mOriginalOffsetTop;
    private final int[] mParentScrollConsumed = new int[2];
    private MaterialProgressDrawable mProgress;
    private Animation.AnimationListener mRefreshListener;
    private boolean mRefreshing = false;
    private boolean mReturningToStart;
    private boolean mScale;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mScaleDownToStartAnimation;
    private float mSpinnerFinalOffset;
    private float mStartingScale;
    private View mTarget;
    private float mTotalDragDistance = -1.0f;
    private float mTotalUnconsumed;
    private int mTouchSlop;
    private boolean mUsingCustomStart;

    static {
        LOG_TAG = SwipeRefreshLayout.class.getSimpleName();
        LAYOUT_ATTRS = new int[]{16842766};
    }

    public SwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRefreshListener = new Animation.AnimationListener(){

            /*
             * Enabled aggressive block sorting
             */
            public void onAnimationEnd(Animation animation) {
                if (SwipeRefreshLayout.this.mRefreshing) {
                    SwipeRefreshLayout.this.mProgress.setAlpha(255);
                    SwipeRefreshLayout.this.mProgress.start();
                    if (SwipeRefreshLayout.this.mNotify && SwipeRefreshLayout.this.mListener != null) {
                        SwipeRefreshLayout.this.mListener.onRefresh();
                    }
                } else {
                    SwipeRefreshLayout.this.mProgress.stop();
                    SwipeRefreshLayout.this.mCircleView.setVisibility(8);
                    SwipeRefreshLayout.this.setColorViewAlpha(255);
                    if (SwipeRefreshLayout.this.mScale) {
                        SwipeRefreshLayout.this.setAnimationProgress(0.0f);
                    } else {
                        SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(SwipeRefreshLayout.this.mOriginalOffsetTop - SwipeRefreshLayout.this.mCurrentTargetOffsetTop, true);
                    }
                }
                SwipeRefreshLayout.this.mCurrentTargetOffsetTop = SwipeRefreshLayout.this.mCircleView.getTop();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        };
        this.mAnimateToCorrectPosition = new Animation(){

            /*
             * Enabled aggressive block sorting
             */
            public void applyTransformation(float f, Transformation transformation) {
                int n = !SwipeRefreshLayout.this.mUsingCustomStart ? (int)(SwipeRefreshLayout.this.mSpinnerFinalOffset - (float)Math.abs(SwipeRefreshLayout.this.mOriginalOffsetTop)) : (int)SwipeRefreshLayout.this.mSpinnerFinalOffset;
                int n2 = SwipeRefreshLayout.this.mFrom;
                n = (int)((float)(n - SwipeRefreshLayout.this.mFrom) * f);
                int n3 = SwipeRefreshLayout.this.mCircleView.getTop();
                SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(n2 + n - n3, false);
                SwipeRefreshLayout.this.mProgress.setArrowScale(1.0f - f);
            }
        };
        this.mAnimateToStartPosition = new Animation(){

            public void applyTransformation(float f, Transformation transformation) {
                SwipeRefreshLayout.this.moveToStart(f);
            }
        };
        this.mTouchSlop = ViewConfiguration.get((Context)context).getScaledTouchSlop();
        this.mMediumAnimationDuration = this.getResources().getInteger(17694721);
        this.setWillNotDraw(false);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
        context = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
        this.setEnabled(context.getBoolean(0, true));
        context.recycle();
        context = this.getResources().getDisplayMetrics();
        this.mCircleWidth = (int)(context.density * 40.0f);
        this.mCircleHeight = (int)(context.density * 40.0f);
        this.createProgressView();
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        this.mTotalDragDistance = this.mSpinnerFinalOffset = 64.0f * context.density;
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
    }

    private void animateOffsetToCorrectPosition(int n, Animation.AnimationListener animationListener) {
        this.mFrom = n;
        this.mAnimateToCorrectPosition.reset();
        this.mAnimateToCorrectPosition.setDuration(200);
        this.mAnimateToCorrectPosition.setInterpolator((Interpolator)this.mDecelerateInterpolator);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mAnimateToCorrectPosition);
    }

    private void animateOffsetToStartPosition(int n, Animation.AnimationListener animationListener) {
        if (this.mScale) {
            this.startScaleDownReturnToStartAnimation(n, animationListener);
            return;
        }
        this.mFrom = n;
        this.mAnimateToStartPosition.reset();
        this.mAnimateToStartPosition.setDuration(200);
        this.mAnimateToStartPosition.setInterpolator((Interpolator)this.mDecelerateInterpolator);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mAnimateToStartPosition);
    }

    private void createProgressView() {
        this.mCircleView = new CircleImageView(this.getContext(), -328966, 20.0f);
        this.mProgress = new MaterialProgressDrawable(this.getContext(), (View)this);
        this.mProgress.setBackgroundColor(-328966);
        this.mCircleView.setImageDrawable((Drawable)this.mProgress);
        this.mCircleView.setVisibility(8);
        this.addView((View)this.mCircleView);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void ensureTarget() {
        if (this.mTarget != null) return;
        int n = 0;
        while (n < this.getChildCount()) {
            View view = this.getChildAt(n);
            if (!view.equals((Object)this.mCircleView)) {
                this.mTarget = view;
                return;
            }
            ++n;
        }
    }

    private void finishSpinner(float f) {
        if (f > this.mTotalDragDistance) {
            this.setRefreshing(true, true);
            return;
        }
        this.mRefreshing = false;
        this.mProgress.setStartEndTrim(0.0f, 0.0f);
        Animation.AnimationListener animationListener = null;
        if (!this.mScale) {
            animationListener = new Animation.AnimationListener(){

                public void onAnimationEnd(Animation animation) {
                    if (!SwipeRefreshLayout.this.mScale) {
                        SwipeRefreshLayout.this.startScaleDownAnimation(null);
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            };
        }
        this.animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, animationListener);
        this.mProgress.showArrow(false);
    }

    private float getMotionEventY(MotionEvent motionEvent, int n) {
        if ((n = MotionEventCompat.findPointerIndex(motionEvent, n)) < 0) {
            return -1.0f;
        }
        return MotionEventCompat.getY(motionEvent, n);
    }

    private boolean isAlphaUsedForScale() {
        if (Build.VERSION.SDK_INT < 11) {
            return true;
        }
        return false;
    }

    private boolean isAnimationRunning(Animation animation) {
        if (animation != null && animation.hasStarted() && !animation.hasEnded()) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void moveSpinner(float f) {
        this.mProgress.showArrow(true);
        float f2 = Math.min(1.0f, Math.abs(f / this.mTotalDragDistance));
        float f3 = (float)Math.max((double)f2 - 0.4, 0.0) * 5.0f / 3.0f;
        float f4 = Math.abs(f);
        float f5 = this.mTotalDragDistance;
        float f6 = this.mUsingCustomStart ? this.mSpinnerFinalOffset - (float)this.mOriginalOffsetTop : this.mSpinnerFinalOffset;
        f4 = Math.max(0.0f, Math.min(f4 - f5, 2.0f * f6) / f6);
        f4 = (float)((double)(f4 / 4.0f) - Math.pow(f4 / 4.0f, 2.0)) * 2.0f;
        int n = this.mOriginalOffsetTop;
        int n2 = (int)(f6 * f2 + f6 * f4 * 2.0f);
        if (this.mCircleView.getVisibility() != 0) {
            this.mCircleView.setVisibility(0);
        }
        if (!this.mScale) {
            ViewCompat.setScaleX((View)this.mCircleView, 1.0f);
            ViewCompat.setScaleY((View)this.mCircleView, 1.0f);
        }
        if (f < this.mTotalDragDistance) {
            if (this.mScale) {
                this.setAnimationProgress(f / this.mTotalDragDistance);
            }
            if (this.mProgress.getAlpha() > 76 && !this.isAnimationRunning(this.mAlphaStartAnimation)) {
                this.startProgressAlphaStartAnimation();
            }
            this.mProgress.setStartEndTrim(0.0f, Math.min(0.8f, f3 * 0.8f));
            this.mProgress.setArrowScale(Math.min(1.0f, f3));
        } else if (this.mProgress.getAlpha() < 255 && !this.isAnimationRunning(this.mAlphaMaxAnimation)) {
            this.startProgressAlphaMaxAnimation();
        }
        this.mProgress.setProgressRotation((-0.25f + 0.4f * f3 + 2.0f * f4) * 0.5f);
        this.setTargetOffsetTopAndBottom(n + n2 - this.mCurrentTargetOffsetTop, true);
    }

    private void moveToStart(float f) {
        this.setTargetOffsetTopAndBottom(this.mFrom + (int)((float)(this.mOriginalOffsetTop - this.mFrom) * f) - this.mCircleView.getTop(), false);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int n = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, n) == this.mActivePointerId) {
            n = n == 0 ? 1 : 0;
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, n);
        }
    }

    private void setAnimationProgress(float f) {
        if (this.isAlphaUsedForScale()) {
            this.setColorViewAlpha((int)(255.0f * f));
            return;
        }
        ViewCompat.setScaleX((View)this.mCircleView, f);
        ViewCompat.setScaleY((View)this.mCircleView, f);
    }

    private void setColorViewAlpha(int n) {
        this.mCircleView.getBackground().setAlpha(n);
        this.mProgress.setAlpha(n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void setRefreshing(boolean bl, boolean bl2) {
        if (this.mRefreshing == bl) return;
        this.mNotify = bl2;
        this.ensureTarget();
        this.mRefreshing = bl;
        if (this.mRefreshing) {
            this.animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
            return;
        }
        this.startScaleDownAnimation(this.mRefreshListener);
    }

    private void setTargetOffsetTopAndBottom(int n, boolean bl) {
        this.mCircleView.bringToFront();
        this.mCircleView.offsetTopAndBottom(n);
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
        if (bl && Build.VERSION.SDK_INT < 11) {
            this.invalidate();
        }
    }

    private Animation startAlphaAnimation(final int n, final int n2) {
        if (this.mScale && this.isAlphaUsedForScale()) {
            return null;
        }
        Animation animation = new Animation(){

            public void applyTransformation(float f, Transformation transformation) {
                SwipeRefreshLayout.this.mProgress.setAlpha((int)((float)n + (float)(n2 - n) * f));
            }
        };
        animation.setDuration(300);
        this.mCircleView.setAnimationListener(null);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(animation);
        return animation;
    }

    private void startProgressAlphaMaxAnimation() {
        this.mAlphaMaxAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 255);
    }

    private void startProgressAlphaStartAnimation() {
        this.mAlphaStartAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 76);
    }

    private void startScaleDownAnimation(Animation.AnimationListener animationListener) {
        this.mScaleDownAnimation = new Animation(){

            public void applyTransformation(float f, Transformation transformation) {
                SwipeRefreshLayout.this.setAnimationProgress(1.0f - f);
            }
        };
        this.mScaleDownAnimation.setDuration(150);
        this.mCircleView.setAnimationListener(animationListener);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownAnimation);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void startScaleDownReturnToStartAnimation(int n, Animation.AnimationListener animationListener) {
        this.mFrom = n;
        this.mStartingScale = this.isAlphaUsedForScale() ? (float)this.mProgress.getAlpha() : ViewCompat.getScaleX((View)this.mCircleView);
        this.mScaleDownToStartAnimation = new Animation(){

            public void applyTransformation(float f, Transformation transformation) {
                float f2 = SwipeRefreshLayout.this.mStartingScale;
                float f3 = - SwipeRefreshLayout.this.mStartingScale;
                SwipeRefreshLayout.this.setAnimationProgress(f2 + f3 * f);
                SwipeRefreshLayout.this.moveToStart(f);
            }
        };
        this.mScaleDownToStartAnimation.setDuration(150);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownToStartAnimation);
    }

    private void startScaleUpAnimation(Animation.AnimationListener animationListener) {
        this.mCircleView.setVisibility(0);
        if (Build.VERSION.SDK_INT >= 11) {
            this.mProgress.setAlpha(255);
        }
        this.mScaleAnimation = new Animation(){

            public void applyTransformation(float f, Transformation transformation) {
                SwipeRefreshLayout.this.setAnimationProgress(f);
            }
        };
        this.mScaleAnimation.setDuration((long)this.mMediumAnimationDuration);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleAnimation);
    }

    public boolean canChildScrollUp() {
        boolean bl = false;
        if (Build.VERSION.SDK_INT < 14) {
            if (this.mTarget instanceof AbsListView) {
                AbsListView absListView = (AbsListView)this.mTarget;
                if (absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop())) {
                    return true;
                }
                return false;
            }
            if (ViewCompat.canScrollVertically(this.mTarget, -1) || this.mTarget.getScrollY() > 0) {
                bl = true;
            }
            return bl;
        }
        return ViewCompat.canScrollVertically(this.mTarget, -1);
    }

    @Override
    public boolean dispatchNestedFling(float f, float f2, boolean bl) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(f, f2, bl);
    }

    @Override
    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(f, f2);
    }

    @Override
    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(n, n2, arrn, arrn2);
    }

    @Override
    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn) {
        return this.mNestedScrollingChildHelper.dispatchNestedScroll(n, n2, n3, n4, arrn);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected int getChildDrawingOrder(int n, int n2) {
        if (this.mCircleViewIndex < 0) {
            return n2;
        }
        if (n2 == n - 1) {
            return this.mCircleViewIndex;
        }
        if (n2 < this.mCircleViewIndex) return n2;
        return n2 + 1;
    }

    @Override
    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public int getProgressCircleDiameter() {
        if (this.mCircleView != null) {
            return this.mCircleView.getMeasuredHeight();
        }
        return 0;
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean isRefreshing() {
        return this.mRefreshing;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onInterceptTouchEvent(MotionEvent var1_1) {
        this.ensureTarget();
        var3_2 = MotionEventCompat.getActionMasked(var1_1);
        if (this.mReturningToStart && var3_2 == 0) {
            this.mReturningToStart = false;
        }
        if (this.isEnabled() == false) return false;
        if (this.mReturningToStart != false) return false;
        if (this.canChildScrollUp() != false) return false;
        if (this.mRefreshing) {
            return false;
        }
        switch (var3_2) {
            case 0: {
                this.setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCircleView.getTop(), true);
                this.mActivePointerId = MotionEventCompat.getPointerId(var1_1, 0);
                this.mIsBeingDragged = false;
                var2_3 = this.getMotionEventY(var1_1, this.mActivePointerId);
                if (var2_3 == -1.0f) return false;
                this.mInitialDownY = var2_3;
                ** break;
            }
            case 2: {
                if (this.mActivePointerId == -1) {
                    Log.e((String)SwipeRefreshLayout.LOG_TAG, (String)"Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }
                var2_4 = this.getMotionEventY(var1_1, this.mActivePointerId);
                if (var2_4 == -1.0f) return false;
                if (var2_4 - this.mInitialDownY <= (float)this.mTouchSlop) return this.mIsBeingDragged;
                if (this.mIsBeingDragged != false) return this.mIsBeingDragged;
                this.mInitialMotionY = this.mInitialDownY + (float)this.mTouchSlop;
                this.mIsBeingDragged = true;
                this.mProgress.setAlpha(76);
                ** break;
            }
            case 6: {
                this.onSecondaryPointerUp(var1_1);
            }
lbl33: // 4 sources:
            default: {
                return this.mIsBeingDragged;
            }
            case 1: 
            case 3: 
        }
        this.mIsBeingDragged = false;
        this.mActivePointerId = -1;
        return this.mIsBeingDragged;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n = this.getMeasuredWidth();
        n2 = this.getMeasuredHeight();
        if (this.getChildCount() == 0) {
            return;
        }
        if (this.mTarget == null) {
            this.ensureTarget();
        }
        if (this.mTarget == null) return;
        View view = this.mTarget;
        n3 = this.getPaddingLeft();
        n4 = this.getPaddingTop();
        view.layout(n3, n4, n3 + (n - this.getPaddingLeft() - this.getPaddingRight()), n4 + (n2 - this.getPaddingTop() - this.getPaddingBottom()));
        n2 = this.mCircleView.getMeasuredWidth();
        n3 = this.mCircleView.getMeasuredHeight();
        this.mCircleView.layout(n / 2 - n2 / 2, this.mCurrentTargetOffsetTop, n / 2 + n2 / 2, this.mCurrentTargetOffsetTop + n3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        if (this.mTarget == null) {
            this.ensureTarget();
        }
        if (this.mTarget == null) {
            return;
        }
        this.mTarget.measure(View.MeasureSpec.makeMeasureSpec((int)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight()), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom()), (int)1073741824));
        this.mCircleView.measure(View.MeasureSpec.makeMeasureSpec((int)this.mCircleWidth, (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)this.mCircleHeight, (int)1073741824));
        if (!this.mUsingCustomStart && !this.mOriginalOffsetCalculated) {
            this.mOriginalOffsetCalculated = true;
            this.mOriginalOffsetTop = n = - this.mCircleView.getMeasuredHeight();
            this.mCurrentTargetOffsetTop = n;
        }
        this.mCircleViewIndex = -1;
        n = 0;
        while (n < this.getChildCount()) {
            if (this.getChildAt(n) == this.mCircleView) {
                this.mCircleViewIndex = n;
                return;
            }
            ++n;
        }
    }

    @Override
    public boolean onNestedFling(View view, float f, float f2, boolean bl) {
        return this.dispatchNestedFling(f, f2, bl);
    }

    @Override
    public boolean onNestedPreFling(View view, float f, float f2) {
        return this.dispatchNestedPreFling(f, f2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onNestedPreScroll(View arrn, int n, int n2, int[] arrn2) {
        if (n2 > 0 && this.mTotalUnconsumed > 0.0f) {
            if ((float)n2 > this.mTotalUnconsumed) {
                arrn2[1] = n2 - (int)this.mTotalUnconsumed;
                this.mTotalUnconsumed = 0.0f;
            } else {
                this.mTotalUnconsumed -= (float)n2;
                arrn2[1] = n2;
            }
            this.moveSpinner(this.mTotalUnconsumed);
        }
        if (this.mUsingCustomStart && n2 > 0 && this.mTotalUnconsumed == 0.0f && Math.abs(n2 - arrn2[1]) > 0) {
            this.mCircleView.setVisibility(8);
        }
        if (this.dispatchNestedPreScroll(n - arrn2[0], n2 - arrn2[1], arrn = this.mParentScrollConsumed, null)) {
            arrn2[0] = arrn2[0] + arrn[0];
            arrn2[1] = arrn2[1] + arrn[1];
        }
    }

    @Override
    public void onNestedScroll(View view, int n, int n2, int n3, int n4) {
        if (n4 < 0) {
            n4 = Math.abs(n4);
            this.mTotalUnconsumed += (float)n4;
            this.moveSpinner(this.mTotalUnconsumed);
        }
        this.dispatchNestedScroll(n, n2, n3, n, null);
    }

    @Override
    public void onNestedScrollAccepted(View view, View view2, int n) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, n);
        this.mTotalUnconsumed = 0.0f;
    }

    @Override
    public boolean onStartNestedScroll(View view, View view2, int n) {
        if (this.isEnabled() && !this.mReturningToStart && !this.canChildScrollUp() && !this.mRefreshing && (n & 2) != 0) {
            this.startNestedScroll(n & 2);
            return true;
        }
        return false;
    }

    @Override
    public void onStopNestedScroll(View view) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view);
        if (this.mTotalUnconsumed > 0.0f) {
            this.finishSpinner(this.mTotalUnconsumed);
            this.mTotalUnconsumed = 0.0f;
        }
        this.stopNestedScroll();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1_1) {
        var4_2 = MotionEventCompat.getActionMasked(var1_1);
        if (this.mReturningToStart && var4_2 == 0) {
            this.mReturningToStart = false;
        }
        if (this.isEnabled() == false) return false;
        if (this.mReturningToStart != false) return false;
        if (this.canChildScrollUp()) {
            return false;
        }
        switch (var4_2) {
            case 3: {
                return false;
            }
            case 0: {
                this.mActivePointerId = MotionEventCompat.getPointerId(var1_1, 0);
                this.mIsBeingDragged = false;
                ** break;
            }
            case 2: {
                var4_2 = MotionEventCompat.findPointerIndex(var1_1, this.mActivePointerId);
                if (var4_2 < 0) {
                    Log.e((String)SwipeRefreshLayout.LOG_TAG, (String)"Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                var2_3 = (MotionEventCompat.getY(var1_1, var4_2) - this.mInitialMotionY) * 0.5f;
                if (this.mIsBeingDragged == false) return true;
                if (var2_3 <= 0.0f) return false;
                this.moveSpinner(var2_3);
                ** break;
            }
            case 5: {
                var4_2 = MotionEventCompat.getActionIndex(var1_1);
                if (var4_2 < 0) {
                    Log.e((String)SwipeRefreshLayout.LOG_TAG, (String)"Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                this.mActivePointerId = MotionEventCompat.getPointerId(var1_1, var4_2);
                ** break;
            }
            case 6: {
                this.onSecondaryPointerUp(var1_1);
            }
lbl34: // 5 sources:
            default: {
                return true;
            }
            case 1: 
        }
        var4_2 = MotionEventCompat.findPointerIndex(var1_1, this.mActivePointerId);
        if (var4_2 < 0) {
            Log.e((String)SwipeRefreshLayout.LOG_TAG, (String)"Got ACTION_UP event but don't have an active pointer id.");
            return false;
        }
        var2_4 = MotionEventCompat.getY(var1_1, var4_2);
        var3_5 = this.mInitialMotionY;
        this.mIsBeingDragged = false;
        this.finishSpinner((var2_4 - var3_5) * 0.5f);
        this.mActivePointerId = -1;
        return false;
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        if (Build.VERSION.SDK_INT < 21 && this.mTarget instanceof AbsListView || this.mTarget != null && !ViewCompat.isNestedScrollingEnabled(this.mTarget)) {
            return;
        }
        super.requestDisallowInterceptTouchEvent(bl);
    }

    @Deprecated
    public /* varargs */ void setColorScheme(@ColorInt int ... arrn) {
        this.setColorSchemeResources(arrn);
    }

    @ColorInt
    public /* varargs */ void setColorSchemeColors(int ... arrn) {
        this.ensureTarget();
        this.mProgress.setColorSchemeColors(arrn);
    }

    public /* varargs */ void setColorSchemeResources(@ColorRes int ... arrn) {
        Resources resources = this.getResources();
        int[] arrn2 = new int[arrn.length];
        for (int i = 0; i < arrn.length; ++i) {
            arrn2[i] = resources.getColor(arrn[i]);
        }
        this.setColorSchemeColors(arrn2);
    }

    public void setDistanceToTriggerSync(int n) {
        this.mTotalDragDistance = n;
    }

    @Override
    public void setNestedScrollingEnabled(boolean bl) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(bl);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mListener = onRefreshListener;
    }

    @Deprecated
    public void setProgressBackgroundColor(int n) {
        this.setProgressBackgroundColorSchemeResource(n);
    }

    public void setProgressBackgroundColorSchemeColor(@ColorInt int n) {
        this.mCircleView.setBackgroundColor(n);
        this.mProgress.setBackgroundColor(n);
    }

    public void setProgressBackgroundColorSchemeResource(@ColorRes int n) {
        this.setProgressBackgroundColorSchemeColor(this.getResources().getColor(n));
    }

    public void setProgressViewEndTarget(boolean bl, int n) {
        this.mSpinnerFinalOffset = n;
        this.mScale = bl;
        this.mCircleView.invalidate();
    }

    public void setProgressViewOffset(boolean bl, int n, int n2) {
        this.mScale = bl;
        this.mCircleView.setVisibility(8);
        this.mCurrentTargetOffsetTop = n;
        this.mOriginalOffsetTop = n;
        this.mSpinnerFinalOffset = n2;
        this.mUsingCustomStart = true;
        this.mCircleView.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setRefreshing(boolean bl) {
        if (bl && this.mRefreshing != bl) {
            this.mRefreshing = bl;
            int n = !this.mUsingCustomStart ? (int)(this.mSpinnerFinalOffset + (float)this.mOriginalOffsetTop) : (int)this.mSpinnerFinalOffset;
            this.setTargetOffsetTopAndBottom(n - this.mCurrentTargetOffsetTop, true);
            this.mNotify = false;
            this.startScaleUpAnimation(this.mRefreshListener);
            return;
        }
        this.setRefreshing(bl, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSize(int n) {
        if (n != 0 && n != 1) {
            return;
        }
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        if (n == 0) {
            int n2;
            this.mCircleWidth = n2 = (int)(56.0f * displayMetrics.density);
            this.mCircleHeight = n2;
        } else {
            int n3;
            this.mCircleWidth = n3 = (int)(40.0f * displayMetrics.density);
            this.mCircleHeight = n3;
        }
        this.mCircleView.setImageDrawable(null);
        this.mProgress.updateSizes(n);
        this.mCircleView.setImageDrawable((Drawable)this.mProgress);
    }

    @Override
    public boolean startNestedScroll(int n) {
        return this.mNestedScrollingChildHelper.startNestedScroll(n);
    }

    @Override
    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    public static interface OnRefreshListener {
        public void onRefresh();
    }

}

