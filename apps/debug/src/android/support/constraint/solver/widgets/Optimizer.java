/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ChainHead;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.support.constraint.solver.widgets.ResolutionDimension;
import android.support.constraint.solver.widgets.ResolutionNode;

public class Optimizer {
    static final int FLAG_CHAIN_DANGLING = 1;
    static final int FLAG_RECOMPUTE_BOUNDS = 2;
    static final int FLAG_USE_OPTIMIZE = 0;
    public static final int OPTIMIZATION_BARRIER = 2;
    public static final int OPTIMIZATION_CHAIN = 4;
    public static final int OPTIMIZATION_DIMENSIONS = 8;
    public static final int OPTIMIZATION_DIRECT = 1;
    public static final int OPTIMIZATION_GROUPS = 32;
    public static final int OPTIMIZATION_NONE = 0;
    public static final int OPTIMIZATION_RATIO = 16;
    public static final int OPTIMIZATION_STANDARD = 7;
    static boolean[] flags = new boolean[3];

    static void analyze(int n, ConstraintWidget constraintWidget) {
        constraintWidget.updateResolutionNodes();
        ResolutionAnchor resolutionAnchor = constraintWidget.mLeft.getResolutionNode();
        ResolutionAnchor resolutionAnchor2 = constraintWidget.mTop.getResolutionNode();
        ResolutionAnchor resolutionAnchor3 = constraintWidget.mRight.getResolutionNode();
        ResolutionAnchor resolutionAnchor4 = constraintWidget.mBottom.getResolutionNode();
        n = (n & 8) == 8 ? 1 : 0;
        int n2 = constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && Optimizer.optimizableMatchConstraint(constraintWidget, 0) ? 1 : 0;
        if (resolutionAnchor.type != 4 && resolutionAnchor3.type != 4) {
            if (constraintWidget.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.FIXED && (n2 == 0 || constraintWidget.getVisibility() != 8)) {
                if (n2 != 0) {
                    n2 = constraintWidget.getWidth();
                    resolutionAnchor.setType(1);
                    resolutionAnchor3.setType(1);
                    if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null) {
                        if (n != 0) {
                            resolutionAnchor3.dependsOn(resolutionAnchor, 1, constraintWidget.getResolutionWidth());
                        } else {
                            resolutionAnchor3.dependsOn(resolutionAnchor, n2);
                        }
                    } else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget == null) {
                        if (n != 0) {
                            resolutionAnchor3.dependsOn(resolutionAnchor, 1, constraintWidget.getResolutionWidth());
                        } else {
                            resolutionAnchor3.dependsOn(resolutionAnchor, n2);
                        }
                    } else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget != null) {
                        if (n != 0) {
                            resolutionAnchor.dependsOn(resolutionAnchor3, -1, constraintWidget.getResolutionWidth());
                        } else {
                            resolutionAnchor.dependsOn(resolutionAnchor3, - n2);
                        }
                    } else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                        if (n != 0) {
                            constraintWidget.getResolutionWidth().addDependent(resolutionAnchor);
                            constraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
                        }
                        if (constraintWidget.mDimensionRatio == 0.0f) {
                            resolutionAnchor.setType(3);
                            resolutionAnchor3.setType(3);
                            resolutionAnchor.setOpposite(resolutionAnchor3, 0.0f);
                            resolutionAnchor3.setOpposite(resolutionAnchor, 0.0f);
                        } else {
                            resolutionAnchor.setType(2);
                            resolutionAnchor3.setType(2);
                            resolutionAnchor.setOpposite(resolutionAnchor3, - n2);
                            resolutionAnchor3.setOpposite(resolutionAnchor, n2);
                            constraintWidget.setWidth(n2);
                        }
                    }
                }
            } else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget == null) {
                resolutionAnchor.setType(1);
                resolutionAnchor3.setType(1);
                if (n != 0) {
                    resolutionAnchor3.dependsOn(resolutionAnchor, 1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionAnchor3.dependsOn(resolutionAnchor, constraintWidget.getWidth());
                }
            } else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget == null) {
                resolutionAnchor.setType(1);
                resolutionAnchor3.setType(1);
                if (n != 0) {
                    resolutionAnchor3.dependsOn(resolutionAnchor, 1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionAnchor3.dependsOn(resolutionAnchor, constraintWidget.getWidth());
                }
            } else if (constraintWidget.mLeft.mTarget == null && constraintWidget.mRight.mTarget != null) {
                resolutionAnchor.setType(1);
                resolutionAnchor3.setType(1);
                resolutionAnchor.dependsOn(resolutionAnchor3, - constraintWidget.getWidth());
                if (n != 0) {
                    resolutionAnchor.dependsOn(resolutionAnchor3, -1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionAnchor.dependsOn(resolutionAnchor3, - constraintWidget.getWidth());
                }
            } else if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                resolutionAnchor.setType(2);
                resolutionAnchor3.setType(2);
                if (n != 0) {
                    constraintWidget.getResolutionWidth().addDependent(resolutionAnchor);
                    constraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
                    resolutionAnchor.setOpposite(resolutionAnchor3, -1, constraintWidget.getResolutionWidth());
                    resolutionAnchor3.setOpposite(resolutionAnchor, 1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionAnchor.setOpposite(resolutionAnchor3, - constraintWidget.getWidth());
                    resolutionAnchor3.setOpposite(resolutionAnchor, constraintWidget.getWidth());
                }
            }
        }
        n2 = constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && Optimizer.optimizableMatchConstraint(constraintWidget, 1) ? 1 : 0;
        if (resolutionAnchor2.type != 4 && resolutionAnchor4.type != 4) {
            if (constraintWidget.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.FIXED && (n2 == 0 || constraintWidget.getVisibility() != 8)) {
                if (n2 != 0) {
                    n2 = constraintWidget.getHeight();
                    resolutionAnchor2.setType(1);
                    resolutionAnchor4.setType(1);
                    if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
                        if (n != 0) {
                            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, constraintWidget.getResolutionHeight());
                            return;
                        }
                        resolutionAnchor4.dependsOn(resolutionAnchor2, n2);
                        return;
                    }
                    if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget == null) {
                        if (n != 0) {
                            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, constraintWidget.getResolutionHeight());
                            return;
                        }
                        resolutionAnchor4.dependsOn(resolutionAnchor2, n2);
                        return;
                    }
                    if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget != null) {
                        if (n != 0) {
                            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, constraintWidget.getResolutionHeight());
                            return;
                        }
                        resolutionAnchor2.dependsOn(resolutionAnchor4, - n2);
                        return;
                    }
                    if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                        if (n != 0) {
                            constraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
                            constraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
                        }
                        if (constraintWidget.mDimensionRatio == 0.0f) {
                            resolutionAnchor2.setType(3);
                            resolutionAnchor4.setType(3);
                            resolutionAnchor2.setOpposite(resolutionAnchor4, 0.0f);
                            resolutionAnchor4.setOpposite(resolutionAnchor2, 0.0f);
                            return;
                        }
                        resolutionAnchor2.setType(2);
                        resolutionAnchor4.setType(2);
                        resolutionAnchor2.setOpposite(resolutionAnchor4, - n2);
                        resolutionAnchor4.setOpposite(resolutionAnchor2, n2);
                        constraintWidget.setHeight(n2);
                        if (constraintWidget.mBaselineDistance > 0) {
                            constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, constraintWidget.mBaselineDistance);
                            return;
                        }
                    }
                }
            } else if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
                resolutionAnchor2.setType(1);
                resolutionAnchor4.setType(1);
                if (n != 0) {
                    resolutionAnchor4.dependsOn(resolutionAnchor2, 1, constraintWidget.getResolutionHeight());
                } else {
                    resolutionAnchor4.dependsOn(resolutionAnchor2, constraintWidget.getHeight());
                }
                if (constraintWidget.mBaseline.mTarget != null) {
                    constraintWidget.mBaseline.getResolutionNode().setType(1);
                    resolutionAnchor2.dependsOn(1, constraintWidget.mBaseline.getResolutionNode(), - constraintWidget.mBaselineDistance);
                    return;
                }
            } else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget == null) {
                resolutionAnchor2.setType(1);
                resolutionAnchor4.setType(1);
                if (n != 0) {
                    resolutionAnchor4.dependsOn(resolutionAnchor2, 1, constraintWidget.getResolutionHeight());
                } else {
                    resolutionAnchor4.dependsOn(resolutionAnchor2, constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, constraintWidget.mBaselineDistance);
                    return;
                }
            } else if (constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget != null) {
                resolutionAnchor2.setType(1);
                resolutionAnchor4.setType(1);
                if (n != 0) {
                    resolutionAnchor2.dependsOn(resolutionAnchor4, -1, constraintWidget.getResolutionHeight());
                } else {
                    resolutionAnchor2.dependsOn(resolutionAnchor4, - constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, constraintWidget.mBaselineDistance);
                    return;
                }
            } else if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                resolutionAnchor2.setType(2);
                resolutionAnchor4.setType(2);
                if (n != 0) {
                    resolutionAnchor2.setOpposite(resolutionAnchor4, -1, constraintWidget.getResolutionHeight());
                    resolutionAnchor4.setOpposite(resolutionAnchor2, 1, constraintWidget.getResolutionHeight());
                    constraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
                    constraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
                } else {
                    resolutionAnchor2.setOpposite(resolutionAnchor4, - constraintWidget.getHeight());
                    resolutionAnchor4.setOpposite(resolutionAnchor2, constraintWidget.getHeight());
                }
                if (constraintWidget.mBaselineDistance > 0) {
                    constraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, constraintWidget.mBaselineDistance);
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    static boolean applyChainOptimized(ConstraintWidgetContainer var0, LinearSystem var1_1, int var2_2, int var3_3, ChainHead var4_4) {
        var18_5 = var2_2;
        var20_6 = var4_4.mFirst;
        var22_7 = var4_4.mLast;
        var23_8 = var4_4.mFirstVisibleWidget;
        var24_9 = var4_4.mLastVisibleWidget;
        var25_10 = var4_4.mHead;
        var15_11 = false;
        var10_12 = var4_4.mTotalWeight;
        var21_13 = var4_4.mFirstMatchConstraintWidget;
        var4_4 = var4_4.mLastMatchConstraintWidget;
        var19_14 = var0.mListDimensionBehaviors[var18_5] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (var18_5 == 0) {
            var11_15 = var25_10.mHorizontalChainStyle == 0 ? 1 : 0;
            var13_16 = var11_15;
            var11_15 = var25_10.mHorizontalChainStyle == 1 ? 1 : 0;
            var12_17 = var25_10.mHorizontalChainStyle;
            var14_18 = var11_15;
            var11_15 = var12_17 == 2 ? 1 : 0;
        } else {
            var11_15 = var25_10.mVerticalChainStyle == 0 ? 1 : 0;
            var13_16 = var11_15;
            var11_15 = var25_10.mVerticalChainStyle == 1 ? 1 : 0;
            var12_17 = var25_10.mVerticalChainStyle;
            var14_18 = var11_15;
            var11_15 = var12_17 == 2 ? 1 : 0;
        }
        var12_17 = 0;
        var16_19 = 0;
        var7_20 = 0.0f;
        var9_21 = 0.0f;
        var4_4 = var20_6;
        while (!var15_11) {
            var17_24 = var16_19;
            var5_22 = var9_21;
            var6_23 = var7_20;
            if (var4_4.getVisibility() != 8) {
                var17_24 = var16_19 + 1;
                var5_22 = var18_5 == 0 ? var9_21 + (float)var4_4.getWidth() : var9_21 + (float)var4_4.getHeight();
                var6_23 = var5_22;
                if (var4_4 != var23_8) {
                    var6_23 = var5_22 + (float)var4_4.mListAnchors[var3_3].getMargin();
                }
                var5_22 = var6_23;
                if (var4_4 != var24_9) {
                    var5_22 = var6_23 + (float)var4_4.mListAnchors[var3_3 + 1].getMargin();
                }
                var6_23 = var7_20 + (float)var4_4.mListAnchors[var3_3].getMargin() + (float)var4_4.mListAnchors[var3_3 + 1].getMargin();
            }
            var0 = var4_4.mListAnchors[var3_3];
            var16_19 = var12_17;
            if (var4_4.getVisibility() != 8) {
                var16_19 = var12_17;
                if (var4_4.mListDimensionBehaviors[var18_5] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    var16_19 = var12_17 + 1;
                    if (var18_5 == 0) {
                        if (var4_4.mMatchConstraintDefaultWidth != 0) {
                            return false;
                        }
                        if (var4_4.mMatchConstraintMinWidth != 0) return false;
                        if (var4_4.mMatchConstraintMaxWidth != 0) {
                            return false;
                        }
                    } else {
                        if (var4_4.mMatchConstraintDefaultHeight != 0) {
                            return false;
                        }
                        if (var4_4.mMatchConstraintMinHeight != 0) return false;
                        if (var4_4.mMatchConstraintMaxHeight != 0) {
                            return false;
                        }
                    }
                    if (var4_4.mDimensionRatio != 0.0f) {
                        return false;
                    }
                }
            }
            if ((var0 = var4_4.mListAnchors[var3_3 + 1].mTarget) != null) {
                var0 = var0.mOwner;
                if (var0.mListAnchors[var3_3].mTarget == null || var0.mListAnchors[var3_3].mTarget.mOwner != var4_4) {
                    var0 = null;
                }
            } else {
                var0 = null;
            }
            if (var0 != null) {
                var4_4 = var0;
            } else {
                var15_11 = true;
            }
            var12_17 = var16_19;
            var16_19 = var17_24;
            var9_21 = var5_22;
            var7_20 = var6_23;
        }
        var21_13 = var20_6.mListAnchors[var3_3].getResolutionNode();
        var0 = var22_7.mListAnchors[var3_3 + 1].getResolutionNode();
        if (var21_13.target == null) return false;
        if (var0.target == null) {
            return false;
        }
        if (var21_13.target.state != 1) return false;
        if (var0.target.state != 1) {
            return false;
        }
        if (var12_17 > 0 && var12_17 != var16_19) {
            return false;
        }
        var5_22 = 0.0f;
        var6_23 = 0.0f;
        if (var11_15 != 0 || var13_16 != 0 || var14_18 != 0) {
            if (var23_8 != null) {
                var6_23 = var23_8.mListAnchors[var3_3].getMargin();
            }
            var5_22 = var6_23;
            if (var24_9 != null) {
                var5_22 = var6_23 + (float)var24_9.mListAnchors[var3_3 + 1].getMargin();
            }
        }
        var6_23 = (var8_25 = var21_13.target.resolvedOffset) < (var6_23 = var0.target.resolvedOffset) ? var6_23 - var8_25 - var9_21 : var8_25 - var6_23 - var9_21;
        if (var12_17 <= 0 || var12_17 != var16_19) ** GOTO lbl103
        if (var4_4.getParent() != null && var4_4.getParent().mListDimensionBehaviors[var18_5] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
            return false;
        }
        ** GOTO lbl135
lbl103: // 1 sources:
        if (var6_23 < 0.0f) {
            var13_16 = 0;
            var14_18 = 0;
            var11_15 = 1;
        }
        if (var11_15 != 0) ** GOTO lbl161
        if (var13_16 == 0) {
            if (var14_18 == 0) return true;
        }
        if (var13_16 != 0) {
            var7_20 = var6_23 - var5_22;
        } else {
            var7_20 = var6_23;
            if (var14_18 != 0) {
                var7_20 = var6_23 - var5_22;
            }
        }
        var6_23 = var7_20 / (float)(var16_19 + 1);
        if (var14_18 != 0) {
            var6_23 = var16_19 > 1 ? var7_20 / (float)(var16_19 - 1) : var7_20 / 2.0f;
        }
        var5_22 = var7_20 = var8_25;
        if (var20_6.getVisibility() != 8) {
            var5_22 = var7_20 + var6_23;
        }
        var7_20 = var5_22;
        if (var14_18 != 0) {
            var7_20 = var5_22;
            if (var16_19 > 1) {
                var7_20 = var8_25 + (float)var23_8.mListAnchors[var3_3].getMargin();
            }
        }
        if (var13_16 != 0 && var23_8 != null) {
            var5_22 = var23_8.mListAnchors[var3_3].getMargin();
            var0 = var20_6;
            var5_22 = var7_20 + var5_22;
        } else {
            var0 = var20_6;
            var5_22 = var7_20;
        }
        ** GOTO lbl185
lbl135: // 1 sources:
        var9_21 = var6_23 + var9_21 - var7_20;
        var4_4 = var20_6;
        var7_20 = var8_25;
        var6_23 = var5_22;
        while (var4_4 != null) {
            if (LinearSystem.sMetrics != null) {
                var20_6 = LinearSystem.sMetrics;
                --var20_6.nonresolvedWidgets;
                var20_6 = LinearSystem.sMetrics;
                ++var20_6.resolvedWidgets;
                var20_6 = LinearSystem.sMetrics;
                ++var20_6.chainConnectionResolved;
            }
            if ((var20_6 = var4_4.mNextChainWidget[var18_5]) != null || var4_4 == var22_7) {
                var5_22 = var9_21 / (float)var12_17;
                if (var10_12 > 0.0f) {
                    var5_22 = var4_4.mWeight[var18_5] == -1.0f ? 0.0f : var4_4.mWeight[var18_5] * var9_21 / var10_12;
                }
                if (var4_4.getVisibility() == 8) {
                    var5_22 = 0.0f;
                }
                var4_4.mListAnchors[var3_3].getResolutionNode().resolve(var21_13.resolvedTarget, var7_20 += (float)var4_4.mListAnchors[var3_3].getMargin());
                var4_4.mListAnchors[var3_3 + 1].getResolutionNode().resolve(var21_13.resolvedTarget, var7_20 + var5_22);
                var4_4.mListAnchors[var3_3].getResolutionNode().addResolvedValue(var1_1);
                var4_4.mListAnchors[var3_3 + 1].getResolutionNode().addResolvedValue(var1_1);
                var7_20 = var7_20 + var5_22 + (float)var4_4.mListAnchors[var3_3 + 1].getMargin();
            }
            var4_4 = var20_6;
        }
        return true;
lbl161: // 1 sources:
        var7_20 = var20_6.getBiasPercent(var18_5);
        var0 = var20_6;
        var5_22 = var7_20 * (var6_23 - var5_22) + var8_25;
        while (var0 != null) {
            if (LinearSystem.sMetrics != null) {
                var4_4 = LinearSystem.sMetrics;
                --var4_4.nonresolvedWidgets;
                var4_4 = LinearSystem.sMetrics;
                ++var4_4.resolvedWidgets;
                var4_4 = LinearSystem.sMetrics;
                ++var4_4.chainConnectionResolved;
            }
            if ((var4_4 = var0.mNextChainWidget[var18_5]) != null) ** GOTO lbl-1000
            var6_23 = var5_22;
            if (var0 == var22_7) lbl-1000: // 2 sources:
            {
                var6_23 = var18_5 == 0 ? (float)var0.getWidth() : (float)var0.getHeight();
                var0.mListAnchors[var3_3].getResolutionNode().resolve(var21_13.resolvedTarget, var5_22 += (float)var0.mListAnchors[var3_3].getMargin());
                var0.mListAnchors[var3_3 + 1].getResolutionNode().resolve(var21_13.resolvedTarget, var5_22 + var6_23);
                var0.mListAnchors[var3_3].getResolutionNode().addResolvedValue(var1_1);
                var0.mListAnchors[var3_3 + 1].getResolutionNode().addResolvedValue(var1_1);
                var6_23 = var5_22 + var6_23 + (float)var0.mListAnchors[var3_3 + 1].getMargin();
            }
            var0 = var4_4;
            var5_22 = var6_23;
        }
        return true;
lbl185: // 2 sources:
        do {
            var11_15 = var2_2;
            if (var0 == null) return true;
            if (LinearSystem.sMetrics != null) {
                var4_4 = LinearSystem.sMetrics;
                --var4_4.nonresolvedWidgets;
                var4_4 = LinearSystem.sMetrics;
                ++var4_4.resolvedWidgets;
                var4_4 = LinearSystem.sMetrics;
                ++var4_4.chainConnectionResolved;
            }
            if ((var4_4 = var0.mNextChainWidget[var11_15]) != null || var0 == var22_7) {
                var7_20 = var11_15 == 0 ? (float)var0.getWidth() : (float)var0.getHeight();
                if (var0 != var23_8) {
                    var5_22 += (float)var0.mListAnchors[var3_3].getMargin();
                }
                var0.mListAnchors[var3_3].getResolutionNode().resolve(var21_13.resolvedTarget, var5_22);
                var0.mListAnchors[var3_3 + 1].getResolutionNode().resolve(var21_13.resolvedTarget, var5_22 + var7_20);
                var0.mListAnchors[var3_3].getResolutionNode().addResolvedValue(var1_1);
                var0.mListAnchors[var3_3 + 1].getResolutionNode().addResolvedValue(var1_1);
                var7_20 = var5_22 + ((float)var0.mListAnchors[var3_3 + 1].getMargin() + var7_20);
                if (var4_4 != null) {
                    var5_22 = var7_20;
                    if (var4_4.getVisibility() != 8) {
                        var5_22 = var7_20 + var6_23;
                    }
                } else {
                    var5_22 = var7_20;
                }
            }
            var0 = var4_4;
        } while (true);
    }

    static void checkMatchParent(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        int n;
        int n2;
        if (constraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            n = constraintWidget.mLeft.mMargin;
            n2 = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n2);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(n, n2);
        }
        if (constraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            n = constraintWidget.mTop.mMargin;
            n2 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + n);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(n, n2);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean optimizableMatchConstraint(ConstraintWidget arrdimensionBehaviour, int n) {
        if (arrdimensionBehaviour.mListDimensionBehaviors[n] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            return false;
        }
        float f = arrdimensionBehaviour.mDimensionRatio;
        int n2 = 1;
        if (f != 0.0f) {
            arrdimensionBehaviour = arrdimensionBehaviour.mListDimensionBehaviors;
            n = n == 0 ? n2 : 0;
            if (arrdimensionBehaviour[n] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) return false;
            return false;
        }
        if (n == 0) {
            if (arrdimensionBehaviour.mMatchConstraintDefaultWidth != 0) {
                return false;
            }
            if (arrdimensionBehaviour.mMatchConstraintMinWidth == 0 && arrdimensionBehaviour.mMatchConstraintMaxWidth == 0) return true;
            return false;
        }
        if (arrdimensionBehaviour.mMatchConstraintDefaultHeight != 0) {
            return false;
        }
        if (arrdimensionBehaviour.mMatchConstraintMinHeight != 0) return false;
        if (arrdimensionBehaviour.mMatchConstraintMaxHeight == 0) return true;
        return false;
    }

    static void setOptimizedWidget(ConstraintWidget constraintWidget, int n, int n2) {
        int n3 = n * 2;
        int n4 = n3 + 1;
        constraintWidget.mListAnchors[n3].getResolutionNode().resolvedTarget = constraintWidget.getParent().mLeft.getResolutionNode();
        constraintWidget.mListAnchors[n3].getResolutionNode().resolvedOffset = n2;
        constraintWidget.mListAnchors[n3].getResolutionNode().state = 1;
        constraintWidget.mListAnchors[n4].getResolutionNode().resolvedTarget = constraintWidget.mListAnchors[n3].getResolutionNode();
        constraintWidget.mListAnchors[n4].getResolutionNode().resolvedOffset = constraintWidget.getLength(n);
        constraintWidget.mListAnchors[n4].getResolutionNode().state = 1;
    }
}

