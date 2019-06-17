/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.IMediaBrowserServiceCompat;
import android.support.v4.media.IMediaBrowserServiceCompatCallbacks;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class MediaBrowserCompat {
    private final MediaBrowserImplBase mImpl;

    public MediaBrowserCompat(Context context, ComponentName componentName, ConnectionCallback connectionCallback, Bundle bundle) {
        this.mImpl = new MediaBrowserImplBase(context, componentName, connectionCallback, bundle);
    }

    public void connect() {
        this.mImpl.connect();
    }

    public void disconnect() {
        this.mImpl.disconnect();
    }

    @Nullable
    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }

    public void getItem(@NonNull String string2, @NonNull ItemCallback itemCallback) {
        this.mImpl.getItem(string2, itemCallback);
    }

    @NonNull
    public String getRoot() {
        return this.mImpl.getRoot();
    }

    @NonNull
    public ComponentName getServiceComponent() {
        return this.mImpl.getServiceComponent();
    }

    @NonNull
    public MediaSessionCompat.Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public boolean isConnected() {
        return this.mImpl.isConnected();
    }

    public void subscribe(@NonNull String string2, @NonNull SubscriptionCallback subscriptionCallback) {
        this.mImpl.subscribe(string2, subscriptionCallback);
    }

    public void unsubscribe(@NonNull String string2) {
        this.mImpl.unsubscribe(string2);
    }

    public static class ConnectionCallback {
        public void onConnected() {
        }

        public void onConnectionFailed() {
        }

        public void onConnectionSuspended() {
        }
    }

    public static abstract class ItemCallback {
        public void onError(@NonNull String string2) {
        }

        public void onItemLoaded(MediaItem mediaItem) {
        }
    }

    static class MediaBrowserImplBase {
        private static final int CONNECT_STATE_CONNECTED = 2;
        private static final int CONNECT_STATE_CONNECTING = 1;
        private static final int CONNECT_STATE_DISCONNECTED = 0;
        private static final int CONNECT_STATE_SUSPENDED = 3;
        private static final boolean DBG = false;
        private static final String TAG = "MediaBrowserCompat";
        private final ConnectionCallback mCallback;
        private final Context mContext;
        private Bundle mExtras;
        private final Handler mHandler = new Handler();
        private MediaSessionCompat.Token mMediaSessionToken;
        private final Bundle mRootHints;
        private String mRootId;
        private IMediaBrowserServiceCompat mServiceBinder;
        private IMediaBrowserServiceCompatCallbacks mServiceCallbacks;
        private final ComponentName mServiceComponent;
        private MediaServiceConnection mServiceConnection;
        private int mState = 0;
        private final ArrayMap<String, Subscription> mSubscriptions = new ArrayMap();

        public MediaBrowserImplBase(Context context, ComponentName componentName, ConnectionCallback connectionCallback, Bundle bundle) {
            if (context == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            if (componentName == null) {
                throw new IllegalArgumentException("service component must not be null");
            }
            if (connectionCallback == null) {
                throw new IllegalArgumentException("connection callback must not be null");
            }
            this.mContext = context;
            this.mServiceComponent = componentName;
            this.mCallback = connectionCallback;
            this.mRootHints = bundle;
        }

        private void forceCloseConnection() {
            if (this.mServiceConnection != null) {
                this.mContext.unbindService((ServiceConnection)this.mServiceConnection);
            }
            this.mState = 0;
            this.mServiceConnection = null;
            this.mServiceBinder = null;
            this.mServiceCallbacks = null;
            this.mRootId = null;
            this.mMediaSessionToken = null;
        }

        private ServiceCallbacks getNewServiceCallbacks() {
            return new ServiceCallbacks(this);
        }

        private static String getStateLabel(int n) {
            switch (n) {
                default: {
                    return "UNKNOWN/" + n;
                }
                case 0: {
                    return "CONNECT_STATE_DISCONNECTED";
                }
                case 1: {
                    return "CONNECT_STATE_CONNECTING";
                }
                case 2: {
                    return "CONNECT_STATE_CONNECTED";
                }
                case 3: 
            }
            return "CONNECT_STATE_SUSPENDED";
        }

        private boolean isCurrent(IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks, String string2) {
            if (this.mServiceCallbacks != iMediaBrowserServiceCompatCallbacks) {
                if (this.mState != 0) {
                    Log.i((String)"MediaBrowserCompat", (String)(string2 + " for " + (Object)this.mServiceComponent + " with mServiceConnection=" + this.mServiceCallbacks + " this=" + this));
                }
                return false;
            }
            return true;
        }

        private final void onConnectionFailed(final IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) {
            this.mHandler.post(new Runnable(){

                @Override
                public void run() {
                    Log.e((String)"MediaBrowserCompat", (String)("onConnectFailed for " + (Object)MediaBrowserImplBase.this.mServiceComponent));
                    if (!MediaBrowserImplBase.this.isCurrent(iMediaBrowserServiceCompatCallbacks, "onConnectFailed")) {
                        return;
                    }
                    if (MediaBrowserImplBase.this.mState != 1) {
                        Log.w((String)"MediaBrowserCompat", (String)("onConnect from service while mState=" + MediaBrowserImplBase.getStateLabel(MediaBrowserImplBase.this.mState) + "... ignoring"));
                        return;
                    }
                    MediaBrowserImplBase.this.forceCloseConnection();
                    MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                }
            });
        }

        private final void onLoadChildren(final IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks, final String string2, final List list) {
            this.mHandler.post(new Runnable(){

                /*
                 * Enabled aggressive block sorting
                 * Lifted jumps to return sites
                 */
                @Override
                public void run() {
                    if (!MediaBrowserImplBase.this.isCurrent(iMediaBrowserServiceCompatCallbacks, "onLoadChildren")) {
                        return;
                    }
                    Object object = list;
                    List<MediaItem> list2 = object;
                    if (object == null) {
                        list2 = Collections.emptyList();
                    }
                    if ((object = (Subscription)MediaBrowserImplBase.this.mSubscriptions.get(string2)) == null) return;
                    object.callback.onChildrenLoaded(string2, list2);
                }
            });
        }

        private final void onServiceConnected(final IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks, final String string2, final MediaSessionCompat.Token token, final Bundle bundle) {
            this.mHandler.post(new Runnable(){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public void run() {
                    if (MediaBrowserImplBase.this.isCurrent(iMediaBrowserServiceCompatCallbacks, "onConnect")) {
                        if (MediaBrowserImplBase.this.mState != 1) {
                            Log.w((String)"MediaBrowserCompat", (String)("onConnect from service while mState=" + MediaBrowserImplBase.getStateLabel(MediaBrowserImplBase.this.mState) + "... ignoring"));
                            return;
                        }
                        MediaBrowserImplBase.this.mRootId = string2;
                        MediaBrowserImplBase.this.mMediaSessionToken = token;
                        MediaBrowserImplBase.this.mExtras = bundle;
                        MediaBrowserImplBase.this.mState = 2;
                        MediaBrowserImplBase.this.mCallback.onConnected();
                        for (String string22 : MediaBrowserImplBase.this.mSubscriptions.keySet()) {
                            try {
                                MediaBrowserImplBase.this.mServiceBinder.addSubscription(string22, MediaBrowserImplBase.this.mServiceCallbacks);
                            }
                            catch (RemoteException remoteException) {
                                Log.d((String)"MediaBrowserCompat", (String)("addSubscription failed with RemoteException parentId=" + string22));
                            }
                        }
                    }
                }
            });
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void connect() {
            MediaServiceConnection mediaServiceConnection;
            if (this.mState != 0) {
                throw new IllegalStateException("connect() called while not disconnected (state=" + MediaBrowserImplBase.getStateLabel(this.mState) + ")");
            }
            if (this.mServiceBinder != null) {
                throw new RuntimeException("mServiceBinder should be null. Instead it is " + this.mServiceBinder);
            }
            if (this.mServiceCallbacks != null) {
                throw new RuntimeException("mServiceCallbacks should be null. Instead it is " + this.mServiceCallbacks);
            }
            this.mState = 1;
            Intent intent = new Intent("android.media.browse.MediaBrowserServiceCompat");
            intent.setComponent(this.mServiceComponent);
            this.mServiceConnection = mediaServiceConnection = new MediaServiceConnection();
            boolean bl = false;
            try {
                boolean bl2;
                bl = bl2 = this.mContext.bindService(intent, (ServiceConnection)this.mServiceConnection, 1);
            }
            catch (Exception exception) {
                Log.e((String)"MediaBrowserCompat", (String)("Failed binding to service " + (Object)this.mServiceComponent));
            }
            if (!bl) {
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        if (mediaServiceConnection == MediaBrowserImplBase.this.mServiceConnection) {
                            MediaBrowserImplBase.this.forceCloseConnection();
                            MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                        }
                    }
                });
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void disconnect() {
            if (this.mServiceCallbacks != null) {
                try {
                    this.mServiceBinder.disconnect(this.mServiceCallbacks);
                }
                catch (RemoteException remoteException) {
                    Log.w((String)"MediaBrowserCompat", (String)("RemoteException during connect for " + (Object)this.mServiceComponent));
                }
            }
            this.forceCloseConnection();
        }

        void dump() {
            Log.d((String)"MediaBrowserCompat", (String)"MediaBrowserCompat...");
            Log.d((String)"MediaBrowserCompat", (String)("  mServiceComponent=" + (Object)this.mServiceComponent));
            Log.d((String)"MediaBrowserCompat", (String)("  mCallback=" + this.mCallback));
            Log.d((String)"MediaBrowserCompat", (String)("  mRootHints=" + (Object)this.mRootHints));
            Log.d((String)"MediaBrowserCompat", (String)("  mState=" + MediaBrowserImplBase.getStateLabel(this.mState)));
            Log.d((String)"MediaBrowserCompat", (String)("  mServiceConnection=" + this.mServiceConnection));
            Log.d((String)"MediaBrowserCompat", (String)("  mServiceBinder=" + this.mServiceBinder));
            Log.d((String)"MediaBrowserCompat", (String)("  mServiceCallbacks=" + this.mServiceCallbacks));
            Log.d((String)"MediaBrowserCompat", (String)("  mRootId=" + this.mRootId));
            Log.d((String)"MediaBrowserCompat", (String)("  mMediaSessionToken=" + this.mMediaSessionToken));
        }

        @Nullable
        public Bundle getExtras() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getExtras() called while not connected (state=" + MediaBrowserImplBase.getStateLabel(this.mState) + ")");
            }
            return this.mExtras;
        }

        public void getItem(final @NonNull String string2, final @NonNull ItemCallback itemCallback) {
            if (TextUtils.isEmpty((CharSequence)string2)) {
                throw new IllegalArgumentException("mediaId is empty.");
            }
            if (itemCallback == null) {
                throw new IllegalArgumentException("cb is null.");
            }
            if (this.mState != 2) {
                Log.i((String)"MediaBrowserCompat", (String)"Not connected, unable to retrieve the MediaItem.");
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        itemCallback.onError(string2);
                    }
                });
                return;
            }
            ResultReceiver resultReceiver = new ResultReceiver(this.mHandler){

                @Override
                protected void onReceiveResult(int n, Bundle bundle) {
                    if (n != 0 || bundle == null || !bundle.containsKey("media_item")) {
                        itemCallback.onError(string2);
                        return;
                    }
                    if (!((bundle = bundle.getParcelable("media_item")) instanceof MediaItem)) {
                        itemCallback.onError(string2);
                        return;
                    }
                    itemCallback.onItemLoaded((MediaItem)bundle);
                }
            };
            try {
                this.mServiceBinder.getMediaItem(string2, resultReceiver);
                return;
            }
            catch (RemoteException remoteException) {
                Log.i((String)"MediaBrowserCompat", (String)"Remote error getting media item.");
                this.mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        itemCallback.onError(string2);
                    }
                });
                return;
            }
        }

        @NonNull
        public String getRoot() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getSessionToken() called while not connected(state=" + MediaBrowserImplBase.getStateLabel(this.mState) + ")");
            }
            return this.mRootId;
        }

        @NonNull
        public ComponentName getServiceComponent() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getServiceComponent() called while not connected (state=" + this.mState + ")");
            }
            return this.mServiceComponent;
        }

        @NonNull
        public MediaSessionCompat.Token getSessionToken() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.mState + ")");
            }
            return this.mMediaSessionToken;
        }

        public boolean isConnected() {
            if (this.mState == 2) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public void subscribe(@NonNull String string2, @NonNull SubscriptionCallback subscriptionCallback) {
            if (string2 == null) {
                throw new IllegalArgumentException("parentId is null");
            }
            if (subscriptionCallback == null) {
                throw new IllegalArgumentException("callback is null");
            }
            Subscription subscription = this.mSubscriptions.get(string2);
            boolean bl = subscription == null;
            if (bl) {
                subscription = new Subscription(string2);
                this.mSubscriptions.put(string2, subscription);
            }
            subscription.callback = subscriptionCallback;
            if (this.mState != 2) return;
            try {
                this.mServiceBinder.addSubscription(string2, this.mServiceCallbacks);
                return;
            }
            catch (RemoteException remoteException) {
                Log.d((String)"MediaBrowserCompat", (String)("addSubscription failed with RemoteException parentId=" + string2));
                return;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public void unsubscribe(@NonNull String string2) {
            if (TextUtils.isEmpty((CharSequence)string2)) {
                throw new IllegalArgumentException("parentId is empty.");
            }
            Subscription subscription = this.mSubscriptions.remove(string2);
            if (this.mState != 2 || subscription == null) return;
            try {
                this.mServiceBinder.removeSubscription(string2, this.mServiceCallbacks);
                return;
            }
            catch (RemoteException remoteException) {
                Log.d((String)"MediaBrowserCompat", (String)("removeSubscription failed with RemoteException parentId=" + string2));
                return;
            }
        }

        private class MediaServiceConnection
        implements ServiceConnection {
            private MediaServiceConnection() {
            }

            private boolean isCurrent(String string2) {
                if (MediaBrowserImplBase.this.mServiceConnection != this) {
                    if (MediaBrowserImplBase.this.mState != 0) {
                        Log.i((String)"MediaBrowserCompat", (String)(string2 + " for " + (Object)MediaBrowserImplBase.this.mServiceComponent + " with mServiceConnection=" + MediaBrowserImplBase.this.mServiceConnection + " this=" + this));
                    }
                    return false;
                }
                return true;
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (!this.isCurrent("onServiceConnected")) {
                    return;
                }
                MediaBrowserImplBase.this.mServiceBinder = IMediaBrowserServiceCompat.Stub.asInterface(iBinder);
                MediaBrowserImplBase.this.mServiceCallbacks = MediaBrowserImplBase.this.getNewServiceCallbacks();
                MediaBrowserImplBase.this.mState = 1;
                try {
                    MediaBrowserImplBase.this.mServiceBinder.connect(MediaBrowserImplBase.this.mContext.getPackageName(), MediaBrowserImplBase.this.mRootHints, MediaBrowserImplBase.this.mServiceCallbacks);
                    return;
                }
                catch (RemoteException remoteException) {
                    Log.w((String)"MediaBrowserCompat", (String)("RemoteException during connect for " + (Object)MediaBrowserImplBase.this.mServiceComponent));
                    return;
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                if (!this.isCurrent("onServiceDisconnected")) {
                    return;
                }
                MediaBrowserImplBase.this.mServiceBinder = null;
                MediaBrowserImplBase.this.mServiceCallbacks = null;
                MediaBrowserImplBase.this.mState = 3;
                MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
            }
        }

        private static class ServiceCallbacks
        extends IMediaBrowserServiceCompatCallbacks.Stub {
            private WeakReference<MediaBrowserImplBase> mMediaBrowser;

            public ServiceCallbacks(MediaBrowserImplBase mediaBrowserImplBase) {
                this.mMediaBrowser = new WeakReference<MediaBrowserImplBase>(mediaBrowserImplBase);
            }

            @Override
            public void onConnect(String string2, MediaSessionCompat.Token token, Bundle bundle) {
                MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
                if (mediaBrowserImplBase != null) {
                    mediaBrowserImplBase.onServiceConnected(this, string2, token, bundle);
                }
            }

            @Override
            public void onConnectFailed() {
                MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
                if (mediaBrowserImplBase != null) {
                    mediaBrowserImplBase.onConnectionFailed(this);
                }
            }

            @Override
            public void onLoadChildren(String string2, List list) {
                MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
                if (mediaBrowserImplBase != null) {
                    mediaBrowserImplBase.onLoadChildren(this, string2, list);
                }
            }
        }

        private static class Subscription {
            SubscriptionCallback callback;
            final String id;

            Subscription(String string2) {
                this.id = string2;
            }
        }

    }

    public static class MediaItem
    implements Parcelable {
        public static final Parcelable.Creator<MediaItem> CREATOR = new Parcelable.Creator<MediaItem>(){

            public MediaItem createFromParcel(Parcel parcel) {
                return new MediaItem(parcel);
            }

            public MediaItem[] newArray(int n) {
                return new MediaItem[n];
            }
        };
        public static final int FLAG_BROWSABLE = 1;
        public static final int FLAG_PLAYABLE = 2;
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;

        private MediaItem(Parcel parcel) {
            this.mFlags = parcel.readInt();
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        }

        public MediaItem(@NonNull MediaDescriptionCompat mediaDescriptionCompat, int n) {
            if (mediaDescriptionCompat == null) {
                throw new IllegalArgumentException("description cannot be null");
            }
            if (TextUtils.isEmpty((CharSequence)mediaDescriptionCompat.getMediaId())) {
                throw new IllegalArgumentException("description must have a non-empty media id");
            }
            this.mFlags = n;
            this.mDescription = mediaDescriptionCompat;
        }

        public int describeContents() {
            return 0;
        }

        @NonNull
        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }

        public int getFlags() {
            return this.mFlags;
        }

        @NonNull
        public String getMediaId() {
            return this.mDescription.getMediaId();
        }

        public boolean isBrowsable() {
            if ((this.mFlags & 1) != 0) {
                return true;
            }
            return false;
        }

        public boolean isPlayable() {
            if ((this.mFlags & 2) != 0) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("MediaItem{");
            stringBuilder.append("mFlags=").append(this.mFlags);
            stringBuilder.append(", mDescription=").append(this.mDescription);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeInt(this.mFlags);
            this.mDescription.writeToParcel(parcel, n);
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface Flags {
        }

    }

    public static abstract class SubscriptionCallback {
        public void onChildrenLoaded(@NonNull String string2, @NonNull List<MediaItem> list) {
        }

        public void onError(@NonNull String string2) {
        }
    }

}

