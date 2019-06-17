/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.Window
 */
package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;

public class AppCompatDialogFragment
extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return new AppCompatDialog(this.getContext(), this.getTheme());
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public void setupDialog(Dialog dialog, int n) {
        if (dialog instanceof AppCompatDialog) {
            block3 : {
                AppCompatDialog appCompatDialog = (AppCompatDialog)dialog;
                if (n != 1 && n != 2) {
                    if (n != 3) break block3;
                    dialog.getWindow().addFlags(24);
                }
                appCompatDialog.supportRequestWindowFeature(1);
            }
            return;
        }
        super.setupDialog(dialog, n);
    }
}

