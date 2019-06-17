/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public abstract class ComputableLiveData<T> {
    private AtomicBoolean mComputing = new AtomicBoolean(false);
    private final Executor mExecutor;
    private AtomicBoolean mInvalid = new AtomicBoolean(true);
    @VisibleForTesting
    final Runnable mInvalidationRunnable;
    private final LiveData<T> mLiveData;
    @VisibleForTesting
    final Runnable mRefreshRunnable;

    public ComputableLiveData() {
        this(ArchTaskExecutor.getIOThreadExecutor());
    }

    public ComputableLiveData(@NonNull Executor executor) {
        this.mRefreshRunnable = new Runnable(){

            @WorkerThread
            @Override
            public void run() {
                boolean bl;
                do {
                    bl = false;
                    boolean bl2 = false;
                    if (!ComputableLiveData.this.mComputing.compareAndSet(false, true)) continue;
                    Object t = null;
                    bl = bl2;
                    do {
                        if (!ComputableLiveData.this.mInvalid.compareAndSet(true, false)) break;
                        bl = true;
                        t = ComputableLiveData.this.compute();
                        continue;
                        break;
                    } while (true);
                    if (!bl) continue;
                    try {
                        ComputableLiveData.this.mLiveData.postValue(t);
                    }
                    catch (Throwable throwable) {
                        throw throwable;
                    }
                    finally {
                        ComputableLiveData.this.mComputing.set(false);
                    }
                } while (bl && ComputableLiveData.this.mInvalid.get());
            }
        };
        this.mInvalidationRunnable = new Runnable(){

            @MainThread
            @Override
            public void run() {
                boolean bl = ComputableLiveData.this.mLiveData.hasActiveObservers();
                if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && bl) {
                    ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
                }
            }
        };
        this.mExecutor = executor;
        this.mLiveData = new LiveData<T>(){

            @Override
            protected void onActive() {
                ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
            }
        };
    }

    @WorkerThread
    protected abstract T compute();

    @NonNull
    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }

    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }

}

