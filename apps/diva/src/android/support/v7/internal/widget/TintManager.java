/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.graphics.drawable.DrawableContainer
 *  android.graphics.drawable.DrawableContainer$DrawableContainerState
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.SparseArray
 */
package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.LruCache;
import android.support.v7.appcompat.R;
import android.support.v7.internal.widget.ThemeUtils;
import android.support.v7.internal.widget.TintInfo;
import android.util.Log;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class TintManager {
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
    private static final ColorFilterLruCache COLOR_FILTER_CACHE;
    private static final boolean DEBUG = false;
    private static final PorterDuff.Mode DEFAULT_MODE;
    private static final WeakHashMap<Context, TintManager> INSTANCE_CACHE;
    public static final boolean SHOULD_BE_USED;
    private static final String TAG = "TintManager";
    private static final int[] TINT_CHECKABLE_BUTTON_LIST;
    private static final int[] TINT_COLOR_CONTROL_NORMAL;
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
    private final WeakReference<Context> mContextRef;
    private ColorStateList mDefaultColorStateList;
    private SparseArray<ColorStateList> mTintLists;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = Build.VERSION.SDK_INT < 21;
        SHOULD_BE_USED = bl;
        DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
        INSTANCE_CACHE = new WeakHashMap();
        COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
        COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha};
        TINT_COLOR_CONTROL_NORMAL = new int[]{R.drawable.abc_ic_ab_back_mtrl_am_alpha, R.drawable.abc_ic_go_search_api_mtrl_alpha, R.drawable.abc_ic_search_api_mtrl_alpha, R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_ic_clear_mtrl_alpha, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha, R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha, R.drawable.abc_ic_voice_search_api_mtrl_alpha};
        COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material};
        COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult};
        TINT_COLOR_CONTROL_STATE_LIST = new int[]{R.drawable.abc_edit_text_material, R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material, R.drawable.abc_spinner_mtrl_am_alpha, R.drawable.abc_spinner_textfield_background_material, R.drawable.abc_ratingbar_full_material, R.drawable.abc_switch_track_mtrl_alpha, R.drawable.abc_switch_thumb_material, R.drawable.abc_btn_default_mtrl_shape, R.drawable.abc_btn_borderless_material};
        TINT_CHECKABLE_BUTTON_LIST = new int[]{R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material};
    }

    private TintManager(Context context) {
        this.mContextRef = new WeakReference<Context>(context);
    }

    private static boolean arrayContains(int[] arrn, int n) {
        int n2 = arrn.length;
        for (int i = 0; i < n2; ++i) {
            if (arrn[i] != n) continue;
            return true;
        }
        return false;
    }

    private ColorStateList createButtonColorStateList(Context context, int n) {
        int[][] arrarrn = new int[4][];
        int[] arrn = new int[4];
        n = ThemeUtils.getThemeAttrColor(context, n);
        int n2 = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlHighlight);
        arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
        arrn[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);
        int n3 = 0 + 1;
        arrarrn[n3] = ThemeUtils.PRESSED_STATE_SET;
        arrn[n3] = ColorUtils.compositeColors(n2, n);
        arrarrn[++n3] = ThemeUtils.FOCUSED_STATE_SET;
        arrn[n3] = ColorUtils.compositeColors(n2, n);
        n2 = n3 + 1;
        arrarrn[n2] = ThemeUtils.EMPTY_STATE_SET;
        arrn[n2] = n;
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private ColorStateList createCheckableButtonColorStateList(Context context) {
        int[][] arrarrn = new int[3][];
        int[] arrn = new int[3];
        arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
        arrn[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal);
        int n = 0 + 1;
        arrarrn[n] = ThemeUtils.CHECKED_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        arrarrn[++n] = ThemeUtils.EMPTY_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal);
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private ColorStateList createColoredButtonColorStateList(Context context) {
        return this.createButtonColorStateList(context, R.attr.colorAccent);
    }

    private ColorStateList createDefaultButtonColorStateList(Context context) {
        return this.createButtonColorStateList(context, R.attr.colorButtonNormal);
    }

    private ColorStateList createEditTextColorStateList(Context context) {
        int[][] arrarrn = new int[3][];
        int[] arrn = new int[3];
        arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
        arrn[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal);
        int n = 0 + 1;
        arrarrn[n] = ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal);
        arrarrn[++n] = ThemeUtils.EMPTY_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private ColorStateList createSeekbarThumbColorStateList(Context context) {
        int[][] arrarrn = new int[2][];
        int[] arrn = new int[2];
        arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
        arrn[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlActivated);
        int n = 0 + 1;
        arrarrn[n] = ThemeUtils.EMPTY_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private ColorStateList createSpinnerColorStateList(Context context) {
        int[][] arrarrn = new int[3][];
        int[] arrn = new int[3];
        arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
        arrn[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal);
        int n = 0 + 1;
        arrarrn[n] = ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal);
        arrarrn[++n] = ThemeUtils.EMPTY_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private ColorStateList createSwitchThumbColorStateList(Context context) {
        int[][] arrarrn = new int[3][];
        int[] arrn = new int[3];
        ColorStateList colorStateList = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorSwitchThumbNormal);
        if (colorStateList != null && colorStateList.isStateful()) {
            arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
            arrn[0] = colorStateList.getColorForState(arrarrn[0], 0);
            int n = 0 + 1;
            arrarrn[n] = ThemeUtils.CHECKED_STATE_SET;
            arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
            arrarrn[++n] = ThemeUtils.EMPTY_STATE_SET;
            arrn[n] = colorStateList.getDefaultColor();
            do {
                return new ColorStateList((int[][])arrarrn, arrn);
                break;
            } while (true);
        }
        arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
        arrn[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
        int n = 0 + 1;
        arrarrn[n] = ThemeUtils.CHECKED_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        arrarrn[++n] = ThemeUtils.EMPTY_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private ColorStateList createSwitchTrackColorStateList(Context context) {
        int[][] arrarrn = new int[3][];
        int[] arrn = new int[3];
        arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
        arrn[0] = ThemeUtils.getThemeAttrColor(context, 16842800, 0.1f);
        int n = 0 + 1;
        arrarrn[n] = ThemeUtils.CHECKED_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated, 0.3f);
        arrarrn[++n] = ThemeUtils.EMPTY_STATE_SET;
        arrn[n] = ThemeUtils.getThemeAttrColor(context, 16842800, 0.3f);
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private static PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, PorterDuff.Mode mode, int[] arrn) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return TintManager.getPorterDuffColorFilter(colorStateList.getColorForState(arrn, 0), mode);
    }

    public static TintManager get(Context context) {
        TintManager tintManager;
        TintManager tintManager2 = tintManager = INSTANCE_CACHE.get((Object)context);
        if (tintManager == null) {
            tintManager2 = new TintManager(context);
            INSTANCE_CACHE.put(context, tintManager2);
        }
        return tintManager2;
    }

    private ColorStateList getDefaultColorStateList(Context context) {
        if (this.mDefaultColorStateList == null) {
            int n = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal);
            int n2 = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
            int[][] arrarrn = new int[7][];
            int[] arrn = new int[7];
            arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
            arrn[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal);
            int n3 = 0 + 1;
            arrarrn[n3] = ThemeUtils.FOCUSED_STATE_SET;
            arrn[n3] = n2;
            arrarrn[++n3] = ThemeUtils.ACTIVATED_STATE_SET;
            arrn[n3] = n2;
            arrarrn[++n3] = ThemeUtils.PRESSED_STATE_SET;
            arrn[n3] = n2;
            arrarrn[++n3] = ThemeUtils.CHECKED_STATE_SET;
            arrn[n3] = n2;
            arrarrn[++n3] = ThemeUtils.SELECTED_STATE_SET;
            arrn[n3] = n2;
            n2 = n3 + 1;
            arrarrn[n2] = ThemeUtils.EMPTY_STATE_SET;
            arrn[n2] = n;
            this.mDefaultColorStateList = new ColorStateList((int[][])arrarrn, arrn);
        }
        return this.mDefaultColorStateList;
    }

    public static Drawable getDrawable(Context context, int n) {
        if (TintManager.isInTintList(n)) {
            return TintManager.get(context).getDrawable(n);
        }
        return ContextCompat.getDrawable(context, n);
    }

    private static PorterDuffColorFilter getPorterDuffColorFilter(int n, PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        PorterDuffColorFilter porterDuffColorFilter2 = porterDuffColorFilter = COLOR_FILTER_CACHE.get(n, mode);
        if (porterDuffColorFilter == null) {
            porterDuffColorFilter2 = new PorterDuffColorFilter(n, mode);
            COLOR_FILTER_CACHE.put(n, mode, porterDuffColorFilter2);
        }
        return porterDuffColorFilter2;
    }

    private static boolean isInTintList(int n) {
        if (TintManager.arrayContains(TINT_COLOR_CONTROL_NORMAL, n) || TintManager.arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, n) || TintManager.arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, n) || TintManager.arrayContains(TINT_COLOR_CONTROL_STATE_LIST, n) || TintManager.arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, n) || TintManager.arrayContains(TINT_CHECKABLE_BUTTON_LIST, n) || n == R.drawable.abc_cab_background_top_material) {
            return true;
        }
        return false;
    }

    private static void setPorterDuffColorFilter(Drawable drawable2, int n, PorterDuff.Mode mode) {
        PorterDuff.Mode mode2 = mode;
        if (mode == null) {
            mode2 = DEFAULT_MODE;
        }
        drawable2.setColorFilter((ColorFilter)TintManager.getPorterDuffColorFilter(n, mode2));
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean shouldMutateBackground(Drawable arrdrawable) {
        if (Build.VERSION.SDK_INT >= 16) return true;
        if (arrdrawable instanceof LayerDrawable) {
            if (Build.VERSION.SDK_INT >= 16) return true;
            return false;
        }
        if (arrdrawable instanceof InsetDrawable) {
            if (Build.VERSION.SDK_INT >= 14) return true;
            return false;
        }
        if (!(arrdrawable instanceof DrawableContainer) || !((arrdrawable = arrdrawable.getConstantState()) instanceof DrawableContainer.DrawableContainerState)) {
            return true;
        }
        arrdrawable = ((DrawableContainer.DrawableContainerState)arrdrawable).getChildren();
        int n = arrdrawable.length;
        int n2 = 0;
        while (n2 < n) {
            if (!TintManager.shouldMutateBackground(arrdrawable[n2])) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void tintDrawable(Drawable drawable2, TintInfo tintInfo, int[] arrn) {
        if (TintManager.shouldMutateBackground(drawable2) && drawable2.mutate() != drawable2) {
            Log.d((String)"TintManager", (String)"Mutated drawable is not the same instance as the input.");
            return;
        } else {
            if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
                ColorStateList colorStateList = tintInfo.mHasTintList ? tintInfo.mTintList : null;
                tintInfo = tintInfo.mHasTintMode ? tintInfo.mTintMode : DEFAULT_MODE;
                drawable2.setColorFilter((ColorFilter)TintManager.createTintFilter(colorStateList, (PorterDuff.Mode)tintInfo, arrn));
            } else {
                drawable2.clearColorFilter();
            }
            if (Build.VERSION.SDK_INT > 10) return;
            {
                drawable2.invalidateSelf();
                return;
            }
        }
    }

    public Drawable getDrawable(int n) {
        return this.getDrawable(n, false);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Drawable getDrawable(int n, boolean bl) {
        Drawable drawable2;
        Context context = this.mContextRef.get();
        if (context == null) {
            return null;
        }
        Drawable drawable3 = drawable2 = ContextCompat.getDrawable(context, n);
        if (drawable2 == null) return drawable3;
        Drawable drawable4 = drawable2;
        if (Build.VERSION.SDK_INT >= 8) {
            drawable4 = drawable2.mutate();
        }
        if ((drawable3 = this.getTintList(n)) != null) {
            drawable4 = DrawableCompat.wrap(drawable4);
            DrawableCompat.setTintList(drawable4, (ColorStateList)drawable3);
            drawable2 = this.getTintMode(n);
            drawable3 = drawable4;
            if (drawable2 == null) return drawable3;
            DrawableCompat.setTintMode(drawable4, (PorterDuff.Mode)drawable2);
            return drawable4;
        }
        if (n == R.drawable.abc_cab_background_top_material) {
            return new LayerDrawable(new Drawable[]{this.getDrawable(R.drawable.abc_cab_background_internal_bg), this.getDrawable(R.drawable.abc_cab_background_top_mtrl_alpha)});
        }
        if (n == R.drawable.abc_seekbar_track_material) {
            drawable3 = (LayerDrawable)drawable4;
            TintManager.setPorterDuffColorFilter(drawable3.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
            TintManager.setPorterDuffColorFilter(drawable3.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
            TintManager.setPorterDuffColorFilter(drawable3.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
            return drawable4;
        }
        drawable3 = drawable4;
        if (this.tintDrawableUsingColorFilter(n, drawable4)) return drawable3;
        drawable3 = drawable4;
        if (!bl) return drawable3;
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public final ColorStateList getTintList(int n) {
        ColorStateList colorStateList = null;
        ColorStateList colorStateList2 = null;
        Context context = this.mContextRef.get();
        if (context == null) {
            return colorStateList2;
        }
        if (this.mTintLists != null) {
            colorStateList = (ColorStateList)this.mTintLists.get(n);
        }
        colorStateList2 = colorStateList;
        if (colorStateList != null) return colorStateList2;
        if (n == R.drawable.abc_edit_text_material) {
            colorStateList = this.createEditTextColorStateList(context);
        } else if (n == R.drawable.abc_switch_track_mtrl_alpha) {
            colorStateList = this.createSwitchTrackColorStateList(context);
        } else if (n == R.drawable.abc_switch_thumb_material) {
            colorStateList = this.createSwitchThumbColorStateList(context);
        } else if (n == R.drawable.abc_btn_default_mtrl_shape || n == R.drawable.abc_btn_borderless_material) {
            colorStateList = this.createDefaultButtonColorStateList(context);
        } else if (n == R.drawable.abc_btn_colored_material) {
            colorStateList = this.createColoredButtonColorStateList(context);
        } else if (n == R.drawable.abc_spinner_mtrl_am_alpha || n == R.drawable.abc_spinner_textfield_background_material) {
            colorStateList = this.createSpinnerColorStateList(context);
        } else if (TintManager.arrayContains(TINT_COLOR_CONTROL_NORMAL, n)) {
            colorStateList = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
        } else if (TintManager.arrayContains(TINT_COLOR_CONTROL_STATE_LIST, n)) {
            colorStateList = this.getDefaultColorStateList(context);
        } else if (TintManager.arrayContains(TINT_CHECKABLE_BUTTON_LIST, n)) {
            colorStateList = this.createCheckableButtonColorStateList(context);
        } else if (n == R.drawable.abc_seekbar_thumb_material) {
            colorStateList = this.createSeekbarThumbColorStateList(context);
        }
        colorStateList2 = colorStateList;
        if (colorStateList == null) return colorStateList2;
        if (this.mTintLists == null) {
            this.mTintLists = new SparseArray();
        }
        this.mTintLists.append(n, (Object)colorStateList);
        return colorStateList;
    }

    final PorterDuff.Mode getTintMode(int n) {
        PorterDuff.Mode mode = null;
        if (n == R.drawable.abc_switch_thumb_material) {
            mode = PorterDuff.Mode.MULTIPLY;
        }
        return mode;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public final boolean tintDrawableUsingColorFilter(int n, Drawable drawable2) {
        PorterDuff.Mode mode;
        Context context = this.mContextRef.get();
        if (context == null) {
            return false;
        }
        PorterDuff.Mode mode2 = DEFAULT_MODE;
        boolean bl = false;
        int n2 = 0;
        int n3 = -1;
        if (TintManager.arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, n)) {
            n2 = R.attr.colorControlNormal;
            bl = true;
            mode = mode2;
        } else if (TintManager.arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, n)) {
            n2 = R.attr.colorControlActivated;
            bl = true;
            mode = mode2;
        } else if (TintManager.arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, n)) {
            n2 = 16842801;
            bl = true;
            mode = PorterDuff.Mode.MULTIPLY;
        } else {
            mode = mode2;
            if (n == R.drawable.abc_list_divider_mtrl_alpha) {
                n2 = 16842800;
                bl = true;
                n3 = Math.round(40.8f);
                mode = mode2;
            }
        }
        if (!bl) return false;
        drawable2.setColorFilter((ColorFilter)TintManager.getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, n2), mode));
        if (n3 == -1) return true;
        drawable2.setAlpha(n3);
        return true;
    }

    private static class ColorFilterLruCache
    extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int n) {
            super(n);
        }

        private static int generateCacheKey(int n, PorterDuff.Mode mode) {
            return (n + 31) * 31 + mode.hashCode();
        }

        PorterDuffColorFilter get(int n, PorterDuff.Mode mode) {
            return (PorterDuffColorFilter)this.get(ColorFilterLruCache.generateCacheKey(n, mode));
        }

        PorterDuffColorFilter put(int n, PorterDuff.Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return this.put(ColorFilterLruCache.generateCacheKey(n, mode), porterDuffColorFilter);
        }
    }

}

