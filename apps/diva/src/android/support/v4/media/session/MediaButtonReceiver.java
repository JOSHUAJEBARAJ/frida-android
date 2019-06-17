/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.os.Parcelable
 *  android.view.KeyEvent
 */
package android.support.v4.media.session;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Parcelable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;
import java.util.List;

public class MediaButtonReceiver
extends BroadcastReceiver {
    public static KeyEvent handleIntent(MediaSessionCompat mediaSessionCompat, Intent intent) {
        if (mediaSessionCompat == null || intent == null || !"android.intent.action.MEDIA_BUTTON".equals(intent.getAction()) || !intent.hasExtra("android.intent.extra.KEY_EVENT")) {
            return null;
        }
        intent = (KeyEvent)intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
        mediaSessionCompat.getController().dispatchMediaButtonEvent((KeyEvent)intent);
        return intent;
    }

    public void onReceive(Context context, Intent intent) {
        Object object = new Intent("android.intent.action.MEDIA_BUTTON");
        object.setPackage(context.getPackageName());
        object = context.getPackageManager().queryIntentServices((Intent)object, 0);
        if (object.size() != 1) {
            throw new IllegalStateException("Expected 1 Service that handles android.intent.action.MEDIA_BUTTON, found " + object.size());
        }
        object = (ResolveInfo)object.get(0);
        intent.setComponent(new ComponentName(object.serviceInfo.packageName, object.serviceInfo.name));
        context.startService(intent);
    }
}

