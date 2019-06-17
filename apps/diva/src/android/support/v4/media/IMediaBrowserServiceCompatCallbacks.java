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
 */
package android.support.v4.media;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.media.session.MediaSessionCompat;
import java.util.ArrayList;
import java.util.List;

public interface IMediaBrowserServiceCompatCallbacks
extends IInterface {
    public void onConnect(String var1, MediaSessionCompat.Token var2, Bundle var3) throws RemoteException;

    public void onConnectFailed() throws RemoteException;

    public void onLoadChildren(String var1, List var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IMediaBrowserServiceCompatCallbacks {
        private static final String DESCRIPTOR = "android.support.v4.media.IMediaBrowserServiceCompatCallbacks";
        static final int TRANSACTION_onConnect = 1;
        static final int TRANSACTION_onConnectFailed = 2;
        static final int TRANSACTION_onLoadChildren = 3;

        public Stub() {
            this.attachInterface((IInterface)this, "android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
        }

        public static IMediaBrowserServiceCompatCallbacks asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
            if (iInterface != null && iInterface instanceof IMediaBrowserServiceCompatCallbacks) {
                return (IMediaBrowserServiceCompatCallbacks)iInterface;
            }
            return new Proxy(iBinder);
        }

        public IBinder asBinder() {
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onTransact(int n, Parcel parcel, Parcel object, int n2) throws RemoteException {
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, (Parcel)object, n2);
                }
                case 1598968902: {
                    object.writeString("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
                    String string2 = parcel.readString();
                    object = parcel.readInt() != 0 ? (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel(parcel) : null;
                    parcel = parcel.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel(parcel) : null;
                    this.onConnect(string2, (MediaSessionCompat.Token)object, (Bundle)parcel);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
                    this.onConnectFailed();
                    return true;
                }
                case 3: 
            }
            parcel.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
            this.onLoadChildren(parcel.readString(), (List)parcel.readArrayList(this.getClass().getClassLoader()));
            return true;
        }

        private static class Proxy
        implements IMediaBrowserServiceCompatCallbacks {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.support.v4.media.IMediaBrowserServiceCompatCallbacks";
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onConnect(String string2, MediaSessionCompat.Token token, Bundle bundle) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
                    parcel.writeString(string2);
                    if (token != null) {
                        parcel.writeInt(1);
                        token.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
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

            @Override
            public void onConnectFailed() throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
                    this.mRemote.transact(2, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void onLoadChildren(String string2, List list) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
                    parcel.writeString(string2);
                    parcel.writeList(list);
                    this.mRemote.transact(3, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }
        }

    }

}

