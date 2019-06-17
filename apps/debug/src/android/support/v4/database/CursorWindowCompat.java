/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.database.CursorWindow
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.database;

import android.database.CursorWindow;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class CursorWindowCompat {
    private CursorWindowCompat() {
    }

    @NonNull
    public static CursorWindow create(@Nullable String string2, long l) {
        if (Build.VERSION.SDK_INT >= 28) {
            return new CursorWindow(string2, l);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return new CursorWindow(string2);
        }
        return new CursorWindow(false);
    }
}

