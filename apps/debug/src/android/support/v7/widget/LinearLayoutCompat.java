/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat
extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned = true;
    private int mBaselineAlignedChildIndex = -1;
    private int mBaselineChildTop = 0;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity = 8388659;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context object, AttributeSet attributeSet, int n) {
        boolean bl;
        super((Context)object, attributeSet, n);
        object = TintTypedArray.obtainStyledAttributes((Context)object, attributeSet, R.styleable.LinearLayoutCompat, n, 0);
        n = object.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (n >= 0) {
            this.setOrientation(n);
        }
        if ((n = object.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1)) >= 0) {
            this.setGravity(n);
        }
        if (!(bl = object.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true))) {
            this.setBaselineAligned(bl);
        }
        this.mWeightSum = object.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = object.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = object.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        this.setDividerDrawable(object.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = object.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = object.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        object.recycle();
    }

    private void forceUniformHeight(int n, int n2) {
        int n3 = View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredHeight(), (int)1073741824);
        for (int i = 0; i < n; ++i) {
            View view = this.getVirtualChildAt(i);
            if (view.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.height != -1) continue;
            int n4 = layoutParams.width;
            layoutParams.width = view.getMeasuredWidth();
            this.measureChildWithMargins(view, n2, 0, n3, 0);
            layoutParams.width = n4;
        }
    }

    private void forceUniformWidth(int n, int n2) {
        int n3 = View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)1073741824);
        for (int i = 0; i < n; ++i) {
            View view = this.getVirtualChildAt(i);
            if (view.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.width != -1) continue;
            int n4 = layoutParams.height;
            layoutParams.height = view.getMeasuredHeight();
            this.measureChildWithMargins(view, n3, 0, n2, 0);
            layoutParams.height = n4;
        }
    }

    private void setChildFrame(View view, int n, int n2, int n3, int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    void drawDividersHorizontal(Canvas canvas) {
        View view;
        int n;
        LayoutParams layoutParams;
        int n2 = this.getVirtualChildCount();
        boolean bl = ViewUtils.isLayoutRtl((View)this);
        for (n = 0; n < n2; ++n) {
            view = this.getVirtualChildAt(n);
            if (view == null || view.getVisibility() == 8 || !this.hasDividerBeforeChildAt(n)) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            int n3 = bl ? view.getRight() + layoutParams.rightMargin : view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
            this.drawVerticalDivider(canvas, n3);
        }
        if (this.hasDividerBeforeChildAt(n2)) {
            view = this.getVirtualChildAt(n2 - 1);
            if (view == null) {
                n = bl ? this.getPaddingLeft() : this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
            } else {
                layoutParams = (LayoutParams)view.getLayoutParams();
                n = bl ? view.getLeft() - layoutParams.leftMargin - this.mDividerWidth : view.getRight() + layoutParams.rightMargin;
            }
            this.drawVerticalDivider(canvas, n);
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int n;
        View view;
        LayoutParams layoutParams;
        int n2 = this.getVirtualChildCount();
        for (n = 0; n < n2; ++n) {
            view = this.getVirtualChildAt(n);
            if (view == null || view.getVisibility() == 8 || !this.hasDividerBeforeChildAt(n)) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            this.drawHorizontalDivider(canvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
        }
        if (this.hasDividerBeforeChildAt(n2)) {
            view = this.getVirtualChildAt(n2 - 1);
            if (view == null) {
                n = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
            } else {
                layoutParams = (LayoutParams)view.getLayoutParams();
                n = view.getBottom() + layoutParams.bottomMargin;
            }
            this.drawHorizontalDivider(canvas, n);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int n) {
        this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, n, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + n);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int n) {
        this.mDivider.setBounds(n, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + n, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        int n = this.mOrientation;
        if (n == 0) {
            return new LayoutParams(-2, -2);
        }
        if (n == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getBaseline() {
        int n;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int n2 = this.getChildCount();
        if (n2 > (n = this.mBaselineAlignedChildIndex)) {
            View view = this.getChildAt(n);
            int n3 = view.getBaseline();
            if (n3 == -1) {
                if (this.mBaselineAlignedChildIndex == 0) {
                    return -1;
                }
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
            n2 = n = this.mBaselineChildTop;
            if (this.mOrientation == 1) {
                int n4 = this.mGravity & 112;
                n2 = n;
                if (n4 != 48) {
                    n2 = n4 != 16 ? (n4 != 80 ? n : this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength) : n + (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                }
            }
            return ((LayoutParams)view.getLayoutParams()).topMargin + n2 + n3;
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    int getChildrenSkipCount(View view, int n) {
        return 0;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getGravity() {
        return this.mGravity;
    }

    int getLocationOffset(View view) {
        return 0;
    }

    int getNextLocationOffset(View view) {
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    View getVirtualChildAt(int n) {
        return this.getChildAt(n);
    }

    int getVirtualChildCount() {
        return this.getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY})
    protected boolean hasDividerBeforeChildAt(int n) {
        boolean bl = false;
        boolean bl2 = false;
        if (n == 0) {
            if ((this.mShowDividers & 1) != 0) {
                bl2 = true;
            }
            return bl2;
        }
        if (n == this.getChildCount()) {
            bl2 = bl;
            if ((this.mShowDividers & 4) != 0) {
                bl2 = true;
            }
            return bl2;
        }
        if ((this.mShowDividers & 2) != 0) {
            --n;
            while (n >= 0) {
                if (this.getChildAt(n).getVisibility() != 8) {
                    return true;
                }
                --n;
            }
            return false;
        }
        return false;
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    void layoutHorizontal(int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        boolean bl = ViewUtils.isLayoutRtl((View)this);
        int n7 = this.getPaddingTop();
        int n8 = n4 - n2;
        int n9 = this.getPaddingBottom();
        int n10 = this.getPaddingBottom();
        int n11 = this.getVirtualChildCount();
        int n12 = this.mGravity;
        boolean bl2 = this.mBaselineAligned;
        int[] arrn = this.mMaxAscent;
        int[] arrn2 = this.mMaxDescent;
        int n13 = ViewCompat.getLayoutDirection((View)this);
        n2 = GravityCompat.getAbsoluteGravity(n12 & 8388615, n13);
        n = n2 != 1 ? (n2 != 5 ? this.getPaddingLeft() : this.getPaddingLeft() + n3 - n - this.mTotalLength) : this.getPaddingLeft() + (n3 - n - this.mTotalLength) / 2;
        if (bl) {
            n6 = n11 - 1;
            n5 = -1;
        } else {
            n6 = 0;
            n5 = 1;
        }
        int n14 = n8;
        n3 = n7;
        n4 = n;
        for (n2 = 0; n2 < n11; ++n2) {
            int n15 = n6 + n5 * n2;
            View view = this.getVirtualChildAt(n15);
            if (view == null) {
                n4 += this.measureNullChild(n15);
                continue;
            }
            if (view.getVisibility() == 8) continue;
            int n16 = view.getMeasuredWidth();
            int n17 = view.getMeasuredHeight();
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            n = bl2 && layoutParams.height != -1 ? view.getBaseline() : -1;
            int n18 = layoutParams.gravity;
            if (n18 < 0) {
                n18 = n12 & 112;
            }
            if ((n18 &= 112) != 16) {
                if (n18 != 48) {
                    if (n18 != 80) {
                        n = n3;
                    } else {
                        n18 = n8 - n9 - n17 - layoutParams.bottomMargin;
                        if (n != -1) {
                            int n19 = view.getMeasuredHeight();
                            n = n18 - (arrn2[2] - (n19 - n));
                        } else {
                            n = n18;
                        }
                    }
                } else {
                    n18 = layoutParams.topMargin + n3;
                    n = n != -1 ? n18 + (arrn[1] - n) : n18;
                }
            } else {
                n = (n8 - n7 - n10 - n17) / 2 + n3 + layoutParams.topMargin - layoutParams.bottomMargin;
            }
            n18 = n4;
            if (this.hasDividerBeforeChildAt(n15)) {
                n18 = n4 + this.mDividerWidth;
            }
            n4 = n18 + layoutParams.leftMargin;
            this.setChildFrame(view, n4 + this.getLocationOffset(view), n, n16, n17);
            n = layoutParams.rightMargin;
            n18 = this.getNextLocationOffset(view);
            n2 += this.getChildrenSkipCount(view, n15);
            n4 += n16 + n + n18;
        }
    }

    void layoutVertical(int n, int n2, int n3, int n4) {
        int n5 = this.getPaddingLeft();
        int n6 = n3 - n;
        int n7 = this.getPaddingRight();
        int n8 = this.getPaddingRight();
        int n9 = this.getVirtualChildCount();
        int n10 = this.mGravity;
        n = n10 & 112;
        n = n != 16 ? (n != 80 ? this.getPaddingTop() : this.getPaddingTop() + n4 - n2 - this.mTotalLength) : this.getPaddingTop() + (n4 - n2 - this.mTotalLength) / 2;
        n2 = 0;
        n3 = n5;
        do {
            n4 = n3;
            if (n2 >= n9) break;
            View view = this.getVirtualChildAt(n2);
            if (view == null) {
                n += this.measureNullChild(n2);
            } else if (view.getVisibility() != 8) {
                int n11 = view.getMeasuredWidth();
                int n12 = view.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                n3 = layoutParams.gravity;
                if (n3 < 0) {
                    n3 = n10 & 8388615;
                }
                n3 = (n3 = GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection((View)this)) & 7) != 1 ? (n3 != 5 ? layoutParams.leftMargin + n4 : n6 - n7 - n11 - layoutParams.rightMargin) : (n6 - n5 - n8 - n11) / 2 + n4 + layoutParams.leftMargin - layoutParams.rightMargin;
                int n13 = n;
                if (this.hasDividerBeforeChildAt(n2)) {
                    n13 = n + this.mDividerHeight;
                }
                n = n13 + layoutParams.topMargin;
                this.setChildFrame(view, n3, n + this.getLocationOffset(view), n11, n12);
                n3 = layoutParams.bottomMargin;
                n13 = this.getNextLocationOffset(view);
                n2 += this.getChildrenSkipCount(view, n2);
                n += n12 + n3 + n13;
            }
            ++n2;
            n3 = n4;
        } while (true);
    }

    void measureChildBeforeLayout(View view, int n, int n2, int n3, int n4, int n5) {
        this.measureChildWithMargins(view, n2, n3, n4, n5);
    }

    void measureHorizontal(int n, int n2) {
        int n3;
        int n4;
        LayoutParams layoutParams;
        int n5;
        LayoutParams layoutParams2;
        int n6;
        int n7;
        this.mTotalLength = 0;
        int n8 = this.getVirtualChildCount();
        int n9 = View.MeasureSpec.getMode((int)n);
        int n10 = View.MeasureSpec.getMode((int)n2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] arrn = this.mMaxAscent;
        int[] arrn2 = this.mMaxDescent;
        arrn[3] = -1;
        arrn[2] = -1;
        arrn[1] = -1;
        arrn[0] = -1;
        arrn2[3] = -1;
        arrn2[2] = -1;
        arrn2[1] = -1;
        arrn2[0] = -1;
        boolean bl = this.mBaselineAligned;
        int n11 = 0;
        boolean bl2 = this.mUseLargestChild;
        boolean bl3 = n9 == 1073741824;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = 1;
        int n16 = 0;
        float f = 0.0f;
        int n17 = 0;
        int n18 = 0;
        for (n7 = 0; n7 < n8; ++n7) {
            View view = this.getVirtualChildAt(n7);
            if (view == null) {
                this.mTotalLength += this.measureNullChild(n7);
                n5 = n12;
                n12 = n17;
                n17 = n5;
            } else if (view.getVisibility() == 8) {
                n7 += this.getChildrenSkipCount(view, n7);
                n5 = n12;
                n12 = n17;
                n17 = n5;
            } else {
                int n19;
                if (this.hasDividerBeforeChildAt(n7)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                layoutParams = (LayoutParams)view.getLayoutParams();
                f += layoutParams.weight;
                if (n9 == 1073741824 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    if (bl3) {
                        this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                    } else {
                        n5 = this.mTotalLength;
                        this.mTotalLength = Math.max(n5, layoutParams.leftMargin + n5 + layoutParams.rightMargin);
                    }
                    if (bl) {
                        n5 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                        view.measure(n5, n5);
                    } else {
                        n11 = 1;
                    }
                } else {
                    if (layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                        layoutParams.width = -2;
                        n5 = 0;
                    } else {
                        n5 = Integer.MIN_VALUE;
                    }
                    n6 = f == 0.0f ? this.mTotalLength : 0;
                    this.measureChildBeforeLayout(view, n7, n, n6, n2, 0);
                    if (n5 != Integer.MIN_VALUE) {
                        layoutParams.width = n5;
                    }
                    layoutParams2 = layoutParams;
                    n5 = view.getMeasuredWidth();
                    if (bl3) {
                        this.mTotalLength += layoutParams2.leftMargin + n5 + layoutParams2.rightMargin + this.getNextLocationOffset(view);
                    } else {
                        n6 = this.mTotalLength;
                        this.mTotalLength = Math.max(n6, n6 + n5 + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset(view));
                    }
                    if (bl2) {
                        n13 = Math.max(n5, n13);
                    }
                }
                n5 = n18;
                n4 = n7;
                n6 = n7 = 0;
                n18 = n14;
                if (n10 != 1073741824) {
                    n6 = n7;
                    n18 = n14;
                    if (layoutParams.height == -1) {
                        n18 = 1;
                        n6 = 1;
                    }
                }
                n7 = layoutParams.topMargin + layoutParams.bottomMargin;
                n14 = view.getMeasuredHeight() + n7;
                n3 = View.combineMeasuredStates((int)n12, (int)view.getMeasuredState());
                if (bl && (n19 = view.getBaseline()) != -1) {
                    n12 = layoutParams.gravity < 0 ? this.mGravity : layoutParams.gravity;
                    n12 = ((n12 & 112) >> 4 & -2) >> 1;
                    arrn[n12] = Math.max(arrn[n12], n19);
                    arrn2[n12] = Math.max(arrn2[n12], n14 - n19);
                }
                n16 = Math.max(n16, n14);
                n12 = n15 != 0 && layoutParams.height == -1 ? 1 : 0;
                if (layoutParams.weight > 0.0f) {
                    if (n6 == 0) {
                        n7 = n14;
                    }
                    n15 = Math.max(n5, n7);
                } else {
                    if (n6 == 0) {
                        n7 = n14;
                    }
                    n17 = Math.max(n17, n7);
                    n15 = n5;
                }
                n14 = this.getChildrenSkipCount(view, n4);
                n7 = n12;
                n5 = n15;
                n12 = n17;
                n17 = n3;
                n6 = n4 + n14;
                n14 = n18;
                n15 = n7;
                n7 = n6;
                n18 = n5;
            }
            n5 = n17;
            n17 = n12;
            n12 = n5;
        }
        n5 = n18;
        n18 = n16;
        n16 = n13;
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(n8)) {
            this.mTotalLength += this.mDividerWidth;
        }
        n13 = arrn[1] == -1 && arrn[0] == -1 && arrn[2] == -1 && arrn[3] == -1 ? n18 : Math.max(n18, Math.max(arrn[3], Math.max(arrn[0], Math.max(arrn[1], arrn[2]))) + Math.max(arrn2[3], Math.max(arrn2[0], Math.max(arrn2[1], arrn2[2]))));
        n18 = n12;
        if (bl2 && ((n12 = n9) == Integer.MIN_VALUE || n12 == 0)) {
            this.mTotalLength = 0;
            n12 = 0;
            n7 = n13;
            while (n12 < n8) {
                layoutParams = this.getVirtualChildAt(n12);
                if (layoutParams == null) {
                    this.mTotalLength += this.measureNullChild(n12);
                    n13 = n12;
                } else if (layoutParams.getVisibility() == 8) {
                    n13 = n12 + this.getChildrenSkipCount((View)layoutParams, n12);
                } else {
                    layoutParams2 = (LayoutParams)layoutParams.getLayoutParams();
                    if (bl3) {
                        n6 = this.mTotalLength;
                        n4 = layoutParams2.leftMargin;
                        n13 = n12;
                        this.mTotalLength = n6 + (n4 + n16 + layoutParams2.rightMargin + this.getNextLocationOffset((View)layoutParams));
                    } else {
                        n13 = n12;
                        n12 = this.mTotalLength;
                        this.mTotalLength = Math.max(n12, n12 + n16 + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset((View)layoutParams));
                    }
                }
                n12 = n13 + 1;
            }
            n13 = n7;
        }
        n7 = n9;
        this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
        n9 = View.resolveSizeAndState((int)Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), (int)n, (int)0);
        n12 = n9 & 16777215;
        n4 = n12 - this.mTotalLength;
        if (n11 == 0 && (n4 == 0 || f <= 0.0f)) {
            n17 = Math.max(n17, n5);
            if (bl2 && n7 != 1073741824) {
                for (n7 = 0; n7 < n8; ++n7) {
                    layoutParams = this.getVirtualChildAt(n7);
                    if (layoutParams == null || layoutParams.getVisibility() == 8 || ((LayoutParams)layoutParams.getLayoutParams()).weight <= 0.0f) continue;
                    layoutParams.measure(View.MeasureSpec.makeMeasureSpec((int)n16, (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)layoutParams.getMeasuredHeight(), (int)1073741824));
                }
            }
            n7 = n8;
            n12 = n17;
        } else {
            block54 : {
                float f2 = this.mWeightSum;
                if (f2 > 0.0f) {
                    f = f2;
                }
                arrn[3] = -1;
                arrn[2] = -1;
                arrn[1] = -1;
                arrn[0] = -1;
                arrn2[3] = -1;
                arrn2[2] = -1;
                arrn2[1] = -1;
                arrn2[0] = -1;
                n3 = -1;
                this.mTotalLength = 0;
                n13 = 0;
                n6 = n17;
                n17 = n18;
                n11 = n5;
                n12 = n8;
                n8 = n13;
                n18 = n9;
                n13 = n4;
                n16 = n7;
                n7 = n3;
                n9 = n6;
                while (n8 < n12) {
                    layoutParams = this.getVirtualChildAt(n8);
                    if (layoutParams != null && layoutParams.getVisibility() != 8) {
                        layoutParams2 = (LayoutParams)layoutParams.getLayoutParams();
                        f2 = layoutParams2.weight;
                        if (f2 > 0.0f) {
                            n6 = (int)((float)n13 * f2 / f);
                            n3 = LinearLayoutCompat.getChildMeasureSpec((int)n2, (int)(this.getPaddingTop() + this.getPaddingBottom() + layoutParams2.topMargin + layoutParams2.bottomMargin), (int)layoutParams2.height);
                            if (layoutParams2.width == 0 && n16 == 1073741824) {
                                n5 = n6 > 0 ? n6 : 0;
                                layoutParams.measure(View.MeasureSpec.makeMeasureSpec((int)n5, (int)1073741824), n3);
                            } else {
                                n5 = n4 = layoutParams.getMeasuredWidth() + n6;
                                if (n4 < 0) {
                                    n5 = 0;
                                }
                                layoutParams.measure(View.MeasureSpec.makeMeasureSpec((int)n5, (int)1073741824), n3);
                            }
                            n17 = View.combineMeasuredStates((int)n17, (int)(layoutParams.getMeasuredState() & -16777216));
                            f -= f2;
                            n13 -= n6;
                        }
                        if (bl3) {
                            this.mTotalLength += layoutParams.getMeasuredWidth() + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset((View)layoutParams);
                        } else {
                            n5 = this.mTotalLength;
                            this.mTotalLength = Math.max(n5, layoutParams.getMeasuredWidth() + n5 + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset((View)layoutParams));
                        }
                        n5 = n10 != 1073741824 && layoutParams2.height == -1 ? 1 : 0;
                        n3 = layoutParams2.topMargin + layoutParams2.bottomMargin;
                        n4 = layoutParams.getMeasuredHeight() + n3;
                        n6 = Math.max(n7, n4);
                        n7 = n5 != 0 ? n3 : n4;
                        n7 = Math.max(n9, n7);
                        n15 = n15 != 0 && layoutParams2.height == -1 ? 1 : 0;
                        if (bl && (n5 = layoutParams.getBaseline()) != -1) {
                            n9 = layoutParams2.gravity < 0 ? this.mGravity : layoutParams2.gravity;
                            n9 = ((n9 & 112) >> 4 & -2) >> 1;
                            arrn[n9] = Math.max(arrn[n9], n5);
                            arrn2[n9] = Math.max(arrn2[n9], n4 - n5);
                        }
                        n9 = n7;
                        n7 = n6;
                    }
                    ++n8;
                }
                this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
                if (arrn[1] == -1 && arrn[0] == -1 && arrn[2] == -1) {
                    n13 = n7;
                    if (arrn[3] == -1) break block54;
                }
                n13 = Math.max(n7, Math.max(arrn[3], Math.max(arrn[0], Math.max(arrn[1], arrn[2]))) + Math.max(arrn2[3], Math.max(arrn2[0], Math.max(arrn2[1], arrn2[2]))));
            }
            n11 = n9;
            n7 = n12;
            n9 = n18;
            n12 = n11;
            n18 = n17;
        }
        n17 = n13;
        if (n15 == 0) {
            n17 = n13;
            if (n10 != 1073741824) {
                n17 = n12;
            }
        }
        this.setMeasuredDimension(n9 | -16777216 & n18, View.resolveSizeAndState((int)Math.max(n17 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight()), (int)n2, (int)(n18 << 16)));
        if (n14 != 0) {
            this.forceUniformHeight(n7, n);
            return;
        }
    }

    int measureNullChild(int n) {
        return 0;
    }

    void measureVertical(int n, int n2) {
        int n3;
        int n4;
        Object object;
        int n5;
        int n6;
        LayoutParams layoutParams;
        int n7;
        int n8;
        this.mTotalLength = 0;
        int n9 = 0;
        float f = 0.0f;
        int n10 = this.getVirtualChildCount();
        int n11 = View.MeasureSpec.getMode((int)n);
        int n12 = View.MeasureSpec.getMode((int)n2);
        int n13 = this.mBaselineAlignedChildIndex;
        boolean bl = this.mUseLargestChild;
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        int n17 = 0;
        int n18 = 0;
        int n19 = 0;
        int n20 = 1;
        for (n4 = 0; n4 < n10; ++n4) {
            object = this.getVirtualChildAt(n4);
            if (object == null) {
                this.mTotalLength += this.measureNullChild(n4);
                continue;
            }
            if (object.getVisibility() == 8) {
                n4 += this.getChildrenSkipCount((View)object, n4);
                continue;
            }
            if (this.hasDividerBeforeChildAt(n4)) {
                this.mTotalLength += this.mDividerHeight;
            }
            layoutParams = (LayoutParams)object.getLayoutParams();
            f += layoutParams.weight;
            if (n12 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                n19 = this.mTotalLength;
                this.mTotalLength = Math.max(n19, layoutParams.topMargin + n19 + layoutParams.bottomMargin);
                n3 = 1;
            } else {
                if (layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                    layoutParams.height = -2;
                    n3 = 0;
                } else {
                    n3 = Integer.MIN_VALUE;
                }
                n8 = f == 0.0f ? this.mTotalLength : 0;
                LayoutParams layoutParams2 = layoutParams;
                n5 = n18;
                this.measureChildBeforeLayout((View)object, n4, n, 0, n2, n8);
                if (n3 != Integer.MIN_VALUE) {
                    layoutParams2.height = n3;
                }
                n8 = object.getMeasuredHeight();
                n18 = this.mTotalLength;
                this.mTotalLength = Math.max(n18, n18 + n8 + layoutParams2.topMargin + layoutParams2.bottomMargin + this.getNextLocationOffset((View)object));
                n18 = n5;
                n3 = n19;
                if (bl) {
                    n18 = Math.max(n8, n5);
                    n3 = n19;
                }
            }
            n19 = n17;
            if (n13 >= 0 && n13 == n4 + 1) {
                this.mBaselineChildTop = this.mTotalLength;
            }
            if (n4 < n13 && layoutParams.weight > 0.0f) {
                throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
            }
            n5 = n8 = 0;
            n17 = n14;
            if (n11 != 1073741824) {
                n5 = n8;
                n17 = n14;
                if (layoutParams.width == -1) {
                    n17 = 1;
                    n5 = 1;
                }
            }
            n14 = layoutParams.leftMargin + layoutParams.rightMargin;
            n8 = object.getMeasuredWidth() + n14;
            n6 = Math.max(n16, n8);
            n7 = View.combineMeasuredStates((int)n9, (int)object.getMeasuredState());
            n20 = n20 != 0 && layoutParams.width == -1 ? 1 : 0;
            if (layoutParams.weight > 0.0f) {
                if (n5 == 0) {
                    n14 = n8;
                }
                n9 = Math.max(n19, n14);
                n16 = n15;
                n15 = n9;
            } else {
                if (n5 == 0) {
                    n14 = n8;
                }
                n16 = Math.max(n15, n14);
                n15 = n19;
            }
            n4 += this.getChildrenSkipCount((View)object, n4);
            n9 = n6;
            n5 = n7;
            n8 = n15;
            n14 = n17;
            n19 = n3;
            n15 = n16;
            n17 = n8;
            n16 = n9;
            n9 = n5;
        }
        n4 = n9;
        n9 = n16;
        n3 = n17;
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(n10)) {
            this.mTotalLength += this.mDividerHeight;
        }
        n6 = n10;
        if (bl && ((n17 = n12) == Integer.MIN_VALUE || n17 == 0)) {
            this.mTotalLength = 0;
            for (n17 = 0; n17 < n6; ++n17) {
                layoutParams = this.getVirtualChildAt(n17);
                if (layoutParams == null) {
                    this.mTotalLength += this.measureNullChild(n17);
                    continue;
                }
                if (layoutParams.getVisibility() == 8) {
                    n17 += this.getChildrenSkipCount((View)layoutParams, n17);
                    continue;
                }
                object = (LayoutParams)layoutParams.getLayoutParams();
                n10 = this.mTotalLength;
                this.mTotalLength = Math.max(n10, n10 + n18 + object.topMargin + object.bottomMargin + this.getNextLocationOffset((View)layoutParams));
            }
        }
        this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
        int n21 = View.resolveSizeAndState((int)Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), (int)n2, (int)0);
        n10 = n21 & 16777215;
        n17 = n10 - this.mTotalLength;
        if (n19 == 0 && (n17 == 0 || f <= 0.0f)) {
            n15 = Math.max(n15, n3);
            if (bl) {
                if (n12 != 1073741824) {
                    n12 = n10;
                    for (n16 = 0; n16 < n6; ++n16) {
                        layoutParams = this.getVirtualChildAt(n16);
                        if (layoutParams == null || layoutParams.getVisibility() == 8 || ((LayoutParams)layoutParams.getLayoutParams()).weight <= 0.0f) continue;
                        layoutParams.measure(View.MeasureSpec.makeMeasureSpec((int)layoutParams.getMeasuredWidth(), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)n18, (int)1073741824));
                    }
                    n18 = n17;
                    n17 = n9;
                } else {
                    n18 = n17;
                    n17 = n9;
                }
            } else {
                n18 = n17;
                n17 = n9;
            }
            n18 = n15;
            n12 = n17;
        } else {
            float f2 = this.mWeightSum;
            if (f2 > 0.0f) {
                f = f2;
            }
            this.mTotalLength = 0;
            n7 = 0;
            n19 = n15;
            n5 = n17;
            n8 = n16;
            n15 = n13;
            n9 = n18;
            n10 = n3;
            n17 = n4;
            n18 = n19;
            n4 = n12;
            n16 = n5;
            n12 = n8;
            for (n19 = n7; n19 < n6; ++n19) {
                layoutParams = this.getVirtualChildAt(n19);
                if (layoutParams.getVisibility() == 8) continue;
                object = (LayoutParams)layoutParams.getLayoutParams();
                f2 = object.weight;
                if (f2 > 0.0f) {
                    n5 = (int)((float)n16 * f2 / f);
                    n8 = this.getPaddingLeft();
                    n13 = this.getPaddingRight();
                    f -= f2;
                    n7 = object.leftMargin;
                    int n22 = object.rightMargin;
                    n3 = n16 - n5;
                    n8 = LinearLayoutCompat.getChildMeasureSpec((int)n, (int)(n8 + n13 + n7 + n22), (int)object.width);
                    if (object.height == 0 && n4 == 1073741824) {
                        n16 = n5 > 0 ? n5 : 0;
                        layoutParams.measure(n8, View.MeasureSpec.makeMeasureSpec((int)n16, (int)1073741824));
                    } else {
                        n16 = n5 = layoutParams.getMeasuredHeight() + n5;
                        if (n5 < 0) {
                            n16 = 0;
                        }
                        layoutParams.measure(n8, View.MeasureSpec.makeMeasureSpec((int)n16, (int)1073741824));
                    }
                    n17 = View.combineMeasuredStates((int)n17, (int)(layoutParams.getMeasuredState() & -256));
                    n16 = n3;
                }
                n5 = object.leftMargin + object.rightMargin;
                n8 = layoutParams.getMeasuredWidth() + n5;
                n3 = Math.max(n12, n8);
                n12 = n11 != 1073741824 && object.width == -1 ? 1 : 0;
                n12 = n12 != 0 ? n5 : n8;
                n5 = Math.max(n18, n12);
                n18 = n20 != 0 && object.width == -1 ? 1 : 0;
                n20 = this.mTotalLength;
                this.mTotalLength = Math.max(n20, n20 + layoutParams.getMeasuredHeight() + object.topMargin + object.bottomMargin + this.getNextLocationOffset((View)layoutParams));
                n12 = n3;
                n20 = n18;
                n18 = n5;
            }
            this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
            n4 = n17;
        }
        n17 = n12;
        if (n20 == 0) {
            n17 = n12;
            if (n11 != 1073741824) {
                n17 = n18;
            }
        }
        this.setMeasuredDimension(View.resolveSizeAndState((int)Math.max(n17 + (this.getPaddingLeft() + this.getPaddingRight()), this.getSuggestedMinimumWidth()), (int)n, (int)n4), n21);
        if (n14 != 0) {
            this.forceUniformWidth(n6, n2);
        }
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            this.drawDividersVertical(canvas);
            return;
        }
        this.drawDividersHorizontal(canvas);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)LinearLayoutCompat.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)LinearLayoutCompat.class.getName());
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        if (this.mOrientation == 1) {
            this.layoutVertical(n, n2, n3, n4);
            return;
        }
        this.layoutHorizontal(n, n2, n3, n4);
    }

    protected void onMeasure(int n, int n2) {
        if (this.mOrientation == 1) {
            this.measureVertical(n, n2);
            return;
        }
        this.measureHorizontal(n, n2);
    }

    public void setBaselineAligned(boolean bl) {
        this.mBaselineAligned = bl;
    }

    public void setBaselineAlignedChildIndex(int n) {
        if (n >= 0 && n < this.getChildCount()) {
            this.mBaselineAlignedChildIndex = n;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("base aligned child index out of range (0, ");
        stringBuilder.append(this.getChildCount());
        stringBuilder.append(")");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setDividerDrawable(Drawable drawable2) {
        if (drawable2 == this.mDivider) {
            return;
        }
        this.mDivider = drawable2;
        boolean bl = false;
        if (drawable2 != null) {
            this.mDividerWidth = drawable2.getIntrinsicWidth();
            this.mDividerHeight = drawable2.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        if (drawable2 == null) {
            bl = true;
        }
        this.setWillNotDraw(bl);
        this.requestLayout();
    }

    public void setDividerPadding(int n) {
        this.mDividerPadding = n;
    }

    public void setGravity(int n) {
        if (this.mGravity != n) {
            int n2 = n;
            if ((8388615 & n) == 0) {
                n2 = n | 8388611;
            }
            n = n2;
            if ((n2 & 112) == 0) {
                n = n2 | 48;
            }
            this.mGravity = n;
            this.requestLayout();
        }
    }

    public void setHorizontalGravity(int n) {
        int n2 = this.mGravity;
        if ((8388615 & n2) != (n &= 8388615)) {
            this.mGravity = -8388616 & n2 | n;
            this.requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean bl) {
        this.mUseLargestChild = bl;
    }

    public void setOrientation(int n) {
        if (this.mOrientation != n) {
            this.mOrientation = n;
            this.requestLayout();
        }
    }

    public void setShowDividers(int n) {
        if (n != this.mShowDividers) {
            this.requestLayout();
        }
        this.mShowDividers = n;
    }

    public void setVerticalGravity(int n) {
        int n2 = this.mGravity;
        if ((n2 & 112) != (n &= 112)) {
            this.mGravity = n2 & -113 | n;
            this.requestLayout();
        }
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface DividerMode {
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        public int gravity = -1;
        public float weight;

        public LayoutParams(int n, int n2) {
            super(n, n2);
            this.weight = 0.0f;
        }

        public LayoutParams(int n, int n2, float f) {
            super(n, n2);
            this.weight = f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutCompat_Layout);
            this.weight = context.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = context.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            context.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface OrientationMode {
    }

}

