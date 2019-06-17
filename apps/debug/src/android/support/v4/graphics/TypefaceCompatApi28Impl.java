/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Typeface
 */
package android.support.v4.graphics;

import android.graphics.Typeface;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.TypefaceCompatApi26Impl;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(value=28)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatApi28Impl
extends TypefaceCompatApi26Impl {
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi28Impl";

    @Override
    protected Typeface createFromFamiliesWithDefault(Object object) {
        void var1_4;
        try {
            Object object2 = Array.newInstance(this.mFontFamily, 1);
            Array.set(object2, 0, object);
            object = (Typeface)this.mCreateFromFamiliesWithDefault.invoke(null, object2, "sans-serif", -1, -1);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        throw new RuntimeException((Throwable)var1_4);
    }

    @Override
    protected Method obtainCreateFromFamiliesWithDefaultMethod(Class genericDeclaration) throws NoSuchMethodException {
        genericDeclaration = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(genericDeclaration, 1).getClass(), String.class, Integer.TYPE, Integer.TYPE);
        genericDeclaration.setAccessible(true);
        return genericDeclaration;
    }
}

