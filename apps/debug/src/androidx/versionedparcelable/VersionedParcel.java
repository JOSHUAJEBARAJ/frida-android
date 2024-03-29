/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.BadParcelableException
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.NetworkOnMainThreadException
 *  android.os.Parcelable
 *  android.util.Size
 *  android.util.SizeF
 *  android.util.SparseBooleanArray
 */
package androidx.versionedparcelable;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArraySet;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseBooleanArray;
import androidx.versionedparcelable.VersionedParcelable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public abstract class VersionedParcel {
    private static final int EX_BAD_PARCELABLE = -2;
    private static final int EX_ILLEGAL_ARGUMENT = -3;
    private static final int EX_ILLEGAL_STATE = -5;
    private static final int EX_NETWORK_MAIN_THREAD = -6;
    private static final int EX_NULL_POINTER = -4;
    private static final int EX_PARCELABLE = -9;
    private static final int EX_SECURITY = -1;
    private static final int EX_UNSUPPORTED_OPERATION = -7;
    private static final String TAG = "VersionedParcel";
    private static final int TYPE_BINDER = 5;
    private static final int TYPE_PARCELABLE = 2;
    private static final int TYPE_SERIALIZABLE = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_VERSIONED_PARCELABLE = 1;

    private Exception createException(int n, String string2) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown exception code: ");
                stringBuilder.append(n);
                stringBuilder.append(" msg ");
                stringBuilder.append(string2);
                return new RuntimeException(stringBuilder.toString());
            }
            case -1: {
                return new SecurityException(string2);
            }
            case -2: {
                return new BadParcelableException(string2);
            }
            case -3: {
                return new IllegalArgumentException(string2);
            }
            case -4: {
                return new NullPointerException(string2);
            }
            case -5: {
                return new IllegalStateException(string2);
            }
            case -6: {
                return new NetworkOnMainThreadException();
            }
            case -7: {
                return new UnsupportedOperationException(string2);
            }
            case -9: 
        }
        return (Exception)this.readParcelable();
    }

    private static <T extends VersionedParcelable> Class findParcelClass(T t) throws ClassNotFoundException {
        return VersionedParcel.findParcelClass(t.getClass());
    }

    private static Class findParcelClass(Class<? extends VersionedParcelable> class_) throws ClassNotFoundException {
        return Class.forName(String.format("%s.%sParcelizer", class_.getPackage().getName(), class_.getSimpleName()), false, class_.getClassLoader());
    }

    @NonNull
    protected static Throwable getRootCause(@NonNull Throwable throwable) {
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return throwable;
    }

    private <T> int getType(T t) {
        if (t instanceof String) {
            return 4;
        }
        if (t instanceof Parcelable) {
            return 2;
        }
        if (t instanceof VersionedParcelable) {
            return 1;
        }
        if (t instanceof Serializable) {
            return 3;
        }
        if (t instanceof IBinder) {
            return 5;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(t.getClass().getName());
        stringBuilder.append(" cannot be VersionedParcelled");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private <T, S extends Collection<T>> S readCollection(int n, S s) {
        int n2;
        n = this.readInt();
        if (n < 0) {
            return null;
        }
        if (n == 0) return s;
        int n3 = this.readInt();
        if (n < 0) {
            return null;
        }
        if (n3 != 1) {
            if (n3 != 2) {
                if (n3 != 3) {
                    if (n3 != 4) {
                        if (n3 != 5) {
                            return s;
                        }
                        while (n > 0) {
                            s.add((IBinder)this.readStrongBinder());
                            --n;
                        }
                        return s;
                    } else {
                        for (n2 = n; n2 > 0; --n2) {
                            s.add((String)this.readString());
                        }
                    }
                    return s;
                } else {
                    for (n2 = n; n2 > 0; --n2) {
                        s.add((Serializable)this.readSerializable());
                    }
                }
                return s;
            } else {
                for (n2 = n; n2 > 0; --n2) {
                    s.add(this.readParcelable());
                }
            }
            return s;
        } else {
            for (n2 = n; n2 > 0; --n2) {
                s.add(this.readVersionedParcelable());
            }
        }
        return s;
    }

    private Exception readException(int n, String string2) {
        return this.createException(n, string2);
    }

    private int readExceptionCode() {
        return this.readInt();
    }

    protected static <T extends VersionedParcelable> T readFromParcel(String object, VersionedParcel versionedParcel) {
        try {
            object = (VersionedParcelable)Class.forName((String)object, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class).invoke(null, versionedParcel);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", classNotFoundException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", noSuchMethodException);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getCause() instanceof RuntimeException) {
                throw (RuntimeException)invocationTargetException.getCause();
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", invocationTargetException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", illegalAccessException);
        }
        return (T)object;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private <T> void writeCollection(Collection<T> iterator, int n) {
        this.setOutputField(n);
        if (iterator == null) {
            this.writeInt(-1);
            return;
        }
        n = iterator.size();
        this.writeInt(n);
        if (n <= 0) return;
        n = this.getType(iterator.iterator().next());
        this.writeInt(n);
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    if (n != 4) {
                        if (n != 5) {
                            return;
                        }
                        iterator = iterator.iterator();
                        while (iterator.hasNext()) {
                            this.writeStrongBinder((IBinder)iterator.next());
                        }
                        return;
                    }
                    iterator = iterator.iterator();
                    while (iterator.hasNext()) {
                        this.writeString((String)iterator.next());
                    }
                    return;
                }
                iterator = iterator.iterator();
                while (iterator.hasNext()) {
                    this.writeSerializable((Serializable)iterator.next());
                }
                return;
            }
            iterator = iterator.iterator();
            while (iterator.hasNext()) {
                this.writeParcelable((Parcelable)iterator.next());
            }
            return;
        }
        iterator = iterator.iterator();
        while (iterator.hasNext()) {
            this.writeVersionedParcelable((VersionedParcelable)iterator.next());
        }
    }

    private void writeSerializable(Serializable serializable) {
        if (serializable == null) {
            this.writeString(null);
            return;
        }
        String string2 = serializable.getClass().getName();
        this.writeString(string2);
        Object object = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream((OutputStream)object);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            this.writeByteArray(object.toByteArray());
            return;
        }
        catch (IOException iOException) {
            object = new StringBuilder();
            object.append("VersionedParcelable encountered IOException writing serializable object (name = ");
            object.append(string2);
            object.append(")");
            throw new RuntimeException(object.toString(), iOException);
        }
    }

    protected static <T extends VersionedParcelable> void writeToParcel(T t, VersionedParcel versionedParcel) {
        try {
            VersionedParcel.findParcelClass(t).getDeclaredMethod("write", t.getClass(), VersionedParcel.class).invoke(null, t, versionedParcel);
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", classNotFoundException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", noSuchMethodException);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getCause() instanceof RuntimeException) {
                throw (RuntimeException)invocationTargetException.getCause();
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", invocationTargetException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", illegalAccessException);
        }
    }

    private void writeVersionedParcelableCreator(VersionedParcelable versionedParcelable) {
        try {
            Class class_ = VersionedParcel.findParcelClass(versionedParcelable.getClass());
            this.writeString(class_.getName());
            return;
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(versionedParcelable.getClass().getSimpleName());
            stringBuilder.append(" does not have a Parcelizer");
            throw new RuntimeException(stringBuilder.toString(), classNotFoundException);
        }
    }

    protected abstract void closeField();

    protected abstract VersionedParcel createSubParcel();

    public boolean isStream() {
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected <T> T[] readArray(T[] arrT) {
        int n;
        int n2 = this.readInt();
        if (n2 < 0) {
            return null;
        }
        ArrayList<Object> arrayList = new ArrayList<Object>(n2);
        if (n2 == 0) return arrayList.toArray(arrT);
        int n3 = this.readInt();
        if (n2 < 0) {
            return null;
        }
        if (n3 != 1) {
            if (n3 != 2) {
                if (n3 != 3) {
                    if (n3 != 4) {
                        if (n3 != 5) return arrayList.toArray(arrT);
                        while (n2 > 0) {
                            arrayList.add((Object)this.readStrongBinder());
                            --n2;
                        }
                        return arrayList.toArray(arrT);
                    } else {
                        for (n = n2; n > 0; --n) {
                            arrayList.add(this.readString());
                        }
                    }
                    return arrayList.toArray(arrT);
                } else {
                    for (n = n2; n > 0; --n) {
                        arrayList.add(this.readSerializable());
                    }
                }
                return arrayList.toArray(arrT);
            } else {
                for (n = n2; n > 0; --n) {
                    arrayList.add(this.readParcelable());
                }
            }
            return arrayList.toArray(arrT);
        } else {
            for (n = n2; n > 0; --n) {
                arrayList.add(this.readVersionedParcelable());
            }
        }
        return arrayList.toArray(arrT);
    }

    public <T> T[] readArray(T[] arrT, int n) {
        if (!this.readField(n)) {
            return arrT;
        }
        return this.readArray(arrT);
    }

    protected abstract boolean readBoolean();

    public boolean readBoolean(boolean bl, int n) {
        if (!this.readField(n)) {
            return bl;
        }
        return this.readBoolean();
    }

    protected boolean[] readBooleanArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        boolean[] arrbl = new boolean[n];
        for (int i = 0; i < n; ++i) {
            boolean bl = this.readInt() != 0;
            arrbl[i] = bl;
        }
        return arrbl;
    }

    public boolean[] readBooleanArray(boolean[] arrbl, int n) {
        if (!this.readField(n)) {
            return arrbl;
        }
        return this.readBooleanArray();
    }

    protected abstract Bundle readBundle();

    public Bundle readBundle(Bundle bundle, int n) {
        if (!this.readField(n)) {
            return bundle;
        }
        return this.readBundle();
    }

    public byte readByte(byte by, int n) {
        if (!this.readField(n)) {
            return by;
        }
        return (byte)(this.readInt() & 255);
    }

    protected abstract byte[] readByteArray();

    public byte[] readByteArray(byte[] arrby, int n) {
        if (!this.readField(n)) {
            return arrby;
        }
        return this.readByteArray();
    }

    public char[] readCharArray(char[] arrc, int n) {
        if (!this.readField(n)) {
            return arrc;
        }
        int n2 = this.readInt();
        if (n2 < 0) {
            return null;
        }
        arrc = new char[n2];
        for (n = 0; n < n2; ++n) {
            arrc[n] = (char)this.readInt();
        }
        return arrc;
    }

    protected abstract double readDouble();

    public double readDouble(double d, int n) {
        if (!this.readField(n)) {
            return d;
        }
        return this.readDouble();
    }

    protected double[] readDoubleArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        double[] arrd = new double[n];
        for (int i = 0; i < n; ++i) {
            arrd[i] = this.readDouble();
        }
        return arrd;
    }

    public double[] readDoubleArray(double[] arrd, int n) {
        if (!this.readField(n)) {
            return arrd;
        }
        return this.readDoubleArray();
    }

    public Exception readException(Exception exception, int n) {
        if (!this.readField(n)) {
            return exception;
        }
        n = this.readExceptionCode();
        if (n != 0) {
            return this.readException(n, this.readString());
        }
        return exception;
    }

    protected abstract boolean readField(int var1);

    protected abstract float readFloat();

    public float readFloat(float f, int n) {
        if (!this.readField(n)) {
            return f;
        }
        return this.readFloat();
    }

    protected float[] readFloatArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        float[] arrf = new float[n];
        for (int i = 0; i < n; ++i) {
            arrf[i] = this.readFloat();
        }
        return arrf;
    }

    public float[] readFloatArray(float[] arrf, int n) {
        if (!this.readField(n)) {
            return arrf;
        }
        return this.readFloatArray();
    }

    protected abstract int readInt();

    public int readInt(int n, int n2) {
        if (!this.readField(n2)) {
            return n;
        }
        return this.readInt();
    }

    protected int[] readIntArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        int[] arrn = new int[n];
        for (int i = 0; i < n; ++i) {
            arrn[i] = this.readInt();
        }
        return arrn;
    }

    public int[] readIntArray(int[] arrn, int n) {
        if (!this.readField(n)) {
            return arrn;
        }
        return this.readIntArray();
    }

    public <T> List<T> readList(List<T> list, int n) {
        if (!this.readField(n)) {
            return list;
        }
        return this.readCollection(n, new ArrayList<E>());
    }

    protected abstract long readLong();

    public long readLong(long l, int n) {
        if (!this.readField(n)) {
            return l;
        }
        return this.readLong();
    }

    protected long[] readLongArray() {
        int n = this.readInt();
        if (n < 0) {
            return null;
        }
        long[] arrl = new long[n];
        for (int i = 0; i < n; ++i) {
            arrl[i] = this.readLong();
        }
        return arrl;
    }

    public long[] readLongArray(long[] arrl, int n) {
        if (!this.readField(n)) {
            return arrl;
        }
        return this.readLongArray();
    }

    protected abstract <T extends Parcelable> T readParcelable();

    public <T extends Parcelable> T readParcelable(T t, int n) {
        if (!this.readField(n)) {
            return t;
        }
        return this.readParcelable();
    }

    protected Serializable readSerializable() {
        String string2 = this.readString();
        if (string2 == null) {
            return null;
        }
        Object object = new ByteArrayInputStream(this.readByteArray());
        try {
            object = (Serializable)new ObjectInputStream((InputStream)object){

                @Override
                protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                    Class class_ = Class.forName(objectStreamClass.getName(), false, this.getClass().getClassLoader());
                    if (class_ != null) {
                        return class_;
                    }
                    return super.resolveClass(objectStreamClass);
                }
            }.readObject();
            return object;
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = ");
            stringBuilder.append(string2);
            stringBuilder.append(")");
            throw new RuntimeException(stringBuilder.toString(), classNotFoundException);
        }
        catch (IOException iOException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("VersionedParcelable encountered IOException reading a Serializable object (name = ");
            stringBuilder.append(string2);
            stringBuilder.append(")");
            throw new RuntimeException(stringBuilder.toString(), iOException);
        }
    }

    public <T> Set<T> readSet(Set<T> set, int n) {
        if (!this.readField(n)) {
            return set;
        }
        return this.readCollection(n, new ArraySet<E>());
    }

    @RequiresApi(api=21)
    public Size readSize(Size size, int n) {
        if (!this.readField(n)) {
            return size;
        }
        if (this.readBoolean()) {
            return new Size(this.readInt(), this.readInt());
        }
        return null;
    }

    @RequiresApi(api=21)
    public SizeF readSizeF(SizeF sizeF, int n) {
        if (!this.readField(n)) {
            return sizeF;
        }
        if (this.readBoolean()) {
            return new SizeF(this.readFloat(), this.readFloat());
        }
        return null;
    }

    public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int n) {
        if (!this.readField(n)) {
            return sparseBooleanArray;
        }
        int n2 = this.readInt();
        if (n2 < 0) {
            return null;
        }
        sparseBooleanArray = new SparseBooleanArray(n2);
        for (n = 0; n < n2; ++n) {
            sparseBooleanArray.put(this.readInt(), this.readBoolean());
        }
        return sparseBooleanArray;
    }

    protected abstract String readString();

    public String readString(String string2, int n) {
        if (!this.readField(n)) {
            return string2;
        }
        return this.readString();
    }

    protected abstract IBinder readStrongBinder();

    public IBinder readStrongBinder(IBinder iBinder, int n) {
        if (!this.readField(n)) {
            return iBinder;
        }
        return this.readStrongBinder();
    }

    protected <T extends VersionedParcelable> T readVersionedParcelable() {
        String string2 = this.readString();
        if (string2 == null) {
            return null;
        }
        return VersionedParcel.readFromParcel(string2, this.createSubParcel());
    }

    public <T extends VersionedParcelable> T readVersionedParcelable(T t, int n) {
        if (!this.readField(n)) {
            return t;
        }
        return this.readVersionedParcelable();
    }

    protected abstract void setOutputField(int var1);

    public void setSerializationFlags(boolean bl, boolean bl2) {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected <T> void writeArray(T[] arrT) {
        int n;
        if (arrT == null) {
            this.writeInt(-1);
            return;
        }
        int n2 = arrT.length;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        this.writeInt(n2);
        if (n2 <= 0) return;
        int n7 = this.getType(arrT[0]);
        this.writeInt(n7);
        if (n7 != 1) {
            if (n7 != 2) {
                if (n7 != 3) {
                    if (n7 != 4) {
                        if (n7 != 5) {
                            return;
                        }
                        for (n = n6; n < n2; ++n) {
                            this.writeStrongBinder((IBinder)arrT[n]);
                        }
                        return;
                    } else {
                        for (n = n3; n < n2; ++n) {
                            this.writeString((String)arrT[n]);
                        }
                    }
                    return;
                } else {
                    for (n = n4; n < n2; ++n) {
                        this.writeSerializable((Serializable)arrT[n]);
                    }
                }
                return;
            } else {
                for (n = n5; n < n2; ++n) {
                    this.writeParcelable((Parcelable)arrT[n]);
                }
            }
            return;
        } else {
            for (n = 0; n < n2; ++n) {
                this.writeVersionedParcelable((VersionedParcelable)arrT[n]);
            }
        }
    }

    public <T> void writeArray(T[] arrT, int n) {
        this.setOutputField(n);
        this.writeArray(arrT);
    }

    protected abstract void writeBoolean(boolean var1);

    public void writeBoolean(boolean bl, int n) {
        this.setOutputField(n);
        this.writeBoolean(bl);
    }

    protected void writeBooleanArray(boolean[] arrbl) {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:553)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

    public void writeBooleanArray(boolean[] arrbl, int n) {
        this.setOutputField(n);
        this.writeBooleanArray(arrbl);
    }

    protected abstract void writeBundle(Bundle var1);

    public void writeBundle(Bundle bundle, int n) {
        this.setOutputField(n);
        this.writeBundle(bundle);
    }

    public void writeByte(byte by, int n) {
        this.setOutputField(n);
        this.writeInt(by);
    }

    protected abstract void writeByteArray(byte[] var1);

    public void writeByteArray(byte[] arrby, int n) {
        this.setOutputField(n);
        this.writeByteArray(arrby);
    }

    protected abstract void writeByteArray(byte[] var1, int var2, int var3);

    public void writeByteArray(byte[] arrby, int n, int n2, int n3) {
        this.setOutputField(n3);
        this.writeByteArray(arrby, n, n2);
    }

    public void writeCharArray(char[] arrc, int n) {
        this.setOutputField(n);
        if (arrc != null) {
            int n2 = arrc.length;
            this.writeInt(n2);
            for (n = 0; n < n2; ++n) {
                this.writeInt(arrc[n]);
            }
            return;
        }
        this.writeInt(-1);
    }

    protected abstract void writeDouble(double var1);

    public void writeDouble(double d, int n) {
        this.setOutputField(n);
        this.writeDouble(d);
    }

    protected void writeDoubleArray(double[] arrd) {
        if (arrd != null) {
            int n = arrd.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeDouble(arrd[i]);
            }
            return;
        }
        this.writeInt(-1);
    }

    public void writeDoubleArray(double[] arrd, int n) {
        this.setOutputField(n);
        this.writeDoubleArray(arrd);
    }

    public void writeException(Exception exception, int n) {
        this.setOutputField(n);
        if (exception == null) {
            this.writeNoException();
            return;
        }
        n = 0;
        if (exception instanceof Parcelable && exception.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
            n = -9;
        } else if (exception instanceof SecurityException) {
            n = -1;
        } else if (exception instanceof BadParcelableException) {
            n = -2;
        } else if (exception instanceof IllegalArgumentException) {
            n = -3;
        } else if (exception instanceof NullPointerException) {
            n = -4;
        } else if (exception instanceof IllegalStateException) {
            n = -5;
        } else if (exception instanceof NetworkOnMainThreadException) {
            n = -6;
        } else if (exception instanceof UnsupportedOperationException) {
            n = -7;
        }
        this.writeInt(n);
        if (n == 0) {
            if (exception instanceof RuntimeException) {
                throw (RuntimeException)exception;
            }
            throw new RuntimeException(exception);
        }
        this.writeString(exception.getMessage());
        if (n != -9) {
            return;
        }
        this.writeParcelable((Parcelable)exception);
    }

    protected abstract void writeFloat(float var1);

    public void writeFloat(float f, int n) {
        this.setOutputField(n);
        this.writeFloat(f);
    }

    protected void writeFloatArray(float[] arrf) {
        if (arrf != null) {
            int n = arrf.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeFloat(arrf[i]);
            }
            return;
        }
        this.writeInt(-1);
    }

    public void writeFloatArray(float[] arrf, int n) {
        this.setOutputField(n);
        this.writeFloatArray(arrf);
    }

    protected abstract void writeInt(int var1);

    public void writeInt(int n, int n2) {
        this.setOutputField(n2);
        this.writeInt(n);
    }

    protected void writeIntArray(int[] arrn) {
        if (arrn != null) {
            int n = arrn.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeInt(arrn[i]);
            }
            return;
        }
        this.writeInt(-1);
    }

    public void writeIntArray(int[] arrn, int n) {
        this.setOutputField(n);
        this.writeIntArray(arrn);
    }

    public <T> void writeList(List<T> list, int n) {
        this.writeCollection(list, n);
    }

    protected abstract void writeLong(long var1);

    public void writeLong(long l, int n) {
        this.setOutputField(n);
        this.writeLong(l);
    }

    protected void writeLongArray(long[] arrl) {
        if (arrl != null) {
            int n = arrl.length;
            this.writeInt(n);
            for (int i = 0; i < n; ++i) {
                this.writeLong(arrl[i]);
            }
            return;
        }
        this.writeInt(-1);
    }

    public void writeLongArray(long[] arrl, int n) {
        this.setOutputField(n);
        this.writeLongArray(arrl);
    }

    protected void writeNoException() {
        this.writeInt(0);
    }

    protected abstract void writeParcelable(Parcelable var1);

    public void writeParcelable(Parcelable parcelable, int n) {
        this.setOutputField(n);
        this.writeParcelable(parcelable);
    }

    public void writeSerializable(Serializable serializable, int n) {
        this.setOutputField(n);
        this.writeSerializable(serializable);
    }

    public <T> void writeSet(Set<T> set, int n) {
        this.writeCollection(set, n);
    }

    @RequiresApi(api=21)
    public void writeSize(Size size, int n) {
        this.setOutputField(n);
        boolean bl = size != null;
        this.writeBoolean(bl);
        if (size != null) {
            this.writeInt(size.getWidth());
            this.writeInt(size.getHeight());
        }
    }

    @RequiresApi(api=21)
    public void writeSizeF(SizeF sizeF, int n) {
        this.setOutputField(n);
        boolean bl = sizeF != null;
        this.writeBoolean(bl);
        if (sizeF != null) {
            this.writeFloat(sizeF.getWidth());
            this.writeFloat(sizeF.getHeight());
        }
    }

    public void writeSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int n) {
        this.setOutputField(n);
        if (sparseBooleanArray == null) {
            this.writeInt(-1);
            return;
        }
        int n2 = sparseBooleanArray.size();
        this.writeInt(n2);
        for (n = 0; n < n2; ++n) {
            this.writeInt(sparseBooleanArray.keyAt(n));
            this.writeBoolean(sparseBooleanArray.valueAt(n));
        }
    }

    protected abstract void writeString(String var1);

    public void writeString(String string2, int n) {
        this.setOutputField(n);
        this.writeString(string2);
    }

    protected abstract void writeStrongBinder(IBinder var1);

    public void writeStrongBinder(IBinder iBinder, int n) {
        this.setOutputField(n);
        this.writeStrongBinder(iBinder);
    }

    protected abstract void writeStrongInterface(IInterface var1);

    public void writeStrongInterface(IInterface iInterface, int n) {
        this.setOutputField(n);
        this.writeStrongInterface(iInterface);
    }

    protected void writeVersionedParcelable(VersionedParcelable versionedParcelable) {
        if (versionedParcelable == null) {
            this.writeString(null);
            return;
        }
        this.writeVersionedParcelableCreator(versionedParcelable);
        VersionedParcel versionedParcel = this.createSubParcel();
        VersionedParcel.writeToParcel(versionedParcelable, versionedParcel);
        versionedParcel.closeField();
    }

    public void writeVersionedParcelable(VersionedParcelable versionedParcelable, int n) {
        this.setOutputField(n);
        this.writeVersionedParcelable(versionedParcelable);
    }

    public static class ParcelException
    extends RuntimeException {
        public ParcelException(Throwable throwable) {
            super(throwable);
        }
    }

}

