/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.media.MediaRouter
 *  android.media.MediaRouter$Callback
 *  android.media.MediaRouter$RouteCategory
 *  android.media.MediaRouter$RouteGroup
 *  android.media.MediaRouter$RouteInfo
 *  android.media.MediaRouter$UserRouteInfo
 *  android.media.MediaRouter$VolumeCallback
 *  android.media.RemoteControlClient
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package android.support.v4.media.routing;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRouter;
import android.media.RemoteControlClient;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class MediaRouterJellybean {
    public static final int ALL_ROUTE_TYPES = 8388611;
    public static final int ROUTE_TYPE_LIVE_AUDIO = 1;
    public static final int ROUTE_TYPE_LIVE_VIDEO = 2;
    public static final int ROUTE_TYPE_USER = 8388608;
    private static final String TAG = "MediaRouterJellybean";

    MediaRouterJellybean() {
    }

    public static void addCallback(Object object, int n, Object object2) {
        ((MediaRouter)object).addCallback(n, (MediaRouter.Callback)object2);
    }

    public static void addUserRoute(Object object, Object object2) {
        ((MediaRouter)object).addUserRoute((MediaRouter.UserRouteInfo)object2);
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy<Callback>(callback);
    }

    public static Object createRouteCategory(Object object, String string2, boolean bl) {
        return ((MediaRouter)object).createRouteCategory((CharSequence)string2, bl);
    }

    public static Object createUserRoute(Object object, Object object2) {
        return ((MediaRouter)object).createUserRoute((MediaRouter.RouteCategory)object2);
    }

    public static Object createVolumeCallback(VolumeCallback volumeCallback) {
        return new VolumeCallbackProxy<VolumeCallback>(volumeCallback);
    }

    public static List getCategories(Object object) {
        object = (MediaRouter)object;
        int n = object.getCategoryCount();
        ArrayList<MediaRouter.RouteCategory> arrayList = new ArrayList<MediaRouter.RouteCategory>(n);
        for (int i = 0; i < n; ++i) {
            arrayList.add(object.getCategoryAt(i));
        }
        return arrayList;
    }

    public static Object getMediaRouter(Context context) {
        return context.getSystemService("media_router");
    }

    public static List getRoutes(Object object) {
        object = (MediaRouter)object;
        int n = object.getRouteCount();
        ArrayList<MediaRouter.RouteInfo> arrayList = new ArrayList<MediaRouter.RouteInfo>(n);
        for (int i = 0; i < n; ++i) {
            arrayList.add(object.getRouteAt(i));
        }
        return arrayList;
    }

    public static Object getSelectedRoute(Object object, int n) {
        return ((MediaRouter)object).getSelectedRoute(n);
    }

    public static void removeCallback(Object object, Object object2) {
        ((MediaRouter)object).removeCallback((MediaRouter.Callback)object2);
    }

    public static void removeUserRoute(Object object, Object object2) {
        ((MediaRouter)object).removeUserRoute((MediaRouter.UserRouteInfo)object2);
    }

    public static void selectRoute(Object object, int n, Object object2) {
        ((MediaRouter)object).selectRoute(n, (MediaRouter.RouteInfo)object2);
    }

    public static interface Callback {
        public void onRouteAdded(Object var1);

        public void onRouteChanged(Object var1);

        public void onRouteGrouped(Object var1, Object var2, int var3);

        public void onRouteRemoved(Object var1);

        public void onRouteSelected(int var1, Object var2);

        public void onRouteUngrouped(Object var1, Object var2);

        public void onRouteUnselected(int var1, Object var2);

        public void onRouteVolumeChanged(Object var1);
    }

    static class CallbackProxy<T extends Callback>
    extends MediaRouter.Callback {
        protected final T mCallback;

        public CallbackProxy(T t) {
            this.mCallback = t;
        }

        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteAdded((Object)routeInfo);
        }

        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteChanged((Object)routeInfo);
        }

        public void onRouteGrouped(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo, MediaRouter.RouteGroup routeGroup, int n) {
            this.mCallback.onRouteGrouped((Object)routeInfo, (Object)routeGroup, n);
        }

        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteRemoved((Object)routeInfo);
        }

        public void onRouteSelected(MediaRouter mediaRouter, int n, MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteSelected(n, (Object)routeInfo);
        }

        public void onRouteUngrouped(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo, MediaRouter.RouteGroup routeGroup) {
            this.mCallback.onRouteUngrouped((Object)routeInfo, (Object)routeGroup);
        }

        public void onRouteUnselected(MediaRouter mediaRouter, int n, MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteUnselected(n, (Object)routeInfo);
        }

        public void onRouteVolumeChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            this.mCallback.onRouteVolumeChanged((Object)routeInfo);
        }
    }

    public static final class GetDefaultRouteWorkaround {
        private Method mGetSystemAudioRouteMethod;

        public GetDefaultRouteWorkaround() {
            if (Build.VERSION.SDK_INT < 16 || Build.VERSION.SDK_INT > 17) {
                throw new UnsupportedOperationException();
            }
            try {
                this.mGetSystemAudioRouteMethod = MediaRouter.class.getMethod("getSystemAudioRoute", new Class[0]);
                return;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                return;
            }
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public Object getDefaultRoute(Object object) {
            object = (MediaRouter)object;
            if (this.mGetSystemAudioRouteMethod == null) return object.getRouteAt(0);
            try {
                return this.mGetSystemAudioRouteMethod.invoke(object, new Object[0]);
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
            return object.getRouteAt(0);
            catch (IllegalAccessException illegalAccessException) {
                return object.getRouteAt(0);
            }
        }
    }

    public static final class RouteCategory {
        public static CharSequence getName(Object object, Context context) {
            return ((MediaRouter.RouteCategory)object).getName(context);
        }

        public static List getRoutes(Object object) {
            ArrayList arrayList = new ArrayList();
            ((MediaRouter.RouteCategory)object).getRoutes(arrayList);
            return arrayList;
        }

        public static int getSupportedTypes(Object object) {
            return ((MediaRouter.RouteCategory)object).getSupportedTypes();
        }

        public static boolean isGroupable(Object object) {
            return ((MediaRouter.RouteCategory)object).isGroupable();
        }
    }

    public static final class RouteGroup {
        public static List getGroupedRoutes(Object object) {
            object = (MediaRouter.RouteGroup)object;
            int n = object.getRouteCount();
            ArrayList<MediaRouter.RouteInfo> arrayList = new ArrayList<MediaRouter.RouteInfo>(n);
            for (int i = 0; i < n; ++i) {
                arrayList.add(object.getRouteAt(i));
            }
            return arrayList;
        }
    }

    public static final class RouteInfo {
        public static Object getCategory(Object object) {
            return ((MediaRouter.RouteInfo)object).getCategory();
        }

        public static Object getGroup(Object object) {
            return ((MediaRouter.RouteInfo)object).getGroup();
        }

        public static Drawable getIconDrawable(Object object) {
            return ((MediaRouter.RouteInfo)object).getIconDrawable();
        }

        public static CharSequence getName(Object object, Context context) {
            return ((MediaRouter.RouteInfo)object).getName(context);
        }

        public static int getPlaybackStream(Object object) {
            return ((MediaRouter.RouteInfo)object).getPlaybackStream();
        }

        public static int getPlaybackType(Object object) {
            return ((MediaRouter.RouteInfo)object).getPlaybackType();
        }

        public static CharSequence getStatus(Object object) {
            return ((MediaRouter.RouteInfo)object).getStatus();
        }

        public static int getSupportedTypes(Object object) {
            return ((MediaRouter.RouteInfo)object).getSupportedTypes();
        }

        public static Object getTag(Object object) {
            return ((MediaRouter.RouteInfo)object).getTag();
        }

        public static int getVolume(Object object) {
            return ((MediaRouter.RouteInfo)object).getVolume();
        }

        public static int getVolumeHandling(Object object) {
            return ((MediaRouter.RouteInfo)object).getVolumeHandling();
        }

        public static int getVolumeMax(Object object) {
            return ((MediaRouter.RouteInfo)object).getVolumeMax();
        }

        public static boolean isGroup(Object object) {
            return object instanceof MediaRouter.RouteGroup;
        }

        public static void requestSetVolume(Object object, int n) {
            ((MediaRouter.RouteInfo)object).requestSetVolume(n);
        }

        public static void requestUpdateVolume(Object object, int n) {
            ((MediaRouter.RouteInfo)object).requestUpdateVolume(n);
        }

        public static void setTag(Object object, Object object2) {
            ((MediaRouter.RouteInfo)object).setTag(object2);
        }
    }

    public static final class SelectRouteWorkaround {
        private Method mSelectRouteIntMethod;

        public SelectRouteWorkaround() {
            if (Build.VERSION.SDK_INT < 16 || Build.VERSION.SDK_INT > 17) {
                throw new UnsupportedOperationException();
            }
            try {
                this.mSelectRouteIntMethod = MediaRouter.class.getMethod("selectRouteInt", Integer.TYPE, MediaRouter.RouteInfo.class);
                return;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void selectRoute(Object object, int n, Object object2) {
            object = (MediaRouter)object;
            if ((8388608 & (object2 = (MediaRouter.RouteInfo)object2).getSupportedTypes()) == 0) {
                if (this.mSelectRouteIntMethod != null) {
                    try {
                        this.mSelectRouteIntMethod.invoke(object, n, object2);
                        return;
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        Log.w((String)"MediaRouterJellybean", (String)"Cannot programmatically select non-user route.  Media routing may not work.", (Throwable)illegalAccessException);
                    }
                    catch (InvocationTargetException invocationTargetException) {
                        Log.w((String)"MediaRouterJellybean", (String)"Cannot programmatically select non-user route.  Media routing may not work.", (Throwable)invocationTargetException);
                    }
                } else {
                    Log.w((String)"MediaRouterJellybean", (String)"Cannot programmatically select non-user route because the platform is missing the selectRouteInt() method.  Media routing may not work.");
                }
            }
            object.selectRoute(n, (MediaRouter.RouteInfo)object2);
        }
    }

    public static final class UserRouteInfo {
        public static void setIconDrawable(Object object, Drawable drawable2) {
            ((MediaRouter.UserRouteInfo)object).setIconDrawable(drawable2);
        }

        public static void setName(Object object, CharSequence charSequence) {
            ((MediaRouter.UserRouteInfo)object).setName(charSequence);
        }

        public static void setPlaybackStream(Object object, int n) {
            ((MediaRouter.UserRouteInfo)object).setPlaybackStream(n);
        }

        public static void setPlaybackType(Object object, int n) {
            ((MediaRouter.UserRouteInfo)object).setPlaybackType(n);
        }

        public static void setRemoteControlClient(Object object, Object object2) {
            ((MediaRouter.UserRouteInfo)object).setRemoteControlClient((RemoteControlClient)object2);
        }

        public static void setStatus(Object object, CharSequence charSequence) {
            ((MediaRouter.UserRouteInfo)object).setStatus(charSequence);
        }

        public static void setVolume(Object object, int n) {
            ((MediaRouter.UserRouteInfo)object).setVolume(n);
        }

        public static void setVolumeCallback(Object object, Object object2) {
            ((MediaRouter.UserRouteInfo)object).setVolumeCallback((MediaRouter.VolumeCallback)object2);
        }

        public static void setVolumeHandling(Object object, int n) {
            ((MediaRouter.UserRouteInfo)object).setVolumeHandling(n);
        }

        public static void setVolumeMax(Object object, int n) {
            ((MediaRouter.UserRouteInfo)object).setVolumeMax(n);
        }
    }

    public static interface VolumeCallback {
        public void onVolumeSetRequest(Object var1, int var2);

        public void onVolumeUpdateRequest(Object var1, int var2);
    }

    static class VolumeCallbackProxy<T extends VolumeCallback>
    extends MediaRouter.VolumeCallback {
        protected final T mCallback;

        public VolumeCallbackProxy(T t) {
            this.mCallback = t;
        }

        public void onVolumeSetRequest(MediaRouter.RouteInfo routeInfo, int n) {
            this.mCallback.onVolumeSetRequest((Object)routeInfo, n);
        }

        public void onVolumeUpdateRequest(MediaRouter.RouteInfo routeInfo, int n) {
            this.mCallback.onVolumeUpdateRequest((Object)routeInfo, n);
        }
    }

}

