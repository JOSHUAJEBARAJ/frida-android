/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.util.SparseIntArray
 */
package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.SparseIntArray;
import androidx.versionedparcelable.VersionedParcel;

@RestrictTo(value={RestrictTo.Scope.LIBRARY})
class VersionedParcelParcel
extends VersionedParcel {
    private static final boolean DEBUG = false;
    private static final String TAG = "VersionedParcelParcel";
    private int mCurrentField = -1;
    private final int mEnd;
    private int mNextRead = 0;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup = new SparseIntArray();
    private final String mPrefix;

    VersionedParcelParcel(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "");
    }

    VersionedParcelParcel(Parcel parcel, int n, int n2, String string2) {
        this.mParcel = parcel;
        this.mOffset = n;
        this.mEnd = n2;
        this.mNextRead = this.mOffset;
        this.mPrefix = string2;
    }

    private int readUntilField(int n) {
        int n2;
        while ((n2 = this.mNextRead) < this.mEnd) {
            this.mParcel.setDataPosition(n2);
            n2 = this.mParcel.readInt();
            int n3 = this.mParcel.readInt();
            this.mNextRead += n2;
            if (n3 != n) continue;
            return this.mParcel.dataPosition();
        }
        return -1;
    }

    @Override
    public void closeField() {
        int n = this.mCurrentField;
        if (n >= 0) {
            n = this.mPositionLookup.get(n);
            int n2 = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(n);
            this.mParcel.writeInt(n2 - n);
            this.mParcel.setDataPosition(n2);
        }
    }

    @Override
    protected VersionedParcel createSubParcel() {
        int n;
        Parcel parcel = this.mParcel;
        int n2 = parcel.dataPosition();
        int n3 = n = this.mNextRead;
        if (n == this.mOffset) {
            n3 = this.mEnd;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mPrefix);
        stringBuilder.append("  ");
        return new VersionedParcelParcel(parcel, n2, n3, stringBuilder.toString());
    }

    @Override
    public boolean readBoolean() {
        if (this.mParcel.readInt() != 0) {
            return true;
        }
        return false;
    }

    @Override
    public Bundle readBundle() {
        return this.mParcel.readBundle(this.getClass().getClassLoader());
    }

    @Override
    public byte[] readByteArray() {
        int n = this.mParcel.readInt();
        if (n < 0) {
            return null;
        }
        byte[] arrby = new byte[n];
        this.mParcel.readByteArray(arrby);
        return arrby;
    }

    @Override
    public double readDouble() {
        return this.mParcel.readDouble();
    }

    @Override
    public boolean readField(int n) {
        if ((n = this.readUntilField(n)) == -1) {
            return false;
        }
        this.mParcel.setDataPosition(n);
        return true;
    }

    @Override
    public float readFloat() {
        return this.mParcel.readFloat();
    }

    @Override
    public int readInt() {
        return this.mParcel.readInt();
    }

    @Override
    public long readLong() {
        return this.mParcel.readLong();
    }

    @Override
    public <T extends Parcelable> T readParcelable() {
        return (T)this.mParcel.readParcelable(this.getClass().getClassLoader());
    }

    @Override
    public String readString() {
        return this.mParcel.readString();
    }

    @Override
    public IBinder readStrongBinder() {
        return this.mParcel.readStrongBinder();
    }

    @Override
    public void setOutputField(int n) {
        this.closeField();
        this.mCurrentField = n;
        this.mPositionLookup.put(n, this.mParcel.dataPosition());
        this.writeInt(0);
        this.writeInt(n);
    }

    @Override
    public void writeBoolean(boolean bl) {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

    @Override
    public void writeBundle(Bundle bundle) {
        this.mParcel.writeBundle(bundle);
    }

    @Override
    public void writeByteArray(byte[] arrby) {
        if (arrby != null) {
            this.mParcel.writeInt(arrby.length);
            this.mParcel.writeByteArray(arrby);
            return;
        }
        this.mParcel.writeInt(-1);
    }

    @Override
    public void writeByteArray(byte[] arrby, int n, int n2) {
        if (arrby != null) {
            this.mParcel.writeInt(arrby.length);
            this.mParcel.writeByteArray(arrby, n, n2);
            return;
        }
        this.mParcel.writeInt(-1);
    }

    @Override
    public void writeDouble(double d) {
        this.mParcel.writeDouble(d);
    }

    @Override
    public void writeFloat(float f) {
        this.mParcel.writeFloat(f);
    }

    @Override
    public void writeInt(int n) {
        this.mParcel.writeInt(n);
    }

    @Override
    public void writeLong(long l) {
        this.mParcel.writeLong(l);
    }

    @Override
    public void writeParcelable(Parcelable parcelable) {
        this.mParcel.writeParcelable(parcelable, 0);
    }

    @Override
    public void writeString(String string2) {
        this.mParcel.writeString(string2);
    }

    @Override
    public void writeStrongBinder(IBinder iBinder) {
        this.mParcel.writeStrongBinder(iBinder);
    }

    @Override
    public void writeStrongInterface(IInterface iInterface) {
        this.mParcel.writeStrongInterface(iInterface);
    }
}

