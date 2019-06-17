/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public abstract class Lifecycle {
    @MainThread
    public abstract void addObserver(@NonNull LifecycleObserver var1);

    @MainThread
    @NonNull
    public abstract State getCurrentState();

    @MainThread
    public abstract void removeObserver(@NonNull LifecycleObserver var1);

    public static enum Event {
        ON_CREATE,
        ON_START,
        ON_RESUME,
        ON_PAUSE,
        ON_STOP,
        ON_DESTROY,
        ON_ANY;
        

        private Event() {
        }
    }

    public static enum State {
        DESTROYED,
        INITIALIZED,
        CREATED,
        STARTED,
        RESUMED;
        

        private State() {
        }

        public boolean isAtLeast(@NonNull State state) {
            if (this.compareTo(state) >= 0) {
                return true;
            }
            return false;
        }
    }

}

