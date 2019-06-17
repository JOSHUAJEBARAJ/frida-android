/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.os.Bundle
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.MenuInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window
 */
package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegateImpl;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class AppCompatDelegate {
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
    public static final int FEATURE_SUPPORT_ACTION_BAR = 108;
    public static final int FEATURE_SUPPORT_ACTION_BAR_OVERLAY = 109;
    public static final int MODE_NIGHT_AUTO = 0;
    public static final int MODE_NIGHT_FOLLOW_SYSTEM = -1;
    public static final int MODE_NIGHT_NO = 1;
    static final int MODE_NIGHT_UNSPECIFIED = -100;
    public static final int MODE_NIGHT_YES = 2;
    static final String TAG = "AppCompatDelegate";
    private static int sDefaultNightMode = -1;

    AppCompatDelegate() {
    }

    public static AppCompatDelegate create(Activity activity, AppCompatCallback appCompatCallback) {
        return new AppCompatDelegateImpl((Context)activity, activity.getWindow(), appCompatCallback);
    }

    public static AppCompatDelegate create(Dialog dialog, AppCompatCallback appCompatCallback) {
        return new AppCompatDelegateImpl(dialog.getContext(), dialog.getWindow(), appCompatCallback);
    }

    public static AppCompatDelegate create(Context context, Window window, AppCompatCallback appCompatCallback) {
        return new AppCompatDelegateImpl(context, window, appCompatCallback);
    }

    public static int getDefaultNightMode() {
        return sDefaultNightMode;
    }

    public static boolean isCompatVectorFromResourcesEnabled() {
        return VectorEnabledTintResources.isCompatVectorFromResourcesEnabled();
    }

    public static void setCompatVectorFromResourcesEnabled(boolean bl) {
        VectorEnabledTintResources.setCompatVectorFromResourcesEnabled(bl);
    }

    public static void setDefaultNightMode(int n) {
        if (n != -1 && n != 0 && n != 1 && n != 2) {
            Log.d((String)"AppCompatDelegate", (String)"setDefaultNightMode() called with an unknown mode");
            return;
        }
        sDefaultNightMode = n;
    }

    public abstract void addContentView(View var1, ViewGroup.LayoutParams var2);

    public abstract boolean applyDayNight();

    public abstract View createView(@Nullable View var1, String var2, @NonNull Context var3, @NonNull AttributeSet var4);

    @Nullable
    public abstract <T extends View> T findViewById(@IdRes int var1);

    @Nullable
    public abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();

    public abstract MenuInflater getMenuInflater();

    @Nullable
    public abstract ActionBar getSupportActionBar();

    public abstract boolean hasWindowFeature(int var1);

    public abstract void installViewFactory();

    public abstract void invalidateOptionsMenu();

    public abstract boolean isHandleNativeActionModesEnabled();

    public abstract void onConfigurationChanged(Configuration var1);

    public abstract void onCreate(Bundle var1);

    public abstract void onDestroy();

    public abstract void onPostCreate(Bundle var1);

    public abstract void onPostResume();

    public abstract void onSaveInstanceState(Bundle var1);

    public abstract void onStart();

    public abstract void onStop();

    public abstract boolean requestWindowFeature(int var1);

    public abstract void setContentView(@LayoutRes int var1);

    public abstract void setContentView(View var1);

    public abstract void setContentView(View var1, ViewGroup.LayoutParams var2);

    public abstract void setHandleNativeActionModesEnabled(boolean var1);

    public abstract void setLocalNightMode(int var1);

    public abstract void setSupportActionBar(@Nullable Toolbar var1);

    public abstract void setTitle(@Nullable CharSequence var1);

    @Nullable
    public abstract ActionMode startSupportActionMode(@NonNull ActionMode.Callback var1);

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface NightMode {
    }

}

