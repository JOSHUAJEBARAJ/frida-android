/*
 * Decompiled with CFR 0_121.
 */
package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;

public class Pair<F, S> {
    @Nullable
    public final F first;
    @Nullable
    public final S second;

    public Pair(@Nullable F f, @Nullable S s) {
        this.first = f;
        this.second = s;
    }

    @NonNull
    public static <A, B> Pair<A, B> create(@Nullable A a2, @Nullable B b) {
        return new Pair<A, B>(a2, b);
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof Pair;
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        object = (Pair)object;
        bl = bl2;
        if (ObjectsCompat.equals(object.first, this.first)) {
            bl = bl2;
            if (ObjectsCompat.equals(object.second, this.second)) {
                bl = true;
            }
        }
        return bl;
    }

    public int hashCode() {
        Object object = this.first;
        int n = 0;
        int n2 = object == null ? 0 : object.hashCode();
        object = this.second;
        if (object != null) {
            n = object.hashCode();
        }
        return n2 ^ n;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pair{");
        stringBuilder.append(String.valueOf(this.first));
        stringBuilder.append(" ");
        stringBuilder.append(String.valueOf(this.second));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

