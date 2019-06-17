/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityManager
 */
package android.support.v4.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewParentCompatICS;
import android.support.v4.view.ViewParentCompatKitKat;
import android.support.v4.view.ViewParentCompatLollipop;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public class ViewParentCompat {
    static final ViewParentCompatImpl IMPL;

    static {
        int n = Build.VERSION.SDK_INT;
        IMPL = n >= 21 ? new ViewParentCompatLollipopImpl() : (n >= 19 ? new ViewParentCompatKitKatImpl() : (n >= 14 ? new ViewParentCompatICSImpl() : new ViewParentCompatStubImpl()));
    }

    private ViewParentCompat() {
    }

    public static void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int n) {
        IMPL.notifySubtreeAccessibilityStateChanged(viewParent, view, view2, n);
    }

    public static boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean bl) {
        return IMPL.onNestedFling(viewParent, view, f, f2, bl);
    }

    public static boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
        return IMPL.onNestedPreFling(viewParent, view, f, f2);
    }

    public static void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] arrn) {
        IMPL.onNestedPreScroll(viewParent, view, n, n2, arrn);
    }

    public static void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4) {
        IMPL.onNestedScroll(viewParent, view, n, n2, n3, n4);
    }

    public static void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int n) {
        IMPL.onNestedScrollAccepted(viewParent, view, view2, n);
    }

    public static boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int n) {
        return IMPL.onStartNestedScroll(viewParent, view, view2, n);
    }

    public static void onStopNestedScroll(ViewParent viewParent, View view) {
        IMPL.onStopNestedScroll(viewParent, view);
    }

    public static boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
        return IMPL.requestSendAccessibilityEvent(viewParent, view, accessibilityEvent);
    }

    static class ViewParentCompatICSImpl
    extends ViewParentCompatStubImpl {
        ViewParentCompatICSImpl() {
        }

        @Override
        public boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
            return ViewParentCompatICS.requestSendAccessibilityEvent(viewParent, view, accessibilityEvent);
        }
    }

    static interface ViewParentCompatImpl {
        public void notifySubtreeAccessibilityStateChanged(ViewParent var1, View var2, View var3, int var4);

        public boolean onNestedFling(ViewParent var1, View var2, float var3, float var4, boolean var5);

        public boolean onNestedPreFling(ViewParent var1, View var2, float var3, float var4);

        public void onNestedPreScroll(ViewParent var1, View var2, int var3, int var4, int[] var5);

        public void onNestedScroll(ViewParent var1, View var2, int var3, int var4, int var5, int var6);

        public void onNestedScrollAccepted(ViewParent var1, View var2, View var3, int var4);

        public boolean onStartNestedScroll(ViewParent var1, View var2, View var3, int var4);

        public void onStopNestedScroll(ViewParent var1, View var2);

        public boolean requestSendAccessibilityEvent(ViewParent var1, View var2, AccessibilityEvent var3);
    }

    static class ViewParentCompatKitKatImpl
    extends ViewParentCompatICSImpl {
        ViewParentCompatKitKatImpl() {
        }

        @Override
        public void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int n) {
            ViewParentCompatKitKat.notifySubtreeAccessibilityStateChanged(viewParent, view, view2, n);
        }
    }

    static class ViewParentCompatLollipopImpl
    extends ViewParentCompatKitKatImpl {
        ViewParentCompatLollipopImpl() {
        }

        @Override
        public boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean bl) {
            return ViewParentCompatLollipop.onNestedFling(viewParent, view, f, f2, bl);
        }

        @Override
        public boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
            return ViewParentCompatLollipop.onNestedPreFling(viewParent, view, f, f2);
        }

        @Override
        public void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] arrn) {
            ViewParentCompatLollipop.onNestedPreScroll(viewParent, view, n, n2, arrn);
        }

        @Override
        public void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4) {
            ViewParentCompatLollipop.onNestedScroll(viewParent, view, n, n2, n3, n4);
        }

        @Override
        public void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int n) {
            ViewParentCompatLollipop.onNestedScrollAccepted(viewParent, view, view2, n);
        }

        @Override
        public boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int n) {
            return ViewParentCompatLollipop.onStartNestedScroll(viewParent, view, view2, n);
        }

        @Override
        public void onStopNestedScroll(ViewParent viewParent, View view) {
            ViewParentCompatLollipop.onStopNestedScroll(viewParent, view);
        }
    }

    static class ViewParentCompatStubImpl
    implements ViewParentCompatImpl {
        ViewParentCompatStubImpl() {
        }

        @Override
        public void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int n) {
        }

        @Override
        public boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean bl) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)viewParent).onNestedFling(view, f, f2, bl);
            }
            return false;
        }

        @Override
        public boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)viewParent).onNestedPreFling(view, f, f2);
            }
            return false;
        }

        @Override
        public void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] arrn) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedPreScroll(view, n, n2, arrn);
            }
        }

        @Override
        public void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScroll(view, n, n2, n3, n4);
            }
        }

        @Override
        public void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int n) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScrollAccepted(view, view2, n);
            }
        }

        @Override
        public boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int n) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)viewParent).onStartNestedScroll(view, view2, n);
            }
            return false;
        }

        @Override
        public void onStopNestedScroll(ViewParent viewParent, View view) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onStopNestedScroll(view);
            }
        }

        @Override
        public boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
            if (view == null) {
                return false;
            }
            ((AccessibilityManager)view.getContext().getSystemService("accessibility")).sendAccessibilityEvent(accessibilityEvent);
            return true;
        }
    }

}

