/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Parcelable
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.media;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.IMediaBrowserServiceCompat;
import android.support.v4.media.IMediaBrowserServiceCompatCallbacks;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class MediaBrowserServiceCompat
extends Service {
    private static final boolean DBG = false;
    public static final String KEY_MEDIA_ITEM = "media_item";
    public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserServiceCompat";
    private static final String TAG = "MediaBrowserServiceCompat";
    private ServiceBinder mBinder;
    private final ArrayMap<IBinder, ConnectionRecord> mConnections = new ArrayMap();
    private final Handler mHandler = new Handler();
    MediaSessionCompat.Token mSession;

    private void addSubscription(String string2, ConnectionRecord connectionRecord) {
        connectionRecord.subscriptions.add(string2);
        this.performLoadChildren(string2, connectionRecord);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isValidPackage(String string2, int n) {
        if (string2 == null) {
            return false;
        }
        String[] arrstring = this.getPackageManager().getPackagesForUid(n);
        int n2 = arrstring.length;
        n = 0;
        while (n < n2) {
            if (arrstring[n].equals(string2)) {
                return true;
            }
            ++n;
        }
        return false;
    }

    private void performLoadChildren(final String string2, final ConnectionRecord connectionRecord) {
        Result<List<MediaBrowserCompat.MediaItem>> result = new Result<List<MediaBrowserCompat.MediaItem>>((Object)string2){

            @Override
            void onResultSent(List<MediaBrowserCompat.MediaItem> list) {
                if (list == null) {
                    throw new IllegalStateException("onLoadChildren sent null list for id " + string2);
                }
                if (MediaBrowserServiceCompat.this.mConnections.get((Object)connectionRecord.callbacks.asBinder()) != connectionRecord) {
                    return;
                }
                try {
                    connectionRecord.callbacks.onLoadChildren(string2, list);
                    return;
                }
                catch (RemoteException remoteException) {
                    Log.w((String)"MediaBrowserServiceCompat", (String)("Calling onLoadChildren() failed for id=" + string2 + " package=" + connectionRecord.pkg));
                    return;
                }
            }
        };
        this.onLoadChildren(string2, result);
        if (!result.isDone()) {
            throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + connectionRecord.pkg + " id=" + string2);
        }
    }

    private void performLoadItem(String string2, ResultReceiver object) {
        object = new Result<MediaBrowserCompat.MediaItem>((Object)string2, (ResultReceiver)object){
            final /* synthetic */ ResultReceiver val$receiver;

            @Override
            void onResultSent(MediaBrowserCompat.MediaItem mediaItem) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("media_item", (Parcelable)mediaItem);
                this.val$receiver.send(0, bundle);
            }
        };
        this.onLoadItem(string2, (Result<MediaBrowserCompat.MediaItem>)object);
        if (!object.isDone()) {
            throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + string2);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
    }

    @Nullable
    public MediaSessionCompat.Token getSessionToken() {
        return this.mSession;
    }

    public void notifyChildrenChanged(final @NonNull String string2) {
        if (string2 == null) {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        this.mHandler.post(new Runnable(){

            @Override
            public void run() {
                Iterator iterator = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();
                while (iterator.hasNext()) {
                    Object object = (IBinder)iterator.next();
                    object = (ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(object);
                    if (!object.subscriptions.contains(string2)) continue;
                    MediaBrowserServiceCompat.this.performLoadChildren(string2, (ConnectionRecord)object);
                }
            }
        });
    }

    public IBinder onBind(Intent intent) {
        if ("android.media.browse.MediaBrowserServiceCompat".equals(intent.getAction())) {
            return this.mBinder;
        }
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mBinder = new ServiceBinder();
    }

    @Nullable
    public abstract BrowserRoot onGetRoot(@NonNull String var1, int var2, @Nullable Bundle var3);

    public abstract void onLoadChildren(@NonNull String var1, @NonNull Result<List<MediaBrowserCompat.MediaItem>> var2);

    public void onLoadItem(String string2, Result<MediaBrowserCompat.MediaItem> result) {
        result.sendResult(null);
    }

    public void setSessionToken(final MediaSessionCompat.Token token) {
        if (token == null) {
            throw new IllegalArgumentException("Session token may not be null.");
        }
        if (this.mSession != null) {
            throw new IllegalStateException("The session token has already been set.");
        }
        this.mSession = token;
        this.mHandler.post(new Runnable(){

            @Override
            public void run() {
                for (IBinder iBinder : MediaBrowserServiceCompat.this.mConnections.keySet()) {
                    ConnectionRecord connectionRecord = (ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get((Object)iBinder);
                    try {
                        connectionRecord.callbacks.onConnect(connectionRecord.root.getRootId(), token, connectionRecord.root.getExtras());
                    }
                    catch (RemoteException remoteException) {
                        Log.w((String)"MediaBrowserServiceCompat", (String)("Connection for " + connectionRecord.pkg + " is no longer valid."));
                        MediaBrowserServiceCompat.this.mConnections.remove((Object)iBinder);
                    }
                }
            }
        });
    }

    public static final class BrowserRoot {
        private final Bundle mExtras;
        private final String mRootId;

        public BrowserRoot(@NonNull String string2, @Nullable Bundle bundle) {
            if (string2 == null) {
                throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
            }
            this.mRootId = string2;
            this.mExtras = bundle;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public String getRootId() {
            return this.mRootId;
        }
    }

    private class ConnectionRecord {
        IMediaBrowserServiceCompatCallbacks callbacks;
        String pkg;
        BrowserRoot root;
        Bundle rootHints;
        HashSet<String> subscriptions;

        private ConnectionRecord() {
            this.subscriptions = new HashSet();
        }
    }

    public class Result<T> {
        private Object mDebug;
        private boolean mDetachCalled;
        private boolean mSendResultCalled;

        Result(Object object) {
            this.mDebug = object;
        }

        public void detach() {
            if (this.mDetachCalled) {
                throw new IllegalStateException("detach() called when detach() had already been called for: " + this.mDebug);
            }
            if (this.mSendResultCalled) {
                throw new IllegalStateException("detach() called when sendResult() had already been called for: " + this.mDebug);
            }
            this.mDetachCalled = true;
        }

        boolean isDone() {
            if (this.mDetachCalled || this.mSendResultCalled) {
                return true;
            }
            return false;
        }

        void onResultSent(T t) {
        }

        public void sendResult(T t) {
            if (this.mSendResultCalled) {
                throw new IllegalStateException("sendResult() called twice for: " + this.mDebug);
            }
            this.mSendResultCalled = true;
            this.onResultSent(t);
        }
    }

    private class ServiceBinder
    extends IMediaBrowserServiceCompat.Stub {
        private ServiceBinder() {
        }

        @Override
        public void addSubscription(final String string2, final IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    Object object = iMediaBrowserServiceCompatCallbacks.asBinder();
                    object = (ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(object);
                    if (object == null) {
                        Log.w((String)"MediaBrowserServiceCompat", (String)("addSubscription for callback that isn't registered id=" + string2));
                        return;
                    }
                    MediaBrowserServiceCompat.this.addSubscription(string2, (ConnectionRecord)object);
                }
            });
        }

        @Override
        public void connect(final String string2, final Bundle bundle, final IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) {
            final int n = Binder.getCallingUid();
            if (!MediaBrowserServiceCompat.this.isValidPackage(string2, n)) {
                throw new IllegalArgumentException("Package/uid mismatch: uid=" + n + " package=" + string2);
            }
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable(){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public void run() {
                    IBinder iBinder = iMediaBrowserServiceCompatCallbacks.asBinder();
                    MediaBrowserServiceCompat.this.mConnections.remove((Object)iBinder);
                    ConnectionRecord connectionRecord = new ConnectionRecord();
                    connectionRecord.pkg = string2;
                    connectionRecord.rootHints = bundle;
                    connectionRecord.callbacks = iMediaBrowserServiceCompatCallbacks;
                    connectionRecord.root = MediaBrowserServiceCompat.this.onGetRoot(string2, n, bundle);
                    if (connectionRecord.root == null) {
                        Log.i((String)"MediaBrowserServiceCompat", (String)("No root for client " + string2 + " from service " + this.getClass().getName()));
                        try {
                            iMediaBrowserServiceCompatCallbacks.onConnectFailed();
                            return;
                        }
                        catch (RemoteException remoteException) {
                            Log.w((String)"MediaBrowserServiceCompat", (String)("Calling onConnectFailed() failed. Ignoring. pkg=" + string2));
                            return;
                        }
                    }
                    try {
                        MediaBrowserServiceCompat.this.mConnections.put(iBinder, connectionRecord);
                        if (MediaBrowserServiceCompat.this.mSession == null) return;
                        iMediaBrowserServiceCompatCallbacks.onConnect(connectionRecord.root.getRootId(), MediaBrowserServiceCompat.this.mSession, connectionRecord.root.getExtras());
                        return;
                    }
                    catch (RemoteException remoteException) {
                        Log.w((String)"MediaBrowserServiceCompat", (String)("Calling onConnect() failed. Dropping client. pkg=" + string2));
                        MediaBrowserServiceCompat.this.mConnections.remove((Object)iBinder);
                        return;
                    }
                }
            });
        }

        @Override
        public void disconnect(final IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    IBinder iBinder = iMediaBrowserServiceCompatCallbacks.asBinder();
                    if ((ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove((Object)iBinder) != null) {
                        // empty if block
                    }
                }
            });
        }

        @Override
        public void getMediaItem(final String string2, final ResultReceiver resultReceiver) {
            if (TextUtils.isEmpty((CharSequence)string2) || resultReceiver == null) {
                return;
            }
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    MediaBrowserServiceCompat.this.performLoadItem(string2, resultReceiver);
                }
            });
        }

        @Override
        public void removeSubscription(final String string2, final IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.post(new Runnable(){

                /*
                 * Enabled aggressive block sorting
                 */
                @Override
                public void run() {
                    Object object = iMediaBrowserServiceCompatCallbacks.asBinder();
                    object = (ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(object);
                    if (object == null) {
                        Log.w((String)"MediaBrowserServiceCompat", (String)("removeSubscription for callback that isn't registered id=" + string2));
                        return;
                    } else {
                        if (object.subscriptions.remove(string2)) return;
                        {
                            Log.w((String)"MediaBrowserServiceCompat", (String)("removeSubscription called for " + string2 + " which is not subscribed"));
                            return;
                        }
                    }
                }
            });
        }

    }

}

