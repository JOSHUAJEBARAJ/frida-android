/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.design.widget;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SwipeDismissBehavior<V extends View>
extends CoordinatorLayout.Behavior<V> {
    private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0f;
    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5f;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    public static final int SWIPE_DIRECTION_ANY = 2;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    private float mAlphaEndSwipeDistance = 0.5f;
    private float mAlphaStartSwipeDistance = 0.0f;
    private final ViewDragHelper.Callback mDragCallback;
    private float mDragDismissThreshold = 0.5f;
    private boolean mIgnoreEvents;
    private OnDismissListener mListener;
    private float mSensitivity = 0.0f;
    private boolean mSensitivitySet;
    private int mSwipeDirection = 2;
    private ViewDragHelper mViewDragHelper;

    public SwipeDismissBehavior() {
        this.mDragCallback = new ViewDragHelper.Callback(){
            private int mOriginalCapturedViewLeft;

            /*
             * Enabled aggressive block sorting
             */
            private boolean shouldDismiss(View view, float f) {
                if (f != 0.0f) {
                    boolean bl = ViewCompat.getLayoutDirection(view) == 1;
                    if (SwipeDismissBehavior.this.mSwipeDirection == 2) return true;
                    {
                        if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                            if (!(bl ? f >= 0.0f : f <= 0.0f)) return true;
                            return false;
                        } else {
                            if (SwipeDismissBehavior.this.mSwipeDirection != 1) {
                                return false;
                            }
                            if (!(bl ? f <= 0.0f : f >= 0.0f)) return true;
                            return false;
                        }
                    }
                }
                int n = view.getLeft();
                int n2 = this.mOriginalCapturedViewLeft;
                int n3 = Math.round((float)view.getWidth() * SwipeDismissBehavior.this.mDragDismissThreshold);
                if (Math.abs(n - n2) < n3) return false;
                return true;
            }

            /*
             * Enabled aggressive block sorting
             */
            @Override
            public int clampViewPositionHorizontal(View view, int n, int n2) {
                int n3;
                n2 = ViewCompat.getLayoutDirection(view) == 1 ? 1 : 0;
                if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                    if (n2 != 0) {
                        n3 = this.mOriginalCapturedViewLeft - view.getWidth();
                        n2 = this.mOriginalCapturedViewLeft;
                        return SwipeDismissBehavior.clamp(n3, n, n2);
                    }
                    n3 = this.mOriginalCapturedViewLeft;
                    n2 = this.mOriginalCapturedViewLeft + view.getWidth();
                    return SwipeDismissBehavior.clamp(n3, n, n2);
                }
                if (SwipeDismissBehavior.this.mSwipeDirection != 1) {
                    n3 = this.mOriginalCapturedViewLeft - view.getWidth();
                    n2 = this.mOriginalCapturedViewLeft + view.getWidth();
                    return SwipeDismissBehavior.clamp(n3, n, n2);
                }
                if (n2 != 0) {
                    n3 = this.mOriginalCapturedViewLeft;
                    n2 = this.mOriginalCapturedViewLeft + view.getWidth();
                    return SwipeDismissBehavior.clamp(n3, n, n2);
                }
                n3 = this.mOriginalCapturedViewLeft - view.getWidth();
                n2 = this.mOriginalCapturedViewLeft;
                return SwipeDismissBehavior.clamp(n3, n, n2);
            }

            @Override
            public int clampViewPositionVertical(View view, int n, int n2) {
                return view.getTop();
            }

            @Override
            public int getViewHorizontalDragRange(View view) {
                return view.getWidth();
            }

            @Override
            public void onViewDragStateChanged(int n) {
                if (SwipeDismissBehavior.this.mListener != null) {
                    SwipeDismissBehavior.this.mListener.onDragStateChanged(n);
                }
            }

            @Override
            public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
                float f = (float)this.mOriginalCapturedViewLeft + (float)view.getWidth() * SwipeDismissBehavior.this.mAlphaStartSwipeDistance;
                float f2 = (float)this.mOriginalCapturedViewLeft + (float)view.getWidth() * SwipeDismissBehavior.this.mAlphaEndSwipeDistance;
                if ((float)n <= f) {
                    ViewCompat.setAlpha(view, 1.0f);
                    return;
                }
                if ((float)n >= f2) {
                    ViewCompat.setAlpha(view, 0.0f);
                    return;
                }
                ViewCompat.setAlpha(view, SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(f, f2, n), 1.0f));
            }

            /*
             * Enabled aggressive block sorting
             */
            @Override
            public void onViewReleased(View view, float f, float f2) {
                int n = view.getWidth();
                boolean bl = false;
                if (this.shouldDismiss(view, f)) {
                    n = view.getLeft() < this.mOriginalCapturedViewLeft ? this.mOriginalCapturedViewLeft - n : this.mOriginalCapturedViewLeft + n;
                    bl = true;
                } else {
                    n = this.mOriginalCapturedViewLeft;
                }
                if (SwipeDismissBehavior.this.mViewDragHelper.settleCapturedViewAt(n, view.getTop())) {
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, bl));
                    return;
                } else {
                    if (SwipeDismissBehavior.this.mListener == null) return;
                    {
                        SwipeDismissBehavior.this.mListener.onDismiss(view);
                        return;
                    }
                }
            }

            @Override
            public boolean tryCaptureView(View view, int n) {
                this.mOriginalCapturedViewLeft = view.getLeft();
                return true;
            }
        };
    }

    private static float clamp(float f, float f2, float f3) {
        return Math.min(Math.max(f, f2), f3);
    }

    private static int clamp(int n, int n2, int n3) {
        return Math.min(Math.max(n, n2), n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void ensureViewDragHelper(ViewGroup object) {
        if (this.mViewDragHelper == null) {
            object = this.mSensitivitySet ? ViewDragHelper.create((ViewGroup)object, this.mSensitivity, this.mDragCallback) : ViewDragHelper.create((ViewGroup)object, this.mDragCallback);
            this.mViewDragHelper = object;
        }
    }

    static float fraction(float f, float f2, float f3) {
        return (f3 - f) / (f2 - f);
    }

    public int getDragState() {
        if (this.mViewDragHelper != null) {
            return this.mViewDragHelper.getViewDragState();
        }
        return 0;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout var1_1, V var2_2, MotionEvent var3_3) {
        switch (MotionEventCompat.getActionMasked(var3_3)) {
            default: {
                var4_4 = var1_1.isPointInChildBounds((View)var2_2, (int)var3_3.getX(), (int)var3_3.getY()) == false;
            }
            case 1: 
            case 3: {
                if (this.mIgnoreEvents) {
                    this.mIgnoreEvents = false;
                    return false;
                }
                ** GOTO lbl10
            }
        }
        this.mIgnoreEvents = var4_4;
lbl10: // 2 sources:
        if (this.mIgnoreEvents) {
            return false;
        }
        this.ensureViewDragHelper(var1_1);
        return this.mViewDragHelper.shouldInterceptTouchEvent(var3_3);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (this.mViewDragHelper != null) {
            this.mViewDragHelper.processTouchEvent(motionEvent);
            return true;
        }
        return false;
    }

    public void setDragDismissDistance(float f) {
        this.mDragDismissThreshold = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setEndAlphaSwipeDistance(float f) {
        this.mAlphaEndSwipeDistance = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setListener(OnDismissListener onDismissListener) {
        this.mListener = onDismissListener;
    }

    public void setSensitivity(float f) {
        this.mSensitivity = f;
        this.mSensitivitySet = true;
    }

    public void setStartAlphaSwipeDistance(float f) {
        this.mAlphaStartSwipeDistance = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setSwipeDirection(int n) {
        this.mSwipeDirection = n;
    }

    public static interface OnDismissListener {
        public void onDismiss(View var1);

        public void onDragStateChanged(int var1);
    }

    private class SettleRunnable
    implements Runnable {
        private final boolean mDismiss;
        private final View mView;

        SettleRunnable(View view, boolean bl) {
            this.mView = view;
            this.mDismiss = bl;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void run() {
            if (SwipeDismissBehavior.this.mViewDragHelper != null && SwipeDismissBehavior.this.mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.mView, this);
                return;
            } else {
                if (!this.mDismiss || SwipeDismissBehavior.this.mListener == null) return;
                {
                    SwipeDismissBehavior.this.mListener.onDismiss(this.mView);
                    return;
                }
            }
        }
    }

}

