/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.app.UiModeManager
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.media.AudioManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.AndroidRuntimeException
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.KeyCharacterMap
 *  android.view.KeyEvent
 *  android.view.KeyboardShortcutGroup
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory
 *  android.view.LayoutInflater$Factory2
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.Window
 *  android.view.Window$Callback
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.ListAdapter
 *  android.widget.PopupWindow
 *  android.widget.TextView
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.NavUtils;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatViewInflater;
import android.support.v7.app.ResourcesFlusher;
import android.support.v7.app.ToolbarActionBar;
import android.support.v7.app.TwilightManager;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.reflect.Constructor;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

class AppCompatDelegateImpl
extends AppCompatDelegate
implements MenuBuilder.Callback,
LayoutInflater.Factory2 {
    private static final boolean DEBUG = false;
    static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
    private static final boolean IS_PRE_LOLLIPOP;
    private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
    private static boolean sInstalledExceptionHandler;
    private static final int[] sWindowBackgroundStyleable;
    ActionBar mActionBar;
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    android.support.v7.view.ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    final AppCompatCallback mAppCompatCallback;
    private AppCompatViewInflater mAppCompatViewInflater;
    final Window.Callback mAppCompatWindowCallback;
    private boolean mApplyDayNightCalled;
    private AutoNightModeManager mAutoNightModeManager;
    private boolean mClosingActionMenu;
    final Context mContext;
    private DecorContentParent mDecorContentParent;
    private boolean mEnableDefaultActionBarUp;
    ViewPropertyAnimatorCompat mFadeAnim = null;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    private boolean mHandleNativeActionModes = true;
    boolean mHasActionBar;
    int mInvalidatePanelMenuFeatures;
    boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable;
    boolean mIsDestroyed;
    boolean mIsFloating;
    private int mLocalNightMode = -100;
    private boolean mLongPressBackDown;
    MenuInflater mMenuInflater;
    final Window.Callback mOriginalWindowCallback;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private View mStatusGuard;
    private ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private CharSequence mTitle;
    private TextView mTitleView;
    final Window mWindow;
    boolean mWindowNoTitle;

    static {
        boolean bl = Build.VERSION.SDK_INT < 21;
        IS_PRE_LOLLIPOP = bl;
        sWindowBackgroundStyleable = new int[]{16842836};
        if (IS_PRE_LOLLIPOP && !sInstalledExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()){
                final /* synthetic */ Thread.UncaughtExceptionHandler val$defHandler;

                private boolean shouldWrapException(Throwable object) {
                    if (object instanceof Resources.NotFoundException) {
                        if ((object = object.getMessage()) != null && (object.contains("drawable") || object.contains("Drawable"))) {
                            return true;
                        }
                        return false;
                    }
                    return false;
                }

                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    if (this.shouldWrapException(throwable)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(throwable.getMessage());
                        stringBuilder.append(". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                        stringBuilder = new Resources.NotFoundException(stringBuilder.toString());
                        stringBuilder.initCause(throwable.getCause());
                        stringBuilder.setStackTrace(throwable.getStackTrace());
                        this.val$defHandler.uncaughtException(thread, (Throwable)((Object)stringBuilder));
                        return;
                    }
                    this.val$defHandler.uncaughtException(thread, throwable);
                }
            });
            sInstalledExceptionHandler = true;
        }
    }

    AppCompatDelegateImpl(Context object, Window window, AppCompatCallback appCompatCallback) {
        this.mInvalidatePanelMenuRunnable = new Runnable(){

            @Override
            public void run() {
                if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 1) != 0) {
                    AppCompatDelegateImpl.this.doInvalidatePanelMenu(0);
                }
                if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 4096) != 0) {
                    AppCompatDelegateImpl.this.doInvalidatePanelMenu(108);
                }
                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                appCompatDelegateImpl.mInvalidatePanelMenuPosted = false;
                appCompatDelegateImpl.mInvalidatePanelMenuFeatures = 0;
            }
        };
        this.mContext = object;
        this.mWindow = window;
        this.mAppCompatCallback = appCompatCallback;
        this.mOriginalWindowCallback = this.mWindow.getCallback();
        window = this.mOriginalWindowCallback;
        if (!(window instanceof AppCompatWindowCallback)) {
            this.mAppCompatWindowCallback = new AppCompatWindowCallback((Window.Callback)window);
            this.mWindow.setCallback(this.mAppCompatWindowCallback);
            object = TintTypedArray.obtainStyledAttributes((Context)object, null, sWindowBackgroundStyleable);
            window = object.getDrawableIfKnown(0);
            if (window != null) {
                this.mWindow.setBackgroundDrawable((Drawable)window);
            }
            object.recycle();
            return;
        }
        throw new IllegalStateException("AppCompat has already installed itself into the Window");
    }

    private void applyFixedSizeWindow() {
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
        View view = this.mWindow.getDecorView();
        contentFrameLayout.setDecorPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        view = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        view.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        view.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        if (view.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
            view.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor());
        }
        if (view.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
            view.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor());
        }
        if (view.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
            view.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor());
        }
        if (view.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
            view.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor());
        }
        view.recycle();
        contentFrameLayout.requestLayout();
    }

    private ViewGroup createSubDecor() {
        Object object = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (object.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            if (object.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
                this.requestWindowFeature(1);
            } else if (object.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
                this.requestWindowFeature(108);
            }
            if (object.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
                this.requestWindowFeature(109);
            }
            if (object.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
                this.requestWindowFeature(10);
            }
            this.mIsFloating = object.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
            object.recycle();
            this.mWindow.getDecorView();
            Object object2 = LayoutInflater.from((Context)this.mContext);
            object = null;
            if (!this.mWindowNoTitle) {
                if (this.mIsFloating) {
                    object = (ViewGroup)object2.inflate(R.layout.abc_dialog_title_material, null);
                    this.mOverlayActionBar = false;
                    this.mHasActionBar = false;
                } else if (this.mHasActionBar) {
                    object = new TypedValue();
                    this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, (TypedValue)object, true);
                    object = object.resourceId != 0 ? new ContextThemeWrapper(this.mContext, object.resourceId) : this.mContext;
                    object = (ViewGroup)LayoutInflater.from((Context)object).inflate(R.layout.abc_screen_toolbar, null);
                    this.mDecorContentParent = (DecorContentParent)object.findViewById(R.id.decor_content_parent);
                    this.mDecorContentParent.setWindowCallback(this.getWindowCallback());
                    if (this.mOverlayActionBar) {
                        this.mDecorContentParent.initFeature(109);
                    }
                    if (this.mFeatureProgress) {
                        this.mDecorContentParent.initFeature(2);
                    }
                    if (this.mFeatureIndeterminateProgress) {
                        this.mDecorContentParent.initFeature(5);
                    }
                }
            } else {
                object = this.mOverlayActionMode ? (ViewGroup)object2.inflate(R.layout.abc_screen_simple_overlay_action_mode, null) : (ViewGroup)object2.inflate(R.layout.abc_screen_simple, null);
                if (Build.VERSION.SDK_INT >= 21) {
                    ViewCompat.setOnApplyWindowInsetsListener((View)object, new OnApplyWindowInsetsListener(){

                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                            int n = windowInsetsCompat.getSystemWindowInsetTop();
                            int n2 = AppCompatDelegateImpl.this.updateStatusGuard(n);
                            WindowInsetsCompat windowInsetsCompat2 = windowInsetsCompat;
                            if (n != n2) {
                                windowInsetsCompat2 = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), n2, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                            }
                            return ViewCompat.onApplyWindowInsets(view, windowInsetsCompat2);
                        }
                    });
                } else {
                    ((FitWindowsViewGroup)object).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener(){

                        @Override
                        public void onFitSystemWindows(Rect rect) {
                            rect.top = AppCompatDelegateImpl.this.updateStatusGuard(rect.top);
                        }
                    });
                }
            }
            if (object != null) {
                if (this.mDecorContentParent == null) {
                    this.mTitleView = (TextView)object.findViewById(R.id.title);
                }
                ViewUtils.makeOptionalFitsSystemWindows((View)object);
                object2 = (ContentFrameLayout)object.findViewById(R.id.action_bar_activity_content);
                ViewGroup viewGroup = (ViewGroup)this.mWindow.findViewById(16908290);
                if (viewGroup != null) {
                    while (viewGroup.getChildCount() > 0) {
                        View view = viewGroup.getChildAt(0);
                        viewGroup.removeViewAt(0);
                        object2.addView(view);
                    }
                    viewGroup.setId(-1);
                    object2.setId(16908290);
                    if (viewGroup instanceof FrameLayout) {
                        ((FrameLayout)viewGroup).setForeground(null);
                    }
                }
                this.mWindow.setContentView((View)object);
                object2.setAttachListener(new ContentFrameLayout.OnAttachListener(){

                    @Override
                    public void onAttachedFromWindow() {
                    }

                    @Override
                    public void onDetachedFromWindow() {
                        AppCompatDelegateImpl.this.dismissPopups();
                    }
                });
                return object;
            }
            object = new StringBuilder();
            object.append("AppCompat does not support the current theme features: { windowActionBar: ");
            object.append(this.mHasActionBar);
            object.append(", windowActionBarOverlay: ");
            object.append(this.mOverlayActionBar);
            object.append(", android:windowIsFloating: ");
            object.append(this.mIsFloating);
            object.append(", windowActionModeOverlay: ");
            object.append(this.mOverlayActionMode);
            object.append(", windowNoTitle: ");
            object.append(this.mWindowNoTitle);
            object.append(" }");
            throw new IllegalArgumentException(object.toString());
        }
        object.recycle();
        object = new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        throw object;
    }

    private void ensureAutoNightModeManager() {
        if (this.mAutoNightModeManager == null) {
            this.mAutoNightModeManager = new AutoNightModeManager(TwilightManager.getInstance(this.mContext));
        }
    }

    private void ensureSubDecor() {
        if (!this.mSubDecorInstalled) {
            this.mSubDecor = this.createSubDecor();
            Object object = this.getTitle();
            if (!TextUtils.isEmpty((CharSequence)object)) {
                DecorContentParent decorContentParent = this.mDecorContentParent;
                if (decorContentParent != null) {
                    decorContentParent.setWindowTitle((CharSequence)object);
                } else if (this.peekSupportActionBar() != null) {
                    this.peekSupportActionBar().setWindowTitle((CharSequence)object);
                } else {
                    decorContentParent = this.mTitleView;
                    if (decorContentParent != null) {
                        decorContentParent.setText((CharSequence)object);
                    }
                }
            }
            this.applyFixedSizeWindow();
            this.onSubDecorInstalled(this.mSubDecor);
            this.mSubDecorInstalled = true;
            object = this.getPanelState(0, false);
            if (!(this.mIsDestroyed || object != null && object.menu != null)) {
                this.invalidatePanelMenu(108);
            }
        }
    }

    private int getNightMode() {
        int n = this.mLocalNightMode;
        if (n != -100) {
            return n;
        }
        return AppCompatDelegateImpl.getDefaultNightMode();
    }

    private void initWindowDecorActionBar() {
        this.ensureSubDecor();
        if (this.mHasActionBar) {
            if (this.mActionBar != null) {
                return;
            }
            Object object = this.mOriginalWindowCallback;
            if (object instanceof Activity) {
                this.mActionBar = new WindowDecorActionBar((Activity)object, this.mOverlayActionBar);
            } else if (object instanceof Dialog) {
                this.mActionBar = new WindowDecorActionBar((Dialog)object);
            }
            object = this.mActionBar;
            if (object != null) {
                object.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }
            return;
        }
    }

    private boolean initializePanelContent(PanelFeatureState panelFeatureState) {
        if (panelFeatureState.createdPanelView != null) {
            panelFeatureState.shownPanelView = panelFeatureState.createdPanelView;
            return true;
        }
        if (panelFeatureState.menu == null) {
            return false;
        }
        if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback();
        }
        panelFeatureState.shownPanelView = (View)panelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
        if (panelFeatureState.shownPanelView != null) {
            return true;
        }
        return false;
    }

    private boolean initializePanelDecor(PanelFeatureState panelFeatureState) {
        panelFeatureState.setStyle(this.getActionBarThemedContext());
        panelFeatureState.decorView = new ListMenuDecorView(panelFeatureState.listPresenterContext);
        panelFeatureState.gravity = 81;
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean initializePanelMenu(PanelFeatureState var1_1) {
        var4_2 = this.mContext;
        if (var1_1.featureId == 0) ** GOTO lbl-1000
        var2_3 = var4_2;
        if (var1_1.featureId == 108) lbl-1000: // 2 sources:
        {
            var2_3 = var4_2;
            if (this.mDecorContentParent != null) {
                var5_4 = new TypedValue();
                var6_5 = var4_2.getTheme();
                var6_5.resolveAttribute(R.attr.actionBarTheme, var5_4, true);
                var2_3 = null;
                if (var5_4.resourceId != 0) {
                    var2_3 = var4_2.getResources().newTheme();
                    var2_3.setTo(var6_5);
                    var2_3.applyStyle(var5_4.resourceId, true);
                    var2_3.resolveAttribute(R.attr.actionBarWidgetTheme, var5_4, true);
                } else {
                    var6_5.resolveAttribute(R.attr.actionBarWidgetTheme, var5_4, true);
                }
                var3_6 = var2_3;
                if (var5_4.resourceId != 0) {
                    var3_6 = var2_3;
                    if (var2_3 == null) {
                        var3_6 = var4_2.getResources().newTheme();
                        var3_6.setTo(var6_5);
                    }
                    var3_6.applyStyle(var5_4.resourceId, true);
                }
                var2_3 = var4_2;
                if (var3_6 != null) {
                    var2_3 = new ContextThemeWrapper(var4_2, 0);
                    var2_3.getTheme().setTo((Resources.Theme)var3_6);
                }
            }
        }
        var2_3 = new MenuBuilder((Context)var2_3);
        var2_3.setCallback(this);
        var1_1.setMenu((MenuBuilder)var2_3);
        return true;
    }

    private void invalidatePanelMenu(int n) {
        this.mInvalidatePanelMenuFeatures |= 1 << n;
        if (!this.mInvalidatePanelMenuPosted) {
            ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuPosted = true;
        }
    }

    private boolean onKeyDownPanel(int n, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() == 0) {
            PanelFeatureState panelFeatureState = this.getPanelState(n, true);
            if (!panelFeatureState.isOpen) {
                return this.preparePanel(panelFeatureState, keyEvent);
            }
        }
        return false;
    }

    private boolean onKeyUpPanel(int n, KeyEvent keyEvent) {
        boolean bl;
        DecorContentParent decorContentParent;
        if (this.mActionMode != null) {
            return false;
        }
        boolean bl2 = false;
        PanelFeatureState panelFeatureState = this.getPanelState(n, true);
        if (n == 0 && (decorContentParent = this.mDecorContentParent) != null && decorContentParent.canShowOverflowMenu() && !ViewConfiguration.get((Context)this.mContext).hasPermanentMenuKey()) {
            if (!this.mDecorContentParent.isOverflowMenuShowing()) {
                bl = bl2;
                if (!this.mIsDestroyed) {
                    bl = bl2;
                    if (this.preparePanel(panelFeatureState, keyEvent)) {
                        bl = this.mDecorContentParent.showOverflowMenu();
                    }
                }
            } else {
                bl = this.mDecorContentParent.hideOverflowMenu();
            }
        } else if (!panelFeatureState.isOpen && !panelFeatureState.isHandled) {
            bl = bl2;
            if (panelFeatureState.isPrepared) {
                boolean bl3 = true;
                if (panelFeatureState.refreshMenuContent) {
                    panelFeatureState.isPrepared = false;
                    bl3 = this.preparePanel(panelFeatureState, keyEvent);
                }
                bl = bl2;
                if (bl3) {
                    this.openPanel(panelFeatureState, keyEvent);
                    bl = true;
                }
            }
        } else {
            bl = panelFeatureState.isOpen;
            this.closePanel(panelFeatureState, true);
        }
        if (bl) {
            keyEvent = (AudioManager)this.mContext.getSystemService("audio");
            if (keyEvent != null) {
                keyEvent.playSoundEffect(0);
                return bl;
            }
            Log.w((String)"AppCompatDelegate", (String)"Couldn't get audio manager");
        }
        return bl;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void openPanel(PanelFeatureState var1_1, KeyEvent var2_2) {
        if (var1_1.isOpen != false) return;
        if (this.mIsDestroyed) {
            return;
        }
        if (var1_1.featureId == 0) {
            if ((this.mContext.getResources().getConfiguration().screenLayout & 15) == 4) {
                return;
            }
            var3_3 = 0;
            if (var3_3 != 0) {
                return;
            }
        }
        if ((var5_4 = this.getWindowCallback()) != null && !var5_4.onMenuOpened(var1_1.featureId, (Menu)var1_1.menu)) {
            this.closePanel(var1_1, true);
            return;
        }
        var6_5 = (WindowManager)this.mContext.getSystemService("window");
        if (var6_5 == null) {
            return;
        }
        if (!this.preparePanel(var1_1, var2_2)) {
            return;
        }
        var4_6 = -2;
        if (var1_1.decorView == null || var1_1.refreshDecorView) ** GOTO lbl28
        if (var1_1.createdPanelView == null) ** GOTO lbl50
        var2_2 = var1_1.createdPanelView.getLayoutParams();
        var3_3 = var4_6;
        if (var2_2 != null) {
            var3_3 = var4_6;
            if (var2_2.width == -1) {
                var3_3 = -1;
            }
        }
        ** GOTO lbl51
lbl28: // 1 sources:
        if (var1_1.decorView == null) {
            if (this.initializePanelDecor(var1_1) == false) return;
            if (var1_1.decorView == null) {
                return;
            }
        } else if (var1_1.refreshDecorView && var1_1.decorView.getChildCount() > 0) {
            var1_1.decorView.removeAllViews();
        }
        if (this.initializePanelContent(var1_1) == false) return;
        if (!var1_1.hasPanelItems()) {
            return;
        }
        var5_4 = var1_1.shownPanelView.getLayoutParams();
        var2_2 = var5_4;
        if (var5_4 == null) {
            var2_2 = new ViewGroup.LayoutParams(-2, -2);
        }
        var3_3 = var1_1.background;
        var1_1.decorView.setBackgroundResource(var3_3);
        var5_4 = var1_1.shownPanelView.getParent();
        if (var5_4 != null && var5_4 instanceof ViewGroup) {
            ((ViewGroup)var5_4).removeView(var1_1.shownPanelView);
        }
        var1_1.decorView.addView(var1_1.shownPanelView, (ViewGroup.LayoutParams)var2_2);
        if (!var1_1.shownPanelView.hasFocus()) {
            var1_1.shownPanelView.requestFocus();
        }
lbl50: // 4 sources:
        var3_3 = var4_6;
lbl51: // 2 sources:
        var1_1.isHandled = false;
        var2_2 = new WindowManager.LayoutParams(var3_3, -2, var1_1.x, var1_1.y, 1002, 8519680, -3);
        var2_2.gravity = var1_1.gravity;
        var2_2.windowAnimations = var1_1.windowAnimations;
        var6_5.addView((View)var1_1.decorView, (ViewGroup.LayoutParams)var2_2);
        var1_1.isOpen = true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean performPanelShortcut(PanelFeatureState var1_1, int var2_2, KeyEvent var3_3, int var4_4) {
        if (var3_3.isSystem()) {
            return false;
        }
        var6_5 = false;
        if (var1_1.isPrepared) ** GOTO lbl-1000
        var5_6 = var6_5;
        if (this.preparePanel(var1_1, var3_3)) lbl-1000: // 2 sources:
        {
            var5_6 = var6_5;
            if (var1_1.menu != null) {
                var5_6 = var1_1.menu.performShortcut(var2_2, var3_3, var4_4);
            }
        }
        if (var5_6 == false) return var5_6;
        if ((var4_4 & 1) != 0) return var5_6;
        if (this.mDecorContentParent != null) return var5_6;
        this.closePanel(var1_1, true);
        return var5_6;
    }

    private boolean preparePanel(PanelFeatureState object, KeyEvent object2) {
        DecorContentParent decorContentParent;
        if (this.mIsDestroyed) {
            return false;
        }
        if (object.isPrepared) {
            return true;
        }
        PanelFeatureState panelFeatureState = this.mPreparedPanel;
        if (panelFeatureState != null && panelFeatureState != object) {
            this.closePanel(panelFeatureState, false);
        }
        if ((panelFeatureState = this.getWindowCallback()) != null) {
            object.createdPanelView = panelFeatureState.onCreatePanelView(object.featureId);
        }
        int n = object.featureId != 0 && object.featureId != 108 ? 0 : 1;
        if (n != 0 && (decorContentParent = this.mDecorContentParent) != null) {
            decorContentParent.setMenuPrepared();
        }
        if (!(object.createdPanelView != null || n != 0 && this.peekSupportActionBar() instanceof ToolbarActionBar)) {
            if (object.menu == null || object.refreshMenuContent) {
                if (!(object.menu != null || this.initializePanelMenu((PanelFeatureState)object) && object.menu != null)) {
                    return false;
                }
                if (n != 0 && this.mDecorContentParent != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                    }
                    this.mDecorContentParent.setMenu(object.menu, this.mActionMenuPresenterCallback);
                }
                object.menu.stopDispatchingItemsChanged();
                if (!panelFeatureState.onCreatePanelMenu(object.featureId, (Menu)object.menu)) {
                    object.setMenu(null);
                    if (n != 0 && (object = this.mDecorContentParent) != null) {
                        object.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                    return false;
                }
                object.refreshMenuContent = false;
            }
            object.menu.stopDispatchingItemsChanged();
            if (object.frozenActionViewState != null) {
                object.menu.restoreActionViewStates(object.frozenActionViewState);
                object.frozenActionViewState = null;
            }
            if (!panelFeatureState.onPreparePanel(0, object.createdPanelView, (Menu)object.menu)) {
                if (n != 0 && (object2 = this.mDecorContentParent) != null) {
                    object2.setMenu(null, this.mActionMenuPresenterCallback);
                }
                object.menu.startDispatchingItemsChanged();
                return false;
            }
            n = object2 != null ? object2.getDeviceId() : -1;
            boolean bl = KeyCharacterMap.load((int)n).getKeyboardType() != 1;
            object.qwertyMode = bl;
            object.menu.setQwertyMode(object.qwertyMode);
            object.menu.startDispatchingItemsChanged();
        }
        object.isPrepared = true;
        object.isHandled = false;
        this.mPreparedPanel = object;
        return true;
    }

    private void reopenMenu(MenuBuilder object, boolean bl) {
        object = this.mDecorContentParent;
        if (object != null && object.canShowOverflowMenu() && (!ViewConfiguration.get((Context)this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
            object = this.getWindowCallback();
            if (this.mDecorContentParent.isOverflowMenuShowing() && bl) {
                this.mDecorContentParent.hideOverflowMenu();
                if (!this.mIsDestroyed) {
                    object.onPanelClosed(108, (Menu)this.getPanelState((int)0, (boolean)true).menu);
                    return;
                }
            } else if (object != null && !this.mIsDestroyed) {
                if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
                    this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
                    this.mInvalidatePanelMenuRunnable.run();
                }
                PanelFeatureState panelFeatureState = this.getPanelState(0, true);
                if (panelFeatureState.menu != null && !panelFeatureState.refreshMenuContent && object.onPreparePanel(0, panelFeatureState.createdPanelView, (Menu)panelFeatureState.menu)) {
                    object.onMenuOpened(108, (Menu)panelFeatureState.menu);
                    this.mDecorContentParent.showOverflowMenu();
                }
            }
            return;
        }
        object = this.getPanelState(0, true);
        object.refreshDecorView = true;
        this.closePanel((PanelFeatureState)object, false);
        this.openPanel((PanelFeatureState)object, null);
    }

    private int sanitizeWindowFeatureId(int n) {
        if (n == 8) {
            Log.i((String)"AppCompatDelegate", (String)"You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        }
        if (n == 9) {
            Log.i((String)"AppCompatDelegate", (String)"You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            return 109;
        }
        return n;
    }

    private boolean shouldInheritContext(ViewParent viewParent) {
        if (viewParent == null) {
            return false;
        }
        View view = this.mWindow.getDecorView();
        do {
            if (viewParent == null) {
                return true;
            }
            if (viewParent == view || !(viewParent instanceof View)) break;
            if (ViewCompat.isAttachedToWindow((View)viewParent)) {
                return false;
            }
            viewParent = viewParent.getParent();
        } while (true);
        return false;
    }

    private boolean shouldRecreateOnNightModeChange() {
        Context context;
        boolean bl = this.mApplyDayNightCalled;
        boolean bl2 = false;
        if (bl && (context = this.mContext) instanceof Activity) {
            context = context.getPackageManager();
            try {
                int n = context.getActivityInfo((ComponentName)new ComponentName((Context)this.mContext, this.mContext.getClass()), (int)0).configChanges;
                if ((n & 512) == 0) {
                    bl2 = true;
                }
                return bl2;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Log.d((String)"AppCompatDelegate", (String)"Exception while getting ActivityInfo", (Throwable)nameNotFoundException);
                return true;
            }
        }
        return false;
    }

    private void throwFeatureRequestIfSubDecorInstalled() {
        if (!this.mSubDecorInstalled) {
            return;
        }
        throw new AndroidRuntimeException("Window feature must be requested before adding content");
    }

    private boolean updateForNightMode(int n) {
        Resources resources = this.mContext.getResources();
        Configuration configuration = resources.getConfiguration();
        int n2 = configuration.uiMode;
        n = n == 2 ? 32 : 16;
        if ((n2 & 48) != n) {
            if (this.shouldRecreateOnNightModeChange()) {
                ((Activity)this.mContext).recreate();
            } else {
                configuration = new Configuration(configuration);
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                configuration.uiMode = configuration.uiMode & -49 | n;
                resources.updateConfiguration(configuration, displayMetrics);
                if (Build.VERSION.SDK_INT < 26) {
                    ResourcesFlusher.flush(resources);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.ensureSubDecor();
        ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(view, layoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override
    public boolean applyDayNight() {
        boolean bl = false;
        int n = this.getNightMode();
        int n2 = this.mapNightMode(n);
        if (n2 != -1) {
            bl = this.updateForNightMode(n2);
        }
        if (n == 0) {
            this.ensureAutoNightModeManager();
            this.mAutoNightModeManager.setup();
        }
        this.mApplyDayNightCalled = true;
        return bl;
    }

    void callOnPanelClosed(int n, PanelFeatureState arrpanelFeatureState, Menu menu) {
        PanelFeatureState[] arrpanelFeatureState2 = arrpanelFeatureState;
        Menu menu2 = menu;
        if (menu == null) {
            Object object = arrpanelFeatureState;
            if (arrpanelFeatureState == null) {
                object = arrpanelFeatureState;
                if (n >= 0) {
                    arrpanelFeatureState2 = this.mPanels;
                    object = arrpanelFeatureState;
                    if (n < arrpanelFeatureState2.length) {
                        object = arrpanelFeatureState2[n];
                    }
                }
            }
            arrpanelFeatureState2 = object;
            menu2 = menu;
            if (object != null) {
                menu2 = object.menu;
                arrpanelFeatureState2 = object;
            }
        }
        if (arrpanelFeatureState2 != null && !arrpanelFeatureState2.isOpen) {
            return;
        }
        if (!this.mIsDestroyed) {
            this.mOriginalWindowCallback.onPanelClosed(n, menu2);
        }
    }

    void checkCloseActionMenu(MenuBuilder menuBuilder) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mDecorContentParent.dismissPopups();
        Window.Callback callback = this.getWindowCallback();
        if (callback != null && !this.mIsDestroyed) {
            callback.onPanelClosed(108, (Menu)menuBuilder);
        }
        this.mClosingActionMenu = false;
    }

    void closePanel(int n) {
        this.closePanel(this.getPanelState(n, true), true);
    }

    void closePanel(PanelFeatureState panelFeatureState, boolean bl) {
        DecorContentParent decorContentParent;
        if (bl && panelFeatureState.featureId == 0 && (decorContentParent = this.mDecorContentParent) != null && decorContentParent.isOverflowMenuShowing()) {
            this.checkCloseActionMenu(panelFeatureState.menu);
            return;
        }
        decorContentParent = (WindowManager)this.mContext.getSystemService("window");
        if (decorContentParent != null && panelFeatureState.isOpen && panelFeatureState.decorView != null) {
            decorContentParent.removeView((View)panelFeatureState.decorView);
            if (bl) {
                this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
            }
        }
        panelFeatureState.isPrepared = false;
        panelFeatureState.isHandled = false;
        panelFeatureState.isOpen = false;
        panelFeatureState.shownPanelView = null;
        panelFeatureState.refreshDecorView = true;
        if (this.mPreparedPanel == panelFeatureState) {
            this.mPreparedPanel = null;
        }
    }

    @Override
    public View createView(View view, String string2, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        Object object = this.mAppCompatViewInflater;
        boolean bl = false;
        if (object == null) {
            object = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme).getString(R.styleable.AppCompatTheme_viewInflaterClass);
            if (object != null && !AppCompatViewInflater.class.getName().equals(object)) {
                try {
                    this.mAppCompatViewInflater = (AppCompatViewInflater)Class.forName((String)object).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                }
                catch (Throwable throwable) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to instantiate custom view inflater ");
                    stringBuilder.append((String)object);
                    stringBuilder.append(". Falling back to default.");
                    Log.i((String)"AppCompatDelegate", (String)stringBuilder.toString(), (Throwable)throwable);
                    this.mAppCompatViewInflater = new AppCompatViewInflater();
                }
            } else {
                this.mAppCompatViewInflater = new AppCompatViewInflater();
            }
        }
        boolean bl2 = false;
        if (IS_PRE_LOLLIPOP) {
            if (attributeSet instanceof XmlPullParser) {
                bl2 = bl;
                if (((XmlPullParser)attributeSet).getDepth() > 1) {
                    bl2 = true;
                }
            } else {
                bl2 = this.shouldInheritContext((ViewParent)view);
            }
        }
        return this.mAppCompatViewInflater.createView(view, string2, context, attributeSet, bl2, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
    }

    void dismissPopups() {
        Object object = this.mDecorContentParent;
        if (object != null) {
            object.dismissPopups();
        }
        if (this.mActionModePopup != null) {
            this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
            if (this.mActionModePopup.isShowing()) {
                try {
                    this.mActionModePopup.dismiss();
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    // empty catch block
                }
            }
            this.mActionModePopup = null;
        }
        this.endOnGoingFadeAnimation();
        object = this.getPanelState(0, false);
        if (object != null && object.menu != null) {
            object.menu.close();
        }
    }

    boolean dispatchKeyEvent(KeyEvent keyEvent) {
        Window.Callback callback = this.mOriginalWindowCallback;
        boolean bl = callback instanceof KeyEventDispatcher.Component;
        boolean bl2 = true;
        if ((bl || callback instanceof AppCompatDialog) && (callback = this.mWindow.getDecorView()) != null && KeyEventDispatcher.dispatchBeforeHierarchy((View)callback, keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 82 && this.mOriginalWindowCallback.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        int n = keyEvent.getKeyCode();
        if (keyEvent.getAction() != 0) {
            bl2 = false;
        }
        if (bl2) {
            return this.onKeyDown(n, keyEvent);
        }
        return this.onKeyUp(n, keyEvent);
    }

    void doInvalidatePanelMenu(int n) {
        PanelFeatureState panelFeatureState = this.getPanelState(n, true);
        if (panelFeatureState.menu != null) {
            Bundle bundle = new Bundle();
            panelFeatureState.menu.saveActionViewStates(bundle);
            if (bundle.size() > 0) {
                panelFeatureState.frozenActionViewState = bundle;
            }
            panelFeatureState.menu.stopDispatchingItemsChanged();
            panelFeatureState.menu.clear();
        }
        panelFeatureState.refreshMenuContent = true;
        panelFeatureState.refreshDecorView = true;
        if ((n == 108 || n == 0) && this.mDecorContentParent != null && (panelFeatureState = this.getPanelState(0, false)) != null) {
            panelFeatureState.isPrepared = false;
            this.preparePanel(panelFeatureState, null);
        }
    }

    void endOnGoingFadeAnimation() {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mFadeAnim;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.cancel();
        }
    }

    PanelFeatureState findMenuPanel(Menu menu) {
        PanelFeatureState[] arrpanelFeatureState = this.mPanels;
        int n = arrpanelFeatureState != null ? arrpanelFeatureState.length : 0;
        for (int i = 0; i < n; ++i) {
            PanelFeatureState panelFeatureState = arrpanelFeatureState[i];
            if (panelFeatureState == null || panelFeatureState.menu != menu) continue;
            return panelFeatureState;
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends View> T findViewById(@IdRes int n) {
        this.ensureSubDecor();
        return (T)this.mWindow.findViewById(n);
    }

    final Context getActionBarThemedContext() {
        Context context = null;
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            context = actionBar.getThemedContext();
        }
        actionBar = context;
        if (context == null) {
            actionBar = this.mContext;
        }
        return actionBar;
    }

    @VisibleForTesting
    final AutoNightModeManager getAutoNightModeManager() {
        this.ensureAutoNightModeManager();
        return this.mAutoNightModeManager;
    }

    @Override
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.initWindowDecorActionBar();
            ActionBar actionBar = this.mActionBar;
            actionBar = actionBar != null ? actionBar.getThemedContext() : this.mContext;
            this.mMenuInflater = new SupportMenuInflater((Context)actionBar);
        }
        return this.mMenuInflater;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected PanelFeatureState getPanelState(int var1_1, boolean var2_2) {
        var3_3 = this.mPanels;
        var4_4 = var3_3;
        if (var3_3 == null) ** GOTO lbl-1000
        var3_3 = var4_4;
        if (var4_4.length <= var1_1) lbl-1000: // 2 sources:
        {
            var5_5 = new PanelFeatureState[var1_1 + 1];
            if (var4_4 != null) {
                System.arraycopy(var4_4, 0, var5_5, 0, var4_4.length);
            }
            var3_3 = var5_5;
            this.mPanels = var5_5;
        }
        var4_4 = var5_5 = var3_3[var1_1];
        if (var5_5 != null) return var4_4;
        var4_4 = var5_5 = new PanelFeatureState(var1_1);
        var3_3[var1_1] = var5_5;
        return var4_4;
    }

    ViewGroup getSubDecor() {
        return this.mSubDecor;
    }

    @Override
    public ActionBar getSupportActionBar() {
        this.initWindowDecorActionBar();
        return this.mActionBar;
    }

    final CharSequence getTitle() {
        Window.Callback callback = this.mOriginalWindowCallback;
        if (callback instanceof Activity) {
            return ((Activity)callback).getTitle();
        }
        return this.mTitle;
    }

    final Window.Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }

    @Override
    public boolean hasWindowFeature(int n) {
        boolean bl = false;
        int n2 = this.sanitizeWindowFeatureId(n);
        boolean bl2 = true;
        if (n2 != 1) {
            if (n2 != 2) {
                if (n2 != 5) {
                    if (n2 != 10) {
                        if (n2 != 108) {
                            if (n2 == 109) {
                                bl = this.mOverlayActionBar;
                            }
                        } else {
                            bl = this.mHasActionBar;
                        }
                    } else {
                        bl = this.mOverlayActionMode;
                    }
                } else {
                    bl = this.mFeatureIndeterminateProgress;
                }
            } else {
                bl = this.mFeatureProgress;
            }
        } else {
            bl = this.mWindowNoTitle;
        }
        if (!bl) {
            if (this.mWindow.hasFeature(n)) {
                return true;
            }
            bl2 = false;
        }
        return bl2;
    }

    @Override
    public void installViewFactory() {
        LayoutInflater layoutInflater = LayoutInflater.from((Context)this.mContext);
        if (layoutInflater.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(layoutInflater, this);
            return;
        }
        if (!(layoutInflater.getFactory2() instanceof AppCompatDelegateImpl)) {
            Log.i((String)"AppCompatDelegate", (String)"The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    @Override
    public void invalidateOptionsMenu() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null && actionBar.invalidateOptionsMenu()) {
            return;
        }
        this.invalidatePanelMenu(0);
    }

    @Override
    public boolean isHandleNativeActionModesEnabled() {
        return this.mHandleNativeActionModes;
    }

    int mapNightMode(int n) {
        if (n != -100) {
            if (n != 0) {
                return n;
            }
            if (Build.VERSION.SDK_INT >= 23 && ((UiModeManager)this.mContext.getSystemService(UiModeManager.class)).getNightMode() == 0) {
                return -1;
            }
            this.ensureAutoNightModeManager();
            return this.mAutoNightModeManager.getApplyableNightMode();
        }
        return -1;
    }

    boolean onBackPressed() {
        Object object = this.mActionMode;
        if (object != null) {
            object.finish();
            return true;
        }
        object = this.getSupportActionBar();
        if (object != null && object.collapseActionView()) {
            return true;
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        ActionBar actionBar;
        if (this.mHasActionBar && this.mSubDecorInstalled && (actionBar = this.getSupportActionBar()) != null) {
            actionBar.onConfigurationChanged(configuration);
        }
        AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
        this.applyDayNight();
    }

    @Override
    public void onCreate(Bundle bundle) {
        Object object = this.mOriginalWindowCallback;
        if (object instanceof Activity) {
            Object object2 = null;
            try {
                object2 = object = NavUtils.getParentActivityName((Activity)object);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
            if (object2 != null) {
                object2 = this.peekSupportActionBar();
                if (object2 == null) {
                    this.mEnableDefaultActionBarUp = true;
                } else {
                    object2.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
        }
        if (bundle != null && this.mLocalNightMode == -100) {
            this.mLocalNightMode = bundle.getInt("appcompat:local_night_mode", -100);
        }
    }

    public final View onCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
        return this.createView(view, string2, context, attributeSet);
    }

    public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
        return this.onCreateView(null, string2, context, attributeSet);
    }

    @Override
    public void onDestroy() {
        if (this.mInvalidatePanelMenuPosted) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
        }
        this.mIsDestroyed = true;
        Object object = this.mActionBar;
        if (object != null) {
            object.onDestroy();
        }
        if ((object = this.mAutoNightModeManager) != null) {
            object.cleanup();
        }
    }

    boolean onKeyDown(int n, KeyEvent keyEvent) {
        boolean bl = true;
        if (n != 4) {
            if (n != 82) {
                return false;
            }
            this.onKeyDownPanel(0, keyEvent);
            return true;
        }
        if ((keyEvent.getFlags() & 128) == 0) {
            bl = false;
        }
        this.mLongPressBackDown = bl;
        return false;
    }

    boolean onKeyShortcut(int n, KeyEvent object) {
        Object object2 = this.getSupportActionBar();
        if (object2 != null && object2.onKeyShortcut(n, (KeyEvent)object)) {
            return true;
        }
        object2 = this.mPreparedPanel;
        if (object2 != null && this.performPanelShortcut((PanelFeatureState)object2, object.getKeyCode(), (KeyEvent)object, 1)) {
            object = this.mPreparedPanel;
            if (object != null) {
                object.isHandled = true;
            }
            return true;
        }
        if (this.mPreparedPanel == null) {
            object2 = this.getPanelState(0, true);
            this.preparePanel((PanelFeatureState)object2, (KeyEvent)object);
            boolean bl = this.performPanelShortcut((PanelFeatureState)object2, object.getKeyCode(), (KeyEvent)object, 1);
            object2.isPrepared = false;
            if (bl) {
                return true;
            }
        }
        return false;
    }

    boolean onKeyUp(int n, KeyEvent object) {
        if (n != 4) {
            if (n != 82) {
                return false;
            }
            this.onKeyUpPanel(0, (KeyEvent)object);
            return true;
        }
        boolean bl = this.mLongPressBackDown;
        this.mLongPressBackDown = false;
        object = this.getPanelState(0, false);
        if (object != null && object.isOpen) {
            if (!bl) {
                this.closePanel((PanelFeatureState)object, true);
            }
            return true;
        }
        if (this.onBackPressed()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemSelected(MenuBuilder object, MenuItem menuItem) {
        Window.Callback callback = this.getWindowCallback();
        if (callback != null && !this.mIsDestroyed && (object = this.findMenuPanel(object.getRootMenu())) != null) {
            return callback.onMenuItemSelected(object.featureId, menuItem);
        }
        return false;
    }

    @Override
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        this.reopenMenu(menuBuilder, true);
    }

    void onMenuOpened(int n) {
        ActionBar actionBar;
        if (n == 108 && (actionBar = this.getSupportActionBar()) != null) {
            actionBar.dispatchMenuVisibilityChanged(true);
        }
    }

    void onPanelClosed(int n) {
        if (n == 108) {
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.dispatchMenuVisibilityChanged(false);
            }
        } else if (n == 0) {
            PanelFeatureState panelFeatureState = this.getPanelState(n, true);
            if (panelFeatureState.isOpen) {
                this.closePanel(panelFeatureState, false);
                return;
            }
        }
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        this.ensureSubDecor();
    }

    @Override
    public void onPostResume() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        int n = this.mLocalNightMode;
        if (n != -100) {
            bundle.putInt("appcompat:local_night_mode", n);
        }
    }

    @Override
    public void onStart() {
        this.applyDayNight();
    }

    @Override
    public void onStop() {
        Object object = this.getSupportActionBar();
        if (object != null) {
            object.setShowHideAnimationEnabled(false);
        }
        if ((object = this.mAutoNightModeManager) != null) {
            object.cleanup();
        }
    }

    void onSubDecorInstalled(ViewGroup viewGroup) {
    }

    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }

    @Override
    public boolean requestWindowFeature(int n) {
        n = this.sanitizeWindowFeatureId(n);
        if (this.mWindowNoTitle && n == 108) {
            return false;
        }
        if (this.mHasActionBar && n == 1) {
            this.mHasActionBar = false;
        }
        if (n != 1) {
            if (n != 2) {
                if (n != 5) {
                    if (n != 10) {
                        if (n != 108) {
                            if (n != 109) {
                                return this.mWindow.requestFeature(n);
                            }
                            this.throwFeatureRequestIfSubDecorInstalled();
                            this.mOverlayActionBar = true;
                            return true;
                        }
                        this.throwFeatureRequestIfSubDecorInstalled();
                        this.mHasActionBar = true;
                        return true;
                    }
                    this.throwFeatureRequestIfSubDecorInstalled();
                    this.mOverlayActionMode = true;
                    return true;
                }
                this.throwFeatureRequestIfSubDecorInstalled();
                this.mFeatureIndeterminateProgress = true;
                return true;
            }
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureProgress = true;
            return true;
        }
        this.throwFeatureRequestIfSubDecorInstalled();
        this.mWindowNoTitle = true;
        return true;
    }

    @Override
    public void setContentView(int n) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        LayoutInflater.from((Context)this.mContext).inflate(n, viewGroup);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override
    public void setHandleNativeActionModesEnabled(boolean bl) {
        this.mHandleNativeActionModes = bl;
    }

    @Override
    public void setLocalNightMode(int n) {
        if (n != -1 && n != 0 && n != 1 && n != 2) {
            Log.i((String)"AppCompatDelegate", (String)"setLocalNightMode() called with an unknown mode");
            return;
        }
        if (this.mLocalNightMode != n) {
            this.mLocalNightMode = n;
            if (this.mApplyDayNightCalled) {
                this.applyDayNight();
            }
        }
    }

    @Override
    public void setSupportActionBar(Toolbar object) {
        if (!(this.mOriginalWindowCallback instanceof Activity)) {
            return;
        }
        ActionBar actionBar = this.getSupportActionBar();
        if (!(actionBar instanceof WindowDecorActionBar)) {
            this.mMenuInflater = null;
            if (actionBar != null) {
                actionBar.onDestroy();
            }
            if (object != null) {
                this.mActionBar = object = new ToolbarActionBar((Toolbar)((Object)object), ((Activity)this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
                this.mWindow.setCallback(object.getWrappedWindowCallback());
            } else {
                this.mActionBar = null;
                this.mWindow.setCallback(this.mAppCompatWindowCallback);
            }
            this.invalidateOptionsMenu();
            return;
        }
        throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
    }

    @Override
    public final void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.setWindowTitle(charSequence);
            return;
        }
        if (this.peekSupportActionBar() != null) {
            this.peekSupportActionBar().setWindowTitle(charSequence);
            return;
        }
        decorContentParent = this.mTitleView;
        if (decorContentParent != null) {
            decorContentParent.setText(charSequence);
        }
    }

    final boolean shouldAnimateActionModeView() {
        ViewGroup viewGroup;
        if (this.mSubDecorInstalled && (viewGroup = this.mSubDecor) != null && ViewCompat.isLaidOut((View)viewGroup)) {
            return true;
        }
        return false;
    }

    @Override
    public android.support.v7.view.ActionMode startSupportActionMode(@NonNull ActionMode.Callback callback) {
        if (callback != null) {
            Object object = this.mActionMode;
            if (object != null) {
                object.finish();
            }
            callback = new ActionModeCallbackWrapperV9(callback);
            object = this.getSupportActionBar();
            if (object != null) {
                AppCompatCallback appCompatCallback;
                this.mActionMode = object.startActionMode(callback);
                object = this.mActionMode;
                if (object != null && (appCompatCallback = this.mAppCompatCallback) != null) {
                    appCompatCallback.onSupportActionModeStarted((android.support.v7.view.ActionMode)object);
                }
            }
            if (this.mActionMode == null) {
                this.mActionMode = this.startSupportActionModeFromWindow(callback);
            }
            return this.mActionMode;
        }
        throw new IllegalArgumentException("ActionMode callback can not be null.");
    }

    android.support.v7.view.ActionMode startSupportActionModeFromWindow(@NonNull ActionMode.Callback object) {
        this.endOnGoingFadeAnimation();
        Object object2 = this.mActionMode;
        if (object2 != null) {
            object2.finish();
        }
        object2 = object;
        if (!(object instanceof ActionModeCallbackWrapperV9)) {
            object2 = new ActionModeCallbackWrapperV9((ActionMode.Callback)object);
        }
        Object object3 = null;
        AppCompatCallback appCompatCallback = this.mAppCompatCallback;
        object = object3;
        if (appCompatCallback != null) {
            object = object3;
            if (!this.mIsDestroyed) {
                try {
                    object = appCompatCallback.onWindowStartingSupportActionMode((ActionMode.Callback)object2);
                }
                catch (AbstractMethodError abstractMethodError) {
                    object = object3;
                }
            }
        }
        if (object != null) {
            this.mActionMode = object;
        } else {
            object = this.mActionModeView;
            boolean bl = true;
            if (object == null) {
                if (this.mIsFloating) {
                    object3 = new TypedValue();
                    object = this.mContext.getTheme();
                    object.resolveAttribute(R.attr.actionBarTheme, (TypedValue)object3, true);
                    if (object3.resourceId != 0) {
                        appCompatCallback = this.mContext.getResources().newTheme();
                        appCompatCallback.setTo((Resources.Theme)object);
                        appCompatCallback.applyStyle(object3.resourceId, true);
                        object = new ContextThemeWrapper(this.mContext, 0);
                        object.getTheme().setTo((Resources.Theme)appCompatCallback);
                    } else {
                        object = this.mContext;
                    }
                    this.mActionModeView = new ActionBarContextView((Context)object);
                    this.mActionModePopup = new PopupWindow((Context)object, null, R.attr.actionModePopupWindowStyle);
                    PopupWindowCompat.setWindowLayoutType(this.mActionModePopup, 2);
                    this.mActionModePopup.setContentView((View)this.mActionModeView);
                    this.mActionModePopup.setWidth(-1);
                    object.getTheme().resolveAttribute(R.attr.actionBarSize, (TypedValue)object3, true);
                    int n = TypedValue.complexToDimensionPixelSize((int)object3.data, (DisplayMetrics)object.getResources().getDisplayMetrics());
                    this.mActionModeView.setContentHeight(n);
                    this.mActionModePopup.setHeight(-2);
                    this.mShowActionModePopup = new Runnable(){

                        @Override
                        public void run() {
                            AppCompatDelegateImpl.this.mActionModePopup.showAtLocation((View)AppCompatDelegateImpl.this.mActionModeView, 55, 0, 0);
                            AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                            if (AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                                AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0f);
                                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                                appCompatDelegateImpl.mFadeAnim = ViewCompat.animate((View)appCompatDelegateImpl.mActionModeView).alpha(1.0f);
                                AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter(){

                                    @Override
                                    public void onAnimationEnd(View view) {
                                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                        AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                                        AppCompatDelegateImpl.this.mFadeAnim = null;
                                    }

                                    @Override
                                    public void onAnimationStart(View view) {
                                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                    }
                                });
                                return;
                            }
                            AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                            AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                        }

                    };
                } else {
                    object = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
                    if (object != null) {
                        object.setLayoutInflater(LayoutInflater.from((Context)this.getActionBarThemedContext()));
                        this.mActionModeView = (ActionBarContextView)object.inflate();
                    }
                }
            }
            if (this.mActionModeView != null) {
                this.endOnGoingFadeAnimation();
                this.mActionModeView.killMode();
                object = this.mActionModeView.getContext();
                object3 = this.mActionModeView;
                if (this.mActionModePopup != null) {
                    bl = false;
                }
                object = new StandaloneActionMode((Context)object, (ActionBarContextView)((Object)object3), (ActionMode.Callback)object2, bl);
                if (object2.onCreateActionMode((android.support.v7.view.ActionMode)object, object.getMenu())) {
                    object.invalidate();
                    this.mActionModeView.initForMode((android.support.v7.view.ActionMode)object);
                    this.mActionMode = object;
                    if (this.shouldAnimateActionModeView()) {
                        this.mActionModeView.setAlpha(0.0f);
                        this.mFadeAnim = ViewCompat.animate((View)this.mActionModeView).alpha(1.0f);
                        this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter(){

                            @Override
                            public void onAnimationEnd(View view) {
                                AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0f);
                                AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                                AppCompatDelegateImpl.this.mFadeAnim = null;
                            }

                            @Override
                            public void onAnimationStart(View view) {
                                AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                                AppCompatDelegateImpl.this.mActionModeView.sendAccessibilityEvent(32);
                                if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                                    ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                                }
                            }
                        });
                    } else {
                        this.mActionModeView.setAlpha(1.0f);
                        this.mActionModeView.setVisibility(0);
                        this.mActionModeView.sendAccessibilityEvent(32);
                        if (this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent());
                        }
                    }
                    if (this.mActionModePopup != null) {
                        this.mWindow.getDecorView().post(this.mShowActionModePopup);
                    }
                } else {
                    this.mActionMode = null;
                }
            }
        }
        object = this.mActionMode;
        if (object != null && (object2 = this.mAppCompatCallback) != null) {
            object2.onSupportActionModeStarted((android.support.v7.view.ActionMode)object);
        }
        return this.mActionMode;
    }

    int updateStatusGuard(int n) {
        int n2 = 0;
        int n3 = 0;
        ActionBarContextView actionBarContextView = this.mActionModeView;
        int n4 = 0;
        int n5 = n2;
        int n6 = n;
        if (actionBarContextView != null) {
            n5 = n2;
            n6 = n;
            if (actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                int n7;
                actionBarContextView = (ViewGroup.MarginLayoutParams)this.mActionModeView.getLayoutParams();
                int n8 = 0;
                n2 = 0;
                if (this.mActionModeView.isShown()) {
                    if (this.mTempRect1 == null) {
                        this.mTempRect1 = new Rect();
                        this.mTempRect2 = new Rect();
                    }
                    Rect rect = this.mTempRect1;
                    Rect rect2 = this.mTempRect2;
                    rect.set(0, n, 0, 0);
                    ViewUtils.computeFitSystemWindows((View)this.mSubDecor, rect, rect2);
                    n7 = rect2.top == 0 ? n : 0;
                    if (actionBarContextView.topMargin != n7) {
                        n7 = 1;
                        actionBarContextView.topMargin = n;
                        rect = this.mStatusGuard;
                        if (rect == null) {
                            this.mStatusGuard = new View(this.mContext);
                            this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
                            this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup.LayoutParams(-1, n));
                            n2 = n7;
                        } else {
                            rect = rect.getLayoutParams();
                            n2 = n7;
                            if (rect.height != n) {
                                rect.height = n;
                                this.mStatusGuard.setLayoutParams((ViewGroup.LayoutParams)rect);
                                n2 = n7;
                            }
                        }
                    }
                    n7 = this.mStatusGuard != null ? 1 : 0;
                    n8 = n;
                    if (!this.mOverlayActionMode) {
                        n8 = n;
                        if (n7 != 0) {
                            n8 = 0;
                        }
                    }
                } else {
                    n7 = n3;
                    n2 = n8;
                    n8 = n;
                    if (actionBarContextView.topMargin != 0) {
                        n2 = 1;
                        actionBarContextView.topMargin = 0;
                        n8 = n;
                        n7 = n3;
                    }
                }
                n5 = n7;
                n6 = n8;
                if (n2 != 0) {
                    this.mActionModeView.setLayoutParams((ViewGroup.LayoutParams)actionBarContextView);
                    n6 = n8;
                    n5 = n7;
                }
            }
        }
        if ((actionBarContextView = this.mStatusGuard) != null) {
            n = n5 != 0 ? n4 : 8;
            actionBarContextView.setVisibility(n);
        }
        return n6;
    }

    private class ActionBarDrawableToggleImpl
    implements ActionBarDrawerToggle.Delegate {
        ActionBarDrawableToggleImpl() {
        }

        @Override
        public Context getActionBarThemedContext() {
            return AppCompatDelegateImpl.this.getActionBarThemedContext();
        }

        @Override
        public Drawable getThemeUpIndicator() {
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.getActionBarThemedContext(), null, new int[]{R.attr.homeAsUpIndicator});
            Drawable drawable2 = tintTypedArray.getDrawable(0);
            tintTypedArray.recycle();
            return drawable2;
        }

        @Override
        public boolean isNavigationVisible() {
            ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            if (actionBar != null && (actionBar.getDisplayOptions() & 4) != 0) {
                return true;
            }
            return false;
        }

        @Override
        public void setActionBarDescription(int n) {
            ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeActionContentDescription(n);
            }
        }

        @Override
        public void setActionBarUpIndicator(Drawable drawable2, int n) {
            ActionBar actionBar = AppCompatDelegateImpl.this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(drawable2);
                actionBar.setHomeActionContentDescription(n);
            }
        }
    }

    private final class ActionMenuPresenterCallback
    implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
            AppCompatDelegateImpl.this.checkCloseActionMenu(menuBuilder);
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback callback = AppCompatDelegateImpl.this.getWindowCallback();
            if (callback != null) {
                callback.onMenuOpened(108, (Menu)menuBuilder);
            }
            return true;
        }
    }

    class ActionModeCallbackWrapperV9
    implements ActionMode.Callback {
        private ActionMode.Callback mWrapped;

        public ActionModeCallbackWrapperV9(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode object) {
            this.mWrapped.onDestroyActionMode((android.support.v7.view.ActionMode)object);
            if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup);
            }
            if (AppCompatDelegateImpl.this.mActionModeView != null) {
                AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                object = AppCompatDelegateImpl.this;
                object.mFadeAnim = ViewCompat.animate((View)object.mActionModeView).alpha(0.0f);
                AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter(){

                    @Override
                    public void onAnimationEnd(View view) {
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                        if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                            AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                        } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                        }
                        AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
                        AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                        AppCompatDelegateImpl.this.mFadeAnim = null;
                    }
                });
            }
            if (AppCompatDelegateImpl.this.mAppCompatCallback != null) {
                AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode);
            }
            AppCompatDelegateImpl.this.mActionMode = null;
        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

    }

    class AppCompatWindowCallback
    extends WindowCallbackWrapper {
        AppCompatWindowCallback(Window.Callback callback) {
            super(callback);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (!AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) && !super.dispatchKeyEvent(keyEvent)) {
                return false;
            }
            return true;
        }

        @Override
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            if (!super.dispatchKeyShortcutEvent(keyEvent) && !AppCompatDelegateImpl.this.onKeyShortcut(keyEvent.getKeyCode(), keyEvent)) {
                return false;
            }
            return true;
        }

        @Override
        public void onContentChanged() {
        }

        @Override
        public boolean onCreatePanelMenu(int n, Menu menu) {
            if (n == 0 && !(menu instanceof MenuBuilder)) {
                return false;
            }
            return super.onCreatePanelMenu(n, menu);
        }

        @Override
        public boolean onMenuOpened(int n, Menu menu) {
            super.onMenuOpened(n, menu);
            AppCompatDelegateImpl.this.onMenuOpened(n);
            return true;
        }

        @Override
        public void onPanelClosed(int n, Menu menu) {
            super.onPanelClosed(n, menu);
            AppCompatDelegateImpl.this.onPanelClosed(n);
        }

        @Override
        public boolean onPreparePanel(int n, View view, Menu menu) {
            MenuBuilder menuBuilder = menu instanceof MenuBuilder ? (MenuBuilder)menu : null;
            if (n == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(true);
            }
            boolean bl = super.onPreparePanel(n, view, menu);
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(false);
            }
            return bl;
        }

        @RequiresApi(value=24)
        @Override
        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int n) {
            PanelFeatureState panelFeatureState = AppCompatDelegateImpl.this.getPanelState(0, true);
            if (panelFeatureState != null && panelFeatureState.menu != null) {
                super.onProvideKeyboardShortcuts(list, panelFeatureState.menu, n);
                return;
            }
            super.onProvideKeyboardShortcuts(list, menu, n);
        }

        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            if (Build.VERSION.SDK_INT >= 23) {
                return null;
            }
            if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled()) {
                return this.startAsSupportActionMode(callback);
            }
            return super.onWindowStartingActionMode(callback);
        }

        @RequiresApi(value=23)
        @Override
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int n) {
            if (AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() && n == 0) {
                return this.startAsSupportActionMode(callback);
            }
            return super.onWindowStartingActionMode(callback, n);
        }

        final ActionMode startAsSupportActionMode(ActionMode.Callback object) {
            android.support.v7.view.ActionMode actionMode = AppCompatDelegateImpl.this.startSupportActionMode((ActionMode.Callback)(object = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, (ActionMode.Callback)object)));
            if (actionMode != null) {
                return object.getActionModeWrapper(actionMode);
            }
            return null;
        }
    }

    @VisibleForTesting
    final class AutoNightModeManager {
        private BroadcastReceiver mAutoTimeChangeReceiver;
        private IntentFilter mAutoTimeChangeReceiverFilter;
        private boolean mIsNight;
        private TwilightManager mTwilightManager;

        AutoNightModeManager(TwilightManager twilightManager) {
            this.mTwilightManager = twilightManager;
            this.mIsNight = twilightManager.isNight();
        }

        void cleanup() {
            if (this.mAutoTimeChangeReceiver != null) {
                AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
                this.mAutoTimeChangeReceiver = null;
            }
        }

        void dispatchTimeChanged() {
            boolean bl = this.mTwilightManager.isNight();
            if (bl != this.mIsNight) {
                this.mIsNight = bl;
                AppCompatDelegateImpl.this.applyDayNight();
            }
        }

        int getApplyableNightMode() {
            this.mIsNight = this.mTwilightManager.isNight();
            if (this.mIsNight) {
                return 2;
            }
            return 1;
        }

        void setup() {
            this.cleanup();
            if (this.mAutoTimeChangeReceiver == null) {
                this.mAutoTimeChangeReceiver = new BroadcastReceiver(){

                    public void onReceive(Context context, Intent intent) {
                        AutoNightModeManager.this.dispatchTimeChanged();
                    }
                };
            }
            if (this.mAutoTimeChangeReceiverFilter == null) {
                this.mAutoTimeChangeReceiverFilter = new IntentFilter();
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_SET");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
            }
            AppCompatDelegateImpl.this.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
        }

    }

    private class ListMenuDecorView
    extends ContentFrameLayout {
        public ListMenuDecorView(Context context) {
            super(context);
        }

        private boolean isOutOfBounds(int n, int n2) {
            if (n >= -5 && n2 >= -5 && n <= this.getWidth() + 5 && n2 <= this.getHeight() + 5) {
                return false;
            }
            return true;
        }

        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (!AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) && !super.dispatchKeyEvent(keyEvent)) {
                return false;
            }
            return true;
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && this.isOutOfBounds((int)motionEvent.getX(), (int)motionEvent.getY())) {
                AppCompatDelegateImpl.this.closePanel(0);
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        public void setBackgroundResource(int n) {
            this.setBackgroundDrawable(AppCompatResources.getDrawable(this.getContext(), n));
        }
    }

    protected static final class PanelFeatureState {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastOpen;
        int windowAnimations;
        int x;
        int y;

        PanelFeatureState(int n) {
            this.featureId = n;
            this.refreshDecorView = false;
        }

        void applyFrozenState() {
            Bundle bundle;
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null && (bundle = this.frozenMenuState) != null) {
                menuBuilder.restorePresenterStates(bundle);
                this.frozenMenuState = null;
            }
        }

        public void clearMenuPresenters() {
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null) {
                menuBuilder.removeMenuPresenter(this.listMenuPresenter);
            }
            this.listMenuPresenter = null;
        }

        MenuView getListMenuView(MenuPresenter.Callback callback) {
            if (this.menu == null) {
                return null;
            }
            if (this.listMenuPresenter == null) {
                this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout);
                this.listMenuPresenter.setCallback(callback);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }

        public boolean hasPanelItems() {
            View view = this.shownPanelView;
            boolean bl = false;
            if (view == null) {
                return false;
            }
            if (this.createdPanelView != null) {
                return true;
            }
            if (this.listMenuPresenter.getAdapter().getCount() > 0) {
                bl = true;
            }
            return bl;
        }

        void onRestoreInstanceState(Parcelable parcelable) {
            parcelable = (SavedState)parcelable;
            this.featureId = parcelable.featureId;
            this.wasLastOpen = parcelable.isOpen;
            this.frozenMenuState = parcelable.menuState;
            this.shownPanelView = null;
            this.decorView = null;
        }

        Parcelable onSaveInstanceState() {
            SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return savedState;
        }

        void setMenu(MenuBuilder menuBuilder) {
            Object object = this.menu;
            if (menuBuilder == object) {
                return;
            }
            if (object != null) {
                object.removeMenuPresenter(this.listMenuPresenter);
            }
            this.menu = menuBuilder;
            if (menuBuilder != null && (object = this.listMenuPresenter) != null) {
                menuBuilder.addMenuPresenter((MenuPresenter)object);
            }
        }

        void setStyle(Context object) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = object.getResources().newTheme();
            theme.setTo(object.getTheme());
            theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            } else {
                theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
            }
            object = new ContextThemeWrapper((Context)object, 0);
            object.getTheme().setTo(theme);
            this.listPresenterContext = object;
            object = object.obtainStyledAttributes(R.styleable.AppCompatTheme);
            this.background = object.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
            this.windowAnimations = object.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            object.recycle();
        }

        private static class SavedState
        implements Parcelable {
            public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

                public SavedState createFromParcel(Parcel parcel) {
                    return SavedState.readFromParcel(parcel, null);
                }

                public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return SavedState.readFromParcel(parcel, classLoader);
                }

                public SavedState[] newArray(int n) {
                    return new SavedState[n];
                }
            };
            int featureId;
            boolean isOpen;
            Bundle menuState;

            SavedState() {
            }

            static SavedState readFromParcel(Parcel parcel, ClassLoader classLoader) {
                SavedState savedState = new SavedState();
                savedState.featureId = parcel.readInt();
                int n = parcel.readInt();
                boolean bl = true;
                if (n != 1) {
                    bl = false;
                }
                savedState.isOpen = bl;
                if (savedState.isOpen) {
                    savedState.menuState = parcel.readBundle(classLoader);
                }
                return savedState;
            }

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(Parcel parcel, int n) {
                RuntimeException runtimeException;
                super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
                throw runtimeException;
            }

        }

    }

    private final class PanelMenuPresenterCallback
    implements MenuPresenter.Callback {
        PanelMenuPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder object, boolean bl) {
            MenuBuilder menuBuilder = object.getRootMenu();
            boolean bl2 = menuBuilder != object;
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (bl2) {
                object = menuBuilder;
            }
            if ((object = appCompatDelegateImpl.findMenuPanel((Menu)object)) != null) {
                if (bl2) {
                    AppCompatDelegateImpl.this.callOnPanelClosed(object.featureId, (PanelFeatureState)object, menuBuilder);
                    AppCompatDelegateImpl.this.closePanel((PanelFeatureState)object, true);
                    return;
                }
                AppCompatDelegateImpl.this.closePanel((PanelFeatureState)object, bl);
            }
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback callback;
            if (menuBuilder == null && AppCompatDelegateImpl.this.mHasActionBar && (callback = AppCompatDelegateImpl.this.getWindowCallback()) != null && !AppCompatDelegateImpl.this.mIsDestroyed) {
                callback.onMenuOpened(108, (Menu)menuBuilder);
            }
            return true;
        }
    }

}

