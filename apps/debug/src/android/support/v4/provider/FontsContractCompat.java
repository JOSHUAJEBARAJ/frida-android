/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.ContentUris
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.pm.ProviderInfo
 *  android.content.pm.Signature
 *  android.content.res.Resources
 *  android.database.Cursor
 *  android.graphics.Typeface
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 *  android.os.Handler
 *  android.provider.BaseColumns
 */
package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.SelfDestructiveThread;
import android.support.v4.util.LruCache;
import android.support.v4.util.Preconditions;
import android.support.v4.util.SimpleArrayMap;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FontsContractCompat {
    private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final String PARCEL_FONT_RESULTS = "font_results";
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final String TAG = "FontsContractCompat";
    private static final SelfDestructiveThread sBackgroundThread;
    private static final Comparator<byte[]> sByteArrayComparator;
    static final Object sLock;
    @GuardedBy(value="sLock")
    static final SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>> sPendingReplies;
    static final LruCache<String, Typeface> sTypefaceCache;

    static {
        sTypefaceCache = new LruCache(16);
        sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
        sLock = new Object();
        sPendingReplies = new SimpleArrayMap();
        sByteArrayComparator = new Comparator<byte[]>(){

            @Override
            public int compare(byte[] arrby, byte[] arrby2) {
                if (arrby.length != arrby2.length) {
                    return arrby.length - arrby2.length;
                }
                for (int i = 0; i < arrby.length; ++i) {
                    if (arrby[i] == arrby2[i]) continue;
                    return arrby[i] - arrby2[i];
                }
                return 0;
            }
        };
    }

    private FontsContractCompat() {
    }

    @Nullable
    public static Typeface buildTypeface(@NonNull Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontInfo[] arrfontInfo) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, arrfontInfo, 0);
    }

    private static List<byte[]> convertToByteArrayList(Signature[] arrsignature) {
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        for (int i = 0; i < arrsignature.length; ++i) {
            arrayList.add(arrsignature[i].toByteArray());
        }
        return arrayList;
    }

    private static boolean equalsByteArrayList(List<byte[]> list, List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (Arrays.equals(list.get(i), list2.get(i))) continue;
            return false;
        }
        return true;
    }

    @NonNull
    public static FontFamilyResult fetchFonts(@NonNull Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontRequest fontRequest) throws PackageManager.NameNotFoundException {
        ProviderInfo providerInfo = FontsContractCompat.getProvider(context.getPackageManager(), fontRequest, context.getResources());
        if (providerInfo == null) {
            return new FontFamilyResult(1, null);
        }
        return new FontFamilyResult(0, FontsContractCompat.getFontFromProvider(context, fontRequest, providerInfo.authority, cancellationSignal));
    }

    private static List<List<byte[]>> getCertificates(FontRequest fontRequest, Resources resources) {
        if (fontRequest.getCertificates() != null) {
            return fontRequest.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @NonNull
    @VisibleForTesting
    static FontInfo[] getFontFromProvider(Context var0, FontRequest var1_1, String var2_6, CancellationSignal var3_7) {
        block10 : {
            block9 : {
                var14_8 = new ArrayList();
                var16_9 = new Uri.Builder().scheme("content").authority((String)var2_6).build();
                var17_10 = new Uri.Builder().scheme("content").authority((String)var2_6).appendPath("file").build();
                var2_6 = null;
                var15_11 = null;
                var4_12 = Build.VERSION.SDK_INT;
                if (var4_12 <= 16) ** GOTO lbl15
                var0 = var0.getContentResolver();
                var1_1 = var1_1.getQuery();
                var2_6 = var15_11;
                var0 = var0.query(var16_9, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var1_1}, null, (CancellationSignal)var3_7);
                ** GOTO lbl21
lbl15: // 1 sources:
                var2_6 = var15_11;
                var0 = var0.getContentResolver();
                var2_6 = var15_11;
                var1_1 = var1_1.getQuery();
                var2_6 = var15_11;
                var0 = var0.query(var16_9, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{var1_1}, null);
lbl21: // 2 sources:
                if (var0 == null) break block9;
                var2_6 = var0;
                if (var0.getCount() <= 0) break block9;
                var2_6 = var0;
                var7_13 = var0.getColumnIndex("result_code");
                var2_6 = var0;
                var3_7 = new ArrayList<E>();
                try {
                    var8_14 = var0.getColumnIndex("_id");
                    var9_15 = var0.getColumnIndex("file_id");
                    var10_16 = var0.getColumnIndex("font_ttc_index");
                    var11_17 = var0.getColumnIndex("font_weight");
                    var12_18 = var0.getColumnIndex("font_italic");
                    do {
                        var1_1 = var3_7;
                        if (var0.moveToNext()) {
                            var4_12 = var7_13 != -1 ? var0.getInt(var7_13) : 0;
                            var5_19 = var10_16 != -1 ? var0.getInt(var10_16) : 0;
                            var1_1 = var9_15 == -1 ? ContentUris.withAppendedId((Uri)var16_9, (long)var0.getLong(var8_14)) : ContentUris.withAppendedId((Uri)var17_10, (long)var0.getLong(var9_15));
                            var6_20 = var11_17 != -1 ? var0.getInt(var11_17) : 400;
                            var13_21 = var12_18 != -1 && var0.getInt(var12_18) == 1;
                            var3_7.add(new FontInfo((Uri)var1_1, var5_19, var6_20, var13_21, var4_12));
                            continue;
                        }
                        break block10;
                        break;
                    } while (true);
                }
                catch (Throwable var1_2) {}
                ** GOTO lbl58
            }
            var1_1 = var14_8;
        }
        if (var0 == null) return var1_1.toArray(new FontInfo[0]);
        var0.close();
        return var1_1.toArray(new FontInfo[0]);
        catch (Throwable var1_3) {
            var0 = var2_6;
        }
        ** GOTO lbl58
        catch (Throwable var1_4) {
            var0 = var2_6;
        }
lbl58: // 3 sources:
        if (var0 == null) throw var1_5;
        var0.close();
        throw var1_5;
    }

    @NonNull
    static TypefaceResult getFontInternal(Context context, FontRequest object, int n) {
        try {
            object = FontsContractCompat.fetchFonts(context, null, (FontRequest)object);
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return new TypefaceResult(null, -1);
        }
        int n2 = object.getStatusCode();
        int n3 = -3;
        if (n2 == 0) {
            if ((context = TypefaceCompat.createFromFontInfo(context, null, object.getFonts(), n)) != null) {
                n3 = 0;
            }
            return new TypefaceResult((Typeface)context, n3);
        }
        if (object.getStatusCode() == 1) {
            n3 = -2;
        }
        return new TypefaceResult(null, n3);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static Typeface getFontSync(Context object, FontRequest object2, @Nullable ResourcesCompat.FontCallback object3, @Nullable Handler object4, boolean bl, int n, int n2) {
        CharSequence charSequence = new StringBuilder();
        charSequence.append(object2.getIdentifier());
        charSequence.append("-");
        charSequence.append(n2);
        charSequence = charSequence.toString();
        Typeface typeface = sTypefaceCache.get((String)charSequence);
        if (typeface != null) {
            if (object3 == null) return typeface;
            object3.onFontRetrieved(typeface);
            return typeface;
        }
        if (bl && n == -1) {
            object = FontsContractCompat.getFontInternal((Context)object, (FontRequest)object2, n2);
            if (object3 == null) return object.mTypeface;
            if (object.mResult == 0) {
                object3.callbackSuccessAsync(object.mTypeface, (Handler)object4);
                return object.mTypeface;
            }
            object3.callbackFailAsync(object.mResult, (Handler)object4);
            return object.mTypeface;
        }
        object2 = new Callable<TypefaceResult>((Context)object, (FontRequest)object2, n2, (String)charSequence){
            final /* synthetic */ Context val$context;
            final /* synthetic */ String val$id;
            final /* synthetic */ FontRequest val$request;
            final /* synthetic */ int val$style;

            @Override
            public TypefaceResult call() throws Exception {
                TypefaceResult typefaceResult = FontsContractCompat.getFontInternal(this.val$context, this.val$request, this.val$style);
                if (typefaceResult.mTypeface != null) {
                    FontsContractCompat.sTypefaceCache.put(this.val$id, typefaceResult.mTypeface);
                }
                return typefaceResult;
            }
        };
        if (bl) {
            try {
                return ((TypefaceResult)FontsContractCompat.sBackgroundThread.postAndWait(object2, (int)n)).mTypeface;
            }
            catch (InterruptedException interruptedException) {
                return null;
            }
        }
        object = object3 == null ? null : new SelfDestructiveThread.ReplyCallback<TypefaceResult>((ResourcesCompat.FontCallback)object3, (Handler)object4){
            final /* synthetic */ ResourcesCompat.FontCallback val$fontCallback;
            final /* synthetic */ Handler val$handler;

            @Override
            public void onReply(TypefaceResult typefaceResult) {
                if (typefaceResult == null) {
                    this.val$fontCallback.callbackFailAsync(1, this.val$handler);
                    return;
                }
                if (typefaceResult.mResult == 0) {
                    this.val$fontCallback.callbackSuccessAsync(typefaceResult.mTypeface, this.val$handler);
                    return;
                }
                this.val$fontCallback.callbackFailAsync(typefaceResult.mResult, this.val$handler);
            }
        };
        object3 = sLock;
        synchronized (object3) {
            if (sPendingReplies.containsKey(charSequence)) {
                if (object == null) return null;
                sPendingReplies.get(charSequence).add((SelfDestructiveThread.ReplyCallback<TypefaceResult>)object);
                return null;
            }
            if (object != null) {
                object4 = new ArrayList();
                object4.add(object);
                sPendingReplies.put((String)charSequence, (ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>)object4);
            }
        }
        sBackgroundThread.postAndReply(object2, new SelfDestructiveThread.ReplyCallback<TypefaceResult>((String)charSequence){
            final /* synthetic */ String val$id;

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            @Override
            public void onReply(TypefaceResult var1_1) {
                var3_3 = FontsContractCompat.sLock;
                // MONITORENTER : var3_3
                var4_4 = FontsContractCompat.sPendingReplies.get(this.val$id);
                if (var4_4 != null) ** GOTO lbl8
                // MONITOREXIT : var3_3
                return;
lbl8: // 1 sources:
                FontsContractCompat.sPendingReplies.remove(this.val$id);
                // MONITOREXIT : var3_3
                var2_5 = 0;
                while (var2_5 < var4_4.size()) {
                    var4_4.get(var2_5).onReply(var1_1);
                    ++var2_5;
                }
                return;
            }
        });
        return null;
    }

    @Nullable
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @VisibleForTesting
    public static ProviderInfo getProvider(@NonNull PackageManager nameNotFoundException, @NonNull FontRequest object, @Nullable Resources object2) throws PackageManager.NameNotFoundException {
        String string2 = object.getProviderAuthority();
        ProviderInfo providerInfo = nameNotFoundException.resolveContentProvider(string2, 0);
        if (providerInfo != null) {
            if (providerInfo.packageName.equals(object.getProviderPackage())) {
                nameNotFoundException = FontsContractCompat.convertToByteArrayList(nameNotFoundException.getPackageInfo((String)providerInfo.packageName, (int)64).signatures);
                Collections.sort(nameNotFoundException, sByteArrayComparator);
                object = FontsContractCompat.getCertificates((FontRequest)object, (Resources)object2);
                for (int i = 0; i < object.size(); ++i) {
                    object2 = new ArrayList((Collection)object.get(i));
                    Collections.sort(object2, sByteArrayComparator);
                    if (!FontsContractCompat.equalsByteArrayList(nameNotFoundException, object2)) continue;
                    return providerInfo;
                }
                return null;
            }
            nameNotFoundException = new StringBuilder();
            nameNotFoundException.append("Found content provider ");
            nameNotFoundException.append(string2);
            nameNotFoundException.append(", but package was not ");
            nameNotFoundException.append(object.getProviderPackage());
            throw new PackageManager.NameNotFoundException(nameNotFoundException.toString());
        }
        nameNotFoundException = new StringBuilder();
        nameNotFoundException.append("No package found for authority: ");
        nameNotFoundException.append(string2);
        nameNotFoundException = new PackageManager.NameNotFoundException(nameNotFoundException.toString());
        throw nameNotFoundException;
    }

    @RequiresApi(value=19)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static Map<Uri, ByteBuffer> prepareFontData(Context context, FontInfo[] arrfontInfo, CancellationSignal cancellationSignal) {
        HashMap<FontInfo, ByteBuffer> hashMap = new HashMap<FontInfo, ByteBuffer>();
        for (FontInfo fontInfo : arrfontInfo) {
            if (fontInfo.getResultCode() != 0 || hashMap.containsKey(fontInfo = fontInfo.getUri())) continue;
            hashMap.put(fontInfo, TypefaceCompatUtil.mmap(context, cancellationSignal, (Uri)fontInfo));
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public static void requestFont(final @NonNull Context context, final @NonNull FontRequest fontRequest, @NonNull FontRequestCallback fontRequestCallback, @NonNull Handler handler) {
        handler.post(new Runnable(new Handler(), fontRequestCallback){
            final /* synthetic */ FontRequestCallback val$callback;
            final /* synthetic */ Handler val$callerThreadHandler;

            @Override
            public void run() {
                Typeface typeface;
                try {
                    typeface = FontsContractCompat.fetchFonts(context, null, fontRequest);
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    this.val$callerThreadHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            4.this.val$callback.onTypefaceRequestFailed(-1);
                        }
                    });
                    return;
                }
                if (typeface.getStatusCode() != 0) {
                    int n = typeface.getStatusCode();
                    if (n != 1) {
                        if (n != 2) {
                            this.val$callerThreadHandler.post(new Runnable(){

                                @Override
                                public void run() {
                                    4.this.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        this.val$callerThreadHandler.post(new Runnable(){

                            @Override
                            public void run() {
                                4.this.val$callback.onTypefaceRequestFailed(-3);
                            }
                        });
                        return;
                    }
                    this.val$callerThreadHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            4.this.val$callback.onTypefaceRequestFailed(-2);
                        }
                    });
                    return;
                }
                if ((typeface = typeface.getFonts()) != null && typeface.length != 0) {
                    int n = typeface.length;
                    for (int i = 0; i < n; ++i) {
                        FontInfo fontInfo = typeface[i];
                        if (fontInfo.getResultCode() == 0) continue;
                        i = fontInfo.getResultCode();
                        if (i < 0) {
                            this.val$callerThreadHandler.post(new Runnable(){

                                @Override
                                public void run() {
                                    4.this.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        this.val$callerThreadHandler.post(new Runnable(){

                            @Override
                            public void run() {
                                4.this.val$callback.onTypefaceRequestFailed(i);
                            }
                        });
                        return;
                    }
                    if ((typeface = FontsContractCompat.buildTypeface(context, null, (FontInfo[])typeface)) == null) {
                        this.val$callerThreadHandler.post(new Runnable(){

                            @Override
                            public void run() {
                                4.this.val$callback.onTypefaceRequestFailed(-3);
                            }
                        });
                        return;
                    }
                    this.val$callerThreadHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            4.this.val$callback.onTypefaceRetrieved(typeface);
                        }
                    });
                    return;
                }
                this.val$callerThreadHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        4.this.val$callback.onTypefaceRequestFailed(1);
                    }
                });
                return;
            }

        });
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void resetCache() {
        sTypefaceCache.evictAll();
    }

    public static final class Columns
    implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }

    public static class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public FontFamilyResult(int n, @Nullable FontInfo[] arrfontInfo) {
            this.mStatusCode = n;
            this.mFonts = arrfontInfo;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }
    }

    public static class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public FontInfo(@NonNull Uri uri, @IntRange(from=0) int n, @IntRange(from=1, to=1000) int n2, boolean bl, int n3) {
            this.mUri = Preconditions.checkNotNull(uri);
            this.mTtcIndex = n;
            this.mWeight = n2;
            this.mItalic = bl;
            this.mResultCode = n3;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        @IntRange(from=0)
        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        @NonNull
        public Uri getUri() {
            return this.mUri;
        }

        @IntRange(from=1, to=1000)
        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }
    }

    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public static final int RESULT_OK = 0;

        public void onTypefaceRequestFailed(int n) {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }

        @Retention(value=RetentionPolicy.SOURCE)
        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public static @interface FontRequestFailReason {
        }

    }

    private static final class TypefaceResult {
        final int mResult;
        final Typeface mTypeface;

        TypefaceResult(@Nullable Typeface typeface, int n) {
            this.mTypeface = typeface;
            this.mResult = n;
        }
    }

}

