/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityWindowInfoCompatApi21;

public class AccessibilityWindowInfoCompat {
    private static final AccessibilityWindowInfoImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new AccessibilityWindowInfoApi21Impl() : new AccessibilityWindowInfoStubImpl();
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private Object mInfo;

    private AccessibilityWindowInfoCompat(Object object) {
        this.mInfo = object;
    }

    public static AccessibilityWindowInfoCompat obtain() {
        return AccessibilityWindowInfoCompat.wrapNonNullInstance(IMPL.obtain());
    }

    public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat accessibilityWindowInfoCompat) {
        return AccessibilityWindowInfoCompat.wrapNonNullInstance(IMPL.obtain(accessibilityWindowInfoCompat.mInfo));
    }

    private static String typeToString(int n) {
        switch (n) {
            default: {
                return "<UNKNOWN>";
            }
            case 1: {
                return "TYPE_APPLICATION";
            }
            case 2: {
                return "TYPE_INPUT_METHOD";
            }
            case 3: {
                return "TYPE_SYSTEM";
            }
            case 4: 
        }
        return "TYPE_ACCESSIBILITY_OVERLAY";
    }

    static AccessibilityWindowInfoCompat wrapNonNullInstance(Object object) {
        if (object != null) {
            return new AccessibilityWindowInfoCompat(object);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        object = (AccessibilityWindowInfoCompat)object;
        if (this.mInfo == null) {
            if (object.mInfo == null) return true;
            return false;
        }
        if (!this.mInfo.equals(object.mInfo)) return false;
        return true;
    }

    public void getBoundsInScreen(Rect rect) {
        IMPL.getBoundsInScreen(this.mInfo, rect);
    }

    public AccessibilityWindowInfoCompat getChild(int n) {
        return AccessibilityWindowInfoCompat.wrapNonNullInstance(IMPL.getChild(this.mInfo, n));
    }

    public int getChildCount() {
        return IMPL.getChildCount(this.mInfo);
    }

    public int getId() {
        return IMPL.getId(this.mInfo);
    }

    public int getLayer() {
        return IMPL.getLayer(this.mInfo);
    }

    public AccessibilityWindowInfoCompat getParent() {
        return AccessibilityWindowInfoCompat.wrapNonNullInstance(IMPL.getParent(this.mInfo));
    }

    public AccessibilityNodeInfoCompat getRoot() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getRoot(this.mInfo));
    }

    public int getType() {
        return IMPL.getType(this.mInfo);
    }

    public int hashCode() {
        if (this.mInfo == null) {
            return 0;
        }
        return this.mInfo.hashCode();
    }

    public boolean isAccessibilityFocused() {
        return IMPL.isAccessibilityFocused(this.mInfo);
    }

    public boolean isActive() {
        return IMPL.isActive(this.mInfo);
    }

    public boolean isFocused() {
        return IMPL.isFocused(this.mInfo);
    }

    public void recycle() {
        IMPL.recycle(this.mInfo);
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        boolean bl = true;
        StringBuilder stringBuilder = new StringBuilder();
        Object object = new Rect();
        this.getBoundsInScreen((Rect)object);
        stringBuilder.append("AccessibilityWindowInfo[");
        stringBuilder.append("id=").append(this.getId());
        stringBuilder.append(", type=").append(AccessibilityWindowInfoCompat.typeToString(this.getType()));
        stringBuilder.append(", layer=").append(this.getLayer());
        stringBuilder.append(", bounds=").append(object);
        stringBuilder.append(", focused=").append(this.isFocused());
        stringBuilder.append(", active=").append(this.isActive());
        object = stringBuilder.append(", hasParent=");
        boolean bl2 = this.getParent() != null;
        object.append(bl2);
        object = stringBuilder.append(", hasChildren=");
        bl2 = this.getChildCount() > 0 ? bl : false;
        object.append(bl2);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    private static class AccessibilityWindowInfoApi21Impl
    extends AccessibilityWindowInfoStubImpl {
        private AccessibilityWindowInfoApi21Impl() {
            super();
        }

        @Override
        public void getBoundsInScreen(Object object, Rect rect) {
            AccessibilityWindowInfoCompatApi21.getBoundsInScreen(object, rect);
        }

        @Override
        public Object getChild(Object object, int n) {
            return AccessibilityWindowInfoCompatApi21.getChild(object, n);
        }

        @Override
        public int getChildCount(Object object) {
            return AccessibilityWindowInfoCompatApi21.getChildCount(object);
        }

        @Override
        public int getId(Object object) {
            return AccessibilityWindowInfoCompatApi21.getId(object);
        }

        @Override
        public int getLayer(Object object) {
            return AccessibilityWindowInfoCompatApi21.getLayer(object);
        }

        @Override
        public Object getParent(Object object) {
            return AccessibilityWindowInfoCompatApi21.getParent(object);
        }

        @Override
        public Object getRoot(Object object) {
            return AccessibilityWindowInfoCompatApi21.getRoot(object);
        }

        @Override
        public int getType(Object object) {
            return AccessibilityWindowInfoCompatApi21.getType(object);
        }

        @Override
        public boolean isAccessibilityFocused(Object object) {
            return AccessibilityWindowInfoCompatApi21.isAccessibilityFocused(object);
        }

        @Override
        public boolean isActive(Object object) {
            return AccessibilityWindowInfoCompatApi21.isActive(object);
        }

        @Override
        public boolean isFocused(Object object) {
            return AccessibilityWindowInfoCompatApi21.isFocused(object);
        }

        @Override
        public Object obtain() {
            return AccessibilityWindowInfoCompatApi21.obtain();
        }

        @Override
        public Object obtain(Object object) {
            return AccessibilityWindowInfoCompatApi21.obtain(object);
        }

        @Override
        public void recycle(Object object) {
            AccessibilityWindowInfoCompatApi21.recycle(object);
        }
    }

    private static interface AccessibilityWindowInfoImpl {
        public void getBoundsInScreen(Object var1, Rect var2);

        public Object getChild(Object var1, int var2);

        public int getChildCount(Object var1);

        public int getId(Object var1);

        public int getLayer(Object var1);

        public Object getParent(Object var1);

        public Object getRoot(Object var1);

        public int getType(Object var1);

        public boolean isAccessibilityFocused(Object var1);

        public boolean isActive(Object var1);

        public boolean isFocused(Object var1);

        public Object obtain();

        public Object obtain(Object var1);

        public void recycle(Object var1);
    }

    private static class AccessibilityWindowInfoStubImpl
    implements AccessibilityWindowInfoImpl {
        private AccessibilityWindowInfoStubImpl() {
        }

        @Override
        public void getBoundsInScreen(Object object, Rect rect) {
        }

        @Override
        public Object getChild(Object object, int n) {
            return null;
        }

        @Override
        public int getChildCount(Object object) {
            return 0;
        }

        @Override
        public int getId(Object object) {
            return -1;
        }

        @Override
        public int getLayer(Object object) {
            return -1;
        }

        @Override
        public Object getParent(Object object) {
            return null;
        }

        @Override
        public Object getRoot(Object object) {
            return null;
        }

        @Override
        public int getType(Object object) {
            return -1;
        }

        @Override
        public boolean isAccessibilityFocused(Object object) {
            return true;
        }

        @Override
        public boolean isActive(Object object) {
            return true;
        }

        @Override
        public boolean isFocused(Object object) {
            return true;
        }

        @Override
        public Object obtain() {
            return null;
        }

        @Override
        public Object obtain(Object object) {
            return null;
        }

        @Override
        public void recycle(Object object) {
        }
    }

}

