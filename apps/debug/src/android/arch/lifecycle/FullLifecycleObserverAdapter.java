/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.FullLifecycleObserver;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

class FullLifecycleObserverAdapter
implements GenericLifecycleObserver {
    private final FullLifecycleObserver mObserver;

    FullLifecycleObserverAdapter(FullLifecycleObserver fullLifecycleObserver) {
        this.mObserver = fullLifecycleObserver;
    }

    @Override
    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        switch (.$SwitchMap$android$arch$lifecycle$Lifecycle$Event[event.ordinal()]) {
            default: {
                return;
            }
            case 7: {
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
            }
            case 6: {
                this.mObserver.onDestroy(lifecycleOwner);
                return;
            }
            case 5: {
                this.mObserver.onStop(lifecycleOwner);
                return;
            }
            case 4: {
                this.mObserver.onPause(lifecycleOwner);
                return;
            }
            case 3: {
                this.mObserver.onResume(lifecycleOwner);
                return;
            }
            case 2: {
                this.mObserver.onStart(lifecycleOwner);
                return;
            }
            case 1: 
        }
        this.mObserver.onCreate(lifecycleOwner);
    }

}

