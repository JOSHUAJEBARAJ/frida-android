/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Color
 *  android.util.AttributeSet
 *  android.util.StateSet
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.compat.R;
import android.support.v4.content.res.GrowingArrayUtils;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public final class ColorStateListInflaterCompat {
    private static final int DEFAULT_COLOR = -65536;

    private ColorStateListInflaterCompat() {
    }

    @NonNull
    public static ColorStateList createFromXml(@NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlPullParser);
        while ((n = xmlPullParser.next()) != 2 && n != 1) {
        }
        if (n == 2) {
            return ColorStateListInflaterCompat.createFromXmlInner(resources, xmlPullParser, attributeSet, theme);
        }
        resources = new XmlPullParserException("No start tag found");
        throw resources;
    }

    @NonNull
    public static ColorStateList createFromXmlInner(@NonNull Resources object, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        String string2 = xmlPullParser.getName();
        if (string2.equals("selector")) {
            return ColorStateListInflaterCompat.inflate((Resources)object, xmlPullParser, attributeSet, theme);
        }
        object = new StringBuilder();
        object.append(xmlPullParser.getPositionDescription());
        object.append(": invalid color state list tag ");
        object.append(string2);
        throw new XmlPullParserException(object.toString());
    }

    private static ColorStateList inflate(@NonNull Resources arrn, @NonNull XmlPullParser arrarrn, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        int n2;
        int n3 = arrarrn.getDepth() + 1;
        int n4 = -65536;
        int[][] arrarrn2 = new int[20][];
        int[] arrn2 = new int[arrarrn2.length];
        int n5 = 0;
        while ((n = arrarrn.next()) != 1 && ((n2 = arrarrn.getDepth()) >= n3 || n != 3)) {
            if (n != 2 || n2 > n3 || !arrarrn.getName().equals("item")) continue;
            int[] arrn3 = ColorStateListInflaterCompat.obtainAttributes((Resources)arrn, theme, attributeSet, R.styleable.ColorStateListItem);
            int n6 = arrn3.getColor(R.styleable.ColorStateListItem_android_color, -65281);
            float f = 1.0f;
            if (arrn3.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
                f = arrn3.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0f);
            } else if (arrn3.hasValue(R.styleable.ColorStateListItem_alpha)) {
                f = arrn3.getFloat(R.styleable.ColorStateListItem_alpha, 1.0f);
            }
            arrn3.recycle();
            n2 = attributeSet.getAttributeCount();
            arrn3 = new int[n2];
            n = 0;
            for (int i = 0; i < n2; ++i) {
                int n7 = attributeSet.getAttributeNameResource(i);
                int n8 = n;
                if (n7 != 16843173) {
                    n8 = n;
                    if (n7 != 16843551) {
                        n8 = n;
                        if (n7 != R.attr.alpha) {
                            n8 = attributeSet.getAttributeBooleanValue(i, false) ? n7 : - n7;
                            arrn3[n] = n8;
                            n8 = n + 1;
                        }
                    }
                }
                n = n8;
            }
            arrn3 = StateSet.trimStateSet((int[])arrn3, (int)n);
            n = ColorStateListInflaterCompat.modulateColorAlpha(n6, f);
            if (n5 == 0 || arrn3.length == 0) {
                n4 = n;
            }
            arrn2 = GrowingArrayUtils.append(arrn2, n5, n);
            arrarrn2 = GrowingArrayUtils.append(arrarrn2, n5, arrn3);
            ++n5;
        }
        arrn = new int[n5];
        arrarrn = new int[n5][];
        System.arraycopy((Object)arrn2, 0, (Object)arrn, 0, n5);
        System.arraycopy(arrarrn2, 0, arrarrn, 0, n5);
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    @ColorInt
    private static int modulateColorAlpha(@ColorInt int n, @FloatRange(from=0.0, to=1.0) float f) {
        return 16777215 & n | Math.round((float)Color.alpha((int)n) * f) << 24;
    }

    private static TypedArray obtainAttributes(Resources resources, Resources.Theme theme, AttributeSet attributeSet, int[] arrn) {
        if (theme == null) {
            return resources.obtainAttributes(attributeSet, arrn);
        }
        return theme.obtainStyledAttributes(attributeSet, arrn, 0, 0);
    }
}

