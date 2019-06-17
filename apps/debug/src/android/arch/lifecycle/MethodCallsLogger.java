/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.support.annotation.RestrictTo;
import java.util.HashMap;
import java.util.Map;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class MethodCallsLogger {
    private Map<String, Integer> mCalledMethods = new HashMap<String, Integer>();

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public boolean approveCall(String string2, int n) {
        Integer n2 = this.mCalledMethods.get(string2);
        boolean bl = false;
        int n3 = n2 != null ? n2 : 0;
        boolean bl2 = (n3 & n) != 0;
        this.mCalledMethods.put(string2, n3 | n);
        if (!bl2) {
            bl = true;
        }
        return bl;
    }
}

