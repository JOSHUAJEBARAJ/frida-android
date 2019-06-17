/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.animation.ObjectAnimator
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Outline
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.AnimationDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.StateSet
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v7.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.graphics.drawable.DrawableContainer;
import android.support.v7.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedStateListDrawableCompat
extends StateListDrawable {
    private static final String ELEMENT_ITEM = "item";
    private static final String ELEMENT_TRANSITION = "transition";
    private static final String ITEM_MISSING_DRAWABLE_ERROR = ": <item> tag requires a 'drawable' attribute or child tag defining a drawable";
    private static final String LOGTAG = AnimatedStateListDrawableCompat.class.getSimpleName();
    private static final String TRANSITION_MISSING_DRAWABLE_ERROR = ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable";
    private static final String TRANSITION_MISSING_FROM_TO_ID = ": <transition> tag requires 'fromId' & 'toId' attributes";
    private boolean mMutated;
    private AnimatedStateListState mState;
    private Transition mTransition;
    private int mTransitionFromIndex = -1;
    private int mTransitionToIndex = -1;

    public AnimatedStateListDrawableCompat() {
        this(null, null);
    }

    AnimatedStateListDrawableCompat(@Nullable AnimatedStateListState animatedStateListState, @Nullable Resources resources) {
        super(null);
        this.setConstantState(new AnimatedStateListState(animatedStateListState, this, resources));
        this.onStateChange(this.getState());
        this.jumpToCurrentState();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Nullable
    public static AnimatedStateListDrawableCompat create(@NonNull Context context, @DrawableRes int n, @Nullable Resources.Theme theme) {
        Resources resources = context.getResources();
        XmlResourceParser xmlResourceParser = resources.getXml(n);
        AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
        while ((n = xmlResourceParser.next()) != 2 && n != 1) {
        }
        if (n != 2) throw new XmlPullParserException("No start tag found");
        try {
            return AnimatedStateListDrawableCompat.createFromXmlInner(context, resources, (XmlPullParser)xmlResourceParser, attributeSet, theme);
        }
        catch (IOException iOException) {
            Log.e((String)LOGTAG, (String)"parser error", (Throwable)iOException);
            return null;
        }
        catch (XmlPullParserException xmlPullParserException) {
            Log.e((String)LOGTAG, (String)"parser error", (Throwable)xmlPullParserException);
        }
        return null;
    }

    public static AnimatedStateListDrawableCompat createFromXmlInner(@NonNull Context object, @NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) throws IOException, XmlPullParserException {
        Object object2 = xmlPullParser.getName();
        if (object2.equals("animated-selector")) {
            object2 = new AnimatedStateListDrawableCompat();
            object2.inflate((Context)object, resources, xmlPullParser, attributeSet, theme);
            return object2;
        }
        object = new StringBuilder();
        object.append(xmlPullParser.getPositionDescription());
        object.append(": invalid animated-selector tag ");
        object.append((String)object2);
        throw new XmlPullParserException(object.toString());
    }

    private void inflateChildElements(@NonNull Context context, @NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        int n2;
        int n3 = xmlPullParser.getDepth() + 1;
        while ((n2 = xmlPullParser.next()) != 1 && ((n = xmlPullParser.getDepth()) >= n3 || n2 != 3)) {
            if (n2 != 2 || n > n3) continue;
            if (xmlPullParser.getName().equals("item")) {
                this.parseItem(context, resources, xmlPullParser, attributeSet, theme);
                continue;
            }
            if (!xmlPullParser.getName().equals("transition")) continue;
            this.parseTransition(context, resources, xmlPullParser, attributeSet, theme);
        }
    }

    private void init() {
        this.onStateChange(this.getState());
    }

    private int parseItem(@NonNull Context object, @NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        int[] arrn = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.AnimatedStateListDrawableItem);
        int n = arrn.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_id, 0);
        Drawable drawable2 = null;
        int n2 = arrn.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_drawable, -1);
        if (n2 > 0) {
            drawable2 = AppCompatResources.getDrawable((Context)object, n2);
        }
        arrn.recycle();
        arrn = this.extractStateSet(attributeSet);
        object = drawable2;
        if (drawable2 == null) {
            while ((n2 = xmlPullParser.next()) == 4) {
            }
            if (n2 == 2) {
                object = xmlPullParser.getName().equals("vector") ? VectorDrawableCompat.createFromXmlInner(resources, xmlPullParser, attributeSet, theme) : (Build.VERSION.SDK_INT >= 21 ? Drawable.createFromXmlInner((Resources)resources, (XmlPullParser)xmlPullParser, (AttributeSet)attributeSet, (Resources.Theme)theme) : Drawable.createFromXmlInner((Resources)resources, (XmlPullParser)xmlPullParser, (AttributeSet)attributeSet));
            } else {
                object = new StringBuilder();
                object.append(xmlPullParser.getPositionDescription());
                object.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                throw new XmlPullParserException(object.toString());
            }
        }
        if (object != null) {
            return this.mState.addStateSet(arrn, (Drawable)object, n);
        }
        object = new StringBuilder();
        object.append(xmlPullParser.getPositionDescription());
        object.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
        object = new XmlPullParserException(object.toString());
        throw object;
    }

    private int parseTransition(@NonNull Context object, @NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        Object object2 = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.AnimatedStateListDrawableTransition);
        int n = object2.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_fromId, -1);
        int n2 = object2.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_toId, -1);
        Drawable drawable2 = null;
        int n3 = object2.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_drawable, -1);
        if (n3 > 0) {
            drawable2 = AppCompatResources.getDrawable((Context)object, n3);
        }
        boolean bl = object2.getBoolean(R.styleable.AnimatedStateListDrawableTransition_android_reversible, false);
        object2.recycle();
        object2 = drawable2;
        if (drawable2 == null) {
            while ((n3 = xmlPullParser.next()) == 4) {
            }
            if (n3 == 2) {
                object2 = xmlPullParser.getName().equals("animated-vector") ? AnimatedVectorDrawableCompat.createFromXmlInner((Context)object, resources, xmlPullParser, attributeSet, theme) : (Build.VERSION.SDK_INT >= 21 ? Drawable.createFromXmlInner((Resources)resources, (XmlPullParser)xmlPullParser, (AttributeSet)attributeSet, (Resources.Theme)theme) : Drawable.createFromXmlInner((Resources)resources, (XmlPullParser)xmlPullParser, (AttributeSet)attributeSet));
            } else {
                object = new StringBuilder();
                object.append(xmlPullParser.getPositionDescription());
                object.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
                throw new XmlPullParserException(object.toString());
            }
        }
        if (object2 != null) {
            if (n != -1 && n2 != -1) {
                return this.mState.addTransition(n, n2, (Drawable)object2, bl);
            }
            object = new StringBuilder();
            object.append(xmlPullParser.getPositionDescription());
            object.append(": <transition> tag requires 'fromId' & 'toId' attributes");
            throw new XmlPullParserException(object.toString());
        }
        object = new StringBuilder();
        object.append(xmlPullParser.getPositionDescription());
        object.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
        object = new XmlPullParserException(object.toString());
        throw object;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean selectTransition(int n) {
        int n2;
        Object object = this.mTransition;
        if (object != null) {
            if (n == this.mTransitionToIndex) {
                return true;
            }
            if (n == this.mTransitionFromIndex && object.canReverse()) {
                object.reverse();
                this.mTransitionToIndex = this.mTransitionFromIndex;
                this.mTransitionFromIndex = n;
                return true;
            }
            n2 = this.mTransitionToIndex;
            object.stop();
        } else {
            n2 = this.getCurrentIndex();
        }
        this.mTransition = null;
        this.mTransitionFromIndex = -1;
        this.mTransitionToIndex = -1;
        object = this.mState;
        int n3 = object.getKeyframeIdAt(n2);
        int n4 = object.getKeyframeIdAt(n);
        if (n4 == 0) return false;
        if (n3 == 0) {
            return false;
        }
        int n5 = object.indexOfTransition(n3, n4);
        if (n5 < 0) {
            return false;
        }
        boolean bl = object.transitionHasReversibleFlag(n3, n4);
        this.selectDrawable(n5);
        Drawable drawable2 = this.getCurrent();
        if (drawable2 instanceof AnimationDrawable) {
            boolean bl2 = object.isTransitionReversed(n3, n4);
            object = new AnimationDrawableTransition((AnimationDrawable)drawable2, bl2, bl);
        } else if (drawable2 instanceof AnimatedVectorDrawableCompat) {
            object = new AnimatedVectorDrawableTransition((AnimatedVectorDrawableCompat)drawable2);
        } else {
            if (!(drawable2 instanceof Animatable)) return false;
            object = new AnimatableTransition((Animatable)drawable2);
        }
        object.start();
        this.mTransition = object;
        this.mTransitionFromIndex = n2;
        this.mTransitionToIndex = n;
        return true;
    }

    private void updateStateFromTypedArray(TypedArray typedArray) {
        AnimatedStateListState animatedStateListState = this.mState;
        if (Build.VERSION.SDK_INT >= 21) {
            animatedStateListState.mChangingConfigurations |= typedArray.getChangingConfigurations();
        }
        animatedStateListState.setVariablePadding(typedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_variablePadding, animatedStateListState.mVariablePadding));
        animatedStateListState.setConstantSize(typedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_constantSize, animatedStateListState.mConstantSize));
        animatedStateListState.setEnterFadeDuration(typedArray.getInt(R.styleable.AnimatedStateListDrawableCompat_android_enterFadeDuration, animatedStateListState.mEnterFadeDuration));
        animatedStateListState.setExitFadeDuration(typedArray.getInt(R.styleable.AnimatedStateListDrawableCompat_android_exitFadeDuration, animatedStateListState.mExitFadeDuration));
        this.setDither(typedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_dither, animatedStateListState.mDither));
    }

    public void addState(@NonNull int[] arrn, @NonNull Drawable drawable2, int n) {
        if (drawable2 != null) {
            this.mState.addStateSet(arrn, drawable2, n);
            this.onStateChange(this.getState());
            return;
        }
        throw new IllegalArgumentException("Drawable must not be null");
    }

    public <T extends Drawable> void addTransition(int n, int n2, @NonNull T t, boolean bl) {
        if (t != null) {
            this.mState.addTransition(n, n2, (Drawable)t, bl);
            return;
        }
        throw new IllegalArgumentException("Transition drawable must not be null");
    }

    @Override
    void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Override
    AnimatedStateListState cloneConstantState() {
        return new AnimatedStateListState(this.mState, this, null);
    }

    @Override
    public void inflate(@NonNull Context context, @NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray typedArray = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.AnimatedStateListDrawableCompat);
        this.setVisible(typedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_visible, true), true);
        this.updateStateFromTypedArray(typedArray);
        this.updateDensity(resources);
        typedArray.recycle();
        this.inflateChildElements(context, resources, xmlPullParser, attributeSet, theme);
        this.init();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        Transition transition = this.mTransition;
        if (transition != null) {
            transition.stop();
            this.mTransition = null;
            this.selectDrawable(this.mTransitionToIndex);
            this.mTransitionToIndex = -1;
            this.mTransitionFromIndex = -1;
        }
    }

    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    @Override
    protected boolean onStateChange(int[] arrn) {
        int n = this.mState.indexOfKeyframe(arrn);
        boolean bl = n != this.getCurrentIndex() && (this.selectTransition(n) || this.selectDrawable(n));
        Drawable drawable2 = this.getCurrent();
        boolean bl2 = bl;
        if (drawable2 != null) {
            bl2 = bl | drawable2.setState(arrn);
        }
        return bl2;
    }

    @Override
    protected void setConstantState(@NonNull DrawableContainer.DrawableContainerState drawableContainerState) {
        super.setConstantState(drawableContainerState);
        if (drawableContainerState instanceof AnimatedStateListState) {
            this.mState = (AnimatedStateListState)drawableContainerState;
        }
    }

    @Override
    public boolean setVisible(boolean bl, boolean bl2) {
        boolean bl3 = super.setVisible(bl, bl2);
        if (this.mTransition != null && (bl3 || bl2)) {
            if (bl) {
                this.mTransition.start();
                return bl3;
            }
            this.jumpToCurrentState();
        }
        return bl3;
    }

    private static class AnimatableTransition
    extends Transition {
        private final Animatable mA;

        AnimatableTransition(Animatable animatable) {
            super();
            this.mA = animatable;
        }

        @Override
        public void start() {
            this.mA.start();
        }

        @Override
        public void stop() {
            this.mA.stop();
        }
    }

    static class AnimatedStateListState
    extends StateListDrawable.StateListState {
        private static final long REVERSED_BIT = 0x100000000L;
        private static final long REVERSIBLE_FLAG_BIT = 0x200000000L;
        SparseArrayCompat<Integer> mStateIds;
        LongSparseArray<Long> mTransitions;

        AnimatedStateListState(@Nullable AnimatedStateListState animatedStateListState, @NonNull AnimatedStateListDrawableCompat animatedStateListDrawableCompat, @Nullable Resources resources) {
            super(animatedStateListState, animatedStateListDrawableCompat, resources);
            if (animatedStateListState != null) {
                this.mTransitions = animatedStateListState.mTransitions;
                this.mStateIds = animatedStateListState.mStateIds;
                return;
            }
            this.mTransitions = new LongSparseArray();
            this.mStateIds = new SparseArrayCompat();
        }

        private static long generateTransitionKey(int n, int n2) {
            return (long)n << 32 | (long)n2;
        }

        int addStateSet(@NonNull int[] arrn, @NonNull Drawable drawable2, int n) {
            int n2 = super.addStateSet(arrn, drawable2);
            this.mStateIds.put(n2, n);
            return n2;
        }

        int addTransition(int n, int n2, @NonNull Drawable drawable2, boolean bl) {
            int n3 = super.addChild(drawable2);
            long l = AnimatedStateListState.generateTransitionKey(n, n2);
            long l2 = 0;
            if (bl) {
                l2 = 0x200000000L;
            }
            this.mTransitions.append(l, (long)n3 | l2);
            if (bl) {
                l = AnimatedStateListState.generateTransitionKey(n2, n);
                this.mTransitions.append(l, (long)n3 | 0x100000000L | l2);
                return n3;
            }
            return n3;
        }

        int getKeyframeIdAt(int n) {
            if (n < 0) {
                return 0;
            }
            return this.mStateIds.get(n, 0);
        }

        int indexOfKeyframe(@NonNull int[] arrn) {
            int n = super.indexOfStateSet(arrn);
            if (n >= 0) {
                return n;
            }
            return super.indexOfStateSet(StateSet.WILD_CARD);
        }

        int indexOfTransition(int n, int n2) {
            long l = AnimatedStateListState.generateTransitionKey(n, n2);
            return (int)this.mTransitions.get(l, -1).longValue();
        }

        boolean isTransitionReversed(int n, int n2) {
            long l = AnimatedStateListState.generateTransitionKey(n, n2);
            if ((this.mTransitions.get(l, -1) & 0x100000000L) != 0) {
                return true;
            }
            return false;
        }

        @Override
        void mutate() {
            this.mTransitions = this.mTransitions.clone();
            this.mStateIds = this.mStateIds.clone();
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new AnimatedStateListDrawableCompat(this, null);
        }

        @NonNull
        @Override
        public Drawable newDrawable(Resources resources) {
            return new AnimatedStateListDrawableCompat(this, resources);
        }

        boolean transitionHasReversibleFlag(int n, int n2) {
            long l = AnimatedStateListState.generateTransitionKey(n, n2);
            if ((this.mTransitions.get(l, -1) & 0x200000000L) != 0) {
                return true;
            }
            return false;
        }
    }

    private static class AnimatedVectorDrawableTransition
    extends Transition {
        private final AnimatedVectorDrawableCompat mAvd;

        AnimatedVectorDrawableTransition(AnimatedVectorDrawableCompat animatedVectorDrawableCompat) {
            super();
            this.mAvd = animatedVectorDrawableCompat;
        }

        @Override
        public void start() {
            this.mAvd.start();
        }

        @Override
        public void stop() {
            this.mAvd.stop();
        }
    }

    private static class AnimationDrawableTransition
    extends Transition {
        private final ObjectAnimator mAnim;
        private final boolean mHasReversibleFlag;

        AnimationDrawableTransition(AnimationDrawable animationDrawable, boolean bl, boolean bl2) {
            super();
            int n = animationDrawable.getNumberOfFrames();
            int n2 = bl ? n - 1 : 0;
            n = bl ? 0 : --n;
            FrameInterpolator frameInterpolator = new FrameInterpolator(animationDrawable, bl);
            animationDrawable = ObjectAnimator.ofInt((Object)animationDrawable, (String)"currentIndex", (int[])new int[]{n2, n});
            if (Build.VERSION.SDK_INT >= 18) {
                animationDrawable.setAutoCancel(true);
            }
            animationDrawable.setDuration((long)frameInterpolator.getTotalDuration());
            animationDrawable.setInterpolator((TimeInterpolator)frameInterpolator);
            this.mHasReversibleFlag = bl2;
            this.mAnim = animationDrawable;
        }

        @Override
        public boolean canReverse() {
            return this.mHasReversibleFlag;
        }

        @Override
        public void reverse() {
            this.mAnim.reverse();
        }

        @Override
        public void start() {
            this.mAnim.start();
        }

        @Override
        public void stop() {
            this.mAnim.cancel();
        }
    }

    private static class FrameInterpolator
    implements TimeInterpolator {
        private int[] mFrameTimes;
        private int mFrames;
        private int mTotalDuration;

        FrameInterpolator(AnimationDrawable animationDrawable, boolean bl) {
            this.updateFrames(animationDrawable, bl);
        }

        public float getInterpolation(float f) {
            int n;
            int n2 = (int)((float)this.mTotalDuration * f + 0.5f);
            int n3 = this.mFrames;
            int[] arrn = this.mFrameTimes;
            for (n = 0; n < n3 && n2 >= arrn[n]; n2 -= arrn[n], ++n) {
            }
            f = n < n3 ? (float)n2 / (float)this.mTotalDuration : 0.0f;
            return (float)n / (float)n3 + f;
        }

        int getTotalDuration() {
            return this.mTotalDuration;
        }

        int updateFrames(AnimationDrawable animationDrawable, boolean bl) {
            int n;
            this.mFrames = n = animationDrawable.getNumberOfFrames();
            int[] arrn = this.mFrameTimes;
            if (arrn == null || arrn.length < n) {
                this.mFrameTimes = new int[n];
            }
            arrn = this.mFrameTimes;
            int n2 = 0;
            for (int i = 0; i < n; ++i) {
                int n3 = bl ? n - i - 1 : i;
                arrn[i] = n3 = animationDrawable.getDuration(n3);
                n2 += n3;
            }
            this.mTotalDuration = n2;
            return n2;
        }
    }

    private static abstract class Transition {
        private Transition() {
        }

        public boolean canReverse() {
            return false;
        }

        public void reverse() {
        }

        public abstract void start();

        public abstract void stop();
    }

}

