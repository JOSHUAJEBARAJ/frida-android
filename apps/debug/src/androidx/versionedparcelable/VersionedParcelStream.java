/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcelable
 *  android.util.SparseArray
 */
package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.util.SparseArray;
import androidx.versionedparcelable.VersionedParcel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

@RestrictTo(value={RestrictTo.Scope.LIBRARY})
class VersionedParcelStream
extends VersionedParcel {
    private static final int TYPE_BOOLEAN = 5;
    private static final int TYPE_BOOLEAN_ARRAY = 6;
    private static final int TYPE_DOUBLE = 7;
    private static final int TYPE_DOUBLE_ARRAY = 8;
    private static final int TYPE_FLOAT = 13;
    private static final int TYPE_FLOAT_ARRAY = 14;
    private static final int TYPE_INT = 9;
    private static final int TYPE_INT_ARRAY = 10;
    private static final int TYPE_LONG = 11;
    private static final int TYPE_LONG_ARRAY = 12;
    private static final int TYPE_NULL = 0;
    private static final int TYPE_STRING = 3;
    private static final int TYPE_STRING_ARRAY = 4;
    private static final int TYPE_SUB_BUNDLE = 1;
    private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
    private static final Charset UTF_16 = Charset.forName("UTF-16");
    private final SparseArray<InputBuffer> mCachedFields = new SparseArray();
    private DataInputStream mCurrentInput;
    private DataOutputStream mCurrentOutput;
    private FieldBuffer mFieldBuffer;
    private boolean mIgnoreParcelables;
    private final DataInputStream mMasterInput;
    private final DataOutputStream mMasterOutput;

    public VersionedParcelStream(InputStream closeable, OutputStream outputStream) {
        Object var3_3 = null;
        closeable = closeable != null ? new DataInputStream((InputStream)closeable) : null;
        this.mMasterInput = closeable;
        closeable = var3_3;
        if (outputStream != null) {
            closeable = new DataOutputStream(outputStream);
        }
        this.mMasterOutput = closeable;
        this.mCurrentInput = this.mMasterInput;
        this.mCurrentOutput = this.mMasterOutput;
    }

    private void readObject(int n, String charSequence, Bundle bundle) {
        switch (n) {
            default: {
                charSequence = new StringBuilder();
                charSequence.append("Unknown type ");
                charSequence.append(n);
                throw new RuntimeException(charSequence.toString());
            }
            case 14: {
                bundle.putFloatArray(charSequence, this.readFloatArray());
                return;
            }
            case 13: {
                bundle.putFloat(charSequence, this.readFloat());
                return;
            }
            case 12: {
                bundle.putLongArray(charSequence, this.readLongArray());
                return;
            }
            case 11: {
                bundle.putLong(charSequence, this.readLong());
                return;
            }
            case 10: {
                bundle.putIntArray(charSequence, this.readIntArray());
                return;
            }
            case 9: {
                bundle.putInt(charSequence, this.readInt());
                return;
            }
            case 8: {
                bundle.putDoubleArray(charSequence, this.readDoubleArray());
                return;
            }
            case 7: {
                bundle.putDouble(charSequence, this.readDouble());
                return;
            }
            case 6: {
                bundle.putBooleanArray(charSequence, this.readBooleanArray());
                return;
            }
            case 5: {
                bundle.putBoolean(charSequence, this.readBoolean());
                return;
            }
            case 4: {
                bundle.putStringArray(charSequence, this.readArray(new String[0]));
                return;
            }
            case 3: {
                bundle.putString(charSequence, this.readString());
                return;
            }
            case 2: {
                bundle.putBundle(charSequence, this.readBundle());
                return;
            }
            case 1: {
                bundle.putBundle(charSequence, this.readBundle());
                return;
            }
            case 0: 
        }
        bundle.putParcelable(charSequence, null);
    }

    private void writeObject(Object object) {
        if (object == null) {
            this.writeInt(0);
            return;
        }
        if (object instanceof Bundle) {
            this.writeInt(1);
            this.writeBundle((Bundle)object);
            return;
        }
        if (object instanceof String) {
            this.writeInt(3);
            this.writeString((String)object);
            return;
        }
        if (object instanceof String[]) {
            this.writeInt(4);
            this.writeArray((String[])object);
            return;
        }
        if (object instanceof Boolean) {
            this.writeInt(5);
            this.writeBoolean((Boolean)object);
            return;
        }
        if (object instanceof boolean[]) {
            this.writeInt(6);
            this.writeBooleanArray((boolean[])object);
            return;
        }
        if (object instanceof Double) {
            this.writeInt(7);
            this.writeDouble((Double)object);
            return;
        }
        if (object instanceof double[]) {
            this.writeInt(8);
            this.writeDoubleArray((double[])object);
            return;
        }
        if (object instanceof Integer) {
            this.writeInt(9);
            this.writeInt((Integer)object);
            return;
        }
        if (object instanceof int[]) {
            this.writeInt(10);
            this.writeIntArray((int[])object);
            return;
        }
        if (object instanceof Long) {
            this.writeInt(11);
            this.writeLong((Long)object);
            return;
        }
        if (object instanceof long[]) {
            this.writeInt(12);
            this.writeLongArray((long[])object);
            return;
        }
        if (object instanceof Float) {
            this.writeInt(13);
            this.writeFloat(((Float)object).floatValue());
            return;
        }
        if (object instanceof float[]) {
            this.writeInt(14);
            this.writeFloatArray((float[])object);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported type ");
        stringBuilder.append(object.getClass());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Override
    public void closeField() {
        FieldBuffer fieldBuffer = this.mFieldBuffer;
        if (fieldBuffer != null) {
            try {
                if (fieldBuffer.mOutput.size() != 0) {
                    this.mFieldBuffer.flushField();
                }
                this.mFieldBuffer = null;
                return;
            }
            catch (IOException iOException) {
                throw new VersionedParcel.ParcelException(iOException);
            }
        }
    }

    @Override
    protected VersionedParcel createSubParcel() {
        return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput);
    }

    @Override
    public boolean isStream() {
        return true;
    }

    @Override
    public boolean readBoolean() {
        try {
            boolean bl = this.mCurrentInput.readBoolean();
            return bl;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public Bundle readBundle() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (int i = 0; i < n; ++i) {
            String string2 = this.readString();
            this.readObject(this.readInt(), string2, bundle);
        }
        return bundle;
    }

    @Override
    public byte[] readByteArray() {
        block3 : {
            int n;
            try {
                n = this.mCurrentInput.readInt();
                if (n <= 0) break block3;
            }
            catch (IOException iOException) {
                throw new VersionedParcel.ParcelException(iOException);
            }
            byte[] arrby = new byte[n];
            this.mCurrentInput.readFully(arrby);
            return arrby;
        }
        return null;
    }

    @Override
    public double readDouble() {
        try {
            double d = this.mCurrentInput.readDouble();
            return d;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean readField(int n) {
        InputBuffer inputBuffer = (InputBuffer)this.mCachedFields.get(n);
        if (inputBuffer != null) {
            this.mCachedFields.remove(n);
            this.mCurrentInput = inputBuffer.mInputStream;
            return true;
        }
        try {
            do {
                int n2;
                int n3 = this.mMasterInput.readInt();
                int n4 = n2 = n3 & 65535;
                if (n2 == 65535) {
                    n4 = this.mMasterInput.readInt();
                }
                inputBuffer = new InputBuffer(65535 & n3 >> 16, n4, this.mMasterInput);
                if (inputBuffer.mFieldId == n) {
                    this.mCurrentInput = inputBuffer.mInputStream;
                    return true;
                }
                this.mCachedFields.put(inputBuffer.mFieldId, (Object)inputBuffer);
            } while (true);
        }
        catch (IOException iOException) {
            return false;
        }
    }

    @Override
    public float readFloat() {
        try {
            float f = this.mCurrentInput.readFloat();
            return f;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public int readInt() {
        try {
            int n = this.mCurrentInput.readInt();
            return n;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public long readLong() {
        try {
            long l = this.mCurrentInput.readLong();
            return l;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public <T extends Parcelable> T readParcelable() {
        return null;
    }

    @Override
    public String readString() {
        block3 : {
            int n;
            try {
                n = this.mCurrentInput.readInt();
                if (n <= 0) break block3;
            }
            catch (IOException iOException) {
                throw new VersionedParcel.ParcelException(iOException);
            }
            Object object = new byte[n];
            this.mCurrentInput.readFully((byte[])object);
            object = new String((byte[])object, UTF_16);
            return object;
        }
        return null;
    }

    @Override
    public IBinder readStrongBinder() {
        return null;
    }

    @Override
    public void setOutputField(int n) {
        this.closeField();
        this.mFieldBuffer = new FieldBuffer(n, this.mMasterOutput);
        this.mCurrentOutput = this.mFieldBuffer.mDataStream;
    }

    @Override
    public void setSerializationFlags(boolean bl, boolean bl2) {
        if (bl) {
            this.mIgnoreParcelables = bl2;
            return;
        }
        throw new RuntimeException("Serialization of this object is not allowed");
    }

    @Override
    public void writeBoolean(boolean bl) {
        try {
            this.mCurrentOutput.writeBoolean(bl);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void writeBundle(Bundle var1_1) {
        if (var1_1 == null) ** GOTO lbl12
        try {
            var2_4 = var1_1.keySet();
            this.mCurrentOutput.writeInt(var2_4.size());
            var2_4 = var2_4.iterator();
            while (var2_4.hasNext() != false) {
                var3_5 = (String)var2_4.next();
                this.writeString(var3_5);
                this.writeObject(var1_1.get(var3_5));
            }
            return;
lbl12: // 1 sources:
            this.mCurrentOutput.writeInt(-1);
            return;
        }
        catch (IOException var1_2) {
            var1_3 = new VersionedParcel.ParcelException(var1_2);
            throw var1_3;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void writeByteArray(byte[] var1_1) {
        if (var1_1 == null) ** GOTO lbl6
        try {
            this.mCurrentOutput.writeInt(var1_1.length);
            this.mCurrentOutput.write(var1_1);
            return;
lbl6: // 1 sources:
            this.mCurrentOutput.writeInt(-1);
            return;
        }
        catch (IOException var1_2) {
            throw new VersionedParcel.ParcelException(var1_2);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void writeByteArray(byte[] var1_1, int var2_3, int var3_4) {
        if (var1_1 == null) ** GOTO lbl6
        try {
            this.mCurrentOutput.writeInt(var3_4);
            this.mCurrentOutput.write(var1_1, var2_3, var3_4);
            return;
lbl6: // 1 sources:
            this.mCurrentOutput.writeInt(-1);
            return;
        }
        catch (IOException var1_2) {
            throw new VersionedParcel.ParcelException(var1_2);
        }
    }

    @Override
    public void writeDouble(double d) {
        try {
            this.mCurrentOutput.writeDouble(d);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public void writeFloat(float f) {
        try {
            this.mCurrentOutput.writeFloat(f);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public void writeInt(int n) {
        try {
            this.mCurrentOutput.writeInt(n);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public void writeLong(long l) {
        try {
            this.mCurrentOutput.writeLong(l);
            return;
        }
        catch (IOException iOException) {
            throw new VersionedParcel.ParcelException(iOException);
        }
    }

    @Override
    public void writeParcelable(Parcelable parcelable) {
        if (this.mIgnoreParcelables) {
            return;
        }
        throw new RuntimeException("Parcelables cannot be written to an OutputStream");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void writeString(String var1_1) {
        if (var1_1 == null) ** GOTO lbl7
        try {
            var1_1 = var1_1.getBytes(VersionedParcelStream.UTF_16);
            this.mCurrentOutput.writeInt(var1_1.length);
            this.mCurrentOutput.write(var1_1);
            return;
lbl7: // 1 sources:
            this.mCurrentOutput.writeInt(-1);
            return;
        }
        catch (IOException var1_2) {
            throw new VersionedParcel.ParcelException(var1_2);
        }
    }

    @Override
    public void writeStrongBinder(IBinder iBinder) {
        if (this.mIgnoreParcelables) {
            return;
        }
        throw new RuntimeException("Binders cannot be written to an OutputStream");
    }

    @Override
    public void writeStrongInterface(IInterface iInterface) {
        if (this.mIgnoreParcelables) {
            return;
        }
        throw new RuntimeException("Binders cannot be written to an OutputStream");
    }

    private static class FieldBuffer {
        final DataOutputStream mDataStream;
        private final int mFieldId;
        final ByteArrayOutputStream mOutput = new ByteArrayOutputStream();
        private final DataOutputStream mTarget;

        FieldBuffer(int n, DataOutputStream dataOutputStream) {
            this.mDataStream = new DataOutputStream(this.mOutput);
            this.mFieldId = n;
            this.mTarget = dataOutputStream;
        }

        void flushField() throws IOException {
            this.mDataStream.flush();
            int n = this.mOutput.size();
            int n2 = this.mFieldId;
            int n3 = n >= 65535 ? 65535 : n;
            this.mTarget.writeInt(n2 << 16 | n3);
            if (n >= 65535) {
                this.mTarget.writeInt(n);
            }
            this.mOutput.writeTo(this.mTarget);
        }
    }

    private static class InputBuffer {
        final int mFieldId;
        final DataInputStream mInputStream;
        private final int mSize;

        InputBuffer(int n, int n2, DataInputStream dataInputStream) throws IOException {
            this.mSize = n2;
            this.mFieldId = n;
            byte[] arrby = new byte[this.mSize];
            dataInputStream.readFully(arrby);
            this.mInputStream = new DataInputStream(new ByteArrayInputStream(arrby));
        }
    }

}

