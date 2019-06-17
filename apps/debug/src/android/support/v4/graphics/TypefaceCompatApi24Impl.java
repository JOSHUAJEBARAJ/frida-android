/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.net.Uri
 *  android.os.CancellationSignal
 *  android.util.Log
 */
package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompatBaseImpl;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;

@RequiresApi(value=24)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
class TypefaceCompatApi24Impl
extends TypefaceCompatBaseImpl {
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi24Impl";
    private static final Method sAddFontWeightStyle;
    private static final Method sCreateFromFamiliesWithDefault;
    private static final Class sFontFamily;
    private static final Constructor sFontFamilyCtor;

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    static {
        Constructor constructor;
        Method method2;
        Class class_;
        Method method;
        block12 : {
            block13 : {
                class_ = Class.forName("android.graphics.FontFamily");
                constructor = class_.getConstructor(new Class[0]);
                method = class_.getMethod("addFontWeightStyle", ByteBuffer.class, Integer.TYPE, List.class, Integer.TYPE, Boolean.TYPE);
                try {
                    method2 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(class_, 1).getClass());
                    break block12;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    break block13;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    break block13;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    break block13;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    break block13;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    break block13;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    break block13;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                }
            }
            Log.e((String)"TypefaceCompatApi24Impl", (String)method2.getClass().getName(), (Throwable)((Object)method2));
            class_ = null;
            constructor = null;
            method = null;
            method2 = null;
        }
        sFontFamilyCtor = constructor;
        sFontFamily = class_;
        sAddFontWeightStyle = method;
        sCreateFromFamiliesWithDefault = method2;
    }

    TypefaceCompatApi24Impl() {
    }

    private static boolean addFontWeightStyle(Object object, ByteBuffer byteBuffer, int n, int n2, boolean bl) {
        void var0_3;
        try {
            bl = (Boolean)sAddFontWeightStyle.invoke(object, byteBuffer, n, null, n2, bl);
            return bl;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        throw new RuntimeException((Throwable)var0_3);
    }

    private static Typeface createFromFamiliesWithDefault(Object object) {
        void var0_3;
        try {
            Object object2 = Array.newInstance(sFontFamily, 1);
            Array.set(object2, 0, object);
            object = (Typeface)sCreateFromFamiliesWithDefault.invoke(null, object2);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        throw new RuntimeException((Throwable)var0_3);
    }

    public static boolean isUsable() {
        if (sAddFontWeightStyle == null) {
            Log.w((String)"TypefaceCompatApi24Impl", (String)"Unable to collect necessary private methods.Fallback to legacy implementation.");
        }
        if (sAddFontWeightStyle != null) {
            return true;
        }
        return false;
    }

    private static Object newFamily() {
        void var0_4;
        try {
            Object t = sFontFamilyCtor.newInstance(new Object[0]);
            return t;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (InstantiationException instantiationException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        throw new RuntimeException((Throwable)var0_4);
    }

    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry arrfontFileResourceEntry, Resources resources, int n) {
        Object object = TypefaceCompatApi24Impl.newFamily();
        for (FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry : arrfontFileResourceEntry.getEntries()) {
            ByteBuffer byteBuffer = TypefaceCompatUtil.copyToDirectBuffer(context, resources, fontFileResourceEntry.getResourceId());
            if (byteBuffer == null) {
                return null;
            }
            if (TypefaceCompatApi24Impl.addFontWeightStyle(object, byteBuffer, fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic())) continue;
            return null;
        }
        return TypefaceCompatApi24Impl.createFromFamiliesWithDefault(object);
    }

    @Override
    public Typeface createFromFontInfo(Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontsContractCompat.FontInfo[] arrfontInfo, int n) {
        Object object = TypefaceCompatApi24Impl.newFamily();
        SimpleArrayMap<Uri, ByteBuffer> simpleArrayMap = new SimpleArrayMap<Uri, ByteBuffer>();
        for (FontsContractCompat.FontInfo fontInfo : arrfontInfo) {
            ByteBuffer byteBuffer;
            Uri uri = fontInfo.getUri();
            ByteBuffer byteBuffer2 = byteBuffer = (ByteBuffer)simpleArrayMap.get((Object)uri);
            if (byteBuffer == null) {
                byteBuffer2 = TypefaceCompatUtil.mmap(context, cancellationSignal, uri);
                simpleArrayMap.put(uri, byteBuffer2);
            }
            if (TypefaceCompatApi24Impl.addFontWeightStyle(object, byteBuffer2, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic())) continue;
            return null;
        }
        return Typeface.create((Typeface)TypefaceCompatApi24Impl.createFromFamiliesWithDefault(object), (int)n);
    }
}

