/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.PointF
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScrollbarHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

public class LinearLayoutManager
extends RecyclerView.LayoutManager
implements ItemTouchHelper.ViewDropHandler {
    private static final boolean DEBUG = false;
    public static final int HORIZONTAL = 0;
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33f;
    private static final String TAG = "LinearLayoutManager";
    public static final int VERTICAL = 1;
    final AnchorInfo mAnchorInfo;
    private boolean mLastStackFromEnd;
    private LayoutState mLayoutState;
    int mOrientation;
    OrientationHelper mOrientationHelper;
    SavedState mPendingSavedState = null;
    int mPendingScrollPosition = -1;
    int mPendingScrollPositionOffset = Integer.MIN_VALUE;
    private boolean mRecycleChildrenOnDetach;
    private boolean mReverseLayout = false;
    boolean mShouldReverseLayout = false;
    private boolean mSmoothScrollbarEnabled = true;
    private boolean mStackFromEnd = false;

    public LinearLayoutManager(Context context) {
        this(context, 1, false);
    }

    public LinearLayoutManager(Context context, int n, boolean bl) {
        this.mAnchorInfo = new AnchorInfo();
        this.setOrientation(n);
        this.setReverseLayout(bl);
    }

    public LinearLayoutManager(Context object, AttributeSet attributeSet, int n, int n2) {
        this.mAnchorInfo = new AnchorInfo();
        object = LinearLayoutManager.getProperties((Context)object, attributeSet, n, n2);
        this.setOrientation(object.orientation);
        this.setReverseLayout(object.reverseLayout);
        this.setStackFromEnd(object.stackFromEnd);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollExtent(RecyclerView.State state) {
        boolean bl = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        OrientationHelper orientationHelper = this.mOrientationHelper;
        boolean bl2 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleChildClosestToStart(bl2, true);
        bl2 = bl;
        if (!this.mSmoothScrollbarEnabled) {
            bl2 = true;
        }
        return ScrollbarHelper.computeScrollExtent(state, orientationHelper, view, this.findFirstVisibleChildClosestToEnd(bl2, true), this, this.mSmoothScrollbarEnabled);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollOffset(RecyclerView.State state) {
        boolean bl = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        OrientationHelper orientationHelper = this.mOrientationHelper;
        boolean bl2 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleChildClosestToStart(bl2, true);
        bl2 = bl;
        if (!this.mSmoothScrollbarEnabled) {
            bl2 = true;
        }
        return ScrollbarHelper.computeScrollOffset(state, orientationHelper, view, this.findFirstVisibleChildClosestToEnd(bl2, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollRange(RecyclerView.State state) {
        boolean bl = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        OrientationHelper orientationHelper = this.mOrientationHelper;
        boolean bl2 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleChildClosestToStart(bl2, true);
        bl2 = bl;
        if (!this.mSmoothScrollbarEnabled) {
            bl2 = true;
        }
        return ScrollbarHelper.computeScrollRange(state, orientationHelper, view, this.findFirstVisibleChildClosestToEnd(bl2, true), this, this.mSmoothScrollbarEnabled);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int convertFocusDirectionToLayoutDirection(int n) {
        int n2 = -1;
        int n3 = 1;
        int n4 = Integer.MIN_VALUE;
        int n5 = n2;
        switch (n) {
            default: {
                n5 = Integer.MIN_VALUE;
            }
            case 1: {
                return n5;
            }
            case 2: {
                return 1;
            }
            case 33: {
                n5 = n2;
                if (this.mOrientation == 1) return n5;
                return Integer.MIN_VALUE;
            }
            case 130: {
                n = n4;
                if (this.mOrientation != 1) return n;
                return 1;
            }
            case 17: {
                n5 = n2;
                if (this.mOrientation == 0) return n5;
                return Integer.MIN_VALUE;
            }
            case 66: 
        }
        if (this.mOrientation != 0) return Integer.MIN_VALUE;
        return n3;
    }

    private View findFirstReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.findReferenceChild(recycler, state, 0, this.getChildCount(), state.getItemCount());
    }

    private View findFirstVisibleChildClosestToEnd(boolean bl, boolean bl2) {
        if (this.mShouldReverseLayout) {
            return this.findOneVisibleChild(0, this.getChildCount(), bl, bl2);
        }
        return this.findOneVisibleChild(this.getChildCount() - 1, -1, bl, bl2);
    }

    private View findFirstVisibleChildClosestToStart(boolean bl, boolean bl2) {
        if (this.mShouldReverseLayout) {
            return this.findOneVisibleChild(this.getChildCount() - 1, -1, bl, bl2);
        }
        return this.findOneVisibleChild(0, this.getChildCount(), bl, bl2);
    }

    private View findLastReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.findReferenceChild(recycler, state, this.getChildCount() - 1, -1, state.getItemCount());
    }

    private View findReferenceChildClosestToEnd(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mShouldReverseLayout) {
            return this.findFirstReferenceChild(recycler, state);
        }
        return this.findLastReferenceChild(recycler, state);
    }

    private View findReferenceChildClosestToStart(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mShouldReverseLayout) {
            return this.findLastReferenceChild(recycler, state);
        }
        return this.findFirstReferenceChild(recycler, state);
    }

    private int fixLayoutEndGap(int n, RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl) {
        int n2 = this.mOrientationHelper.getEndAfterPadding() - n;
        if (n2 > 0) {
            n2 = - this.scrollBy(- n2, recycler, state);
            if (bl && (n = this.mOrientationHelper.getEndAfterPadding() - (n + n2)) > 0) {
                this.mOrientationHelper.offsetChildren(n);
                return n + n2;
            }
        } else {
            return 0;
        }
        return n2;
    }

    private int fixLayoutStartGap(int n, RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl) {
        int n2 = n - this.mOrientationHelper.getStartAfterPadding();
        if (n2 > 0) {
            n2 = - this.scrollBy(n2, recycler, state);
            if (bl && (n = n + n2 - this.mOrientationHelper.getStartAfterPadding()) > 0) {
                this.mOrientationHelper.offsetChildren(- n);
                return n2 - n;
            }
        } else {
            return 0;
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private View getChildClosestToEnd() {
        int n;
        if (this.mShouldReverseLayout) {
            n = 0;
            do {
                return this.getChildAt(n);
                break;
            } while (true);
        }
        n = this.getChildCount() - 1;
        return this.getChildAt(n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private View getChildClosestToStart() {
        int n;
        if (this.mShouldReverseLayout) {
            n = this.getChildCount() - 1;
            do {
                return this.getChildAt(n);
                break;
            } while (true);
        }
        n = 0;
        return this.getChildAt(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void layoutForPredictiveAnimations(RecyclerView.Recycler recycler, RecyclerView.State state, int n, int n2) {
        if (!state.willRunPredictiveAnimations() || this.getChildCount() == 0 || state.isPreLayout() || !this.supportsPredictiveItemAnimations()) {
            return;
        }
        int n3 = 0;
        int n4 = 0;
        List<RecyclerView.ViewHolder> list = recycler.getScrapList();
        int n5 = list.size();
        int n6 = this.getPosition(this.getChildAt(0));
        for (int i = 0; i < n5; ++i) {
            RecyclerView.ViewHolder viewHolder = list.get(i);
            if (viewHolder.isRemoved()) continue;
            boolean bl = viewHolder.getLayoutPosition() < n6;
            int n7 = bl != this.mShouldReverseLayout ? -1 : 1;
            if (n7 == -1) {
                n3 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                continue;
            }
            n4 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
        }
        this.mLayoutState.mScrapList = list;
        if (n3 > 0) {
            this.updateLayoutStateToFillStart(this.getPosition(this.getChildClosestToStart()), n);
            this.mLayoutState.mExtra = n3;
            this.mLayoutState.mAvailable = 0;
            this.mLayoutState.assignPositionFromScrapList();
            this.fill(recycler, this.mLayoutState, state, false);
        }
        if (n4 > 0) {
            this.updateLayoutStateToFillEnd(this.getPosition(this.getChildClosestToEnd()), n2);
            this.mLayoutState.mExtra = n4;
            this.mLayoutState.mAvailable = 0;
            this.mLayoutState.assignPositionFromScrapList();
            this.fill(recycler, this.mLayoutState, state, false);
        }
        this.mLayoutState.mScrapList = null;
    }

    private void logChildren() {
        Log.d((String)"LinearLayoutManager", (String)"internal representation of views on the screen");
        for (int i = 0; i < this.getChildCount(); ++i) {
            View view = this.getChildAt(i);
            Log.d((String)"LinearLayoutManager", (String)("item " + this.getPosition(view) + ", coord:" + this.mOrientationHelper.getDecoratedStart(view)));
        }
        Log.d((String)"LinearLayoutManager", (String)"==============");
    }

    private void recycleByLayoutState(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (!layoutState.mRecycle) {
            return;
        }
        if (layoutState.mLayoutDirection == -1) {
            this.recycleViewsFromEnd(recycler, layoutState.mScrollingOffset);
            return;
        }
        this.recycleViewsFromStart(recycler, layoutState.mScrollingOffset);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycleChildren(RecyclerView.Recycler recycler, int n, int n2) {
        if (n == n2) return;
        if (n2 > n) {
            --n2;
            while (n2 >= n) {
                this.removeAndRecycleViewAt(n2, recycler);
                --n2;
            }
            return;
        } else {
            while (n > n2) {
                this.removeAndRecycleViewAt(n, recycler);
                --n;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycleViewsFromEnd(RecyclerView.Recycler recycler, int n) {
        int n2 = this.getChildCount();
        if (n >= 0) {
            int n3 = this.mOrientationHelper.getEnd() - n;
            if (!this.mShouldReverseLayout) {
            } else {
                n = 0;
                while (n < n2) {
                    View view = this.getChildAt(n);
                    if (this.mOrientationHelper.getDecoratedStart(view) < n3) {
                        this.recycleChildren(recycler, 0, n);
                        return;
                    }
                    ++n;
                }
                return;
            }
            for (n = n2 - 1; n >= 0; --n) {
                View view = this.getChildAt(n);
                if (this.mOrientationHelper.getDecoratedStart(view) >= n3) continue;
                this.recycleChildren(recycler, n2 - 1, n);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycleViewsFromStart(RecyclerView.Recycler recycler, int n) {
        int n2;
        if (n >= 0) {
            n2 = this.getChildCount();
            if (this.mShouldReverseLayout) {
                for (int i = n2 - 1; i >= 0; --i) {
                    View view = this.getChildAt(i);
                    if (this.mOrientationHelper.getDecoratedEnd(view) <= n) continue;
                    this.recycleChildren(recycler, n2 - 1, i);
                    return;
                }
            }
        } else {
            return;
        }
        int n3 = 0;
        while (n3 < n2) {
            View view = this.getChildAt(n3);
            if (this.mOrientationHelper.getDecoratedEnd(view) > n) {
                this.recycleChildren(recycler, 0, n3);
                return;
            }
            ++n3;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void resolveShouldLayoutReverse() {
        boolean bl = true;
        if (this.mOrientation == 1 || !this.isLayoutRTL()) {
            this.mShouldReverseLayout = this.mReverseLayout;
            return;
        }
        if (this.mReverseLayout) {
            bl = false;
        }
        this.mShouldReverseLayout = bl;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean updateAnchorFromChildren(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo) {
        if (this.getChildCount() == 0) {
            return false;
        }
        View view = this.getFocusedChild();
        if (view != null && anchorInfo.isViewValidAsAnchor(view, state)) {
            anchorInfo.assignFromViewAndKeepVisibleRect(view);
            return true;
        }
        if (this.mLastStackFromEnd != this.mStackFromEnd) return false;
        recycler = anchorInfo.mLayoutFromEnd ? this.findReferenceChildClosestToEnd(recycler, state) : this.findReferenceChildClosestToStart(recycler, state);
        if (recycler == null) return false;
        anchorInfo.assignFromView((View)recycler);
        if (state.isPreLayout()) return true;
        if (!this.supportsPredictiveItemAnimations()) return true;
        if (this.mOrientationHelper.getDecoratedStart((View)recycler) < this.mOrientationHelper.getEndAfterPadding()) {
            if (this.mOrientationHelper.getDecoratedEnd((View)recycler) >= this.mOrientationHelper.getStartAfterPadding()) return true;
        }
        int n = 1;
        if (n == 0) return true;
        n = anchorInfo.mLayoutFromEnd ? this.mOrientationHelper.getEndAfterPadding() : this.mOrientationHelper.getStartAfterPadding();
        anchorInfo.mCoordinate = n;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean updateAnchorFromPendingData(RecyclerView.State state, AnchorInfo anchorInfo) {
        boolean bl = false;
        if (state.isPreLayout()) return false;
        if (this.mPendingScrollPosition == -1) {
            return false;
        }
        if (this.mPendingScrollPosition < 0 || this.mPendingScrollPosition >= state.getItemCount()) {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
        }
        anchorInfo.mPosition = this.mPendingScrollPosition;
        if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
            if (anchorInfo.mLayoutFromEnd) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
                return true;
            }
            anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
            return true;
        }
        if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
            state = this.findViewByPosition(this.mPendingScrollPosition);
            if (state != null) {
                if (this.mOrientationHelper.getDecoratedMeasurement((View)state) > this.mOrientationHelper.getTotalSpace()) {
                    anchorInfo.assignCoordinateFromPadding();
                    return true;
                }
                if (this.mOrientationHelper.getDecoratedStart((View)state) - this.mOrientationHelper.getStartAfterPadding() < 0) {
                    anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                    anchorInfo.mLayoutFromEnd = false;
                    return true;
                }
                if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd((View)state) < 0) {
                    anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                    anchorInfo.mLayoutFromEnd = true;
                    return true;
                }
                int n = anchorInfo.mLayoutFromEnd ? this.mOrientationHelper.getDecoratedEnd((View)state) + this.mOrientationHelper.getTotalSpaceChange() : this.mOrientationHelper.getDecoratedStart((View)state);
                anchorInfo.mCoordinate = n;
                return true;
            }
            if (this.getChildCount() > 0) {
                int n = this.getPosition(this.getChildAt(0));
                boolean bl2 = this.mPendingScrollPosition < n;
                if (bl2 == this.mShouldReverseLayout) {
                    bl = true;
                }
                anchorInfo.mLayoutFromEnd = bl;
            }
            anchorInfo.assignCoordinateFromPadding();
            return true;
        }
        anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        if (this.mShouldReverseLayout) {
            anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
            return true;
        }
        anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateAnchorInfoForLayout(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo) {
        if (this.updateAnchorFromPendingData(state, anchorInfo) || this.updateAnchorFromChildren(recycler, state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        int n = this.mStackFromEnd ? state.getItemCount() - 1 : 0;
        anchorInfo.mPosition = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLayoutState(int n, int n2, boolean bl, RecyclerView.State object) {
        int n3 = -1;
        int n4 = 1;
        this.mLayoutState.mExtra = this.getExtraLayoutSpace((RecyclerView.State)object);
        this.mLayoutState.mLayoutDirection = n;
        if (n == 1) {
            object = this.mLayoutState;
            object.mExtra += this.mOrientationHelper.getEndPadding();
            object = this.getChildClosestToEnd();
            LayoutState layoutState = this.mLayoutState;
            n = this.mShouldReverseLayout ? n3 : 1;
            layoutState.mItemDirection = n;
            this.mLayoutState.mCurrentPosition = this.getPosition((View)object) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd((View)object);
            n = this.mOrientationHelper.getDecoratedEnd((View)object) - this.mOrientationHelper.getEndAfterPadding();
        } else {
            object = this.getChildClosestToStart();
            LayoutState layoutState = this.mLayoutState;
            layoutState.mExtra += this.mOrientationHelper.getStartAfterPadding();
            layoutState = this.mLayoutState;
            n = this.mShouldReverseLayout ? n4 : -1;
            layoutState.mItemDirection = n;
            this.mLayoutState.mCurrentPosition = this.getPosition((View)object) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart((View)object);
            n = - this.mOrientationHelper.getDecoratedStart((View)object) + this.mOrientationHelper.getStartAfterPadding();
        }
        this.mLayoutState.mAvailable = n2;
        if (bl) {
            object = this.mLayoutState;
            object.mAvailable -= n;
        }
        this.mLayoutState.mScrollingOffset = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLayoutStateToFillEnd(int n, int n2) {
        this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - n2;
        LayoutState layoutState = this.mLayoutState;
        int n3 = this.mShouldReverseLayout ? -1 : 1;
        layoutState.mItemDirection = n3;
        this.mLayoutState.mCurrentPosition = n;
        this.mLayoutState.mLayoutDirection = 1;
        this.mLayoutState.mOffset = n2;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    private void updateLayoutStateToFillEnd(AnchorInfo anchorInfo) {
        this.updateLayoutStateToFillEnd(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLayoutStateToFillStart(int n, int n2) {
        this.mLayoutState.mAvailable = n2 - this.mOrientationHelper.getStartAfterPadding();
        this.mLayoutState.mCurrentPosition = n;
        LayoutState layoutState = this.mLayoutState;
        n = this.mShouldReverseLayout ? 1 : -1;
        layoutState.mItemDirection = n;
        this.mLayoutState.mLayoutDirection = -1;
        this.mLayoutState.mOffset = n2;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    private void updateLayoutStateToFillStart(AnchorInfo anchorInfo) {
        this.updateLayoutStateToFillStart(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    @Override
    public void assertNotInLayoutOrScroll(String string2) {
        if (this.mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(string2);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        if (this.mOrientation == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        if (this.mOrientation == 1) {
            return true;
        }
        return false;
    }

    @Override
    public int computeHorizontalScrollExtent(RecyclerView.State state) {
        return this.computeScrollExtent(state);
    }

    @Override
    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return this.computeScrollOffset(state);
    }

    @Override
    public int computeHorizontalScrollRange(RecyclerView.State state) {
        return this.computeScrollRange(state);
    }

    /*
     * Enabled aggressive block sorting
     */
    public PointF computeScrollVectorForPosition(int n) {
        boolean bl = false;
        if (this.getChildCount() == 0) {
            return null;
        }
        if (n < this.getPosition(this.getChildAt(0))) {
            bl = true;
        }
        n = bl != this.mShouldReverseLayout ? -1 : 1;
        if (this.mOrientation == 0) {
            return new PointF((float)n, 0.0f);
        }
        return new PointF(0.0f, (float)n);
    }

    @Override
    public int computeVerticalScrollExtent(RecyclerView.State state) {
        return this.computeScrollExtent(state);
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        return this.computeScrollOffset(state);
    }

    @Override
    public int computeVerticalScrollRange(RecyclerView.State state) {
        return this.computeScrollRange(state);
    }

    LayoutState createLayoutState() {
        return new LayoutState();
    }

    void ensureLayoutState() {
        if (this.mLayoutState == null) {
            this.mLayoutState = this.createLayoutState();
        }
        if (this.mOrientationHelper == null) {
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    int fill(RecyclerView.Recycler var1_1, LayoutState var2_2, RecyclerView.State var3_3, boolean var4_4) {
        var7_5 = var2_2.mAvailable;
        if (var2_2.mScrollingOffset != Integer.MIN_VALUE) {
            if (var2_2.mAvailable < 0) {
                var2_2.mScrollingOffset += var2_2.mAvailable;
            }
            this.recycleByLayoutState(var1_1, var2_2);
        }
        var5_6 = var2_2.mAvailable + var2_2.mExtra;
        var8_7 = new LayoutChunkResult();
        do lbl-1000: // 3 sources:
        {
            if (var5_6 <= 0) return var7_5 - var2_2.mAvailable;
            if (var2_2.hasMore(var3_3) == false) return var7_5 - var2_2.mAvailable;
            var8_7.resetInternal();
            this.layoutChunk(var1_1, var3_3, var2_2, var8_7);
            if (var8_7.mFinished) {
                return var7_5 - var2_2.mAvailable;
            }
            var2_2.mOffset += var8_7.mConsumed * var2_2.mLayoutDirection;
            if (!var8_7.mIgnoreConsumed || this.mLayoutState.mScrapList != null) ** GOTO lbl-1000
            var6_8 = var5_6;
            if (!var3_3.isPreLayout()) lbl-1000: // 2 sources:
            {
                var2_2.mAvailable -= var8_7.mConsumed;
                var6_8 = var5_6 - var8_7.mConsumed;
            }
            if (var2_2.mScrollingOffset != Integer.MIN_VALUE) {
                var2_2.mScrollingOffset += var8_7.mConsumed;
                if (var2_2.mAvailable < 0) {
                    var2_2.mScrollingOffset += var2_2.mAvailable;
                }
                this.recycleByLayoutState(var1_1, var2_2);
            }
            var5_6 = var6_8;
            if (!var4_4) ** GOTO lbl-1000
            var5_6 = var6_8;
        } while (!var8_7.mFocusable);
        return var7_5 - var2_2.mAvailable;
    }

    public int findFirstCompletelyVisibleItemPosition() {
        View view = this.findOneVisibleChild(0, this.getChildCount(), true, false);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    public int findFirstVisibleItemPosition() {
        View view = this.findOneVisibleChild(0, this.getChildCount(), false, true);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    public int findLastCompletelyVisibleItemPosition() {
        View view = this.findOneVisibleChild(this.getChildCount() - 1, -1, true, false);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    public int findLastVisibleItemPosition() {
        View view = this.findOneVisibleChild(this.getChildCount() - 1, -1, false, true);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    View findOneVisibleChild(int n, int n2, boolean bl, boolean bl2) {
        this.ensureLayoutState();
        int n3 = this.mOrientationHelper.getStartAfterPadding();
        int n4 = this.mOrientationHelper.getEndAfterPadding();
        int n5 = n2 > n ? 1 : -1;
        View view = null;
        while (n != n2) {
            View view2 = this.getChildAt(n);
            int n6 = this.mOrientationHelper.getDecoratedStart(view2);
            int n7 = this.mOrientationHelper.getDecoratedEnd(view2);
            View view3 = view;
            if (n6 < n4) {
                view3 = view;
                if (n7 > n3) {
                    if (!bl || n6 >= n3 && n7 <= n4) {
                        return view2;
                    }
                    view3 = view;
                    if (bl2) {
                        view3 = view;
                        if (view == null) {
                            view3 = view2;
                        }
                    }
                }
            }
            n += n5;
            view = view3;
        }
        return view;
    }

    /*
     * Enabled aggressive block sorting
     */
    View findReferenceChild(RecyclerView.Recycler object, RecyclerView.State object2, int n, int n2, int n3) {
        this.ensureLayoutState();
        object2 = null;
        object = null;
        int n4 = this.mOrientationHelper.getStartAfterPadding();
        int n5 = this.mOrientationHelper.getEndAfterPadding();
        int n6 = n2 > n ? 1 : -1;
        while (n != n2) {
            View view = this.getChildAt(n);
            int n7 = this.getPosition(view);
            Object object3 = object2;
            Object object4 = object;
            if (n7 >= 0) {
                object3 = object2;
                object4 = object;
                if (n7 < n3) {
                    if (((RecyclerView.LayoutParams)view.getLayoutParams()).isItemRemoved()) {
                        object3 = object2;
                        object4 = object;
                        if (object2 == null) {
                            object4 = object;
                            object3 = view;
                        }
                    } else {
                        if (this.mOrientationHelper.getDecoratedStart(view) < n5) {
                            object3 = view;
                            if (this.mOrientationHelper.getDecoratedEnd(view) >= n4) return object3;
                        }
                        object3 = object2;
                        object4 = object;
                        if (object == null) {
                            object3 = object2;
                            object4 = view;
                        }
                    }
                }
            }
            n += n6;
            object2 = object3;
            object = object4;
        }
        if (object != null) return object;
        object = object2;
        return object;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public View findViewByPosition(int n) {
        View view;
        int n2 = this.getChildCount();
        if (n2 == 0) {
            return null;
        }
        int n3 = n - this.getPosition(this.getChildAt(0));
        if (n3 < 0) return super.findViewByPosition(n);
        if (n3 >= n2) return super.findViewByPosition(n);
        View view2 = view = this.getChildAt(n3);
        if (this.getPosition(view) == n) return view2;
        return super.findViewByPosition(n);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if (state.hasTargetScrollPosition()) {
            return this.mOrientationHelper.getTotalSpace();
        }
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public boolean getRecycleChildrenOnDetach() {
        return this.mRecycleChildrenOnDetach;
    }

    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }

    public boolean getStackFromEnd() {
        return this.mStackFromEnd;
    }

    protected boolean isLayoutRTL() {
        if (this.getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }

    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    /*
     * Enabled aggressive block sorting
     */
    void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State object, LayoutState layoutState, LayoutChunkResult layoutChunkResult) {
        int n;
        int n2;
        int n3;
        int n4;
        if ((recycler = layoutState.next(recycler)) == null) {
            layoutChunkResult.mFinished = true;
            return;
        }
        object = (RecyclerView.LayoutParams)recycler.getLayoutParams();
        if (layoutState.mScrapList == null) {
            boolean bl = this.mShouldReverseLayout;
            boolean bl2 = layoutState.mLayoutDirection == -1;
            if (bl == bl2) {
                this.addView((View)recycler);
            } else {
                this.addView((View)recycler, 0);
            }
        } else {
            boolean bl = this.mShouldReverseLayout;
            boolean bl3 = layoutState.mLayoutDirection == -1;
            if (bl == bl3) {
                this.addDisappearingView((View)recycler);
            } else {
                this.addDisappearingView((View)recycler, 0);
            }
        }
        this.measureChildWithMargins((View)recycler, 0, 0);
        layoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement((View)recycler);
        if (this.mOrientation == 1) {
            if (this.isLayoutRTL()) {
                n = this.getWidth() - this.getPaddingRight();
                n3 = n - this.mOrientationHelper.getDecoratedMeasurementInOther((View)recycler);
            } else {
                n3 = this.getPaddingLeft();
                n = n3 + this.mOrientationHelper.getDecoratedMeasurementInOther((View)recycler);
            }
            if (layoutState.mLayoutDirection == -1) {
                n4 = layoutState.mOffset;
                n2 = layoutState.mOffset - layoutChunkResult.mConsumed;
            } else {
                n2 = layoutState.mOffset;
                n4 = layoutState.mOffset + layoutChunkResult.mConsumed;
            }
        } else {
            n2 = this.getPaddingTop();
            n4 = n2 + this.mOrientationHelper.getDecoratedMeasurementInOther((View)recycler);
            if (layoutState.mLayoutDirection == -1) {
                n = layoutState.mOffset;
                n3 = layoutState.mOffset - layoutChunkResult.mConsumed;
            } else {
                n3 = layoutState.mOffset;
                n = layoutState.mOffset + layoutChunkResult.mConsumed;
            }
        }
        this.layoutDecorated((View)recycler, n3 + object.leftMargin, n2 + object.topMargin, n - object.rightMargin, n4 - object.bottomMargin);
        if (object.isItemRemoved() || object.isItemChanged()) {
            layoutChunkResult.mIgnoreConsumed = true;
        }
        layoutChunkResult.mFocusable = recycler.isFocusable();
    }

    void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo) {
    }

    @Override
    public void onDetachedFromWindow(RecyclerView recyclerView, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        if (this.mRecycleChildrenOnDetach) {
            this.removeAndRecycleAllViews(recycler);
            recycler.clear();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public View onFocusSearchFailed(View view, int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.resolveShouldLayoutReverse();
        if (this.getChildCount() == 0) {
            return null;
        }
        if ((n = this.convertFocusDirectionToLayoutDirection(n)) == Integer.MIN_VALUE) {
            return null;
        }
        this.ensureLayoutState();
        View view2 = n == -1 ? this.findReferenceChildClosestToStart(recycler, state) : this.findReferenceChildClosestToEnd(recycler, state);
        if (view2 == null) {
            return null;
        }
        this.ensureLayoutState();
        this.updateLayoutState(n, (int)(0.33f * (float)this.mOrientationHelper.getTotalSpace()), false, state);
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        this.mLayoutState.mRecycle = false;
        this.fill(recycler, this.mLayoutState, state, true);
        view = n == -1 ? this.getChildClosestToStart() : this.getChildClosestToEnd();
        if (view == view2) return null;
        recycler = view;
        if (view.isFocusable()) return recycler;
        return null;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent object) {
        super.onInitializeAccessibilityEvent((AccessibilityEvent)object);
        if (this.getChildCount() > 0) {
            object = AccessibilityEventCompat.asRecord((AccessibilityEvent)object);
            object.setFromIndex(this.findFirstVisibleItemPosition());
            object.setToIndex(this.findLastVisibleItemPosition());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int n;
        int n2;
        Object object;
        int n3;
        if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && state.getItemCount() == 0) {
            this.removeAndRecycleAllViews(recycler);
            return;
        }
        if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
        }
        this.ensureLayoutState();
        this.mLayoutState.mRecycle = false;
        this.resolveShouldLayoutReverse();
        this.mAnchorInfo.reset();
        this.mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout ^ this.mStackFromEnd;
        this.updateAnchorInfoForLayout(recycler, state, this.mAnchorInfo);
        int n4 = this.getExtraLayoutSpace(state);
        if (this.mLayoutState.mLastScrollDelta >= 0) {
            n3 = 0;
        } else {
            n3 = n4;
            n4 = 0;
        }
        int n5 = n3 + this.mOrientationHelper.getStartAfterPadding();
        n4 = n = n4 + this.mOrientationHelper.getEndPadding();
        n3 = n5;
        if (state.isPreLayout()) {
            n4 = n;
            n3 = n5;
            if (this.mPendingScrollPosition != -1) {
                n4 = n;
                n3 = n5;
                if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                    object = this.findViewByPosition(this.mPendingScrollPosition);
                    n4 = n;
                    n3 = n5;
                    if (object != null) {
                        if (this.mShouldReverseLayout) {
                            n4 = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd((View)object) - this.mPendingScrollPositionOffset;
                        } else {
                            n4 = this.mOrientationHelper.getDecoratedStart((View)object);
                            n3 = this.mOrientationHelper.getStartAfterPadding();
                            n4 = this.mPendingScrollPositionOffset - (n4 - n3);
                        }
                        if (n4 > 0) {
                            n3 = n5 + n4;
                            n4 = n;
                        } else {
                            n4 = n - n4;
                            n3 = n5;
                        }
                    }
                }
            }
        }
        this.onAnchorReady(recycler, state, this.mAnchorInfo);
        this.detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mIsPreLayout = state.isPreLayout();
        if (this.mAnchorInfo.mLayoutFromEnd) {
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            this.mLayoutState.mExtra = n3;
            this.fill(recycler, this.mLayoutState, state, false);
            n5 = this.mLayoutState.mOffset;
            n2 = this.mLayoutState.mCurrentPosition;
            n3 = n4;
            if (this.mLayoutState.mAvailable > 0) {
                n3 = n4 + this.mLayoutState.mAvailable;
            }
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            this.mLayoutState.mExtra = n3;
            object = this.mLayoutState;
            object.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state, false);
            n4 = n = this.mLayoutState.mOffset;
            n3 = n5;
            if (this.mLayoutState.mAvailable > 0) {
                n4 = this.mLayoutState.mAvailable;
                this.updateLayoutStateToFillStart(n2, n5);
                this.mLayoutState.mExtra = n4;
                this.fill(recycler, this.mLayoutState, state, false);
                n3 = this.mLayoutState.mOffset;
                n4 = n;
            }
        } else {
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            this.mLayoutState.mExtra = n4;
            this.fill(recycler, this.mLayoutState, state, false);
            n5 = this.mLayoutState.mOffset;
            n2 = this.mLayoutState.mCurrentPosition;
            n4 = n3;
            if (this.mLayoutState.mAvailable > 0) {
                n4 = n3 + this.mLayoutState.mAvailable;
            }
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            this.mLayoutState.mExtra = n4;
            object = this.mLayoutState;
            object.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state, false);
            n = this.mLayoutState.mOffset;
            n4 = n5;
            n3 = n;
            if (this.mLayoutState.mAvailable > 0) {
                n4 = this.mLayoutState.mAvailable;
                this.updateLayoutStateToFillEnd(n2, n5);
                this.mLayoutState.mExtra = n4;
                this.fill(recycler, this.mLayoutState, state, false);
                n4 = this.mLayoutState.mOffset;
                n3 = n;
            }
        }
        n5 = n4;
        n = n3;
        if (this.getChildCount() > 0) {
            if (this.mShouldReverseLayout ^ this.mStackFromEnd) {
                n5 = this.fixLayoutEndGap(n4, recycler, state, true);
                n = n3 + n5;
                n3 = this.fixLayoutStartGap(n, recycler, state, false);
                n += n3;
                n5 = n4 + n5 + n3;
            } else {
                n5 = this.fixLayoutStartGap(n3, recycler, state, true);
                n2 = this.fixLayoutEndGap(n4 += n5, recycler, state, false);
                n = n3 + n5 + n2;
                n5 = n4 + n2;
            }
        }
        this.layoutForPredictiveAnimations(recycler, state, n, n5);
        if (!state.isPreLayout()) {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            this.mOrientationHelper.onLayoutComplete();
        }
        this.mLastStackFromEnd = this.mStackFromEnd;
        this.mPendingSavedState = null;
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mPendingSavedState = (SavedState)parcelable;
            this.requestLayout();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) {
            return new SavedState(this.mPendingSavedState);
        }
        SavedState savedState = new SavedState();
        if (this.getChildCount() > 0) {
            boolean bl;
            this.ensureLayoutState();
            savedState.mAnchorLayoutFromEnd = bl = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
            if (bl) {
                View view = this.getChildClosestToEnd();
                savedState.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view);
                savedState.mAnchorPosition = this.getPosition(view);
                return savedState;
            }
            View view = this.getChildClosestToStart();
            savedState.mAnchorPosition = this.getPosition(view);
            savedState.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(view) - this.mOrientationHelper.getStartAfterPadding();
            return savedState;
        }
        savedState.invalidateAnchor();
        return savedState;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void prepareForDrop(View view, View view2, int n, int n2) {
        this.assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
        this.ensureLayoutState();
        this.resolveShouldLayoutReverse();
        n = this.getPosition(view);
        n2 = this.getPosition(view2);
        n = n < n2 ? 1 : -1;
        if (!this.mShouldReverseLayout) {
            this.scrollToPositionWithOffset(n2, this.mOrientationHelper.getDecoratedEnd(view2) - this.mOrientationHelper.getDecoratedMeasurement(view));
            return;
        }
        if (n == 1) {
            this.scrollToPositionWithOffset(n2, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedStart(view2) + this.mOrientationHelper.getDecoratedMeasurement(view)));
            return;
        }
        this.scrollToPositionWithOffset(n2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view2));
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    int scrollBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.getChildCount() == 0) return 0;
        if (n == 0) {
            return 0;
        }
        this.mLayoutState.mRecycle = true;
        this.ensureLayoutState();
        int n2 = n > 0 ? 1 : -1;
        int n3 = Math.abs(n);
        this.updateLayoutState(n2, n3, true, state);
        int n4 = this.mLayoutState.mScrollingOffset + this.fill(recycler, this.mLayoutState, state, false);
        if (n4 < 0) return 0;
        if (n3 > n4) {
            n = n2 * n4;
        }
        this.mOrientationHelper.offsetChildren(- n);
        this.mLayoutState.mLastScrollDelta = n;
        return n;
    }

    @Override
    public int scrollHorizontallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return 0;
        }
        return this.scrollBy(n, recycler, state);
    }

    @Override
    public void scrollToPosition(int n) {
        this.mPendingScrollPosition = n;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchor();
        }
        this.requestLayout();
    }

    public void scrollToPositionWithOffset(int n, int n2) {
        this.mPendingScrollPosition = n;
        this.mPendingScrollPositionOffset = n2;
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchor();
        }
        this.requestLayout();
    }

    @Override
    public int scrollVerticallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return 0;
        }
        return this.scrollBy(n, recycler, state);
    }

    public void setOrientation(int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException("invalid orientation:" + n);
        }
        this.assertNotInLayoutOrScroll(null);
        if (n == this.mOrientation) {
            return;
        }
        this.mOrientation = n;
        this.mOrientationHelper = null;
        this.requestLayout();
    }

    public void setRecycleChildrenOnDetach(boolean bl) {
        this.mRecycleChildrenOnDetach = bl;
    }

    public void setReverseLayout(boolean bl) {
        this.assertNotInLayoutOrScroll(null);
        if (bl == this.mReverseLayout) {
            return;
        }
        this.mReverseLayout = bl;
        this.requestLayout();
    }

    public void setSmoothScrollbarEnabled(boolean bl) {
        this.mSmoothScrollbarEnabled = bl;
    }

    public void setStackFromEnd(boolean bl) {
        this.assertNotInLayoutOrScroll(null);
        if (this.mStackFromEnd == bl) {
            return;
        }
        this.mStackFromEnd = bl;
        this.requestLayout();
    }

    @Override
    public void smoothScrollToPosition(RecyclerView object, RecyclerView.State state, int n) {
        object = new LinearSmoothScroller(object.getContext()){

            @Override
            public PointF computeScrollVectorForPosition(int n) {
                return LinearLayoutManager.this.computeScrollVectorForPosition(n);
            }
        };
        object.setTargetPosition(n);
        this.startSmoothScroll((RecyclerView.SmoothScroller)object);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    void validateChildOrder() {
        boolean bl = true;
        boolean bl2 = true;
        Log.d((String)"LinearLayoutManager", (String)("validating child count " + this.getChildCount()));
        if (this.getChildCount() < 1) {
            return;
        }
        int n = this.getPosition(this.getChildAt(0));
        int n2 = this.mOrientationHelper.getDecoratedStart(this.getChildAt(0));
        if (this.mShouldReverseLayout) {
            int n3 = 1;
            while (n3 < this.getChildCount()) {
                Object object = this.getChildAt(n3);
                int n4 = this.getPosition((View)object);
                int n5 = this.mOrientationHelper.getDecoratedStart((View)object);
                if (n4 < n) {
                    this.logChildren();
                    object = new StringBuilder().append("detected invalid position. loc invalid? ");
                    if (n5 < n2) {
                        do {
                            throw new RuntimeException(object.append(bl2).toString());
                            break;
                        } while (true);
                    }
                    bl2 = false;
                    throw new RuntimeException(object.append(bl2).toString());
                }
                if (n5 > n2) {
                    this.logChildren();
                    throw new RuntimeException("detected invalid location");
                }
                ++n3;
            }
            return;
        }
        int n6 = 1;
        while (n6 < this.getChildCount()) {
            Object object = this.getChildAt(n6);
            int n7 = this.getPosition((View)object);
            int n8 = this.mOrientationHelper.getDecoratedStart((View)object);
            if (n7 < n) {
                this.logChildren();
                object = new StringBuilder().append("detected invalid position. loc invalid? ");
                if (n8 < n2) {
                    bl2 = bl;
                    do {
                        throw new RuntimeException(object.append(bl2).toString());
                        break;
                    } while (true);
                }
                bl2 = false;
                throw new RuntimeException(object.append(bl2).toString());
            }
            if (n8 < n2) {
                this.logChildren();
                throw new RuntimeException("detected invalid location");
            }
            ++n6;
        }
    }

    class AnchorInfo {
        int mCoordinate;
        boolean mLayoutFromEnd;
        int mPosition;

        AnchorInfo() {
        }

        private boolean isViewValidAsAnchor(View object, RecyclerView.State state) {
            if (!(object = (RecyclerView.LayoutParams)object.getLayoutParams()).isItemRemoved() && object.getViewLayoutPosition() >= 0 && object.getViewLayoutPosition() < state.getItemCount()) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        void assignCoordinateFromPadding() {
            int n = this.mLayoutFromEnd ? LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() : LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
            this.mCoordinate = n;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void assignFromView(View view) {
            this.mCoordinate = this.mLayoutFromEnd ? LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view) + LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange() : LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view);
            this.mPosition = LinearLayoutManager.this.getPosition(view);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void assignFromViewAndKeepVisibleRect(View view) {
            int n = LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange();
            if (n >= 0) {
                this.assignFromView(view);
                return;
            } else {
                this.mPosition = LinearLayoutManager.this.getPosition(view);
                if (this.mLayoutFromEnd) {
                    int n2 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - n - LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view);
                    this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - n2;
                    if (n2 <= 0) return;
                    {
                        n = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(view);
                        int n3 = this.mCoordinate;
                        int n4 = LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
                        if ((n = n3 - n - (n4 + Math.min(LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view) - n4, 0))) >= 0) return;
                        {
                            this.mCoordinate += Math.min(n2, - n);
                            return;
                        }
                    }
                } else {
                    int n5 = LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view);
                    int n6 = n5 - LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
                    this.mCoordinate = n5;
                    if (n6 <= 0) return;
                    {
                        int n7 = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(view);
                        int n8 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding();
                        int n9 = LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view);
                        n = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - Math.min(0, n8 - n - n9) - (n5 + n7);
                        if (n >= 0) return;
                        {
                            this.mCoordinate -= Math.min(n6, - n);
                            return;
                        }
                    }
                }
            }
        }

        void reset() {
            this.mPosition = -1;
            this.mCoordinate = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
        }

        public String toString() {
            return "AnchorInfo{mPosition=" + this.mPosition + ", mCoordinate=" + this.mCoordinate + ", mLayoutFromEnd=" + this.mLayoutFromEnd + '}';
        }
    }

    protected static class LayoutChunkResult {
        public int mConsumed;
        public boolean mFinished;
        public boolean mFocusable;
        public boolean mIgnoreConsumed;

        protected LayoutChunkResult() {
        }

        void resetInternal() {
            this.mConsumed = 0;
            this.mFinished = false;
            this.mIgnoreConsumed = false;
            this.mFocusable = false;
        }
    }

    static class LayoutState {
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;
        static final int ITEM_DIRECTION_HEAD = -1;
        static final int ITEM_DIRECTION_TAIL = 1;
        static final int LAYOUT_END = 1;
        static final int LAYOUT_START = -1;
        static final int SCOLLING_OFFSET_NaN = Integer.MIN_VALUE;
        static final String TAG = "LinearLayoutManager#LayoutState";
        int mAvailable;
        int mCurrentPosition;
        int mExtra = 0;
        boolean mIsPreLayout = false;
        int mItemDirection;
        int mLastScrollDelta;
        int mLayoutDirection;
        int mOffset;
        boolean mRecycle = true;
        List<RecyclerView.ViewHolder> mScrapList = null;
        int mScrollingOffset;

        LayoutState() {
        }

        /*
         * Enabled aggressive block sorting
         */
        private View nextViewFromScrapList() {
            int n = this.mScrapList.size();
            int n2 = 0;
            while (n2 < n) {
                View view = this.mScrapList.get((int)n2).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                if (!layoutParams.isItemRemoved() && this.mCurrentPosition == layoutParams.getViewLayoutPosition()) {
                    this.assignPositionFromScrapList(view);
                    return view;
                }
                ++n2;
            }
            return null;
        }

        public void assignPositionFromScrapList() {
            this.assignPositionFromScrapList(null);
        }

        public void assignPositionFromScrapList(View view) {
            if ((view = this.nextViewInLimitedList(view)) == null) {
                this.mCurrentPosition = -1;
                return;
            }
            this.mCurrentPosition = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        }

        boolean hasMore(RecyclerView.State state) {
            if (this.mCurrentPosition >= 0 && this.mCurrentPosition < state.getItemCount()) {
                return true;
            }
            return false;
        }

        void log() {
            Log.d((String)"LinearLayoutManager#LayoutState", (String)("avail:" + this.mAvailable + ", ind:" + this.mCurrentPosition + ", dir:" + this.mItemDirection + ", offset:" + this.mOffset + ", layoutDir:" + this.mLayoutDirection));
        }

        View next(RecyclerView.Recycler recycler) {
            if (this.mScrapList != null) {
                return this.nextViewFromScrapList();
            }
            recycler = recycler.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return recycler;
        }

        /*
         * Enabled aggressive block sorting
         */
        public View nextViewInLimitedList(View view) {
            int n = this.mScrapList.size();
            View view2 = null;
            int n2 = Integer.MAX_VALUE;
            int n3 = 0;
            do {
                View view3 = view2;
                if (n3 >= n) return view3;
                View view4 = this.mScrapList.get((int)n3).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view4.getLayoutParams();
                view3 = view2;
                int n4 = n2;
                if (view4 != view) {
                    if (layoutParams.isItemRemoved()) {
                        n4 = n2;
                        view3 = view2;
                    } else {
                        int n5 = (layoutParams.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
                        view3 = view2;
                        n4 = n2;
                        if (n5 >= 0) {
                            view3 = view2;
                            n4 = n2;
                            if (n5 < n2) {
                                view2 = view4;
                                n4 = n5;
                                view3 = view2;
                                if (n5 == 0) {
                                    return view2;
                                }
                            }
                        }
                    }
                }
                ++n3;
                view2 = view3;
                n2 = n4;
            } while (true);
        }
    }

    public static class SavedState
    implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        boolean mAnchorLayoutFromEnd;
        int mAnchorOffset;
        int mAnchorPosition;

        public SavedState() {
        }

        /*
         * Enabled aggressive block sorting
         */
        SavedState(Parcel parcel) {
            boolean bl = true;
            this.mAnchorPosition = parcel.readInt();
            this.mAnchorOffset = parcel.readInt();
            if (parcel.readInt() != 1) {
                bl = false;
            }
            this.mAnchorLayoutFromEnd = bl;
        }

        public SavedState(SavedState savedState) {
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mAnchorOffset = savedState.mAnchorOffset;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
        }

        public int describeContents() {
            return 0;
        }

        boolean hasValidAnchor() {
            if (this.mAnchorPosition >= 0) {
                return true;
            }
            return false;
        }

        void invalidateAnchor() {
            this.mAnchorPosition = -1;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeInt(this.mAnchorPosition);
            parcel.writeInt(this.mAnchorOffset);
            n = this.mAnchorLayoutFromEnd ? 1 : 0;
            parcel.writeInt(n);
        }

    }

}

