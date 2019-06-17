/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.util.AttributeSet
 *  android.widget.ProgressBar
 *  android.widget.RatingBar
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.widget.AppCompatProgressBarHelper;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RatingBar;

public class AppCompatRatingBar
extends RatingBar {
    private AppCompatProgressBarHelper mAppCompatProgressBarHelper;
    private TintManager mTintManager;

    public AppCompatRatingBar(Context context) {
        this(context, null);
    }

    public AppCompatRatingBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.ratingBarStyle);
    }

    public AppCompatRatingBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mTintManager = TintManager.get(context);
        this.mAppCompatProgressBarHelper = new AppCompatProgressBarHelper((ProgressBar)this, this.mTintManager);
        this.mAppCompatProgressBarHelper.loadFromAttributes(attributeSet, n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onMeasure(int n, int n2) {
        synchronized (this) {
            super.onMeasure(n, n2);
            Bitmap bitmap = this.mAppCompatProgressBarHelper.getSampleTime();
            if (bitmap != null) {
                this.setMeasuredDimension(ViewCompat.resolveSizeAndState(bitmap.getWidth() * this.getNumStars(), n, 0), this.getMeasuredHeight());
            }
            return;
        }
    }
}

