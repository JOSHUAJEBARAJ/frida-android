/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.util.SparseBooleanArray
 *  android.util.SparseIntArray
 */
package android.support.v7.util;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.support.v7.util.MessageThreadUtil;
import android.support.v7.util.ThreadUtil;
import android.support.v7.util.TileList;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

public class AsyncListUtil<T> {
    private static final boolean DEBUG = false;
    private static final String TAG = "AsyncListUtil";
    private boolean mAllowScrollHints;
    private final ThreadUtil.BackgroundCallback<T> mBackgroundCallback;
    final ThreadUtil.BackgroundCallback<T> mBackgroundProxy;
    final DataCallback<T> mDataCallback;
    int mDisplayedGeneration;
    private int mItemCount = 0;
    private final ThreadUtil.MainThreadCallback<T> mMainThreadCallback;
    final ThreadUtil.MainThreadCallback<T> mMainThreadProxy;
    private final SparseIntArray mMissingPositions;
    final int[] mPrevRange = new int[2];
    int mRequestedGeneration;
    private int mScrollHint = 0;
    final Class<T> mTClass;
    final TileList<T> mTileList;
    final int mTileSize;
    final int[] mTmpRange = new int[2];
    final int[] mTmpRangeExtended = new int[2];
    final ViewCallback mViewCallback;

    public AsyncListUtil(Class<T> object, int n, DataCallback<T> dataCallback, ViewCallback viewCallback) {
        this.mRequestedGeneration = this.mDisplayedGeneration = 0;
        this.mMissingPositions = new SparseIntArray();
        this.mMainThreadCallback = new ThreadUtil.MainThreadCallback<T>(){

            private boolean isRequestedGeneration(int n) {
                if (n == AsyncListUtil.this.mRequestedGeneration) {
                    return true;
                }
                return false;
            }

            private void recycleAllTiles() {
                for (int i = 0; i < AsyncListUtil.this.mTileList.size(); ++i) {
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(AsyncListUtil.this.mTileList.getAtIndex(i));
                }
                AsyncListUtil.this.mTileList.clear();
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public void addTile(int n, TileList.Tile<T> tile) {
                if (!this.isRequestedGeneration(n)) {
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(tile);
                    return;
                }
                TileList.Tile tile2 = AsyncListUtil.this.mTileList.addOrReplace(tile);
                if (tile2 != null) {
                    Log.e((String)"AsyncListUtil", (String)("duplicate tile @" + tile2.mStartPosition));
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(tile2);
                }
                int n2 = tile.mStartPosition;
                int n3 = tile.mItemCount;
                n = 0;
                while (n < AsyncListUtil.this.mMissingPositions.size()) {
                    int n4 = AsyncListUtil.this.mMissingPositions.keyAt(n);
                    if (tile.mStartPosition <= n4 && n4 < n2 + n3) {
                        AsyncListUtil.this.mMissingPositions.removeAt(n);
                        AsyncListUtil.this.mViewCallback.onItemLoaded(n4);
                        continue;
                    }
                    ++n;
                }
            }

            @Override
            public void removeTile(int n, int n2) {
                if (!this.isRequestedGeneration(n)) {
                    return;
                }
                TileList.Tile tile = AsyncListUtil.this.mTileList.removeAtPos(n2);
                if (tile == null) {
                    Log.e((String)"AsyncListUtil", (String)("tile not found @" + n2));
                    return;
                }
                AsyncListUtil.this.mBackgroundProxy.recycleTile(tile);
            }

            @Override
            public void updateItemCount(int n, int n2) {
                if (!this.isRequestedGeneration(n)) {
                    return;
                }
                AsyncListUtil.this.mItemCount = n2;
                AsyncListUtil.this.mViewCallback.onDataRefresh();
                AsyncListUtil.this.mDisplayedGeneration = AsyncListUtil.this.mRequestedGeneration;
                this.recycleAllTiles();
                AsyncListUtil.this.mAllowScrollHints = false;
                AsyncListUtil.this.updateRange();
            }
        };
        this.mBackgroundCallback = new ThreadUtil.BackgroundCallback<T>(){
            private int mFirstRequiredTileStart;
            private int mGeneration;
            private int mItemCount;
            private int mLastRequiredTileStart;
            final SparseBooleanArray mLoadedTiles;
            private TileList.Tile<T> mRecycledRoot;

            private TileList.Tile<T> acquireTile() {
                if (this.mRecycledRoot != null) {
                    TileList.Tile<T> tile = this.mRecycledRoot;
                    this.mRecycledRoot = this.mRecycledRoot.mNext;
                    return tile;
                }
                return new TileList.Tile(AsyncListUtil.this.mTClass, AsyncListUtil.this.mTileSize);
            }

            private void addTile(TileList.Tile<T> tile) {
                this.mLoadedTiles.put(tile.mStartPosition, true);
                AsyncListUtil.this.mMainThreadProxy.addTile(this.mGeneration, tile);
            }

            private void flushTileCache(int n) {
                int n2 = AsyncListUtil.this.mDataCallback.getMaxCachedTiles();
                while (this.mLoadedTiles.size() >= n2) {
                    int n3 = this.mLoadedTiles.keyAt(0);
                    int n4 = this.mLoadedTiles.keyAt(this.mLoadedTiles.size() - 1);
                    int n5 = this.mFirstRequiredTileStart - n3;
                    int n6 = n4 - this.mLastRequiredTileStart;
                    if (n5 > 0 && (n5 >= n6 || n == 2)) {
                        this.removeTile(n3);
                        continue;
                    }
                    if (n6 <= 0 || n5 >= n6 && n != 1) break;
                    this.removeTile(n4);
                }
            }

            private int getTileStart(int n) {
                return n - n % AsyncListUtil.this.mTileSize;
            }

            private boolean isTileLoaded(int n) {
                return this.mLoadedTiles.get(n);
            }

            private /* varargs */ void log(String string2, Object ... arrobject) {
                Log.d((String)"AsyncListUtil", (String)("[BKGR] " + String.format(string2, arrobject)));
            }

            private void removeTile(int n) {
                this.mLoadedTiles.delete(n);
                AsyncListUtil.this.mMainThreadProxy.removeTile(this.mGeneration, n);
            }

            /*
             * Enabled aggressive block sorting
             */
            private void requestTiles(int n, int n2, int n3, boolean bl) {
                int n4 = n;
                while (n4 <= n2) {
                    int n5 = bl ? n2 + n - n4 : n4;
                    AsyncListUtil.this.mBackgroundProxy.loadTile(n5, n3);
                    n4 += AsyncListUtil.this.mTileSize;
                }
                return;
            }

            @Override
            public void loadTile(int n, int n2) {
                if (this.isTileLoaded(n)) {
                    return;
                }
                TileList.Tile<T> tile = this.acquireTile();
                tile.mStartPosition = n;
                tile.mItemCount = Math.min(AsyncListUtil.this.mTileSize, this.mItemCount - tile.mStartPosition);
                AsyncListUtil.this.mDataCallback.fillData(tile.mItems, tile.mStartPosition, tile.mItemCount);
                this.flushTileCache(n2);
                this.addTile(tile);
            }

            @Override
            public void recycleTile(TileList.Tile<T> tile) {
                AsyncListUtil.this.mDataCallback.recycleData(tile.mItems, tile.mItemCount);
                tile.mNext = this.mRecycledRoot;
                this.mRecycledRoot = tile;
            }

            @Override
            public void refresh(int n) {
                this.mGeneration = n;
                this.mLoadedTiles.clear();
                this.mItemCount = AsyncListUtil.this.mDataCallback.refreshData();
                AsyncListUtil.this.mMainThreadProxy.updateItemCount(this.mGeneration, this.mItemCount);
            }

            @Override
            public void updateRange(int n, int n2, int n3, int n4, int n5) {
                if (n > n2) {
                    return;
                }
                n = this.getTileStart(n);
                n2 = this.getTileStart(n2);
                this.mFirstRequiredTileStart = this.getTileStart(n3);
                this.mLastRequiredTileStart = this.getTileStart(n4);
                if (n5 == 1) {
                    this.requestTiles(this.mFirstRequiredTileStart, n2, n5, true);
                    this.requestTiles(AsyncListUtil.this.mTileSize + n2, this.mLastRequiredTileStart, n5, false);
                    return;
                }
                this.requestTiles(n, this.mLastRequiredTileStart, n5, false);
                this.requestTiles(this.mFirstRequiredTileStart, n - AsyncListUtil.this.mTileSize, n5, true);
            }
        };
        this.mTClass = object;
        this.mTileSize = n;
        this.mDataCallback = dataCallback;
        this.mViewCallback = viewCallback;
        this.mTileList = new TileList(this.mTileSize);
        object = new MessageThreadUtil();
        this.mMainThreadProxy = object.getMainThreadProxy(this.mMainThreadCallback);
        this.mBackgroundProxy = object.getBackgroundProxy(this.mBackgroundCallback);
        this.refresh();
    }

    private boolean isRefreshPending() {
        if (this.mRequestedGeneration != this.mDisplayedGeneration) {
            return true;
        }
        return false;
    }

    private /* varargs */ void log(String string2, Object ... arrobject) {
        Log.d((String)"AsyncListUtil", (String)("[MAIN] " + String.format(string2, arrobject)));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateRange() {
        this.mViewCallback.getItemRangeInto(this.mTmpRange);
        if (this.mTmpRange[0] > this.mTmpRange[1] || this.mTmpRange[0] < 0 || this.mTmpRange[1] >= this.mItemCount) {
            return;
        }
        if (!this.mAllowScrollHints) {
            this.mScrollHint = 0;
        } else if (this.mTmpRange[0] > this.mPrevRange[1] || this.mPrevRange[0] > this.mTmpRange[1]) {
            this.mScrollHint = 0;
        } else if (this.mTmpRange[0] < this.mPrevRange[0]) {
            this.mScrollHint = 1;
        } else if (this.mTmpRange[0] > this.mPrevRange[0]) {
            this.mScrollHint = 2;
        }
        this.mPrevRange[0] = this.mTmpRange[0];
        this.mPrevRange[1] = this.mTmpRange[1];
        this.mViewCallback.extendRangeInto(this.mTmpRange, this.mTmpRangeExtended, this.mScrollHint);
        this.mTmpRangeExtended[0] = Math.min(this.mTmpRange[0], Math.max(this.mTmpRangeExtended[0], 0));
        this.mTmpRangeExtended[1] = Math.max(this.mTmpRange[1], Math.min(this.mTmpRangeExtended[1], this.mItemCount - 1));
        this.mBackgroundProxy.updateRange(this.mTmpRange[0], this.mTmpRange[1], this.mTmpRangeExtended[0], this.mTmpRangeExtended[1], this.mScrollHint);
    }

    public T getItem(int n) {
        if (n < 0 || n >= this.mItemCount) {
            throw new IndexOutOfBoundsException("" + n + " is not within 0 and " + this.mItemCount);
        }
        T t = this.mTileList.getItemAt(n);
        if (t == null && !this.isRefreshPending()) {
            this.mMissingPositions.put(n, 0);
        }
        return t;
    }

    public int getItemCount() {
        return this.mItemCount;
    }

    public void onRangeChanged() {
        if (this.isRefreshPending()) {
            return;
        }
        this.updateRange();
        this.mAllowScrollHints = true;
    }

    public void refresh() {
        int n;
        this.mMissingPositions.clear();
        ThreadUtil.BackgroundCallback<T> backgroundCallback = this.mBackgroundProxy;
        this.mRequestedGeneration = n = this.mRequestedGeneration + 1;
        backgroundCallback.refresh(n);
    }

    public static abstract class DataCallback<T> {
        @WorkerThread
        public abstract void fillData(T[] var1, int var2, int var3);

        @WorkerThread
        public int getMaxCachedTiles() {
            return 10;
        }

        @WorkerThread
        public void recycleData(T[] arrT, int n) {
        }

        @WorkerThread
        public abstract int refreshData();
    }

    public static abstract class ViewCallback {
        public static final int HINT_SCROLL_ASC = 2;
        public static final int HINT_SCROLL_DESC = 1;
        public static final int HINT_SCROLL_NONE = 0;

        /*
         * Enabled aggressive block sorting
         */
        @UiThread
        public void extendRangeInto(int[] arrn, int[] arrn2, int n) {
            int n2 = arrn[1] - arrn[0] + 1;
            int n3 = n2 / 2;
            int n4 = arrn[0];
            int n5 = n == 1 ? n2 : n3;
            arrn2[0] = n4 - n5;
            n5 = arrn[1];
            if (n != 2) {
                n2 = n3;
            }
            arrn2[1] = n5 + n2;
        }

        @UiThread
        public abstract void getItemRangeInto(int[] var1);

        @UiThread
        public abstract void onDataRefresh();

        @UiThread
        public abstract void onItemLoaded(int var1);
    }

}
