/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 */
package android.support.design.widget;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

class ViewGroupUtilsHoneycomb {
    private static final Matrix IDENTITY;
    private static final ThreadLocal<Matrix> sMatrix;
    private static final ThreadLocal<RectF> sRectF;

    static {
        sMatrix = new ThreadLocal();
        sRectF = new ThreadLocal();
        IDENTITY = new Matrix();
    }

    ViewGroupUtilsHoneycomb() {
    }

    static void offsetDescendantMatrix(ViewParent viewParent, View view, Matrix matrix) {
        ViewParent viewParent2 = view.getParent();
        if (viewParent2 instanceof View && viewParent2 != viewParent) {
            viewParent2 = (View)viewParent2;
            ViewGroupUtilsHoneycomb.offsetDescendantMatrix(viewParent, (View)viewParent2, matrix);
            matrix.preTranslate((float)(- viewParent2.getScrollX()), (float)(- viewParent2.getScrollY()));
        }
        matrix.preTranslate((float)view.getLeft(), (float)view.getTop());
        if (!view.getMatrix().isIdentity()) {
            matrix.preConcat(view.getMatrix());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void offsetDescendantRect(ViewGroup viewGroup, View view, Rect rect) {
        Matrix matrix = sMatrix.get();
        if (matrix == null) {
            matrix = new Matrix();
            sMatrix.set(matrix);
        } else {
            matrix.set(IDENTITY);
        }
        ViewGroupUtilsHoneycomb.offsetDescendantMatrix((ViewParent)viewGroup, view, matrix);
        view = sRectF.get();
        viewGroup = view;
        if (view == null) {
            viewGroup = new RectF();
        }
        viewGroup.set(rect);
        matrix.mapRect((RectF)viewGroup);
        rect.set((int)(viewGroup.left + 0.5f), (int)(viewGroup.top + 0.5f), (int)(viewGroup.right + 0.5f), (int)(viewGroup.bottom + 0.5f));
    }
}

