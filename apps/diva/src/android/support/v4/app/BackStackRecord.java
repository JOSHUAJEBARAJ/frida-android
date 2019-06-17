/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnPreDrawListener
 */
package android.support.v4.app;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransitionCompat21;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class BackStackRecord
extends FragmentTransaction
implements FragmentManager.BackStackEntry,
Runnable {
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SHOW = 5;
    static final boolean SUPPORTS_TRANSITIONS;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack = true;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    Op mHead;
    int mIndex = -1;
    final FragmentManagerImpl mManager;
    String mName;
    int mNumOp;
    int mPopEnterAnim;
    int mPopExitAnim;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    Op mTail;
    int mTransition;
    int mTransitionStyle;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = Build.VERSION.SDK_INT >= 21;
        SUPPORTS_TRANSITIONS = bl;
    }

    public BackStackRecord(FragmentManagerImpl fragmentManagerImpl) {
        this.mManager = fragmentManagerImpl;
    }

    private TransitionState beginTransition(SparseArray<Fragment> object, SparseArray<Fragment> sparseArray, boolean bl) {
        int n;
        TransitionState transitionState = new TransitionState();
        transitionState.nonExistentView = new View(this.mManager.mHost.getContext());
        boolean bl2 = false;
        for (n = 0; n < object.size(); ++n) {
            if (!this.configureTransitions(object.keyAt(n), transitionState, bl, (SparseArray<Fragment>)object, sparseArray)) continue;
            bl2 = true;
        }
        for (n = 0; n < sparseArray.size(); ++n) {
            int n2 = sparseArray.keyAt(n);
            boolean bl3 = bl2;
            if (object.get(n2) == null) {
                bl3 = bl2;
                if (this.configureTransitions(n2, transitionState, bl, (SparseArray<Fragment>)object, sparseArray)) {
                    bl3 = true;
                }
            }
            bl2 = bl3;
        }
        object = transitionState;
        if (!bl2) {
            object = null;
        }
        return object;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void calculateFragments(SparseArray<Fragment> var1_1, SparseArray<Fragment> var2_2) {
        if (!this.mManager.mContainer.onHasView()) {
            return;
        }
        var5_3 = this.mHead;
        while (var5_3 != null) {
            switch (var5_3.cmd) {
                case 1: {
                    this.setLastIn(var2_2, var5_3.fragment);
                    ** break;
                }
                case 2: {
                    var6_6 = var4_5 = var5_3.fragment;
                    if (this.mManager.mAdded == null) ** GOTO lbl28
                    var3_4 = 0;
                    do {
                        var6_6 = var4_5;
                        if (var3_4 >= this.mManager.mAdded.size()) break;
                        var7_7 = this.mManager.mAdded.get(var3_4);
                        if (var4_5 == null) ** GOTO lbl-1000
                        var6_6 = var4_5;
                        if (var7_7.mContainerId == var4_5.mContainerId) lbl-1000: // 2 sources:
                        {
                            if (var7_7 == var4_5) {
                                var6_6 = null;
                            } else {
                                BackStackRecord.setFirstOut(var1_1, var7_7);
                                var6_6 = var4_5;
                            }
                        }
                        ++var3_4;
                        var4_5 = var6_6;
                    } while (true);
lbl28: // 2 sources:
                    this.setLastIn(var2_2, var6_6);
                    ** break;
                }
                case 3: {
                    BackStackRecord.setFirstOut(var1_1, var5_3.fragment);
                    ** break;
                }
                case 4: {
                    BackStackRecord.setFirstOut(var1_1, var5_3.fragment);
                    ** break;
                }
                case 5: {
                    this.setLastIn(var2_2, var5_3.fragment);
                    ** break;
                }
                case 6: {
                    BackStackRecord.setFirstOut(var1_1, var5_3.fragment);
                }
lbl41: // 7 sources:
                default: {
                    ** GOTO lbl45
                }
                case 7: 
            }
            this.setLastIn(var2_2, var5_3.fragment);
lbl45: // 2 sources:
            var5_3 = var5_3.next;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void callSharedElementEnd(TransitionState object, Fragment fragment, Fragment fragment2, boolean bl, ArrayMap<String, View> arrayMap) {
        object = bl ? fragment2.mEnterTransitionCallback : fragment.mEnterTransitionCallback;
        if (object != null) {
            object.onSharedElementEnd(new ArrayList<String>(arrayMap.keySet()), new ArrayList<View>(arrayMap.values()), null);
        }
    }

    private static Object captureExitingViews(Object object, Fragment fragment, ArrayList<View> arrayList, ArrayMap<String, View> arrayMap, View view) {
        Object object2 = object;
        if (object != null) {
            object2 = FragmentTransitionCompat21.captureExitingViews(object, fragment.getView(), arrayList, arrayMap, view);
        }
        return object2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean configureTransitions(int n, TransitionState transitionState, boolean bl, SparseArray<Fragment> object, SparseArray<Fragment> arrayList) {
        ArrayMap<String, View> arrayMap;
        ViewGroup viewGroup = (ViewGroup)this.mManager.mContainer.onFindViewById(n);
        if (viewGroup == null) {
            return false;
        }
        Object object2 = (Fragment)arrayList.get(n);
        Object object3 = (Fragment)object.get(n);
        Object object4 = BackStackRecord.getEnterTransition((Fragment)object2, bl);
        ArrayList<View> arrayList2 = BackStackRecord.getSharedElementTransition((Fragment)object2, (Fragment)object3, bl);
        ArrayList<View> arrayList3 = BackStackRecord.getExitTransition((Fragment)object3, bl);
        object = null;
        ArrayList<View> arrayList4 = new ArrayList<View>();
        arrayList = arrayList2;
        if (arrayList2 != null) {
            arrayMap = this.remapSharedElements(transitionState, (Fragment)object3, bl);
            if (arrayMap.isEmpty()) {
                arrayList = null;
                object = null;
            } else {
                object = bl ? object3.mEnterTransitionCallback : object2.mEnterTransitionCallback;
                if (object != null) {
                    object.onSharedElementStart(new ArrayList<String>(arrayMap.keySet()), new ArrayList<View>(arrayMap.values()), null);
                }
                this.prepareSharedElementTransition(transitionState, (View)viewGroup, arrayList2, (Fragment)object2, (Fragment)object3, bl, arrayList4);
                arrayList = arrayList2;
                object = arrayMap;
            }
        }
        if (object4 == null && arrayList == null && arrayList3 == null) {
            return false;
        }
        arrayList2 = new ArrayList<View>();
        arrayMap = BackStackRecord.captureExitingViews(arrayList3, (Fragment)object3, arrayList2, object, transitionState.nonExistentView);
        if (this.mSharedElementTargetNames != null && object != null && (object3 = (View)object.get(this.mSharedElementTargetNames.get(0))) != null) {
            if (arrayMap != null) {
                FragmentTransitionCompat21.setEpicenter(arrayMap, (View)object3);
            }
            if (arrayList != null) {
                FragmentTransitionCompat21.setEpicenter(arrayList, (View)object3);
            }
        }
        object3 = new FragmentTransitionCompat21.ViewRetriever((Fragment)object2){
            final /* synthetic */ Fragment val$inFragment;

            @Override
            public View getView() {
                return this.val$inFragment.getView();
            }
        };
        arrayList3 = new ArrayList<View>();
        ArrayMap<String, View> arrayMap2 = new ArrayMap<String, View>();
        boolean bl2 = true;
        if (object2 != null) {
            bl2 = bl ? object2.getAllowReturnTransitionOverlap() : object2.getAllowEnterTransitionOverlap();
        }
        if ((object2 = FragmentTransitionCompat21.mergeTransitions(object4, arrayMap, arrayList, bl2)) != null) {
            FragmentTransitionCompat21.addTransitionTargets(object4, arrayList, (View)viewGroup, (FragmentTransitionCompat21.ViewRetriever)object3, transitionState.nonExistentView, transitionState.enteringEpicenterView, transitionState.nameOverrides, arrayList3, object, arrayMap2, arrayList4);
            this.excludeHiddenFragmentsAfterEnter((View)viewGroup, transitionState, n, object2);
            FragmentTransitionCompat21.excludeTarget(object2, transitionState.nonExistentView, true);
            this.excludeHiddenFragments(transitionState, n, object2);
            FragmentTransitionCompat21.beginDelayedTransition(viewGroup, object2);
            FragmentTransitionCompat21.cleanupTransitions((View)viewGroup, transitionState.nonExistentView, object4, arrayList3, arrayMap, arrayList2, arrayList, arrayList4, object2, transitionState.hiddenFragmentViews, arrayMap2);
        }
        if (object2 != null) {
            return true;
        }
        return false;
    }

    private void doAddOp(int n, Fragment fragment, String object, int n2) {
        fragment.mFragmentManager = this.mManager;
        if (object != null) {
            if (fragment.mTag != null && !object.equals(fragment.mTag)) {
                throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + (String)object);
            }
            fragment.mTag = object;
        }
        if (n != 0) {
            if (fragment.mFragmentId != 0 && fragment.mFragmentId != n) {
                throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + n);
            }
            fragment.mFragmentId = n;
            fragment.mContainerId = n;
        }
        object = new Op();
        object.cmd = n2;
        object.fragment = fragment;
        this.addOp((Op)object);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void excludeHiddenFragments(TransitionState transitionState, int n, Object object) {
        if (this.mManager.mAdded == null) {
            return;
        }
        int n2 = 0;
        while (n2 < this.mManager.mAdded.size()) {
            Fragment fragment = this.mManager.mAdded.get(n2);
            if (fragment.mView != null && fragment.mContainer != null && fragment.mContainerId == n) {
                if (fragment.mHidden) {
                    if (!transitionState.hiddenFragmentViews.contains((Object)fragment.mView)) {
                        FragmentTransitionCompat21.excludeTarget(object, fragment.mView, true);
                        transitionState.hiddenFragmentViews.add(fragment.mView);
                    }
                } else {
                    FragmentTransitionCompat21.excludeTarget(object, fragment.mView, false);
                    transitionState.hiddenFragmentViews.remove((Object)fragment.mView);
                }
            }
            ++n2;
        }
    }

    private void excludeHiddenFragmentsAfterEnter(final View view, final TransitionState transitionState, final int n, final Object object) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){

            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
                BackStackRecord.this.excludeHiddenFragments(transitionState, n, object);
                return true;
            }
        });
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Object getEnterTransition(Fragment object, boolean bl) {
        if (object == null) {
            return null;
        }
        if (bl) {
            object = object.getReenterTransition();
            do {
                return FragmentTransitionCompat21.cloneTransition(object);
                break;
            } while (true);
        }
        object = object.getEnterTransition();
        return FragmentTransitionCompat21.cloneTransition(object);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Object getExitTransition(Fragment object, boolean bl) {
        if (object == null) {
            return null;
        }
        if (bl) {
            object = object.getReturnTransition();
            do {
                return FragmentTransitionCompat21.cloneTransition(object);
                break;
            } while (true);
        }
        object = object.getExitTransition();
        return FragmentTransitionCompat21.cloneTransition(object);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Object getSharedElementTransition(Fragment object, Fragment fragment, boolean bl) {
        if (object == null || fragment == null) {
            return null;
        }
        if (bl) {
            object = fragment.getSharedElementReturnTransition();
            do {
                return FragmentTransitionCompat21.wrapSharedElementTransition(object);
                break;
            } while (true);
        }
        object = object.getSharedElementEnterTransition();
        return FragmentTransitionCompat21.wrapSharedElementTransition(object);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private ArrayMap<String, View> mapEnteringSharedElements(TransitionState arrayMap, Fragment fragment, boolean bl) {
        ArrayMap<String, View> arrayMap2 = new ArrayMap<String, View>();
        fragment = fragment.getView();
        arrayMap = arrayMap2;
        if (fragment == null) return arrayMap;
        arrayMap = arrayMap2;
        if (this.mSharedElementSourceNames == null) return arrayMap;
        FragmentTransitionCompat21.findNamedViews(arrayMap2, (View)fragment);
        if (bl) {
            return BackStackRecord.remapNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames, arrayMap2);
        }
        arrayMap2.retainAll(this.mSharedElementTargetNames);
        return arrayMap2;
    }

    private ArrayMap<String, View> mapSharedElementsIn(TransitionState transitionState, boolean bl, Fragment fragment) {
        ArrayMap<String, View> arrayMap = this.mapEnteringSharedElements(transitionState, fragment, bl);
        if (bl) {
            if (fragment.mExitTransitionCallback != null) {
                fragment.mExitTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, arrayMap);
            }
            this.setBackNameOverrides(transitionState, arrayMap, true);
            return arrayMap;
        }
        if (fragment.mEnterTransitionCallback != null) {
            fragment.mEnterTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, arrayMap);
        }
        this.setNameOverrides(transitionState, arrayMap, true);
        return arrayMap;
    }

    private void prepareSharedElementTransition(final TransitionState transitionState, final View view, final Object object, final Fragment fragment, final Fragment fragment2, final boolean bl, final ArrayList<View> arrayList) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){

            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
                if (object != null) {
                    FragmentTransitionCompat21.removeTargets(object, arrayList);
                    arrayList.clear();
                    ArrayMap arrayMap = BackStackRecord.this.mapSharedElementsIn(transitionState, bl, fragment);
                    FragmentTransitionCompat21.setSharedElementTargets(object, transitionState.nonExistentView, arrayMap, arrayList);
                    BackStackRecord.this.setEpicenterIn(arrayMap, transitionState);
                    BackStackRecord.this.callSharedElementEnd(transitionState, fragment, fragment2, bl, arrayMap);
                }
                return true;
            }
        });
    }

    private static ArrayMap<String, View> remapNames(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayMap<String, View> arrayMap) {
        if (arrayMap.isEmpty()) {
            return arrayMap;
        }
        ArrayMap<String, View> arrayMap2 = new ArrayMap<String, View>();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            View view = arrayMap.get(arrayList.get(i));
            if (view == null) continue;
            arrayMap2.put(arrayList2.get(i), view);
        }
        return arrayMap2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private ArrayMap<String, View> remapSharedElements(TransitionState transitionState, Fragment fragment, boolean bl) {
        ArrayMap<String, View> arrayMap;
        ArrayMap<String, View> arrayMap2 = arrayMap = new ArrayMap<String, View>();
        if (this.mSharedElementSourceNames != null) {
            FragmentTransitionCompat21.findNamedViews(arrayMap, fragment.getView());
            if (bl) {
                arrayMap.retainAll(this.mSharedElementTargetNames);
                arrayMap2 = arrayMap;
            } else {
                arrayMap2 = BackStackRecord.remapNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames, arrayMap);
            }
        }
        if (bl) {
            if (fragment.mEnterTransitionCallback != null) {
                fragment.mEnterTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, arrayMap2);
            }
            this.setBackNameOverrides(transitionState, arrayMap2, false);
            return arrayMap2;
        }
        if (fragment.mExitTransitionCallback != null) {
            fragment.mExitTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, arrayMap2);
        }
        this.setNameOverrides(transitionState, arrayMap2, false);
        return arrayMap2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setBackNameOverrides(TransitionState transitionState, ArrayMap<String, View> arrayMap, boolean bl) {
        int n = this.mSharedElementTargetNames == null ? 0 : this.mSharedElementTargetNames.size();
        int n2 = 0;
        while (n2 < n) {
            String string2 = this.mSharedElementSourceNames.get(n2);
            Object object = arrayMap.get(this.mSharedElementTargetNames.get(n2));
            if (object != null) {
                object = FragmentTransitionCompat21.getTransitionName((View)object);
                if (bl) {
                    BackStackRecord.setNameOverride(transitionState.nameOverrides, string2, (String)object);
                } else {
                    BackStackRecord.setNameOverride(transitionState.nameOverrides, (String)object, string2);
                }
            }
            ++n2;
        }
    }

    private void setEpicenterIn(ArrayMap<String, View> view, TransitionState transitionState) {
        if (this.mSharedElementTargetNames != null && !view.isEmpty() && (view = view.get(this.mSharedElementTargetNames.get(0))) != null) {
            transitionState.enteringEpicenterView.epicenter = view;
        }
    }

    private static void setFirstOut(SparseArray<Fragment> sparseArray, Fragment fragment) {
        int n;
        if (fragment != null && (n = fragment.mContainerId) != 0 && !fragment.isHidden() && fragment.isAdded() && fragment.getView() != null && sparseArray.get(n) == null) {
            sparseArray.put(n, (Object)fragment);
        }
    }

    private void setLastIn(SparseArray<Fragment> sparseArray, Fragment fragment) {
        int n;
        if (fragment != null && (n = fragment.mContainerId) != 0) {
            sparseArray.put(n, (Object)fragment);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void setNameOverride(ArrayMap<String, String> arrayMap, String string2, String string3) {
        if (string2 == null || string3 == null) return;
        for (int i = 0; i < arrayMap.size(); ++i) {
            if (!string2.equals(arrayMap.valueAt(i))) continue;
            arrayMap.setValueAt(i, string3);
            return;
        }
        arrayMap.put(string2, string3);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void setNameOverrides(TransitionState transitionState, ArrayMap<String, View> arrayMap, boolean bl) {
        int n = arrayMap.size();
        int n2 = 0;
        while (n2 < n) {
            String string2 = arrayMap.keyAt(n2);
            String string3 = FragmentTransitionCompat21.getTransitionName(arrayMap.valueAt(n2));
            if (bl) {
                BackStackRecord.setNameOverride(transitionState.nameOverrides, string2, string3);
            } else {
                BackStackRecord.setNameOverride(transitionState.nameOverrides, string3, string2);
            }
            ++n2;
        }
    }

    private static void setNameOverrides(TransitionState transitionState, ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); ++i) {
                String string2 = arrayList.get(i);
                String string3 = arrayList2.get(i);
                BackStackRecord.setNameOverride(transitionState.nameOverrides, string2, string3);
            }
        }
    }

    @Override
    public FragmentTransaction add(int n, Fragment fragment) {
        this.doAddOp(n, fragment, null, 1);
        return this;
    }

    @Override
    public FragmentTransaction add(int n, Fragment fragment, String string2) {
        this.doAddOp(n, fragment, string2, 1);
        return this;
    }

    @Override
    public FragmentTransaction add(Fragment fragment, String string2) {
        this.doAddOp(0, fragment, string2, 1);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    void addOp(Op op) {
        if (this.mHead == null) {
            this.mTail = op;
            this.mHead = op;
        } else {
            op.prev = this.mTail;
            this.mTail.next = op;
            this.mTail = op;
        }
        op.enterAnim = this.mEnterAnim;
        op.exitAnim = this.mExitAnim;
        op.popEnterAnim = this.mPopEnterAnim;
        op.popExitAnim = this.mPopExitAnim;
        ++this.mNumOp;
    }

    @Override
    public FragmentTransaction addSharedElement(View object, String string2) {
        if (SUPPORTS_TRANSITIONS) {
            if ((object = FragmentTransitionCompat21.getTransitionName((View)object)) == null) {
                throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
            }
            if (this.mSharedElementSourceNames == null) {
                this.mSharedElementSourceNames = new ArrayList();
                this.mSharedElementTargetNames = new ArrayList();
            }
            this.mSharedElementSourceNames.add((String)object);
            this.mSharedElementTargetNames.add(string2);
        }
        return this;
    }

    @Override
    public FragmentTransaction addToBackStack(String string2) {
        if (!this.mAllowAddToBackStack) {
            throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        }
        this.mAddToBackStack = true;
        this.mName = string2;
        return this;
    }

    @Override
    public FragmentTransaction attach(Fragment fragment) {
        Op op = new Op();
        op.cmd = 7;
        op.fragment = fragment;
        this.addOp(op);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    void bumpBackStackNesting(int n) {
        if (this.mAddToBackStack) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v((String)"FragmentManager", (String)("Bump nesting in " + this + " by " + n));
            }
            Op op = this.mHead;
            while (op != null) {
                Fragment fragment;
                if (op.fragment != null) {
                    fragment = op.fragment;
                    fragment.mBackStackNesting += n;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v((String)"FragmentManager", (String)("Bump nesting of " + op.fragment + " to " + op.fragment.mBackStackNesting));
                    }
                }
                if (op.removed != null) {
                    for (int i = op.removed.size() - 1; i >= 0; --i) {
                        fragment = op.removed.get(i);
                        fragment.mBackStackNesting += n;
                        if (!FragmentManagerImpl.DEBUG) continue;
                        Log.v((String)"FragmentManager", (String)("Bump nesting of " + fragment + " to " + fragment.mBackStackNesting));
                    }
                }
                op = op.next;
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void calculateBackFragments(SparseArray<Fragment> var1_1, SparseArray<Fragment> var2_2) {
        if (!this.mManager.mContainer.onHasView()) {
            return;
        }
        var4_3 = this.mHead;
        while (var4_3 != null) {
            switch (var4_3.cmd) {
                case 1: {
                    BackStackRecord.setFirstOut(var1_1, var4_3.fragment);
                    ** break;
                }
                case 2: {
                    if (var4_3.removed != null) {
                        for (var3_4 = var4_3.removed.size() - 1; var3_4 >= 0; --var3_4) {
                            this.setLastIn(var2_2, var4_3.removed.get(var3_4));
                        }
                    }
                    BackStackRecord.setFirstOut(var1_1, var4_3.fragment);
                    ** break;
                }
                case 3: {
                    this.setLastIn(var2_2, var4_3.fragment);
                    ** break;
                }
                case 4: {
                    this.setLastIn(var2_2, var4_3.fragment);
                    ** break;
                }
                case 5: {
                    BackStackRecord.setFirstOut(var1_1, var4_3.fragment);
                    ** break;
                }
                case 6: {
                    this.setLastIn(var2_2, var4_3.fragment);
                }
lbl27: // 7 sources:
                default: {
                    ** GOTO lbl31
                }
                case 7: 
            }
            BackStackRecord.setFirstOut(var1_1, var4_3.fragment);
lbl31: // 2 sources:
            var4_3 = var4_3.next;
        }
    }

    @Override
    public int commit() {
        return this.commitInternal(false);
    }

    @Override
    public int commitAllowingStateLoss() {
        return this.commitInternal(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    int commitInternal(boolean bl) {
        if (this.mCommitted) {
            throw new IllegalStateException("commit already called");
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v((String)"FragmentManager", (String)("Commit: " + this));
            this.dump("  ", null, new PrintWriter(new LogWriter("FragmentManager")), null);
        }
        this.mCommitted = true;
        this.mIndex = this.mAddToBackStack ? this.mManager.allocBackStackIndex(this) : -1;
        this.mManager.enqueueAction(this, bl);
        return this.mIndex;
    }

    @Override
    public FragmentTransaction detach(Fragment fragment) {
        Op op = new Op();
        op.cmd = 6;
        op.fragment = fragment;
        this.addOp(op);
        return this;
    }

    @Override
    public FragmentTransaction disallowAddToBackStack() {
        if (this.mAddToBackStack) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        }
        this.mAllowAddToBackStack = false;
        return this;
    }

    public void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] arrstring) {
        this.dump(string2, printWriter, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void dump(String string2, PrintWriter printWriter, boolean bl) {
        if (bl) {
            printWriter.print(string2);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(string2);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
                printWriter.print(" mTransitionStyle=#");
                printWriter.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                printWriter.print(string2);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                printWriter.print(string2);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
                printWriter.print(string2);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
                printWriter.print(string2);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (this.mHead == null) {
            return;
        }
        printWriter.print(string2);
        printWriter.println("Operations:");
        String string3 = string2 + "    ";
        Op op = this.mHead;
        int n = 0;
        while (op != null) {
            String string4;
            switch (op.cmd) {
                default: {
                    string4 = "cmd=" + op.cmd;
                    break;
                }
                case 0: {
                    string4 = "NULL";
                    break;
                }
                case 1: {
                    string4 = "ADD";
                    break;
                }
                case 2: {
                    string4 = "REPLACE";
                    break;
                }
                case 3: {
                    string4 = "REMOVE";
                    break;
                }
                case 4: {
                    string4 = "HIDE";
                    break;
                }
                case 5: {
                    string4 = "SHOW";
                    break;
                }
                case 6: {
                    string4 = "DETACH";
                    break;
                }
                case 7: {
                    string4 = "ATTACH";
                }
            }
            printWriter.print(string2);
            printWriter.print("  Op #");
            printWriter.print(n);
            printWriter.print(": ");
            printWriter.print(string4);
            printWriter.print(" ");
            printWriter.println(op.fragment);
            if (bl) {
                if (op.enterAnim != 0 || op.exitAnim != 0) {
                    printWriter.print(string2);
                    printWriter.print("enterAnim=#");
                    printWriter.print(Integer.toHexString(op.enterAnim));
                    printWriter.print(" exitAnim=#");
                    printWriter.println(Integer.toHexString(op.exitAnim));
                }
                if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
                    printWriter.print(string2);
                    printWriter.print("popEnterAnim=#");
                    printWriter.print(Integer.toHexString(op.popEnterAnim));
                    printWriter.print(" popExitAnim=#");
                    printWriter.println(Integer.toHexString(op.popExitAnim));
                }
            }
            if (op.removed != null && op.removed.size() > 0) {
                for (int i = 0; i < op.removed.size(); ++i) {
                    printWriter.print(string3);
                    if (op.removed.size() == 1) {
                        printWriter.print("Removed: ");
                    } else {
                        if (i == 0) {
                            printWriter.println("Removed:");
                        }
                        printWriter.print(string3);
                        printWriter.print("  #");
                        printWriter.print(i);
                        printWriter.print(": ");
                    }
                    printWriter.println(op.removed.get(i));
                }
            }
            op = op.next;
            ++n;
        }
    }

    @Override
    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }

    @Override
    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    @Override
    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }

    @Override
    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    @Override
    public int getId() {
        return this.mIndex;
    }

    @Override
    public String getName() {
        return this.mName;
    }

    public int getTransition() {
        return this.mTransition;
    }

    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }

    @Override
    public FragmentTransaction hide(Fragment fragment) {
        Op op = new Op();
        op.cmd = 4;
        op.fragment = fragment;
        this.addOp(op);
        return this;
    }

    @Override
    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }

    @Override
    public boolean isEmpty() {
        if (this.mNumOp == 0) {
            return true;
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public TransitionState popFromBackStack(boolean var1_1, TransitionState var2_2, SparseArray<Fragment> var3_3, SparseArray<Fragment> var4_4) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v((String)"FragmentManager", (String)("popFromBackStack: " + this));
            this.dump("  ", null, new PrintWriter(new LogWriter("FragmentManager")), null);
        }
        var9_5 = var2_2;
        if (!BackStackRecord.SUPPORTS_TRANSITIONS) ** GOTO lbl16
        if (var2_2 != null) ** GOTO lbl12
        if (var3_3.size() != 0) ** GOTO lbl-1000
        var9_5 = var2_2;
        if (var4_4.size() != 0) lbl-1000: // 2 sources:
        {
            var9_5 = this.beginTransition((SparseArray<Fragment>)var3_3, var4_4, true);
        }
        ** GOTO lbl16
lbl12: // 1 sources:
        var9_5 = var2_2;
        if (!var1_1) {
            BackStackRecord.setNameOverrides((TransitionState)var2_2, this.mSharedElementTargetNames, this.mSharedElementSourceNames);
            var9_5 = var2_2;
        }
lbl16: // 5 sources:
        this.bumpBackStackNesting(-1);
        var5_6 = var9_5 != null ? 0 : this.mTransitionStyle;
        var6_7 = var9_5 != null ? 0 : this.mTransition;
        var2_2 = this.mTail;
        while (var2_2 != null) {
            var7_8 = var9_5 != null ? 0 : var2_2.popEnterAnim;
            var8_9 = var9_5 != null ? 0 : var2_2.popExitAnim;
            switch (var2_2.cmd) {
                default: {
                    throw new IllegalArgumentException("Unknown cmd: " + var2_2.cmd);
                }
                case 1: {
                    var3_3 = var2_2.fragment;
                    var3_3.mNextAnim = var8_9;
                    this.mManager.removeFragment((Fragment)var3_3, FragmentManagerImpl.reverseTransit(var6_7), var5_6);
                    break;
                }
                case 2: {
                    var3_3 = var2_2.fragment;
                    if (var3_3 != null) {
                        var3_3.mNextAnim = var8_9;
                        this.mManager.removeFragment((Fragment)var3_3, FragmentManagerImpl.reverseTransit(var6_7), var5_6);
                    }
                    if (var2_2.removed == null) break;
                    for (var8_9 = 0; var8_9 < var2_2.removed.size(); ++var8_9) {
                        var3_3 = var2_2.removed.get(var8_9);
                        var3_3.mNextAnim = var7_8;
                        this.mManager.addFragment((Fragment)var3_3, false);
                    }
                    break;
                }
                case 3: {
                    var3_3 = var2_2.fragment;
                    var3_3.mNextAnim = var7_8;
                    this.mManager.addFragment((Fragment)var3_3, false);
                    break;
                }
                case 4: {
                    var3_3 = var2_2.fragment;
                    var3_3.mNextAnim = var7_8;
                    this.mManager.showFragment((Fragment)var3_3, FragmentManagerImpl.reverseTransit(var6_7), var5_6);
                    break;
                }
                case 5: {
                    var3_3 = var2_2.fragment;
                    var3_3.mNextAnim = var8_9;
                    this.mManager.hideFragment((Fragment)var3_3, FragmentManagerImpl.reverseTransit(var6_7), var5_6);
                    break;
                }
                case 6: {
                    var3_3 = var2_2.fragment;
                    var3_3.mNextAnim = var7_8;
                    this.mManager.attachFragment((Fragment)var3_3, FragmentManagerImpl.reverseTransit(var6_7), var5_6);
                    break;
                }
                case 7: {
                    var3_3 = var2_2.fragment;
                    var3_3.mNextAnim = var7_8;
                    this.mManager.detachFragment((Fragment)var3_3, FragmentManagerImpl.reverseTransit(var6_7), var5_6);
                }
            }
            var2_2 = var2_2.prev;
        }
        if (var1_1) {
            this.mManager.moveToState(this.mManager.mCurState, FragmentManagerImpl.reverseTransit(var6_7), var5_6, true);
            var9_5 = null;
        }
        if (this.mIndex < 0) return var9_5;
        this.mManager.freeBackStackIndex(this.mIndex);
        this.mIndex = -1;
        return var9_5;
    }

    @Override
    public FragmentTransaction remove(Fragment fragment) {
        Op op = new Op();
        op.cmd = 3;
        op.fragment = fragment;
        this.addOp(op);
        return this;
    }

    @Override
    public FragmentTransaction replace(int n, Fragment fragment) {
        return this.replace(n, fragment, null);
    }

    @Override
    public FragmentTransaction replace(int n, Fragment fragment, String string2) {
        if (n == 0) {
            throw new IllegalArgumentException("Must use non-zero containerViewId");
        }
        this.doAddOp(n, fragment, string2, 2);
        return this;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void run() {
        if (FragmentManagerImpl.DEBUG) {
            Log.v((String)"FragmentManager", (String)("Run: " + this));
        }
        if (this.mAddToBackStack && this.mIndex < 0) {
            throw new IllegalStateException("addToBackStack() called after commit()");
        }
        this.bumpBackStackNesting(1);
        var8_1 = null;
        if (!BackStackRecord.SUPPORTS_TRANSITIONS) ** GOTO lbl-1000
        var7_2 = new SparseArray();
        var8_1 = new SparseArray();
        this.calculateFragments((SparseArray<Fragment>)var7_2, (SparseArray<Fragment>)var8_1);
        var8_1 = this.beginTransition((SparseArray<Fragment>)var7_2, (SparseArray<Fragment>)var8_1, false);
        if (var8_1 != null) {
            var1_3 = 0;
        } else lbl-1000: // 2 sources:
        {
            var1_3 = this.mTransitionStyle;
        }
        var2_4 = var8_1 != null ? 0 : this.mTransition;
        var9_5 = this.mHead;
        do {
            if (var9_5 == null) {
                this.mManager.moveToState(this.mManager.mCurState, var2_4, var1_3, true);
                if (this.mAddToBackStack == false) return;
                this.mManager.addBackStackState(this);
                return;
            }
            var3_6 = var8_1 != null ? 0 : var9_5.enterAnim;
            var4_7 = var8_1 != null ? 0 : var9_5.exitAnim;
            switch (var9_5.cmd) {
                default: {
                    throw new IllegalArgumentException("Unknown cmd: " + var9_5.cmd);
                }
                case 1: {
                    var7_2 = var9_5.fragment;
                    var7_2.mNextAnim = var3_6;
                    this.mManager.addFragment((Fragment)var7_2, false);
                    break;
                }
                case 2: {
                    var7_2 = var9_5.fragment;
                    var6_9 = var7_2.mContainerId;
                    var10_10 = var7_2;
                    if (this.mManager.mAdded != null) {
                        var5_8 = 0;
                        do {
                            var10_10 = var7_2;
                            if (var5_8 >= this.mManager.mAdded.size()) break;
                            var11_11 = this.mManager.mAdded.get(var5_8);
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v((String)"FragmentManager", (String)("OP_REPLACE: adding=" + var7_2 + " old=" + var11_11));
                            }
                            var10_10 = var7_2;
                            if (var11_11.mContainerId == var6_9) {
                                if (var11_11 == var7_2) {
                                    var10_10 = null;
                                    var9_5.fragment = null;
                                } else {
                                    if (var9_5.removed == null) {
                                        var9_5.removed = new ArrayList<E>();
                                    }
                                    var9_5.removed.add(var11_11);
                                    var11_11.mNextAnim = var4_7;
                                    if (this.mAddToBackStack) {
                                        ++var11_11.mBackStackNesting;
                                        if (FragmentManagerImpl.DEBUG) {
                                            Log.v((String)"FragmentManager", (String)("Bump nesting of " + var11_11 + " to " + var11_11.mBackStackNesting));
                                        }
                                    }
                                    this.mManager.removeFragment(var11_11, var2_4, var1_3);
                                    var10_10 = var7_2;
                                }
                            }
                            ++var5_8;
                            var7_2 = var10_10;
                        } while (true);
                    }
                    if (var10_10 == null) break;
                    var10_10.mNextAnim = var3_6;
                    this.mManager.addFragment((Fragment)var10_10, false);
                    break;
                }
                case 3: {
                    var7_2 = var9_5.fragment;
                    var7_2.mNextAnim = var4_7;
                    this.mManager.removeFragment((Fragment)var7_2, var2_4, var1_3);
                    break;
                }
                case 4: {
                    var7_2 = var9_5.fragment;
                    var7_2.mNextAnim = var4_7;
                    this.mManager.hideFragment((Fragment)var7_2, var2_4, var1_3);
                    break;
                }
                case 5: {
                    var7_2 = var9_5.fragment;
                    var7_2.mNextAnim = var3_6;
                    this.mManager.showFragment((Fragment)var7_2, var2_4, var1_3);
                    break;
                }
                case 6: {
                    var7_2 = var9_5.fragment;
                    var7_2.mNextAnim = var4_7;
                    this.mManager.detachFragment((Fragment)var7_2, var2_4, var1_3);
                    break;
                }
                case 7: {
                    var7_2 = var9_5.fragment;
                    var7_2.mNextAnim = var3_6;
                    this.mManager.attachFragment((Fragment)var7_2, var2_4, var1_3);
                }
            }
            var9_5 = var9_5.next;
        } while (true);
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(int n) {
        this.mBreadCrumbShortTitleRes = n;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(CharSequence charSequence) {
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = charSequence;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(int n) {
        this.mBreadCrumbTitleRes = n;
        this.mBreadCrumbTitleText = null;
        return this;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(CharSequence charSequence) {
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = charSequence;
        return this;
    }

    @Override
    public FragmentTransaction setCustomAnimations(int n, int n2) {
        return this.setCustomAnimations(n, n2, 0, 0);
    }

    @Override
    public FragmentTransaction setCustomAnimations(int n, int n2, int n3, int n4) {
        this.mEnterAnim = n;
        this.mExitAnim = n2;
        this.mPopEnterAnim = n3;
        this.mPopExitAnim = n4;
        return this;
    }

    @Override
    public FragmentTransaction setTransition(int n) {
        this.mTransition = n;
        return this;
    }

    @Override
    public FragmentTransaction setTransitionStyle(int n) {
        this.mTransitionStyle = n;
        return this;
    }

    @Override
    public FragmentTransaction show(Fragment fragment) {
        Op op = new Op();
        op.cmd = 5;
        op.fragment = fragment;
        this.addOp(op);
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("BackStackEntry{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            stringBuilder.append(" #");
            stringBuilder.append(this.mIndex);
        }
        if (this.mName != null) {
            stringBuilder.append(" ");
            stringBuilder.append(this.mName);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    static final class Op {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        Op next;
        int popEnterAnim;
        int popExitAnim;
        Op prev;
        ArrayList<Fragment> removed;

        Op() {
        }
    }

    public class TransitionState {
        public FragmentTransitionCompat21.EpicenterView enteringEpicenterView;
        public ArrayList<View> hiddenFragmentViews;
        public ArrayMap<String, String> nameOverrides;
        public View nonExistentView;

        public TransitionState() {
            this.nameOverrides = new ArrayMap();
            this.hiddenFragmentViews = new ArrayList();
            this.enteringEpicenterView = new FragmentTransitionCompat21.EpicenterView();
        }
    }

}

