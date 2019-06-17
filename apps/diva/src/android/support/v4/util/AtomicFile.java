/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
    private final File mBackupName;
    private final File mBaseName;

    public AtomicFile(File file) {
        this.mBaseName = file;
        this.mBackupName = new File(file.getPath() + ".bak");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static boolean sync(FileOutputStream fileOutputStream) {
        if (fileOutputStream == null) return true;
        try {
            fileOutputStream.getFD().sync();
        }
        catch (IOException iOException) {
            return false;
        }
        return true;
    }

    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void failWrite(FileOutputStream fileOutputStream) {
        if (fileOutputStream == null) return;
        AtomicFile.sync(fileOutputStream);
        try {
            fileOutputStream.close();
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
            return;
        }
        catch (IOException iOException) {
            Log.w((String)"AtomicFile", (String)"failWrite: Got exception:", (Throwable)iOException);
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void finishWrite(FileOutputStream fileOutputStream) {
        if (fileOutputStream == null) return;
        AtomicFile.sync(fileOutputStream);
        try {
            fileOutputStream.close();
            this.mBackupName.delete();
            return;
        }
        catch (IOException iOException) {
            Log.w((String)"AtomicFile", (String)"finishWrite: Got exception:", (Throwable)iOException);
            return;
        }
    }

    public File getBaseFile() {
        return this.mBaseName;
    }

    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }

    public byte[] readFully() throws IOException {
        byte[] arrby;
        int n;
        FileInputStream fileInputStream = this.openRead();
        int n2 = 0;
        try {
            arrby = new byte[fileInputStream.available()];
            do {
                if ((n = fileInputStream.read(arrby, n2, arrby.length - n2)) > 0) break block5;
                break;
            } while (true);
        }
        catch (Throwable throwable) {
            fileInputStream.close();
            throw throwable;
        }
        {
            block5 : {
                fileInputStream.close();
                return arrby;
            }
            n = n2 + n;
            int n3 = fileInputStream.available();
            n2 = n;
            if (n3 <= arrby.length - n) continue;
            byte[] arrby2 = new byte[n + n3];
            System.arraycopy((Object)arrby, 0, (Object)arrby2, 0, n);
            arrby = arrby2;
            n2 = n;
            continue;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public FileOutputStream startWrite() throws IOException {
        if (this.mBaseName.exists()) {
            if (!this.mBackupName.exists()) {
                if (!this.mBaseName.renameTo(this.mBackupName)) {
                    Log.w((String)"AtomicFile", (String)("Couldn't rename file " + this.mBaseName + " to backup file " + this.mBackupName));
                }
            } else {
                this.mBaseName.delete();
            }
        }
        try {
            return new FileOutputStream(this.mBaseName);
        }
        catch (FileNotFoundException fileNotFoundException) {
            if (!this.mBaseName.getParentFile().mkdir()) {
                throw new IOException("Couldn't create directory " + this.mBaseName);
            }
            try {
                return new FileOutputStream(this.mBaseName);
            }
            catch (FileNotFoundException fileNotFoundException2) {
                throw new IOException("Couldn't create " + this.mBaseName);
            }
        }
    }
}

