/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Set;

public abstract class FragmentStatePagerAdapter
extends PagerAdapter {
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentStatePagerAdapt";
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private final FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments = new ArrayList();
    private ArrayList<Fragment.SavedState> mSavedState = new ArrayList();

    public FragmentStatePagerAdapter(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup object, int n, @NonNull Object object2) {
        object2 = (Fragment)object2;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        while (this.mSavedState.size() <= n) {
            this.mSavedState.add(null);
        }
        ArrayList<Fragment.SavedState> arrayList = this.mSavedState;
        object = object2.isAdded() ? this.mFragmentManager.saveFragmentInstanceState((Fragment)object2) : null;
        arrayList.set(n, (Fragment.SavedState)object);
        this.mFragments.set(n, null);
        this.mCurTransaction.remove((Fragment)object2);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup object) {
        object = this.mCurTransaction;
        if (object != null) {
            object.commitNowAllowingStateLoss();
            this.mCurTransaction = null;
        }
    }

    public abstract Fragment getItem(int var1);

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int n) {
        Fragment.SavedState savedState;
        Fragment fragment;
        if (this.mFragments.size() > n && (fragment = this.mFragments.get(n)) != null) {
            return fragment;
        }
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        fragment = this.getItem(n);
        if (this.mSavedState.size() > n && (savedState = this.mSavedState.get(n)) != null) {
            fragment.setInitialSavedState(savedState);
        }
        while (this.mFragments.size() <= n) {
            this.mFragments.add(null);
        }
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        this.mFragments.set(n, fragment);
        this.mCurTransaction.add(viewGroup.getId(), fragment);
        return fragment;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        if (((Fragment)object).getView() == view) {
            return true;
        }
        return false;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader object) {
        if (parcelable != null) {
            int n;
            parcelable = (Bundle)parcelable;
            parcelable.setClassLoader((ClassLoader)object);
            object = parcelable.getParcelableArray("states");
            this.mSavedState.clear();
            this.mFragments.clear();
            if (object != null) {
                for (n = 0; n < object.length; ++n) {
                    this.mSavedState.add((Fragment.SavedState)object[n]);
                }
            }
            for (String string2 : parcelable.keySet()) {
                if (!string2.startsWith("f")) continue;
                n = Integer.parseInt(string2.substring(1));
                Object object2 = this.mFragmentManager.getFragment((Bundle)parcelable, string2);
                if (object2 != null) {
                    while (this.mFragments.size() <= n) {
                        this.mFragments.add(null);
                    }
                    object2.setMenuVisibility(false);
                    this.mFragments.set(n, (Fragment)object2);
                    continue;
                }
                object2 = new StringBuilder();
                object2.append("Bad fragment at key ");
                object2.append(string2);
                Log.w((String)"FragmentStatePagerAdapt", (String)object2.toString());
            }
        }
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle;
        Object object = null;
        if (this.mSavedState.size() > 0) {
            object = new Bundle();
            bundle = new Fragment.SavedState[this.mSavedState.size()];
            this.mSavedState.toArray((T[])bundle);
            object.putParcelableArray("states", (Parcelable[])bundle);
        }
        for (int i = 0; i < this.mFragments.size(); ++i) {
            Fragment fragment = this.mFragments.get(i);
            bundle = object;
            if (fragment != null) {
                bundle = object;
                if (fragment.isAdded()) {
                    bundle = object;
                    if (object == null) {
                        bundle = new Bundle();
                    }
                    object = new StringBuilder();
                    object.append("f");
                    object.append(i);
                    object = object.toString();
                    this.mFragmentManager.putFragment(bundle, (String)object, fragment);
                }
            }
            object = bundle;
        }
        return object;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup object, int n, @NonNull Object object2) {
        object = (Fragment)object2;
        object2 = this.mCurrentPrimaryItem;
        if (object != object2) {
            if (object2 != null) {
                object2.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            object.setMenuVisibility(true);
            object.setUserVisibleHint(true);
            this.mCurrentPrimaryItem = object;
        }
    }

    @Override
    public void startUpdate(@NonNull ViewGroup object) {
        if (object.getId() != -1) {
            return;
        }
        object = new StringBuilder();
        object.append("ViewPager with adapter ");
        object.append(this);
        object.append(" requires a view id");
        throw new IllegalStateException(object.toString());
    }
}
