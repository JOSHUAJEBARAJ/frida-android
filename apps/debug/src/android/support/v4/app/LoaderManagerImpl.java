/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Looper
 *  android.util.Log
 */
package android.support.v4.app;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl
extends LoaderManager {
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    @NonNull
    private final LifecycleOwner mLifecycleOwner;
    @NonNull
    private final LoaderViewModel mLoaderViewModel;

    static {
        DEBUG = false;
    }

    LoaderManagerImpl(@NonNull LifecycleOwner lifecycleOwner, @NonNull ViewModelStore viewModelStore) {
        this.mLifecycleOwner = lifecycleOwner;
        this.mLoaderViewModel = LoaderViewModel.getInstance(viewModelStore);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @MainThread
    @NonNull
    private <D> Loader<D> createAndInstallLoader(int var1_1, @Nullable Bundle var2_2, @NonNull LoaderManager.LoaderCallbacks<D> var3_6, @Nullable Loader<D> var4_7) {
        block7 : {
            this.mLoaderViewModel.startCreatingLoader();
            var5_8 = var3_6.onCreateLoader(var1_1, (Bundle)var2_2);
            if (var5_8 == null) break block7;
            if (var5_8.getClass().isMemberClass() && !Modifier.isStatic(var5_8.getClass().getModifiers())) {
                var2_2 = new StringBuilder();
                var2_2.append("Object returned from onCreateLoader must not be a non-static inner member class: ");
                var2_2.append(var5_8);
                throw new IllegalArgumentException(var2_2.toString());
            }
            var2_2 = new LoaderInfo<D>(var1_1, (Bundle)var2_2, var5_8, (Loader<D>)var4_7);
            try {
                if (LoaderManagerImpl.DEBUG) {
                    var4_7 = new StringBuilder();
                    var4_7.append("  Created new loader ");
                    var4_7.append(var2_2);
                    Log.v((String)"LoaderManager", (String)var4_7.toString());
                }
                this.mLoaderViewModel.putLoader(var1_1, (LoaderInfo)var2_2);
                this.mLoaderViewModel.finishCreatingLoader();
                return var2_2.setCallback(this.mLifecycleOwner, var3_6);
            }
            catch (Throwable var2_3) {}
            ** GOTO lbl27
        }
        try {
            throw new IllegalArgumentException("Object returned from onCreateLoader must not be null");
        }
        catch (Throwable var2_4) {
            // empty catch block
        }
lbl27: // 2 sources:
        this.mLoaderViewModel.finishCreatingLoader();
        throw var2_5;
    }

    @MainThread
    @Override
    public void destroyLoader(int n) {
        if (!this.mLoaderViewModel.isCreatingLoader()) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                Object object;
                if (DEBUG) {
                    object = new StringBuilder();
                    object.append("destroyLoader in ");
                    object.append(this);
                    object.append(" of ");
                    object.append(n);
                    Log.v((String)"LoaderManager", (String)object.toString());
                }
                if ((object = this.mLoaderViewModel.getLoader(n)) != null) {
                    object.destroy(true);
                    this.mLoaderViewModel.removeLoader(n);
                }
                return;
            }
            throw new IllegalStateException("destroyLoader must be called on the main thread");
        }
        throw new IllegalStateException("Called while creating a loader");
    }

    @Deprecated
    @Override
    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
        this.mLoaderViewModel.dump(string2, fileDescriptor, printWriter, arrstring);
    }

    @Nullable
    @Override
    public <D> Loader<D> getLoader(int n) {
        if (!this.mLoaderViewModel.isCreatingLoader()) {
            LoaderInfo loaderInfo = this.mLoaderViewModel.getLoader(n);
            if (loaderInfo != null) {
                return loaderInfo.getLoader();
            }
            return null;
        }
        throw new IllegalStateException("Called while creating a loader");
    }

    @Override
    public boolean hasRunningLoaders() {
        return this.mLoaderViewModel.hasRunningLoaders();
    }

    @MainThread
    @NonNull
    @Override
    public <D> Loader<D> initLoader(int n, @Nullable Bundle object, @NonNull LoaderManager.LoaderCallbacks<D> loaderCallbacks) {
        if (!this.mLoaderViewModel.isCreatingLoader()) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                LoaderInfo<D> loaderInfo = this.mLoaderViewModel.getLoader(n);
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("initLoader in ");
                    stringBuilder.append(this);
                    stringBuilder.append(": args=");
                    stringBuilder.append(object);
                    Log.v((String)"LoaderManager", (String)stringBuilder.toString());
                }
                if (loaderInfo == null) {
                    return this.createAndInstallLoader(n, (Bundle)object, loaderCallbacks, null);
                }
                if (DEBUG) {
                    object = new StringBuilder();
                    object.append("  Re-using existing loader ");
                    object.append(loaderInfo);
                    Log.v((String)"LoaderManager", (String)object.toString());
                }
                return loaderInfo.setCallback(this.mLifecycleOwner, loaderCallbacks);
            }
            throw new IllegalStateException("initLoader must be called on the main thread");
        }
        throw new IllegalStateException("Called while creating a loader");
    }

    @Override
    public void markForRedelivery() {
        this.mLoaderViewModel.markForRedelivery();
    }

    @MainThread
    @NonNull
    @Override
    public <D> Loader<D> restartLoader(int n, @Nullable Bundle bundle, @NonNull LoaderManager.LoaderCallbacks<D> loaderCallbacks) {
        if (!this.mLoaderViewModel.isCreatingLoader()) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                Object object;
                if (DEBUG) {
                    object = new StringBuilder();
                    object.append("restartLoader in ");
                    object.append(this);
                    object.append(": args=");
                    object.append((Object)bundle);
                    Log.v((String)"LoaderManager", (String)object.toString());
                }
                LoaderInfo loaderInfo = this.mLoaderViewModel.getLoader(n);
                object = null;
                if (loaderInfo != null) {
                    object = loaderInfo.destroy(false);
                }
                return this.createAndInstallLoader(n, bundle, loaderCallbacks, (Loader<D>)object);
            }
            throw new IllegalStateException("restartLoader must be called on the main thread");
        }
        throw new IllegalStateException("Called while creating a loader");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("LoaderManager{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" in ");
        DebugUtils.buildShortClassTag(this.mLifecycleOwner, stringBuilder);
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    public static class LoaderInfo<D>
    extends MutableLiveData<D>
    implements Loader.OnLoadCompleteListener<D> {
        @Nullable
        private final Bundle mArgs;
        private final int mId;
        private LifecycleOwner mLifecycleOwner;
        @NonNull
        private final Loader<D> mLoader;
        private LoaderObserver<D> mObserver;
        private Loader<D> mPriorLoader;

        LoaderInfo(int n, @Nullable Bundle bundle, @NonNull Loader<D> loader, @Nullable Loader<D> loader2) {
            this.mId = n;
            this.mArgs = bundle;
            this.mLoader = loader;
            this.mPriorLoader = loader2;
            this.mLoader.registerListener(n, this);
        }

        @MainThread
        Loader<D> destroy(boolean bl) {
            Object object;
            if (LoaderManagerImpl.DEBUG) {
                object = new StringBuilder();
                object.append("  Destroying: ");
                object.append(this);
                Log.v((String)"LoaderManager", (String)object.toString());
            }
            this.mLoader.cancelLoad();
            this.mLoader.abandon();
            object = this.mObserver;
            if (object != null) {
                this.removeObserver((Observer<? super D>)object);
                if (bl) {
                    object.reset();
                }
            }
            this.mLoader.unregisterListener(this);
            if (object != null && !object.hasDeliveredData() || bl) {
                this.mLoader.reset();
                return this.mPriorLoader;
            }
            return this.mLoader;
        }

        public void dump(String string2, FileDescriptor object, PrintWriter printWriter, String[] object2) {
            printWriter.print(string2);
            printWriter.print("mId=");
            printWriter.print(this.mId);
            printWriter.print(" mArgs=");
            printWriter.println((Object)this.mArgs);
            printWriter.print(string2);
            printWriter.print("mLoader=");
            printWriter.println(this.mLoader);
            Loader<D> loader = this.mLoader;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("  ");
            loader.dump(stringBuilder.toString(), (FileDescriptor)object, printWriter, (String[])object2);
            if (this.mObserver != null) {
                printWriter.print(string2);
                printWriter.print("mCallbacks=");
                printWriter.println(this.mObserver);
                object = this.mObserver;
                object2 = new StringBuilder();
                object2.append(string2);
                object2.append("  ");
                object.dump(object2.toString(), printWriter);
            }
            printWriter.print(string2);
            printWriter.print("mData=");
            printWriter.println(this.getLoader().dataToString(this.getValue()));
            printWriter.print(string2);
            printWriter.print("mStarted=");
            printWriter.println(this.hasActiveObservers());
        }

        @NonNull
        Loader<D> getLoader() {
            return this.mLoader;
        }

        boolean isCallbackWaitingForData() {
            boolean bl = this.hasActiveObservers();
            boolean bl2 = false;
            if (!bl) {
                return false;
            }
            LoaderObserver<D> loaderObserver = this.mObserver;
            bl = bl2;
            if (loaderObserver != null) {
                bl = bl2;
                if (!loaderObserver.hasDeliveredData()) {
                    bl = true;
                }
            }
            return bl;
        }

        void markForRedelivery() {
            LifecycleOwner lifecycleOwner = this.mLifecycleOwner;
            LoaderObserver<D> loaderObserver = this.mObserver;
            if (lifecycleOwner != null && loaderObserver != null) {
                super.removeObserver(loaderObserver);
                this.observe(lifecycleOwner, loaderObserver);
            }
        }

        @Override
        protected void onActive() {
            if (LoaderManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("  Starting: ");
                stringBuilder.append(this);
                Log.v((String)"LoaderManager", (String)stringBuilder.toString());
            }
            this.mLoader.startLoading();
        }

        @Override
        protected void onInactive() {
            if (LoaderManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("  Stopping: ");
                stringBuilder.append(this);
                Log.v((String)"LoaderManager", (String)stringBuilder.toString());
            }
            this.mLoader.stopLoading();
        }

        @Override
        public void onLoadComplete(@NonNull Loader<D> object, @Nullable D d) {
            if (LoaderManagerImpl.DEBUG) {
                object = new StringBuilder();
                object.append("onLoadComplete: ");
                object.append(this);
                Log.v((String)"LoaderManager", (String)object.toString());
            }
            if (Looper.myLooper() == Looper.getMainLooper()) {
                this.setValue(d);
                return;
            }
            if (LoaderManagerImpl.DEBUG) {
                Log.w((String)"LoaderManager", (String)"onLoadComplete was incorrectly called on a background thread");
            }
            this.postValue(d);
        }

        @Override
        public void removeObserver(@NonNull Observer<? super D> observer) {
            super.removeObserver(observer);
            this.mLifecycleOwner = null;
            this.mObserver = null;
        }

        @MainThread
        @NonNull
        Loader<D> setCallback(@NonNull LifecycleOwner lifecycleOwner, @NonNull LoaderManager.LoaderCallbacks<D> object) {
            object = new LoaderObserver<D>(this.mLoader, (LoaderManager.LoaderCallbacks<D>)object);
            this.observe(lifecycleOwner, object);
            LoaderObserver<D> loaderObserver = this.mObserver;
            if (loaderObserver != null) {
                this.removeObserver(loaderObserver);
            }
            this.mLifecycleOwner = lifecycleOwner;
            this.mObserver = object;
            return this.mLoader;
        }

        @Override
        public void setValue(D object) {
            super.setValue(object);
            object = this.mPriorLoader;
            if (object != null) {
                object.reset();
                this.mPriorLoader = null;
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(64);
            stringBuilder.append("LoaderInfo{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" #");
            stringBuilder.append(this.mId);
            stringBuilder.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, stringBuilder);
            stringBuilder.append("}}");
            return stringBuilder.toString();
        }
    }

    static class LoaderObserver<D>
    implements Observer<D> {
        @NonNull
        private final LoaderManager.LoaderCallbacks<D> mCallback;
        private boolean mDeliveredData = false;
        @NonNull
        private final Loader<D> mLoader;

        LoaderObserver(@NonNull Loader<D> loader, @NonNull LoaderManager.LoaderCallbacks<D> loaderCallbacks) {
            this.mLoader = loader;
            this.mCallback = loaderCallbacks;
        }

        public void dump(String string2, PrintWriter printWriter) {
            printWriter.print(string2);
            printWriter.print("mDeliveredData=");
            printWriter.println(this.mDeliveredData);
        }

        boolean hasDeliveredData() {
            return this.mDeliveredData;
        }

        @Override
        public void onChanged(@Nullable D d) {
            if (LoaderManagerImpl.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("  onLoadFinished in ");
                stringBuilder.append(this.mLoader);
                stringBuilder.append(": ");
                stringBuilder.append(this.mLoader.dataToString(d));
                Log.v((String)"LoaderManager", (String)stringBuilder.toString());
            }
            this.mCallback.onLoadFinished(this.mLoader, d);
            this.mDeliveredData = true;
        }

        @MainThread
        void reset() {
            if (this.mDeliveredData) {
                if (LoaderManagerImpl.DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("  Resetting: ");
                    stringBuilder.append(this.mLoader);
                    Log.v((String)"LoaderManager", (String)stringBuilder.toString());
                }
                this.mCallback.onLoaderReset(this.mLoader);
            }
        }

        public String toString() {
            return this.mCallback.toString();
        }
    }

    static class LoaderViewModel
    extends ViewModel {
        private static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory(){

            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> class_) {
                return (T)new LoaderViewModel();
            }
        };
        private boolean mCreatingLoader = false;
        private SparseArrayCompat<LoaderInfo> mLoaders = new SparseArrayCompat();

        LoaderViewModel() {
        }

        @NonNull
        static LoaderViewModel getInstance(ViewModelStore viewModelStore) {
            return new ViewModelProvider(viewModelStore, FACTORY).get(LoaderViewModel.class);
        }

        public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
            if (this.mLoaders.size() > 0) {
                printWriter.print(string2);
                printWriter.println("Loaders:");
                CharSequence charSequence = new StringBuilder();
                charSequence.append(string2);
                charSequence.append("    ");
                charSequence = charSequence.toString();
                for (int i = 0; i < this.mLoaders.size(); ++i) {
                    LoaderInfo loaderInfo = this.mLoaders.valueAt(i);
                    printWriter.print(string2);
                    printWriter.print("  #");
                    printWriter.print(this.mLoaders.keyAt(i));
                    printWriter.print(": ");
                    printWriter.println(loaderInfo.toString());
                    loaderInfo.dump((String)charSequence, fileDescriptor, printWriter, arrstring);
                }
            }
        }

        void finishCreatingLoader() {
            this.mCreatingLoader = false;
        }

        <D> LoaderInfo<D> getLoader(int n) {
            return this.mLoaders.get(n);
        }

        boolean hasRunningLoaders() {
            int n = this.mLoaders.size();
            for (int i = 0; i < n; ++i) {
                if (!this.mLoaders.valueAt(i).isCallbackWaitingForData()) continue;
                return true;
            }
            return false;
        }

        boolean isCreatingLoader() {
            return this.mCreatingLoader;
        }

        void markForRedelivery() {
            int n = this.mLoaders.size();
            for (int i = 0; i < n; ++i) {
                this.mLoaders.valueAt(i).markForRedelivery();
            }
        }

        @Override
        protected void onCleared() {
            super.onCleared();
            int n = this.mLoaders.size();
            for (int i = 0; i < n; ++i) {
                this.mLoaders.valueAt(i).destroy(true);
            }
            this.mLoaders.clear();
        }

        void putLoader(int n, @NonNull LoaderInfo loaderInfo) {
            this.mLoaders.put(n, loaderInfo);
        }

        void removeLoader(int n) {
            this.mLoaders.remove(n);
        }

        void startCreatingLoader() {
            this.mCreatingLoader = true;
        }

    }

}

