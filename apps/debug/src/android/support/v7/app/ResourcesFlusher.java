/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.LongSparseArray
 */
package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.LongSparseArray;
import java.lang.reflect.Field;
import java.util.Map;

class ResourcesFlusher {
    private static final String TAG = "ResourcesFlusher";
    private static Field sDrawableCacheField;
    private static boolean sDrawableCacheFieldFetched;
    private static Field sResourcesImplField;
    private static boolean sResourcesImplFieldFetched;
    private static Class sThemedResourceCacheClazz;
    private static boolean sThemedResourceCacheClazzFetched;
    private static Field sThemedResourceCache_mUnthemedEntriesField;
    private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;

    private ResourcesFlusher() {
    }

    static void flush(@NonNull Resources resources) {
        if (Build.VERSION.SDK_INT >= 28) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            ResourcesFlusher.flushNougats(resources);
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            ResourcesFlusher.flushMarshmallows(resources);
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            ResourcesFlusher.flushLollipops(resources);
        }
    }

    @RequiresApi(value=21)
    private static void flushLollipops(@NonNull Resources object) {
        Field field;
        if (!sDrawableCacheFieldFetched) {
            try {
                sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
                sDrawableCacheField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not retrieve Resources#mDrawableCache field", (Throwable)noSuchFieldException);
            }
            sDrawableCacheFieldFetched = true;
        }
        if ((field = sDrawableCacheField) != null) {
            Object var1_3 = null;
            try {
                object = (Map)field.get(object);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not retrieve value from Resources#mDrawableCache", (Throwable)illegalAccessException);
                object = var1_3;
            }
            if (object != null) {
                object.clear();
            }
        }
    }

    @RequiresApi(value=23)
    private static void flushMarshmallows(@NonNull Resources resources) {
        if (!sDrawableCacheFieldFetched) {
            try {
                sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
                sDrawableCacheField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not retrieve Resources#mDrawableCache field", (Throwable)noSuchFieldException);
            }
            sDrawableCacheFieldFetched = true;
        }
        Object object = null;
        Field field = sDrawableCacheField;
        Object object2 = object;
        if (field != null) {
            try {
                object2 = field.get((Object)resources);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not retrieve value from Resources#mDrawableCache", (Throwable)illegalAccessException);
                object2 = object;
            }
        }
        if (object2 == null) {
            return;
        }
        ResourcesFlusher.flushThemedResourcesCache(object2);
    }

    @RequiresApi(value=24)
    private static void flushNougats(@NonNull Resources object) {
        Field field;
        if (!sResourcesImplFieldFetched) {
            try {
                sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
                sResourcesImplField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not retrieve Resources#mResourcesImpl field", (Throwable)noSuchFieldException);
            }
            sResourcesImplFieldFetched = true;
        }
        if ((field = sResourcesImplField) == null) {
            return;
        }
        Object object2 = null;
        try {
            object = field.get(object);
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.e((String)"ResourcesFlusher", (String)"Could not retrieve value from Resources#mResourcesImpl", (Throwable)illegalAccessException);
            object = object2;
        }
        if (object == null) {
            return;
        }
        if (!sDrawableCacheFieldFetched) {
            try {
                sDrawableCacheField = object.getClass().getDeclaredField("mDrawableCache");
                sDrawableCacheField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not retrieve ResourcesImpl#mDrawableCache field", (Throwable)noSuchFieldException);
            }
            sDrawableCacheFieldFetched = true;
        }
        field = null;
        Field field2 = sDrawableCacheField;
        object2 = field;
        if (field2 != null) {
            try {
                object2 = field2.get(object);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not retrieve value from ResourcesImpl#mDrawableCache", (Throwable)illegalAccessException);
                object2 = field;
            }
        }
        if (object2 != null) {
            ResourcesFlusher.flushThemedResourcesCache(object2);
        }
    }

    @RequiresApi(value=16)
    private static void flushThemedResourcesCache(@NonNull Object object) {
        Class class_;
        Field field;
        if (!sThemedResourceCacheClazzFetched) {
            try {
                sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
            }
            catch (ClassNotFoundException classNotFoundException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not find ThemedResourceCache class", (Throwable)classNotFoundException);
            }
            sThemedResourceCacheClazzFetched = true;
        }
        if ((class_ = sThemedResourceCacheClazz) == null) {
            return;
        }
        if (!sThemedResourceCache_mUnthemedEntriesFieldFetched) {
            try {
                sThemedResourceCache_mUnthemedEntriesField = class_.getDeclaredField("mUnthemedEntries");
                sThemedResourceCache_mUnthemedEntriesField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.e((String)"ResourcesFlusher", (String)"Could not retrieve ThemedResourceCache#mUnthemedEntries field", (Throwable)noSuchFieldException);
            }
            sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
        }
        if ((field = sThemedResourceCache_mUnthemedEntriesField) == null) {
            return;
        }
        class_ = null;
        try {
            object = (LongSparseArray)field.get(object);
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.e((String)"ResourcesFlusher", (String)"Could not retrieve value from ThemedResourceCache#mUnthemedEntries", (Throwable)illegalAccessException);
            object = class_;
        }
        if (object != null) {
            object.clear();
        }
    }
}

