/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Helper;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.support.constraint.solver.widgets.ResolutionNode;
import java.util.ArrayList;

public class Barrier
extends Helper {
    public static final int BOTTOM = 3;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    private boolean mAllowsGoneWidget = true;
    private int mBarrierType = 0;
    private ArrayList<ResolutionAnchor> mNodes = new ArrayList(4);

    @Override
    public void addToSolver(LinearSystem linearSystem) {
        int n;
        this.mListAnchors[0] = this.mLeft;
        this.mListAnchors[2] = this.mTop;
        this.mListAnchors[1] = this.mRight;
        this.mListAnchors[3] = this.mBottom;
        for (n = 0; n < this.mListAnchors.length; ++n) {
            this.mListAnchors[n].mSolverVariable = linearSystem.createObjectVariable(this.mListAnchors[n]);
        }
        n = this.mBarrierType;
        if (n >= 0 && n < 4) {
            Object object;
            boolean bl;
            int n2;
            ConstraintAnchor constraintAnchor = this.mListAnchors[this.mBarrierType];
            boolean bl2 = false;
            n = 0;
            do {
                bl = bl2;
                if (n >= this.mWidgetsCount) break;
                object = this.mWidgets[n];
                if (this.mAllowsGoneWidget || object.allowedInBarrier()) {
                    n2 = this.mBarrierType;
                    if ((n2 == 0 || n2 == 1) && object.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        bl = true;
                        break;
                    }
                    n2 = this.mBarrierType;
                    if ((n2 == 2 || n2 == 3) && object.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        bl = true;
                        break;
                    }
                }
                ++n;
            } while (true);
            if ((n = this.mBarrierType) != 0 && n != 1) {
                if (this.getParent().getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    bl = false;
                }
            } else if (this.getParent().getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                bl = false;
            }
            for (n = 0; n < this.mWidgetsCount; ++n) {
                ConstraintAnchor[] arrconstraintAnchor = this.mWidgets[n];
                if (!this.mAllowsGoneWidget && !arrconstraintAnchor.allowedInBarrier()) continue;
                object = linearSystem.createObjectVariable(arrconstraintAnchor.mListAnchors[this.mBarrierType]);
                arrconstraintAnchor = arrconstraintAnchor.mListAnchors;
                n2 = this.mBarrierType;
                arrconstraintAnchor[n2].mSolverVariable = object;
                if (n2 != 0 && n2 != 2) {
                    linearSystem.addGreaterBarrier(constraintAnchor.mSolverVariable, (SolverVariable)object, bl);
                    continue;
                }
                linearSystem.addLowerBarrier(constraintAnchor.mSolverVariable, (SolverVariable)object, bl);
            }
            n = this.mBarrierType;
            if (n == 0) {
                linearSystem.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 6);
                if (!bl) {
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 5);
                    return;
                }
            } else if (n == 1) {
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 6);
                if (!bl) {
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 5);
                    return;
                }
            } else if (n == 2) {
                linearSystem.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 6);
                if (!bl) {
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 5);
                    return;
                }
            } else if (n == 3) {
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 6);
                if (!bl) {
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 5);
                }
            }
            return;
        }
    }

    @Override
    public boolean allowedInBarrier() {
        return true;
    }

    public boolean allowsGoneWidget() {
        return this.mAllowsGoneWidget;
    }

    @Override
    public void analyze(int n) {
        ResolutionAnchor resolutionAnchor;
        if (this.mParent == null) {
            return;
        }
        if (!((ConstraintWidgetContainer)this.mParent).optimizeFor(2)) {
            return;
        }
        n = this.mBarrierType;
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        return;
                    }
                    resolutionAnchor = this.mBottom.getResolutionNode();
                } else {
                    resolutionAnchor = this.mTop.getResolutionNode();
                }
            } else {
                resolutionAnchor = this.mRight.getResolutionNode();
            }
        } else {
            resolutionAnchor = this.mLeft.getResolutionNode();
        }
        resolutionAnchor.setType(5);
        n = this.mBarrierType;
        if (n != 0 && n != 1) {
            this.mLeft.getResolutionNode().resolve(null, 0.0f);
            this.mRight.getResolutionNode().resolve(null, 0.0f);
        } else {
            this.mTop.getResolutionNode().resolve(null, 0.0f);
            this.mBottom.getResolutionNode().resolve(null, 0.0f);
        }
        this.mNodes.clear();
        for (n = 0; n < this.mWidgetsCount; ++n) {
            ConstraintWidget constraintWidget = this.mWidgets[n];
            if (!this.mAllowsGoneWidget && !constraintWidget.allowedInBarrier()) continue;
            ResolutionAnchor resolutionAnchor2 = null;
            int n2 = this.mBarrierType;
            if (n2 != 0) {
                if (n2 != 1) {
                    if (n2 != 2) {
                        if (n2 == 3) {
                            resolutionAnchor2 = constraintWidget.mBottom.getResolutionNode();
                        }
                    } else {
                        resolutionAnchor2 = constraintWidget.mTop.getResolutionNode();
                    }
                } else {
                    resolutionAnchor2 = constraintWidget.mRight.getResolutionNode();
                }
            } else {
                resolutionAnchor2 = constraintWidget.mLeft.getResolutionNode();
            }
            if (resolutionAnchor2 == null) continue;
            this.mNodes.add(resolutionAnchor2);
            resolutionAnchor2.addDependent(resolutionAnchor);
        }
    }

    @Override
    public void resetResolutionNodes() {
        super.resetResolutionNodes();
        this.mNodes.clear();
    }

    @Override
    public void resolve() {
        ResolutionAnchor resolutionAnchor;
        Object object;
        float f = 0.0f;
        int n = this.mBarrierType;
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        return;
                    }
                    resolutionAnchor = this.mBottom.getResolutionNode();
                } else {
                    resolutionAnchor = this.mTop.getResolutionNode();
                    f = Float.MAX_VALUE;
                }
            } else {
                resolutionAnchor = this.mRight.getResolutionNode();
            }
        } else {
            resolutionAnchor = this.mLeft.getResolutionNode();
            f = Float.MAX_VALUE;
        }
        int n2 = this.mNodes.size();
        ResolutionAnchor resolutionAnchor2 = null;
        float f2 = f;
        for (n = 0; n < n2; ++n) {
            object = this.mNodes.get(n);
            if (object.state != 1) {
                return;
            }
            int n3 = this.mBarrierType;
            if (n3 != 0 && n3 != 2) {
                f = f2;
                if (object.resolvedOffset > f2) {
                    f = object.resolvedOffset;
                    resolutionAnchor2 = object.resolvedTarget;
                }
            } else {
                f = f2;
                if (object.resolvedOffset < f2) {
                    f = object.resolvedOffset;
                    resolutionAnchor2 = object.resolvedTarget;
                }
            }
            f2 = f;
        }
        if (LinearSystem.getMetrics() != null) {
            object = LinearSystem.getMetrics();
            ++object.barrierConnectionResolved;
        }
        resolutionAnchor.resolvedTarget = resolutionAnchor2;
        resolutionAnchor.resolvedOffset = f2;
        resolutionAnchor.didResolve();
        n = this.mBarrierType;
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        return;
                    }
                    this.mTop.getResolutionNode().resolve(resolutionAnchor2, f2);
                    return;
                }
                this.mBottom.getResolutionNode().resolve(resolutionAnchor2, f2);
                return;
            }
            this.mLeft.getResolutionNode().resolve(resolutionAnchor2, f2);
            return;
        }
        this.mRight.getResolutionNode().resolve(resolutionAnchor2, f2);
    }

    public void setAllowsGoneWidget(boolean bl) {
        this.mAllowsGoneWidget = bl;
    }

    public void setBarrierType(int n) {
        this.mBarrierType = n;
    }
}

