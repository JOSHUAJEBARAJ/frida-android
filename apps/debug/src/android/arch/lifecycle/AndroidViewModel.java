/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.Application
 */
package android.arch.lifecycle;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

public class AndroidViewModel
extends ViewModel {
    @SuppressLint(value={"StaticFieldLeak"})
    private Application mApplication;

    public AndroidViewModel(@NonNull Application application) {
        this.mApplication = application;
    }

    @NonNull
    public <T extends Application> T getApplication() {
        return (T)this.mApplication;
    }
}
