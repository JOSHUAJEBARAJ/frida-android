/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.text.Layout
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 *  android.widget.Button
 *  android.widget.FrameLayout
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SnackbarManager;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.design.widget.ThemeUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Snackbar {
    private static final int ANIMATION_DURATION = 250;
    private static final int ANIMATION_FADE_DURATION = 180;
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    private static final int MSG_DISMISS = 1;
    private static final int MSG_SHOW = 0;
    private static final Handler sHandler = new Handler(Looper.getMainLooper(), new Handler.Callback(){

        public boolean handleMessage(Message message) {
            switch (message.what) {
                default: {
                    return false;
                }
                case 0: {
                    ((Snackbar)message.obj).showView();
                    return true;
                }
                case 1: 
            }
            ((Snackbar)message.obj).hideView(message.arg1);
            return true;
        }
    });
    private Callback mCallback;
    private final Context mContext;
    private int mDuration;
    private final SnackbarManager.Callback mManagerCallback;
    private final ViewGroup mTargetParent;
    private final SnackbarLayout mView;

    private Snackbar(ViewGroup viewGroup) {
        this.mManagerCallback = new SnackbarManager.Callback(){

            @Override
            public void dismiss(int n) {
                sHandler.sendMessage(sHandler.obtainMessage(1, n, 0, (Object)Snackbar.this));
            }

            @Override
            public void show() {
                sHandler.sendMessage(sHandler.obtainMessage(0, (Object)Snackbar.this));
            }
        };
        this.mTargetParent = viewGroup;
        this.mContext = viewGroup.getContext();
        ThemeUtils.checkAppCompatTheme(this.mContext);
        this.mView = (SnackbarLayout)LayoutInflater.from((Context)this.mContext).inflate(R.layout.design_layout_snackbar, this.mTargetParent, false);
    }

    private void animateViewIn() {
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.setTranslationY((View)this.mView, this.mView.getHeight());
            ViewCompat.animate((View)this.mView).translationY(0.0f).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setDuration(250).setListener(new ViewPropertyAnimatorListenerAdapter(){

                @Override
                public void onAnimationEnd(View view) {
                    if (Snackbar.this.mCallback != null) {
                        Snackbar.this.mCallback.onShown(Snackbar.this);
                    }
                    SnackbarManager.getInstance().onShown(Snackbar.this.mManagerCallback);
                }

                @Override
                public void onAnimationStart(View view) {
                    Snackbar.this.mView.animateChildrenIn(70, 180);
                }
            }).start();
            return;
        }
        Animation animation = android.view.animation.AnimationUtils.loadAnimation((Context)this.mView.getContext(), (int)R.anim.design_snackbar_in);
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                if (Snackbar.this.mCallback != null) {
                    Snackbar.this.mCallback.onShown(Snackbar.this);
                }
                SnackbarManager.getInstance().onShown(Snackbar.this.mManagerCallback);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.mView.startAnimation(animation);
    }

    private void animateViewOut(final int n) {
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate((View)this.mView).translationY(this.mView.getHeight()).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).setDuration(250).setListener(new ViewPropertyAnimatorListenerAdapter(){

                @Override
                public void onAnimationEnd(View view) {
                    Snackbar.this.onViewHidden(n);
                }

                @Override
                public void onAnimationStart(View view) {
                    Snackbar.this.mView.animateChildrenOut(0, 180);
                }
            }).start();
            return;
        }
        Animation animation = android.view.animation.AnimationUtils.loadAnimation((Context)this.mView.getContext(), (int)R.anim.design_snackbar_out);
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                Snackbar.this.onViewHidden(n);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.mView.startAnimation(animation);
    }

    private void dispatchDismiss(int n) {
        SnackbarManager.getInstance().dismiss(this.mManagerCallback, n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static ViewGroup findSuitableParent(View view) {
        ViewGroup viewGroup;
        ViewGroup viewGroup2 = null;
        View view2 = view;
        do {
            if (view2 instanceof CoordinatorLayout) {
                return (ViewGroup)view2;
            }
            viewGroup = viewGroup2;
            if (view2 instanceof FrameLayout) {
                if (view2.getId() == 16908290) {
                    return (ViewGroup)view2;
                }
                viewGroup = (ViewGroup)view2;
            }
            view = view2;
            if (view2 != null && !((view = view2.getParent()) instanceof View)) {
                view = null;
            }
            viewGroup2 = viewGroup;
            view2 = view;
        } while (view != null);
        return viewGroup;
    }

    private boolean isBeingDragged() {
        boolean bl = false;
        Object object = this.mView.getLayoutParams();
        boolean bl2 = bl;
        if (object instanceof CoordinatorLayout.LayoutParams) {
            object = ((CoordinatorLayout.LayoutParams)((Object)object)).getBehavior();
            bl2 = bl;
            if (object instanceof SwipeDismissBehavior) {
                bl2 = bl;
                if (((SwipeDismissBehavior)object).getDragState() != 0) {
                    bl2 = true;
                }
            }
        }
        return bl2;
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @StringRes int n, int n2) {
        return Snackbar.make(view, view.getResources().getText(n), n2);
    }

    @NonNull
    public static Snackbar make(@NonNull View object, @NonNull CharSequence charSequence, int n) {
        object = new Snackbar(Snackbar.findSuitableParent((View)object));
        object.setText(charSequence);
        object.setDuration(n);
        return object;
    }

    private void onViewHidden(int n) {
        ViewParent viewParent = this.mView.getParent();
        if (viewParent instanceof ViewGroup) {
            ((ViewGroup)viewParent).removeView((View)this.mView);
        }
        if (this.mCallback != null) {
            this.mCallback.onDismissed(this, n);
        }
        SnackbarManager.getInstance().onDismissed(this.mManagerCallback);
    }

    public void dismiss() {
        this.dispatchDismiss(3);
    }

    public int getDuration() {
        return this.mDuration;
    }

    @NonNull
    public View getView() {
        return this.mView;
    }

    final void hideView(int n) {
        if (this.mView.getVisibility() != 0 || this.isBeingDragged()) {
            this.onViewHidden(n);
            return;
        }
        this.animateViewOut(n);
    }

    public boolean isShown() {
        return SnackbarManager.getInstance().isCurrent(this.mManagerCallback);
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(this.mManagerCallback);
    }

    @NonNull
    public Snackbar setAction(@StringRes int n, View.OnClickListener onClickListener) {
        return this.setAction(this.mContext.getText(n), onClickListener);
    }

    @NonNull
    public Snackbar setAction(CharSequence charSequence, final View.OnClickListener onClickListener) {
        Button button = this.mView.getActionView();
        if (TextUtils.isEmpty((CharSequence)charSequence) || onClickListener == null) {
            button.setVisibility(8);
            button.setOnClickListener(null);
            return this;
        }
        button.setVisibility(0);
        button.setText(charSequence);
        button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                onClickListener.onClick(view);
                Snackbar.this.dispatchDismiss(1);
            }
        });
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(@ColorInt int n) {
        this.mView.getActionView().setTextColor(n);
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(ColorStateList colorStateList) {
        this.mView.getActionView().setTextColor(colorStateList);
        return this;
    }

    @NonNull
    public Snackbar setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    @NonNull
    public Snackbar setDuration(int n) {
        this.mDuration = n;
        return this;
    }

    @NonNull
    public Snackbar setText(@StringRes int n) {
        return this.setText(this.mContext.getText(n));
    }

    @NonNull
    public Snackbar setText(@NonNull CharSequence charSequence) {
        this.mView.getMessageView().setText(charSequence);
        return this;
    }

    public void show() {
        SnackbarManager.getInstance().show(this.mDuration, this.mManagerCallback);
    }

    final void showView() {
        if (this.mView.getParent() == null) {
            ViewGroup.LayoutParams layoutParams = this.mView.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(0);
                behavior.setListener(new SwipeDismissBehavior.OnDismissListener(){

                    @Override
                    public void onDismiss(View view) {
                        Snackbar.this.dispatchDismiss(0);
                    }

                    @Override
                    public void onDragStateChanged(int n) {
                        switch (n) {
                            default: {
                                return;
                            }
                            case 1: 
                            case 2: {
                                SnackbarManager.getInstance().cancelTimeout(Snackbar.this.mManagerCallback);
                                return;
                            }
                            case 0: 
                        }
                        SnackbarManager.getInstance().restoreTimeout(Snackbar.this.mManagerCallback);
                    }
                });
                ((CoordinatorLayout.LayoutParams)layoutParams).setBehavior(behavior);
            }
            this.mTargetParent.addView((View)this.mView);
        }
        this.mView.setOnAttachStateChangeListener(new SnackbarLayout.OnAttachStateChangeListener(){

            @Override
            public void onViewAttachedToWindow(View view) {
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                if (Snackbar.this.isShownOrQueued()) {
                    sHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            Snackbar.this.onViewHidden(3);
                        }
                    });
                }
            }

        });
        if (ViewCompat.isLaidOut((View)this.mView)) {
            this.animateViewIn();
            return;
        }
        this.mView.setOnLayoutChangeListener(new SnackbarLayout.OnLayoutChangeListener(){

            @Override
            public void onLayoutChange(View view, int n, int n2, int n3, int n4) {
                Snackbar.this.animateViewIn();
                Snackbar.this.mView.setOnLayoutChangeListener(null);
            }
        });
    }

    final class Behavior
    extends SwipeDismissBehavior<SnackbarLayout> {
        Behavior() {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, SnackbarLayout snackbarLayout, MotionEvent motionEvent) {
            if (!coordinatorLayout.isPointInChildBounds((View)snackbarLayout, (int)motionEvent.getX(), (int)motionEvent.getY())) return super.onInterceptTouchEvent(coordinatorLayout, snackbarLayout, motionEvent);
            switch (motionEvent.getActionMasked()) {
                case 0: {
                    SnackbarManager.getInstance().cancelTimeout(Snackbar.this.mManagerCallback);
                }
                default: {
                    return super.onInterceptTouchEvent(coordinatorLayout, snackbarLayout, motionEvent);
                }
                case 1: 
                case 3: 
            }
            SnackbarManager.getInstance().restoreTimeout(Snackbar.this.mManagerCallback);
            return super.onInterceptTouchEvent(coordinatorLayout, snackbarLayout, motionEvent);
        }
    }

    public static abstract class Callback {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        public void onDismissed(Snackbar snackbar, int n) {
        }

        public void onShown(Snackbar snackbar) {
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface DismissEvent {
        }

    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Duration {
    }

    public static class SnackbarLayout
    extends LinearLayout {
        private Button mActionView;
        private int mMaxInlineActionWidth;
        private int mMaxWidth;
        private TextView mMessageView;
        private OnAttachStateChangeListener mOnAttachStateChangeListener;
        private OnLayoutChangeListener mOnLayoutChangeListener;

        public SnackbarLayout(Context context) {
            this(context, null);
        }

        public SnackbarLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.SnackbarLayout);
            this.mMaxWidth = attributeSet.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
            this.mMaxInlineActionWidth = attributeSet.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
            if (attributeSet.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation((View)this, attributeSet.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            attributeSet.recycle();
            this.setClickable(true);
            LayoutInflater.from((Context)context).inflate(R.layout.design_layout_snackbar_include, (ViewGroup)this);
            ViewCompat.setAccessibilityLiveRegion((View)this, 1);
        }

        private static void updateTopBottomPadding(View view, int n, int n2) {
            if (ViewCompat.isPaddingRelative(view)) {
                ViewCompat.setPaddingRelative(view, ViewCompat.getPaddingStart(view), n, ViewCompat.getPaddingEnd(view), n2);
                return;
            }
            view.setPadding(view.getPaddingLeft(), n, view.getPaddingRight(), n2);
        }

        private boolean updateViewsWithinLayout(int n, int n2, int n3) {
            boolean bl = false;
            if (n != this.getOrientation()) {
                this.setOrientation(n);
                bl = true;
            }
            if (this.mMessageView.getPaddingTop() != n2 || this.mMessageView.getPaddingBottom() != n3) {
                SnackbarLayout.updateTopBottomPadding((View)this.mMessageView, n2, n3);
                bl = true;
            }
            return bl;
        }

        void animateChildrenIn(int n, int n2) {
            ViewCompat.setAlpha((View)this.mMessageView, 0.0f);
            ViewCompat.animate((View)this.mMessageView).alpha(1.0f).setDuration(n2).setStartDelay(n).start();
            if (this.mActionView.getVisibility() == 0) {
                ViewCompat.setAlpha((View)this.mActionView, 0.0f);
                ViewCompat.animate((View)this.mActionView).alpha(1.0f).setDuration(n2).setStartDelay(n).start();
            }
        }

        void animateChildrenOut(int n, int n2) {
            ViewCompat.setAlpha((View)this.mMessageView, 1.0f);
            ViewCompat.animate((View)this.mMessageView).alpha(0.0f).setDuration(n2).setStartDelay(n).start();
            if (this.mActionView.getVisibility() == 0) {
                ViewCompat.setAlpha((View)this.mActionView, 1.0f);
                ViewCompat.animate((View)this.mActionView).alpha(0.0f).setDuration(n2).setStartDelay(n).start();
            }
        }

        Button getActionView() {
            return this.mActionView;
        }

        TextView getMessageView() {
            return this.mMessageView;
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.mOnAttachStateChangeListener != null) {
                this.mOnAttachStateChangeListener.onViewAttachedToWindow((View)this);
            }
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.mOnAttachStateChangeListener != null) {
                this.mOnAttachStateChangeListener.onViewDetachedFromWindow((View)this);
            }
        }

        protected void onFinishInflate() {
            super.onFinishInflate();
            this.mMessageView = (TextView)this.findViewById(R.id.snackbar_text);
            this.mActionView = (Button)this.findViewById(R.id.snackbar_action);
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            super.onLayout(bl, n, n2, n3, n4);
            if (bl && this.mOnLayoutChangeListener != null) {
                this.mOnLayoutChangeListener.onLayoutChange((View)this, n, n2, n3, n4);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        protected void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            int n3 = n;
            if (this.mMaxWidth > 0) {
                n3 = n;
                if (this.getMeasuredWidth() > this.mMaxWidth) {
                    n3 = View.MeasureSpec.makeMeasureSpec((int)this.mMaxWidth, (int)1073741824);
                    super.onMeasure(n3, n2);
                }
            }
            int n4 = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
            int n5 = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
            n = this.mMessageView.getLayout().getLineCount() > 1 ? 1 : 0;
            int n6 = 0;
            if (n != 0 && this.mMaxInlineActionWidth > 0 && this.mActionView.getMeasuredWidth() > this.mMaxInlineActionWidth) {
                n = n6;
                if (this.updateViewsWithinLayout(1, n4, n4 - n5)) {
                    n = 1;
                }
            } else {
                if (n == 0) {
                    n4 = n5;
                }
                n = n6;
                if (this.updateViewsWithinLayout(0, n4, n4)) {
                    n = 1;
                }
            }
            if (n != 0) {
                super.onMeasure(n3, n2);
            }
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener onAttachStateChangeListener) {
            this.mOnAttachStateChangeListener = onAttachStateChangeListener;
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            this.mOnLayoutChangeListener = onLayoutChangeListener;
        }

        static interface OnAttachStateChangeListener {
            public void onViewAttachedToWindow(View var1);

            public void onViewDetachedFromWindow(View var1);
        }

        static interface OnLayoutChangeListener {
            public void onLayoutChange(View var1, int var2, int var3, int var4, int var5);
        }

    }

}

