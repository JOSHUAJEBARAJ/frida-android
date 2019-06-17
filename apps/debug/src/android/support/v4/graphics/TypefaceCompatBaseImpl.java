/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.net.Uri
 *  android.os.CancellationSignal
 */
package android.support.v4.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.provider.FontsContractCompat;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
class TypefaceCompatBaseImpl {
    private static final String CACHE_FILE_PREFIX = "cached_font_";
    private static final String TAG = "TypefaceCompatBaseImpl";

    TypefaceCompatBaseImpl() {
    }

    private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, int n) {
        return TypefaceCompatBaseImpl.findBestFont(fontFamilyFilesResourceEntry.getEntries(), n, new StyleExtractor<FontResourcesParserCompat.FontFileResourceEntry>(){

            @Override
            public int getWeight(FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.getWeight();
            }

            @Override
            public boolean isItalic(FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.isItalic();
            }
        });
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static <T> T findBestFont(T[] var0, int var1_1, StyleExtractor<T> var2_2) {
        var3_3 = (var1_1 & 1) == 0 ? 400 : 700;
        var8_4 = (var1_1 & 2) != 0;
        var7_5 = var0.length;
        var4_6 = Integer.MAX_VALUE;
        var9_7 = null;
        var1_1 = 0;
        while (var1_1 < var7_5) {
            var10_10 = var0[var1_1];
            var6_9 = Math.abs(var2_2.getWeight(var10_10) - var3_3);
            var5_8 = var2_2.isItalic(var10_10) == var8_4 ? 0 : 1;
            var6_9 = var6_9 * 2 + var5_8;
            if (var9_7 == null) ** GOTO lbl-1000
            var5_8 = var4_6;
            if (var4_6 > var6_9) lbl-1000: // 2 sources:
            {
                var9_7 = var10_10;
                var5_8 = var6_9;
            }
            ++var1_1;
            var4_6 = var5_8;
        }
        return var9_7;
    }

    @Nullable
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry object, Resources resources, int n) {
        if ((object = this.findBestEntry((FontResourcesParserCompat.FontFamilyFilesResourceEntry)object, n)) == null) {
            return null;
        }
        return TypefaceCompat.createFromResourcesFontFile(context, resources, object.getResourceId(), object.getFileName(), n);
    }

    public Typeface createFromFontInfo(Context context, @Nullable CancellationSignal object, @NonNull FontsContractCompat.FontInfo[] object2, int n) {
        if (object2.length < 1) {
            return null;
        }
        Object object3 = this.findBestInfo((FontsContractCompat.FontInfo[])object2, n);
        object2 = null;
        object = null;
        object = object3 = context.getContentResolver().openInputStream(object3.getUri());
        object2 = object3;
        try {
            context = this.createFromInputStream(context, (InputStream)object3);
        }
        catch (Throwable throwable) {
            TypefaceCompatUtil.closeQuietly((Closeable)object);
            throw throwable;
        }
        catch (IOException iOException) {
            TypefaceCompatUtil.closeQuietly((Closeable)object2);
            return null;
        }
        TypefaceCompatUtil.closeQuietly((Closeable)object3);
        return context;
    }

    protected Typeface createFromInputStream(Context object, InputStream inputStream) {
        block6 : {
            if ((object = TypefaceCompatUtil.getTempFile((Context)object)) == null) {
                return null;
            }
            boolean bl = TypefaceCompatUtil.copyToFile((File)object, inputStream);
            if (bl) break block6;
            object.delete();
            return null;
        }
        try {
            inputStream = Typeface.createFromFile((String)object.getPath());
            return inputStream;
        }
        catch (Throwable throwable) {
            throw throwable;
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
        finally {
            object.delete();
        }
    }

    @Nullable
    public Typeface createFromResourcesFontFile(Context object, Resources resources, int n, String string2, int n2) {
        block6 : {
            if ((object = TypefaceCompatUtil.getTempFile((Context)object)) == null) {
                return null;
            }
            boolean bl = TypefaceCompatUtil.copyToFile((File)object, resources, n);
            if (bl) break block6;
            object.delete();
            return null;
        }
        try {
            resources = Typeface.createFromFile((String)object.getPath());
            return resources;
        }
        catch (Throwable throwable) {
            throw throwable;
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
        finally {
            object.delete();
        }
    }

    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] arrfontInfo, int n) {
        return TypefaceCompatBaseImpl.findBestFont(arrfontInfo, n, new StyleExtractor<FontsContractCompat.FontInfo>(){

            @Override
            public int getWeight(FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.getWeight();
            }

            @Override
            public boolean isItalic(FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.isItalic();
            }
        });
    }

    private static interface StyleExtractor<T> {
        public int getWeight(T var1);

        public boolean isItalic(T var1);
    }

}

