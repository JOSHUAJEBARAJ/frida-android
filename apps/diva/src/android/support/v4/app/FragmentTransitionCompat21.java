/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.transition.Transition
 *  android.transition.Transition$EpicenterCallback
 *  android.transition.TransitionManager
 *  android.transition.TransitionSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnPreDrawListener
 */
package android.support.v4.app;

import android.graphics.Rect;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

class FragmentTransitionCompat21 {
    FragmentTransitionCompat21() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void addTargets(Object object, ArrayList<View> arrayList) {
        if ((object = (Transition)object) instanceof TransitionSet) {
            object = (TransitionSet)object;
            int n = object.getTransitionCount();
            int n2 = 0;
            while (n2 < n) {
                FragmentTransitionCompat21.addTargets((Object)object.getTransitionAt(n2), arrayList);
                ++n2;
            }
            return;
        }
        if (FragmentTransitionCompat21.hasSimpleTarget((Transition)object) || !FragmentTransitionCompat21.isNullOrEmpty(object.getTargets())) return;
        int n = arrayList.size();
        int n3 = 0;
        while (n3 < n) {
            object.addTarget(arrayList.get(n3));
            ++n3;
        }
    }

    public static void addTransitionTargets(Object object, Object object2, final View view, ViewRetriever viewRetriever, View view2, EpicenterView epicenterView, Map<String, String> map, ArrayList<View> arrayList, Map<String, View> map2, Map<String, View> map3, ArrayList<View> arrayList2) {
        if (object != null || object2 != null) {
            if ((object = (Transition)object) != null) {
                object.addTarget(view2);
            }
            if (object2 != null) {
                FragmentTransitionCompat21.setSharedElementTargets(object2, view2, map2, arrayList2);
            }
            if (viewRetriever != null) {
                view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener((Transition)object, view2, viewRetriever, map, map3, arrayList){
                    final /* synthetic */ Transition val$enterTransition;
                    final /* synthetic */ ArrayList val$enteringViews;
                    final /* synthetic */ ViewRetriever val$inFragment;
                    final /* synthetic */ Map val$nameOverrides;
                    final /* synthetic */ View val$nonExistentView;
                    final /* synthetic */ Map val$renamedViews;

                    public boolean onPreDraw() {
                        View view2;
                        view.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
                        if (this.val$enterTransition != null) {
                            this.val$enterTransition.removeTarget(this.val$nonExistentView);
                        }
                        if ((view2 = this.val$inFragment.getView()) != null) {
                            if (!this.val$nameOverrides.isEmpty()) {
                                FragmentTransitionCompat21.findNamedViews(this.val$renamedViews, view2);
                                this.val$renamedViews.keySet().retainAll(this.val$nameOverrides.values());
                                for (Map.Entry entry : this.val$nameOverrides.entrySet()) {
                                    String string2 = (String)entry.getValue();
                                    if ((string2 = (View)this.val$renamedViews.get(string2)) == null) continue;
                                    string2.setTransitionName((String)entry.getKey());
                                }
                            }
                            if (this.val$enterTransition != null) {
                                FragmentTransitionCompat21.captureTransitioningViews(this.val$enteringViews, view2);
                                this.val$enteringViews.removeAll(this.val$renamedViews.values());
                                this.val$enteringViews.add(this.val$nonExistentView);
                                FragmentTransitionCompat21.addTargets((Object)this.val$enterTransition, this.val$enteringViews);
                            }
                        }
                        return true;
                    }
                });
            }
            FragmentTransitionCompat21.setSharedElementEpicenter((Transition)object, epicenterView);
        }
    }

    public static void beginDelayedTransition(ViewGroup viewGroup, Object object) {
        TransitionManager.beginDelayedTransition((ViewGroup)viewGroup, (Transition)((Transition)object));
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void bfsAddViewChildren(List<View> list, View view) {
        int n = list.size();
        if (!FragmentTransitionCompat21.containedBeforeIndex(list, view, n)) {
            list.add(view);
            for (int i = n; i < list.size(); ++i) {
                view = list.get(i);
                if (!(view instanceof ViewGroup)) continue;
                view = (ViewGroup)view;
                int n2 = view.getChildCount();
                for (int j = 0; j < n2; ++j) {
                    View view2 = view.getChildAt(j);
                    if (FragmentTransitionCompat21.containedBeforeIndex(list, view2, n)) continue;
                    list.add(view2);
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Object captureExitingViews(Object object, View view, ArrayList<View> arrayList, Map<String, View> map, View view2) {
        Object object2 = object;
        if (object == null) return object2;
        FragmentTransitionCompat21.captureTransitioningViews(arrayList, view);
        if (map != null) {
            arrayList.removeAll(map.values());
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        arrayList.add(view2);
        FragmentTransitionCompat21.addTargets((Object)((Transition)object), arrayList);
        return object;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void captureTransitioningViews(ArrayList<View> arrayList, View view) {
        if (view.getVisibility() != 0) return;
        if (view instanceof ViewGroup) {
            if ((view = (ViewGroup)view).isTransitionGroup()) {
                arrayList.add(view);
                return;
            }
            int n = view.getChildCount();
            int n2 = 0;
            while (n2 < n) {
                FragmentTransitionCompat21.captureTransitioningViews(arrayList, view.getChildAt(n2));
                ++n2;
            }
            return;
        }
        arrayList.add(view);
    }

    public static void cleanupTransitions(final View view, View view2, Object object, ArrayList<View> arrayList, Object object2, ArrayList<View> arrayList2, Object object3, ArrayList<View> arrayList3, Object object4, ArrayList<View> arrayList4, Map<String, View> map) {
        object = (Transition)object;
        object2 = (Transition)object2;
        object3 = (Transition)object3;
        if ((object4 = (Transition)object4) != null) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener((Transition)object, arrayList, (Transition)object2, arrayList2, (Transition)object3, arrayList3, map, arrayList4, (Transition)object4, view2){
                final /* synthetic */ Transition val$enterTransition;
                final /* synthetic */ ArrayList val$enteringViews;
                final /* synthetic */ Transition val$exitTransition;
                final /* synthetic */ ArrayList val$exitingViews;
                final /* synthetic */ ArrayList val$hiddenViews;
                final /* synthetic */ View val$nonExistentView;
                final /* synthetic */ Transition val$overallTransition;
                final /* synthetic */ Map val$renamedViews;
                final /* synthetic */ ArrayList val$sharedElementTargets;
                final /* synthetic */ Transition val$sharedElementTransition;

                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
                    if (this.val$enterTransition != null) {
                        FragmentTransitionCompat21.removeTargets((Object)this.val$enterTransition, this.val$enteringViews);
                    }
                    if (this.val$exitTransition != null) {
                        FragmentTransitionCompat21.removeTargets((Object)this.val$exitTransition, this.val$exitingViews);
                    }
                    if (this.val$sharedElementTransition != null) {
                        FragmentTransitionCompat21.removeTargets((Object)this.val$sharedElementTransition, this.val$sharedElementTargets);
                    }
                    for (Map.Entry entry : this.val$renamedViews.entrySet()) {
                        ((View)entry.getValue()).setTransitionName((String)entry.getKey());
                    }
                    int n = this.val$hiddenViews.size();
                    for (int i = 0; i < n; ++i) {
                        this.val$overallTransition.excludeTarget((View)this.val$hiddenViews.get(i), false);
                    }
                    this.val$overallTransition.excludeTarget(this.val$nonExistentView, false);
                    return true;
                }
            });
        }
    }

    public static Object cloneTransition(Object object) {
        Object object2 = object;
        if (object != null) {
            object2 = ((Transition)object).clone();
        }
        return object2;
    }

    private static boolean containedBeforeIndex(List<View> list, View view, int n) {
        for (int i = 0; i < n; ++i) {
            if (list.get(i) != view) continue;
            return true;
        }
        return false;
    }

    public static void excludeTarget(Object object, View view, boolean bl) {
        ((Transition)object).excludeTarget(view, bl);
    }

    public static void findNamedViews(Map<String, View> map, View view) {
        if (view.getVisibility() == 0) {
            String string2 = view.getTransitionName();
            if (string2 != null) {
                map.put(string2, view);
            }
            if (view instanceof ViewGroup) {
                view = (ViewGroup)view;
                int n = view.getChildCount();
                for (int i = 0; i < n; ++i) {
                    FragmentTransitionCompat21.findNamedViews(map, view.getChildAt(i));
                }
            }
        }
    }

    private static Rect getBoundsOnScreen(View view) {
        Rect rect = new Rect();
        int[] arrn = new int[2];
        view.getLocationOnScreen(arrn);
        rect.set(arrn[0], arrn[1], arrn[0] + view.getWidth(), arrn[1] + view.getHeight());
        return rect;
    }

    public static String getTransitionName(View view) {
        return view.getTransitionName();
    }

    private static boolean hasSimpleTarget(Transition transition) {
        if (!(FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetIds()) && FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetNames()) && FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetTypes()))) {
            return true;
        }
        return false;
    }

    private static boolean isNullOrEmpty(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Object mergeTransitions(Object object, Object object2, Object object3, boolean bl) {
        boolean bl2 = true;
        Transition transition = (Transition)object;
        object = (Transition)object2;
        object3 = (Transition)object3;
        boolean bl3 = bl2;
        if (transition != null) {
            bl3 = bl2;
            if (object != null) {
                bl3 = bl;
            }
        }
        if (bl3) {
            object2 = new TransitionSet();
            if (transition != null) {
                object2.addTransition(transition);
            }
            if (object != null) {
                object2.addTransition((Transition)object);
            }
            if (object3 != null) {
                object2.addTransition((Transition)object3);
            }
            return object2;
        }
        object2 = null;
        if (object != null && transition != null) {
            object = new TransitionSet().addTransition((Transition)object).addTransition(transition).setOrdering(1);
        } else if (object == null) {
            object = object2;
            if (transition != null) {
                object = transition;
            }
        }
        if (object3 == null) {
            return object;
        }
        object2 = new TransitionSet();
        if (object != null) {
            object2.addTransition((Transition)object);
        }
        object2.addTransition((Transition)object3);
        return object2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void removeTargets(Object object, ArrayList<View> arrayList) {
        List list;
        if ((object = (Transition)object) instanceof TransitionSet) {
            object = (TransitionSet)object;
            int n = object.getTransitionCount();
            int n2 = 0;
            while (n2 < n) {
                FragmentTransitionCompat21.removeTargets((Object)object.getTransitionAt(n2), arrayList);
                ++n2;
            }
            return;
        }
        if (FragmentTransitionCompat21.hasSimpleTarget((Transition)object) || (list = object.getTargets()) == null || list.size() != arrayList.size() || !list.containsAll(arrayList)) return;
        for (int i = arrayList.size() - 1; i >= 0; --i) {
            object.removeTarget(arrayList.get(i));
        }
    }

    public static void setEpicenter(Object object, View view) {
        ((Transition)object).setEpicenterCallback(new Transition.EpicenterCallback(FragmentTransitionCompat21.getBoundsOnScreen(view)){
            final /* synthetic */ Rect val$epicenter;

            public Rect onGetEpicenter(Transition transition) {
                return this.val$epicenter;
            }
        });
    }

    private static void setSharedElementEpicenter(Transition transition, final EpicenterView epicenterView) {
        if (transition != null) {
            transition.setEpicenterCallback(new Transition.EpicenterCallback(){
                private Rect mEpicenter;

                public Rect onGetEpicenter(Transition transition) {
                    if (this.mEpicenter == null && epicenterView.epicenter != null) {
                        this.mEpicenter = FragmentTransitionCompat21.getBoundsOnScreen(epicenterView.epicenter);
                    }
                    return this.mEpicenter;
                }
            });
        }
    }

    public static void setSharedElementTargets(Object object, View view, Map<String, View> object2, ArrayList<View> arrayList) {
        object = (TransitionSet)object;
        arrayList.clear();
        arrayList.addAll(object2.values());
        object2 = object.getTargets();
        object2.clear();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            FragmentTransitionCompat21.bfsAddViewChildren(object2, arrayList.get(i));
        }
        arrayList.add(view);
        FragmentTransitionCompat21.addTargets(object, arrayList);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Object wrapSharedElementTransition(Object object) {
        if (object == null || (object = (Transition)object) == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition((Transition)object);
        return transitionSet;
    }

    public static class EpicenterView {
        public View epicenter;
    }

    public static interface ViewRetriever {
        public View getView();
    }

}

