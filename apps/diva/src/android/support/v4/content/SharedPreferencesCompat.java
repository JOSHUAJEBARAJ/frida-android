/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.content;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.EditorCompatGingerbread;

public class SharedPreferencesCompat {

    public static class EditorCompat {
        private static EditorCompat sInstance;
        private final Helper mHelper;

        private EditorCompat() {
            if (Build.VERSION.SDK_INT >= 9) {
                this.mHelper = new EditorHelperApi9Impl();
                return;
            }
            this.mHelper = new EditorHelperBaseImpl();
        }

        public static EditorCompat getInstance() {
            if (sInstance == null) {
                sInstance = new EditorCompat();
            }
            return sInstance;
        }

        public void apply(@NonNull SharedPreferences.Editor editor) {
            this.mHelper.apply(editor);
        }

        private static class EditorHelperApi9Impl
        implements Helper {
            private EditorHelperApi9Impl() {
            }

            @Override
            public void apply(@NonNull SharedPreferences.Editor editor) {
                EditorCompatGingerbread.apply(editor);
            }
        }

        private static class EditorHelperBaseImpl
        implements Helper {
            private EditorHelperBaseImpl() {
            }

            @Override
            public void apply(@NonNull SharedPreferences.Editor editor) {
                editor.commit();
            }
        }

        private static interface Helper {
            public void apply(@NonNull SharedPreferences.Editor var1);
        }

    }

}

