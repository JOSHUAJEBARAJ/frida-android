/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.Menu
 *  android.view.MenuItem
 */
package android.support.v4.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.internal.view.SupportMenu;
import android.view.Menu;
import android.view.MenuItem;

public final class MenuCompat {
    private MenuCompat() {
    }

    @SuppressLint(value={"NewApi"})
    public static void setGroupDividerEnabled(Menu menu, boolean bl) {
        if (menu instanceof SupportMenu) {
            ((SupportMenu)menu).setGroupDividerEnabled(bl);
            return;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            menu.setGroupDividerEnabled(bl);
        }
    }

    @Deprecated
    public static void setShowAsAction(MenuItem menuItem, int n) {
        menuItem.setShowAsAction(n);
    }
}

