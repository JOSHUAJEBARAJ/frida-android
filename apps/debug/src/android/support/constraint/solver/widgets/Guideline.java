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
import android.support.constraint.solver.widgets.Rectangle;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import java.util.ArrayList;

public class Guideline
extends ConstraintWidget {
    public static final int HORIZONTAL = 0;
    public static final int RELATIVE_BEGIN = 1;
    public static final int RELATIVE_END = 2;
    public static final int RELATIVE_PERCENT = 0;
    public static final int RELATIVE_UNKNWON = -1;
    public static final int VERTICAL = 1;
    private ConstraintAnchor mAnchor;
    private Rectangle mHead;
    private int mHeadSize;
    private boolean mIsPositionRelaxed;
    private int mMinimumPosition;
    private int mOrientation;
    protected int mRelativeBegin = -1;
    protected int mRelativeEnd = -1;
    protected float mRelativePercent = -1.0f;

    public Guideline() {
        this.mAnchor = this.mTop;
        this.mOrientation = 0;
        this.mIsPositionRelaxed = false;
        this.mMinimumPosition = 0;
        this.mHead = new Rectangle();
        this.mHeadSize = 8;
        this.mAnchors.clear();
        this.mAnchors.add(this.mAnchor);
        int n = this.mListAnchors.length;
        for (int i = 0; i < n; ++i) {
            this.mListAnchors[i] = this.mAnchor;
        }
    }

    @Override
    public void addToSolver(LinearSystem linearSystem) {
        Object object = (ConstraintWidgetContainer)this.getParent();
        if (object == null) {
            return;
        }
        ConstraintAnchor constraintAnchor = object.getAnchor(ConstraintAnchor.Type.LEFT);
        Object object2 = object.getAnchor(ConstraintAnchor.Type.RIGHT);
        ConstraintWidget constraintWidget = this.mParent;
        boolean bl = true;
        boolean bl2 = constraintWidget != null && this.mParent.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (this.mOrientation == 0) {
            constraintAnchor = object.getAnchor(ConstraintAnchor.Type.TOP);
            object2 = object.getAnchor(ConstraintAnchor.Type.BOTTOM);
            bl2 = this.mParent != null && this.mParent.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? bl : false;
        }
        if (this.mRelativeBegin != -1) {
            object = linearSystem.createObjectVariable(this.mAnchor);
            linearSystem.addEquality((SolverVariable)object, linearSystem.createObjectVariable(constraintAnchor), this.mRelativeBegin, 6);
            if (bl2) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(object2), (SolverVariable)object, 0, 5);
            }
            return;
        }
        if (this.mRelativeEnd != -1) {
            object = linearSystem.createObjectVariable(this.mAnchor);
            object2 = linearSystem.createObjectVariable(object2);
            linearSystem.addEquality((SolverVariable)object, (SolverVariable)object2, - this.mRelativeEnd, 6);
            if (bl2) {
                linearSystem.addGreaterThan((SolverVariable)object, linearSystem.createObjectVariable(constraintAnchor), 0, 5);
                linearSystem.addGreaterThan((SolverVariable)object2, (SolverVariable)object, 0, 5);
            }
        } else if (this.mRelativePercent != -1.0f) {
            linearSystem.addConstraint(LinearSystem.createRowDimensionPercent(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(constraintAnchor), linearSystem.createObjectVariable(object2), this.mRelativePercent, this.mIsPositionRelaxed));
            return;
        }
    }

    @Override
    public boolean allowedInBarrier() {
        return true;
    }

    @Override
    public void analyze(int n) {
        ConstraintWidget constraintWidget = this.getParent();
        if (constraintWidget == null) {
            return;
        }
        if (this.getOrientation() == 1) {
            this.mTop.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), 0);
            this.mBottom.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), 0);
            if (this.mRelativeBegin != -1) {
                this.mLeft.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), this.mRelativeBegin);
                this.mRight.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), this.mRelativeBegin);
                return;
            }
            if (this.mRelativeEnd != -1) {
                this.mLeft.getResolutionNode().dependsOn(1, constraintWidget.mRight.getResolutionNode(), - this.mRelativeEnd);
                this.mRight.getResolutionNode().dependsOn(1, constraintWidget.mRight.getResolutionNode(), - this.mRelativeEnd);
                return;
            }
            if (this.mRelativePercent != -1.0f && constraintWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
                n = (int)((float)constraintWidget.mWidth * this.mRelativePercent);
                this.mLeft.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), n);
                this.mRight.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), n);
                return;
            }
        } else {
            this.mLeft.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), 0);
            this.mRight.getResolutionNode().dependsOn(1, constraintWidget.mLeft.getResolutionNode(), 0);
            if (this.mRelativeBegin != -1) {
                this.mTop.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), this.mRelativeBegin);
                this.mBottom.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), this.mRelativeBegin);
                return;
            }
            if (this.mRelativeEnd != -1) {
                this.mTop.getResolutionNode().dependsOn(1, constraintWidget.mBottom.getResolutionNode(), - this.mRelativeEnd);
                this.mBottom.getResolutionNode().dependsOn(1, constraintWidget.mBottom.getResolutionNode(), - this.mRelativeEnd);
                return;
            }
            if (this.mRelativePercent != -1.0f && constraintWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED) {
                n = (int)((float)constraintWidget.mHeight * this.mRelativePercent);
                this.mTop.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), n);
                this.mBottom.getResolutionNode().dependsOn(1, constraintWidget.mTop.getResolutionNode(), n);
            }
        }
    }

    public void cyclePosition() {
        if (this.mRelativeBegin != -1) {
            this.inferRelativePercentPosition();
            return;
        }
        if (this.mRelativePercent != -1.0f) {
            this.inferRelativeEndPosition();
            return;
        }
        if (this.mRelativeEnd != -1) {
            this.inferRelativeBeginPosition();
        }
    }

    public ConstraintAnchor getAnchor() {
        return this.mAnchor;
    }

    @Override
    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                break;
            }
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: {
                return null;
            }
            case 3: 
            case 4: {
                if (this.mOrientation != 0) break;
                return this.mAnchor;
            }
            case 1: 
            case 2: {
                if (this.mOrientation != 1) break;
                return this.mAnchor;
            }
        }
        throw new AssertionError((Object)type.name());
    }

    @Override
    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public Rectangle getHead() {
        Rectangle rectangle = this.mHead;
        int n = this.getDrawX();
        int n2 = this.mHeadSize;
        int n3 = this.getDrawY();
        int n4 = this.mHeadSize;
        rectangle.setBounds(n - n2, n3 - n4 * 2, n4 * 2, n4 * 2);
        if (this.getOrientation() == 0) {
            rectangle = this.mHead;
            n = this.getDrawX();
            n2 = this.mHeadSize;
            n3 = this.getDrawY();
            n4 = this.mHeadSize;
            rectangle.setBounds(n - n2 * 2, n3 - n4, n4 * 2, n4 * 2);
        }
        return this.mHead;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getRelativeBegin() {
        return this.mRelativeBegin;
    }

    public int getRelativeBehaviour() {
        if (this.mRelativePercent != -1.0f) {
            return 0;
        }
        if (this.mRelativeBegin != -1) {
            return 1;
        }
        if (this.mRelativeEnd != -1) {
            return 2;
        }
        return -1;
    }

    public int getRelativeEnd() {
        return this.mRelativeEnd;
    }

    public float getRelativePercent() {
        return this.mRelativePercent;
    }

    @Override
    public String getType() {
        return "Guideline";
    }

    void inferRelativeBeginPosition() {
        int n = this.getX();
        if (this.mOrientation == 0) {
            n = this.getY();
        }
        this.setGuideBegin(n);
    }

    void inferRelativeEndPosition() {
        int n = this.getParent().getWidth() - this.getX();
        if (this.mOrientation == 0) {
            n = this.getParent().getHeight() - this.getY();
        }
        this.setGuideEnd(n);
    }

    void inferRelativePercentPosition() {
        float f = (float)this.getX() / (float)this.getParent().getWidth();
        if (this.mOrientation == 0) {
            f = (float)this.getY() / (float)this.getParent().getHeight();
        }
        this.setGuidePercent(f);
    }

    @Override
    public void setDrawOrigin(int n, int n2) {
        if (this.mOrientation == 1) {
            n -= this.mOffsetX;
            if (this.mRelativeBegin != -1) {
                this.setGuideBegin(n);
            } else if (this.mRelativeEnd != -1) {
                this.setGuideEnd(this.getParent().getWidth() - n);
            } else if (this.mRelativePercent != -1.0f) {
                this.setGuidePercent((float)n / (float)this.getParent().getWidth());
            }
            return;
        }
        n = n2 - this.mOffsetY;
        if (this.mRelativeBegin != -1) {
            this.setGuideBegin(n);
            return;
        }
        if (this.mRelativeEnd != -1) {
            this.setGuideEnd(this.getParent().getHeight() - n);
            return;
        }
        if (this.mRelativePercent != -1.0f) {
            this.setGuidePercent((float)n / (float)this.getParent().getHeight());
        }
    }

    public void setGuideBegin(int n) {
        if (n > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = n;
            this.mRelativeEnd = -1;
        }
    }

    public void setGuideEnd(int n) {
        if (n > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = n;
        }
    }

    public void setGuidePercent(float f) {
        if (f > -1.0f) {
            this.mRelativePercent = f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = -1;
        }
    }

    public void setGuidePercent(int n) {
        this.setGuidePercent((float)n / 100.0f);
    }

    public void setMinimumPosition(int n) {
        this.mMinimumPosition = n;
    }

    public void setOrientation(int n) {
        if (this.mOrientation == n) {
            return;
        }
        this.mOrientation = n;
        this.mAnchors.clear();
        this.mAnchor = this.mOrientation == 1 ? this.mLeft : this.mTop;
        this.mAnchors.add(this.mAnchor);
        int n2 = this.mListAnchors.length;
        for (n = 0; n < n2; ++n) {
            this.mListAnchors[n] = this.mAnchor;
        }
    }

    public void setPositionRelaxed(boolean bl) {
        if (this.mIsPositionRelaxed == bl) {
            return;
        }
        this.mIsPositionRelaxed = bl;
    }

    @Override
    public void updateFromSolver(LinearSystem linearSystem) {
        if (this.getParent() == null) {
            return;
        }
        int n = linearSystem.getObjectVariableValue(this.mAnchor);
        if (this.mOrientation == 1) {
            this.setX(n);
            this.setY(0);
            this.setHeight(this.getParent().getHeight());
            this.setWidth(0);
            return;
        }
        this.setX(0);
        this.setY(n);
        this.setWidth(this.getParent().getWidth());
        this.setHeight(0);
    }

}

