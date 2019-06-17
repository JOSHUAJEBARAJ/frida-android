/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.StateSet
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.graphics.drawable.DrawableContainer;
import android.util.AttributeSet;
import android.util.StateSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
class StateListDrawable
extends DrawableContainer {
    private static final boolean DEBUG = false;
    private static final String TAG = "StateListDrawable";
    private boolean mMutated;
    private StateListState mStateListState;

    StateListDrawable() {
        this(null, null);
    }

    StateListDrawable(@Nullable StateListState stateListState) {
        if (stateListState != null) {
            this.setConstantState(stateListState);
        }
    }

    StateListDrawable(StateListState stateListState, Resources resources) {
        this.setConstantState(new StateListState(stateListState, this, resources));
        this.onStateChange(this.getState());
    }

    private void inflateChildElements(Context object, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        StateListState stateListState = this.mStateListState;
        int n2 = xmlPullParser.getDepth() + 1;
        while ((n = xmlPullParser.next()) != 1) {
            int n3 = xmlPullParser.getDepth();
            if (n3 < n2 && n == 3) {
                return;
            }
            if (n != 2 || n3 > n2 || !xmlPullParser.getName().equals("item")) continue;
            TypedArray typedArray = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.StateListDrawableItem);
            Drawable drawable2 = null;
            n = typedArray.getResourceId(R.styleable.StateListDrawableItem_android_drawable, -1);
            if (n > 0) {
                drawable2 = AppCompatResources.getDrawable((Context)object, n);
            }
            typedArray.recycle();
            int[] arrn = this.extractStateSet(attributeSet);
            typedArray = drawable2;
            if (drawable2 == null) {
                while ((n = xmlPullParser.next()) == 4) {
                }
                if (n == 2) {
                    typedArray = Build.VERSION.SDK_INT >= 21 ? Drawable.createFromXmlInner((Resources)resources, (XmlPullParser)xmlPullParser, (AttributeSet)attributeSet, (Resources.Theme)theme) : Drawable.createFromXmlInner((Resources)resources, (XmlPullParser)xmlPullParser, (AttributeSet)attributeSet);
                } else {
                    object = new StringBuilder();
                    object.append(xmlPullParser.getPositionDescription());
                    object.append(": <item> tag requires a 'drawable' attribute or ");
                    object.append("child tag defining a drawable");
                    throw new XmlPullParserException(object.toString());
                }
            }
            stateListState.addStateSet(arrn, (Drawable)typedArray);
        }
    }

    private void updateStateFromTypedArray(TypedArray typedArray) {
        StateListState stateListState = this.mStateListState;
        if (Build.VERSION.SDK_INT >= 21) {
            stateListState.mChangingConfigurations |= typedArray.getChangingConfigurations();
        }
        stateListState.mVariablePadding = typedArray.getBoolean(R.styleable.StateListDrawable_android_variablePadding, stateListState.mVariablePadding);
        stateListState.mConstantSize = typedArray.getBoolean(R.styleable.StateListDrawable_android_constantSize, stateListState.mConstantSize);
        stateListState.mEnterFadeDuration = typedArray.getInt(R.styleable.StateListDrawable_android_enterFadeDuration, stateListState.mEnterFadeDuration);
        stateListState.mExitFadeDuration = typedArray.getInt(R.styleable.StateListDrawable_android_exitFadeDuration, stateListState.mExitFadeDuration);
        stateListState.mDither = typedArray.getBoolean(R.styleable.StateListDrawable_android_dither, stateListState.mDither);
    }

    public void addState(int[] arrn, Drawable drawable2) {
        if (drawable2 != null) {
            this.mStateListState.addStateSet(arrn, drawable2);
            this.onStateChange(this.getState());
        }
    }

    @RequiresApi(value=21)
    @Override
    public void applyTheme(@NonNull Resources.Theme theme) {
        super.applyTheme(theme);
        this.onStateChange(this.getState());
    }

    @Override
    void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Override
    StateListState cloneConstantState() {
        return new StateListState(this.mStateListState, this, null);
    }

    int[] extractStateSet(AttributeSet attributeSet) {
        int n = 0;
        int n2 = attributeSet.getAttributeCount();
        int[] arrn = new int[n2];
        for (int i = 0; i < n2; ++i) {
            int n3 = attributeSet.getAttributeNameResource(i);
            if (n3 == 0 || n3 == 16842960 || n3 == 16843161) continue;
            if (!attributeSet.getAttributeBooleanValue(i, false)) {
                n3 = - n3;
            }
            arrn[n] = n3;
            ++n;
        }
        return StateSet.trimStateSet((int[])arrn, (int)n);
    }

    int getStateCount() {
        return this.mStateListState.getChildCount();
    }

    Drawable getStateDrawable(int n) {
        return this.mStateListState.getChild(n);
    }

    int getStateDrawableIndex(int[] arrn) {
        return this.mStateListState.indexOfStateSet(arrn);
    }

    StateListState getStateListState() {
        return this.mStateListState;
    }

    int[] getStateSet(int n) {
        return this.mStateListState.mStateSets[n];
    }

    public void inflate(@NonNull Context context, @NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray typedArray = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.StateListDrawable);
        this.setVisible(typedArray.getBoolean(R.styleable.StateListDrawable_android_visible, true), true);
        this.updateStateFromTypedArray(typedArray);
        this.updateDensity(resources);
        typedArray.recycle();
        this.inflateChildElements(context, resources, xmlPullParser, attributeSet, theme);
        this.onStateChange(this.getState());
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mStateListState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    @Override
    protected boolean onStateChange(int[] arrn) {
        int n;
        boolean bl = super.onStateChange(arrn);
        int n2 = n = this.mStateListState.indexOfStateSet(arrn);
        if (n < 0) {
            n2 = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        if (!this.selectDrawable(n2) && !bl) {
            return false;
        }
        return true;
    }

    @Override
    protected void setConstantState(@NonNull DrawableContainer.DrawableContainerState drawableContainerState) {
        super.setConstantState(drawableContainerState);
        if (drawableContainerState instanceof StateListState) {
            this.mStateListState = (StateListState)drawableContainerState;
        }
    }

    static class StateListState
    extends DrawableContainer.DrawableContainerState {
        int[][] mStateSets;

        StateListState(StateListState stateListState, StateListDrawable stateListDrawable, Resources resources) {
            super(stateListState, stateListDrawable, resources);
            if (stateListState != null) {
                this.mStateSets = stateListState.mStateSets;
                return;
            }
            this.mStateSets = new int[this.getCapacity()][];
        }

        int addStateSet(int[] arrn, Drawable drawable2) {
            int n = this.addChild(drawable2);
            this.mStateSets[n] = arrn;
            return n;
        }

        @Override
        public void growArray(int n, int n2) {
            super.growArray(n, n2);
            int[][] arrarrn = new int[n2][];
            System.arraycopy(this.mStateSets, 0, arrarrn, 0, n);
            this.mStateSets = arrarrn;
        }

        int indexOfStateSet(int[] arrn) {
            int[][] arrn2 = this.mStateSets;
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                if (!StateSet.stateSetMatches((int[])arrn2[i], (int[])arrn)) continue;
                return i;
            }
            return -1;
        }

        @Override
        void mutate() {
            int[][] arrn = this.mStateSets;
            int[][] arrarrn = new int[arrn.length][];
            for (int i = arrn.length - 1; i >= 0; --i) {
                arrn = this.mStateSets;
                arrn = arrn[i] != null ? (int[])arrn[i].clone() : null;
                arrarrn[i] = arrn;
            }
            this.mStateSets = arrarrn;
        }

        @NonNull
        public Drawable newDrawable() {
            return new StateListDrawable(this, null);
        }

        @NonNull
        public Drawable newDrawable(Resources resources) {
            return new StateListDrawable(this, resources);
        }
    }

}

