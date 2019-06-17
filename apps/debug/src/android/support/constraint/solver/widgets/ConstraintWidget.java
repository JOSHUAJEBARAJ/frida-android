/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.Barrier;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.ConstraintWidgetGroup;
import android.support.constraint.solver.widgets.Optimizer;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.support.constraint.solver.widgets.ResolutionDimension;
import android.support.constraint.solver.widgets.WidgetContainer;
import java.util.ArrayList;

public class ConstraintWidget {
    protected static final int ANCHOR_BASELINE = 4;
    protected static final int ANCHOR_BOTTOM = 3;
    protected static final int ANCHOR_LEFT = 0;
    protected static final int ANCHOR_RIGHT = 1;
    protected static final int ANCHOR_TOP = 2;
    private static final boolean AUTOTAG_CENTER = false;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.0f;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private static final int WRAP = -2;
    protected ArrayList<ConstraintAnchor> mAnchors;
    ConstraintAnchor mBaseline;
    int mBaselineDistance;
    ConstraintWidgetGroup mBelongingGroup = null;
    ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle = 0.0f;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    protected float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mDrawX;
    private int mDrawY;
    boolean mGroupsToSolver;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution = -1;
    boolean mHorizontalWrapVisited;
    boolean mIsHeightWrapContent;
    boolean mIsWidthWrapContent;
    ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    protected ConstraintAnchor[] mListAnchors;
    protected DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    int mMatchConstraintDefaultHeight = 0;
    int mMatchConstraintDefaultWidth = 0;
    int mMatchConstraintMaxHeight = 0;
    int mMatchConstraintMaxWidth = 0;
    int mMatchConstraintMinHeight = 0;
    int mMatchConstraintMinWidth = 0;
    float mMatchConstraintPercentHeight = 1.0f;
    float mMatchConstraintPercentWidth = 1.0f;
    private int[] mMaxDimension = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    boolean mOptimizerMeasurable;
    boolean mOptimizerMeasured;
    ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    ResolutionDimension mResolutionHeight;
    ResolutionDimension mResolutionWidth;
    float mResolvedDimensionRatio = 1.0f;
    int mResolvedDimensionRatioSide = -1;
    int[] mResolvedMatchConstraintDefault = new int[2];
    ConstraintAnchor mRight;
    boolean mRightHasCentered;
    ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution = -1;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    float[] mWeight;
    int mWidth;
    private int mWrapHeight;
    private int mWrapWidth;
    protected int mX;
    protected int mY;

    static {
        DEFAULT_BIAS = 0.5f;
    }

    public ConstraintWidget() {
        float f;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter};
        this.mAnchors = new ArrayList();
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.addAnchors();
    }

    public ConstraintWidget(int n, int n2) {
        this(0, 0, n, n2);
    }

    public ConstraintWidget(int n, int n2, int n3, int n4) {
        float f;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter};
        this.mAnchors = new ArrayList();
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.mX = n;
        this.mY = n2;
        this.mWidth = n3;
        this.mHeight = n4;
        this.addAnchors();
        this.forceUpdateDrawPosition();
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    private void applyConstraints(LinearSystem linearSystem, boolean bl, SolverVariable object, SolverVariable solverVariable, DimensionBehaviour object2, boolean bl2, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n, int n2, int n3, int n4, float f, boolean bl3, boolean bl4, int n5, int n6, int n7, float f2, boolean bl5) {
        Object object3;
        SolverVariable solverVariable2;
        SolverVariable solverVariable3 = linearSystem.createObjectVariable(constraintAnchor);
        SolverVariable solverVariable4 = solverVariable2 = linearSystem.createObjectVariable(constraintAnchor2);
        SolverVariable solverVariable5 = linearSystem.createObjectVariable(constraintAnchor.getTarget());
        SolverVariable solverVariable6 = linearSystem.createObjectVariable(constraintAnchor2.getTarget());
        if (linearSystem.graphOptimizer && constraintAnchor.getResolutionNode().state == 1 && constraintAnchor2.getResolutionNode().state == 1) {
            if (LinearSystem.getMetrics() != null) {
                object = LinearSystem.getMetrics();
                ++object.resolvedWidgets;
            }
            constraintAnchor.getResolutionNode().addResolvedValue(linearSystem);
            constraintAnchor2.getResolutionNode().addResolvedValue(linearSystem);
            if (!bl4 && bl) {
                linearSystem.addGreaterThan(solverVariable, solverVariable4, 0, 6);
            }
            return;
        }
        if (LinearSystem.getMetrics() != null) {
            object3 = LinearSystem.getMetrics();
            ++object3.nonresolvedWidgets;
        }
        boolean bl6 = constraintAnchor.isConnected();
        boolean bl7 = constraintAnchor2.isConnected();
        boolean bl8 = this.mCenter.isConnected();
        int n8 = 0;
        if (bl6) {
            n8 = 0 + 1;
        }
        int n9 = n8;
        if (bl7) {
            n9 = n8 + 1;
        }
        n8 = n9;
        if (bl8) {
            n8 = n9 + 1;
        }
        n9 = bl3 ? 3 : n5;
        n5 = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintWidget$DimensionBehaviour[object2.ordinal()];
        n5 = n5 != 1 ? (n5 != 2 ? (n5 != 3 ? (n5 != 4 ? 0 : (n9 == 4 ? 0 : 1)) : 0) : 0) : 0;
        if (this.mVisibility == 8) {
            n2 = 0;
            n5 = 0;
        }
        if (bl5) {
            if (!(bl6 || bl7 || bl8)) {
                linearSystem.addEquality(solverVariable3, n);
            } else if (bl6 && !bl7) {
                linearSystem.addEquality(solverVariable3, solverVariable5, constraintAnchor.getMargin(), 6);
            }
        }
        if (n5 == 0) {
            if (bl2) {
                linearSystem.addEquality(solverVariable4, solverVariable3, 0, 3);
                if (n3 > 0) {
                    linearSystem.addGreaterThan(solverVariable4, solverVariable3, n3, 6);
                }
                if (n4 < Integer.MAX_VALUE) {
                    linearSystem.addLowerThan(solverVariable4, solverVariable3, n4, 6);
                }
            } else {
                linearSystem.addEquality(solverVariable4, solverVariable3, n2, 6);
            }
            n4 = n6;
            n = n7;
        } else {
            n = n6;
            if (n6 == -2) {
                n = n2;
            }
            n4 = n7;
            if (n7 == -2) {
                n4 = n2;
            }
            if (n > 0) {
                linearSystem.addGreaterThan(solverVariable4, solverVariable3, n, 6);
                n6 = Math.max(n2, n);
            } else {
                n6 = n2;
            }
            n2 = n6;
            if (n4 > 0) {
                linearSystem.addLowerThan(solverVariable4, solverVariable3, n4, 6);
                n2 = Math.min(n6, n4);
            }
            if (n9 == 1) {
                if (bl) {
                    linearSystem.addEquality(solverVariable4, solverVariable3, n2, 6);
                } else if (bl4) {
                    linearSystem.addEquality(solverVariable4, solverVariable3, n2, 4);
                } else {
                    linearSystem.addEquality(solverVariable4, solverVariable3, n2, 1);
                }
            } else if (n9 == 2) {
                if (constraintAnchor.getType() != ConstraintAnchor.Type.TOP && constraintAnchor.getType() != ConstraintAnchor.Type.BOTTOM) {
                    object2 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.LEFT));
                    object3 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.RIGHT));
                } else {
                    object2 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.TOP));
                    object3 = linearSystem.createObjectVariable(this.mParent.getAnchor(ConstraintAnchor.Type.BOTTOM));
                }
                linearSystem.addConstraint(linearSystem.createRow().createRowDimensionRatio(solverVariable4, solverVariable3, (SolverVariable)object3, (SolverVariable)object2, f2));
                n5 = 0;
            }
            n6 = n;
            if (n5 != 0 && n8 != 2 && !bl3) {
                n5 = 0;
                n = n7 = Math.max(n6, n2);
                if (n4 > 0) {
                    n = Math.min(n4, n7);
                }
                linearSystem.addEquality(solverVariable4, solverVariable3, n, 6);
                n = n4;
                n4 = n6;
            } else {
                n = n4;
                n4 = n6;
            }
        }
        object2 = solverVariable5;
        if (bl5 && !bl4) {
            if (!(bl6 || bl7 || bl8)) {
                if (bl) {
                    linearSystem.addGreaterThan(solverVariable, solverVariable4, 0, 5);
                }
            } else if (bl6 && !bl7) {
                if (bl) {
                    linearSystem.addGreaterThan(solverVariable, solverVariable4, 0, 5);
                }
            } else if (!bl6 && bl7) {
                linearSystem.addEquality(solverVariable4, solverVariable6, - constraintAnchor2.getMargin(), 6);
                if (bl) {
                    linearSystem.addGreaterThan(solverVariable3, (SolverVariable)object, 0, 5);
                }
            } else if (bl6 && bl7) {
                n6 = 0;
                if (n5 != 0) {
                    if (bl && n3 == 0) {
                        linearSystem.addGreaterThan(solverVariable4, solverVariable3, 0, 6);
                    }
                    if (n9 == 0) {
                        block76 : {
                            if (n <= 0 && n4 <= 0) {
                                n2 = 0;
                                n3 = 6;
                            } else {
                                n2 = 1;
                                n3 = 4;
                            }
                            linearSystem.addEquality(solverVariable3, (SolverVariable)object2, constraintAnchor.getMargin(), n3);
                            linearSystem.addEquality(solverVariable4, solverVariable6, - constraintAnchor2.getMargin(), n3);
                            if (n <= 0) {
                                n = n6;
                                if (n4 <= 0) break block76;
                            }
                            n = 1;
                        }
                        n3 = 5;
                        n4 = n2;
                        n2 = n;
                        n = n4;
                    } else if (n9 == 1) {
                        n = 1;
                        n2 = 1;
                        n3 = 6;
                    } else if (n9 == 3) {
                        n3 = 4;
                        if (!bl3) {
                            n2 = n3;
                            if (this.mResolvedDimensionRatioSide != -1) {
                                n2 = n3;
                                if (n <= 0) {
                                    n2 = 6;
                                }
                            }
                        } else {
                            n2 = n3;
                        }
                        linearSystem.addEquality(solverVariable3, (SolverVariable)object2, constraintAnchor.getMargin(), n2);
                        linearSystem.addEquality(solverVariable4, solverVariable6, - constraintAnchor2.getMargin(), n2);
                        n = 1;
                        n2 = 1;
                        n3 = 5;
                    } else {
                        n = 0;
                        n2 = 0;
                        n3 = 5;
                    }
                } else {
                    n = 0;
                    n2 = 1;
                    n3 = 5;
                }
                n4 = 5;
                n6 = 5;
                bl2 = bl;
                bl3 = bl;
                if (n2 != 0) {
                    linearSystem.addCentering(solverVariable3, (SolverVariable)object2, constraintAnchor.getMargin(), f, solverVariable6, solverVariable4, constraintAnchor2.getMargin(), n3);
                    bl6 = constraintAnchor.mTarget.mOwner instanceof Barrier;
                    bl7 = constraintAnchor2.mTarget.mOwner instanceof Barrier;
                    if (bl6 && !bl7) {
                        n3 = 6;
                        bl5 = true;
                        n2 = n4;
                        bl4 = bl2;
                    } else {
                        n2 = n4;
                        n3 = n6;
                        bl4 = bl2;
                        bl5 = bl3;
                        if (!bl6) {
                            n2 = n4;
                            n3 = n6;
                            bl4 = bl2;
                            bl5 = bl3;
                            if (bl7) {
                                n2 = 6;
                                bl4 = true;
                                n3 = n6;
                                bl5 = bl3;
                            }
                        }
                    }
                } else {
                    bl5 = bl3;
                    bl4 = bl2;
                    n3 = n6;
                    n2 = n4;
                }
                if (n != 0) {
                    n2 = 6;
                    n3 = 6;
                }
                if (n5 == 0 && bl4 || n != 0) {
                    linearSystem.addGreaterThan(solverVariable3, (SolverVariable)object2, constraintAnchor.getMargin(), n2);
                }
                if (n5 == 0 && bl5 || n != 0) {
                    linearSystem.addLowerThan(solverVariable2, solverVariable6, - constraintAnchor2.getMargin(), n3);
                }
                if (bl) {
                    linearSystem.addGreaterThan(solverVariable3, (SolverVariable)object, 0, 6);
                    solverVariable4 = solverVariable2;
                } else {
                    solverVariable4 = solverVariable2;
                }
            }
            if (bl) {
                linearSystem.addGreaterThan(solverVariable, solverVariable4, 0, 6);
            }
            return;
        }
        if (n8 < 2 && bl) {
            linearSystem.addGreaterThan(solverVariable3, (SolverVariable)object, 0, 6);
            linearSystem.addGreaterThan(solverVariable, solverVariable4, 0, 6);
        }
    }

    private boolean isChainHead(int n) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor[] arrconstraintAnchor;
        if (this.mListAnchors[n *= 2].mTarget != null && (constraintAnchor = this.mListAnchors[n].mTarget.mTarget) != (arrconstraintAnchor = this.mListAnchors)[n] && arrconstraintAnchor[n + 1].mTarget != null && this.mListAnchors[n + 1].mTarget.mTarget == this.mListAnchors[n + 1]) {
            return true;
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void addToSolver(LinearSystem var1_1) {
        var24_2 = var1_1.createObjectVariable(this.mLeft);
        var23_3 = var1_1.createObjectVariable(this.mRight);
        var22_4 = var1_1.createObjectVariable(this.mTop);
        var18_5 = var1_1.createObjectVariable(this.mBottom);
        var21_6 = var1_1.createObjectVariable(this.mBaseline);
        var19_7 = this.mParent;
        if (var19_7 != null) {
            var11_8 = var19_7 != null && var19_7.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT;
            var19_7 = this.mParent;
            var12_9 = var19_7 != null && var19_7.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT;
            if (this.isChainHead(0)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 0);
                var13_10 = true;
            } else {
                var13_10 = this.isInHorizontalChain();
            }
            if (this.isChainHead(1)) {
                ((ConstraintWidgetContainer)this.mParent).addChain(this, 1);
                var14_11 = true;
            } else {
                var14_11 = this.isInVerticalChain();
            }
            if (var11_8 && this.mVisibility != 8 && this.mLeft.mTarget == null && this.mRight.mTarget == null) {
                var1_1.addGreaterThan(var1_1.createObjectVariable(this.mParent.mRight), (SolverVariable)var23_3, 0, 1);
            }
            if (var12_9 && this.mVisibility != 8 && this.mTop.mTarget == null && this.mBottom.mTarget == null && this.mBaseline == null) {
                var1_1.addGreaterThan(var1_1.createObjectVariable(this.mParent.mBottom), (SolverVariable)var18_5, 0, 1);
            }
            var16_12 = var14_11;
            var14_11 = var11_8;
            var15_13 = var13_10;
            var13_10 = var16_12;
        } else {
            var15_13 = false;
            var13_10 = false;
            var14_11 = false;
            var12_9 = false;
        }
        var5_15 = var3_14 = this.mWidth;
        if (var3_14 < this.mMinWidth) {
            var5_15 = this.mMinWidth;
        }
        var6_16 = var3_14 = this.mHeight;
        if (var3_14 < this.mMinHeight) {
            var6_16 = this.mMinHeight;
        }
        var11_8 = this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT;
        var16_12 = this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT;
        var7_17 = 0;
        this.mResolvedDimensionRatioSide = this.mDimensionRatioSide;
        this.mResolvedDimensionRatio = var2_18 = this.mDimensionRatio;
        var8_19 = this.mMatchConstraintDefaultWidth;
        var9_20 = this.mMatchConstraintDefaultHeight;
        if (var2_18 <= 0.0f || this.mVisibility == 8) ** GOTO lbl-1000
        var10_21 = 1;
        var3_14 = var8_19;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
            var3_14 = var8_19;
            if (var8_19 == 0) {
                var3_14 = 3;
            }
        }
        var4_22 = var9_20;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
            var4_22 = var9_20;
            if (var9_20 == 0) {
                var4_22 = 3;
            }
        }
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT || var3_14 != 3 || var4_22 != 3) ** GOTO lbl65
        this.setupDimensionRatio(var14_11, var12_9, var11_8, var16_12);
        var9_20 = var4_22;
        var8_19 = var3_14;
        var7_17 = var10_21;
        ** GOTO lbl-1000
lbl65: // 1 sources:
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT || var3_14 != 3) ** GOTO lbl80
        this.mResolvedDimensionRatioSide = 0;
        var7_17 = (int)(this.mResolvedDimensionRatio * (float)this.mHeight);
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            var3_14 = 4;
            var8_19 = var6_16;
            var5_15 = 0;
            var6_16 = var7_17;
            var7_17 = var8_19;
        } else {
            var8_19 = var6_16;
            var5_15 = 1;
            var6_16 = var7_17;
            var7_17 = var8_19;
        }
        ** GOTO lbl106
lbl80: // 1 sources:
        var9_20 = var4_22;
        var8_19 = var3_14;
        var7_17 = var10_21;
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) ** GOTO lbl-1000
        var9_20 = var4_22;
        var8_19 = var3_14;
        var7_17 = var10_21;
        if (var4_22 == 3) {
            this.mResolvedDimensionRatioSide = 1;
            if (this.mDimensionRatioSide == -1) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
            }
            var7_17 = (int)(this.mResolvedDimensionRatio * (float)this.mWidth);
            if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
                var4_22 = 4;
                var6_16 = var5_15;
                var5_15 = 0;
            } else {
                var6_16 = var5_15;
                var5_15 = 1;
            }
        } else lbl-1000: // 4 sources:
        {
            var4_22 = var9_20;
            var3_14 = var8_19;
            var8_19 = var7_17;
            var7_17 = var6_16;
            var6_16 = var5_15;
            var5_15 = var8_19;
        }
lbl106: // 5 sources:
        var19_7 = this.mResolvedMatchConstraintDefault;
        var19_7[0] = var3_14;
        var19_7[1] = var4_22;
        var16_12 = var5_15 != 0 && ((var8_19 = this.mResolvedDimensionRatioSide) == 0 || var8_19 == -1);
        var17_23 = this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer != false;
        var11_8 = this.mCenter.isConnected() == false;
        if (this.mHorizontalResolution != 2) {
            var19_7 = this.mParent;
            var19_7 = var19_7 != null ? var1_1.createObjectVariable(var19_7.mRight) : null;
            var20_24 = this.mParent;
            var20_24 = var20_24 != null ? var1_1.createObjectVariable(var20_24.mLeft) : null;
            this.applyConstraints(var1_1, var14_11, (SolverVariable)var20_24, (SolverVariable)var19_7, this.mListDimensionBehaviors[0], var17_23, this.mLeft, this.mRight, this.mX, var6_16, this.mMinWidth, this.mMaxDimension[0], this.mHorizontalBiasPercent, var16_12, var15_13, var3_14, this.mMatchConstraintMinWidth, this.mMatchConstraintMaxWidth, this.mMatchConstraintPercentWidth, var11_8);
        }
        var20_24 = var23_3;
        var19_7 = var22_4;
        var22_4 = var18_5;
        if (this.mVerticalResolution == 2) {
            return;
        }
        var14_11 = this.mListDimensionBehaviors[1] == DimensionBehaviour.WRAP_CONTENT && this instanceof ConstraintWidgetContainer != false;
        var15_13 = var5_15 != 0 && ((var3_14 = this.mResolvedDimensionRatioSide) == 1 || var3_14 == -1);
        if (this.mBaselineDistance > 0) {
            if (this.mBaseline.getResolutionNode().state == 1) {
                this.mBaseline.getResolutionNode().addResolvedValue(var1_1);
            } else {
                var18_5 = var1_1;
                var18_5.addEquality((SolverVariable)var21_6, (SolverVariable)var19_7, this.getBaselineDistance(), 6);
                if (this.mBaseline.mTarget != null) {
                    var18_5.addEquality((SolverVariable)var21_6, var18_5.createObjectVariable(this.mBaseline.mTarget), 0, 6);
                    var11_8 = false;
                }
            }
        }
        var23_3 = var19_7;
        var21_6 = var1_1;
        var18_5 = this.mParent;
        var18_5 = var18_5 != null ? var21_6.createObjectVariable(var18_5.mBottom) : null;
        var19_7 = this.mParent;
        var19_7 = var19_7 != null ? var21_6.createObjectVariable(var19_7.mTop) : null;
        this.applyConstraints(var1_1, var12_9, (SolverVariable)var19_7, (SolverVariable)var18_5, this.mListDimensionBehaviors[1], var14_11, this.mTop, this.mBottom, this.mY, var7_17, this.mMinHeight, this.mMaxDimension[1], this.mVerticalBiasPercent, var15_13, var13_10, var4_22, this.mMatchConstraintMinHeight, this.mMatchConstraintMaxHeight, this.mMatchConstraintPercentHeight, var11_8);
        if (var5_15 != 0) {
            if (this.mResolvedDimensionRatioSide == 1) {
                var1_1.addRatio(var22_4, (SolverVariable)var23_3, (SolverVariable)var20_24, var24_2, this.mResolvedDimensionRatio, 6);
            } else {
                var1_1.addRatio((SolverVariable)var20_24, var24_2, var22_4, (SolverVariable)var23_3, this.mResolvedDimensionRatio, 6);
            }
        }
        if (this.mCenter.isConnected() == false) return;
        var21_6.addCenterPoint(this, this.mCenter.getTarget().getOwner(), (float)Math.toRadians(this.mCircleConstraintAngle + 90.0f), this.mCenter.getMargin());
    }

    public boolean allowedInBarrier() {
        if (this.mVisibility != 8) {
            return true;
        }
        return false;
    }

    public void analyze(int n) {
        Optimizer.analyze(n, this);
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2) {
        this.connect(type, constraintWidget, type2, 0, ConstraintAnchor.Strength.STRONG);
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int n) {
        this.connect(type, constraintWidget, type2, n, ConstraintAnchor.Strength.STRONG);
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int n, ConstraintAnchor.Strength strength) {
        this.connect(type, constraintWidget, type2, n, strength, 0);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void connect(ConstraintAnchor.Type var1_1, ConstraintWidget var2_3, ConstraintAnchor.Type var3_4, int var4_5, ConstraintAnchor.Strength var5_6, int var6_7) {
        if (var1_1 != ConstraintAnchor.Type.CENTER) ** GOTO lbl51
        if (var3_4 != ConstraintAnchor.Type.CENTER) ** GOTO lbl36
        var1_1 = this.getAnchor(ConstraintAnchor.Type.LEFT);
        var3_4 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
        var9_8 = this.getAnchor(ConstraintAnchor.Type.TOP);
        var10_10 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
        var7_11 = 0;
        var8_12 = 0;
        if (var1_1 == null) ** GOTO lbl-1000
        var4_5 = var7_11;
        if (!var1_1.isConnected()) lbl-1000: // 2 sources:
        {
            if (var3_4 != null && var3_4.isConnected()) {
                var4_5 = var7_11;
            } else {
                this.connect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var2_3, ConstraintAnchor.Type.LEFT, 0, var5_6, var6_7);
                this.connect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)var2_3, ConstraintAnchor.Type.RIGHT, 0, var5_6, var6_7);
                var4_5 = 1;
            }
        }
        if (var9_8 == null) ** GOTO lbl-1000
        var7_11 = var8_12;
        if (!var9_8.isConnected()) lbl-1000: // 2 sources:
        {
            if (var10_10 != null && var10_10.isConnected()) {
                var7_11 = var8_12;
            } else {
                this.connect(ConstraintAnchor.Type.TOP, (ConstraintWidget)var2_3, ConstraintAnchor.Type.TOP, 0, var5_6, var6_7);
                this.connect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)var2_3, ConstraintAnchor.Type.BOTTOM, 0, var5_6, var6_7);
                var7_11 = 1;
            }
        }
        if (var4_5 != 0 && var7_11 != 0) {
            this.getAnchor(ConstraintAnchor.Type.CENTER).connect(var2_3.getAnchor(ConstraintAnchor.Type.CENTER), 0, var6_7);
            return;
        }
        if (var4_5 != 0) {
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(var2_3.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, var6_7);
            return;
        }
        if (var7_11 == 0) return;
        this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(var2_3.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, var6_7);
        return;
lbl36: // 1 sources:
        if (var3_4 != ConstraintAnchor.Type.LEFT && var3_4 != ConstraintAnchor.Type.RIGHT) {
            if (var3_4 != ConstraintAnchor.Type.TOP) {
                if (var3_4 != ConstraintAnchor.Type.BOTTOM) return;
            }
            this.connect(ConstraintAnchor.Type.TOP, (ConstraintWidget)var2_3, (ConstraintAnchor.Type)var3_4, 0, var5_6, var6_7);
            this.connect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)var2_3, (ConstraintAnchor.Type)var3_4, 0, var5_6, var6_7);
            this.getAnchor(ConstraintAnchor.Type.CENTER).connect(var2_3.getAnchor((ConstraintAnchor.Type)var3_4), 0, var6_7);
            return;
        }
        this.connect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var2_3, (ConstraintAnchor.Type)var3_4, 0, var5_6, var6_7);
        var1_1 = ConstraintAnchor.Type.RIGHT;
        this.connect((ConstraintAnchor.Type)var1_1, (ConstraintWidget)var2_3, (ConstraintAnchor.Type)var3_4, 0, var5_6, var6_7);
        this.getAnchor(ConstraintAnchor.Type.CENTER).connect(var2_3.getAnchor((ConstraintAnchor.Type)var3_4), 0, var6_7);
        return;
lbl51: // 1 sources:
        if (var1_1 == ConstraintAnchor.Type.CENTER_X && (var3_4 == ConstraintAnchor.Type.LEFT || var3_4 == ConstraintAnchor.Type.RIGHT)) {
            var1_1 = this.getAnchor(ConstraintAnchor.Type.LEFT);
            var2_3 = var2_3.getAnchor((ConstraintAnchor.Type)var3_4);
            var3_4 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
            var1_1.connect((ConstraintAnchor)var2_3, 0, var6_7);
            var3_4.connect((ConstraintAnchor)var2_3, 0, var6_7);
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect((ConstraintAnchor)var2_3, 0, var6_7);
            return;
        }
        if (var1_1 == ConstraintAnchor.Type.CENTER_Y && (var3_4 == ConstraintAnchor.Type.TOP || var3_4 == ConstraintAnchor.Type.BOTTOM)) {
            var1_1 = var2_3.getAnchor((ConstraintAnchor.Type)var3_4);
            this.getAnchor(ConstraintAnchor.Type.TOP).connect((ConstraintAnchor)var1_1, 0, var6_7);
            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect((ConstraintAnchor)var1_1, 0, var6_7);
            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect((ConstraintAnchor)var1_1, 0, var6_7);
            return;
        }
        if (var1_1 == ConstraintAnchor.Type.CENTER_X && var3_4 == ConstraintAnchor.Type.CENTER_X) {
            this.getAnchor(ConstraintAnchor.Type.LEFT).connect(var2_3.getAnchor(ConstraintAnchor.Type.LEFT), 0, var6_7);
            this.getAnchor(ConstraintAnchor.Type.RIGHT).connect(var2_3.getAnchor(ConstraintAnchor.Type.RIGHT), 0, var6_7);
            this.getAnchor(ConstraintAnchor.Type.CENTER_X).connect(var2_3.getAnchor((ConstraintAnchor.Type)var3_4), 0, var6_7);
            return;
        }
        if (var1_1 == ConstraintAnchor.Type.CENTER_Y && var3_4 == ConstraintAnchor.Type.CENTER_Y) {
            this.getAnchor(ConstraintAnchor.Type.TOP).connect(var2_3.getAnchor(ConstraintAnchor.Type.TOP), 0, var6_7);
            this.getAnchor(ConstraintAnchor.Type.BOTTOM).connect(var2_3.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, var6_7);
            this.getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(var2_3.getAnchor((ConstraintAnchor.Type)var3_4), 0, var6_7);
            return;
        }
        var9_9 = this.getAnchor((ConstraintAnchor.Type)var1_1);
        if (var9_9.isValidConnection((ConstraintAnchor)(var2_3 = var2_3.getAnchor((ConstraintAnchor.Type)var3_4))) == false) return;
        if (var1_1 == ConstraintAnchor.Type.BASELINE) {
            var1_1 = this.getAnchor(ConstraintAnchor.Type.TOP);
            var3_4 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
            if (var1_1 != null) {
                var1_1.reset();
            }
            if (var3_4 != null) {
                var3_4.reset();
            }
            var4_5 = 0;
        } else if (var1_1 != ConstraintAnchor.Type.TOP && var1_1 != ConstraintAnchor.Type.BOTTOM) {
            if (var1_1 == ConstraintAnchor.Type.LEFT || var1_1 == ConstraintAnchor.Type.RIGHT) {
                var3_4 = this.getAnchor(ConstraintAnchor.Type.CENTER);
                if (var3_4.getTarget() != var2_3) {
                    var3_4.reset();
                }
                var1_1 = this.getAnchor((ConstraintAnchor.Type)var1_1).getOpposite();
                var3_4 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
                if (var3_4.isConnected()) {
                    var1_1.reset();
                    var3_4.reset();
                }
            }
        } else {
            var3_4 = this.getAnchor(ConstraintAnchor.Type.BASELINE);
            if (var3_4 != null) {
                var3_4.reset();
            }
            if ((var3_4 = this.getAnchor(ConstraintAnchor.Type.CENTER)).getTarget() != var2_3) {
                var3_4.reset();
            }
            var1_1 = this.getAnchor((ConstraintAnchor.Type)var1_1).getOpposite();
            var3_4 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
            if (var3_4.isConnected()) {
                var1_1.reset();
                var3_4.reset();
            }
        }
        var9_9.connect((ConstraintAnchor)var2_3, var4_5, var5_6, var6_7);
        var2_3.getOwner().connectedTo(var9_9.getOwner());
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, 0);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n, int n2) {
        this.connect(constraintAnchor, constraintAnchor2, n, ConstraintAnchor.Strength.STRONG, n2);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int n, ConstraintAnchor.Strength strength, int n2) {
        if (constraintAnchor.getOwner() == this) {
            this.connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), n, strength, n2);
        }
    }

    public void connectCircularConstraint(ConstraintWidget constraintWidget, float f, int n) {
        this.immediateConnect(ConstraintAnchor.Type.CENTER, constraintWidget, ConstraintAnchor.Type.CENTER, n, 0);
        this.mCircleConstraintAngle = f;
    }

    public void connectedTo(ConstraintWidget constraintWidget) {
    }

    public void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public void disconnectUnlockedWidget(ConstraintWidget constraintWidget) {
        ArrayList<ConstraintAnchor> arrayList = this.getAnchors();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            ConstraintAnchor constraintAnchor = arrayList.get(i);
            if (!constraintAnchor.isConnected() || constraintAnchor.getTarget().getOwner() != constraintWidget || constraintAnchor.getConnectionCreator() != 2) continue;
            constraintAnchor.reset();
        }
    }

    public void disconnectWidget(ConstraintWidget constraintWidget) {
        ArrayList<ConstraintAnchor> arrayList = this.getAnchors();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            ConstraintAnchor constraintAnchor = arrayList.get(i);
            if (!constraintAnchor.isConnected() || constraintAnchor.getTarget().getOwner() != constraintWidget) continue;
            constraintAnchor.reset();
        }
    }

    public void forceUpdateDrawPosition() {
        int n = this.mX;
        int n2 = this.mY;
        int n3 = this.mX;
        int n4 = this.mWidth;
        int n5 = this.mY;
        int n6 = this.mHeight;
        this.mDrawX = n;
        this.mDrawY = n2;
        this.mDrawWidth = n3 + n4 - n;
        this.mDrawHeight = n5 + n6 - n2;
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                throw new AssertionError((Object)type.name());
            }
            case 9: {
                return null;
            }
            case 8: {
                return this.mCenterY;
            }
            case 7: {
                return this.mCenterX;
            }
            case 6: {
                return this.mCenter;
            }
            case 5: {
                return this.mBaseline;
            }
            case 4: {
                return this.mBottom;
            }
            case 3: {
                return this.mRight;
            }
            case 2: {
                return this.mTop;
            }
            case 1: 
        }
        return this.mLeft;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public float getBiasPercent(int n) {
        if (n == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (n == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public int getBottom() {
        return this.getY() + this.mHeight;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public DimensionBehaviour getDimensionBehaviour(int n) {
        if (n == 0) {
            return this.getHorizontalDimensionBehaviour();
        }
        if (n == 1) {
            return this.getVerticalDimensionBehaviour();
        }
        return null;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public int getDrawBottom() {
        return this.getDrawY() + this.mDrawHeight;
    }

    public int getDrawHeight() {
        return this.mDrawHeight;
    }

    public int getDrawRight() {
        return this.getDrawX() + this.mDrawWidth;
    }

    public int getDrawWidth() {
        return this.mDrawWidth;
    }

    public int getDrawX() {
        return this.mDrawX + this.mOffsetX;
    }

    public int getDrawY() {
        return this.mDrawY + this.mOffsetY;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        Object object = null;
        Object object2 = null;
        if (this.isInHorizontalChain()) {
            Object object3 = this;
            do {
                object = object2;
                if (object2 != null) break;
                object = object2;
                if (object3 == null) break;
                object = object3.getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor constraintAnchor = null;
                object = object == null ? null : object.getTarget();
                object = object == null ? null : object.getOwner();
                if (object == this.getParent()) {
                    return object3;
                }
                if (object != null) {
                    constraintAnchor = object.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
                }
                if (constraintAnchor != null && constraintAnchor.getOwner() != object3) {
                    object2 = object3;
                    continue;
                }
                object3 = object;
            } while (true);
        }
        return object;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public int getInternalDrawBottom() {
        return this.mDrawY + this.mDrawHeight;
    }

    public int getInternalDrawRight() {
        return this.mDrawX + this.mDrawWidth;
    }

    int getInternalDrawX() {
        return this.mDrawX;
    }

    int getInternalDrawY() {
        return this.mDrawY;
    }

    public int getLeft() {
        return this.getX();
    }

    public int getLength(int n) {
        if (n == 0) {
            return this.getWidth();
        }
        if (n == 1) {
            return this.getHeight();
        }
        return 0;
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getOptimizerWrapHeight() {
        int n;
        int n2 = n = this.mHeight;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.mMatchConstraintDefaultHeight == 1) {
                n = Math.max(this.mMatchConstraintMinHeight, n);
            } else if (this.mMatchConstraintMinHeight > 0) {
                this.mHeight = n = this.mMatchConstraintMinHeight;
            } else {
                n = 0;
            }
            int n3 = this.mMatchConstraintMaxHeight;
            n2 = n;
            if (n3 > 0) {
                n2 = n;
                if (n3 < n) {
                    n2 = this.mMatchConstraintMaxHeight;
                }
            }
        }
        return n2;
    }

    public int getOptimizerWrapWidth() {
        int n;
        int n2 = n = this.mWidth;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.mMatchConstraintDefaultWidth == 1) {
                n = Math.max(this.mMatchConstraintMinWidth, n);
            } else if (this.mMatchConstraintMinWidth > 0) {
                this.mWidth = n = this.mMatchConstraintMinWidth;
            } else {
                n = 0;
            }
            int n3 = this.mMatchConstraintMaxWidth;
            n2 = n;
            if (n3 > 0) {
                n2 = n;
                if (n3 < n) {
                    n2 = this.mMatchConstraintMaxWidth;
                }
            }
        }
        return n2;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    int getRelativePositioning(int n) {
        if (n == 0) {
            return this.mRelX;
        }
        if (n == 1) {
            return this.mRelY;
        }
        return 0;
    }

    public ResolutionDimension getResolutionHeight() {
        if (this.mResolutionHeight == null) {
            this.mResolutionHeight = new ResolutionDimension();
        }
        return this.mResolutionHeight;
    }

    public ResolutionDimension getResolutionWidth() {
        if (this.mResolutionWidth == null) {
            this.mResolutionWidth = new ResolutionDimension();
        }
        return this.mResolutionWidth;
    }

    public int getRight() {
        return this.getX() + this.mWidth;
    }

    public WidgetContainer getRootWidgetContainer() {
        ConstraintWidget constraintWidget = this;
        while (constraintWidget.getParent() != null) {
            constraintWidget = constraintWidget.getParent();
        }
        if (constraintWidget instanceof WidgetContainer) {
            return (WidgetContainer)constraintWidget;
        }
        return null;
    }

    protected int getRootX() {
        return this.mX + this.mOffsetX;
    }

    protected int getRootY() {
        return this.mY + this.mOffsetY;
    }

    public int getTop() {
        return this.getY();
    }

    public String getType() {
        return this.mType;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        Object object = null;
        Object object2 = null;
        if (this.isInVerticalChain()) {
            Object object3 = this;
            do {
                object = object2;
                if (object2 != null) break;
                object = object2;
                if (object3 == null) break;
                object = object3.getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor constraintAnchor = null;
                object = object == null ? null : object.getTarget();
                object = object == null ? null : object.getOwner();
                if (object == this.getParent()) {
                    return object3;
                }
                if (object != null) {
                    constraintAnchor = object.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
                }
                if (constraintAnchor != null && constraintAnchor.getOwner() != object3) {
                    object2 = object3;
                    continue;
                }
                object3 = object;
            } while (true);
        }
        return object;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getWrapHeight() {
        return this.mWrapHeight;
    }

    public int getWrapWidth() {
        return this.mWrapWidth;
    }

    public int getX() {
        return this.mX;
    }

    public int getY() {
        return this.mY;
    }

    public boolean hasAncestor(ConstraintWidget constraintWidget) {
        ConstraintWidget constraintWidget2 = this.getParent();
        if (constraintWidget2 == constraintWidget) {
            return true;
        }
        if (constraintWidget2 == constraintWidget.getParent()) {
            return false;
        }
        for (ConstraintWidget constraintWidget3 = constraintWidget2; constraintWidget3 != null; constraintWidget3 = constraintWidget3.getParent()) {
            if (constraintWidget3 == constraintWidget) {
                return true;
            }
            if (constraintWidget3 != constraintWidget.getParent()) continue;
            return true;
        }
        return false;
    }

    public boolean hasBaseline() {
        if (this.mBaselineDistance > 0) {
            return true;
        }
        return false;
    }

    public void immediateConnect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int n, int n2) {
        this.getAnchor(type).connect(constraintWidget.getAnchor(type2), n, n2, ConstraintAnchor.Strength.STRONG, 0, true);
    }

    public boolean isFullyResolved() {
        if (this.mLeft.getResolutionNode().state == 1 && this.mRight.getResolutionNode().state == 1 && this.mTop.getResolutionNode().state == 1 && this.mBottom.getResolutionNode().state == 1) {
            return true;
        }
        return false;
    }

    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }

    public boolean isInHorizontalChain() {
        if (this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft || this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight) {
            return true;
        }
        return false;
    }

    public boolean isInVerticalChain() {
        if (this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop || this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom) {
            return true;
        }
        return false;
    }

    public boolean isInsideConstraintLayout() {
        ConstraintWidget constraintWidget;
        if (constraintWidget == null) {
            return false;
        }
        for (ConstraintWidget constraintWidget2 = constraintWidget = this.getParent(); constraintWidget2 != null; constraintWidget2 = constraintWidget2.getParent()) {
            if (!(constraintWidget2 instanceof ConstraintWidgetContainer)) continue;
            return true;
        }
        return false;
    }

    public boolean isRoot() {
        if (this.mParent == null) {
            return true;
        }
        return false;
    }

    public boolean isRootContainer() {
        ConstraintWidget constraintWidget;
        if (this instanceof ConstraintWidgetContainer && ((constraintWidget = this.mParent) == null || !(constraintWidget instanceof ConstraintWidgetContainer))) {
            return true;
        }
        return false;
    }

    public boolean isSpreadHeight() {
        if (this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
            return true;
        }
        return false;
    }

    public boolean isSpreadWidth() {
        boolean bl;
        int n = this.mMatchConstraintDefaultWidth;
        boolean bl2 = bl = false;
        if (n == 0) {
            bl2 = bl;
            if (this.mDimensionRatio == 0.0f) {
                bl2 = bl;
                if (this.mMatchConstraintMinWidth == 0) {
                    bl2 = bl;
                    if (this.mMatchConstraintMaxWidth == 0) {
                        bl2 = bl;
                        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
                            bl2 = true;
                        }
                    }
                }
            }
        }
        return bl2;
    }

    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }

    public void reset() {
        float f;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mWrapWidth = 0;
        this.mWrapHeight = 0;
        this.mHorizontalBiasPercent = f = DEFAULT_BIAS;
        this.mVerticalBiasPercent = f;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        Object object = this.mWeight;
        object[0] = -1.0f;
        object[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        object = this.mMaxDimension;
        object[0] = Integer.MAX_VALUE;
        object[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        object = this.mResolutionWidth;
        if (object != null) {
            object.reset();
        }
        if ((object = this.mResolutionHeight) != null) {
            object.reset();
        }
        this.mBelongingGroup = null;
        this.mOptimizerMeasurable = false;
        this.mOptimizerMeasured = false;
        this.mGroupsToSolver = false;
    }

    public void resetAllConstraints() {
        this.resetAnchors();
        this.setVerticalBiasPercent(DEFAULT_BIAS);
        this.setHorizontalBiasPercent(DEFAULT_BIAS);
        if (this instanceof ConstraintWidgetContainer) {
            return;
        }
        if (this.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getWidth() == this.getWrapWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
            } else if (this.getWidth() > this.getMinWidth()) {
                this.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
            }
        }
        if (this.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
            if (this.getHeight() == this.getWrapHeight()) {
                this.setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
                return;
            }
            if (this.getHeight() > this.getMinHeight()) {
                this.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
            }
        }
    }

    public void resetAnchor(ConstraintAnchor constraintAnchor) {
        if (this.getParent() != null && this.getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        ConstraintAnchor constraintAnchor2 = this.getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor constraintAnchor3 = this.getAnchor(ConstraintAnchor.Type.RIGHT);
        ConstraintAnchor constraintAnchor4 = this.getAnchor(ConstraintAnchor.Type.TOP);
        ConstraintAnchor constraintAnchor5 = this.getAnchor(ConstraintAnchor.Type.BOTTOM);
        ConstraintAnchor constraintAnchor6 = this.getAnchor(ConstraintAnchor.Type.CENTER);
        ConstraintAnchor constraintAnchor7 = this.getAnchor(ConstraintAnchor.Type.CENTER_X);
        ConstraintAnchor constraintAnchor8 = this.getAnchor(ConstraintAnchor.Type.CENTER_Y);
        if (constraintAnchor == constraintAnchor6) {
            if (constraintAnchor2.isConnected() && constraintAnchor3.isConnected() && constraintAnchor2.getTarget() == constraintAnchor3.getTarget()) {
                constraintAnchor2.reset();
                constraintAnchor3.reset();
            }
            if (constraintAnchor4.isConnected() && constraintAnchor5.isConnected() && constraintAnchor4.getTarget() == constraintAnchor5.getTarget()) {
                constraintAnchor4.reset();
                constraintAnchor5.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
            this.mVerticalBiasPercent = 0.5f;
        } else if (constraintAnchor == constraintAnchor7) {
            if (constraintAnchor2.isConnected() && constraintAnchor3.isConnected() && constraintAnchor2.getTarget().getOwner() == constraintAnchor3.getTarget().getOwner()) {
                constraintAnchor2.reset();
                constraintAnchor3.reset();
            }
            this.mHorizontalBiasPercent = 0.5f;
        } else if (constraintAnchor == constraintAnchor8) {
            if (constraintAnchor4.isConnected() && constraintAnchor5.isConnected() && constraintAnchor4.getTarget().getOwner() == constraintAnchor5.getTarget().getOwner()) {
                constraintAnchor4.reset();
                constraintAnchor5.reset();
            }
            this.mVerticalBiasPercent = 0.5f;
        } else if (constraintAnchor != constraintAnchor2 && constraintAnchor != constraintAnchor3) {
            if ((constraintAnchor == constraintAnchor4 || constraintAnchor == constraintAnchor5) && constraintAnchor4.isConnected() && constraintAnchor4.getTarget() == constraintAnchor5.getTarget()) {
                constraintAnchor6.reset();
            }
        } else if (constraintAnchor2.isConnected() && constraintAnchor2.getTarget() == constraintAnchor3.getTarget()) {
            constraintAnchor6.reset();
        }
        constraintAnchor.reset();
    }

    public void resetAnchors() {
        ConstraintWidget constraintWidget = this.getParent();
        if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        int n = this.mAnchors.size();
        for (int i = 0; i < n; ++i) {
            this.mAnchors.get(i).reset();
        }
    }

    public void resetAnchors(int n) {
        Object object = this.getParent();
        if (object != null && object instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)this.getParent()).handlesInternalConstraints()) {
            return;
        }
        int n2 = this.mAnchors.size();
        for (int i = 0; i < n2; ++i) {
            object = this.mAnchors.get(i);
            if (n != object.getConnectionCreator()) continue;
            if (object.isVerticalAnchor()) {
                this.setVerticalBiasPercent(DEFAULT_BIAS);
            } else {
                this.setHorizontalBiasPercent(DEFAULT_BIAS);
            }
            object.reset();
        }
    }

    public void resetResolutionNodes() {
        for (int i = 0; i < 6; ++i) {
            this.mListAnchors[i].getResolutionNode().reset();
        }
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    public void resolve() {
    }

    public void setBaselineDistance(int n) {
        this.mBaselineDistance = n;
    }

    public void setCompanionWidget(Object object) {
        this.mCompanionWidget = object;
    }

    public void setContainerItemSkip(int n) {
        if (n >= 0) {
            this.mContainerItemSkip = n;
            return;
        }
        this.mContainerItemSkip = 0;
    }

    public void setDebugName(String string2) {
        this.mDebugName = string2;
    }

    public void setDebugSolverName(LinearSystem object, String string2) {
        this.mDebugName = string2;
        Object object2 = object.createObjectVariable(this.mLeft);
        Object object3 = object.createObjectVariable(this.mTop);
        Object object4 = object.createObjectVariable(this.mRight);
        Object object5 = object.createObjectVariable(this.mBottom);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(".left");
        object2.setName(stringBuilder.toString());
        object2 = new StringBuilder();
        object2.append(string2);
        object2.append(".top");
        object3.setName(object2.toString());
        object3 = new StringBuilder();
        object3.append(string2);
        object3.append(".right");
        object4.setName(object3.toString());
        object4 = new StringBuilder();
        object4.append(string2);
        object4.append(".bottom");
        object5.setName(object4.toString());
        if (this.mBaselineDistance > 0) {
            object = object.createObjectVariable(this.mBaseline);
            object5 = new StringBuilder();
            object5.append(string2);
            object5.append(".baseline");
            object.setName(object5.toString());
        }
    }

    public void setDimension(int n, int n2) {
        this.mWidth = n;
        int n3 = this.mMinWidth;
        if ((n = this.mWidth) < n3) {
            this.mWidth = n3;
        }
        n = this.mHeight = n2;
        n2 = this.mMinHeight;
        if (n < n2) {
            this.mHeight = n2;
        }
    }

    public void setDimensionRatio(float f, int n) {
        this.mDimensionRatio = f;
        this.mDimensionRatioSide = n;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setDimensionRatio(String string2) {
        if (string2 != null && string2.length() != 0) {
            int n;
            float f;
            block18 : {
                String string3;
                n = -1;
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                int n2 = string2.length();
                int n3 = string2.indexOf(44);
                if (n3 > 0 && n3 < n2 - 1) {
                    string3 = string2.substring(0, n3);
                    if (string3.equalsIgnoreCase("W")) {
                        n = 0;
                    } else if (string3.equalsIgnoreCase("H")) {
                        n = 1;
                    }
                    ++n3;
                } else {
                    n3 = 0;
                }
                int n4 = string2.indexOf(58);
                if (n4 >= 0 && n4 < n2 - 1) {
                    string3 = string2.substring(n3, n4);
                    string2 = string2.substring(n4 + 1);
                    f = f2;
                    if (string3.length() > 0) {
                        f = f2;
                        if (string2.length() > 0) {
                            try {
                                f3 = Float.parseFloat(string3);
                                float f5 = Float.parseFloat(string2);
                                f = f4;
                                if (f3 <= 0.0f) break block18;
                                f = f4;
                                if (f5 <= 0.0f) break block18;
                                if (n == 1) {
                                    f = Math.abs(f5 / f3);
                                    break block18;
                                }
                                f = Math.abs(f3 / f5);
                            }
                            catch (NumberFormatException numberFormatException) {
                                f = f2;
                            }
                        }
                    }
                } else {
                    string2 = string2.substring(n3);
                    f = f3;
                    if (string2.length() > 0) {
                        try {
                            f = Float.parseFloat(string2);
                        }
                        catch (NumberFormatException numberFormatException) {
                            f = f3;
                        }
                    }
                }
            }
            if (f > 0.0f) {
                this.mDimensionRatio = f;
                this.mDimensionRatioSide = n;
            }
            return;
        }
        this.mDimensionRatio = 0.0f;
    }

    public void setDrawHeight(int n) {
        this.mDrawHeight = n;
    }

    public void setDrawOrigin(int n, int n2) {
        this.mDrawX = n - this.mOffsetX;
        this.mDrawY = n2 - this.mOffsetY;
        this.mX = this.mDrawX;
        this.mY = this.mDrawY;
    }

    public void setDrawWidth(int n) {
        this.mDrawWidth = n;
    }

    public void setDrawX(int n) {
        this.mX = this.mDrawX = n - this.mOffsetX;
    }

    public void setDrawY(int n) {
        this.mY = this.mDrawY = n - this.mOffsetY;
    }

    public void setFrame(int n, int n2, int n3) {
        if (n3 == 0) {
            this.setHorizontalDimension(n, n2);
        } else if (n3 == 1) {
            this.setVerticalDimension(n, n2);
        }
        this.mOptimizerMeasured = true;
    }

    public void setFrame(int n, int n2, int n3, int n4) {
        int n5 = n3 - n;
        n3 = n4 - n2;
        this.mX = n;
        this.mY = n2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        n = n5;
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED) {
            n = n5;
            if (n5 < this.mWidth) {
                n = this.mWidth;
            }
        }
        n2 = n3;
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED) {
            n2 = n3;
            if (n3 < this.mHeight) {
                n2 = this.mHeight;
            }
        }
        this.mWidth = n;
        n = this.mHeight = n2;
        n2 = this.mMinHeight;
        if (n < n2) {
            this.mHeight = n2;
        }
        if ((n = this.mWidth) < (n2 = this.mMinWidth)) {
            this.mWidth = n2;
        }
        this.mOptimizerMeasured = true;
    }

    public void setGoneMargin(ConstraintAnchor.Type type, int n) {
        int n2 = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()];
        if (n2 != 1) {
            if (n2 != 2) {
                if (n2 != 3) {
                    if (n2 != 4) {
                        return;
                    }
                    this.mBottom.mGoneMargin = n;
                    return;
                }
                this.mRight.mGoneMargin = n;
                return;
            }
            this.mTop.mGoneMargin = n;
            return;
        }
        this.mLeft.mGoneMargin = n;
    }

    public void setHeight(int n) {
        this.mHeight = n;
        int n2 = this.mMinHeight;
        if ((n = this.mHeight) < n2) {
            this.mHeight = n2;
        }
    }

    public void setHeightWrapContent(boolean bl) {
        this.mIsHeightWrapContent = bl;
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setHorizontalChainStyle(int n) {
        this.mHorizontalChainStyle = n;
    }

    public void setHorizontalDimension(int n, int n2) {
        this.mX = n;
        n = this.mWidth = n2 - n;
        n2 = this.mMinWidth;
        if (n < n2) {
            this.mWidth = n2;
        }
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setWidth(this.mWrapWidth);
        }
    }

    public void setHorizontalMatchStyle(int n, int n2, int n3, float f) {
        this.mMatchConstraintDefaultWidth = n;
        this.mMatchConstraintMinWidth = n2;
        this.mMatchConstraintMaxWidth = n3;
        this.mMatchConstraintPercentWidth = f;
        if (f < 1.0f && this.mMatchConstraintDefaultWidth == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setHorizontalWeight(float f) {
        this.mWeight[0] = f;
    }

    public void setLength(int n, int n2) {
        if (n2 == 0) {
            this.setWidth(n);
            return;
        }
        if (n2 == 1) {
            this.setHeight(n);
        }
    }

    public void setMaxHeight(int n) {
        this.mMaxDimension[1] = n;
    }

    public void setMaxWidth(int n) {
        this.mMaxDimension[0] = n;
    }

    public void setMinHeight(int n) {
        if (n < 0) {
            this.mMinHeight = 0;
            return;
        }
        this.mMinHeight = n;
    }

    public void setMinWidth(int n) {
        if (n < 0) {
            this.mMinWidth = 0;
            return;
        }
        this.mMinWidth = n;
    }

    public void setOffset(int n, int n2) {
        this.mOffsetX = n;
        this.mOffsetY = n2;
    }

    public void setOrigin(int n, int n2) {
        this.mX = n;
        this.mY = n2;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    void setRelativePositioning(int n, int n2) {
        if (n2 == 0) {
            this.mRelX = n;
            return;
        }
        if (n2 == 1) {
            this.mRelY = n;
        }
    }

    public void setType(String string2) {
        this.mType = string2;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setVerticalChainStyle(int n) {
        this.mVerticalChainStyle = n;
    }

    public void setVerticalDimension(int n, int n2) {
        this.mY = n;
        n = this.mHeight = n2 - n;
        n2 = this.mMinHeight;
        if (n < n2) {
            this.mHeight = n2;
        }
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
        if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            this.setHeight(this.mWrapHeight);
        }
    }

    public void setVerticalMatchStyle(int n, int n2, int n3, float f) {
        this.mMatchConstraintDefaultHeight = n;
        this.mMatchConstraintMinHeight = n2;
        this.mMatchConstraintMaxHeight = n3;
        this.mMatchConstraintPercentHeight = f;
        if (f < 1.0f && this.mMatchConstraintDefaultHeight == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    public void setVerticalWeight(float f) {
        this.mWeight[1] = f;
    }

    public void setVisibility(int n) {
        this.mVisibility = n;
    }

    public void setWidth(int n) {
        this.mWidth = n;
        int n2 = this.mMinWidth;
        if ((n = this.mWidth) < n2) {
            this.mWidth = n2;
        }
    }

    public void setWidthWrapContent(boolean bl) {
        this.mIsWidthWrapContent = bl;
    }

    public void setWrapHeight(int n) {
        this.mWrapHeight = n;
    }

    public void setWrapWidth(int n) {
        this.mWrapWidth = n;
    }

    public void setX(int n) {
        this.mX = n;
    }

    public void setY(int n) {
        this.mY = n;
    }

    public void setupDimensionRatio(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (bl3 && !bl4) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!bl3 && bl4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (!(this.mResolvedDimensionRatioSide != 0 || this.mTop.isConnected() && this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (!(this.mResolvedDimensionRatioSide != 1 || this.mLeft.isConnected() && this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (!(this.mResolvedDimensionRatioSide != -1 || this.mTop.isConnected() && this.mBottom.isConnected() && this.mLeft.isConnected() && this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (bl && !bl2) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!bl && bl2) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1 && bl && bl2) {
            this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
            this.mResolvedDimensionRatioSide = 1;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        CharSequence charSequence = this.mType;
        String string2 = "";
        if (charSequence != null) {
            charSequence = new StringBuilder();
            charSequence.append("type: ");
            charSequence.append(this.mType);
            charSequence.append(" ");
            charSequence = charSequence.toString();
        } else {
            charSequence = "";
        }
        stringBuilder.append((String)charSequence);
        charSequence = string2;
        if (this.mDebugName != null) {
            charSequence = new StringBuilder();
            charSequence.append("id: ");
            charSequence.append(this.mDebugName);
            charSequence.append(" ");
            charSequence = charSequence.toString();
        }
        stringBuilder.append((String)charSequence);
        stringBuilder.append("(");
        stringBuilder.append(this.mX);
        stringBuilder.append(", ");
        stringBuilder.append(this.mY);
        stringBuilder.append(") - (");
        stringBuilder.append(this.mWidth);
        stringBuilder.append(" x ");
        stringBuilder.append(this.mHeight);
        stringBuilder.append(") wrap: (");
        stringBuilder.append(this.mWrapWidth);
        stringBuilder.append(" x ");
        stringBuilder.append(this.mWrapHeight);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void updateDrawPosition() {
        int n = this.mX;
        int n2 = this.mY;
        int n3 = this.mX;
        int n4 = this.mWidth;
        int n5 = this.mY;
        int n6 = this.mHeight;
        this.mDrawX = n;
        this.mDrawY = n2;
        this.mDrawWidth = n3 + n4 - n;
        this.mDrawHeight = n5 + n6 - n2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void updateFromSolver(LinearSystem var1_1) {
        var3_2 = var1_1.getObjectVariableValue(this.mLeft);
        var4_3 = var1_1.getObjectVariableValue(this.mTop);
        var5_4 = var1_1.getObjectVariableValue(this.mRight);
        var6_5 = var1_1.getObjectVariableValue(this.mBottom);
        if (var5_4 - var3_2 < 0 || var6_5 - var4_3 < 0 || var3_2 == Integer.MIN_VALUE || var3_2 == Integer.MAX_VALUE || var4_3 == Integer.MIN_VALUE || var4_3 == Integer.MAX_VALUE || var5_4 == Integer.MIN_VALUE || var5_4 == Integer.MAX_VALUE || var6_5 == Integer.MIN_VALUE) ** GOTO lbl-1000
        var2_6 = var6_5;
        if (var6_5 == Integer.MAX_VALUE) lbl-1000: // 2 sources:
        {
            var3_2 = 0;
            var4_3 = 0;
            var5_4 = 0;
            var2_6 = 0;
        }
        this.setFrame(var3_2, var4_3, var5_4, var2_6);
    }

    public void updateResolutionNodes() {
        for (int i = 0; i < 6; ++i) {
            this.mListAnchors[i].getResolutionNode().update();
        }
    }

    public static enum ContentAlignment {
        BEGIN,
        MIDDLE,
        END,
        TOP,
        VERTICAL_MIDDLE,
        BOTTOM,
        LEFT,
        RIGHT;
        

        private ContentAlignment() {
        }
    }

    public static enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT;
        

        private DimensionBehaviour() {
        }
    }

}

