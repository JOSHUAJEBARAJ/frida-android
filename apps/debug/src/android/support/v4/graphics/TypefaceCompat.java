/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 *  android.os.Handler
 */
package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.TypefaceCompatApi21Impl;
import android.support.v4.graphics.TypefaceCompatApi24Impl;
import android.support.v4.graphics.TypefaceCompatApi26Impl;
import android.support.v4.graphics.TypefaceCompatApi28Impl;
import android.support.v4.graphics.TypefaceCompatBaseImpl;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.LruCache;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompat {
    private static final String TAG = "TypefaceCompat";
    private static final LruCache<String, Typeface> sTypefaceCache;
    private static final TypefaceCompatBaseImpl sTypefaceCompatImpl;

    static {
        sTypefaceCompatImpl = Build.VERSION.SDK_INT >= 28 ? new TypefaceCompatApi28Impl() : (Build.VERSION.SDK_INT >= 26 ? new TypefaceCompatApi26Impl() : (Build.VERSION.SDK_INT >= 24 && TypefaceCompatApi24Impl.isUsable() ? new TypefaceCompatApi24Impl() : (Build.VERSION.SDK_INT >= 21 ? new TypefaceCompatApi21Impl() : new TypefaceCompatBaseImpl())));
        sTypefaceCache = new LruCache(16);
    }

    private TypefaceCompat() {
    }

    @Nullable
    public static Typeface createFromFontInfo(@NonNull Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontsContractCompat.FontInfo[] arrfontInfo, int n) {
        return sTypefaceCompatImpl.createFromFontInfo(context, cancellationSignal, arrfontInfo, n);
    }

    @Nullable
    public static Typeface createFromResourcesFamilyXml(@NonNull Context object, @NonNull FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry, @NonNull Resources resources, int n, int n2, @Nullable ResourcesCompat.FontCallback fontCallback, @Nullable Handler handler, boolean bl) {
        if (familyResourceEntry instanceof FontResourcesParserCompat.ProviderResourceEntry) {
            familyResourceEntry = (FontResourcesParserCompat.ProviderResourceEntry)familyResourceEntry;
            boolean bl2 = bl ? familyResourceEntry.getFetchStrategy() == 0 : fontCallback == null;
            int n3 = bl ? familyResourceEntry.getTimeout() : -1;
            object = FontsContractCompat.getFontSync((Context)object, familyResourceEntry.getRequest(), fontCallback, handler, bl2, n3, n2);
        } else {
            familyResourceEntry = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry((Context)object, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)familyResourceEntry, resources, n2);
            object = familyResourceEntry;
            if (fontCallback != null) {
                if (familyResourceEntry != null) {
                    fontCallback.callbackSuccessAsync((Typeface)familyResourceEntry, handler);
                    object = familyResourceEntry;
                } else {
                    fontCallback.callbackFailAsync(-3, handler);
                    object = familyResourceEntry;
                }
            }
        }
        if (object != null) {
            sTypefaceCache.put(TypefaceCompat.createResourceUid(resources, n, n2), (Typeface)object);
        }
        return object;
    }

    @Nullable
    public static Typeface createFromResourcesFontFile(@NonNull Context context, @NonNull Resources object, int n, String string2, int n2) {
        if ((context = sTypefaceCompatImpl.createFromResourcesFontFile(context, (Resources)object, n, string2, n2)) != null) {
            object = TypefaceCompat.createResourceUid((Resources)object, n, n2);
            sTypefaceCache.put((String)object, (Typeface)context);
        }
        return context;
    }

    private static String createResourceUid(Resources resources, int n, int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(resources.getResourcePackageName(n));
        stringBuilder.append("-");
        stringBuilder.append(n);
        stringBuilder.append("-");
        stringBuilder.append(n2);
        return stringBuilder.toString();
    }

    @Nullable
    public static Typeface findFromCache(@NonNull Resources resources, int n, int n2) {
        return sTypefaceCache.get(TypefaceCompat.createResourceUid(resources, n, n2));
    }
}

