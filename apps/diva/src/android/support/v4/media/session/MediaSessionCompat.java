/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.media.AudioManager
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.RemoteCallbackList
 *  android.os.RemoteException
 *  android.os.ResultReceiver
 *  android.os.SystemClock
 *  android.text.TextUtils
 *  android.util.Log
 *  android.view.KeyEvent
 */
package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompatApi14;
import android.support.v4.media.session.MediaSessionCompatApi18;
import android.support.v4.media.session.MediaSessionCompatApi19;
import android.support.v4.media.session.MediaSessionCompatApi21;
import android.support.v4.media.session.MediaSessionCompatApi22;
import android.support.v4.media.session.MediaSessionCompatApi23;
import android.support.v4.media.session.MediaSessionCompatApi8;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaSessionCompat {
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    private static final String TAG = "MediaSessionCompat";
    private final ArrayList<OnActiveChangeListener> mActiveListeners = new ArrayList();
    private final MediaControllerCompat mController;
    private final MediaSessionImpl mImpl;

    private MediaSessionCompat(Context context, MediaSessionImpl mediaSessionImpl) {
        this.mImpl = mediaSessionImpl;
        this.mController = new MediaControllerCompat(context, this);
    }

    public MediaSessionCompat(Context context, String string2) {
        this(context, string2, null, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public MediaSessionCompat(Context context, String string2, ComponentName componentName, PendingIntent pendingIntent) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        if (TextUtils.isEmpty((CharSequence)string2)) {
            throw new IllegalArgumentException("tag must not be null or empty");
        }
        ComponentName componentName2 = componentName;
        if (componentName == null) {
            componentName2 = new Intent("android.intent.action.MEDIA_BUTTON");
            componentName2.setPackage(context.getPackageName());
            List list = context.getPackageManager().queryBroadcastReceivers((Intent)componentName2, 0);
            if (list.size() == 1) {
                componentName = (ResolveInfo)list.get(0);
                componentName2 = new ComponentName(componentName.activityInfo.packageName, componentName.activityInfo.name);
            } else {
                componentName2 = componentName;
                if (list.size() > 1) {
                    Log.w((String)"MediaSessionCompat", (String)"More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, using null. Provide a specific ComponentName to use as this session's media button receiver");
                    componentName2 = componentName;
                }
            }
        }
        componentName = pendingIntent;
        if (componentName2 != null) {
            componentName = pendingIntent;
            if (pendingIntent == null) {
                componentName = new Intent("android.intent.action.MEDIA_BUTTON");
                componentName.setComponent(componentName2);
                componentName = PendingIntent.getBroadcast((Context)context, (int)0, (Intent)componentName, (int)0);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaSessionImplApi21(context, string2);
            this.mImpl.setMediaButtonReceiver((PendingIntent)componentName);
        } else {
            this.mImpl = new MediaSessionImplBase(context, string2, componentName2, (PendingIntent)componentName);
        }
        this.mController = new MediaControllerCompat(context, this);
    }

    public static MediaSessionCompat obtain(Context context, Object object) {
        return new MediaSessionCompat(context, new MediaSessionImplApi21(object));
    }

    public void addOnActiveChangeListener(OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener == null) {
            throw new IllegalArgumentException("Listener may not be null");
        }
        this.mActiveListeners.add(onActiveChangeListener);
    }

    public MediaControllerCompat getController() {
        return this.mController;
    }

    public Object getMediaSession() {
        return this.mImpl.getMediaSession();
    }

    public Object getRemoteControlClient() {
        return this.mImpl.getRemoteControlClient();
    }

    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public boolean isActive() {
        return this.mImpl.isActive();
    }

    public void release() {
        this.mImpl.release();
    }

    public void removeOnActiveChangeListener(OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener == null) {
            throw new IllegalArgumentException("Listener may not be null");
        }
        this.mActiveListeners.remove(onActiveChangeListener);
    }

    public void sendSessionEvent(String string2, Bundle bundle) {
        if (TextUtils.isEmpty((CharSequence)string2)) {
            throw new IllegalArgumentException("event cannot be null or empty");
        }
        this.mImpl.sendSessionEvent(string2, bundle);
    }

    public void setActive(boolean bl) {
        this.mImpl.setActive(bl);
        Iterator<OnActiveChangeListener> iterator = this.mActiveListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onActiveChanged();
        }
    }

    public void setCallback(Callback callback) {
        this.setCallback(callback, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setCallback(Callback callback, Handler handler) {
        MediaSessionImpl mediaSessionImpl = this.mImpl;
        if (handler == null) {
            handler = new Handler();
        }
        mediaSessionImpl.setCallback(callback, handler);
    }

    public void setExtras(Bundle bundle) {
        this.mImpl.setExtras(bundle);
    }

    public void setFlags(int n) {
        this.mImpl.setFlags(n);
    }

    public void setMediaButtonReceiver(PendingIntent pendingIntent) {
        this.mImpl.setMediaButtonReceiver(pendingIntent);
    }

    public void setMetadata(MediaMetadataCompat mediaMetadataCompat) {
        this.mImpl.setMetadata(mediaMetadataCompat);
    }

    public void setPlaybackState(PlaybackStateCompat playbackStateCompat) {
        this.mImpl.setPlaybackState(playbackStateCompat);
    }

    public void setPlaybackToLocal(int n) {
        this.mImpl.setPlaybackToLocal(n);
    }

    public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
        if (volumeProviderCompat == null) {
            throw new IllegalArgumentException("volumeProvider may not be null!");
        }
        this.mImpl.setPlaybackToRemote(volumeProviderCompat);
    }

    public void setQueue(List<QueueItem> list) {
        this.mImpl.setQueue(list);
    }

    public void setQueueTitle(CharSequence charSequence) {
        this.mImpl.setQueueTitle(charSequence);
    }

    public void setRatingType(int n) {
        this.mImpl.setRatingType(n);
    }

    public void setSessionActivity(PendingIntent pendingIntent) {
        this.mImpl.setSessionActivity(pendingIntent);
    }

    public static abstract class Callback {
        final Object mCallbackObj;

        public Callback() {
            if (Build.VERSION.SDK_INT >= 23) {
                this.mCallbackObj = MediaSessionCompatApi23.createCallback(new StubApi23());
                return;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                this.mCallbackObj = MediaSessionCompatApi21.createCallback(new StubApi21());
                return;
            }
            this.mCallbackObj = null;
        }

        public void onCommand(String string2, Bundle bundle, ResultReceiver resultReceiver) {
        }

        public void onCustomAction(String string2, Bundle bundle) {
        }

        public void onFastForward() {
        }

        public boolean onMediaButtonEvent(Intent intent) {
            return false;
        }

        public void onPause() {
        }

        public void onPlay() {
        }

        public void onPlayFromMediaId(String string2, Bundle bundle) {
        }

        public void onPlayFromSearch(String string2, Bundle bundle) {
        }

        public void onPlayFromUri(Uri uri, Bundle bundle) {
        }

        public void onRewind() {
        }

        public void onSeekTo(long l) {
        }

        public void onSetRating(RatingCompat ratingCompat) {
        }

        public void onSkipToNext() {
        }

        public void onSkipToPrevious() {
        }

        public void onSkipToQueueItem(long l) {
        }

        public void onStop() {
        }

        private class StubApi21
        implements MediaSessionCompatApi21.Callback {
            private StubApi21() {
            }

            @Override
            public void onCommand(String string2, Bundle bundle, ResultReceiver resultReceiver) {
                Callback.this.onCommand(string2, bundle, resultReceiver);
            }

            @Override
            public void onCustomAction(String string2, Bundle bundle) {
                Callback.this.onCustomAction(string2, bundle);
            }

            @Override
            public void onFastForward() {
                Callback.this.onFastForward();
            }

            @Override
            public boolean onMediaButtonEvent(Intent intent) {
                return Callback.this.onMediaButtonEvent(intent);
            }

            @Override
            public void onPause() {
                Callback.this.onPause();
            }

            @Override
            public void onPlay() {
                Callback.this.onPlay();
            }

            @Override
            public void onPlayFromMediaId(String string2, Bundle bundle) {
                Callback.this.onPlayFromMediaId(string2, bundle);
            }

            @Override
            public void onPlayFromSearch(String string2, Bundle bundle) {
                Callback.this.onPlayFromSearch(string2, bundle);
            }

            @Override
            public void onRewind() {
                Callback.this.onRewind();
            }

            @Override
            public void onSeekTo(long l) {
                Callback.this.onSeekTo(l);
            }

            @Override
            public void onSetRating(Object object) {
                Callback.this.onSetRating(RatingCompat.fromRating(object));
            }

            @Override
            public void onSkipToNext() {
                Callback.this.onSkipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                Callback.this.onSkipToPrevious();
            }

            @Override
            public void onSkipToQueueItem(long l) {
                Callback.this.onSkipToQueueItem(l);
            }

            @Override
            public void onStop() {
                Callback.this.onStop();
            }
        }

        private class StubApi23
        extends StubApi21
        implements MediaSessionCompatApi23.Callback {
            private StubApi23() {
            }

            @Override
            public void onPlayFromUri(Uri uri, Bundle bundle) {
                Callback.this.onPlayFromUri(uri, bundle);
            }
        }

    }

    static interface MediaSessionImpl {
        public Object getMediaSession();

        public Object getRemoteControlClient();

        public Token getSessionToken();

        public boolean isActive();

        public void release();

        public void sendSessionEvent(String var1, Bundle var2);

        public void setActive(boolean var1);

        public void setCallback(Callback var1, Handler var2);

        public void setExtras(Bundle var1);

        public void setFlags(int var1);

        public void setMediaButtonReceiver(PendingIntent var1);

        public void setMetadata(MediaMetadataCompat var1);

        public void setPlaybackState(PlaybackStateCompat var1);

        public void setPlaybackToLocal(int var1);

        public void setPlaybackToRemote(VolumeProviderCompat var1);

        public void setQueue(List<QueueItem> var1);

        public void setQueueTitle(CharSequence var1);

        public void setRatingType(int var1);

        public void setSessionActivity(PendingIntent var1);
    }

    static class MediaSessionImplApi21
    implements MediaSessionImpl {
        private PendingIntent mMediaButtonIntent;
        private final Object mSessionObj;
        private final Token mToken;

        public MediaSessionImplApi21(Context context, String string2) {
            this.mSessionObj = MediaSessionCompatApi21.createSession(context, string2);
            this.mToken = new Token((Object)MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
        }

        public MediaSessionImplApi21(Object object) {
            this.mSessionObj = MediaSessionCompatApi21.verifySession(object);
            this.mToken = new Token((Object)MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
        }

        @Override
        public Object getMediaSession() {
            return this.mSessionObj;
        }

        @Override
        public Object getRemoteControlClient() {
            return null;
        }

        @Override
        public Token getSessionToken() {
            return this.mToken;
        }

        @Override
        public boolean isActive() {
            return MediaSessionCompatApi21.isActive(this.mSessionObj);
        }

        @Override
        public void release() {
            MediaSessionCompatApi21.release(this.mSessionObj);
        }

        @Override
        public void sendSessionEvent(String string2, Bundle bundle) {
            MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, string2, bundle);
        }

        @Override
        public void setActive(boolean bl) {
            MediaSessionCompatApi21.setActive(this.mSessionObj, bl);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void setCallback(Callback object, Handler handler) {
            Object object2 = this.mSessionObj;
            object = object == null ? null : object.mCallbackObj;
            MediaSessionCompatApi21.setCallback(object2, object, handler);
        }

        @Override
        public void setExtras(Bundle bundle) {
            MediaSessionCompatApi21.setExtras(this.mSessionObj, bundle);
        }

        @Override
        public void setFlags(int n) {
            MediaSessionCompatApi21.setFlags(this.mSessionObj, n);
        }

        @Override
        public void setMediaButtonReceiver(PendingIntent pendingIntent) {
            this.mMediaButtonIntent = pendingIntent;
            MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, pendingIntent);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void setMetadata(MediaMetadataCompat object) {
            Object object2 = this.mSessionObj;
            object = object == null ? null : object.getMediaMetadata();
            MediaSessionCompatApi21.setMetadata(object2, object);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void setPlaybackState(PlaybackStateCompat object) {
            Object object2 = this.mSessionObj;
            object = object == null ? null : object.getPlaybackState();
            MediaSessionCompatApi21.setPlaybackState(object2, object);
        }

        @Override
        public void setPlaybackToLocal(int n) {
            MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, n);
        }

        @Override
        public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
            MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, volumeProviderCompat.getVolumeProvider());
        }

        @Override
        public void setQueue(List<QueueItem> object) {
            ArrayList<Object> arrayList = null;
            if (object != null) {
                ArrayList<Object> arrayList2 = new ArrayList<Object>();
                object = object.iterator();
                do {
                    arrayList = arrayList2;
                    if (!object.hasNext()) break;
                    arrayList2.add(((QueueItem)object.next()).getQueueItem());
                } while (true);
            }
            MediaSessionCompatApi21.setQueue(this.mSessionObj, arrayList);
        }

        @Override
        public void setQueueTitle(CharSequence charSequence) {
            MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, charSequence);
        }

        @Override
        public void setRatingType(int n) {
            if (Build.VERSION.SDK_INT < 22) {
                return;
            }
            MediaSessionCompatApi22.setRatingType(this.mSessionObj, n);
        }

        @Override
        public void setSessionActivity(PendingIntent pendingIntent) {
            MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, pendingIntent);
        }
    }

    static class MediaSessionImplBase
    implements MediaSessionImpl {
        private final AudioManager mAudioManager;
        private Callback mCallback;
        private final ComponentName mComponentName;
        private final Context mContext;
        private final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks = new RemoteCallbackList();
        private boolean mDestroyed = false;
        private Bundle mExtras;
        private int mFlags;
        private final MessageHandler mHandler;
        private boolean mIsActive = false;
        private boolean mIsMbrRegistered = false;
        private boolean mIsRccRegistered = false;
        private int mLocalStream;
        private final Object mLock = new Object();
        private final PendingIntent mMediaButtonEventReceiver;
        private MediaMetadataCompat mMetadata;
        private final String mPackageName;
        private List<QueueItem> mQueue;
        private CharSequence mQueueTitle;
        private int mRatingType;
        private final Object mRccObj;
        private PendingIntent mSessionActivity;
        private PlaybackStateCompat mState;
        private final MediaSessionStub mStub;
        private final String mTag;
        private final Token mToken;
        private VolumeProviderCompat.Callback mVolumeCallback;
        private VolumeProviderCompat mVolumeProvider;
        private int mVolumeType;

        public MediaSessionImplBase(Context context, String string2, ComponentName componentName, PendingIntent pendingIntent) {
            this.mVolumeCallback = new VolumeProviderCompat.Callback(){

                @Override
                public void onVolumeChanged(VolumeProviderCompat object) {
                    if (MediaSessionImplBase.this.mVolumeProvider != object) {
                        return;
                    }
                    object = new ParcelableVolumeInfo(MediaSessionImplBase.this.mVolumeType, MediaSessionImplBase.this.mLocalStream, object.getVolumeControl(), object.getMaxVolume(), object.getCurrentVolume());
                    MediaSessionImplBase.this.sendVolumeInfoChanged((ParcelableVolumeInfo)object);
                }
            };
            if (componentName == null) {
                throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
            }
            this.mContext = context;
            this.mPackageName = context.getPackageName();
            this.mAudioManager = (AudioManager)context.getSystemService("audio");
            this.mTag = string2;
            this.mComponentName = componentName;
            this.mMediaButtonEventReceiver = pendingIntent;
            this.mStub = new MediaSessionStub();
            this.mToken = new Token(this.mStub);
            this.mHandler = new MessageHandler(Looper.myLooper());
            this.mRatingType = 0;
            this.mVolumeType = 1;
            this.mLocalStream = 3;
            if (Build.VERSION.SDK_INT >= 14) {
                this.mRccObj = MediaSessionCompatApi14.createRemoteControlClient(pendingIntent);
                return;
            }
            this.mRccObj = null;
        }

        private void adjustVolume(int n, int n2) {
            if (this.mVolumeType == 2) {
                if (this.mVolumeProvider != null) {
                    this.mVolumeProvider.onAdjustVolume(n);
                }
                return;
            }
            this.mAudioManager.adjustStreamVolume(this.mLocalStream, n, n2);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        private PlaybackStateCompat getStateWithUpdatedPosition() {
            var3_1 = -1;
            var7_2 = this.mLock;
            // MONITORENTER : var7_2
            var9_3 = this.mState;
            var1_4 = var3_1;
            if (this.mMetadata != null) {
                var1_4 = var3_1;
                if (this.mMetadata.containsKey("android.media.metadata.DURATION")) {
                    var1_4 = this.mMetadata.getLong("android.media.metadata.DURATION");
                }
            }
            // MONITOREXIT : var7_2
            var8_5 = null;
            var7_2 = var8_5;
            if (var9_3 == null) return var9_3;
            if (var9_3.getState() == 3 || var9_3.getState() == 4) ** GOTO lbl-1000
            var7_2 = var8_5;
            if (var9_3.getState() == 5) lbl-1000: // 2 sources:
            {
                var3_1 = var9_3.getLastPositionUpdateTime();
                var5_6 = SystemClock.elapsedRealtime();
                var7_2 = var8_5;
                if (var3_1 > 0) {
                    var3_1 = (long)(var9_3.getPlaybackSpeed() * (float)(var5_6 - var3_1)) + var9_3.getPosition();
                    if (var1_4 < 0 || var3_1 <= var1_4) {
                        var1_4 = var3_1;
                        if (var3_1 < 0) {
                            var1_4 = 0;
                        }
                    }
                    var7_2 = new PlaybackStateCompat.Builder(var9_3);
                    var7_2.setState(var9_3.getState(), var1_4, var9_3.getPlaybackSpeed(), var5_6);
                    var7_2 = var7_2.build();
                }
            }
            if (var7_2 != null) return var7_2;
            return var9_3;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void sendEvent(String string2, Bundle bundle) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
            do {
                if (n < 0) {
                    this.mControllerCallbacks.finishBroadcast();
                    return;
                }
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                try {
                    iMediaControllerCallback.onEvent(string2, bundle);
                }
                catch (RemoteException remoteException) {}
                --n;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void sendMetadata(MediaMetadataCompat mediaMetadataCompat) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
            do {
                if (n < 0) {
                    this.mControllerCallbacks.finishBroadcast();
                    return;
                }
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                try {
                    iMediaControllerCallback.onMetadataChanged(mediaMetadataCompat);
                }
                catch (RemoteException remoteException) {}
                --n;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void sendQueue(List<QueueItem> list) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
            do {
                if (n < 0) {
                    this.mControllerCallbacks.finishBroadcast();
                    return;
                }
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                try {
                    iMediaControllerCallback.onQueueChanged(list);
                }
                catch (RemoteException remoteException) {}
                --n;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void sendQueueTitle(CharSequence charSequence) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
            do {
                if (n < 0) {
                    this.mControllerCallbacks.finishBroadcast();
                    return;
                }
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                try {
                    iMediaControllerCallback.onQueueTitleChanged(charSequence);
                }
                catch (RemoteException remoteException) {}
                --n;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void sendSessionDestroyed() {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
            do {
                if (n < 0) {
                    this.mControllerCallbacks.finishBroadcast();
                    this.mControllerCallbacks.kill();
                    return;
                }
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                try {
                    iMediaControllerCallback.onSessionDestroyed();
                }
                catch (RemoteException remoteException) {}
                --n;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void sendState(PlaybackStateCompat playbackStateCompat) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
            do {
                if (n < 0) {
                    this.mControllerCallbacks.finishBroadcast();
                    return;
                }
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                try {
                    iMediaControllerCallback.onPlaybackStateChanged(playbackStateCompat);
                }
                catch (RemoteException remoteException) {}
                --n;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void sendVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
            do {
                if (n < 0) {
                    this.mControllerCallbacks.finishBroadcast();
                    return;
                }
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                try {
                    iMediaControllerCallback.onVolumeInfoChanged(parcelableVolumeInfo);
                }
                catch (RemoteException remoteException) {}
                --n;
            } while (true);
        }

        private void setVolumeTo(int n, int n2) {
            if (this.mVolumeType == 2) {
                if (this.mVolumeProvider != null) {
                    this.mVolumeProvider.onSetVolumeTo(n);
                }
                return;
            }
            this.mAudioManager.setStreamVolume(this.mLocalStream, n, n2);
        }

        /*
         * Enabled aggressive block sorting
         */
        private boolean update() {
            boolean bl;
            boolean bl2 = false;
            if (this.mIsActive) {
                if (Build.VERSION.SDK_INT >= 8) {
                    if (!this.mIsMbrRegistered && (this.mFlags & 1) != 0) {
                        if (Build.VERSION.SDK_INT >= 18) {
                            MediaSessionCompatApi18.registerMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
                        } else {
                            MediaSessionCompatApi8.registerMediaButtonEventReceiver(this.mContext, this.mComponentName);
                        }
                        this.mIsMbrRegistered = true;
                    } else if (this.mIsMbrRegistered && (this.mFlags & 1) == 0) {
                        if (Build.VERSION.SDK_INT >= 18) {
                            MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
                        } else {
                            MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(this.mContext, this.mComponentName);
                        }
                        this.mIsMbrRegistered = false;
                    }
                }
                bl = bl2;
                if (Build.VERSION.SDK_INT < 14) return bl;
                if (!this.mIsRccRegistered && (this.mFlags & 2) != 0) {
                    MediaSessionCompatApi14.registerRemoteControlClient(this.mContext, this.mRccObj);
                    this.mIsRccRegistered = true;
                    return true;
                }
                bl = bl2;
                if (!this.mIsRccRegistered) return bl;
                bl = bl2;
                if ((this.mFlags & 2) != 0) return bl;
                MediaSessionCompatApi14.setState(this.mRccObj, 0);
                MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
                this.mIsRccRegistered = false;
                return false;
            }
            if (this.mIsMbrRegistered) {
                if (Build.VERSION.SDK_INT >= 18) {
                    MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
                } else {
                    MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(this.mContext, this.mComponentName);
                }
                this.mIsMbrRegistered = false;
            }
            bl = bl2;
            if (!this.mIsRccRegistered) return bl;
            MediaSessionCompatApi14.setState(this.mRccObj, 0);
            MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
            this.mIsRccRegistered = false;
            return false;
        }

        @Override
        public Object getMediaSession() {
            return null;
        }

        @Override
        public Object getRemoteControlClient() {
            return this.mRccObj;
        }

        @Override
        public Token getSessionToken() {
            return this.mToken;
        }

        @Override
        public boolean isActive() {
            return this.mIsActive;
        }

        @Override
        public void release() {
            this.mIsActive = false;
            this.mDestroyed = true;
            this.update();
            this.sendSessionDestroyed();
        }

        @Override
        public void sendSessionEvent(String string2, Bundle bundle) {
            this.sendEvent(string2, bundle);
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        @Override
        public void setActive(boolean bl) {
            if (bl == this.mIsActive) {
                return;
            }
            this.mIsActive = bl;
            if (!this.update()) return;
            this.setMetadata(this.mMetadata);
            this.setPlaybackState(this.mState);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void setCallback(final Callback callback, Handler object) {
            if (callback == this.mCallback) {
                return;
            }
            if (callback == null || Build.VERSION.SDK_INT < 18) {
                if (Build.VERSION.SDK_INT >= 18) {
                    MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, null);
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, null);
                }
            } else {
                if (object == null) {
                    new Handler();
                }
                object = new MediaSessionCompatApi14.Callback(){

                    @Override
                    public void onCommand(String string2, Bundle bundle, ResultReceiver resultReceiver) {
                        callback.onCommand(string2, bundle, resultReceiver);
                    }

                    @Override
                    public void onFastForward() {
                        callback.onFastForward();
                    }

                    @Override
                    public boolean onMediaButtonEvent(Intent intent) {
                        return callback.onMediaButtonEvent(intent);
                    }

                    @Override
                    public void onPause() {
                        callback.onPause();
                    }

                    @Override
                    public void onPlay() {
                        callback.onPlay();
                    }

                    @Override
                    public void onRewind() {
                        callback.onRewind();
                    }

                    @Override
                    public void onSeekTo(long l) {
                        callback.onSeekTo(l);
                    }

                    @Override
                    public void onSetRating(Object object) {
                        callback.onSetRating(RatingCompat.fromRating(object));
                    }

                    @Override
                    public void onSkipToNext() {
                        callback.onSkipToNext();
                    }

                    @Override
                    public void onSkipToPrevious() {
                        callback.onSkipToPrevious();
                    }

                    @Override
                    public void onStop() {
                        callback.onStop();
                    }
                };
                if (Build.VERSION.SDK_INT >= 18) {
                    Object object2 = MediaSessionCompatApi18.createPlaybackPositionUpdateListener((MediaSessionCompatApi14.Callback)object);
                    MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, object2);
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    object = MediaSessionCompatApi19.createMetadataUpdateListener((MediaSessionCompatApi14.Callback)object);
                    MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, object);
                }
            }
            this.mCallback = callback;
        }

        @Override
        public void setExtras(Bundle bundle) {
            this.mExtras = bundle;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setFlags(int n) {
            Object object = this.mLock;
            synchronized (object) {
                this.mFlags = n;
            }
            this.update();
        }

        @Override
        public void setMediaButtonReceiver(PendingIntent pendingIntent) {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        @Override
        public void setMetadata(MediaMetadataCompat object) {
            Object object2 = null;
            Object object3 = null;
            Object object4 = this.mLock;
            // MONITORENTER : object4
            this.mMetadata = object;
            // MONITOREXIT : object4
            this.sendMetadata((MediaMetadataCompat)object);
            if (!this.mIsActive) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                object2 = this.mRccObj;
                object = object == null ? object3 : object.getBundle();
                long l = this.mState == null ? 0 : this.mState.getActions();
                MediaSessionCompatApi19.setMetadata(object2, (Bundle)object, l);
                return;
            }
            if (Build.VERSION.SDK_INT < 14) return;
            object3 = this.mRccObj;
            object = object == null ? object2 : object.getBundle();
            MediaSessionCompatApi14.setMetadata(object3, (Bundle)object);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setPlaybackState(PlaybackStateCompat playbackStateCompat) {
            Object object = this.mLock;
            synchronized (object) {
                this.mState = playbackStateCompat;
            }
            this.sendState(playbackStateCompat);
            if (!this.mIsActive) return;
            if (playbackStateCompat == null) {
                if (Build.VERSION.SDK_INT < 14) return;
                {
                    MediaSessionCompatApi14.setState(this.mRccObj, 0);
                    MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, 0);
                    return;
                }
            }
            if (Build.VERSION.SDK_INT >= 18) {
                MediaSessionCompatApi18.setState(this.mRccObj, playbackStateCompat.getState(), playbackStateCompat.getPosition(), playbackStateCompat.getPlaybackSpeed(), playbackStateCompat.getLastPositionUpdateTime());
            } else if (Build.VERSION.SDK_INT >= 14) {
                MediaSessionCompatApi14.setState(this.mRccObj, playbackStateCompat.getState());
            }
            if (Build.VERSION.SDK_INT >= 19) {
                MediaSessionCompatApi19.setTransportControlFlags(this.mRccObj, playbackStateCompat.getActions());
                return;
            }
            if (Build.VERSION.SDK_INT >= 18) {
                MediaSessionCompatApi18.setTransportControlFlags(this.mRccObj, playbackStateCompat.getActions());
                return;
            }
            if (Build.VERSION.SDK_INT < 14) {
                return;
            }
            MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, playbackStateCompat.getActions());
        }

        @Override
        public void setPlaybackToLocal(int n) {
            if (this.mVolumeProvider != null) {
                this.mVolumeProvider.setCallback(null);
            }
            this.mVolumeType = 1;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
        }

        @Override
        public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
            if (volumeProviderCompat == null) {
                throw new IllegalArgumentException("volumeProvider may not be null");
            }
            if (this.mVolumeProvider != null) {
                this.mVolumeProvider.setCallback(null);
            }
            this.mVolumeType = 2;
            this.mVolumeProvider = volumeProviderCompat;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
            volumeProviderCompat.setCallback(this.mVolumeCallback);
        }

        @Override
        public void setQueue(List<QueueItem> list) {
            this.mQueue = list;
            this.sendQueue(list);
        }

        @Override
        public void setQueueTitle(CharSequence charSequence) {
            this.mQueueTitle = charSequence;
            this.sendQueueTitle(charSequence);
        }

        @Override
        public void setRatingType(int n) {
            this.mRatingType = n;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setSessionActivity(PendingIntent pendingIntent) {
            Object object = this.mLock;
            synchronized (object) {
                this.mSessionActivity = pendingIntent;
                return;
            }
        }

        private static final class Command {
            public final String command;
            public final Bundle extras;
            public final ResultReceiver stub;

            public Command(String string2, Bundle bundle, ResultReceiver resultReceiver) {
                this.command = string2;
                this.extras = bundle;
                this.stub = resultReceiver;
            }
        }

        class MediaSessionStub
        extends IMediaSession.Stub {
            MediaSessionStub() {
            }

            @Override
            public void adjustVolume(int n, int n2, String string2) {
                MediaSessionImplBase.this.adjustVolume(n, n2);
            }

            @Override
            public void fastForward() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(9);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public Bundle getExtras() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    return MediaSessionImplBase.this.mExtras;
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public long getFlags() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    return MediaSessionImplBase.this.mFlags;
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public PendingIntent getLaunchPendingIntent() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    return MediaSessionImplBase.this.mSessionActivity;
                }
            }

            @Override
            public MediaMetadataCompat getMetadata() {
                return MediaSessionImplBase.this.mMetadata;
            }

            @Override
            public String getPackageName() {
                return MediaSessionImplBase.this.mPackageName;
            }

            @Override
            public PlaybackStateCompat getPlaybackState() {
                return MediaSessionImplBase.this.getStateWithUpdatedPosition();
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public List<QueueItem> getQueue() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    return MediaSessionImplBase.this.mQueue;
                }
            }

            @Override
            public CharSequence getQueueTitle() {
                return MediaSessionImplBase.this.mQueueTitle;
            }

            @Override
            public int getRatingType() {
                return MediaSessionImplBase.this.mRatingType;
            }

            @Override
            public String getTag() {
                return MediaSessionImplBase.this.mTag;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public ParcelableVolumeInfo getVolumeAttributes() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    int n;
                    int n2;
                    int n3;
                    int n4 = MediaSessionImplBase.this.mVolumeType;
                    int n5 = MediaSessionImplBase.this.mLocalStream;
                    VolumeProviderCompat volumeProviderCompat = MediaSessionImplBase.this.mVolumeProvider;
                    if (n4 == 2) {
                        n2 = volumeProviderCompat.getVolumeControl();
                        n3 = volumeProviderCompat.getMaxVolume();
                        n = volumeProviderCompat.getCurrentVolume();
                    } else {
                        n2 = 2;
                        n3 = MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(n5);
                        n = MediaSessionImplBase.this.mAudioManager.getStreamVolume(n5);
                    }
                    return new ParcelableVolumeInfo(n4, n5, n2, n3, n);
                }
            }

            @Override
            public boolean isTransportControlEnabled() {
                if ((MediaSessionImplBase.this.mFlags & 2) != 0) {
                    return true;
                }
                return false;
            }

            @Override
            public void next() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(7);
            }

            @Override
            public void pause() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(5);
            }

            @Override
            public void play() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(1);
            }

            @Override
            public void playFromMediaId(String string2, Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(2, (Object)string2, bundle);
            }

            @Override
            public void playFromSearch(String string2, Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(3, (Object)string2, bundle);
            }

            @Override
            public void playFromUri(Uri uri, Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(18, (Object)uri, bundle);
            }

            @Override
            public void previous() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(8);
            }

            @Override
            public void rate(RatingCompat ratingCompat) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(12, ratingCompat);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void registerCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                if (!MediaSessionImplBase.this.mDestroyed) {
                    MediaSessionImplBase.this.mControllerCallbacks.register((IInterface)iMediaControllerCallback);
                    return;
                }
                try {
                    iMediaControllerCallback.onSessionDestroyed();
                    return;
                }
                catch (Exception exception) {
                    return;
                }
            }

            @Override
            public void rewind() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(10);
            }

            @Override
            public void seekTo(long l) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(11, l);
            }

            @Override
            public void sendCommand(String string2, Bundle bundle, ResultReceiverWrapper resultReceiverWrapper) {
                MediaSessionImplBase.this.mHandler.post(15, new Command(string2, bundle, resultReceiverWrapper.mResultReceiver));
            }

            @Override
            public void sendCustomAction(String string2, Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(13, (Object)string2, bundle);
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public boolean sendMediaButton(KeyEvent keyEvent) {
                if ((MediaSessionImplBase.this.mFlags & 1) == 0) return false;
                boolean bl = true;
                if (!bl) return bl;
                MediaSessionImplBase.this.mHandler.post(14, (Object)keyEvent);
                return bl;
            }

            @Override
            public void setVolumeTo(int n, int n2, String string2) {
                MediaSessionImplBase.this.setVolumeTo(n, n2);
            }

            @Override
            public void skipToQueueItem(long l) {
                MediaSessionImplBase.this.mHandler.post(4, l);
            }

            @Override
            public void stop() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(6);
            }

            @Override
            public void unregisterCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                MediaSessionImplBase.this.mControllerCallbacks.unregister((IInterface)iMediaControllerCallback);
            }
        }

        private class MessageHandler
        extends Handler {
            private static final int KEYCODE_MEDIA_PAUSE = 127;
            private static final int KEYCODE_MEDIA_PLAY = 126;
            private static final int MSG_ADJUST_VOLUME = 16;
            private static final int MSG_COMMAND = 15;
            private static final int MSG_CUSTOM_ACTION = 13;
            private static final int MSG_FAST_FORWARD = 9;
            private static final int MSG_MEDIA_BUTTON = 14;
            private static final int MSG_NEXT = 7;
            private static final int MSG_PAUSE = 5;
            private static final int MSG_PLAY = 1;
            private static final int MSG_PLAY_MEDIA_ID = 2;
            private static final int MSG_PLAY_SEARCH = 3;
            private static final int MSG_PLAY_URI = 18;
            private static final int MSG_PREVIOUS = 8;
            private static final int MSG_RATE = 12;
            private static final int MSG_REWIND = 10;
            private static final int MSG_SEEK_TO = 11;
            private static final int MSG_SET_VOLUME = 17;
            private static final int MSG_SKIP_TO_ITEM = 4;
            private static final int MSG_STOP = 6;

            public MessageHandler(Looper looper) {
                super(looper);
            }

            /*
             * Enabled aggressive block sorting
             */
            private void onMediaButtonEvent(KeyEvent keyEvent) {
                boolean bl;
                boolean bl2 = true;
                if (keyEvent == null) return;
                if (keyEvent.getAction() != 0) return;
                long l = MediaSessionImplBase.this.mState == null ? 0 : MediaSessionImplBase.this.mState.getActions();
                switch (keyEvent.getKeyCode()) {
                    default: {
                        return;
                    }
                    case 79: 
                    case 85: {
                        bl = MediaSessionImplBase.this.mState != null && MediaSessionImplBase.this.mState.getState() == 3;
                    }
                    case 126: {
                        if ((4 & l) == 0) return;
                        {
                            MediaSessionImplBase.this.mCallback.onPlay();
                            return;
                        }
                    }
                    case 127: {
                        if ((2 & l) == 0) return;
                        {
                            MediaSessionImplBase.this.mCallback.onPause();
                            return;
                        }
                    }
                    case 87: {
                        if ((32 & l) == 0) return;
                        {
                            MediaSessionImplBase.this.mCallback.onSkipToNext();
                            return;
                        }
                    }
                    case 88: {
                        if ((16 & l) == 0) return;
                        {
                            MediaSessionImplBase.this.mCallback.onSkipToPrevious();
                            return;
                        }
                    }
                    case 86: {
                        if ((1 & l) == 0) return;
                        {
                            MediaSessionImplBase.this.mCallback.onStop();
                            return;
                        }
                    }
                    case 90: {
                        if ((64 & l) == 0) return;
                        {
                            MediaSessionImplBase.this.mCallback.onFastForward();
                            return;
                        }
                    }
                    case 89: {
                        if ((8 & l) == 0) return;
                        {
                            MediaSessionImplBase.this.mCallback.onRewind();
                            return;
                        }
                    }
                }
                boolean bl3 = (516 & l) != 0;
                if ((514 & l) == 0) {
                    bl2 = false;
                }
                if (bl && bl2) {
                    MediaSessionImplBase.this.mCallback.onPause();
                    return;
                }
                if (bl || !bl3) {
                    return;
                }
                MediaSessionImplBase.this.mCallback.onPlay();
            }

            /*
             * Enabled aggressive block sorting
             * Lifted jumps to return sites
             */
            public void handleMessage(Message object) {
                if (MediaSessionImplBase.this.mCallback == null) {
                    return;
                }
                switch (object.what) {
                    default: {
                        return;
                    }
                    case 1: {
                        MediaSessionImplBase.this.mCallback.onPlay();
                        return;
                    }
                    case 2: {
                        MediaSessionImplBase.this.mCallback.onPlayFromMediaId((String)object.obj, object.getData());
                        return;
                    }
                    case 3: {
                        MediaSessionImplBase.this.mCallback.onPlayFromSearch((String)object.obj, object.getData());
                        return;
                    }
                    case 18: {
                        MediaSessionImplBase.this.mCallback.onPlayFromUri((Uri)object.obj, object.getData());
                        return;
                    }
                    case 4: {
                        MediaSessionImplBase.this.mCallback.onSkipToQueueItem((Long)object.obj);
                        return;
                    }
                    case 5: {
                        MediaSessionImplBase.this.mCallback.onPause();
                        return;
                    }
                    case 6: {
                        MediaSessionImplBase.this.mCallback.onStop();
                        return;
                    }
                    case 7: {
                        MediaSessionImplBase.this.mCallback.onSkipToNext();
                        return;
                    }
                    case 8: {
                        MediaSessionImplBase.this.mCallback.onSkipToPrevious();
                        return;
                    }
                    case 9: {
                        MediaSessionImplBase.this.mCallback.onFastForward();
                        return;
                    }
                    case 10: {
                        MediaSessionImplBase.this.mCallback.onRewind();
                        return;
                    }
                    case 11: {
                        MediaSessionImplBase.this.mCallback.onSeekTo((Long)object.obj);
                        return;
                    }
                    case 12: {
                        MediaSessionImplBase.this.mCallback.onSetRating((RatingCompat)object.obj);
                        return;
                    }
                    case 13: {
                        MediaSessionImplBase.this.mCallback.onCustomAction((String)object.obj, object.getData());
                        return;
                    }
                    case 14: {
                        object = (KeyEvent)object.obj;
                        Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                        intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)object);
                        if (MediaSessionImplBase.this.mCallback.onMediaButtonEvent(intent)) return;
                        this.onMediaButtonEvent((KeyEvent)object);
                        return;
                    }
                    case 15: {
                        object = (Command)object.obj;
                        MediaSessionImplBase.this.mCallback.onCommand(object.command, object.extras, object.stub);
                        return;
                    }
                    case 16: {
                        MediaSessionImplBase.this.adjustVolume((Integer)object.obj, 0);
                        return;
                    }
                    case 17: 
                }
                MediaSessionImplBase.this.setVolumeTo((Integer)object.obj, 0);
            }

            public void post(int n) {
                this.post(n, null);
            }

            public void post(int n, Object object) {
                this.obtainMessage(n, object).sendToTarget();
            }

            public void post(int n, Object object, int n2) {
                this.obtainMessage(n, n2, 0, object).sendToTarget();
            }

            public void post(int n, Object object, Bundle bundle) {
                object = this.obtainMessage(n, object);
                object.setData(bundle);
                object.sendToTarget();
            }
        }

    }

    public static interface OnActiveChangeListener {
        public void onActiveChanged();
    }

    public static final class QueueItem
    implements Parcelable {
        public static final Parcelable.Creator<QueueItem> CREATOR = new Parcelable.Creator<QueueItem>(){

            public QueueItem createFromParcel(Parcel parcel) {
                return new QueueItem(parcel);
            }

            public QueueItem[] newArray(int n) {
                return new QueueItem[n];
            }
        };
        public static final int UNKNOWN_ID = -1;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private Object mItem;

        private QueueItem(Parcel parcel) {
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.mId = parcel.readLong();
        }

        public QueueItem(MediaDescriptionCompat mediaDescriptionCompat, long l) {
            this(null, mediaDescriptionCompat, l);
        }

        private QueueItem(Object object, MediaDescriptionCompat mediaDescriptionCompat, long l) {
            if (mediaDescriptionCompat == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            }
            if (l == -1) {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            this.mDescription = mediaDescriptionCompat;
            this.mId = l;
            this.mItem = object;
        }

        public static QueueItem obtain(Object object) {
            return new QueueItem(object, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(object)), MediaSessionCompatApi21.QueueItem.getQueueId(object));
        }

        public int describeContents() {
            return 0;
        }

        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }

        public long getQueueId() {
            return this.mId;
        }

        public Object getQueueItem() {
            if (this.mItem != null || Build.VERSION.SDK_INT < 21) {
                return this.mItem;
            }
            this.mItem = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
            return this.mItem;
        }

        public String toString() {
            return "MediaSession.QueueItem {Description=" + this.mDescription + ", Id=" + this.mId + " }";
        }

        public void writeToParcel(Parcel parcel, int n) {
            this.mDescription.writeToParcel(parcel, n);
            parcel.writeLong(this.mId);
        }

    }

    static final class ResultReceiverWrapper
    implements Parcelable {
        public static final Parcelable.Creator<ResultReceiverWrapper> CREATOR = new Parcelable.Creator<ResultReceiverWrapper>(){

            public ResultReceiverWrapper createFromParcel(Parcel parcel) {
                return new ResultReceiverWrapper(parcel);
            }

            public ResultReceiverWrapper[] newArray(int n) {
                return new ResultReceiverWrapper[n];
            }
        };
        private ResultReceiver mResultReceiver;

        ResultReceiverWrapper(Parcel parcel) {
            this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
        }

        public ResultReceiverWrapper(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int n) {
            this.mResultReceiver.writeToParcel(parcel, n);
        }

    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface SessionFlags {
    }

    public static final class Token
    implements Parcelable {
        public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>(){

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Token createFromParcel(Parcel parcel) {
                if (Build.VERSION.SDK_INT >= 21) {
                    parcel = parcel.readParcelable(null);
                    do {
                        return new Token((Object)parcel);
                        break;
                    } while (true);
                }
                parcel = parcel.readStrongBinder();
                return new Token((Object)parcel);
            }

            public Token[] newArray(int n) {
                return new Token[n];
            }
        };
        private final Object mInner;

        Token(Object object) {
            this.mInner = object;
        }

        public static Token fromToken(Object object) {
            if (object == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            return new Token(MediaSessionCompatApi21.verifyToken(object));
        }

        public int describeContents() {
            return 0;
        }

        public Object getToken() {
            return this.mInner;
        }

        public void writeToParcel(Parcel parcel, int n) {
            if (Build.VERSION.SDK_INT >= 21) {
                parcel.writeParcelable((Parcelable)this.mInner, n);
                return;
            }
            parcel.writeStrongBinder((IBinder)this.mInner);
        }

    }

}

