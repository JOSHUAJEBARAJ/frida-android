/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionInfo
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo
 *  android.view.accessibility.AccessibilityNodeInfo$RangeInfo
 */
package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

class AccessibilityNodeInfoCompatKitKat {
    AccessibilityNodeInfoCompatKitKat() {
    }

    public static boolean canOpenPopup(Object object) {
        return ((AccessibilityNodeInfo)object).canOpenPopup();
    }

    static Object getCollectionInfo(Object object) {
        return ((AccessibilityNodeInfo)object).getCollectionInfo();
    }

    static Object getCollectionItemInfo(Object object) {
        return ((AccessibilityNodeInfo)object).getCollectionItemInfo();
    }

    public static Bundle getExtras(Object object) {
        return ((AccessibilityNodeInfo)object).getExtras();
    }

    public static int getInputType(Object object) {
        return ((AccessibilityNodeInfo)object).getInputType();
    }

    static int getLiveRegion(Object object) {
        return ((AccessibilityNodeInfo)object).getLiveRegion();
    }

    static Object getRangeInfo(Object object) {
        return ((AccessibilityNodeInfo)object).getRangeInfo();
    }

    public static boolean isContentInvalid(Object object) {
        return ((AccessibilityNodeInfo)object).isContentInvalid();
    }

    public static boolean isDismissable(Object object) {
        return ((AccessibilityNodeInfo)object).isDismissable();
    }

    public static boolean isMultiLine(Object object) {
        return ((AccessibilityNodeInfo)object).isMultiLine();
    }

    public static Object obtainCollectionInfo(int n, int n2, boolean bl, int n3) {
        return AccessibilityNodeInfo.CollectionInfo.obtain((int)n, (int)n2, (boolean)bl);
    }

    public static Object obtainCollectionItemInfo(int n, int n2, int n3, int n4, boolean bl) {
        return AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n, (int)n2, (int)n3, (int)n4, (boolean)bl);
    }

    public static void setCanOpenPopup(Object object, boolean bl) {
        ((AccessibilityNodeInfo)object).setCanOpenPopup(bl);
    }

    public static void setCollectionInfo(Object object, Object object2) {
        ((AccessibilityNodeInfo)object).setCollectionInfo((AccessibilityNodeInfo.CollectionInfo)object2);
    }

    public static void setCollectionItemInfo(Object object, Object object2) {
        ((AccessibilityNodeInfo)object).setCollectionItemInfo((AccessibilityNodeInfo.CollectionItemInfo)object2);
    }

    public static void setContentInvalid(Object object, boolean bl) {
        ((AccessibilityNodeInfo)object).setContentInvalid(bl);
    }

    public static void setDismissable(Object object, boolean bl) {
        ((AccessibilityNodeInfo)object).setDismissable(bl);
    }

    public static void setInputType(Object object, int n) {
        ((AccessibilityNodeInfo)object).setInputType(n);
    }

    static void setLiveRegion(Object object, int n) {
        ((AccessibilityNodeInfo)object).setLiveRegion(n);
    }

    public static void setMultiLine(Object object, boolean bl) {
        ((AccessibilityNodeInfo)object).setMultiLine(bl);
    }

    public static void setRangeInfo(Object object, Object object2) {
        ((AccessibilityNodeInfo)object).setRangeInfo((AccessibilityNodeInfo.RangeInfo)object2);
    }

    static class CollectionInfo {
        CollectionInfo() {
        }

        static int getColumnCount(Object object) {
            return ((AccessibilityNodeInfo.CollectionInfo)object).getColumnCount();
        }

        static int getRowCount(Object object) {
            return ((AccessibilityNodeInfo.CollectionInfo)object).getRowCount();
        }

        static boolean isHierarchical(Object object) {
            return ((AccessibilityNodeInfo.CollectionInfo)object).isHierarchical();
        }
    }

    static class CollectionItemInfo {
        CollectionItemInfo() {
        }

        static int getColumnIndex(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).getColumnIndex();
        }

        static int getColumnSpan(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).getColumnSpan();
        }

        static int getRowIndex(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).getRowIndex();
        }

        static int getRowSpan(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).getRowSpan();
        }

        static boolean isHeading(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).isHeading();
        }
    }

    static class RangeInfo {
        RangeInfo() {
        }

        static float getCurrent(Object object) {
            return ((AccessibilityNodeInfo.RangeInfo)object).getCurrent();
        }

        static float getMax(Object object) {
            return ((AccessibilityNodeInfo.RangeInfo)object).getMax();
        }

        static float getMin(Object object) {
            return ((AccessibilityNodeInfo.RangeInfo)object).getMin();
        }

        static int getType(Object object) {
            return ((AccessibilityNodeInfo.RangeInfo)object).getType();
        }
    }

}

