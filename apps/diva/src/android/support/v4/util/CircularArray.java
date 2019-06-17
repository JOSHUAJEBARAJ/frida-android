/*
 * Decompiled with CFR 0_121.
 */
package android.support.v4.util;

public final class CircularArray<E> {
    private int mCapacityBitmask;
    private E[] mElements;
    private int mHead;
    private int mTail;

    public CircularArray() {
        this(8);
    }

    public CircularArray(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        int n2 = n;
        if (Integer.bitCount(n) != 1) {
            n2 = 1 << Integer.highestOneBit(n) + 1;
        }
        this.mCapacityBitmask = n2 - 1;
        this.mElements = new Object[n2];
    }

    private void doubleCapacity() {
        int n = this.mElements.length;
        int n2 = n - this.mHead;
        int n3 = n << 1;
        if (n3 < 0) {
            throw new RuntimeException("Max array capacity exceeded");
        }
        Object[] arrobject = new Object[n3];
        System.arraycopy(this.mElements, this.mHead, (Object)arrobject, 0, n2);
        System.arraycopy(this.mElements, 0, (Object)arrobject, n2, this.mHead);
        this.mElements = arrobject;
        this.mHead = 0;
        this.mTail = n;
        this.mCapacityBitmask = n3 - 1;
    }

    public void addFirst(E e) {
        this.mHead = this.mHead - 1 & this.mCapacityBitmask;
        this.mElements[this.mHead] = e;
        if (this.mHead == this.mTail) {
            this.doubleCapacity();
        }
    }

    public void addLast(E e) {
        this.mElements[this.mTail] = e;
        this.mTail = this.mTail + 1 & this.mCapacityBitmask;
        if (this.mTail == this.mHead) {
            this.doubleCapacity();
        }
    }

    public void clear() {
        this.removeFromStart(this.size());
    }

    public E get(int n) {
        if (n < 0 || n >= this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mHead + n & this.mCapacityBitmask];
    }

    public E getFirst() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mHead];
    }

    public E getLast() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mTail - 1 & this.mCapacityBitmask];
    }

    public boolean isEmpty() {
        if (this.mHead == this.mTail) {
            return true;
        }
        return false;
    }

    public E popFirst() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        E e = this.mElements[this.mHead];
        this.mElements[this.mHead] = null;
        this.mHead = this.mHead + 1 & this.mCapacityBitmask;
        return e;
    }

    public E popLast() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int n = this.mTail - 1 & this.mCapacityBitmask;
        E e = this.mElements[n];
        this.mElements[n] = null;
        this.mTail = n;
        return e;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void removeFromEnd(int n) {
        if (n <= 0) {
            return;
        }
        if (n > this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int n2 = 0;
        if (n < this.mTail) {
            n2 = this.mTail - n;
        }
        for (int i = n2; i < this.mTail; ++i) {
            this.mElements[i] = null;
        }
        n2 = this.mTail - n2;
        this.mTail -= n2;
        if ((n -= n2) <= 0) return;
        this.mTail = this.mElements.length;
        n = n2 = this.mTail - n;
        do {
            if (n >= this.mTail) {
                this.mTail = n2;
                return;
            }
            this.mElements[n] = null;
            ++n;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void removeFromStart(int n) {
        int n2;
        if (n <= 0) {
            return;
        }
        if (n > this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int n3 = n2 = this.mElements.length;
        if (n < n2 - this.mHead) {
            n3 = this.mHead + n;
        }
        for (n2 = this.mHead; n2 < n3; ++n2) {
            this.mElements[n2] = null;
        }
        n2 = n3 - this.mHead;
        n3 = n - n2;
        this.mHead = this.mHead + n2 & this.mCapacityBitmask;
        if (n3 <= 0) return;
        n = 0;
        do {
            if (n >= n3) {
                this.mHead = n3;
                return;
            }
            this.mElements[n] = null;
            ++n;
        } while (true);
    }

    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}

