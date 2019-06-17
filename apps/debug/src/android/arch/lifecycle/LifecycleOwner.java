/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

public interface LifecycleOwner {
    @NonNull
    public Lifecycle getLifecycle();
}

