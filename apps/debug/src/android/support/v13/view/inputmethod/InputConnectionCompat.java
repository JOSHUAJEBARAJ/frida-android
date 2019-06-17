/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ClipDescription
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.os.ResultReceiver
 *  android.text.TextUtils
 *  android.view.inputmethod.EditorInfo
 *  android.view.inputmethod.InputConnection
 *  android.view.inputmethod.InputConnectionWrapper
 *  android.view.inputmethod.InputContentInfo
 */
package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    public static boolean commitContent(@NonNull InputConnection inputConnection, @NonNull EditorInfo bundle, @NonNull InputContentInfoCompat inputContentInfoCompat, int n, @Nullable Bundle bundle2) {
        boolean bl;
        ClipDescription clipDescription = inputContentInfoCompat.getDescription();
        boolean bl2 = false;
        bundle = EditorInfoCompat.getContentMimeTypes((EditorInfo)bundle);
        int n2 = bundle.length;
        int n3 = 0;
        do {
            bl = bl2;
            if (n3 >= n2) break;
            if (clipDescription.hasMimeType(bundle[n3])) {
                bl = true;
                break;
            }
            ++n3;
        } while (true);
        if (!bl) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo)inputContentInfoCompat.unwrap(), n, bundle2);
        }
        bundle = new Bundle();
        bundle.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI", (Parcelable)inputContentInfoCompat.getContentUri());
        bundle.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION", (Parcelable)inputContentInfoCompat.getDescription());
        bundle.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI", (Parcelable)inputContentInfoCompat.getLinkUri());
        bundle.putInt("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS", n);
        bundle.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS", (Parcelable)bundle2);
        return inputConnection.performPrivateCommand("android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", bundle);
    }

    @NonNull
    public static InputConnection createWrapper(@NonNull InputConnection inputConnection, @NonNull EditorInfo editorInfo, final @NonNull OnCommitContentListener onCommitContentListener) {
        if (inputConnection != null) {
            if (editorInfo != null) {
                if (onCommitContentListener != null) {
                    if (Build.VERSION.SDK_INT >= 25) {
                        return new InputConnectionWrapper(inputConnection, false){

                            public boolean commitContent(InputContentInfo inputContentInfo, int n, Bundle bundle) {
                                if (onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap((Object)inputContentInfo), n, bundle)) {
                                    return true;
                                }
                                return super.commitContent(inputContentInfo, n, bundle);
                            }
                        };
                    }
                    if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
                        return inputConnection;
                    }
                    return new InputConnectionWrapper(inputConnection, false){

                        public boolean performPrivateCommand(String string2, Bundle bundle) {
                            if (InputConnectionCompat.handlePerformPrivateCommand(string2, bundle, onCommitContentListener)) {
                                return true;
                            }
                            return super.performPrivateCommand(string2, bundle);
                        }
                    };
                }
                throw new IllegalArgumentException("onCommitContentListener must be non-null");
            }
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        throw new IllegalArgumentException("inputConnection must be non-null");
    }

    static boolean handlePerformPrivateCommand(@Nullable String string2, @NonNull Bundle bundle, @NonNull OnCommitContentListener onCommitContentListener) {
        boolean bl;
        block12 : {
            ResultReceiver resultReceiver;
            int n;
            block13 : {
                bl = TextUtils.equals((CharSequence)"android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", (CharSequence)string2);
                int n2 = 0;
                n = 0;
                if (!bl) {
                    return false;
                }
                if (bundle == null) {
                    return false;
                }
                string2 = null;
                try {
                    resultReceiver = (ResultReceiver)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
                    string2 = resultReceiver;
                }
                catch (Throwable throwable) {
                    if (string2 != null) {
                        n = n2;
                        if (false) {
                            n = 1;
                        }
                        string2.send(n, null);
                    }
                    throw throwable;
                }
                Uri uri = (Uri)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI");
                string2 = resultReceiver;
                ClipDescription clipDescription = (ClipDescription)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION");
                string2 = resultReceiver;
                Uri uri2 = (Uri)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI");
                string2 = resultReceiver;
                int n3 = bundle.getInt("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS");
                string2 = resultReceiver;
                bundle = (Bundle)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS");
                string2 = resultReceiver;
                bl = onCommitContentListener.onCommitContent(new InputContentInfoCompat(uri, clipDescription, uri2), n3, bundle);
                if (resultReceiver == null) break block12;
                if (!bl) break block13;
                n = 1;
            }
            resultReceiver.send(n, null);
        }
        return bl;
    }

    public static interface OnCommitContentListener {
        public boolean onCommitContent(InputContentInfoCompat var1, int var2, Bundle var3);
    }

}

