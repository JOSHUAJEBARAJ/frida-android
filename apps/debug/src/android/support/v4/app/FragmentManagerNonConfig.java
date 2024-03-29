/*
 * Decompiled with CFR 0_121.
 */
package android.support.v4.app;

import android.arch.lifecycle.ViewModelStore;
import android.support.v4.app.Fragment;
import java.util.List;

public class FragmentManagerNonConfig {
    private final List<FragmentManagerNonConfig> mChildNonConfigs;
    private final List<Fragment> mFragments;
    private final List<ViewModelStore> mViewModelStores;

    FragmentManagerNonConfig(List<Fragment> list, List<FragmentManagerNonConfig> list2, List<ViewModelStore> list3) {
        this.mFragments = list;
        this.mChildNonConfigs = list2;
        this.mViewModelStores = list3;
    }

    List<FragmentManagerNonConfig> getChildNonConfigs() {
        return this.mChildNonConfigs;
    }

    List<Fragment> getFragments() {
        return this.mFragments;
    }

    List<ViewModelStore> getViewModelStores() {
        return this.mViewModelStores;
    }
}

