/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.ContextThemeWrapper
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewDebug
 *  android.view.ViewDebug$ExportedProperty
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView
extends LinearLayoutCompat
implements MenuBuilder.ItemInvoker,
MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setBaselineAligned(false);
        float f = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * f);
        this.mGeneratedItemPadding = (int)(4.0f * f);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    static int measureChildForCells(View var0, int var1_1, int var2_2, int var3_3, int var4_4) {
        var10_5 = (LayoutParams)var0.getLayoutParams();
        var6_6 = View.MeasureSpec.makeMeasureSpec((int)(View.MeasureSpec.getSize((int)var3_3) - var4_4), (int)View.MeasureSpec.getMode((int)var3_3));
        var9_7 = var0 instanceof ActionMenuItemView != false ? (ActionMenuItemView)var0 : null;
        var8_8 = false;
        var4_4 = var9_7 != null && var9_7.hasText() != false ? 1 : 0;
        var3_3 = var5_9 = 0;
        if (var2_2 <= 0) ** GOTO lbl21
        if (var4_4 == 0) ** GOTO lbl-1000
        var3_3 = var5_9;
        if (var2_2 >= 2) lbl-1000: // 2 sources:
        {
            var0.measure(View.MeasureSpec.makeMeasureSpec((int)(var1_1 * var2_2), (int)Integer.MIN_VALUE), var6_6);
            var5_9 = var0.getMeasuredWidth();
            var2_2 = var3_3 = var5_9 / var1_1;
            if (var5_9 % var1_1 != 0) {
                var2_2 = var3_3 + 1;
            }
            var3_3 = var2_2;
            if (var4_4 != 0) {
                var3_3 = var2_2;
                if (var2_2 < 2) {
                    var3_3 = 2;
                }
            }
        }
lbl21: // 8 sources:
        var7_10 = var8_8;
        if (!var10_5.isOverflowButton) {
            var7_10 = var8_8;
            if (var4_4 != 0) {
                var7_10 = true;
            }
        }
        var10_5.expandable = var7_10;
        var10_5.cellsUsed = var3_3;
        var0.measure(View.MeasureSpec.makeMeasureSpec((int)(var3_3 * var1_1), (int)1073741824), var6_6);
        return var3_3;
    }

    private void onMeasureExactFormat(int n, int n2) {
        int n3;
        int n4;
        Object object;
        LayoutParams layoutParams;
        int n5;
        int n6 = View.MeasureSpec.getMode((int)n2);
        n = View.MeasureSpec.getSize((int)n);
        int n7 = View.MeasureSpec.getSize((int)n2);
        int n8 = this.getPaddingLeft() + this.getPaddingRight();
        int n9 = this.getPaddingTop() + this.getPaddingBottom();
        int n10 = ActionMenuView.getChildMeasureSpec((int)n2, (int)n9, (int)-2);
        int n11 = n - n8;
        n = this.mMinCellSize;
        int n12 = n11 / n;
        int n13 = n11 % n;
        if (n12 == 0) {
            this.setMeasuredDimension(n11, 0);
            return;
        }
        int n14 = n + n13 / n12;
        n2 = 0;
        int n15 = this.getChildCount();
        int n16 = 0;
        int n17 = 0;
        int n18 = 0;
        int n19 = 0;
        n = n12;
        long l = 0;
        for (n4 = 0; n4 < n15; ++n4) {
            object = this.getChildAt(n4);
            if (object.getVisibility() == 8) continue;
            boolean bl = object instanceof ActionMenuItemView;
            ++n17;
            if (bl) {
                n3 = this.mGeneratedItemPadding;
                object.setPadding(n3, 0, n3, 0);
            }
            layoutParams = (LayoutParams)object.getLayoutParams();
            layoutParams.expanded = false;
            layoutParams.extraPixels = 0;
            layoutParams.cellsUsed = 0;
            layoutParams.expandable = false;
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            bl = bl && ((ActionMenuItemView)object).hasText();
            layoutParams.preventEdgeOffset = bl;
            n3 = layoutParams.isOverflowButton ? 1 : n;
            n5 = ActionMenuView.measureChildForCells((View)object, n14, n3, n10, n9);
            n19 = Math.max(n19, n5);
            n3 = n18;
            if (layoutParams.expandable) {
                n3 = n18 + 1;
            }
            if (layoutParams.isOverflowButton) {
                n2 = 1;
            }
            n -= n5;
            n16 = Math.max(n16, object.getMeasuredHeight());
            if (n5 == 1) {
                l |= (long)(1 << n4);
                n18 = n3;
                continue;
            }
            n18 = n3;
        }
        n12 = n2 != 0 && n17 == 2 ? 1 : 0;
        n3 = 0;
        n8 = n;
        n = n3;
        n9 = n11;
        while (n18 > 0 && n8 > 0) {
            long l2;
            long l3 = 0;
            n4 = Integer.MAX_VALUE;
            n13 = 0;
            n3 = n;
            n = n13;
            for (n11 = 0; n11 < n15; ++n11) {
                object = (LayoutParams)this.getChildAt(n11).getLayoutParams();
                if (!object.expandable) {
                    n13 = n;
                    n5 = n4;
                    l2 = l3;
                } else if (object.cellsUsed < n4) {
                    n5 = object.cellsUsed;
                    l2 = 1 << n11;
                    n13 = 1;
                } else {
                    n13 = n;
                    n5 = n4;
                    l2 = l3;
                    if (object.cellsUsed == n4) {
                        l2 = l3 | 1 << n11;
                        n13 = n + 1;
                        n5 = n4;
                    }
                }
                n = n13;
                n4 = n5;
                l3 = l2;
            }
            l |= l3;
            if (n > n8) {
                n = n3;
                break;
            }
            for (n3 = 0; n3 < n15; ++n3) {
                object = this.getChildAt(n3);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if ((l3 & (long)(1 << n3)) == 0) {
                    n13 = n8;
                    l2 = l;
                    if (layoutParams.cellsUsed == n4 + 1) {
                        l2 = l | (long)(1 << n3);
                        n13 = n8;
                    }
                } else {
                    if (n12 != 0 && layoutParams.preventEdgeOffset && n8 == 1) {
                        n13 = this.mGeneratedItemPadding;
                        object.setPadding(n13 + n14, 0, n13, 0);
                    }
                    ++layoutParams.cellsUsed;
                    layoutParams.expanded = true;
                    n13 = n8 - 1;
                    l2 = l;
                }
                n8 = n13;
                l = l2;
            }
            n = 1;
        }
        n2 = n2 == 0 && n17 == 1 ? 1 : 0;
        if (n8 > 0 && l != 0 && (n8 < n17 - 1 || n2 != 0 || n19 > 1)) {
            float f = Long.bitCount(l);
            if (n2 == 0) {
                float f2;
                if ((l & 1) != 0) {
                    f2 = f;
                    if (!((LayoutParams)this.getChildAt((int)0).getLayoutParams()).preventEdgeOffset) {
                        f2 = f - 0.5f;
                    }
                } else {
                    f2 = f;
                }
                f = f2;
                if ((l & (long)(1 << n15 - 1)) != 0) {
                    f = f2;
                    if (!((LayoutParams)this.getChildAt((int)(n15 - 1)).getLayoutParams()).preventEdgeOffset) {
                        f = f2 - 0.5f;
                    }
                }
            }
            n18 = f > 0.0f ? (int)((float)(n8 * n14) / f) : 0;
            n3 = n2;
            for (n13 = 0; n13 < n15; ++n13) {
                if ((l & (long)(1 << n13)) == 0) {
                    n2 = n;
                } else {
                    object = this.getChildAt(n13);
                    layoutParams = (LayoutParams)object.getLayoutParams();
                    if (object instanceof ActionMenuItemView) {
                        layoutParams.extraPixels = n18;
                        layoutParams.expanded = true;
                        if (n13 == 0 && !layoutParams.preventEdgeOffset) {
                            layoutParams.leftMargin = (- n18) / 2;
                        }
                        n2 = 1;
                    } else if (layoutParams.isOverflowButton) {
                        layoutParams.extraPixels = n18;
                        layoutParams.expanded = true;
                        layoutParams.rightMargin = (- n18) / 2;
                        n2 = 1;
                    } else {
                        if (n13 != 0) {
                            layoutParams.leftMargin = n18 / 2;
                        }
                        n2 = n;
                        if (n13 != n15 - 1) {
                            layoutParams.rightMargin = n18 / 2;
                            n2 = n;
                        }
                    }
                }
                n = n2;
            }
        }
        if (n != 0) {
            for (n = 0; n < n15; ++n) {
                object = this.getChildAt(n);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if (!layoutParams.expanded) continue;
                object.measure(View.MeasureSpec.makeMeasureSpec((int)(layoutParams.cellsUsed * n14 + layoutParams.extraPixels), (int)1073741824), n10);
            }
        }
        n = n6 != 1073741824 ? n16 : n7;
        this.setMeasuredDimension(n9, n);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams != null && layoutParams instanceof LayoutParams) {
            return true;
        }
        return false;
    }

    public void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus();
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams object) {
        if (object != null) {
            object = object instanceof LayoutParams ? new LayoutParams((LayoutParams)((Object)object)) : new LayoutParams((ViewGroup.LayoutParams)object);
            if (object.gravity <= 0) {
                object.gravity = 16;
            }
            return object;
        }
        return this.generateDefaultLayoutParams();
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams layoutParams = this.generateDefaultLayoutParams();
        layoutParams.isOverflowButton = true;
        return layoutParams;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Object object = this.getContext();
            this.mMenu = new MenuBuilder((Context)object);
            this.mMenu.setCallback(new MenuBuilderCallback());
            this.mPresenter = new ActionMenuPresenter((Context)object);
            this.mPresenter.setReserveOverflow(true);
            ActionMenuPresenter actionMenuPresenter = this.mPresenter;
            object = this.mActionMenuPresenterCallback;
            if (object == null) {
                object = new ActionMenuPresenterCallback();
            }
            actionMenuPresenter.setCallback((MenuPresenter.Callback)object);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    @Nullable
    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public int getWindowAnimations() {
        return 0;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected boolean hasSupportDividerBeforeChildAt(int n) {
        boolean bl;
        if (n == 0) {
            return false;
        }
        View view = this.getChildAt(n - 1);
        View view2 = this.getChildAt(n);
        boolean bl2 = bl = false;
        if (n < this.getChildCount()) {
            bl2 = bl;
            if (view instanceof ActionMenuChildView) {
                bl2 = false | ((ActionMenuChildView)view).needsDividerAfter();
            }
        }
        bl = bl2;
        if (n > 0) {
            bl = bl2;
            if (view2 instanceof ActionMenuChildView) {
                bl = bl2 | ((ActionMenuChildView)view2).needsDividerBefore();
            }
        }
        return bl;
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu()) {
            return true;
        }
        return false;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isOverflowMenuShowPending() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing()) {
            return true;
        }
        return false;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void onConfigurationChanged(Configuration object) {
        super.onConfigurationChanged((Configuration)object);
        object = this.mPresenter;
        if (object != null) {
            object.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dismissPopupMenus();
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        Object object;
        LayoutParams layoutParams;
        int n5;
        ActionMenuView actionMenuView = this;
        if (!actionMenuView.mFormatItems) {
            super.onLayout(bl, n, n2, n3, n4);
            return;
        }
        int n6 = this.getChildCount();
        int n7 = (n4 - n2) / 2;
        int n8 = this.getDividerWidth();
        n2 = 0;
        int n9 = 0;
        int n10 = 0;
        n4 = n3 - n - this.getPaddingRight() - this.getPaddingLeft();
        int n11 = 0;
        bl = ViewUtils.isLayoutRtl((View)this);
        for (n5 = 0; n5 < n6; ++n5) {
            int n12;
            int n13;
            object = actionMenuView.getChildAt(n5);
            if (object.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)object.getLayoutParams();
            if (layoutParams.isOverflowButton) {
                n2 = n11 = object.getMeasuredWidth();
                if (actionMenuView.hasSupportDividerBeforeChildAt(n5)) {
                    n2 = n11 + n8;
                }
                n13 = object.getMeasuredHeight();
                if (bl) {
                    n11 = this.getPaddingLeft() + layoutParams.leftMargin;
                    n12 = n11 + n2;
                } else {
                    n12 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                    n11 = n12 - n2;
                }
                int n14 = n7 - n13 / 2;
                object.layout(n11, n14, n12, n14 + n13);
                n4 -= n2;
                n11 = 1;
                continue;
            }
            n13 = object.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            n12 = n9 + n13;
            n4 -= n13;
            n9 = n12;
            if (actionMenuView.hasSupportDividerBeforeChildAt(n5)) {
                n9 = n12 + n8;
            }
            ++n10;
        }
        n9 = 1;
        if (n6 == 1 && n11 == 0) {
            actionMenuView = actionMenuView.getChildAt(0);
            n2 = actionMenuView.getMeasuredWidth();
            n4 = actionMenuView.getMeasuredHeight();
            n = (n3 - n) / 2 - n2 / 2;
            n3 = n7 - n4 / 2;
            actionMenuView.layout(n, n3, n + n2, n3 + n4);
            return;
        }
        n = n9;
        if (n11 != 0) {
            n = 0;
        }
        n = (n = n10 - n) > 0 ? n4 / n : 0;
        n9 = Math.max(0, n);
        if (bl) {
            n4 = this.getWidth() - this.getPaddingRight();
            n3 = n8;
            for (n = 0; n < n6; ++n) {
                object = actionMenuView.getChildAt(n);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if (object.getVisibility() == 8 || layoutParams.isOverflowButton) continue;
                n5 = object.getMeasuredWidth();
                n10 = object.getMeasuredHeight();
                n11 = n7 - n10 / 2;
                object.layout(n4 - n5, n11, n4 -= layoutParams.rightMargin, n11 + n10);
                n4 -= layoutParams.leftMargin + n5 + n9;
            }
            return;
        }
        n2 = this.getPaddingLeft();
        for (n = 0; n < n6; ++n) {
            actionMenuView = this.getChildAt(n);
            object = (LayoutParams)actionMenuView.getLayoutParams();
            n3 = n2;
            if (actionMenuView.getVisibility() != 8) {
                if (object.isOverflowButton) {
                    n3 = n2;
                } else {
                    n3 = actionMenuView.getMeasuredWidth();
                    n4 = actionMenuView.getMeasuredHeight();
                    n5 = n7 - n4 / 2;
                    actionMenuView.layout(n2, n5, (n2 += object.leftMargin) + n3, n5 + n4);
                    n3 = n2 + (object.rightMargin + n3 + n9);
                }
            }
            n2 = n3;
        }
    }

    @Override
    protected void onMeasure(int n, int n2) {
        Object object;
        boolean bl = this.mFormatItems;
        boolean bl2 = View.MeasureSpec.getMode((int)n) == 1073741824;
        this.mFormatItems = bl2;
        if (bl != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int n3 = View.MeasureSpec.getSize((int)n);
        if (this.mFormatItems && (object = this.mMenu) != null && n3 != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = n3;
            object.onItemsChanged(true);
        }
        int n4 = this.getChildCount();
        if (this.mFormatItems && n4 > 0) {
            this.onMeasureExactFormat(n, n2);
            return;
        }
        for (n3 = 0; n3 < n4; ++n3) {
            object = (LayoutParams)this.getChildAt(n3).getLayoutParams();
            object.rightMargin = 0;
            object.leftMargin = 0;
        }
        super.onMeasure(n, n2);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setExpandedActionViewsExclusive(boolean bl) {
        this.mPresenter.setExpandedActionViewsExclusive(bl);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setMenuCallbacks(MenuPresenter.Callback callback, MenuBuilder.Callback callback2) {
        this.mActionMenuPresenterCallback = callback;
        this.mMenuBuilderCallback = callback2;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOverflowIcon(@Nullable Drawable drawable2) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(drawable2);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setOverflowReserved(boolean bl) {
        this.mReserveOverflow = bl;
    }

    public void setPopupTheme(@StyleRes int n) {
        if (this.mPopupTheme != n) {
            this.mPopupTheme = n;
            if (n == 0) {
                this.mPopupContext = this.getContext();
                return;
            }
            this.mPopupContext = new ContextThemeWrapper(this.getContext(), n);
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
        this.mPresenter.setMenuView(this);
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu()) {
            return true;
        }
        return false;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static interface ActionMenuChildView {
        public boolean needsDividerAfter();

        public boolean needsDividerBefore();
    }

    private static class ActionMenuPresenterCallback
    implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            return false;
        }
    }

    public static class LayoutParams
    extends LinearLayoutCompat.LayoutParams {
        @ViewDebug.ExportedProperty
        public int cellsUsed;
        @ViewDebug.ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug.ExportedProperty
        public int extraPixels;
        @ViewDebug.ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug.ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(int n, int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }

        LayoutParams(int n, int n2, boolean bl) {
            super(n, n2);
            this.isOverflowButton = bl;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    private class MenuBuilderCallback
    implements MenuBuilder.Callback {
        MenuBuilderCallback() {
        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(menuItem)) {
                return true;
            }
            return false;
        }

        @Override
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }

    public static interface OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem var1);
    }

}

