/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseIntArray
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;
import java.util.List;

public class GridLayoutManager
extends LinearLayoutManager {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    static final int MAIN_DIR_SPEC = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets = new Rect();
    boolean mPendingSpanCountChange = false;
    final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
    final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
    View[] mSet;
    int mSpanCount = -1;
    SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();

    public GridLayoutManager(Context context, int n) {
        super(context);
        this.setSpanCount(n);
    }

    public GridLayoutManager(Context context, int n, int n2, boolean bl) {
        super(context, n2, bl);
        this.setSpanCount(n);
    }

    public GridLayoutManager(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.setSpanCount(GridLayoutManager.getProperties((Context)context, (AttributeSet)attributeSet, (int)n, (int)n2).spanCount);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void assignSpans(RecyclerView.Recycler recycler, RecyclerView.State state, int n, int n2, boolean bl) {
        int n3;
        int n4;
        int n5;
        if (bl) {
            n2 = 0;
            n4 = n;
            n5 = 1;
            n = n2;
        } else {
            --n;
            n4 = -1;
            n5 = -1;
        }
        if (this.mOrientation == 1 && this.isLayoutRTL()) {
            n2 = this.mSpanCount - 1;
            n3 = -1;
        } else {
            n2 = 0;
            n3 = 1;
        }
        while (n != n4) {
            View view = this.mSet[n];
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.mSpanSize = this.getSpanSize(recycler, state, this.getPosition(view));
            if (n3 == -1 && layoutParams.mSpanSize > 1) {
                layoutParams.mSpanIndex = n2 - (layoutParams.mSpanSize - 1);
            } else {
                layoutParams.mSpanIndex = n2;
            }
            n2 += layoutParams.mSpanSize * n3;
            n += n5;
        }
    }

    private void cachePreLayoutSpanMapping() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            int n2 = layoutParams.getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(n2, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(n2, layoutParams.getSpanIndex());
        }
    }

    private void calculateItemBorders(int n) {
        if (this.mCachedBorders == null || this.mCachedBorders.length != this.mSpanCount + 1 || this.mCachedBorders[this.mCachedBorders.length - 1] != n) {
            this.mCachedBorders = new int[this.mSpanCount + 1];
        }
        this.mCachedBorders[0] = 0;
        int n2 = n / this.mSpanCount;
        int n3 = n % this.mSpanCount;
        int n4 = 0;
        n = 0;
        for (int i = 1; i <= this.mSpanCount; ++i) {
            int n5;
            int n6 = n2;
            n = n5 = n + n3;
            int n7 = n6;
            if (n5 > 0) {
                n = n5;
                n7 = n6;
                if (this.mSpanCount - n5 < n3) {
                    n7 = n6 + 1;
                    n = n5 - this.mSpanCount;
                }
            }
            this.mCachedBorders[i] = n4 += n7;
        }
    }

    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }

    private void ensureAnchorIsInFirstSpan(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo) {
        int n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
        while (n > 0 && anchorInfo.mPosition > 0) {
            --anchorInfo.mPosition;
            n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
        }
    }

    private int getMainDirSpec(int n) {
        if (n < 0) {
            return MAIN_DIR_SPEC;
        }
        return View.MeasureSpec.makeMeasureSpec((int)n, (int)1073741824);
    }

    private int getSpanGroupIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanGroupIndex(n, this.mSpanCount);
        }
        int n2 = recycler.convertPreLayoutPositionToPostLayout(n);
        if (n2 == -1) {
            Log.w((String)"GridLayoutManager", (String)("Cannot find span size for pre layout position. " + n));
            return 0;
        }
        return this.mSpanSizeLookup.getSpanGroupIndex(n2, this.mSpanCount);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getSpanIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int n) {
        int n2;
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(n, this.mSpanCount);
        }
        int n3 = n2 = this.mPreLayoutSpanIndexCache.get(n, -1);
        if (n2 != -1) return n3;
        n3 = recycler.convertPreLayoutPositionToPostLayout(n);
        if (n3 != -1) return this.mSpanSizeLookup.getCachedSpanIndex(n3, this.mSpanCount);
        Log.w((String)"GridLayoutManager", (String)("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + n));
        return 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getSpanSize(RecyclerView.Recycler recycler, RecyclerView.State state, int n) {
        int n2;
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(n);
        }
        int n3 = n2 = this.mPreLayoutSpanSizeCache.get(n, -1);
        if (n2 != -1) return n3;
        n3 = recycler.convertPreLayoutPositionToPostLayout(n);
        if (n3 != -1) return this.mSpanSizeLookup.getSpanSize(n3);
        Log.w((String)"GridLayoutManager", (String)("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + n));
        return 1;
    }

    private void measureChildWithDecorationsAndMargin(View view, int n, int n2, boolean bl) {
        int n3;
        block5 : {
            block4 : {
                this.calculateItemDecorationsForChild(view, this.mDecorInsets);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                if (!bl) {
                    n3 = n;
                    if (this.mOrientation != 1) break block4;
                }
                n3 = this.updateSpecWithExtra(n, layoutParams.leftMargin + this.mDecorInsets.left, layoutParams.rightMargin + this.mDecorInsets.right);
            }
            if (!bl) {
                n = n2;
                if (this.mOrientation != 0) break block5;
            }
            n = this.updateSpecWithExtra(n2, layoutParams.topMargin + this.mDecorInsets.top, layoutParams.bottomMargin + this.mDecorInsets.bottom);
        }
        view.measure(n3, n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateMeasurements() {
        int n = this.getOrientation() == 1 ? this.getWidth() - this.getPaddingRight() - this.getPaddingLeft() : this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
        this.calculateItemBorders(n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int updateSpecWithExtra(int n, int n2, int n3) {
        int n4;
        if (n2 == 0 && n3 == 0 || (n4 = View.MeasureSpec.getMode((int)n)) != Integer.MIN_VALUE && n4 != 1073741824) {
            return n;
        }
        return View.MeasureSpec.makeMeasureSpec((int)(View.MeasureSpec.getSize((int)n) - n2 - n3), (int)n4);
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state, int n, int n2, int n3) {
        this.ensureLayoutState();
        View view = null;
        View view2 = null;
        int n4 = this.mOrientationHelper.getStartAfterPadding();
        int n5 = this.mOrientationHelper.getEndAfterPadding();
        int n6 = n2 > n ? 1 : -1;
        while (n != n2) {
            View view3 = this.getChildAt(n);
            int n7 = this.getPosition(view3);
            View view4 = view;
            View view5 = view2;
            if (n7 >= 0) {
                view4 = view;
                view5 = view2;
                if (n7 < n3) {
                    if (this.getSpanIndex(recycler, state, n7) != 0) {
                        view5 = view2;
                        view4 = view;
                    } else if (((RecyclerView.LayoutParams)view3.getLayoutParams()).isItemRemoved()) {
                        view4 = view;
                        view5 = view2;
                        if (view == null) {
                            view4 = view3;
                            view5 = view2;
                        }
                    } else {
                        if (this.mOrientationHelper.getDecoratedStart(view3) < n5) {
                            view4 = view3;
                            if (this.mOrientationHelper.getDecoratedEnd(view3) >= n4) return view4;
                        }
                        view4 = view;
                        view5 = view2;
                        if (view2 == null) {
                            view4 = view;
                            view5 = view3;
                        }
                    }
                }
            }
            n += n6;
            view = view4;
            view2 = view5;
        }
        if (view2 != null) return view2;
        view2 = view;
        return view2;
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
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1);
    }

    @Override
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1);
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State object, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult layoutChunkResult) {
        boolean bl = layoutState.mItemDirection == 1;
        int n = 0;
        int n2 = 0;
        int n3 = this.mSpanCount;
        int n4 = n;
        int n5 = n2;
        if (!bl) {
            n3 = this.getSpanIndex(recycler, (RecyclerView.State)object, layoutState.mCurrentPosition) + this.getSpanSize(recycler, (RecyclerView.State)object, layoutState.mCurrentPosition);
            n5 = n2;
            n4 = n;
        }
        while (n4 < this.mSpanCount && layoutState.hasMore((RecyclerView.State)object) && n3 > 0) {
            View view;
            n2 = layoutState.mCurrentPosition;
            n = this.getSpanSize(recycler, (RecyclerView.State)object, n2);
            if (n > this.mSpanCount) {
                throw new IllegalArgumentException("Item at position " + n2 + " requires " + n + " spans but GridLayoutManager has only " + this.mSpanCount + " spans.");
            }
            if ((n3 -= n) < 0 || (view = layoutState.next(recycler)) == null) break;
            n5 += n;
            this.mSet[n4] = view;
            ++n4;
        }
        if (n4 == 0) {
            layoutChunkResult.mFinished = true;
            return;
        }
        n3 = 0;
        this.assignSpans(recycler, (RecyclerView.State)object, n4, n5, bl);
        for (n5 = 0; n5 < n4; ++n5) {
            recycler = this.mSet[n5];
            if (layoutState.mScrapList == null) {
                if (bl) {
                    this.addView((View)recycler);
                } else {
                    this.addView((View)recycler, 0);
                }
            } else if (bl) {
                this.addDisappearingView((View)recycler);
            } else {
                this.addDisappearingView((View)recycler, 0);
            }
            object = (LayoutParams)recycler.getLayoutParams();
            n = View.MeasureSpec.makeMeasureSpec((int)(this.mCachedBorders[((LayoutParams)((Object)object)).mSpanIndex + ((LayoutParams)((Object)object)).mSpanSize] - this.mCachedBorders[((LayoutParams)((Object)object)).mSpanIndex]), (int)1073741824);
            if (this.mOrientation == 1) {
                this.measureChildWithDecorationsAndMargin((View)recycler, n, this.getMainDirSpec(object.height), false);
            } else {
                this.measureChildWithDecorationsAndMargin((View)recycler, this.getMainDirSpec(object.width), n, false);
            }
            n2 = this.mOrientationHelper.getDecoratedMeasurement((View)recycler);
            n = n3;
            if (n2 > n3) {
                n = n2;
            }
            n3 = n;
        }
        n = this.getMainDirSpec(n3);
        for (n5 = 0; n5 < n4; ++n5) {
            recycler = this.mSet[n5];
            if (this.mOrientationHelper.getDecoratedMeasurement((View)recycler) == n3) continue;
            object = (LayoutParams)recycler.getLayoutParams();
            n2 = View.MeasureSpec.makeMeasureSpec((int)(this.mCachedBorders[((LayoutParams)((Object)object)).mSpanIndex + ((LayoutParams)((Object)object)).mSpanSize] - this.mCachedBorders[((LayoutParams)((Object)object)).mSpanIndex]), (int)1073741824);
            if (this.mOrientation == 1) {
                this.measureChildWithDecorationsAndMargin((View)recycler, n2, n, true);
                continue;
            }
            this.measureChildWithDecorationsAndMargin((View)recycler, n, n2, true);
        }
        layoutChunkResult.mConsumed = n3;
        n5 = 0;
        n = 0;
        n2 = 0;
        int n6 = 0;
        if (this.mOrientation == 1) {
            if (layoutState.mLayoutDirection == -1) {
                n6 = layoutState.mOffset;
                n2 = n6 - n3;
                n3 = n6;
            } else {
                n2 = layoutState.mOffset;
                n3 = n2 + n3;
            }
        } else if (layoutState.mLayoutDirection == -1) {
            n = layoutState.mOffset;
            n5 = n - n3;
            n3 = n6;
        } else {
            n5 = layoutState.mOffset;
            n = n5 + n3;
            n3 = n6;
        }
        int n7 = 0;
        n6 = n2;
        n2 = n5;
        n5 = n7;
        do {
            if (n5 >= n4) {
                Arrays.fill(this.mSet, null);
                return;
            }
            recycler = this.mSet[n5];
            object = (LayoutParams)recycler.getLayoutParams();
            if (this.mOrientation == 1) {
                n2 = this.getPaddingLeft() + this.mCachedBorders[((LayoutParams)((Object)object)).mSpanIndex];
                n = n2 + this.mOrientationHelper.getDecoratedMeasurementInOther((View)recycler);
            } else {
                n6 = this.getPaddingTop() + this.mCachedBorders[((LayoutParams)((Object)object)).mSpanIndex];
                n3 = n6 + this.mOrientationHelper.getDecoratedMeasurementInOther((View)recycler);
            }
            this.layoutDecorated((View)recycler, n2 + object.leftMargin, n6 + object.topMargin, n - object.rightMargin, n3 - object.bottomMargin);
            if (object.isItemRemoved() || object.isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            layoutChunkResult.mFocusable |= recycler.isFocusable();
            ++n5;
        } while (true);
    }

    @Override
    void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo) {
        super.onAnchorReady(recycler, state, anchorInfo);
        this.updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            this.ensureAnchorIsInFirstSpan(recycler, state, anchorInfo);
        }
        if (this.mSet == null || this.mSet.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewGroup.LayoutParams layoutParams = object.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem((View)object, accessibilityNodeInfoCompat);
            return;
        }
        object = (LayoutParams)layoutParams;
        int n = this.getSpanGroupIndex(recycler, state, object.getViewLayoutPosition());
        if (this.mOrientation == 0) {
            int n2 = object.getSpanIndex();
            int n3 = object.getSpanSize();
            boolean bl = this.mSpanCount > 1 && object.getSpanSize() == this.mSpanCount;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n2, n3, n, 1, bl, false));
            return;
        }
        int n4 = object.getSpanIndex();
        int n5 = object.getSpanSize();
        boolean bl = this.mSpanCount > 1 && object.getSpanSize() == this.mSpanCount;
        accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n, 1, n4, n5, bl, false));
    }

    @Override
    public void onItemsAdded(RecyclerView recyclerView, int n, int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsChanged(RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsMoved(RecyclerView recyclerView, int n, int n2, int n3) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsRemoved(RecyclerView recyclerView, int n, int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsUpdated(RecyclerView recyclerView, int n, int n2, Object object) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            this.cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        this.clearPreLayoutSpanMappingCache();
        if (!state.isPreLayout()) {
            this.mPendingSpanCountChange = false;
        }
    }

    public void setSpanCount(int n) {
        if (n == this.mSpanCount) {
            return;
        }
        this.mPendingSpanCountChange = true;
        if (n < 1) {
            throw new IllegalArgumentException("Span count should be at least 1. Provided " + n);
        }
        this.mSpanCount = n;
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public void setStackFromEnd(boolean bl) {
        if (bl) {
            throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
        }
        super.setStackFromEnd(false);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState == null && !this.mPendingSpanCountChange) {
            return true;
        }
        return false;
    }

    public static final class DefaultSpanSizeLookup
    extends SpanSizeLookup {
        @Override
        public int getSpanIndex(int n, int n2) {
            return n % n2;
        }

        @Override
        public int getSpanSize(int n) {
            return 1;
        }
    }

    public static class LayoutParams
    extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        private int mSpanIndex = -1;
        private int mSpanSize = 0;

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

        public int getSpanIndex() {
            return this.mSpanIndex;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }
    }

    public static abstract class SpanSizeLookup {
        private boolean mCacheSpanIndices = false;
        final SparseIntArray mSpanIndexCache = new SparseIntArray();

        int findReferenceIndexFromCache(int n) {
            int n2 = 0;
            int n3 = this.mSpanIndexCache.size() - 1;
            while (n2 <= n3) {
                int n4 = n2 + n3 >>> 1;
                if (this.mSpanIndexCache.keyAt(n4) < n) {
                    n2 = n4 + 1;
                    continue;
                }
                n3 = n4 - 1;
            }
            n = n2 - 1;
            if (n >= 0 && n < this.mSpanIndexCache.size()) {
                return this.mSpanIndexCache.keyAt(n);
            }
            return -1;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        int getCachedSpanIndex(int n, int n2) {
            int n3;
            if (!this.mCacheSpanIndices) {
                return this.getSpanIndex(n, n2);
            }
            int n4 = n3 = this.mSpanIndexCache.get(n, -1);
            if (n3 != -1) return n4;
            n2 = this.getSpanIndex(n, n2);
            this.mSpanIndexCache.put(n, n2);
            return n2;
        }

        /*
         * Enabled aggressive block sorting
         */
        public int getSpanGroupIndex(int n, int n2) {
            int n3 = 0;
            int n4 = 0;
            int n5 = this.getSpanSize(n);
            for (int i = 0; i < n; ++i) {
                int n6;
                int n7 = this.getSpanSize(i);
                int n8 = n3 + n7;
                if (n8 == n2) {
                    n3 = 0;
                    n6 = n4 + 1;
                } else {
                    n6 = n4;
                    n3 = n8;
                    if (n8 > n2) {
                        n3 = n7;
                        n6 = n4 + 1;
                    }
                }
                n4 = n6;
            }
            n = n4;
            if (n3 + n5 <= n2) return n;
            return n4 + 1;
        }

        /*
         * Enabled aggressive block sorting
         */
        public int getSpanIndex(int n, int n2) {
            int n3 = this.getSpanSize(n);
            if (n3 == n2) {
                return 0;
            }
            int n4 = 0;
            int n5 = 0;
            int n6 = n4;
            int n7 = n5;
            if (this.mCacheSpanIndices) {
                n6 = n4;
                n7 = n5;
                if (this.mSpanIndexCache.size() > 0) {
                    int n8 = this.findReferenceIndexFromCache(n);
                    n6 = n4;
                    n7 = n5;
                    if (n8 >= 0) {
                        n6 = this.mSpanIndexCache.get(n8) + this.getSpanSize(n8);
                        n7 = n8 + 1;
                    }
                }
            }
            while (n7 < n) {
                n4 = this.getSpanSize(n7);
                n5 = n6 + n4;
                if (n5 == n2) {
                    n6 = 0;
                } else {
                    n6 = n5;
                    if (n5 > n2) {
                        n6 = n4;
                    }
                }
                ++n7;
            }
            n = n6;
            if (n6 + n3 <= n2) return n;
            return 0;
        }

        public abstract int getSpanSize(int var1);

        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }

        public void setSpanIndexCacheEnabled(boolean bl) {
            this.mCacheSpanIndices = bl;
        }
    }

}

