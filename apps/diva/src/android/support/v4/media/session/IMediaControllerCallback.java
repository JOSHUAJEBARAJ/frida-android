/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.RemoteException
 *  android.text.TextUtils
 */
package android.support.v4.media.session;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public interface IMediaControllerCallback
extends IInterface {
    public void onEvent(String var1, Bundle var2) throws RemoteException;

    public void onExtrasChanged(Bundle var1) throws RemoteException;

    public void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException;

    public void onPlaybackStateChanged(PlaybackStateCompat var1) throws RemoteException;

    public void onQueueChanged(List<MediaSessionCompat.QueueItem> var1) throws RemoteException;

    public void onQueueTitleChanged(CharSequence var1) throws RemoteException;

    public void onSessionDestroyed() throws RemoteException;

    public void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IMediaControllerCallback {
        private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaControllerCallback";
        static final int TRANSACTION_onEvent = 1;
        static final int TRANSACTION_onExtrasChanged = 7;
        static final int TRANSACTION_onMetadataChanged = 4;
        static final int TRANSACTION_onPlaybackStateChanged = 3;
        static final int TRANSACTION_onQueueChanged = 5;
        static final int TRANSACTION_onQueueTitleChanged = 6;
        static final int TRANSACTION_onSessionDestroyed = 2;
        static final int TRANSACTION_onVolumeInfoChanged = 8;

        public Stub() {
            this.attachInterface((IInterface)this, "android.support.v4.media.session.IMediaControllerCallback");
        }

        public static IMediaControllerCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("android.support.v4.media.session.IMediaControllerCallback");
            if (iInterface != null && iInterface instanceof IMediaControllerCallback) {
                return (IMediaControllerCallback)iInterface;
            }
            return new Proxy(iBinder);
        }

        public IBinder asBinder() {
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onTransact(int n, Parcel object, Parcel object2, int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, (Parcel)object, (Parcel)object2, n2);
                }
                case 1598968902: {
                    object2.writeString("android.support.v4.media.session.IMediaControllerCallback");
                    return true;
                }
                case 1: {
                    object.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    object2 = object.readString();
                    object = object.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel((Parcel)object) : null;
                    this.onEvent((String)object2, (Bundle)object);
                    return true;
                }
                case 2: {
                    object.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    this.onSessionDestroyed();
                    return true;
                }
                case 3: {
                    object.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    object = object.readInt() != 0 ? (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel((Parcel)object) : null;
                    this.onPlaybackStateChanged((PlaybackStateCompat)object);
                    return true;
                }
                case 4: {
                    object.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    object = object.readInt() != 0 ? (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel((Parcel)object) : null;
                    this.onMetadataChanged((MediaMetadataCompat)object);
                    return true;
                }
                case 5: {
                    object.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    this.onQueueChanged((List)object.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR));
                    return true;
                }
                case 6: {
                    object.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    object = object.readInt() != 0 ? (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel((Parcel)object) : null;
                    this.onQueueTitleChanged((CharSequence)object);
                    return true;
                }
                case 7: {
                    object.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                    object = object.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel((Parcel)object) : null;
                    this.onExtrasChanged((Bundle)object);
                    return true;
                }
                case 8: 
            }
            object.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
            object = object.readInt() != 0 ? (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel((Parcel)object) : null;
            this.onVolumeInfoChanged((ParcelableVolumeInfo)object);
            return true;
        }

        private static class Proxy
        implements IMediaControllerCallback {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.support.v4.media.session.IMediaControllerCallback";
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onEvent(String string2, Bundle bundle) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                    parcel.writeString(string2);
                    if (bundle != null) {
                        parcel.writeInt(1);
                        bundle.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(1, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onExtrasChanged(Bundle bundle) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                    if (bundle != null) {
                        parcel.writeInt(1);
                        bundle.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(7, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                    if (mediaMetadataCompat != null) {
                        parcel.writeInt(1);
                        mediaMetadataCompat.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(4, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                    if (playbackStateCompat != null) {
                        parcel.writeInt(1);
                        playbackStateCompat.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(3, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> list) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                    parcel.writeTypedList(list);
                    this.mRemote.transact(5, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onQueueTitleChanged(CharSequence charSequence) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                    if (charSequence != null) {
                        parcel.writeInt(1);
                        TextUtils.writeToParcel((CharSequence)charSequence, (Parcel)parcel, (int)0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(6, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void onSessionDestroyed() throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                    this.mRemote.transact(2, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.session.IMediaControllerCallback");
                    if (parcelableVolumeInfo != null) {
                        parcel.writeInt(1);
                        parcelableVolumeInfo.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(8, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }
        }

    }

}

