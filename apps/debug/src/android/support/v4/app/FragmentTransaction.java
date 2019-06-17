/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.v4.app;

import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class FragmentTransaction {
    public static final int TRANSIT_ENTER_MASK = 4096;
    public static final int TRANSIT_EXIT_MASK = 8192;
    public static final int TRANSIT_FRAGMENT_CLOSE = 8194;
    public static final int TRANSIT_FRAGMENT_FADE = 4099;
    public static final int TRANSIT_FRAGMENT_OPEN = 4097;
    public static final int TRANSIT_NONE = 0;
    public static final int TRANSIT_UNSET = -1;

    @NonNull
    public abstract FragmentTransaction add(@IdRes int var1, @NonNull Fragment var2);

    @NonNull
    public abstract FragmentTransaction add(@IdRes int var1, @NonNull Fragment var2, @Nullable String var3);

    @NonNull
    public abstract FragmentTransaction add(@NonNull Fragment var1, @Nullable String var2);

    @NonNull
    public abstract FragmentTransaction addSharedElement(@NonNull View var1, @NonNull String var2);

    @NonNull
    public abstract FragmentTransaction addToBackStack(@Nullable String var1);

    @NonNull
    public abstract FragmentTransaction attach(@NonNull Fragment var1);

    public abstract int commit();

    public abstract int commitAllowingStateLoss();

    public abstract void commitNow();

    public abstract void commitNowAllowingStateLoss();

    @NonNull
    public abstract FragmentTransaction detach(@NonNull Fragment var1);

    @NonNull
    public abstract FragmentTransaction disallowAddToBackStack();

    @NonNull
    public abstract FragmentTransaction hide(@NonNull Fragment var1);

    public abstract boolean isAddToBackStackAllowed();

    public abstract boolean isEmpty();

    @NonNull
    public abstract FragmentTransaction remove(@NonNull Fragment var1);

    @NonNull
    public abstract FragmentTransaction replace(@IdRes int var1, @NonNull Fragment var2);

    @NonNull
    public abstract FragmentTransaction replace(@IdRes int var1, @NonNull Fragment var2, @Nullable String var3);

    @NonNull
    public abstract FragmentTransaction runOnCommit(@NonNull Runnable var1);

    @Deprecated
    public abstract FragmentTransaction setAllowOptimization(boolean var1);

    @NonNull
    public abstract FragmentTransaction setBreadCrumbShortTitle(@StringRes int var1);

    @NonNull
    public abstract FragmentTransaction setBreadCrumbShortTitle(@Nullable CharSequence var1);

    @NonNull
    public abstract FragmentTransaction setBreadCrumbTitle(@StringRes int var1);

    @NonNull
    public abstract FragmentTransaction setBreadCrumbTitle(@Nullable CharSequence var1);

    @NonNull
    public abstract FragmentTransaction setCustomAnimations(@AnimRes @AnimatorRes int var1, @AnimRes @AnimatorRes int var2);

    @NonNull
    public abstract FragmentTransaction setCustomAnimations(@AnimRes @AnimatorRes int var1, @AnimRes @AnimatorRes int var2, @AnimRes @AnimatorRes int var3, @AnimRes @AnimatorRes int var4);

    @NonNull
    public abstract FragmentTransaction setPrimaryNavigationFragment(@Nullable Fragment var1);

    @NonNull
    public abstract FragmentTransaction setReorderingAllowed(boolean var1);

    @NonNull
    public abstract FragmentTransaction setTransition(int var1);

    @NonNull
    public abstract FragmentTransaction setTransitionStyle(@StyleRes int var1);

    @NonNull
    public abstract FragmentTransaction show(@NonNull Fragment var1);
}

