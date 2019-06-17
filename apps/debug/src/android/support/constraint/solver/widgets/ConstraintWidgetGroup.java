/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Helper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstraintWidgetGroup {
    public List<ConstraintWidget> mConstrainedGroup;
    public final int[] mGroupDimensions;
    int mGroupHeight = -1;
    int mGroupWidth = -1;
    public boolean mSkipSolver = false;
    List<ConstraintWidget> mStartHorizontalWidgets;
    List<ConstraintWidget> mStartVerticalWidgets;
    List<ConstraintWidget> mUnresolvedWidgets;
    HashSet<ConstraintWidget> mWidgetsToSetHorizontal;
    HashSet<ConstraintWidget> mWidgetsToSetVertical;
    List<ConstraintWidget> mWidgetsToSolve;

    ConstraintWidgetGroup(List<ConstraintWidget> list) {
        this.mGroupDimensions = new int[]{this.mGroupWidth, this.mGroupHeight};
        this.mStartHorizontalWidgets = new ArrayList<ConstraintWidget>();
        this.mStartVerticalWidgets = new ArrayList<ConstraintWidget>();
        this.mWidgetsToSetHorizontal = new HashSet();
        this.mWidgetsToSetVertical = new HashSet();
        this.mWidgetsToSolve = new ArrayList<ConstraintWidget>();
        this.mUnresolvedWidgets = new ArrayList<ConstraintWidget>();
        this.mConstrainedGroup = list;
    }

    ConstraintWidgetGroup(List<ConstraintWidget> list, boolean bl) {
        this.mGroupDimensions = new int[]{this.mGroupWidth, this.mGroupHeight};
        this.mStartHorizontalWidgets = new ArrayList<ConstraintWidget>();
        this.mStartVerticalWidgets = new ArrayList<ConstraintWidget>();
        this.mWidgetsToSetHorizontal = new HashSet();
        this.mWidgetsToSetVertical = new HashSet();
        this.mWidgetsToSolve = new ArrayList<ConstraintWidget>();
        this.mUnresolvedWidgets = new ArrayList<ConstraintWidget>();
        this.mConstrainedGroup = list;
        this.mSkipSolver = bl;
    }

    private void getWidgetsToSolveTraversal(ArrayList<ConstraintWidget> arrayList, ConstraintWidget constraintWidget) {
        Object object;
        int n;
        int n2;
        if (constraintWidget.mGroupsToSolver) {
            return;
        }
        arrayList.add(constraintWidget);
        constraintWidget.mGroupsToSolver = true;
        if (constraintWidget.isFullyResolved()) {
            return;
        }
        if (constraintWidget instanceof Helper) {
            object = (Helper)constraintWidget;
            n2 = object.mWidgetsCount;
            for (n = 0; n < n2; ++n) {
                this.getWidgetsToSolveTraversal(arrayList, object.mWidgets[n]);
            }
        }
        n2 = constraintWidget.mListAnchors.length;
        for (n = 0; n < n2; ++n) {
            object = constraintWidget.mListAnchors[n].mTarget;
            if (object == null) continue;
            ConstraintWidget constraintWidget2 = object.mOwner;
            if (object == null || constraintWidget2 == constraintWidget.getParent()) continue;
            this.getWidgetsToSolveTraversal(arrayList, constraintWidget2);
        }
    }

    private void updateResolvedDimension(ConstraintWidget constraintWidget) {
        int n = 0;
        if (constraintWidget.mOptimizerMeasurable) {
            if (constraintWidget.isFullyResolved()) {
                return;
            }
            ConstraintAnchor constraintAnchor = constraintWidget.mRight.mTarget;
            boolean bl = false;
            int n2 = constraintAnchor != null ? 1 : 0;
            constraintAnchor = n2 != 0 ? constraintWidget.mRight.mTarget : constraintWidget.mLeft.mTarget;
            int n3 = n;
            if (constraintAnchor != null) {
                if (!constraintAnchor.mOwner.mOptimizerMeasured) {
                    this.updateResolvedDimension(constraintAnchor.mOwner);
                }
                if (constraintAnchor.mType == ConstraintAnchor.Type.RIGHT) {
                    n3 = constraintAnchor.mOwner.mX + constraintAnchor.mOwner.getWidth();
                } else {
                    n3 = n;
                    if (constraintAnchor.mType == ConstraintAnchor.Type.LEFT) {
                        n3 = constraintAnchor.mOwner.mX;
                    }
                }
            }
            n3 = n2 != 0 ? (n3 -= constraintWidget.mRight.getMargin()) : (n3 += constraintWidget.mLeft.getMargin() + constraintWidget.getWidth());
            constraintWidget.setHorizontalDimension(n3 - constraintWidget.getWidth(), n3);
            if (constraintWidget.mBaseline.mTarget != null) {
                constraintAnchor = constraintWidget.mBaseline.mTarget;
                if (!constraintAnchor.mOwner.mOptimizerMeasured) {
                    this.updateResolvedDimension(constraintAnchor.mOwner);
                }
                n3 = constraintAnchor.mOwner.mY + constraintAnchor.mOwner.mBaselineDistance - constraintWidget.mBaselineDistance;
                constraintWidget.setVerticalDimension(n3, constraintWidget.mHeight + n3);
                constraintWidget.mOptimizerMeasured = true;
                return;
            }
            if (constraintWidget.mBottom.mTarget != null) {
                bl = true;
            }
            constraintAnchor = bl ? constraintWidget.mBottom.mTarget : constraintWidget.mTop.mTarget;
            n2 = n3;
            if (constraintAnchor != null) {
                if (!constraintAnchor.mOwner.mOptimizerMeasured) {
                    this.updateResolvedDimension(constraintAnchor.mOwner);
                }
                if (constraintAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
                    n2 = constraintAnchor.mOwner.mY + constraintAnchor.mOwner.getHeight();
                } else {
                    n2 = n3;
                    if (constraintAnchor.mType == ConstraintAnchor.Type.TOP) {
                        n2 = constraintAnchor.mOwner.mY;
                    }
                }
            }
            n3 = bl ? n2 - constraintWidget.mBottom.getMargin() : n2 + (constraintWidget.mTop.getMargin() + constraintWidget.getHeight());
            constraintWidget.setVerticalDimension(n3 - constraintWidget.getHeight(), n3);
            constraintWidget.mOptimizerMeasured = true;
        }
    }

    void addWidgetsToSet(ConstraintWidget constraintWidget, int n) {
        if (n == 0) {
            this.mWidgetsToSetHorizontal.add(constraintWidget);
            return;
        }
        if (n == 1) {
            this.mWidgetsToSetVertical.add(constraintWidget);
        }
    }

    public List<ConstraintWidget> getStartWidgets(int n) {
        if (n == 0) {
            return this.mStartHorizontalWidgets;
        }
        if (n == 1) {
            return this.mStartVerticalWidgets;
        }
        return null;
    }

    Set<ConstraintWidget> getWidgetsToSet(int n) {
        if (n == 0) {
            return this.mWidgetsToSetHorizontal;
        }
        if (n == 1) {
            return this.mWidgetsToSetVertical;
        }
        return null;
    }

    List<ConstraintWidget> getWidgetsToSolve() {
        if (!this.mWidgetsToSolve.isEmpty()) {
            return this.mWidgetsToSolve;
        }
        int n = this.mConstrainedGroup.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = this.mConstrainedGroup.get(i);
            if (constraintWidget.mOptimizerMeasurable) continue;
            this.getWidgetsToSolveTraversal((ArrayList)this.mWidgetsToSolve, constraintWidget);
        }
        this.mUnresolvedWidgets.clear();
        this.mUnresolvedWidgets.addAll(this.mConstrainedGroup);
        this.mUnresolvedWidgets.removeAll(this.mWidgetsToSolve);
        return this.mWidgetsToSolve;
    }

    void updateUnresolvedWidgets() {
        int n = this.mUnresolvedWidgets.size();
        for (int i = 0; i < n; ++i) {
            this.updateResolvedDimension(this.mUnresolvedWidgets.get(i));
        }
    }
}

