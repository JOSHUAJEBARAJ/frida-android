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

import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.FragmentManagerNonConfig;
import android.support.v4.app.LoaderManager;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class FragmentController {
    private final FragmentHostCallback<?> mHost;

    private FragmentController(FragmentHostCallback<?> fragmentHostCallback) {
        this.mHost = fragmentHostCallback;
    }

    public static FragmentController createController(FragmentHostCallback<?> fragmentHostCallback) {
        return new FragmentController(fragmentHostCallback);
    }

    public void attachHost(Fragment fragment) {
        FragmentManagerImpl fragmentManagerImpl = this.mHost.mFragmentManager;
        FragmentHostCallback fragmentHostCallback = this.mHost;
        fragmentManagerImpl.attachController(fragmentHostCallback, fragmentHostCallback, fragment);
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

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        return this.mHost.mFragmentManager.dispatchCreateOptionsMenu(menu, menuInflater);
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

    public void dispatchMultiWindowModeChanged(boolean bl) {
        this.mHost.mFragmentManager.dispatchMultiWindowModeChanged(bl);
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        return this.mHost.mFragmentManager.dispatchOptionsItemSelected(menuItem);
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        this.mHost.mFragmentManager.dispatchOptionsMenuClosed(menu);
    }

    public void dispatchPause() {
        this.mHost.mFragmentManager.dispatchPause();
    }

    public void dispatchPictureInPictureModeChanged(boolean bl) {
        this.mHost.mFragmentManager.dispatchPictureInPictureModeChanged(bl);
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        return this.mHost.mFragmentManager.dispatchPrepareOptionsMenu(menu);
    }

    @Deprecated
    public void dispatchReallyStop() {
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

    @Deprecated
    public void doLoaderDestroy() {
    }

    @Deprecated
    public void doLoaderRetain() {
    }

    @Deprecated
    public void doLoaderStart() {
    }

    @Deprecated
    public void doLoaderStop(boolean bl) {
    }

    @Deprecated
    public void dumpLoaders(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
    }

    public boolean execPendingActions() {
        return this.mHost.mFragmentManager.execPendingActions();
    }

    @Nullable
    public Fragment findFragmentByWho(String string2) {
        return this.mHost.mFragmentManager.findFragmentByWho(string2);
    }

    public List<Fragment> getActiveFragments(List<Fragment> list) {
        return this.mHost.mFragmentManager.getActiveFragments();
    }

    public int getActiveFragmentsCount() {
        return this.mHost.mFragmentManager.getActiveFragmentCount();
    }

    public FragmentManager getSupportFragmentManager() {
        return this.mHost.getFragmentManagerImpl();
    }

    @Deprecated
    public LoaderManager getSupportLoaderManager() {
        throw new UnsupportedOperationException("Loaders are managed separately from FragmentController, use LoaderManager.getInstance() to obtain a LoaderManager.");
    }

    public void noteStateNotSaved() {
        this.mHost.mFragmentManager.noteStateNotSaved();
    }

    public View onCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
        return this.mHost.mFragmentManager.onCreateView(view, string2, context, attributeSet);
    }

    @Deprecated
    public void reportLoaderStart() {
    }

    public void restoreAllState(Parcelable parcelable, FragmentManagerNonConfig fragmentManagerNonConfig) {
        this.mHost.mFragmentManager.restoreAllState(parcelable, fragmentManagerNonConfig);
    }

    @Deprecated
    public void restoreAllState(Parcelable parcelable, List<Fragment> list) {
        this.mHost.mFragmentManager.restoreAllState(parcelable, new FragmentManagerNonConfig(list, null, null));
    }

    @Deprecated
    public void restoreLoaderNonConfig(SimpleArrayMap<String, LoaderManager> simpleArrayMap) {
    }

    @Deprecated
    public SimpleArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        return null;
    }

    public FragmentManagerNonConfig retainNestedNonConfig() {
        return this.mHost.mFragmentManager.retainNonConfig();
    }

    @Deprecated
    public List<Fragment> retainNonConfig() {
        FragmentManagerNonConfig fragmentManagerNonConfig = this.mHost.mFragmentManager.retainNonConfig();
        if (fragmentManagerNonConfig != null) {
            return fragmentManagerNonConfig.getFragments();
        }
        return null;
    }

    public Parcelable saveAllState() {
        return this.mHost.mFragmentManager.saveAllState();
    }
}

