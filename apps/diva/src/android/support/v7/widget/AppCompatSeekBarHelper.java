/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.widget.ProgressBar
 *  android.widget.SeekBar
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.widget.AppCompatProgressBarHelper;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.SeekBar;

class AppCompatSeekBarHelper
extends AppCompatProgressBarHelper {
    private static final int[] TINT_ATTRS = new int[]{16843074};
    private final SeekBar mView;

    AppCompatSeekBarHelper(SeekBar seekBar, TintManager tintManager) {
        super((ProgressBar)seekBar, tintManager);
        this.mView = seekBar;
    }

    @Override
    void loadFromAttributes(AttributeSet object, int n) {
        super.loadFromAttributes((AttributeSet)object, n);
        object = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), (AttributeSet)object, TINT_ATTRS, n, 0);
        Drawable drawable2 = object.getDrawableIfKnown(0);
        if (drawable2 != null) {
            this.mView.setThumb(drawable2);
        }
        object.recycle();
    }
}

