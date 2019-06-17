/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.util.SparseIntArray
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 */
package android.support.constraint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.constraint.ConstraintHelper;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Constraints;
import android.support.constraint.Guideline;
import android.support.constraint.Placeholder;
import android.support.constraint.R;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.widgets.Analyzer;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.ConstraintWidgetGroup;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.support.constraint.solver.widgets.ResolutionDimension;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConstraintLayout
extends ViewGroup {
    static final boolean ALLOWS_EMBEDDED = false;
    private static final boolean CACHE_MEASURED_DIMENSION = false;
    private static final boolean DEBUG = false;
    public static final int DESIGN_INFO_ID = 0;
    private static final String TAG = "ConstraintLayout";
    private static final boolean USE_CONSTRAINTS_HELPER = true;
    public static final String VERSION = "ConstraintLayout-1.1.3";
    SparseArray<View> mChildrenByIds = new SparseArray();
    private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList(4);
    private ConstraintSet mConstraintSet = null;
    private int mConstraintSetId = -1;
    private HashMap<String, Integer> mDesignIds = new HashMap();
    private boolean mDirtyHierarchy = true;
    private int mLastMeasureHeight = -1;
    int mLastMeasureHeightMode = 0;
    int mLastMeasureHeightSize = -1;
    private int mLastMeasureWidth = -1;
    int mLastMeasureWidthMode = 0;
    int mLastMeasureWidthSize = -1;
    ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
    private int mMaxHeight = Integer.MAX_VALUE;
    private int mMaxWidth = Integer.MAX_VALUE;
    private Metrics mMetrics;
    private int mMinHeight = 0;
    private int mMinWidth = 0;
    private int mOptimizationLevel = 7;
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList(100);

    public ConstraintLayout(Context context) {
        super(context);
        this.init(null);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(attributeSet);
    }

    public ConstraintLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init(attributeSet);
    }

    private final ConstraintWidget getTargetWidget(int n) {
        View view;
        if (n == 0) {
            return this.mLayoutWidget;
        }
        View view2 = view = (View)this.mChildrenByIds.get(n);
        if (view == null) {
            view2 = view = this.findViewById(n);
            if (view != null) {
                view2 = view;
                if (view != this) {
                    view2 = view;
                    if (view.getParent() == this) {
                        this.onViewAdded(view);
                        view2 = view;
                    }
                }
            }
        }
        if (view2 == this) {
            return this.mLayoutWidget;
        }
        if (view2 == null) {
            return null;
        }
        return ((LayoutParams)view2.getLayoutParams()).widget;
    }

    private void init(AttributeSet attributeSet) {
        this.mLayoutWidget.setCompanionWidget((Object)this);
        this.mChildrenByIds.put(this.getId(), (Object)this);
        this.mConstraintSet = null;
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_Layout);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 == R.styleable.ConstraintLayout_Layout_android_minWidth) {
                    this.mMinWidth = attributeSet.getDimensionPixelOffset(n2, this.mMinWidth);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_minHeight) {
                    this.mMinHeight = attributeSet.getDimensionPixelOffset(n2, this.mMinHeight);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
                    this.mMaxWidth = attributeSet.getDimensionPixelOffset(n2, this.mMaxWidth);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
                    this.mMaxHeight = attributeSet.getDimensionPixelOffset(n2, this.mMaxHeight);
                    continue;
                }
                if (n2 == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
                    this.mOptimizationLevel = attributeSet.getInt(n2, this.mOptimizationLevel);
                    continue;
                }
                if (n2 != R.styleable.ConstraintLayout_Layout_constraintSet) continue;
                n2 = attributeSet.getResourceId(n2, 0);
                try {
                    this.mConstraintSet = new ConstraintSet();
                    this.mConstraintSet.load(this.getContext(), n2);
                }
                catch (Resources.NotFoundException notFoundException) {
                    this.mConstraintSet = null;
                }
                this.mConstraintSetId = n2;
            }
            attributeSet.recycle();
        }
        this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
    }

    private void internalMeasureChildren(int n, int n2) {
        int n3 = this.getPaddingTop() + this.getPaddingBottom();
        int n4 = this.getPaddingLeft() + this.getPaddingRight();
        int n5 = this.getChildCount();
        int n6 = 0;
        do {
            int n7 = n;
            Object object = this;
            if (n6 >= n5) break;
            View view = object.getChildAt(n6);
            if (view.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                ConstraintWidget constraintWidget = layoutParams.widget;
                if (!layoutParams.isGuideline && !layoutParams.isHelper) {
                    constraintWidget.setVisibility(view.getVisibility());
                    int n8 = layoutParams.width;
                    int n9 = layoutParams.height;
                    int n10 = !layoutParams.horizontalDimensionFixed && !layoutParams.verticalDimensionFixed && (layoutParams.horizontalDimensionFixed || layoutParams.matchConstraintDefaultWidth != 1) && layoutParams.width != -1 && (layoutParams.verticalDimensionFixed || layoutParams.matchConstraintDefaultHeight != 1 && layoutParams.height != -1) ? 0 : 1;
                    int n11 = 0;
                    int n12 = 0;
                    int n13 = 0;
                    int n14 = 0;
                    int n15 = 0;
                    int n16 = 0;
                    int n17 = n8;
                    int n18 = n9;
                    if (n10 != 0) {
                        if (n8 == 0) {
                            n15 = ConstraintLayout.getChildMeasureSpec((int)n7, (int)n4, (int)-2);
                            n18 = 1;
                        } else if (n8 == -1) {
                            n15 = ConstraintLayout.getChildMeasureSpec((int)n7, (int)n4, (int)-1);
                            n18 = n11;
                        } else {
                            n18 = n13;
                            if (n8 == -2) {
                                n18 = 1;
                            }
                            n15 = ConstraintLayout.getChildMeasureSpec((int)n7, (int)n4, (int)n8);
                        }
                        if (n9 == 0) {
                            n10 = ConstraintLayout.getChildMeasureSpec((int)n2, (int)n3, (int)-2);
                            n12 = 1;
                        } else if (n9 == -1) {
                            n10 = ConstraintLayout.getChildMeasureSpec((int)n2, (int)n3, (int)-1);
                            n12 = n14;
                        } else {
                            n12 = n16;
                            if (n9 == -2) {
                                n12 = 1;
                            }
                            n10 = ConstraintLayout.getChildMeasureSpec((int)n2, (int)n3, (int)n9);
                        }
                        view.measure(n15, n10);
                        object = object.mMetrics;
                        if (object != null) {
                            ++object.measures;
                        }
                        boolean bl = n8 == -2;
                        constraintWidget.setWidthWrapContent(bl);
                        bl = n9 == -2;
                        constraintWidget.setHeightWrapContent(bl);
                        n17 = view.getMeasuredWidth();
                        n10 = view.getMeasuredHeight();
                        n15 = n12;
                        n12 = n18;
                        n18 = n10;
                    }
                    constraintWidget.setWidth(n17);
                    constraintWidget.setHeight(n18);
                    if (n12 != 0) {
                        constraintWidget.setWrapWidth(n17);
                    }
                    if (n15 != 0) {
                        constraintWidget.setWrapHeight(n18);
                    }
                    if (layoutParams.needsBaseline && (n18 = view.getBaseline()) != -1) {
                        constraintWidget.setBaselineDistance(n18);
                    }
                }
            }
            ++n6;
        } while (true);
    }

    private void internalMeasureDimensions(int n, int n2) {
        Object object;
        boolean bl;
        int n3;
        int n4;
        Object object2;
        Object object3;
        Object object4;
        int n5;
        boolean bl2;
        int n6;
        int n7;
        Object object5 = this;
        int n8 = this.getPaddingTop() + this.getPaddingBottom();
        int n9 = this.getPaddingLeft() + this.getPaddingRight();
        int n10 = this.getChildCount();
        for (n3 = 0; n3 < n10; ++n3) {
            object4 = object5.getChildAt(n3);
            if (object4.getVisibility() == 8) continue;
            object2 = (LayoutParams)object4.getLayoutParams();
            object = object2.widget;
            if (object2.isGuideline || object2.isHelper) continue;
            object.setVisibility(object4.getVisibility());
            n4 = object2.width;
            n5 = object2.height;
            if (n4 != 0 && n5 != 0) {
                n7 = 0;
                bl2 = false;
                if (n4 == -2) {
                    n7 = 1;
                }
                n6 = ConstraintLayout.getChildMeasureSpec((int)n, (int)n9, (int)n4);
                if (n5 == -2) {
                    bl2 = true;
                }
                object4.measure(n6, ConstraintLayout.getChildMeasureSpec((int)n2, (int)n8, (int)n5));
                object3 = object5.mMetrics;
                if (object3 != null) {
                    ++object3.measures;
                }
                bl = n4 == -2;
                object.setWidthWrapContent(bl);
                bl = n5 == -2;
                object.setHeightWrapContent(bl);
                n4 = object4.getMeasuredWidth();
                n5 = object4.getMeasuredHeight();
                object.setWidth(n4);
                object.setHeight(n5);
                if (n7 != 0) {
                    object.setWrapWidth(n4);
                }
                if (bl2) {
                    object.setWrapHeight(n5);
                }
                if (object2.needsBaseline && (n7 = object4.getBaseline()) != -1) {
                    object.setBaselineDistance(n7);
                }
                if (!object2.horizontalDimensionFixed || !object2.verticalDimensionFixed) continue;
                object.getResolutionWidth().resolve(n4);
                object.getResolutionHeight().resolve(n5);
                continue;
            }
            object.getResolutionWidth().invalidate();
            object.getResolutionHeight().invalidate();
        }
        object5.mLayoutWidget.solveGraph();
        n6 = 0;
        do {
            int n11 = n;
            if (n6 >= n10) break;
            object4 = object5.getChildAt(n6);
            if (object4.getVisibility() == 8) {
                object4 = object5;
            } else {
                object = (LayoutParams)object4.getLayoutParams();
                object2 = object.widget;
                if (!object.isGuideline) {
                    if (object.isHelper) {
                        object4 = object5;
                    } else {
                        object2.setVisibility(object4.getVisibility());
                        int n12 = object.width;
                        int n13 = object.height;
                        if (n12 != 0 && n13 != 0) {
                            object4 = object5;
                        } else {
                            object3 = object2.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
                            ResolutionAnchor resolutionAnchor = object2.getAnchor(ConstraintAnchor.Type.RIGHT).getResolutionNode();
                            n3 = object2.getAnchor(ConstraintAnchor.Type.LEFT).getTarget() != null && object2.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget() != null ? 1 : 0;
                            ResolutionAnchor resolutionAnchor2 = object2.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
                            ResolutionAnchor resolutionAnchor3 = object2.getAnchor(ConstraintAnchor.Type.BOTTOM).getResolutionNode();
                            n5 = object2.getAnchor(ConstraintAnchor.Type.TOP).getTarget() != null && object2.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget() != null ? 1 : 0;
                            if (n12 == 0 && n13 == 0 && n3 != 0 && n5 != 0) {
                                object4 = object5;
                            } else {
                                bl2 = false;
                                boolean bl3 = false;
                                int n14 = 0;
                                int n15 = 0;
                                n4 = object5.mLayoutWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
                                n7 = object5.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
                                if (n4 == 0) {
                                    object2.getResolutionWidth().invalidate();
                                }
                                if (n7 == 0) {
                                    object2.getResolutionHeight().invalidate();
                                }
                                if (n12 == 0) {
                                    if (n4 != 0 && object2.isSpreadWidth() && n3 != 0 && object3.isResolved() && resolutionAnchor.isResolved()) {
                                        n12 = (int)(resolutionAnchor.getResolvedValue() - object3.getResolvedValue());
                                        object2.getResolutionWidth().resolve(n12);
                                        n3 = ConstraintLayout.getChildMeasureSpec((int)n11, (int)n9, (int)n12);
                                        n11 = n4;
                                    } else {
                                        n3 = ConstraintLayout.getChildMeasureSpec((int)n11, (int)n9, (int)-2);
                                        bl2 = true;
                                        n11 = 0;
                                    }
                                } else if (n12 == -1) {
                                    n3 = ConstraintLayout.getChildMeasureSpec((int)n11, (int)n9, (int)-1);
                                    n11 = n4;
                                } else {
                                    bl2 = bl3;
                                    if (n12 == -2) {
                                        bl2 = true;
                                    }
                                    n3 = ConstraintLayout.getChildMeasureSpec((int)n11, (int)n9, (int)n12);
                                    n11 = n4;
                                }
                                if (n13 == 0) {
                                    if (n7 != 0 && object2.isSpreadHeight() && n5 != 0 && resolutionAnchor2.isResolved() && resolutionAnchor3.isResolved()) {
                                        float f = resolutionAnchor3.getResolvedValue();
                                        float f2 = resolutionAnchor2.getResolvedValue();
                                        n5 = n7;
                                        n13 = (int)(f - f2);
                                        object2.getResolutionHeight().resolve(n13);
                                        n7 = ConstraintLayout.getChildMeasureSpec((int)n2, (int)n8, (int)n13);
                                        n4 = n14;
                                    } else {
                                        n7 = ConstraintLayout.getChildMeasureSpec((int)n2, (int)n8, (int)-2);
                                        n4 = 1;
                                        n5 = 0;
                                    }
                                } else {
                                    n5 = n7;
                                    if (n13 == -1) {
                                        n7 = ConstraintLayout.getChildMeasureSpec((int)n2, (int)n8, (int)-1);
                                        n4 = n14;
                                    } else {
                                        n4 = n15;
                                        if (n13 == -2) {
                                            n4 = 1;
                                        }
                                        n7 = ConstraintLayout.getChildMeasureSpec((int)n2, (int)n8, (int)n13);
                                    }
                                }
                                object4.measure(n3, n7);
                                object5 = this;
                                object3 = object5.mMetrics;
                                if (object3 != null) {
                                    ++object3.measures;
                                }
                                bl = n12 == -2;
                                object2.setWidthWrapContent(bl);
                                bl = n13 == -2;
                                object2.setHeightWrapContent(bl);
                                n3 = object4.getMeasuredWidth();
                                n7 = object4.getMeasuredHeight();
                                object2.setWidth(n3);
                                object2.setHeight(n7);
                                if (bl2) {
                                    object2.setWrapWidth(n3);
                                }
                                if (n4 != 0) {
                                    object2.setWrapHeight(n7);
                                }
                                if (n11 != 0) {
                                    object2.getResolutionWidth().resolve(n3);
                                } else {
                                    object2.getResolutionWidth().remove();
                                }
                                if (n5 != 0) {
                                    object2.getResolutionHeight().resolve(n7);
                                } else {
                                    object2.getResolutionHeight().remove();
                                }
                                if (object.needsBaseline) {
                                    n3 = object4.getBaseline();
                                    object4 = object5;
                                    if (n3 != -1) {
                                        object2.setBaselineDistance(n3);
                                        object4 = object5;
                                    }
                                } else {
                                    object4 = object5;
                                }
                            }
                        }
                    }
                } else {
                    object4 = object5;
                }
            }
            ++n6;
            object5 = object4;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void setChildrenConstraints() {
        var13_1 = this.isInEditMode();
        var9_2 = this.getChildCount();
        if (var13_1) {
            for (var2_3 = 0; var2_3 < var9_2; ++var2_3) {
                var16_4 = this.getChildAt(var2_3);
                try {
                    var15_8 = this.getResources().getResourceName(var16_4.getId());
                    this.setDesignInformation(0, var15_8, var16_4.getId());
                    var3_5 = var15_8.indexOf(47);
                    var14_6 = var15_8;
                    if (var3_5 != -1) {
                        var14_6 = var15_8.substring(var3_5 + 1);
                    }
                    this.getTargetWidget(var16_4.getId()).setDebugName((String)var14_6);
                    continue;
                }
                catch (Resources.NotFoundException var14_7) {
                    // empty catch block
                }
            }
        }
        for (var2_3 = 0; var2_3 < var9_2; ++var2_3) {
            var14_6 = this.getViewWidget(this.getChildAt(var2_3));
            if (var14_6 == null) continue;
            var14_6.reset();
        }
        if (this.mConstraintSetId != -1) {
            for (var2_3 = 0; var2_3 < var9_2; ++var2_3) {
                var14_6 = this.getChildAt(var2_3);
                if (var14_6.getId() != this.mConstraintSetId || !(var14_6 instanceof Constraints)) continue;
                this.mConstraintSet = ((Constraints)var14_6).getConstraintSet();
            }
        }
        if ((var14_6 = this.mConstraintSet) != null) {
            var14_6.applyToInternal(this);
        }
        this.mLayoutWidget.removeAllChildren();
        var8_9 = this.mConstraintHelpers.size();
        if (var8_9 > 0) {
            for (var2_3 = 0; var2_3 < var8_9; ++var2_3) {
                this.mConstraintHelpers.get(var2_3).updatePreLayout(this);
            }
        }
        for (var2_3 = 0; var2_3 < var9_2; ++var2_3) {
            var14_6 = this.getChildAt(var2_3);
            if (!(var14_6 instanceof Placeholder)) continue;
            ((Placeholder)var14_6).updatePreLayout(this);
        }
        var10_10 = 0;
        while (var10_10 < var9_2) {
            var15_8 = this.getChildAt(var10_10);
            var16_4 = this.getViewWidget((View)var15_8);
            if (var16_4 == null) ** GOTO lbl221
            var14_6 = (LayoutParams)var15_8.getLayoutParams();
            var14_6.validate();
            if (var14_6.helped) {
                var14_6.helped = false;
            } else if (var13_1) {
                try {
                    var17_18 = this.getResources().getResourceName(var15_8.getId());
                    this.setDesignInformation(0, var17_18, var15_8.getId());
                    var17_18 = var17_18.substring(var17_18.indexOf("id/") + 3);
                    this.getTargetWidget(var15_8.getId()).setDebugName((String)var17_18);
                }
                catch (Resources.NotFoundException var17_19) {
                    // empty catch block
                }
            }
            var16_4.setVisibility(var15_8.getVisibility());
            if (var14_6.isInPlaceholder) {
                var16_4.setVisibility(8);
            }
            var16_4.setCompanionWidget(var15_8);
            this.mLayoutWidget.add((ConstraintWidget)var16_4);
            if (!var14_6.verticalDimensionFixed || !var14_6.horizontalDimensionFixed) {
                this.mVariableDimensionsWidgets.add((ConstraintWidget)var16_4);
            }
            if (!var14_6.isGuideline) ** GOTO lbl86
            var15_8 = (android.support.constraint.solver.widgets.Guideline)var16_4;
            var2_3 = var14_6.resolvedGuideBegin;
            var3_5 = var14_6.resolvedGuideEnd;
            var1_11 = var14_6.resolvedGuidePercent;
            if (Build.VERSION.SDK_INT < 17) {
                var2_3 = var14_6.guideBegin;
                var3_5 = var14_6.guideEnd;
                var1_11 = var14_6.guidePercent;
            }
            if (var1_11 != -1.0f) {
                var15_8.setGuidePercent(var1_11);
            } else if (var2_3 != -1) {
                var15_8.setGuideBegin(var2_3);
            } else if (var3_5 != -1) {
                var15_8.setGuideEnd(var3_5);
            }
            ** GOTO lbl221
lbl86: // 1 sources:
            if (var14_6.leftToLeft == -1 && var14_6.leftToRight == -1 && var14_6.rightToLeft == -1 && var14_6.rightToRight == -1 && var14_6.startToStart == -1 && var14_6.startToEnd == -1 && var14_6.endToStart == -1 && var14_6.endToEnd == -1 && var14_6.topToTop == -1 && var14_6.topToBottom == -1 && var14_6.bottomToTop == -1 && var14_6.bottomToBottom == -1 && var14_6.baselineToBaseline == -1 && var14_6.editorAbsoluteX == -1 && var14_6.editorAbsoluteY == -1 && var14_6.circleConstraint == -1 && var14_6.width != -1 && var14_6.height != -1) ** GOTO lbl221
            var3_5 = var14_6.resolvedLeftToLeft;
            var4_12 = var14_6.resolvedLeftToRight;
            var2_3 = var14_6.resolvedRightToLeft;
            var6_14 = var14_6.resolvedRightToRight;
            var5_13 = var14_6.resolveGoneLeftMargin;
            var7_15 = var14_6.resolveGoneRightMargin;
            var1_11 = var14_6.resolvedHorizontalBias;
            if (Build.VERSION.SDK_INT >= 17) ** GOTO lbl141
            var2_3 = var14_6.leftToLeft;
            var6_14 = var14_6.leftToRight;
            var7_15 = var14_6.rightToLeft;
            var12_17 = var14_6.rightToRight;
            var4_12 = var14_6.goneLeftMargin;
            var5_13 = var14_6.goneRightMargin;
            var1_11 = var14_6.horizontalBias;
            if (var2_3 != -1 || var6_14 != -1) ** GOTO lbl-1000
            if (var14_6.startToStart != -1) {
                var3_5 = var14_6.startToStart;
                var2_3 = var6_14;
            } else if (var14_6.startToEnd != -1) {
                var6_14 = var14_6.startToEnd;
                var3_5 = var2_3;
                var2_3 = var6_14;
            } else lbl-1000: // 2 sources:
            {
                var3_5 = var2_3;
                var2_3 = var6_14;
            }
            if (var7_15 != -1 || var12_17 != -1) ** GOTO lbl-1000
            var6_14 = var3_5;
            if (var14_6.endToStart != -1) {
                var11_16 = var14_6.endToStart;
                var3_5 = var6_14;
                var7_15 = var5_13;
                var5_13 = var4_12;
                var4_12 = var2_3;
                var6_14 = var12_17;
                var2_3 = var11_16;
            } else if (var14_6.endToEnd != -1) {
                var12_17 = var14_6.endToEnd;
                var3_5 = var6_14;
                var11_16 = var5_13;
                var5_13 = var4_12;
                var4_12 = var2_3;
                var6_14 = var12_17;
                var2_3 = var7_15;
                var7_15 = var11_16;
            } else lbl-1000: // 2 sources:
            {
                var11_16 = var5_13;
                var5_13 = var4_12;
                var4_12 = var2_3;
                var6_14 = var12_17;
                var2_3 = var7_15;
                var7_15 = var11_16;
            }
lbl141: // 4 sources:
            if (var14_6.circleConstraint != -1) {
                var15_8 = this.getTargetWidget(var14_6.circleConstraint);
                if (var15_8 != null) {
                    var16_4.connectCircularConstraint((ConstraintWidget)var15_8, var14_6.circleAngle, var14_6.circleRadius);
                }
            } else {
                if (var3_5 != -1) {
                    var15_8 = this.getTargetWidget(var3_5);
                    if (var15_8 != null) {
                        var16_4.immediateConnect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var15_8, ConstraintAnchor.Type.LEFT, var14_6.leftMargin, var5_13);
                    }
                } else if (var4_12 != -1 && (var15_8 = this.getTargetWidget(var4_12)) != null) {
                    var16_4.immediateConnect(ConstraintAnchor.Type.LEFT, (ConstraintWidget)var15_8, ConstraintAnchor.Type.RIGHT, var14_6.leftMargin, var5_13);
                }
                var15_8 = var14_6;
                if (var2_3 != -1) {
                    var17_18 = this.getTargetWidget(var2_3);
                    if (var17_18 != null) {
                        var16_4.immediateConnect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)var17_18, ConstraintAnchor.Type.LEFT, var15_8.rightMargin, var7_15);
                    }
                } else if (var6_14 != -1 && (var17_18 = this.getTargetWidget(var6_14)) != null) {
                    var16_4.immediateConnect(ConstraintAnchor.Type.RIGHT, (ConstraintWidget)var17_18, ConstraintAnchor.Type.RIGHT, var15_8.rightMargin, var7_15);
                }
                if (var15_8.topToTop != -1) {
                    var17_18 = this.getTargetWidget(var15_8.topToTop);
                    if (var17_18 != null) {
                        var16_4.immediateConnect(ConstraintAnchor.Type.TOP, (ConstraintWidget)var17_18, ConstraintAnchor.Type.TOP, var15_8.topMargin, var15_8.goneTopMargin);
                    }
                } else if (var15_8.topToBottom != -1 && (var17_18 = this.getTargetWidget(var15_8.topToBottom)) != null) {
                    var16_4.immediateConnect(ConstraintAnchor.Type.TOP, (ConstraintWidget)var17_18, ConstraintAnchor.Type.BOTTOM, var15_8.topMargin, var15_8.goneTopMargin);
                }
                if (var15_8.bottomToTop != -1) {
                    var17_18 = this.getTargetWidget(var15_8.bottomToTop);
                    if (var17_18 != null) {
                        var16_4.immediateConnect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)var17_18, ConstraintAnchor.Type.TOP, var15_8.bottomMargin, var15_8.goneBottomMargin);
                    }
                } else if (var15_8.bottomToBottom != -1 && (var17_18 = this.getTargetWidget(var15_8.bottomToBottom)) != null) {
                    var16_4.immediateConnect(ConstraintAnchor.Type.BOTTOM, (ConstraintWidget)var17_18, ConstraintAnchor.Type.BOTTOM, var15_8.bottomMargin, var15_8.goneBottomMargin);
                }
                if (var15_8.baselineToBaseline != -1) {
                    var18_20 = (View)this.mChildrenByIds.get(var15_8.baselineToBaseline);
                    var17_18 = this.getTargetWidget(var15_8.baselineToBaseline);
                    if (var17_18 != null && var18_20 != null && var18_20.getLayoutParams() instanceof LayoutParams) {
                        var18_20 = (LayoutParams)var18_20.getLayoutParams();
                        var15_8.needsBaseline = true;
                        var18_20.needsBaseline = true;
                        var16_4.getAnchor(ConstraintAnchor.Type.BASELINE).connect(var17_18.getAnchor(ConstraintAnchor.Type.BASELINE), 0, -1, ConstraintAnchor.Strength.STRONG, 0, true);
                        var16_4.getAnchor(ConstraintAnchor.Type.TOP).reset();
                        var16_4.getAnchor(ConstraintAnchor.Type.BOTTOM).reset();
                    }
                }
                if (var1_11 >= 0.0f && var1_11 != 0.5f) {
                    var16_4.setHorizontalBiasPercent(var1_11);
                }
                if (var15_8.verticalBias >= 0.0f && var15_8.verticalBias != 0.5f) {
                    var16_4.setVerticalBiasPercent(var15_8.verticalBias);
                }
            }
            if (var13_1 && (var14_6.editorAbsoluteX != -1 || var14_6.editorAbsoluteY != -1)) {
                var16_4.setOrigin(var14_6.editorAbsoluteX, var14_6.editorAbsoluteY);
            }
            if (!var14_6.horizontalDimensionFixed) {
                if (var14_6.width == -1) {
                    var16_4.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                    var16_4.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.LEFT).mMargin = var14_6.leftMargin;
                    var16_4.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.RIGHT).mMargin = var14_6.rightMargin;
                } else {
                    var16_4.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    var16_4.setWidth(0);
                }
            } else {
                var16_4.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                var16_4.setWidth(var14_6.width);
            }
            if (!var14_6.verticalDimensionFixed) {
                if (var14_6.height == -1) {
                    var16_4.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
                    var16_4.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.TOP).mMargin = var14_6.topMargin;
                    var16_4.getAnchor((ConstraintAnchor.Type)ConstraintAnchor.Type.BOTTOM).mMargin = var14_6.bottomMargin;
                } else {
                    var16_4.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                    var16_4.setHeight(0);
                }
            } else {
                var16_4.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                var16_4.setHeight(var14_6.height);
            }
            if (var14_6.dimensionRatio != null) {
                var16_4.setDimensionRatio(var14_6.dimensionRatio);
            }
            var16_4.setHorizontalWeight(var14_6.horizontalWeight);
            var16_4.setVerticalWeight(var14_6.verticalWeight);
            var16_4.setHorizontalChainStyle(var14_6.horizontalChainStyle);
            var16_4.setVerticalChainStyle(var14_6.verticalChainStyle);
            var16_4.setHorizontalMatchStyle(var14_6.matchConstraintDefaultWidth, var14_6.matchConstraintMinWidth, var14_6.matchConstraintMaxWidth, var14_6.matchConstraintPercentWidth);
            var16_4.setVerticalMatchStyle(var14_6.matchConstraintDefaultHeight, var14_6.matchConstraintMinHeight, var14_6.matchConstraintMaxHeight, var14_6.matchConstraintPercentHeight);
lbl221: // 6 sources:
            ++var10_10;
        }
    }

    private void setSelfDimensionBehaviour(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        n = View.MeasureSpec.getSize((int)n);
        int n4 = View.MeasureSpec.getMode((int)n2);
        n2 = View.MeasureSpec.getSize((int)n2);
        int n5 = this.getPaddingTop();
        int n6 = this.getPaddingBottom();
        int n7 = this.getPaddingLeft();
        int n8 = this.getPaddingRight();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
        int n9 = 0;
        int n10 = 0;
        this.getLayoutParams();
        if (n3 != Integer.MIN_VALUE) {
            if (n3 != 0) {
                n = n3 != 1073741824 ? n9 : Math.min(this.mMaxWidth, n) - (n7 + n8);
            } else {
                dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                n = n9;
            }
        } else {
            dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        if (n4 != Integer.MIN_VALUE) {
            if (n4 != 0) {
                n2 = n4 != 1073741824 ? n10 : Math.min(this.mMaxHeight, n2) - (n5 + n6);
            } else {
                dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                n2 = n10;
            }
        } else {
            dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        this.mLayoutWidget.setMinWidth(0);
        this.mLayoutWidget.setMinHeight(0);
        this.mLayoutWidget.setHorizontalDimensionBehaviour(dimensionBehaviour);
        this.mLayoutWidget.setWidth(n);
        this.mLayoutWidget.setVerticalDimensionBehaviour(dimensionBehaviour2);
        this.mLayoutWidget.setHeight(n2);
        this.mLayoutWidget.setMinWidth(this.mMinWidth - this.getPaddingLeft() - this.getPaddingRight());
        this.mLayoutWidget.setMinHeight(this.mMinHeight - this.getPaddingTop() - this.getPaddingBottom());
    }

    private void updateHierarchy() {
        boolean bl;
        int n = this.getChildCount();
        boolean bl2 = false;
        int n2 = 0;
        do {
            bl = bl2;
            if (n2 >= n) break;
            if (this.getChildAt(n2).isLayoutRequested()) {
                bl = true;
                break;
            }
            ++n2;
        } while (true);
        if (bl) {
            this.mVariableDimensionsWidgets.clear();
            this.setChildrenConstraints();
        }
    }

    private void updatePostMeasures() {
        int n;
        int n2 = this.getChildCount();
        for (n = 0; n < n2; ++n) {
            View view = this.getChildAt(n);
            if (!(view instanceof Placeholder)) continue;
            ((Placeholder)view).updatePostMeasure(this);
        }
        n2 = this.mConstraintHelpers.size();
        if (n2 > 0) {
            for (n = 0; n < n2; ++n) {
                this.mConstraintHelpers.get(n).updatePostMeasure(this);
            }
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, n, layoutParams);
        if (Build.VERSION.SDK_INT < 14) {
            this.onViewAdded(view);
        }
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.isInEditMode()) {
            int n = this.getChildCount();
            float f = this.getWidth();
            float f2 = this.getHeight();
            float f3 = 1080.0f;
            for (int i = 0; i < n; ++i) {
                Paint paint = this.getChildAt(i);
                if (paint.getVisibility() == 8 || (paint = paint.getTag()) == null || !(paint instanceof String) || (paint = ((String)paint).split(",")).length != 4) continue;
                int n2 = Integer.parseInt(paint[0]);
                int n3 = Integer.parseInt(paint[1]);
                int n4 = Integer.parseInt(paint[2]);
                int n5 = Integer.parseInt(paint[3]);
                n2 = (int)((float)n2 / f3 * f);
                n3 = (int)((float)n3 / 1920.0f * f2);
                n4 = (int)((float)n4 / f3 * f);
                n5 = (int)((float)n5 / 1920.0f * f2);
                paint = new Paint();
                paint.setColor(-65536);
                canvas.drawLine((float)n2, (float)n3, (float)(n2 + n4), (float)n3, paint);
                canvas.drawLine((float)(n2 + n4), (float)n3, (float)(n2 + n4), (float)(n3 + n5), paint);
                canvas.drawLine((float)(n2 + n4), (float)(n3 + n5), (float)n2, (float)(n3 + n5), paint);
                canvas.drawLine((float)n2, (float)(n3 + n5), (float)n2, (float)n3, paint);
                paint.setColor(-16711936);
                canvas.drawLine((float)n2, (float)n3, (float)(n2 + n4), (float)(n3 + n5), paint);
                canvas.drawLine((float)n2, (float)(n3 + n5), (float)(n2 + n4), (float)n3, paint);
            }
            return;
        }
    }

    public void fillMetrics(Metrics metrics) {
        this.mMetrics = metrics;
        this.mLayoutWidget.fillMetrics(metrics);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public Object getDesignInformation(int n, Object object) {
        if (n == 0 && object instanceof String) {
            object = (String)object;
            HashMap<String, Integer> hashMap = this.mDesignIds;
            if (hashMap != null && hashMap.containsKey(object)) {
                return this.mDesignIds.get(object);
            }
        }
        return null;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getOptimizationLevel() {
        return this.mLayoutWidget.getOptimizationLevel();
    }

    public View getViewById(int n) {
        return (View)this.mChildrenByIds.get(n);
    }

    public final ConstraintWidget getViewWidget(View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).widget;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        n2 = this.getChildCount();
        bl = this.isInEditMode();
        for (n = 0; n < n2; ++n) {
            View view = this.getChildAt(n);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            ConstraintWidget constraintWidget = layoutParams.widget;
            if (view.getVisibility() == 8 && !layoutParams.isGuideline && !layoutParams.isHelper && !bl || layoutParams.isInPlaceholder) continue;
            n3 = constraintWidget.getDrawX();
            n4 = constraintWidget.getDrawY();
            int n5 = constraintWidget.getWidth() + n3;
            int n6 = constraintWidget.getHeight() + n4;
            view.layout(n3, n4, n5, n6);
            if (!(view instanceof Placeholder) || (view = ((Placeholder)view).getContent()) == null) continue;
            view.setVisibility(0);
            view.layout(n3, n4, n5, n6);
        }
        n2 = this.mConstraintHelpers.size();
        if (n2 > 0) {
            for (n = 0; n < n2; ++n) {
                this.mConstraintHelpers.get(n).updatePostLayout(this);
            }
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        Object object;
        System.currentTimeMillis();
        int n4 = View.MeasureSpec.getMode((int)n);
        int n5 = View.MeasureSpec.getSize((int)n);
        int n6 = View.MeasureSpec.getMode((int)n2);
        int n7 = View.MeasureSpec.getSize((int)n2);
        int n8 = this.getPaddingLeft();
        int n9 = this.getPaddingTop();
        this.mLayoutWidget.setX(n8);
        this.mLayoutWidget.setY(n9);
        this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
        this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
        if (Build.VERSION.SDK_INT >= 17) {
            object = this.mLayoutWidget;
            boolean bl = this.getLayoutDirection() == 1;
            object.setRtl(bl);
        }
        this.setSelfDimensionBehaviour(n, n2);
        int n10 = this.mLayoutWidget.getWidth();
        int n11 = this.mLayoutWidget.getHeight();
        int n12 = 0;
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            this.updateHierarchy();
            n12 = 1;
        }
        boolean bl = (this.mOptimizationLevel & 8) == 8;
        if (bl) {
            this.mLayoutWidget.preOptimize();
            this.mLayoutWidget.optimizeForDimensions(n10, n11);
            this.internalMeasureDimensions(n, n2);
        } else {
            this.internalMeasureChildren(n, n2);
        }
        this.updatePostMeasures();
        if (this.getChildCount() > 0 && n12 != 0) {
            Analyzer.determineGroups(this.mLayoutWidget);
        }
        if (this.mLayoutWidget.mGroupsWrapOptimized) {
            if (this.mLayoutWidget.mHorizontalWrapOptimized && n4 == Integer.MIN_VALUE) {
                if (this.mLayoutWidget.mWrapFixedWidth < n5) {
                    object = this.mLayoutWidget;
                    object.setWidth(object.mWrapFixedWidth);
                }
                this.mLayoutWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }
            if (this.mLayoutWidget.mVerticalWrapOptimized && n6 == Integer.MIN_VALUE) {
                if (this.mLayoutWidget.mWrapFixedHeight < n7) {
                    object = this.mLayoutWidget;
                    object.setHeight(object.mWrapFixedHeight);
                }
                this.mLayoutWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
            }
        }
        n12 = this.mOptimizationLevel;
        int n13 = 0;
        if ((n12 & 32) == 32) {
            n12 = this.mLayoutWidget.getWidth();
            n3 = this.mLayoutWidget.getHeight();
            if (this.mLastMeasureWidth != n12 && n4 == 1073741824) {
                Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, n12);
            }
            if (this.mLastMeasureHeight != n3 && n6 == 1073741824) {
                Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, n3);
            }
            if (this.mLayoutWidget.mHorizontalWrapOptimized && this.mLayoutWidget.mWrapFixedWidth > n5) {
                Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, n5);
            }
            if (this.mLayoutWidget.mVerticalWrapOptimized && this.mLayoutWidget.mWrapFixedHeight > n7) {
                Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, n7);
            }
        }
        int n14 = 0;
        if (this.getChildCount() > 0) {
            this.solveLinearSystem("First pass");
        }
        n6 = this.mVariableDimensionsWidgets.size();
        int n15 = this.getPaddingBottom() + n9;
        int n16 = n8 + this.getPaddingRight();
        if (n6 > 0) {
            View view;
            n7 = 0;
            n4 = this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
            n3 = this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 1 : 0;
            n8 = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
            n12 = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
            n5 = 0;
            for (int i = 0; i < n6; ++i) {
                int n17;
                int n18;
                int n19;
                int n20;
                object = this.mVariableDimensionsWidgets.get(i);
                view = (View)object.getCompanionWidget();
                if (view == null) {
                    n20 = n8;
                    n17 = n12;
                    n19 = n5;
                    n18 = n7;
                } else {
                    LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                    if (!layoutParams.isHelper) {
                        if (layoutParams.isGuideline) {
                            n20 = n8;
                            n17 = n12;
                            n19 = n5;
                            n18 = n7;
                        } else if (view.getVisibility() == 8) {
                            n20 = n8;
                            n17 = n12;
                            n19 = n5;
                            n18 = n7;
                        } else if (bl && object.getResolutionWidth().isResolved() && object.getResolutionHeight().isResolved()) {
                            n20 = n8;
                            n17 = n12;
                            n19 = n5;
                            n18 = n7;
                        } else {
                            n20 = layoutParams.width == -2 && layoutParams.horizontalDimensionFixed ? ConstraintLayout.getChildMeasureSpec((int)n, (int)n16, (int)layoutParams.width) : View.MeasureSpec.makeMeasureSpec((int)object.getWidth(), (int)1073741824);
                            n17 = layoutParams.height == -2 && layoutParams.verticalDimensionFixed ? ConstraintLayout.getChildMeasureSpec((int)n2, (int)n15, (int)layoutParams.height) : View.MeasureSpec.makeMeasureSpec((int)object.getHeight(), (int)1073741824);
                            view.measure(n20, n17);
                            Metrics metrics = this.mMetrics;
                            if (metrics != null) {
                                ++metrics.additionalMeasures;
                            }
                            int n21 = n13 + 1;
                            n13 = view.getMeasuredWidth();
                            n20 = view.getMeasuredHeight();
                            if (n13 != object.getWidth()) {
                                object.setWidth(n13);
                                if (bl) {
                                    object.getResolutionWidth().resolve(n13);
                                }
                                if (n4 != 0 && object.getRight() > n8) {
                                    n8 = Math.max(n8, object.getRight() + object.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                                }
                                n13 = 1;
                            } else {
                                n13 = n7;
                            }
                            n7 = n12;
                            if (n20 != object.getHeight()) {
                                object.setHeight(n20);
                                if (bl) {
                                    object.getResolutionHeight().resolve(n20);
                                }
                                n7 = n12;
                                if (n3 != 0) {
                                    n7 = n12;
                                    if (object.getBottom() > n12) {
                                        n7 = Math.max(n12, object.getBottom() + object.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                                    }
                                }
                                n13 = 1;
                            }
                            n12 = n13;
                            if (layoutParams.needsBaseline) {
                                n20 = view.getBaseline();
                                n12 = n13;
                                if (n20 != -1) {
                                    n12 = n13;
                                    if (n20 != object.getBaselineDistance()) {
                                        object.setBaselineDistance(n20);
                                        n12 = 1;
                                    }
                                }
                            }
                            n20 = n8;
                            n17 = n7;
                            n19 = n5;
                            n13 = n21;
                            n18 = n12;
                            if (Build.VERSION.SDK_INT >= 11) {
                                n19 = ConstraintLayout.combineMeasuredStates((int)n5, (int)view.getMeasuredState());
                                n20 = n8;
                                n17 = n7;
                                n13 = n21;
                                n18 = n12;
                            }
                        }
                    } else {
                        n18 = n7;
                        n19 = n5;
                        n17 = n12;
                        n20 = n8;
                    }
                }
                n8 = n20;
                n12 = n17;
                n5 = n19;
                n7 = n18;
            }
            n13 = n6;
            if (n7 != 0) {
                this.mLayoutWidget.setWidth(n10);
                this.mLayoutWidget.setHeight(n11);
                if (bl) {
                    this.mLayoutWidget.solveGraph();
                }
                this.solveLinearSystem("2nd pass");
                n6 = 0;
                if (this.mLayoutWidget.getWidth() < n8) {
                    this.mLayoutWidget.setWidth(n8);
                    n6 = 1;
                }
                if (this.mLayoutWidget.getHeight() < n12) {
                    this.mLayoutWidget.setHeight(n12);
                    n6 = 1;
                }
                if (n6 != 0) {
                    this.solveLinearSystem("3rd pass");
                }
            }
            n12 = n10;
            n10 = n14;
            for (n8 = 0; n8 < n13; ++n8) {
                object = this.mVariableDimensionsWidgets.get(n8);
                view = (View)object.getCompanionWidget();
                if (view == null || view.getMeasuredWidth() == object.getWidth() && view.getMeasuredHeight() == object.getHeight()) continue;
                if (object.getVisibility() == 8) continue;
                view.measure(View.MeasureSpec.makeMeasureSpec((int)object.getWidth(), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)object.getHeight(), (int)1073741824));
                object = this.mMetrics;
                if (object != null) {
                    ++object.additionalMeasures;
                }
                ++n10;
            }
        } else {
            n5 = 0;
        }
        n12 = this.mLayoutWidget.getWidth() + n16;
        n8 = this.mLayoutWidget.getHeight() + n15;
        if (Build.VERSION.SDK_INT >= 11) {
            n = ConstraintLayout.resolveSizeAndState((int)n12, (int)n, (int)n5);
            n12 = ConstraintLayout.resolveSizeAndState((int)n8, (int)n2, (int)(n5 << 16));
            n2 = Math.min(this.mMaxWidth, n & 16777215);
            n12 = Math.min(this.mMaxHeight, n12 & 16777215);
            n = n2;
            if (this.mLayoutWidget.isWidthMeasuredTooSmall()) {
                n = n2 | 16777216;
            }
            n2 = n12;
            if (this.mLayoutWidget.isHeightMeasuredTooSmall()) {
                n2 = n12 | 16777216;
            }
            this.setMeasuredDimension(n, n2);
            this.mLastMeasureWidth = n;
            this.mLastMeasureHeight = n2;
            return;
        }
        this.setMeasuredDimension(n12, n8);
        this.mLastMeasureWidth = n12;
        this.mLastMeasureHeight = n8;
    }

    public void onViewAdded(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        Object object = this.getViewWidget(view);
        if (view instanceof Guideline && !(object instanceof android.support.constraint.solver.widgets.Guideline)) {
            object = (LayoutParams)view.getLayoutParams();
            object.widget = new android.support.constraint.solver.widgets.Guideline();
            object.isGuideline = true;
            ((android.support.constraint.solver.widgets.Guideline)object.widget).setOrientation(object.orientation);
        }
        if (view instanceof ConstraintHelper) {
            object = (ConstraintHelper)view;
            object.validateParams();
            ((LayoutParams)view.getLayoutParams()).isHelper = true;
            if (!this.mConstraintHelpers.contains(object)) {
                this.mConstraintHelpers.add((ConstraintHelper)((Object)object));
            }
        }
        this.mChildrenByIds.put(view.getId(), (Object)view);
        this.mDirtyHierarchy = true;
    }

    public void onViewRemoved(View view) {
        if (Build.VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        ConstraintWidget constraintWidget = this.getViewWidget(view);
        this.mLayoutWidget.remove(constraintWidget);
        this.mConstraintHelpers.remove((Object)view);
        this.mVariableDimensionsWidgets.remove(constraintWidget);
        this.mDirtyHierarchy = true;
    }

    public void removeView(View view) {
        super.removeView(view);
        if (Build.VERSION.SDK_INT < 14) {
            this.onViewRemoved(view);
        }
    }

    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
        this.mLastMeasureWidth = -1;
        this.mLastMeasureHeight = -1;
        this.mLastMeasureWidthSize = -1;
        this.mLastMeasureHeightSize = -1;
        this.mLastMeasureWidthMode = 0;
        this.mLastMeasureHeightMode = 0;
    }

    public void setConstraintSet(ConstraintSet constraintSet) {
        this.mConstraintSet = constraintSet;
    }

    public void setDesignInformation(int n, Object object, Object object2) {
        if (n == 0 && object instanceof String && object2 instanceof Integer) {
            if (this.mDesignIds == null) {
                this.mDesignIds = new HashMap();
            }
            String string2 = (String)object;
            n = string2.indexOf("/");
            object = string2;
            if (n != -1) {
                object = string2.substring(n + 1);
            }
            n = (Integer)object2;
            this.mDesignIds.put((String)object, n);
        }
    }

    public void setId(int n) {
        this.mChildrenByIds.remove(this.getId());
        super.setId(n);
        this.mChildrenByIds.put(this.getId(), (Object)this);
    }

    public void setMaxHeight(int n) {
        if (n == this.mMaxHeight) {
            return;
        }
        this.mMaxHeight = n;
        this.requestLayout();
    }

    public void setMaxWidth(int n) {
        if (n == this.mMaxWidth) {
            return;
        }
        this.mMaxWidth = n;
        this.requestLayout();
    }

    public void setMinHeight(int n) {
        if (n == this.mMinHeight) {
            return;
        }
        this.mMinHeight = n;
        this.requestLayout();
    }

    public void setMinWidth(int n) {
        if (n == this.mMinWidth) {
            return;
        }
        this.mMinWidth = n;
        this.requestLayout();
    }

    public void setOptimizationLevel(int n) {
        this.mLayoutWidget.setOptimizationLevel(n);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    protected void solveLinearSystem(String object) {
        this.mLayoutWidget.layout();
        object = this.mMetrics;
        if (object != null) {
            ++object.resolutions;
        }
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public static final int BASELINE = 5;
        public static final int BOTTOM = 4;
        public static final int CHAIN_PACKED = 2;
        public static final int CHAIN_SPREAD = 0;
        public static final int CHAIN_SPREAD_INSIDE = 1;
        public static final int END = 7;
        public static final int HORIZONTAL = 0;
        public static final int LEFT = 1;
        public static final int MATCH_CONSTRAINT = 0;
        public static final int MATCH_CONSTRAINT_PERCENT = 2;
        public static final int MATCH_CONSTRAINT_SPREAD = 0;
        public static final int MATCH_CONSTRAINT_WRAP = 1;
        public static final int PARENT_ID = 0;
        public static final int RIGHT = 2;
        public static final int START = 6;
        public static final int TOP = 3;
        public static final int UNSET = -1;
        public static final int VERTICAL = 1;
        public int baselineToBaseline = -1;
        public int bottomToBottom = -1;
        public int bottomToTop = -1;
        public float circleAngle = 0.0f;
        public int circleConstraint = -1;
        public int circleRadius = 0;
        public boolean constrainedHeight = false;
        public boolean constrainedWidth = false;
        public String dimensionRatio = null;
        int dimensionRatioSide = 1;
        float dimensionRatioValue = 0.0f;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int endToEnd = -1;
        public int endToStart = -1;
        public int goneBottomMargin = -1;
        public int goneEndMargin = -1;
        public int goneLeftMargin = -1;
        public int goneRightMargin = -1;
        public int goneStartMargin = -1;
        public int goneTopMargin = -1;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = -1.0f;
        public boolean helped = false;
        public float horizontalBias = 0.5f;
        public int horizontalChainStyle = 0;
        boolean horizontalDimensionFixed = true;
        public float horizontalWeight = -1.0f;
        boolean isGuideline = false;
        boolean isHelper = false;
        boolean isInPlaceholder = false;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        public int matchConstraintDefaultHeight = 0;
        public int matchConstraintDefaultWidth = 0;
        public int matchConstraintMaxHeight = 0;
        public int matchConstraintMaxWidth = 0;
        public int matchConstraintMinHeight = 0;
        public int matchConstraintMinWidth = 0;
        public float matchConstraintPercentHeight = 1.0f;
        public float matchConstraintPercentWidth = 1.0f;
        boolean needsBaseline = false;
        public int orientation = -1;
        int resolveGoneLeftMargin = -1;
        int resolveGoneRightMargin = -1;
        int resolvedGuideBegin;
        int resolvedGuideEnd;
        float resolvedGuidePercent;
        float resolvedHorizontalBias = 0.5f;
        int resolvedLeftToLeft = -1;
        int resolvedLeftToRight = -1;
        int resolvedRightToLeft = -1;
        int resolvedRightToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int topToBottom = -1;
        public int topToTop = -1;
        public float verticalBias = 0.5f;
        public int verticalChainStyle = 0;
        boolean verticalDimensionFixed = true;
        public float verticalWeight = -1.0f;
        ConstraintWidget widget = new ConstraintWidget();

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public LayoutParams(Context var1_1, AttributeSet var2_2) {
            super(var1_1, (AttributeSet)var2_2);
            var1_1 = var1_1.obtainStyledAttributes((AttributeSet)var2_2, R.styleable.ConstraintLayout_Layout);
            var7_9 = var1_1.getIndexCount();
            var5_10 = 0;
            do {
                if (var5_10 >= var7_9) {
                    var1_1.recycle();
                    this.validate();
                    return;
                }
                var6_13 = var1_1.getIndex(var5_10);
                switch (Table.map.get(var6_13)) {
                    default: {
                        break;
                    }
                    case 50: {
                        this.editorAbsoluteY = var1_1.getDimensionPixelOffset(var6_13, this.editorAbsoluteY);
                        break;
                    }
                    case 49: {
                        this.editorAbsoluteX = var1_1.getDimensionPixelOffset(var6_13, this.editorAbsoluteX);
                        break;
                    }
                    case 48: {
                        this.verticalChainStyle = var1_1.getInt(var6_13, 0);
                        break;
                    }
                    case 47: {
                        this.horizontalChainStyle = var1_1.getInt(var6_13, 0);
                        break;
                    }
                    case 46: {
                        this.verticalWeight = var1_1.getFloat(var6_13, this.verticalWeight);
                        break;
                    }
                    case 45: {
                        this.horizontalWeight = var1_1.getFloat(var6_13, this.horizontalWeight);
                        break;
                    }
                    case 44: {
                        this.dimensionRatio = var1_1.getString(var6_13);
                        this.dimensionRatioValue = Float.NaN;
                        this.dimensionRatioSide = -1;
                        var2_2 = this.dimensionRatio;
                        if (var2_2 == null) break;
                        var8_14 = var2_2.length();
                        var6_13 = this.dimensionRatio.indexOf(44);
                        if (var6_13 > 0 && var6_13 < var8_14 - 1) {
                            var2_2 = this.dimensionRatio.substring(0, var6_13);
                            if (var2_2.equalsIgnoreCase("W")) {
                                this.dimensionRatioSide = 0;
                            } else if (var2_2.equalsIgnoreCase("H")) {
                                this.dimensionRatioSide = 1;
                            }
                            ++var6_13;
                        } else {
                            var6_13 = 0;
                        }
                        var9_15 = this.dimensionRatio.indexOf(58);
                        if (var9_15 >= 0 && var9_15 < var8_14 - 1) {
                            var2_2 = this.dimensionRatio.substring(var6_13, var9_15);
                            var10_16 = this.dimensionRatio.substring(var9_15 + 1);
                            if (var2_2.length() <= 0 || var10_16.length() <= 0) break;
                            try {
                                var3_11 = Float.parseFloat((String)var2_2);
                                var4_12 = Float.parseFloat(var10_16);
                                if (var3_11 <= 0.0f || var4_12 <= 0.0f) break;
                                if (this.dimensionRatioSide == 1) {
                                    this.dimensionRatioValue = Math.abs(var4_12 / var3_11);
                                    break;
                                }
                                this.dimensionRatioValue = Math.abs(var3_11 / var4_12);
                            }
                            catch (NumberFormatException var2_3) {}
                            break;
                        }
                        var2_2 = this.dimensionRatio.substring(var6_13);
                        if (var2_2.length() <= 0) break;
                        try {
                            this.dimensionRatioValue = Float.parseFloat((String)var2_2);
                        }
                        catch (NumberFormatException var2_4) {}
                        break;
                    }
                    case 42: {
                        break;
                    }
                    case 41: {
                        break;
                    }
                    case 40: {
                        break;
                    }
                    case 39: {
                        break;
                    }
                    case 38: {
                        this.matchConstraintPercentHeight = Math.max(0.0f, var1_1.getFloat(var6_13, this.matchConstraintPercentHeight));
                        break;
                    }
                    case 37: {
                        try {
                            this.matchConstraintMaxHeight = var1_1.getDimensionPixelSize(var6_13, this.matchConstraintMaxHeight);
                        }
                        catch (Exception var2_5) {
                            if (var1_1.getInt(var6_13, this.matchConstraintMaxHeight) != -2) break;
                            this.matchConstraintMaxHeight = -2;
                        }
                        break;
                    }
                    case 36: {
                        try {
                            this.matchConstraintMinHeight = var1_1.getDimensionPixelSize(var6_13, this.matchConstraintMinHeight);
                        }
                        catch (Exception var2_6) {
                            if (var1_1.getInt(var6_13, this.matchConstraintMinHeight) != -2) break;
                            this.matchConstraintMinHeight = -2;
                        }
                        break;
                    }
                    case 35: {
                        this.matchConstraintPercentWidth = Math.max(0.0f, var1_1.getFloat(var6_13, this.matchConstraintPercentWidth));
                        break;
                    }
                    case 34: {
                        try {
                            this.matchConstraintMaxWidth = var1_1.getDimensionPixelSize(var6_13, this.matchConstraintMaxWidth);
                        }
                        catch (Exception var2_7) {
                            if (var1_1.getInt(var6_13, this.matchConstraintMaxWidth) != -2) break;
                            this.matchConstraintMaxWidth = -2;
                        }
                        break;
                    }
                    case 33: {
                        try {
                            this.matchConstraintMinWidth = var1_1.getDimensionPixelSize(var6_13, this.matchConstraintMinWidth);
                        }
                        catch (Exception var2_8) {
                            if (var1_1.getInt(var6_13, this.matchConstraintMinWidth) != -2) break;
                            this.matchConstraintMinWidth = -2;
                        }
                        break;
                    }
                    case 32: {
                        this.matchConstraintDefaultHeight = var1_1.getInt(var6_13, 0);
                        if (this.matchConstraintDefaultHeight != 1) break;
                        Log.e((String)"ConstraintLayout", (String)"layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead.");
                        break;
                    }
                    case 31: {
                        this.matchConstraintDefaultWidth = var1_1.getInt(var6_13, 0);
                        if (this.matchConstraintDefaultWidth != 1) break;
                        Log.e((String)"ConstraintLayout", (String)"layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead.");
                        break;
                    }
                    case 30: {
                        this.verticalBias = var1_1.getFloat(var6_13, this.verticalBias);
                        break;
                    }
                    case 29: {
                        this.horizontalBias = var1_1.getFloat(var6_13, this.horizontalBias);
                        break;
                    }
                    case 28: {
                        this.constrainedHeight = var1_1.getBoolean(var6_13, this.constrainedHeight);
                        break;
                    }
                    case 27: {
                        this.constrainedWidth = var1_1.getBoolean(var6_13, this.constrainedWidth);
                        break;
                    }
                    case 26: {
                        this.goneEndMargin = var1_1.getDimensionPixelSize(var6_13, this.goneEndMargin);
                        break;
                    }
                    case 25: {
                        this.goneStartMargin = var1_1.getDimensionPixelSize(var6_13, this.goneStartMargin);
                        break;
                    }
                    case 24: {
                        this.goneBottomMargin = var1_1.getDimensionPixelSize(var6_13, this.goneBottomMargin);
                        break;
                    }
                    case 23: {
                        this.goneRightMargin = var1_1.getDimensionPixelSize(var6_13, this.goneRightMargin);
                        break;
                    }
                    case 22: {
                        this.goneTopMargin = var1_1.getDimensionPixelSize(var6_13, this.goneTopMargin);
                        break;
                    }
                    case 21: {
                        this.goneLeftMargin = var1_1.getDimensionPixelSize(var6_13, this.goneLeftMargin);
                        break;
                    }
                    case 20: {
                        this.endToEnd = var1_1.getResourceId(var6_13, this.endToEnd);
                        if (this.endToEnd != -1) break;
                        this.endToEnd = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 19: {
                        this.endToStart = var1_1.getResourceId(var6_13, this.endToStart);
                        if (this.endToStart != -1) break;
                        this.endToStart = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 18: {
                        this.startToStart = var1_1.getResourceId(var6_13, this.startToStart);
                        if (this.startToStart != -1) break;
                        this.startToStart = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 17: {
                        this.startToEnd = var1_1.getResourceId(var6_13, this.startToEnd);
                        if (this.startToEnd != -1) break;
                        this.startToEnd = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 16: {
                        this.baselineToBaseline = var1_1.getResourceId(var6_13, this.baselineToBaseline);
                        if (this.baselineToBaseline != -1) break;
                        this.baselineToBaseline = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 15: {
                        this.bottomToBottom = var1_1.getResourceId(var6_13, this.bottomToBottom);
                        if (this.bottomToBottom != -1) break;
                        this.bottomToBottom = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 14: {
                        this.bottomToTop = var1_1.getResourceId(var6_13, this.bottomToTop);
                        if (this.bottomToTop != -1) break;
                        this.bottomToTop = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 13: {
                        this.topToBottom = var1_1.getResourceId(var6_13, this.topToBottom);
                        if (this.topToBottom != -1) break;
                        this.topToBottom = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 12: {
                        this.topToTop = var1_1.getResourceId(var6_13, this.topToTop);
                        if (this.topToTop != -1) break;
                        this.topToTop = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 11: {
                        this.rightToRight = var1_1.getResourceId(var6_13, this.rightToRight);
                        if (this.rightToRight != -1) break;
                        this.rightToRight = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 10: {
                        this.rightToLeft = var1_1.getResourceId(var6_13, this.rightToLeft);
                        if (this.rightToLeft != -1) break;
                        this.rightToLeft = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 9: {
                        this.leftToRight = var1_1.getResourceId(var6_13, this.leftToRight);
                        if (this.leftToRight != -1) break;
                        this.leftToRight = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 8: {
                        this.leftToLeft = var1_1.getResourceId(var6_13, this.leftToLeft);
                        if (this.leftToLeft != -1) break;
                        this.leftToLeft = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 7: {
                        this.guidePercent = var1_1.getFloat(var6_13, this.guidePercent);
                        break;
                    }
                    case 6: {
                        this.guideEnd = var1_1.getDimensionPixelOffset(var6_13, this.guideEnd);
                        break;
                    }
                    case 5: {
                        this.guideBegin = var1_1.getDimensionPixelOffset(var6_13, this.guideBegin);
                        break;
                    }
                    case 4: {
                        var3_11 = this.circleAngle = var1_1.getFloat(var6_13, this.circleAngle) % 360.0f;
                        if (var3_11 >= 0.0f) break;
                        this.circleAngle = (360.0f - var3_11) % 360.0f;
                        break;
                    }
                    case 3: {
                        this.circleRadius = var1_1.getDimensionPixelSize(var6_13, this.circleRadius);
                        break;
                    }
                    case 2: {
                        this.circleConstraint = var1_1.getResourceId(var6_13, this.circleConstraint);
                        if (this.circleConstraint != -1) break;
                        this.circleConstraint = var1_1.getInt(var6_13, -1);
                        break;
                    }
                    case 1: {
                        this.orientation = var1_1.getInt(var6_13, this.orientation);
                        ** break;
                    }
lbl311: // 2 sources:
                    case 0: 
                }
                ++var5_10;
            } while (true);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.guideBegin = layoutParams.guideBegin;
            this.guideEnd = layoutParams.guideEnd;
            this.guidePercent = layoutParams.guidePercent;
            this.leftToLeft = layoutParams.leftToLeft;
            this.leftToRight = layoutParams.leftToRight;
            this.rightToLeft = layoutParams.rightToLeft;
            this.rightToRight = layoutParams.rightToRight;
            this.topToTop = layoutParams.topToTop;
            this.topToBottom = layoutParams.topToBottom;
            this.bottomToTop = layoutParams.bottomToTop;
            this.bottomToBottom = layoutParams.bottomToBottom;
            this.baselineToBaseline = layoutParams.baselineToBaseline;
            this.circleConstraint = layoutParams.circleConstraint;
            this.circleRadius = layoutParams.circleRadius;
            this.circleAngle = layoutParams.circleAngle;
            this.startToEnd = layoutParams.startToEnd;
            this.startToStart = layoutParams.startToStart;
            this.endToStart = layoutParams.endToStart;
            this.endToEnd = layoutParams.endToEnd;
            this.goneLeftMargin = layoutParams.goneLeftMargin;
            this.goneTopMargin = layoutParams.goneTopMargin;
            this.goneRightMargin = layoutParams.goneRightMargin;
            this.goneBottomMargin = layoutParams.goneBottomMargin;
            this.goneStartMargin = layoutParams.goneStartMargin;
            this.goneEndMargin = layoutParams.goneEndMargin;
            this.horizontalBias = layoutParams.horizontalBias;
            this.verticalBias = layoutParams.verticalBias;
            this.dimensionRatio = layoutParams.dimensionRatio;
            this.dimensionRatioValue = layoutParams.dimensionRatioValue;
            this.dimensionRatioSide = layoutParams.dimensionRatioSide;
            this.horizontalWeight = layoutParams.horizontalWeight;
            this.verticalWeight = layoutParams.verticalWeight;
            this.horizontalChainStyle = layoutParams.horizontalChainStyle;
            this.verticalChainStyle = layoutParams.verticalChainStyle;
            this.constrainedWidth = layoutParams.constrainedWidth;
            this.constrainedHeight = layoutParams.constrainedHeight;
            this.matchConstraintDefaultWidth = layoutParams.matchConstraintDefaultWidth;
            this.matchConstraintDefaultHeight = layoutParams.matchConstraintDefaultHeight;
            this.matchConstraintMinWidth = layoutParams.matchConstraintMinWidth;
            this.matchConstraintMaxWidth = layoutParams.matchConstraintMaxWidth;
            this.matchConstraintMinHeight = layoutParams.matchConstraintMinHeight;
            this.matchConstraintMaxHeight = layoutParams.matchConstraintMaxHeight;
            this.matchConstraintPercentWidth = layoutParams.matchConstraintPercentWidth;
            this.matchConstraintPercentHeight = layoutParams.matchConstraintPercentHeight;
            this.editorAbsoluteX = layoutParams.editorAbsoluteX;
            this.editorAbsoluteY = layoutParams.editorAbsoluteY;
            this.orientation = layoutParams.orientation;
            this.horizontalDimensionFixed = layoutParams.horizontalDimensionFixed;
            this.verticalDimensionFixed = layoutParams.verticalDimensionFixed;
            this.needsBaseline = layoutParams.needsBaseline;
            this.isGuideline = layoutParams.isGuideline;
            this.resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
            this.resolvedLeftToRight = layoutParams.resolvedLeftToRight;
            this.resolvedRightToLeft = layoutParams.resolvedRightToLeft;
            this.resolvedRightToRight = layoutParams.resolvedRightToRight;
            this.resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
            this.resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
            this.resolvedHorizontalBias = layoutParams.resolvedHorizontalBias;
            this.widget = layoutParams.widget;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public void reset() {
            ConstraintWidget constraintWidget = this.widget;
            if (constraintWidget != null) {
                constraintWidget.reset();
            }
        }

        @TargetApi(value=17)
        public void resolveLayoutDirection(int n) {
            int n2 = this.leftMargin;
            int n3 = this.rightMargin;
            super.resolveLayoutDirection(n);
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = this.horizontalBias;
            this.resolvedGuideBegin = this.guideBegin;
            this.resolvedGuideEnd = this.guideEnd;
            this.resolvedGuidePercent = this.guidePercent;
            n = 1 == this.getLayoutDirection() ? 1 : 0;
            if (n != 0) {
                n = 0;
                int n4 = this.startToEnd;
                if (n4 != -1) {
                    this.resolvedRightToLeft = n4;
                    n = 1;
                } else {
                    n4 = this.startToStart;
                    if (n4 != -1) {
                        this.resolvedRightToRight = n4;
                        n = 1;
                    }
                }
                n4 = this.endToStart;
                if (n4 != -1) {
                    this.resolvedLeftToRight = n4;
                    n = 1;
                }
                if ((n4 = this.endToEnd) != -1) {
                    this.resolvedLeftToLeft = n4;
                    n = 1;
                }
                if ((n4 = this.goneStartMargin) != -1) {
                    this.resolveGoneRightMargin = n4;
                }
                if ((n4 = this.goneEndMargin) != -1) {
                    this.resolveGoneLeftMargin = n4;
                }
                if (n != 0) {
                    this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
                }
                if (this.isGuideline && this.orientation == 1) {
                    float f = this.guidePercent;
                    if (f != -1.0f) {
                        this.resolvedGuidePercent = 1.0f - f;
                        this.resolvedGuideBegin = -1;
                        this.resolvedGuideEnd = -1;
                    } else {
                        n = this.guideBegin;
                        if (n != -1) {
                            this.resolvedGuideEnd = n;
                            this.resolvedGuideBegin = -1;
                            this.resolvedGuidePercent = -1.0f;
                        } else {
                            n = this.guideEnd;
                            if (n != -1) {
                                this.resolvedGuideBegin = n;
                                this.resolvedGuideEnd = -1;
                                this.resolvedGuidePercent = -1.0f;
                            }
                        }
                    }
                }
            } else {
                n = this.startToEnd;
                if (n != -1) {
                    this.resolvedLeftToRight = n;
                }
                if ((n = this.startToStart) != -1) {
                    this.resolvedLeftToLeft = n;
                }
                if ((n = this.endToStart) != -1) {
                    this.resolvedRightToLeft = n;
                }
                if ((n = this.endToEnd) != -1) {
                    this.resolvedRightToRight = n;
                }
                if ((n = this.goneStartMargin) != -1) {
                    this.resolveGoneLeftMargin = n;
                }
                if ((n = this.goneEndMargin) != -1) {
                    this.resolveGoneRightMargin = n;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
                n = this.rightToLeft;
                if (n != -1) {
                    this.resolvedRightToLeft = n;
                    if (this.rightMargin <= 0 && n3 > 0) {
                        this.rightMargin = n3;
                    }
                } else {
                    n = this.rightToRight;
                    if (n != -1) {
                        this.resolvedRightToRight = n;
                        if (this.rightMargin <= 0 && n3 > 0) {
                            this.rightMargin = n3;
                        }
                    }
                }
                if ((n = this.leftToLeft) != -1) {
                    this.resolvedLeftToLeft = n;
                    if (this.leftMargin <= 0 && n2 > 0) {
                        this.leftMargin = n2;
                        return;
                    }
                } else {
                    n = this.leftToRight;
                    if (n != -1) {
                        this.resolvedLeftToRight = n;
                        if (this.leftMargin <= 0 && n2 > 0) {
                            this.leftMargin = n2;
                        }
                    }
                }
            }
        }

        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = true;
            this.verticalDimensionFixed = true;
            if (this.width == -2 && this.constrainedWidth) {
                this.horizontalDimensionFixed = false;
                this.matchConstraintDefaultWidth = 1;
            }
            if (this.height == -2 && this.constrainedHeight) {
                this.verticalDimensionFixed = false;
                this.matchConstraintDefaultHeight = 1;
            }
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
                if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
                    this.width = -2;
                    this.constrainedWidth = true;
                }
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
                if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
                    this.height = -2;
                    this.constrainedHeight = true;
                }
            }
            if (this.guidePercent != -1.0f || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = true;
                this.horizontalDimensionFixed = true;
                this.verticalDimensionFixed = true;
                if (!(this.widget instanceof android.support.constraint.solver.widgets.Guideline)) {
                    this.widget = new android.support.constraint.solver.widgets.Guideline();
                }
                ((android.support.constraint.solver.widgets.Guideline)this.widget).setOrientation(this.orientation);
            }
        }

        private static class Table {
            public static final int ANDROID_ORIENTATION = 1;
            public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
            public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
            public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
            public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
            public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
            public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
            public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
            public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
            public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
            public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
            public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
            public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
            public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
            public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
            public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
            public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
            public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
            public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
            public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
            public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
            public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
            public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
            public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
            public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
            public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
            public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
            public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
            public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
            public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
            public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
            public static final int LAYOUT_GONE_MARGIN_END = 26;
            public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
            public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
            public static final int LAYOUT_GONE_MARGIN_START = 25;
            public static final int LAYOUT_GONE_MARGIN_TOP = 22;
            public static final int UNUSED = 0;
            public static final SparseIntArray map = new SparseIntArray();

            static {
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
                map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
                map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
                map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
                map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
                map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
            }

            private Table() {
            }
        }

    }

}

