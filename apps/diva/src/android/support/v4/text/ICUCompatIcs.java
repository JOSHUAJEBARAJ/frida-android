/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package android.support.v4.text;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

class ICUCompatIcs {
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static {
        Class class_;
        try {
            class_ = Class.forName("libcore.icu.ICU");
            if (class_ == null) return;
        }
        catch (Exception exception) {
            sGetScriptMethod = null;
            sAddLikelySubtagsMethod = null;
            Log.w((String)"ICUCompatIcs", (Throwable)exception);
            return;
        }
        sGetScriptMethod = class_.getMethod("getScript", String.class);
        sAddLikelySubtagsMethod = class_.getMethod("addLikelySubtags", String.class);
    }

    ICUCompatIcs() {
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static String addLikelySubtags(Locale object) {
        object = object.toString();
        try {
            if (sAddLikelySubtagsMethod == null) return object;
            return (String)sAddLikelySubtagsMethod.invoke(null, object);
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompatIcs", (Throwable)illegalAccessException);
        }
        return object;
        catch (InvocationTargetException invocationTargetException) {
            Log.w((String)"ICUCompatIcs", (Throwable)invocationTargetException);
            return object;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static String getScript(String string2) {
        try {
            if (sGetScriptMethod == null) return null;
            return (String)sGetScriptMethod.invoke(null, string2);
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompatIcs", (Throwable)illegalAccessException);
        }
        return null;
        catch (InvocationTargetException invocationTargetException) {
            Log.w((String)"ICUCompatIcs", (Throwable)invocationTargetException);
            return null;
        }
    }

    public static String maximizeAndGetScript(Locale object) {
        if ((object = ICUCompatIcs.addLikelySubtags((Locale)object)) != null) {
            return ICUCompatIcs.getScript((String)object);
        }
        return null;
    }
}

