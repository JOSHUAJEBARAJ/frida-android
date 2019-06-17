/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Transformations {
    private Transformations() {
    }

    @MainThread
    public static <X, Y> LiveData<Y> map(@NonNull LiveData<X> liveData, final @NonNull Function<X, Y> function) {
        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<X>(){

            @Override
            public void onChanged(@Nullable X x) {
                mediatorLiveData.setValue(function.apply(x));
            }
        });
        return mediatorLiveData;
    }

    @MainThread
    public static <X, Y> LiveData<Y> switchMap(@NonNull LiveData<X> liveData, final @NonNull Function<X, LiveData<Y>> function) {
        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<X>(){
            LiveData<Y> mSource;

            @Override
            public void onChanged(@Nullable X liveData) {
                LiveData<Y> liveData2 = this.mSource;
                if (liveData2 == (liveData = (LiveData)function.apply(liveData))) {
                    return;
                }
                if (liveData2 != null) {
                    mediatorLiveData.removeSource(liveData2);
                }
                this.mSource = liveData;
                liveData = this.mSource;
                if (liveData != null) {
                    mediatorLiveData.addSource(liveData, new Observer<Y>(){

                        @Override
                        public void onChanged(@Nullable Y y) {
                            mediatorLiveData.setValue(y);
                        }
                    });
                }
            }

        });
        return mediatorLiveData;
    }

}

