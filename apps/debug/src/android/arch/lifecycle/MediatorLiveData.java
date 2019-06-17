/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.core.internal.SafeIterableMap;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

public class MediatorLiveData<T>
extends MutableLiveData<T> {
    private SafeIterableMap<LiveData<?>, Source<?>> mSources = new SafeIterableMap();

    @MainThread
    public <S> void addSource(@NonNull LiveData<S> object, @NonNull Observer<S> observer) {
        Source<S> source = new Source<S>((LiveData<S>)object, observer);
        if ((object = this.mSources.putIfAbsent(object, source)) != null && object.mObserver != observer) {
            throw new IllegalArgumentException("This source was already added with the different observer");
        }
        if (object != null) {
            return;
        }
        if (this.hasActiveObservers()) {
            source.plug();
        }
    }

    @CallSuper
    @Override
    protected void onActive() {
        Iterator iterator = this.mSources.iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().plug();
        }
    }

    @CallSuper
    @Override
    protected void onInactive() {
        Iterator iterator = this.mSources.iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().unplug();
        }
    }

    @MainThread
    public <S> void removeSource(@NonNull LiveData<S> object) {
        if ((object = this.mSources.remove(object)) != null) {
            object.unplug();
        }
    }

    private static class Source<V>
    implements Observer<V> {
        final LiveData<V> mLiveData;
        final Observer<V> mObserver;
        int mVersion = -1;

        Source(LiveData<V> liveData, Observer<V> observer) {
            this.mLiveData = liveData;
            this.mObserver = observer;
        }

        @Override
        public void onChanged(@Nullable V v) {
            if (this.mVersion != this.mLiveData.getVersion()) {
                this.mVersion = this.mLiveData.getVersion();
                this.mObserver.onChanged(v);
            }
        }

        void plug() {
            this.mLiveData.observeForever(this);
        }

        void unplug() {
            this.mLiveData.removeObserver(this);
        }
    }

}

