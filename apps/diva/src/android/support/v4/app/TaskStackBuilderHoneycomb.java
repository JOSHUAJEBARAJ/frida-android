/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 */
package android.support.v4.app;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

class TaskStackBuilderHoneycomb {
    TaskStackBuilderHoneycomb() {
    }

    public static PendingIntent getActivitiesPendingIntent(Context context, int n, Intent[] arrintent, int n2) {
        return PendingIntent.getActivities((Context)context, (int)n, (Intent[])arrintent, (int)n2);
    }
}
