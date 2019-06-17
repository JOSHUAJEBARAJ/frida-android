/*
 * Decompiled with CFR 0_121.
 */
package android.support.v4.util;

public final class CircularIntArray {
    private int mCapacityBitmask;
    private int[] mElements;
    private int mHead;
    private int mTail;

    public CircularIntArray() {
        this(8);
    }

    public CircularIntArray(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        int n2 = n;
        if (Integer.bitCount(n) != 1) {
            n2 = 1 << Integer.highestOneBit(n) + 1;
        }
        this.mCapacityBitmask = n2 - 1;
        this.mElements = new int[n2];
    }

    private void doubleCapacity() {
        int n = this.mElements.length;
        int n2 = n - this.mHead;
        int n3 = n << 1;
        if (n3 < 0) {
            throw new RuntimeException("Max array capacity exceeded");
        }
        int[] arrn = new int[n3];
        System.arraycopy((Object)this.mElements, this.mHead, (Object)arrn, 0, n2);
        System.arraycopy((Object)this.mElements, 0, (Object)arrn, n2, this.mHead);
        this.mElements = arrn;
        this.mHead = 0;
        this.mTail = n;
        this.mCapacityBitmask = n3 - 1;
    }

    public void addFirst(int n) {
        this.mHead = this.mHead - 1 & this.mCapacityBitmask;
        this.mElements[this.mHead] = n;
        if (this.mHead == this.mTail) {
            this.doubleCapacity();
        }
    }

    public void addLast(int n) {
        this.mElements[this.mTail] = n;
        this.mTail = this.mTail + 1 & this.mCapacityBitmask;
        if (this.mTail == this.mHead) {
            this.doubleCapacity();
        }
    }

    public void clear() {
        this.mTail = this.mHead;
    }

    public int get(int n) {
        if (n < 0 || n >= this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mHead + n & this.mCapacityBitmask];
    }

    public int getFirst() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mHead];
    }

    public int getLast() {
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

    public int popFirst() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int n = this.mElements[this.mHead];
        this.mHead = this.mHead + 1 & this.mCapacityBitmask;
        return n;
    }

    public int popLast() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int n = this.mTail - 1 & this.mCapacityBitmask;
        int n2 = this.mElements[n];
        this.mTail = n;
        return n2;
    }

    public void removeFromEnd(int n) {
        if (n <= 0) {
            return;
        }
        if (n > this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.mTail = this.mTail - n & this.mCapacityBitmask;
    }

    public void removeFromStart(int n) {
        if (n <= 0) {
            return;
        }
        if (n > this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.mHead = this.mHead + n & this.mCapacityBitmask;
    }

    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}

