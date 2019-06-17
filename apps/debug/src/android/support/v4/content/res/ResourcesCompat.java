/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Looper
 *  android.util.Log
 *  android.util.TypedValue
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v4.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.util.TypedValue;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
    private static final String TAG = "ResourcesCompat";

    private ResourcesCompat() {
    }

    @ColorInt
    public static int getColor(@NonNull Resources resources, @ColorRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColor(n, theme);
        }
        return resources.getColor(n);
    }

    @Nullable
    public static ColorStateList getColorStateList(@NonNull Resources resources, @ColorRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(n, theme);
        }
        return resources.getColorStateList(n);
    }

    @Nullable
    public static Drawable getDrawable(@NonNull Resources resources, @DrawableRes int n, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(n, theme);
        }
        return resources.getDrawable(n);
    }

    @Nullable
    public static Drawable getDrawableForDensity(@NonNull Resources resources, @DrawableRes int n, int n2, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(n, n2, theme);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(n, n2);
        }
        return resources.getDrawable(n);
    }

    @Nullable
    public static Typeface getFont(@NonNull Context context, @FontRes int n) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, n, new TypedValue(), 0, null, null, false);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static Typeface getFont(@NonNull Context context, @FontRes int n, TypedValue typedValue, int n2, @Nullable FontCallback fontCallback) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, n, typedValue, n2, fontCallback, null, true);
    }

    public static void getFont(@NonNull Context context, @FontRes int n, @NonNull FontCallback fontCallback, @Nullable Handler handler) throws Resources.NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
            return;
        }
        ResourcesCompat.loadFont(context, n, new TypedValue(), 0, fontCallback, handler, false);
    }

    private static Typeface loadFont(@NonNull Context object, int n, TypedValue typedValue, int n2, @Nullable FontCallback fontCallback, @Nullable Handler handler, boolean bl) {
        Resources resources = object.getResources();
        resources.getValue(n, typedValue, true);
        object = ResourcesCompat.loadFont((Context)object, resources, typedValue, n, n2, fontCallback, handler, bl);
        if (object == null) {
            if (fontCallback != null) {
                return object;
            }
            object = new StringBuilder();
            object.append("Font resource ID #0x");
            object.append(Integer.toHexString(n));
            object.append(" could not be retrieved.");
            throw new Resources.NotFoundException(object.toString());
        }
        return object;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static Typeface loadFont(@NonNull Context var0, Resources var1_15, TypedValue var2_16, int var3_17, int var4_18, @Nullable FontCallback var5_19, @Nullable Handler var6_20, boolean var7_21) {
        block24 : {
            block23 : {
                block21 : {
                    block22 : {
                        if (var2_16.string == null) {
                            var0 = new StringBuilder();
                            var0.append("Resource \"");
                            var0.append(var1_15.getResourceName(var3_17));
                            var0.append("\" (");
                            var0.append(Integer.toHexString(var3_17));
                            var0.append(") is not a Font: ");
                            var0.append(var2_16);
                            throw new Resources.NotFoundException(var0.toString());
                        }
                        var2_16 = var2_16.string.toString();
                        if (!var2_16.startsWith("res/")) {
                            if (var5_19 == null) return null;
                            var5_19.callbackFailAsync(-3, var6_20);
                            return null;
                        }
                        var9_22 = TypefaceCompat.findFromCache((Resources)var1_15, var3_17, var4_18);
                        if (var9_22 != null) {
                            if (var5_19 == null) return var9_22;
                            var5_19.callbackSuccessAsync((Typeface)var9_22, var6_20);
                            return var9_22;
                        }
                        var8_23 = var2_16.toLowerCase().endsWith(".xml");
                        if (!var8_23) break block21;
                        var9_22 = FontResourcesParserCompat.parse((XmlPullParser)var1_15.getXml(var3_17), (Resources)var1_15);
                        if (var9_22 != null) break block22;
                        try {
                            Log.e((String)"ResourcesCompat", (String)"Failed to find font-family tag");
                            if (var5_19 == null) return null;
                            var5_19.callbackFailAsync(-3, var6_20);
                            return null;
                        }
                        catch (IOException var0_1) {
                            break block23;
                        }
                        catch (XmlPullParserException var0_2) {
                            break block24;
                        }
                    }
                    try {
                        return TypefaceCompat.createFromResourcesFamilyXml((Context)var0, (FontResourcesParserCompat.FamilyResourceEntry)var9_22, (Resources)var1_15, var3_17, var4_18, var5_19, var6_20, var7_21);
                    }
                    catch (IOException var0_3) {
                        break block23;
                    }
                    catch (XmlPullParserException var0_4) {
                        break block24;
                    }
                    catch (IOException var0_5) {
                        break block23;
                    }
                    catch (XmlPullParserException var0_6) {
                        break block24;
                    }
                }
                var0 = TypefaceCompat.createFromResourcesFontFile((Context)var0, (Resources)var1_15, var3_17, (String)var2_16, var4_18);
                if (var5_19 == null) return var0;
                if (var0 == null) ** GOTO lbl54
                try {
                    var5_19.callbackSuccessAsync((Typeface)var0, var6_20);
                    return var0;
lbl54: // 1 sources:
                    var5_19.callbackFailAsync(-3, var6_20);
                    return var0;
                }
                catch (IOException var0_7) {
                    break block23;
                }
                catch (XmlPullParserException var0_8) {
                    break block24;
                }
                catch (IOException var0_9) {
                    break block23;
                }
                catch (XmlPullParserException var0_10) {
                    break block24;
                }
                catch (IOException var0_11) {
                    // empty catch block
                }
            }
            var1_15 = new StringBuilder();
            var1_15.append("Failed to read xml resource ");
            var1_15.append((String)var2_16);
            Log.e((String)"ResourcesCompat", (String)var1_15.toString(), (Throwable)var0_12);
            ** GOTO lbl79
            catch (XmlPullParserException var0_13) {
                // empty catch block
            }
        }
        var1_15 = new StringBuilder();
        var1_15.append("Failed to parse xml resource ");
        var1_15.append((String)var2_16);
        Log.e((String)"ResourcesCompat", (String)var1_15.toString(), (Throwable)var0_14);
lbl79: // 2 sources:
        if (var5_19 == null) return null;
        var5_19.callbackFailAsync(-3, var6_20);
        return null;
    }

    public static abstract class FontCallback {
        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public final void callbackFailAsync(final int n, @Nullable Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post(new Runnable(){

                @Override
                public void run() {
                    FontCallback.this.onFontRetrievalFailed(n);
                }
            });
        }

        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public final void callbackSuccessAsync(final Typeface typeface, @Nullable Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post(new Runnable(){

                @Override
                public void run() {
                    FontCallback.this.onFontRetrieved(typeface);
                }
            });
        }

        public abstract void onFontRetrievalFailed(int var1);

        public abstract void onFontRetrieved(@NonNull Typeface var1);

    }

}

