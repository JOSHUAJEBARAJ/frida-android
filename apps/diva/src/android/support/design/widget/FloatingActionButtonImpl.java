/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.view.View
 */
package android.support.design.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.design.widget.CircularBorderDrawable;
import android.support.design.widget.ShadowViewDelegate;
import android.view.View;

abstract class FloatingActionButtonImpl {
    static final int[] EMPTY_STATE_SET;
    static final int[] FOCUSED_ENABLED_STATE_SET;
    static final int[] PRESSED_ENABLED_STATE_SET;
    static final int SHOW_HIDE_ANIM_DURATION = 200;
    final ShadowViewDelegate mShadowViewDelegate;
    final View mView;

    static {
        PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
        FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
        EMPTY_STATE_SET = new int[0];
    }

    FloatingActionButtonImpl(View view, ShadowViewDelegate shadowViewDelegate) {
        this.mView = view;
        this.mShadowViewDelegate = shadowViewDelegate;
    }

    Drawable createBorderDrawable(int n, ColorStateList colorStateList) {
        Resources resources = this.mView.getResources();
        CircularBorderDrawable circularBorderDrawable = this.newCircularDrawable();
        circularBorderDrawable.setGradientColors(resources.getColor(R.color.design_fab_stroke_top_outer_color), resources.getColor(R.color.design_fab_stroke_top_inner_color), resources.getColor(R.color.design_fab_stroke_end_inner_color), resources.getColor(R.color.design_fab_stroke_end_outer_color));
        circularBorderDrawable.setBorderWidth(n);
        circularBorderDrawable.setTintColor(colorStateList.getDefaultColor());
        return circularBorderDrawable;
    }

    abstract void hide(@Nullable InternalVisibilityChangedListener var1);

    abstract void jumpDrawableToCurrentState();

    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawable();
    }

    abstract void onDrawableStateChanged(int[] var1);

    abstract void setBackgroundDrawable(Drawable var1, ColorStateList var2, PorterDuff.Mode var3, int var4, int var5);

    abstract void setBackgroundTintList(ColorStateList var1);

    abstract void setBackgroundTintMode(PorterDuff.Mode var1);

    abstract void setElevation(float var1);

    abstract void setPressedTranslationZ(float var1);

    abstract void setRippleColor(int var1);

    abstract void show(@Nullable InternalVisibilityChangedListener var1);

    static interface InternalVisibilityChangedListener {
        public void onHidden();

        public void onShown();
    }

}

