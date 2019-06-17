/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.util.Log
 */
package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManagerImpl;
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
    boolean mCreatingLoader;
    private FragmentHostCallback mHost;
    final SparseArrayCompat<LoaderInfo> mInactiveLoaders = new SparseArrayCompat();
    final SparseArrayCompat<LoaderInfo> mLoaders = new SparseArrayCompat();
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;
    final String mWho;

    static {
        DEBUG = false;
    }

    LoaderManagerImpl(String string2, FragmentHostCallback fragmentHostCallback, boolean bl) {
        this.mWho = string2;
        this.mHost = fragmentHostCallback;
        this.mStarted = bl;
    }

    private LoaderInfo createAndInstallLoader(int n, Bundle object, LoaderManager.LoaderCallbacks<Object> loaderCallbacks) {
        try {
            this.mCreatingLoader = true;
            object = this.createLoader(n, (Bundle)object, loaderCallbacks);
            this.installLoader((LoaderInfo)object);
            return object;
        }
        finally {
            this.mCreatingLoader = false;
        }
    }

    private LoaderInfo createLoader(int n, Bundle bundle, LoaderManager.LoaderCallbacks<Object> loaderCallbacks) {
        LoaderInfo loaderInfo = new LoaderInfo(n, bundle, loaderCallbacks);
        loaderInfo.mLoader = loaderCallbacks.onCreateLoader(n, bundle);
        return loaderInfo;
    }

    @Override
    public void destroyLoader(int n) {
        int n2;
        LoaderInfo loaderInfo;
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        if (DEBUG) {
            Log.v((String)"LoaderManager", (String)("destroyLoader in " + this + " of " + n));
        }
        if ((n2 = this.mLoaders.indexOfKey(n)) >= 0) {
            loaderInfo = this.mLoaders.valueAt(n2);
            this.mLoaders.removeAt(n2);
            loaderInfo.destroy();
        }
        if ((n = this.mInactiveLoaders.indexOfKey(n)) >= 0) {
            loaderInfo = this.mInactiveLoaders.valueAt(n);
            this.mInactiveLoaders.removeAt(n);
            loaderInfo.destroy();
        }
        if (this.mHost != null && !this.hasRunningLoaders()) {
            this.mHost.mFragmentManager.startPendingDeferredFragments();
        }
    }

    void doDestroy() {
        int n;
        if (!this.mRetaining) {
            if (DEBUG) {
                Log.v((String)"LoaderManager", (String)("Destroying Active in " + this));
            }
            for (n = this.mLoaders.size() - 1; n >= 0; --n) {
                this.mLoaders.valueAt(n).destroy();
            }
            this.mLoaders.clear();
        }
        if (DEBUG) {
            Log.v((String)"LoaderManager", (String)("Destroying Inactive in " + this));
        }
        for (n = this.mInactiveLoaders.size() - 1; n >= 0; --n) {
            this.mInactiveLoaders.valueAt(n).destroy();
        }
        this.mInactiveLoaders.clear();
    }

    void doReportNextStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt((int)i).mReportNextStart = true;
        }
    }

    void doReportStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).reportStart();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    void doRetain() {
        if (DEBUG) {
            Log.v((String)"LoaderManager", (String)("Retaining in " + this));
        }
        if (!this.mStarted) {
            RuntimeException runtimeException = new RuntimeException("here");
            runtimeException.fillInStackTrace();
            Log.w((String)"LoaderManager", (String)("Called doRetain when not started: " + this), (Throwable)runtimeException);
            return;
        } else {
            this.mRetaining = true;
            this.mStarted = false;
            for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
                this.mLoaders.valueAt(i).retain();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    void doStart() {
        if (DEBUG) {
            Log.v((String)"LoaderManager", (String)("Starting in " + this));
        }
        if (this.mStarted) {
            RuntimeException runtimeException = new RuntimeException("here");
            runtimeException.fillInStackTrace();
            Log.w((String)"LoaderManager", (String)("Called doStart when already started: " + this), (Throwable)runtimeException);
            return;
        } else {
            this.mStarted = true;
            for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
                this.mLoaders.valueAt(i).start();
            }
        }
    }

    void doStop() {
        if (DEBUG) {
            Log.v((String)"LoaderManager", (String)("Stopping in " + this));
        }
        if (!this.mStarted) {
            RuntimeException runtimeException = new RuntimeException("here");
            runtimeException.fillInStackTrace();
            Log.w((String)"LoaderManager", (String)("Called doStop when not started: " + this), (Throwable)runtimeException);
            return;
        }
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).stop();
        }
        this.mStarted = false;
    }

    @Override
    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
        String string3;
        LoaderInfo loaderInfo;
        int n;
        if (this.mLoaders.size() > 0) {
            printWriter.print(string2);
            printWriter.println("Active Loaders:");
            string3 = string2 + "    ";
            for (n = 0; n < this.mLoaders.size(); ++n) {
                loaderInfo = this.mLoaders.valueAt(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(this.mLoaders.keyAt(n));
                printWriter.print(": ");
                printWriter.println(loaderInfo.toString());
                loaderInfo.dump(string3, fileDescriptor, printWriter, arrstring);
            }
        }
        if (this.mInactiveLoaders.size() > 0) {
            printWriter.print(string2);
            printWriter.println("Inactive Loaders:");
            string3 = string2 + "    ";
            for (n = 0; n < this.mInactiveLoaders.size(); ++n) {
                loaderInfo = this.mInactiveLoaders.valueAt(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(this.mInactiveLoaders.keyAt(n));
                printWriter.print(": ");
                printWriter.println(loaderInfo.toString());
                loaderInfo.dump(string3, fileDescriptor, printWriter, arrstring);
            }
        }
    }

    void finishRetain() {
        if (this.mRetaining) {
            if (DEBUG) {
                Log.v((String)"LoaderManager", (String)("Finished Retaining in " + this));
            }
            this.mRetaining = false;
            for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
                this.mLoaders.valueAt(i).finishRetain();
            }
        }
    }

    @Override
    public <D> Loader<D> getLoader(int n) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (loaderInfo != null) {
            if (loaderInfo.mPendingLoader != null) {
                return loaderInfo.mPendingLoader.mLoader;
            }
            return loaderInfo.mLoader;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean hasRunningLoaders() {
        boolean bl = false;
        int n = this.mLoaders.size();
        int n2 = 0;
        while (n2 < n) {
            LoaderInfo loaderInfo = this.mLoaders.valueAt(n2);
            boolean bl2 = loaderInfo.mStarted && !loaderInfo.mDeliveredData;
            bl |= bl2;
            ++n2;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public <D> Loader<D> initLoader(int n, Bundle object, LoaderManager.LoaderCallbacks<D> object2) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (DEBUG) {
            Log.v((String)"LoaderManager", (String)("initLoader in " + this + ": args=" + object));
        }
        if (loaderInfo == null) {
            object = object2 = this.createAndInstallLoader(n, (Bundle)object, (LoaderManager.LoaderCallbacks<Object>)object2);
            if (DEBUG) {
                Log.v((String)"LoaderManager", (String)("  Created new loader " + object2));
                object = object2;
            }
        } else {
            if (DEBUG) {
                Log.v((String)"LoaderManager", (String)("  Re-using existing loader " + loaderInfo));
            }
            loaderInfo.mCallbacks = object2;
            object = loaderInfo;
        }
        if (object.mHaveData && this.mStarted) {
            object.callOnLoadFinished(object.mLoader, object.mData);
        }
        return object.mLoader;
    }

    void installLoader(LoaderInfo loaderInfo) {
        this.mLoaders.put(loaderInfo.mId, loaderInfo);
        if (this.mStarted) {
            loaderInfo.start();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public <D> Loader<D> restartLoader(int n, Bundle bundle, LoaderManager.LoaderCallbacks<D> loaderCallbacks) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (DEBUG) {
            Log.v((String)"LoaderManager", (String)("restartLoader in " + this + ": args=" + (Object)bundle));
        }
        if (loaderInfo == null) return this.createAndInstallLoader((int)n, (Bundle)bundle, loaderCallbacks).mLoader;
        LoaderInfo loaderInfo2 = this.mInactiveLoaders.get(n);
        if (loaderInfo2 != null) {
            if (loaderInfo.mHaveData) {
                if (DEBUG) {
                    Log.v((String)"LoaderManager", (String)("  Removing last inactive loader: " + loaderInfo));
                }
                loaderInfo2.mDeliveredData = false;
                loaderInfo2.destroy();
                loaderInfo.mLoader.abandon();
                this.mInactiveLoaders.put(n, loaderInfo);
                return this.createAndInstallLoader((int)n, (Bundle)bundle, loaderCallbacks).mLoader;
            }
            if (!loaderInfo.mStarted) {
                if (DEBUG) {
                    Log.v((String)"LoaderManager", (String)"  Current loader is stopped; replacing");
                }
                this.mLoaders.put(n, null);
                loaderInfo.destroy();
                return this.createAndInstallLoader((int)n, (Bundle)bundle, loaderCallbacks).mLoader;
            }
            if (DEBUG) {
                Log.v((String)"LoaderManager", (String)"  Current loader is running; attempting to cancel");
            }
            loaderInfo.cancel();
            if (loaderInfo.mPendingLoader != null) {
                if (DEBUG) {
                    Log.v((String)"LoaderManager", (String)("  Removing pending loader: " + loaderInfo.mPendingLoader));
                }
                loaderInfo.mPendingLoader.destroy();
                loaderInfo.mPendingLoader = null;
            }
            if (DEBUG) {
                Log.v((String)"LoaderManager", (String)"  Enqueuing as new pending loader");
            }
            loaderInfo.mPendingLoader = this.createLoader(n, bundle, loaderCallbacks);
            return loaderInfo.mPendingLoader.mLoader;
        }
        if (DEBUG) {
            Log.v((String)"LoaderManager", (String)("  Making last loader inactive: " + loaderInfo));
        }
        loaderInfo.mLoader.abandon();
        this.mInactiveLoaders.put(n, loaderInfo);
        return this.createAndInstallLoader((int)n, (Bundle)bundle, loaderCallbacks).mLoader;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("LoaderManager{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" in ");
        DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    void updateHostController(FragmentHostCallback fragmentHostCallback) {
        this.mHost = fragmentHostCallback;
    }

    final class LoaderInfo
    implements Loader.OnLoadCompleteListener<Object>,
    Loader.OnLoadCanceledListener<Object> {
        final Bundle mArgs;
        LoaderManager.LoaderCallbacks<Object> mCallbacks;
        Object mData;
        boolean mDeliveredData;
        boolean mDestroyed;
        boolean mHaveData;
        final int mId;
        boolean mListenerRegistered;
        Loader<Object> mLoader;
        LoaderInfo mPendingLoader;
        boolean mReportNextStart;
        boolean mRetaining;
        boolean mRetainingStarted;
        boolean mStarted;

        public LoaderInfo(int n, Bundle bundle, LoaderManager.LoaderCallbacks<Object> loaderCallbacks) {
            this.mId = n;
            this.mArgs = bundle;
            this.mCallbacks = loaderCallbacks;
        }

        void callOnLoadFinished(Loader<Object> loader, Object object) {
            String string2;
            if (this.mCallbacks != null) {
                string2 = null;
                if (LoaderManagerImpl.this.mHost != null) {
                    string2 = LoaderManagerImpl.access$000((LoaderManagerImpl)LoaderManagerImpl.this).mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.access$000((LoaderManagerImpl)LoaderManagerImpl.this).mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
                }
                if (LoaderManagerImpl.DEBUG) {
                    Log.v((String)"LoaderManager", (String)("  onLoadFinished in " + loader + ": " + loader.dataToString(object)));
                }
                this.mCallbacks.onLoadFinished(loader, object);
                this.mDeliveredData = true;
            }
            return;
            finally {
                if (LoaderManagerImpl.this.mHost != null) {
                    LoaderManagerImpl.access$000((LoaderManagerImpl)LoaderManagerImpl.this).mFragmentManager.mNoTransactionsBecause = string2;
                }
            }
        }

        void cancel() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v((String)"LoaderManager", (String)("  Canceling: " + this));
            }
            if (this.mStarted && this.mLoader != null && this.mListenerRegistered && !this.mLoader.cancelLoad()) {
                this.onLoadCanceled(this.mLoader);
            }
        }

        void destroy() {
            String string2;
            if (LoaderManagerImpl.DEBUG) {
                Log.v((String)"LoaderManager", (String)("  Destroying: " + this));
            }
            this.mDestroyed = true;
            boolean bl = this.mDeliveredData;
            this.mDeliveredData = false;
            if (this.mCallbacks != null && this.mLoader != null && this.mHaveData && bl) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v((String)"LoaderManager", (String)("  Reseting: " + this));
                }
                string2 = null;
                if (LoaderManagerImpl.this.mHost != null) {
                    string2 = LoaderManagerImpl.access$000((LoaderManagerImpl)LoaderManagerImpl.this).mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.access$000((LoaderManagerImpl)LoaderManagerImpl.this).mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
                }
                this.mCallbacks.onLoaderReset(this.mLoader);
            }
            this.mCallbacks = null;
            this.mData = null;
            this.mHaveData = false;
            if (this.mLoader != null) {
                if (this.mListenerRegistered) {
                    this.mListenerRegistered = false;
                    this.mLoader.unregisterListener(this);
                    this.mLoader.unregisterOnLoadCanceledListener(this);
                }
                this.mLoader.reset();
            }
            if (this.mPendingLoader != null) {
                this.mPendingLoader.destroy();
            }
            return;
            finally {
                if (LoaderManagerImpl.this.mHost != null) {
                    LoaderManagerImpl.access$000((LoaderManagerImpl)LoaderManagerImpl.this).mFragmentManager.mNoTransactionsBecause = string2;
                }
            }
        }

        public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
            printWriter.print(string2);
            printWriter.print("mId=");
            printWriter.print(this.mId);
            printWriter.print(" mArgs=");
            printWriter.println((Object)this.mArgs);
            printWriter.print(string2);
            printWriter.print("mCallbacks=");
            printWriter.println(this.mCallbacks);
            printWriter.print(string2);
            printWriter.print("mLoader=");
            printWriter.println(this.mLoader);
            if (this.mLoader != null) {
                this.mLoader.dump(string2 + "  ", fileDescriptor, printWriter, arrstring);
            }
            if (this.mHaveData || this.mDeliveredData) {
                printWriter.print(string2);
                printWriter.print("mHaveData=");
                printWriter.print(this.mHaveData);
                printWriter.print("  mDeliveredData=");
                printWriter.println(this.mDeliveredData);
                printWriter.print(string2);
                printWriter.print("mData=");
                printWriter.println(this.mData);
            }
            printWriter.print(string2);
            printWriter.print("mStarted=");
            printWriter.print(this.mStarted);
            printWriter.print(" mReportNextStart=");
            printWriter.print(this.mReportNextStart);
            printWriter.print(" mDestroyed=");
            printWriter.println(this.mDestroyed);
            printWriter.print(string2);
            printWriter.print("mRetaining=");
            printWriter.print(this.mRetaining);
            printWriter.print(" mRetainingStarted=");
            printWriter.print(this.mRetainingStarted);
            printWriter.print(" mListenerRegistered=");
            printWriter.println(this.mListenerRegistered);
            if (this.mPendingLoader != null) {
                printWriter.print(string2);
                printWriter.println("Pending Loader ");
                printWriter.print(this.mPendingLoader);
                printWriter.println(":");
                this.mPendingLoader.dump(string2 + "  ", fileDescriptor, printWriter, arrstring);
            }
        }

        void finishRetain() {
            if (this.mRetaining) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v((String)"LoaderManager", (String)("  Finished Retaining: " + this));
                }
                this.mRetaining = false;
                if (this.mStarted != this.mRetainingStarted && !this.mStarted) {
                    this.stop();
                }
            }
            if (this.mStarted && this.mHaveData && !this.mReportNextStart) {
                this.callOnLoadFinished(this.mLoader, this.mData);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onLoadCanceled(Loader<Object> object) {
            if (LoaderManagerImpl.DEBUG) {
                Log.v((String)"LoaderManager", (String)("onLoadCanceled: " + this));
            }
            if (this.mDestroyed) {
                if (!LoaderManagerImpl.DEBUG) return;
                {
                    Log.v((String)"LoaderManager", (String)"  Ignoring load canceled -- destroyed");
                    return;
                }
            } else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (!LoaderManagerImpl.DEBUG) return;
                {
                    Log.v((String)"LoaderManager", (String)"  Ignoring load canceled -- not active");
                    return;
                }
            } else {
                object = this.mPendingLoader;
                if (object == null) return;
                {
                    if (LoaderManagerImpl.DEBUG) {
                        Log.v((String)"LoaderManager", (String)("  Switching to pending loader: " + object));
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    this.destroy();
                    LoaderManagerImpl.this.installLoader((LoaderInfo)object);
                    return;
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onLoadComplete(Loader<Object> object, Object object2) {
            if (LoaderManagerImpl.DEBUG) {
                Log.v((String)"LoaderManager", (String)("onLoadComplete: " + this));
            }
            if (this.mDestroyed) {
                if (!LoaderManagerImpl.DEBUG) return;
                {
                    Log.v((String)"LoaderManager", (String)"  Ignoring load complete -- destroyed");
                    return;
                }
            } else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (!LoaderManagerImpl.DEBUG) return;
                {
                    Log.v((String)"LoaderManager", (String)"  Ignoring load complete -- not active");
                    return;
                }
            } else {
                LoaderInfo loaderInfo = this.mPendingLoader;
                if (loaderInfo != null) {
                    if (LoaderManagerImpl.DEBUG) {
                        Log.v((String)"LoaderManager", (String)("  Switching to pending loader: " + loaderInfo));
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    this.destroy();
                    LoaderManagerImpl.this.installLoader(loaderInfo);
                    return;
                }
                if (this.mData != object2 || !this.mHaveData) {
                    this.mData = object2;
                    this.mHaveData = true;
                    if (this.mStarted) {
                        this.callOnLoadFinished((Loader<Object>)object, object2);
                    }
                }
                if ((object = LoaderManagerImpl.this.mInactiveLoaders.get(this.mId)) != null && object != this) {
                    object.mDeliveredData = false;
                    object.destroy();
                    LoaderManagerImpl.this.mInactiveLoaders.remove(this.mId);
                }
                if (LoaderManagerImpl.this.mHost == null || LoaderManagerImpl.this.hasRunningLoaders()) return;
                {
                    LoaderManagerImpl.access$000((LoaderManagerImpl)LoaderManagerImpl.this).mFragmentManager.startPendingDeferredFragments();
                    return;
                }
            }
        }

        void reportStart() {
            if (this.mStarted && this.mReportNextStart) {
                this.mReportNextStart = false;
                if (this.mHaveData) {
                    this.callOnLoadFinished(this.mLoader, this.mData);
                }
            }
        }

        void retain() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v((String)"LoaderManager", (String)("  Retaining: " + this));
            }
            this.mRetaining = true;
            this.mRetainingStarted = this.mStarted;
            this.mStarted = false;
            this.mCallbacks = null;
        }

        /*
         * Enabled aggressive block sorting
         */
        void start() {
            if (this.mRetaining && this.mRetainingStarted) {
                this.mStarted = true;
                return;
            }
            if (this.mStarted) return;
            this.mStarted = true;
            if (LoaderManagerImpl.DEBUG) {
                Log.v((String)"LoaderManager", (String)("  Starting: " + this));
            }
            if (this.mLoader == null && this.mCallbacks != null) {
                this.mLoader = this.mCallbacks.onCreateLoader(this.mId, this.mArgs);
            }
            if (this.mLoader == null) return;
            {
                if (this.mLoader.getClass().isMemberClass() && !Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                    throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + this.mLoader);
                }
            }
            if (!this.mListenerRegistered) {
                this.mLoader.registerListener(this.mId, this);
                this.mLoader.registerOnLoadCanceledListener(this);
                this.mListenerRegistered = true;
            }
            this.mLoader.startLoading();
        }

        void stop() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v((String)"LoaderManager", (String)("  Stopping: " + this));
            }
            this.mStarted = false;
            if (!this.mRetaining && this.mLoader != null && this.mListenerRegistered) {
                this.mListenerRegistered = false;
                this.mLoader.unregisterListener(this);
                this.mLoader.unregisterOnLoadCanceledListener(this);
                this.mLoader.stopLoading();
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

}

