/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.View
 */
package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public abstract class FragmentManager {
    public static final int POP_BACK_STACK_INCLUSIVE = 1;

    public static void enableDebugLogging(boolean bl) {
        FragmentManagerImpl.DEBUG = bl;
    }

    public abstract void addOnBackStackChangedListener(@NonNull OnBackStackChangedListener var1);

    @NonNull
    public abstract FragmentTransaction beginTransaction();

    public abstract void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4);

    public abstract boolean executePendingTransactions();

    @Nullable
    public abstract Fragment findFragmentById(@IdRes int var1);

    @Nullable
    public abstract Fragment findFragmentByTag(@Nullable String var1);

    @NonNull
    public abstract BackStackEntry getBackStackEntryAt(int var1);

    public abstract int getBackStackEntryCount();

    @Nullable
    public abstract Fragment getFragment(@NonNull Bundle var1, @NonNull String var2);

    @NonNull
    public abstract List<Fragment> getFragments();

    @Nullable
    public abstract Fragment getPrimaryNavigationFragment();

    public abstract boolean isDestroyed();

    public abstract boolean isStateSaved();

    @Deprecated
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public FragmentTransaction openTransaction() {
        return this.beginTransaction();
    }

    public abstract void popBackStack();

    public abstract void popBackStack(int var1, int var2);

    public abstract void popBackStack(@Nullable String var1, int var2);

    public abstract boolean popBackStackImmediate();

    public abstract boolean popBackStackImmediate(int var1, int var2);

    public abstract boolean popBackStackImmediate(@Nullable String var1, int var2);

    public abstract void putFragment(@NonNull Bundle var1, @NonNull String var2, @NonNull Fragment var3);

    public abstract void registerFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks var1, boolean var2);

    public abstract void removeOnBackStackChangedListener(@NonNull OnBackStackChangedListener var1);

    @Nullable
    public abstract Fragment.SavedState saveFragmentInstanceState(Fragment var1);

    public abstract void unregisterFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks var1);

    public static interface BackStackEntry {
        @Nullable
        public CharSequence getBreadCrumbShortTitle();

        @StringRes
        public int getBreadCrumbShortTitleRes();

        @Nullable
        public CharSequence getBreadCrumbTitle();

        @StringRes
        public int getBreadCrumbTitleRes();

        public int getId();

        @Nullable
        public String getName();
    }

    public static abstract class FragmentLifecycleCallbacks {
        public void onFragmentActivityCreated(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @Nullable Bundle bundle) {
        }

        public void onFragmentAttached(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @NonNull Context context) {
        }

        public void onFragmentCreated(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @Nullable Bundle bundle) {
        }

        public void onFragmentDestroyed(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        }

        public void onFragmentDetached(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        }

        public void onFragmentPaused(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        }

        public void onFragmentPreAttached(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @NonNull Context context) {
        }

        public void onFragmentPreCreated(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @Nullable Bundle bundle) {
        }

        public void onFragmentResumed(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        }

        public void onFragmentSaveInstanceState(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @NonNull Bundle bundle) {
        }

        public void onFragmentStarted(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        }

        public void onFragmentStopped(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        }

        public void onFragmentViewCreated(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @NonNull View view, @Nullable Bundle bundle) {
        }

        public void onFragmentViewDestroyed(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        }
    }

    public static interface OnBackStackChangedListener {
        public void onBackStackChanged();
    }

}

