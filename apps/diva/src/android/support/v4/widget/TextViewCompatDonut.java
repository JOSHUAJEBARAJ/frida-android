/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.widget.TextView
 */
package android.support.v4.widget;

import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;

class TextViewCompatDonut {
    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompatDonut";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;

    TextViewCompatDonut() {
    }

    static int getMaxLines(TextView textView) {
        if (!sMaxModeFieldFetched) {
            sMaxModeField = TextViewCompatDonut.retrieveField("mMaxMode");
            sMaxModeFieldFetched = true;
        }
        if (sMaxModeField != null && TextViewCompatDonut.retrieveIntFromField(sMaxModeField, textView) == 1) {
            if (!sMaximumFieldFetched) {
                sMaximumField = TextViewCompatDonut.retrieveField("mMaximum");
                sMaximumFieldFetched = true;
            }
            if (sMaximumField != null) {
                return TextViewCompatDonut.retrieveIntFromField(sMaximumField, textView);
            }
        }
        return -1;
    }

    static int getMinLines(TextView textView) {
        if (!sMinModeFieldFetched) {
            sMinModeField = TextViewCompatDonut.retrieveField("mMinMode");
            sMinModeFieldFetched = true;
        }
        if (sMinModeField != null && TextViewCompatDonut.retrieveIntFromField(sMinModeField, textView) == 1) {
            if (!sMinimumFieldFetched) {
                sMinimumField = TextViewCompatDonut.retrieveField("mMinimum");
                sMinimumFieldFetched = true;
            }
            if (sMinimumField != null) {
                return TextViewCompatDonut.retrieveIntFromField(sMinimumField, textView);
            }
        }
        return -1;
    }

    private static Field retrieveField(String string2) {
        Field field;
        Field field2 = null;
        try {
            field2 = field = TextView.class.getDeclaredField(string2);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.e((String)"TextViewCompatDonut", (String)("Could not retrieve " + string2 + " field."));
            return field2;
        }
        field.setAccessible(true);
        return field;
    }

    private static int retrieveIntFromField(Field field, TextView textView) {
        try {
            int n = field.getInt((Object)textView);
            return n;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.d((String)"TextViewCompatDonut", (String)("Could not retrieve value of " + field.getName() + " field."));
            return -1;
        }
    }
}

