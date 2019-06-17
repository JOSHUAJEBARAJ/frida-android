/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.RadialGradient
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.ShapeDrawable
 *  android.graphics.drawable.shapes.OvalShape
 *  android.graphics.drawable.shapes.Shape
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.DisplayMetrics
 *  android.view.View
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.widget.ImageView
 */
package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

class CircleImageView
extends ImageView {
    private static final int FILL_SHADOW_COLOR = 1023410176;
    private static final int KEY_SHADOW_COLOR = 503316480;
    private static final int SHADOW_ELEVATION = 4;
    private static final float SHADOW_RADIUS = 3.5f;
    private static final float X_OFFSET = 0.0f;
    private static final float Y_OFFSET = 1.75f;
    private Animation.AnimationListener mListener;
    private int mShadowRadius;

    /*
     * Enabled aggressive block sorting
     */
    public CircleImageView(Context context, int n, float f) {
        super(context);
        float f2 = this.getContext().getResources().getDisplayMetrics().density;
        int n2 = (int)(f * f2 * 2.0f);
        int n3 = (int)(1.75f * f2);
        int n4 = (int)(0.0f * f2);
        this.mShadowRadius = (int)(3.5f * f2);
        if (this.elevationSupported()) {
            context = new ShapeDrawable((Shape)new OvalShape());
            ViewCompat.setElevation((View)this, 4.0f * f2);
        } else {
            context = new ShapeDrawable((Shape)new OvalShadow(this.mShadowRadius, n2));
            ViewCompat.setLayerType((View)this, 1, context.getPaint());
            context.getPaint().setShadowLayer((float)this.mShadowRadius, (float)n4, (float)n3, 503316480);
            n2 = this.mShadowRadius;
            this.setPadding(n2, n2, n2, n2);
        }
        context.getPaint().setColor(n);
        this.setBackgroundDrawable((Drawable)context);
    }

    private boolean elevationSupported() {
        if (Build.VERSION.SDK_INT >= 21) {
            return true;
        }
        return false;
    }

    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (this.mListener != null) {
            this.mListener.onAnimationEnd(this.getAnimation());
        }
    }

    public void onAnimationStart() {
        super.onAnimationStart();
        if (this.mListener != null) {
            this.mListener.onAnimationStart(this.getAnimation());
        }
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        if (!this.elevationSupported()) {
            this.setMeasuredDimension(this.getMeasuredWidth() + this.mShadowRadius * 2, this.getMeasuredHeight() + this.mShadowRadius * 2);
        }
    }

    public void setAnimationListener(Animation.AnimationListener animationListener) {
        this.mListener = animationListener;
    }

    public void setBackgroundColor(int n) {
        if (this.getBackground() instanceof ShapeDrawable) {
            ((ShapeDrawable)this.getBackground()).getPaint().setColor(n);
        }
    }

    public void setBackgroundColorRes(int n) {
        this.setBackgroundColor(this.getContext().getResources().getColor(n));
    }

    private class OvalShadow
    extends OvalShape {
        private int mCircleDiameter;
        private RadialGradient mRadialGradient;
        private Paint mShadowPaint;

        public OvalShadow(int n, int n2) {
            this.mShadowPaint = new Paint();
            CircleImageView.this.mShadowRadius = n;
            this.mCircleDiameter = n2;
            float f = this.mCircleDiameter / 2;
            float f2 = this.mCircleDiameter / 2;
            float f3 = CircleImageView.this.mShadowRadius;
            CircleImageView.this = Shader.TileMode.CLAMP;
            this.mRadialGradient = new RadialGradient(f, f2, f3, new int[]{1023410176, 0}, null, (Shader.TileMode)CircleImageView.this);
            this.mShadowPaint.setShader((Shader)this.mRadialGradient);
        }

        public void draw(Canvas canvas, Paint paint) {
            int n = CircleImageView.this.getWidth();
            int n2 = CircleImageView.this.getHeight();
            canvas.drawCircle((float)(n / 2), (float)(n2 / 2), (float)(this.mCircleDiameter / 2 + CircleImageView.this.mShadowRadius), this.mShadowPaint);
            canvas.drawCircle((float)(n / 2), (float)(n2 / 2), (float)(this.mCircleDiameter / 2), paint);
        }
    }

}

