/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.provider.DocumentsContract
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;

class DocumentsContractApi19 {
    private static final String TAG = "DocumentFile";

    DocumentsContractApi19() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean canRead(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 1) != 0 || TextUtils.isEmpty((CharSequence)DocumentsContractApi19.getRawType(context, uri))) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean canWrite(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        String string2 = DocumentsContractApi19.getRawType(context, uri);
        int n = DocumentsContractApi19.queryForInt(context, uri, "flags", 0);
        if (TextUtils.isEmpty((CharSequence)string2)) return false;
        if ((n & 4) != 0) {
            return true;
        }
        if ("vnd.android.document/directory".equals(string2) && (n & 8) != 0) {
            return true;
        }
        if (TextUtils.isEmpty((CharSequence)string2)) return false;
        if ((n & 2) == 0) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable == null) return;
        try {
            autoCloseable.close();
            return;
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Exception exception) {
            return;
        }
    }

    public static boolean delete(Context context, Uri uri) {
        return DocumentsContract.deleteDocument((ContentResolver)context.getContentResolver(), (Uri)uri);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean exists(Context context, Uri uri) {
        boolean bl;
        ContentResolver contentResolver = context.getContentResolver();
        Context context2 = null;
        context = null;
        try {
            uri = contentResolver.query(uri, new String[]{"document_id"}, null, null, null);
            context = uri;
            context2 = uri;
            int n = uri.getCount();
            bl = n > 0;
        }
        catch (Exception exception) {
            context2 = context;
            try {
                Log.w((String)"DocumentFile", (String)("Failed query: " + exception));
            }
            catch (Throwable throwable) {
                DocumentsContractApi19.closeQuietly((AutoCloseable)context2);
                throw throwable;
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)context);
            return false;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
        return bl;
    }

    public static String getName(Context context, Uri uri) {
        return DocumentsContractApi19.queryForString(context, uri, "_display_name", null);
    }

    private static String getRawType(Context context, Uri uri) {
        return DocumentsContractApi19.queryForString(context, uri, "mime_type", null);
    }

    public static String getType(Context object, Uri object2) {
        object = object2 = DocumentsContractApi19.getRawType((Context)object, (Uri)object2);
        if ("vnd.android.document/directory".equals(object2)) {
            object = null;
        }
        return object;
    }

    public static boolean isDirectory(Context context, Uri uri) {
        return "vnd.android.document/directory".equals(DocumentsContractApi19.getRawType(context, uri));
    }

    public static boolean isDocumentUri(Context context, Uri uri) {
        return DocumentsContract.isDocumentUri((Context)context, (Uri)uri);
    }

    public static boolean isFile(Context object, Uri uri) {
        if ("vnd.android.document/directory".equals(object = DocumentsContractApi19.getRawType((Context)object, uri)) || TextUtils.isEmpty((CharSequence)object)) {
            return false;
        }
        return true;
    }

    public static long lastModified(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "last_modified", 0);
    }

    public static long length(Context context, Uri uri) {
        return DocumentsContractApi19.queryForLong(context, uri, "_size", 0);
    }

    private static int queryForInt(Context context, Uri uri, String string2, int n) {
        return (int)DocumentsContractApi19.queryForLong(context, uri, string2, n);
    }

    private static long queryForLong(Context context, Uri uri, String string2, long l) {
        block7 : {
            long l2;
            ContentResolver contentResolver = context.getContentResolver();
            Context context2 = null;
            context = null;
            uri = contentResolver.query(uri, new String[]{string2}, null, null, null);
            context = uri;
            context2 = uri;
            if (!uri.moveToFirst()) break block7;
            context = uri;
            context2 = uri;
            if (uri.isNull(0)) break block7;
            context = uri;
            context2 = uri;
            try {
                l2 = uri.getLong(0);
            }
            catch (Exception exception) {
                context2 = context;
                try {
                    Log.w((String)"DocumentFile", (String)("Failed query: " + exception));
                }
                catch (Throwable throwable) {
                    DocumentsContractApi19.closeQuietly((AutoCloseable)context2);
                    throw throwable;
                }
                DocumentsContractApi19.closeQuietly((AutoCloseable)context);
                return l;
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
            return l2;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
        return l;
    }

    private static String queryForString(Context context, Uri uri, String string2, String string3) {
        block7 : {
            ContentResolver contentResolver = context.getContentResolver();
            Context context2 = null;
            context = null;
            uri = contentResolver.query(uri, new String[]{string2}, null, null, null);
            context = uri;
            context2 = uri;
            if (!uri.moveToFirst()) break block7;
            context = uri;
            context2 = uri;
            if (uri.isNull(0)) break block7;
            context = uri;
            context2 = uri;
            try {
                string2 = uri.getString(0);
            }
            catch (Exception exception) {
                context2 = context;
                try {
                    Log.w((String)"DocumentFile", (String)("Failed query: " + exception));
                }
                catch (Throwable throwable) {
                    DocumentsContractApi19.closeQuietly((AutoCloseable)context2);
                    throw throwable;
                }
                DocumentsContractApi19.closeQuietly((AutoCloseable)context);
                return string3;
            }
            DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
            return string2;
        }
        DocumentsContractApi19.closeQuietly((AutoCloseable)uri);
        return string3;
    }
}

