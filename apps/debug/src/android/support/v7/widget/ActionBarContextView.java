/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
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
package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.AbsActionBarView;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.ViewUtils;
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
import java.io.Serializable;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
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
        ViewCompat.setBackground((View)this, object.getDrawable(R.styleable.ActionMode_background));
        this.mTitleStyleRes = object.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = object.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
        this.mContentHeight = object.getLayoutDimension(R.styleable.ActionMode_height, 0);
        this.mCloseItemLayout = object.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
        object.recycle();
    }

    private void initTitle() {
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
        boolean bl = TextUtils.isEmpty((CharSequence)this.mTitle);
        boolean bl2 = TextUtils.isEmpty((CharSequence)this.mSubtitle) ^ true;
        TextView textView = this.mSubtitleView;
        int n = 0;
        int n2 = bl2 ? 0 : 8;
        textView.setVisibility(n2);
        textView = this.mTitleLayout;
        n2 = n;
        if (!(bl ^ true)) {
            n2 = bl2 ? n : 8;
        }
        textView.setVisibility(n2);
        if (this.mTitleLayout.getParent() == null) {
            this.addView((View)this.mTitleLayout);
        }
    }

    public void closeMode() {
        if (this.mClose == null) {
            this.killMode();
            return;
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

    public void initForMode(ActionMode object) {
        View view = this.mClose;
        if (view == null) {
            this.mClose = LayoutInflater.from((Context)this.getContext()).inflate(this.mCloseItemLayout, (ViewGroup)this, false);
            this.addView(this.mClose);
        } else if (view.getParent() == null) {
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
        view = new ViewGroup.LayoutParams(-2, -1);
        object.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
        this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
        ViewCompat.setBackground((View)this.mMenuView, null);
        this.addView((View)this.mMenuView, (ViewGroup.LayoutParams)view);
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

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 32) {
            accessibilityEvent.setSource((View)this);
            accessibilityEvent.setClassName((CharSequence)this.getClass().getName());
            accessibilityEvent.setPackageName((CharSequence)this.getContext().getPackageName());
            accessibilityEvent.setContentDescription(this.mTitle);
            return;
        }
        super.onInitializeAccessibilityEvent(accessibilityEvent);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        bl = ViewUtils.isLayoutRtl((View)this);
        int n5 = bl ? n3 - n - this.getPaddingRight() : this.getPaddingLeft();
        int n6 = this.getPaddingTop();
        int n7 = n4 - n2 - this.getPaddingTop() - this.getPaddingBottom();
        View view = this.mClose;
        if (view != null && view.getVisibility() != 8) {
            view = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
            n2 = bl ? view.rightMargin : view.leftMargin;
            n4 = bl ? view.leftMargin : view.rightMargin;
            n2 = ActionBarContextView.next(n5, n2, bl);
            n2 = ActionBarContextView.next(n2 + this.positionChild(this.mClose, n2, n6, n7, bl), n4, bl);
        } else {
            n2 = n5;
        }
        view = this.mTitleLayout;
        n4 = n2;
        if (view != null) {
            n4 = n2;
            if (this.mCustomView == null) {
                n4 = n2;
                if (view.getVisibility() != 8) {
                    n4 = n2 + this.positionChild((View)this.mTitleLayout, n2, n6, n7, bl);
                }
            }
        }
        if ((view = this.mCustomView) != null) {
            this.positionChild(view, n4, n6, n7, bl);
        }
        n = bl ? this.getPaddingLeft() : n3 - n - this.getPaddingRight();
        if (this.mMenuView != null) {
            this.positionChild((View)this.mMenuView, n, n6, n7, bl ^ true);
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        int n4 = 1073741824;
        if (n3 == 1073741824) {
            if (View.MeasureSpec.getMode((int)n2) != 0) {
                int n5 = View.MeasureSpec.getSize((int)n);
                n3 = this.mContentHeight > 0 ? this.mContentHeight : View.MeasureSpec.getSize((int)n2);
                int n6 = this.getPaddingTop() + this.getPaddingBottom();
                n = n5 - this.getPaddingLeft() - this.getPaddingRight();
                int n7 = n3 - n6;
                int n8 = View.MeasureSpec.makeMeasureSpec((int)n7, (int)Integer.MIN_VALUE);
                View view = this.mClose;
                int n9 = 0;
                n2 = n;
                if (view != null) {
                    n = this.measureChildView(view, n, n8, 0);
                    view = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
                    n2 = n - (view.leftMargin + view.rightMargin);
                }
                n = n2;
                if (this.mMenuView != null) {
                    n = n2;
                    if (this.mMenuView.getParent() == this) {
                        n = this.measureChildView((View)this.mMenuView, n2, n8, 0);
                    }
                }
                view = this.mTitleLayout;
                n2 = n;
                if (view != null) {
                    n2 = n;
                    if (this.mCustomView == null) {
                        if (this.mTitleOptional) {
                            n2 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                            this.mTitleLayout.measure(n2, n8);
                            int n10 = this.mTitleLayout.getMeasuredWidth();
                            n8 = n10 <= n ? 1 : 0;
                            n2 = n;
                            if (n8 != 0) {
                                n2 = n - n10;
                            }
                            view = this.mTitleLayout;
                            n = n8 != 0 ? n9 : 8;
                            view.setVisibility(n);
                        } else {
                            n2 = this.measureChildView(view, n, n8, 0);
                        }
                    }
                }
                if ((view = this.mCustomView) != null) {
                    view = view.getLayoutParams();
                    n = view.width != -2 ? 1073741824 : Integer.MIN_VALUE;
                    if (view.width >= 0) {
                        n2 = Math.min(view.width, n2);
                    }
                    n8 = view.height != -2 ? n4 : Integer.MIN_VALUE;
                    n4 = view.height >= 0 ? Math.min(view.height, n7) : n7;
                    this.mCustomView.measure(View.MeasureSpec.makeMeasureSpec((int)n2, (int)n), View.MeasureSpec.makeMeasureSpec((int)n4, (int)n8));
                }
                if (this.mContentHeight <= 0) {
                    n2 = 0;
                    n4 = this.getChildCount();
                    for (n = 0; n < n4; ++n) {
                        n8 = this.getChildAt(n).getMeasuredHeight() + n6;
                        n3 = n2;
                        if (n8 > n2) {
                            n3 = n8;
                        }
                        n2 = n3;
                    }
                    this.setMeasuredDimension(n5, n2);
                    return;
                }
                this.setMeasuredDimension(n5, n3);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.getClass().getSimpleName());
            stringBuilder.append(" can only be used ");
            stringBuilder.append("with android:layout_height=\"wrap_content\"");
            throw new IllegalStateException(stringBuilder.toString());
        }
        Serializable serializable = new StringBuilder();
        serializable.append(this.getClass().getSimpleName());
        serializable.append(" can only be used ");
        serializable.append("with android:layout_width=\"match_parent\" (or fill_parent)");
        serializable = new IllegalStateException(serializable.toString());
        throw serializable;
    }

    @Override
    public void setContentHeight(int n) {
        this.mContentHeight = n;
    }

    public void setCustomView(View view) {
        View view2 = this.mCustomView;
        if (view2 != null) {
            this.removeView(view2);
        }
        this.mCustomView = view;
        if (view != null && (view2 = this.mTitleLayout) != null) {
            this.removeView(view2);
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

