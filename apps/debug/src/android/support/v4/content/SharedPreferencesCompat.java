/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 */
package android.support.v4.content;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

@Deprecated
public final class SharedPreferencesCompat {
    private SharedPreferencesCompat() {
    }

    @Deprecated
    public static final class EditorCompat {
        private static EditorCompat sInstance;
        private final Helper mHelper = new Helper();

        private EditorCompat() {
        }

        @Deprecated
        public static EditorCompat getInstance() {
            if (sInstance == null) {
                sInstance = new EditorCompat();
            }
            return sInstance;
        }

        @Deprecated
        public void apply(@NonNull SharedPreferences.Editor editor) {
            this.mHelper.apply(editor);
        }

        private static class Helper {
            Helper() {
            }

            public void apply(@NonNull SharedPreferences.Editor editor) {
                try {
                    editor.apply();
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    editor.commit();
                    return;
                }
            }
        }

    }

}

