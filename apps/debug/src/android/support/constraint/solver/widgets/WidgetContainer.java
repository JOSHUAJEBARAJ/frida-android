/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Rectangle;
import java.util.ArrayList;

public class WidgetContainer
extends ConstraintWidget {
    protected ArrayList<ConstraintWidget> mChildren = new ArrayList();

    public WidgetContainer() {
    }

    public WidgetContainer(int n, int n2) {
        super(n, n2);
    }

    public WidgetContainer(int n, int n2, int n3, int n4) {
        super(n, n2, n3, n4);
    }

    public static Rectangle getBounds(ArrayList<ConstraintWidget> arrayList) {
        Rectangle rectangle = new Rectangle();
        if (arrayList.size() == 0) {
            return rectangle;
        }
        int n = Integer.MAX_VALUE;
        int n2 = 0;
        int n3 = Integer.MAX_VALUE;
        int n4 = 0;
        int n5 = arrayList.size();
        for (int i = 0; i < n5; ++i) {
            ConstraintWidget constraintWidget = arrayList.get(i);
            int n6 = n;
            if (constraintWidget.getX() < n) {
                n6 = constraintWidget.getX();
            }
            int n7 = n3;
            if (constraintWidget.getY() < n3) {
                n7 = constraintWidget.getY();
            }
            n3 = n2;
            if (constraintWidget.getRight() > n2) {
                n3 = constraintWidget.getRight();
            }
            int n8 = n4;
            if (constraintWidget.getBottom() > n4) {
                n8 = constraintWidget.getBottom();
            }
            n = n6;
            n2 = n3;
            n3 = n7;
            n4 = n8;
        }
        rectangle.setBounds(n, n3, n2 - n, n4 - n3);
        return rectangle;
    }

    public void add(ConstraintWidget constraintWidget) {
        this.mChildren.add(constraintWidget);
        if (constraintWidget.getParent() != null) {
            ((WidgetContainer)constraintWidget.getParent()).remove(constraintWidget);
        }
        constraintWidget.setParent(this);
    }

    public /* varargs */ void add(ConstraintWidget ... arrconstraintWidget) {
        int n = arrconstraintWidget.length;
        for (int i = 0; i < n; ++i) {
            this.add(arrconstraintWidget[i]);
        }
    }

    public ConstraintWidget findWidget(float f, float f2) {
        ConstraintWidget constraintWidget = null;
        int n = this.getDrawX();
        int n2 = this.getDrawY();
        int n3 = this.getWidth();
        int n4 = this.getHeight();
        ConstraintWidget constraintWidget2 = constraintWidget;
        if (f >= (float)n) {
            constraintWidget2 = constraintWidget;
            if (f <= (float)(n3 + n)) {
                constraintWidget2 = constraintWidget;
                if (f2 >= (float)n2) {
                    constraintWidget2 = constraintWidget;
                    if (f2 <= (float)(n4 + n2)) {
                        constraintWidget2 = this;
                    }
                }
            }
        }
        n2 = this.mChildren.size();
        for (n = 0; n < n2; ++n) {
            constraintWidget = this.mChildren.get(n);
            if (constraintWidget instanceof WidgetContainer) {
                if ((constraintWidget = ((WidgetContainer)constraintWidget).findWidget(f, f2)) == null) continue;
                constraintWidget2 = constraintWidget;
                continue;
            }
            n3 = constraintWidget.getDrawX();
            n4 = constraintWidget.getDrawY();
            int n5 = constraintWidget.getWidth();
            int n6 = constraintWidget.getHeight();
            if (f < (float)n3 || f > (float)(n5 + n3) || f2 < (float)n4 || f2 > (float)(n6 + n4)) continue;
            constraintWidget2 = constraintWidget;
        }
        return constraintWidget2;
    }

    public ArrayList<ConstraintWidget> findWidgets(int n, int n2, int n3, int n4) {
        ArrayList<ConstraintWidget> arrayList = new ArrayList<ConstraintWidget>();
        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(n, n2, n3, n4);
        n2 = this.mChildren.size();
        for (n = 0; n < n2; ++n) {
            ConstraintWidget constraintWidget = this.mChildren.get(n);
            Rectangle rectangle2 = new Rectangle();
            rectangle2.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
            if (!rectangle.intersects(rectangle2)) continue;
            arrayList.add(constraintWidget);
        }
        return arrayList;
    }

    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }

    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2 = this.getParent();
        ConstraintWidgetContainer constraintWidgetContainer = null;
        ConstraintWidget constraintWidget3 = constraintWidget2;
        if (this instanceof ConstraintWidgetContainer) {
            constraintWidgetContainer = (ConstraintWidgetContainer)this;
            constraintWidget3 = constraintWidget2;
        }
        while ((constraintWidget = constraintWidget3) != null) {
            constraintWidget3 = constraintWidget2 = constraintWidget.getParent();
            if (!(constraintWidget instanceof ConstraintWidgetContainer)) continue;
            constraintWidgetContainer = (ConstraintWidgetContainer)constraintWidget;
            constraintWidget3 = constraintWidget2;
        }
        return constraintWidgetContainer;
    }

    public void layout() {
        this.updateDrawPosition();
        Object object = this.mChildren;
        if (object == null) {
            return;
        }
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            object = this.mChildren.get(i);
            if (!(object instanceof WidgetContainer)) continue;
            ((WidgetContainer)object).layout();
        }
    }

    public void remove(ConstraintWidget constraintWidget) {
        this.mChildren.remove(constraintWidget);
        constraintWidget.setParent(null);
    }

    public void removeAllChildren() {
        this.mChildren.clear();
    }

    @Override
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }

    @Override
    public void resetSolverVariables(Cache cache) {
        super.resetSolverVariables(cache);
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }

    @Override
    public void setOffset(int n, int n2) {
        super.setOffset(n, n2);
        n2 = this.mChildren.size();
        for (n = 0; n < n2; ++n) {
            this.mChildren.get(n).setOffset(this.getRootX(), this.getRootY());
        }
    }

    @Override
    public void updateDrawPosition() {
        super.updateDrawPosition();
        Object object = this.mChildren;
        if (object == null) {
            return;
        }
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            object = this.mChildren.get(i);
            object.setOffset(this.getDrawX(), this.getDrawY());
            if (object instanceof ConstraintWidgetContainer) continue;
            object.updateDrawPosition();
        }
    }
}

