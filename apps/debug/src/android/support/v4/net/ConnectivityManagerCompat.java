/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 */
package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.RestrictTo;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ConnectivityManagerCompat {
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

    private ConnectivityManagerCompat() {
    }

    @Nullable
    @RequiresPermission(value="android.permission.ACCESS_NETWORK_STATE")
    public static NetworkInfo getNetworkInfoFromBroadcast(@NonNull ConnectivityManager connectivityManager, @NonNull Intent intent) {
        if ((intent = (NetworkInfo)intent.getParcelableExtra("networkInfo")) != null) {
            return connectivityManager.getNetworkInfo(intent.getType());
        }
        return null;
    }

    public static int getRestrictBackgroundStatus(@NonNull ConnectivityManager connectivityManager) {
        if (Build.VERSION.SDK_INT >= 24) {
            return connectivityManager.getRestrictBackgroundStatus();
        }
        return 3;
    }

    @RequiresPermission(value="android.permission.ACCESS_NETWORK_STATE")
    public static boolean isActiveNetworkMetered(@NonNull ConnectivityManager connectivityManager) {
        if (Build.VERSION.SDK_INT >= 16) {
            return connectivityManager.isActiveNetworkMetered();
        }
        if ((connectivityManager = connectivityManager.getActiveNetworkInfo()) == null) {
            return true;
        }
        switch (connectivityManager.getType()) {
            default: {
                return true;
            }
            case 1: 
            case 7: 
            case 9: {
                return false;
            }
            case 0: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
        }
        return true;
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface RestrictBackgroundStatus {
    }

}

