/*
 * Decompiled with CFR 0_121.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

public class ChainHead {
    private boolean mDefined;
    protected ConstraintWidget mFirst;
    protected ConstraintWidget mFirstMatchConstraintWidget;
    protected ConstraintWidget mFirstVisibleWidget;
    protected boolean mHasComplexMatchWeights;
    protected boolean mHasDefinedWeights;
    protected boolean mHasUndefinedWeights;
    protected ConstraintWidget mHead;
    private boolean mIsRtl = false;
    protected ConstraintWidget mLast;
    protected ConstraintWidget mLastMatchConstraintWidget;
    protected ConstraintWidget mLastVisibleWidget;
    private int mOrientation;
    protected float mTotalWeight = 0.0f;
    protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
    protected int mWidgetsCount;
    protected int mWidgetsMatchCount;

    public ChainHead(ConstraintWidget constraintWidget, int n, boolean bl) {
        this.mFirst = constraintWidget;
        this.mOrientation = n;
        this.mIsRtl = bl;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void defineChainProperties() {
        var3_1 = this.mOrientation * 2;
        var7_2 = this.mFirst;
        var6_3 = this.mFirst;
        var5_4 = this.mFirst;
        var2_5 = false;
        do {
            var4_7 = true;
            if (var2_5) break;
            ++this.mWidgetsCount;
            var6_3.mNextChainWidget[this.mOrientation] = null;
            var6_3.mListNextMatchConstraintsWidget[this.mOrientation] = null;
            if (var6_3.getVisibility() != 8) {
                if (this.mFirstVisibleWidget == null) {
                    this.mFirstVisibleWidget = var6_3;
                }
                this.mLastVisibleWidget = var6_3;
                if (var6_3.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (var6_3.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || var6_3.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || var6_3.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
                    ++this.mWidgetsMatchCount;
                    var1_6 = var6_3.mWeight[this.mOrientation];
                    if (var1_6 > 0.0f) {
                        this.mTotalWeight += var6_3.mWeight[this.mOrientation];
                    }
                    if (ChainHead.isMatchConstraintEqualityCandidate((ConstraintWidget)var6_3, this.mOrientation)) {
                        if (var1_6 < 0.0f) {
                            this.mHasUndefinedWeights = true;
                        } else {
                            this.mHasDefinedWeights = true;
                        }
                        if (this.mWeightedMatchConstraintsWidgets == null) {
                            this.mWeightedMatchConstraintsWidgets = new ArrayList<E>();
                        }
                        this.mWeightedMatchConstraintsWidgets.add((ConstraintWidget)var6_3);
                    }
                    if (this.mFirstMatchConstraintWidget == null) {
                        this.mFirstMatchConstraintWidget = var6_3;
                    }
                    if ((var5_4 = this.mLastMatchConstraintWidget) != null) {
                        var5_4.mListNextMatchConstraintsWidget[this.mOrientation] = var6_3;
                    }
                    this.mLastMatchConstraintWidget = var6_3;
                }
            }
            if (var7_2 != var6_3) {
                var7_2.mNextChainWidget[this.mOrientation] = var6_3;
            }
            var7_2 = var6_3;
            var5_4 = var6_3.mListAnchors[var3_1 + 1].mTarget;
            if (var5_4 == null) ** GOTO lbl45
            var8_8 = var5_4.mOwner;
            if (var8_8.mListAnchors[var3_1].mTarget == null) ** GOTO lbl-1000
            var5_4 = var8_8;
            if (var8_8.mListAnchors[var3_1].mTarget.mOwner != var6_3) lbl-1000: // 2 sources:
            {
                var5_4 = null;
            }
            ** GOTO lbl46
lbl45: // 1 sources:
            var5_4 = null;
lbl46: // 2 sources:
            if (var5_4 != null) {
                var6_3 = var5_4;
                continue;
            }
            var2_5 = true;
        } while (true);
        this.mLast = var6_3;
        this.mHead = this.mOrientation == 0 && this.mIsRtl != false ? this.mLast : this.mFirst;
        if (!this.mHasDefinedWeights || !this.mHasUndefinedWeights) {
            var4_7 = false;
        }
        this.mHasComplexMatchWeights = var4_7;
    }

    private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget constraintWidget, int n) {
        if (constraintWidget.getVisibility() != 8 && constraintWidget.mListDimensionBehaviors[n] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget.mResolvedMatchConstraintDefault[n] == 0 || constraintWidget.mResolvedMatchConstraintDefault[n] == 3)) {
            return true;
        }
        return false;
    }

    public void define() {
        if (!this.mDefined) {
            this.defineChainProperties();
        }
        this.mDefined = true;
    }

    public ConstraintWidget getFirst() {
        return this.mFirst;
    }

    public ConstraintWidget getFirstMatchConstraintWidget() {
        return this.mFirstMatchConstraintWidget;
    }

    public ConstraintWidget getFirstVisibleWidget() {
        return this.mFirstVisibleWidget;
    }

    public ConstraintWidget getHead() {
        return this.mHead;
    }

    public ConstraintWidget getLast() {
        return this.mLast;
    }

    public ConstraintWidget getLastMatchConstraintWidget() {
        return this.mLastMatchConstraintWidget;
    }

    public ConstraintWidget getLastVisibleWidget() {
        return this.mLastVisibleWidget;
    }

    public float getTotalWeight() {
        return this.mTotalWeight;
    }
}

