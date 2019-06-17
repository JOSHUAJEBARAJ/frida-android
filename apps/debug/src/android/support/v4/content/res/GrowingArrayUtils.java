/*
 * Decompiled with CFR 0_121.
 */
package android.support.v4.content.res;

import java.lang.reflect.Array;

final class GrowingArrayUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    private GrowingArrayUtils() {
    }

    public static int[] append(int[] arrn, int n, int n2) {
        int[] arrn2 = arrn;
        if (n + 1 > arrn.length) {
            arrn2 = new int[GrowingArrayUtils.growSize(n)];
            System.arraycopy((Object)arrn, 0, (Object)arrn2, 0, n);
        }
        arrn2[n] = n2;
        return arrn2;
    }

    public static long[] append(long[] arrl, int n, long l) {
        long[] arrl2 = arrl;
        if (n + 1 > arrl.length) {
            arrl2 = new long[GrowingArrayUtils.growSize(n)];
            System.arraycopy((Object)arrl, 0, (Object)arrl2, 0, n);
        }
        arrl2[n] = l;
        return arrl2;
    }

    public static <T> T[] append(T[] arrT, int n, T t) {
        T[] arrT2 = arrT;
        if (n + 1 > arrT.length) {
            arrT2 = (Object[])Array.newInstance(arrT.getClass().getComponentType(), GrowingArrayUtils.growSize(n));
            System.arraycopy(arrT, 0, arrT2, 0, n);
        }
        arrT2[n] = t;
        return arrT2;
    }

    public static boolean[] append(boolean[] arrbl, int n, boolean bl) {
        boolean[] arrbl2 = arrbl;
        if (n + 1 > arrbl.length) {
            arrbl2 = new boolean[GrowingArrayUtils.growSize(n)];
            System.arraycopy((Object)arrbl, 0, (Object)arrbl2, 0, n);
        }
        arrbl2[n] = bl;
        return arrbl2;
    }

    public static int growSize(int n) {
        if (n <= 4) {
            return 8;
        }
        return n * 2;
    }

    public static int[] insert(int[] arrn, int n, int n2, int n3) {
        if (n + 1 <= arrn.length) {
            System.arraycopy((Object)arrn, n2, (Object)arrn, n2 + 1, n - n2);
            arrn[n2] = n3;
            return arrn;
        }
        int[] arrn2 = new int[GrowingArrayUtils.growSize(n)];
        System.arraycopy((Object)arrn, 0, (Object)arrn2, 0, n2);
        arrn2[n2] = n3;
        System.arraycopy((Object)arrn, n2, (Object)arrn2, n2 + 1, arrn.length - n2);
        return arrn2;
    }

    public static long[] insert(long[] arrl, int n, int n2, long l) {
        if (n + 1 <= arrl.length) {
            System.arraycopy((Object)arrl, n2, (Object)arrl, n2 + 1, n - n2);
            arrl[n2] = l;
            return arrl;
        }
        long[] arrl2 = new long[GrowingArrayUtils.growSize(n)];
        System.arraycopy((Object)arrl, 0, (Object)arrl2, 0, n2);
        arrl2[n2] = l;
        System.arraycopy((Object)arrl, n2, (Object)arrl2, n2 + 1, arrl.length - n2);
        return arrl2;
    }

    public static <T> T[] insert(T[] arrT, int n, int n2, T t) {
        if (n + 1 <= arrT.length) {
            System.arraycopy(arrT, n2, arrT, n2 + 1, n - n2);
            arrT[n2] = t;
            return arrT;
        }
        Object[] arrobject = (Object[])Array.newInstance(arrT.getClass().getComponentType(), GrowingArrayUtils.growSize(n));
        System.arraycopy(arrT, 0, (Object)arrobject, 0, n2);
        arrobject[n2] = t;
        System.arraycopy(arrT, n2, (Object)arrobject, n2 + 1, arrT.length - n2);
        return arrobject;
    }

    public static boolean[] insert(boolean[] arrbl, int n, int n2, boolean bl) {
        if (n + 1 <= arrbl.length) {
            System.arraycopy((Object)arrbl, n2, (Object)arrbl, n2 + 1, n - n2);
            arrbl[n2] = bl;
            return arrbl;
        }
        boolean[] arrbl2 = new boolean[GrowingArrayUtils.growSize(n)];
        System.arraycopy((Object)arrbl, 0, (Object)arrbl2, 0, n2);
        arrbl2[n2] = bl;
        System.arraycopy((Object)arrbl, n2, (Object)arrbl2, n2 + 1, arrbl.length - n2);
        return arrbl2;
    }
}

