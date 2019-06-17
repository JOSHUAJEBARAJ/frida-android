/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Guideline;
import java.util.ArrayList;

public class ConstraintTableLayout
extends ConstraintWidgetContainer {
    public static final int ALIGN_CENTER = 0;
    private static final int ALIGN_FULL = 3;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    private ArrayList<Guideline> mHorizontalGuidelines = new ArrayList();
    private ArrayList<HorizontalSlice> mHorizontalSlices = new ArrayList();
    private int mNumCols = 0;
    private int mNumRows = 0;
    private int mPadding = 8;
    private boolean mVerticalGrowth = true;
    private ArrayList<Guideline> mVerticalGuidelines = new ArrayList();
    private ArrayList<VerticalSlice> mVerticalSlices = new ArrayList();
    private LinearSystem system = null;

    public ConstraintTableLayout() {
    }

    public ConstraintTableLayout(int n, int n2) {
        super(n, n2);
    }

    public ConstraintTableLayout(int n, int n2, int n3, int n4) {
        super(n, n2, n3, n4);
    }

    private void setChildrenConnections() {
        int n = this.mChildren.size();
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            int n3 = this.mNumCols;
            int n4 = (n2 += constraintWidget.getContainerItemSkip()) / n3;
            Object object = this.mHorizontalSlices.get(n4);
            VerticalSlice verticalSlice = this.mVerticalSlices.get(n2 % n3);
            ConstraintWidget constraintWidget2 = verticalSlice.left;
            ConstraintWidget constraintWidget3 = verticalSlice.right;
            ConstraintWidget constraintWidget4 = object.top;
            object = object.bottom;
            constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT), this.mPadding);
            if (constraintWidget3 instanceof Guideline) {
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintWidget3.getAnchor(ConstraintAnchor.Type.LEFT), this.mPadding);
            } else {
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintWidget3.getAnchor(ConstraintAnchor.Type.RIGHT), this.mPadding);
            }
            n3 = verticalSlice.alignment;
            if (n3 != 1) {
                if (n3 != 2) {
                    if (n3 == 3) {
                        constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    }
                } else {
                    constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).setStrength(ConstraintAnchor.Strength.WEAK);
                    constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).setStrength(ConstraintAnchor.Strength.STRONG);
                }
            } else {
                constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).setStrength(ConstraintAnchor.Strength.STRONG);
                constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).setStrength(ConstraintAnchor.Strength.WEAK);
            }
            constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).connect(constraintWidget4.getAnchor(ConstraintAnchor.Type.TOP), this.mPadding);
            if (object instanceof Guideline) {
                constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(object.getAnchor(ConstraintAnchor.Type.TOP), this.mPadding);
            } else {
                constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(object.getAnchor(ConstraintAnchor.Type.BOTTOM), this.mPadding);
            }
            ++n2;
        }
    }

    private void setHorizontalSlices() {
        float f;
        this.mHorizontalSlices.clear();
        float f2 = f = 100.0f / (float)this.mNumRows;
        ConstraintWidget constraintWidget = this;
        for (int i = 0; i < this.mNumRows; ++i) {
            HorizontalSlice horizontalSlice = new HorizontalSlice();
            horizontalSlice.top = constraintWidget;
            if (i < this.mNumRows - 1) {
                constraintWidget = new Guideline();
                constraintWidget.setOrientation(0);
                constraintWidget.setParent(this);
                constraintWidget.setGuidePercent((int)f2);
                f2 += f;
                horizontalSlice.bottom = constraintWidget;
                this.mHorizontalGuidelines.add((Guideline)constraintWidget);
            } else {
                horizontalSlice.bottom = this;
            }
            constraintWidget = horizontalSlice.bottom;
            this.mHorizontalSlices.add(horizontalSlice);
        }
        this.updateDebugSolverNames();
    }

    private void setVerticalSlices() {
        float f;
        this.mVerticalSlices.clear();
        ConstraintWidget constraintWidget = this;
        float f2 = f = 100.0f / (float)this.mNumCols;
        for (int i = 0; i < this.mNumCols; ++i) {
            VerticalSlice verticalSlice = new VerticalSlice();
            verticalSlice.left = constraintWidget;
            if (i < this.mNumCols - 1) {
                constraintWidget = new Guideline();
                constraintWidget.setOrientation(1);
                constraintWidget.setParent(this);
                constraintWidget.setGuidePercent((int)f2);
                f2 += f;
                verticalSlice.right = constraintWidget;
                this.mVerticalGuidelines.add((Guideline)constraintWidget);
            } else {
                verticalSlice.right = this;
            }
            constraintWidget = verticalSlice.right;
            this.mVerticalSlices.add(verticalSlice);
        }
        this.updateDebugSolverNames();
    }

    private void updateDebugSolverNames() {
        LinearSystem linearSystem;
        Guideline guideline;
        StringBuilder stringBuilder;
        int n;
        if (this.system == null) {
            return;
        }
        int n2 = this.mVerticalGuidelines.size();
        for (n = 0; n < n2; ++n) {
            guideline = this.mVerticalGuidelines.get(n);
            linearSystem = this.system;
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.getDebugName());
            stringBuilder.append(".VG");
            stringBuilder.append(n);
            guideline.setDebugSolverName(linearSystem, stringBuilder.toString());
        }
        n2 = this.mHorizontalGuidelines.size();
        for (n = 0; n < n2; ++n) {
            guideline = this.mHorizontalGuidelines.get(n);
            linearSystem = this.system;
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.getDebugName());
            stringBuilder.append(".HG");
            stringBuilder.append(n);
            guideline.setDebugSolverName(linearSystem, stringBuilder.toString());
        }
    }

    @Override
    public void addToSolver(LinearSystem linearSystem) {
        super.addToSolver(linearSystem);
        int n = this.mChildren.size();
        if (n == 0) {
            return;
        }
        this.setTableDimensions();
        if (linearSystem == this.mSystem) {
            boolean bl;
            Guideline guideline;
            int n2 = this.mVerticalGuidelines.size();
            int n3 = 0;
            do {
                bl = false;
                if (n3 >= n2) break;
                guideline = this.mVerticalGuidelines.get(n3);
                if (this.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    bl = true;
                }
                guideline.setPositionRelaxed(bl);
                guideline.addToSolver(linearSystem);
                ++n3;
            } while (true);
            n2 = this.mHorizontalGuidelines.size();
            for (n3 = 0; n3 < n2; ++n3) {
                guideline = this.mHorizontalGuidelines.get(n3);
                bl = this.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                guideline.setPositionRelaxed(bl);
                guideline.addToSolver(linearSystem);
            }
            for (n3 = 0; n3 < n; ++n3) {
                ((ConstraintWidget)this.mChildren.get(n3)).addToSolver(linearSystem);
            }
        }
    }

    public void computeGuidelinesPercentPositions() {
        int n;
        int n2 = this.mVerticalGuidelines.size();
        for (n = 0; n < n2; ++n) {
            this.mVerticalGuidelines.get(n).inferRelativePercentPosition();
        }
        n2 = this.mHorizontalGuidelines.size();
        for (n = 0; n < n2; ++n) {
            this.mHorizontalGuidelines.get(n).inferRelativePercentPosition();
        }
    }

    public void cycleColumnAlignment(int n) {
        VerticalSlice verticalSlice = this.mVerticalSlices.get(n);
        n = verticalSlice.alignment;
        if (n != 0) {
            if (n != 1) {
                if (n == 2) {
                    verticalSlice.alignment = 1;
                }
            } else {
                verticalSlice.alignment = 0;
            }
        } else {
            verticalSlice.alignment = 2;
        }
        this.setChildrenConnections();
    }

    public String getColumnAlignmentRepresentation(int n) {
        VerticalSlice verticalSlice = this.mVerticalSlices.get(n);
        if (verticalSlice.alignment == 1) {
            return "L";
        }
        if (verticalSlice.alignment == 0) {
            return "C";
        }
        if (verticalSlice.alignment == 3) {
            return "F";
        }
        if (verticalSlice.alignment == 2) {
            return "R";
        }
        return "!";
    }

    public String getColumnsAlignmentRepresentation() {
        int n = this.mVerticalSlices.size();
        CharSequence charSequence = "";
        for (int i = 0; i < n; ++i) {
            CharSequence charSequence22;
            CharSequence charSequence22;
            VerticalSlice verticalSlice = this.mVerticalSlices.get(i);
            if (verticalSlice.alignment == 1) {
                charSequence22 = new StringBuilder();
                charSequence22.append((String)charSequence);
                charSequence22.append("L");
                charSequence22 = charSequence22.toString();
            } else if (verticalSlice.alignment == 0) {
                charSequence22 = new StringBuilder();
                charSequence22.append((String)charSequence);
                charSequence22.append("C");
                charSequence22 = charSequence22.toString();
            } else if (verticalSlice.alignment == 3) {
                charSequence22 = new StringBuilder();
                charSequence22.append((String)charSequence);
                charSequence22.append("F");
                charSequence22 = charSequence22.toString();
            } else {
                charSequence22 = charSequence;
                if (verticalSlice.alignment == 2) {
                    charSequence22 = new StringBuilder();
                    charSequence22.append((String)charSequence);
                    charSequence22.append("R");
                    charSequence22 = charSequence22.toString();
                }
            }
            charSequence = charSequence22;
        }
        return charSequence;
    }

    @Override
    public ArrayList<Guideline> getHorizontalGuidelines() {
        return this.mHorizontalGuidelines;
    }

    public int getNumCols() {
        return this.mNumCols;
    }

    public int getNumRows() {
        return this.mNumRows;
    }

    public int getPadding() {
        return this.mPadding;
    }

    @Override
    public String getType() {
        return "ConstraintTableLayout";
    }

    @Override
    public ArrayList<Guideline> getVerticalGuidelines() {
        return this.mVerticalGuidelines;
    }

    @Override
    public boolean handlesInternalConstraints() {
        return true;
    }

    public boolean isVerticalGrowth() {
        return this.mVerticalGrowth;
    }

    public void setColumnAlignment(int n, int n2) {
        if (n < this.mVerticalSlices.size()) {
            this.mVerticalSlices.get((int)n).alignment = n2;
            this.setChildrenConnections();
        }
    }

    public void setColumnAlignment(String string2) {
        int n = string2.length();
        for (int i = 0; i < n; ++i) {
            char c = string2.charAt(i);
            if (c == 'L') {
                this.setColumnAlignment(i, 1);
                continue;
            }
            if (c == 'C') {
                this.setColumnAlignment(i, 0);
                continue;
            }
            if (c == 'F') {
                this.setColumnAlignment(i, 3);
                continue;
            }
            if (c == 'R') {
                this.setColumnAlignment(i, 2);
                continue;
            }
            this.setColumnAlignment(i, 0);
        }
    }

    @Override
    public void setDebugSolverName(LinearSystem linearSystem, String string2) {
        this.system = linearSystem;
        super.setDebugSolverName(linearSystem, string2);
        this.updateDebugSolverNames();
    }

    public void setNumCols(int n) {
        if (this.mVerticalGrowth && this.mNumCols != n) {
            this.mNumCols = n;
            this.setVerticalSlices();
            this.setTableDimensions();
        }
    }

    public void setNumRows(int n) {
        if (!this.mVerticalGrowth && this.mNumCols != n) {
            this.mNumRows = n;
            this.setHorizontalSlices();
            this.setTableDimensions();
        }
    }

    public void setPadding(int n) {
        if (n > 1) {
            this.mPadding = n;
        }
    }

    public void setTableDimensions() {
        int n;
        int n2 = 0;
        int n3 = this.mChildren.size();
        for (n = 0; n < n3; ++n) {
            n2 += ((ConstraintWidget)this.mChildren.get(n)).getContainerItemSkip();
        }
        n3 += n2;
        if (this.mVerticalGrowth) {
            if (this.mNumCols == 0) {
                this.setNumCols(1);
            }
            int n4 = this.mNumCols;
            n = n2 = n3 / n4;
            if (n4 * n2 < n3) {
                n = n2 + 1;
            }
            if (this.mNumRows == n && this.mVerticalGuidelines.size() == this.mNumCols - 1) {
                return;
            }
            this.mNumRows = n;
            this.setHorizontalSlices();
        } else {
            if (this.mNumRows == 0) {
                this.setNumRows(1);
            }
            int n5 = this.mNumRows;
            n = n2 = n3 / n5;
            if (n5 * n2 < n3) {
                n = n2 + 1;
            }
            if (this.mNumCols == n && this.mHorizontalGuidelines.size() == this.mNumRows - 1) {
                return;
            }
            this.mNumCols = n;
            this.setVerticalSlices();
        }
        this.setChildrenConnections();
    }

    public void setVerticalGrowth(boolean bl) {
        this.mVerticalGrowth = bl;
    }

    @Override
    public void updateFromSolver(LinearSystem linearSystem) {
        super.updateFromSolver(linearSystem);
        if (linearSystem == this.mSystem) {
            int n;
            int n2 = this.mVerticalGuidelines.size();
            for (n = 0; n < n2; ++n) {
                this.mVerticalGuidelines.get(n).updateFromSolver(linearSystem);
            }
            n2 = this.mHorizontalGuidelines.size();
            for (n = 0; n < n2; ++n) {
                this.mHorizontalGuidelines.get(n).updateFromSolver(linearSystem);
            }
        }
    }

    class HorizontalSlice {
        ConstraintWidget bottom;
        int padding;
        ConstraintWidget top;

        HorizontalSlice() {
        }
    }

    class VerticalSlice {
        int alignment;
        ConstraintWidget left;
        int padding;
        ConstraintWidget right;

        VerticalSlice() {
            this.alignment = 1;
        }
    }

}

