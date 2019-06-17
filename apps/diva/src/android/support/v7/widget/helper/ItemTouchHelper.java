/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.GestureDetector$SimpleOnGestureListener
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewParent
 *  android.view.animation.Interpolator
 */
package android.support.v7.widget.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.animation.AnimatorListenerCompat;
import android.support.v4.animation.AnimatorUpdateListenerCompat;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.support.v7.widget.helper.ItemTouchUIUtilImpl;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper
extends RecyclerView.ItemDecoration
implements RecyclerView.OnChildAttachStateChangeListener {
    private static final int ACTION_MODE_DRAG_MASK = 16711680;
    private static final int ACTION_MODE_IDLE_MASK = 255;
    private static final int ACTION_MODE_SWIPE_MASK = 65280;
    public static final int ACTION_STATE_DRAG = 2;
    public static final int ACTION_STATE_IDLE = 0;
    public static final int ACTION_STATE_SWIPE = 1;
    private static final int ACTIVE_POINTER_ID_NONE = -1;
    public static final int ANIMATION_TYPE_DRAG = 8;
    public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
    public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
    private static final boolean DEBUG = false;
    private static final int DIRECTION_FLAG_COUNT = 8;
    public static final int DOWN = 2;
    public static final int END = 32;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int START = 16;
    private static final String TAG = "ItemTouchHelper";
    public static final int UP = 1;
    int mActionState = 0;
    int mActivePointerId = -1;
    Callback mCallback;
    private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback;
    private List<Integer> mDistances;
    private long mDragScrollStartTimeInMs;
    float mDx;
    float mDy;
    private GestureDetectorCompat mGestureDetector;
    float mInitialTouchX;
    float mInitialTouchY;
    private final RecyclerView.OnItemTouchListener mOnItemTouchListener;
    private View mOverdrawChild;
    private int mOverdrawChildPosition;
    final List<View> mPendingCleanup = new ArrayList<View>();
    List<RecoverAnimation> mRecoverAnimations = new ArrayList<RecoverAnimation>();
    private RecyclerView mRecyclerView;
    private final Runnable mScrollRunnable;
    RecyclerView.ViewHolder mSelected = null;
    int mSelectedFlags;
    float mSelectedStartX;
    float mSelectedStartY;
    private int mSlop;
    private List<RecyclerView.ViewHolder> mSwapTargets;
    private final float[] mTmpPosition = new float[2];
    private Rect mTmpRect;
    private VelocityTracker mVelocityTracker;

    public ItemTouchHelper(Callback callback) {
        this.mScrollRunnable = new Runnable(){

            @Override
            public void run() {
                if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
                    if (ItemTouchHelper.this.mSelected != null) {
                        ItemTouchHelper.this.moveIfNecessary(ItemTouchHelper.this.mSelected);
                    }
                    ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                    ViewCompat.postOnAnimation((View)ItemTouchHelper.this.mRecyclerView, this);
                }
            }
        };
        this.mChildDrawingOrderCallback = null;
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        this.mOnItemTouchListener = new RecyclerView.OnItemTouchListener(){

            /*
             * Enabled aggressive block sorting
             */
            @Override
            public boolean onInterceptTouchEvent(RecyclerView object, MotionEvent motionEvent) {
                int n;
                ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
                int n2 = MotionEventCompat.getActionMasked(motionEvent);
                if (n2 == 0) {
                    ItemTouchHelper.this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                    ItemTouchHelper.this.mInitialTouchX = motionEvent.getX();
                    ItemTouchHelper.this.mInitialTouchY = motionEvent.getY();
                    ItemTouchHelper.this.obtainVelocityTracker();
                    if (ItemTouchHelper.this.mSelected == null && (object = ItemTouchHelper.this.findAnimation(motionEvent)) != null) {
                        ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                        itemTouchHelper.mInitialTouchX -= object.mX;
                        itemTouchHelper = ItemTouchHelper.this;
                        itemTouchHelper.mInitialTouchY -= object.mY;
                        ItemTouchHelper.this.endRecoverAnimation(object.mViewHolder, true);
                        if (ItemTouchHelper.this.mPendingCleanup.remove((Object)object.mViewHolder.itemView)) {
                            ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, object.mViewHolder);
                        }
                        ItemTouchHelper.this.select(object.mViewHolder, object.mActionState);
                        ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, 0);
                    }
                } else if (n2 == 3 || n2 == 1) {
                    ItemTouchHelper.this.mActivePointerId = -1;
                    ItemTouchHelper.this.select(null, 0);
                } else if (ItemTouchHelper.this.mActivePointerId != -1 && (n = MotionEventCompat.findPointerIndex(motionEvent, ItemTouchHelper.this.mActivePointerId)) >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(n2, motionEvent, n);
                }
                if (ItemTouchHelper.this.mVelocityTracker != null) {
                    ItemTouchHelper.this.mVelocityTracker.addMovement(motionEvent);
                }
                if (ItemTouchHelper.this.mSelected != null) {
                    return true;
                }
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean bl) {
                if (!bl) {
                    return;
                }
                ItemTouchHelper.this.select(null, 0);
            }

            /*
             * Enabled aggressive block sorting
             * Lifted jumps to return sites
             */
            @Override
            public void onTouchEvent(RecyclerView object, MotionEvent motionEvent) {
                int n = 0;
                ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
                if (ItemTouchHelper.this.mVelocityTracker != null) {
                    ItemTouchHelper.this.mVelocityTracker.addMovement(motionEvent);
                }
                if (ItemTouchHelper.this.mActivePointerId == -1) {
                    return;
                }
                int n2 = MotionEventCompat.getActionMasked(motionEvent);
                int n3 = MotionEventCompat.findPointerIndex(motionEvent, ItemTouchHelper.this.mActivePointerId);
                if (n3 >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(n2, motionEvent, n3);
                }
                if ((object = ItemTouchHelper.this.mSelected) == null) return;
                switch (n2) {
                    default: {
                        return;
                    }
                    case 1: 
                    case 3: {
                        if (ItemTouchHelper.this.mVelocityTracker != null) {
                            ItemTouchHelper.this.mVelocityTracker.computeCurrentVelocity(1000, (float)ItemTouchHelper.this.mRecyclerView.getMaxFlingVelocity());
                        }
                        ItemTouchHelper.this.select(null, 0);
                        ItemTouchHelper.this.mActivePointerId = -1;
                        return;
                    }
                    case 2: {
                        if (n3 < 0) return;
                        ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, n3);
                        ItemTouchHelper.this.moveIfNecessary((RecyclerView.ViewHolder)object);
                        ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                        ItemTouchHelper.this.mScrollRunnable.run();
                        ItemTouchHelper.this.mRecyclerView.invalidate();
                        return;
                    }
                    case 6: 
                }
                n2 = MotionEventCompat.getActionIndex(motionEvent);
                if (MotionEventCompat.getPointerId(motionEvent, n2) != ItemTouchHelper.this.mActivePointerId) return;
                if (ItemTouchHelper.this.mVelocityTracker != null) {
                    ItemTouchHelper.this.mVelocityTracker.computeCurrentVelocity(1000, (float)ItemTouchHelper.this.mRecyclerView.getMaxFlingVelocity());
                }
                if (n2 == 0) {
                    n = 1;
                }
                ItemTouchHelper.this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, n);
                ItemTouchHelper.this.updateDxDy(motionEvent, ItemTouchHelper.this.mSelectedFlags, n2);
            }
        };
        this.mCallback = callback;
    }

    private void addChildDrawingOrderCallback() {
        if (Build.VERSION.SDK_INT >= 21) {
            return;
        }
        if (this.mChildDrawingOrderCallback == null) {
            this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback(){

                /*
                 * Enabled aggressive block sorting
                 * Lifted jumps to return sites
                 */
                @Override
                public int onGetChildDrawingOrder(int n, int n2) {
                    int n3;
                    if (ItemTouchHelper.this.mOverdrawChild == null) {
                        return n2;
                    }
                    int n4 = n3 = ItemTouchHelper.this.mOverdrawChildPosition;
                    if (n3 == -1) {
                        n4 = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                        ItemTouchHelper.this.mOverdrawChildPosition = n4;
                    }
                    if (n2 == n - 1) {
                        return n4;
                    }
                    if (n2 < n4) return n2;
                    return n2 + 1;
                }
            };
        }
        this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int checkHorizontalSwipe(RecyclerView.ViewHolder viewHolder, int n) {
        if ((n & 12) != 0) {
            float f;
            int n2 = this.mDx > 0.0f ? 8 : 4;
            if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
                f = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId);
                int n3 = f > 0.0f ? 8 : 4;
                if ((n3 & n) != 0 && n2 == n3 && Math.abs(f) >= (float)this.mRecyclerView.getMinFlingVelocity()) {
                    return n3;
                }
            }
            f = this.mRecyclerView.getWidth();
            float f2 = this.mCallback.getSwipeThreshold(viewHolder);
            if ((n & n2) != 0 && Math.abs(this.mDx) > f * f2) {
                return n2;
            }
        }
        return 0;
    }

    private boolean checkSelectForSwipe(int n, MotionEvent motionEvent, int n2) {
        if (this.mSelected != null || n != 2 || this.mActionState == 2 || !this.mCallback.isItemViewSwipeEnabled()) {
            return false;
        }
        if (this.mRecyclerView.getScrollState() == 1) {
            return false;
        }
        RecyclerView.ViewHolder viewHolder = this.findSwipedView(motionEvent);
        if (viewHolder == null) {
            return false;
        }
        n = (65280 & this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolder)) >> 8;
        if (n == 0) {
            return false;
        }
        float f = MotionEventCompat.getX(motionEvent, n2);
        float f2 = MotionEventCompat.getY(motionEvent, n2);
        float f3 = Math.abs(f -= this.mInitialTouchX);
        float f4 = Math.abs(f2 -= this.mInitialTouchY);
        if (f3 < (float)this.mSlop && f4 < (float)this.mSlop) {
            return false;
        }
        if (f3 > f4) {
            if (f < 0.0f && (n & 4) == 0) {
                return false;
            }
            if (f > 0.0f && (n & 8) == 0) {
                return false;
            }
        } else {
            if (f2 < 0.0f && (n & 1) == 0) {
                return false;
            }
            if (f2 > 0.0f && (n & 2) == 0) {
                return false;
            }
        }
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
        this.select(viewHolder, 1);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int checkVerticalSwipe(RecyclerView.ViewHolder viewHolder, int n) {
        if ((n & 3) != 0) {
            float f;
            int n2 = this.mDy > 0.0f ? 2 : 1;
            if (this.mVelocityTracker != null && this.mActivePointerId > -1) {
                f = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId);
                int n3 = f > 0.0f ? 2 : 1;
                if ((n3 & n) != 0 && n3 == n2 && Math.abs(f) >= (float)this.mRecyclerView.getMinFlingVelocity()) {
                    return n3;
                }
            }
            f = this.mRecyclerView.getHeight();
            float f2 = this.mCallback.getSwipeThreshold(viewHolder);
            if ((n & n2) != 0 && Math.abs(this.mDy) > f * f2) {
                return n2;
            }
        }
        return 0;
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(0);
            this.mCallback.clearView(this.mRecyclerView, recoverAnimation.mViewHolder);
        }
        this.mRecoverAnimations.clear();
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        this.releaseVelocityTracker();
    }

    private int endRecoverAnimation(RecyclerView.ViewHolder viewHolder, boolean bl) {
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
            if (recoverAnimation.mViewHolder != viewHolder) continue;
            recoverAnimation.mOverridden |= bl;
            if (!recoverAnimation.mEnded) {
                recoverAnimation.cancel();
            }
            this.mRecoverAnimations.remove(i);
            recoverAnimation.mViewHolder.setIsRecyclable(true);
            return recoverAnimation.mAnimationType;
        }
        return 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private RecoverAnimation findAnimation(MotionEvent object) {
        if (this.mRecoverAnimations.isEmpty()) {
            return null;
        }
        View view = this.findChildView((MotionEvent)object);
        int n = this.mRecoverAnimations.size() - 1;
        while (n >= 0) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(n);
            object = recoverAnimation;
            if (recoverAnimation.mViewHolder.itemView == view) return object;
            --n;
        }
        return null;
    }

    private View findChildView(MotionEvent object) {
        float f = object.getX();
        float f2 = object.getY();
        if (this.mSelected != null && ItemTouchHelper.hitTest((View)(object = this.mSelected.itemView), f, f2, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
            return object;
        }
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            object = this.mRecoverAnimations.get(i);
            View view = object.mViewHolder.itemView;
            if (!ItemTouchHelper.hitTest(view, f, f2, object.mX, object.mY)) continue;
            return view;
        }
        return this.mRecyclerView.findChildViewUnder(f, f2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder viewHolder) {
        if (this.mSwapTargets == null) {
            this.mSwapTargets = new ArrayList<RecyclerView.ViewHolder>();
            this.mDistances = new ArrayList<Integer>();
        } else {
            this.mSwapTargets.clear();
            this.mDistances.clear();
        }
        int n = this.mCallback.getBoundingBoxMargin();
        int n2 = Math.round(this.mSelectedStartX + this.mDx) - n;
        int n3 = Math.round(this.mSelectedStartY + this.mDy) - n;
        int n4 = viewHolder.itemView.getWidth() + n2 + n * 2;
        int n5 = viewHolder.itemView.getHeight() + n3 + n * 2;
        int n6 = (n2 + n4) / 2;
        int n7 = (n3 + n5) / 2;
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        int n8 = layoutManager.getChildCount();
        n = 0;
        while (n < n8) {
            RecyclerView.ViewHolder viewHolder2;
            View view = layoutManager.getChildAt(n);
            if (view != viewHolder.itemView && view.getBottom() >= n3 && view.getTop() <= n5 && view.getRight() >= n2 && view.getLeft() <= n4 && this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, viewHolder2 = this.mRecyclerView.getChildViewHolder(view))) {
                int n9 = Math.abs(n6 - (view.getLeft() + view.getRight()) / 2);
                int n10 = Math.abs(n7 - (view.getTop() + view.getBottom()) / 2);
                int n11 = n9 * n9 + n10 * n10;
                n10 = 0;
                int n12 = this.mSwapTargets.size();
                for (n9 = 0; n9 < n12 && n11 > this.mDistances.get(n9); ++n10, ++n9) {
                }
                this.mSwapTargets.add(n10, viewHolder2);
                this.mDistances.add(n10, n11);
            }
            ++n;
        }
        return this.mSwapTargets;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private RecyclerView.ViewHolder findSwipedView(MotionEvent motionEvent) {
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (this.mActivePointerId == -1) {
            return null;
        }
        int n = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
        float f = MotionEventCompat.getX(motionEvent, n);
        float f2 = this.mInitialTouchX;
        float f3 = MotionEventCompat.getY(motionEvent, n);
        float f4 = this.mInitialTouchY;
        f = Math.abs(f - f2);
        f3 = Math.abs(f3 - f4);
        if (f < (float)this.mSlop) {
            if (f3 < (float)this.mSlop) return null;
        }
        if (f > f3) {
            if (layoutManager.canScrollHorizontally()) return null;
        }
        if (f3 > f) {
            if (layoutManager.canScrollVertically()) return null;
        }
        if ((motionEvent = this.findChildView(motionEvent)) == null) return null;
        return this.mRecyclerView.getChildViewHolder((View)motionEvent);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void getSelectedDxDy(float[] arrf) {
        arrf[0] = (this.mSelectedFlags & 12) != 0 ? this.mSelectedStartX + this.mDx - (float)this.mSelected.itemView.getLeft() : ViewCompat.getTranslationX(this.mSelected.itemView);
        if ((this.mSelectedFlags & 3) != 0) {
            arrf[1] = this.mSelectedStartY + this.mDy - (float)this.mSelected.itemView.getTop();
            return;
        }
        arrf[1] = ViewCompat.getTranslationY(this.mSelected.itemView);
    }

    private boolean hasRunningRecoverAnim() {
        int n = this.mRecoverAnimations.size();
        for (int i = 0; i < n; ++i) {
            if (this.mRecoverAnimations.get(i).mEnded) continue;
            return true;
        }
        return false;
    }

    private static boolean hitTest(View view, float f, float f2, float f3, float f4) {
        if (f >= f3 && f <= (float)view.getWidth() + f3 && f2 >= f4 && f2 <= (float)view.getHeight() + f4) {
            return true;
        }
        return false;
    }

    private void initGestureDetector() {
        if (this.mGestureDetector != null) {
            return;
        }
        this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), (GestureDetector.OnGestureListener)new ItemTouchHelperGestureListener());
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void moveIfNecessary(RecyclerView.ViewHolder viewHolder) {
        Object object;
        if (this.mRecyclerView.isLayoutRequested()) {
            return;
        }
        if (this.mActionState != 2) return;
        float f = this.mCallback.getMoveThreshold(viewHolder);
        int n = (int)(this.mSelectedStartX + this.mDx);
        int n2 = (int)(this.mSelectedStartY + this.mDy);
        if ((float)Math.abs(n2 - viewHolder.itemView.getTop()) < (float)viewHolder.itemView.getHeight() * f) {
            if ((float)Math.abs(n - viewHolder.itemView.getLeft()) < (float)viewHolder.itemView.getWidth() * f) return;
        }
        if ((object = this.findSwapTargets(viewHolder)).size() == 0) return;
        if ((object = this.mCallback.chooseDropTarget(viewHolder, (List<RecyclerView.ViewHolder>)object, n, n2)) == null) {
            this.mSwapTargets.clear();
            this.mDistances.clear();
            return;
        }
        int n3 = object.getAdapterPosition();
        int n4 = viewHolder.getAdapterPosition();
        if (!this.mCallback.onMove(this.mRecyclerView, viewHolder, (RecyclerView.ViewHolder)object)) return;
        this.mCallback.onMoved(this.mRecyclerView, viewHolder, n4, (RecyclerView.ViewHolder)object, n3, n, n2);
    }

    private void obtainVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
        }
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    private void postDispatchSwipe(final RecoverAnimation recoverAnimation, final int n) {
        this.mRecyclerView.post(new Runnable(){

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public void run() {
                if (ItemTouchHelper.this.mRecyclerView == null || !ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() || recoverAnimation.mOverridden || recoverAnimation.mViewHolder.getAdapterPosition() == -1) return;
                RecyclerView.ItemAnimator itemAnimator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
                if (!(itemAnimator != null && itemAnimator.isRunning(null) || ItemTouchHelper.this.hasRunningRecoverAnim())) {
                    ItemTouchHelper.this.mCallback.onSwiped(recoverAnimation.mViewHolder, n);
                    return;
                }
                ItemTouchHelper.this.mRecyclerView.post((Runnable)this);
            }
        });
    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void removeChildDrawingOrderCallbackIfNecessary(View view) {
        if (view == this.mOverdrawChild) {
            this.mOverdrawChild = null;
            if (this.mChildDrawingOrderCallback != null) {
                this.mRecyclerView.setChildDrawingOrderCallback(null);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean scrollIfNecessary() {
        int n;
        if (this.mSelected == null) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        long l = System.currentTimeMillis();
        long l2 = this.mDragScrollStartTimeInMs == Long.MIN_VALUE ? 0 : l - this.mDragScrollStartTimeInMs;
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (this.mTmpRect == null) {
            this.mTmpRect = new Rect();
        }
        int n2 = 0;
        int n3 = 0;
        layoutManager.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
        int n4 = n2;
        if (layoutManager.canScrollHorizontally()) {
            n = (int)(this.mSelectedStartX + this.mDx);
            n4 = n - this.mTmpRect.left - this.mRecyclerView.getPaddingLeft();
            if (this.mDx >= 0.0f || n4 >= 0) {
                n4 = n2;
                if (this.mDx > 0.0f) {
                    n = this.mSelected.itemView.getWidth() + n + this.mTmpRect.right - (this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight());
                    n4 = n2;
                    if (n > 0) {
                        n4 = n;
                    }
                }
            }
        }
        n2 = n3;
        if (layoutManager.canScrollVertically()) {
            n = (int)(this.mSelectedStartY + this.mDy);
            n2 = n - this.mTmpRect.top - this.mRecyclerView.getPaddingTop();
            if (this.mDy >= 0.0f || n2 >= 0) {
                n2 = n3;
                if (this.mDy > 0.0f) {
                    n = this.mSelected.itemView.getHeight() + n + this.mTmpRect.bottom - (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom());
                    n2 = n3;
                    if (n > 0) {
                        n2 = n;
                    }
                }
            }
        }
        n3 = n4;
        if (n4 != 0) {
            n3 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), n4, this.mRecyclerView.getWidth(), l2);
        }
        n4 = n2;
        if (n2 != 0) {
            n4 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), n2, this.mRecyclerView.getHeight(), l2);
        }
        if (n3 == 0 && n4 == 0) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            this.mDragScrollStartTimeInMs = l;
        }
        this.mRecyclerView.scrollBy(n3, n4);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void select(RecyclerView.ViewHolder viewHolder, int n) {
        if (viewHolder == this.mSelected && n == this.mActionState) {
            return;
        }
        this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
        int n2 = this.mActionState;
        this.endRecoverAnimation(viewHolder, true);
        this.mActionState = n;
        if (n == 2) {
            this.mOverdrawChild = viewHolder.itemView;
            this.addChildDrawingOrderCallback();
        }
        int n3 = 0;
        final int n4 = 0;
        if (this.mSelected != null) {
            Object object = this.mSelected;
            if (object.itemView.getParent() != null) {
                float f;
                float f2;
                n4 = n2 == 2 ? 0 : this.swipeIfNecessary((RecyclerView.ViewHolder)object);
                this.releaseVelocityTracker();
                switch (n4) {
                    default: {
                        f2 = 0.0f;
                        f = 0.0f;
                        break;
                    }
                    case 4: 
                    case 8: 
                    case 16: 
                    case 32: {
                        f = 0.0f;
                        f2 = Math.signum(this.mDx) * (float)this.mRecyclerView.getWidth();
                        break;
                    }
                    case 1: 
                    case 2: {
                        f2 = 0.0f;
                        f = Math.signum(this.mDy) * (float)this.mRecyclerView.getHeight();
                    }
                }
                n3 = n2 == 2 ? 8 : (n4 > 0 ? 2 : 4);
                this.getSelectedDxDy(this.mTmpPosition);
                float f3 = this.mTmpPosition[0];
                float f4 = this.mTmpPosition[1];
                object = new RecoverAnimation((RecyclerView.ViewHolder)object, n3, n2, f3, f4, f2, f, (RecyclerView.ViewHolder)object){
                    final /* synthetic */ RecyclerView.ViewHolder val$prevSelected;

                    /*
                     * Enabled aggressive block sorting
                     * Lifted jumps to return sites
                     */
                    @Override
                    public void onAnimationEnd(ValueAnimatorCompat valueAnimatorCompat) {
                        super.onAnimationEnd(valueAnimatorCompat);
                        if (this.mOverridden) {
                            return;
                        }
                        if (n4 <= 0) {
                            ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, this.val$prevSelected);
                        } else {
                            ItemTouchHelper.this.mPendingCleanup.add(this.val$prevSelected.itemView);
                            this.mIsPendingCleanup = true;
                            if (n4 > 0) {
                                ItemTouchHelper.this.postDispatchSwipe(this, n4);
                            }
                        }
                        if (ItemTouchHelper.this.mOverdrawChild != this.val$prevSelected.itemView) return;
                        ItemTouchHelper.this.removeChildDrawingOrderCallbackIfNecessary(this.val$prevSelected.itemView);
                    }
                };
                object.setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, n3, f2 - f3, f - f4));
                this.mRecoverAnimations.add((RecoverAnimation)object);
                object.start();
                n3 = 1;
            } else {
                this.removeChildDrawingOrderCallbackIfNecessary(object.itemView);
                this.mCallback.clearView(this.mRecyclerView, (RecyclerView.ViewHolder)object);
                n3 = n4;
            }
            this.mSelected = null;
        }
        if (viewHolder != null) {
            this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolder) & (1 << n * 8 + 8) - 1) >> this.mActionState * 8;
            this.mSelectedStartX = viewHolder.itemView.getLeft();
            this.mSelectedStartY = viewHolder.itemView.getTop();
            this.mSelected = viewHolder;
            if (n == 2) {
                this.mSelected.itemView.performHapticFeedback(0);
            }
        }
        if ((viewHolder = this.mRecyclerView.getParent()) != null) {
            boolean bl = this.mSelected != null;
            viewHolder.requestDisallowInterceptTouchEvent(bl);
        }
        if (n3 == 0) {
            this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
        }
        this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
        this.mRecyclerView.invalidate();
    }

    private void setupCallbacks() {
        this.mSlop = ViewConfiguration.get((Context)this.mRecyclerView.getContext()).getScaledTouchSlop();
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this);
        this.initGestureDetector();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int swipeIfNecessary(RecyclerView.ViewHolder viewHolder) {
        int n;
        if (this.mActionState == 2) {
            return 0;
        }
        int n2 = this.mCallback.getMovementFlags(this.mRecyclerView, viewHolder);
        int n3 = (this.mCallback.convertToAbsoluteDirection(n2, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) & 65280) >> 8;
        if (n3 == 0) {
            return 0;
        }
        int n4 = (n2 & 65280) >> 8;
        if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
            int n5 = this.checkHorizontalSwipe(viewHolder, n3);
            if (n5 > 0) {
                n2 = n5;
                if ((n4 & n5) != 0) return n2;
                return Callback.convertToRelativeDirection(n5, ViewCompat.getLayoutDirection((View)this.mRecyclerView));
            }
            n2 = n5 = this.checkVerticalSwipe(viewHolder, n3);
            if (n5 > 0) return n2;
            return 0;
        }
        n2 = n = this.checkVerticalSwipe(viewHolder, n3);
        if (n > 0) return n2;
        n = this.checkHorizontalSwipe(viewHolder, n3);
        if (n <= 0) return 0;
        n2 = n;
        if ((n4 & n) != 0) return n2;
        return Callback.convertToRelativeDirection(n, ViewCompat.getLayoutDirection((View)this.mRecyclerView));
    }

    private void updateDxDy(MotionEvent motionEvent, int n, int n2) {
        float f = MotionEventCompat.getX(motionEvent, n2);
        float f2 = MotionEventCompat.getY(motionEvent, n2);
        this.mDx = f - this.mInitialTouchX;
        this.mDy = f2 - this.mInitialTouchY;
        if ((n & 4) == 0) {
            this.mDx = Math.max(0.0f, this.mDx);
        }
        if ((n & 8) == 0) {
            this.mDx = Math.min(0.0f, this.mDx);
        }
        if ((n & 1) == 0) {
            this.mDy = Math.max(0.0f, this.mDy);
        }
        if ((n & 2) == 0) {
            this.mDy = Math.min(0.0f, this.mDy);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void attachToRecyclerView(RecyclerView recyclerView) {
        if (this.mRecyclerView == recyclerView) {
            return;
        }
        if (this.mRecyclerView != null) {
            this.destroyCallbacks();
        }
        this.mRecyclerView = recyclerView;
        if (this.mRecyclerView == null) return;
        this.setupCallbacks();
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        rect.setEmpty();
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void onChildViewDetachedFromWindow(View object) {
        this.removeChildDrawingOrderCallbackIfNecessary((View)object);
        object = this.mRecyclerView.getChildViewHolder((View)object);
        if (object == null) {
            return;
        }
        if (this.mSelected != null && object == this.mSelected) {
            this.select(null, 0);
            return;
        }
        this.endRecoverAnimation((RecyclerView.ViewHolder)object, false);
        if (!this.mPendingCleanup.remove((Object)object.itemView)) return;
        this.mCallback.clearView(this.mRecyclerView, (RecyclerView.ViewHolder)object);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        this.mOverdrawChildPosition = -1;
        float f = 0.0f;
        float f2 = 0.0f;
        if (this.mSelected != null) {
            this.getSelectedDxDy(this.mTmpPosition);
            f = this.mTmpPosition[0];
            f2 = this.mTmpPosition[1];
        }
        this.mCallback.onDraw(canvas, recyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f, f2);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        float f = 0.0f;
        float f2 = 0.0f;
        if (this.mSelected != null) {
            this.getSelectedDxDy(this.mTmpPosition);
            f = this.mTmpPosition[0];
            f2 = this.mTmpPosition[1];
        }
        this.mCallback.onDrawOver(canvas, recyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f, f2);
    }

    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasDragFlag(this.mRecyclerView, viewHolder)) {
            Log.e((String)"ItemTouchHelper", (String)"Start drag has been called but swiping is not enabled");
            return;
        }
        if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e((String)"ItemTouchHelper", (String)"Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
            return;
        }
        this.obtainVelocityTracker();
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        this.select(viewHolder, 2);
    }

    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, viewHolder)) {
            Log.e((String)"ItemTouchHelper", (String)"Start swipe has been called but dragging is not enabled");
            return;
        }
        if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e((String)"ItemTouchHelper", (String)"Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
            return;
        }
        this.obtainVelocityTracker();
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        this.select(viewHolder, 1);
    }

    public static abstract class Callback {
        private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
        public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
        public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
        private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000;
        static final int RELATIVE_DIR_FLAGS = 3158064;
        private static final Interpolator sDragScrollInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return f * f * f * f * f;
            }
        };
        private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return f * f * f * f * (f -= 1.0f) + 1.0f;
            }
        };
        private static final ItemTouchUIUtil sUICallback = Build.VERSION.SDK_INT >= 21 ? new ItemTouchUIUtilImpl.Lollipop() : (Build.VERSION.SDK_INT >= 11 ? new ItemTouchUIUtilImpl.Honeycomb() : new ItemTouchUIUtilImpl.Gingerbread());
        private int mCachedMaxScrollSpeed = -1;

        public static int convertToRelativeDirection(int n, int n2) {
            int n3 = n & 789516;
            if (n3 == 0) {
                return n;
            }
            n &= ~ n3;
            if (n2 == 0) {
                return n | n3 << 2;
            }
            return n | n3 << 1 & -789517 | (n3 << 1 & 789516) << 2;
        }

        public static ItemTouchUIUtil getDefaultUIUtil() {
            return sUICallback;
        }

        private int getMaxDragScroll(RecyclerView recyclerView) {
            if (this.mCachedMaxScrollSpeed == -1) {
                this.mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame);
            }
            return this.mCachedMaxScrollSpeed;
        }

        private boolean hasDragFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if ((16711680 & this.getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0) {
                return true;
            }
            return false;
        }

        private boolean hasSwipeFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if ((65280 & this.getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0) {
                return true;
            }
            return false;
        }

        public static int makeFlag(int n, int n2) {
            return n2 << n * 8;
        }

        public static int makeMovementFlags(int n, int n2) {
            return Callback.makeFlag(0, n2 | n) | Callback.makeFlag(1, n2) | Callback.makeFlag(2, n);
        }

        private void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, List<RecoverAnimation> list, int n, float f, float f2) {
            int n2;
            int n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                RecoverAnimation recoverAnimation = list.get(n2);
                recoverAnimation.update();
                int n4 = canvas.save();
                this.onChildDraw(canvas, recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                canvas.restoreToCount(n4);
            }
            if (viewHolder != null) {
                n2 = canvas.save();
                this.onChildDraw(canvas, recyclerView, viewHolder, f, f2, n, true);
                canvas.restoreToCount(n2);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private void onDrawOver(Canvas object, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, List<RecoverAnimation> list, int n, float f, float f2) {
            int n2;
            int n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                RecoverAnimation recoverAnimation = list.get(n2);
                int n4 = object.save();
                this.onChildDrawOver((Canvas)object, recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                object.restoreToCount(n4);
            }
            if (viewHolder != null) {
                n2 = object.save();
                this.onChildDrawOver((Canvas)object, recyclerView, viewHolder, f, f2, n, true);
                object.restoreToCount(n2);
            }
            n2 = 0;
            for (n = n3 - 1; n >= 0; --n) {
                object = list.get(n);
                if (((RecoverAnimation)object).mEnded && !object.mIsPendingCleanup) {
                    list.remove(n);
                    object.mViewHolder.setIsRecyclable(true);
                    continue;
                }
                if (((RecoverAnimation)object).mEnded) continue;
                n2 = 1;
            }
            if (n2 != 0) {
                recyclerView.invalidate();
            }
        }

        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            return true;
        }

        public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder viewHolder, List<RecyclerView.ViewHolder> list, int n, int n2) {
            int n3 = viewHolder.itemView.getWidth();
            int n4 = viewHolder.itemView.getHeight();
            RecyclerView.ViewHolder viewHolder2 = null;
            int n5 = -1;
            int n6 = n - viewHolder.itemView.getLeft();
            int n7 = n2 - viewHolder.itemView.getTop();
            int n8 = list.size();
            for (int i = 0; i < n8; ++i) {
                int n9;
                RecyclerView.ViewHolder viewHolder3 = list.get(i);
                RecyclerView.ViewHolder viewHolder4 = viewHolder2;
                int n10 = n5;
                if (n6 > 0) {
                    n9 = viewHolder3.itemView.getRight() - (n + n3);
                    viewHolder4 = viewHolder2;
                    n10 = n5;
                    if (n9 < 0) {
                        viewHolder4 = viewHolder2;
                        n10 = n5;
                        if (viewHolder3.itemView.getRight() > viewHolder.itemView.getRight()) {
                            n9 = Math.abs(n9);
                            viewHolder4 = viewHolder2;
                            n10 = n5;
                            if (n9 > n5) {
                                n10 = n9;
                                viewHolder4 = viewHolder3;
                            }
                        }
                    }
                }
                viewHolder2 = viewHolder4;
                n5 = n10;
                if (n6 < 0) {
                    n9 = viewHolder3.itemView.getLeft() - n;
                    viewHolder2 = viewHolder4;
                    n5 = n10;
                    if (n9 > 0) {
                        viewHolder2 = viewHolder4;
                        n5 = n10;
                        if (viewHolder3.itemView.getLeft() < viewHolder.itemView.getLeft()) {
                            n9 = Math.abs(n9);
                            viewHolder2 = viewHolder4;
                            n5 = n10;
                            if (n9 > n10) {
                                n5 = n9;
                                viewHolder2 = viewHolder3;
                            }
                        }
                    }
                }
                viewHolder4 = viewHolder2;
                n10 = n5;
                if (n7 < 0) {
                    n9 = viewHolder3.itemView.getTop() - n2;
                    viewHolder4 = viewHolder2;
                    n10 = n5;
                    if (n9 > 0) {
                        viewHolder4 = viewHolder2;
                        n10 = n5;
                        if (viewHolder3.itemView.getTop() < viewHolder.itemView.getTop()) {
                            n9 = Math.abs(n9);
                            viewHolder4 = viewHolder2;
                            n10 = n5;
                            if (n9 > n5) {
                                n10 = n9;
                                viewHolder4 = viewHolder3;
                            }
                        }
                    }
                }
                viewHolder2 = viewHolder4;
                n5 = n10;
                if (n7 <= 0) continue;
                n9 = viewHolder3.itemView.getBottom() - (n2 + n4);
                viewHolder2 = viewHolder4;
                n5 = n10;
                if (n9 >= 0) continue;
                viewHolder2 = viewHolder4;
                n5 = n10;
                if (viewHolder3.itemView.getBottom() <= viewHolder.itemView.getBottom()) continue;
                n9 = Math.abs(n9);
                viewHolder2 = viewHolder4;
                n5 = n10;
                if (n9 <= n10) continue;
                n5 = n9;
                viewHolder2 = viewHolder3;
            }
            return viewHolder2;
        }

        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            sUICallback.clearView(viewHolder.itemView);
        }

        public int convertToAbsoluteDirection(int n, int n2) {
            int n3 = n & 3158064;
            if (n3 == 0) {
                return n;
            }
            n &= ~ n3;
            if (n2 == 0) {
                return n | n3 >> 2;
            }
            return n | n3 >> 1 & -3158065 | (n3 >> 1 & 3158064) >> 2;
        }

        final int getAbsoluteMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.convertToAbsoluteDirection(this.getMovementFlags(recyclerView, viewHolder), ViewCompat.getLayoutDirection((View)recyclerView));
        }

        public long getAnimationDuration(RecyclerView object, int n, float f, float f2) {
            if ((object = object.getItemAnimator()) == null) {
                if (n == 8) {
                    return 200;
                }
                return 250;
            }
            if (n == 8) {
                return object.getMoveDuration();
            }
            return object.getRemoveDuration();
        }

        public int getBoundingBoxMargin() {
            return 0;
        }

        public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public abstract int getMovementFlags(RecyclerView var1, RecyclerView.ViewHolder var2);

        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        /*
         * Enabled aggressive block sorting
         */
        public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int n, int n2, int n3, long l) {
            n3 = this.getMaxDragScroll(recyclerView);
            int n4 = Math.abs(n2);
            int n5 = (int)Math.signum(n2);
            float f = Math.min(1.0f, 1.0f * (float)n4 / (float)n);
            n = (int)((float)(n5 * n3) * sDragViewScrollCapInterpolator.getInterpolation(f));
            f = l > 2000 ? 1.0f : (float)l / 2000.0f;
            if ((n = (int)((float)n * sDragScrollInterpolator.getInterpolation(f))) != 0) return n;
            if (n2 > 0) {
                return 1;
            }
            return -1;
        }

        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        public boolean isLongPressDragEnabled() {
            return true;
        }

        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int n, boolean bl) {
            sUICallback.onDraw(canvas, recyclerView, viewHolder.itemView, f, f2, n, bl);
        }

        public void onChildDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int n, boolean bl) {
            sUICallback.onDrawOver(canvas, recyclerView, viewHolder.itemView, f, f2, n, bl);
        }

        public abstract boolean onMove(RecyclerView var1, RecyclerView.ViewHolder var2, RecyclerView.ViewHolder var3);

        /*
         * Enabled aggressive block sorting
         */
        public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int n, RecyclerView.ViewHolder viewHolder2, int n2, int n3, int n4) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof ViewDropHandler) {
                ((ViewDropHandler)((Object)layoutManager)).prepareForDrop(viewHolder.itemView, viewHolder2.itemView, n3, n4);
                return;
            } else {
                if (layoutManager.canScrollHorizontally()) {
                    if (layoutManager.getDecoratedLeft(viewHolder2.itemView) <= recyclerView.getPaddingLeft()) {
                        recyclerView.scrollToPosition(n2);
                    }
                    if (layoutManager.getDecoratedRight(viewHolder2.itemView) >= recyclerView.getWidth() - recyclerView.getPaddingRight()) {
                        recyclerView.scrollToPosition(n2);
                    }
                }
                if (!layoutManager.canScrollVertically()) return;
                {
                    if (layoutManager.getDecoratedTop(viewHolder2.itemView) <= recyclerView.getPaddingTop()) {
                        recyclerView.scrollToPosition(n2);
                    }
                    if (layoutManager.getDecoratedBottom(viewHolder2.itemView) < recyclerView.getHeight() - recyclerView.getPaddingBottom()) return;
                    {
                        recyclerView.scrollToPosition(n2);
                        return;
                    }
                }
            }
        }

        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int n) {
            if (viewHolder != null) {
                sUICallback.onSelected(viewHolder.itemView);
            }
        }

        public abstract void onSwiped(RecyclerView.ViewHolder var1, int var2);

    }

    private class ItemTouchHelperGestureListener
    extends GestureDetector.SimpleOnGestureListener {
        private ItemTouchHelperGestureListener() {
        }

        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void onLongPress(MotionEvent object) {
            Object object2 = ItemTouchHelper.this.findChildView((MotionEvent)object);
            if (object2 == null) return;
            object2 = ItemTouchHelper.this.mRecyclerView.getChildViewHolder((View)object2);
            if (object2 == null) return;
            if (!ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, (RecyclerView.ViewHolder)object2)) {
                return;
            }
            if (MotionEventCompat.getPointerId((MotionEvent)object, 0) != ItemTouchHelper.this.mActivePointerId) return;
            int n = MotionEventCompat.findPointerIndex((MotionEvent)object, ItemTouchHelper.this.mActivePointerId);
            float f = MotionEventCompat.getX((MotionEvent)object, n);
            float f2 = MotionEventCompat.getY((MotionEvent)object, n);
            ItemTouchHelper.this.mInitialTouchX = f;
            ItemTouchHelper.this.mInitialTouchY = f2;
            object = ItemTouchHelper.this;
            ItemTouchHelper.this.mDy = 0.0f;
            object.mDx = 0.0f;
            if (!ItemTouchHelper.this.mCallback.isLongPressDragEnabled()) return;
            ItemTouchHelper.this.select((RecyclerView.ViewHolder)object2, 2);
        }
    }

    private class RecoverAnimation
    implements AnimatorListenerCompat {
        final int mActionState;
        private final int mAnimationType;
        private boolean mEnded;
        private float mFraction;
        public boolean mIsPendingCleanup;
        boolean mOverridden;
        final float mStartDx;
        final float mStartDy;
        final float mTargetX;
        final float mTargetY;
        private final ValueAnimatorCompat mValueAnimator;
        final RecyclerView.ViewHolder mViewHolder;
        float mX;
        float mY;

        public RecoverAnimation(RecyclerView.ViewHolder viewHolder, int n, int n2, float f, float f2, float f3, float f4) {
            this.mOverridden = false;
            this.mEnded = false;
            this.mActionState = n2;
            this.mAnimationType = n;
            this.mViewHolder = viewHolder;
            this.mStartDx = f;
            this.mStartDy = f2;
            this.mTargetX = f3;
            this.mTargetY = f4;
            this.mValueAnimator = AnimatorCompatHelper.emptyValueAnimator();
            this.mValueAnimator.addUpdateListener(new AnimatorUpdateListenerCompat(ItemTouchHelper.this){
                final /* synthetic */ ItemTouchHelper val$this$0;

                @Override
                public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                    RecoverAnimation.this.setFraction(valueAnimatorCompat.getAnimatedFraction());
                }
            });
            this.mValueAnimator.setTarget(viewHolder.itemView);
            this.mValueAnimator.addListener(this);
            this.setFraction(0.0f);
        }

        public void cancel() {
            this.mValueAnimator.cancel();
        }

        @Override
        public void onAnimationCancel(ValueAnimatorCompat valueAnimatorCompat) {
            this.setFraction(1.0f);
        }

        @Override
        public void onAnimationEnd(ValueAnimatorCompat valueAnimatorCompat) {
            this.mEnded = true;
        }

        @Override
        public void onAnimationRepeat(ValueAnimatorCompat valueAnimatorCompat) {
        }

        @Override
        public void onAnimationStart(ValueAnimatorCompat valueAnimatorCompat) {
        }

        public void setDuration(long l) {
            this.mValueAnimator.setDuration(l);
        }

        public void setFraction(float f) {
            this.mFraction = f;
        }

        public void start() {
            this.mViewHolder.setIsRecyclable(false);
            this.mValueAnimator.start();
        }

        /*
         * Enabled aggressive block sorting
         */
        public void update() {
            this.mX = this.mStartDx == this.mTargetX ? ViewCompat.getTranslationX(this.mViewHolder.itemView) : this.mStartDx + this.mFraction * (this.mTargetX - this.mStartDx);
            if (this.mStartDy == this.mTargetY) {
                this.mY = ViewCompat.getTranslationY(this.mViewHolder.itemView);
                return;
            }
            this.mY = this.mStartDy + this.mFraction * (this.mTargetY - this.mStartDy);
        }

    }

    public static abstract class SimpleCallback
    extends Callback {
        private int mDefaultDragDirs;
        private int mDefaultSwipeDirs;

        public SimpleCallback(int n, int n2) {
            this.mDefaultSwipeDirs = n2;
            this.mDefaultDragDirs = n;
        }

        public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultDragDirs;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return SimpleCallback.makeMovementFlags(this.getDragDirs(recyclerView, viewHolder), this.getSwipeDirs(recyclerView, viewHolder));
        }

        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultSwipeDirs;
        }

        public void setDefaultDragDirs(int n) {
            this.mDefaultDragDirs = n;
        }

        public void setDefaultSwipeDirs(int n) {
            this.mDefaultSwipeDirs = n;
        }
    }

    public static interface ViewDropHandler {
        public void prepareForDrop(View var1, View var2, int var3, int var4);
    }

}

