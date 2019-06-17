/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.hardware.display.DisplayManager
 *  android.media.MediaRouter
 *  android.media.MediaRouter$RouteInfo
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.util.Log
 *  android.view.Display
 */
package android.support.v4.media.routing;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Build;
import android.os.Handler;
import android.support.v4.media.routing.MediaRouterJellybean;
import android.util.Log;
import android.view.Display;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MediaRouterJellybeanMr1
extends MediaRouterJellybean {
    private static final String TAG = "MediaRouterJellybeanMr1";

    MediaRouterJellybeanMr1() {
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy<Callback>(callback);
    }

    public static final class ActiveScanWorkaround
    implements Runnable {
        private static final int WIFI_DISPLAY_SCAN_INTERVAL = 15000;
        private boolean mActivelyScanningWifiDisplays;
        private final DisplayManager mDisplayManager;
        private final Handler mHandler;
        private Method mScanWifiDisplaysMethod;

        public ActiveScanWorkaround(Context context, Handler handler) {
            if (Build.VERSION.SDK_INT != 17) {
                throw new UnsupportedOperationException();
            }
            this.mDisplayManager = (DisplayManager)context.getSystemService("display");
            this.mHandler = handler;
            try {
                this.mScanWifiDisplaysMethod = DisplayManager.class.getMethod("scanWifiDisplays", new Class[0]);
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
        @Override
        public void run() {
            if (this.mActivelyScanningWifiDisplays) {
                try {
                    this.mScanWifiDisplaysMethod.invoke((Object)this.mDisplayManager, new Object[0]);
                }
                catch (IllegalAccessException illegalAccessException) {
                    Log.w((String)"MediaRouterJellybeanMr1", (String)"Cannot scan for wifi displays.", (Throwable)illegalAccessException);
                }
                catch (InvocationTargetException invocationTargetException) {
                    Log.w((String)"MediaRouterJellybeanMr1", (String)"Cannot scan for wifi displays.", (Throwable)invocationTargetException);
                }
                this.mHandler.postDelayed((Runnable)this, 15000);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setActiveScanRouteTypes(int n) {
            if ((n & 2) != 0) {
                if (this.mActivelyScanningWifiDisplays) return;
                {
                    if (this.mScanWifiDisplaysMethod == null) {
                        Log.w((String)"MediaRouterJellybeanMr1", (String)"Cannot scan for wifi displays because the DisplayManager.scanWifiDisplays() method is not available on this device.");
                        return;
                    }
                    this.mActivelyScanningWifiDisplays = true;
                    this.mHandler.post((Runnable)this);
                    return;
                }
            } else {
                if (!this.mActivelyScanningWifiDisplays) return;
                {
                    this.mActivelyScanningWifiDisplays = false;
                    this.mHandler.removeCallbacks((Runnable)this);
                    return;
                }
            }
        }
    }

    public static interface Callback
    extends MediaRouterJellybean.Callback {
        public void onRoutePresentationDisplayChanged(Object var1);
    }

    static class CallbackProxy<T extends Callback>
    extends MediaRouterJellybean.CallbackProxy<T> {
        public CallbackProxy(T t) {
            super(t);
        }

        public void onRoutePresentationDisplayChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            ((Callback)this.mCallback).onRoutePresentationDisplayChanged((Object)routeInfo);
        }
    }

    public static final class IsConnectingWorkaround {
        private Method mGetStatusCodeMethod;
        private int mStatusConnecting;

        public IsConnectingWorkaround() {
            if (Build.VERSION.SDK_INT != 17) {
                throw new UnsupportedOperationException();
            }
            try {
                this.mStatusConnecting = MediaRouter.RouteInfo.class.getField("STATUS_CONNECTING").getInt(null);
                this.mGetStatusCodeMethod = MediaRouter.RouteInfo.class.getMethod("getStatusCode", new Class[0]);
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                return;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                return;
            }
            catch (NoSuchFieldException noSuchFieldException) {
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
        public boolean isConnecting(Object object) {
            object = (MediaRouter.RouteInfo)object;
            if (this.mGetStatusCodeMethod == null) return false;
            try {
                int n = (Integer)this.mGetStatusCodeMethod.invoke(object, new Object[0]);
                int n2 = this.mStatusConnecting;
                if (n != n2) return false;
                return true;
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
            return false;
            catch (IllegalAccessException illegalAccessException) {
                return false;
            }
        }
    }

    public static final class RouteInfo {
        public static Display getPresentationDisplay(Object object) {
            return ((MediaRouter.RouteInfo)object).getPresentationDisplay();
        }

        public static boolean isEnabled(Object object) {
            return ((MediaRouter.RouteInfo)object).isEnabled();
        }
    }

}

