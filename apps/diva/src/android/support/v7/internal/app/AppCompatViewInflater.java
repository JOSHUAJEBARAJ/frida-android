/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.InflateException
 *  android.view.View
 *  android.view.View$OnClickListener
 */
package android.support.v7.internal.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class AppCompatViewInflater {
    private static final String LOG_TAG = "AppCompatViewInflater";
    private static final Map<String, Constructor<? extends View>> sConstructorMap;
    private static final Class<?>[] sConstructorSignature;
    private static final int[] sOnClickAttrs;
    private final Object[] mConstructorArgs = new Object[2];

    static {
        sConstructorSignature = new Class[]{Context.class, AttributeSet.class};
        sOnClickAttrs = new int[]{16843375};
        sConstructorMap = new ArrayMap<String, Constructor<? extends View>>();
    }

    private void checkOnClickListener(View view, AttributeSet attributeSet) {
        Object object = view.getContext();
        if (!ViewCompat.hasOnClickListeners(view) || !(object instanceof ContextWrapper)) {
            return;
        }
        attributeSet = object.obtainStyledAttributes(attributeSet, sOnClickAttrs);
        object = attributeSet.getString(0);
        if (object != null) {
            view.setOnClickListener((View.OnClickListener)new DeclaredOnClickListener(view, (String)object));
        }
        attributeSet.recycle();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private View createView(Context var1_1, String var2_3, String var3_4) throws ClassNotFoundException, InflateException {
        var5_5 = AppCompatViewInflater.sConstructorMap.get(var2_3);
        var4_6 = var5_5;
        if (var5_5 != null) ** GOTO lbl9
        try {
            var4_6 = var1_1.getClassLoader();
            var1_1 = var3_4 != null ? var3_4 + var2_3 : var2_3;
            var4_6 = var4_6.loadClass((String)var1_1).asSubclass(View.class).getConstructor(AppCompatViewInflater.sConstructorSignature);
            AppCompatViewInflater.sConstructorMap.put(var2_3, var4_6);
lbl9: // 2 sources:
            var4_6.setAccessible(true);
            return (View)var4_6.newInstance(this.mConstructorArgs);
        }
        catch (Exception var1_2) {
            return null;
        }
    }

    private View createViewFromTag(Context context, String string2, AttributeSet attributeSet) {
        String string3 = string2;
        if (string2.equals("view")) {
            string3 = attributeSet.getAttributeValue(null, "class");
        }
        try {
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = attributeSet;
            if (-1 == string3.indexOf(46)) {
                context = this.createView(context, string3, "android.widget.");
                return context;
            }
            context = this.createView(context, string3, null);
            return context;
        }
        catch (Exception exception) {
            return null;
        }
        finally {
            this.mConstructorArgs[0] = null;
            this.mConstructorArgs[1] = null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Context themifyContext(Context context, AttributeSet object, boolean bl, boolean bl2) {
        object = context.obtainStyledAttributes((AttributeSet)object, R.styleable.View, 0, 0);
        int n = 0;
        if (bl) {
            n = object.getResourceId(R.styleable.View_android_theme, 0);
        }
        int n2 = n;
        if (bl2) {
            n2 = n;
            if (n == 0) {
                n2 = n = object.getResourceId(R.styleable.View_theme, 0);
                if (n != 0) {
                    Log.i((String)"AppCompatViewInflater", (String)"app:theme is now deprecated. Please move to using android:theme instead.");
                    n2 = n;
                }
            }
        }
        object.recycle();
        object = context;
        if (n2 == 0) return object;
        if (!(context instanceof ContextThemeWrapper)) return new ContextThemeWrapper(context, n2);
        object = context;
        if (((ContextThemeWrapper)context).getThemeResId() == n2) return object;
        return new ContextThemeWrapper(context, n2);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public final View createView(View var1_1, String var2_2, @NonNull Context var3_3, @NonNull AttributeSet var4_4, boolean var5_5, boolean var6_6, boolean var7_7) {
        block49 : {
            var9_8 = var3_3;
            if (var5_5) {
                var9_8 = var3_3;
                if (var1_1 != null) {
                    var9_8 = var1_1.getContext();
                }
            }
            var1_1 = var9_8;
            if (!var6_6) {
                var9_8 = var1_1;
                if (!var7_7) break block49;
            }
            var9_8 = AppCompatViewInflater.themifyContext((Context)var1_1, var4_4, var6_6, var7_7);
        }
        var1_1 = null;
        var8_9 = -1;
        switch (var2_2.hashCode()) {
            case -938935918: {
                if (var2_2.equals("TextView")) {
                    var8_9 = 0;
                    ** break;
                }
                ** GOTO lbl73
            }
            case 1125864064: {
                if (var2_2.equals("ImageView")) {
                    var8_9 = 1;
                    ** break;
                }
                ** GOTO lbl73
            }
            case 2001146706: {
                if (var2_2.equals("Button")) {
                    var8_9 = 2;
                    ** break;
                }
                ** GOTO lbl73
            }
            case 1666676343: {
                if (var2_2.equals("EditText")) {
                    var8_9 = 3;
                    ** break;
                }
                ** GOTO lbl73
            }
            case -339785223: {
                if (var2_2.equals("Spinner")) {
                    var8_9 = 4;
                    ** break;
                }
                ** GOTO lbl73
            }
            case -937446323: {
                if (var2_2.equals("ImageButton")) {
                    var8_9 = 5;
                    ** break;
                }
                ** GOTO lbl73
            }
            case 1601505219: {
                if (var2_2.equals("CheckBox")) {
                    var8_9 = 6;
                    ** break;
                }
                ** GOTO lbl73
            }
            case 776382189: {
                if (var2_2.equals("RadioButton")) {
                    var8_9 = 7;
                    ** break;
                }
                ** GOTO lbl73
            }
            case -1455429095: {
                if (var2_2.equals("CheckedTextView")) {
                    var8_9 = 8;
                    ** break;
                }
                ** GOTO lbl73
            }
            case 1413872058: {
                if (var2_2.equals("AutoCompleteTextView")) {
                    var8_9 = 9;
                    ** break;
                }
                ** GOTO lbl73
            }
            case -1346021293: {
                if (var2_2.equals("MultiAutoCompleteTextView")) {
                    var8_9 = 10;
                    ** break;
                }
                ** GOTO lbl73
            }
            case -1946472170: {
                if (var2_2.equals("RatingBar")) {
                    var8_9 = 11;
                }
            }
lbl73: // 26 sources:
            default: {
                ** GOTO lbl78
            }
            case -658531749: 
        }
        if (var2_2.equals("SeekBar")) {
            var8_9 = 12;
        }
lbl78: // 4 sources:
        switch (var8_9) {
            case 0: {
                var1_1 = new AppCompatTextView((Context)var9_8, var4_4);
                ** break;
            }
            case 1: {
                var1_1 = new AppCompatImageView((Context)var9_8, var4_4);
                ** break;
            }
            case 2: {
                var1_1 = new AppCompatButton((Context)var9_8, var4_4);
                ** break;
            }
            case 3: {
                var1_1 = new AppCompatEditText((Context)var9_8, var4_4);
                ** break;
            }
            case 4: {
                var1_1 = new AppCompatSpinner((Context)var9_8, var4_4);
                ** break;
            }
            case 5: {
                var1_1 = new AppCompatImageButton((Context)var9_8, var4_4);
                ** break;
            }
            case 6: {
                var1_1 = new AppCompatCheckBox((Context)var9_8, var4_4);
                ** break;
            }
            case 7: {
                var1_1 = new AppCompatRadioButton((Context)var9_8, var4_4);
                ** break;
            }
            case 8: {
                var1_1 = new AppCompatCheckedTextView((Context)var9_8, var4_4);
                ** break;
            }
            case 9: {
                var1_1 = new AppCompatAutoCompleteTextView((Context)var9_8, var4_4);
                ** break;
            }
            case 10: {
                var1_1 = new AppCompatMultiAutoCompleteTextView((Context)var9_8, var4_4);
                ** break;
            }
            case 11: {
                var1_1 = new AppCompatRatingBar((Context)var9_8, var4_4);
            }
lbl114: // 13 sources:
            default: {
                ** GOTO lbl118
            }
            case 12: 
        }
        var1_1 = new AppCompatSeekBar((Context)var9_8, var4_4);
lbl118: // 2 sources:
        var10_10 = var1_1;
        if (var1_1 == null) {
            var10_10 = var1_1;
            if (var3_3 != var9_8) {
                var10_10 = this.createViewFromTag((Context)var9_8, var2_2, var4_4);
            }
        }
        if (var10_10 == null) return var10_10;
        this.checkOnClickListener((View)var10_10, var4_4);
        return var10_10;
    }

    private static class DeclaredOnClickListener
    implements View.OnClickListener {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;

        public DeclaredOnClickListener(@NonNull View view, @NonNull String string2) {
            this.mHostView = view;
            this.mMethodName = string2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @NonNull
        private void resolveMethod(@Nullable Context object, @NonNull String object2) {
            while (object != null) {
                try {
                    if (!object.isRestricted() && (object2 = object.getClass().getMethod(this.mMethodName, View.class)) != null) {
                        this.mResolvedMethod = object2;
                        this.mResolvedContext = object;
                        return;
                    }
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
                if (object instanceof ContextWrapper) {
                    object = ((ContextWrapper)object).getBaseContext();
                    continue;
                }
                object = null;
            }
            int n = this.mHostView.getId();
            if (n == -1) {
                object = "";
                throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + this.mHostView.getClass() + (String)object);
            }
            object = " with id '" + this.mHostView.getContext().getResources().getResourceEntryName(n) + "'";
            throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + this.mHostView.getClass() + (String)object);
        }

        public void onClick(@NonNull View view) {
            if (this.mResolvedMethod == null) {
                this.resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke((Object)this.mResolvedContext, new Object[]{view});
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new IllegalStateException("Could not execute method for android:onClick", invocationTargetException);
            }
        }
    }

}

