/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.util;

import android.os.Build;
import android.support.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public class ObjectsCompat {
    private ObjectsCompat() {
    }

    public static boolean equals(@Nullable Object object, @Nullable Object object2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Objects.equals(object, object2);
        }
        if (!(object == object2 || object != null && object.equals(object2))) {
            return false;
        }
        return true;
    }

    public static /* varargs */ int hash(@Nullable Object ... arrobject) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Objects.hash(arrobject);
        }
        return Arrays.hashCode(arrobject);
    }

    public static int hashCode(@Nullable Object object) {
        if (object != null) {
            return object.hashCode();
        }
        return 0;
    }
}

