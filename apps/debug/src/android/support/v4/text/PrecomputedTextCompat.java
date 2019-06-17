/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 *  android.text.Layout
 *  android.text.Layout$Alignment
 *  android.text.PrecomputedText
 *  android.text.PrecomputedText$Params
 *  android.text.PrecomputedText$Params$Builder
 *  android.text.Spannable
 *  android.text.SpannableString
 *  android.text.StaticLayout
 *  android.text.StaticLayout$Builder
 *  android.text.TextDirectionHeuristic
 *  android.text.TextDirectionHeuristics
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.style.MetricAffectingSpan
 */
package android.support.v4.text;

import android.graphics.Typeface;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.UiThread;
import android.support.v4.os.TraceCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.util.Preconditions;
import android.text.Layout;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class PrecomputedTextCompat
implements Spannable {
    private static final char LINE_FEED = '\n';
    @GuardedBy(value="sLock")
    @NonNull
    private static Executor sExecutor;
    private static final Object sLock;
    @NonNull
    private final int[] mParagraphEnds;
    @NonNull
    private final Params mParams;
    @NonNull
    private final Spannable mText;
    @Nullable
    private final PrecomputedText mWrapped;

    static {
        sLock = new Object();
        sExecutor = null;
    }

    @RequiresApi(value=28)
    private PrecomputedTextCompat(@NonNull PrecomputedText precomputedText, @NonNull Params params) {
        this.mText = precomputedText;
        this.mParams = params;
        this.mParagraphEnds = null;
        this.mWrapped = precomputedText;
    }

    private PrecomputedTextCompat(@NonNull CharSequence charSequence, @NonNull Params params, @NonNull int[] arrn) {
        this.mText = new SpannableString(charSequence);
        this.mParams = params;
        this.mParagraphEnds = arrn;
        this.mWrapped = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static PrecomputedTextCompat create(@NonNull CharSequence object, @NonNull Params params) {
        Preconditions.checkNotNull(object);
        Preconditions.checkNotNull(params);
        try {
            TraceCompat.beginSection("PrecomputedText");
            if (Build.VERSION.SDK_INT >= 28 && params.mWrapped != null) {
                object = new PrecomputedTextCompat(PrecomputedText.create((CharSequence)object, (PrecomputedText.Params)params.mWrapped), params);
                return object;
            }
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            int n = object.length();
            int n2 = 0;
            while (n2 < n) {
                n2 = (n2 = TextUtils.indexOf((CharSequence)object, (char)'\n', (int)n2, (int)n)) < 0 ? n : ++n2;
                arrayList.add(n2);
            }
            int[] arrn = new int[arrayList.size()];
            for (n2 = 0; n2 < arrayList.size(); ++n2) {
                arrn[n2] = (Integer)arrayList.get(n2);
            }
            if (Build.VERSION.SDK_INT >= 23) {
                StaticLayout.Builder.obtain((CharSequence)object, (int)0, (int)object.length(), (TextPaint)params.getTextPaint(), (int)Integer.MAX_VALUE).setBreakStrategy(params.getBreakStrategy()).setHyphenationFrequency(params.getHyphenationFrequency()).setTextDirection(params.getTextDirection()).build();
            } else if (Build.VERSION.SDK_INT >= 21) {
                new StaticLayout((CharSequence)object, params.getTextPaint(), Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            object = new PrecomputedTextCompat((CharSequence)object, params, arrn);
            return object;
        }
        finally {
            TraceCompat.endSection();
        }
    }

    private int findParaIndex(@IntRange(from=0) int n) {
        Object object;
        for (int i = 0; i < (object = this.mParagraphEnds).length; ++i) {
            if (n >= object[i]) continue;
            return i;
        }
        object = new StringBuilder();
        object.append("pos must be less than ");
        int[] arrn = this.mParagraphEnds;
        object.append(arrn[arrn.length - 1]);
        object.append(", gave ");
        object.append(n);
        object = new IndexOutOfBoundsException(object.toString());
        throw object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @UiThread
    public static Future<PrecomputedTextCompat> getTextFuture(@NonNull CharSequence object, @NonNull Params object2, @Nullable Executor object3) {
        object2 = new PrecomputedTextFutureTask((Params)object2, (CharSequence)object);
        object = object3;
        if (object3 == null) {
            object3 = sLock;
            synchronized (object3) {
                if (sExecutor == null) {
                    sExecutor = Executors.newFixedThreadPool(1);
                }
                object = sExecutor;
            }
        }
        object.execute((Runnable)object2);
        return object2;
    }

    public char charAt(int n) {
        return this.mText.charAt(n);
    }

    @IntRange(from=0)
    public int getParagraphCount() {
        if (Build.VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphCount();
        }
        return this.mParagraphEnds.length;
    }

    @IntRange(from=0)
    public int getParagraphEnd(@IntRange(from=0) int n) {
        Preconditions.checkArgumentInRange(n, 0, this.getParagraphCount(), "paraIndex");
        if (Build.VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphEnd(n);
        }
        return this.mParagraphEnds[n];
    }

    @IntRange(from=0)
    public int getParagraphStart(@IntRange(from=0) int n) {
        Preconditions.checkArgumentInRange(n, 0, this.getParagraphCount(), "paraIndex");
        if (Build.VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphStart(n);
        }
        if (n == 0) {
            return 0;
        }
        return this.mParagraphEnds[n - 1];
    }

    @NonNull
    public Params getParams() {
        return this.mParams;
    }

    @Nullable
    @RequiresApi(value=28)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public PrecomputedText getPrecomputedText() {
        Spannable spannable = this.mText;
        if (spannable instanceof PrecomputedText) {
            return (PrecomputedText)spannable;
        }
        return null;
    }

    public int getSpanEnd(Object object) {
        return this.mText.getSpanEnd(object);
    }

    public int getSpanFlags(Object object) {
        return this.mText.getSpanFlags(object);
    }

    public int getSpanStart(Object object) {
        return this.mText.getSpanStart(object);
    }

    public <T> T[] getSpans(int n, int n2, Class<T> class_) {
        if (Build.VERSION.SDK_INT >= 28) {
            return this.mWrapped.getSpans(n, n2, class_);
        }
        return this.mText.getSpans(n, n2, class_);
    }

    public int length() {
        return this.mText.length();
    }

    public int nextSpanTransition(int n, int n2, Class class_) {
        return this.mText.nextSpanTransition(n, n2, class_);
    }

    public void removeSpan(Object object) {
        if (!(object instanceof MetricAffectingSpan)) {
            if (Build.VERSION.SDK_INT >= 28) {
                this.mWrapped.removeSpan(object);
                return;
            }
            this.mText.removeSpan(object);
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
    }

    public void setSpan(Object object, int n, int n2, int n3) {
        if (!(object instanceof MetricAffectingSpan)) {
            if (Build.VERSION.SDK_INT >= 28) {
                this.mWrapped.setSpan(object, n, n2, n3);
                return;
            }
            this.mText.setSpan(object, n, n2, n3);
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
    }

    public CharSequence subSequence(int n, int n2) {
        return this.mText.subSequence(n, n2);
    }

    public String toString() {
        return this.mText.toString();
    }

    public static final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        @NonNull
        private final TextPaint mPaint;
        @Nullable
        private final TextDirectionHeuristic mTextDir;
        final PrecomputedText.Params mWrapped;

        @RequiresApi(value=28)
        public Params(@NonNull PrecomputedText.Params params) {
            this.mPaint = params.getTextPaint();
            this.mTextDir = params.getTextDirection();
            this.mBreakStrategy = params.getBreakStrategy();
            this.mHyphenationFrequency = params.getHyphenationFrequency();
            this.mWrapped = params;
        }

        Params(@NonNull TextPaint textPaint, @NonNull TextDirectionHeuristic textDirectionHeuristic, int n, int n2) {
            this.mWrapped = Build.VERSION.SDK_INT >= 28 ? new PrecomputedText.Params.Builder(textPaint).setBreakStrategy(n).setHyphenationFrequency(n2).setTextDirection(textDirectionHeuristic).build() : null;
            this.mPaint = textPaint;
            this.mTextDir = textDirectionHeuristic;
            this.mBreakStrategy = n;
            this.mHyphenationFrequency = n2;
        }

        public boolean equals(@Nullable Object object) {
            if (object == this) {
                return true;
            }
            if (object != null) {
                if (!(object instanceof Params)) {
                    return false;
                }
                object = (Params)object;
                PrecomputedText.Params params = this.mWrapped;
                if (params != null) {
                    return params.equals((Object)object.mWrapped);
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    if (this.mBreakStrategy != object.getBreakStrategy()) {
                        return false;
                    }
                    if (this.mHyphenationFrequency != object.getHyphenationFrequency()) {
                        return false;
                    }
                }
                if (Build.VERSION.SDK_INT >= 18 && this.mTextDir != object.getTextDirection()) {
                    return false;
                }
                if (this.mPaint.getTextSize() != object.getTextPaint().getTextSize()) {
                    return false;
                }
                if (this.mPaint.getTextScaleX() != object.getTextPaint().getTextScaleX()) {
                    return false;
                }
                if (this.mPaint.getTextSkewX() != object.getTextPaint().getTextSkewX()) {
                    return false;
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    if (this.mPaint.getLetterSpacing() != object.getTextPaint().getLetterSpacing()) {
                        return false;
                    }
                    if (!TextUtils.equals((CharSequence)this.mPaint.getFontFeatureSettings(), (CharSequence)object.getTextPaint().getFontFeatureSettings())) {
                        return false;
                    }
                }
                if (this.mPaint.getFlags() != object.getTextPaint().getFlags()) {
                    return false;
                }
                if (Build.VERSION.SDK_INT >= 24 ? !this.mPaint.getTextLocales().equals((Object)object.getTextPaint().getTextLocales()) : Build.VERSION.SDK_INT >= 17 && !this.mPaint.getTextLocale().equals(object.getTextPaint().getTextLocale())) {
                    return false;
                }
                if (this.mPaint.getTypeface() == null ? object.getTextPaint().getTypeface() != null : !this.mPaint.getTypeface().equals((Object)object.getTextPaint().getTypeface())) {
                    return false;
                }
                return true;
            }
            return false;
        }

        @RequiresApi(value=23)
        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        @RequiresApi(value=23)
        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        @Nullable
        @RequiresApi(value=18)
        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        @NonNull
        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        public int hashCode() {
            if (Build.VERSION.SDK_INT >= 24) {
                return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), this.mPaint.getFlags(), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
            }
            if (Build.VERSION.SDK_INT >= 21) {
                return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
            }
            if (Build.VERSION.SDK_INT >= 18) {
                return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
            }
            if (Build.VERSION.SDK_INT >= 17) {
                return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
            }
            return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{");
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("textSize=");
            stringBuilder2.append(this.mPaint.getTextSize());
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textScaleX=");
            stringBuilder2.append(this.mPaint.getTextScaleX());
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textSkewX=");
            stringBuilder2.append(this.mPaint.getTextSkewX());
            stringBuilder.append(stringBuilder2.toString());
            if (Build.VERSION.SDK_INT >= 21) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", letterSpacing=");
                stringBuilder2.append(this.mPaint.getLetterSpacing());
                stringBuilder.append(stringBuilder2.toString());
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", elegantTextHeight=");
                stringBuilder2.append(this.mPaint.isElegantTextHeight());
                stringBuilder.append(stringBuilder2.toString());
            }
            if (Build.VERSION.SDK_INT >= 24) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", textLocale=");
                stringBuilder2.append((Object)this.mPaint.getTextLocales());
                stringBuilder.append(stringBuilder2.toString());
            } else if (Build.VERSION.SDK_INT >= 17) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", textLocale=");
                stringBuilder2.append(this.mPaint.getTextLocale());
                stringBuilder.append(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", typeface=");
            stringBuilder2.append((Object)this.mPaint.getTypeface());
            stringBuilder.append(stringBuilder2.toString());
            if (Build.VERSION.SDK_INT >= 26) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", variationSettings=");
                stringBuilder2.append(this.mPaint.getFontVariationSettings());
                stringBuilder.append(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textDir=");
            stringBuilder2.append((Object)this.mTextDir);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", breakStrategy=");
            stringBuilder2.append(this.mBreakStrategy);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", hyphenationFrequency=");
            stringBuilder2.append(this.mHyphenationFrequency);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public static class Builder {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            @NonNull
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;

            public Builder(@NonNull TextPaint textPaint) {
                this.mPaint = textPaint;
                if (Build.VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                } else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                if (Build.VERSION.SDK_INT >= 18) {
                    this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                    return;
                }
                this.mTextDir = null;
            }

            @NonNull
            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }

            @RequiresApi(value=23)
            public Builder setBreakStrategy(int n) {
                this.mBreakStrategy = n;
                return this;
            }

            @RequiresApi(value=23)
            public Builder setHyphenationFrequency(int n) {
                this.mHyphenationFrequency = n;
                return this;
            }

            @RequiresApi(value=18)
            public Builder setTextDirection(@NonNull TextDirectionHeuristic textDirectionHeuristic) {
                this.mTextDir = textDirectionHeuristic;
                return this;
            }
        }

    }

    private static class PrecomputedTextFutureTask
    extends FutureTask<PrecomputedTextCompat> {
        PrecomputedTextFutureTask(@NonNull Params params, @NonNull CharSequence charSequence) {
            super(new PrecomputedTextCallback(params, charSequence));
        }

        private static class PrecomputedTextCallback
        implements Callable<PrecomputedTextCompat> {
            private Params mParams;
            private CharSequence mText;

            PrecomputedTextCallback(@NonNull Params params, @NonNull CharSequence charSequence) {
                this.mParams = params;
                this.mText = charSequence;
            }

            @Override
            public PrecomputedTextCompat call() throws Exception {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }
        }

    }

}

