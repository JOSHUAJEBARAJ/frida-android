/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.Log
 */
package android.support.v4.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public final class TaskStackBuilder
implements Iterable<Intent> {
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents = new ArrayList();
    private final Context mSourceContext;

    private TaskStackBuilder(Context context) {
        this.mSourceContext = context;
    }

    @NonNull
    public static TaskStackBuilder create(@NonNull Context context) {
        return new TaskStackBuilder(context);
    }

    @Deprecated
    public static TaskStackBuilder from(Context context) {
        return TaskStackBuilder.create(context);
    }

    @NonNull
    public TaskStackBuilder addNextIntent(@NonNull Intent intent) {
        this.mIntents.add(intent);
        return this;
    }

    @NonNull
    public TaskStackBuilder addNextIntentWithParentStack(@NonNull Intent intent) {
        ComponentName componentName;
        ComponentName componentName2 = componentName = intent.getComponent();
        if (componentName == null) {
            componentName2 = intent.resolveActivity(this.mSourceContext.getPackageManager());
        }
        if (componentName2 != null) {
            this.addParentStack(componentName2);
        }
        this.addNextIntent(intent);
        return this;
    }

    @NonNull
    public TaskStackBuilder addParentStack(@NonNull Activity activity) {
        Intent intent = null;
        if (activity instanceof SupportParentable) {
            intent = ((SupportParentable)activity).getSupportParentActivityIntent();
        }
        Intent intent2 = intent;
        if (intent == null) {
            intent2 = NavUtils.getParentActivityIntent(activity);
        }
        if (intent2 != null) {
            intent = intent2.getComponent();
            activity = intent;
            if (intent == null) {
                activity = intent2.resolveActivity(this.mSourceContext.getPackageManager());
            }
            this.addParentStack((ComponentName)activity);
            this.addNextIntent(intent2);
        }
        return this;
    }

    public TaskStackBuilder addParentStack(ComponentName componentName) {
        int n = this.mIntents.size();
        try {
            componentName = NavUtils.getParentActivityIntent(this.mSourceContext, componentName);
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.e((String)"TaskStackBuilder", (String)"Bad ComponentName while traversing activity parent metadata");
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException((Throwable)nameNotFoundException);
            throw illegalArgumentException;
        }
        while (componentName != null) {
            this.mIntents.add(n, (Intent)componentName);
            componentName = NavUtils.getParentActivityIntent(this.mSourceContext, componentName.getComponent());
        }
        return this;
    }

    @NonNull
    public TaskStackBuilder addParentStack(@NonNull Class<?> class_) {
        return this.addParentStack(new ComponentName(this.mSourceContext, class_));
    }

    @Nullable
    public Intent editIntentAt(int n) {
        return this.mIntents.get(n);
    }

    @Deprecated
    public Intent getIntent(int n) {
        return this.editIntentAt(n);
    }

    public int getIntentCount() {
        return this.mIntents.size();
    }

    @NonNull
    public Intent[] getIntents() {
        Intent[] arrintent = new Intent[this.mIntents.size()];
        if (arrintent.length == 0) {
            return arrintent;
        }
        arrintent[0] = new Intent(this.mIntents.get(0)).addFlags(268484608);
        for (int i = 1; i < arrintent.length; ++i) {
            arrintent[i] = new Intent(this.mIntents.get(i));
        }
        return arrintent;
    }

    @Nullable
    public PendingIntent getPendingIntent(int n, int n2) {
        return this.getPendingIntent(n, n2, null);
    }

    @Nullable
    public PendingIntent getPendingIntent(int n, int n2, @Nullable Bundle bundle) {
        if (!this.mIntents.isEmpty()) {
            Intent[] arrintent = this.mIntents;
            arrintent = arrintent.toArray((T[])new Intent[arrintent.size()]);
            arrintent[0] = new Intent(arrintent[0]).addFlags(268484608);
            if (Build.VERSION.SDK_INT >= 16) {
                return PendingIntent.getActivities((Context)this.mSourceContext, (int)n, (Intent[])arrintent, (int)n2, (Bundle)bundle);
            }
            return PendingIntent.getActivities((Context)this.mSourceContext, (int)n, (Intent[])arrintent, (int)n2);
        }
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
    }

    @Deprecated
    @Override
    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }

    public void startActivities() {
        this.startActivities(null);
    }

    public void startActivities(@Nullable Bundle bundle) {
        if (!this.mIntents.isEmpty()) {
            Intent[] arrintent = this.mIntents;
            arrintent = arrintent.toArray((T[])new Intent[arrintent.size()]);
            arrintent[0] = new Intent(arrintent[0]).addFlags(268484608);
            if (!ContextCompat.startActivities(this.mSourceContext, arrintent, bundle)) {
                bundle = new Intent(arrintent[arrintent.length - 1]);
                bundle.addFlags(268435456);
                this.mSourceContext.startActivity((Intent)bundle);
            }
            return;
        }
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
    }

    public static interface SupportParentable {
        @Nullable
        public Intent getSupportParentActivityIntent();
    }

}

