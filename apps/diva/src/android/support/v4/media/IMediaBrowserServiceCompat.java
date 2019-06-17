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
import android.support.v4.media.IMediaBrowserServiceCompatCallbacks;
import android.support.v4.os.ResultReceiver;

public interface IMediaBrowserServiceCompat
extends IInterface {
    public void addSubscription(String var1, IMediaBrowserServiceCompatCallbacks var2) throws RemoteException;

    public void connect(String var1, Bundle var2, IMediaBrowserServiceCompatCallbacks var3) throws RemoteException;

    public void disconnect(IMediaBrowserServiceCompatCallbacks var1) throws RemoteException;

    public void getMediaItem(String var1, ResultReceiver var2) throws RemoteException;

    public void removeSubscription(String var1, IMediaBrowserServiceCompatCallbacks var2) throws RemoteException;

    public static abstract class Stub
    extends Binder
    implements IMediaBrowserServiceCompat {
        private static final String DESCRIPTOR = "android.support.v4.media.IMediaBrowserServiceCompat";
        static final int TRANSACTION_addSubscription = 3;
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_disconnect = 2;
        static final int TRANSACTION_getMediaItem = 5;
        static final int TRANSACTION_removeSubscription = 4;

        public Stub() {
            this.attachInterface((IInterface)this, "android.support.v4.media.IMediaBrowserServiceCompat");
        }

        public static IMediaBrowserServiceCompat asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("android.support.v4.media.IMediaBrowserServiceCompat");
            if (iInterface != null && iInterface instanceof IMediaBrowserServiceCompat) {
                return (IMediaBrowserServiceCompat)iInterface;
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
                    object2.writeString("android.support.v4.media.IMediaBrowserServiceCompat");
                    return true;
                }
                case 1: {
                    object.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
                    String string2 = object.readString();
                    object2 = object.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel((Parcel)object) : null;
                    this.connect(string2, (Bundle)object2, IMediaBrowserServiceCompatCallbacks.Stub.asInterface(object.readStrongBinder()));
                    return true;
                }
                case 2: {
                    object.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
                    this.disconnect(IMediaBrowserServiceCompatCallbacks.Stub.asInterface(object.readStrongBinder()));
                    return true;
                }
                case 3: {
                    object.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
                    this.addSubscription(object.readString(), IMediaBrowserServiceCompatCallbacks.Stub.asInterface(object.readStrongBinder()));
                    return true;
                }
                case 4: {
                    object.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
                    this.removeSubscription(object.readString(), IMediaBrowserServiceCompatCallbacks.Stub.asInterface(object.readStrongBinder()));
                    return true;
                }
                case 5: 
            }
            object.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
            object2 = object.readString();
            object = object.readInt() != 0 ? (ResultReceiver)ResultReceiver.CREATOR.createFromParcel((Parcel)object) : null;
            this.getMediaItem((String)object2, (ResultReceiver)object);
            return true;
        }

        private static class Proxy
        implements IMediaBrowserServiceCompat {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void addSubscription(String string2, IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) throws RemoteException {
                Object var3_4 = null;
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
                    parcel.writeString(string2);
                    string2 = var3_4;
                    if (iMediaBrowserServiceCompatCallbacks != null) {
                        string2 = iMediaBrowserServiceCompatCallbacks.asBinder();
                    }
                    parcel.writeStrongBinder((IBinder)string2);
                    this.mRemote.transact(3, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void connect(String string2, Bundle bundle, IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) throws RemoteException {
                Object var4_5 = null;
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
                    parcel.writeString(string2);
                    if (bundle != null) {
                        parcel.writeInt(1);
                        bundle.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    string2 = var4_5;
                    if (iMediaBrowserServiceCompatCallbacks != null) {
                        string2 = iMediaBrowserServiceCompatCallbacks.asBinder();
                    }
                    parcel.writeStrongBinder((IBinder)string2);
                    this.mRemote.transact(1, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void disconnect(IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) throws RemoteException {
                IBinder iBinder = null;
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
                    if (iMediaBrowserServiceCompatCallbacks != null) {
                        iBinder = iMediaBrowserServiceCompatCallbacks.asBinder();
                    }
                    parcel.writeStrongBinder(iBinder);
                    this.mRemote.transact(2, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return "android.support.v4.media.IMediaBrowserServiceCompat";
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void getMediaItem(String string2, ResultReceiver resultReceiver) throws RemoteException {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
                    parcel.writeString(string2);
                    if (resultReceiver != null) {
                        parcel.writeInt(1);
                        resultReceiver.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
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
            public void removeSubscription(String string2, IMediaBrowserServiceCompatCallbacks iMediaBrowserServiceCompatCallbacks) throws RemoteException {
                Object var3_4 = null;
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
                    parcel.writeString(string2);
                    string2 = var3_4;
                    if (iMediaBrowserServiceCompatCallbacks != null) {
                        string2 = iMediaBrowserServiceCompatCallbacks.asBinder();
                    }
                    parcel.writeStrongBinder((IBinder)string2);
                    this.mRemote.transact(4, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }
        }

    }

}

