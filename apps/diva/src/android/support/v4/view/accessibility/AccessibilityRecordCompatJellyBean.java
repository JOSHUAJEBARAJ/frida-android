/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.accessibility.AccessibilityRecord
 */
package android.support.v4.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityRecord;

class AccessibilityRecordCompatJellyBean {
    AccessibilityRecordCompatJellyBean() {
    }

    public static void setSource(Object object, View view, int n) {
        ((AccessibilityRecord)object).setSource(view, n);
    }
}

