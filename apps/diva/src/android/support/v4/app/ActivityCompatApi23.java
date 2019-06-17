/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 */
package android.support.v4.app;

import android.app.Activity;

class ActivityCompatApi23 {
    ActivityCompatApi23() {
    }

    public static void requestPermissions(Activity activity, String[] arrstring, int n) {
        if (activity instanceof RequestPermissionsRequestCodeValidator) {
            ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(n);
        }
        activity.requestPermissions(arrstring, n);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String string2) {
        return activity.shouldShowRequestPermissionRationale(string2);
    }

    public static interface RequestPermissionsRequestCodeValidator {
        public void validateRequestPermissionsRequestCode(int var1);
    }

}

