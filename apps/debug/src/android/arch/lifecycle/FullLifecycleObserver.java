/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;

interface FullLifecycleObserver
extends LifecycleObserver {
    public void onCreate(LifecycleOwner var1);

    public void onDestroy(LifecycleOwner var1);

    public void onPause(LifecycleOwner var1);

    public void onResume(LifecycleOwner var1);

    public void onStart(LifecycleOwner var1);

    public void onStop(LifecycleOwner var1);
}

