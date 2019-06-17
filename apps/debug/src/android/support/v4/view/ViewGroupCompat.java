/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.accessibility.AccessibilityEvent
 */
package android.support.v4.view;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.compat.R;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public final class ViewGroupCompat {
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;

    private ViewGroupCompat() {
    }

    public static int getLayoutMode(@NonNull ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= 18) {
            return viewGroup.getLayoutMode();
        }
        return 0;
    }

    public static int getNestedScrollAxes(@NonNull ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= 21) {
            return viewGroup.getNestedScrollAxes();
        }
        if (viewGroup instanceof NestedScrollingParent) {
            return ((NestedScrollingParent)viewGroup).getNestedScrollAxes();
        }
        return 0;
    }

    public static boolean isTransitionGroup(@NonNull ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= 21) {
            return viewGroup.isTransitionGroup();
        }
        Boolean bl = (Boolean)viewGroup.getTag(R.id.tag_transition_group);
        if (!(bl != null && bl.booleanValue() || viewGroup.getBackground() != null || ViewCompat.getTransitionName((View)viewGroup) != null)) {
            return false;
        }
        return true;
    }

    @Deprecated
    public static boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        return viewGroup.onRequestSendAccessibilityEvent(view, accessibilityEvent);
    }

    public static void setLayoutMode(@NonNull ViewGroup viewGroup, int n) {
        if (Build.VERSION.SDK_INT >= 18) {
            viewGroup.setLayoutMode(n);
        }
    }

    @Deprecated
    public static void setMotionEventSplittingEnabled(ViewGroup viewGroup, boolean bl) {
        viewGroup.setMotionEventSplittingEnabled(bl);
    }

    public static void setTransitionGroup(@NonNull ViewGroup viewGroup, boolean bl) {
        if (Build.VERSION.SDK_INT >= 21) {
            viewGroup.setTransitionGroup(bl);
            return;
        }
        viewGroup.setTag(R.id.tag_transition_group, (Object)bl);
    }
}

