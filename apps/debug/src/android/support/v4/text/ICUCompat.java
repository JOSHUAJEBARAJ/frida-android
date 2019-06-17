/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package android.support.v4.text;

import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ICUCompat {
    private static final String TAG = "ICUCompat";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static {
        Class class_;
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
                return;
            }
            catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        }
        try {
            class_ = Class.forName("libcore.icu.ICU");
            if (class_ == null) return;
        }
        catch (Exception exception) {
            sGetScriptMethod = null;
            sAddLikelySubtagsMethod = null;
            Log.w((String)"ICUCompat", (Throwable)exception);
            return;
        }
        sGetScriptMethod = class_.getMethod("getScript", String.class);
        sAddLikelySubtagsMethod = class_.getMethod("addLikelySubtags", String.class);
    }

    private ICUCompat() {
    }

    private static String addLikelySubtags(Locale object) {
        object = object.toString();
        try {
            if (sAddLikelySubtagsMethod != null) {
                String string2 = (String)sAddLikelySubtagsMethod.invoke(null, object);
                return string2;
            }
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.w((String)"ICUCompat", (Throwable)invocationTargetException);
            return object;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompat", (Throwable)illegalAccessException);
        }
        return object;
    }

    private static String getScript(String string2) {
        try {
            if (sGetScriptMethod != null) {
                string2 = (String)sGetScriptMethod.invoke(null, string2);
                return string2;
            }
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.w((String)"ICUCompat", (Throwable)invocationTargetException);
            return null;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompat", (Throwable)illegalAccessException);
        }
        return null;
    }

    @Nullable
    public static String maximizeAndGetScript(Locale object) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                String string2 = ((Locale)sAddLikelySubtagsMethod.invoke(null, object)).getScript();
                return string2;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.w((String)"ICUCompat", (Throwable)illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.w((String)"ICUCompat", (Throwable)invocationTargetException);
            }
            return object.getScript();
        }
        if ((object = ICUCompat.addLikelySubtags((Locale)object)) != null) {
            return ICUCompat.getScript((String)object);
        }
        return null;
    }
}

