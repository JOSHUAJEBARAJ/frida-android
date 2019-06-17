/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 */
package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.lang.reflect.Method;

class DrawableCompatJellybeanMr1 {
    private static final String TAG = "DrawableCompatJellybeanMr1";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;

    DrawableCompatJellybeanMr1() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static int getLayoutDirection(Drawable drawable2) {
        if (!sGetLayoutDirectionMethodFetched) {
            try {
                sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", new Class[0]);
                sGetLayoutDirectionMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"DrawableCompatJellybeanMr1", (String)"Failed to retrieve getLayoutDirection() method", (Throwable)noSuchMethodException);
            }
            sGetLayoutDirectionMethodFetched = true;
        }
        if (sGetLayoutDirectionMethod == null) return -1;
        try {
            return (Integer)sGetLayoutDirectionMethod.invoke((Object)drawable2, new Object[0]);
        }
        catch (Exception exception) {
            Log.i((String)"DrawableCompatJellybeanMr1", (String)"Failed to invoke getLayoutDirection() via reflection", (Throwable)exception);
            sGetLayoutDirectionMethod = null;
        }
        return -1;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void setLayoutDirection(Drawable drawable2, int n) {
        if (!sSetLayoutDirectionMethodFetched) {
            try {
                sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE);
                sSetLayoutDirectionMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"DrawableCompatJellybeanMr1", (String)"Failed to retrieve setLayoutDirection(int) method", (Throwable)noSuchMethodException);
            }
            sSetLayoutDirectionMethodFetched = true;
        }
        if (sSetLayoutDirectionMethod == null) return;
        try {
            sSetLayoutDirectionMethod.invoke((Object)drawable2, n);
            return;
        }
        catch (Exception exception) {
            Log.i((String)"DrawableCompatJellybeanMr1", (String)"Failed to invoke setLayoutDirection(int) via reflection", (Throwable)exception);
            sSetLayoutDirectionMethod = null;
            return;
        }
    }
}

