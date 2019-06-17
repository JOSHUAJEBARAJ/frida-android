/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.SparseBooleanArray
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.internal.transition.ActionBarTransition;
import android.support.v7.internal.view.ActionBarPolicy;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.internal.view.menu.BaseMenuPresenter;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;

public class ActionMenuPresenter
extends BaseMenuPresenter
implements ActionProvider.SubUiVisibilityListener {
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
    private ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    private OverflowMenuButton mOverflowButton;
    private OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback;
    private OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;

    public ActionMenuPresenter(Context context) {
        super(context, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
        this.mPopupPresenterCallback = new PopupPresenterCallback();
    }

    static /* synthetic */ OverflowMenuButton access$600(ActionMenuPresenter actionMenuPresenter) {
        return actionMenuPresenter.mOverflowButton;
    }

    static /* synthetic */ MenuView access$700(ActionMenuPresenter actionMenuPresenter) {
        return actionMenuPresenter.mMenuView;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private View findViewForItem(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        if (viewGroup == null) {
            return null;
        }
        int n = viewGroup.getChildCount();
        int n2 = 0;
        while (n2 < n) {
            View view = viewGroup.getChildAt(n2);
            if (view instanceof MenuView.ItemView) {
                View view2 = view;
                if (((MenuView.ItemView)view).getItemData() == menuItem) return view2;
            }
            ++n2;
        }
        return null;
    }

    @Override
    public void bindItemView(MenuItemImpl object, MenuView.ItemView itemView) {
        itemView.initialize((MenuItemImpl)object, 0);
        object = (ActionMenuView)this.mMenuView;
        itemView = (ActionMenuItemView)itemView;
        itemView.setItemInvoker((MenuBuilder.ItemInvoker)object);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        itemView.setPopupCallback(this.mPopupCallback);
    }

    public boolean dismissPopupMenus() {
        return this.hideOverflowMenu() | this.hideSubMenus();
    }

    @Override
    public boolean filterLeftoverView(ViewGroup viewGroup, int n) {
        if (viewGroup.getChildAt(n) == this.mOverflowButton) {
            return false;
        }
        return super.filterLeftoverView(viewGroup, n);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public boolean flagActionItems() {
        var16_1 = this.mMenu.getVisibleItems();
        var10_2 = var16_1.size();
        var1_3 = this.mMaxItems;
        var9_4 = this.mActionItemWidthLimit;
        var11_5 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        var17_6 = (ViewGroup)this.mMenuView;
        var2_7 = 0;
        var4_8 = 0;
        var7_9 = 0;
        var5_10 = 0;
        for (var3_11 = 0; var3_11 < var10_2; ++var3_11) {
            var18_13 = var16_1.get(var3_11);
            if (var18_13.requiresActionButton()) {
                ++var2_7;
            } else if (var18_13.requestsActionButton()) {
                ++var4_8;
            } else {
                var5_10 = 1;
            }
            var6_12 = var1_3;
            if (this.mExpandedActionViewsExclusive) {
                var6_12 = var1_3;
                if (var18_13.isActionViewExpanded()) {
                    var6_12 = 0;
                }
            }
            var1_3 = var6_12;
        }
        var3_11 = var1_3;
        if (!this.mReserveOverflow) ** GOTO lbl33
        if (var5_10 != 0) ** GOTO lbl-1000
        var3_11 = var1_3;
        if (var2_7 + var4_8 > var1_3) lbl-1000: // 2 sources:
        {
            var3_11 = var1_3 - 1;
        }
lbl33: // 4 sources:
        var3_11 -= var2_7;
        var18_13 = this.mActionButtonGroups;
        var18_13.clear();
        var8_14 = 0;
        var2_7 = 0;
        if (this.mStrictWidthLimit) {
            var2_7 = var9_4 / this.mMinCellSize;
            var1_3 = this.mMinCellSize;
            var8_14 = this.mMinCellSize + var9_4 % var1_3 / var2_7;
        }
        var1_3 = 0;
        var5_10 = var9_4;
        var9_4 = var1_3;
        var1_3 = var7_9;
        while (var9_4 < var10_2) {
            var19_19 = var16_1.get(var9_4);
            if (var19_19.requiresActionButton()) {
                var20_20 = this.getItemView(var19_19, this.mScrapActionButtonView, var17_6);
                if (this.mScrapActionButtonView == null) {
                    this.mScrapActionButtonView = var20_20;
                }
                if (this.mStrictWidthLimit) {
                    var2_7 -= ActionMenuView.measureChildForCells((View)var20_20, var8_14, var2_7, var11_5, 0);
                } else {
                    var20_20.measure(var11_5, var11_5);
                }
                var6_12 = var20_20.getMeasuredWidth();
                var4_8 = var5_10 - var6_12;
                var5_10 = var1_3;
                if (var1_3 == 0) {
                    var5_10 = var6_12;
                }
                if ((var1_3 = var19_19.getGroupId()) != 0) {
                    var18_13.put(var1_3, true);
                }
                var19_19.setIsActionButton(true);
                var1_3 = var5_10;
            } else if (var19_19.requestsActionButton()) {
                var12_15 = var19_19.getGroupId();
                var15_18 = var18_13.get(var12_15);
                var13_16 = !(var3_11 <= 0 && var15_18 == false || var5_10 <= 0 || this.mStrictWidthLimit != false && var2_7 <= 0) ? 1 : 0;
                var7_9 = var2_7;
                var6_12 = var1_3;
                var14_17 = var13_16;
                var4_8 = var5_10;
                if (var13_16 != 0) {
                    var20_20 = this.getItemView(var19_19, this.mScrapActionButtonView, var17_6);
                    if (this.mScrapActionButtonView == null) {
                        this.mScrapActionButtonView = var20_20;
                    }
                    if (this.mStrictWidthLimit) {
                        var6_12 = ActionMenuView.measureChildForCells((View)var20_20, var8_14, var2_7, var11_5, 0);
                        var2_7 = var4_8 = var2_7 - var6_12;
                        if (var6_12 == 0) {
                            var13_16 = 0;
                            var2_7 = var4_8;
                        }
                    } else {
                        var20_20.measure(var11_5, var11_5);
                    }
                    var7_9 = var20_20.getMeasuredWidth();
                    var4_8 = var5_10 - var7_9;
                    var6_12 = var1_3;
                    if (var1_3 == 0) {
                        var6_12 = var7_9;
                    }
                    if (this.mStrictWidthLimit) {
                        var1_3 = var4_8 >= 0 ? 1 : 0;
                        var14_17 = var13_16 & var1_3;
                        var7_9 = var2_7;
                    } else {
                        var1_3 = var4_8 + var6_12 > 0 ? 1 : 0;
                        var14_17 = var13_16 & var1_3;
                        var7_9 = var2_7;
                    }
                }
                if (var14_17 != 0 && var12_15 != 0) {
                    var18_13.put(var12_15, true);
                    var1_3 = var3_11;
                } else {
                    var1_3 = var3_11;
                    if (var15_18) {
                        var18_13.put(var12_15, false);
                        var2_7 = 0;
                        do {
                            var1_3 = var3_11;
                            if (var2_7 >= var9_4) break;
                            var20_20 = var16_1.get(var2_7);
                            var1_3 = var3_11;
                            if (var20_20.getGroupId() == var12_15) {
                                var1_3 = var3_11;
                                if (var20_20.isActionButton()) {
                                    var1_3 = var3_11 + 1;
                                }
                                var20_20.setIsActionButton(false);
                            }
                            ++var2_7;
                            var3_11 = var1_3;
                        } while (true);
                    }
                }
                var3_11 = var1_3;
                if (var14_17 != 0) {
                    var3_11 = var1_3 - 1;
                }
                var19_19.setIsActionButton((boolean)var14_17);
                var2_7 = var7_9;
                var1_3 = var6_12;
            } else {
                var19_19.setIsActionButton(false);
                var4_8 = var5_10;
            }
            ++var9_4;
            var5_10 = var4_8;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public View getItemView(MenuItemImpl object, View view, ViewGroup viewGroup) {
        View view2 = object.getActionView();
        if (view2 == null || object.hasCollapsibleActionView()) {
            view2 = super.getItemView((MenuItemImpl)object, view, viewGroup);
        }
        int n = object.isActionViewExpanded() ? 8 : 0;
        view2.setVisibility(n);
        object = (ActionMenuView)viewGroup;
        view = view2.getLayoutParams();
        if (!object.checkLayoutParams((ViewGroup.LayoutParams)view)) {
            view2.setLayoutParams((ViewGroup.LayoutParams)object.generateLayoutParams((ViewGroup.LayoutParams)view));
        }
        return view2;
    }

    @Override
    public MenuView getMenuView(ViewGroup object) {
        object = super.getMenuView((ViewGroup)object);
        ((ActionMenuView)object).setPresenter(this);
        return object;
    }

    public Drawable getOverflowIcon() {
        if (this.mOverflowButton != null) {
            return this.mOverflowButton.getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }

    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
            ((View)this.mMenuView).removeCallbacks((Runnable)this.mPostedOpenRunnable);
            this.mPostedOpenRunnable = null;
            return true;
        }
        OverflowPopup overflowPopup = this.mOverflowPopup;
        if (overflowPopup != null) {
            overflowPopup.dismiss();
            return true;
        }
        return false;
    }

    public boolean hideSubMenus() {
        if (this.mActionButtonPopup != null) {
            this.mActionButtonPopup.dismiss();
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void initForMenu(Context object, MenuBuilder menuBuilder) {
        super.initForMenu((Context)object, menuBuilder);
        menuBuilder = object.getResources();
        object = ActionBarPolicy.get((Context)object);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = object.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = object.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = object.getMaxActionButtons();
        }
        int n = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                int n2 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                this.mOverflowButton.measure(n2, n2);
            }
            n -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = n;
        this.mMinCellSize = (int)(56.0f * menuBuilder.getDisplayMetrics().density);
        this.mScrapActionButtonView = null;
    }

    public boolean isOverflowMenuShowPending() {
        if (this.mPostedOpenRunnable != null || this.isOverflowMenuShowing()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        if (this.mOverflowPopup != null && this.mOverflowPopup.isShowing()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        this.dismissPopupMenus();
        super.onCloseMenu(menuBuilder, bl);
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = this.mContext.getResources().getInteger(R.integer.abc_max_action_buttons);
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        parcelable = (SavedState)parcelable;
        if (parcelable.openSubMenuId > 0 && (parcelable = this.mMenu.findItem(parcelable.openSubMenuId)) != null) {
            this.onSubMenuSelected((SubMenuBuilder)parcelable.getSubMenu());
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.openSubMenuId = this.mOpenSubMenuId;
        return savedState;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        Object object = subMenuBuilder;
        while (object.getParentMenu() != this.mMenu) {
            object = (SubMenuBuilder)object.getParentMenu();
        }
        View view = this.findViewForItem(object.getItem());
        object = view;
        if (view == null) {
            if (this.mOverflowButton == null) return false;
            object = this.mOverflowButton;
        }
        this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
        this.mActionButtonPopup = new ActionButtonSubmenu(this, this.mContext, subMenuBuilder);
        this.mActionButtonPopup.setAnchorView((View)object);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(subMenuBuilder);
        return true;
    }

    @Override
    public void onSubUiVisibilityChanged(boolean bl) {
        if (bl) {
            super.onSubMenuSelected(null);
            return;
        }
        this.mMenu.close(false);
    }

    public void setExpandedActionViewsExclusive(boolean bl) {
        this.mExpandedActionViewsExclusive = bl;
    }

    public void setItemLimit(int n) {
        this.mMaxItems = n;
        this.mMaxItemsSet = true;
    }

    public void setMenuView(ActionMenuView actionMenuView) {
        this.mMenuView = actionMenuView;
        actionMenuView.initialize(this.mMenu);
    }

    public void setOverflowIcon(Drawable drawable2) {
        if (this.mOverflowButton != null) {
            this.mOverflowButton.setImageDrawable(drawable2);
            return;
        }
        this.mPendingOverflowIconSet = true;
        this.mPendingOverflowIcon = drawable2;
    }

    public void setReserveOverflow(boolean bl) {
        this.mReserveOverflow = bl;
        this.mReserveOverflowSet = true;
    }

    public void setWidthLimit(int n, boolean bl) {
        this.mWidthLimit = n;
        this.mStrictWidthLimit = bl;
        this.mWidthLimitSet = true;
    }

    @Override
    public boolean shouldIncludeItem(int n, MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }

    public boolean showOverflowMenu() {
        if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
            this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
            ((View)this.mMenuView).post((Runnable)this.mPostedOpenRunnable);
            super.onSubMenuSelected(null);
            return true;
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void updateMenuView(boolean var1_1) {
        var4_2 = (ViewGroup)((View)this.mMenuView).getParent();
        if (var4_2 != null) {
            ActionBarTransition.beginDelayedTransition(var4_2);
        }
        super.updateMenuView(var1_1);
        ((View)this.mMenuView).requestLayout();
        if (this.mMenu != null) {
            var4_2 = this.mMenu.getActionItems();
            var3_3 = var4_2.size();
            for (var2_4 = 0; var2_4 < var3_3; ++var2_4) {
                var5_5 = var4_2.get(var2_4).getSupportActionProvider();
                if (var5_5 == null) continue;
                var5_5.setSubUiVisibilityListener(this);
            }
        }
        var4_2 = this.mMenu != null ? this.mMenu.getNonActionItems() : null;
        var2_4 = var3_3 = 0;
        if (!this.mReserveOverflow) ** GOTO lbl-1000
        var2_4 = var3_3;
        if (var4_2 != null) {
            var2_4 = var4_2.size();
            var2_4 = var2_4 == 1 ? (!((MenuItemImpl)var4_2.get(0)).isActionViewExpanded() ? 1 : 0) : (var2_4 > 0 ? 1 : 0);
        }
        if (var2_4 != 0) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
            }
            if ((var4_2 = (ViewGroup)this.mOverflowButton.getParent()) != this.mMenuView) {
                if (var4_2 != null) {
                    var4_2.removeView((View)this.mOverflowButton);
                }
                var4_2 = (ActionMenuView)this.mMenuView;
                var4_2.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)var4_2.generateOverflowButtonLayoutParams());
            }
        } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
            ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton);
        }
        ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }

    /*
     * Exception performing whole class analysis.
     */
    private class ActionButtonSubmenu
    extends MenuPopupHelper {
        private SubMenuBuilder mSubMenu;
        final /* synthetic */ ActionMenuPresenter this$0;

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public ActionButtonSubmenu(ActionMenuPresenter var1_1, Context var2_2, SubMenuBuilder var3_3) {
            this.this$0 = var1_1;
            super((Context)var2_2, var3_3, null, false, R.attr.actionOverflowMenuStyle);
            this.mSubMenu = var3_3;
            if (!((MenuItemImpl)var3_3.getItem()).isActionButton()) {
                var2_2 = ActionMenuPresenter.access$600(var1_1) == null ? (View)ActionMenuPresenter.access$700(var1_1) : ActionMenuPresenter.access$600(var1_1);
                this.setAnchorView((View)var2_2);
            }
            this.setCallback(var1_1.mPopupPresenterCallback);
            var7_4 = false;
            var5_5 = var3_3.size();
            var4_6 = 0;
            do {
                var6_7 = var7_4;
                if (var4_6 >= var5_5) ** GOTO lbl17
                var1_1 = var3_3.getItem(var4_6);
                if (var1_1.isVisible() && var1_1.getIcon() != null) {
                    var6_7 = true;
lbl17: // 2 sources:
                    this.setForceShowIcon(var6_7);
                    return;
                }
                ++var4_6;
            } while (true);
        }

        @Override
        public void onDismiss() {
            super.onDismiss();
            this.this$0.mActionButtonPopup = null;
            this.this$0.mOpenSubMenuId = 0;
        }
    }

    private class ActionMenuPopupCallback
    extends ActionMenuItemView.PopupCallback {
        private ActionMenuPopupCallback() {
        }

        @Override
        public ListPopupWindow getPopup() {
            if (ActionMenuPresenter.this.mActionButtonPopup != null) {
                return ActionMenuPresenter.this.mActionButtonPopup.getPopup();
            }
            return null;
        }
    }

    private class OpenOverflowRunnable
    implements Runnable {
        private OverflowPopup mPopup;

        public OpenOverflowRunnable(OverflowPopup overflowPopup) {
            this.mPopup = overflowPopup;
        }

        @Override
        public void run() {
            ActionMenuPresenter.this.mMenu.changeMenuMode();
            View view = (View)ActionMenuPresenter.this.mMenuView;
            if (view != null && view.getWindowToken() != null && this.mPopup.tryShow()) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    private class OverflowMenuButton
    extends AppCompatImageView
    implements ActionMenuView.ActionMenuChildView {
        private final float[] mTempPts;

        public OverflowMenuButton(Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            this.mTempPts = new float[2];
            this.setClickable(true);
            this.setFocusable(true);
            this.setVisibility(0);
            this.setEnabled(true);
            this.setOnTouchListener((View.OnTouchListener)new ListPopupWindow.ForwardingListener((View)this, ActionMenuPresenter.this){
                final /* synthetic */ ActionMenuPresenter val$this$0;

                @Override
                public ListPopupWindow getPopup() {
                    if (ActionMenuPresenter.this.mOverflowPopup == null) {
                        return null;
                    }
                    return ActionMenuPresenter.this.mOverflowPopup.getPopup();
                }

                @Override
                public boolean onForwardingStarted() {
                    ActionMenuPresenter.this.showOverflowMenu();
                    return true;
                }

                @Override
                public boolean onForwardingStopped() {
                    if (ActionMenuPresenter.this.mPostedOpenRunnable != null) {
                        return false;
                    }
                    ActionMenuPresenter.this.hideOverflowMenu();
                    return true;
                }
            });
        }

        @Override
        public boolean needsDividerAfter() {
            return false;
        }

        @Override
        public boolean needsDividerBefore() {
            return false;
        }

        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            this.playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
        }

        protected boolean setFrame(int n, int n2, int n3, int n4) {
            boolean bl = super.setFrame(n, n2, n3, n4);
            Drawable drawable2 = this.getDrawable();
            Drawable drawable3 = this.getBackground();
            if (drawable2 != null && drawable3 != null) {
                int n5 = this.getWidth();
                n2 = this.getHeight();
                n = Math.max(n5, n2) / 2;
                int n6 = this.getPaddingLeft();
                int n7 = this.getPaddingRight();
                n3 = this.getPaddingTop();
                n4 = this.getPaddingBottom();
                n5 = (n5 + (n6 - n7)) / 2;
                n2 = (n2 + (n3 - n4)) / 2;
                DrawableCompat.setHotspotBounds(drawable3, n5 - n, n2 - n, n5 + n, n2 + n);
            }
            return bl;
        }

    }

    private class OverflowPopup
    extends MenuPopupHelper {
        public OverflowPopup(Context context, MenuBuilder menuBuilder, View view, boolean bl) {
            super(context, menuBuilder, view, bl, R.attr.actionOverflowMenuStyle);
            this.setGravity(8388613);
            this.setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        @Override
        public void onDismiss() {
            super.onDismiss();
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.close();
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
        }
    }

    private class PopupPresenterCallback
    implements MenuPresenter.Callback {
        private PopupPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
            MenuPresenter.Callback callback;
            if (menuBuilder instanceof SubMenuBuilder) {
                ((SubMenuBuilder)menuBuilder).getRootMenu().close(false);
            }
            if ((callback = ActionMenuPresenter.this.getCallback()) != null) {
                callback.onCloseMenu(menuBuilder, bl);
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (menuBuilder == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)menuBuilder).getItem().getItemId();
            MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
            if (callback == null) return false;
            return callback.onOpenSubMenu(menuBuilder);
        }
    }

    private static class SavedState
    implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(Parcel parcel) {
            this.openSubMenuId = parcel.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeInt(this.openSubMenuId);
        }

    }

}

