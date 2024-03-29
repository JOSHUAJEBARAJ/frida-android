/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.widget.ListView
 */
package android.support.v4.widget;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

public final class ListViewCompat {
    private ListViewCompat() {
    }

    public static boolean canScrollList(@NonNull ListView listView, int n) {
        boolean bl;
        block6 : {
            if (Build.VERSION.SDK_INT >= 19) {
                return listView.canScrollList(n);
            }
            int n2 = listView.getChildCount();
            boolean bl2 = false;
            bl = false;
            if (n2 == 0) {
                return false;
            }
            int n3 = listView.getFirstVisiblePosition();
            if (n > 0) {
                n = listView.getChildAt(n2 - 1).getBottom();
                if (n3 + n2 < listView.getCount() || n > listView.getHeight() - listView.getListPaddingBottom()) {
                    bl = true;
                }
                return bl;
            }
            n = listView.getChildAt(0).getTop();
            if (n3 <= 0) {
                bl = bl2;
                if (n >= listView.getListPaddingTop()) break block6;
            }
            bl = true;
        }
        return bl;
    }

    public static void scrollListBy(@NonNull ListView listView, int n) {
        if (Build.VERSION.SDK_INT >= 19) {
            listView.scrollListBy(n);
            return;
        }
        int n2 = listView.getFirstVisiblePosition();
        if (n2 == -1) {
            return;
        }
        View view = listView.getChildAt(0);
        if (view == null) {
            return;
        }
        listView.setSelectionFromTop(n2, view.getTop() - n);
    }
}

