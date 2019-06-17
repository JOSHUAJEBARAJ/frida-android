/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.ActivityOptions
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.Pair
 *  android.view.View
 */
package android.support.v4.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Pair;
import android.view.View;

public class ActivityOptionsCompat {
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

    protected ActivityOptionsCompat() {
    }

    @NonNull
    public static ActivityOptionsCompat makeBasic() {
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeBasic());
        }
        return new ActivityOptionsCompat();
    }

    @NonNull
    public static ActivityOptionsCompat makeClipRevealAnimation(@NonNull View view, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeClipRevealAnimation((View)view, (int)n, (int)n2, (int)n3, (int)n4));
        }
        return new ActivityOptionsCompat();
    }

    @NonNull
    public static ActivityOptionsCompat makeCustomAnimation(@NonNull Context context, int n, int n2) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeCustomAnimation((Context)context, (int)n, (int)n2));
        }
        return new ActivityOptionsCompat();
    }

    @NonNull
    public static ActivityOptionsCompat makeScaleUpAnimation(@NonNull View view, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeScaleUpAnimation((View)view, (int)n, (int)n2, (int)n3, (int)n4));
        }
        return new ActivityOptionsCompat();
    }

    @NonNull
    public static ActivityOptionsCompat makeSceneTransitionAnimation(@NonNull Activity activity, @NonNull View view, @NonNull String string2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeSceneTransitionAnimation((Activity)activity, (View)view, (String)string2));
        }
        return new ActivityOptionsCompat();
    }

    @NonNull
    public static /* varargs */ ActivityOptionsCompat makeSceneTransitionAnimation(@NonNull Activity activity, android.support.v4.util.Pair<View, String> ... arrpair) {
        if (Build.VERSION.SDK_INT >= 21) {
            Pair[] arrpair2 = null;
            if (arrpair != null) {
                Pair[] arrpair3 = new Pair[arrpair.length];
                int n = 0;
                do {
                    arrpair2 = arrpair3;
                    if (n >= arrpair.length) break;
                    arrpair3[n] = Pair.create(arrpair[n].first, arrpair[n].second);
                    ++n;
                } while (true);
            }
            return new ActivityOptionsCompatImpl(ActivityOptions.makeSceneTransitionAnimation((Activity)activity, (Pair[])arrpair2));
        }
        return new ActivityOptionsCompat();
    }

    @NonNull
    public static ActivityOptionsCompat makeTaskLaunchBehind() {
        if (Build.VERSION.SDK_INT >= 21) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeTaskLaunchBehind());
        }
        return new ActivityOptionsCompat();
    }

    @NonNull
    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(@NonNull View view, @NonNull Bitmap bitmap, int n, int n2) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeThumbnailScaleUpAnimation((View)view, (Bitmap)bitmap, (int)n, (int)n2));
        }
        return new ActivityOptionsCompat();
    }

    @Nullable
    public Rect getLaunchBounds() {
        return null;
    }

    public void requestUsageTimeReport(@NonNull PendingIntent pendingIntent) {
    }

    @NonNull
    public ActivityOptionsCompat setLaunchBounds(@Nullable Rect rect) {
        return this;
    }

    @Nullable
    public Bundle toBundle() {
        return null;
    }

    public void update(@NonNull ActivityOptionsCompat activityOptionsCompat) {
    }

    @RequiresApi(value=16)
    private static class ActivityOptionsCompatImpl
    extends ActivityOptionsCompat {
        private final ActivityOptions mActivityOptions;

        ActivityOptionsCompatImpl(ActivityOptions activityOptions) {
            this.mActivityOptions = activityOptions;
        }

        @Override
        public Rect getLaunchBounds() {
            if (Build.VERSION.SDK_INT < 24) {
                return null;
            }
            return this.mActivityOptions.getLaunchBounds();
        }

        @Override
        public void requestUsageTimeReport(PendingIntent pendingIntent) {
            if (Build.VERSION.SDK_INT >= 23) {
                this.mActivityOptions.requestUsageTimeReport(pendingIntent);
            }
        }

        @Override
        public ActivityOptionsCompat setLaunchBounds(@Nullable Rect rect) {
            if (Build.VERSION.SDK_INT < 24) {
                return this;
            }
            return new ActivityOptionsCompatImpl(this.mActivityOptions.setLaunchBounds(rect));
        }

        @Override
        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }

        @Override
        public void update(ActivityOptionsCompat activityOptionsCompat) {
            if (activityOptionsCompat instanceof ActivityOptionsCompatImpl) {
                activityOptionsCompat = (ActivityOptionsCompatImpl)activityOptionsCompat;
                this.mActivityOptions.update(activityOptionsCompat.mActivityOptions);
            }
        }
    }

}

