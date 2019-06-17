/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.widgets.Chain;
import android.support.constraint.solver.widgets.ChainHead;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetGroup;
import android.support.constraint.solver.widgets.Guideline;
import android.support.constraint.solver.widgets.Optimizer;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.support.constraint.solver.widgets.ResolutionDimension;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.constraint.solver.widgets.WidgetContainer;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstraintWidgetContainer
extends WidgetContainer {
    private static final boolean DEBUG = false;
    static final boolean DEBUG_GRAPH = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final int MAX_ITERATIONS = 8;
    private static final boolean USE_SNAPSHOT = true;
    int mDebugSolverPassCount = 0;
    public boolean mGroupsWrapOptimized = false;
    private boolean mHeightMeasuredTooSmall = false;
    ChainHead[] mHorizontalChainsArray = new ChainHead[4];
    int mHorizontalChainsSize = 0;
    public boolean mHorizontalWrapOptimized = false;
    private boolean mIsRtl = false;
    private int mOptimizationLevel = 7;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    public boolean mSkipSolver = false;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem = new LinearSystem();
    ChainHead[] mVerticalChainsArray = new ChainHead[4];
    int mVerticalChainsSize = 0;
    public boolean mVerticalWrapOptimized = false;
    public List<ConstraintWidgetGroup> mWidgetGroups = new ArrayList<ConstraintWidgetGroup>();
    private boolean mWidthMeasuredTooSmall = false;
    public int mWrapFixedHeight = 0;
    public int mWrapFixedWidth = 0;

    public ConstraintWidgetContainer() {
    }

    public ConstraintWidgetContainer(int n, int n2) {
        super(n, n2);
    }

    public ConstraintWidgetContainer(int n, int n2, int n3, int n4) {
        super(n, n2, n3, n4);
    }

    private void addHorizontalChain(ConstraintWidget constraintWidget) {
        int n = this.mHorizontalChainsSize;
        ChainHead[] arrchainHead = this.mHorizontalChainsArray;
        if (n + 1 >= arrchainHead.length) {
            this.mHorizontalChainsArray = Arrays.copyOf(arrchainHead, arrchainHead.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(constraintWidget, 0, this.isRtl());
        ++this.mHorizontalChainsSize;
    }

    private void addVerticalChain(ConstraintWidget constraintWidget) {
        int n = this.mVerticalChainsSize;
        ChainHead[] arrchainHead = this.mVerticalChainsArray;
        if (n + 1 >= arrchainHead.length) {
            this.mVerticalChainsArray = Arrays.copyOf(arrchainHead, arrchainHead.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(constraintWidget, 1, this.isRtl());
        ++this.mVerticalChainsSize;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    void addChain(ConstraintWidget constraintWidget, int n) {
        if (n == 0) {
            this.addHorizontalChain(constraintWidget);
            return;
        }
        if (n == 1) {
            this.addVerticalChain(constraintWidget);
        }
    }

    public boolean addChildrenToSolver(LinearSystem linearSystem) {
        this.addToSolver(linearSystem);
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.mListDimensionBehaviors[0];
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                }
                if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem);
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(dimensionBehaviour);
                }
                if (dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) continue;
                constraintWidget.setVerticalDimensionBehaviour(dimensionBehaviour2);
                continue;
            }
            Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
            constraintWidget.addToSolver(linearSystem);
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 1);
        }
        return true;
    }

    @Override
    public void analyze(int n) {
        super.analyze(n);
        int n2 = this.mChildren.size();
        for (int i = 0; i < n2; ++i) {
            ((ConstraintWidget)this.mChildren.get(i)).analyze(n);
        }
    }

    public void fillMetrics(Metrics metrics) {
        this.mSystem.fillMetrics(metrics);
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<Guideline>();
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            if (!(constraintWidget instanceof Guideline) || (constraintWidget = (Guideline)constraintWidget).getOrientation() != 0) continue;
            arrayList.add((Guideline)constraintWidget);
        }
        return arrayList;
    }

    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    @Override
    public String getType() {
        return "ConstraintLayout";
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<Guideline>();
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            if (!(constraintWidget instanceof Guideline) || (constraintWidget = (Guideline)constraintWidget).getOrientation() != 1) continue;
            arrayList.add((Guideline)constraintWidget);
        }
        return arrayList;
    }

    public List<ConstraintWidgetGroup> getWidgetGroups() {
        return this.mWidgetGroups;
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }

    public boolean isRtl() {
        return this.mIsRtl;
    }

    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void layout() {
        var10_1 = this.mX;
        var11_2 = this.mY;
        var12_3 = Math.max(0, this.getWidth());
        var13_4 = Math.max(0, this.getHeight());
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.setX(this.mPaddingLeft);
            this.setY(this.mPaddingTop);
            this.resetAnchors();
            this.resetSolverVariables(this.mSystem.getCache());
        } else {
            this.mX = 0;
            this.mY = 0;
        }
        if (this.mOptimizationLevel != 0) {
            if (!this.optimizeFor(8)) {
                this.optimizeReset();
            }
            if (!this.optimizeFor(32)) {
                this.optimize();
            }
            this.mSystem.graphOptimizer = true;
        } else {
            this.mSystem.graphOptimizer = false;
        }
        var2_5 = 0;
        var17_6 = this.mListDimensionBehaviors[1];
        var18_7 = this.mListDimensionBehaviors[0];
        this.resetChains();
        if (this.mWidgetGroups.size() == 0) {
            this.mWidgetGroups.clear();
            this.mWidgetGroups.add(0, new ConstraintWidgetGroup(this.mChildren));
        }
        var1_8 = this.mWidgetGroups.size();
        var19_9 = this.mChildren;
        var5_10 = this.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || this.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        var6_11 = 0;
        block9 : do {
            if (var6_11 >= var1_8 || this.mSkipSolver) ** GOTO lbl51
            if (this.mWidgetGroups.get((int)var6_11).mSkipSolver) ** GOTO lbl87
            if (this.optimizeFor(32)) {
                this.mChildren = this.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED && this.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.FIXED ? (ArrayList)this.mWidgetGroups.get(var6_11).getWidgetsToSolve() : (ArrayList)this.mWidgetGroups.get((int)var6_11).mConstrainedGroup;
            }
            this.resetChains();
            var7_14 = this.mChildren.size();
            var3_12 = 0;
            for (var4_13 = 0; var4_13 < var7_14; ++var4_13) {
                var16_19 = (ConstraintWidget)this.mChildren.get(var4_13);
                if (!(var16_19 instanceof WidgetContainer)) continue;
                ((WidgetContainer)var16_19).layout();
            }
            ** GOTO lbl68
lbl51: // 1 sources:
            this.mChildren = var19_9;
            if (this.mParent != null) {
                var1_8 = Math.max(this.mMinWidth, this.getWidth());
                var3_12 = Math.max(this.mMinHeight, this.getHeight());
                this.mSnapshot.applyTo(this);
                this.setWidth(this.mPaddingLeft + var1_8 + this.mPaddingRight);
                this.setHeight(this.mPaddingTop + var3_12 + this.mPaddingBottom);
            } else {
                this.mX = var10_1;
                this.mY = var11_2;
            }
            if (var2_5 != 0) {
                this.mListDimensionBehaviors[0] = var18_7;
                this.mListDimensionBehaviors[1] = var17_6;
            }
            this.resetSolverVariables(this.mSystem.getCache());
            if (this != this.getRootConstraintContainer()) return;
            this.updateDrawPosition();
            return;
lbl68: // 1 sources:
            var14_17 = true;
            var4_13 = var3_12;
            var3_12 = var1_8;
            var1_8 = var2_5;
            var2_5 = var7_14;
            do {
                block46 : {
                    block47 : {
                        block45 : {
                            if (var14_17) {
                                var9_16 = var4_13 + 1;
                                try {
                                    this.mSystem.reset();
                                    this.resetChains();
                                    this.createObjectVariables(this.mSystem);
                                    break block45;
                                }
                                catch (Exception var16_22) {
                                    // empty catch block
                                    break block46;
                                }
                            }
                            this.mWidgetGroups.get(var6_11).updateUnresolvedWidgets();
                            var2_5 = var1_8;
                            var1_8 = var3_12;
lbl87: // 2 sources:
                            ++var6_11;
                            continue block9;
                        }
                        for (var4_13 = 0; var4_13 < var2_5; ++var4_13) {
                            var15_18 = var14_17;
                            var16_19 = (ConstraintWidget)this.mChildren.get(var4_13);
                            var16_19.createObjectVariables(this.mSystem);
                            var14_17 = var15_18;
                            continue;
                        }
                        var4_13 = var1_8;
                        try {
                            var14_17 = var15_18 = this.addChildrenToSolver(this.mSystem);
                            if (!var14_17) break block47;
                        }
                        catch (Exception var16_21) {}
                        try {
                            this.mSystem.minimize();
                        }
                        catch (Exception var16_20) {
                            var1_8 = var4_13;
                            break block46;
                        }
                    }
                    var1_8 = var4_13;
                    ** GOTO lbl122
                    ** GOTO lbl115
                    catch (Exception var16_23) {
                        var14_17 = var15_18;
                    }
                }
                var16_19.printStackTrace();
                var20_24 = System.out;
                var21_25 = new StringBuilder();
                var21_25.append("EXCEPTION : ");
                var21_25.append(var16_19);
                var20_24.println(var21_25.toString());
lbl122: // 2 sources:
                if (var14_17) {
                    this.updateChildrenFromSolver(this.mSystem, Optimizer.flags);
                } else {
                    this.updateFromSolver(this.mSystem);
                    for (var4_13 = 0; var4_13 < var2_5; ++var4_13) {
                        var16_19 = (ConstraintWidget)this.mChildren.get(var4_13);
                        if (var16_19.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && var16_19.getWidth() < var16_19.getWrapWidth()) {
                            Optimizer.flags[2] = true;
                            break;
                        }
                        if (var16_19.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || var16_19.getHeight() >= var16_19.getWrapHeight()) continue;
                        Optimizer.flags[2] = true;
                        break;
                    }
                }
                var14_17 = false;
                if (var5_10 && var9_16 < 8 && Optimizer.flags[2]) {
                    var4_13 = 0;
                    var8_15 = 0;
                    for (var7_14 = 0; var7_14 < var2_5; ++var7_14) {
                        var16_19 = (ConstraintWidget)this.mChildren.get(var7_14);
                        var8_15 = Math.max(var8_15, var16_19.mX + var16_19.getWidth());
                        var4_13 = Math.max(var4_13, var16_19.mY + var16_19.getHeight());
                    }
                    var7_14 = var2_5;
                    var2_5 = Math.max(this.mMinWidth, var8_15);
                    var8_15 = Math.max(this.mMinHeight, var4_13);
                    if (var18_7 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.getWidth() < var2_5) {
                        this.setWidth(var2_5);
                        this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                        var4_13 = 1;
                        var15_18 = true;
                    } else {
                        var15_18 = var14_17;
                        var4_13 = var1_8;
                    }
                    var1_8 = var4_13;
                    var14_17 = var15_18;
                    var2_5 = var7_14;
                    if (var17_6 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        var1_8 = var4_13;
                        var14_17 = var15_18;
                        var2_5 = var7_14;
                        if (this.getHeight() < var8_15) {
                            this.setHeight(var8_15);
                            this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                            var1_8 = 1;
                            var14_17 = true;
                            var2_5 = var7_14;
                        }
                    }
                } else {
                    var14_17 = false;
                }
                if ((var4_13 = Math.max(this.mMinWidth, this.getWidth())) > this.getWidth()) {
                    this.setWidth(var4_13);
                    this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                    var1_8 = 1;
                    var14_17 = true;
                }
                if ((var4_13 = Math.max(this.mMinHeight, this.getHeight())) > this.getHeight()) {
                    this.setHeight(var4_13);
                    this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                    var1_8 = 1;
                    var14_17 = true;
                    if (var1_8 == 0) {
                        var4_13 = var1_8;
                        var15_18 = var14_17;
                        if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                            var4_13 = var1_8;
                            var15_18 = var14_17;
                            if (var12_3 > 0) {
                                var4_13 = var1_8;
                                var15_18 = var14_17;
                                if (this.getWidth() > var12_3) {
                                    this.mWidthMeasuredTooSmall = true;
                                    var4_13 = 1;
                                    this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                                    this.setWidth(var12_3);
                                    var15_18 = true;
                                }
                            }
                        }
                        if (this.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && var13_4 > 0 && this.getHeight() > var13_4) {
                            this.mHeightMeasuredTooSmall = true;
                            var1_8 = 1;
                            this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                            this.setHeight(var13_4);
                            var14_17 = true;
                        } else {
                            var14_17 = var15_18;
                            var1_8 = var4_13;
                        }
                    }
                }
                var4_13 = var9_16;
            } while (true);
            break;
        } while (true);
    }

    public void optimize() {
        if (!this.optimizeFor(8)) {
            this.analyze(this.mOptimizationLevel);
        }
        this.solveGraph();
    }

    public boolean optimizeFor(int n) {
        if ((this.mOptimizationLevel & n) == n) {
            return true;
        }
        return false;
    }

    public void optimizeForDimensions(int n, int n2) {
        if (this.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionWidth != null) {
            this.mResolutionWidth.resolve(n);
        }
        if (this.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null) {
            this.mResolutionHeight.resolve(n2);
        }
    }

    public void optimizeReset() {
        int n = this.mChildren.size();
        this.resetResolutionNodes();
        for (int i = 0; i < n; ++i) {
            ((ConstraintWidget)this.mChildren.get(i)).resetResolutionNodes();
        }
    }

    public void preOptimize() {
        this.optimizeReset();
        this.analyze(this.mOptimizationLevel);
    }

    @Override
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mWidgetGroups.clear();
        this.mSkipSolver = false;
        super.reset();
    }

    public void resetGraph() {
        ResolutionAnchor resolutionAnchor = this.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
        ResolutionAnchor resolutionAnchor2 = this.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
        resolutionAnchor.invalidateAnchors();
        resolutionAnchor2.invalidateAnchors();
        resolutionAnchor.resolve(null, 0.0f);
        resolutionAnchor2.resolve(null, 0.0f);
    }

    public void setOptimizationLevel(int n) {
        this.mOptimizationLevel = n;
    }

    public void setPadding(int n, int n2, int n3, int n4) {
        this.mPaddingLeft = n;
        this.mPaddingTop = n2;
        this.mPaddingRight = n3;
        this.mPaddingBottom = n4;
    }

    public void setRtl(boolean bl) {
        this.mIsRtl = bl;
    }

    public void solveGraph() {
        ResolutionAnchor resolutionAnchor = this.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
        ResolutionAnchor resolutionAnchor2 = this.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
        resolutionAnchor.resolve(null, 0.0f);
        resolutionAnchor2.resolve(null, 0.0f);
    }

    public void updateChildrenFromSolver(LinearSystem linearSystem, boolean[] arrbl) {
        arrbl[2] = false;
        this.updateFromSolver(linearSystem);
        int n = this.mChildren.size();
        for (int i = 0; i < n; ++i) {
            ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(i);
            constraintWidget.updateFromSolver(linearSystem);
            if (constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                arrbl[2] = true;
            }
            if (constraintWidget.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.getHeight() >= constraintWidget.getWrapHeight()) continue;
            arrbl[2] = true;
        }
    }
}

