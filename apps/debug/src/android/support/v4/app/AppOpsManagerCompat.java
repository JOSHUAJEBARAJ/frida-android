/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.AppOpsManager
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class AppOpsManagerCompat {
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_IGNORED = 1;

    private AppOpsManagerCompat() {
    }

    public static int noteOp(@NonNull Context context, @NonNull String string2, int n, @NonNull String string3) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ((AppOpsManager)context.getSystemService("appops")).noteOp(string2, n, string3);
        }
        return 1;
    }

    public static int noteOpNoThrow(@NonNull Context context, @NonNull String string2, int n, @NonNull String string3) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ((AppOpsManager)context.getSystemService("appops")).noteOpNoThrow(string2, n, string3);
        }
        return 1;
    }

    public static int noteProxyOp(@NonNull Context context, @NonNull String string2, @NonNull String string3) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ((AppOpsManager)context.getSystemService(AppOpsManager.class)).noteProxyOp(string2, string3);
        }
        return 1;
    }

    public static int noteProxyOpNoThrow(@NonNull Context context, @NonNull String string2, @NonNull String string3) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ((AppOpsManager)context.getSystemService(AppOpsManager.class)).noteProxyOpNoThrow(string2, string3);
        }
        return 1;
    }

    @Nullable
    public static String permissionToOp(@NonNull String string2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return AppOpsManager.permissionToOp((String)string2);
        }
        return null;
    }
}

