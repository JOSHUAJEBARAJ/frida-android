/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Service
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.os.RemoteException
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel;

public abstract class NotificationCompatSideChannelService
extends Service {
    public abstract void cancel(String var1, int var2, String var3);

    public abstract void cancelAll(String var1);

    void checkPermission(int n, String object) {
        Object object2 = this.getPackageManager().getPackagesForUid(n);
        int n2 = object2.length;
        for (int i = 0; i < n2; ++i) {
            if (!object2[i].equals(object)) continue;
            return;
        }
        object2 = new StringBuilder();
        object2.append("NotificationSideChannelService: Uid ");
        object2.append(n);
        object2.append(" is not authorized for package ");
        object2.append((String)object);
        object = new SecurityException(object2.toString());
        throw object;
    }

    public abstract void notify(String var1, int var2, String var3, Notification var4);

    public IBinder onBind(Intent intent) {
        if (intent.getAction().equals("android.support.BIND_NOTIFICATION_SIDE_CHANNEL")) {
            if (Build.VERSION.SDK_INT > 19) {
                return null;
            }
            return new NotificationSideChannelStub();
        }
        return null;
    }

    private class NotificationSideChannelStub
    extends INotificationSideChannel.Stub {
        NotificationSideChannelStub() {
        }

        @Override
        public void cancel(String string2, int n, String string3) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l = NotificationSideChannelStub.clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.cancel(string2, n, string3);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l);
            }
        }

        @Override
        public void cancelAll(String string2) {
            NotificationCompatSideChannelService.this.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l = NotificationSideChannelStub.clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.cancelAll(string2);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l);
            }
        }

        @Override
        public void notify(String string2, int n, String string3, Notification notification) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l = NotificationSideChannelStub.clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.notify(string2, n, string3, notification);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l);
            }
        }
    }

}

