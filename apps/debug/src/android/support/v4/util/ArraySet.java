/*
 * Decompiled with CFR 0_121.
 */
package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ContainerHelpers;
import android.support.v4.util.MapCollections;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet<E>
implements Collection<E>,
Set<E> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final int[] INT = new int[0];
    private static final Object[] OBJECT = new Object[0];
    private static final String TAG = "ArraySet";
    @Nullable
    private static Object[] sBaseCache;
    private static int sBaseCacheSize;
    @Nullable
    private static Object[] sTwiceBaseCache;
    private static int sTwiceBaseCacheSize;
    Object[] mArray;
    private MapCollections<E, E> mCollections;
    private int[] mHashes;
    int mSize;

    public ArraySet() {
        this(0);
    }

    public ArraySet(int n) {
        if (n == 0) {
            this.mHashes = INT;
            this.mArray = OBJECT;
        } else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }

    public ArraySet(@Nullable ArraySet<E> arraySet) {
        this();
        if (arraySet != null) {
            this.addAll(arraySet);
        }
    }

    public ArraySet(@Nullable Collection<E> collection) {
        this();
        if (collection != null) {
            this.addAll(collection);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void allocArrays(int n) {
        if (n == 8) {
            synchronized (ArraySet.class) {
                if (sTwiceBaseCache != null) {
                    Object[] arrobject = sTwiceBaseCache;
                    this.mArray = arrobject;
                    sTwiceBaseCache = (Object[])arrobject[0];
                    this.mHashes = (int[])arrobject[1];
                    arrobject[1] = null;
                    arrobject[0] = null;
                    --sTwiceBaseCacheSize;
                    return;
                }
            }
        } else if (n == 4) {
            synchronized (ArraySet.class) {
                if (sBaseCache != null) {
                    Object[] arrobject = sBaseCache;
                    this.mArray = arrobject;
                    sBaseCache = (Object[])arrobject[0];
                    this.mHashes = (int[])arrobject[1];
                    arrobject[1] = null;
                    arrobject[0] = null;
                    --sBaseCacheSize;
                    return;
                }
            }
        }
        this.mHashes = new int[n];
        this.mArray = new Object[n];
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static void freeArrays(int[] var0, Object[] var1_1, int var2_2) {
        if (var0.length != 8) ** GOTO lbl8
        // MONITORENTER : android.support.v4.util.ArraySet.class
        if (ArraySet.sTwiceBaseCacheSize >= 10) ** GOTO lbl21
        var1_1[0] = ArraySet.sTwiceBaseCache;
        var1_1[1] = var0;
        --var2_2;
        ** GOTO lbl15
lbl8: // 1 sources:
        if (var0.length != 4) return;
        // MONITORENTER : android.support.v4.util.ArraySet.class
        if (ArraySet.sBaseCacheSize >= 10) ** GOTO lbl29
        var1_1[0] = ArraySet.sBaseCache;
        var1_1[1] = var0;
        --var2_2;
        ** GOTO lbl23
lbl15: // 2 sources:
        while (var2_2 >= 2) {
            var1_1[var2_2] = null;
            --var2_2;
        }
        ArraySet.sTwiceBaseCache = var1_1;
        ++ArraySet.sTwiceBaseCacheSize;
lbl21: // 2 sources:
        // MONITOREXIT : android.support.v4.util.ArraySet.class
        return;
lbl23: // 2 sources:
        while (var2_2 >= 2) {
            var1_1[var2_2] = null;
            --var2_2;
        }
        ArraySet.sBaseCache = var1_1;
        ++ArraySet.sBaseCacheSize;
lbl29: // 2 sources:
        // MONITOREXIT : android.support.v4.util.ArraySet.class
    }

    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<E, E>(){

                @Override
                protected void colClear() {
                    ArraySet.this.clear();
                }

                @Override
                protected Object colGetEntry(int n, int n2) {
                    return ArraySet.this.mArray[n];
                }

                @Override
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override
                protected int colGetSize() {
                    return ArraySet.this.mSize;
                }

                @Override
                protected int colIndexOfKey(Object object) {
                    return ArraySet.this.indexOf(object);
                }

                @Override
                protected int colIndexOfValue(Object object) {
                    return ArraySet.this.indexOf(object);
                }

                @Override
                protected void colPut(E e, E e2) {
                    ArraySet.this.add(e);
                }

                @Override
                protected void colRemoveAt(int n) {
                    ArraySet.this.removeAt(n);
                }

                @Override
                protected E colSetValue(int n, E e) {
                    throw new UnsupportedOperationException("not a map");
                }
            };
        }
        return this.mCollections;
    }

    private int indexOf(Object object, int n) {
        int n2;
        int n3 = this.mSize;
        if (n3 == 0) {
            return -1;
        }
        int n4 = ContainerHelpers.binarySearch(this.mHashes, n3, n);
        if (n4 < 0) {
            return n4;
        }
        if (object.equals(this.mArray[n4])) {
            return n4;
        }
        for (n2 = n4 + 1; n2 < n3 && this.mHashes[n2] == n; ++n2) {
            if (!object.equals(this.mArray[n2])) continue;
            return n2;
        }
        for (n3 = n4 - 1; n3 >= 0 && this.mHashes[n3] == n; --n3) {
            if (!object.equals(this.mArray[n3])) continue;
            return n3;
        }
        return ~ n2;
    }

    private int indexOfNull() {
        int n;
        int n2 = this.mSize;
        if (n2 == 0) {
            return -1;
        }
        int n3 = ContainerHelpers.binarySearch(this.mHashes, n2, 0);
        if (n3 < 0) {
            return n3;
        }
        if (this.mArray[n3] == null) {
            return n3;
        }
        for (n = n3 + 1; n < n2 && this.mHashes[n] == 0; ++n) {
            if (this.mArray[n] != null) continue;
            return n;
        }
        for (n2 = n3 - 1; n2 >= 0 && this.mHashes[n2] == 0; --n2) {
            if (this.mArray[n2] != null) continue;
            return n2;
        }
        return ~ n;
    }

    @Override
    public boolean add(@Nullable E e) {
        int n;
        int n2;
        int[] arrn;
        if (e == null) {
            n2 = 0;
            n = this.indexOfNull();
        } else {
            n2 = e.hashCode();
            n = this.indexOf(e, n2);
        }
        if (n >= 0) {
            return false;
        }
        int n3 = ~ n;
        int n4 = this.mSize;
        if (n4 >= this.mHashes.length) {
            n = 4;
            if (n4 >= 8) {
                n = (n4 >> 1) + n4;
            } else if (n4 >= 4) {
                n = 8;
            }
            arrn = this.mHashes;
            Object[] arrobject = this.mArray;
            this.allocArrays(n);
            int[] arrn2 = this.mHashes;
            if (arrn2.length > 0) {
                System.arraycopy((Object)arrn, 0, (Object)arrn2, 0, arrn.length);
                System.arraycopy((Object)arrobject, 0, (Object)this.mArray, 0, arrobject.length);
            }
            ArraySet.freeArrays(arrn, arrobject, this.mSize);
        }
        if (n3 < (n = this.mSize)) {
            arrn = this.mHashes;
            System.arraycopy((Object)arrn, n3, (Object)arrn, n3 + 1, n - n3);
            arrn = this.mArray;
            System.arraycopy((Object)arrn, n3, (Object)arrn, n3 + 1, this.mSize - n3);
        }
        this.mHashes[n3] = n2;
        this.mArray[n3] = e;
        ++this.mSize;
        return true;
    }

    public void addAll(@NonNull ArraySet<? extends E> arraySet) {
        int n = arraySet.mSize;
        this.ensureCapacity(this.mSize + n);
        if (this.mSize == 0) {
            if (n > 0) {
                System.arraycopy((Object)arraySet.mHashes, 0, (Object)this.mHashes, 0, n);
                System.arraycopy((Object)arraySet.mArray, 0, (Object)this.mArray, 0, n);
                this.mSize = n;
                return;
            }
        } else {
            for (int i = 0; i < n; ++i) {
                this.add(arraySet.valueAt(i));
            }
        }
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> object) {
        this.ensureCapacity(this.mSize + object.size());
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            bl |= this.add(object.next());
        }
        return bl;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void append(E e) {
        int n = this.mSize;
        int n2 = e == null ? 0 : e.hashCode();
        int[] arrn = this.mHashes;
        if (n < arrn.length) {
            if (n > 0 && arrn[n - 1] > n2) {
                this.add(e);
                return;
            }
            this.mSize = n + 1;
            this.mHashes[n] = n2;
            this.mArray[n] = e;
            return;
        }
        throw new IllegalStateException("Array is full");
    }

    @Override
    public void clear() {
        int n = this.mSize;
        if (n != 0) {
            ArraySet.freeArrays(this.mHashes, this.mArray, n);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        }
    }

    @Override
    public boolean contains(@Nullable Object object) {
        if (this.indexOf(object) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> object) {
        object = object.iterator();
        while (object.hasNext()) {
            if (this.contains(object.next())) continue;
            return false;
        }
        return true;
    }

    public void ensureCapacity(int n) {
        if (this.mHashes.length < n) {
            int[] arrn = this.mHashes;
            Object[] arrobject = this.mArray;
            this.allocArrays(n);
            n = this.mSize;
            if (n > 0) {
                System.arraycopy((Object)arrn, 0, (Object)this.mHashes, 0, n);
                System.arraycopy((Object)arrobject, 0, (Object)this.mArray, 0, this.mSize);
            }
            ArraySet.freeArrays(arrn, arrobject, this.mSize);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        object = (Set)object;
        if (this.size() != object.size()) {
            return false;
        }
        int n = 0;
        try {
            do {
                if (n >= this.mSize) {
                    return true;
                }
                boolean bl = object.contains(this.valueAt(n));
                if (!bl) {
                    return false;
                }
                ++n;
            } while (true);
        }
        catch (ClassCastException classCastException) {
            return false;
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int[] arrn = this.mHashes;
        int n = 0;
        int n2 = this.mSize;
        for (int i = 0; i < n2; ++i) {
            n += arrn[i];
        }
        return n;
    }

    public int indexOf(@Nullable Object object) {
        if (object == null) {
            return this.indexOfNull();
        }
        return this.indexOf(object, object.hashCode());
    }

    @Override
    public boolean isEmpty() {
        if (this.mSize <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return this.getCollection().getKeySet().iterator();
    }

    @Override
    public boolean remove(@Nullable Object object) {
        int n = this.indexOf(object);
        if (n >= 0) {
            this.removeAt(n);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(@NonNull ArraySet<? extends E> arraySet) {
        int n = arraySet.mSize;
        int n2 = this.mSize;
        for (int i = 0; i < n; ++i) {
            this.remove(arraySet.valueAt(i));
        }
        if (n2 != this.mSize) {
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> object) {
        boolean bl = false;
        object = object.iterator();
        while (object.hasNext()) {
            bl |= this.remove(object.next());
        }
        return bl;
    }

    public E removeAt(int n) {
        Object[] arrobject = this.mArray;
        Object object = arrobject[n];
        int n2 = this.mSize;
        if (n2 <= 1) {
            ArraySet.freeArrays(this.mHashes, arrobject, n2);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
            return (E)object;
        }
        arrobject = this.mHashes;
        int n3 = arrobject.length;
        int n4 = 8;
        if (n3 > 8 && n2 < arrobject.length / 3) {
            if (n2 > 8) {
                n4 = n2 + (n2 >> 1);
            }
            arrobject = this.mHashes;
            Object[] arrobject2 = this.mArray;
            this.allocArrays(n4);
            --this.mSize;
            if (n > 0) {
                System.arraycopy((Object)arrobject, 0, (Object)this.mHashes, 0, n);
                System.arraycopy((Object)arrobject2, 0, (Object)this.mArray, 0, n);
            }
            if (n < (n4 = this.mSize)) {
                System.arraycopy((Object)arrobject, n + 1, (Object)this.mHashes, n, n4 - n);
                System.arraycopy((Object)arrobject2, n + 1, (Object)this.mArray, n, this.mSize - n);
            }
            return (E)object;
        }
        --this.mSize;
        n4 = this.mSize;
        if (n < n4) {
            arrobject = this.mHashes;
            System.arraycopy((Object)arrobject, n + 1, (Object)arrobject, n, n4 - n);
            arrobject = this.mArray;
            System.arraycopy((Object)arrobject, n + 1, (Object)arrobject, n, this.mSize - n);
        }
        this.mArray[this.mSize] = null;
        return (E)object;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        boolean bl = false;
        for (int i = this.mSize - 1; i >= 0; --i) {
            if (collection.contains(this.mArray[i])) continue;
            this.removeAt(i);
            bl = true;
        }
        return bl;
    }

    @Override
    public int size() {
        return this.mSize;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        int n = this.mSize;
        Object[] arrobject = new Object[n];
        System.arraycopy((Object)this.mArray, 0, (Object)arrobject, 0, n);
        return arrobject;
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] arrT) {
        T[] arrT2 = arrT;
        if (arrT.length < this.mSize) {
            arrT2 = (Object[])Array.newInstance(arrT.getClass().getComponentType(), this.mSize);
        }
        System.arraycopy((Object)this.mArray, 0, arrT2, 0, this.mSize);
        int n = arrT2.length;
        int n2 = this.mSize;
        if (n > n2) {
            arrT2[n2] = null;
        }
        return arrT2;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.mSize * 14);
        stringBuilder.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            E e;
            if (i > 0) {
                stringBuilder.append(", ");
            }
            if ((e = this.valueAt(i)) != this) {
                stringBuilder.append(e);
                continue;
            }
            stringBuilder.append("(this Set)");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Nullable
    public E valueAt(int n) {
        return (E)this.mArray[n];
    }

}

