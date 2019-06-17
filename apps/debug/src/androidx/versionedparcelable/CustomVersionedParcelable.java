/*
 * Decompiled with CFR 0_121.
 */
package androidx.versionedparcelable;

import android.support.annotation.RestrictTo;
import androidx.versionedparcelable.VersionedParcelable;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public abstract class CustomVersionedParcelable
implements VersionedParcelable {
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void onPostParceling() {
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void onPreParceling(boolean bl) {
    }
}

