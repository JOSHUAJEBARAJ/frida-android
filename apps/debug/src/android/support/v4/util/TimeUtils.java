/*
 * Decompiled with CFR 0_121.
 */
package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.io.PrintWriter;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public final class TimeUtils {
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;

    static {
        sFormatSync = new Object();
        sFormatStr = new char[24];
    }

    private TimeUtils() {
    }

    private static int accumField(int n, int n2, boolean bl, int n3) {
        if (!(n > 99 || bl && n3 >= 3)) {
            if (!(n > 9 || bl && n3 >= 2)) {
                if (!bl && n <= 0) {
                    return 0;
                }
                return n2 + 1;
            }
            return n2 + 2;
        }
        return n2 + 3;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void formatDuration(long l, long l2, PrintWriter printWriter) {
        if (l == 0) {
            printWriter.print("--");
            return;
        }
        TimeUtils.formatDuration(l - l2, printWriter, 0);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void formatDuration(long l, PrintWriter printWriter) {
        TimeUtils.formatDuration(l, printWriter, 0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void formatDuration(long l, PrintWriter printWriter, int n) {
        Object object = sFormatSync;
        synchronized (object) {
            n = TimeUtils.formatDurationLocked(l, n);
            printWriter.print(new String(sFormatStr, 0, n));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void formatDuration(long l, StringBuilder stringBuilder) {
        Object object = sFormatSync;
        synchronized (object) {
            int n = TimeUtils.formatDurationLocked(l, 0);
            stringBuilder.append(sFormatStr, 0, n);
            return;
        }
    }

    private static int formatDurationLocked(long l, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        if (sFormatStr.length < n) {
            sFormatStr = new char[n];
        }
        char[] arrc = sFormatStr;
        if (l == 0) {
            while (n - 1 < 0) {
                arrc[0] = 32;
            }
            arrc[0] = 48;
            return 0 + 1;
        }
        if (l > 0) {
            n6 = 43;
        } else {
            l = - l;
            n6 = 45;
        }
        int n8 = (int)(l % 1000);
        int n9 = (int)Math.floor(l / 1000);
        if (n9 > 86400) {
            n4 = n9 / 86400;
            n9 -= 86400 * n4;
        } else {
            n4 = 0;
        }
        if (n9 > 3600) {
            n3 = n9 / 3600;
            n9 -= n3 * 3600;
        } else {
            n3 = 0;
        }
        if (n9 > 60) {
            n5 = n9 / 60;
            n2 = n9 - n5 * 60;
        } else {
            n5 = 0;
            n2 = n9;
        }
        int n10 = 0;
        int n11 = 0;
        int n12 = 3;
        boolean bl = false;
        if (n != 0) {
            n9 = TimeUtils.accumField(n4, 1, false, 0);
            if (n9 > 0) {
                bl = true;
            }
            bl = (n9 += TimeUtils.accumField(n3, 1, bl, 2)) > 0;
            bl = (n9 += TimeUtils.accumField(n5, 1, bl, 2)) > 0;
            n7 = n9 + TimeUtils.accumField(n2, 1, bl, 2);
            n9 = n7 > 0 ? 3 : 0;
            n7 += TimeUtils.accumField(n8, 2, true, n9) + 1;
            n9 = n11;
            do {
                n10 = n9++;
                if (n7 >= n) break;
                arrc[n9] = 32;
                ++n7;
            } while (true);
        }
        arrc[n10] = n6;
        n = n != 0 ? 1 : 0;
        boolean bl2 = true;
        n7 = 2;
        n4 = TimeUtils.printField(arrc, n4, 'd', ++n10, false, 0);
        bl = n4 != n10;
        n9 = n != 0 ? 2 : 0;
        n4 = TimeUtils.printField(arrc, n3, 'h', n4, bl, n9);
        bl = n4 != n10;
        n9 = n != 0 ? 2 : 0;
        n4 = TimeUtils.printField(arrc, n5, 'm', n4, bl, n9);
        bl = n4 != n10 ? bl2 : false;
        n9 = n != 0 ? n7 : 0;
        n9 = TimeUtils.printField(arrc, n2, 's', n4, bl, n9);
        n = n != 0 && n9 != n10 ? n12 : 0;
        n = TimeUtils.printField(arrc, n8, 'm', n9, true, n);
        arrc[n] = 115;
        return n + 1;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static int printField(char[] var0, int var1_1, char var2_2, int var3_3, boolean var4_4, int var5_5) {
        if (!var4_4) {
            var6_6 = var3_3;
            if (var1_1 <= 0) return var6_6;
        }
        if (var4_4 && var5_5 >= 3) ** GOTO lbl-1000
        var6_6 = var1_1;
        var7_7 = var3_3;
        if (var1_1 > 99) lbl-1000: // 2 sources:
        {
            var6_6 = var1_1 / 100;
            var0[var3_3] = (char)(var6_6 + 48);
            var7_7 = var3_3 + 1;
            var6_6 = var1_1 - var6_6 * 100;
        }
        if (var4_4 && var5_5 >= 2 || var6_6 > 9) ** GOTO lbl-1000
        var5_5 = var6_6;
        var1_1 = var7_7;
        if (var3_3 != var7_7) lbl-1000: // 2 sources:
        {
            var3_3 = var6_6 / 10;
            var0[var7_7] = (char)(var3_3 + 48);
            var1_1 = var7_7 + 1;
            var5_5 = var6_6 - var3_3 * 10;
        }
        var0[var1_1] = (char)(var5_5 + 48);
        var0[++var1_1] = var2_2;
        return var1_1 + 1;
    }
}

