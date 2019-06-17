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
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.CoreComponentFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@RequiresApi(value=28)
public class AppComponentFactory
extends android.app.AppComponentFactory {
    public final Activity instantiateActivity(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateActivityCompat(classLoader, string2, intent));
    }

    @NonNull
    public Activity instantiateActivityCompat(@NonNull ClassLoader classLoader, @NonNull String string2, @Nullable Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = (Activity)classLoader.loadClass(string2).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }

    public final Application instantiateApplication(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateApplicationCompat(classLoader, string2));
    }

    @NonNull
    public Application instantiateApplicationCompat(@NonNull ClassLoader classLoader, @NonNull String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = (Application)classLoader.loadClass(string2).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }

    public final ContentProvider instantiateProvider(ClassLoader classLoader, String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateProviderCompat(classLoader, string2));
    }

    @NonNull
    public ContentProvider instantiateProviderCompat(@NonNull ClassLoader classLoader, @NonNull String string2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = (ContentProvider)classLoader.loadClass(string2).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }

    public final BroadcastReceiver instantiateReceiver(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateReceiverCompat(classLoader, string2, intent));
    }

    @NonNull
    public BroadcastReceiver instantiateReceiverCompat(@NonNull ClassLoader classLoader, @NonNull String string2, @Nullable Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = (BroadcastReceiver)classLoader.loadClass(string2).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }

    public final Service instantiateService(ClassLoader classLoader, String string2, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateServiceCompat(classLoader, string2, intent));
    }

    @NonNull
    public Service instantiateServiceCompat(@NonNull ClassLoader classLoader, @NonNull String string2, @Nullable Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        void var1_4;
        try {
            classLoader = (Service)classLoader.loadClass(string2).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return classLoader;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new RuntimeException("Couldn't call constructor", (Throwable)var1_4);
    }
}

