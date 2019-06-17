/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.GeneratedAdapter;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MethodCallsLogger;
import android.support.annotation.RestrictTo;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class CompositeGeneratedAdaptersObserver
implements GenericLifecycleObserver {
    private final GeneratedAdapter[] mGeneratedAdapters;

    CompositeGeneratedAdaptersObserver(GeneratedAdapter[] arrgeneratedAdapter) {
        this.mGeneratedAdapters = arrgeneratedAdapter;
    }

    @Override
    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        int n;
        MethodCallsLogger methodCallsLogger = new MethodCallsLogger();
        GeneratedAdapter[] arrgeneratedAdapter = this.mGeneratedAdapters;
        int n2 = arrgeneratedAdapter.length;
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            arrgeneratedAdapter[n].callMethods(lifecycleOwner, event, false, methodCallsLogger);
        }
        arrgeneratedAdapter = this.mGeneratedAdapters;
        n2 = arrgeneratedAdapter.length;
        for (n = n3; n < n2; ++n) {
            arrgeneratedAdapter[n].callMethods(lifecycleOwner, event, true, methodCallsLogger);
        }
    }
}

