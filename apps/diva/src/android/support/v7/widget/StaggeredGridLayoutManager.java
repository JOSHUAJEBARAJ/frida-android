/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.accessibility.AccessibilityEvent
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.widget.LayoutState;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScrollbarHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class StaggeredGridLayoutManager
extends RecyclerView.LayoutManager {
    private static final boolean DEBUG = false;
    @Deprecated
    public static final int GAP_HANDLING_LAZY = 1;
    public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
    public static final int GAP_HANDLING_NONE = 0;
    public static final int HORIZONTAL = 0;
    private static final int INVALID_OFFSET = Integer.MIN_VALUE;
    public static final String TAG = "StaggeredGridLayoutManager";
    public static final int VERTICAL = 1;
    private final AnchorInfo mAnchorInfo;
    private final Runnable mCheckForGapsRunnable;
    private int mFullSizeSpec;
    private int mGapStrategy = 2;
    private int mHeightSpec;
    private boolean mLaidOutInvalidFullSpan;
    private boolean mLastLayoutFromEnd;
    private boolean mLastLayoutRTL;
    private LayoutState mLayoutState;
    LazySpanLookup mLazySpanLookup = new LazySpanLookup();
    private int mOrientation;
    private SavedState mPendingSavedState;
    int mPendingScrollPosition = -1;
    int mPendingScrollPositionOffset = Integer.MIN_VALUE;
    OrientationHelper mPrimaryOrientation;
    private BitSet mRemainingSpans;
    private boolean mReverseLayout = false;
    OrientationHelper mSecondaryOrientation;
    boolean mShouldReverseLayout = false;
    private int mSizePerSpan;
    private boolean mSmoothScrollbarEnabled;
    private int mSpanCount = -1;
    private Span[] mSpans;
    private final Rect mTmpRect = new Rect();
    private int mWidthSpec;

    public StaggeredGridLayoutManager(int n, int n2) {
        this.mAnchorInfo = new AnchorInfo();
        this.mLaidOutInvalidFullSpan = false;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new Runnable(){

            @Override
            public void run() {
                StaggeredGridLayoutManager.this.checkForGaps();
            }
        };
        this.mOrientation = n2;
        this.setSpanCount(n);
    }

    public StaggeredGridLayoutManager(Context object, AttributeSet attributeSet, int n, int n2) {
        this.mAnchorInfo = new AnchorInfo();
        this.mLaidOutInvalidFullSpan = false;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new ;
        object = StaggeredGridLayoutManager.getProperties((Context)object, attributeSet, n, n2);
        this.setOrientation(object.orientation);
        this.setSpanCount(object.spanCount);
        this.setReverseLayout(object.reverseLayout);
    }

    private void appendViewToAllSpans(View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].appendToSpan(view);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void applyPendingSavedState(AnchorInfo anchorInfo) {
        if (this.mPendingSavedState.mSpanOffsetsSize > 0) {
            if (this.mPendingSavedState.mSpanOffsetsSize != this.mSpanCount) {
                this.mPendingSavedState.invalidateSpanInfo();
                this.mPendingSavedState.mAnchorPosition = this.mPendingSavedState.mVisibleAnchorPosition;
            } else {
                for (int i = 0; i < this.mSpanCount; ++i) {
                    int n;
                    this.mSpans[i].clear();
                    int n2 = n = this.mPendingSavedState.mSpanOffsets[i];
                    if (n != Integer.MIN_VALUE) {
                        n2 = this.mPendingSavedState.mAnchorLayoutFromEnd ? n + this.mPrimaryOrientation.getEndAfterPadding() : n + this.mPrimaryOrientation.getStartAfterPadding();
                    }
                    this.mSpans[i].setLine(n2);
                }
            }
        }
        this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
        this.setReverseLayout(this.mPendingSavedState.mReverseLayout);
        this.resolveShouldLayoutReverse();
        if (this.mPendingSavedState.mAnchorPosition != -1) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
        } else {
            anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        }
        if (this.mPendingSavedState.mSpanLookupSize > 1) {
            this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
            this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
        }
    }

    private void attachViewToSpans(View view, LayoutParams layoutParams, LayoutState layoutState) {
        if (layoutState.mLayoutDirection == 1) {
            if (layoutParams.mFullSpan) {
                this.appendViewToAllSpans(view);
                return;
            }
            layoutParams.mSpan.appendToSpan(view);
            return;
        }
        if (layoutParams.mFullSpan) {
            this.prependViewToAllSpans(view);
            return;
        }
        layoutParams.mSpan.prependToSpan(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int calculateScrollDirectionForPosition(int n) {
        int n2 = -1;
        if (this.getChildCount() == 0) {
            if (!this.mShouldReverseLayout) return -1;
            return 1;
        }
        boolean bl = n < this.getFirstChildPosition();
        if (bl == this.mShouldReverseLayout) return 1;
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean checkForGaps() {
        int n;
        int n2;
        if (this.getChildCount() == 0 || this.mGapStrategy == 0 || !this.isAttachedToWindow()) {
            return false;
        }
        if (this.mShouldReverseLayout) {
            n = this.getLastChildPosition();
            n2 = this.getFirstChildPosition();
        } else {
            n = this.getFirstChildPosition();
            n2 = this.getLastChildPosition();
        }
        if (n == 0 && this.hasGapsToFix() != null) {
            this.mLazySpanLookup.clear();
            this.requestSimpleAnimationsInNextLayout();
            this.requestLayout();
            return true;
        }
        if (!this.mLaidOutInvalidFullSpan) {
            return false;
        }
        int n3 = this.mShouldReverseLayout ? -1 : 1;
        LazySpanLookup.FullSpanItem fullSpanItem = this.mLazySpanLookup.getFirstFullSpanItemInRange(n, n2 + 1, n3, true);
        if (fullSpanItem == null) {
            this.mLaidOutInvalidFullSpan = false;
            this.mLazySpanLookup.forceInvalidateAfter(n2 + 1);
            return false;
        }
        LazySpanLookup.FullSpanItem fullSpanItem2 = this.mLazySpanLookup.getFirstFullSpanItemInRange(n, fullSpanItem.mPosition, n3 * -1, true);
        if (fullSpanItem2 == null) {
            this.mLazySpanLookup.forceInvalidateAfter(fullSpanItem.mPosition);
        } else {
            this.mLazySpanLookup.forceInvalidateAfter(fullSpanItem2.mPosition + 1);
        }
        this.requestSimpleAnimationsInNextLayout();
        this.requestLayout();
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean checkSpanForGap(Span span) {
        if (this.mShouldReverseLayout ? span.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding() : span.getStartLine() > this.mPrimaryOrientation.getStartAfterPadding()) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollExtent(RecyclerView.State state) {
        boolean bl = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureOrientationHelper();
        OrientationHelper orientationHelper = this.mPrimaryOrientation;
        boolean bl2 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleItemClosestToStart(bl2, true);
        bl2 = bl;
        if (!this.mSmoothScrollbarEnabled) {
            bl2 = true;
        }
        return ScrollbarHelper.computeScrollExtent(state, orientationHelper, view, this.findFirstVisibleItemClosestToEnd(bl2, true), this, this.mSmoothScrollbarEnabled);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollOffset(RecyclerView.State state) {
        boolean bl = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureOrientationHelper();
        OrientationHelper orientationHelper = this.mPrimaryOrientation;
        boolean bl2 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleItemClosestToStart(bl2, true);
        bl2 = bl;
        if (!this.mSmoothScrollbarEnabled) {
            bl2 = true;
        }
        return ScrollbarHelper.computeScrollOffset(state, orientationHelper, view, this.findFirstVisibleItemClosestToEnd(bl2, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollRange(RecyclerView.State state) {
        boolean bl = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureOrientationHelper();
        OrientationHelper orientationHelper = this.mPrimaryOrientation;
        boolean bl2 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleItemClosestToStart(bl2, true);
        bl2 = bl;
        if (!this.mSmoothScrollbarEnabled) {
            bl2 = true;
        }
        return ScrollbarHelper.computeScrollRange(state, orientationHelper, view, this.findFirstVisibleItemClosestToEnd(bl2, true), this, this.mSmoothScrollbarEnabled);
    }

    private LazySpanLookup.FullSpanItem createFullSpanItemFromEnd(int n) {
        LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = n - this.mSpans[i].getEndLine(n);
        }
        return fullSpanItem;
    }

    private LazySpanLookup.FullSpanItem createFullSpanItemFromStart(int n) {
        LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = this.mSpans[i].getStartLine(n) - n;
        }
        return fullSpanItem;
    }

    private void ensureOrientationHelper() {
        if (this.mPrimaryOrientation == null) {
            this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
            this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
            this.mLayoutState = new LayoutState();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int fill(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state) {
        int n;
        block24 : {
            this.mRemainingSpans.set(0, this.mSpanCount, true);
            int n2 = layoutState.mLayoutDirection == 1 ? layoutState.mEndLine + layoutState.mAvailable : layoutState.mStartLine - layoutState.mAvailable;
            this.updateAllRemainingSpans(layoutState.mLayoutDirection, n2);
            int n3 = this.mShouldReverseLayout ? this.mPrimaryOrientation.getEndAfterPadding() : this.mPrimaryOrientation.getStartAfterPadding();
            n = 0;
            while (layoutState.hasMore(state)) {
                if (!this.mRemainingSpans.isEmpty()) {
                    int n4;
                    Span span;
                    int n5;
                    LazySpanLookup.FullSpanItem fullSpanItem;
                    int n6;
                    View view = layoutState.next(recycler);
                    LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                    int n7 = layoutParams.getViewLayoutPosition();
                    n = this.mLazySpanLookup.getSpan(n7);
                    int n8 = n == -1 ? 1 : 0;
                    if (n8 != 0) {
                        span = layoutParams.mFullSpan ? this.mSpans[0] : this.getNextSpan(layoutState);
                        this.mLazySpanLookup.setSpan(n7, span);
                    } else {
                        span = this.mSpans[n];
                    }
                    layoutParams.mSpan = span;
                    if (layoutState.mLayoutDirection == 1) {
                        this.addView(view);
                    } else {
                        this.addView(view, 0);
                    }
                    this.measureChildWithDecorationsAndMargin(view, layoutParams);
                    if (layoutState.mLayoutDirection == 1) {
                        n = layoutParams.mFullSpan ? this.getMaxEnd(n3) : span.getEndLine(n3);
                        n6 = n + this.mPrimaryOrientation.getDecoratedMeasurement(view);
                        n4 = n;
                        n5 = n6;
                        if (n8 != 0) {
                            n4 = n;
                            n5 = n6;
                            if (layoutParams.mFullSpan) {
                                fullSpanItem = this.createFullSpanItemFromEnd(n);
                                fullSpanItem.mGapDir = -1;
                                fullSpanItem.mPosition = n7;
                                this.mLazySpanLookup.addFullSpanItem(fullSpanItem);
                                n5 = n6;
                                n4 = n;
                            }
                        }
                    } else {
                        n = layoutParams.mFullSpan ? this.getMinStart(n3) : span.getStartLine(n3);
                        n4 = n6 = n - this.mPrimaryOrientation.getDecoratedMeasurement(view);
                        n5 = n;
                        if (n8 != 0) {
                            n4 = n6;
                            n5 = n;
                            if (layoutParams.mFullSpan) {
                                fullSpanItem = this.createFullSpanItemFromStart(n);
                                fullSpanItem.mGapDir = 1;
                                fullSpanItem.mPosition = n7;
                                this.mLazySpanLookup.addFullSpanItem(fullSpanItem);
                                n4 = n6;
                                n5 = n;
                            }
                        }
                    }
                    if (layoutParams.mFullSpan && layoutState.mItemDirection == -1) {
                        if (n8 != 0) {
                            this.mLaidOutInvalidFullSpan = true;
                        } else {
                            n = layoutState.mLayoutDirection == 1 ? (!this.areAllEndsEqual() ? 1 : 0) : (!this.areAllStartsEqual() ? 1 : 0);
                            if (n != 0) {
                                fullSpanItem = this.mLazySpanLookup.getFullSpanItem(n7);
                                if (fullSpanItem != null) {
                                    fullSpanItem.mHasUnwantedGapAfter = true;
                                }
                                this.mLaidOutInvalidFullSpan = true;
                            }
                        }
                    }
                    this.attachViewToSpans(view, layoutParams, layoutState);
                    n = layoutParams.mFullSpan ? this.mSecondaryOrientation.getStartAfterPadding() : span.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
                    n8 = n + this.mSecondaryOrientation.getDecoratedMeasurement(view);
                    if (this.mOrientation == 1) {
                        this.layoutDecoratedWithMargins(view, n, n4, n8, n5);
                    } else {
                        this.layoutDecoratedWithMargins(view, n4, n, n5, n8);
                    }
                    if (layoutParams.mFullSpan) {
                        this.updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, n2);
                    } else {
                        this.updateRemainingSpans(span, this.mLayoutState.mLayoutDirection, n2);
                    }
                    this.recycle(recycler, this.mLayoutState);
                    n = 1;
                    continue;
                }
                if (n != 0) break block24;
            }
            this.recycle(recycler, this.mLayoutState);
        }
        if (this.mLayoutState.mLayoutDirection == -1) {
            n = this.getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
            n = this.mPrimaryOrientation.getStartAfterPadding() - n;
        } else {
            n = this.getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
        }
        if (n > 0) {
            return Math.min(layoutState.mAvailable, n);
        }
        return 0;
    }

    private int findFirstReferenceChildPosition(int n) {
        int n2 = this.getChildCount();
        for (int i = 0; i < n2; ++i) {
            int n3 = this.getPosition(this.getChildAt(i));
            if (n3 < 0 || n3 >= n) continue;
            return n3;
        }
        return 0;
    }

    private int findLastReferenceChildPosition(int n) {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            int n2 = this.getPosition(this.getChildAt(i));
            if (n2 < 0 || n2 >= n) continue;
            return n2;
        }
        return 0;
    }

    private void fixEndGap(RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl) {
        int n = this.getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding());
        n = this.mPrimaryOrientation.getEndAfterPadding() - n;
        if (n > 0) {
            n -= - this.scrollBy(- n, recycler, state);
            if (bl && n > 0) {
                this.mPrimaryOrientation.offsetChildren(n);
            }
        }
    }

    private void fixStartGap(RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl) {
        int n = this.getMinStart(this.mPrimaryOrientation.getStartAfterPadding()) - this.mPrimaryOrientation.getStartAfterPadding();
        if (n > 0) {
            n -= this.scrollBy(n, recycler, state);
            if (bl && n > 0) {
                this.mPrimaryOrientation.offsetChildren(- n);
            }
        }
    }

    private int getFirstChildPosition() {
        if (this.getChildCount() == 0) {
            return 0;
        }
        return this.getPosition(this.getChildAt(0));
    }

    private int getLastChildPosition() {
        int n = this.getChildCount();
        if (n == 0) {
            return 0;
        }
        return this.getPosition(this.getChildAt(n - 1));
    }

    private int getMaxEnd(int n) {
        int n2 = this.mSpans[0].getEndLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            int n3 = this.mSpans[i].getEndLine(n);
            int n4 = n2;
            if (n3 > n2) {
                n4 = n3;
            }
            n2 = n4;
        }
        return n2;
    }

    private int getMaxStart(int n) {
        int n2 = this.mSpans[0].getStartLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            int n3 = this.mSpans[i].getStartLine(n);
            int n4 = n2;
            if (n3 > n2) {
                n4 = n3;
            }
            n2 = n4;
        }
        return n2;
    }

    private int getMinEnd(int n) {
        int n2 = this.mSpans[0].getEndLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            int n3 = this.mSpans[i].getEndLine(n);
            int n4 = n2;
            if (n3 < n2) {
                n4 = n3;
            }
            n2 = n4;
        }
        return n2;
    }

    private int getMinStart(int n) {
        int n2 = this.mSpans[0].getStartLine(n);
        for (int i = 1; i < this.mSpanCount; ++i) {
            int n3 = this.mSpans[i].getStartLine(n);
            int n4 = n2;
            if (n3 < n2) {
                n4 = n3;
            }
            n2 = n4;
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Span getNextSpan(LayoutState object) {
        int n;
        int n2;
        Object object2;
        int n3;
        if (this.preferLastSpan(object.mLayoutDirection)) {
            n2 = this.mSpanCount - 1;
            n3 = -1;
            n = -1;
        } else {
            n2 = 0;
            n3 = this.mSpanCount;
            n = 1;
        }
        if (object.mLayoutDirection == 1) {
            object = null;
            int n4 = Integer.MAX_VALUE;
            int n5 = this.mPrimaryOrientation.getStartAfterPadding();
            do {
                object2 = object;
                if (n2 == n3) return object2;
                object2 = this.mSpans[n2];
                int n6 = object2.getEndLine(n5);
                int n7 = n4;
                if (n6 < n4) {
                    object = object2;
                    n7 = n6;
                }
                n2 += n;
                n4 = n7;
            } while (true);
        }
        object = null;
        int n8 = Integer.MIN_VALUE;
        int n9 = this.mPrimaryOrientation.getEndAfterPadding();
        while (n2 != n3) {
            object2 = this.mSpans[n2];
            int n10 = object2.getStartLine(n9);
            int n11 = n8;
            if (n10 > n8) {
                object = object2;
                n11 = n10;
            }
            n2 += n;
            n8 = n11;
        }
        return object;
    }

    private int getSpecForDimension(int n, int n2) {
        if (n < 0) {
            return n2;
        }
        return View.MeasureSpec.makeMeasureSpec((int)n, (int)1073741824);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void handleUpdate(int n, int n2, int n3) {
        int n4;
        int n5;
        int n6 = this.mShouldReverseLayout ? this.getLastChildPosition() : this.getFirstChildPosition();
        if (n3 == 8) {
            if (n < n2) {
                n5 = n2 + 1;
                n4 = n;
            } else {
                n5 = n + 1;
                n4 = n2;
            }
        } else {
            n4 = n;
            n5 = n + n2;
        }
        this.mLazySpanLookup.invalidateAfter(n4);
        switch (n3) {
            case 1: {
                this.mLazySpanLookup.offsetForAddition(n, n2);
                break;
            }
            case 2: {
                this.mLazySpanLookup.offsetForRemoval(n, n2);
                break;
            }
            case 8: {
                this.mLazySpanLookup.offsetForRemoval(n, 1);
                this.mLazySpanLookup.offsetForAddition(n2, 1);
            }
        }
        if (n5 <= n6) {
            return;
        }
        n = this.mShouldReverseLayout ? this.getFirstChildPosition() : this.getLastChildPosition();
        if (n4 > n) return;
        this.requestLayout();
    }

    private void layoutDecoratedWithMargins(View view, int n, int n2, int n3, int n4) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        this.layoutDecorated(view, n + layoutParams.leftMargin, n2 + layoutParams.topMargin, n3 - layoutParams.rightMargin, n4 - layoutParams.bottomMargin);
    }

    private void measureChildWithDecorationsAndMargin(View view, int n, int n2) {
        this.calculateItemDecorationsForChild(view, this.mTmpRect);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        view.measure(this.updateSpecWithExtra(n, layoutParams.leftMargin + this.mTmpRect.left, layoutParams.rightMargin + this.mTmpRect.right), this.updateSpecWithExtra(n2, layoutParams.topMargin + this.mTmpRect.top, layoutParams.bottomMargin + this.mTmpRect.bottom));
    }

    private void measureChildWithDecorationsAndMargin(View view, LayoutParams layoutParams) {
        if (layoutParams.mFullSpan) {
            if (this.mOrientation == 1) {
                this.measureChildWithDecorationsAndMargin(view, this.mFullSizeSpec, this.getSpecForDimension(layoutParams.height, this.mHeightSpec));
                return;
            }
            this.measureChildWithDecorationsAndMargin(view, this.getSpecForDimension(layoutParams.width, this.mWidthSpec), this.mFullSizeSpec);
            return;
        }
        if (this.mOrientation == 1) {
            this.measureChildWithDecorationsAndMargin(view, this.mWidthSpec, this.getSpecForDimension(layoutParams.height, this.mHeightSpec));
            return;
        }
        this.measureChildWithDecorationsAndMargin(view, this.getSpecForDimension(layoutParams.width, this.mWidthSpec), this.mHeightSpec);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean preferLastSpan(int n) {
        if (this.mOrientation == 0) {
            boolean bl = n == -1;
            if (bl == this.mShouldReverseLayout) return false;
            return true;
        }
        boolean bl = n == -1;
        bl = bl == this.mShouldReverseLayout;
        if (bl == this.isLayoutRTL()) return true;
        return false;
    }

    private void prependViewToAllSpans(View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].prependToSpan(view);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycle(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (layoutState.mAvailable == 0) {
            if (layoutState.mLayoutDirection == -1) {
                this.recycleFromEnd(recycler, layoutState.mEndLine);
                return;
            }
            this.recycleFromStart(recycler, layoutState.mStartLine);
            return;
        }
        if (layoutState.mLayoutDirection == -1) {
            int n = layoutState.mStartLine - this.getMaxStart(layoutState.mStartLine);
            n = n < 0 ? layoutState.mEndLine : layoutState.mEndLine - Math.min(n, layoutState.mAvailable);
            this.recycleFromEnd(recycler, n);
            return;
        }
        int n = this.getMinEnd(layoutState.mEndLine) - layoutState.mEndLine;
        n = n < 0 ? layoutState.mStartLine : layoutState.mStartLine + Math.min(n, layoutState.mAvailable);
        this.recycleFromStart(recycler, n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycleFromEnd(RecyclerView.Recycler recycler, int n) {
        View view;
        block0 : for (int i = this.getChildCount() - 1; i >= 0 && this.mPrimaryOrientation.getDecoratedStart(view = this.getChildAt(i)) >= n; --i) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.mFullSpan) {
                int n2;
                for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                    if (this.mSpans[n2].mViews.size() == 1) break block0;
                }
                for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                    this.mSpans[n2].popEnd();
                }
            } else {
                if (layoutParams.mSpan.mViews.size() == 1) break;
                layoutParams.mSpan.popEnd();
            }
            this.removeAndRecycleView(view, recycler);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycleFromStart(RecyclerView.Recycler recycler, int n) {
        View view;
        block0 : while (this.getChildCount() > 0 && this.mPrimaryOrientation.getDecoratedEnd(view = this.getChildAt(0)) <= n) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.mFullSpan) {
                int n2;
                for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                    if (this.mSpans[n2].mViews.size() == 1) break block0;
                }
                for (n2 = 0; n2 < this.mSpanCount; ++n2) {
                    this.mSpans[n2].popStart();
                }
            } else {
                if (layoutParams.mSpan.mViews.size() == 1) break;
                layoutParams.mSpan.popStart();
            }
            this.removeAndRecycleView(view, recycler);
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
     */
    private void setLayoutStateDirection(int n) {
        int n2 = 1;
        this.mLayoutState.mLayoutDirection = n;
        LayoutState layoutState = this.mLayoutState;
        boolean bl = this.mShouldReverseLayout;
        boolean bl2 = n == -1;
        n = bl == bl2 ? n2 : -1;
        layoutState.mItemDirection = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateAllRemainingSpans(int n, int n2) {
        int n3 = 0;
        while (n3 < this.mSpanCount) {
            if (!this.mSpans[n3].mViews.isEmpty()) {
                this.updateRemainingSpans(this.mSpans[n3], n, n2);
            }
            ++n3;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean updateAnchorFromChildren(RecyclerView.State state, AnchorInfo anchorInfo) {
        int n = this.mLastLayoutFromEnd ? this.findLastReferenceChildPosition(state.getItemCount()) : this.findFirstReferenceChildPosition(state.getItemCount());
        anchorInfo.mPosition = n;
        anchorInfo.mOffset = Integer.MIN_VALUE;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLayoutState(int n, RecyclerView.State state) {
        int n2;
        boolean bl = false;
        this.mLayoutState.mAvailable = 0;
        this.mLayoutState.mCurrentPosition = n;
        int n3 = 0;
        int n4 = n2 = 0;
        int n5 = n3;
        if (this.isSmoothScrolling()) {
            int n6 = state.getTargetScrollPosition();
            n4 = n2;
            n5 = n3;
            if (n6 != -1) {
                boolean bl2 = this.mShouldReverseLayout;
                if (n6 < n) {
                    bl = true;
                }
                if (bl2 == bl) {
                    n4 = this.mPrimaryOrientation.getTotalSpace();
                    n5 = n3;
                } else {
                    n5 = this.mPrimaryOrientation.getTotalSpace();
                    n4 = n2;
                }
            }
        }
        if (this.getClipToPadding()) {
            this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - n5;
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + n4;
            return;
        }
        this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + n4;
        this.mLayoutState.mStartLine = - n5;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateRemainingSpans(Span span, int n, int n2) {
        int n3 = span.getDeletedSize();
        if (n == -1) {
            if (span.getStartLine() + n3 > n2) return;
            {
                this.mRemainingSpans.set(span.mIndex, false);
                return;
            }
        } else {
            if (span.getEndLine() - n3 < n2) return;
            {
                this.mRemainingSpans.set(span.mIndex, false);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int updateSpecWithExtra(int n, int n2, int n3) {
        int n4;
        if (n2 == 0 && n3 == 0 || (n4 = View.MeasureSpec.getMode((int)n)) != Integer.MIN_VALUE && n4 != 1073741824) {
            return n;
        }
        return View.MeasureSpec.makeMeasureSpec((int)Math.max(0, View.MeasureSpec.getSize((int)n) - n2 - n3), (int)n4);
    }

    boolean areAllEndsEqual() {
        int n = this.mSpans[0].getEndLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getEndLine(Integer.MIN_VALUE) == n) continue;
            return false;
        }
        return true;
    }

    boolean areAllStartsEqual() {
        int n = this.mSpans[0].getStartLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getStartLine(Integer.MIN_VALUE) == n) continue;
            return false;
        }
        return true;
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
    public boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
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

    /*
     * Enabled aggressive block sorting
     */
    public int[] findFirstCompletelyVisibleItemPositions(int[] arrn) {
        int[] arrn2;
        if (arrn == null) {
            arrn2 = new int[this.mSpanCount];
        } else {
            arrn2 = arrn;
            if (arrn.length < this.mSpanCount) {
                throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + arrn.length);
            }
        }
        int n = 0;
        while (n < this.mSpanCount) {
            arrn2[n] = this.mSpans[n].findFirstCompletelyVisibleItemPosition();
            ++n;
        }
        return arrn2;
    }

    /*
     * Enabled aggressive block sorting
     */
    View findFirstVisibleItemClosestToEnd(boolean bl, boolean bl2) {
        this.ensureOrientationHelper();
        int n = this.mPrimaryOrientation.getStartAfterPadding();
        int n2 = this.mPrimaryOrientation.getEndAfterPadding();
        View view = null;
        int n3 = this.getChildCount() - 1;
        while (n3 >= 0) {
            View view2 = this.getChildAt(n3);
            int n4 = this.mPrimaryOrientation.getDecoratedStart(view2);
            int n5 = this.mPrimaryOrientation.getDecoratedEnd(view2);
            View view3 = view;
            if (n5 > n) {
                if (n4 >= n2) {
                    view3 = view;
                } else {
                    if (n5 <= n2 || !bl) {
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
            --n3;
            view = view3;
        }
        return view;
    }

    /*
     * Enabled aggressive block sorting
     */
    View findFirstVisibleItemClosestToStart(boolean bl, boolean bl2) {
        this.ensureOrientationHelper();
        int n = this.mPrimaryOrientation.getStartAfterPadding();
        int n2 = this.mPrimaryOrientation.getEndAfterPadding();
        int n3 = this.getChildCount();
        View view = null;
        int n4 = 0;
        while (n4 < n3) {
            View view2 = this.getChildAt(n4);
            int n5 = this.mPrimaryOrientation.getDecoratedStart(view2);
            View view3 = view;
            if (this.mPrimaryOrientation.getDecoratedEnd(view2) > n) {
                if (n5 >= n2) {
                    view3 = view;
                } else {
                    if (n5 >= n || !bl) {
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
            ++n4;
            view = view3;
        }
        return view;
    }

    /*
     * Enabled aggressive block sorting
     */
    int findFirstVisibleItemPositionInt() {
        View view = this.mShouldReverseLayout ? this.findFirstVisibleItemClosestToEnd(true, true) : this.findFirstVisibleItemClosestToStart(true, true);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    public int[] findFirstVisibleItemPositions(int[] arrn) {
        int[] arrn2;
        if (arrn == null) {
            arrn2 = new int[this.mSpanCount];
        } else {
            arrn2 = arrn;
            if (arrn.length < this.mSpanCount) {
                throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + arrn.length);
            }
        }
        int n = 0;
        while (n < this.mSpanCount) {
            arrn2[n] = this.mSpans[n].findFirstVisibleItemPosition();
            ++n;
        }
        return arrn2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int[] findLastCompletelyVisibleItemPositions(int[] arrn) {
        int[] arrn2;
        if (arrn == null) {
            arrn2 = new int[this.mSpanCount];
        } else {
            arrn2 = arrn;
            if (arrn.length < this.mSpanCount) {
                throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + arrn.length);
            }
        }
        int n = 0;
        while (n < this.mSpanCount) {
            arrn2[n] = this.mSpans[n].findLastCompletelyVisibleItemPosition();
            ++n;
        }
        return arrn2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int[] findLastVisibleItemPositions(int[] arrn) {
        int[] arrn2;
        if (arrn == null) {
            arrn2 = new int[this.mSpanCount];
        } else {
            arrn2 = arrn;
            if (arrn.length < this.mSpanCount) {
                throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + arrn.length);
            }
        }
        int n = 0;
        while (n < this.mSpanCount) {
            arrn2[n] = this.mSpans[n].findLastVisibleItemPosition();
            ++n;
        }
        return arrn2;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    @Override
    public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        return super.getColumnCountForAccessibility(recycler, state);
    }

    public int getGapStrategy() {
        return this.mGapStrategy;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }

    @Override
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        return super.getRowCountForAccessibility(recycler, state);
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    /*
     * Enabled aggressive block sorting
     */
    View hasGapsToFix() {
        int n;
        View view;
        int n2;
        int n3 = this.getChildCount() - 1;
        BitSet bitSet = new BitSet(this.mSpanCount);
        bitSet.set(0, this.mSpanCount, true);
        int n4 = this.mOrientation == 1 && this.isLayoutRTL() ? 1 : -1;
        if (this.mShouldReverseLayout) {
            n = 0 - 1;
        } else {
            n2 = 0;
            n = n3 + 1;
            n3 = n2;
        }
        n2 = n3 < n ? 1 : -1;
        int n5 = n3;
        do {
            if (n5 == n) {
                return null;
            }
            view = this.getChildAt(n5);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (bitSet.get(layoutParams.mSpan.mIndex)) {
                if (this.checkSpanForGap(layoutParams.mSpan)) break;
                bitSet.clear(layoutParams.mSpan.mIndex);
            }
            if (!layoutParams.mFullSpan && n5 + n2 != n) {
                int n6;
                int n7;
                Object object = this.getChildAt(n5 + n2);
                n3 = 0;
                if (this.mShouldReverseLayout) {
                    n7 = this.mPrimaryOrientation.getDecoratedEnd(view);
                    if (n7 < (n6 = this.mPrimaryOrientation.getDecoratedEnd((View)object))) break;
                    if (n7 == n6) {
                        n3 = 1;
                    }
                } else {
                    n7 = this.mPrimaryOrientation.getDecoratedStart(view);
                    if (n7 > (n6 = this.mPrimaryOrientation.getDecoratedStart((View)object))) break;
                    if (n7 == n6) {
                        n3 = 1;
                    }
                }
                if (n3 != 0) {
                    object = (LayoutParams)object.getLayoutParams();
                    n3 = layoutParams.mSpan.mIndex - object.mSpan.mIndex < 0 ? 1 : 0;
                    if (n4 >= 0) return view;
                    n7 = 1;
                    if (n3 != n7) {
                        return view;
                    }
                }
            }
            n5 += n2;
        } while (true);
        return view;
    }

    public void invalidateSpanAssignments() {
        this.mLazySpanLookup.clear();
        this.requestLayout();
    }

    boolean isLayoutRTL() {
        if (this.getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void offsetChildrenHorizontal(int n) {
        super.offsetChildrenHorizontal(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }

    @Override
    public void offsetChildrenVertical(int n) {
        super.offsetChildrenVertical(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }

    @Override
    public void onDetachedFromWindow(RecyclerView recyclerView, RecyclerView.Recycler recycler) {
        this.removeCallbacks(this.mCheckForGapsRunnable);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].clear();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent object) {
        int n;
        super.onInitializeAccessibilityEvent((AccessibilityEvent)object);
        if (this.getChildCount() <= 0) return;
        object = AccessibilityEventCompat.asRecord((AccessibilityEvent)object);
        View view = this.findFirstVisibleItemClosestToStart(false, true);
        View view2 = this.findFirstVisibleItemClosestToEnd(false, true);
        if (view == null || view2 == null) {
            return;
        }
        int n2 = this.getPosition(view);
        if (n2 < (n = this.getPosition(view2))) {
            object.setFromIndex(n2);
            object.setToIndex(n);
            return;
        }
        object.setFromIndex(n);
        object.setToIndex(n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler object, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        object = view.getLayoutParams();
        if (!(object instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
            return;
        }
        object = (LayoutParams)((Object)object);
        if (this.mOrientation == 0) {
            int n = object.getSpanIndex();
            int n2 = object.mFullSpan ? this.mSpanCount : 1;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n, n2, -1, -1, object.mFullSpan, false));
            return;
        }
        int n = object.getSpanIndex();
        int n3 = object.mFullSpan ? this.mSpanCount : 1;
        accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, n, n3, object.mFullSpan, false));
    }

    @Override
    public void onItemsAdded(RecyclerView recyclerView, int n, int n2) {
        this.handleUpdate(n, n2, 1);
    }

    @Override
    public void onItemsChanged(RecyclerView recyclerView) {
        this.mLazySpanLookup.clear();
        this.requestLayout();
    }

    @Override
    public void onItemsMoved(RecyclerView recyclerView, int n, int n2, int n3) {
        this.handleUpdate(n, n2, 8);
    }

    @Override
    public void onItemsRemoved(RecyclerView recyclerView, int n, int n2) {
        this.handleUpdate(n, n2, 2);
    }

    @Override
    public void onItemsUpdated(RecyclerView recyclerView, int n, int n2, Object object) {
        this.handleUpdate(n, n2, 4);
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler var1_1, RecyclerView.State var2_2) {
        block16 : {
            block15 : {
                block13 : {
                    block14 : {
                        var4_3 = 1;
                        this.ensureOrientationHelper();
                        var5_4 = this.mAnchorInfo;
                        var5_4.reset();
                        if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && var2_2.getItemCount() == 0) {
                            this.removeAndRecycleAllViews(var1_1);
                            return;
                        }
                        if (this.mPendingSavedState != null) {
                            this.applyPendingSavedState(var5_4);
lbl10: // 2 sources:
                            do {
                                this.updateAnchorInfoForLayout(var2_2, var5_4);
                                if (this.mPendingSavedState == null && (var5_4.mLayoutFromEnd != this.mLastLayoutFromEnd || this.isLayoutRTL() != this.mLastLayoutRTL)) {
                                    this.mLazySpanLookup.clear();
                                    var5_4.mInvalidateOffsets = true;
                                }
                                if (this.getChildCount() <= 0 || this.mPendingSavedState != null && this.mPendingSavedState.mSpanOffsetsSize >= 1) break block13;
                                if (var5_4.mInvalidateOffsets) {
                                    for (var3_5 = 0; var3_5 < this.mSpanCount; ++var3_5) {
                                        this.mSpans[var3_5].clear();
                                        if (var5_4.mOffset == Integer.MIN_VALUE) continue;
                                        this.mSpans[var3_5].setLine(var5_4.mOffset);
                                    }
                                }
                                break block14;
                                break block13;
                                break;
                            } while (true);
                        }
                        this.resolveShouldLayoutReverse();
                        var5_4.mLayoutFromEnd = this.mShouldReverseLayout;
                        ** while (true)
                    }
                    for (var3_5 = 0; var3_5 < this.mSpanCount; ++var3_5) {
                        this.mSpans[var3_5].cacheReferenceLineAndClear(this.mShouldReverseLayout, var5_4.mOffset);
                    }
                }
                this.detachAndScrapAttachedViews(var1_1);
                this.mLaidOutInvalidFullSpan = false;
                this.updateMeasureSpecs();
                this.updateLayoutState(var5_4.mPosition, var2_2);
                if (!var5_4.mLayoutFromEnd) ** GOTO lbl65
                this.setLayoutStateDirection(-1);
                this.fill(var1_1, this.mLayoutState, var2_2);
                this.setLayoutStateDirection(1);
                this.mLayoutState.mCurrentPosition = var5_4.mPosition + this.mLayoutState.mItemDirection;
                this.fill(var1_1, this.mLayoutState, var2_2);
lbl42: // 2 sources:
                do {
                    if (this.getChildCount() > 0) {
                        if (!this.mShouldReverseLayout) break block15;
                        this.fixEndGap(var1_1, var2_2, true);
                        this.fixStartGap(var1_1, var2_2, false);
                    }
lbl47: // 4 sources:
                    do {
                        if (var2_2.isPreLayout()) ** GOTO lbl60
                        if (this.mGapStrategy == 0 || this.getChildCount() <= 0) break block16;
                        var3_5 = var4_3;
                        if (this.mLaidOutInvalidFullSpan) ** GOTO lbl54
                        if (this.hasGapsToFix() != null) {
                            var3_5 = var4_3;
lbl54: // 3 sources:
                            do {
                                if (var3_5 != 0) {
                                    this.removeCallbacks(this.mCheckForGapsRunnable);
                                    this.postOnAnimation(this.mCheckForGapsRunnable);
                                }
                                this.mPendingScrollPosition = -1;
                                this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
lbl60: // 2 sources:
                                this.mLastLayoutFromEnd = var5_4.mLayoutFromEnd;
                                this.mLastLayoutRTL = this.isLayoutRTL();
                                this.mPendingSavedState = null;
                                return;
                                break;
                            } while (true);
                        }
                        break block16;
                        break;
                    } while (true);
                    break;
                } while (true);
lbl65: // 1 sources:
                this.setLayoutStateDirection(1);
                this.fill(var1_1, this.mLayoutState, var2_2);
                this.setLayoutStateDirection(-1);
                this.mLayoutState.mCurrentPosition = var5_4.mPosition + this.mLayoutState.mItemDirection;
                this.fill(var1_1, this.mLayoutState, var2_2);
                ** while (true)
            }
            this.fixStartGap(var1_1, var2_2, true);
            this.fixEndGap(var1_1, var2_2, false);
            ** while (true)
        }
        var3_5 = 0;
        ** while (true)
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mPendingSavedState = (SavedState)parcelable;
            this.requestLayout();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) {
            return new SavedState(this.mPendingSavedState);
        }
        SavedState savedState = new SavedState();
        savedState.mReverseLayout = this.mReverseLayout;
        savedState.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
        savedState.mLastLayoutRTL = this.mLastLayoutRTL;
        if (this.mLazySpanLookup != null && this.mLazySpanLookup.mData != null) {
            savedState.mSpanLookup = this.mLazySpanLookup.mData;
            savedState.mSpanLookupSize = savedState.mSpanLookup.length;
            savedState.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
        } else {
            savedState.mSpanLookupSize = 0;
        }
        if (this.getChildCount() <= 0) {
            savedState.mAnchorPosition = -1;
            savedState.mVisibleAnchorPosition = -1;
            savedState.mSpanOffsetsSize = 0;
            return savedState;
        }
        this.ensureOrientationHelper();
        int n = this.mLastLayoutFromEnd ? this.getLastChildPosition() : this.getFirstChildPosition();
        savedState.mAnchorPosition = n;
        savedState.mVisibleAnchorPosition = this.findFirstVisibleItemPositionInt();
        savedState.mSpanOffsetsSize = this.mSpanCount;
        savedState.mSpanOffsets = new int[this.mSpanCount];
        int n2 = 0;
        do {
            int n3;
            SavedState savedState2 = savedState;
            if (n2 >= this.mSpanCount) return savedState2;
            if (this.mLastLayoutFromEnd) {
                n = n3 = this.mSpans[n2].getEndLine(Integer.MIN_VALUE);
                if (n3 != Integer.MIN_VALUE) {
                    n = n3 - this.mPrimaryOrientation.getEndAfterPadding();
                }
            } else {
                n = n3 = this.mSpans[n2].getStartLine(Integer.MIN_VALUE);
                if (n3 != Integer.MIN_VALUE) {
                    n = n3 - this.mPrimaryOrientation.getStartAfterPadding();
                }
            }
            savedState.mSpanOffsets[n2] = n;
            ++n2;
        } while (true);
    }

    @Override
    public void onScrollStateChanged(int n) {
        if (n == 0) {
            this.checkForGaps();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    int scrollBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int n2;
        int n3;
        this.ensureOrientationHelper();
        if (n > 0) {
            n3 = 1;
            n2 = this.getLastChildPosition();
        } else {
            n3 = -1;
            n2 = this.getFirstChildPosition();
        }
        this.updateLayoutState(n2, state);
        this.setLayoutStateDirection(n3);
        this.mLayoutState.mCurrentPosition = this.mLayoutState.mItemDirection + n2;
        this.mLayoutState.mAvailable = n2 = Math.abs(n);
        n3 = this.fill(recycler, this.mLayoutState, state);
        if (n2 >= n3) {
            n = n < 0 ? - n3 : n3;
        }
        this.mPrimaryOrientation.offsetChildren(- n);
        this.mLastLayoutFromEnd = this.mShouldReverseLayout;
        return n;
    }

    @Override
    public int scrollHorizontallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.scrollBy(n, recycler, state);
    }

    @Override
    public void scrollToPosition(int n) {
        if (this.mPendingSavedState != null && this.mPendingSavedState.mAnchorPosition != n) {
            this.mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = n;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.requestLayout();
    }

    public void scrollToPositionWithOffset(int n, int n2) {
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = n;
        this.mPendingScrollPositionOffset = n2;
        this.requestLayout();
    }

    @Override
    public int scrollVerticallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.scrollBy(n, recycler, state);
    }

    public void setGapStrategy(int n) {
        this.assertNotInLayoutOrScroll(null);
        if (n == this.mGapStrategy) {
            return;
        }
        if (n != 0 && n != 2) {
            throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
        }
        this.mGapStrategy = n;
        this.requestLayout();
    }

    public void setOrientation(int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException("invalid orientation.");
        }
        this.assertNotInLayoutOrScroll(null);
        if (n == this.mOrientation) {
            return;
        }
        this.mOrientation = n;
        if (this.mPrimaryOrientation != null && this.mSecondaryOrientation != null) {
            OrientationHelper orientationHelper = this.mPrimaryOrientation;
            this.mPrimaryOrientation = this.mSecondaryOrientation;
            this.mSecondaryOrientation = orientationHelper;
        }
        this.requestLayout();
    }

    public void setReverseLayout(boolean bl) {
        this.assertNotInLayoutOrScroll(null);
        if (this.mPendingSavedState != null && this.mPendingSavedState.mReverseLayout != bl) {
            this.mPendingSavedState.mReverseLayout = bl;
        }
        this.mReverseLayout = bl;
        this.requestLayout();
    }

    public void setSpanCount(int n) {
        this.assertNotInLayoutOrScroll(null);
        if (n != this.mSpanCount) {
            this.invalidateSpanAssignments();
            this.mSpanCount = n;
            this.mRemainingSpans = new BitSet(this.mSpanCount);
            this.mSpans = new Span[this.mSpanCount];
            for (n = 0; n < this.mSpanCount; ++n) {
                this.mSpans[n] = new Span(n);
            }
            this.requestLayout();
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView object, RecyclerView.State state, int n) {
        object = new LinearSmoothScroller(object.getContext()){

            @Override
            public PointF computeScrollVectorForPosition(int n) {
                if ((n = StaggeredGridLayoutManager.this.calculateScrollDirectionForPosition(n)) == 0) {
                    return null;
                }
                if (StaggeredGridLayoutManager.this.mOrientation == 0) {
                    return new PointF((float)n, 0.0f);
                }
                return new PointF(0.0f, (float)n);
            }
        };
        object.setTargetPosition(n);
        this.startSmoothScroll((RecyclerView.SmoothScroller)object);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState == null) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean updateAnchorFromPendingData(RecyclerView.State state, AnchorInfo anchorInfo) {
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
        if (this.mPendingSavedState != null && this.mPendingSavedState.mAnchorPosition != -1 && this.mPendingSavedState.mSpanOffsetsSize >= 1) {
            anchorInfo.mOffset = Integer.MIN_VALUE;
            anchorInfo.mPosition = this.mPendingScrollPosition;
            return true;
        }
        state = this.findViewByPosition(this.mPendingScrollPosition);
        if (state != null) {
            int n = this.mShouldReverseLayout ? this.getLastChildPosition() : this.getFirstChildPosition();
            anchorInfo.mPosition = n;
            if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                if (anchorInfo.mLayoutFromEnd) {
                    anchorInfo.mOffset = this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd((View)state);
                    return true;
                }
                anchorInfo.mOffset = this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart((View)state);
                return true;
            }
            if (this.mPrimaryOrientation.getDecoratedMeasurement((View)state) > this.mPrimaryOrientation.getTotalSpace()) {
                n = anchorInfo.mLayoutFromEnd ? this.mPrimaryOrientation.getEndAfterPadding() : this.mPrimaryOrientation.getStartAfterPadding();
                anchorInfo.mOffset = n;
                return true;
            }
            n = this.mPrimaryOrientation.getDecoratedStart((View)state) - this.mPrimaryOrientation.getStartAfterPadding();
            if (n < 0) {
                anchorInfo.mOffset = - n;
                return true;
            }
            n = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd((View)state);
            if (n < 0) {
                anchorInfo.mOffset = n;
                return true;
            }
            anchorInfo.mOffset = Integer.MIN_VALUE;
            return true;
        }
        anchorInfo.mPosition = this.mPendingScrollPosition;
        if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
            if (this.calculateScrollDirectionForPosition(anchorInfo.mPosition) == 1) {
                bl = true;
            }
            anchorInfo.mLayoutFromEnd = bl;
            anchorInfo.assignCoordinateFromPadding();
        } else {
            anchorInfo.assignCoordinateFromPadding(this.mPendingScrollPositionOffset);
        }
        anchorInfo.mInvalidateOffsets = true;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    void updateAnchorInfoForLayout(RecyclerView.State state, AnchorInfo anchorInfo) {
        if (this.updateAnchorFromPendingData(state, anchorInfo) || this.updateAnchorFromChildren(state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        anchorInfo.mPosition = 0;
    }

    void updateMeasureSpecs() {
        this.mSizePerSpan = this.mSecondaryOrientation.getTotalSpace() / this.mSpanCount;
        this.mFullSizeSpec = View.MeasureSpec.makeMeasureSpec((int)this.mSecondaryOrientation.getTotalSpace(), (int)1073741824);
        if (this.mOrientation == 1) {
            this.mWidthSpec = View.MeasureSpec.makeMeasureSpec((int)this.mSizePerSpan, (int)1073741824);
            this.mHeightSpec = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
            return;
        }
        this.mHeightSpec = View.MeasureSpec.makeMeasureSpec((int)this.mSizePerSpan, (int)1073741824);
        this.mWidthSpec = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
    }

    private class AnchorInfo {
        boolean mInvalidateOffsets;
        boolean mLayoutFromEnd;
        int mOffset;
        int mPosition;

        private AnchorInfo() {
        }

        /*
         * Enabled aggressive block sorting
         */
        void assignCoordinateFromPadding() {
            int n = this.mLayoutFromEnd ? StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() : StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
            this.mOffset = n;
        }

        void assignCoordinateFromPadding(int n) {
            if (this.mLayoutFromEnd) {
                this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() - n;
                return;
            }
            this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding() + n;
        }

        void reset() {
            this.mPosition = -1;
            this.mOffset = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mInvalidateOffsets = false;
        }
    }

    public static class LayoutParams
    extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        boolean mFullSpan;
        Span mSpan;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public final int getSpanIndex() {
            if (this.mSpan == null) {
                return -1;
            }
            return this.mSpan.mIndex;
        }

        public boolean isFullSpan() {
            return this.mFullSpan;
        }

        public void setFullSpan(boolean bl) {
            this.mFullSpan = bl;
        }
    }

    static class LazySpanLookup {
        private static final int MIN_SIZE = 10;
        int[] mData;
        List<FullSpanItem> mFullSpanItems;

        LazySpanLookup() {
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        private int invalidateFullSpansAfter(int var1_1) {
            if (this.mFullSpanItems == null) {
                return -1;
            }
            var6_2 = this.getFullSpanItem(var1_1);
            if (var6_2 != null) {
                this.mFullSpanItems.remove(var6_2);
            }
            var4_3 = -1;
            var5_4 = this.mFullSpanItems.size();
            var2_5 = 0;
            do {
                var3_6 = var4_3;
                if (var2_5 >= var5_4) ** GOTO lbl14
                if (this.mFullSpanItems.get((int)var2_5).mPosition >= var1_1) {
                    var3_6 = var2_5;
lbl14: // 2 sources:
                    if (var3_6 == -1) return -1;
                    var6_2 = this.mFullSpanItems.get(var3_6);
                    this.mFullSpanItems.remove(var3_6);
                    return var6_2.mPosition;
                }
                ++var2_5;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void offsetFullSpansForAddition(int n, int n2) {
            if (this.mFullSpanItems != null) {
                for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                    FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                    if (fullSpanItem.mPosition < n) continue;
                    fullSpanItem.mPosition += n2;
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private void offsetFullSpansForRemoval(int n, int n2) {
            if (this.mFullSpanItems != null) {
                for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                    FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                    if (fullSpanItem.mPosition < n) continue;
                    if (fullSpanItem.mPosition < n + n2) {
                        this.mFullSpanItems.remove(i);
                        continue;
                    }
                    fullSpanItem.mPosition -= n2;
                }
            }
        }

        public void addFullSpanItem(FullSpanItem fullSpanItem) {
            if (this.mFullSpanItems == null) {
                this.mFullSpanItems = new ArrayList<FullSpanItem>();
            }
            int n = this.mFullSpanItems.size();
            for (int i = 0; i < n; ++i) {
                FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(i);
                if (fullSpanItem2.mPosition == fullSpanItem.mPosition) {
                    this.mFullSpanItems.remove(i);
                }
                if (fullSpanItem2.mPosition < fullSpanItem.mPosition) continue;
                this.mFullSpanItems.add(i, fullSpanItem);
                return;
            }
            this.mFullSpanItems.add(fullSpanItem);
        }

        void clear() {
            if (this.mData != null) {
                Arrays.fill(this.mData, -1);
            }
            this.mFullSpanItems = null;
        }

        /*
         * Enabled aggressive block sorting
         */
        void ensureSize(int n) {
            if (this.mData == null) {
                this.mData = new int[Math.max(n, 10) + 1];
                Arrays.fill(this.mData, -1);
                return;
            } else {
                if (n < this.mData.length) return;
                {
                    int[] arrn = this.mData;
                    this.mData = new int[this.sizeForPosition(n)];
                    System.arraycopy((Object)arrn, 0, (Object)this.mData, 0, arrn.length);
                    Arrays.fill(this.mData, arrn.length, this.mData.length, -1);
                    return;
                }
            }
        }

        int forceInvalidateAfter(int n) {
            if (this.mFullSpanItems != null) {
                for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                    if (this.mFullSpanItems.get((int)i).mPosition < n) continue;
                    this.mFullSpanItems.remove(i);
                }
            }
            return this.invalidateAfter(n);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public FullSpanItem getFirstFullSpanItemInRange(int n, int n2, int n3, boolean bl) {
            if (this.mFullSpanItems == null) {
                return null;
            }
            int n4 = this.mFullSpanItems.size();
            int n5 = 0;
            while (n5 < n4) {
                FullSpanItem fullSpanItem = this.mFullSpanItems.get(n5);
                if (fullSpanItem.mPosition >= n2) {
                    return null;
                }
                if (fullSpanItem.mPosition >= n) {
                    FullSpanItem fullSpanItem2 = fullSpanItem;
                    if (n3 == 0) return fullSpanItem2;
                    fullSpanItem2 = fullSpanItem;
                    if (fullSpanItem.mGapDir == n3) return fullSpanItem2;
                    if (bl) {
                        fullSpanItem2 = fullSpanItem;
                        if (fullSpanItem.mHasUnwantedGapAfter) return fullSpanItem2;
                    }
                }
                ++n5;
            }
            return null;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public FullSpanItem getFullSpanItem(int n) {
            if (this.mFullSpanItems == null) {
                return null;
            }
            int n2 = this.mFullSpanItems.size() - 1;
            while (n2 >= 0) {
                FullSpanItem fullSpanItem;
                FullSpanItem fullSpanItem2 = fullSpanItem = this.mFullSpanItems.get(n2);
                if (fullSpanItem.mPosition == n) return fullSpanItem2;
                --n2;
            }
            return null;
        }

        int getSpan(int n) {
            if (this.mData == null || n >= this.mData.length) {
                return -1;
            }
            return this.mData[n];
        }

        /*
         * Enabled aggressive block sorting
         */
        int invalidateAfter(int n) {
            if (this.mData == null || n >= this.mData.length) {
                return -1;
            }
            int n2 = this.invalidateFullSpansAfter(n);
            if (n2 == -1) {
                Arrays.fill(this.mData, n, this.mData.length, -1);
                return this.mData.length;
            }
            Arrays.fill(this.mData, n, n2 + 1, -1);
            return n2 + 1;
        }

        void offsetForAddition(int n, int n2) {
            if (this.mData == null || n >= this.mData.length) {
                return;
            }
            this.ensureSize(n + n2);
            System.arraycopy((Object)this.mData, n, (Object)this.mData, n + n2, this.mData.length - n - n2);
            Arrays.fill(this.mData, n, n + n2, -1);
            this.offsetFullSpansForAddition(n, n2);
        }

        void offsetForRemoval(int n, int n2) {
            if (this.mData == null || n >= this.mData.length) {
                return;
            }
            this.ensureSize(n + n2);
            System.arraycopy((Object)this.mData, n + n2, (Object)this.mData, n, this.mData.length - n - n2);
            Arrays.fill(this.mData, this.mData.length - n2, this.mData.length, -1);
            this.offsetFullSpansForRemoval(n, n2);
        }

        void setSpan(int n, Span span) {
            this.ensureSize(n);
            this.mData[n] = span.mIndex;
        }

        int sizeForPosition(int n) {
            int n2;
            for (n2 = this.mData.length; n2 <= n; n2 *= 2) {
            }
            return n2;
        }

        static class FullSpanItem
        implements Parcelable {
            public static final Parcelable.Creator<FullSpanItem> CREATOR = new Parcelable.Creator<FullSpanItem>(){

                public FullSpanItem createFromParcel(Parcel parcel) {
                    return new FullSpanItem(parcel);
                }

                public FullSpanItem[] newArray(int n) {
                    return new FullSpanItem[n];
                }
            };
            int mGapDir;
            int[] mGapPerSpan;
            boolean mHasUnwantedGapAfter;
            int mPosition;

            public FullSpanItem() {
            }

            /*
             * Enabled aggressive block sorting
             */
            public FullSpanItem(Parcel parcel) {
                boolean bl = true;
                this.mPosition = parcel.readInt();
                this.mGapDir = parcel.readInt();
                if (parcel.readInt() != 1) {
                    bl = false;
                }
                this.mHasUnwantedGapAfter = bl;
                int n = parcel.readInt();
                if (n > 0) {
                    this.mGapPerSpan = new int[n];
                    parcel.readIntArray(this.mGapPerSpan);
                }
            }

            public int describeContents() {
                return 0;
            }

            int getGapForSpan(int n) {
                if (this.mGapPerSpan == null) {
                    return 0;
                }
                return this.mGapPerSpan[n];
            }

            public void invalidateSpanGaps() {
                this.mGapPerSpan = null;
            }

            public String toString() {
                return "FullSpanItem{mPosition=" + this.mPosition + ", mGapDir=" + this.mGapDir + ", mHasUnwantedGapAfter=" + this.mHasUnwantedGapAfter + ", mGapPerSpan=" + Arrays.toString(this.mGapPerSpan) + '}';
            }

            /*
             * Enabled aggressive block sorting
             */
            public void writeToParcel(Parcel parcel, int n) {
                parcel.writeInt(this.mPosition);
                parcel.writeInt(this.mGapDir);
                n = this.mHasUnwantedGapAfter ? 1 : 0;
                parcel.writeInt(n);
                if (this.mGapPerSpan != null && this.mGapPerSpan.length > 0) {
                    parcel.writeInt(this.mGapPerSpan.length);
                    parcel.writeIntArray(this.mGapPerSpan);
                    return;
                }
                parcel.writeInt(0);
            }

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
        int mAnchorPosition;
        List<LazySpanLookup.FullSpanItem> mFullSpanItems;
        boolean mLastLayoutRTL;
        boolean mReverseLayout;
        int[] mSpanLookup;
        int mSpanLookupSize;
        int[] mSpanOffsets;
        int mSpanOffsetsSize;
        int mVisibleAnchorPosition;

        public SavedState() {
        }

        /*
         * Enabled aggressive block sorting
         */
        SavedState(Parcel parcel) {
            boolean bl = true;
            this.mAnchorPosition = parcel.readInt();
            this.mVisibleAnchorPosition = parcel.readInt();
            this.mSpanOffsetsSize = parcel.readInt();
            if (this.mSpanOffsetsSize > 0) {
                this.mSpanOffsets = new int[this.mSpanOffsetsSize];
                parcel.readIntArray(this.mSpanOffsets);
            }
            this.mSpanLookupSize = parcel.readInt();
            if (this.mSpanLookupSize > 0) {
                this.mSpanLookup = new int[this.mSpanLookupSize];
                parcel.readIntArray(this.mSpanLookup);
            }
            boolean bl2 = parcel.readInt() == 1;
            this.mReverseLayout = bl2;
            bl2 = parcel.readInt() == 1;
            this.mAnchorLayoutFromEnd = bl2;
            bl2 = parcel.readInt() == 1 ? bl : false;
            this.mLastLayoutRTL = bl2;
            this.mFullSpanItems = parcel.readArrayList(LazySpanLookup.FullSpanItem.class.getClassLoader());
        }

        public SavedState(SavedState savedState) {
            this.mSpanOffsetsSize = savedState.mSpanOffsetsSize;
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mVisibleAnchorPosition = savedState.mVisibleAnchorPosition;
            this.mSpanOffsets = savedState.mSpanOffsets;
            this.mSpanLookupSize = savedState.mSpanLookupSize;
            this.mSpanLookup = savedState.mSpanLookup;
            this.mReverseLayout = savedState.mReverseLayout;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
            this.mLastLayoutRTL = savedState.mLastLayoutRTL;
            this.mFullSpanItems = savedState.mFullSpanItems;
        }

        public int describeContents() {
            return 0;
        }

        void invalidateAnchorPositionInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mAnchorPosition = -1;
            this.mVisibleAnchorPosition = -1;
        }

        void invalidateSpanInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mSpanLookupSize = 0;
            this.mSpanLookup = null;
            this.mFullSpanItems = null;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void writeToParcel(Parcel parcel, int n) {
            int n2 = 1;
            parcel.writeInt(this.mAnchorPosition);
            parcel.writeInt(this.mVisibleAnchorPosition);
            parcel.writeInt(this.mSpanOffsetsSize);
            if (this.mSpanOffsetsSize > 0) {
                parcel.writeIntArray(this.mSpanOffsets);
            }
            parcel.writeInt(this.mSpanLookupSize);
            if (this.mSpanLookupSize > 0) {
                parcel.writeIntArray(this.mSpanLookup);
            }
            n = this.mReverseLayout ? 1 : 0;
            parcel.writeInt(n);
            n = this.mAnchorLayoutFromEnd ? 1 : 0;
            parcel.writeInt(n);
            n = this.mLastLayoutRTL ? n2 : 0;
            parcel.writeInt(n);
            parcel.writeList(this.mFullSpanItems);
        }

    }

    class Span {
        static final int INVALID_LINE = Integer.MIN_VALUE;
        int mCachedEnd;
        int mCachedStart;
        int mDeletedSize;
        final int mIndex;
        private ArrayList<View> mViews;

        private Span(int n) {
            this.mViews = new ArrayList();
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
            this.mDeletedSize = 0;
            this.mIndex = n;
        }

        void appendToSpan(View view) {
            LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = this;
            this.mViews.add(view);
            this.mCachedEnd = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        void cacheReferenceLineAndClear(boolean bl, int n) {
            int n2 = bl ? this.getEndLine(Integer.MIN_VALUE) : this.getStartLine(Integer.MIN_VALUE);
            this.clear();
            if (n2 == Integer.MIN_VALUE || bl && n2 < StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() || !bl && n2 > StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding()) {
                return;
            }
            int n3 = n2;
            if (n != Integer.MIN_VALUE) {
                n3 = n2 + n;
            }
            this.mCachedEnd = n3;
            this.mCachedStart = n3;
        }

        void calculateCachedEnd() {
            Object object = this.mViews.get(this.mViews.size() - 1);
            LayoutParams layoutParams = this.getLayoutParams((View)object);
            this.mCachedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd((View)object);
            if (layoutParams.mFullSpan && (object = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(layoutParams.getViewLayoutPosition())) != null && object.mGapDir == 1) {
                this.mCachedEnd += object.getGapForSpan(this.mIndex);
            }
        }

        void calculateCachedStart() {
            Object object = this.mViews.get(0);
            LayoutParams layoutParams = this.getLayoutParams((View)object);
            this.mCachedStart = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart((View)object);
            if (layoutParams.mFullSpan && (object = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(layoutParams.getViewLayoutPosition())) != null && object.mGapDir == -1) {
                this.mCachedStart -= object.getGapForSpan(this.mIndex);
            }
        }

        void clear() {
            this.mViews.clear();
            this.invalidateCache();
            this.mDeletedSize = 0;
        }

        public int findFirstCompletelyVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
            }
            return this.findOneVisibleChild(0, this.mViews.size(), true);
        }

        public int findFirstVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
            }
            return this.findOneVisibleChild(0, this.mViews.size(), false);
        }

        public int findLastCompletelyVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOneVisibleChild(0, this.mViews.size(), true);
            }
            return this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
        }

        public int findLastVisibleItemPosition() {
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                return this.findOneVisibleChild(0, this.mViews.size(), false);
            }
            return this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
        }

        /*
         * Enabled aggressive block sorting
         */
        int findOneVisibleChild(int n, int n2, boolean bl) {
            int n3 = -1;
            int n4 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
            int n5 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
            int n6 = n2 > n ? 1 : -1;
            do {
                int n7 = n3;
                if (n == n2) return n7;
                View view = this.mViews.get(n);
                n7 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
                int n8 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
                if (n7 < n5 && n8 > n4) {
                    if (!bl) {
                        return StaggeredGridLayoutManager.this.getPosition(view);
                    }
                    if (n7 >= n4 && n8 <= n5) {
                        return StaggeredGridLayoutManager.this.getPosition(view);
                    }
                }
                n += n6;
            } while (true);
        }

        public int getDeletedSize() {
            return this.mDeletedSize;
        }

        int getEndLine() {
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                return this.mCachedEnd;
            }
            this.calculateCachedEnd();
            return this.mCachedEnd;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        int getEndLine(int n) {
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                return this.mCachedEnd;
            }
            if (this.mViews.size() == 0) return n;
            this.calculateCachedEnd();
            return this.mCachedEnd;
        }

        LayoutParams getLayoutParams(View view) {
            return (LayoutParams)view.getLayoutParams();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        int getNormalizedOffset(int n, int n2, int n3) {
            if (this.mViews.size() == 0) {
                return 0;
            }
            if (n < 0) {
                n3 = this.getEndLine() - n3;
                if (n3 <= 0) {
                    return 0;
                }
                n2 = n;
                if (- n <= n3) return n2;
                return - n3;
            }
            if ((n2 -= this.getStartLine()) <= 0) {
                return 0;
            }
            if (n2 >= n) return n;
            return n2;
        }

        int getStartLine() {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                return this.mCachedStart;
            }
            this.calculateCachedStart();
            return this.mCachedStart;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        int getStartLine(int n) {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                return this.mCachedStart;
            }
            if (this.mViews.size() == 0) return n;
            this.calculateCachedStart();
            return this.mCachedStart;
        }

        void invalidateCache() {
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
        }

        boolean isEmpty(int n, int n2) {
            int n3 = this.mViews.size();
            for (int i = 0; i < n3; ++i) {
                View view = this.mViews.get(i);
                if (StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view) >= n2 || StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view) <= n) continue;
                return false;
            }
            return true;
        }

        void onOffset(int n) {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                this.mCachedStart += n;
            }
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                this.mCachedEnd += n;
            }
        }

        void popEnd() {
            int n = this.mViews.size();
            View view = this.mViews.remove(n - 1);
            LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            if (n == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            this.mCachedEnd = Integer.MIN_VALUE;
        }

        void popStart() {
            View view = this.mViews.remove(0);
            LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (this.mViews.size() == 0) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            this.mCachedStart = Integer.MIN_VALUE;
        }

        void prependToSpan(View view) {
            LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = this;
            this.mViews.add(0, view);
            this.mCachedStart = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
                this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
        }

        void setLine(int n) {
            this.mCachedStart = n;
            this.mCachedEnd = n;
        }
    }

}

