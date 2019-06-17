/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.R;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.internal.NavigationMenuView;
import android.support.design.internal.ParcelableSparseArray;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;

public class NavigationMenuPresenter
implements MenuPresenter {
    private static final String STATE_ADAPTER = "android:menu:adapter";
    private static final String STATE_HIERARCHY = "android:menu:list";
    private NavigationMenuAdapter mAdapter;
    private MenuPresenter.Callback mCallback;
    private LinearLayout mHeaderLayout;
    private ColorStateList mIconTintList;
    private int mId;
    private Drawable mItemBackground;
    private LayoutInflater mLayoutInflater;
    private MenuBuilder mMenu;
    private NavigationMenuView mMenuView;
    private final View.OnClickListener mOnClickListener;
    private int mPaddingSeparator;
    private int mPaddingTopDefault;
    private int mTextAppearance;
    private boolean mTextAppearanceSet;
    private ColorStateList mTextColor;

    public NavigationMenuPresenter() {
        this.mOnClickListener = new View.OnClickListener(){

            public void onClick(View object) {
                object = (NavigationMenuItemView)object;
                NavigationMenuPresenter.this.setUpdateSuspended(true);
                object = object.getItemData();
                boolean bl = NavigationMenuPresenter.this.mMenu.performItemAction((MenuItem)object, NavigationMenuPresenter.this, 0);
                if (object != null && object.isCheckable() && bl) {
                    NavigationMenuPresenter.this.mAdapter.setCheckedItem((MenuItemImpl)object);
                }
                NavigationMenuPresenter.this.setUpdateSuspended(false);
                NavigationMenuPresenter.this.updateMenuView(false);
            }
        };
    }

    public void addHeaderView(@NonNull View view) {
        this.mHeaderLayout.addView(view);
        this.mMenuView.setPadding(0, 0, 0, this.mMenuView.getPaddingBottom());
    }

    @Override
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    @Override
    public int getId() {
        return this.mId;
    }

    public Drawable getItemBackground() {
        return this.mItemBackground;
    }

    @Nullable
    public ColorStateList getItemTextColor() {
        return this.mTextColor;
    }

    @Nullable
    public ColorStateList getItemTintList() {
        return this.mIconTintList;
    }

    @Override
    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            this.mMenuView = (NavigationMenuView)this.mLayoutInflater.inflate(R.layout.design_navigation_menu, viewGroup, false);
            if (this.mAdapter == null) {
                this.mAdapter = new NavigationMenuAdapter();
            }
            this.mHeaderLayout = (LinearLayout)this.mLayoutInflater.inflate(R.layout.design_navigation_item_header, (ViewGroup)this.mMenuView, false);
            this.mMenuView.setAdapter(this.mAdapter);
        }
        return this.mMenuView;
    }

    public View inflateHeaderView(@LayoutRes int n) {
        View view = this.mLayoutInflater.inflate(n, (ViewGroup)this.mHeaderLayout, false);
        this.addHeaderView(view);
        return view;
    }

    @Override
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.mLayoutInflater = LayoutInflater.from((Context)context);
        this.mMenu = menuBuilder;
        context = context.getResources();
        this.mPaddingTopDefault = context.getDimensionPixelOffset(R.dimen.design_navigation_padding_top_default);
        this.mPaddingSeparator = context.getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        if (this.mCallback != null) {
            this.mCallback.onCloseMenu(menuBuilder, bl);
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        SparseArray sparseArray = (parcelable = (Bundle)parcelable).getSparseParcelableArray("android:menu:list");
        if (sparseArray != null) {
            this.mMenuView.restoreHierarchyState(sparseArray);
        }
        if ((parcelable = parcelable.getBundle("android:menu:adapter")) != null) {
            this.mAdapter.restoreInstanceState((Bundle)parcelable);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (this.mMenuView != null) {
            SparseArray sparseArray = new SparseArray();
            this.mMenuView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        if (this.mAdapter != null) {
            bundle.putBundle("android:menu:adapter", this.mAdapter.createInstanceState());
        }
        return bundle;
    }

    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        return false;
    }

    public void removeHeaderView(@NonNull View view) {
        this.mHeaderLayout.removeView(view);
        if (this.mHeaderLayout.getChildCount() == 0) {
            this.mMenuView.setPadding(0, this.mPaddingTopDefault, 0, this.mMenuView.getPaddingBottom());
        }
    }

    @Override
    public void setCallback(MenuPresenter.Callback callback) {
        this.mCallback = callback;
    }

    public void setCheckedItem(MenuItemImpl menuItemImpl) {
        this.mAdapter.setCheckedItem(menuItemImpl);
    }

    public void setId(int n) {
        this.mId = n;
    }

    public void setItemBackground(Drawable drawable2) {
        this.mItemBackground = drawable2;
    }

    public void setItemIconTintList(@Nullable ColorStateList colorStateList) {
        this.mIconTintList = colorStateList;
        this.updateMenuView(false);
    }

    public void setItemTextAppearance(@StyleRes int n) {
        this.mTextAppearance = n;
        this.mTextAppearanceSet = true;
        this.updateMenuView(false);
    }

    public void setItemTextColor(@Nullable ColorStateList colorStateList) {
        this.mTextColor = colorStateList;
        this.updateMenuView(false);
    }

    public void setUpdateSuspended(boolean bl) {
        if (this.mAdapter != null) {
            this.mAdapter.setUpdateSuspended(bl);
        }
    }

    @Override
    public void updateMenuView(boolean bl) {
        if (this.mAdapter != null) {
            this.mAdapter.update();
        }
    }

    private static class HeaderViewHolder
    extends ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    private class NavigationMenuAdapter
    extends RecyclerView.Adapter<ViewHolder> {
        private static final String STATE_ACTION_VIEWS = "android:menu:action_views";
        private static final String STATE_CHECKED_ITEM = "android:menu:checked";
        private static final int VIEW_TYPE_HEADER = 3;
        private static final int VIEW_TYPE_NORMAL = 0;
        private static final int VIEW_TYPE_SEPARATOR = 2;
        private static final int VIEW_TYPE_SUBHEADER = 1;
        private MenuItemImpl mCheckedItem;
        private final ArrayList<NavigationMenuItem> mItems;
        private ColorDrawable mTransparentIcon;
        private boolean mUpdateSuspended;

        NavigationMenuAdapter() {
            this.mItems = new ArrayList();
            this.prepareMenuItems();
        }

        private void appendTransparentIconIfMissing(int n, int n2) {
            while (n < n2) {
                MenuItemImpl menuItemImpl = ((NavigationMenuTextItem)this.mItems.get(n)).getMenuItem();
                if (menuItemImpl.getIcon() == null) {
                    if (this.mTransparentIcon == null) {
                        this.mTransparentIcon = new ColorDrawable(17170445);
                    }
                    menuItemImpl.setIcon((Drawable)this.mTransparentIcon);
                }
                ++n;
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private void prepareMenuItems() {
            if (this.mUpdateSuspended) {
                return;
            }
            this.mUpdateSuspended = true;
            this.mItems.clear();
            this.mItems.add(new NavigationMenuHeaderItem());
            int n = -1;
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            int n5 = NavigationMenuPresenter.this.mMenu.getVisibleItems().size();
            do {
                int n6;
                int n7;
                int n8;
                int n9;
                if (n4 >= n5) {
                    this.mUpdateSuspended = false;
                    return;
                }
                MenuItemImpl menuItemImpl = NavigationMenuPresenter.this.mMenu.getVisibleItems().get(n4);
                if (menuItemImpl.isChecked()) {
                    this.setCheckedItem(menuItemImpl);
                }
                if (menuItemImpl.isCheckable()) {
                    menuItemImpl.setExclusiveCheckable(false);
                }
                if (menuItemImpl.hasSubMenu()) {
                    SubMenu subMenu = menuItemImpl.getSubMenu();
                    n9 = n3;
                    n7 = n;
                    n8 = n2;
                    if (subMenu.hasVisibleItems()) {
                        if (n4 != 0) {
                            this.mItems.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, 0));
                        }
                        this.mItems.add(new NavigationMenuTextItem(menuItemImpl));
                        n6 = 0;
                        int n10 = this.mItems.size();
                        n8 = subMenu.size();
                        for (n7 = 0; n7 < n8; ++n7) {
                            MenuItemImpl menuItemImpl2 = (MenuItemImpl)subMenu.getItem(n7);
                            n9 = n6;
                            if (menuItemImpl2.isVisible()) {
                                n9 = n6;
                                if (n6 == 0) {
                                    n9 = n6;
                                    if (menuItemImpl2.getIcon() != null) {
                                        n9 = 1;
                                    }
                                }
                                if (menuItemImpl2.isCheckable()) {
                                    menuItemImpl2.setExclusiveCheckable(false);
                                }
                                if (menuItemImpl.isChecked()) {
                                    this.setCheckedItem(menuItemImpl);
                                }
                                this.mItems.add(new NavigationMenuTextItem(menuItemImpl2));
                            }
                            n6 = n9;
                        }
                        n9 = n3;
                        n7 = n;
                        n8 = n2;
                        if (n6 != 0) {
                            this.appendTransparentIconIfMissing(n10, this.mItems.size());
                            n8 = n2;
                            n7 = n;
                            n9 = n3;
                        }
                    }
                } else {
                    n7 = menuItemImpl.getGroupId();
                    if (n7 != n) {
                        n3 = this.mItems.size();
                        n2 = menuItemImpl.getIcon() != null ? 1 : 0;
                        n9 = n2;
                        n6 = n3;
                        if (n4 != 0) {
                            n6 = n3 + 1;
                            this.mItems.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, NavigationMenuPresenter.this.mPaddingSeparator));
                            n9 = n2;
                        }
                    } else {
                        n9 = n3;
                        n6 = n2;
                        if (n3 == 0) {
                            n9 = n3;
                            n6 = n2;
                            if (menuItemImpl.getIcon() != null) {
                                n9 = 1;
                                this.appendTransparentIconIfMissing(n2, this.mItems.size());
                                n6 = n2;
                            }
                        }
                    }
                    if (n9 != 0 && menuItemImpl.getIcon() == null) {
                        menuItemImpl.setIcon(17170445);
                    }
                    this.mItems.add(new NavigationMenuTextItem(menuItemImpl));
                    n8 = n6;
                }
                ++n4;
                n3 = n9;
                n = n7;
                n2 = n8;
            } while (true);
        }

        /*
         * Enabled aggressive block sorting
         */
        public Bundle createInstanceState() {
            Bundle bundle = new Bundle();
            if (this.mCheckedItem != null) {
                bundle.putInt("android:menu:checked", this.mCheckedItem.getItemId());
            }
            SparseArray sparseArray = new SparseArray();
            Iterator<NavigationMenuItem> iterator = this.mItems.iterator();
            do {
                if (!iterator.hasNext()) {
                    bundle.putSparseParcelableArray("android:menu:action_views", sparseArray);
                    return bundle;
                }
                NavigationMenuItem navigationMenuItem = iterator.next();
                if (!(navigationMenuItem instanceof NavigationMenuTextItem)) continue;
                MenuItemImpl menuItemImpl = ((NavigationMenuTextItem)navigationMenuItem).getMenuItem();
                navigationMenuItem = menuItemImpl != null ? menuItemImpl.getActionView() : null;
                if (navigationMenuItem == null) continue;
                ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
                navigationMenuItem.saveHierarchyState((SparseArray)parcelableSparseArray);
                sparseArray.put(menuItemImpl.getItemId(), (Object)parcelableSparseArray);
            } while (true);
        }

        @Override
        public int getItemCount() {
            return this.mItems.size();
        }

        @Override
        public long getItemId(int n) {
            return n;
        }

        @Override
        public int getItemViewType(int n) {
            NavigationMenuItem navigationMenuItem = this.mItems.get(n);
            if (navigationMenuItem instanceof NavigationMenuSeparatorItem) {
                return 2;
            }
            if (navigationMenuItem instanceof NavigationMenuHeaderItem) {
                return 3;
            }
            if (navigationMenuItem instanceof NavigationMenuTextItem) {
                if (((NavigationMenuTextItem)navigationMenuItem).getMenuItem().hasSubMenu()) {
                    return 1;
                }
                return 0;
            }
            throw new RuntimeException("Unknown item type.");
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int n) {
            switch (this.getItemViewType(n)) {
                default: {
                    return;
                }
                case 0: {
                    NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)viewHolder.itemView;
                    navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.mIconTintList);
                    if (NavigationMenuPresenter.this.mTextAppearanceSet) {
                        navigationMenuItemView.setTextAppearance(navigationMenuItemView.getContext(), NavigationMenuPresenter.this.mTextAppearance);
                    }
                    if (NavigationMenuPresenter.this.mTextColor != null) {
                        navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.mTextColor);
                    }
                    viewHolder = NavigationMenuPresenter.this.mItemBackground != null ? NavigationMenuPresenter.this.mItemBackground.getConstantState().newDrawable() : null;
                    navigationMenuItemView.setBackgroundDrawable((Drawable)viewHolder);
                    navigationMenuItemView.initialize(((NavigationMenuTextItem)this.mItems.get(n)).getMenuItem(), 0);
                    return;
                }
                case 1: {
                    ((TextView)viewHolder.itemView).setText(((NavigationMenuTextItem)this.mItems.get(n)).getMenuItem().getTitle());
                    return;
                }
                case 2: 
            }
            NavigationMenuSeparatorItem navigationMenuSeparatorItem = (NavigationMenuSeparatorItem)this.mItems.get(n);
            viewHolder.itemView.setPadding(0, navigationMenuSeparatorItem.getPaddingTop(), 0, navigationMenuSeparatorItem.getPaddingBottom());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
            switch (n) {
                default: {
                    return null;
                }
                case 0: {
                    return new NormalViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup, NavigationMenuPresenter.this.mOnClickListener);
                }
                case 1: {
                    return new SubheaderViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup);
                }
                case 2: {
                    return new SeparatorViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup);
                }
                case 3: 
            }
            return new HeaderViewHolder((View)NavigationMenuPresenter.this.mHeaderLayout);
        }

        @Override
        public void onViewRecycled(ViewHolder viewHolder) {
            if (viewHolder instanceof NormalViewHolder) {
                ((NavigationMenuItemView)viewHolder.itemView).recycle();
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        public void restoreInstanceState(Bundle object) {
            int n = object.getInt("android:menu:checked", 0);
            if (n != 0) {
                this.mUpdateSuspended = true;
                for (NavigationMenuItem navigationMenuItem : this.mItems) {
                    MenuItemImpl menuItemImpl;
                    if (!(navigationMenuItem instanceof NavigationMenuTextItem) || (menuItemImpl = ((NavigationMenuTextItem)navigationMenuItem).getMenuItem()) == null || menuItemImpl.getItemId() != n) continue;
                    this.setCheckedItem(menuItemImpl);
                    break;
                }
                this.mUpdateSuspended = false;
                this.prepareMenuItems();
            }
            SparseArray sparseArray = object.getSparseParcelableArray("android:menu:action_views");
            Iterator<NavigationMenuItem> iterator = this.mItems.iterator();
            while (iterator.hasNext()) {
                object = iterator.next();
                if (!(object instanceof NavigationMenuTextItem)) continue;
                MenuItemImpl menuItemImpl = ((NavigationMenuTextItem)object).getMenuItem();
                object = menuItemImpl != null ? menuItemImpl.getActionView() : null;
                if (object == null) continue;
                object.restoreHierarchyState((SparseArray)sparseArray.get(menuItemImpl.getItemId()));
            }
        }

        public void setCheckedItem(MenuItemImpl menuItemImpl) {
            if (this.mCheckedItem == menuItemImpl || !menuItemImpl.isCheckable()) {
                return;
            }
            if (this.mCheckedItem != null) {
                this.mCheckedItem.setChecked(false);
            }
            this.mCheckedItem = menuItemImpl;
            menuItemImpl.setChecked(true);
        }

        public void setUpdateSuspended(boolean bl) {
            this.mUpdateSuspended = bl;
        }

        public void update() {
            this.prepareMenuItems();
            this.notifyDataSetChanged();
        }
    }

    private static class NavigationMenuHeaderItem
    implements NavigationMenuItem {
        private NavigationMenuHeaderItem() {
        }
    }

    private static interface NavigationMenuItem {
    }

    private static class NavigationMenuSeparatorItem
    implements NavigationMenuItem {
        private final int mPaddingBottom;
        private final int mPaddingTop;

        public NavigationMenuSeparatorItem(int n, int n2) {
            this.mPaddingTop = n;
            this.mPaddingBottom = n2;
        }

        public int getPaddingBottom() {
            return this.mPaddingBottom;
        }

        public int getPaddingTop() {
            return this.mPaddingTop;
        }
    }

    private static class NavigationMenuTextItem
    implements NavigationMenuItem {
        private final MenuItemImpl mMenuItem;

        private NavigationMenuTextItem(MenuItemImpl menuItemImpl) {
            this.mMenuItem = menuItemImpl;
        }

        public MenuItemImpl getMenuItem() {
            return this.mMenuItem;
        }
    }

    private static class NormalViewHolder
    extends ViewHolder {
        public NormalViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, View.OnClickListener onClickListener) {
            super(layoutInflater.inflate(R.layout.design_navigation_item, viewGroup, false));
            this.itemView.setOnClickListener(onClickListener);
        }
    }

    private static class SeparatorViewHolder
    extends ViewHolder {
        public SeparatorViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_separator, viewGroup, false));
        }
    }

    private static class SubheaderViewHolder
    extends ViewHolder {
        public SubheaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_subheader, viewGroup, false));
        }
    }

    private static abstract class ViewHolder
    extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

}

