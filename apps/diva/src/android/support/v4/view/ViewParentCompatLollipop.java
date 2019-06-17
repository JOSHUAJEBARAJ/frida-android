/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.v4.view;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;

class ViewParentCompatLollipop {
    private static final String TAG = "ViewParentCompat";

    ViewParentCompatLollipop() {
    }

    public static boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean bl) {
        try {
            bl = viewParent.onNestedFling(view, f, f2, bl);
            return bl;
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e((String)"ViewParentCompat", (String)("ViewParent " + (Object)viewParent + " does not implement interface " + "method onNestedFling"), (Throwable)abstractMethodError);
            return false;
        }
    }

    public static boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
        try {
            boolean bl = viewParent.onNestedPreFling(view, f, f2);
            return bl;
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e((String)"ViewParentCompat", (String)("ViewParent " + (Object)viewParent + " does not implement interface " + "method onNestedPreFling"), (Throwable)abstractMethodError);
            return false;
        }
    }

    public static void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] arrn) {
        try {
            viewParent.onNestedPreScroll(view, n, n2, arrn);
            return;
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e((String)"ViewParentCompat", (String)("ViewParent " + (Object)viewParent + " does not implement interface " + "method onNestedPreScroll"), (Throwable)abstractMethodError);
            return;
        }
    }

    public static void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4) {
        try {
            viewParent.onNestedScroll(view, n, n2, n3, n4);
            return;
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e((String)"ViewParentCompat", (String)("ViewParent " + (Object)viewParent + " does not implement interface " + "method onNestedScroll"), (Throwable)abstractMethodError);
            return;
        }
    }

    public static void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int n) {
        try {
            viewParent.onNestedScrollAccepted(view, view2, n);
            return;
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e((String)"ViewParentCompat", (String)("ViewParent " + (Object)viewParent + " does not implement interface " + "method onNestedScrollAccepted"), (Throwable)abstractMethodError);
            return;
        }
    }

    public static boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int n) {
        try {
            boolean bl = viewParent.onStartNestedScroll(view, view2, n);
            return bl;
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e((String)"ViewParentCompat", (String)("ViewParent " + (Object)viewParent + " does not implement interface " + "method onStartNestedScroll"), (Throwable)abstractMethodError);
            return false;
        }
    }

    public static void onStopNestedScroll(ViewParent viewParent, View view) {
        try {
            viewParent.onStopNestedScroll(view);
            return;
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e((String)"ViewParentCompat", (String)("ViewParent " + (Object)viewParent + " does not implement interface " + "method onStopNestedScroll"), (Throwable)abstractMethodError);
            return;
        }
    }
}

