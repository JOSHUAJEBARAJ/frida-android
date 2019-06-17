/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.widget.PopupMenu
 */
package android.support.v4.widget;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.PopupMenu;

public final class PopupMenuCompat {
    private PopupMenuCompat() {
    }

    @Nullable
    public static View.OnTouchListener getDragToOpenListener(@NonNull Object object) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ((PopupMenu)object).getDragToOpenListener();
        }
        return null;
    }
}

