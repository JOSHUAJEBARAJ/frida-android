/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.SystemClock
 *  android.widget.RemoteViews
 */
package android.support.v7.internal.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatBase;
import android.support.v7.appcompat.R;
import android.widget.RemoteViews;
import java.text.NumberFormat;
import java.util.List;

public class NotificationCompatImplBase {
    static final int MAX_MEDIA_BUTTONS = 5;
    static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;

    /*
     * Enabled aggressive block sorting
     */
    private static RemoteViews applyStandardTemplate(Context context, CharSequence object, CharSequence charSequence, CharSequence charSequence2, int n, Bitmap bitmap, CharSequence charSequence3, boolean bl, long l, int n2, boolean bl2) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), n2);
        n2 = 0;
        int n3 = 0;
        if (bitmap != null && Build.VERSION.SDK_INT >= 16) {
            remoteViews.setViewVisibility(R.id.icon, 0);
            remoteViews.setImageViewBitmap(R.id.icon, bitmap);
        } else {
            remoteViews.setViewVisibility(R.id.icon, 8);
        }
        if (object != null) {
            remoteViews.setTextViewText(R.id.title, (CharSequence)object);
        }
        if (charSequence != null) {
            remoteViews.setTextViewText(R.id.text, charSequence);
            n2 = 1;
        }
        if (charSequence2 != null) {
            remoteViews.setTextViewText(R.id.info, charSequence2);
            remoteViews.setViewVisibility(R.id.info, 0);
            n = 1;
        } else if (n > 0) {
            if (n > context.getResources().getInteger(R.integer.status_bar_notification_info_maxnum)) {
                remoteViews.setTextViewText(R.id.info, (CharSequence)context.getResources().getString(R.string.status_bar_notification_info_overflow));
            } else {
                object = NumberFormat.getIntegerInstance();
                remoteViews.setTextViewText(R.id.info, (CharSequence)object.format(n));
            }
            remoteViews.setViewVisibility(R.id.info, 0);
            n = 1;
        } else {
            remoteViews.setViewVisibility(R.id.info, 8);
            n = n2;
        }
        n2 = n3;
        if (charSequence3 != null) {
            n2 = n3;
            if (Build.VERSION.SDK_INT >= 16) {
                remoteViews.setTextViewText(R.id.text, charSequence3);
                if (charSequence != null) {
                    remoteViews.setTextViewText(R.id.text2, charSequence);
                    remoteViews.setViewVisibility(R.id.text2, 0);
                    n2 = 1;
                } else {
                    remoteViews.setViewVisibility(R.id.text2, 8);
                    n2 = n3;
                }
            }
        }
        if (n2 != 0 && Build.VERSION.SDK_INT >= 16) {
            if (bl2) {
                float f = context.getResources().getDimensionPixelSize(R.dimen.notification_subtext_size);
                remoteViews.setTextViewTextSize(R.id.text, 0, f);
            }
            remoteViews.setViewPadding(R.id.line1, 0, 0, 0, 0);
        }
        if (l != 0) {
            if (bl) {
                remoteViews.setViewVisibility(R.id.chronometer, 0);
                remoteViews.setLong(R.id.chronometer, "setBase", SystemClock.elapsedRealtime() - System.currentTimeMillis() + l);
                remoteViews.setBoolean(R.id.chronometer, "setStarted", true);
            } else {
                remoteViews.setViewVisibility(R.id.time, 0);
                remoteViews.setLong(R.id.time, "setTime", l);
            }
        }
        n2 = R.id.line3;
        n = n != 0 ? 0 : 8;
        remoteViews.setViewVisibility(n2, n);
        return remoteViews;
    }

    private static <T extends NotificationCompatBase.Action> RemoteViews generateBigContentView(Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, List<T> list, boolean bl2, PendingIntent pendingIntent) {
        int n2 = Math.min(list.size(), 5);
        charSequence = NotificationCompatImplBase.applyStandardTemplate(context, charSequence, charSequence2, charSequence3, n, bitmap, charSequence4, bl, l, NotificationCompatImplBase.getBigLayoutResource(n2), false);
        charSequence.removeAllViews(R.id.media_actions);
        if (n2 > 0) {
            for (n = 0; n < n2; ++n) {
                charSequence2 = NotificationCompatImplBase.generateMediaActionButton(context, (NotificationCompatBase.Action)list.get(n));
                charSequence.addView(R.id.media_actions, (RemoteViews)charSequence2);
            }
        }
        if (bl2) {
            charSequence.setViewVisibility(R.id.cancel_action, 0);
            charSequence.setInt(R.id.cancel_action, "setAlpha", context.getResources().getInteger(R.integer.cancel_button_image_alpha));
            charSequence.setOnClickPendingIntent(R.id.cancel_action, pendingIntent);
            return charSequence;
        }
        charSequence.setViewVisibility(R.id.cancel_action, 8);
        return charSequence;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static <T extends NotificationCompatBase.Action> RemoteViews generateContentView(Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, List<T> list, int[] arrn, boolean bl2, PendingIntent pendingIntent) {
        charSequence = NotificationCompatImplBase.applyStandardTemplate(context, charSequence, charSequence2, charSequence3, n, bitmap, charSequence4, bl, l, R.layout.notification_template_media, true);
        int n2 = list.size();
        n = arrn == null ? 0 : Math.min(arrn.length, 3);
        charSequence.removeAllViews(R.id.media_actions);
        if (n > 0) {
            for (int i = 0; i < n; ++i) {
                if (i >= n2) {
                    throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", i, n2 - 1));
                }
                charSequence2 = NotificationCompatImplBase.generateMediaActionButton(context, (NotificationCompatBase.Action)list.get(arrn[i]));
                charSequence.addView(R.id.media_actions, (RemoteViews)charSequence2);
            }
        }
        if (bl2) {
            charSequence.setViewVisibility(R.id.end_padder, 8);
            charSequence.setViewVisibility(R.id.cancel_action, 0);
            charSequence.setOnClickPendingIntent(R.id.cancel_action, pendingIntent);
            charSequence.setInt(R.id.cancel_action, "setAlpha", context.getResources().getInteger(R.integer.cancel_button_image_alpha));
            return charSequence;
        }
        charSequence.setViewVisibility(R.id.end_padder, 0);
        charSequence.setViewVisibility(R.id.cancel_action, 8);
        return charSequence;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static RemoteViews generateMediaActionButton(Context context, NotificationCompatBase.Action action) {
        boolean bl = action.getActionIntent() == null;
        context = new RemoteViews(context.getPackageName(), R.layout.notification_media_action);
        context.setImageViewResource(R.id.action0, action.getIcon());
        if (!bl) {
            context.setOnClickPendingIntent(R.id.action0, action.getActionIntent());
        }
        if (Build.VERSION.SDK_INT >= 15) {
            context.setContentDescription(R.id.action0, action.getTitle());
        }
        return context;
    }

    private static int getBigLayoutResource(int n) {
        if (n <= 3) {
            return R.layout.notification_template_big_media_narrow;
        }
        return R.layout.notification_template_big_media;
    }

    public static <T extends NotificationCompatBase.Action> void overrideBigContentView(Notification notification, Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, List<T> list, boolean bl2, PendingIntent pendingIntent) {
        notification.bigContentView = NotificationCompatImplBase.generateBigContentView(context, charSequence, charSequence2, charSequence3, n, bitmap, charSequence4, bl, l, list, bl2, pendingIntent);
        if (bl2) {
            notification.flags |= 2;
        }
    }

    public static <T extends NotificationCompatBase.Action> void overrideContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, Context context, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int n, Bitmap bitmap, CharSequence charSequence4, boolean bl, long l, List<T> list, int[] arrn, boolean bl2, PendingIntent pendingIntent) {
        context = NotificationCompatImplBase.generateContentView(context, charSequence, charSequence2, charSequence3, n, bitmap, charSequence4, bl, l, list, arrn, bl2, pendingIntent);
        notificationBuilderWithBuilderAccessor.getBuilder().setContent((RemoteViews)context);
        if (bl2) {
            notificationBuilderWithBuilderAccessor.getBuilder().setOngoing(true);
        }
    }
}

