/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.ContextThemeWrapper
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.Window
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.SpinnerAdapter
 */
package android.support.v7.internal.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.internal.app.NavItemSelectedListener;
import android.support.v7.internal.view.ActionBarPolicy;
import android.support.v7.internal.view.SupportMenuInflater;
import android.support.v7.internal.view.ViewPropertyAnimatorCompatSet;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.support.v7.internal.widget.DecorToolbar;
import android.support.v7.internal.widget.ScrollingTabContainerView;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class WindowDecorActionBar
extends ActionBar
implements ActionBarOverlayLayout.ActionBarVisibilityCallback {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final boolean ALLOW_SHOW_HIDE_ANIMATIONS;
    private static final long FADE_IN_DURATION_MS = 200;
    private static final long FADE_OUT_DURATION_MS = 100;
    private static final int INVALID_POSITION = -1;
    private static final String TAG = "WindowDecorActionBar";
    private static final Interpolator sHideInterpolator;
    private static final Interpolator sShowInterpolator;
    ActionModeImpl mActionMode;
    private Activity mActivity;
    private ActionBarContainer mContainerView;
    private boolean mContentAnimations = true;
    private View mContentView;
    private Context mContext;
    private ActionBarContextView mContextView;
    private int mCurWindowVisibility = 0;
    private ViewPropertyAnimatorCompatSet mCurrentShowAnim;
    private DecorToolbar mDecorToolbar;
    ActionMode mDeferredDestroyActionMode;
    ActionMode.Callback mDeferredModeDestroyCallback;
    private Dialog mDialog;
    private boolean mDisplayHomeAsUpSet;
    private boolean mHasEmbeddedTabs;
    private boolean mHiddenByApp;
    private boolean mHiddenBySystem;
    final ViewPropertyAnimatorListener mHideListener;
    boolean mHideOnContentScroll;
    private boolean mLastMenuVisibility;
    private ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new ArrayList();
    private boolean mNowShowing = true;
    private ActionBarOverlayLayout mOverlayLayout;
    private int mSavedTabPosition = -1;
    private TabImpl mSelectedTab;
    private boolean mShowHideAnimationEnabled;
    final ViewPropertyAnimatorListener mShowListener;
    private boolean mShowingForMode;
    private ScrollingTabContainerView mTabScrollView;
    private ArrayList<TabImpl> mTabs = new ArrayList();
    private Context mThemedContext;
    private TintManager mTintManager;
    final ViewPropertyAnimatorUpdateListener mUpdateListener;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = true;
        boolean bl2 = !WindowDecorActionBar.class.desiredAssertionStatus();
        $assertionsDisabled = bl2;
        sHideInterpolator = new AccelerateInterpolator();
        sShowInterpolator = new DecelerateInterpolator();
        bl2 = Build.VERSION.SDK_INT >= 14 ? bl : false;
        ALLOW_SHOW_HIDE_ANIMATIONS = bl2;
    }

    public WindowDecorActionBar(Activity activity, boolean bl) {
        this.mHideListener = new ViewPropertyAnimatorListenerAdapter(){

            @Override
            public void onAnimationEnd(View view) {
                if (WindowDecorActionBar.this.mContentAnimations && WindowDecorActionBar.this.mContentView != null) {
                    ViewCompat.setTranslationY(WindowDecorActionBar.this.mContentView, 0.0f);
                    ViewCompat.setTranslationY((View)WindowDecorActionBar.this.mContainerView, 0.0f);
                }
                WindowDecorActionBar.this.mContainerView.setVisibility(8);
                WindowDecorActionBar.this.mContainerView.setTransitioning(false);
                WindowDecorActionBar.this.mCurrentShowAnim = null;
                WindowDecorActionBar.this.completeDeferredDestroyActionMode();
                if (WindowDecorActionBar.this.mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets((View)WindowDecorActionBar.this.mOverlayLayout);
                }
            }
        };
        this.mShowListener = new ViewPropertyAnimatorListenerAdapter(){

            @Override
            public void onAnimationEnd(View view) {
                WindowDecorActionBar.this.mCurrentShowAnim = null;
                WindowDecorActionBar.this.mContainerView.requestLayout();
            }
        };
        this.mUpdateListener = new ViewPropertyAnimatorUpdateListener(){

            @Override
            public void onAnimationUpdate(View view) {
                ((View)WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
            }
        };
        this.mActivity = activity;
        activity = activity.getWindow().getDecorView();
        this.init((View)activity);
        if (!bl) {
            this.mContentView = activity.findViewById(16908290);
        }
    }

    public WindowDecorActionBar(Dialog dialog) {
        this.mHideListener = new ;
        this.mShowListener = new ;
        this.mUpdateListener = new ;
        this.mDialog = dialog;
        this.init(dialog.getWindow().getDecorView());
    }

    public WindowDecorActionBar(View view) {
        this.mHideListener = new ;
        this.mShowListener = new ;
        this.mUpdateListener = new ;
        if (!$assertionsDisabled && !view.isInEditMode()) {
            throw new AssertionError();
        }
        this.init(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean checkShowingFlags(boolean bl, boolean bl2, boolean bl3) {
        if (bl3 || !bl && !bl2) {
            return true;
        }
        return false;
    }

    private void cleanupTabs() {
        if (this.mSelectedTab != null) {
            this.selectTab(null);
        }
        this.mTabs.clear();
        if (this.mTabScrollView != null) {
            this.mTabScrollView.removeAllTabs();
        }
        this.mSavedTabPosition = -1;
    }

    private void configureTab(ActionBar.Tab tab, int n) {
        if ((tab = (TabImpl)tab).getCallback() == null) {
            throw new IllegalStateException("Action Bar Tab must have a Callback");
        }
        tab.setPosition(n);
        this.mTabs.add(n, (TabImpl)tab);
        int n2 = this.mTabs.size();
        ++n;
        while (n < n2) {
            this.mTabs.get(n).setPosition(n);
            ++n;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void ensureTabsExist() {
        if (this.mTabScrollView != null) {
            return;
        }
        ScrollingTabContainerView scrollingTabContainerView = new ScrollingTabContainerView(this.mContext);
        if (this.mHasEmbeddedTabs) {
            scrollingTabContainerView.setVisibility(0);
            this.mDecorToolbar.setEmbeddedTabView(scrollingTabContainerView);
        } else {
            if (this.getNavigationMode() == 2) {
                scrollingTabContainerView.setVisibility(0);
                if (this.mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets((View)this.mOverlayLayout);
                }
            } else {
                scrollingTabContainerView.setVisibility(8);
            }
            this.mContainerView.setTabContainer(scrollingTabContainerView);
        }
        this.mTabScrollView = scrollingTabContainerView;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private DecorToolbar getDecorToolbar(View object) {
        if (object instanceof DecorToolbar) {
            return (DecorToolbar)object;
        }
        if (object instanceof Toolbar) {
            return ((Toolbar)((Object)object)).getWrapper();
        }
        if ("Can't make a decor toolbar out of " + object != null) {
            object = object.getClass().getSimpleName();
            do {
                throw new IllegalStateException((String)object);
                break;
            } while (true);
        }
        object = "null";
        throw new IllegalStateException((String)object);
    }

    private void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            if (this.mOverlayLayout != null) {
                this.mOverlayLayout.setShowingForActionMode(false);
            }
            this.updateVisibility(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void init(View object) {
        this.mOverlayLayout = (ActionBarOverlayLayout)object.findViewById(R.id.decor_content_parent);
        if (this.mOverlayLayout != null) {
            this.mOverlayLayout.setActionBarVisibilityCallback(this);
        }
        this.mDecorToolbar = this.getDecorToolbar(object.findViewById(R.id.action_bar));
        this.mContextView = (ActionBarContextView)object.findViewById(R.id.action_context_bar);
        this.mContainerView = (ActionBarContainer)object.findViewById(R.id.action_bar_container);
        if (this.mDecorToolbar == null || this.mContextView == null || this.mContainerView == null) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with a compatible window decor layout");
        }
        this.mContext = this.mDecorToolbar.getContext();
        int n = (this.mDecorToolbar.getDisplayOptions() & 4) != 0 ? 1 : 0;
        if (n != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        boolean bl = (object = ActionBarPolicy.get(this.mContext)).enableHomeButtonByDefault() || n != 0;
        this.setHomeButtonEnabled(bl);
        this.setHasEmbeddedTabs(object.hasEmbeddedTabs());
        object = this.mContext.obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        if (object.getBoolean(R.styleable.ActionBar_hideOnContentScroll, false)) {
            this.setHideOnContentScrollEnabled(true);
        }
        if ((n = object.getDimensionPixelSize(R.styleable.ActionBar_elevation, 0)) != 0) {
            this.setElevation(n);
        }
        object.recycle();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setHasEmbeddedTabs(boolean bl) {
        boolean bl2 = true;
        this.mHasEmbeddedTabs = bl;
        if (!this.mHasEmbeddedTabs) {
            this.mDecorToolbar.setEmbeddedTabView(null);
            this.mContainerView.setTabContainer(this.mTabScrollView);
        } else {
            this.mContainerView.setTabContainer(null);
            this.mDecorToolbar.setEmbeddedTabView(this.mTabScrollView);
        }
        boolean bl3 = this.getNavigationMode() == 2;
        if (this.mTabScrollView != null) {
            if (bl3) {
                this.mTabScrollView.setVisibility(0);
                if (this.mOverlayLayout != null) {
                    ViewCompat.requestApplyInsets((View)this.mOverlayLayout);
                }
            } else {
                this.mTabScrollView.setVisibility(8);
            }
        }
        Object object = this.mDecorToolbar;
        bl = !this.mHasEmbeddedTabs && bl3;
        object.setCollapsible(bl);
        object = this.mOverlayLayout;
        bl = !this.mHasEmbeddedTabs && bl3 ? bl2 : false;
        object.setHasNonEmbeddedTabs(bl);
    }

    private void showForActionMode() {
        if (!this.mShowingForMode) {
            this.mShowingForMode = true;
            if (this.mOverlayLayout != null) {
                this.mOverlayLayout.setShowingForActionMode(true);
            }
            this.updateVisibility(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateVisibility(boolean bl) {
        if (WindowDecorActionBar.checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
            if (this.mNowShowing) return;
            {
                this.mNowShowing = true;
                this.doShow(bl);
                return;
            }
        } else {
            if (!this.mNowShowing) return;
            {
                this.mNowShowing = false;
                this.doHide(bl);
                return;
            }
        }
    }

    @Override
    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.add(onMenuVisibilityListener);
    }

    @Override
    public void addTab(ActionBar.Tab tab) {
        this.addTab(tab, this.mTabs.isEmpty());
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n) {
        this.addTab(tab, n, this.mTabs.isEmpty());
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n, boolean bl) {
        this.ensureTabsExist();
        this.mTabScrollView.addTab(tab, n, bl);
        this.configureTab(tab, n);
        if (bl) {
            this.selectTab(tab);
        }
    }

    @Override
    public void addTab(ActionBar.Tab tab, boolean bl) {
        this.ensureTabsExist();
        this.mTabScrollView.addTab(tab, bl);
        this.configureTab(tab, this.mTabs.size());
        if (bl) {
            this.selectTab(tab);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void animateToMode(boolean bl) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2;
        if (bl) {
            this.showForActionMode();
        } else {
            this.hideForActionMode();
        }
        if (bl) {
            viewPropertyAnimatorCompat = this.mDecorToolbar.setupAnimatorToVisibility(4, 100);
            viewPropertyAnimatorCompat2 = this.mContextView.setupAnimatorToVisibility(0, 200);
        } else {
            viewPropertyAnimatorCompat2 = this.mDecorToolbar.setupAnimatorToVisibility(0, 200);
            viewPropertyAnimatorCompat = this.mContextView.setupAnimatorToVisibility(8, 100);
        }
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
        viewPropertyAnimatorCompatSet.playSequentially(viewPropertyAnimatorCompat, viewPropertyAnimatorCompat2);
        viewPropertyAnimatorCompatSet.start();
    }

    @Override
    public boolean collapseActionView() {
        if (this.mDecorToolbar != null && this.mDecorToolbar.hasExpandedActionView()) {
            this.mDecorToolbar.collapseActionView();
            return true;
        }
        return false;
    }

    void completeDeferredDestroyActionMode() {
        if (this.mDeferredModeDestroyCallback != null) {
            this.mDeferredModeDestroyCallback.onDestroyActionMode(this.mDeferredDestroyActionMode);
            this.mDeferredDestroyActionMode = null;
            this.mDeferredModeDestroyCallback = null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void dispatchMenuVisibilityChanged(boolean bl) {
        if (bl == this.mLastMenuVisibility) {
            return;
        }
        this.mLastMenuVisibility = bl;
        int n = this.mMenuVisibilityListeners.size();
        int n2 = 0;
        while (n2 < n) {
            this.mMenuVisibilityListeners.get(n2).onMenuVisibilityChanged(bl);
            ++n2;
        }
    }

    public void doHide(boolean bl) {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
        if (this.mCurWindowVisibility == 0 && ALLOW_SHOW_HIDE_ANIMATIONS && (this.mShowHideAnimationEnabled || bl)) {
            float f;
            Object object;
            ViewCompat.setAlpha((View)this.mContainerView, 1.0f);
            this.mContainerView.setTransitioning(true);
            ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
            float f2 = f = (float)(- this.mContainerView.getHeight());
            if (bl) {
                object = new int[2];
                int[] arrn = object;
                arrn[0] = 0;
                arrn[1] = 0;
                this.mContainerView.getLocationInWindow((int[])object);
                f2 = f - (Object)object[1];
            }
            object = ViewCompat.animate((View)this.mContainerView).translationY(f2);
            object.setUpdateListener(this.mUpdateListener);
            viewPropertyAnimatorCompatSet.play((ViewPropertyAnimatorCompat)object);
            if (this.mContentAnimations && this.mContentView != null) {
                viewPropertyAnimatorCompatSet.play(ViewCompat.animate(this.mContentView).translationY(f2));
            }
            viewPropertyAnimatorCompatSet.setInterpolator(sHideInterpolator);
            viewPropertyAnimatorCompatSet.setDuration(250);
            viewPropertyAnimatorCompatSet.setListener(this.mHideListener);
            this.mCurrentShowAnim = viewPropertyAnimatorCompatSet;
            viewPropertyAnimatorCompatSet.start();
            return;
        }
        this.mHideListener.onAnimationEnd(null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void doShow(boolean bl) {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
        this.mContainerView.setVisibility(0);
        if (this.mCurWindowVisibility == 0 && ALLOW_SHOW_HIDE_ANIMATIONS && (this.mShowHideAnimationEnabled || bl)) {
            Object object;
            float f;
            ViewCompat.setTranslationY((View)this.mContainerView, 0.0f);
            float f2 = f = (float)(- this.mContainerView.getHeight());
            if (bl) {
                object = new int[2];
                int[] arrn = object;
                arrn[0] = 0;
                arrn[1] = 0;
                this.mContainerView.getLocationInWindow((int[])object);
                f2 = f - (Object)object[1];
            }
            ViewCompat.setTranslationY((View)this.mContainerView, f2);
            object = new ViewPropertyAnimatorCompatSet();
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate((View)this.mContainerView).translationY(0.0f);
            viewPropertyAnimatorCompat.setUpdateListener(this.mUpdateListener);
            object.play(viewPropertyAnimatorCompat);
            if (this.mContentAnimations && this.mContentView != null) {
                ViewCompat.setTranslationY(this.mContentView, f2);
                object.play(ViewCompat.animate(this.mContentView).translationY(0.0f));
            }
            object.setInterpolator(sShowInterpolator);
            object.setDuration(250);
            object.setListener(this.mShowListener);
            this.mCurrentShowAnim = object;
            object.start();
        } else {
            ViewCompat.setAlpha((View)this.mContainerView, 1.0f);
            ViewCompat.setTranslationY((View)this.mContainerView, 0.0f);
            if (this.mContentAnimations && this.mContentView != null) {
                ViewCompat.setTranslationY(this.mContentView, 0.0f);
            }
            this.mShowListener.onAnimationEnd(null);
        }
        if (this.mOverlayLayout != null) {
            ViewCompat.requestApplyInsets((View)this.mOverlayLayout);
        }
    }

    @Override
    public void enableContentAnimations(boolean bl) {
        this.mContentAnimations = bl;
    }

    @Override
    public View getCustomView() {
        return this.mDecorToolbar.getCustomView();
    }

    @Override
    public int getDisplayOptions() {
        return this.mDecorToolbar.getDisplayOptions();
    }

    @Override
    public float getElevation() {
        return ViewCompat.getElevation((View)this.mContainerView);
    }

    @Override
    public int getHeight() {
        return this.mContainerView.getHeight();
    }

    @Override
    public int getHideOffset() {
        return this.mOverlayLayout.getActionBarHideOffset();
    }

    @Override
    public int getNavigationItemCount() {
        switch (this.mDecorToolbar.getNavigationMode()) {
            default: {
                return 0;
            }
            case 2: {
                return this.mTabs.size();
            }
            case 1: 
        }
        return this.mDecorToolbar.getDropdownItemCount();
    }

    @Override
    public int getNavigationMode() {
        return this.mDecorToolbar.getNavigationMode();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int getSelectedNavigationIndex() {
        switch (this.mDecorToolbar.getNavigationMode()) {
            case 2: {
                if (this.mSelectedTab != null) {
                    return this.mSelectedTab.getPosition();
                }
            }
            default: {
                return -1;
            }
            case 1: 
        }
        return this.mDecorToolbar.getDropdownSelectedPosition();
    }

    @Override
    public ActionBar.Tab getSelectedTab() {
        return this.mSelectedTab;
    }

    @Override
    public CharSequence getSubtitle() {
        return this.mDecorToolbar.getSubtitle();
    }

    @Override
    public ActionBar.Tab getTabAt(int n) {
        return this.mTabs.get(n);
    }

    @Override
    public int getTabCount() {
        return this.mTabs.size();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public Context getThemedContext() {
        if (this.mThemedContext != null) return this.mThemedContext;
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
        int n = typedValue.resourceId;
        if (n != 0) {
            this.mThemedContext = new ContextThemeWrapper(this.mContext, n);
            return this.mThemedContext;
        }
        this.mThemedContext = this.mContext;
        return this.mThemedContext;
    }

    TintManager getTintManager() {
        if (this.mTintManager == null) {
            this.mTintManager = TintManager.get(this.mContext);
        }
        return this.mTintManager;
    }

    @Override
    public CharSequence getTitle() {
        return this.mDecorToolbar.getTitle();
    }

    public boolean hasIcon() {
        return this.mDecorToolbar.hasIcon();
    }

    public boolean hasLogo() {
        return this.mDecorToolbar.hasLogo();
    }

    @Override
    public void hide() {
        if (!this.mHiddenByApp) {
            this.mHiddenByApp = true;
            this.updateVisibility(false);
        }
    }

    @Override
    public void hideForSystem() {
        if (!this.mHiddenBySystem) {
            this.mHiddenBySystem = true;
            this.updateVisibility(true);
        }
    }

    @Override
    public boolean isHideOnContentScrollEnabled() {
        return this.mOverlayLayout.isHideOnContentScrollEnabled();
    }

    @Override
    public boolean isShowing() {
        int n = this.getHeight();
        if (this.mNowShowing && (n == 0 || this.getHideOffset() < n)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTitleTruncated() {
        if (this.mDecorToolbar != null && this.mDecorToolbar.isTitleTruncated()) {
            return true;
        }
        return false;
    }

    @Override
    public ActionBar.Tab newTab() {
        return new TabImpl();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        this.setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
    }

    @Override
    public void onContentScrollStarted() {
        if (this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
            this.mCurrentShowAnim = null;
        }
    }

    @Override
    public void onContentScrollStopped() {
    }

    @Override
    public void onWindowVisibilityChanged(int n) {
        this.mCurWindowVisibility = n;
    }

    @Override
    public void removeAllTabs() {
        this.cleanupTabs();
    }

    @Override
    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.remove(onMenuVisibilityListener);
    }

    @Override
    public void removeTab(ActionBar.Tab tab) {
        this.removeTabAt(tab.getPosition());
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void removeTabAt(int n) {
        if (this.mTabScrollView == null) {
            return;
        }
        int n2 = this.mSelectedTab != null ? this.mSelectedTab.getPosition() : this.mSavedTabPosition;
        this.mTabScrollView.removeTabAt(n);
        TabImpl tabImpl = this.mTabs.remove(n);
        if (tabImpl != null) {
            tabImpl.setPosition(-1);
        }
        int n3 = this.mTabs.size();
        for (int i = n; i < n3; ++i) {
            this.mTabs.get(i).setPosition(i);
        }
        if (n2 != n) return;
        tabImpl = this.mTabs.isEmpty() ? null : this.mTabs.get(Math.max(0, n - 1));
        this.selectTab(tabImpl);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void selectTab(ActionBar.Tab tab) {
        int n = -1;
        if (this.getNavigationMode() != 2) {
            n = tab != null ? tab.getPosition() : -1;
            this.mSavedTabPosition = n;
            return;
        } else {
            FragmentTransaction fragmentTransaction = this.mActivity instanceof FragmentActivity && !this.mDecorToolbar.getViewGroup().isInEditMode() ? ((FragmentActivity)this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack() : null;
            if (this.mSelectedTab == tab) {
                if (this.mSelectedTab != null) {
                    this.mSelectedTab.getCallback().onTabReselected(this.mSelectedTab, fragmentTransaction);
                    this.mTabScrollView.animateToTab(tab.getPosition());
                }
            } else {
                ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
                if (tab != null) {
                    n = tab.getPosition();
                }
                scrollingTabContainerView.setTabSelected(n);
                if (this.mSelectedTab != null) {
                    this.mSelectedTab.getCallback().onTabUnselected(this.mSelectedTab, fragmentTransaction);
                }
                this.mSelectedTab = (TabImpl)tab;
                if (this.mSelectedTab != null) {
                    this.mSelectedTab.getCallback().onTabSelected(this.mSelectedTab, fragmentTransaction);
                }
            }
            if (fragmentTransaction == null || fragmentTransaction.isEmpty()) return;
            {
                fragmentTransaction.commit();
                return;
            }
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable2) {
        this.mContainerView.setPrimaryBackground(drawable2);
    }

    @Override
    public void setCustomView(int n) {
        this.setCustomView(LayoutInflater.from((Context)this.getThemedContext()).inflate(n, this.mDecorToolbar.getViewGroup(), false));
    }

    @Override
    public void setCustomView(View view) {
        this.mDecorToolbar.setCustomView(view);
    }

    @Override
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.mDecorToolbar.setCustomView(view);
    }

    @Override
    public void setDefaultDisplayHomeAsUpEnabled(boolean bl) {
        if (!this.mDisplayHomeAsUpSet) {
            this.setDisplayHomeAsUpEnabled(bl);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayHomeAsUpEnabled(boolean bl) {
        int n = bl ? 4 : 0;
        this.setDisplayOptions(n, 4);
    }

    @Override
    public void setDisplayOptions(int n) {
        if ((n & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mDecorToolbar.setDisplayOptions(n);
    }

    @Override
    public void setDisplayOptions(int n, int n2) {
        int n3 = this.mDecorToolbar.getDisplayOptions();
        if ((n2 & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mDecorToolbar.setDisplayOptions(n & n2 | ~ n2 & n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowCustomEnabled(boolean bl) {
        int n = bl ? 16 : 0;
        this.setDisplayOptions(n, 16);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowHomeEnabled(boolean bl) {
        int n = bl ? 2 : 0;
        this.setDisplayOptions(n, 2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowTitleEnabled(boolean bl) {
        int n = bl ? 8 : 0;
        this.setDisplayOptions(n, 8);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayUseLogoEnabled(boolean bl) {
        int n = bl ? 1 : 0;
        this.setDisplayOptions(n, 1);
    }

    @Override
    public void setElevation(float f) {
        ViewCompat.setElevation((View)this.mContainerView, f);
    }

    @Override
    public void setHideOffset(int n) {
        if (n != 0 && !this.mOverlayLayout.isInOverlayMode()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to set a non-zero hide offset");
        }
        this.mOverlayLayout.setActionBarHideOffset(n);
    }

    @Override
    public void setHideOnContentScrollEnabled(boolean bl) {
        if (bl && !this.mOverlayLayout.isInOverlayMode()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
        }
        this.mHideOnContentScroll = bl;
        this.mOverlayLayout.setHideOnContentScrollEnabled(bl);
    }

    @Override
    public void setHomeActionContentDescription(int n) {
        this.mDecorToolbar.setNavigationContentDescription(n);
    }

    @Override
    public void setHomeActionContentDescription(CharSequence charSequence) {
        this.mDecorToolbar.setNavigationContentDescription(charSequence);
    }

    @Override
    public void setHomeAsUpIndicator(int n) {
        this.mDecorToolbar.setNavigationIcon(n);
    }

    @Override
    public void setHomeAsUpIndicator(Drawable drawable2) {
        this.mDecorToolbar.setNavigationIcon(drawable2);
    }

    @Override
    public void setHomeButtonEnabled(boolean bl) {
        this.mDecorToolbar.setHomeButtonEnabled(bl);
    }

    @Override
    public void setIcon(int n) {
        this.mDecorToolbar.setIcon(n);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        this.mDecorToolbar.setIcon(drawable2);
    }

    @Override
    public void setListNavigationCallbacks(SpinnerAdapter spinnerAdapter, ActionBar.OnNavigationListener onNavigationListener) {
        this.mDecorToolbar.setDropdownParams(spinnerAdapter, new NavItemSelectedListener(onNavigationListener));
    }

    @Override
    public void setLogo(int n) {
        this.mDecorToolbar.setLogo(n);
    }

    @Override
    public void setLogo(Drawable drawable2) {
        this.mDecorToolbar.setLogo(drawable2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setNavigationMode(int n) {
        boolean bl = true;
        int n2 = this.mDecorToolbar.getNavigationMode();
        switch (n2) {
            case 2: {
                this.mSavedTabPosition = this.getSelectedNavigationIndex();
                this.selectTab(null);
                this.mTabScrollView.setVisibility(8);
                break;
            }
        }
        if (n2 != n && !this.mHasEmbeddedTabs && this.mOverlayLayout != null) {
            ViewCompat.requestApplyInsets((View)this.mOverlayLayout);
        }
        this.mDecorToolbar.setNavigationMode(n);
        switch (n) {
            case 2: {
                this.ensureTabsExist();
                this.mTabScrollView.setVisibility(0);
                if (this.mSavedTabPosition == -1) break;
                this.setSelectedNavigationItem(this.mSavedTabPosition);
                this.mSavedTabPosition = -1;
            }
        }
        Object object = this.mDecorToolbar;
        boolean bl2 = n == 2 && !this.mHasEmbeddedTabs;
        object.setCollapsible(bl2);
        object = this.mOverlayLayout;
        bl2 = n == 2 && !this.mHasEmbeddedTabs ? bl : false;
        object.setHasNonEmbeddedTabs(bl2);
    }

    @Override
    public void setSelectedNavigationItem(int n) {
        switch (this.mDecorToolbar.getNavigationMode()) {
            default: {
                throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
            }
            case 2: {
                this.selectTab(this.mTabs.get(n));
                return;
            }
            case 1: 
        }
        this.mDecorToolbar.setDropdownSelectedPosition(n);
    }

    @Override
    public void setShowHideAnimationEnabled(boolean bl) {
        this.mShowHideAnimationEnabled = bl;
        if (!bl && this.mCurrentShowAnim != null) {
            this.mCurrentShowAnim.cancel();
        }
    }

    @Override
    public void setSplitBackgroundDrawable(Drawable drawable2) {
    }

    @Override
    public void setStackedBackgroundDrawable(Drawable drawable2) {
        this.mContainerView.setStackedBackground(drawable2);
    }

    @Override
    public void setSubtitle(int n) {
        this.setSubtitle(this.mContext.getString(n));
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        this.mDecorToolbar.setSubtitle(charSequence);
    }

    @Override
    public void setTitle(int n) {
        this.setTitle(this.mContext.getString(n));
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mDecorToolbar.setTitle(charSequence);
    }

    @Override
    public void setWindowTitle(CharSequence charSequence) {
        this.mDecorToolbar.setWindowTitle(charSequence);
    }

    @Override
    public void show() {
        if (this.mHiddenByApp) {
            this.mHiddenByApp = false;
            this.updateVisibility(false);
        }
    }

    @Override
    public void showForSystem() {
        if (this.mHiddenBySystem) {
            this.mHiddenBySystem = false;
            this.updateVisibility(true);
        }
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback object) {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        this.mOverlayLayout.setHideOnContentScrollEnabled(false);
        this.mContextView.killMode();
        object = new ActionModeImpl(this.mContextView.getContext(), (ActionMode.Callback)object);
        if (object.dispatchOnCreate()) {
            object.invalidate();
            this.mContextView.initForMode((ActionMode)object);
            this.animateToMode(true);
            this.mContextView.sendAccessibilityEvent(32);
            this.mActionMode = object;
            return object;
        }
        return null;
    }

    public class ActionModeImpl
    extends ActionMode
    implements MenuBuilder.Callback {
        private final Context mActionModeContext;
        private ActionMode.Callback mCallback;
        private WeakReference<View> mCustomView;
        private final MenuBuilder mMenu;

        public ActionModeImpl(Context context, ActionMode.Callback callback) {
            this.mActionModeContext = context;
            this.mCallback = callback;
            this.mMenu = new MenuBuilder(context).setDefaultShowAsAction(1);
            this.mMenu.setCallback(this);
        }

        public boolean dispatchOnCreate() {
            this.mMenu.stopDispatchingItemsChanged();
            try {
                boolean bl = this.mCallback.onCreateActionMode(this, this.mMenu);
                return bl;
            }
            finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void finish() {
            if (WindowDecorActionBar.this.mActionMode != this) {
                return;
            }
            if (!WindowDecorActionBar.checkShowingFlags(WindowDecorActionBar.this.mHiddenByApp, WindowDecorActionBar.this.mHiddenBySystem, false)) {
                WindowDecorActionBar.this.mDeferredDestroyActionMode = this;
                WindowDecorActionBar.this.mDeferredModeDestroyCallback = this.mCallback;
            } else {
                this.mCallback.onDestroyActionMode(this);
            }
            this.mCallback = null;
            WindowDecorActionBar.this.animateToMode(false);
            WindowDecorActionBar.this.mContextView.closeMode();
            WindowDecorActionBar.this.mDecorToolbar.getViewGroup().sendAccessibilityEvent(32);
            WindowDecorActionBar.this.mOverlayLayout.setHideOnContentScrollEnabled(WindowDecorActionBar.this.mHideOnContentScroll);
            WindowDecorActionBar.this.mActionMode = null;
        }

        @Override
        public View getCustomView() {
            if (this.mCustomView != null) {
                return this.mCustomView.get();
            }
            return null;
        }

        @Override
        public Menu getMenu() {
            return this.mMenu;
        }

        @Override
        public MenuInflater getMenuInflater() {
            return new SupportMenuInflater(this.mActionModeContext);
        }

        @Override
        public CharSequence getSubtitle() {
            return WindowDecorActionBar.this.mContextView.getSubtitle();
        }

        @Override
        public CharSequence getTitle() {
            return WindowDecorActionBar.this.mContextView.getTitle();
        }

        @Override
        public void invalidate() {
            if (WindowDecorActionBar.this.mActionMode != this) {
                return;
            }
            this.mMenu.stopDispatchingItemsChanged();
            try {
                this.mCallback.onPrepareActionMode(this, this.mMenu);
                return;
            }
            finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }

        @Override
        public boolean isTitleOptional() {
            return WindowDecorActionBar.this.mContextView.isTitleOptional();
        }

        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        }

        public void onCloseSubMenu(SubMenuBuilder subMenuBuilder) {
        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (this.mCallback != null) {
                return this.mCallback.onActionItemClicked(this, menuItem);
            }
            return false;
        }

        @Override
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (this.mCallback == null) {
                return;
            }
            this.invalidate();
            WindowDecorActionBar.this.mContextView.showOverflowMenu();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            boolean bl = true;
            if (this.mCallback == null) {
                return false;
            }
            if (!subMenuBuilder.hasVisibleItems()) return bl;
            new MenuPopupHelper(WindowDecorActionBar.this.getThemedContext(), subMenuBuilder).show();
            return true;
        }

        @Override
        public void setCustomView(View view) {
            WindowDecorActionBar.this.mContextView.setCustomView(view);
            this.mCustomView = new WeakReference<View>(view);
        }

        @Override
        public void setSubtitle(int n) {
            this.setSubtitle(WindowDecorActionBar.this.mContext.getResources().getString(n));
        }

        @Override
        public void setSubtitle(CharSequence charSequence) {
            WindowDecorActionBar.this.mContextView.setSubtitle(charSequence);
        }

        @Override
        public void setTitle(int n) {
            this.setTitle(WindowDecorActionBar.this.mContext.getResources().getString(n));
        }

        @Override
        public void setTitle(CharSequence charSequence) {
            WindowDecorActionBar.this.mContextView.setTitle(charSequence);
        }

        @Override
        public void setTitleOptionalHint(boolean bl) {
            super.setTitleOptionalHint(bl);
            WindowDecorActionBar.this.mContextView.setTitleOptional(bl);
        }
    }

    public class TabImpl
    extends ActionBar.Tab {
        private ActionBar.TabListener mCallback;
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        private int mPosition;
        private Object mTag;
        private CharSequence mText;

        public TabImpl() {
            this.mPosition = -1;
        }

        public ActionBar.TabListener getCallback() {
            return this.mCallback;
        }

        @Override
        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }

        @Override
        public View getCustomView() {
            return this.mCustomView;
        }

        @Override
        public Drawable getIcon() {
            return this.mIcon;
        }

        @Override
        public int getPosition() {
            return this.mPosition;
        }

        @Override
        public Object getTag() {
            return this.mTag;
        }

        @Override
        public CharSequence getText() {
            return this.mText;
        }

        @Override
        public void select() {
            WindowDecorActionBar.this.selectTab(this);
        }

        @Override
        public ActionBar.Tab setContentDescription(int n) {
            return this.setContentDescription(WindowDecorActionBar.this.mContext.getResources().getText(n));
        }

        @Override
        public ActionBar.Tab setContentDescription(CharSequence charSequence) {
            this.mContentDesc = charSequence;
            if (this.mPosition >= 0) {
                WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        @Override
        public ActionBar.Tab setCustomView(int n) {
            return this.setCustomView(LayoutInflater.from((Context)WindowDecorActionBar.this.getThemedContext()).inflate(n, null));
        }

        @Override
        public ActionBar.Tab setCustomView(View view) {
            this.mCustomView = view;
            if (this.mPosition >= 0) {
                WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        @Override
        public ActionBar.Tab setIcon(int n) {
            return this.setIcon(WindowDecorActionBar.this.getTintManager().getDrawable(n));
        }

        @Override
        public ActionBar.Tab setIcon(Drawable drawable2) {
            this.mIcon = drawable2;
            if (this.mPosition >= 0) {
                WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        public void setPosition(int n) {
            this.mPosition = n;
        }

        @Override
        public ActionBar.Tab setTabListener(ActionBar.TabListener tabListener) {
            this.mCallback = tabListener;
            return this;
        }

        @Override
        public ActionBar.Tab setTag(Object object) {
            this.mTag = object;
            return this;
        }

        @Override
        public ActionBar.Tab setText(int n) {
            return this.setText(WindowDecorActionBar.this.mContext.getResources().getText(n));
        }

        @Override
        public ActionBar.Tab setText(CharSequence charSequence) {
            this.mText = charSequence;
            if (this.mPosition >= 0) {
                WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }
    }

}

