/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnDoubleTapListener
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.ViewConfiguration
 */
package android.support.v4.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
    private final GestureDetectorCompatImpl mImpl;

    public GestureDetectorCompat(Context context, GestureDetector.OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public GestureDetectorCompat(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
        if (Build.VERSION.SDK_INT > 17) {
            this.mImpl = new GestureDetectorCompatImplJellybeanMr2(context, onGestureListener, handler);
            return;
        }
        this.mImpl = new GestureDetectorCompatImplBase(context, onGestureListener, handler);
    }

    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mImpl.onTouchEvent(motionEvent);
    }

    public void setIsLongpressEnabled(boolean bl) {
        this.mImpl.setIsLongpressEnabled(bl);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        this.mImpl.setOnDoubleTapListener(onDoubleTapListener);
    }

    static interface GestureDetectorCompatImpl {
        public boolean isLongpressEnabled();

        public boolean onTouchEvent(MotionEvent var1);

        public void setIsLongpressEnabled(boolean var1);

        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener var1);
    }

    static class GestureDetectorCompatImplBase
    implements GestureDetectorCompatImpl {
        private static final int DOUBLE_TAP_TIMEOUT;
        private static final int LONGPRESS_TIMEOUT;
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT;
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        MotionEvent mCurrentDownEvent;
        boolean mDeferConfirmSingleTap;
        GestureDetector.OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        final GestureDetector.OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;

        static {
            LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
            TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
            DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        }

        GestureDetectorCompatImplBase(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            this.mHandler = handler != null ? new GestureHandler(this, handler) : new GestureHandler(this);
            this.mListener = onGestureListener;
            if (onGestureListener instanceof GestureDetector.OnDoubleTapListener) {
                this.setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)onGestureListener);
            }
            this.init(context);
        }

        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private void init(Context context) {
            if (context != null) {
                if (this.mListener != null) {
                    this.mIsLongpressEnabled = true;
                    context = ViewConfiguration.get((Context)context);
                    int n = context.getScaledTouchSlop();
                    int n2 = context.getScaledDoubleTapSlop();
                    this.mMinimumFlingVelocity = context.getScaledMinimumFlingVelocity();
                    this.mMaximumFlingVelocity = context.getScaledMaximumFlingVelocity();
                    this.mTouchSlopSquare = n * n;
                    this.mDoubleTapSlopSquare = n2 * n2;
                    return;
                }
                throw new IllegalArgumentException("OnGestureListener must not be null");
            }
            throw new IllegalArgumentException("Context must not be null");
        }

        private boolean isConsideredDoubleTap(MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
            int n;
            boolean bl = this.mAlwaysInBiggerTapRegion;
            boolean bl2 = false;
            if (!bl) {
                return false;
            }
            if (motionEvent3.getEventTime() - motionEvent2.getEventTime() > (long)DOUBLE_TAP_TIMEOUT) {
                return false;
            }
            int n2 = (int)motionEvent.getX() - (int)motionEvent3.getX();
            if (n2 * n2 + (n = (int)motionEvent.getY() - (int)motionEvent3.getY()) * n < this.mDoubleTapSlopSquare) {
                bl2 = true;
            }
            return bl2;
        }

        void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }

        @Override
        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public boolean onTouchEvent(MotionEvent var1_1) {
            var9_2 = var1_1.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(var1_1);
            var6_3 = (var9_2 & 255) == 6 ? 1 : 0;
            var7_4 = var6_3 != 0 ? var1_1.getActionIndex() : -1;
            var3_5 = 0.0f;
            var2_6 = 0.0f;
            var10_7 = var1_1.getPointerCount();
            for (var8_8 = 0; var8_8 < var10_7; ++var8_8) {
                if (var7_4 == var8_8) continue;
                var3_5 += var1_1.getX(var8_8);
                var2_6 += var1_1.getY(var8_8);
            }
            var7_4 = var6_3 != 0 ? var10_7 - 1 : var10_7;
            var3_5 /= (float)var7_4;
            var2_6 /= (float)var7_4;
            var13_9 = false;
            var7_4 = 0;
            var12_10 = false;
            var8_8 = var9_2 & 255;
            if (var8_8 == 0) ** GOTO lbl105
            if (var8_8 == 1) ** GOTO lbl64
            if (var8_8 == 2) ** GOTO lbl38
            if (var8_8 == 3) {
                this.cancel();
                return false;
            }
            if (var8_8 == 5) {
                this.mLastFocusX = var3_5;
                this.mDownFocusX = var3_5;
                this.mLastFocusY = var2_6;
                this.mDownFocusY = var2_6;
                this.cancelTaps();
                return false;
            }
            if (var8_8 != 6) {
                return false;
            }
            ** GOTO lbl134
lbl38: // 1 sources:
            if (this.mInLongPress) {
                return false;
            }
            var4_12 = this.mLastFocusX - var3_5;
            var5_13 = this.mLastFocusY - var2_6;
            if (this.mIsDoubleTapping) {
                return false | this.mDoubleTapListener.onDoubleTapEvent(var1_1);
            }
            if (this.mAlwaysInTapRegion) {
                var6_3 = (int)(var3_5 - this.mDownFocusX);
                var7_4 = (int)(var2_6 - this.mDownFocusY);
                if ((var6_3 = var6_3 * var6_3 + var7_4 * var7_4) > this.mTouchSlopSquare) {
                    var12_10 = this.mListener.onScroll(this.mCurrentDownEvent, var1_1, var4_12, var5_13);
                    this.mLastFocusX = var3_5;
                    this.mLastFocusY = var2_6;
                    this.mAlwaysInTapRegion = false;
                    this.mHandler.removeMessages(3);
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(2);
                }
                if (var6_3 <= this.mTouchSlopSquare) return var12_10;
                this.mAlwaysInBiggerTapRegion = false;
                return var12_10;
            }
            if (Math.abs(var4_12) < 1.0f) {
                if (Math.abs(var5_13) < 1.0f) return false;
            }
            var12_10 = this.mListener.onScroll(this.mCurrentDownEvent, var1_1, var4_12, var5_13);
            this.mLastFocusX = var3_5;
            this.mLastFocusY = var2_6;
            return var12_10;
lbl64: // 1 sources:
            this.mStillDown = false;
            var14_14 = MotionEvent.obtain((MotionEvent)var1_1);
            if (!this.mIsDoubleTapping) ** GOTO lbl69
            var12_10 = false | this.mDoubleTapListener.onDoubleTapEvent(var1_1);
            ** GOTO lbl92
lbl69: // 1 sources:
            if (!this.mInLongPress) ** GOTO lbl74
            this.mHandler.removeMessages(3);
            this.mInLongPress = false;
            var12_10 = var13_9;
            ** GOTO lbl92
lbl74: // 1 sources:
            if (!this.mAlwaysInTapRegion) ** GOTO lbl83
            var12_10 = var13_9 = this.mListener.onSingleTapUp(var1_1);
            if (this.mDeferConfirmSingleTap) {
                var15_16 = this.mDoubleTapListener;
                var12_10 = var13_9;
                if (var15_16 != null) {
                    var15_16.onSingleTapConfirmed(var1_1);
                    var12_10 = var13_9;
                }
            }
            ** GOTO lbl92
lbl83: // 1 sources:
            var15_17 = this.mVelocityTracker;
            var6_3 = var1_1.getPointerId(0);
            var15_17.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
            var2_6 = var15_17.getYVelocity(var6_3);
            var3_5 = var15_17.getXVelocity(var6_3);
            if (Math.abs(var2_6) > (float)this.mMinimumFlingVelocity) ** GOTO lbl-1000
            var12_10 = var13_9;
            if (Math.abs(var3_5) > (float)this.mMinimumFlingVelocity) lbl-1000: // 2 sources:
            {
                var12_10 = this.mListener.onFling(this.mCurrentDownEvent, var1_1, var3_5, var2_6);
            }
lbl92: // 6 sources:
            var1_1 = this.mPreviousUpEvent;
            if (var1_1 != null) {
                var1_1.recycle();
            }
            this.mPreviousUpEvent = var14_14;
            var1_1 = this.mVelocityTracker;
            if (var1_1 != null) {
                var1_1.recycle();
                this.mVelocityTracker = null;
            }
            this.mIsDoubleTapping = false;
            this.mDeferConfirmSingleTap = false;
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            return var12_10;
lbl105: // 1 sources:
            var6_3 = var7_4;
            if (this.mDoubleTapListener != null) {
                var12_10 = this.mHandler.hasMessages(3);
                if (var12_10) {
                    this.mHandler.removeMessages(3);
                }
                if ((var14_15 = this.mCurrentDownEvent) != null && (var15_18 = this.mPreviousUpEvent) != null && var12_10 && this.isConsideredDoubleTap(var14_15, var15_18, var1_1)) {
                    this.mIsDoubleTapping = true;
                    var6_3 = this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(var1_1);
                } else {
                    this.mHandler.sendEmptyMessageDelayed(3, (long)GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT);
                    var6_3 = var7_4;
                }
            }
            this.mLastFocusX = var3_5;
            this.mDownFocusX = var3_5;
            this.mLastFocusY = var2_6;
            this.mDownFocusY = var2_6;
            var14_15 = this.mCurrentDownEvent;
            if (var14_15 != null) {
                var14_15.recycle();
            }
            this.mCurrentDownEvent = MotionEvent.obtain((MotionEvent)var1_1);
            this.mAlwaysInTapRegion = true;
            this.mAlwaysInBiggerTapRegion = true;
            this.mStillDown = true;
            this.mInLongPress = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mIsLongpressEnabled) {
                this.mHandler.removeMessages(2);
                this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + (long)GestureDetectorCompatImplBase.TAP_TIMEOUT + (long)GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT);
            }
            this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + (long)GestureDetectorCompatImplBase.TAP_TIMEOUT);
            return (boolean)(var6_3 | this.mListener.onDown(var1_1));
lbl134: // 1 sources:
            this.mLastFocusX = var3_5;
            this.mDownFocusX = var3_5;
            this.mLastFocusY = var2_6;
            this.mDownFocusY = var2_6;
            this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
            var8_8 = var1_1.getActionIndex();
            var7_4 = var1_1.getPointerId(var8_8);
            var2_6 = this.mVelocityTracker.getXVelocity(var7_4);
            var3_5 = this.mVelocityTracker.getYVelocity(var7_4);
            var9_2 = 0;
            while (var9_2 < var10_7) {
                if (var9_2 != var8_8 && this.mVelocityTracker.getXVelocity(var11_11 = var1_1.getPointerId(var9_2)) * var2_6 + this.mVelocityTracker.getYVelocity(var11_11) * var3_5 < 0.0f) {
                    this.mVelocityTracker.clear();
                    return false;
                }
                ++var9_2;
            }
            return false;
        }

        @Override
        public void setIsLongpressEnabled(boolean bl) {
            this.mIsLongpressEnabled = bl;
        }

        @Override
        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
            this.mDoubleTapListener = onDoubleTapListener;
        }

        private class GestureHandler
        extends Handler {
            final /* synthetic */ GestureDetectorCompatImplBase this$0;

            GestureHandler(GestureDetectorCompatImplBase gestureDetectorCompatImplBase) {
                this.this$0 = gestureDetectorCompatImplBase;
            }

            GestureHandler(GestureDetectorCompatImplBase gestureDetectorCompatImplBase, Handler handler) {
                this.this$0 = gestureDetectorCompatImplBase;
                super(handler.getLooper());
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public void handleMessage(Message message) {
                int n = message.what;
                if (n != 1) {
                    if (n != 2) {
                        if (n == 3) {
                            if (this.this$0.mDoubleTapListener == null) return;
                            if (!this.this$0.mStillDown) {
                                this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
                                return;
                            }
                            this.this$0.mDeferConfirmSingleTap = true;
                            return;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown message ");
                        stringBuilder.append((Object)message);
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    this.this$0.dispatchLongPress();
                    return;
                }
                this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
            }
        }

    }

    static class GestureDetectorCompatImplJellybeanMr2
    implements GestureDetectorCompatImpl {
        private final GestureDetector mDetector;

        GestureDetectorCompatImplJellybeanMr2(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            this.mDetector = new GestureDetector(context, onGestureListener, handler);
        }

        @Override
        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.mDetector.onTouchEvent(motionEvent);
        }

        @Override
        public void setIsLongpressEnabled(boolean bl) {
            this.mDetector.setIsLongpressEnabled(bl);
        }

        @Override
        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
            this.mDetector.setOnDoubleTapListener(onDoubleTapListener);
        }
    }

}

