/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.database.sqlite.SQLiteCursor
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.database.sqlite;

import android.database.sqlite.SQLiteCursor;
import android.os.Build;
import android.support.annotation.NonNull;

public final class SQLiteCursorCompat {
    private SQLiteCursorCompat() {
    }

    public static void setFillWindowForwardOnly(@NonNull SQLiteCursor sQLiteCursor, boolean bl) {
        if (Build.VERSION.SDK_INT >= 28) {
            sQLiteCursor.setFillWindowForwardOnly(bl);
        }
    }
}

