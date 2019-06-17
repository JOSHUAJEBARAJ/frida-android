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
 *  android.graphics.Paint
 *  android.graphics.drawable.Drawable
 *  android.os.IBinder
 *  android.text.Layout
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.View$OnLongClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.animation.Interpolator
 *  android.widget.HorizontalScrollView
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 *  android.widget.Toast
 */
package android.support.design.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.ThemeUtils;
import android.support.design.widget.ValueAnimatorCompat;
import android.support.design.widget.ViewUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.internal.widget.TintManager;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class TabLayout
extends HorizontalScrollView {
    private static final int ANIMATION_DURATION = 300;
    private static final int DEFAULT_GAP_TEXT_ICON = 8;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
    private static final int FIXED_WRAP_GUTTER_MIN = 16;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_FILL = 0;
    private static final int INVALID_WIDTH = -1;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    private static final int MOTION_NON_ADJACENT_OFFSET = 24;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    private int mContentInsetStart;
    private ValueAnimatorCompat mIndicatorAnimator;
    private int mMode;
    private OnTabSelectedListener mOnTabSelectedListener;
    private final int mRequestedTabMaxWidth;
    private final int mRequestedTabMinWidth;
    private ValueAnimatorCompat mScrollAnimator;
    private final int mScrollableTabMinWidth;
    private Tab mSelectedTab;
    private final int mTabBackgroundResId;
    private View.OnClickListener mTabClickListener;
    private int mTabGravity;
    private int mTabMaxWidth;
    private int mTabPaddingBottom;
    private int mTabPaddingEnd;
    private int mTabPaddingStart;
    private int mTabPaddingTop;
    private final SlidingTabStrip mTabStrip;
    private int mTabTextAppearance;
    private ColorStateList mTabTextColors;
    private float mTabTextMultiLineSize;
    private float mTabTextSize;
    private final ArrayList<Tab> mTabs;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TabLayout(Context context, AttributeSet attributeSet, int n) {
        block4 : {
            super(context, attributeSet, n);
            this.mTabs = new ArrayList();
            this.mTabMaxWidth = Integer.MAX_VALUE;
            ThemeUtils.checkAppCompatTheme(context);
            this.setHorizontalScrollBarEnabled(false);
            this.mTabStrip = new SlidingTabStrip(context);
            this.addView((View)this.mTabStrip, -2, -1);
            attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.TabLayout, n, R.style.Widget_Design_TabLayout);
            this.mTabStrip.setSelectedIndicatorHeight(attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, 0));
            this.mTabStrip.setSelectedIndicatorColor(attributeSet.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
            this.mTabPaddingBottom = n = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
            this.mTabPaddingEnd = n;
            this.mTabPaddingTop = n;
            this.mTabPaddingStart = n;
            this.mTabPaddingStart = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.mTabPaddingStart);
            this.mTabPaddingTop = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.mTabPaddingTop);
            this.mTabPaddingEnd = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.mTabPaddingEnd);
            this.mTabPaddingBottom = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.mTabPaddingBottom);
            this.mTabTextAppearance = attributeSet.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
            context = context.obtainStyledAttributes(this.mTabTextAppearance, R.styleable.TextAppearance);
            this.mTabTextSize = context.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
            this.mTabTextColors = context.getColorStateList(R.styleable.TextAppearance_android_textColor);
            if (!attributeSet.hasValue(R.styleable.TabLayout_tabTextColor)) break block4;
            this.mTabTextColors = attributeSet.getColorStateList(R.styleable.TabLayout_tabTextColor);
        }
        if (attributeSet.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
            n = attributeSet.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
            this.mTabTextColors = TabLayout.createColorStateList(this.mTabTextColors.getDefaultColor(), n);
        }
        this.mRequestedTabMinWidth = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
        this.mRequestedTabMaxWidth = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
        this.mTabBackgroundResId = attributeSet.getResourceId(R.styleable.TabLayout_tabBackground, 0);
        this.mContentInsetStart = attributeSet.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
        this.mMode = attributeSet.getInt(R.styleable.TabLayout_tabMode, 1);
        this.mTabGravity = attributeSet.getInt(R.styleable.TabLayout_tabGravity, 0);
        attributeSet.recycle();
        context = this.getResources();
        this.mTabTextMultiLineSize = context.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
        this.mScrollableTabMinWidth = context.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
        this.applyModeAndGravity();
        return;
        finally {
            context.recycle();
        }
    }

    private void addTabView(Tab object, int n, boolean bl) {
        object = this.createTabView((Tab)object);
        this.mTabStrip.addView((View)object, n, (ViewGroup.LayoutParams)this.createLayoutParamsForTabs());
        if (bl) {
            object.setSelected(true);
        }
    }

    private void addTabView(Tab object, boolean bl) {
        object = this.createTabView((Tab)object);
        this.mTabStrip.addView((View)object, (ViewGroup.LayoutParams)this.createLayoutParamsForTabs());
        if (bl) {
            object.setSelected(true);
        }
    }

    private void animateToTab(int n) {
        int n2;
        if (n == -1) {
            return;
        }
        if (this.getWindowToken() == null || !ViewCompat.isLaidOut((View)this) || this.mTabStrip.childrenNeedLayout()) {
            this.setScrollPosition(n, 0.0f, true);
            return;
        }
        int n3 = this.getScrollX();
        if (n3 != (n2 = this.calculateScrollXForTab(n, 0.0f))) {
            if (this.mScrollAnimator == null) {
                this.mScrollAnimator = ViewUtils.createAnimator();
                this.mScrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                this.mScrollAnimator.setDuration(300);
                this.mScrollAnimator.setUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener(){

                    @Override
                    public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                        TabLayout.this.scrollTo(valueAnimatorCompat.getAnimatedIntValue(), 0);
                    }
                });
            }
            this.mScrollAnimator.setIntValues(n3, n2);
            this.mScrollAnimator.start();
        }
        this.mTabStrip.animateIndicatorToPosition(n, 300);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void applyModeAndGravity() {
        int n = 0;
        if (this.mMode == 0) {
            n = Math.max(0, this.mContentInsetStart - this.mTabPaddingStart);
        }
        ViewCompat.setPaddingRelative((View)this.mTabStrip, n, 0, 0, 0);
        switch (this.mMode) {
            case 1: {
                this.mTabStrip.setGravity(1);
            }
            default: {
                break;
            }
            case 0: {
                this.mTabStrip.setGravity(8388611);
            }
        }
        this.updateTabViews(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int calculateScrollXForTab(int n, float f) {
        int n2 = 0;
        int n3 = 0;
        if (this.mMode != 0) return n2;
        View view = this.mTabStrip.getChildAt(n);
        View view2 = n + 1 < this.mTabStrip.getChildCount() ? this.mTabStrip.getChildAt(n + 1) : null;
        n = view != null ? view.getWidth() : 0;
        n2 = n3;
        if (view2 == null) return view.getLeft() + (int)((float)(n + n2) * f * 0.5f) + view.getWidth() / 2 - this.getWidth() / 2;
        n2 = view2.getWidth();
        return view.getLeft() + (int)((float)(n + n2) * f * 0.5f) + view.getWidth() / 2 - this.getWidth() / 2;
    }

    private void configureTab(Tab tab, int n) {
        tab.setPosition(n);
        this.mTabs.add(n, tab);
        int n2 = this.mTabs.size();
        ++n;
        while (n < n2) {
            this.mTabs.get(n).setPosition(n);
            ++n;
        }
    }

    private static ColorStateList createColorStateList(int n, int n2) {
        int[][] arrarrn = new int[2][];
        int[] arrn = new int[2];
        arrarrn[0] = SELECTED_STATE_SET;
        arrn[0] = n2;
        n2 = 0 + 1;
        arrarrn[n2] = EMPTY_STATE_SET;
        arrn[n2] = n;
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        this.updateTabViewLayoutParams(layoutParams);
        return layoutParams;
    }

    private TabView createTabView(Tab object) {
        object = new TabView(this.getContext(), (Tab)object);
        object.setFocusable(true);
        object.setMinimumWidth(this.getTabMinWidth());
        if (this.mTabClickListener == null) {
            this.mTabClickListener = new View.OnClickListener(){

                public void onClick(View view) {
                    ((TabView)view).getTab().select();
                }
            };
        }
        object.setOnClickListener(this.mTabClickListener);
        return object;
    }

    private int dpToPx(int n) {
        return Math.round(this.getResources().getDisplayMetrics().density * (float)n);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int getDefaultHeight() {
        boolean bl = false;
        int n = 0;
        int n2 = this.mTabs.size();
        do {
            boolean bl2 = bl;
            if (n < n2) {
                Tab tab = this.mTabs.get(n);
                if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty((CharSequence)tab.getText())) {
                    return 72;
                }
            } else {
                if (!bl2) return 48;
                return 72;
            }
            ++n;
        } while (true);
    }

    private float getScrollPosition() {
        return this.mTabStrip.getIndicatorPosition();
    }

    private int getTabMaxWidth() {
        return this.mTabMaxWidth;
    }

    private int getTabMinWidth() {
        if (this.mRequestedTabMinWidth != -1) {
            return this.mRequestedTabMinWidth;
        }
        if (this.mMode == 0) {
            return this.mScrollableTabMinWidth;
        }
        return 0;
    }

    private void removeTabViewAt(int n) {
        this.mTabStrip.removeViewAt(n);
        this.requestLayout();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setSelectedTabView(int n) {
        int n2 = this.mTabStrip.getChildCount();
        if (n >= n2 || this.mTabStrip.getChildAt(n).isSelected()) {
            return;
        }
        int n3 = 0;
        while (n3 < n2) {
            View view = this.mTabStrip.getChildAt(n3);
            boolean bl = n3 == n;
            view.setSelected(bl);
            ++n3;
        }
    }

    private void updateAllTabs() {
        int n = this.mTabStrip.getChildCount();
        for (int i = 0; i < n; ++i) {
            this.updateTab(i);
        }
    }

    private void updateTab(int n) {
        TabView tabView = (TabView)this.mTabStrip.getChildAt(n);
        if (tabView != null) {
            tabView.update();
        }
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams layoutParams) {
        if (this.mMode == 1 && this.mTabGravity == 0) {
            layoutParams.width = 0;
            layoutParams.weight = 1.0f;
            return;
        }
        layoutParams.width = -2;
        layoutParams.weight = 0.0f;
    }

    private void updateTabViews(boolean bl) {
        for (int i = 0; i < this.mTabStrip.getChildCount(); ++i) {
            View view = this.mTabStrip.getChildAt(i);
            view.setMinimumWidth(this.getTabMinWidth());
            this.updateTabViewLayoutParams((LinearLayout.LayoutParams)view.getLayoutParams());
            if (!bl) continue;
            view.requestLayout();
        }
    }

    public void addTab(@NonNull Tab tab) {
        this.addTab(tab, this.mTabs.isEmpty());
    }

    public void addTab(@NonNull Tab tab, int n) {
        this.addTab(tab, n, this.mTabs.isEmpty());
    }

    public void addTab(@NonNull Tab tab, int n, boolean bl) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        this.addTabView(tab, n, bl);
        this.configureTab(tab, n);
        if (bl) {
            tab.select();
        }
    }

    public void addTab(@NonNull Tab tab, boolean bl) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        this.addTabView(tab, bl);
        this.configureTab(tab, this.mTabs.size());
        if (bl) {
            tab.select();
        }
    }

    public int getSelectedTabPosition() {
        if (this.mSelectedTab != null) {
            return this.mSelectedTab.getPosition();
        }
        return -1;
    }

    @Nullable
    public Tab getTabAt(int n) {
        return this.mTabs.get(n);
    }

    public int getTabCount() {
        return this.mTabs.size();
    }

    public int getTabGravity() {
        return this.mTabGravity;
    }

    public int getTabMode() {
        return this.mMode;
    }

    @Nullable
    public ColorStateList getTabTextColors() {
        return this.mTabTextColors;
    }

    @NonNull
    public Tab newTab() {
        return new Tab(this);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        int n3 = this.dpToPx(this.getDefaultHeight()) + this.getPaddingTop() + this.getPaddingBottom();
        switch (View.MeasureSpec.getMode((int)n2)) {
            case Integer.MIN_VALUE: {
                n2 = View.MeasureSpec.makeMeasureSpec((int)Math.min(n3, View.MeasureSpec.getSize((int)n2)), (int)1073741824);
                break;
            }
            case 0: {
                n2 = View.MeasureSpec.makeMeasureSpec((int)n3, (int)1073741824);
            }
        }
        n3 = View.MeasureSpec.getSize((int)n);
        if (View.MeasureSpec.getMode((int)n) != 0) {
            n3 = this.mRequestedTabMaxWidth > 0 ? this.mRequestedTabMaxWidth : (n3 -= this.dpToPx(56));
            this.mTabMaxWidth = n3;
        }
        super.onMeasure(n, n2);
        if (this.getChildCount() == 1) {
            View view = this.getChildAt(0);
            n = 0;
            switch (this.mMode) {
                case 0: {
                    if (view.getMeasuredWidth() >= this.getMeasuredWidth()) {
                        return;
                    }
                    n = 1;
                }
                default: {
                    break;
                }
                case 1: {
                    if (view.getMeasuredWidth() == this.getMeasuredWidth()) {
                        return;
                    }
                    n = 1;
                }
            }
            if (n != 0) {
                n = TabLayout.getChildMeasureSpec((int)n2, (int)(this.getPaddingTop() + this.getPaddingBottom()), (int)view.getLayoutParams().height);
                view.measure(View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)1073741824), n);
            }
        }
    }

    public void removeAllTabs() {
        this.mTabStrip.removeAllViews();
        Iterator<Tab> iterator = this.mTabs.iterator();
        while (iterator.hasNext()) {
            iterator.next().setPosition(-1);
            iterator.remove();
        }
        this.mSelectedTab = null;
    }

    public void removeTab(Tab tab) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
        }
        this.removeTabAt(tab.getPosition());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void removeTabAt(int n) {
        int n2 = this.mSelectedTab != null ? this.mSelectedTab.getPosition() : 0;
        this.removeTabViewAt(n);
        Tab tab = this.mTabs.remove(n);
        if (tab != null) {
            tab.setPosition(-1);
        }
        int n3 = this.mTabs.size();
        for (int i = n; i < n3; ++i) {
            this.mTabs.get(i).setPosition(i);
        }
        if (n2 == n) {
            tab = this.mTabs.isEmpty() ? null : this.mTabs.get(Math.max(0, n - 1));
            this.selectTab(tab);
        }
    }

    void selectTab(Tab tab) {
        this.selectTab(tab, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    void selectTab(Tab tab, boolean bl) {
        if (this.mSelectedTab == tab) {
            if (this.mSelectedTab == null) return;
            {
                if (this.mOnTabSelectedListener != null) {
                    this.mOnTabSelectedListener.onTabReselected(this.mSelectedTab);
                }
                this.animateToTab(tab.getPosition());
                return;
            }
        } else {
            if (bl) {
                int n = tab != null ? tab.getPosition() : -1;
                if (n != -1) {
                    this.setSelectedTabView(n);
                }
                if (this.mSelectedTab != null && this.mSelectedTab.getPosition() == -1 && n != -1) {
                    this.setScrollPosition(n, 0.0f, true);
                } else {
                    this.animateToTab(n);
                }
            }
            if (this.mSelectedTab != null && this.mOnTabSelectedListener != null) {
                this.mOnTabSelectedListener.onTabUnselected(this.mSelectedTab);
            }
            this.mSelectedTab = tab;
            if (this.mSelectedTab == null || this.mOnTabSelectedListener == null) return;
            {
                this.mOnTabSelectedListener.onTabSelected(this.mSelectedTab);
                return;
            }
        }
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mOnTabSelectedListener = onTabSelectedListener;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void setScrollPosition(int n, float f, boolean bl) {
        if (this.mIndicatorAnimator != null && this.mIndicatorAnimator.isRunning()) {
            return;
        }
        if (n < 0) return;
        if (n >= this.mTabStrip.getChildCount()) return;
        this.mTabStrip.setIndicatorPositionFromTabPosition(n, f);
        this.scrollTo(this.calculateScrollXForTab(n, f), 0);
        if (!bl) return;
        this.setSelectedTabView(Math.round((float)n + f));
    }

    public void setSelectedTabIndicatorColor(@ColorInt int n) {
        this.mTabStrip.setSelectedIndicatorColor(n);
    }

    public void setSelectedTabIndicatorHeight(int n) {
        this.mTabStrip.setSelectedIndicatorHeight(n);
    }

    public void setTabGravity(int n) {
        if (this.mTabGravity != n) {
            this.mTabGravity = n;
            this.applyModeAndGravity();
        }
    }

    public void setTabMode(int n) {
        if (n != this.mMode) {
            this.mMode = n;
            this.applyModeAndGravity();
        }
    }

    public void setTabTextColors(int n, int n2) {
        this.setTabTextColors(TabLayout.createColorStateList(n, n2));
    }

    public void setTabTextColors(@Nullable ColorStateList colorStateList) {
        if (this.mTabTextColors != colorStateList) {
            this.mTabTextColors = colorStateList;
            this.updateAllTabs();
        }
    }

    public void setTabsFromPagerAdapter(@NonNull PagerAdapter pagerAdapter) {
        this.removeAllTabs();
        int n = pagerAdapter.getCount();
        for (int i = 0; i < n; ++i) {
            this.addTab(this.newTab().setText(pagerAdapter.getPageTitle(i)));
        }
    }

    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }
        this.setTabsFromPagerAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(this));
        this.setOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager));
        if (pagerAdapter.getCount() > 0) {
            int n = viewPager.getCurrentItem();
            if (this.getSelectedTabPosition() != n) {
                this.selectTab(this.getTabAt(n));
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Mode {
    }

    public static interface OnTabSelectedListener {
        public void onTabReselected(Tab var1);

        public void onTabSelected(Tab var1);

        public void onTabUnselected(Tab var1);
    }

    private class SlidingTabStrip
    extends LinearLayout {
        private ValueAnimatorCompat mCurrentAnimator;
        private int mIndicatorLeft;
        private int mIndicatorRight;
        private int mSelectedIndicatorHeight;
        private final Paint mSelectedIndicatorPaint;
        private int mSelectedPosition;
        private float mSelectionOffset;

        SlidingTabStrip(Context context) {
            super(context);
            this.mSelectedPosition = -1;
            this.mIndicatorLeft = -1;
            this.mIndicatorRight = -1;
            this.setWillNotDraw(false);
            this.mSelectedIndicatorPaint = new Paint();
        }

        private void setIndicatorPosition(int n, int n2) {
            if (n != this.mIndicatorLeft || n2 != this.mIndicatorRight) {
                this.mIndicatorLeft = n;
                this.mIndicatorRight = n2;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private void updateIndicatorPosition() {
            int n;
            int n2;
            View view = this.getChildAt(this.mSelectedPosition);
            if (view != null && view.getWidth() > 0) {
                int n3 = view.getLeft();
                int n4 = view.getRight();
                n = n3;
                n2 = n4;
                if (this.mSelectionOffset > 0.0f) {
                    n = n3;
                    n2 = n4;
                    if (this.mSelectedPosition < this.getChildCount() - 1) {
                        view = this.getChildAt(this.mSelectedPosition + 1);
                        n = (int)(this.mSelectionOffset * (float)view.getLeft() + (1.0f - this.mSelectionOffset) * (float)n3);
                        n2 = (int)(this.mSelectionOffset * (float)view.getRight() + (1.0f - this.mSelectionOffset) * (float)n4);
                    }
                }
            } else {
                n2 = -1;
                n = -1;
            }
            this.setIndicatorPosition(n, n2);
        }

        /*
         * Enabled aggressive block sorting
         */
        void animateIndicatorToPosition(final int n, int n2) {
            int n3;
            final int n4 = ViewCompat.getLayoutDirection((View)this) == 1 ? 1 : 0;
            Object object = this.getChildAt(n);
            final int n5 = object.getLeft();
            final int n6 = object.getRight();
            if (Math.abs(n - this.mSelectedPosition) <= 1) {
                n4 = this.mIndicatorLeft;
                n3 = this.mIndicatorRight;
            } else {
                n3 = TabLayout.this.dpToPx(24);
                n4 = n < this.mSelectedPosition ? (n4 != 0 ? (n3 = n5 - n3) : (n3 = n6 + n3)) : (n4 != 0 ? (n3 = n6 + n3) : (n3 = n5 - n3));
            }
            if (n4 != n5 || n3 != n6) {
                object = TabLayout.this.mIndicatorAnimator = ViewUtils.createAnimator();
                object.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                object.setDuration(n2);
                object.setFloatValues(0.0f, 1.0f);
                object.setUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener(){

                    @Override
                    public void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat) {
                        float f = valueAnimatorCompat.getAnimatedFraction();
                        SlidingTabStrip.this.setIndicatorPosition(AnimationUtils.lerp(n4, n5, f), AnimationUtils.lerp(n3, n6, f));
                    }
                });
                object.setListener(new ValueAnimatorCompat.AnimatorListenerAdapter(){

                    @Override
                    public void onAnimationCancel(ValueAnimatorCompat valueAnimatorCompat) {
                        SlidingTabStrip.this.mSelectedPosition = n;
                        SlidingTabStrip.this.mSelectionOffset = 0.0f;
                    }

                    @Override
                    public void onAnimationEnd(ValueAnimatorCompat valueAnimatorCompat) {
                        SlidingTabStrip.this.mSelectedPosition = n;
                        SlidingTabStrip.this.mSelectionOffset = 0.0f;
                    }
                });
                object.start();
                this.mCurrentAnimator = object;
            }
        }

        boolean childrenNeedLayout() {
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                if (this.getChildAt(i).getWidth() > 0) continue;
                return true;
            }
            return false;
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (this.mIndicatorLeft >= 0 && this.mIndicatorRight > this.mIndicatorLeft) {
                canvas.drawRect((float)this.mIndicatorLeft, (float)(this.getHeight() - this.mSelectedIndicatorHeight), (float)this.mIndicatorRight, (float)this.getHeight(), this.mSelectedIndicatorPaint);
            }
        }

        float getIndicatorPosition() {
            return (float)this.mSelectedPosition + this.mSelectionOffset;
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            super.onLayout(bl, n, n2, n3, n4);
            if (this.mCurrentAnimator != null && this.mCurrentAnimator.isRunning()) {
                this.mCurrentAnimator.cancel();
                long l = this.mCurrentAnimator.getDuration();
                this.animateIndicatorToPosition(this.mSelectedPosition, Math.round((1.0f - this.mCurrentAnimator.getAnimatedFraction()) * (float)l));
                return;
            }
            this.updateIndicatorPosition();
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        protected void onMeasure(int n, int n2) {
            int n3;
            int n4;
            View view;
            int n5;
            super.onMeasure(n, n2);
            if (View.MeasureSpec.getMode((int)n) != 1073741824) {
                return;
            }
            if (TabLayout.this.mMode != 1) return;
            if (TabLayout.this.mTabGravity != 1) return;
            int n6 = this.getChildCount();
            int n7 = 0;
            for (n5 = 0; n5 < n6; ++n5) {
                view = this.getChildAt(n5);
                n3 = n7;
                if (view.getVisibility() == 0) {
                    n3 = Math.max(n7, view.getMeasuredWidth());
                }
                n7 = n3;
            }
            if (n7 <= 0) return;
            n3 = TabLayout.this.dpToPx(16);
            n5 = 0;
            if (n7 * n6 <= this.getMeasuredWidth() - n3 * 2) {
                n3 = 0;
                do {
                    n4 = n5;
                    if (n3 < n6) {
                        view = (LinearLayout.LayoutParams)this.getChildAt(n3).getLayoutParams();
                        if (view.width != n7 || view.weight != 0.0f) {
                            view.width = n7;
                            view.weight = 0.0f;
                            n5 = 1;
                        }
                        ++n3;
                        continue;
                    }
                    break;
                } while (true);
            } else {
                TabLayout.this.mTabGravity = 0;
                TabLayout.this.updateTabViews(false);
                n4 = 1;
            }
            if (n4 == 0) return;
            super.onMeasure(n, n2);
        }

        void setIndicatorPositionFromTabPosition(int n, float f) {
            this.mSelectedPosition = n;
            this.mSelectionOffset = f;
            this.updateIndicatorPosition();
        }

        void setSelectedIndicatorColor(int n) {
            if (this.mSelectedIndicatorPaint.getColor() != n) {
                this.mSelectedIndicatorPaint.setColor(n);
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }

        void setSelectedIndicatorHeight(int n) {
            if (this.mSelectedIndicatorHeight != n) {
                this.mSelectedIndicatorHeight = n;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }

    }

    public static final class Tab {
        public static final int INVALID_POSITION = -1;
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        private final TabLayout mParent;
        private int mPosition = -1;
        private Object mTag;
        private CharSequence mText;

        Tab(TabLayout tabLayout) {
            this.mParent = tabLayout;
        }

        @Nullable
        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }

        @Nullable
        public View getCustomView() {
            return this.mCustomView;
        }

        @Nullable
        public Drawable getIcon() {
            return this.mIcon;
        }

        public int getPosition() {
            return this.mPosition;
        }

        @Nullable
        public Object getTag() {
            return this.mTag;
        }

        @Nullable
        public CharSequence getText() {
            return this.mText;
        }

        public boolean isSelected() {
            if (this.mParent.getSelectedTabPosition() == this.mPosition) {
                return true;
            }
            return false;
        }

        public void select() {
            this.mParent.selectTab(this);
        }

        @NonNull
        public Tab setContentDescription(@StringRes int n) {
            return this.setContentDescription(this.mParent.getResources().getText(n));
        }

        @NonNull
        public Tab setContentDescription(@Nullable CharSequence charSequence) {
            this.mContentDesc = charSequence;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }

        @NonNull
        public Tab setCustomView(@LayoutRes int n) {
            return this.setCustomView(LayoutInflater.from((Context)this.mParent.getContext()).inflate(n, null));
        }

        @NonNull
        public Tab setCustomView(@Nullable View view) {
            this.mCustomView = view;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }

        @NonNull
        public Tab setIcon(@DrawableRes int n) {
            return this.setIcon(TintManager.getDrawable(this.mParent.getContext(), n));
        }

        @NonNull
        public Tab setIcon(@Nullable Drawable drawable2) {
            this.mIcon = drawable2;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }

        void setPosition(int n) {
            this.mPosition = n;
        }

        @NonNull
        public Tab setTag(@Nullable Object object) {
            this.mTag = object;
            return this;
        }

        @NonNull
        public Tab setText(@StringRes int n) {
            return this.setText(this.mParent.getResources().getText(n));
        }

        @NonNull
        public Tab setText(@Nullable CharSequence charSequence) {
            this.mText = charSequence;
            if (this.mPosition >= 0) {
                this.mParent.updateTab(this.mPosition);
            }
            return this;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TabGravity {
    }

    public static class TabLayoutOnPageChangeListener
    implements ViewPager.OnPageChangeListener {
        private int mPreviousScrollState;
        private int mScrollState;
        private final WeakReference<TabLayout> mTabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.mTabLayoutRef = new WeakReference<TabLayout>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(int n) {
            this.mPreviousScrollState = this.mScrollState;
            this.mScrollState = n;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onPageScrolled(int n, float f, int n2) {
            boolean bl = true;
            TabLayout tabLayout = this.mTabLayoutRef.get();
            if (tabLayout == null) return;
            boolean bl2 = bl;
            if (this.mScrollState != 1) {
                bl2 = this.mScrollState == 2 && this.mPreviousScrollState == 1 ? bl : false;
            }
            tabLayout.setScrollPosition(n, f, bl2);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onPageSelected(int n) {
            TabLayout tabLayout = this.mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != n) {
                Tab tab = tabLayout.getTabAt(n);
                boolean bl = this.mScrollState == 0;
                tabLayout.selectTab(tab, bl);
            }
        }
    }

    class TabView
    extends LinearLayout
    implements View.OnLongClickListener {
        private ImageView mCustomIconView;
        private TextView mCustomTextView;
        private View mCustomView;
        private int mDefaultMaxLines;
        private ImageView mIconView;
        private final Tab mTab;
        private TextView mTextView;

        public TabView(Context context, Tab tab) {
            super(context);
            this.mDefaultMaxLines = 2;
            this.mTab = tab;
            if (TabLayout.this.mTabBackgroundResId != 0) {
                this.setBackgroundDrawable(TintManager.getDrawable(context, TabLayout.this.mTabBackgroundResId));
            }
            ViewCompat.setPaddingRelative((View)this, TabLayout.this.mTabPaddingStart, TabLayout.this.mTabPaddingTop, TabLayout.this.mTabPaddingEnd, TabLayout.this.mTabPaddingBottom);
            this.setGravity(17);
            this.setOrientation(1);
            this.update();
        }

        private float approximateLineWidth(Layout layout2, int n, float f) {
            return layout2.getLineWidth(n) * (f / layout2.getPaint().getTextSize());
        }

        /*
         * Enabled aggressive block sorting
         */
        private void updateTextAndIcon(Tab tab, TextView textView, ImageView imageView) {
            Drawable drawable2 = tab.getIcon();
            CharSequence charSequence = tab.getText();
            if (imageView != null) {
                if (drawable2 != null) {
                    imageView.setImageDrawable(drawable2);
                    imageView.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable(null);
                }
                imageView.setContentDescription(tab.getContentDescription());
            }
            boolean bl = !TextUtils.isEmpty((CharSequence)charSequence);
            if (textView != null) {
                if (bl) {
                    textView.setText(charSequence);
                    textView.setContentDescription(tab.getContentDescription());
                    textView.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    textView.setVisibility(8);
                    textView.setText(null);
                }
            }
            if (imageView != null) {
                int n;
                textView = (ViewGroup.MarginLayoutParams)imageView.getLayoutParams();
                int n2 = n = 0;
                if (bl) {
                    n2 = n;
                    if (imageView.getVisibility() == 0) {
                        n2 = TabLayout.this.dpToPx(8);
                    }
                }
                if (n2 != textView.bottomMargin) {
                    textView.bottomMargin = n2;
                    imageView.requestLayout();
                }
            }
            if (!bl && !TextUtils.isEmpty((CharSequence)tab.getContentDescription())) {
                this.setOnLongClickListener((View.OnLongClickListener)this);
                return;
            }
            this.setOnLongClickListener(null);
            this.setLongClickable(false);
        }

        public Tab getTab() {
            return this.mTab;
        }

        @TargetApi(value=14)
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        @TargetApi(value=14)
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        public boolean onLongClick(View arrn) {
            arrn = new int[2];
            this.getLocationOnScreen(arrn);
            Context context = this.getContext();
            int n = this.getWidth();
            int n2 = this.getHeight();
            int n3 = context.getResources().getDisplayMetrics().widthPixels;
            context = Toast.makeText((Context)context, (CharSequence)this.mTab.getContentDescription(), (int)0);
            context.setGravity(49, arrn[0] + n / 2 - n3 / 2, n2);
            context.show();
            return true;
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        public void onMeasure(int n, int n2) {
            int n3;
            float f;
            int n4 = View.MeasureSpec.getSize((int)n);
            int n5 = TabLayout.this.getTabMaxWidth();
            if (n5 > 0 && (n4 == 0 || n4 > n5)) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec((int)TabLayout.this.mTabMaxWidth, (int)Integer.MIN_VALUE), n2);
            } else {
                super.onMeasure(n, n2);
            }
            if (this.mTextView == null) return;
            this.getResources();
            float f2 = TabLayout.this.mTabTextSize;
            n5 = this.mDefaultMaxLines;
            if (this.mIconView != null && this.mIconView.getVisibility() == 0) {
                n4 = 1;
                f = f2;
            } else {
                n4 = n5;
                f = f2;
                if (this.mTextView != null) {
                    n4 = n5;
                    f = f2;
                    if (this.mTextView.getLineCount() > 1) {
                        f = TabLayout.this.mTabTextMultiLineSize;
                        n4 = n5;
                    }
                }
            }
            f2 = this.mTextView.getTextSize();
            int n6 = this.mTextView.getLineCount();
            n5 = TextViewCompat.getMaxLines(this.mTextView);
            if (f == f2) {
                if (n5 < 0) return;
                if (n4 == n5) return;
            }
            n5 = n3 = 1;
            if (TabLayout.this.mMode == 1) {
                n5 = n3;
                if (f > f2) {
                    n5 = n3;
                    if (n6 == 1) {
                        Layout layout2 = this.mTextView.getLayout();
                        if (layout2 == null) return;
                        n5 = n3;
                        if (this.approximateLineWidth(layout2, 0, f) > (float)layout2.getWidth()) {
                            return;
                        }
                    }
                }
                if (n5 == 0) return;
            }
            this.mTextView.setTextSize(0, f);
            this.mTextView.setMaxLines(n4);
            super.onMeasure(n, n2);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setSelected(boolean bl) {
            boolean bl2 = this.isSelected() != bl;
            super.setSelected(bl);
            if (bl2 && bl) {
                this.sendAccessibilityEvent(4);
                if (this.mTextView != null) {
                    this.mTextView.setSelected(bl);
                }
                if (this.mIconView != null) {
                    this.mIconView.setSelected(bl);
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        final void update() {
            Tab tab = this.mTab;
            View view = tab.getCustomView();
            if (view != null) {
                ViewParent viewParent = view.getParent();
                if (viewParent != this) {
                    if (viewParent != null) {
                        ((ViewGroup)viewParent).removeView(view);
                    }
                    this.addView(view);
                }
                this.mCustomView = view;
                if (this.mTextView != null) {
                    this.mTextView.setVisibility(8);
                }
                if (this.mIconView != null) {
                    this.mIconView.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
                this.mCustomTextView = (TextView)view.findViewById(16908308);
                if (this.mCustomTextView != null) {
                    this.mDefaultMaxLines = TextViewCompat.getMaxLines(this.mCustomTextView);
                }
                this.mCustomIconView = (ImageView)view.findViewById(16908294);
            } else {
                if (this.mCustomView != null) {
                    this.removeView(this.mCustomView);
                    this.mCustomView = null;
                }
                this.mCustomTextView = null;
                this.mCustomIconView = null;
            }
            if (this.mCustomView == null) {
                if (this.mIconView == null) {
                    view = (ImageView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_layout_tab_icon, (ViewGroup)this, false);
                    this.addView(view, 0);
                    this.mIconView = view;
                }
                if (this.mTextView == null) {
                    view = (TextView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_layout_tab_text, (ViewGroup)this, false);
                    this.addView(view);
                    this.mTextView = view;
                    this.mDefaultMaxLines = TextViewCompat.getMaxLines(this.mTextView);
                }
                this.mTextView.setTextAppearance(this.getContext(), TabLayout.this.mTabTextAppearance);
                if (TabLayout.this.mTabTextColors != null) {
                    this.mTextView.setTextColor(TabLayout.this.mTabTextColors);
                }
                this.updateTextAndIcon(tab, this.mTextView, this.mIconView);
                return;
            } else {
                if (this.mCustomTextView == null && this.mCustomIconView == null) return;
                {
                    this.updateTextAndIcon(tab, this.mCustomTextView, this.mCustomIconView);
                    return;
                }
            }
        }
    }

    public static class ViewPagerOnTabSelectedListener
    implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        @Override
        public void onTabReselected(Tab tab) {
        }

        @Override
        public void onTabSelected(Tab tab) {
            this.mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(Tab tab) {
        }
    }

}

