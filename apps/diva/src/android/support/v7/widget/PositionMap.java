/*
 * Decompiled with CFR 0_121.
 */
package android.support.v7.widget;

import java.util.ArrayList;

class PositionMap<E>
implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage = false;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    public PositionMap() {
        this(10);
    }

    /*
     * Enabled aggressive block sorting
     */
    public PositionMap(int n) {
        if (n == 0) {
            this.mKeys = ContainerHelpers.EMPTY_INTS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            n = PositionMap.idealIntArraySize(n);
            this.mKeys = new int[n];
            this.mValues = new Object[n];
        }
        this.mSize = 0;
    }

    private void gc() {
        int n = this.mSize;
        int n2 = 0;
        int[] arrn = this.mKeys;
        Object[] arrobject = this.mValues;
        for (int i = 0; i < n; ++i) {
            Object object = arrobject[i];
            int n3 = n2;
            if (object != DELETED) {
                if (i != n2) {
                    arrn[n2] = arrn[i];
                    arrobject[n2] = object;
                    arrobject[i] = null;
                }
                n3 = n2 + 1;
            }
            n2 = n3;
        }
        this.mGarbage = false;
        this.mSize = n2;
    }

    static int idealBooleanArraySize(int n) {
        return PositionMap.idealByteArraySize(n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static int idealByteArraySize(int n) {
        int n2 = 4;
        do {
            int n3 = n;
            if (n2 >= 32) return n3;
            if (n <= (1 << n2) - 12) {
                return (1 << n2) - 12;
            }
            ++n2;
        } while (true);
    }

    static int idealCharArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 2) / 2;
    }

    static int idealFloatArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 4) / 4;
    }

    static int idealIntArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 4) / 4;
    }

    static int idealLongArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 8) / 8;
    }

    static int idealObjectArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 4) / 4;
    }

    static int idealShortArraySize(int n) {
        return PositionMap.idealByteArraySize(n * 2) / 2;
    }

    public void append(int n, E e) {
        int n2;
        if (this.mSize != 0 && n <= this.mKeys[this.mSize - 1]) {
            this.put(n, e);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        if ((n2 = this.mSize) >= this.mKeys.length) {
            int n3 = PositionMap.idealIntArraySize(n2 + 1);
            int[] arrn = new int[n3];
            Object[] arrobject = new Object[n3];
            System.arraycopy((Object)this.mKeys, 0, (Object)arrn, 0, this.mKeys.length);
            System.arraycopy((Object)this.mValues, 0, (Object)arrobject, 0, this.mValues.length);
            this.mKeys = arrn;
            this.mValues = arrobject;
        }
        this.mKeys[n2] = n;
        this.mValues[n2] = e;
        this.mSize = n2 + 1;
    }

    public void clear() {
        int n = this.mSize;
        Object[] arrobject = this.mValues;
        for (int i = 0; i < n; ++i) {
            arrobject[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public PositionMap<E> clone() {
        PositionMap positionMap;
        PositionMap positionMap2 = null;
        try {
            positionMap2 = positionMap = (PositionMap)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return positionMap2;
        }
        positionMap.mKeys = (int[])this.mKeys.clone();
        positionMap2 = positionMap;
        positionMap.mValues = (Object[])this.mValues.clone();
        return positionMap;
    }

    public void delete(int n) {
        if ((n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n)) >= 0 && this.mValues[n] != DELETED) {
            this.mValues[n] = DELETED;
            this.mGarbage = true;
        }
    }

    public E get(int n) {
        return this.get(n, null);
    }

    public E get(int n, E e) {
        if ((n = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n)) < 0 || this.mValues[n] == DELETED) {
            return e;
        }
        return (E)this.mValues[n];
    }

    public int indexOfKey(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
    }

    public int indexOfValue(E e) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mValues[i] != e) continue;
            return i;
        }
        return -1;
    }

    public void insertKeyRange(int n, int n2) {
    }

    public int keyAt(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }

    public void put(int n, E e) {
        int n2 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (n2 >= 0) {
            this.mValues[n2] = e;
            return;
        }
        int n3 = ~ n2;
        if (n3 < this.mSize && this.mValues[n3] == DELETED) {
            this.mKeys[n3] = n;
            this.mValues[n3] = e;
            return;
        }
        n2 = n3;
        if (this.mGarbage) {
            n2 = n3;
            if (this.mSize >= this.mKeys.length) {
                this.gc();
                n2 = ~ ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
            }
        }
        if (this.mSize >= this.mKeys.length) {
            n3 = PositionMap.idealIntArraySize(this.mSize + 1);
            int[] arrn = new int[n3];
            Object[] arrobject = new Object[n3];
            System.arraycopy((Object)this.mKeys, 0, (Object)arrn, 0, this.mKeys.length);
            System.arraycopy((Object)this.mValues, 0, (Object)arrobject, 0, this.mValues.length);
            this.mKeys = arrn;
            this.mValues = arrobject;
        }
        if (this.mSize - n2 != 0) {
            System.arraycopy((Object)this.mKeys, n2, (Object)this.mKeys, n2 + 1, this.mSize - n2);
            System.arraycopy((Object)this.mValues, n2, (Object)this.mValues, n2 + 1, this.mSize - n2);
        }
        this.mKeys[n2] = n;
        this.mValues[n2] = e;
        ++this.mSize;
    }

    public void remove(int n) {
        this.delete(n);
    }

    public void removeAt(int n) {
        if (this.mValues[n] != DELETED) {
            this.mValues[n] = DELETED;
            this.mGarbage = true;
        }
    }

    public void removeAtRange(int n, int n2) {
        n2 = Math.min(this.mSize, n + n2);
        while (n < n2) {
            this.removeAt(n);
            ++n;
        }
    }

    public void removeKeyRange(ArrayList<E> arrayList, int n, int n2) {
    }

    public void setValueAt(int n, E e) {
        if (this.mGarbage) {
            this.gc();
        }
        this.mValues[n] = e;
    }

    public int size() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        if (this.size() <= 0) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
        stringBuilder.append('{');
        int n = 0;
        do {
            if (n >= this.mSize) {
                stringBuilder.append('}');
                return stringBuilder.toString();
            }
            if (n > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(this.keyAt(n));
            stringBuilder.append('=');
            E e = this.valueAt(n);
            if (e != this) {
                stringBuilder.append(e);
            } else {
                stringBuilder.append("(this Map)");
            }
            ++n;
        } while (true);
    }

    public E valueAt(int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return (E)this.mValues[n];
    }

    static class ContainerHelpers {
        static final boolean[] EMPTY_BOOLEANS = new boolean[0];
        static final int[] EMPTY_INTS = new int[0];
        static final long[] EMPTY_LONGS = new long[0];
        static final Object[] EMPTY_OBJECTS = new Object[0];

        ContainerHelpers() {
        }

        static int binarySearch(int[] arrn, int n, int n2) {
            int n3;
            block3 : {
                n3 = 0;
                int n4 = n - 1;
                n = n3;
                n3 = n4;
                while (n <= n3) {
                    n4 = n + n3 >>> 1;
                    int n5 = arrn[n4];
                    if (n5 < n2) {
                        n = n4 + 1;
                        continue;
                    }
                    n3 = n4;
                    if (n5 > n2) {
                        n3 = n4 - 1;
                        continue;
                    }
                    break block3;
                }
                n3 = ~ n;
            }
            return n3;
        }
    }

}

