/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.drawable.Drawable
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.ResourcesWrapper;
import android.support.v7.internal.widget.TintManager;

public class TintContextWrapper
extends ContextWrapper {
    private Resources mResources;

    private TintContextWrapper(Context context) {
        super(context);
    }

    public static Context wrap(Context context) {
        Object object = context;
        if (!(context instanceof TintContextWrapper)) {
            object = new TintContextWrapper(context);
        }
        return object;
    }

    public Resources getResources() {
        if (this.mResources == null) {
            this.mResources = new TintResources(super.getResources(), TintManager.get((Context)this));
        }
        return this.mResources;
    }

    static class TintResources
    extends ResourcesWrapper {
        private final TintManager mTintManager;

        public TintResources(Resources resources, TintManager tintManager) {
            super(resources);
            this.mTintManager = tintManager;
        }

        @Override
        public Drawable getDrawable(int n) throws Resources.NotFoundException {
            Drawable drawable2 = super.getDrawable(n);
            if (drawable2 != null) {
                this.mTintManager.tintDrawableUsingColorFilter(n, drawable2);
            }
            return drawable2;
        }
    }

}

