/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.Interpolator
 *  android.widget.ImageButton
 */
package android.support.design.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButtonEclairMr1;
import android.support.design.widget.FloatingActionButtonHoneycombMr1;
import android.support.design.widget.FloatingActionButtonImpl;
import android.support.design.widget.FloatingActionButtonLollipop;
import android.support.design.widget.ShadowViewDelegate;
import android.support.design.widget.Snackbar;
import android.support.design.widget.ThemeUtils;
import android.support.design.widget.ViewGroupUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(value=Behavior.class)
public class FloatingActionButton
extends ImageButton {
    private static final int SIZE_MINI = 1;
    private static final int SIZE_NORMAL = 0;
    private ColorStateList mBackgroundTint;
    private PorterDuff.Mode mBackgroundTintMode;
    private int mBorderWidth;
    private int mContentPadding;
    private final FloatingActionButtonImpl mImpl;
    private int mRippleColor;
    private final Rect mShadowPadding;
    private int mSize;

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    public FloatingActionButton(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object, n);
        ThemeUtils.checkAppCompatTheme(context);
        this.mShadowPadding = new Rect();
        object = context.obtainStyledAttributes((AttributeSet)object, R.styleable.FloatingActionButton, n, R.style.Widget_Design_FloatingActionButton);
        context = object.getDrawable(R.styleable.FloatingActionButton_android_background);
        this.mBackgroundTint = object.getColorStateList(R.styleable.FloatingActionButton_backgroundTint);
        this.mBackgroundTintMode = FloatingActionButton.parseTintMode(object.getInt(R.styleable.FloatingActionButton_backgroundTintMode, -1), null);
        this.mRippleColor = object.getColor(R.styleable.FloatingActionButton_rippleColor, 0);
        this.mSize = object.getInt(R.styleable.FloatingActionButton_fabSize, 0);
        this.mBorderWidth = object.getDimensionPixelSize(R.styleable.FloatingActionButton_borderWidth, 0);
        float f = object.getDimension(R.styleable.FloatingActionButton_elevation, 0.0f);
        float f2 = object.getDimension(R.styleable.FloatingActionButton_pressedTranslationZ, 0.0f);
        object.recycle();
        object = new ShadowViewDelegate(){

            @Override
            public float getRadius() {
                return (float)FloatingActionButton.this.getSizeDimension() / 2.0f;
            }

            @Override
            public void setBackgroundDrawable(Drawable drawable2) {
                FloatingActionButton.this.setBackgroundDrawable(drawable2);
            }

            @Override
            public void setShadowPadding(int n, int n2, int n3, int n4) {
                FloatingActionButton.this.mShadowPadding.set(n, n2, n3, n4);
                FloatingActionButton.this.setPadding(FloatingActionButton.this.mContentPadding + n, FloatingActionButton.this.mContentPadding + n2, FloatingActionButton.this.mContentPadding + n3, FloatingActionButton.this.mContentPadding + n4);
            }
        };
        n = Build.VERSION.SDK_INT;
        this.mImpl = n >= 21 ? new FloatingActionButtonLollipop((View)this, (ShadowViewDelegate)object) : (n >= 12 ? new FloatingActionButtonHoneycombMr1((View)this, (ShadowViewDelegate)object) : new FloatingActionButtonEclairMr1((View)this, (ShadowViewDelegate)object));
        n = (int)this.getResources().getDimension(R.dimen.design_fab_content_size);
        this.mContentPadding = (this.getSizeDimension() - n) / 2;
        this.mImpl.setBackgroundDrawable((Drawable)context, this.mBackgroundTint, this.mBackgroundTintMode, this.mRippleColor, this.mBorderWidth);
        this.mImpl.setElevation(f);
        this.mImpl.setPressedTranslationZ(f2);
    }

    static PorterDuff.Mode parseTintMode(int n, PorterDuff.Mode mode) {
        switch (n) {
            default: {
                return mode;
            }
            case 3: {
                return PorterDuff.Mode.SRC_OVER;
            }
            case 5: {
                return PorterDuff.Mode.SRC_IN;
            }
            case 9: {
                return PorterDuff.Mode.SRC_ATOP;
            }
            case 14: {
                return PorterDuff.Mode.MULTIPLY;
            }
            case 15: 
        }
        return PorterDuff.Mode.SCREEN;
    }

    private static int resolveAdjustedSize(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n2);
        n2 = View.MeasureSpec.getSize((int)n2);
        switch (n3) {
            default: {
                return n;
            }
            case 0: {
                return n;
            }
            case Integer.MIN_VALUE: {
                return Math.min(n, n2);
            }
            case 1073741824: 
        }
        return n2;
    }

    @Nullable
    private FloatingActionButtonImpl.InternalVisibilityChangedListener wrapOnVisibilityChangedListener(final @Nullable OnVisibilityChangedListener onVisibilityChangedListener) {
        if (onVisibilityChangedListener == null) {
            return null;
        }
        return new FloatingActionButtonImpl.InternalVisibilityChangedListener(){

            @Override
            public void onHidden() {
                onVisibilityChangedListener.onHidden(FloatingActionButton.this);
            }

            @Override
            public void onShown() {
                onVisibilityChangedListener.onShown(FloatingActionButton.this);
            }
        };
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.mImpl.onDrawableStateChanged(this.getDrawableState());
    }

    @Nullable
    public ColorStateList getBackgroundTintList() {
        return this.mBackgroundTint;
    }

    @Nullable
    public PorterDuff.Mode getBackgroundTintMode() {
        return this.mBackgroundTintMode;
    }

    final int getSizeDimension() {
        switch (this.mSize) {
            default: {
                return this.getResources().getDimensionPixelSize(R.dimen.design_fab_size_normal);
            }
            case 1: 
        }
        return this.getResources().getDimensionPixelSize(R.dimen.design_fab_size_mini);
    }

    public void hide() {
        this.mImpl.hide(null);
    }

    public void hide(@Nullable OnVisibilityChangedListener onVisibilityChangedListener) {
        this.mImpl.hide(this.wrapOnVisibilityChangedListener(onVisibilityChangedListener));
    }

    @TargetApi(value=11)
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.mImpl.jumpDrawableToCurrentState();
    }

    protected void onMeasure(int n, int n2) {
        int n3 = this.getSizeDimension();
        n = Math.min(FloatingActionButton.resolveAdjustedSize(n3, n), FloatingActionButton.resolveAdjustedSize(n3, n2));
        this.setMeasuredDimension(this.mShadowPadding.left + n + this.mShadowPadding.right, this.mShadowPadding.top + n + this.mShadowPadding.bottom);
    }

    public void setBackgroundDrawable(@NonNull Drawable drawable2) {
        if (this.mImpl != null) {
            this.mImpl.setBackgroundDrawable(drawable2, this.mBackgroundTint, this.mBackgroundTintMode, this.mRippleColor, this.mBorderWidth);
        }
    }

    public void setBackgroundTintList(@Nullable ColorStateList colorStateList) {
        if (this.mBackgroundTint != colorStateList) {
            this.mBackgroundTint = colorStateList;
            this.mImpl.setBackgroundTintList(colorStateList);
        }
    }

    public void setBackgroundTintMode(@Nullable PorterDuff.Mode mode) {
        if (this.mBackgroundTintMode != mode) {
            this.mBackgroundTintMode = mode;
            this.mImpl.setBackgroundTintMode(mode);
        }
    }

    public void setRippleColor(@ColorInt int n) {
        if (this.mRippleColor != n) {
            this.mRippleColor = n;
            this.mImpl.setRippleColor(n);
        }
    }

    public void show() {
        this.mImpl.show(null);
    }

    public void show(@Nullable OnVisibilityChangedListener onVisibilityChangedListener) {
        this.mImpl.show(this.wrapOnVisibilityChangedListener(onVisibilityChangedListener));
    }

    public static class Behavior
    extends CoordinatorLayout.Behavior<FloatingActionButton> {
        private static final boolean SNACKBAR_BEHAVIOR_ENABLED;
        private float mFabTranslationY;
        private Rect mTmpRect;

        /*
         * Enabled aggressive block sorting
         */
        static {
            boolean bl = Build.VERSION.SDK_INT >= 11;
            SNACKBAR_BEHAVIOR_ENABLED = bl;
        }

        private float getFabTranslationYForSnackbar(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton) {
            float f = 0.0f;
            List<View> list = coordinatorLayout.getDependencies((View)floatingActionButton);
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                View view = list.get(i);
                float f2 = f;
                if (view instanceof Snackbar.SnackbarLayout) {
                    f2 = f;
                    if (coordinatorLayout.doViewsOverlap((View)floatingActionButton, view)) {
                        f2 = Math.min(f, ViewCompat.getTranslationY(view) - (float)view.getHeight());
                    }
                }
                f = f2;
            }
            return f;
        }

        /*
         * Enabled aggressive block sorting
         */
        private void offsetIfNeeded(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton) {
            Rect rect = floatingActionButton.mShadowPadding;
            if (rect != null && rect.centerX() > 0 && rect.centerY() > 0) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams();
                int n = 0;
                int n2 = 0;
                if (floatingActionButton.getRight() >= coordinatorLayout.getWidth() - layoutParams.rightMargin) {
                    n2 = rect.right;
                } else if (floatingActionButton.getLeft() <= layoutParams.leftMargin) {
                    n2 = - rect.left;
                }
                if (floatingActionButton.getBottom() >= coordinatorLayout.getBottom() - layoutParams.bottomMargin) {
                    n = rect.bottom;
                } else if (floatingActionButton.getTop() <= layoutParams.topMargin) {
                    n = - rect.top;
                }
                floatingActionButton.offsetTopAndBottom(n);
                floatingActionButton.offsetLeftAndRight(n2);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private void updateFabTranslationForSnackbar(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            float f;
            if (floatingActionButton.getVisibility() != 0 || this.mFabTranslationY == (f = this.getFabTranslationYForSnackbar(coordinatorLayout, floatingActionButton))) {
                return;
            }
            this.mFabTranslationY = f;
            if (Math.abs(ViewCompat.getTranslationY((View)floatingActionButton) - f) > (float)floatingActionButton.getHeight() * 0.667f) {
                ViewCompat.animate((View)floatingActionButton).translationY(f).scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(null);
                return;
            }
            ViewCompat.animate((View)floatingActionButton).cancel();
            ViewCompat.setTranslationY((View)floatingActionButton, f);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private boolean updateFabVisibility(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton) {
            if (((CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams()).getAnchorId() != appBarLayout.getId()) {
                return false;
            }
            if (this.mTmpRect == null) {
                this.mTmpRect = new Rect();
            }
            Rect rect = this.mTmpRect;
            ViewGroupUtils.getDescendantRect(coordinatorLayout, (View)appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                floatingActionButton.hide();
                do {
                    return true;
                    break;
                } while (true);
            }
            floatingActionButton.show();
            return true;
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if (SNACKBAR_BEHAVIOR_ENABLED && view instanceof Snackbar.SnackbarLayout) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View view) {
            if (view instanceof Snackbar.SnackbarLayout) {
                this.updateFabTranslationForSnackbar(coordinatorLayout, floatingActionButton, view);
                return false;
            }
            if (!(view instanceof AppBarLayout)) return false;
            this.updateFabVisibility(coordinatorLayout, (AppBarLayout)view, floatingActionButton);
            return false;
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, int n) {
            List<View> list = coordinatorLayout.getDependencies((View)floatingActionButton);
            int n2 = 0;
            int n3 = list.size();
            do {
                View view;
                if (n2 >= n3 || (view = list.get(n2)) instanceof AppBarLayout && this.updateFabVisibility(coordinatorLayout, (AppBarLayout)view, floatingActionButton)) {
                    coordinatorLayout.onLayoutChild((View)floatingActionButton, n);
                    this.offsetIfNeeded(coordinatorLayout, floatingActionButton);
                    return true;
                }
                ++n2;
            } while (true);
        }
    }

    public static abstract class OnVisibilityChangedListener {
        public void onHidden(FloatingActionButton floatingActionButton) {
        }

        public void onShown(FloatingActionButton floatingActionButton) {
        }
    }

}

