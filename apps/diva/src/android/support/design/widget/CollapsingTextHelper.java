/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.view.View
 *  android.view.animation.Interpolator
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.MathUtils;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Interpolator;

final class CollapsingTextHelper {
    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT;
    private static final boolean USE_SCALING_TEXTURE;
    private boolean mBoundsChanged;
    private final Rect mCollapsedBounds;
    private float mCollapsedDrawX;
    private float mCollapsedDrawY;
    private int mCollapsedShadowColor;
    private float mCollapsedShadowDx;
    private float mCollapsedShadowDy;
    private float mCollapsedShadowRadius;
    private int mCollapsedTextColor;
    private int mCollapsedTextGravity = 16;
    private float mCollapsedTextSize = 15.0f;
    private Typeface mCollapsedTypeface;
    private final RectF mCurrentBounds;
    private float mCurrentDrawX;
    private float mCurrentDrawY;
    private float mCurrentTextSize;
    private Typeface mCurrentTypeface;
    private boolean mDrawTitle;
    private final Rect mExpandedBounds;
    private float mExpandedDrawX;
    private float mExpandedDrawY;
    private float mExpandedFraction;
    private int mExpandedShadowColor;
    private float mExpandedShadowDx;
    private float mExpandedShadowDy;
    private float mExpandedShadowRadius;
    private int mExpandedTextColor;
    private int mExpandedTextGravity = 16;
    private float mExpandedTextSize = 15.0f;
    private Bitmap mExpandedTitleTexture;
    private Typeface mExpandedTypeface;
    private boolean mIsRtl;
    private Interpolator mPositionInterpolator;
    private float mScale;
    private CharSequence mText;
    private final TextPaint mTextPaint;
    private Interpolator mTextSizeInterpolator;
    private CharSequence mTextToDraw;
    private float mTextureAscent;
    private float mTextureDescent;
    private Paint mTexturePaint;
    private boolean mUseTexture;
    private final View mView;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = Build.VERSION.SDK_INT < 18;
        USE_SCALING_TEXTURE = bl;
        DEBUG_DRAW_PAINT = null;
        if (DEBUG_DRAW_PAINT != null) {
            DEBUG_DRAW_PAINT.setAntiAlias(true);
            DEBUG_DRAW_PAINT.setColor(-65281);
        }
    }

    public CollapsingTextHelper(View view) {
        this.mView = view;
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mCollapsedBounds = new Rect();
        this.mExpandedBounds = new Rect();
        this.mCurrentBounds = new RectF();
    }

    private static int blendColors(int n, int n2, float f) {
        float f2 = 1.0f - f;
        float f3 = Color.alpha((int)n);
        float f4 = Color.alpha((int)n2);
        float f5 = Color.red((int)n);
        float f6 = Color.red((int)n2);
        float f7 = Color.green((int)n);
        float f8 = Color.green((int)n2);
        float f9 = Color.blue((int)n);
        float f10 = Color.blue((int)n2);
        return Color.argb((int)((int)(f3 * f2 + f4 * f)), (int)((int)(f5 * f2 + f6 * f)), (int)((int)(f7 * f2 + f8 * f)), (int)((int)(f9 * f2 + f10 * f)));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void calculateBaseOffsets() {
        float f;
        float f2;
        int n = 1;
        float f3 = this.mCurrentTextSize;
        this.calculateUsingTextSize(this.mCollapsedTextSize);
        float f4 = this.mTextToDraw != null ? this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length()) : 0.0f;
        int n2 = this.mCollapsedTextGravity;
        int n3 = this.mIsRtl ? 1 : 0;
        n3 = GravityCompat.getAbsoluteGravity(n2, n3);
        switch (n3 & 112) {
            default: {
                f2 = (this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f;
                f = this.mTextPaint.descent();
                this.mCollapsedDrawY = (float)this.mCollapsedBounds.centerY() + (f2 - f);
                break;
            }
            case 80: {
                this.mCollapsedDrawY = this.mCollapsedBounds.bottom;
                break;
            }
            case 48: {
                this.mCollapsedDrawY = (float)this.mCollapsedBounds.top - this.mTextPaint.ascent();
                break;
            }
        }
        switch (n3 & 7) {
            default: {
                this.mCollapsedDrawX = this.mCollapsedBounds.left;
                break;
            }
            case 1: {
                this.mCollapsedDrawX = (float)this.mCollapsedBounds.centerX() - f4 / 2.0f;
                break;
            }
            case 5: {
                this.mCollapsedDrawX = (float)this.mCollapsedBounds.right - f4;
            }
        }
        this.calculateUsingTextSize(this.mExpandedTextSize);
        f4 = this.mTextToDraw != null ? this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length()) : 0.0f;
        n2 = this.mExpandedTextGravity;
        n3 = this.mIsRtl ? n : 0;
        n3 = GravityCompat.getAbsoluteGravity(n2, n3);
        switch (n3 & 112) {
            default: {
                f2 = (this.mTextPaint.descent() - this.mTextPaint.ascent()) / 2.0f;
                f = this.mTextPaint.descent();
                this.mExpandedDrawY = (float)this.mExpandedBounds.centerY() + (f2 - f);
                break;
            }
            case 80: {
                this.mExpandedDrawY = this.mExpandedBounds.bottom;
                break;
            }
            case 48: {
                this.mExpandedDrawY = (float)this.mExpandedBounds.top - this.mTextPaint.ascent();
            }
        }
        switch (n3 & 7) {
            default: {
                this.mExpandedDrawX = this.mExpandedBounds.left;
                break;
            }
            case 1: {
                this.mExpandedDrawX = (float)this.mExpandedBounds.centerX() - f4 / 2.0f;
                break;
            }
            case 5: {
                this.mExpandedDrawX = (float)this.mExpandedBounds.right - f4;
            }
        }
        this.clearTexture();
        this.setInterpolatedTextSize(f3);
    }

    private void calculateCurrentOffsets() {
        this.calculateOffsets(this.mExpandedFraction);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean calculateIsRtl(CharSequence charSequence) {
        TextDirectionHeuristicCompat textDirectionHeuristicCompat;
        boolean bl = true;
        if (ViewCompat.getLayoutDirection(this.mView) != 1) {
            bl = false;
        }
        if (bl) {
            textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL;
            return textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        }
        textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        return textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void calculateOffsets(float f) {
        this.interpolateBounds(f);
        this.mCurrentDrawX = CollapsingTextHelper.lerp(this.mExpandedDrawX, this.mCollapsedDrawX, f, this.mPositionInterpolator);
        this.mCurrentDrawY = CollapsingTextHelper.lerp(this.mExpandedDrawY, this.mCollapsedDrawY, f, this.mPositionInterpolator);
        this.setInterpolatedTextSize(CollapsingTextHelper.lerp(this.mExpandedTextSize, this.mCollapsedTextSize, f, this.mTextSizeInterpolator));
        if (this.mCollapsedTextColor != this.mExpandedTextColor) {
            this.mTextPaint.setColor(CollapsingTextHelper.blendColors(this.mExpandedTextColor, this.mCollapsedTextColor, f));
        } else {
            this.mTextPaint.setColor(this.mCollapsedTextColor);
        }
        this.mTextPaint.setShadowLayer(CollapsingTextHelper.lerp(this.mExpandedShadowRadius, this.mCollapsedShadowRadius, f, null), CollapsingTextHelper.lerp(this.mExpandedShadowDx, this.mCollapsedShadowDx, f, null), CollapsingTextHelper.lerp(this.mExpandedShadowDy, this.mCollapsedShadowDy, f, null), CollapsingTextHelper.blendColors(this.mExpandedShadowColor, this.mCollapsedShadowColor, f));
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void calculateUsingTextSize(float f) {
        float f2;
        if (this.mText == null) {
            return;
        }
        boolean bl = false;
        boolean bl2 = false;
        if (CollapsingTextHelper.isClose(f, this.mCollapsedTextSize)) {
            float f3 = this.mCollapsedBounds.width();
            float f4 = this.mCollapsedTextSize;
            this.mScale = 1.0f;
            f = f3;
            f2 = f4;
            if (this.mCurrentTypeface != this.mCollapsedTypeface) {
                this.mCurrentTypeface = this.mCollapsedTypeface;
                bl2 = true;
                f2 = f4;
                f = f3;
            }
        } else {
            float f5 = this.mExpandedBounds.width();
            f2 = this.mExpandedTextSize;
            bl2 = bl;
            if (this.mCurrentTypeface != this.mExpandedTypeface) {
                this.mCurrentTypeface = this.mExpandedTypeface;
                bl2 = true;
            }
            if (CollapsingTextHelper.isClose(f, this.mExpandedTextSize)) {
                this.mScale = 1.0f;
                f = f5;
            } else {
                this.mScale = f / this.mExpandedTextSize;
                f = f5;
            }
        }
        bl = bl2;
        if (f > 0.0f) {
            bl2 = this.mCurrentTextSize != f2 || this.mBoundsChanged || bl2;
            this.mCurrentTextSize = f2;
            this.mBoundsChanged = false;
            bl = bl2;
        }
        if (this.mTextToDraw != null) {
            if (!bl) return;
        }
        this.mTextPaint.setTextSize(this.mCurrentTextSize);
        this.mTextPaint.setTypeface(this.mCurrentTypeface);
        CharSequence charSequence = TextUtils.ellipsize((CharSequence)this.mText, (TextPaint)this.mTextPaint, (float)f, (TextUtils.TruncateAt)TextUtils.TruncateAt.END);
        if (TextUtils.equals((CharSequence)charSequence, (CharSequence)this.mTextToDraw)) return;
        this.mTextToDraw = charSequence;
        this.mIsRtl = this.calculateIsRtl(this.mTextToDraw);
    }

    private void clearTexture() {
        if (this.mExpandedTitleTexture != null) {
            this.mExpandedTitleTexture.recycle();
            this.mExpandedTitleTexture = null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void ensureExpandedTexture() {
        if (this.mExpandedTitleTexture != null) return;
        if (this.mExpandedBounds.isEmpty()) return;
        if (TextUtils.isEmpty((CharSequence)this.mTextToDraw)) {
            return;
        }
        this.calculateOffsets(0.0f);
        this.mTextureAscent = this.mTextPaint.ascent();
        this.mTextureDescent = this.mTextPaint.descent();
        int n = Math.round(this.mTextPaint.measureText(this.mTextToDraw, 0, this.mTextToDraw.length()));
        int n2 = Math.round(this.mTextureDescent - this.mTextureAscent);
        if (n <= 0) {
            if (n2 <= 0) return;
        }
        this.mExpandedTitleTexture = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
        new Canvas(this.mExpandedTitleTexture).drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), 0.0f, (float)n2 - this.mTextPaint.descent(), (Paint)this.mTextPaint);
        if (this.mTexturePaint != null) return;
        this.mTexturePaint = new Paint(3);
    }

    private void interpolateBounds(float f) {
        this.mCurrentBounds.left = CollapsingTextHelper.lerp(this.mExpandedBounds.left, this.mCollapsedBounds.left, f, this.mPositionInterpolator);
        this.mCurrentBounds.top = CollapsingTextHelper.lerp(this.mExpandedDrawY, this.mCollapsedDrawY, f, this.mPositionInterpolator);
        this.mCurrentBounds.right = CollapsingTextHelper.lerp(this.mExpandedBounds.right, this.mCollapsedBounds.right, f, this.mPositionInterpolator);
        this.mCurrentBounds.bottom = CollapsingTextHelper.lerp(this.mExpandedBounds.bottom, this.mCollapsedBounds.bottom, f, this.mPositionInterpolator);
    }

    private static boolean isClose(float f, float f2) {
        if (Math.abs(f - f2) < 0.001f) {
            return true;
        }
        return false;
    }

    private static float lerp(float f, float f2, float f3, Interpolator interpolator) {
        float f4 = f3;
        if (interpolator != null) {
            f4 = interpolator.getInterpolation(f3);
        }
        return AnimationUtils.lerp(f, f2, f4);
    }

    private Typeface readFontFamilyTypeface(int n) {
        TypedArray typedArray;
        block4 : {
            typedArray = this.mView.getContext().obtainStyledAttributes(n, new int[]{16843692});
            String string2 = typedArray.getString(0);
            if (string2 == null) break block4;
            string2 = Typeface.create((String)string2, (int)0);
            return string2;
        }
        typedArray.recycle();
        return null;
        finally {
            typedArray.recycle();
        }
    }

    private static boolean rectEquals(Rect rect, int n, int n2, int n3, int n4) {
        if (rect.left == n && rect.top == n2 && rect.right == n3 && rect.bottom == n4) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setInterpolatedTextSize(float f) {
        this.calculateUsingTextSize(f);
        boolean bl = USE_SCALING_TEXTURE && this.mScale != 1.0f;
        this.mUseTexture = bl;
        if (this.mUseTexture) {
            this.ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void draw(Canvas canvas) {
        int n = canvas.save();
        if (this.mTextToDraw != null && this.mDrawTitle) {
            float f;
            float f2;
            float f3 = this.mCurrentDrawX;
            float f4 = this.mCurrentDrawY;
            boolean bl = this.mUseTexture && this.mExpandedTitleTexture != null;
            this.mTextPaint.setTextSize(this.mCurrentTextSize);
            if (bl) {
                f = this.mTextureAscent * this.mScale;
                f2 = this.mTextureDescent;
                f2 = this.mScale;
            } else {
                f = this.mTextPaint.ascent() * this.mScale;
                this.mTextPaint.descent();
                f2 = this.mScale;
            }
            f2 = f4;
            if (bl) {
                f2 = f4 + f;
            }
            if (this.mScale != 1.0f) {
                canvas.scale(this.mScale, this.mScale, f3, f2);
            }
            if (bl) {
                canvas.drawBitmap(this.mExpandedTitleTexture, f3, f2, this.mTexturePaint);
            } else {
                canvas.drawText(this.mTextToDraw, 0, this.mTextToDraw.length(), f3, f2, (Paint)this.mTextPaint);
            }
        }
        canvas.restoreToCount(n);
    }

    int getCollapsedTextColor() {
        return this.mCollapsedTextColor;
    }

    int getCollapsedTextGravity() {
        return this.mCollapsedTextGravity;
    }

    float getCollapsedTextSize() {
        return this.mCollapsedTextSize;
    }

    Typeface getCollapsedTypeface() {
        if (this.mCollapsedTypeface != null) {
            return this.mCollapsedTypeface;
        }
        return Typeface.DEFAULT;
    }

    int getExpandedTextColor() {
        return this.mExpandedTextColor;
    }

    int getExpandedTextGravity() {
        return this.mExpandedTextGravity;
    }

    float getExpandedTextSize() {
        return this.mExpandedTextSize;
    }

    Typeface getExpandedTypeface() {
        if (this.mExpandedTypeface != null) {
            return this.mExpandedTypeface;
        }
        return Typeface.DEFAULT;
    }

    float getExpansionFraction() {
        return this.mExpandedFraction;
    }

    CharSequence getText() {
        return this.mText;
    }

    /*
     * Enabled aggressive block sorting
     */
    void onBoundsChanged() {
        boolean bl = this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0;
        this.mDrawTitle = bl;
    }

    public void recalculate() {
        if (this.mView.getHeight() > 0 && this.mView.getWidth() > 0) {
            this.calculateBaseOffsets();
            this.calculateCurrentOffsets();
        }
    }

    void setCollapsedBounds(int n, int n2, int n3, int n4) {
        if (!CollapsingTextHelper.rectEquals(this.mCollapsedBounds, n, n2, n3, n4)) {
            this.mCollapsedBounds.set(n, n2, n3, n4);
            this.mBoundsChanged = true;
            this.onBoundsChanged();
        }
    }

    void setCollapsedTextAppearance(int n) {
        TypedArray typedArray = this.mView.getContext().obtainStyledAttributes(n, R.styleable.TextAppearance);
        if (typedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.mCollapsedTextColor = typedArray.getColor(R.styleable.TextAppearance_android_textColor, this.mCollapsedTextColor);
        }
        if (typedArray.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.mCollapsedTextSize = typedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mCollapsedTextSize);
        }
        this.mCollapsedShadowColor = typedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.mCollapsedShadowDx = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.mCollapsedShadowDy = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.mCollapsedShadowRadius = typedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        typedArray.recycle();
        if (Build.VERSION.SDK_INT >= 16) {
            this.mCollapsedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }

    void setCollapsedTextColor(int n) {
        if (this.mCollapsedTextColor != n) {
            this.mCollapsedTextColor = n;
            this.recalculate();
        }
    }

    void setCollapsedTextGravity(int n) {
        if (this.mCollapsedTextGravity != n) {
            this.mCollapsedTextGravity = n;
            this.recalculate();
        }
    }

    void setCollapsedTextSize(float f) {
        if (this.mCollapsedTextSize != f) {
            this.mCollapsedTextSize = f;
            this.recalculate();
        }
    }

    void setCollapsedTypeface(Typeface typeface) {
        if (this.mCollapsedTypeface != typeface) {
            this.mCollapsedTypeface = typeface;
            this.recalculate();
        }
    }

    void setExpandedBounds(int n, int n2, int n3, int n4) {
        if (!CollapsingTextHelper.rectEquals(this.mExpandedBounds, n, n2, n3, n4)) {
            this.mExpandedBounds.set(n, n2, n3, n4);
            this.mBoundsChanged = true;
            this.onBoundsChanged();
        }
    }

    void setExpandedTextAppearance(int n) {
        TypedArray typedArray = this.mView.getContext().obtainStyledAttributes(n, R.styleable.TextAppearance);
        if (typedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.mExpandedTextColor = typedArray.getColor(R.styleable.TextAppearance_android_textColor, this.mExpandedTextColor);
        }
        if (typedArray.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.mExpandedTextSize = typedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mExpandedTextSize);
        }
        this.mExpandedShadowColor = typedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.mExpandedShadowDx = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.mExpandedShadowDy = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.mExpandedShadowRadius = typedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        typedArray.recycle();
        if (Build.VERSION.SDK_INT >= 16) {
            this.mExpandedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }

    void setExpandedTextColor(int n) {
        if (this.mExpandedTextColor != n) {
            this.mExpandedTextColor = n;
            this.recalculate();
        }
    }

    void setExpandedTextGravity(int n) {
        if (this.mExpandedTextGravity != n) {
            this.mExpandedTextGravity = n;
            this.recalculate();
        }
    }

    void setExpandedTextSize(float f) {
        if (this.mExpandedTextSize != f) {
            this.mExpandedTextSize = f;
            this.recalculate();
        }
    }

    void setExpandedTypeface(Typeface typeface) {
        if (this.mExpandedTypeface != typeface) {
            this.mExpandedTypeface = typeface;
            this.recalculate();
        }
    }

    void setExpansionFraction(float f) {
        if ((f = MathUtils.constrain(f, 0.0f, 1.0f)) != this.mExpandedFraction) {
            this.mExpandedFraction = f;
            this.calculateCurrentOffsets();
        }
    }

    void setPositionInterpolator(Interpolator interpolator) {
        this.mPositionInterpolator = interpolator;
        this.recalculate();
    }

    void setText(CharSequence charSequence) {
        if (charSequence == null || !charSequence.equals(this.mText)) {
            this.mText = charSequence;
            this.mTextToDraw = null;
            this.clearTexture();
            this.recalculate();
        }
    }

    void setTextSizeInterpolator(Interpolator interpolator) {
        this.mTextSizeInterpolator = interpolator;
        this.recalculate();
    }

    void setTypefaces(Typeface typeface) {
        this.mExpandedTypeface = typeface;
        this.mCollapsedTypeface = typeface;
        this.recalculate();
    }
}

