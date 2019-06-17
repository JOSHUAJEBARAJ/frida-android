/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.AppComponentFactory
 *  android.app.Application
 *  android.app.Service
 *  android.content.BroadcastReceiver
 *  android.content.ContentProvider
 *  android.content.Intent
 */
package android.support.v4.app;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;

@RequiresApi(api=28)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class CoreComponentFactory
extends AppComponentFactory {
    private static final String TAG = "CoreComponentFactory";

    static <T> T checkCompatWrapper(T t) {
        Object object;
        if (t instanceof CompatWrapped && (object = ((CompatWrapped)t).getWrapper()) != null) {
            return (T)object;
        }
        return t;
    }

    public Activity instantiateActivity(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateActivity(classLoader, string2, intent));
    }

    public Application instantiateApplication(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateApplication(classLoader, string2));
    }

    public ContentProvider instantiateProvider(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateProvider(classLoader, string2));
    }

    public BroadcastReceiver instantiateReceiver(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateReceiver(classLoader, string2, intent));
    }

    public Service instantiateService(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateService(classLoader, string2, intent));
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static interface CompatWrapped {
        public Object getWrapper();
    }

}

