/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.Region
 *  android.graphics.Region$Op
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.text.Layout
 *  android.text.Layout$Alignment
 *  android.text.StaticLayout
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.Transformation
 *  android.widget.CompoundButton
 */
package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.internal.text.AllCapsTransformationMethod;
import android.support.v7.internal.widget.DrawableUtils;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.internal.widget.ViewUtils;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;
import java.util.List;

public class SwitchCompat
extends CompoundButton {
    private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private int mMinFlingVelocity;
    private Layout mOffLayout;
    private Layout mOnLayout;
    private ThumbAnimation mPositionAnimator;
    private boolean mShowText;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    private int mSwitchHeight;
    private int mSwitchLeft;
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private TransformationMethod mSwitchTransformationMethod;
    private int mSwitchWidth;
    private final Rect mTempRect = new Rect();
    private ColorStateList mTextColors;
    private CharSequence mTextOff;
    private CharSequence mTextOn;
    private TextPaint mTextPaint = new TextPaint(1);
    private Drawable mThumbDrawable;
    private float mThumbPosition;
    private int mThumbTextPadding;
    private int mThumbWidth;
    private final TintManager mTintManager;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private Drawable mTrackDrawable;
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();

    public SwitchCompat(Context context) {
        this(context, null);
    }

    public SwitchCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.switchStyle);
    }

    public SwitchCompat(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object, n);
        Resources resources = this.getResources();
        this.mTextPaint.density = resources.getDisplayMetrics().density;
        object = TintTypedArray.obtainStyledAttributes(context, (AttributeSet)object, R.styleable.SwitchCompat, n, 0);
        this.mThumbDrawable = object.getDrawable(R.styleable.SwitchCompat_android_thumb);
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setCallback((Drawable.Callback)this);
        }
        this.mTrackDrawable = object.getDrawable(R.styleable.SwitchCompat_track);
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setCallback((Drawable.Callback)this);
        }
        this.mTextOn = object.getText(R.styleable.SwitchCompat_android_textOn);
        this.mTextOff = object.getText(R.styleable.SwitchCompat_android_textOff);
        this.mShowText = object.getBoolean(R.styleable.SwitchCompat_showText, true);
        this.mThumbTextPadding = object.getDimensionPixelSize(R.styleable.SwitchCompat_thumbTextPadding, 0);
        this.mSwitchMinWidth = object.getDimensionPixelSize(R.styleable.SwitchCompat_switchMinWidth, 0);
        this.mSwitchPadding = object.getDimensionPixelSize(R.styleable.SwitchCompat_switchPadding, 0);
        this.mSplitTrack = object.getBoolean(R.styleable.SwitchCompat_splitTrack, false);
        n = object.getResourceId(R.styleable.SwitchCompat_switchTextAppearance, 0);
        if (n != 0) {
            this.setSwitchTextAppearance(context, n);
        }
        this.mTintManager = object.getTintManager();
        object.recycle();
        context = ViewConfiguration.get((Context)context);
        this.mTouchSlop = context.getScaledTouchSlop();
        this.mMinFlingVelocity = context.getScaledMinimumFlingVelocity();
        this.refreshDrawableState();
        this.setChecked(this.isChecked());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void animateThumbToCheckedState(final boolean bl) {
        if (this.mPositionAnimator != null) {
            this.cancelPositionAnimator();
        }
        float f = this.mThumbPosition;
        float f2 = bl ? 1.0f : 0.0f;
        this.mPositionAnimator = new ThumbAnimation(f, f2);
        this.mPositionAnimator.setDuration(250);
        this.mPositionAnimator.setAnimationListener(new Animation.AnimationListener(){

            /*
             * Enabled aggressive block sorting
             */
            public void onAnimationEnd(Animation object) {
                if (SwitchCompat.this.mPositionAnimator == object) {
                    object = SwitchCompat.this;
                    float f = bl ? 1.0f : 0.0f;
                    ((SwitchCompat)((Object)object)).setThumbPosition(f);
                    SwitchCompat.this.mPositionAnimator = null;
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.startAnimation((Animation)this.mPositionAnimator);
    }

    private void cancelPositionAnimator() {
        if (this.mPositionAnimator != null) {
            this.clearAnimation();
            this.mPositionAnimator = null;
        }
    }

    private void cancelSuperTouch(MotionEvent motionEvent) {
        motionEvent = MotionEvent.obtain((MotionEvent)motionEvent);
        motionEvent.setAction(3);
        super.onTouchEvent(motionEvent);
        motionEvent.recycle();
    }

    private static float constrain(float f, float f2, float f3) {
        if (f < f2) {
            return f2;
        }
        if (f > f3) {
            return f3;
        }
        return f;
    }

    private boolean getTargetCheckedState() {
        if (this.mThumbPosition > 0.5f) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getThumbOffset() {
        float f;
        if (ViewUtils.isLayoutRtl((View)this)) {
            f = 1.0f - this.mThumbPosition;
            do {
                return (int)((float)this.getThumbScrollRange() * f + 0.5f);
                break;
            } while (true);
        }
        f = this.mThumbPosition;
        return (int)((float)this.getThumbScrollRange() * f + 0.5f);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getThumbScrollRange() {
        Rect rect;
        if (this.mTrackDrawable == null) return 0;
        Rect rect2 = this.mTempRect;
        this.mTrackDrawable.getPadding(rect2);
        if (this.mThumbDrawable != null) {
            rect = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            do {
                return this.mSwitchWidth - this.mThumbWidth - rect2.left - rect2.right - rect.left - rect.right;
                break;
            } while (true);
        }
        rect = DrawableUtils.INSETS_NONE;
        return this.mSwitchWidth - this.mThumbWidth - rect2.left - rect2.right - rect.left - rect.right;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean hitThumb(float f, float f2) {
        if (this.mThumbDrawable == null) {
            return false;
        }
        int n = this.getThumbOffset();
        this.mThumbDrawable.getPadding(this.mTempRect);
        int n2 = this.mSwitchTop;
        int n3 = this.mTouchSlop;
        n = this.mSwitchLeft + n - this.mTouchSlop;
        int n4 = this.mThumbWidth;
        int n5 = this.mTempRect.left;
        int n6 = this.mTempRect.right;
        int n7 = this.mTouchSlop;
        int n8 = this.mSwitchBottom;
        int n9 = this.mTouchSlop;
        if (f <= (float)n) return false;
        if (f >= (float)(n4 + n + n5 + n6 + n7)) return false;
        if (f2 <= (float)(n2 - n3)) return false;
        if (f2 >= (float)(n8 + n9)) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Layout makeLayout(CharSequence charSequence) {
        int n;
        if (this.mSwitchTransformationMethod != null) {
            charSequence = this.mSwitchTransformationMethod.getTransformation(charSequence, (View)this);
        }
        TextPaint textPaint = this.mTextPaint;
        if (charSequence != null) {
            n = (int)Math.ceil(Layout.getDesiredWidth((CharSequence)charSequence, (TextPaint)this.mTextPaint));
            do {
                return new StaticLayout(charSequence, textPaint, n, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                break;
            } while (true);
        }
        n = 0;
        return new StaticLayout(charSequence, textPaint, n, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void setSwitchTypefaceByIndex(int var1_1, int var2_2) {
        var3_3 = null;
        switch (var1_1) {
            case 1: {
                var3_3 = Typeface.SANS_SERIF;
                ** break;
            }
            case 2: {
                var3_3 = Typeface.SERIF;
            }
lbl8: // 3 sources:
            default: {
                ** GOTO lbl12
            }
            case 3: 
        }
        var3_3 = Typeface.MONOSPACE;
lbl12: // 2 sources:
        this.setSwitchTypeface(var3_3, var2_2);
    }

    private void setThumbPosition(float f) {
        this.mThumbPosition = f;
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void stopDrag(MotionEvent motionEvent) {
        boolean bl;
        this.mTouchMode = 0;
        boolean bl2 = motionEvent.getAction() == 1 && this.isEnabled();
        boolean bl3 = this.isChecked();
        if (bl2) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float f = this.mVelocityTracker.getXVelocity();
            bl = Math.abs(f) > (float)this.mMinFlingVelocity ? (ViewUtils.isLayoutRtl((View)this) ? f < 0.0f : f > 0.0f) : this.getTargetCheckedState();
        } else {
            bl = bl3;
        }
        if (bl != bl3) {
            this.playSoundEffect(0);
        }
        this.setChecked(bl);
        this.cancelSuperTouch(motionEvent);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void draw(Canvas canvas) {
        Rect rect = this.mTempRect;
        int n = this.mSwitchLeft;
        int n2 = this.mSwitchTop;
        int n3 = this.mSwitchRight;
        int n4 = this.mSwitchBottom;
        int n5 = n + this.getThumbOffset();
        Rect rect2 = this.mThumbDrawable != null ? DrawableUtils.getOpticalBounds(this.mThumbDrawable) : DrawableUtils.INSETS_NONE;
        int n6 = n5;
        if (this.mTrackDrawable != null) {
            int n7;
            this.mTrackDrawable.getPadding(rect);
            int n8 = n5 + rect.left;
            n5 = n2;
            int n9 = n7 = n4;
            int n10 = n;
            int n11 = n3;
            int n12 = n5;
            if (rect2 != null) {
                n6 = n;
                if (rect2.left > rect.left) {
                    n6 = n + (rect2.left - rect.left);
                }
                n = n5;
                if (rect2.top > rect.top) {
                    n = n5 + (rect2.top - rect.top);
                }
                n5 = n3;
                if (rect2.right > rect.right) {
                    n5 = n3 - (rect2.right - rect.right);
                }
                n9 = n7;
                n10 = n6;
                n11 = n5;
                n12 = n;
                if (rect2.bottom > rect.bottom) {
                    n9 = n7 - (rect2.bottom - rect.bottom);
                    n12 = n;
                    n11 = n5;
                    n10 = n6;
                }
            }
            this.mTrackDrawable.setBounds(n10, n12, n11, n9);
            n6 = n8;
        }
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.getPadding(rect);
            n3 = n6 - rect.left;
            n6 = this.mThumbWidth + n6 + rect.right;
            this.mThumbDrawable.setBounds(n3, n2, n6, n4);
            rect2 = this.getBackground();
            if (rect2 != null) {
                DrawableCompat.setHotspotBounds((Drawable)rect2, n3, n2, n6, n4);
            }
        }
        super.draw(canvas);
    }

    public void drawableHotspotChanged(float f, float f2) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.drawableHotspotChanged(f, f2);
        }
        if (this.mThumbDrawable != null) {
            DrawableCompat.setHotspot(this.mThumbDrawable, f, f2);
        }
        if (this.mTrackDrawable != null) {
            DrawableCompat.setHotspot(this.mTrackDrawable, f, f2);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] arrn = this.getDrawableState();
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setState(arrn);
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setState(arrn);
        }
        this.invalidate();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getCompoundPaddingLeft() {
        int n;
        if (!ViewUtils.isLayoutRtl((View)this)) {
            return super.getCompoundPaddingLeft();
        }
        int n2 = n = super.getCompoundPaddingLeft() + this.mSwitchWidth;
        if (TextUtils.isEmpty((CharSequence)this.getText())) return n2;
        return n + this.mSwitchPadding;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getCompoundPaddingRight() {
        int n;
        if (ViewUtils.isLayoutRtl((View)this)) {
            return super.getCompoundPaddingRight();
        }
        int n2 = n = super.getCompoundPaddingRight() + this.mSwitchWidth;
        if (TextUtils.isEmpty((CharSequence)this.getText())) return n2;
        return n + this.mSwitchPadding;
    }

    public boolean getShowText() {
        return this.mShowText;
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }

    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }

    public CharSequence getTextOff() {
        return this.mTextOff;
    }

    public CharSequence getTextOn() {
        return this.mTextOn;
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }

    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }

    public void jumpDrawablesToCurrentState() {
        if (Build.VERSION.SDK_INT >= 11) {
            super.jumpDrawablesToCurrentState();
            if (this.mThumbDrawable != null) {
                this.mThumbDrawable.jumpToCurrentState();
            }
            if (this.mTrackDrawable != null) {
                this.mTrackDrawable.jumpToCurrentState();
            }
            this.cancelPositionAnimator();
        }
    }

    protected int[] onCreateDrawableState(int n) {
        int[] arrn = super.onCreateDrawableState(n + 1);
        if (this.isChecked()) {
            SwitchCompat.mergeDrawableStates((int[])arrn, (int[])CHECKED_STATE_SET);
        }
        return arrn;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onDraw(Canvas canvas) {
        int n;
        super.onDraw(canvas);
        Rect rect = this.mTempRect;
        int[] arrn = this.mTrackDrawable;
        if (arrn != null) {
            arrn.getPadding(rect);
        } else {
            rect.setEmpty();
        }
        int n2 = this.mSwitchTop;
        int n3 = this.mSwitchBottom;
        int n4 = rect.top;
        int n5 = rect.bottom;
        Drawable drawable2 = this.mThumbDrawable;
        if (arrn != null) {
            if (this.mSplitTrack && drawable2 != null) {
                Rect rect2 = DrawableUtils.getOpticalBounds(drawable2);
                drawable2.copyBounds(rect);
                rect.left += rect2.left;
                rect.right -= rect2.right;
                n = canvas.save();
                canvas.clipRect(rect, Region.Op.DIFFERENCE);
                arrn.draw(canvas);
                canvas.restoreToCount(n);
            } else {
                arrn.draw(canvas);
            }
        }
        int n6 = canvas.save();
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
        rect = this.getTargetCheckedState() ? this.mOnLayout : this.mOffLayout;
        if (rect != null) {
            arrn = this.getDrawableState();
            if (this.mTextColors != null) {
                this.mTextPaint.setColor(this.mTextColors.getColorForState(arrn, 0));
            }
            this.mTextPaint.drawableState = arrn;
            if (drawable2 != null) {
                drawable2 = drawable2.getBounds();
                n = drawable2.left + drawable2.right;
            } else {
                n = this.getWidth();
            }
            int n7 = rect.getWidth() / 2;
            n2 = (n2 + n4 + (n3 - n5)) / 2;
            n3 = rect.getHeight() / 2;
            canvas.translate((float)((n /= 2) - n7), (float)(n2 - n3));
            rect.draw(canvas);
        }
        canvas.restoreToCount(n6);
    }

    @TargetApi(value=14)
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)"android.widget.Switch");
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)"android.widget.Switch");
            CharSequence charSequence = this.isChecked() ? this.mTextOn : this.mTextOff;
            if (!TextUtils.isEmpty((CharSequence)charSequence)) {
                CharSequence charSequence2 = accessibilityNodeInfo.getText();
                if (!TextUtils.isEmpty((CharSequence)charSequence2)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(charSequence2).append(' ').append(charSequence);
                    accessibilityNodeInfo.setText((CharSequence)stringBuilder);
                    return;
                }
                accessibilityNodeInfo.setText(charSequence);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        n = 0;
        n2 = 0;
        if (this.mThumbDrawable != null) {
            Rect rect = this.mTempRect;
            if (this.mTrackDrawable != null) {
                this.mTrackDrawable.getPadding(rect);
            } else {
                rect.setEmpty();
            }
            Rect rect2 = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            n = Math.max(0, rect2.left - rect.left);
            n2 = Math.max(0, rect2.right - rect.right);
        }
        if (ViewUtils.isLayoutRtl((View)this)) {
            n3 = this.getPaddingLeft() + n;
            n4 = this.mSwitchWidth + n3 - n - n2;
        } else {
            n4 = this.getWidth() - this.getPaddingRight() - n2;
            n3 = n4 - this.mSwitchWidth + n + n2;
        }
        switch (this.getGravity() & 112) {
            default: {
                n2 = this.getPaddingTop();
                n = n2 + this.mSwitchHeight;
                break;
            }
            case 16: {
                n2 = (this.getPaddingTop() + this.getHeight() - this.getPaddingBottom()) / 2 - this.mSwitchHeight / 2;
                n = n2 + this.mSwitchHeight;
                break;
            }
            case 80: {
                n = this.getHeight() - this.getPaddingBottom();
                n2 = n - this.mSwitchHeight;
            }
        }
        this.mSwitchLeft = n3;
        this.mSwitchTop = n2;
        this.mSwitchBottom = n;
        this.mSwitchRight = n4;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onMeasure(int n, int n2) {
        int n3;
        int n4;
        if (this.mShowText) {
            if (this.mOnLayout == null) {
                this.mOnLayout = this.makeLayout(this.mTextOn);
            }
            if (this.mOffLayout == null) {
                this.mOffLayout = this.makeLayout(this.mTextOff);
            }
        }
        Rect rect = this.mTempRect;
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.getPadding(rect);
            n3 = this.mThumbDrawable.getIntrinsicWidth() - rect.left - rect.right;
            n4 = this.mThumbDrawable.getIntrinsicHeight();
        } else {
            n3 = 0;
            n4 = 0;
        }
        int n5 = this.mShowText ? Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + this.mThumbTextPadding * 2 : 0;
        this.mThumbWidth = Math.max(n5, n3);
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.getPadding(rect);
            n3 = this.mTrackDrawable.getIntrinsicHeight();
        } else {
            rect.setEmpty();
            n3 = 0;
        }
        int n6 = rect.left;
        int n7 = rect.right;
        int n8 = n6;
        n5 = n7;
        if (this.mThumbDrawable != null) {
            rect = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            n8 = Math.max(n6, rect.left);
            n5 = Math.max(n7, rect.right);
        }
        n5 = Math.max(this.mSwitchMinWidth, this.mThumbWidth * 2 + n8 + n5);
        n4 = Math.max(n3, n4);
        this.mSwitchWidth = n5;
        this.mSwitchHeight = n4;
        super.onMeasure(n, n2);
        if (this.getMeasuredHeight() < n4) {
            this.setMeasuredDimension(ViewCompat.getMeasuredWidthAndState((View)this), n4);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @TargetApi(value=14)
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        CharSequence charSequence = this.isChecked() ? this.mTextOn : this.mTextOff;
        if (charSequence != null) {
            accessibilityEvent.getText().add(charSequence);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean onTouchEvent(MotionEvent var1_1) {
        this.mVelocityTracker.addMovement(var1_1);
        switch (MotionEventCompat.getActionMasked(var1_1)) lbl-1000: // 6 sources:
        {
            do {
                default: {
                    do {
                        return super.onTouchEvent(var1_1);
                        break;
                    } while (true);
                }
                break;
            } while (true);
            case 0: {
                var2_2 = var1_1.getX();
                var3_5 = var1_1.getY();
                if (!this.isEnabled() || !this.hitThumb(var2_2, var3_5)) ** GOTO lbl-1000
                this.mTouchMode = 1;
                this.mTouchX = var2_2;
                this.mTouchY = var3_5;
                ** GOTO lbl-1000
            }
            case 2: {
                switch (this.mTouchMode) {
                    case 0: {
                        ** GOTO lbl-1000
                    }
                    default: {
                        ** GOTO lbl-1000
                    }
                    case 1: {
                        var2_3 = var1_1.getX();
                        var3_6 = var1_1.getY();
                        if (Math.abs(var2_3 - this.mTouchX) > (float)this.mTouchSlop || Math.abs(var3_6 - this.mTouchY) > (float)this.mTouchSlop) ** break;
                        ** continue;
                        this.mTouchMode = 2;
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                        this.mTouchX = var2_3;
                        this.mTouchY = var3_6;
                        return true;
                    }
                    case 2: 
                }
                var4_8 = var1_1.getX();
                var5_9 = this.getThumbScrollRange();
                var2_4 = var4_8 - this.mTouchX;
                if (var5_9 != 0) {
                    var2_4 /= (float)var5_9;
lbl37: // 3 sources:
                    do {
                        var3_7 = var2_4;
                        if (ViewUtils.isLayoutRtl((View)this)) {
                            var3_7 = - var2_4;
                        }
                        if ((var2_4 = SwitchCompat.constrain(this.mThumbPosition + var3_7, 0.0f, 1.0f)) == this.mThumbPosition) return true;
                        this.mTouchX = var4_8;
                        this.setThumbPosition(var2_4);
                        return true;
                        break;
                    } while (true);
                }
                if (var2_4 <= 0.0f) ** GOTO lbl48
                var2_4 = 1.0f;
                ** GOTO lbl37
lbl48: // 1 sources:
                var2_4 = -1.0f;
                ** continue;
            }
            case 1: 
            case 3: 
        }
        if (this.mTouchMode == 2) {
            this.stopDrag(var1_1);
            super.onTouchEvent(var1_1);
            return true;
        }
        this.mTouchMode = 0;
        this.mVelocityTracker.clear();
        return super.onTouchEvent(var1_1);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setChecked(boolean bl) {
        super.setChecked(bl);
        bl = this.isChecked();
        if (this.getWindowToken() != null && ViewCompat.isLaidOut((View)this) && this.isShown()) {
            this.animateThumbToCheckedState(bl);
            return;
        }
        this.cancelPositionAnimator();
        float f = bl ? 1.0f : 0.0f;
        this.setThumbPosition(f);
    }

    public void setShowText(boolean bl) {
        if (this.mShowText != bl) {
            this.mShowText = bl;
            this.requestLayout();
        }
    }

    public void setSplitTrack(boolean bl) {
        this.mSplitTrack = bl;
        this.invalidate();
    }

    public void setSwitchMinWidth(int n) {
        this.mSwitchMinWidth = n;
        this.requestLayout();
    }

    public void setSwitchPadding(int n) {
        this.mSwitchPadding = n;
        this.requestLayout();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSwitchTextAppearance(Context context, int n) {
        ColorStateList colorStateList = (context = context.obtainStyledAttributes(n, R.styleable.TextAppearance)).getColorStateList(R.styleable.TextAppearance_android_textColor);
        this.mTextColors = colorStateList != null ? colorStateList : this.getTextColors();
        n = context.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
        if (n != 0 && (float)n != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize((float)n);
            this.requestLayout();
        }
        this.setSwitchTypefaceByIndex(context.getInt(R.styleable.TextAppearance_android_typeface, -1), context.getInt(R.styleable.TextAppearance_android_textStyle, -1));
        this.mSwitchTransformationMethod = context.getBoolean(R.styleable.TextAppearance_textAllCaps, false) ? new AllCapsTransformationMethod(this.getContext()) : null;
        context.recycle();
    }

    public void setSwitchTypeface(Typeface typeface) {
        if (this.mTextPaint.getTypeface() != typeface) {
            this.mTextPaint.setTypeface(typeface);
            this.requestLayout();
            this.invalidate();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setSwitchTypeface(Typeface typeface, int n) {
        boolean bl = false;
        if (n <= 0) {
            this.mTextPaint.setFakeBoldText(false);
            this.mTextPaint.setTextSkewX(0.0f);
            this.setSwitchTypeface(typeface);
            return;
        }
        typeface = typeface == null ? Typeface.defaultFromStyle((int)n) : Typeface.create((Typeface)typeface, (int)n);
        this.setSwitchTypeface(typeface);
        int n2 = typeface != null ? typeface.getStyle() : 0;
        typeface = this.mTextPaint;
        if (((n &= ~ n2) & 1) != 0) {
            bl = true;
        }
        typeface.setFakeBoldText(bl);
        typeface = this.mTextPaint;
        float f = (n & 2) != 0 ? -0.25f : 0.0f;
        typeface.setTextSkewX(f);
    }

    public void setTextOff(CharSequence charSequence) {
        this.mTextOff = charSequence;
        this.requestLayout();
    }

    public void setTextOn(CharSequence charSequence) {
        this.mTextOn = charSequence;
        this.requestLayout();
    }

    public void setThumbDrawable(Drawable drawable2) {
        this.mThumbDrawable = drawable2;
        this.requestLayout();
    }

    public void setThumbResource(int n) {
        this.setThumbDrawable(this.mTintManager.getDrawable(n));
    }

    public void setThumbTextPadding(int n) {
        this.mThumbTextPadding = n;
        this.requestLayout();
    }

    public void setTrackDrawable(Drawable drawable2) {
        this.mTrackDrawable = drawable2;
        this.requestLayout();
    }

    public void setTrackResource(int n) {
        this.setTrackDrawable(this.mTintManager.getDrawable(n));
    }

    /*
     * Enabled aggressive block sorting
     */
    public void toggle() {
        boolean bl = !this.isChecked();
        this.setChecked(bl);
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        if (super.verifyDrawable(drawable2) || drawable2 == this.mThumbDrawable || drawable2 == this.mTrackDrawable) {
            return true;
        }
        return false;
    }

    private class ThumbAnimation
    extends Animation {
        final float mDiff;
        final float mEndPosition;
        final float mStartPosition;

        private ThumbAnimation(float f, float f2) {
            this.mStartPosition = f;
            this.mEndPosition = f2;
            this.mDiff = f2 - f;
        }

        protected void applyTransformation(float f, Transformation transformation) {
            SwitchCompat.this.setThumbPosition(this.mStartPosition + this.mDiff * f);
        }
    }

}

