/*
 * Decompiled with CFR 0_121.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.ViewModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ViewModelStore {
    private final HashMap<String, ViewModel> mMap = new HashMap();

    public final void clear() {
        Iterator<ViewModel> iterator = this.mMap.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().onCleared();
        }
        this.mMap.clear();
    }

    final ViewModel get(String string2) {
        return this.mMap.get(string2);
    }

    final void put(String object, ViewModel viewModel) {
        if ((object = this.mMap.put((String)object, viewModel)) != null) {
            object.onCleared();
        }
    }
}

