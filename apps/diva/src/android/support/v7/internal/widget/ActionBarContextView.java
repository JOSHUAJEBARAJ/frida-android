/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.appcompat.R;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.widget.AbsActionBarView;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.internal.widget.ViewUtils;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.ActionMenuView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionBarContextView
extends AbsActionBarView {
    private static final String TAG = "ActionBarContextView";
    private View mClose;
    private int mCloseItemLayout;
    private View mCustomView;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.actionModeStyle);
    }

    public ActionBarContextView(Context object, AttributeSet attributeSet, int n) {
        super((Context)object, attributeSet, n);
        object = TintTypedArray.obtainStyledAttributes((Context)object, attributeSet, R.styleable.ActionMode, n, 0);
        this.setBackgroundDrawable(object.getDrawable(R.styleable.ActionMode_background));
        this.mTitleStyleRes = object.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = object.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
        this.mContentHeight = object.getLayoutDimension(R.styleable.ActionMode_height, 0);
        this.mCloseItemLayout = object.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
        object.recycle();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void initTitle() {
        TextView textView;
        int n;
        block6 : {
            int n2 = 8;
            if (this.mTitleLayout == null) {
                LayoutInflater.from((Context)this.getContext()).inflate(R.layout.abc_action_bar_title_item, (ViewGroup)this);
                this.mTitleLayout = (LinearLayout)this.getChildAt(this.getChildCount() - 1);
                this.mTitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_title);
                this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
                if (this.mTitleStyleRes != 0) {
                    this.mTitleView.setTextAppearance(this.getContext(), this.mTitleStyleRes);
                }
                if (this.mSubtitleStyleRes != 0) {
                    this.mSubtitleView.setTextAppearance(this.getContext(), this.mSubtitleStyleRes);
                }
            }
            this.mTitleView.setText(this.mTitle);
            this.mSubtitleView.setText(this.mSubtitle);
            n = !TextUtils.isEmpty((CharSequence)this.mTitle) ? 1 : 0;
            boolean bl = !TextUtils.isEmpty((CharSequence)this.mSubtitle);
            textView = this.mSubtitleView;
            int n3 = bl ? 0 : 8;
            textView.setVisibility(n3);
            textView = this.mTitleLayout;
            if (n == 0) {
                n = n2;
                if (!bl) break block6;
            }
            n = 0;
        }
        textView.setVisibility(n);
        if (this.mTitleLayout.getParent() == null) {
            this.addView((View)this.mTitleLayout);
        }
    }

    public void closeMode() {
        if (this.mClose == null) {
            this.killMode();
        }
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-1, -2);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(this.getContext(), attributeSet);
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    @Override
    public boolean hideOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.hideOverflowMenu();
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void initForMode(ActionMode object) {
        if (this.mClose == null) {
            this.mClose = LayoutInflater.from((Context)this.getContext()).inflate(this.mCloseItemLayout, (ViewGroup)this, false);
            this.addView(this.mClose);
        } else if (this.mClose.getParent() == null) {
            this.addView(this.mClose);
        }
        this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener(new View.OnClickListener((ActionMode)object){
            final /* synthetic */ ActionMode val$mode;

            public void onClick(View view) {
                this.val$mode.finish();
            }
        });
        object = (MenuBuilder)object.getMenu();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
        this.mActionMenuPresenter = new ActionMenuPresenter(this.getContext());
        this.mActionMenuPresenter.setReserveOverflow(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
        object.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
        this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
        this.mMenuView.setBackgroundDrawable(null);
        this.addView((View)this.mMenuView, layoutParams);
    }

    @Override
    public boolean isOverflowMenuShowing() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.isOverflowMenuShowing();
        }
        return false;
    }

    public boolean isTitleOptional() {
        return this.mTitleOptional;
    }

    public void killMode() {
        this.removeAllViews();
        this.mCustomView = null;
        this.mMenuView = null;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (Build.VERSION.SDK_INT < 14) return;
        if (accessibilityEvent.getEventType() == 32) {
            accessibilityEvent.setSource((View)this);
            accessibilityEvent.setClassName((CharSequence)this.getClass().getName());
            accessibilityEvent.setPackageName((CharSequence)this.getContext().getPackageName());
            accessibilityEvent.setContentDescription(this.mTitle);
            return;
        }
        super.onInitializeAccessibilityEvent(accessibilityEvent);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        Object object;
        bl = ViewUtils.isLayoutRtl((View)this);
        int n5 = bl ? n3 - n - this.getPaddingRight() : this.getPaddingLeft();
        int n6 = this.getPaddingTop();
        int n7 = n4 - n2 - this.getPaddingTop() - this.getPaddingBottom();
        n2 = n5;
        if (this.mClose != null) {
            n2 = n5;
            if (this.mClose.getVisibility() != 8) {
                object = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
                n2 = bl ? object.rightMargin : object.leftMargin;
                n4 = bl ? object.leftMargin : object.rightMargin;
                n2 = ActionBarContextView.next(n5, n2, bl);
                n2 = ActionBarContextView.next(n2 + this.positionChild(this.mClose, n2, n6, n7, bl), n4, bl);
            }
        }
        n4 = n2;
        if (this.mTitleLayout != null) {
            n4 = n2;
            if (this.mCustomView == null) {
                n4 = n2;
                if (this.mTitleLayout.getVisibility() != 8) {
                    n4 = n2 + this.positionChild((View)this.mTitleLayout, n2, n6, n7, bl);
                }
            }
        }
        if (this.mCustomView != null) {
            this.positionChild(this.mCustomView, n4, n6, n7, bl);
        }
        n = bl ? this.getPaddingLeft() : n3 - n - this.getPaddingRight();
        if (this.mMenuView != null) {
            object = this.mMenuView;
            bl = !bl;
            this.positionChild((View)object, n, n6, n7, bl);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        ViewGroup.MarginLayoutParams marginLayoutParams;
        if (View.MeasureSpec.getMode((int)n) != 1073741824) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"match_parent\" (or fill_parent)");
        }
        if (View.MeasureSpec.getMode((int)n2) == 0) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
        }
        int n3 = View.MeasureSpec.getSize((int)n);
        int n4 = this.mContentHeight > 0 ? this.mContentHeight : View.MeasureSpec.getSize((int)n2);
        int n5 = this.getPaddingTop() + this.getPaddingBottom();
        n = n3 - this.getPaddingLeft() - this.getPaddingRight();
        int n6 = n4 - n5;
        int n7 = View.MeasureSpec.makeMeasureSpec((int)n6, (int)Integer.MIN_VALUE);
        n2 = n;
        if (this.mClose != null) {
            n = this.measureChildView(this.mClose, n, n7, 0);
            marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
            n2 = n - (marginLayoutParams.leftMargin + marginLayoutParams.rightMargin);
        }
        n = n2;
        if (this.mMenuView != null) {
            n = n2;
            if (this.mMenuView.getParent() == this) {
                n = this.measureChildView((View)this.mMenuView, n2, n7, 0);
            }
        }
        n2 = n;
        if (this.mTitleLayout != null) {
            n2 = n;
            if (this.mCustomView == null) {
                if (this.mTitleOptional) {
                    n2 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                    this.mTitleLayout.measure(n2, n7);
                    int n8 = this.mTitleLayout.getMeasuredWidth();
                    n7 = n8 <= n ? 1 : 0;
                    n2 = n;
                    if (n7 != 0) {
                        n2 = n - n8;
                    }
                    marginLayoutParams = this.mTitleLayout;
                    n = n7 != 0 ? 0 : 8;
                    marginLayoutParams.setVisibility(n);
                } else {
                    n2 = this.measureChildView((View)this.mTitleLayout, n, n7, 0);
                }
            }
        }
        if (this.mCustomView != null) {
            marginLayoutParams = this.mCustomView.getLayoutParams();
            n = marginLayoutParams.width != -2 ? 1073741824 : Integer.MIN_VALUE;
            if (marginLayoutParams.width >= 0) {
                n2 = Math.min(marginLayoutParams.width, n2);
            }
            n7 = marginLayoutParams.height != -2 ? 1073741824 : Integer.MIN_VALUE;
            if (marginLayoutParams.height >= 0) {
                n6 = Math.min(marginLayoutParams.height, n6);
            }
            this.mCustomView.measure(View.MeasureSpec.makeMeasureSpec((int)n2, (int)n), View.MeasureSpec.makeMeasureSpec((int)n6, (int)n7));
        }
        if (this.mContentHeight > 0) {
            this.setMeasuredDimension(n3, n4);
            return;
        }
        n2 = 0;
        n6 = this.getChildCount();
        n = 0;
        do {
            if (n >= n6) {
                this.setMeasuredDimension(n3, n2);
                return;
            }
            n7 = this.getChildAt(n).getMeasuredHeight() + n5;
            n4 = n2;
            if (n7 > n2) {
                n4 = n7;
            }
            ++n;
            n2 = n4;
        } while (true);
    }

    @Override
    public void setContentHeight(int n) {
        this.mContentHeight = n;
    }

    public void setCustomView(View view) {
        if (this.mCustomView != null) {
            this.removeView(this.mCustomView);
        }
        this.mCustomView = view;
        if (view != null && this.mTitleLayout != null) {
            this.removeView((View)this.mTitleLayout);
            this.mTitleLayout = null;
        }
        if (view != null) {
            this.addView(view);
        }
        this.requestLayout();
    }

    public void setSubtitle(CharSequence charSequence) {
        this.mSubtitle = charSequence;
        this.initTitle();
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        this.initTitle();
    }

    public void setTitleOptional(boolean bl) {
        if (bl != this.mTitleOptional) {
            this.requestLayout();
        }
        this.mTitleOptional = bl;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    public boolean showOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.showOverflowMenu();
        }
        return false;
    }

}

