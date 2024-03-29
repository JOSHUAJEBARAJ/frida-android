/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 */
package android.arch.core.executor;

import android.arch.core.executor.TaskExecutor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class DefaultTaskExecutor
extends TaskExecutor {
    private ExecutorService mDiskIO = Executors.newFixedThreadPool(2);
    private final Object mLock = new Object();
    @Nullable
    private volatile Handler mMainHandler;

    @Override
    public void executeOnDiskIO(Runnable runnable) {
        this.mDiskIO.execute(runnable);
    }

    @Override
    public boolean isMainThread() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void postToMainThread(Runnable runnable) {
        if (this.mMainHandler == null) {
            Object object = this.mLock;
            synchronized (object) {
                if (this.mMainHandler == null) {
                    this.mMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        this.mMainHandler.post(runnable);
    }
}

