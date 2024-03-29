/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.text;

import android.os.Build;
import android.support.v4.text.ICUCompatApi23;
import android.support.v4.text.ICUCompatIcs;
import java.util.Locale;

public class ICUCompat {
    private static final ICUCompatImpl IMPL;

    static {
        int n = Build.VERSION.SDK_INT;
        IMPL = n >= 21 ? new ICUCompatImplLollipop() : (n >= 14 ? new ICUCompatImplIcs() : new ICUCompatImplBase());
    }

    public static String maximizeAndGetScript(Locale locale) {
        return IMPL.maximizeAndGetScript(locale);
    }

    static interface ICUCompatImpl {
        public String maximizeAndGetScript(Locale var1);
    }

    static class ICUCompatImplBase
    implements ICUCompatImpl {
        ICUCompatImplBase() {
        }

        @Override
        public String maximizeAndGetScript(Locale locale) {
            return null;
        }
    }

    static class ICUCompatImplIcs
    implements ICUCompatImpl {
        ICUCompatImplIcs() {
        }

        @Override
        public String maximizeAndGetScript(Locale locale) {
            return ICUCompatIcs.maximizeAndGetScript(locale);
        }
    }

    static class ICUCompatImplLollipop
    implements ICUCompatImpl {
        ICUCompatImplLollipop() {
        }

        @Override
        public String maximizeAndGetScript(Locale locale) {
            return ICUCompatApi23.maximizeAndGetScript(locale);
        }
    }

}

