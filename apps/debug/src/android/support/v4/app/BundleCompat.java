/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.util.Log
 */
package android.support.v4.app;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BundleCompat {
    private BundleCompat() {
    }

    @Nullable
    public static IBinder getBinder(@NonNull Bundle bundle, @Nullable String string2) {
        if (Build.VERSION.SDK_INT >= 18) {
            return bundle.getBinder(string2);
        }
        return BundleCompatBaseImpl.getBinder(bundle, string2);
    }

    public static void putBinder(@NonNull Bundle bundle, @Nullable String string2, @Nullable IBinder iBinder) {
        if (Build.VERSION.SDK_INT >= 18) {
            bundle.putBinder(string2, iBinder);
            return;
        }
        BundleCompatBaseImpl.putBinder(bundle, string2, iBinder);
    }

    static class BundleCompatBaseImpl {
        private static final String TAG = "BundleCompatBaseImpl";
        private static Method sGetIBinderMethod;
        private static boolean sGetIBinderMethodFetched;
        private static Method sPutIBinderMethod;
        private static boolean sPutIBinderMethodFetched;

        private BundleCompatBaseImpl() {
        }

        public static IBinder getBinder(Bundle bundle, String string2) {
            Method method;
            if (!sGetIBinderMethodFetched) {
                try {
                    sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class);
                    sGetIBinderMethod.setAccessible(true);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    Log.i((String)"BundleCompatBaseImpl", (String)"Failed to retrieve getIBinder method", (Throwable)noSuchMethodException);
                }
                sGetIBinderMethodFetched = true;
            }
            if ((method = sGetIBinderMethod) != null) {
                void var0_4;
                try {
                    bundle = (IBinder)method.invoke((Object)bundle, string2);
                    return bundle;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                }
                catch (IllegalAccessException illegalAccessException) {
                }
                catch (InvocationTargetException invocationTargetException) {
                    // empty catch block
                }
                Log.i((String)"BundleCompatBaseImpl", (String)"Failed to invoke getIBinder via reflection", (Throwable)var0_4);
                sGetIBinderMethod = null;
            }
            return null;
        }

        public static void putBinder(Bundle bundle, String string2, IBinder iBinder) {
            Method method;
            if (!sPutIBinderMethodFetched) {
                try {
                    sPutIBinderMethod = Bundle.class.getMethod("putIBinder", String.class, IBinder.class);
                    sPutIBinderMethod.setAccessible(true);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    Log.i((String)"BundleCompatBaseImpl", (String)"Failed to retrieve putIBinder method", (Throwable)noSuchMethodException);
                }
                sPutIBinderMethodFetched = true;
            }
            if ((method = sPutIBinderMethod) != null) {
                void var0_4;
                try {
                    method.invoke((Object)bundle, new Object[]{string2, iBinder});
                    return;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                }
                catch (IllegalAccessException illegalAccessException) {
                }
                catch (InvocationTargetException invocationTargetException) {
                    // empty catch block
                }
                Log.i((String)"BundleCompatBaseImpl", (String)"Failed to invoke putIBinder via reflection", (Throwable)var0_4);
                sPutIBinderMethod = null;
            }
        }
    }

}

