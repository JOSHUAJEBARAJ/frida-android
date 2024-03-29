/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewParent
 *  android.view.inputmethod.EditorInfo
 *  android.view.inputmethod.InputConnection
 */
package android.support.v7.widget;

import android.support.v7.widget.WithHint;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

class AppCompatHintHelper {
    private AppCompatHintHelper() {
    }

    static InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo editorInfo, View view) {
        if (inputConnection != null && editorInfo.hintText == null) {
            view = view.getParent();
            while (view instanceof View) {
                if (view instanceof WithHint) {
                    editorInfo.hintText = ((WithHint)view).getHint();
                    return inputConnection;
                }
                view = view.getParent();
            }
        }
        return inputConnection;
    }
}

