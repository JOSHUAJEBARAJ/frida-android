/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.Log
 */
package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public final class NavUtils {
    public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
    private static final String TAG = "NavUtils";

    private NavUtils() {
    }

    @Nullable
    public static Intent getParentActivityIntent(@NonNull Activity activity) {
        Object object;
        if (Build.VERSION.SDK_INT >= 16 && (object = activity.getParentActivityIntent()) != null) {
            return object;
        }
        object = NavUtils.getParentActivityName(activity);
        if (object == null) {
            return null;
        }
        ComponentName componentName = new ComponentName((Context)activity, (String)object);
        try {
            if (NavUtils.getParentActivityName((Context)activity, componentName) == null) {
                return Intent.makeMainActivity((ComponentName)componentName);
            }
            activity = new Intent().setComponent(componentName);
            return activity;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getParentActivityIntent: bad parentActivityName '");
            stringBuilder.append((String)object);
            stringBuilder.append("' in manifest");
            Log.e((String)"NavUtils", (String)stringBuilder.toString());
            return null;
        }
    }

    @Nullable
    public static Intent getParentActivityIntent(@NonNull Context context, @NonNull ComponentName componentName) throws PackageManager.NameNotFoundException {
        String string2 = NavUtils.getParentActivityName(context, componentName);
        if (string2 == null) {
            return null;
        }
        if (NavUtils.getParentActivityName(context, componentName = new ComponentName(componentName.getPackageName(), string2)) == null) {
            return Intent.makeMainActivity((ComponentName)componentName);
        }
        return new Intent().setComponent(componentName);
    }

    @Nullable
    public static Intent getParentActivityIntent(@NonNull Context context, @NonNull Class<?> object) throws PackageManager.NameNotFoundException {
        if ((object = NavUtils.getParentActivityName(context, new ComponentName(context, object))) == null) {
            return null;
        }
        if (NavUtils.getParentActivityName(context, (ComponentName)(object = new ComponentName(context, (String)object))) == null) {
            return Intent.makeMainActivity((ComponentName)object);
        }
        return new Intent().setComponent((ComponentName)object);
    }

    @Nullable
    public static String getParentActivityName(@NonNull Activity object) {
        try {
            object = NavUtils.getParentActivityName((Context)object, object.getComponentName());
            return object;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            throw new IllegalArgumentException((Throwable)nameNotFoundException);
        }
    }

    @Nullable
    public static String getParentActivityName(@NonNull Context context, @NonNull ComponentName object) throws PackageManager.NameNotFoundException {
        String string2;
        object = context.getPackageManager().getActivityInfo((ComponentName)object, 128);
        if (Build.VERSION.SDK_INT >= 16 && (string2 = object.parentActivityName) != null) {
            return string2;
        }
        if (object.metaData == null) {
            return null;
        }
        string2 = object.metaData.getString("android.support.PARENT_ACTIVITY");
        if (string2 == null) {
            return null;
        }
        object = string2;
        if (string2.charAt(0) == '.') {
            object = new StringBuilder();
            object.append(context.getPackageName());
            object.append(string2);
            object = object.toString();
        }
        return object;
    }

    public static void navigateUpFromSameTask(@NonNull Activity activity) {
        Object object = NavUtils.getParentActivityIntent(activity);
        if (object != null) {
            NavUtils.navigateUpTo(activity, (Intent)object);
            return;
        }
        object = new StringBuilder();
        object.append("Activity ");
        object.append(activity.getClass().getSimpleName());
        object.append(" does not have a parent activity name specified.");
        object.append(" (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> ");
        object.append(" element in your manifest?)");
        throw new IllegalArgumentException(object.toString());
    }

    public static void navigateUpTo(@NonNull Activity activity, @NonNull Intent intent) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.navigateUpTo(intent);
            return;
        }
        intent.addFlags(67108864);
        activity.startActivity(intent);
        activity.finish();
    }

    public static boolean shouldUpRecreateTask(@NonNull Activity object, @NonNull Intent intent) {
        if (Build.VERSION.SDK_INT >= 16) {
            return object.shouldUpRecreateTask(intent);
        }
        if ((object = object.getIntent().getAction()) != null && !object.equals("android.intent.action.MAIN")) {
            return true;
        }
        return false;
    }
}

