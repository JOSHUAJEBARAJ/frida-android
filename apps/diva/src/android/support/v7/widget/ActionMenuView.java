/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
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
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.widget.ViewUtils;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.LinearLayoutCompat;
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
    private MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    private OnMenuItemClickListener mOnMenuItemClickListener;
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
        var9_5 = (LayoutParams)var0.getLayoutParams();
        var6_6 = View.MeasureSpec.makeMeasureSpec((int)(View.MeasureSpec.getSize((int)var3_3) - var4_4), (int)View.MeasureSpec.getMode((int)var3_3));
        var8_7 = var0 instanceof ActionMenuItemView != false ? (ActionMenuItemView)var0 : null;
        var4_4 = var8_7 != null && var8_7.hasText() != false ? 1 : 0;
        var3_3 = var5_8 = 0;
        if (var2_2 <= 0) ** GOTO lbl20
        if (var4_4 == 0) ** GOTO lbl-1000
        var3_3 = var5_8;
        if (var2_2 >= 2) lbl-1000: // 2 sources:
        {
            var0.measure(View.MeasureSpec.makeMeasureSpec((int)(var1_1 * var2_2), (int)Integer.MIN_VALUE), var6_6);
            var5_8 = var0.getMeasuredWidth();
            var2_2 = var3_3 = var5_8 / var1_1;
            if (var5_8 % var1_1 != 0) {
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
lbl20: // 8 sources:
        var7_9 = var9_5.isOverflowButton == false && var4_4 != 0;
        var9_5.expandable = var7_9;
        var9_5.cellsUsed = var3_3;
        var0.measure(View.MeasureSpec.makeMeasureSpec((int)(var3_3 * var1_1), (int)1073741824), var6_6);
        return var3_3;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void onMeasureExactFormat(int var1_1, int var2_2) {
        block39 : {
            var18_3 = View.MeasureSpec.getMode((int)var2_2);
            var1_1 = View.MeasureSpec.getSize((int)var1_1);
            var17_4 = View.MeasureSpec.getSize((int)var2_2);
            var6_5 = this.getPaddingLeft();
            var7_6 = this.getPaddingRight();
            var23_7 = this.getPaddingTop() + this.getPaddingBottom();
            var19_8 = ActionMenuView.getChildMeasureSpec((int)var2_2, (int)var23_7, (int)-2);
            var20_9 = var1_1 - (var6_5 + var7_6);
            var1_1 = var20_9 / this.mMinCellSize;
            var2_2 = this.mMinCellSize;
            if (var1_1 == 0) {
                this.setMeasuredDimension(var20_9, 0);
                return;
            }
            var21_10 = this.mMinCellSize + var20_9 % var2_2 / var1_1;
            var6_5 = 0;
            var9_11 = 0;
            var8_12 = 0;
            var10_13 = 0;
            var7_6 = 0;
            var25_14 = 0;
            var22_15 = this.getChildCount();
            for (var11_16 = 0; var11_16 < var22_15; ++var11_16) {
                var32_25 = this.getChildAt(var11_16);
                if (var32_25.getVisibility() == 8) {
                    var27_23 = var25_14;
                    var12_17 = var7_6;
                } else {
                    var31_24 = var32_25 instanceof ActionMenuItemView;
                    var13_18 = var10_13 + 1;
                    if (var31_24) {
                        var32_25.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
                    }
                    var33_26 = (LayoutParams)var32_25.getLayoutParams();
                    var33_26.expanded = false;
                    var33_26.extraPixels = 0;
                    var33_26.cellsUsed = 0;
                    var33_26.expandable = false;
                    var33_26.leftMargin = 0;
                    var33_26.rightMargin = 0;
                    var31_24 = var31_24 != false && ((ActionMenuItemView)var32_25).hasText() != false;
                    var33_26.preventEdgeOffset = var31_24;
                    var2_2 = var33_26.isOverflowButton != false ? 1 : var1_1;
                    var24_22 = ActionMenuView.measureChildForCells((View)var32_25, var21_10, var2_2, var19_8, var23_7);
                    var14_19 = Math.max(var9_11, var24_22);
                    var2_2 = var8_12;
                    if (var33_26.expandable) {
                        var2_2 = var8_12 + 1;
                    }
                    if (var33_26.isOverflowButton) {
                        var7_6 = 1;
                    }
                    var15_20 = var1_1 - var24_22;
                    var16_21 = Math.max(var6_5, var32_25.getMeasuredHeight());
                    var1_1 = var15_20;
                    var8_12 = var2_2;
                    var12_17 = var7_6;
                    var9_11 = var14_19;
                    var6_5 = var16_21;
                    var27_23 = var25_14;
                    var10_13 = var13_18;
                    if (var24_22 == 1) {
                        var27_23 = var25_14 | (long)(1 << var11_16);
                        var1_1 = var15_20;
                        var8_12 = var2_2;
                        var12_17 = var7_6;
                        var9_11 = var14_19;
                        var6_5 = var16_21;
                        var10_13 = var13_18;
                    }
                }
                var7_6 = var12_17;
                var25_14 = var27_23;
            }
            var11_16 = var7_6 != 0 && var10_13 == 2 ? 1 : 0;
            var2_2 = 0;
            var12_17 = var1_1;
            do {
                var27_23 = var25_14;
                if (var8_12 <= 0) ** GOTO lbl106
                var27_23 = var25_14;
                if (var12_17 <= 0) ** GOTO lbl106
                var13_18 = Integer.MAX_VALUE;
                var29_27 = 0;
                var16_21 = 0;
                for (var14_19 = 0; var14_19 < var22_15; ++var14_19) {
                    var32_25 = (LayoutParams)this.getChildAt(var14_19).getLayoutParams();
                    if (!var32_25.expandable) {
                        var27_23 = var29_27;
                        var1_1 = var16_21;
                        var15_20 = var13_18;
                    } else if (var32_25.cellsUsed < var13_18) {
                        var15_20 = var32_25.cellsUsed;
                        var27_23 = 1 << var14_19;
                        var1_1 = 1;
                    } else {
                        var15_20 = var13_18;
                        var1_1 = var16_21;
                        var27_23 = var29_27;
                        if (var32_25.cellsUsed == var13_18) {
                            var27_23 = var29_27 | (long)(1 << var14_19);
                            var1_1 = var16_21 + 1;
                            var15_20 = var13_18;
                        }
                    }
                    var13_18 = var15_20;
                    var16_21 = var1_1;
                    var29_27 = var27_23;
                }
                var25_14 |= var29_27;
                if (var16_21 <= var12_17) ** GOTO lbl158
                var27_23 = var25_14;
lbl106: // 3 sources:
                var1_1 = var7_6 == 0 && var10_13 == 1 ? 1 : 0;
                var7_6 = var2_2;
                if (var12_17 <= 0) ** GOTO lbl155
                var7_6 = var2_2;
                if (var27_23 == 0) ** GOTO lbl155
                if (var12_17 < var10_13 - 1 || var1_1 != 0) ** GOTO lbl-1000
                var7_6 = var2_2;
                if (var9_11 > 1) lbl-1000: // 2 sources:
                {
                    var4_29 = var5_28 = (float)Long.bitCount(var27_23);
                    if (var1_1 == 0) {
                        var3_30 = var5_28;
                        if ((1 & var27_23) != 0) {
                            var3_30 = var5_28;
                            if (!((LayoutParams)this.getChildAt((int)0).getLayoutParams()).preventEdgeOffset) {
                                var3_30 = var5_28 - 0.5f;
                            }
                        }
                        var4_29 = var3_30;
                        if (((long)(1 << var22_15 - 1) & var27_23) != 0) {
                            var4_29 = var3_30;
                            if (!((LayoutParams)this.getChildAt((int)(var22_15 - 1)).getLayoutParams()).preventEdgeOffset) {
                                var4_29 = var3_30 - 0.5f;
                            }
                        }
                    }
                    var7_6 = var4_29 > 0.0f ? (int)((float)(var12_17 * var21_10) / var4_29) : 0;
                    for (var8_12 = 0; var8_12 < var22_15; ++var8_12) {
                        if (((long)(1 << var8_12) & var27_23) == 0) {
                            var1_1 = var2_2;
                        } else {
                            var32_25 = this.getChildAt(var8_12);
                            var33_26 = (LayoutParams)var32_25.getLayoutParams();
                            if (var32_25 instanceof ActionMenuItemView) {
                                var33_26.extraPixels = var7_6;
                                var33_26.expanded = true;
                                if (var8_12 == 0 && !var33_26.preventEdgeOffset) {
                                    var33_26.leftMargin = (- var7_6) / 2;
                                }
                                var1_1 = 1;
                            } else if (var33_26.isOverflowButton) {
                                var33_26.extraPixels = var7_6;
                                var33_26.expanded = true;
                                var33_26.rightMargin = (- var7_6) / 2;
                                var1_1 = 1;
                            } else {
                                if (var8_12 != 0) {
                                    var33_26.leftMargin = var7_6 / 2;
                                }
                                var1_1 = var2_2;
                                if (var8_12 != var22_15 - 1) {
                                    var33_26.rightMargin = var7_6 / 2;
                                    var1_1 = var2_2;
                                }
                            }
                        }
                        var2_2 = var1_1;
                    }
                    var7_6 = var2_2;
                }
lbl155: // 5 sources:
                if (var7_6 != 0) {
                    break;
                }
                break block39;
lbl158: // 2 sources:
                for (var1_1 = 0; var1_1 < var22_15; ++var1_1) {
                    var32_25 = this.getChildAt(var1_1);
                    var33_26 = (LayoutParams)var32_25.getLayoutParams();
                    if (((long)(1 << var1_1) & var29_27) == 0) {
                        var2_2 = var12_17;
                        var27_23 = var25_14;
                        if (var33_26.cellsUsed == var13_18 + 1) {
                            var27_23 = var25_14 | (long)(1 << var1_1);
                            var2_2 = var12_17;
                        }
                    } else {
                        if (var11_16 != 0 && var33_26.preventEdgeOffset && var12_17 == 1) {
                            var32_25.setPadding(this.mGeneratedItemPadding + var21_10, 0, this.mGeneratedItemPadding, 0);
                        }
                        ++var33_26.cellsUsed;
                        var33_26.expanded = true;
                        var2_2 = var12_17 - 1;
                        var27_23 = var25_14;
                    }
                    var12_17 = var2_2;
                    var25_14 = var27_23;
                }
                var2_2 = 1;
            } while (true);
            for (var1_1 = 0; var1_1 < var22_15; ++var1_1) {
                var32_25 = this.getChildAt(var1_1);
                var33_26 = (LayoutParams)var32_25.getLayoutParams();
                if (!var33_26.expanded) continue;
                var32_25.measure(View.MeasureSpec.makeMeasureSpec((int)(var33_26.cellsUsed * var21_10 + var33_26.extraPixels), (int)1073741824), var19_8);
            }
        }
        var1_1 = var17_4;
        if (var18_3 != 1073741824) {
            var1_1 = var6_5;
        }
        this.setMeasuredDimension(var20_9, var1_1);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams != null && layoutParams instanceof LayoutParams) {
            return true;
        }
        return false;
    }

    public void dismissPopupMenus() {
        if (this.mPresenter != null) {
            this.mPresenter.dismissPopupMenus();
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

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams object) {
        if (object == null) {
            return this.generateDefaultLayoutParams();
        }
        object = object instanceof LayoutParams ? new LayoutParams((LayoutParams)((Object)object)) : new LayoutParams((ViewGroup.LayoutParams)object);
        if (object.gravity <= 0) {
            object.gravity = 16;
        }
        return object;
    }

    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams layoutParams = this.generateDefaultLayoutParams();
        layoutParams.isOverflowButton = true;
        return layoutParams;
    }

    /*
     * Enabled aggressive block sorting
     */
    public Menu getMenu() {
        if (this.mMenu == null) {
            Object object = this.getContext();
            this.mMenu = new MenuBuilder((Context)object);
            this.mMenu.setCallback(new MenuBuilderCallback());
            this.mPresenter = new ActionMenuPresenter((Context)object);
            this.mPresenter.setReserveOverflow(true);
            ActionMenuPresenter actionMenuPresenter = this.mPresenter;
            object = this.mActionMenuPresenterCallback != null ? this.mActionMenuPresenterCallback : new ActionMenuPresenterCallback();
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

    @Override
    public int getWindowAnimations() {
        return 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
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
        if (n <= 0) return bl;
        bl = bl2;
        if (!(view2 instanceof ActionMenuChildView)) return bl;
        return bl2 | ((ActionMenuChildView)view2).needsDividerBefore();
    }

    public boolean hideOverflowMenu() {
        if (this.mPresenter != null && this.mPresenter.hideOverflowMenu()) {
            return true;
        }
        return false;
    }

    @Override
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    @Override
    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    public boolean isOverflowMenuShowPending() {
        if (this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        if (this.mPresenter != null && this.mPresenter.isOverflowMenuShowing()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (Build.VERSION.SDK_INT >= 8) {
            super.onConfigurationChanged(configuration);
        }
        if (this.mPresenter != null) {
            this.mPresenter.updateMenuView(false);
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

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        LayoutParams layoutParams;
        View view;
        int n5;
        if (!this.mFormatItems) {
            super.onLayout(bl, n, n2, n3, n4);
            return;
        }
        int n6 = this.getChildCount();
        int n7 = (n4 - n2) / 2;
        int n8 = this.getDividerWidth();
        int n9 = 0;
        n4 = 0;
        n2 = n3 - n - this.getPaddingRight() - this.getPaddingLeft();
        int n10 = 0;
        bl = ViewUtils.isLayoutRtl((View)this);
        for (n5 = 0; n5 < n6; ++n5) {
            int n11;
            view = this.getChildAt(n5);
            if (view.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.isOverflowButton) {
                int n12;
                n10 = n11 = view.getMeasuredWidth();
                if (this.hasSupportDividerBeforeChildAt(n5)) {
                    n10 = n11 + n8;
                }
                int n13 = view.getMeasuredHeight();
                if (bl) {
                    n11 = this.getPaddingLeft() + layoutParams.leftMargin;
                    n12 = n11 + n10;
                } else {
                    n12 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                    n11 = n12 - n10;
                }
                int n14 = n7 - n13 / 2;
                view.layout(n11, n14, n12, n14 + n13);
                n2 -= n10;
                n10 = 1;
                continue;
            }
            n11 = view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            n11 = n2 - n11;
            n2 = n9 += n11;
            if (this.hasSupportDividerBeforeChildAt(n5)) {
                n2 = n9 + n8;
            }
            ++n4;
            n9 = n2;
            n2 = n11;
        }
        if (n6 == 1 && n10 == 0) {
            view = this.getChildAt(0);
            n2 = view.getMeasuredWidth();
            n4 = view.getMeasuredHeight();
            n = (n3 - n) / 2 - n2 / 2;
            n3 = n7 - n4 / 2;
            view.layout(n, n3, n + n2, n3 + n4);
            return;
        }
        n = n10 != 0 ? 0 : 1;
        n = (n = n4 - n) > 0 ? n2 / n : 0;
        n4 = Math.max(0, n);
        if (bl) {
            n2 = this.getWidth() - this.getPaddingRight();
            n = 0;
            while (n < n6) {
                view = this.getChildAt(n);
                layoutParams = (LayoutParams)view.getLayoutParams();
                n3 = n2;
                if (view.getVisibility() != 8) {
                    if (layoutParams.isOverflowButton) {
                        n3 = n2;
                    } else {
                        n3 = view.getMeasuredWidth();
                        n5 = view.getMeasuredHeight();
                        n9 = n7 - n5 / 2;
                        view.layout(n2 - n3, n9, n2 -= layoutParams.rightMargin, n9 + n5);
                        n3 = n2 - (layoutParams.leftMargin + n3 + n4);
                    }
                }
                ++n;
                n2 = n3;
            }
            return;
        }
        n2 = this.getPaddingLeft();
        n = 0;
        while (n < n6) {
            view = this.getChildAt(n);
            layoutParams = (LayoutParams)view.getLayoutParams();
            n3 = n2;
            if (view.getVisibility() != 8) {
                if (layoutParams.isOverflowButton) {
                    n3 = n2;
                } else {
                    n3 = view.getMeasuredWidth();
                    n5 = view.getMeasuredHeight();
                    n9 = n7 - n5 / 2;
                    view.layout(n2, n9, (n2 += layoutParams.leftMargin) + n3, n9 + n5);
                    n3 = n2 + (layoutParams.rightMargin + n3 + n4);
                }
            }
            ++n;
            n2 = n3;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void onMeasure(int n, int n2) {
        boolean bl = this.mFormatItems;
        boolean bl2 = View.MeasureSpec.getMode((int)n) == 1073741824;
        this.mFormatItems = bl2;
        if (bl != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int n3 = View.MeasureSpec.getSize((int)n);
        if (this.mFormatItems && this.mMenu != null && n3 != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = n3;
            this.mMenu.onItemsChanged(true);
        }
        int n4 = this.getChildCount();
        if (this.mFormatItems && n4 > 0) {
            this.onMeasureExactFormat(n, n2);
            return;
        }
        n3 = 0;
        do {
            if (n3 >= n4) {
                super.onMeasure(n, n2);
                return;
            }
            LayoutParams layoutParams = (LayoutParams)this.getChildAt(n3).getLayoutParams();
            layoutParams.rightMargin = 0;
            layoutParams.leftMargin = 0;
            ++n3;
        } while (true);
    }

    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    public void setExpandedActionViewsExclusive(boolean bl) {
        this.mPresenter.setExpandedActionViewsExclusive(bl);
    }

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

    public void setOverflowReserved(boolean bl) {
        this.mReserveOverflow = bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setPopupTheme(@StyleRes int n) {
        if (this.mPopupTheme == n) return;
        this.mPopupTheme = n;
        if (n == 0) {
            this.mPopupContext = this.getContext();
            return;
        }
        this.mPopupContext = new ContextThemeWrapper(this.getContext(), n);
    }

    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
        this.mPresenter.setMenuView(this);
    }

    public boolean showOverflowMenu() {
        if (this.mPresenter != null && this.mPresenter.showOverflowMenu()) {
            return true;
        }
        return false;
    }

    public static interface ActionMenuChildView {
        public boolean needsDividerAfter();

        public boolean needsDividerBefore();
    }

    private class ActionMenuPresenterCallback
    implements MenuPresenter.Callback {
        private ActionMenuPresenterCallback() {
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
        private MenuBuilderCallback() {
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

