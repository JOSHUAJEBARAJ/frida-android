/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.Menu
 *  android.view.MenuItem
 */
package android.support.design.internal;

import android.content.Context;
import android.support.design.internal.NavigationMenu;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.view.Menu;
import android.view.MenuItem;

public class NavigationSubMenu
extends SubMenuBuilder {
    public NavigationSubMenu(Context context, NavigationMenu navigationMenu, MenuItemImpl menuItemImpl) {
        super(context, navigationMenu, menuItemImpl);
    }

    private void notifyParent() {
        ((MenuBuilder)this.getParentMenu()).onItemsChanged(true);
    }

    @Override
    public MenuItem add(int n) {
        MenuItem menuItem = super.add(n);
        this.notifyParent();
        return menuItem;
    }

    @Override
    public MenuItem add(int n, int n2, int n3, int n4) {
        MenuItem menuItem = super.add(n, n2, n3, n4);
        this.notifyParent();
        return menuItem;
    }

    @Override
    public MenuItem add(int n, int n2, int n3, CharSequence charSequence) {
        charSequence = super.add(n, n2, n3, charSequence);
        this.notifyParent();
        return charSequence;
    }

    @Override
    public MenuItem add(CharSequence charSequence) {
        charSequence = super.add(charSequence);
        this.notifyParent();
        return charSequence;
    }
}

