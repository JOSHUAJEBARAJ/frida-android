/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Configuration
 *  android.os.Bundle
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.v7.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class AppCompatActivity
extends FragmentActivity
implements AppCompatCallback,
TaskStackBuilder.SupportParentable,
ActionBarDrawerToggle.DelegateProvider {
    private AppCompatDelegate mDelegate;

    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.getDelegate().addContentView(view, layoutParams);
    }

    public AppCompatDelegate getDelegate() {
        if (this.mDelegate == null) {
            this.mDelegate = AppCompatDelegate.create(this, (AppCompatCallback)this);
        }
        return this.mDelegate;
    }

    @Nullable
    @Override
    public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return this.getDelegate().getDrawerToggleDelegate();
    }

    public MenuInflater getMenuInflater() {
        return this.getDelegate().getMenuInflater();
    }

    @Nullable
    public ActionBar getSupportActionBar() {
        return this.getDelegate().getSupportActionBar();
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        return NavUtils.getParentActivityIntent(this);
    }

    public void invalidateOptionsMenu() {
        this.getDelegate().invalidateOptionsMenu();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.getDelegate().onConfigurationChanged(configuration);
    }

    public void onContentChanged() {
        this.onSupportContentChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        this.getDelegate().installViewFactory();
        this.getDelegate().onCreate(bundle);
        super.onCreate(bundle);
    }

    public void onCreateSupportNavigateUpTaskStack(TaskStackBuilder taskStackBuilder) {
        taskStackBuilder.addParentStack(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getDelegate().onDestroy();
    }

    @Override
    public final boolean onMenuItemSelected(int n, MenuItem menuItem) {
        if (super.onMenuItemSelected(n, menuItem)) {
            return true;
        }
        ActionBar actionBar = this.getSupportActionBar();
        if (menuItem.getItemId() == 16908332 && actionBar != null && (actionBar.getDisplayOptions() & 4) != 0) {
            return this.onSupportNavigateUp();
        }
        return false;
    }

    public boolean onMenuOpened(int n, Menu menu2) {
        return super.onMenuOpened(n, menu2);
    }

    @Override
    public void onPanelClosed(int n, Menu menu2) {
        super.onPanelClosed(n, menu2);
    }

    protected void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
        this.getDelegate().onPostCreate(bundle);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.getDelegate().onPostResume();
    }

    public void onPrepareSupportNavigateUpTaskStack(TaskStackBuilder taskStackBuilder) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.getDelegate().onStop();
    }

    @CallSuper
    @Override
    public void onSupportActionModeFinished(ActionMode actionMode) {
    }

    @CallSuper
    @Override
    public void onSupportActionModeStarted(ActionMode actionMode) {
    }

    @Deprecated
    public void onSupportContentChanged() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean onSupportNavigateUp() {
        Object object = this.getSupportParentActivityIntent();
        if (object == null) return false;
        if (this.supportShouldUpRecreateTask((Intent)object)) {
            object = TaskStackBuilder.create((Context)this);
            this.onCreateSupportNavigateUpTaskStack((TaskStackBuilder)object);
            this.onPrepareSupportNavigateUpTaskStack((TaskStackBuilder)object);
            object.startActivities();
            try {
                ActivityCompat.finishAffinity(this);
            }
            catch (IllegalStateException illegalStateException) {
                this.finish();
                return true;
            }
            do {
                return true;
                break;
            } while (true);
        }
        this.supportNavigateUpTo((Intent)object);
        return true;
    }

    protected void onTitleChanged(CharSequence charSequence, int n) {
        super.onTitleChanged(charSequence, n);
        this.getDelegate().setTitle(charSequence);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    public void setContentView(@LayoutRes int n) {
        this.getDelegate().setContentView(n);
    }

    public void setContentView(View view) {
        this.getDelegate().setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.getDelegate().setContentView(view, layoutParams);
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        this.getDelegate().setSupportActionBar(toolbar);
    }

    @Deprecated
    public void setSupportProgress(int n) {
    }

    @Deprecated
    public void setSupportProgressBarIndeterminate(boolean bl) {
    }

    @Deprecated
    public void setSupportProgressBarIndeterminateVisibility(boolean bl) {
    }

    @Deprecated
    public void setSupportProgressBarVisibility(boolean bl) {
    }

    public ActionMode startSupportActionMode(ActionMode.Callback callback) {
        return this.getDelegate().startSupportActionMode(callback);
    }

    @Override
    public void supportInvalidateOptionsMenu() {
        this.getDelegate().invalidateOptionsMenu();
    }

    public void supportNavigateUpTo(Intent intent) {
        NavUtils.navigateUpTo(this, intent);
    }

    public boolean supportRequestWindowFeature(int n) {
        return this.getDelegate().requestWindowFeature(n);
    }

    public boolean supportShouldUpRecreateTask(Intent intent) {
        return NavUtils.shouldUpRecreateTask(this, intent);
    }
}

