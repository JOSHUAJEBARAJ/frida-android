/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.View
 */
package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat21;
import android.support.v4.app.ActivityOptionsCompatJB;
import android.support.v4.util.Pair;
import android.view.View;

public class ActivityOptionsCompat {
    protected ActivityOptionsCompat() {
    }

    public static ActivityOptionsCompat makeCustomAnimation(Context context, int n, int n2) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeCustomAnimation(context, n, n2));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeScaleUpAnimation(View view, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeScaleUpAnimation(view, n, n2, n3, n4));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, View view, String string2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(activity, view, string2));
        }
        return new ActivityOptionsCompat();
    }

    public static /* varargs */ ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, Pair<View, String> ... arrpair) {
        if (Build.VERSION.SDK_INT >= 21) {
            View[] arrview = null;
            String[] arrstring = null;
            if (arrpair != null) {
                View[] arrview2 = new View[arrpair.length];
                String[] arrstring2 = new String[arrpair.length];
                int n = 0;
                do {
                    arrstring = arrstring2;
                    arrview = arrview2;
                    if (n >= arrpair.length) break;
                    arrview2[n] = (View)arrpair[n].first;
                    arrstring2[n] = (String)arrpair[n].second;
                    ++n;
                } while (true);
            }
            return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(activity, arrview, arrstring));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View view, Bitmap bitmap, int n, int n2) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(view, bitmap, n, n2));
        }
        return new ActivityOptionsCompat();
    }

    public Bundle toBundle() {
        return null;
    }

    public void update(ActivityOptionsCompat activityOptionsCompat) {
    }

    private static class ActivityOptionsImpl21
    extends ActivityOptionsCompat {
        private final ActivityOptionsCompat21 mImpl;

        ActivityOptionsImpl21(ActivityOptionsCompat21 activityOptionsCompat21) {
            this.mImpl = activityOptionsCompat21;
        }

        @Override
        public Bundle toBundle() {
            return this.mImpl.toBundle();
        }

        @Override
        public void update(ActivityOptionsCompat activityOptionsCompat) {
            if (activityOptionsCompat instanceof ActivityOptionsImpl21) {
                activityOptionsCompat = (ActivityOptionsImpl21)activityOptionsCompat;
                this.mImpl.update(activityOptionsCompat.mImpl);
            }
        }
    }

    private static class ActivityOptionsImplJB
    extends ActivityOptionsCompat {
        private final ActivityOptionsCompatJB mImpl;

        ActivityOptionsImplJB(ActivityOptionsCompatJB activityOptionsCompatJB) {
            this.mImpl = activityOptionsCompatJB;
        }

        @Override
        public Bundle toBundle() {
            return this.mImpl.toBundle();
        }

        @Override
        public void update(ActivityOptionsCompat activityOptionsCompat) {
            if (activityOptionsCompat instanceof ActivityOptionsImplJB) {
                activityOptionsCompat = (ActivityOptionsImplJB)activityOptionsCompat;
                this.mImpl.update(activityOptionsCompat.mImpl);
            }
        }
    }

}

