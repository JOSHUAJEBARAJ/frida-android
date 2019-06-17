/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

class ChildHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "ChildrenHelper";
    final Bucket mBucket;
    final Callback mCallback;
    final List<View> mHiddenViews;

    ChildHelper(Callback callback) {
        this.mCallback = callback;
        this.mBucket = new Bucket();
        this.mHiddenViews = new ArrayList<View>();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getOffset(int n) {
        if (n < 0) {
            return -1;
        }
        int n2 = this.mCallback.getChildCount();
        int n3 = n;
        while (n3 < n2) {
            int n4 = n - (n3 - this.mBucket.countOnesBefore(n3));
            if (n4 == 0) {
                do {
                    n = n3;
                    if (!this.mBucket.get(n3)) return n;
                    ++n3;
                } while (true);
            }
            n3 += n4;
        }
        return -1;
    }

    private void hideViewInternal(View view) {
        this.mHiddenViews.add(view);
        this.mCallback.onEnteredHiddenState(view);
    }

    private boolean unhideViewInternal(View view) {
        if (this.mHiddenViews.remove((Object)view)) {
            this.mCallback.onLeftHiddenState(view);
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    void addView(View view, int n, boolean bl) {
        n = n < 0 ? this.mCallback.getChildCount() : this.getOffset(n);
        this.mBucket.insert(n, bl);
        if (bl) {
            this.hideViewInternal(view);
        }
        this.mCallback.addView(view, n);
    }

    void addView(View view, boolean bl) {
        this.addView(view, -1, bl);
    }

    /*
     * Enabled aggressive block sorting
     */
    void attachViewToParent(View view, int n, ViewGroup.LayoutParams layoutParams, boolean bl) {
        n = n < 0 ? this.mCallback.getChildCount() : this.getOffset(n);
        this.mBucket.insert(n, bl);
        if (bl) {
            this.hideViewInternal(view);
        }
        this.mCallback.attachViewToParent(view, n, layoutParams);
    }

    void detachViewFromParent(int n) {
        n = this.getOffset(n);
        this.mBucket.remove(n);
        this.mCallback.detachViewFromParent(n);
    }

    View findHiddenNonRemovedView(int n, int n2) {
        int n3 = this.mHiddenViews.size();
        for (int i = 0; i < n3; ++i) {
            View view = this.mHiddenViews.get(i);
            RecyclerView.ViewHolder viewHolder = this.mCallback.getChildViewHolder(view);
            if (viewHolder.getLayoutPosition() != n || viewHolder.isInvalid() || n2 != -1 && viewHolder.getItemViewType() != n2) continue;
            return view;
        }
        return null;
    }

    View getChildAt(int n) {
        n = this.getOffset(n);
        return this.mCallback.getChildAt(n);
    }

    int getChildCount() {
        return this.mCallback.getChildCount() - this.mHiddenViews.size();
    }

    View getUnfilteredChildAt(int n) {
        return this.mCallback.getChildAt(n);
    }

    int getUnfilteredChildCount() {
        return this.mCallback.getChildCount();
    }

    void hide(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n < 0) {
            throw new IllegalArgumentException("view is not a child, cannot hide " + (Object)view);
        }
        this.mBucket.set(n);
        this.hideViewInternal(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    int indexOfChild(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n == -1 || this.mBucket.get(n)) {
            return -1;
        }
        return n - this.mBucket.countOnesBefore(n);
    }

    boolean isHidden(View view) {
        return this.mHiddenViews.contains((Object)view);
    }

    void removeAllViewsUnfiltered() {
        this.mBucket.reset();
        for (int i = this.mHiddenViews.size() - 1; i >= 0; --i) {
            this.mCallback.onLeftHiddenState(this.mHiddenViews.get(i));
            this.mHiddenViews.remove(i);
        }
        this.mCallback.removeAllViews();
    }

    void removeView(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n < 0) {
            return;
        }
        if (this.mBucket.remove(n)) {
            this.unhideViewInternal(view);
        }
        this.mCallback.removeViewAt(n);
    }

    void removeViewAt(int n) {
        View view = this.mCallback.getChildAt(n = this.getOffset(n));
        if (view == null) {
            return;
        }
        if (this.mBucket.remove(n)) {
            this.unhideViewInternal(view);
        }
        this.mCallback.removeViewAt(n);
    }

    boolean removeViewIfHidden(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n == -1) {
            if (this.unhideViewInternal(view)) {
                // empty if block
            }
            return true;
        }
        if (this.mBucket.get(n)) {
            this.mBucket.remove(n);
            if (!this.unhideViewInternal(view)) {
                // empty if block
            }
            this.mCallback.removeViewAt(n);
            return true;
        }
        return false;
    }

    public String toString() {
        return this.mBucket.toString() + ", hidden list:" + this.mHiddenViews.size();
    }

    void unhide(View view) {
        int n = this.mCallback.indexOfChild(view);
        if (n < 0) {
            throw new IllegalArgumentException("view is not a child, cannot hide " + (Object)view);
        }
        if (!this.mBucket.get(n)) {
            throw new RuntimeException("trying to unhide a view that was not hidden" + (Object)view);
        }
        this.mBucket.clear(n);
        this.unhideViewInternal(view);
    }

    static class Bucket {
        static final int BITS_PER_WORD = 64;
        static final long LAST_BIT = Long.MIN_VALUE;
        long mData = 0;
        Bucket next;

        Bucket() {
        }

        private void ensureNext() {
            if (this.next == null) {
                this.next = new Bucket();
            }
        }

        void clear(int n) {
            if (n >= 64) {
                if (this.next != null) {
                    this.next.clear(n - 64);
                }
                return;
            }
            this.mData &= 1 << n ^ -1;
        }

        int countOnesBefore(int n) {
            if (this.next == null) {
                if (n >= 64) {
                    return Long.bitCount(this.mData);
                }
                return Long.bitCount(this.mData & (1 << n) - 1);
            }
            if (n < 64) {
                return Long.bitCount(this.mData & (1 << n) - 1);
            }
            return this.next.countOnesBefore(n - 64) + Long.bitCount(this.mData);
        }

        boolean get(int n) {
            if (n >= 64) {
                this.ensureNext();
                return this.next.get(n - 64);
            }
            if ((this.mData & 1 << n) != 0) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        void insert(int n, boolean bl) {
            if (n >= 64) {
                this.ensureNext();
                this.next.insert(n - 64, bl);
                return;
            } else {
                boolean bl2 = (this.mData & Long.MIN_VALUE) != 0;
                long l = (1 << n) - 1;
                this.mData = this.mData & l | (this.mData & (-1 ^ l)) << 1;
                if (bl) {
                    this.set(n);
                } else {
                    this.clear(n);
                }
                if (!bl2 && this.next == null) return;
                {
                    this.ensureNext();
                    this.next.insert(0, bl2);
                    return;
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        boolean remove(int n) {
            if (n >= 64) {
                this.ensureNext();
                return this.next.remove(n - 64);
            }
            long l = 1 << n;
            boolean bl = (this.mData & l) != 0;
            this.mData &= -1 ^ l;
            this.mData = this.mData & l | Long.rotateRight(this.mData & (-1 ^ --l), 1);
            boolean bl2 = bl;
            if (this.next == null) return bl2;
            if (this.next.get(0)) {
                this.set(63);
            }
            this.next.remove(0);
            return bl;
        }

        void reset() {
            this.mData = 0;
            if (this.next != null) {
                this.next.reset();
            }
        }

        void set(int n) {
            if (n >= 64) {
                this.ensureNext();
                this.next.set(n - 64);
                return;
            }
            this.mData |= 1 << n;
        }

        public String toString() {
            if (this.next == null) {
                return Long.toBinaryString(this.mData);
            }
            return this.next.toString() + "xx" + Long.toBinaryString(this.mData);
        }
    }

    static interface Callback {
        public void addView(View var1, int var2);

        public void attachViewToParent(View var1, int var2, ViewGroup.LayoutParams var3);

        public void detachViewFromParent(int var1);

        public View getChildAt(int var1);

        public int getChildCount();

        public RecyclerView.ViewHolder getChildViewHolder(View var1);

        public int indexOfChild(View var1);

        public void onEnteredHiddenState(View var1);

        public void onLeftHiddenState(View var1);

        public void removeAllViews();

        public void removeViewAt(int var1);
    }

}

