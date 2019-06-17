/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Message
 */
package android.support.v4.os;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class HandlerCompat {
    private HandlerCompat() {
    }

    public static boolean postDelayed(@NonNull Handler handler, @NonNull Runnable runnable, @Nullable Object object, long l) {
        if (Build.VERSION.SDK_INT >= 28) {
            return handler.postDelayed(runnable, object, l);
        }
        runnable = Message.obtain((Handler)handler, (Runnable)runnable);
        runnable.obj = object;
        return handler.sendMessageDelayed((Message)runnable, l);
    }
}

