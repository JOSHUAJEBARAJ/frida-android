/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.Intent$ShortcutIconResource
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapShader
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Icon
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.graphics.drawable;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Preconditions;
import android.text.TextUtils;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

public class IconCompat
extends CustomVersionedParcelable {
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final String EXTRA_INT1 = "int1";
    private static final String EXTRA_INT2 = "int2";
    private static final String EXTRA_OBJ = "obj";
    private static final String EXTRA_TINT_LIST = "tint_list";
    private static final String EXTRA_TINT_MODE = "tint_mode";
    private static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_UNKNOWN = -1;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public byte[] mData;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public int mInt1;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public int mInt2;
    Object mObj1;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public Parcelable mParcelable;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public ColorStateList mTintList = null;
    PorterDuff.Mode mTintMode = DEFAULT_TINT_MODE;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public String mTintModeStr;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public int mType;

    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public IconCompat() {
    }

    private IconCompat(int n) {
        this.mType = n;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Nullable
    public static IconCompat createFromBundle(@NonNull Bundle var0) {
        var1_1 = var0.getInt("type");
        var2_2 = new IconCompat(var1_1);
        var2_2.mInt1 = var0.getInt("int1");
        var2_2.mInt2 = var0.getInt("int2");
        if (var0.containsKey("tint_list")) {
            var2_2.mTintList = (ColorStateList)var0.getParcelable("tint_list");
        }
        if (var0.containsKey("tint_mode")) {
            var2_2.mTintMode = PorterDuff.Mode.valueOf((String)var0.getString("tint_mode"));
        }
        if (var1_1 == -1 || var1_1 == 1) ** GOTO lbl24
        if (var1_1 == 2) ** GOTO lbl-1000
        if (var1_1 == 3) {
            var2_2.mObj1 = var0.getByteArray("obj");
            return var2_2;
        }
        if (var1_1 != 4) {
            if (var1_1 != 5) {
                var0 = new StringBuilder();
                var0.append("Unknown type ");
                var0.append(var1_1);
                Log.w((String)"IconCompat", (String)var0.toString());
                return null;
            }
        } else lbl-1000: // 2 sources:
        {
            var2_2.mObj1 = var0.getString("obj");
            return var2_2;
        }
lbl24: // 2 sources:
        var2_2.mObj1 = var0.getParcelable("obj");
        return var2_2;
    }

    @Nullable
    @RequiresApi(value=23)
    public static IconCompat createFromIcon(@NonNull Context object, @NonNull Icon icon) {
        Preconditions.checkNotNull(icon);
        int n = IconCompat.getType(icon);
        if (n != 2) {
            if (n != 4) {
                object = new IconCompat(-1);
                object.mObj1 = icon;
                return object;
            }
            return IconCompat.createWithContentUri(IconCompat.getUri(icon));
        }
        String string2 = IconCompat.getResPackage(icon);
        try {
            object = IconCompat.createWithResource(IconCompat.getResources((Context)object, string2), string2, IconCompat.getResId(icon));
            return object;
        }
        catch (Resources.NotFoundException notFoundException) {
            throw new IllegalArgumentException("Icon resource cannot be found");
        }
    }

    @Nullable
    @RequiresApi(value=23)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static IconCompat createFromIcon(@NonNull Icon icon) {
        Preconditions.checkNotNull(icon);
        int n = IconCompat.getType(icon);
        if (n != 2) {
            if (n != 4) {
                IconCompat iconCompat = new IconCompat(-1);
                iconCompat.mObj1 = icon;
                return iconCompat;
            }
            return IconCompat.createWithContentUri(IconCompat.getUri(icon));
        }
        return IconCompat.createWithResource(null, IconCompat.getResPackage(icon), IconCompat.getResId(icon));
    }

    @VisibleForTesting
    static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap bitmap, boolean bl) {
        int n = (int)((float)Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.6666667f);
        Bitmap bitmap2 = Bitmap.createBitmap((int)n, (int)n, (Bitmap.Config)Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint(3);
        float f = (float)n * 0.5f;
        float f2 = 0.9166667f * f;
        if (bl) {
            float f3 = (float)n * 0.010416667f;
            paint.setColor(0);
            paint.setShadowLayer(f3, 0.0f, (float)n * 0.020833334f, 1023410176);
            canvas.drawCircle(f, f, f2, paint);
            paint.setShadowLayer(f3, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(f, f, f2, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate((float)((- bitmap.getWidth() - n) / 2), (float)((- bitmap.getHeight() - n) / 2));
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader((Shader)bitmapShader);
        canvas.drawCircle(f, f, f2, paint);
        canvas.setBitmap(null);
        return bitmap2;
    }

    public static IconCompat createWithAdaptiveBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            IconCompat iconCompat = new IconCompat(5);
            iconCompat.mObj1 = bitmap;
            return iconCompat;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    public static IconCompat createWithBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            IconCompat iconCompat = new IconCompat(1);
            iconCompat.mObj1 = bitmap;
            return iconCompat;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    public static IconCompat createWithContentUri(Uri uri) {
        if (uri != null) {
            return IconCompat.createWithContentUri(uri.toString());
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    public static IconCompat createWithContentUri(String string2) {
        if (string2 != null) {
            IconCompat iconCompat = new IconCompat(4);
            iconCompat.mObj1 = string2;
            return iconCompat;
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    public static IconCompat createWithData(byte[] arrby, int n, int n2) {
        if (arrby != null) {
            IconCompat iconCompat = new IconCompat(3);
            iconCompat.mObj1 = arrby;
            iconCompat.mInt1 = n;
            iconCompat.mInt2 = n2;
            return iconCompat;
        }
        throw new IllegalArgumentException("Data must not be null.");
    }

    public static IconCompat createWithResource(Context context, @DrawableRes int n) {
        if (context != null) {
            return IconCompat.createWithResource(context.getResources(), context.getPackageName(), n);
        }
        throw new IllegalArgumentException("Context must not be null.");
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public static IconCompat createWithResource(Resources resources, String string2, @DrawableRes int n) {
        if (string2 != null) {
            if (n != 0) {
                IconCompat iconCompat = new IconCompat(2);
                iconCompat.mInt1 = n;
                if (resources != null) {
                    try {
                        iconCompat.mObj1 = resources.getResourceName(n);
                        return iconCompat;
                    }
                    catch (Resources.NotFoundException notFoundException) {
                        throw new IllegalArgumentException("Icon resource cannot be found");
                    }
                }
                iconCompat.mObj1 = string2;
                return iconCompat;
            }
            throw new IllegalArgumentException("Drawable resource ID must not be 0");
        }
        throw new IllegalArgumentException("Package must not be null.");
    }

    @DrawableRes
    @IdRes
    @RequiresApi(value=23)
    private static int getResId(@NonNull Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            return icon.getResId();
        }
        try {
            int n = (Integer)icon.getClass().getMethod("getResId", new Class[0]).invoke((Object)icon, new Object[0]);
            return n;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon resource", (Throwable)noSuchMethodException);
            return 0;
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon resource", (Throwable)invocationTargetException);
            return 0;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon resource", (Throwable)illegalAccessException);
            return 0;
        }
    }

    @Nullable
    @RequiresApi(value=23)
    private static String getResPackage(@NonNull Icon object) {
        if (Build.VERSION.SDK_INT >= 28) {
            return object.getResPackage();
        }
        try {
            object = (String)object.getClass().getMethod("getResPackage", new Class[0]).invoke(object, new Object[0]);
            return object;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon package", (Throwable)noSuchMethodException);
            return null;
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon package", (Throwable)invocationTargetException);
            return null;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon package", (Throwable)illegalAccessException);
            return null;
        }
    }

    private static Resources getResources(Context context, String string2) {
        block4 : {
            ApplicationInfo applicationInfo;
            if ("android".equals(string2)) {
                return Resources.getSystem();
            }
            context = context.getPackageManager();
            try {
                applicationInfo = context.getApplicationInfo(string2, 8192);
                if (applicationInfo == null) break block4;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.e((String)"IconCompat", (String)String.format("Unable to find pkg=%s for icon", string2), (Throwable)nameNotFoundException);
                return null;
            }
            context = context.getResourcesForApplication(applicationInfo);
            return context;
        }
        return null;
    }

    @RequiresApi(value=23)
    private static int getType(@NonNull Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            return icon.getType();
        }
        try {
            int n = (Integer)icon.getClass().getMethod("getType", new Class[0]).invoke((Object)icon, new Object[0]);
            return n;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get icon type ");
            stringBuilder.append((Object)icon);
            Log.e((String)"IconCompat", (String)stringBuilder.toString(), (Throwable)noSuchMethodException);
            return -1;
        }
        catch (InvocationTargetException invocationTargetException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get icon type ");
            stringBuilder.append((Object)icon);
            Log.e((String)"IconCompat", (String)stringBuilder.toString(), (Throwable)invocationTargetException);
            return -1;
        }
        catch (IllegalAccessException illegalAccessException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to get icon type ");
            stringBuilder.append((Object)icon);
            Log.e((String)"IconCompat", (String)stringBuilder.toString(), (Throwable)illegalAccessException);
            return -1;
        }
    }

    @Nullable
    @RequiresApi(value=23)
    private static Uri getUri(@NonNull Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            return icon.getUri();
        }
        try {
            icon = (Uri)icon.getClass().getMethod("getUri", new Class[0]).invoke((Object)icon, new Object[0]);
            return icon;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon uri", (Throwable)noSuchMethodException);
            return null;
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon uri", (Throwable)invocationTargetException);
            return null;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.e((String)"IconCompat", (String)"Unable to get icon uri", (Throwable)illegalAccessException);
            return null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Drawable loadDrawableInner(Context context) {
        String string2;
        int n = this.mType;
        if (n == 1) return new BitmapDrawable(context.getResources(), (Bitmap)this.mObj1);
        if (n != 2) {
            if (n == 3) return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray((byte[])((byte[])this.mObj1), (int)this.mInt1, (int)this.mInt2));
            if (n != 4) {
                if (n == 5) return new BitmapDrawable(context.getResources(), IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                return null;
            }
            Uri uri = Uri.parse((String)((String)this.mObj1));
            String string3 = uri.getScheme();
            InputStream inputStream = null;
            InputStream inputStream2 = null;
            if (!"content".equals(string3) && !"file".equals(string3)) {
                try {
                    inputStream2 = new FileInputStream(new File((String)this.mObj1));
                }
                catch (FileNotFoundException fileNotFoundException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to load image from path: ");
                    stringBuilder.append((Object)uri);
                    Log.w((String)"IconCompat", (String)stringBuilder.toString(), (Throwable)fileNotFoundException);
                    inputStream2 = inputStream;
                }
            } else {
                try {
                    inputStream2 = inputStream = context.getContentResolver().openInputStream(uri);
                }
                catch (Exception exception) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to load image from URI: ");
                    stringBuilder.append((Object)uri);
                    Log.w((String)"IconCompat", (String)stringBuilder.toString(), (Throwable)exception);
                }
            }
            if (inputStream2 == null) return null;
            return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream((InputStream)inputStream2));
        }
        String string4 = string2 = this.getResPackage();
        if (TextUtils.isEmpty((CharSequence)string2)) {
            string4 = context.getPackageName();
        }
        string4 = IconCompat.getResources(context, string4);
        try {
            return ResourcesCompat.getDrawable((Resources)string4, this.mInt1, context.getTheme());
        }
        catch (RuntimeException runtimeException) {
            Log.e((String)"IconCompat", (String)String.format("Unable to load resource 0x%08x from pkg=%s", this.mInt1, this.mObj1), (Throwable)runtimeException);
        }
        return null;
    }

    private static String typeToString(int n) {
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    if (n != 4) {
                        if (n != 5) {
                            return "UNKNOWN";
                        }
                        return "BITMAP_MASKABLE";
                    }
                    return "URI";
                }
                return "DATA";
            }
            return "RESOURCE";
        }
        return "BITMAP";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void addToShortcutIntent(@NonNull Intent intent, @Nullable Drawable object, @NonNull Context context) {
        this.checkResource(context);
        int n = this.mType;
        if (n != 1) {
            if (n != 2) {
                if (n != 5) {
                    throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
                }
                context = IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
            } else {
                try {
                    context = context.createPackageContext(this.getResPackage(), 0);
                    if (object == null) {
                        intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent.ShortcutIconResource.fromContext((Context)context, (int)this.mInt1));
                        return;
                    }
                    Drawable drawable2 = ContextCompat.getDrawable(context, this.mInt1);
                    if (drawable2.getIntrinsicWidth() > 0 && drawable2.getIntrinsicHeight() > 0) {
                        context = Bitmap.createBitmap((int)drawable2.getIntrinsicWidth(), (int)drawable2.getIntrinsicHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
                    } else {
                        n = ((ActivityManager)context.getSystemService("activity")).getLauncherLargeIconSize();
                        context = Bitmap.createBitmap((int)n, (int)n, (Bitmap.Config)Bitmap.Config.ARGB_8888);
                    }
                    drawable2.setBounds(0, 0, context.getWidth(), context.getHeight());
                    drawable2.draw(new Canvas((Bitmap)context));
                }
                catch (PackageManager.NameNotFoundException nameNotFoundException) {
                    object = new StringBuilder();
                    object.append("Can't find package ");
                    object.append(this.mObj1);
                    throw new IllegalArgumentException(object.toString(), (Throwable)nameNotFoundException);
                }
            }
        } else {
            Bitmap bitmap = (Bitmap)this.mObj1;
            context = bitmap;
            if (object != null) {
                context = bitmap.copy(bitmap.getConfig(), true);
            }
        }
        if (object != null) {
            n = context.getWidth();
            int n2 = context.getHeight();
            object.setBounds(n / 2, n2 / 2, n, n2);
            object.draw(new Canvas((Bitmap)context));
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)context);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void checkResource(Context object) {
        if (this.mType == 2) {
            String string2 = (String)this.mObj1;
            if (!string2.contains(":")) {
                return;
            }
            String string3 = string2.split(":", -1)[1];
            String string4 = string3.split("/", -1)[0];
            string3 = string3.split("/", -1)[1];
            int n = IconCompat.getResources((Context)object, string2 = string2.split(":", -1)[0]).getIdentifier(string3, string4, string2);
            if (this.mInt1 != n) {
                object = new StringBuilder();
                object.append("Id has changed for ");
                object.append(string2);
                object.append("/");
                object.append(string3);
                Log.i((String)"IconCompat", (String)object.toString());
                this.mInt1 = n;
            }
        }
    }

    @IdRes
    public int getResId() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return IconCompat.getResId((Icon)this.mObj1);
        }
        if (this.mType == 2) {
            return this.mInt1;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResId() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @NonNull
    public String getResPackage() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return IconCompat.getResPackage((Icon)this.mObj1);
        }
        if (this.mType == 2) {
            return ((String)this.mObj1).split(":", -1)[0];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("called getResPackage() on ");
        stringBuilder.append(this);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int getType() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return IconCompat.getType((Icon)this.mObj1);
        }
        return this.mType;
    }

    @NonNull
    public Uri getUri() {
        if (this.mType == -1 && Build.VERSION.SDK_INT >= 23) {
            return IconCompat.getUri((Icon)this.mObj1);
        }
        return Uri.parse((String)((String)this.mObj1));
    }

    public Drawable loadDrawable(Context context) {
        this.checkResource(context);
        if (Build.VERSION.SDK_INT >= 23) {
            return this.toIcon().loadDrawable(context);
        }
        if ((context = this.loadDrawableInner(context)) != null && (this.mTintList != null || this.mTintMode != DEFAULT_TINT_MODE)) {
            context.mutate();
            DrawableCompat.setTintList((Drawable)context, this.mTintList);
            DrawableCompat.setTintMode((Drawable)context, this.mTintMode);
        }
        return context;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void onPostParceling() {
        this.mTintMode = PorterDuff.Mode.valueOf((String)this.mTintModeStr);
        var1_1 = this.mType;
        if (var1_1 == -1) {
            var2_3 = this.mParcelable;
            if (var2_3 == null) throw new IllegalArgumentException("Invalid icon");
            this.mObj1 = var2_3;
            return;
        }
        if (var1_1 == 1) ** GOTO lbl19
        if (var1_1 == 2) ** GOTO lbl-1000
        if (var1_1 == 3) {
            this.mObj1 = this.mData;
            return;
        }
        if (var1_1 != 4) {
            if (var1_1 != 5) {
                return;
            }
        } else lbl-1000: // 2 sources:
        {
            this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
            return;
        }
lbl19: // 2 sources:
        if ((var2_2 = this.mParcelable) != null) {
            this.mObj1 = var2_2;
            return;
        }
        this.mObj1 = var2_2 = this.mData;
        this.mType = 3;
        this.mInt1 = 0;
        this.mInt2 = var2_2.length;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onPreParceling(boolean bl) {
        this.mTintModeStr = this.mTintMode.name();
        int n = this.mType;
        if (n != -1) {
            if (n != 1) {
                if (n == 2) {
                    this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
                    return;
                }
                if (n == 3) {
                    this.mData = (byte[])this.mObj1;
                    return;
                }
                if (n == 4) {
                    this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                    return;
                }
                if (n != 5) {
                    return;
                }
            }
            if (bl) {
                Bitmap bitmap = (Bitmap)this.mObj1;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, (OutputStream)byteArrayOutputStream);
                this.mData = byteArrayOutputStream.toByteArray();
                return;
            }
            this.mParcelable = (Parcelable)this.mObj1;
            return;
        }
        if (!bl) {
            this.mParcelable = (Parcelable)this.mObj1;
            return;
        }
        throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
    }

    public IconCompat setTint(@ColorInt int n) {
        return this.setTintList(ColorStateList.valueOf((int)n));
    }

    public IconCompat setTintList(ColorStateList colorStateList) {
        this.mTintList = colorStateList;
        return this;
    }

    public IconCompat setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        return this;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public Bundle toBundle() {
        var2_1 = new Bundle();
        var1_2 = this.mType;
        if (var1_2 == -1) ** GOTO lbl16
        if (var1_2 == 1) ** GOTO lbl14
        if (var1_2 == 2) ** GOTO lbl12
        if (var1_2 == 3) ** GOTO lbl10
        if (var1_2 == 4) ** GOTO lbl12
        if (var1_2 != 5) throw new IllegalArgumentException("Invalid icon");
        ** GOTO lbl14
lbl10: // 1 sources:
        var2_1.putByteArray("obj", (byte[])this.mObj1);
        ** GOTO lbl17
lbl12: // 2 sources:
        var2_1.putString("obj", (String)this.mObj1);
        ** GOTO lbl17
lbl14: // 2 sources:
        var2_1.putParcelable("obj", (Parcelable)((Bitmap)this.mObj1));
        ** GOTO lbl17
lbl16: // 1 sources:
        var2_1.putParcelable("obj", (Parcelable)this.mObj1);
lbl17: // 4 sources:
        var2_1.putInt("type", this.mType);
        var2_1.putInt("int1", this.mInt1);
        var2_1.putInt("int2", this.mInt2);
        var3_3 = this.mTintList;
        if (var3_3 != null) {
            var2_1.putParcelable("tint_list", (Parcelable)var3_3);
        }
        if ((var3_3 = this.mTintMode) == IconCompat.DEFAULT_TINT_MODE) return var2_1;
        var2_1.putString("tint_mode", var3_3.name());
        return var2_1;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @RequiresApi(value=23)
    public Icon toIcon() {
        Icon icon;
        int n = this.mType;
        if (n == -1) return (Icon)this.mObj1;
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    if (n != 4) {
                        if (n != 5) throw new IllegalArgumentException("Unknown type");
                        icon = Build.VERSION.SDK_INT >= 26 ? Icon.createWithAdaptiveBitmap((Bitmap)((Bitmap)this.mObj1)) : Icon.createWithBitmap((Bitmap)IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                    } else {
                        icon = Icon.createWithContentUri((String)((String)this.mObj1));
                    }
                } else {
                    icon = Icon.createWithData((byte[])((byte[])this.mObj1), (int)this.mInt1, (int)this.mInt2);
                }
            } else {
                icon = Icon.createWithResource((String)this.getResPackage(), (int)this.mInt1);
            }
        } else {
            icon = Icon.createWithBitmap((Bitmap)((Bitmap)this.mObj1));
        }
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            icon.setTintList(colorStateList);
        }
        if ((colorStateList = this.mTintMode) == DEFAULT_TINT_MODE) return icon;
        icon.setTintMode((PorterDuff.Mode)colorStateList);
        return icon;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public String toString() {
        if (this.mType == -1) {
            return String.valueOf(this.mObj1);
        }
        var2_1 = new StringBuilder("Icon(typ=").append(IconCompat.typeToString(this.mType));
        var1_2 = this.mType;
        if (var1_2 == 1) ** GOTO lbl25
        if (var1_2 == 2) ** GOTO lbl20
        if (var1_2 == 3) ** GOTO lbl14
        if (var1_2 == 4) ** GOTO lbl11
        if (var1_2 == 5) ** GOTO lbl25
        ** GOTO lbl29
lbl11: // 1 sources:
        var2_1.append(" uri=");
        var2_1.append(this.mObj1);
        ** GOTO lbl29
lbl14: // 1 sources:
        var2_1.append(" len=");
        var2_1.append(this.mInt1);
        if (this.mInt2 != 0) {
            var2_1.append(" off=");
            var2_1.append(this.mInt2);
        }
        ** GOTO lbl29
lbl20: // 1 sources:
        var2_1.append(" pkg=");
        var2_1.append(this.getResPackage());
        var2_1.append(" id=");
        var2_1.append(String.format("0x%08x", new Object[]{this.getResId()}));
        ** GOTO lbl29
lbl25: // 2 sources:
        var2_1.append(" size=");
        var2_1.append(((Bitmap)this.mObj1).getWidth());
        var2_1.append("x");
        var2_1.append(((Bitmap)this.mObj1).getHeight());
lbl29: // 5 sources:
        if (this.mTintList != null) {
            var2_1.append(" tint=");
            var2_1.append((Object)this.mTintList);
        }
        if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            var2_1.append(" mode=");
            var2_1.append((Object)this.mTintMode);
        }
        var2_1.append(")");
        return var2_1.toString();
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    public static @interface IconType {
    }

}

