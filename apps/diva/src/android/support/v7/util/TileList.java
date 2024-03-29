/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package android.support.v7.util;

import android.util.SparseArray;
import java.lang.reflect.Array;

class TileList<T> {
    Tile<T> mLastAccessedTile;
    final int mTileSize;
    private final SparseArray<Tile<T>> mTiles = new SparseArray(10);

    public TileList(int n) {
        this.mTileSize = n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Tile<T> addOrReplace(Tile<T> tile) {
        int n = this.mTiles.indexOfKey(tile.mStartPosition);
        if (n < 0) {
            this.mTiles.put(tile.mStartPosition, tile);
            return null;
        }
        Tile tile2 = (Tile)this.mTiles.valueAt(n);
        this.mTiles.setValueAt(n, tile);
        Tile tile3 = tile2;
        if (this.mLastAccessedTile != tile2) return tile3;
        this.mLastAccessedTile = tile;
        return tile2;
    }

    public void clear() {
        this.mTiles.clear();
    }

    public Tile<T> getAtIndex(int n) {
        return (Tile)this.mTiles.valueAt(n);
    }

    public T getItemAt(int n) {
        if (this.mLastAccessedTile == null || !this.mLastAccessedTile.containsPosition(n)) {
            int n2 = this.mTileSize;
            if ((n2 = this.mTiles.indexOfKey(n - n % n2)) < 0) {
                return null;
            }
            this.mLastAccessedTile = (Tile)this.mTiles.valueAt(n2);
        }
        return this.mLastAccessedTile.getByPosition(n);
    }

    public Tile<T> removeAtPos(int n) {
        Tile tile = (Tile)this.mTiles.get(n);
        if (this.mLastAccessedTile == tile) {
            this.mLastAccessedTile = null;
        }
        this.mTiles.delete(n);
        return tile;
    }

    public int size() {
        return this.mTiles.size();
    }

    public static class Tile<T> {
        public int mItemCount;
        public final T[] mItems;
        Tile<T> mNext;
        public int mStartPosition;

        public Tile(Class<T> class_, int n) {
            this.mItems = (Object[])Array.newInstance(class_, n);
        }

        boolean containsPosition(int n) {
            if (this.mStartPosition <= n && n < this.mStartPosition + this.mItemCount) {
                return true;
            }
            return false;
        }

        T getByPosition(int n) {
            return this.mItems[n - this.mStartPosition];
        }
    }

}

