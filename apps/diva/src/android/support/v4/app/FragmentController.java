/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 */
package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManagerImpl;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FragmentController {
    private final FragmentHostCallback<?> mHost;

    private FragmentController(FragmentHostCallback<?> fragmentHostCallback) {
        this.mHost = fragmentHostCallback;
    }

    public static final FragmentController createController(FragmentHostCallback<?> fragmentHostCallback) {
        return new FragmentController(fragmentHostCallback);
    }

    public void attachHost(Fragment fragment) {
        this.mHost.mFragmentManager.attachController(this.mHost, this.mHost, fragment);
    }

    public void dispatchActivityCreated() {
        this.mHost.mFragmentManager.dispatchActivityCreated();
    }

    public void dispatchConfigurationChanged(Configuration configuration) {
        this.mHost.mFragmentManager.dispatchConfigurationChanged(configuration);
    }

    public boolean dispatchContextItemSelected(MenuItem menuItem) {
        return this.mHost.mFragmentManager.dispatchContextItemSelected(menuItem);
    }

    public void dispatchCreate() {
        this.mHost.mFragmentManager.dispatchCreate();
    }

    public boolean dispatchCreateOptionsMenu(Menu menu2, MenuInflater menuInflater) {
        return this.mHost.mFragmentManager.dispatchCreateOptionsMenu(menu2, menuInflater);
    }

    public void dispatchDestroy() {
        this.mHost.mFragmentManager.dispatchDestroy();
    }

    public void dispatchDestroyView() {
        this.mHost.mFragmentManager.dispatchDestroyView();
    }

    public void dispatchLowMemory() {
        this.mHost.mFragmentManager.dispatchLowMemory();
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        return this.mHost.mFragmentManager.dispatchOptionsItemSelected(menuItem);
    }

    public void dispatchOptionsMenuClosed(Menu menu2) {
        this.mHost.mFragmentManager.dispatchOptionsMenuClosed(menu2);
    }

    public void dispatchPause() {
        this.mHost.mFragmentManager.dispatchPause();
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu2) {
        return this.mHost.mFragmentManager.dispatchPrepareOptionsMenu(menu2);
    }

    public void dispatchReallyStop() {
        this.mHost.mFragmentManager.dispatchReallyStop();
    }

    public void dispatchResume() {
        this.mHost.mFragmentManager.dispatchResume();
    }

    public void dispatchStart() {
        this.mHost.mFragmentManager.dispatchStart();
    }

    public void dispatchStop() {
        this.mHost.mFragmentManager.dispatchStop();
    }

    public void doLoaderDestroy() {
        this.mHost.doLoaderDestroy();
    }

    public void doLoaderRetain() {
        this.mHost.doLoaderRetain();
    }

    public void doLoaderStart() {
        this.mHost.doLoaderStart();
    }

    public void doLoaderStop(boolean bl) {
        this.mHost.doLoaderStop(bl);
        this.mHost.mFragmentManager.setRetainLoader(bl);
    }

    public void dumpLoaders(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
        this.mHost.dumpLoaders(string2, fileDescriptor, printWriter, arrstring);
    }

    public boolean execPendingActions() {
        return this.mHost.mFragmentManager.execPendingActions();
    }

    public List<Fragment> getActiveFragments(List<Fragment> list) {
        if (this.mHost.mFragmentManager.mActive == null) {
            return null;
        }
        List<Fragment> list2 = list;
        if (list == null) {
            list2 = new ArrayList<Fragment>(this.getActiveFragmentsCount());
        }
        list2.addAll(this.mHost.mFragmentManager.mActive);
        return list2;
    }

    public int getActiveFragmentsCount() {
        ArrayList<Fragment> arrayList = this.mHost.mFragmentManager.mActive;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public FragmentManager getSupportFragmentManager() {
        return this.mHost.getFragmentManagerImpl();
    }

    public LoaderManager getSupportLoaderManager() {
        return this.mHost.getLoaderManagerImpl();
    }

    public void noteStateNotSaved() {
        this.mHost.mFragmentManager.noteStateNotSaved();
    }

    public View onCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
        return this.mHost.mFragmentManager.onCreateView(view, string2, context, attributeSet);
    }

    public void reportLoaderStart() {
        this.mHost.reportLoaderStart();
    }

    public void restoreAllState(Parcelable parcelable, List<Fragment> list) {
        this.mHost.mFragmentManager.restoreAllState(parcelable, list);
    }

    public void restoreLoaderNonConfig(SimpleArrayMap<String, LoaderManager> simpleArrayMap) {
        this.mHost.restoreLoaderNonConfig(simpleArrayMap);
    }

    public SimpleArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        return this.mHost.retainLoaderNonConfig();
    }

    public List<Fragment> retainNonConfig() {
        return this.mHost.mFragmentManager.retainNonConfig();
    }

    public Parcelable saveAllState() {
        return this.mHost.mFragmentManager.saveAllState();
    }
}
