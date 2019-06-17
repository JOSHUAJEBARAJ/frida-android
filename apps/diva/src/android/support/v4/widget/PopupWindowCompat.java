/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.widget.PopupWindow
 */
package android.support.v4.widget;

import android.os.Build;
import android.support.v4.widget.PopupWindowCompatApi21;
import android.support.v4.widget.PopupWindowCompatApi23;
import android.support.v4.widget.PopupWindowCompatGingerbread;
import android.support.v4.widget.PopupWindowCompatKitKat;
import android.view.View;
import android.widget.PopupWindow;

public class PopupWindowCompat {
    static final PopupWindowImpl IMPL;

    static {
        int n = Build.VERSION.SDK_INT;
        IMPL = n >= 23 ? new Api23PopupWindowImpl() : (n >= 21 ? new Api21PopupWindowImpl() : (n >= 19 ? new KitKatPopupWindowImpl() : (n >= 9 ? new GingerbreadPopupWindowImpl() : new BasePopupWindowImpl())));
    }

    private PopupWindowCompat() {
    }

    public static boolean getOverlapAnchor(PopupWindow popupWindow) {
        return IMPL.getOverlapAnchor(popupWindow);
    }

    public static int getWindowLayoutType(PopupWindow popupWindow) {
        return IMPL.getWindowLayoutType(popupWindow);
    }

    public static void setOverlapAnchor(PopupWindow popupWindow, boolean bl) {
        IMPL.setOverlapAnchor(popupWindow, bl);
    }

    public static void setWindowLayoutType(PopupWindow popupWindow, int n) {
        IMPL.setWindowLayoutType(popupWindow, n);
    }

    public static void showAsDropDown(PopupWindow popupWindow, View view, int n, int n2, int n3) {
        IMPL.showAsDropDown(popupWindow, view, n, n2, n3);
    }

    static class Api21PopupWindowImpl
    extends KitKatPopupWindowImpl {
        Api21PopupWindowImpl() {
        }

        @Override
        public boolean getOverlapAnchor(PopupWindow popupWindow) {
            return PopupWindowCompatApi21.getOverlapAnchor(popupWindow);
        }

        @Override
        public void setOverlapAnchor(PopupWindow popupWindow, boolean bl) {
            PopupWindowCompatApi21.setOverlapAnchor(popupWindow, bl);
        }
    }

    static class Api23PopupWindowImpl
    extends Api21PopupWindowImpl {
        Api23PopupWindowImpl() {
        }

        @Override
        public boolean getOverlapAnchor(PopupWindow popupWindow) {
            return PopupWindowCompatApi23.getOverlapAnchor(popupWindow);
        }

        @Override
        public int getWindowLayoutType(PopupWindow popupWindow) {
            return PopupWindowCompatApi23.getWindowLayoutType(popupWindow);
        }

        @Override
        public void setOverlapAnchor(PopupWindow popupWindow, boolean bl) {
            PopupWindowCompatApi23.setOverlapAnchor(popupWindow, bl);
        }

        @Override
        public void setWindowLayoutType(PopupWindow popupWindow, int n) {
            PopupWindowCompatApi23.setWindowLayoutType(popupWindow, n);
        }
    }

    static class BasePopupWindowImpl
    implements PopupWindowImpl {
        BasePopupWindowImpl() {
        }

        @Override
        public boolean getOverlapAnchor(PopupWindow popupWindow) {
            return false;
        }

        @Override
        public int getWindowLayoutType(PopupWindow popupWindow) {
            return 0;
        }

        @Override
        public void setOverlapAnchor(PopupWindow popupWindow, boolean bl) {
        }

        @Override
        public void setWindowLayoutType(PopupWindow popupWindow, int n) {
        }

        @Override
        public void showAsDropDown(PopupWindow popupWindow, View view, int n, int n2, int n3) {
            popupWindow.showAsDropDown(view, n, n2);
        }
    }

    static class GingerbreadPopupWindowImpl
    extends BasePopupWindowImpl {
        GingerbreadPopupWindowImpl() {
        }

        @Override
        public int getWindowLayoutType(PopupWindow popupWindow) {
            return PopupWindowCompatGingerbread.getWindowLayoutType(popupWindow);
        }

        @Override
        public void setWindowLayoutType(PopupWindow popupWindow, int n) {
            PopupWindowCompatGingerbread.setWindowLayoutType(popupWindow, n);
        }
    }

    static class KitKatPopupWindowImpl
    extends GingerbreadPopupWindowImpl {
        KitKatPopupWindowImpl() {
        }

        @Override
        public void showAsDropDown(PopupWindow popupWindow, View view, int n, int n2, int n3) {
            PopupWindowCompatKitKat.showAsDropDown(popupWindow, view, n, n2, n3);
        }
    }

    static interface PopupWindowImpl {
        public boolean getOverlapAnchor(PopupWindow var1);

        public int getWindowLayoutType(PopupWindow var1);

        public void setOverlapAnchor(PopupWindow var1, boolean var2);

        public void setWindowLayoutType(PopupWindow var1, int var2);

        public void showAsDropDown(PopupWindow var1, View var2, int var3, int var4, int var5);
    }

}

