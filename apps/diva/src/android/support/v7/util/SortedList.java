/*
 * Decompiled with CFR 0_121.
 */
package android.support.v7.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mMergedSize;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;

    public SortedList(Class<T> class_, Callback<T> callback) {
        this(class_, callback, 10);
    }

    public SortedList(Class<T> class_, Callback<T> callback, int n) {
        this.mTClass = class_;
        this.mData = (Object[])Array.newInstance(class_, n);
        this.mCallback = callback;
        this.mSize = 0;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int add(T var1_1, boolean var2_2) {
        var4_3 = this.findIndexOf(var1_1, this.mData, 0, this.mSize, 1);
        if (var4_3 != -1) ** GOTO lbl5
        var3_4 = 0;
        ** GOTO lbl-1000
lbl5: // 1 sources:
        var3_4 = var4_3;
        if (var4_3 >= this.mSize) ** GOTO lbl-1000
        var5_5 = this.mData[var4_3];
        var3_4 = var4_3;
        if (this.mCallback.areItemsTheSame(var5_5, var1_1)) {
            if (this.mCallback.areContentsTheSame(var5_5, var1_1)) {
                this.mData[var4_3] = var1_1;
                return var4_3;
            }
        } else lbl-1000: // 3 sources:
        {
            this.addToData(var3_4, var1_1);
            if (var2_2 == false) return var3_4;
            this.mCallback.onInserted(var3_4, 1);
            return var3_4;
        }
        this.mData[var4_3] = var1_1;
        this.mCallback.onChanged(var4_3, 1);
        return var4_3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addAllInternal(T[] arrT) {
        boolean bl = !(this.mCallback instanceof BatchedCallback);
        if (bl) {
            this.beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        Arrays.sort(arrT, this.mCallback);
        int n = this.deduplicate(arrT);
        if (this.mSize == 0) {
            this.mData = arrT;
            this.mSize = n;
            this.mMergedSize = n;
            this.mCallback.onInserted(0, n);
        } else {
            this.merge(arrT, n);
        }
        this.mOldData = null;
        if (bl) {
            this.endBatchedUpdates();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void addToData(int n, T t) {
        if (n > this.mSize) {
            throw new IndexOutOfBoundsException("cannot add item to " + n + " because size is " + this.mSize);
        }
        if (this.mSize == this.mData.length) {
            Object[] arrobject = (Object[])Array.newInstance(this.mTClass, this.mData.length + 10);
            System.arraycopy(this.mData, 0, (Object)arrobject, 0, n);
            arrobject[n] = t;
            System.arraycopy(this.mData, n, (Object)arrobject, n + 1, this.mSize - n);
            this.mData = arrobject;
        } else {
            System.arraycopy(this.mData, n, this.mData, n + 1, this.mSize - n);
            this.mData[n] = t;
        }
        ++this.mSize;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int deduplicate(T[] arrT) {
        if (arrT.length == 0) {
            throw new IllegalArgumentException("Input array must be non-empty");
        }
        int n = 0;
        int n2 = 1;
        int n3 = 1;
        while (n3 < arrT.length) {
            T t = arrT[n3];
            int n4 = this.mCallback.compare(arrT[n], t);
            if (n4 > 0) {
                throw new IllegalArgumentException("Input must be sorted in ascending order.");
            }
            if (n4 == 0) {
                n4 = this.findSameItem(t, arrT, n, n2);
                if (n4 != -1) {
                    arrT[n4] = t;
                } else {
                    if (n2 != n3) {
                        arrT[n2] = t;
                    }
                    ++n2;
                }
            } else {
                if (n2 != n3) {
                    arrT[n2] = t;
                }
                n = n2++;
            }
            ++n3;
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int findIndexOf(T t, T[] arrT, int n, int n2, int n3) {
        while (n < n2) {
            int n4 = (n + n2) / 2;
            T t2 = arrT[n4];
            int n5 = this.mCallback.compare(t2, t);
            if (n5 < 0) {
                n = n4 + 1;
                continue;
            }
            if (n5 == 0) {
                if (this.mCallback.areItemsTheSame(t2, t)) {
                    return n4;
                }
                n = this.linearEqualitySearch(t, n4, n, n2);
                if (n3 != 1) return n;
                if (n == -1) return n4;
                return n;
            }
            n2 = n4;
        }
        if (n3 != 1) return -1;
        return n;
    }

    private int findSameItem(T t, T[] arrT, int n, int n2) {
        while (n < n2) {
            if (this.mCallback.areItemsTheSame(arrT[n], t)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int linearEqualitySearch(T t, int n, int n2, int n3) {
        T t2;
        int n4 = n - 1;
        do {
            if (n4 < n2 || this.mCallback.compare(t2 = this.mData[n4], t) != 0) {
                ++n;
                break;
            }
            if (this.mCallback.areItemsTheSame(t2, t)) {
                return n4;
            }
            --n4;
        } while (true);
        while (n < n3 && this.mCallback.compare(t2 = this.mData[n], t) == 0) {
            if (this.mCallback.areItemsTheSame(t2, t)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void merge(T[] arrT, int n) {
        int n2 = this.mSize;
        this.mData = (Object[])Array.newInstance(this.mTClass, n2 + n + 10);
        this.mMergedSize = 0;
        n2 = 0;
        while (!(this.mOldDataStart >= this.mOldDataSize && n2 >= n)) {
            if (this.mOldDataStart == this.mOldDataSize) {
                System.arraycopy(arrT, n2, this.mData, this.mMergedSize, n -= n2);
                this.mMergedSize += n;
                this.mSize += n;
                this.mCallback.onInserted(this.mMergedSize - n, n);
                return;
            }
            if (n2 == n) {
                n = this.mOldDataSize - this.mOldDataStart;
                System.arraycopy(this.mOldData, this.mOldDataStart, this.mData, this.mMergedSize, n);
                this.mMergedSize += n;
                return;
            }
            Object object = this.mOldData[this.mOldDataStart];
            Object object2 = arrT[n2];
            int n3 = this.mCallback.compare(object, object2);
            if (n3 > 0) {
                object = this.mData;
                n3 = this.mMergedSize;
                this.mMergedSize = n3 + 1;
                object[n3] = object2;
                ++this.mSize;
                ++n2;
                this.mCallback.onInserted(this.mMergedSize - 1, 1);
                continue;
            }
            if (n3 == 0 && this.mCallback.areItemsTheSame(object, object2)) {
                T[] arrT2 = this.mData;
                n3 = this.mMergedSize;
                this.mMergedSize = n3 + 1;
                arrT2[n3] = object2;
                n3 = n2 + 1;
                ++this.mOldDataStart;
                n2 = n3;
                if (this.mCallback.areContentsTheSame(object, object2)) continue;
                this.mCallback.onChanged(this.mMergedSize - 1, 1);
                n2 = n3;
                continue;
            }
            object2 = this.mData;
            n3 = this.mMergedSize;
            this.mMergedSize = n3 + 1;
            object2[n3] = object;
            ++this.mOldDataStart;
        }
    }

    private boolean remove(T t, boolean bl) {
        int n = this.findIndexOf(t, this.mData, 0, this.mSize, 2);
        if (n == -1) {
            return false;
        }
        this.removeItemAtIndex(n, bl);
        return true;
    }

    private void removeItemAtIndex(int n, boolean bl) {
        System.arraycopy(this.mData, n + 1, this.mData, n, this.mSize - n - 1);
        --this.mSize;
        this.mData[this.mSize] = null;
        if (bl) {
            this.mCallback.onRemoved(n, 1);
        }
    }

    private void throwIfMerging() {
        if (this.mOldData != null) {
            throw new IllegalStateException("Cannot call this method from within addAll");
        }
    }

    public int add(T t) {
        this.throwIfMerging();
        return this.add(t, true);
    }

    public void addAll(Collection<T> collection) {
        this.addAll((T[])collection.toArray((Object[])Array.newInstance(this.mTClass, collection.size())), true);
    }

    public /* varargs */ void addAll(T ... arrT) {
        this.addAll(arrT, false);
    }

    public void addAll(T[] arrT, boolean bl) {
        this.throwIfMerging();
        if (arrT.length == 0) {
            return;
        }
        if (bl) {
            this.addAllInternal(arrT);
            return;
        }
        Object[] arrobject = (Object[])Array.newInstance(this.mTClass, arrT.length);
        System.arraycopy(arrT, 0, (Object)arrobject, 0, arrT.length);
        this.addAllInternal(arrobject);
    }

    public void beginBatchedUpdates() {
        this.throwIfMerging();
        if (this.mCallback instanceof BatchedCallback) {
            return;
        }
        if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new BatchedCallback(this.mCallback);
        }
        this.mCallback = this.mBatchedCallback;
    }

    public void clear() {
        this.throwIfMerging();
        if (this.mSize == 0) {
            return;
        }
        int n = this.mSize;
        Arrays.fill(this.mData, 0, n, null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, n);
    }

    public void endBatchedUpdates() {
        this.throwIfMerging();
        if (this.mCallback instanceof BatchedCallback) {
            ((BatchedCallback)this.mCallback).dispatchLastEvent();
        }
        if (this.mCallback == this.mBatchedCallback) {
            this.mCallback = this.mBatchedCallback.mWrappedCallback;
        }
    }

    public T get(int n) throws IndexOutOfBoundsException {
        if (n >= this.mSize || n < 0) {
            throw new IndexOutOfBoundsException("Asked to get item at " + n + " but size is " + this.mSize);
        }
        if (this.mOldData != null && n >= this.mMergedSize) {
            return this.mOldData[n - this.mMergedSize + this.mOldDataStart];
        }
        return this.mData[n];
    }

    public int indexOf(T t) {
        if (this.mOldData != null) {
            int n = this.findIndexOf(t, this.mData, 0, this.mMergedSize, 4);
            if (n != -1) {
                return n;
            }
            n = this.findIndexOf(t, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            if (n != -1) {
                return n - this.mOldDataStart + this.mMergedSize;
            }
            return -1;
        }
        return this.findIndexOf(t, this.mData, 0, this.mSize, 4);
    }

    public void recalculatePositionOfItemAt(int n) {
        this.throwIfMerging();
        T t = this.get(n);
        this.removeItemAtIndex(n, false);
        int n2 = this.add(t, false);
        if (n != n2) {
            this.mCallback.onMoved(n, n2);
        }
    }

    public boolean remove(T t) {
        this.throwIfMerging();
        return this.remove(t, true);
    }

    public T removeItemAt(int n) {
        this.throwIfMerging();
        T t = this.get(n);
        this.removeItemAtIndex(n, true);
        return t;
    }

    public int size() {
        return this.mSize;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void updateItemAt(int n, T t) {
        int n2;
        block3 : {
            this.throwIfMerging();
            T t2 = this.get(n);
            n2 = t2 == t || !this.mCallback.areContentsTheSame(t2, t) ? 1 : 0;
            if (t2 != t) {
                if (this.mCallback.compare(t2, t) == 0) {
                    this.mData[n] = t;
                    if (n2 == 0) return;
                    this.mCallback.onChanged(n, 1);
                    return;
                }
                if (n2 == 0) break block3;
            }
            this.mCallback.onChanged(n, 1);
        }
        this.removeItemAtIndex(n, false);
        n2 = this.add(t, false);
        if (n == n2) return;
        this.mCallback.onMoved(n, n2);
    }

    public static class BatchedCallback<T2>
    extends Callback<T2> {
        static final int TYPE_ADD = 1;
        static final int TYPE_CHANGE = 3;
        static final int TYPE_MOVE = 4;
        static final int TYPE_NONE = 0;
        static final int TYPE_REMOVE = 2;
        int mLastEventCount = -1;
        int mLastEventPosition = -1;
        int mLastEventType = 0;
        private final Callback<T2> mWrappedCallback;

        public BatchedCallback(Callback<T2> callback) {
            this.mWrappedCallback = callback;
        }

        @Override
        public boolean areContentsTheSame(T2 T2, T2 T22) {
            return this.mWrappedCallback.areContentsTheSame(T2, T22);
        }

        @Override
        public boolean areItemsTheSame(T2 T2, T2 T22) {
            return this.mWrappedCallback.areItemsTheSame(T2, T22);
        }

        @Override
        public int compare(T2 T2, T2 T22) {
            return this.mWrappedCallback.compare(T2, T22);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void dispatchLastEvent() {
            if (this.mLastEventType == 0) {
                return;
            }
            switch (this.mLastEventType) {
                case 1: {
                    this.mWrappedCallback.onInserted(this.mLastEventPosition, this.mLastEventCount);
                    ** break;
                }
                case 2: {
                    this.mWrappedCallback.onRemoved(this.mLastEventPosition, this.mLastEventCount);
                }
lbl9: // 3 sources:
                default: {
                    ** GOTO lbl13
                }
                case 3: 
            }
            this.mWrappedCallback.onChanged(this.mLastEventPosition, this.mLastEventCount);
lbl13: // 2 sources:
            this.mLastEventType = 0;
        }

        @Override
        public void onChanged(int n, int n2) {
            if (this.mLastEventType == 3 && n <= this.mLastEventPosition + this.mLastEventCount && n + n2 >= this.mLastEventPosition) {
                int n3 = this.mLastEventPosition;
                int n4 = this.mLastEventCount;
                this.mLastEventPosition = Math.min(n, this.mLastEventPosition);
                this.mLastEventCount = Math.max(n3 + n4, n + n2) - this.mLastEventPosition;
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = n;
            this.mLastEventCount = n2;
            this.mLastEventType = 3;
        }

        @Override
        public void onInserted(int n, int n2) {
            if (this.mLastEventType == 1 && n >= this.mLastEventPosition && n <= this.mLastEventPosition + this.mLastEventCount) {
                this.mLastEventCount += n2;
                this.mLastEventPosition = Math.min(n, this.mLastEventPosition);
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = n;
            this.mLastEventCount = n2;
            this.mLastEventType = 1;
        }

        @Override
        public void onMoved(int n, int n2) {
            this.dispatchLastEvent();
            this.mWrappedCallback.onMoved(n, n2);
        }

        @Override
        public void onRemoved(int n, int n2) {
            if (this.mLastEventType == 2 && this.mLastEventPosition == n) {
                this.mLastEventCount += n2;
                return;
            }
            this.dispatchLastEvent();
            this.mLastEventPosition = n;
            this.mLastEventCount = n2;
            this.mLastEventType = 2;
        }
    }

    public static abstract class Callback<T2>
    implements Comparator<T2> {
        public abstract boolean areContentsTheSame(T2 var1, T2 var2);

        public abstract boolean areItemsTheSame(T2 var1, T2 var2);

        @Override
        public abstract int compare(T2 var1, T2 var2);

        public abstract void onChanged(int var1, int var2);

        public abstract void onInserted(int var1, int var2);

        public abstract void onMoved(int var1, int var2);

        public abstract void onRemoved(int var1, int var2);
    }

}

