/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.DragAndDropPermissions
 *  android.view.DragEvent
 */
package android.support.v13.view;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;

public final class DragAndDropPermissionsCompat {
    private Object mDragAndDropPermissions;

    private DragAndDropPermissionsCompat(Object object) {
        this.mDragAndDropPermissions = object;
    }

    @Nullable
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static DragAndDropPermissionsCompat request(Activity activity, DragEvent dragEvent) {
        if (Build.VERSION.SDK_INT >= 24 && (activity = activity.requestDragAndDropPermissions(dragEvent)) != null) {
            return new DragAndDropPermissionsCompat((Object)activity);
        }
        return null;
    }

    public void release() {
        if (Build.VERSION.SDK_INT >= 24) {
            ((DragAndDropPermissions)this.mDragAndDropPermissions).release();
        }
    }
}
