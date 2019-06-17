/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorInflater
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.PropertyValuesHolder
 *  android.animation.ValueAnimator
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.graphics.Paint
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory2
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.AlphaAnimation
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationSet
 *  android.view.animation.AnimationUtils
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.ScaleAnimation
 *  android.view.animation.Transformation
 */
package android.support.v4.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.BackStackRecord;
import android.support.v4.app.BackStackState;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerNonConfig;
import android.support.v4.app.FragmentManagerState;
import android.support.v4.app.FragmentState;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransition;
import android.support.v4.app.OneShotPreDrawListener;
import android.support.v4.app.SuperNotCalledException;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl
extends FragmentManager
implements LayoutInflater.Factory2 {
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    SparseArray<Fragment> mActive;
    final ArrayList<Fragment> mAdded = new ArrayList();
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList();
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex = 0;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    @Nullable
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    boolean mStopped;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;

    static {
        DEBUG = false;
        sAnimationListenerField = null;
        DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    }

    FragmentManagerImpl() {
        this.mExecCommit = new Runnable(){

            @Override
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }

    private void addAddedFragments(ArraySet<Fragment> arraySet) {
        int n = this.mCurState;
        if (n < 1) {
            return;
        }
        int n2 = Math.min(n, 3);
        int n3 = this.mAdded.size();
        for (n = 0; n < n3; ++n) {
            Fragment fragment = this.mAdded.get(n);
            if (fragment.mState >= n2) continue;
            this.moveToState(fragment, n2, fragment.getNextAnim(), fragment.getNextTransition(), false);
            if (fragment.mView == null || fragment.mHidden || !fragment.mIsNewlyAdded) continue;
            arraySet.add(fragment);
        }
    }

    private void animateRemoveFragment(final @NonNull Fragment fragment, @NonNull AnimationOrAnimator animationOrAnimator, int n) {
        final View view = fragment.mView;
        final ViewGroup viewGroup = fragment.mContainer;
        viewGroup.startViewTransition(view);
        fragment.setStateAfterAnimating(n);
        if (animationOrAnimator.animation != null) {
            EndViewTransitionAnimator endViewTransitionAnimator = new EndViewTransitionAnimator(animationOrAnimator.animation, viewGroup, view);
            fragment.setAnimatingAway(fragment.mView);
            endViewTransitionAnimator.setAnimationListener((Animation.AnimationListener)new AnimationListenerWrapper(FragmentManagerImpl.getAnimationListener((Animation)endViewTransitionAnimator)){

                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    viewGroup.post(new Runnable(){

                        @Override
                        public void run() {
                            if (fragment.getAnimatingAway() != null) {
                                fragment.setAnimatingAway(null);
                                FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                            }
                        }
                    });
                }

            });
            FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(view, animationOrAnimator);
            fragment.mView.startAnimation((Animation)endViewTransitionAnimator);
            return;
        }
        Animator animator = animationOrAnimator.animator;
        fragment.setAnimator(animationOrAnimator.animator);
        animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator object) {
                viewGroup.endViewTransition(view);
                object = fragment.getAnimator();
                fragment.setAnimator(null);
                if (object != null && viewGroup.indexOfChild(view) < 0) {
                    object = FragmentManagerImpl.this;
                    Fragment fragment2 = fragment;
                    object.moveToState(fragment2, fragment2.getStateAfterAnimating(), 0, 0, false);
                }
            }
        });
        animator.setTarget((Object)fragment.mView);
        FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
        animator.start();
    }

    private void burpActive() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray != null) {
            for (int i = sparseArray.size() - 1; i >= 0; --i) {
                if (this.mActive.valueAt(i) != null) continue;
                sparseArray = this.mActive;
                sparseArray.delete(sparseArray.keyAt(i));
            }
        }
    }

    private void checkStateLoss() {
        if (!this.isStateSaved()) {
            if (this.mNoTransactionsBecause == null) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can not perform this action inside of ");
            stringBuilder.append(this.mNoTransactionsBecause);
            throw new IllegalStateException(stringBuilder.toString());
        }
        throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    private void dispatchStateChange(int n) {
        try {
            this.mExecutingActions = true;
            this.moveToState(n, false);
            this.execPendingActions();
            return;
        }
        finally {
            this.mExecutingActions = false;
        }
    }

    private void endAnimatingAwayFragments() {
        Object object = this.mActive;
        int n = object == null ? 0 : object.size();
        for (int i = 0; i < n; ++i) {
            object = (Fragment)this.mActive.valueAt(i);
            if (object == null) continue;
            if (object.getAnimatingAway() != null) {
                int n2 = object.getStateAfterAnimating();
                View view = object.getAnimatingAway();
                Animation animation = view.getAnimation();
                if (animation != null) {
                    animation.cancel();
                    view.clearAnimation();
                }
                object.setAnimatingAway(null);
                this.moveToState((Fragment)object, n2, 0, 0, false);
                continue;
            }
            if (object.getAnimator() == null) continue;
            object.getAnimator().end();
        }
    }

    private void ensureExecReady(boolean bl) {
        if (!this.mExecutingActions) {
            if (this.mHost != null) {
                if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
                    if (!bl) {
                        this.checkStateLoss();
                    }
                    if (this.mTmpRecords == null) {
                        this.mTmpRecords = new ArrayList();
                        this.mTmpIsPop = new ArrayList();
                    }
                    this.mExecutingActions = true;
                    try {
                        this.executePostponedTransaction(null, null);
                        return;
                    }
                    finally {
                        this.mExecutingActions = false;
                    }
                }
                throw new IllegalStateException("Must be called from main thread of fragment host");
            }
            throw new IllegalStateException("Fragment host has been destroyed");
        }
        throw new IllegalStateException("FragmentManager is already executing transactions");
    }

    private static void executeOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2) {
        while (n < n2) {
            BackStackRecord backStackRecord = arrayList.get(n);
            boolean bl = arrayList2.get(n);
            boolean bl2 = true;
            if (bl) {
                backStackRecord.bumpBackStackNesting(-1);
                if (n != n2 - 1) {
                    bl2 = false;
                }
                backStackRecord.executePopOps(bl2);
            } else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            ++n;
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2) {
        int n3;
        boolean bl = arrayList.get((int)n).mReorderingAllowed;
        ArrayList<Fragment> arrayList3 = this.mTmpAddedFragments;
        if (arrayList3 == null) {
            this.mTmpAddedFragments = new ArrayList();
        } else {
            arrayList3.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        arrayList3 = this.getPrimaryNavigationFragment();
        int n4 = n;
        int n5 = 0;
        do {
            int n6 = 1;
            if (n4 >= n2) break;
            BackStackRecord backStackRecord = arrayList.get(n4);
            arrayList3 = arrayList2.get(n4) == false ? backStackRecord.expandOps(this.mTmpAddedFragments, (Fragment)((Object)arrayList3)) : backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, (Fragment)((Object)arrayList3));
            n3 = n6;
            if (n5 == 0) {
                n3 = backStackRecord.mAddToBackStack ? n6 : 0;
            }
            ++n4;
            n5 = n3;
        } while (true);
        this.mTmpAddedFragments.clear();
        if (!bl) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, n, n2, false);
        }
        FragmentManagerImpl.executeOps(arrayList, arrayList2, n, n2);
        n3 = n2;
        if (bl) {
            arrayList3 = new ArraySet<Fragment>();
            this.addAddedFragments((ArraySet<Fragment>)((Object)arrayList3));
            n3 = this.postponePostponableTransactions(arrayList, arrayList2, n, n2, (ArraySet<Fragment>)((Object)arrayList3));
            this.makeRemovedFragmentsInvisible((ArraySet<Fragment>)((Object)arrayList3));
        }
        if (n3 != n && bl) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, n, n3, true);
            this.moveToState(this.mCurState, true);
        }
        while (n < n2) {
            arrayList3 = arrayList.get(n);
            if (arrayList2.get(n).booleanValue() && arrayList3.mIndex >= 0) {
                this.freeBackStackIndex(arrayList3.mIndex);
                arrayList3.mIndex = -1;
            }
            arrayList3.runOnCommitRunnables();
            ++n;
        }
        if (n5 != 0) {
            this.reportBackStackChanged();
        }
    }

    /*
     * Exception decompiling
     */
    private void executePostponedTransaction(ArrayList<BackStackRecord> var1_1, ArrayList<Boolean> var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Statement already marked as first in another block
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.markFirstStatementInBlock(Op03SimpleStatement.java:420)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.markWholeBlock(Misc.java:219)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.ConditionalRewriter.considerAsSimpleIf(ConditionalRewriter.java:619)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.ConditionalRewriter.identifyNonjumpingConditionals(ConditionalRewriter.java:45)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:679)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    private Fragment findFragmentUnder(Fragment fragment) {
        ViewGroup viewGroup = fragment.mContainer;
        View view = fragment.mView;
        if (viewGroup != null) {
            if (view == null) {
                return null;
            }
            for (int i = this.mAdded.indexOf((Object)fragment) - 1; i >= 0; --i) {
                fragment = this.mAdded.get(i);
                if (fragment.mContainer != viewGroup || fragment.mView == null) continue;
                return fragment;
            }
            return null;
        }
        return null;
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        boolean bl = false;
        synchronized (this) {
            int n;
            if (this.mPendingActions == null) return false;
            if (this.mPendingActions.size() != 0) {
                n = this.mPendingActions.size();
            } else {
                return false;
            }
            for (int i = 0; i < n; bl |= this.mPendingActions.get((int)i).generateOps(arrayList, arrayList2), ++i) {
            }
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            return bl;
        }
    }

    private static Animation.AnimationListener getAnimationListener(Animation animation) {
        try {
            if (sAnimationListenerField == null) {
                sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                sAnimationListenerField.setAccessible(true);
            }
            animation = (Animation.AnimationListener)sAnimationListenerField.get((Object)animation);
            return animation;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.e((String)"FragmentManager", (String)"Cannot access Animation's mListener field", (Throwable)illegalAccessException);
            return null;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.e((String)"FragmentManager", (String)"No field with the name mListener is found in Animation class", (Throwable)noSuchFieldException);
            return null;
        }
    }

    static AnimationOrAnimator makeFadeAnimation(Context context, float f, float f2) {
        context = new AlphaAnimation(f, f2);
        context.setInterpolator(DECELERATE_CUBIC);
        context.setDuration(220);
        return new AnimationOrAnimator((Animation)context);
    }

    static AnimationOrAnimator makeOpenCloseAnimation(Context context, float f, float f2, float f3, float f4) {
        context = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(f, f2, f, f2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(DECELERATE_QUINT);
        scaleAnimation.setDuration(220);
        context.addAnimation((Animation)scaleAnimation);
        scaleAnimation = new AlphaAnimation(f3, f4);
        scaleAnimation.setInterpolator(DECELERATE_CUBIC);
        scaleAnimation.setDuration(220);
        context.addAnimation((Animation)scaleAnimation);
        return new AnimationOrAnimator((Animation)context);
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> arraySet) {
        int n = arraySet.size();
        for (int i = 0; i < n; ++i) {
            Fragment fragment = arraySet.valueAt(i);
            if (fragment.mAdded) continue;
            View view = fragment.getView();
            fragment.mPostponedAlpha = view.getAlpha();
            view.setAlpha(0.0f);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static boolean modifiesAlpha(Animator object) {
        if (object == null) {
            return false;
        }
        if (object instanceof ValueAnimator) {
            object = ((ValueAnimator)object).getValues();
            int n = 0;
            while (n < object.length) {
                if ("alpha".equals(object[n].getPropertyName())) {
                    return true;
                }
                ++n;
            }
            return false;
        }
        if (!(object instanceof AnimatorSet)) return false;
        object = ((AnimatorSet)object).getChildAnimations();
        int n = 0;
        while (n < object.size()) {
            if (FragmentManagerImpl.modifiesAlpha((Animator)object.get(n))) {
                return true;
            }
            ++n;
        }
        return false;
    }

    static boolean modifiesAlpha(AnimationOrAnimator object) {
        if (object.animation instanceof AlphaAnimation) {
            return true;
        }
        if (object.animation instanceof AnimationSet) {
            object = ((AnimationSet)object.animation).getAnimations();
            for (int i = 0; i < object.size(); ++i) {
                if (!(object.get(i) instanceof AlphaAnimation)) continue;
                return true;
            }
            return false;
        }
        return FragmentManagerImpl.modifiesAlpha(object.animator);
    }

    private boolean popBackStackImmediate(String string2, int n, int n2) {
        boolean bl;
        this.execPendingActions();
        this.ensureExecReady(true);
        Object object = this.mPrimaryNav;
        if (object != null && n < 0 && string2 == null && (object = object.peekChildFragmentManager()) != null && object.popBackStackImmediate()) {
            return true;
        }
        bl = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, string2, n, n2);
        if (bl) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return bl;
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2, ArraySet<Fragment> arraySet) {
        int n3 = n2;
        for (int i = n2 - 1; i >= n; --i) {
            BackStackRecord backStackRecord = arrayList.get(i);
            boolean bl = arrayList2.get(i);
            boolean bl2 = backStackRecord.isPostponed() && !backStackRecord.interactsWith(arrayList, i + 1, n2);
            int n4 = n3;
            if (bl2) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList();
                }
                StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bl);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
                if (bl) {
                    backStackRecord.executeOps();
                } else {
                    backStackRecord.executePopOps(false);
                }
                n4 = n3 - 1;
                if (i != n4) {
                    arrayList.remove(i);
                    arrayList.add(n4, backStackRecord);
                }
                this.addAddedFragments(arraySet);
            }
            n3 = n4;
        }
        return n3;
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (arrayList != null) {
            if (arrayList.isEmpty()) {
                return;
            }
            if (arrayList2 != null && arrayList.size() == arrayList2.size()) {
                this.executePostponedTransaction(arrayList, arrayList2);
                int n = arrayList.size();
                int n2 = 0;
                int n3 = 0;
                while (n3 < n) {
                    int n4 = n2;
                    int n5 = n3;
                    if (!arrayList.get((int)n3).mReorderingAllowed) {
                        if (n2 != n3) {
                            this.executeOpsTogether(arrayList, arrayList2, n2, n3);
                        }
                        n2 = n4 = n3 + 1;
                        if (arrayList2.get(n3).booleanValue()) {
                            do {
                                n2 = n4;
                                if (n4 >= n) break;
                                n2 = n4;
                                if (!arrayList2.get(n4).booleanValue()) break;
                                n2 = n4++;
                            } while (!arrayList.get((int)n4).mReorderingAllowed);
                        }
                        this.executeOpsTogether(arrayList, arrayList2, n3, n2);
                        n4 = n2;
                        n5 = n2 - 1;
                    }
                    n3 = n5 + 1;
                    n2 = n4;
                }
                if (n2 != n) {
                    this.executeOpsTogether(arrayList, arrayList2, n2, n);
                }
                return;
            }
            throw new IllegalStateException("Internal error with the back stack records");
        }
    }

    public static int reverseTransit(int n) {
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    return 0;
                }
                return 4097;
            }
            return 4099;
        }
        return 8194;
    }

    private static void setHWLayerAnimListenerIfAlpha(View view, AnimationOrAnimator animationOrAnimator) {
        if (view != null) {
            if (animationOrAnimator == null) {
                return;
            }
            if (FragmentManagerImpl.shouldRunOnHWLayer(view, animationOrAnimator)) {
                if (animationOrAnimator.animator != null) {
                    animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorOnHWLayerIfNeededListener(view));
                    return;
                }
                Animation.AnimationListener animationListener = FragmentManagerImpl.getAnimationListener(animationOrAnimator.animation);
                view.setLayerType(2, null);
                animationOrAnimator.animation.setAnimationListener((Animation.AnimationListener)new AnimateOnHWLayerIfNeededListener(view, animationListener));
            }
            return;
        }
    }

    private static void setRetaining(FragmentManagerNonConfig iterator) {
        if (iterator == null) {
            return;
        }
        List<Fragment> list = iterator.getFragments();
        if (list != null) {
            list = list.iterator();
            while (list.hasNext()) {
                ((Fragment)list.next()).mRetaining = true;
            }
        }
        if ((iterator = iterator.getChildNonConfigs()) != null) {
            iterator = iterator.iterator();
            while (iterator.hasNext()) {
                FragmentManagerImpl.setRetaining((FragmentManagerNonConfig)iterator.next());
            }
        }
    }

    static boolean shouldRunOnHWLayer(View view, AnimationOrAnimator animationOrAnimator) {
        if (view != null) {
            if (animationOrAnimator == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 19 && view.getLayerType() == 0 && ViewCompat.hasOverlappingRendering(view) && FragmentManagerImpl.modifiesAlpha(animationOrAnimator)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private void throwException(RuntimeException runtimeException) {
        Log.e((String)"FragmentManager", (String)runtimeException.getMessage());
        Log.e((String)"FragmentManager", (String)"Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.onDump("  ", null, printWriter, new String[0]);
            }
            catch (Exception exception) {
                Log.e((String)"FragmentManager", (String)"Failed dumping state", (Throwable)exception);
            }
        } else {
            try {
                this.dump("  ", null, printWriter, new String[0]);
            }
            catch (Exception exception) {
                Log.e((String)"FragmentManager", (String)"Failed dumping state", (Throwable)exception);
            }
        }
        throw runtimeException;
    }

    public static int transitToStyleIndex(int n, boolean bl) {
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    return -1;
                }
                n = bl ? 3 : 4;
                return n;
            }
            n = bl ? 5 : 6;
            return n;
        }
        n = bl ? 1 : 2;
        return n;
    }

    void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(backStackRecord);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void addFragment(Fragment fragment, boolean bl) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("add: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.makeActive(fragment);
        if (!fragment.mDetached) {
            if (this.mAdded.contains(fragment)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Fragment already added: ");
                stringBuilder.append(fragment);
                throw new IllegalStateException(stringBuilder.toString());
            }
            stringBuilder = this.mAdded;
            synchronized (stringBuilder) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (bl) {
                this.moveToState(fragment);
                return;
            }
        }
    }

    @Override
    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int allocBackStackIndex(BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                int n = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Adding back stack index ");
                    stringBuilder.append(n);
                    stringBuilder.append(" with ");
                    stringBuilder.append(backStackRecord);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                this.mBackStackIndices.set(n, backStackRecord);
                return n;
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int n = this.mBackStackIndices.size();
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Setting back stack index ");
                stringBuilder.append(n);
                stringBuilder.append(" to ");
                stringBuilder.append(backStackRecord);
                Log.v((String)"FragmentManager", (String)stringBuilder.toString());
            }
            this.mBackStackIndices.add(backStackRecord);
            return n;
        }
    }

    public void attachController(FragmentHostCallback fragmentHostCallback, FragmentContainer fragmentContainer, Fragment fragment) {
        if (this.mHost == null) {
            this.mHost = fragmentHostCallback;
            this.mContainer = fragmentContainer;
            this.mParent = fragment;
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void attachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("attach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                if (this.mAdded.contains(fragment)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Fragment already added: ");
                    stringBuilder.append(fragment);
                    throw new IllegalStateException(stringBuilder.toString());
                }
                if (DEBUG) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("add from attach: ");
                    stringBuilder.append(fragment);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                stringBuilder = this.mAdded;
                synchronized (stringBuilder) {
                    this.mAdded.add(fragment);
                }
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                    return;
                }
            }
        }
    }

    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    void completeExecute(BackStackRecord backStackRecord, boolean bl, boolean bl2, boolean bl3) {
        if (bl) {
            backStackRecord.executePopOps(bl3);
        } else {
            backStackRecord.executeOps();
        }
        Object object = new ArrayList<BackStackRecord>(1);
        ArrayList<Boolean> arrayList = new ArrayList<Boolean>(1);
        object.add((BackStackRecord)backStackRecord);
        arrayList.add(bl);
        if (bl2) {
            FragmentTransition.startTransitions(this, object, arrayList, 0, 1, true);
        }
        if (bl3) {
            this.moveToState(this.mCurState, true);
        }
        if ((object = this.mActive) != null) {
            int n = object.size();
            for (int i = 0; i < n; ++i) {
                object = (Fragment)this.mActive.valueAt(i);
                if (object == null || object.mView == null || !object.mIsNewlyAdded || !backStackRecord.interactsWith(object.mContainerId)) continue;
                if (object.mPostponedAlpha > 0.0f) {
                    object.mView.setAlpha(object.mPostponedAlpha);
                }
                if (bl3) {
                    object.mPostponedAlpha = 0.0f;
                    continue;
                }
                object.mPostponedAlpha = -1.0f;
                object.mIsNewlyAdded = false;
            }
        }
    }

    void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            AnimationOrAnimator animationOrAnimator = this.loadAnimation(fragment, fragment.getNextTransition(), fragment.mHidden ^ true, fragment.getNextTransitionStyle());
            if (animationOrAnimator != null && animationOrAnimator.animator != null) {
                animationOrAnimator.animator.setTarget((Object)fragment.mView);
                if (fragment.mHidden) {
                    if (fragment.isHideReplaced()) {
                        fragment.setHideReplaced(false);
                    } else {
                        final ViewGroup viewGroup = fragment.mContainer;
                        final View view = fragment.mView;
                        viewGroup.startViewTransition(view);
                        animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                            public void onAnimationEnd(Animator animator) {
                                viewGroup.endViewTransition(view);
                                animator.removeListener((Animator.AnimatorListener)this);
                                if (fragment.mView != null) {
                                    fragment.mView.setVisibility(8);
                                }
                            }
                        });
                    }
                } else {
                    fragment.mView.setVisibility(0);
                }
                FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
                animationOrAnimator.animator.start();
            } else {
                if (animationOrAnimator != null) {
                    FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
                    fragment.mView.startAnimation(animationOrAnimator.animation);
                    animationOrAnimator.animation.start();
                }
                int n = fragment.mHidden && !fragment.isHideReplaced() ? 8 : 0;
                fragment.mView.setVisibility(n);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void detachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("detach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (fragment.mDetached) return;
        fragment.mDetached = true;
        if (!fragment.mAdded) return;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("remove from detach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        stringBuilder = this.mAdded;
        // MONITORENTER : stringBuilder
        this.mAdded.remove(fragment);
        // MONITOREXIT : stringBuilder
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mAdded = false;
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(2);
    }

    public void dispatchConfigurationChanged(Configuration configuration) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performConfigurationChanged(configuration);
        }
    }

    public boolean dispatchContextItemSelected(MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null || !fragment.performContextItemSelected(menuItem)) continue;
            return true;
        }
        return false;
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(1);
    }

    public boolean dispatchCreateOptionsMenu(Menu object, MenuInflater menuInflater) {
        int n;
        if (this.mCurState < 1) {
            return false;
        }
        boolean bl = false;
        ArrayList<Fragment> arrayList = null;
        for (n = 0; n < this.mAdded.size(); ++n) {
            Fragment fragment = this.mAdded.get(n);
            boolean bl2 = bl;
            ArrayList<Fragment> arrayList2 = arrayList;
            if (fragment != null) {
                bl2 = bl;
                arrayList2 = arrayList;
                if (fragment.performCreateOptionsMenu((Menu)object, menuInflater)) {
                    bl2 = true;
                    arrayList2 = arrayList;
                    if (arrayList == null) {
                        arrayList2 = new ArrayList<Fragment>();
                    }
                    arrayList2.add(fragment);
                }
            }
            bl = bl2;
            arrayList = arrayList2;
        }
        if (this.mCreatedMenus != null) {
            for (n = 0; n < this.mCreatedMenus.size(); ++n) {
                object = this.mCreatedMenus.get(n);
                if (arrayList != null && arrayList.contains(object)) continue;
                object.onDestroyOptionsMenu();
            }
        }
        this.mCreatedMenus = arrayList;
        return bl;
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        this.execPendingActions();
        this.dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }

    public void dispatchDestroyView() {
        this.dispatchStateChange(1);
    }

    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performLowMemory();
        }
    }

    public void dispatchMultiWindowModeChanged(boolean bl) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performMultiWindowModeChanged(bl);
        }
    }

    void dispatchOnFragmentActivityCreated(@NonNull Fragment fragment, @Nullable Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentActivityCreated(fragment, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentActivityCreated(this, fragment, bundle);
        }
    }

    void dispatchOnFragmentAttached(@NonNull Fragment fragment, @NonNull Context context, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentAttached(fragment, context, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentAttached(this, fragment, context);
        }
    }

    void dispatchOnFragmentCreated(@NonNull Fragment fragment, @Nullable Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentCreated(fragment, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentCreated(this, fragment, bundle);
        }
    }

    void dispatchOnFragmentDestroyed(@NonNull Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentDestroyed(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentDestroyed(this, fragment);
        }
    }

    void dispatchOnFragmentDetached(@NonNull Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentDetached(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentDetached(this, fragment);
        }
    }

    void dispatchOnFragmentPaused(@NonNull Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentPaused(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentPaused(this, fragment);
        }
    }

    void dispatchOnFragmentPreAttached(@NonNull Fragment fragment, @NonNull Context context, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentPreAttached(fragment, context, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreAttached(this, fragment, context);
        }
    }

    void dispatchOnFragmentPreCreated(@NonNull Fragment fragment, @Nullable Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentPreCreated(fragment, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreCreated(this, fragment, bundle);
        }
    }

    void dispatchOnFragmentResumed(@NonNull Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentResumed(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentResumed(this, fragment);
        }
    }

    void dispatchOnFragmentSaveInstanceState(@NonNull Fragment fragment, @NonNull Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentSaveInstanceState(this, fragment, bundle);
        }
    }

    void dispatchOnFragmentStarted(@NonNull Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentStarted(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentStarted(this, fragment);
        }
    }

    void dispatchOnFragmentStopped(@NonNull Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentStopped(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentStopped(this, fragment);
        }
    }

    void dispatchOnFragmentViewCreated(@NonNull Fragment fragment, @NonNull View view, @Nullable Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewCreated(this, fragment, view, bundle);
        }
    }

    void dispatchOnFragmentViewDestroyed(@NonNull Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentViewDestroyed(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewDestroyed(this, fragment);
        }
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null || !fragment.performOptionsItemSelected(menuItem)) continue;
            return true;
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState < 1) {
            return;
        }
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performOptionsMenuClosed(menu);
        }
    }

    public void dispatchPause() {
        this.dispatchStateChange(3);
    }

    public void dispatchPictureInPictureModeChanged(boolean bl) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performPictureInPictureModeChanged(bl);
        }
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean bl = false;
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            boolean bl2 = bl;
            if (fragment != null) {
                bl2 = bl;
                if (fragment.performPrepareOptionsMenu(menu)) {
                    bl2 = true;
                }
            }
            bl = bl2;
        }
        return bl;
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(4);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(3);
    }

    public void dispatchStop() {
        this.mStopped = true;
        this.dispatchStateChange(2);
    }

    void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void dump(String string2, FileDescriptor object, PrintWriter printWriter, String[] arrstring) {
        block19 : {
            int n;
            int n2;
            CharSequence charSequence = new StringBuilder();
            charSequence.append(string2);
            charSequence.append("    ");
            charSequence = charSequence.toString();
            Object object2 = this.mActive;
            if (object2 != null && (n = object2.size()) > 0) {
                printWriter.print(string2);
                printWriter.print("Active Fragments in ");
                printWriter.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter.println(":");
                for (n2 = 0; n2 < n; ++n2) {
                    object2 = (Fragment)this.mActive.valueAt(n2);
                    printWriter.print(string2);
                    printWriter.print("  #");
                    printWriter.print(n2);
                    printWriter.print(": ");
                    printWriter.println(object2);
                    if (object2 == null) continue;
                    object2.dump((String)charSequence, (FileDescriptor)object, printWriter, arrstring);
                }
            }
            if ((n = this.mAdded.size()) > 0) {
                printWriter.print(string2);
                printWriter.println("Added Fragments:");
                for (n2 = 0; n2 < n; ++n2) {
                    object2 = this.mAdded.get(n2);
                    printWriter.print(string2);
                    printWriter.print("  #");
                    printWriter.print(n2);
                    printWriter.print(": ");
                    printWriter.println(object2.toString());
                }
            }
            if ((object2 = this.mCreatedMenus) != null && (n = object2.size()) > 0) {
                printWriter.print(string2);
                printWriter.println("Fragments Created Menus:");
                for (n2 = 0; n2 < n; ++n2) {
                    object2 = this.mCreatedMenus.get(n2);
                    printWriter.print(string2);
                    printWriter.print("  #");
                    printWriter.print(n2);
                    printWriter.print(": ");
                    printWriter.println(object2.toString());
                }
            }
            if ((object2 = this.mBackStack) != null && (n = object2.size()) > 0) {
                printWriter.print(string2);
                printWriter.println("Back Stack:");
                for (n2 = 0; n2 < n; ++n2) {
                    object2 = this.mBackStack.get(n2);
                    printWriter.print(string2);
                    printWriter.print("  #");
                    printWriter.print(n2);
                    printWriter.print(": ");
                    printWriter.println(object2.toString());
                    object2.dump((String)charSequence, (FileDescriptor)object, printWriter, arrstring);
                }
            }
            // MONITORENTER : this
            if (this.mBackStackIndices != null && (n = this.mBackStackIndices.size()) > 0) {
                printWriter.print(string2);
                printWriter.println("Back Stack Indices:");
                for (n2 = 0; n2 < n; ++n2) {
                    object = this.mBackStackIndices.get(n2);
                    printWriter.print(string2);
                    printWriter.print("  #");
                    printWriter.print(n2);
                    printWriter.print(": ");
                    printWriter.println(object);
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(string2);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
            // MONITOREXIT : this
            object = this.mPendingActions;
            if (object == null || (n = object.size()) <= 0) break block19;
            printWriter.print(string2);
            printWriter.println("Pending Actions:");
            for (n2 = 0; n2 < n; ++n2) {
                object = this.mPendingActions.get(n2);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n2);
                printWriter.print(": ");
                printWriter.println(object);
            }
        }
        printWriter.print(string2);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(string2);
        printWriter.print("  mHost=");
        printWriter.println(this.mHost);
        printWriter.print(string2);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(string2);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(string2);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mStopped=");
        printWriter.print(this.mStopped);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(string2);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause == null) return;
        printWriter.print(string2);
        printWriter.print("  mNoTransactionsBecause=");
        printWriter.println(this.mNoTransactionsBecause);
        return;
        {
            catch (Throwable throwable) {}
            {
                // MONITOREXIT : this
                throw throwable;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void enqueueAction(OpGenerator opGenerator, boolean bl) {
        if (!bl) {
            this.checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed && this.mHost != null) {
                if (this.mPendingActions == null) {
                    this.mPendingActions = new ArrayList();
                }
                this.mPendingActions.add(opGenerator);
                this.scheduleCommit();
                return;
            }
            if (bl) {
                return;
            }
            throw new IllegalStateException("Activity has been destroyed");
        }
    }

    void ensureInflatedFragmentView(Fragment fragment) {
        if (fragment.mFromLayout && !fragment.mPerformedCreateView) {
            fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
            if (fragment.mView != null) {
                fragment.mInnerView = fragment.mView;
                fragment.mView.setSaveFromParentEnabled(false);
                if (fragment.mHidden) {
                    fragment.mView.setVisibility(8);
                }
                fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
                return;
            }
            fragment.mInnerView = null;
        }
    }

    public boolean execPendingActions() {
        this.ensureExecReady(true);
        boolean bl = false;
        while (this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                bl = true;
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return bl;
    }

    public void execSingleAction(OpGenerator opGenerator, boolean bl) {
        if (bl && (this.mHost == null || this.mDestroyed)) {
            return;
        }
        this.ensureExecReady(bl);
        if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
    }

    @Override
    public boolean executePendingTransactions() {
        boolean bl = this.execPendingActions();
        this.forcePostponedTransactions();
        return bl;
    }

    @Nullable
    @Override
    public Fragment findFragmentById(int n) {
        int n2;
        Object object;
        for (n2 = this.mAdded.size() - 1; n2 >= 0; --n2) {
            object = this.mAdded.get(n2);
            if (object == null || object.mFragmentId != n) continue;
            return object;
        }
        object = this.mActive;
        if (object != null) {
            for (n2 = object.size() - 1; n2 >= 0; --n2) {
                object = (Fragment)this.mActive.valueAt(n2);
                if (object == null || object.mFragmentId != n) continue;
                return object;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Fragment findFragmentByTag(@Nullable String string2) {
        int n;
        Object object;
        if (string2 != null) {
            for (n = this.mAdded.size() - 1; n >= 0; --n) {
                object = this.mAdded.get(n);
                if (object == null || !string2.equals(object.mTag)) continue;
                return object;
            }
        }
        if ((object = this.mActive) != null && string2 != null) {
            for (n = object.size() - 1; n >= 0; --n) {
                object = (Fragment)this.mActive.valueAt(n);
                if (object == null || !string2.equals(object.mTag)) continue;
                return object;
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String string2) {
        Object object = this.mActive;
        if (object != null && string2 != null) {
            for (int i = object.size() - 1; i >= 0; --i) {
                object = (Fragment)this.mActive.valueAt(i);
                if (object == null || (object = object.findFragmentByWho(string2)) == null) continue;
                return object;
            }
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void freeBackStackIndex(int n) {
        synchronized (this) {
            this.mBackStackIndices.set(n, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList();
            }
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Freeing back stack index ");
                stringBuilder.append(n);
                Log.v((String)"FragmentManager", (String)stringBuilder.toString());
            }
            this.mAvailBackStackIndices.add(n);
            return;
        }
    }

    int getActiveFragmentCount() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null) {
            return 0;
        }
        return sparseArray.size();
    }

    List<Fragment> getActiveFragments() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null) {
            return null;
        }
        int n = sparseArray.size();
        sparseArray = new ArrayList(n);
        for (int i = 0; i < n; ++i) {
            sparseArray.add((Fragment)this.mActive.valueAt(i));
        }
        return sparseArray;
    }

    @Override
    public FragmentManager.BackStackEntry getBackStackEntryAt(int n) {
        return this.mBackStack.get(n);
    }

    @Override
    public int getBackStackEntryCount() {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Nullable
    @Override
    public Fragment getFragment(Bundle object, String string2) {
        int n = object.getInt(string2, -1);
        if (n == -1) {
            return null;
        }
        object = (Fragment)this.mActive.get(n);
        if (object == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment no longer exists for key ");
            stringBuilder.append(string2);
            stringBuilder.append(": index ");
            stringBuilder.append(n);
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<Fragment> arrayList = this.mAdded;
        synchronized (arrayList) {
            return (List)this.mAdded.clone();
        }
    }

    LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this;
    }

    @Nullable
    @Override
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hide: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
        }
    }

    @Override
    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    boolean isStateAtLeast(int n) {
        if (this.mCurState >= n) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isStateSaved() {
        if (!this.mStateSaved && !this.mStopped) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    AnimationOrAnimator loadAnimation(Fragment object, int n, boolean bl, int n2) {
        int n3 = object.getNextAnim();
        Animation animation = object.onCreateAnimation(n, bl, n3);
        if (animation != null) {
            return new AnimationOrAnimator(animation);
        }
        if ((object = object.onCreateAnimator(n, bl, n3)) != null) {
            return new AnimationOrAnimator((Animator)object);
        }
        if (n3 != 0) {
            boolean bl2;
            boolean bl3 = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(n3));
            boolean bl4 = bl2 = false;
            if (bl3) {
                try {
                    object = AnimationUtils.loadAnimation((Context)this.mHost.getContext(), (int)n3);
                    if (object != null) {
                        object = new AnimationOrAnimator((Animation)object);
                        return object;
                    }
                    bl4 = true;
                }
                catch (RuntimeException runtimeException) {
                    bl4 = bl2;
                }
                catch (Resources.NotFoundException notFoundException) {
                    throw notFoundException;
                }
            }
            if (!bl4) {
                try {
                    object = AnimatorInflater.loadAnimator((Context)this.mHost.getContext(), (int)n3);
                    if (object != null) {
                        object = new AnimationOrAnimator((Animator)object);
                        return object;
                    }
                }
                catch (RuntimeException runtimeException) {
                    if (!bl3) {
                        Animation animation2 = AnimationUtils.loadAnimation((Context)this.mHost.getContext(), (int)n3);
                        if (animation2 != null) {
                            return new AnimationOrAnimator(animation2);
                        }
                    }
                    throw runtimeException;
                }
            }
        }
        if (n == 0) {
            return null;
        }
        if ((n = FragmentManagerImpl.transitToStyleIndex(n, bl)) < 0) {
            return null;
        }
        switch (n) {
            default: {
                n = n2;
                if (n2 != 0) break;
                n = n2;
                if (!this.mHost.onHasWindowAnimations()) break;
                n = this.mHost.onGetWindowAnimations();
                break;
            }
            case 6: {
                return FragmentManagerImpl.makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            }
            case 5: {
                return FragmentManagerImpl.makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            }
            case 4: {
                return FragmentManagerImpl.makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            }
            case 3: {
                return FragmentManagerImpl.makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            }
            case 2: {
                return FragmentManagerImpl.makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            }
            case 1: {
                return FragmentManagerImpl.makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            }
        }
        if (n == 0) {
            return null;
        }
        return null;
    }

    void makeActive(Fragment fragment) {
        if (fragment.mIndex >= 0) {
            return;
        }
        int n = this.mNextFragmentIndex;
        this.mNextFragmentIndex = n + 1;
        fragment.setIndex(n, this.mParent);
        if (this.mActive == null) {
            this.mActive = new SparseArray();
        }
        this.mActive.put(fragment.mIndex, (Object)fragment);
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Allocated fragment index ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
    }

    void makeInactive(Fragment fragment) {
        if (fragment.mIndex < 0) {
            return;
        }
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Freeing fragment index ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.mActive.put(fragment.mIndex, (Object)null);
        fragment.initState();
    }

    void moveFragmentToExpectedState(Fragment fragment) {
        int n;
        if (fragment == null) {
            return;
        }
        int n2 = n = this.mCurState;
        if (fragment.mRemoving) {
            n2 = fragment.isInBackStack() ? Math.min(n, 1) : Math.min(n, 0);
        }
        this.moveToState(fragment, n2, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
        if (fragment.mView != null) {
            Object object = this.findFragmentUnder(fragment);
            if (object != null) {
                object = object.mView;
                ViewGroup viewGroup = fragment.mContainer;
                n2 = viewGroup.indexOfChild((View)object);
                n = viewGroup.indexOfChild(fragment.mView);
                if (n < n2) {
                    viewGroup.removeViewAt(n);
                    viewGroup.addView(fragment.mView, n2);
                }
            }
            if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                if (fragment.mPostponedAlpha > 0.0f) {
                    fragment.mView.setAlpha(fragment.mPostponedAlpha);
                }
                fragment.mPostponedAlpha = 0.0f;
                fragment.mIsNewlyAdded = false;
                object = this.loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                if (object != null) {
                    FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(fragment.mView, (AnimationOrAnimator)object);
                    if (object.animation != null) {
                        fragment.mView.startAnimation(object.animation);
                    } else {
                        object.animator.setTarget((Object)fragment.mView);
                        object.animator.start();
                    }
                }
            }
        }
        if (fragment.mHiddenChanged) {
            this.completeShowHideFragment(fragment);
        }
    }

    void moveToState(int n, boolean bl) {
        if (this.mHost == null && n != 0) {
            throw new IllegalStateException("No activity");
        }
        if (!bl && n == this.mCurState) {
            return;
        }
        this.mCurState = n;
        if (this.mActive != null) {
            Object object;
            int n2 = this.mAdded.size();
            for (n = 0; n < n2; ++n) {
                this.moveFragmentToExpectedState(this.mAdded.get(n));
            }
            n2 = this.mActive.size();
            for (n = 0; n < n2; ++n) {
                object = (Fragment)this.mActive.valueAt(n);
                if (object == null || !object.mRemoving && !object.mDetached || object.mIsNewlyAdded) continue;
                this.moveFragmentToExpectedState((Fragment)object);
            }
            this.startPendingDeferredFragments();
            if (this.mNeedMenuInvalidate && (object = this.mHost) != null && this.mCurState == 4) {
                object.onSupportInvalidateOptionsMenu();
                this.mNeedMenuInvalidate = false;
            }
        }
    }

    void moveToState(Fragment fragment) {
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    void moveToState(Fragment var1_1, int var2_2, int var3_3, int var4_4, boolean var5_5) {
        var8_6 = var1_1.mAdded;
        var7_7 = true;
        if (!var8_6 || var1_1.mDetached) {
            var2_2 = var6_8 = var2_2;
            if (var6_8 > 1) {
                var2_2 = 1;
            }
        }
        var6_8 = var2_2;
        if (var1_1.mRemoving) {
            var6_8 = var2_2;
            if (var2_2 > var1_1.mState) {
                var6_8 = var1_1.mState == 0 && var1_1.isInBackStack() != false ? 1 : var1_1.mState;
            }
        }
        var2_2 = var6_8;
        if (var1_1.mDeferStart) {
            var2_2 = var6_8;
            if (var1_1.mState < 3) {
                var2_2 = var6_8;
                if (var6_8 > 2) {
                    var2_2 = 2;
                }
            }
        }
        if (var1_1.mState > var2_2) ** GOTO lbl171
        if (var1_1.mFromLayout && !var1_1.mInLayout) {
            return;
        }
        if (var1_1.getAnimatingAway() != null || var1_1.getAnimator() != null) {
            var1_1.setAnimatingAway(null);
            var1_1.setAnimator(null);
            this.moveToState(var1_1, var1_1.getStateAfterAnimating(), 0, 0, true);
        }
        if ((var6_8 = var1_1.mState) == 0) ** GOTO lbl33
        if (var6_8 == 1) ** GOTO lbl95
        var4_4 = var2_2;
        if (var6_8 == 2) ** GOTO lbl147
        var3_3 = var2_2;
        if (var6_8 == 3) ** GOTO lbl157
        ** GOTO lbl169
lbl33: // 1 sources:
        if (var2_2 > 0) {
            if (FragmentManagerImpl.DEBUG) {
                var9_9 = new StringBuilder();
                var9_9.append("moveto CREATED: ");
                var9_9.append(var1_1);
                Log.v((String)"FragmentManager", (String)var9_9.toString());
            }
            var3_3 = var2_2;
            if (var1_1.mSavedFragmentState != null) {
                var1_1.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                var1_1.mSavedViewState = var1_1.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                var1_1.mTarget = this.getFragment(var1_1.mSavedFragmentState, "android:target_state");
                if (var1_1.mTarget != null) {
                    var1_1.mTargetRequestCode = var1_1.mSavedFragmentState.getInt("android:target_req_state", 0);
                }
                if (var1_1.mSavedUserVisibleHint != null) {
                    var1_1.mUserVisibleHint = var1_1.mSavedUserVisibleHint;
                    var1_1.mSavedUserVisibleHint = null;
                } else {
                    var1_1.mUserVisibleHint = var1_1.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                }
                var3_3 = var2_2;
                if (!var1_1.mUserVisibleHint) {
                    var1_1.mDeferStart = true;
                    var3_3 = var2_2;
                    if (var2_2 > 2) {
                        var3_3 = 2;
                    }
                }
            }
            var1_1.mHost = var9_9 = this.mHost;
            var1_1.mParentFragment = var10_11 = this.mParent;
            var9_9 = var10_11 != null ? var10_11.mChildFragmentManager : var9_9.getFragmentManagerImpl();
            var1_1.mFragmentManager = var9_9;
            if (var1_1.mTarget != null) {
                if (this.mActive.get(var1_1.mTarget.mIndex) != var1_1.mTarget) {
                    var9_9 = new StringBuilder();
                    var9_9.append("Fragment ");
                    var9_9.append(var1_1);
                    var9_9.append(" declared target fragment ");
                    var9_9.append(var1_1.mTarget);
                    var9_9.append(" that does not belong to this FragmentManager!");
                    throw new IllegalStateException(var9_9.toString());
                }
                if (var1_1.mTarget.mState < 1) {
                    this.moveToState(var1_1.mTarget, 1, 0, 0, true);
                }
            }
            this.dispatchOnFragmentPreAttached(var1_1, this.mHost.getContext(), false);
            var1_1.mCalled = false;
            var1_1.onAttach(this.mHost.getContext());
            if (!var1_1.mCalled) {
                var9_9 = new StringBuilder();
                var9_9.append("Fragment ");
                var9_9.append(var1_1);
                var9_9.append(" did not call through to super.onAttach()");
                throw new SuperNotCalledException(var9_9.toString());
            }
            if (var1_1.mParentFragment == null) {
                this.mHost.onAttachFragment(var1_1);
            } else {
                var1_1.mParentFragment.onAttachFragment(var1_1);
            }
            this.dispatchOnFragmentAttached(var1_1, this.mHost.getContext(), false);
            if (!var1_1.mIsCreated) {
                this.dispatchOnFragmentPreCreated(var1_1, var1_1.mSavedFragmentState, false);
                var1_1.performCreate(var1_1.mSavedFragmentState);
                this.dispatchOnFragmentCreated(var1_1, var1_1.mSavedFragmentState, false);
            } else {
                var1_1.restoreChildFragmentState(var1_1.mSavedFragmentState);
                var1_1.mState = 1;
            }
            var1_1.mRetaining = false;
            var2_2 = var3_3;
        }
lbl95: // 4 sources:
        this.ensureInflatedFragmentView(var1_1);
        if (var2_2 > 1) {
            if (FragmentManagerImpl.DEBUG) {
                var9_9 = new StringBuilder();
                var9_9.append("moveto ACTIVITY_CREATED: ");
                var9_9.append(var1_1);
                Log.v((String)"FragmentManager", (String)var9_9.toString());
            }
            if (!var1_1.mFromLayout) {
                var9_9 = null;
                if (var1_1.mContainerId != 0) {
                    if (var1_1.mContainerId == -1) {
                        var9_9 = new StringBuilder();
                        var9_9.append("Cannot create fragment ");
                        var9_9.append(var1_1);
                        var9_9.append(" for a container view with no id");
                        this.throwException(new IllegalArgumentException(var9_9.toString()));
                    }
                    if ((var10_11 = (ViewGroup)this.mContainer.onFindViewById(var1_1.mContainerId)) == null && !var1_1.mRestored) {
                        try {
                            var9_9 = var1_1.getResources().getResourceName(var1_1.mContainerId);
                        }
                        catch (Resources.NotFoundException var9_10) {
                            var9_9 = "unknown";
                        }
                        var11_12 = new StringBuilder();
                        var11_12.append("No view found for id 0x");
                        var11_12.append(Integer.toHexString(var1_1.mContainerId));
                        var11_12.append(" (");
                        var11_12.append((String)var9_9);
                        var11_12.append(") for fragment ");
                        var11_12.append(var1_1);
                        this.throwException(new IllegalArgumentException(var11_12.toString()));
                    }
                    var9_9 = var10_11;
                }
                var1_1.mContainer = var9_9;
                var1_1.performCreateView(var1_1.performGetLayoutInflater(var1_1.mSavedFragmentState), (ViewGroup)var9_9, var1_1.mSavedFragmentState);
                if (var1_1.mView != null) {
                    var1_1.mInnerView = var1_1.mView;
                    var1_1.mView.setSaveFromParentEnabled(false);
                    if (var9_9 != null) {
                        var9_9.addView(var1_1.mView);
                    }
                    if (var1_1.mHidden) {
                        var1_1.mView.setVisibility(8);
                    }
                    var1_1.onViewCreated(var1_1.mView, var1_1.mSavedFragmentState);
                    this.dispatchOnFragmentViewCreated(var1_1, var1_1.mView, var1_1.mSavedFragmentState, false);
                    var5_5 = var1_1.mView.getVisibility() == 0 && var1_1.mContainer != null ? var7_7 : false;
                    var1_1.mIsNewlyAdded = var5_5;
                } else {
                    var1_1.mInnerView = null;
                }
            }
            var1_1.performActivityCreated(var1_1.mSavedFragmentState);
            this.dispatchOnFragmentActivityCreated(var1_1, var1_1.mSavedFragmentState, false);
            if (var1_1.mView != null) {
                var1_1.restoreViewState(var1_1.mSavedFragmentState);
            }
            var1_1.mSavedFragmentState = null;
        }
        var4_4 = var2_2;
lbl147: // 2 sources:
        var3_3 = var4_4;
        if (var4_4 > 2) {
            if (FragmentManagerImpl.DEBUG) {
                var9_9 = new StringBuilder();
                var9_9.append("moveto STARTED: ");
                var9_9.append(var1_1);
                Log.v((String)"FragmentManager", (String)var9_9.toString());
            }
            var1_1.performStart();
            this.dispatchOnFragmentStarted(var1_1, false);
            var3_3 = var4_4;
        }
lbl157: // 4 sources:
        var2_2 = var3_3;
        if (var3_3 > 3) {
            if (FragmentManagerImpl.DEBUG) {
                var9_9 = new StringBuilder();
                var9_9.append("moveto RESUMED: ");
                var9_9.append(var1_1);
                Log.v((String)"FragmentManager", (String)var9_9.toString());
            }
            var1_1.performResume();
            this.dispatchOnFragmentResumed(var1_1, false);
            var1_1.mSavedFragmentState = null;
            var1_1.mSavedViewState = null;
            var2_2 = var3_3;
        }
lbl169: // 4 sources:
        var3_3 = var2_2;
        ** GOTO lbl261
lbl171: // 1 sources:
        if (var1_1.mState <= var2_2) ** GOTO lbl260
        var6_8 = var1_1.mState;
        if (var6_8 == 1) ** GOTO lbl221
        if (var6_8 == 2) ** GOTO lbl195
        if (var6_8 == 3) ** GOTO lbl187
        if (var6_8 != 4) {
            var3_3 = var2_2;
        } else {
            if (var2_2 < 4) {
                if (FragmentManagerImpl.DEBUG) {
                    var9_9 = new StringBuilder();
                    var9_9.append("movefrom RESUMED: ");
                    var9_9.append(var1_1);
                    Log.v((String)"FragmentManager", (String)var9_9.toString());
                }
                var1_1.performPause();
                this.dispatchOnFragmentPaused(var1_1, false);
            }
lbl187: // 4 sources:
            if (var2_2 < 3) {
                if (FragmentManagerImpl.DEBUG) {
                    var9_9 = new StringBuilder();
                    var9_9.append("movefrom STARTED: ");
                    var9_9.append(var1_1);
                    Log.v((String)"FragmentManager", (String)var9_9.toString());
                }
                var1_1.performStop();
                this.dispatchOnFragmentStopped(var1_1, false);
            }
lbl195: // 4 sources:
            if (var2_2 < 2) {
                if (FragmentManagerImpl.DEBUG) {
                    var9_9 = new StringBuilder();
                    var9_9.append("movefrom ACTIVITY_CREATED: ");
                    var9_9.append(var1_1);
                    Log.v((String)"FragmentManager", (String)var9_9.toString());
                }
                if (var1_1.mView != null && this.mHost.onShouldSaveFragmentState(var1_1) && var1_1.mSavedViewState == null) {
                    this.saveFragmentViewState(var1_1);
                }
                var1_1.performDestroyView();
                this.dispatchOnFragmentViewDestroyed(var1_1, false);
                if (var1_1.mView != null && var1_1.mContainer != null) {
                    var1_1.mContainer.endViewTransition(var1_1.mView);
                    var1_1.mView.clearAnimation();
                    var9_9 = null;
                    if (this.mCurState > 0 && !this.mDestroyed && var1_1.mView.getVisibility() == 0 && var1_1.mPostponedAlpha >= 0.0f) {
                        var9_9 = this.loadAnimation(var1_1, var3_3, false, var4_4);
                    }
                    var1_1.mPostponedAlpha = 0.0f;
                    if (var9_9 != null) {
                        this.animateRemoveFragment(var1_1, (AnimationOrAnimator)var9_9, var2_2);
                    }
                    var1_1.mContainer.removeView(var1_1.mView);
                }
                var1_1.mContainer = null;
                var1_1.mView = null;
                var1_1.mViewLifecycleOwner = null;
                var1_1.mViewLifecycleOwnerLiveData.setValue(null);
                var1_1.mInnerView = null;
                var1_1.mInLayout = false;
            }
lbl221: // 4 sources:
            var3_3 = var2_2;
            if (var2_2 < 1) {
                if (this.mDestroyed) {
                    if (var1_1.getAnimatingAway() != null) {
                        var9_9 = var1_1.getAnimatingAway();
                        var1_1.setAnimatingAway(null);
                        var9_9.clearAnimation();
                    } else if (var1_1.getAnimator() != null) {
                        var9_9 = var1_1.getAnimator();
                        var1_1.setAnimator(null);
                        var9_9.cancel();
                    }
                }
                if (var1_1.getAnimatingAway() == null && var1_1.getAnimator() == null) {
                    if (FragmentManagerImpl.DEBUG) {
                        var9_9 = new StringBuilder();
                        var9_9.append("movefrom CREATED: ");
                        var9_9.append(var1_1);
                        Log.v((String)"FragmentManager", (String)var9_9.toString());
                    }
                    if (!var1_1.mRetaining) {
                        var1_1.performDestroy();
                        this.dispatchOnFragmentDestroyed(var1_1, false);
                    } else {
                        var1_1.mState = 0;
                    }
                    var1_1.performDetach();
                    this.dispatchOnFragmentDetached(var1_1, false);
                    var3_3 = var2_2;
                    if (!var5_5) {
                        if (!var1_1.mRetaining) {
                            this.makeInactive(var1_1);
                            var3_3 = var2_2;
                        } else {
                            var1_1.mHost = null;
                            var1_1.mParentFragment = null;
                            var1_1.mFragmentManager = null;
                            var3_3 = var2_2;
                        }
                    }
                } else {
                    var1_1.setStateAfterAnimating(var2_2);
                    var3_3 = 1;
                }
            }
        }
        ** GOTO lbl261
lbl260: // 1 sources:
        var3_3 = var2_2;
lbl261: // 6 sources:
        if (var1_1.mState == var3_3) return;
        var9_9 = new StringBuilder();
        var9_9.append("moveToState: Fragment state for ");
        var9_9.append(var1_1);
        var9_9.append(" not updated inline; ");
        var9_9.append("expected state ");
        var9_9.append(var3_3);
        var9_9.append(" found ");
        var9_9.append(var1_1.mState);
        Log.w((String)"FragmentManager", (String)var9_9.toString());
        var1_1.mState = var3_3;
    }

    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        this.mStopped = false;
        int n = this.mAdded.size();
        for (int i = 0; i < n; ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.noteStateNotSaved();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public View onCreateView(View object, String object2, Context context, AttributeSet attributeSet) {
        if (!"fragment".equals(object2)) {
            return null;
        }
        String string2 = attributeSet.getAttributeValue(null, "class");
        object2 = context.obtainStyledAttributes(attributeSet, FragmentTag.Fragment);
        int n = 0;
        if (string2 == null) {
            string2 = object2.getString(0);
        }
        int n2 = object2.getResourceId(1, -1);
        String string3 = object2.getString(2);
        object2.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), string2)) {
            return null;
        }
        if (object != null) {
            n = object.getId();
        }
        if (n == -1 && n2 == -1 && string3 == null) {
            object = new StringBuilder();
            object.append(attributeSet.getPositionDescription());
            object.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            object.append(string2);
            throw new IllegalArgumentException(object.toString());
        }
        object2 = n2 != -1 ? this.findFragmentById(n2) : null;
        object = object2;
        if (object2 == null) {
            object = object2;
            if (string3 != null) {
                object = this.findFragmentByTag(string3);
            }
        }
        object2 = object;
        if (object == null) {
            object2 = object;
            if (n != -1) {
                object2 = this.findFragmentById(n);
            }
        }
        if (DEBUG) {
            object = new StringBuilder();
            object.append("onCreateView: id=0x");
            object.append(Integer.toHexString(n2));
            object.append(" fname=");
            object.append(string2);
            object.append(" existing=");
            object.append(object2);
            Log.v((String)"FragmentManager", (String)object.toString());
        }
        if (object2 == null) {
            object = this.mContainer.instantiate(context, string2, null);
            object.mFromLayout = true;
            int n3 = n2 != 0 ? n2 : n;
            object.mFragmentId = n3;
            object.mContainerId = n;
            object.mTag = string3;
            object.mInLayout = true;
            object.mFragmentManager = this;
            object.mHost = object2 = this.mHost;
            object.onInflate(object2.getContext(), attributeSet, object.mSavedFragmentState);
            this.addFragment((Fragment)object, true);
        } else {
            if (object2.mInLayout) {
                object = new StringBuilder();
                object.append(attributeSet.getPositionDescription());
                object.append(": Duplicate id 0x");
                object.append(Integer.toHexString(n2));
                object.append(", tag ");
                object.append(string3);
                object.append(", or parent id 0x");
                object.append(Integer.toHexString(n));
                object.append(" with another fragment for ");
                object.append(string2);
                throw new IllegalArgumentException(object.toString());
            }
            object2.mInLayout = true;
            object2.mHost = this.mHost;
            if (!object2.mRetaining) {
                object2.onInflate(this.mHost.getContext(), attributeSet, object2.mSavedFragmentState);
            }
            object = object2;
        }
        if (this.mCurState < 1 && object.mFromLayout) {
            this.moveToState((Fragment)object, 1, 0, 0, false);
        } else {
            this.moveToState((Fragment)object);
        }
        if (object.mView == null) {
            object = new StringBuilder();
            object.append("Fragment ");
            object.append(string2);
            object.append(" did not create a view.");
            throw new IllegalStateException(object.toString());
        }
        if (n2 != 0) {
            object.mView.setId(n2);
        }
        if (object.mView.getTag() == null) {
            object.mView.setTag((Object)string3);
        }
        return object.mView;
    }

    public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
        return this.onCreateView(null, string2, context, attributeSet);
    }

    public void performPendingDeferredStart(Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            this.moveToState(fragment, this.mCurState, 0, 0, false);
        }
    }

    @Override
    public void popBackStack() {
        this.enqueueAction(new PopBackStackState(null, -1, 0), false);
    }

    @Override
    public void popBackStack(int n, int n2) {
        if (n >= 0) {
            this.enqueueAction(new PopBackStackState(null, n, n2), false);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Override
    public void popBackStack(@Nullable String string2, int n) {
        this.enqueueAction(new PopBackStackState(string2, -1, n), false);
    }

    @Override
    public boolean popBackStackImmediate() {
        this.checkStateLoss();
        return this.popBackStackImmediate(null, -1, 0);
    }

    @Override
    public boolean popBackStackImmediate(int n, int n2) {
        this.checkStateLoss();
        this.execPendingActions();
        if (n >= 0) {
            return this.popBackStackImmediate(null, n, n2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Override
    public boolean popBackStackImmediate(@Nullable String string2, int n) {
        this.checkStateLoss();
        return this.popBackStackImmediate(string2, -1, n);
    }

    boolean popBackStackState(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, String string2, int n, int n2) {
        ArrayList<BackStackRecord> arrayList3 = this.mBackStack;
        if (arrayList3 == null) {
            return false;
        }
        if (string2 == null && n < 0 && (n2 & 1) == 0) {
            n = arrayList3.size() - 1;
            if (n < 0) {
                return false;
            }
            arrayList.add(this.mBackStack.remove(n));
            arrayList2.add(true);
            return true;
        }
        int n3 = -1;
        if (string2 != null || n >= 0) {
            int n4;
            for (n4 = this.mBackStack.size() - 1; n4 >= 0; --n4) {
                arrayList3 = this.mBackStack.get(n4);
                if (string2 != null && string2.equals(arrayList3.getName()) || n >= 0 && n == arrayList3.mIndex) break;
            }
            if (n4 < 0) {
                return false;
            }
            n3 = n4;
            if ((n2 & 1) != 0) {
                n2 = n4 - 1;
                do {
                    n3 = n2;
                    if (n2 < 0) break;
                    arrayList3 = this.mBackStack.get(n2);
                    if (string2 == null || !string2.equals(arrayList3.getName())) {
                        n3 = n2;
                        if (n < 0) break;
                        n3 = n2;
                        if (n != arrayList3.mIndex) break;
                    }
                    --n2;
                } while (true);
            }
        }
        if (n3 == this.mBackStack.size() - 1) {
            return false;
        }
        for (n = this.mBackStack.size() - 1; n > n3; --n) {
            arrayList.add(this.mBackStack.remove(n));
            arrayList2.add(true);
        }
        return true;
    }

    @Override
    public void putFragment(Bundle bundle, String string2, Fragment fragment) {
        if (fragment.mIndex < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        bundle.putInt(string2, fragment.mIndex);
    }

    @Override
    public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean bl) {
        this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(fragmentLifecycleCallbacks, bl));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void removeFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("remove: ");
            stringBuilder.append(fragment);
            stringBuilder.append(" nesting=");
            stringBuilder.append(fragment.mBackStackNesting);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        boolean bl = fragment.isInBackStack();
        if (fragment.mDetached) {
            if (!(bl ^ true)) return;
        }
        stringBuilder = this.mAdded;
        // MONITORENTER : stringBuilder
        this.mAdded.remove(fragment);
        // MONITOREXIT : stringBuilder
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mAdded = false;
        fragment.mRemoving = true;
    }

    @Override
    public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        ArrayList<FragmentManager.OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(onBackStackChangedListener);
        }
    }

    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void restoreAllState(Parcelable object, FragmentManagerNonConfig object2) {
        Object object3;
        Object object4;
        Object object5;
        int n;
        int n2;
        Object object6;
        if (object == null) {
            return;
        }
        FragmentManagerState fragmentManagerState = (FragmentManagerState)object;
        if (fragmentManagerState.mActive == null) {
            return;
        }
        if (object2 == null) {
            object6 = null;
            object = null;
        } else {
            object5 = object2.getFragments();
            object = object2.getChildNonConfigs();
            object6 = object2.getViewModelStores();
            n2 = object5 != null ? object5.size() : 0;
            for (n = 0; n < n2; ++n) {
                int n3;
                object3 = object5.get(n);
                if (DEBUG) {
                    object4 = new StringBuilder();
                    object4.append("restoreAllState: re-attaching retained ");
                    object4.append(object3);
                    Log.v((String)"FragmentManager", (String)object4.toString());
                }
                for (n3 = 0; n3 < fragmentManagerState.mActive.length && fragmentManagerState.mActive[n3].mIndex != object3.mIndex; ++n3) {
                }
                if (n3 == fragmentManagerState.mActive.length) {
                    object4 = new StringBuilder();
                    object4.append("Could not find active fragment with index ");
                    object4.append(object3.mIndex);
                    this.throwException(new IllegalStateException(object4.toString()));
                }
                object4 = fragmentManagerState.mActive[n3];
                object4.mInstance = object3;
                object3.mSavedViewState = null;
                object3.mBackStackNesting = 0;
                object3.mInLayout = false;
                object3.mAdded = false;
                object3.mTarget = null;
                if (object4.mSavedFragmentState == null) continue;
                object4.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                object3.mSavedViewState = object4.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                object3.mSavedFragmentState = object4.mSavedFragmentState;
            }
        }
        this.mActive = new SparseArray(fragmentManagerState.mActive.length);
        for (n2 = 0; n2 < fragmentManagerState.mActive.length; ++n2) {
            object4 = fragmentManagerState.mActive[n2];
            if (object4 == null) continue;
            object5 = object != null && n2 < object.size() ? (FragmentManagerNonConfig)((Object)object.get(n2)) : null;
            object3 = object6 != null && n2 < object6.size() ? object6.get(n2) : null;
            object5 = object4.instantiate(this.mHost, this.mContainer, this.mParent, (FragmentManagerNonConfig)object5, (ViewModelStore)object3);
            if (DEBUG) {
                object3 = new StringBuilder();
                object3.append("restoreAllState: active #");
                object3.append(n2);
                object3.append(": ");
                object3.append(object5);
                Log.v((String)"FragmentManager", (String)object3.toString());
            }
            this.mActive.put(object5.mIndex, object5);
            object4.mInstance = null;
        }
        if (object2 != null) {
            object = object2.getFragments();
            n2 = object != null ? object.size() : 0;
            for (n = 0; n < n2; ++n) {
                object2 = object.get(n);
                if (object2.mTargetIndex < 0) continue;
                object2.mTarget = (Fragment)this.mActive.get(object2.mTargetIndex);
                if (object2.mTarget != null) continue;
                object6 = new StringBuilder();
                object6.append("Re-attaching retained fragment ");
                object6.append(object2);
                object6.append(" target no longer exists: ");
                object6.append(object2.mTargetIndex);
                Log.w((String)"FragmentManager", (String)object6.toString());
            }
        }
        this.mAdded.clear();
        if (fragmentManagerState.mAdded != null) {
            for (n2 = 0; n2 < fragmentManagerState.mAdded.length; ++n2) {
                object = (Fragment)this.mActive.get(fragmentManagerState.mAdded[n2]);
                if (object == null) {
                    object2 = new StringBuilder();
                    object2.append("No instantiated fragment for index #");
                    object2.append(fragmentManagerState.mAdded[n2]);
                    this.throwException(new IllegalStateException(object2.toString()));
                }
                object.mAdded = true;
                if (DEBUG) {
                    object2 = new StringBuilder();
                    object2.append("restoreAllState: added #");
                    object2.append(n2);
                    object2.append(": ");
                    object2.append(object);
                    Log.v((String)"FragmentManager", (String)object2.toString());
                }
                if (this.mAdded.contains(object)) {
                    throw new IllegalStateException("Already added!");
                }
                object2 = this.mAdded;
                synchronized (object2) {
                    this.mAdded.add((Fragment)object);
                    continue;
                }
            }
        }
        if (fragmentManagerState.mBackStack != null) {
            this.mBackStack = new ArrayList(fragmentManagerState.mBackStack.length);
            for (n2 = 0; n2 < fragmentManagerState.mBackStack.length; ++n2) {
                object = fragmentManagerState.mBackStack[n2].instantiate(this);
                if (DEBUG) {
                    object2 = new StringBuilder();
                    object2.append("restoreAllState: back stack #");
                    object2.append(n2);
                    object2.append(" (index ");
                    object2.append(object.mIndex);
                    object2.append("): ");
                    object2.append(object);
                    Log.v((String)"FragmentManager", (String)object2.toString());
                    object2 = new PrintWriter(new LogWriter("FragmentManager"));
                    object.dump("  ", (PrintWriter)object2, false);
                    object2.close();
                }
                this.mBackStack.add((BackStackRecord)object);
                if (object.mIndex < 0) continue;
                this.setBackStackIndex(object.mIndex, (BackStackRecord)object);
            }
        } else {
            this.mBackStack = null;
        }
        if (fragmentManagerState.mPrimaryNavActiveIndex >= 0) {
            this.mPrimaryNav = (Fragment)this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex);
        }
        this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
    }

    FragmentManagerNonConfig retainNonConfig() {
        FragmentManagerImpl.setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }

    Parcelable saveAllState() {
        this.forcePostponedTransactions();
        this.endAnimatingAwayFragments();
        this.execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        Object object = this.mActive;
        if (object != null) {
            int n;
            Object object2;
            Object object3;
            if (object.size() <= 0) {
                return null;
            }
            int n2 = this.mActive.size();
            FragmentState[] arrfragmentState = new FragmentState[n2];
            int n3 = 0;
            for (n = 0; n < n2; ++n) {
                object = (Fragment)this.mActive.valueAt(n);
                if (object == null) continue;
                if (object.mIndex < 0) {
                    object2 = new int[]();
                    object2.append("Failure saving state: active ");
                    object2.append(object);
                    object2.append(" has cleared index: ");
                    object2.append(object.mIndex);
                    this.throwException(new IllegalStateException(object2.toString()));
                }
                int n4 = 1;
                arrfragmentState[n] = object2 = new FragmentState((Fragment)object);
                if (object.mState > 0 && object2.mSavedFragmentState == null) {
                    object2.mSavedFragmentState = this.saveFragmentBasicState((Fragment)object);
                    if (object.mTarget != null) {
                        if (object.mTarget.mIndex < 0) {
                            object3 = new StringBuilder();
                            object3.append("Failure saving state: ");
                            object3.append(object);
                            object3.append(" has target not in fragment manager: ");
                            object3.append(object.mTarget);
                            this.throwException(new IllegalStateException(object3.toString()));
                        }
                        if (object2.mSavedFragmentState == null) {
                            object2.mSavedFragmentState = new Bundle();
                        }
                        this.putFragment(object2.mSavedFragmentState, "android:target_state", object.mTarget);
                        if (object.mTargetRequestCode != 0) {
                            object2.mSavedFragmentState.putInt("android:target_req_state", object.mTargetRequestCode);
                        }
                    }
                } else {
                    object2.mSavedFragmentState = object.mSavedFragmentState;
                }
                n3 = n4;
                if (!DEBUG) continue;
                object3 = new StringBuilder();
                object3.append("Saved state of ");
                object3.append(object);
                object3.append(": ");
                object3.append((Object)object2.mSavedFragmentState);
                Log.v((String)"FragmentManager", (String)object3.toString());
                n3 = n4;
            }
            if (n3 == 0) {
                if (DEBUG) {
                    Log.v((String)"FragmentManager", (String)"saveAllState: no fragments!");
                }
                return null;
            }
            object = null;
            object3 = null;
            n3 = this.mAdded.size();
            if (n3 > 0) {
                object2 = new int[n3];
                n = 0;
                do {
                    object = object2;
                    if (n >= n3) break;
                    object2[n] = this.mAdded.get((int)n).mIndex;
                    if (object2[n] < 0) {
                        object = new StringBuilder();
                        object.append("Failure saving state: active ");
                        object.append(this.mAdded.get(n));
                        object.append(" has cleared index: ");
                        object.append((int)object2[n]);
                        this.throwException(new IllegalStateException(object.toString()));
                    }
                    if (DEBUG) {
                        object = new StringBuilder();
                        object.append("saveAllState: adding fragment #");
                        object.append(n);
                        object.append(": ");
                        object.append(this.mAdded.get(n));
                        Log.v((String)"FragmentManager", (String)object.toString());
                    }
                    ++n;
                } while (true);
            }
            ArrayList<BackStackRecord> arrayList = this.mBackStack;
            object2 = object3;
            if (arrayList != null) {
                n3 = arrayList.size();
                object2 = object3;
                if (n3 > 0) {
                    object3 = new BackStackState[n3];
                    n = 0;
                    do {
                        object2 = object3;
                        if (n >= n3) break;
                        object3[n] = new BackStackState(this.mBackStack.get(n));
                        if (DEBUG) {
                            object2 = new StringBuilder();
                            object2.append("saveAllState: adding back stack #");
                            object2.append(n);
                            object2.append(": ");
                            object2.append(this.mBackStack.get(n));
                            Log.v((String)"FragmentManager", (String)object2.toString());
                        }
                        ++n;
                    } while (true);
                }
            }
            object3 = new BackStackState[]();
            object3.mActive = arrfragmentState;
            object3.mAdded = object;
            object3.mBackStack = object2;
            object = this.mPrimaryNav;
            if (object != null) {
                object3.mPrimaryNavActiveIndex = object.mIndex;
            }
            object3.mNextFragmentIndex = this.mNextFragmentIndex;
            this.saveNonConfig();
            return object3;
        }
        return null;
    }

    Bundle saveFragmentBasicState(Fragment fragment) {
        Bundle bundle = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        this.dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            bundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment.mView != null) {
            this.saveFragmentViewState(fragment);
        }
        Bundle bundle2 = bundle;
        if (fragment.mSavedViewState != null) {
            bundle2 = bundle;
            if (bundle == null) {
                bundle2 = new Bundle();
            }
            bundle2.putSparseParcelableArray("android:view_state", fragment.mSavedViewState);
        }
        bundle = bundle2;
        if (!fragment.mUserVisibleHint) {
            bundle = bundle2;
            if (bundle2 == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
        }
        return bundle;
    }

    @Nullable
    @Override
    public Fragment.SavedState saveFragmentInstanceState(Fragment object) {
        StringBuilder stringBuilder;
        if (object.mIndex < 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(object);
            stringBuilder.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        int n = object.mState;
        stringBuilder = null;
        if (n > 0) {
            Bundle bundle = this.saveFragmentBasicState((Fragment)object);
            object = stringBuilder;
            if (bundle != null) {
                object = new Fragment.SavedState(bundle);
            }
            return object;
        }
        return null;
    }

    void saveFragmentViewState(Fragment fragment) {
        if (fragment.mInnerView == null) {
            return;
        }
        SparseArray<Parcelable> sparseArray = this.mStateArray;
        if (sparseArray == null) {
            this.mStateArray = new SparseArray();
        } else {
            sparseArray.clear();
        }
        fragment.mInnerView.saveHierarchyState(this.mStateArray);
        if (this.mStateArray.size() > 0) {
            fragment.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
        }
    }

    void saveNonConfig() {
        Object object = null;
        Object object2 = null;
        Object object3 = null;
        Object object4 = null;
        Object object5 = null;
        Object object6 = null;
        if (this.mActive != null) {
            int n = 0;
            do {
                object = object2;
                object3 = object4;
                object5 = object6;
                if (n >= this.mActive.size()) break;
                Fragment fragment = (Fragment)this.mActive.valueAt(n);
                object3 = object2;
                object5 = object4;
                Object object7 = object6;
                if (fragment != null) {
                    int n2;
                    object = object2;
                    if (fragment.mRetainInstance) {
                        object3 = object2;
                        if (object2 == null) {
                            object3 = new ArrayList();
                        }
                        object3.add(fragment);
                        n2 = fragment.mTarget != null ? fragment.mTarget.mIndex : -1;
                        fragment.mTargetIndex = n2;
                        object = object3;
                        if (DEBUG) {
                            object2 = new StringBuilder();
                            object2.append("retainNonConfig: keeping retained ");
                            object2.append(fragment);
                            Log.v((String)"FragmentManager", (String)object2.toString());
                            object = object3;
                        }
                    }
                    if (fragment.mChildFragmentManager != null) {
                        fragment.mChildFragmentManager.saveNonConfig();
                        object3 = fragment.mChildFragmentManager.mSavedNonConfig;
                    } else {
                        object3 = fragment.mChildNonConfig;
                    }
                    object2 = object4;
                    if (object4 == null) {
                        object2 = object4;
                        if (object3 != null) {
                            object4 = new ArrayList(this.mActive.size());
                            n2 = 0;
                            do {
                                object2 = object4;
                                if (n2 >= n) break;
                                object4.add(null);
                                ++n2;
                            } while (true);
                        }
                    }
                    if (object2 != null) {
                        object2.add(object3);
                    }
                    object4 = object6;
                    if (object6 == null) {
                        object4 = object6;
                        if (fragment.mViewModelStore != null) {
                            object6 = new ArrayList(this.mActive.size());
                            n2 = 0;
                            do {
                                object4 = object6;
                                if (n2 >= n) break;
                                object6.add(null);
                                ++n2;
                            } while (true);
                        }
                    }
                    object3 = object;
                    object5 = object2;
                    object7 = object4;
                    if (object4 != null) {
                        object4.add(fragment.mViewModelStore);
                        object7 = object4;
                        object5 = object2;
                        object3 = object;
                    }
                }
                ++n;
                object2 = object3;
                object4 = object5;
                object6 = object7;
            } while (true);
        }
        if (object == null && object3 == null && object5 == null) {
            this.mSavedNonConfig = null;
            return;
        }
        this.mSavedNonConfig = new FragmentManagerNonConfig((List<Fragment>)object, (List<FragmentManagerNonConfig>)object3, (List<ViewModelStore>)object5);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void scheduleCommit() {
        synchronized (this) {
            ArrayList<StartEnterTransitionListener> arrayList = this.mPostponedTransactions;
            boolean bl = false;
            boolean bl2 = arrayList != null && !this.mPostponedTransactions.isEmpty();
            boolean bl3 = bl;
            if (this.mPendingActions != null) {
                bl3 = bl;
                if (this.mPendingActions.size() == 1) {
                    bl3 = true;
                }
            }
            if (bl2 || bl3) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setBackStackIndex(int n, BackStackRecord backStackRecord) {
        synchronized (this) {
            int n2;
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            if (n < n2) {
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Setting back stack index ");
                    stringBuilder.append(n);
                    stringBuilder.append(" to ");
                    stringBuilder.append(backStackRecord);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                this.mBackStackIndices.set(n, backStackRecord);
            } else {
                StringBuilder stringBuilder;
                for (int i = n2 = this.mBackStackIndices.size(); i < n; ++i) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList();
                    }
                    if (DEBUG) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Adding available back stack index ");
                        stringBuilder.append(i);
                        Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                    }
                    this.mAvailBackStackIndices.add(i);
                }
                if (DEBUG) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Adding back stack index ");
                    stringBuilder.append(n);
                    stringBuilder.append(" with ");
                    stringBuilder.append(backStackRecord);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                this.mBackStackIndices.add(backStackRecord);
            }
            return;
        }
    }

    public void setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment != null && (this.mActive.get(fragment.mIndex) != fragment || fragment.mHost != null && fragment.getFragmentManager() != this)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not an active fragment of FragmentManager ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mPrimaryNav = fragment;
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("show: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged ^= true;
        }
    }

    void startPendingDeferredFragments() {
        if (this.mActive == null) {
            return;
        }
        for (int i = 0; i < this.mActive.size(); ++i) {
            Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment == null) continue;
            this.performPendingDeferredStart(fragment);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("FragmentManager{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            DebugUtils.buildShortClassTag(fragment, stringBuilder);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
        }
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> copyOnWriteArrayList = this.mLifecycleCallbacks;
        synchronized (copyOnWriteArrayList) {
            int n = 0;
            int n2 = this.mLifecycleCallbacks.size();
            while (n < n2) {
                if (this.mLifecycleCallbacks.get((int)n).mCallback == fragmentLifecycleCallbacks) {
                    this.mLifecycleCallbacks.remove(n);
                    return;
                }
                ++n;
            }
            return;
        }
    }

    private static class AnimateOnHWLayerIfNeededListener
    extends AnimationListenerWrapper {
        View mView;

        AnimateOnHWLayerIfNeededListener(View view, Animation.AnimationListener animationListener) {
            super(animationListener);
            this.mView = view;
        }

        @CallSuper
        @Override
        public void onAnimationEnd(Animation animation) {
            if (!ViewCompat.isAttachedToWindow(this.mView) && Build.VERSION.SDK_INT < 24) {
                this.mView.setLayerType(0, null);
            } else {
                this.mView.post(new Runnable(){

                    @Override
                    public void run() {
                        AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, null);
                    }
                });
            }
            super.onAnimationEnd(animation);
        }

    }

    private static class AnimationListenerWrapper
    implements Animation.AnimationListener {
        private final Animation.AnimationListener mWrapped;

        AnimationListenerWrapper(Animation.AnimationListener animationListener) {
            this.mWrapped = animationListener;
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationEnd(animation);
            }
        }

        @CallSuper
        public void onAnimationRepeat(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationRepeat(animation);
            }
        }

        @CallSuper
        public void onAnimationStart(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationStart(animation);
            }
        }
    }

    private static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        AnimationOrAnimator(Animator animator) {
            this.animation = null;
            this.animator = animator;
            if (animator != null) {
                return;
            }
            throw new IllegalStateException("Animator cannot be null");
        }

        AnimationOrAnimator(Animation animation) {
            this.animation = animation;
            this.animator = null;
            if (animation != null) {
                return;
            }
            throw new IllegalStateException("Animation cannot be null");
        }
    }

    private static class AnimatorOnHWLayerIfNeededListener
    extends AnimatorListenerAdapter {
        View mView;

        AnimatorOnHWLayerIfNeededListener(View view) {
            this.mView = view;
        }

        public void onAnimationEnd(Animator animator) {
            this.mView.setLayerType(0, null);
            animator.removeListener((Animator.AnimatorListener)this);
        }

        public void onAnimationStart(Animator animator) {
            this.mView.setLayerType(2, null);
        }
    }

    private static class EndViewTransitionAnimator
    extends AnimationSet
    implements Runnable {
        private boolean mAnimating = true;
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;

        EndViewTransitionAnimator(@NonNull Animation animation, @NonNull ViewGroup viewGroup, @NonNull View view) {
            super(false);
            this.mParent = viewGroup;
            this.mChild = view;
            this.addAnimation(animation);
            this.mParent.post((Runnable)this);
        }

        public boolean getTransformation(long l, Transformation transformation) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(l, transformation)) {
                this.mEnded = true;
                OneShotPreDrawListener.add((View)this.mParent, this);
            }
            return true;
        }

        public boolean getTransformation(long l, Transformation transformation, float f) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(l, transformation, f)) {
                this.mEnded = true;
                OneShotPreDrawListener.add((View)this.mParent, this);
            }
            return true;
        }

        @Override
        public void run() {
            if (!this.mEnded && this.mAnimating) {
                this.mAnimating = false;
                this.mParent.post((Runnable)this);
                return;
            }
            this.mParent.endViewTransition(this.mChild);
            this.mTransitionEnded = true;
        }
    }

    private static final class FragmentLifecycleCallbacksHolder {
        final FragmentManager.FragmentLifecycleCallbacks mCallback;
        final boolean mRecursive;

        FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean bl) {
            this.mCallback = fragmentLifecycleCallbacks;
            this.mRecursive = bl;
        }
    }

    static class FragmentTag {
        public static final int[] Fragment = new int[]{16842755, 16842960, 16842961};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        private FragmentTag() {
        }
    }

    static interface OpGenerator {
        public boolean generateOps(ArrayList<BackStackRecord> var1, ArrayList<Boolean> var2);
    }

    private class PopBackStackState
    implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String string2, int n, int n2) {
            this.mName = string2;
            this.mId = n;
            this.mFlags = n2;
        }

        @Override
        public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
            FragmentManager fragmentManager;
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && (fragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager()) != null && fragmentManager.popBackStackImmediate()) {
                return false;
            }
            return FragmentManagerImpl.this.popBackStackState(arrayList, arrayList2, this.mName, this.mId, this.mFlags);
        }
    }

    static class StartEnterTransitionListener
    implements Fragment.OnStartEnterTransitionListener {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord backStackRecord, boolean bl) {
            this.mIsBack = bl;
            this.mRecord = backStackRecord;
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }

        public void completeTransaction() {
            Object object;
            int n = this.mNumPostponed;
            boolean bl = false;
            n = n > 0 ? 1 : 0;
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            int n2 = fragmentManagerImpl.mAdded.size();
            for (int i = 0; i < n2; ++i) {
                object = fragmentManagerImpl.mAdded.get(i);
                object.setOnStartEnterTransitionListener(null);
                if (n == 0 || !object.isPostponed()) continue;
                object.startPostponedEnterTransition();
            }
            fragmentManagerImpl = this.mRecord.mManager;
            object = this.mRecord;
            boolean bl2 = this.mIsBack;
            if (n == 0) {
                bl = true;
            }
            fragmentManagerImpl.completeExecute((BackStackRecord)object, bl2, bl, true);
        }

        public boolean isReady() {
            if (this.mNumPostponed == 0) {
                return true;
            }
            return false;
        }

        @Override
        public void onStartEnterTransition() {
            --this.mNumPostponed;
            if (this.mNumPostponed != 0) {
                return;
            }
            this.mRecord.mManager.scheduleCommit();
        }

        @Override
        public void startListening() {
            ++this.mNumPostponed;
        }
    }

}

