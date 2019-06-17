/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.database.DataSetObserver
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Looper
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.KeyEvent$DispatcherState
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnTouchListener
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.widget.AbsListView
 *  android.widget.AbsListView$OnScrollListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.PopupWindow
 *  android.widget.PopupWindow$OnDismissListener
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.support.v7.internal.widget.AppCompatPopupWindow;
import android.support.v7.internal.widget.ListViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.lang.reflect.Method;
import java.util.Locale;

public class ListPopupWindow {
    private static final boolean DEBUG = false;
    private static final int EXPAND_LIST_TIMEOUT = 250;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    public static final int MATCH_PARENT = -1;
    public static final int POSITION_PROMPT_ABOVE = 0;
    public static final int POSITION_PROMPT_BELOW = 1;
    private static final String TAG = "ListPopupWindow";
    public static final int WRAP_CONTENT = -2;
    private static Method sClipToWindowEnabledMethod;
    private static Method sGetMaxAvailableHeightMethod;
    private ListAdapter mAdapter;
    private Context mContext;
    private boolean mDropDownAlwaysVisible = false;
    private View mDropDownAnchorView;
    private int mDropDownGravity = 0;
    private int mDropDownHeight = -2;
    private int mDropDownHorizontalOffset;
    private DropDownListView mDropDownList;
    private Drawable mDropDownListHighlight;
    private int mDropDownVerticalOffset;
    private boolean mDropDownVerticalOffsetSet;
    private int mDropDownWidth = -2;
    private int mDropDownWindowLayoutType = 1002;
    private boolean mForceIgnoreOutsideTouch = false;
    private final Handler mHandler;
    private final ListSelectorHider mHideSelector;
    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;
    private int mLayoutDirection;
    int mListItemExpandMaximum = Integer.MAX_VALUE;
    private boolean mModal;
    private DataSetObserver mObserver;
    private PopupWindow mPopup;
    private int mPromptPosition = 0;
    private View mPromptView;
    private final ResizePopupRunnable mResizePopupRunnable;
    private final PopupScrollListener mScrollListener;
    private Runnable mShowDropDownRunnable;
    private Rect mTempRect;
    private final PopupTouchInterceptor mTouchInterceptor;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static {
        try {
            sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", Boolean.TYPE);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.i((String)"ListPopupWindow", (String)"Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
        }
        try {
            sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", View.class, Integer.TYPE, Boolean.TYPE);
            return;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.i((String)"ListPopupWindow", (String)"Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
            return;
        }
    }

    public ListPopupWindow(Context context) {
        this(context, null, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int n, int n2) {
        this.mResizePopupRunnable = new ResizePopupRunnable();
        this.mTouchInterceptor = new PopupTouchInterceptor();
        this.mScrollListener = new PopupScrollListener();
        this.mHideSelector = new ListSelectorHider();
        this.mTempRect = new Rect();
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ListPopupWindow, n, n2);
        this.mDropDownHorizontalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
        this.mDropDownVerticalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
        if (this.mDropDownVerticalOffset != 0) {
            this.mDropDownVerticalOffsetSet = true;
        }
        typedArray.recycle();
        this.mPopup = new AppCompatPopupWindow(context, attributeSet, n);
        this.mPopup.setInputMethodMode(1);
        this.mLayoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(this.mContext.getResources().getConfiguration().locale);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int buildDropDown() {
        Object object;
        boolean bl;
        int n = 0;
        int n2 = 0;
        if (this.mDropDownList == null) {
            Context context = this.mContext;
            this.mShowDropDownRunnable = new Runnable(){

                @Override
                public void run() {
                    View view = ListPopupWindow.this.getAnchorView();
                    if (view != null && view.getWindowToken() != null) {
                        ListPopupWindow.this.show();
                    }
                }
            };
            bl = !this.mModal;
            this.mDropDownList = new DropDownListView(context, bl);
            if (this.mDropDownListHighlight != null) {
                this.mDropDownList.setSelector(this.mDropDownListHighlight);
            }
            this.mDropDownList.setAdapter(this.mAdapter);
            this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
            this.mDropDownList.setFocusable(true);
            this.mDropDownList.setFocusableInTouchMode(true);
            this.mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                public void onItemSelected(AdapterView<?> object, View view, int n, long l) {
                    if (n != -1 && (object = ListPopupWindow.this.mDropDownList) != null) {
                        ((DropDownListView)((Object)object)).mListSelectionHidden = false;
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.mDropDownList.setOnScrollListener((AbsListView.OnScrollListener)this.mScrollListener);
            if (this.mItemSelectedListener != null) {
                this.mDropDownList.setOnItemSelectedListener(this.mItemSelectedListener);
            }
            DropDownListView dropDownListView = this.mDropDownList;
            View view = this.mPromptView;
            object = dropDownListView;
            if (view != null) {
                object = new LinearLayout(context);
                object.setOrientation(1);
                context = new LinearLayout.LayoutParams(-1, 0, 1.0f);
                switch (this.mPromptPosition) {
                    default: {
                        Log.e((String)"ListPopupWindow", (String)("Invalid hint position " + this.mPromptPosition));
                        break;
                    }
                    case 1: {
                        object.addView((View)dropDownListView, (ViewGroup.LayoutParams)context);
                        object.addView(view);
                        break;
                    }
                    case 0: {
                        object.addView(view);
                        object.addView((View)dropDownListView, (ViewGroup.LayoutParams)context);
                    }
                }
                if (this.mDropDownWidth >= 0) {
                    n2 = Integer.MIN_VALUE;
                    n = this.mDropDownWidth;
                } else {
                    n2 = 0;
                    n = 0;
                }
                view.measure(View.MeasureSpec.makeMeasureSpec((int)n, (int)n2), 0);
                dropDownListView = (LinearLayout.LayoutParams)view.getLayoutParams();
                n2 = view.getMeasuredHeight() + dropDownListView.topMargin + dropDownListView.bottomMargin;
            }
            this.mPopup.setContentView((View)object);
        } else {
            object = (ViewGroup)this.mPopup.getContentView();
            object = this.mPromptView;
            n2 = n;
            if (object != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)object.getLayoutParams();
                n2 = object.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }
        }
        int n3 = 0;
        object = this.mPopup.getBackground();
        if (object != null) {
            object.getPadding(this.mTempRect);
            n3 = n = this.mTempRect.top + this.mTempRect.bottom;
            if (!this.mDropDownVerticalOffsetSet) {
                this.mDropDownVerticalOffset = - this.mTempRect.top;
                n3 = n;
            }
        } else {
            this.mTempRect.setEmpty();
        }
        bl = this.mPopup.getInputMethodMode() == 2;
        int n4 = this.getMaxAvailableHeight(this.getAnchorView(), this.mDropDownVerticalOffset, bl);
        if (this.mDropDownAlwaysVisible || this.mDropDownHeight == -1) {
            return n4 + n3;
        }
        switch (this.mDropDownWidth) {
            default: {
                n = View.MeasureSpec.makeMeasureSpec((int)this.mDropDownWidth, (int)1073741824);
                break;
            }
            case -2: {
                n = View.MeasureSpec.makeMeasureSpec((int)(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right)), (int)Integer.MIN_VALUE);
                break;
            }
            case -1: {
                n = View.MeasureSpec.makeMeasureSpec((int)(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right)), (int)1073741824);
            }
        }
        n4 = this.mDropDownList.measureHeightOfChildrenCompat(n, 0, -1, n4 - n2, -1);
        n = n2;
        if (n4 > 0) {
            n = n2 + n3;
        }
        return n4 + n;
    }

    private int getMaxAvailableHeight(View view, int n, boolean bl) {
        if (sGetMaxAvailableHeightMethod != null) {
            try {
                int n2 = (Integer)sGetMaxAvailableHeightMethod.invoke((Object)this.mPopup, new Object[]{view, n, bl});
                return n2;
            }
            catch (Exception exception) {
                Log.i((String)"ListPopupWindow", (String)"Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
            }
        }
        return this.mPopup.getMaxAvailableHeight(view, n);
    }

    private static boolean isConfirmKey(int n) {
        if (n == 66 || n == 23) {
            return true;
        }
        return false;
    }

    private void removePromptView() {
        ViewParent viewParent;
        if (this.mPromptView != null && (viewParent = this.mPromptView.getParent()) instanceof ViewGroup) {
            ((ViewGroup)viewParent).removeView(this.mPromptView);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void setPopupClipToScreenEnabled(boolean bl) {
        if (sClipToWindowEnabledMethod == null) return;
        try {
            sClipToWindowEnabledMethod.invoke((Object)this.mPopup, bl);
            return;
        }
        catch (Exception exception) {
            Log.i((String)"ListPopupWindow", (String)"Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
            return;
        }
    }

    public void clearListSelection() {
        DropDownListView dropDownListView = this.mDropDownList;
        if (dropDownListView != null) {
            dropDownListView.mListSelectionHidden = true;
            dropDownListView.requestLayout();
        }
    }

    public View.OnTouchListener createDragToOpenListener(View view) {
        return new ForwardingListener(view){

            @Override
            public ListPopupWindow getPopup() {
                return ListPopupWindow.this;
            }
        };
    }

    public void dismiss() {
        this.mPopup.dismiss();
        this.removePromptView();
        this.mPopup.setContentView(null);
        this.mDropDownList = null;
        this.mHandler.removeCallbacks((Runnable)this.mResizePopupRunnable);
    }

    public View getAnchorView() {
        return this.mDropDownAnchorView;
    }

    public int getAnimationStyle() {
        return this.mPopup.getAnimationStyle();
    }

    public Drawable getBackground() {
        return this.mPopup.getBackground();
    }

    public int getHeight() {
        return this.mDropDownHeight;
    }

    public int getHorizontalOffset() {
        return this.mDropDownHorizontalOffset;
    }

    public int getInputMethodMode() {
        return this.mPopup.getInputMethodMode();
    }

    public ListView getListView() {
        return this.mDropDownList;
    }

    public int getPromptPosition() {
        return this.mPromptPosition;
    }

    public Object getSelectedItem() {
        if (!this.isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedItem();
    }

    public long getSelectedItemId() {
        if (!this.isShowing()) {
            return Long.MIN_VALUE;
        }
        return this.mDropDownList.getSelectedItemId();
    }

    public int getSelectedItemPosition() {
        if (!this.isShowing()) {
            return -1;
        }
        return this.mDropDownList.getSelectedItemPosition();
    }

    public View getSelectedView() {
        if (!this.isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedView();
    }

    public int getSoftInputMode() {
        return this.mPopup.getSoftInputMode();
    }

    public int getVerticalOffset() {
        if (!this.mDropDownVerticalOffsetSet) {
            return 0;
        }
        return this.mDropDownVerticalOffset;
    }

    public int getWidth() {
        return this.mDropDownWidth;
    }

    public boolean isDropDownAlwaysVisible() {
        return this.mDropDownAlwaysVisible;
    }

    public boolean isInputMethodNotNeeded() {
        if (this.mPopup.getInputMethodMode() == 2) {
            return true;
        }
        return false;
    }

    public boolean isModal() {
        return this.mModal;
    }

    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (!this.isShowing()) return false;
        if (n == 62) return false;
        if (this.mDropDownList.getSelectedItemPosition() < 0) {
            if (ListPopupWindow.isConfirmKey(n)) return false;
        }
        int n2 = this.mDropDownList.getSelectedItemPosition();
        boolean bl = !this.mPopup.isAboveAnchor();
        ListAdapter listAdapter = this.mAdapter;
        int n3 = Integer.MAX_VALUE;
        int n4 = Integer.MIN_VALUE;
        if (listAdapter != null) {
            boolean bl2 = listAdapter.areAllItemsEnabled();
            n3 = bl2 ? 0 : this.mDropDownList.lookForSelectablePosition(0, true);
            n4 = bl2 ? listAdapter.getCount() - 1 : this.mDropDownList.lookForSelectablePosition(listAdapter.getCount() - 1, false);
        }
        if (bl && n == 19 && n2 <= n3 || !bl && n == 20 && n2 >= n4) {
            this.clearListSelection();
            this.mPopup.setInputMethodMode(1);
            this.show();
            return true;
        }
        this.mDropDownList.mListSelectionHidden = false;
        if (this.mDropDownList.onKeyDown(n, keyEvent)) {
            this.mPopup.setInputMethodMode(2);
            this.mDropDownList.requestFocusFromTouch();
            this.show();
            switch (n) {
                case 19: 
                case 20: 
                case 23: 
                case 66: {
                    return true;
                }
            }
            return false;
        }
        if (bl && n == 20) {
            if (n2 != n4) return false;
            return true;
        }
        if (bl) return false;
        if (n != 19) return false;
        if (n2 != n3) return false;
        return true;
    }

    public boolean onKeyPreIme(int n, KeyEvent keyEvent) {
        if (n == 4 && this.isShowing()) {
            View view = this.mDropDownAnchorView;
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                if ((view = view.getKeyDispatcherState()) != null) {
                    view.startTracking(keyEvent, (Object)this);
                }
                return true;
            }
            if (keyEvent.getAction() == 1) {
                if ((view = view.getKeyDispatcherState()) != null) {
                    view.handleUpEvent(keyEvent);
                }
                if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                    this.dismiss();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (this.isShowing() && this.mDropDownList.getSelectedItemPosition() >= 0) {
            boolean bl = this.mDropDownList.onKeyUp(n, keyEvent);
            if (bl && ListPopupWindow.isConfirmKey(n)) {
                this.dismiss();
            }
            return bl;
        }
        return false;
    }

    public boolean performItemClick(int n) {
        if (this.isShowing()) {
            if (this.mItemClickListener != null) {
                DropDownListView dropDownListView = this.mDropDownList;
                View view = dropDownListView.getChildAt(n - dropDownListView.getFirstVisiblePosition());
                ListAdapter listAdapter = dropDownListView.getAdapter();
                this.mItemClickListener.onItemClick((AdapterView)dropDownListView, view, n, listAdapter.getItemId(n));
            }
            return true;
        }
        return false;
    }

    public void postShow() {
        this.mHandler.post(this.mShowDropDownRunnable);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setAdapter(ListAdapter listAdapter) {
        if (this.mObserver == null) {
            this.mObserver = new PopupDataSetObserver();
        } else if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
        }
        this.mAdapter = listAdapter;
        if (this.mAdapter != null) {
            listAdapter.registerDataSetObserver(this.mObserver);
        }
        if (this.mDropDownList != null) {
            this.mDropDownList.setAdapter(this.mAdapter);
        }
    }

    public void setAnchorView(View view) {
        this.mDropDownAnchorView = view;
    }

    public void setAnimationStyle(int n) {
        this.mPopup.setAnimationStyle(n);
    }

    public void setBackgroundDrawable(Drawable drawable2) {
        this.mPopup.setBackgroundDrawable(drawable2);
    }

    public void setContentWidth(int n) {
        Drawable drawable2 = this.mPopup.getBackground();
        if (drawable2 != null) {
            drawable2.getPadding(this.mTempRect);
            this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + n;
            return;
        }
        this.setWidth(n);
    }

    public void setDropDownAlwaysVisible(boolean bl) {
        this.mDropDownAlwaysVisible = bl;
    }

    public void setDropDownGravity(int n) {
        this.mDropDownGravity = n;
    }

    public void setForceIgnoreOutsideTouch(boolean bl) {
        this.mForceIgnoreOutsideTouch = bl;
    }

    public void setHeight(int n) {
        this.mDropDownHeight = n;
    }

    public void setHorizontalOffset(int n) {
        this.mDropDownHorizontalOffset = n;
    }

    public void setInputMethodMode(int n) {
        this.mPopup.setInputMethodMode(n);
    }

    void setListItemExpandMax(int n) {
        this.mListItemExpandMaximum = n;
    }

    public void setListSelector(Drawable drawable2) {
        this.mDropDownListHighlight = drawable2;
    }

    public void setModal(boolean bl) {
        this.mModal = bl;
        this.mPopup.setFocusable(bl);
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mPopup.setOnDismissListener(onDismissListener);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.mItemSelectedListener = onItemSelectedListener;
    }

    public void setPromptPosition(int n) {
        this.mPromptPosition = n;
    }

    public void setPromptView(View view) {
        boolean bl = this.isShowing();
        if (bl) {
            this.removePromptView();
        }
        this.mPromptView = view;
        if (bl) {
            this.show();
        }
    }

    public void setSelection(int n) {
        DropDownListView dropDownListView = this.mDropDownList;
        if (this.isShowing() && dropDownListView != null) {
            dropDownListView.mListSelectionHidden = false;
            dropDownListView.setSelection(n);
            if (Build.VERSION.SDK_INT >= 11 && dropDownListView.getChoiceMode() != 0) {
                dropDownListView.setItemChecked(n, true);
            }
        }
    }

    public void setSoftInputMode(int n) {
        this.mPopup.setSoftInputMode(n);
    }

    public void setVerticalOffset(int n) {
        this.mDropDownVerticalOffset = n;
        this.mDropDownVerticalOffsetSet = true;
    }

    public void setWidth(int n) {
        this.mDropDownWidth = n;
    }

    public void setWindowLayoutType(int n) {
        this.mDropDownWindowLayoutType = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void show() {
        boolean bl = true;
        boolean bl2 = false;
        int n = -1;
        int n2 = this.buildDropDown();
        boolean bl3 = this.isInputMethodNotNeeded();
        PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
        if (this.mPopup.isShowing()) {
            PopupWindow popupWindow;
            int n3;
            int n4 = this.mDropDownWidth == -1 ? -1 : (this.mDropDownWidth == -2 ? this.getAnchorView().getWidth() : this.mDropDownWidth);
            if (this.mDropDownHeight == -1) {
                if (!bl3) {
                    n2 = -1;
                }
                if (bl3) {
                    popupWindow = this.mPopup;
                    n3 = this.mDropDownWidth == -1 ? -1 : 0;
                    popupWindow.setWidth(n3);
                    this.mPopup.setHeight(0);
                } else {
                    popupWindow = this.mPopup;
                    n3 = this.mDropDownWidth == -1 ? -1 : 0;
                    popupWindow.setWidth(n3);
                    this.mPopup.setHeight(-1);
                }
            } else if (this.mDropDownHeight != -2) {
                n2 = this.mDropDownHeight;
            }
            popupWindow = this.mPopup;
            bl = bl2;
            if (!this.mForceIgnoreOutsideTouch) {
                bl = bl2;
                if (!this.mDropDownAlwaysVisible) {
                    bl = true;
                }
            }
            popupWindow.setOutsideTouchable(bl);
            popupWindow = this.mPopup;
            View view = this.getAnchorView();
            n3 = this.mDropDownHorizontalOffset;
            int n5 = this.mDropDownVerticalOffset;
            if (n4 < 0) {
                n4 = -1;
            }
            if (n2 < 0) {
                n2 = n;
            }
            popupWindow.update(view, n3, n5, n4, n2);
            return;
        } else {
            int n6 = this.mDropDownWidth == -1 ? -1 : (this.mDropDownWidth == -2 ? this.getAnchorView().getWidth() : this.mDropDownWidth);
            if (this.mDropDownHeight == -1) {
                n2 = -1;
            } else if (this.mDropDownHeight != -2) {
                n2 = this.mDropDownHeight;
            }
            this.mPopup.setWidth(n6);
            this.mPopup.setHeight(n2);
            this.setPopupClipToScreenEnabled(true);
            PopupWindow popupWindow = this.mPopup;
            if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
                bl = false;
            }
            popupWindow.setOutsideTouchable(bl);
            this.mPopup.setTouchInterceptor((View.OnTouchListener)this.mTouchInterceptor);
            PopupWindowCompat.showAsDropDown(this.mPopup, this.getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
            this.mDropDownList.setSelection(-1);
            if (!this.mModal || this.mDropDownList.isInTouchMode()) {
                this.clearListSelection();
            }
            if (this.mModal) return;
            {
                this.mHandler.post((Runnable)this.mHideSelector);
                return;
            }
        }
    }

    private static class DropDownListView
    extends ListViewCompat {
        private ViewPropertyAnimatorCompat mClickAnimation;
        private boolean mDrawsInPressedState;
        private boolean mHijackFocus;
        private boolean mListSelectionHidden;
        private ListViewAutoScrollHelper mScrollHelper;

        public DropDownListView(Context context, boolean bl) {
            super(context, null, R.attr.dropDownListViewStyle);
            this.mHijackFocus = bl;
            this.setCacheColorHint(0);
        }

        private void clearPressedItem() {
            this.mDrawsInPressedState = false;
            this.setPressed(false);
            this.drawableStateChanged();
            View view = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
            if (view != null) {
                view.setPressed(false);
            }
            if (this.mClickAnimation != null) {
                this.mClickAnimation.cancel();
                this.mClickAnimation = null;
            }
        }

        private void clickPressedItem(View view, int n) {
            this.performItemClick(view, n, this.getItemIdAtPosition(n));
        }

        private void setPressedItem(View view, int n, float f, float f2) {
            View view2;
            this.mDrawsInPressedState = true;
            if (Build.VERSION.SDK_INT >= 21) {
                this.drawableHotspotChanged(f, f2);
            }
            if (!this.isPressed()) {
                this.setPressed(true);
            }
            this.layoutChildren();
            if (this.mMotionPosition != -1 && (view2 = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition())) != null && view2 != view && view2.isPressed()) {
                view2.setPressed(false);
            }
            this.mMotionPosition = n;
            float f3 = view.getLeft();
            float f4 = view.getTop();
            if (Build.VERSION.SDK_INT >= 21) {
                view.drawableHotspotChanged(f - f3, f2 - f4);
            }
            if (!view.isPressed()) {
                view.setPressed(true);
            }
            this.setSelection(n);
            this.positionSelectorLikeTouchCompat(n, view, f, f2);
            this.setSelectorEnabled(false);
            this.refreshDrawableState();
        }

        public boolean hasFocus() {
            if (this.mHijackFocus || super.hasFocus()) {
                return true;
            }
            return false;
        }

        public boolean hasWindowFocus() {
            if (this.mHijackFocus || super.hasWindowFocus()) {
                return true;
            }
            return false;
        }

        public boolean isFocused() {
            if (this.mHijackFocus || super.isFocused()) {
                return true;
            }
            return false;
        }

        public boolean isInTouchMode() {
            if (this.mHijackFocus && this.mListSelectionHidden || super.isInTouchMode()) {
                return true;
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onForwardedEvent(MotionEvent motionEvent, int n) {
            boolean bl = true;
            boolean bl2 = true;
            int n2 = 0;
            int n3 = MotionEventCompat.getActionMasked(motionEvent);
            switch (n3) {
                default: {
                    bl = bl2;
                    n = n2;
                    break;
                }
                case 3: {
                    bl = false;
                    n = n2;
                    break;
                }
                case 1: {
                    bl = false;
                }
                case 2: {
                    int n4 = motionEvent.findPointerIndex(n);
                    if (n4 < 0) {
                        bl = false;
                        n = n2;
                        break;
                    }
                    n = (int)motionEvent.getX(n4);
                    int n5 = (int)motionEvent.getY(n4);
                    n4 = this.pointToPosition(n, n5);
                    if (n4 == -1) {
                        n = 1;
                        break;
                    }
                    View view = this.getChildAt(n4 - this.getFirstVisiblePosition());
                    this.setPressedItem(view, n4, n, n5);
                    bl2 = true;
                    n = n2;
                    bl = bl2;
                    if (n3 != 1) break;
                    this.clickPressedItem(view, n4);
                    n = n2;
                    bl = bl2;
                }
            }
            if (!bl || n != 0) {
                this.clearPressedItem();
            }
            if (bl) {
                if (this.mScrollHelper == null) {
                    this.mScrollHelper = new ListViewAutoScrollHelper(this);
                }
                this.mScrollHelper.setEnabled(true);
                this.mScrollHelper.onTouch((View)this, motionEvent);
                return bl;
            } else {
                if (this.mScrollHelper == null) return bl;
                {
                    this.mScrollHelper.setEnabled(false);
                    return bl;
                }
            }
        }

        @Override
        protected boolean touchModeDrawsInPressedStateCompat() {
            if (this.mDrawsInPressedState || super.touchModeDrawsInPressedStateCompat()) {
                return true;
            }
            return false;
        }
    }

    public static abstract class ForwardingListener
    implements View.OnTouchListener {
        private int mActivePointerId;
        private Runnable mDisallowIntercept;
        private boolean mForwarding;
        private final int mLongPressTimeout;
        private final float mScaledTouchSlop;
        private final View mSrc;
        private final int mTapTimeout;
        private final int[] mTmpLocation = new int[2];
        private Runnable mTriggerLongPress;
        private boolean mWasLongPress;

        public ForwardingListener(View view) {
            this.mSrc = view;
            this.mScaledTouchSlop = ViewConfiguration.get((Context)view.getContext()).getScaledTouchSlop();
            this.mTapTimeout = ViewConfiguration.getTapTimeout();
            this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
        }

        private void clearCallbacks() {
            if (this.mTriggerLongPress != null) {
                this.mSrc.removeCallbacks(this.mTriggerLongPress);
            }
            if (this.mDisallowIntercept != null) {
                this.mSrc.removeCallbacks(this.mDisallowIntercept);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private void onLongPress() {
            this.clearCallbacks();
            View view = this.mSrc;
            if (!view.isEnabled() || view.isLongClickable() || !this.onForwardingStarted()) {
                return;
            }
            view.getParent().requestDisallowInterceptTouchEvent(true);
            long l = SystemClock.uptimeMillis();
            MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
            view.onTouchEvent(motionEvent);
            motionEvent.recycle();
            this.mForwarding = true;
            this.mWasLongPress = true;
        }

        /*
         * Enabled aggressive block sorting
         */
        private boolean onTouchForwarded(MotionEvent motionEvent) {
            boolean bl = true;
            View view = this.mSrc;
            Object object = this.getPopup();
            if (object == null) return false;
            if (!object.isShowing()) {
                return false;
            }
            if ((object = ((ListPopupWindow)object).mDropDownList) == null) return false;
            if (!object.isShown()) return false;
            MotionEvent motionEvent2 = MotionEvent.obtainNoHistory((MotionEvent)motionEvent);
            this.toGlobalMotionEvent(view, motionEvent2);
            this.toLocalMotionEvent((View)object, motionEvent2);
            boolean bl2 = object.onForwardedEvent(motionEvent2, this.mActivePointerId);
            motionEvent2.recycle();
            int n = MotionEventCompat.getActionMasked(motionEvent);
            n = n != 1 && n != 3 ? 1 : 0;
            if (!bl2) return false;
            if (n == 0) return false;
            return bl;
        }

        /*
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        private boolean onTouchObserved(MotionEvent motionEvent) {
            View view = this.mSrc;
            if (!view.isEnabled()) {
                return false;
            }
            switch (MotionEventCompat.getActionMasked(motionEvent)) {
                default: {
                    return false;
                }
                case 0: {
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mWasLongPress = false;
                    if (this.mDisallowIntercept == null) {
                        this.mDisallowIntercept = new DisallowIntercept();
                    }
                    view.postDelayed(this.mDisallowIntercept, (long)this.mTapTimeout);
                    if (this.mTriggerLongPress == null) {
                        this.mTriggerLongPress = new TriggerLongPress();
                    }
                    view.postDelayed(this.mTriggerLongPress, (long)this.mLongPressTimeout);
                    return false;
                }
                case 2: {
                    int n = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (n < 0) return false;
                    if (ForwardingListener.pointInView(view, motionEvent.getX(n), motionEvent.getY(n), this.mScaledTouchSlop)) return false;
                    this.clearCallbacks();
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                case 1: 
                case 3: 
            }
            this.clearCallbacks();
            return false;
        }

        private static boolean pointInView(View view, float f, float f2, float f3) {
            if (f >= - f3 && f2 >= - f3 && f < (float)(view.getRight() - view.getLeft()) + f3 && f2 < (float)(view.getBottom() - view.getTop()) + f3) {
                return true;
            }
            return false;
        }

        private boolean toGlobalMotionEvent(View view, MotionEvent motionEvent) {
            int[] arrn = this.mTmpLocation;
            view.getLocationOnScreen(arrn);
            motionEvent.offsetLocation((float)arrn[0], (float)arrn[1]);
            return true;
        }

        private boolean toLocalMotionEvent(View view, MotionEvent motionEvent) {
            int[] arrn = this.mTmpLocation;
            view.getLocationOnScreen(arrn);
            motionEvent.offsetLocation((float)(- arrn[0]), (float)(- arrn[1]));
            return true;
        }

        public abstract ListPopupWindow getPopup();

        protected boolean onForwardingStarted() {
            ListPopupWindow listPopupWindow = this.getPopup();
            if (listPopupWindow != null && !listPopupWindow.isShowing()) {
                listPopupWindow.show();
            }
            return true;
        }

        protected boolean onForwardingStopped() {
            ListPopupWindow listPopupWindow = this.getPopup();
            if (listPopupWindow != null && listPopupWindow.isShowing()) {
                listPopupWindow.dismiss();
            }
            return true;
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onTouch(View view, MotionEvent motionEvent) {
            boolean bl;
            boolean bl2 = false;
            boolean bl3 = this.mForwarding;
            if (bl3) {
                bl = this.mWasLongPress ? this.onTouchForwarded(motionEvent) : this.onTouchForwarded(motionEvent) || !this.onForwardingStopped();
            } else {
                boolean bl4 = this.onTouchObserved(motionEvent) && this.onForwardingStarted();
                bl = bl4;
                if (bl4) {
                    long l = SystemClock.uptimeMillis();
                    view = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
                    this.mSrc.onTouchEvent((MotionEvent)view);
                    view.recycle();
                    bl = bl4;
                }
            }
            this.mForwarding = bl;
            if (bl) return true;
            bl = bl2;
            if (!bl3) return bl;
            return true;
        }

        private class DisallowIntercept
        implements Runnable {
            private DisallowIntercept() {
            }

            @Override
            public void run() {
                ForwardingListener.this.mSrc.getParent().requestDisallowInterceptTouchEvent(true);
            }
        }

        private class TriggerLongPress
        implements Runnable {
            private TriggerLongPress() {
            }

            @Override
            public void run() {
                ForwardingListener.this.onLongPress();
            }
        }

    }

    private class ListSelectorHider
    implements Runnable {
        private ListSelectorHider() {
        }

        @Override
        public void run() {
            ListPopupWindow.this.clearListSelection();
        }
    }

    private class PopupDataSetObserver
    extends DataSetObserver {
        private PopupDataSetObserver() {
        }

        public void onChanged() {
            if (ListPopupWindow.this.isShowing()) {
                ListPopupWindow.this.show();
            }
        }

        public void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    private class PopupScrollListener
    implements AbsListView.OnScrollListener {
        private PopupScrollListener() {
        }

        public void onScroll(AbsListView absListView, int n, int n2, int n3) {
        }

        public void onScrollStateChanged(AbsListView absListView, int n) {
            if (n == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
                ListPopupWindow.this.mHandler.removeCallbacks((Runnable)ListPopupWindow.this.mResizePopupRunnable);
                ListPopupWindow.this.mResizePopupRunnable.run();
            }
        }
    }

    private class PopupTouchInterceptor
    implements View.OnTouchListener {
        private PopupTouchInterceptor() {
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int n = motionEvent.getAction();
            int n2 = (int)motionEvent.getX();
            int n3 = (int)motionEvent.getY();
            if (n == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && n2 >= 0 && n2 < ListPopupWindow.this.mPopup.getWidth() && n3 >= 0 && n3 < ListPopupWindow.this.mPopup.getHeight()) {
                ListPopupWindow.this.mHandler.postDelayed((Runnable)ListPopupWindow.this.mResizePopupRunnable, 250);
                return false;
            }
            if (n != 1) return false;
            ListPopupWindow.this.mHandler.removeCallbacks((Runnable)ListPopupWindow.this.mResizePopupRunnable);
            return false;
        }
    }

    private class ResizePopupRunnable
    implements Runnable {
        private ResizePopupRunnable() {
        }

        @Override
        public void run() {
            if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow((View)ListPopupWindow.this.mDropDownList) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
                ListPopupWindow.this.mPopup.setInputMethodMode(2);
                ListPopupWindow.this.show();
            }
        }
    }

}

