/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.util.Log
 */
package android.support.v4.app;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class BundleCompatDonut {
    private static final String TAG = "BundleCompatDonut";
    private static Method sGetIBinderMethod;
    private static boolean sGetIBinderMethodFetched;
    private static Method sPutIBinderMethod;
    private static boolean sPutIBinderMethodFetched;

    BundleCompatDonut() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static IBinder getBinder(Bundle var0, String var1_5) {
        if (!BundleCompatDonut.sGetIBinderMethodFetched) {
            try {
                BundleCompatDonut.sGetIBinderMethod = Bundle.class.getMethod("getIBinder", new Class[]{String.class});
                BundleCompatDonut.sGetIBinderMethod.setAccessible(true);
            }
            catch (NoSuchMethodException var2_6) {
                Log.i((String)"BundleCompatDonut", (String)"Failed to retrieve getIBinder method", (Throwable)var2_6);
            }
            BundleCompatDonut.sGetIBinderMethodFetched = true;
        }
        if (BundleCompatDonut.sGetIBinderMethod == null) return null;
        try {
            return (IBinder)BundleCompatDonut.sGetIBinderMethod.invoke((Object)var0, new Object[]{var1_5});
        }
        catch (IllegalArgumentException var0_1) {}
        ** GOTO lbl-1000
        catch (InvocationTargetException var0_3) {
            ** GOTO lbl-1000
        }
        catch (IllegalAccessException var0_4) {}
lbl-1000: // 3 sources:
        {
            Log.i((String)"BundleCompatDonut", (String)"Failed to invoke getIBinder via reflection", (Throwable)var0_2);
            BundleCompatDonut.sGetIBinderMethod = null;
        }
        return null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void putBinder(Bundle var0, String var1_5, IBinder var2_6) {
        if (!BundleCompatDonut.sPutIBinderMethodFetched) {
            try {
                BundleCompatDonut.sPutIBinderMethod = Bundle.class.getMethod("putIBinder", new Class[]{String.class, IBinder.class});
                BundleCompatDonut.sPutIBinderMethod.setAccessible(true);
            }
            catch (NoSuchMethodException var3_7) {
                Log.i((String)"BundleCompatDonut", (String)"Failed to retrieve putIBinder method", (Throwable)var3_7);
            }
            BundleCompatDonut.sPutIBinderMethodFetched = true;
        }
        if (BundleCompatDonut.sPutIBinderMethod == null) return;
        try {
            BundleCompatDonut.sPutIBinderMethod.invoke((Object)var0, new Object[]{var1_5, var2_6});
            return;
        }
        catch (IllegalArgumentException var0_1) {}
        ** GOTO lbl-1000
        catch (InvocationTargetException var0_3) {
            ** GOTO lbl-1000
        }
        catch (IllegalAccessException var0_4) {}
lbl-1000: // 3 sources:
        {
            Log.i((String)"BundleCompatDonut", (String)"Failed to invoke putIBinder via reflection", (Throwable)var0_2);
            BundleCompatDonut.sPutIBinderMethod = null;
            return;
        }
    }
}

