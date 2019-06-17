/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ContentProvider
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.content.pm.ProviderInfo
 *  android.content.res.XmlResourceParser
 *  android.database.Cursor
 *  android.database.MatrixCursor
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Environment
 *  android.os.ParcelFileDescriptor
 *  android.text.TextUtils
 *  android.webkit.MimeTypeMap
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider
extends ContentProvider {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final File DEVICE_ROOT = new File("/");
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    @GuardedBy(value="sCache")
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private PathStrategy mStrategy;

    private static /* varargs */ File buildPath(File file, String ... arrstring) {
        for (String string2 : arrstring) {
            File file2 = file;
            if (string2 != null) {
                file2 = new File(file, string2);
            }
            file = file2;
        }
        return file;
    }

    private static Object[] copyOf(Object[] arrobject, int n) {
        Object[] arrobject2 = new Object[n];
        System.arraycopy((Object)arrobject, 0, (Object)arrobject2, 0, n);
        return arrobject2;
    }

    private static String[] copyOf(String[] arrstring, int n) {
        String[] arrstring2 = new String[n];
        System.arraycopy((Object)arrstring, 0, (Object)arrstring2, 0, n);
        return arrstring2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static PathStrategy getPathStrategy(Context var0, String var1_5) {
        var4_7 = FileProvider.sCache;
        // MONITORENTER : var4_7
        try {
            var3_9 = var2_8 = FileProvider.sCache.get(var1_6);
            if (var2_8 != null) ** GOTO lbl17
        }
        catch (Throwable var0_5) {
            throw var0_3;
        }
        try {
            try {
                var3_9 = FileProvider.parsePathStrategy(var0, (String)var1_6);
            }
            catch (XmlPullParserException var0_1) {
                throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)var0_1);
            }
            catch (IOException var0_2) {
                throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", var0_2);
            }
            FileProvider.sCache.put((String)var1_6, var3_9);
            return var3_9;
lbl17: // 1 sources:
            return var3_9;
        }
        catch (Throwable var0_4) {
            throw var0_3;
        }
    }

    public static Uri getUriForFile(@NonNull Context context, @NonNull String string2, @NonNull File file) {
        return FileProvider.getPathStrategy(context, string2).getUriForFile(file);
    }

    private static int modeToMode(String string2) {
        if ("r".equals(string2)) {
            return 268435456;
        }
        if (!"w".equals(string2) && !"wt".equals(string2)) {
            if ("wa".equals(string2)) {
                return 704643072;
            }
            if ("rw".equals(string2)) {
                return 939524096;
            }
            if ("rwt".equals(string2)) {
                return 1006632960;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid mode: ");
            stringBuilder.append(string2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        return 738197504;
    }

    private static PathStrategy parsePathStrategy(Context object, String object2) throws IOException, XmlPullParserException {
        SimplePathStrategy simplePathStrategy = new SimplePathStrategy((String)object2);
        XmlResourceParser xmlResourceParser = object.getPackageManager().resolveContentProvider((String)object2, 128).loadXmlMetaData(object.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (xmlResourceParser != null) {
            int n;
            while ((n = xmlResourceParser.next()) != 1) {
                if (n != 2) continue;
                String string2 = xmlResourceParser.getName();
                String string3 = xmlResourceParser.getAttributeValue(null, "name");
                String string4 = xmlResourceParser.getAttributeValue(null, "path");
                File[] arrfile = null;
                File[] arrfile2 = null;
                object2 = null;
                if ("root-path".equals(string2)) {
                    object2 = DEVICE_ROOT;
                } else if ("files-path".equals(string2)) {
                    object2 = object.getFilesDir();
                } else if ("cache-path".equals(string2)) {
                    object2 = object.getCacheDir();
                } else if ("external-path".equals(string2)) {
                    object2 = Environment.getExternalStorageDirectory();
                } else if ("external-files-path".equals(string2)) {
                    arrfile2 = ContextCompat.getExternalFilesDirs((Context)object, null);
                    if (arrfile2.length > 0) {
                        object2 = arrfile2[0];
                    }
                } else if ("external-cache-path".equals(string2)) {
                    arrfile2 = ContextCompat.getExternalCacheDirs((Context)object);
                    object2 = arrfile;
                    if (arrfile2.length > 0) {
                        object2 = arrfile2[0];
                    }
                } else {
                    object2 = arrfile;
                    if (Build.VERSION.SDK_INT >= 21) {
                        object2 = arrfile2;
                        if ("external-media-path".equals(string2)) {
                            arrfile = object.getExternalMediaDirs();
                            object2 = arrfile2;
                            if (arrfile.length > 0) {
                                object2 = arrfile[0];
                            }
                        }
                    }
                }
                if (object2 == null) continue;
                simplePathStrategy.addRoot(string3, FileProvider.buildPath((File)object2, string4));
            }
            return simplePathStrategy;
        }
        object = new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        throw object;
    }

    public void attachInfo(@NonNull Context context, @NonNull ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (!providerInfo.exported) {
            if (providerInfo.grantUriPermissions) {
                this.mStrategy = FileProvider.getPathStrategy(context, providerInfo.authority);
                return;
            }
            throw new SecurityException("Provider must grant uri permissions");
        }
        throw new SecurityException("Provider must not be exported");
    }

    public int delete(@NonNull Uri uri, @Nullable String string2, @Nullable String[] arrstring) {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

    public String getType(@NonNull Uri object) {
        int n = (object = this.mStrategy.getFileForUri((Uri)object)).getName().lastIndexOf(46);
        if (n >= 0) {
            object = object.getName().substring(n + 1);
            object = MimeTypeMap.getSingleton().getMimeTypeFromExtension((String)object);
            if (object != null) {
                return object;
            }
        }
        return "application/octet-stream";
    }

    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public boolean onCreate() {
        return true;
    }

    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String string2) throws FileNotFoundException {
        return ParcelFileDescriptor.open((File)this.mStrategy.getFileForUri(uri), (int)FileProvider.modeToMode(string2));
    }

    public Cursor query(@NonNull Uri matrixCursor, @Nullable String[] arrstring, @Nullable String object, @Nullable String[] arrstring2, @Nullable String string22) {
        object = this.mStrategy.getFileForUri((Uri)matrixCursor);
        matrixCursor = arrstring;
        if (arrstring == null) {
            matrixCursor = COLUMNS;
        }
        arrstring2 = new String[matrixCursor.length];
        arrstring = new Object[matrixCursor.length];
        int n = 0;
        for (String string22 : matrixCursor) {
            int n2;
            if ("_display_name".equals(string22)) {
                arrstring2[n] = "_display_name";
                arrstring[n] = object.getName();
                n2 = n + 1;
            } else {
                n2 = n;
                if ("_size".equals(string22)) {
                    arrstring2[n] = "_size";
                    arrstring[n] = object.length();
                    n2 = n + 1;
                }
            }
            n = n2;
        }
        matrixCursor = FileProvider.copyOf(arrstring2, n);
        arrstring = FileProvider.copyOf((Object[])arrstring, n);
        matrixCursor = new MatrixCursor((String[])matrixCursor, 1);
        matrixCursor.addRow((Object[])arrstring);
        return matrixCursor;
    }

    public int update(@NonNull Uri uri, ContentValues contentValues, @Nullable String string2, @Nullable String[] arrstring) {
        throw new UnsupportedOperationException("No external updates");
    }

    static interface PathStrategy {
        public File getFileForUri(Uri var1);

        public Uri getUriForFile(File var1);
    }

    static class SimplePathStrategy
    implements PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        SimplePathStrategy(String string2) {
            this.mAuthority = string2;
        }

        void addRoot(String string2, File file) {
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                try {
                    File file2 = file.getCanonicalFile();
                    this.mRoots.put(string2, file2);
                    return;
                }
                catch (IOException iOException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to resolve canonical path for ");
                    stringBuilder.append(file);
                    throw new IllegalArgumentException(stringBuilder.toString(), iOException);
                }
            }
            throw new IllegalArgumentException("Name must not be empty");
        }

        @Override
        public File getFileForUri(Uri object) {
            Object object2 = object.getEncodedPath();
            int n = object2.indexOf(47, 1);
            Object object3 = Uri.decode((String)object2.substring(1, n));
            object2 = Uri.decode((String)object2.substring(n + 1));
            if ((object3 = this.mRoots.get(object3)) != null) {
                object = new File((File)object3, (String)object2);
                try {
                    object2 = object.getCanonicalFile();
                    if (object2.getPath().startsWith(object3.getPath())) {
                        return object2;
                    }
                    throw new SecurityException("Resolved path jumped beyond configured root");
                }
                catch (IOException iOException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to resolve canonical path for ");
                    stringBuilder.append(object);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            object3 = new StringBuilder();
            object3.append("Unable to find configured root for ");
            object3.append(object);
            throw new IllegalArgumentException(object3.toString());
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public Uri getUriForFile(File var1_1) {
            try {
                var4_2 = var1_1.getCanonicalPath();
                var1_1 = null;
            }
            catch (IOException var2_5) {
                var2_6 = new StringBuilder();
                var2_6.append("Failed to resolve canonical path for ");
                var2_6.append(var1_1);
                var1_1 = new IllegalArgumentException(var2_6.toString());
                throw var1_1;
            }
            for (Map.Entry<String, File> var3_7 : this.mRoots.entrySet()) {
                var6_9 = var3_7.getValue().getPath();
                var2_4 = var1_1;
                if (!var4_2.startsWith(var6_9)) ** GOTO lbl12
                if (var1_1 == null) ** GOTO lbl-1000
                var2_4 = var1_1;
                if (var6_9.length() > ((File)var1_1.getValue()).getPath().length()) lbl-1000: // 2 sources:
                {
                    var2_4 = var3_7;
                }
lbl12: // 4 sources:
                var1_1 = var2_4;
            }
            if (var1_1 == null) {
                var1_1 = new StringBuilder();
                var1_1.append("Failed to find configured root that contains ");
                var1_1.append(var4_2);
                throw new IllegalArgumentException(var1_1.toString());
            }
            var2_4 = ((File)var1_1.getValue()).getPath();
            var2_4 = var2_4.endsWith("/") != false ? var4_2.substring(var2_4.length()) : var4_2.substring(var2_4.length() + 1);
            var3_8 = new StringBuilder();
            var3_8.append(Uri.encode((String)((String)var1_1.getKey())));
            var3_8.append('/');
            var3_8.append(Uri.encode(var2_4, (String)"/"));
            var1_1 = var3_8.toString();
            return new Uri.Builder().scheme("content").authority(this.mAuthority).encodedPath((String)var1_1).build();
        }
    }

}

