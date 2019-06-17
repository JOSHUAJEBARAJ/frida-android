/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.RemoteInput
 *  android.app.RemoteInput$Builder
 *  android.content.ClipData
 *  android.content.ClipData$Item
 *  android.content.ClipDescription
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 */
package android.support.v4.app;

import android.app.RemoteInput;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class RemoteInput {
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    private static final String TAG = "RemoteInput";
    private final boolean mAllowFreeFormTextInput;
    private final Set<String> mAllowedDataTypes;
    private final CharSequence[] mChoices;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;

    RemoteInput(String string2, CharSequence charSequence, CharSequence[] arrcharSequence, boolean bl, Bundle bundle, Set<String> set) {
        this.mResultKey = string2;
        this.mLabel = charSequence;
        this.mChoices = arrcharSequence;
        this.mAllowFreeFormTextInput = bl;
        this.mExtras = bundle;
        this.mAllowedDataTypes = set;
    }

    public static void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> object) {
        if (Build.VERSION.SDK_INT >= 26) {
            android.app.RemoteInput.addDataResultToIntent((android.app.RemoteInput)RemoteInput.fromCompat(remoteInput), (Intent)intent, object);
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            Intent intent2;
            Intent intent3 = intent2 = RemoteInput.getClipDataIntentFromIntent(intent);
            if (intent2 == null) {
                intent3 = new Intent();
            }
            Iterator<Map.Entry<String, Uri>> iterator = object.entrySet().iterator();
            while (iterator.hasNext()) {
                object = iterator.next();
                String string2 = (String)object.getKey();
                Uri uri = (Uri)object.getValue();
                if (string2 == null) continue;
                intent2 = intent3.getBundleExtra(RemoteInput.getExtraResultsKeyForData(string2));
                object = intent2;
                if (intent2 == null) {
                    object = new Bundle();
                }
                object.putString(remoteInput.getResultKey(), uri.toString());
                intent3.putExtra(RemoteInput.getExtraResultsKeyForData(string2), (Bundle)object);
            }
            intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", (Intent)intent3));
            return;
        }
        Log.w((String)"RemoteInput", (String)"RemoteInput is only supported from API Level 16");
    }

    public static void addResultsToIntent(RemoteInput[] arrremoteInput, Intent intent, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 26) {
            android.app.RemoteInput.addResultsToIntent((android.app.RemoteInput[])RemoteInput.fromCompat(arrremoteInput), (Intent)intent, (Bundle)bundle);
            return;
        }
        int n = Build.VERSION.SDK_INT;
        int n2 = 0;
        if (n >= 20) {
            Bundle object2 = RemoteInput.getResultsFromIntent(intent);
            if (object2 != null) {
                object2.putAll(bundle);
                bundle = object2;
            }
            for (RemoteInput remoteInput : arrremoteInput) {
                Map<String, Uri> map = RemoteInput.getDataResultsFromIntent(intent, remoteInput.getResultKey());
                android.app.RemoteInput.addResultsToIntent((android.app.RemoteInput[])RemoteInput.fromCompat(new RemoteInput[]{remoteInput}), (Intent)intent, (Bundle)bundle);
                if (map == null) continue;
                RemoteInput.addDataResultToIntent(remoteInput, intent, map);
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            void var5_10;
            Intent intent2;
            Intent intent3 = intent2 = RemoteInput.getClipDataIntentFromIntent(intent);
            if (intent2 == null) {
                Intent intent4 = new Intent();
            }
            Object object = var5_10.getBundleExtra("android.remoteinput.resultsData");
            intent2 = object;
            if (object == null) {
                intent2 = new Bundle();
            }
            n = arrremoteInput.length;
            while (n2 < n) {
                object = arrremoteInput[n2];
                Object object2 = bundle.get(object.getResultKey());
                if (object2 instanceof CharSequence) {
                    intent2.putCharSequence(object.getResultKey(), (CharSequence)object2);
                }
                ++n2;
            }
            var5_10.putExtra("android.remoteinput.resultsData", (Bundle)intent2);
            intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", (Intent)var5_10));
            return;
        }
        Log.w((String)"RemoteInput", (String)"RemoteInput is only supported from API Level 16");
    }

    @RequiresApi(value=20)
    static android.app.RemoteInput fromCompat(RemoteInput remoteInput) {
        return new RemoteInput.Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build();
    }

    @RequiresApi(value=20)
    static android.app.RemoteInput[] fromCompat(RemoteInput[] arrremoteInput) {
        if (arrremoteInput == null) {
            return null;
        }
        android.app.RemoteInput[] arrremoteInput2 = new android.app.RemoteInput[arrremoteInput.length];
        for (int i = 0; i < arrremoteInput.length; ++i) {
            arrremoteInput2[i] = RemoteInput.fromCompat(arrremoteInput[i]);
        }
        return arrremoteInput2;
    }

    @RequiresApi(value=16)
    private static Intent getClipDataIntentFromIntent(Intent intent) {
        if ((intent = intent.getClipData()) == null) {
            return null;
        }
        ClipDescription clipDescription = intent.getDescription();
        if (!clipDescription.hasMimeType("text/vnd.android.intent")) {
            return null;
        }
        if (!clipDescription.getLabel().equals("android.remoteinput.results")) {
            return null;
        }
        return intent.getItemAt(0).getIntent();
    }

    public static Map<String, Uri> getDataResultsFromIntent(Intent intent, String string2) {
        if (Build.VERSION.SDK_INT >= 26) {
            return android.app.RemoteInput.getDataResultsFromIntent((Intent)intent, (String)string2);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            if ((intent = RemoteInput.getClipDataIntentFromIntent(intent)) == null) {
                return null;
            }
            HashMap<String, Uri> hashMap = new HashMap<String, Uri>();
            for (String string3 : intent.getExtras().keySet()) {
                String string4;
                if (!string3.startsWith("android.remoteinput.dataTypeResultsData") || (string4 = string3.substring("android.remoteinput.dataTypeResultsData".length())).isEmpty() || (string3 = intent.getBundleExtra(string3).getString(string2)) == null || string3.isEmpty()) continue;
                hashMap.put(string4, Uri.parse((String)string3));
            }
            if (hashMap.isEmpty()) {
                return null;
            }
            return hashMap;
        }
        Log.w((String)"RemoteInput", (String)"RemoteInput is only supported from API Level 16");
        return null;
    }

    private static String getExtraResultsKeyForData(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("android.remoteinput.dataTypeResultsData");
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    public static Bundle getResultsFromIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= 20) {
            return android.app.RemoteInput.getResultsFromIntent((Intent)intent);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            if ((intent = RemoteInput.getClipDataIntentFromIntent(intent)) == null) {
                return null;
            }
            return (Bundle)intent.getExtras().getParcelable("android.remoteinput.resultsData");
        }
        Log.w((String)"RemoteInput", (String)"RemoteInput is only supported from API Level 16");
        return null;
    }

    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormTextInput;
    }

    public Set<String> getAllowedDataTypes() {
        return this.mAllowedDataTypes;
    }

    public CharSequence[] getChoices() {
        return this.mChoices;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public String getResultKey() {
        return this.mResultKey;
    }

    public boolean isDataOnly() {
        if (!(this.getAllowFreeFormInput() || this.getChoices() != null && this.getChoices().length != 0 || this.getAllowedDataTypes() == null || this.getAllowedDataTypes().isEmpty())) {
            return true;
        }
        return false;
    }

    public static final class Builder {
        private boolean mAllowFreeFormTextInput = true;
        private final Set<String> mAllowedDataTypes = new HashSet<String>();
        private CharSequence[] mChoices;
        private final Bundle mExtras = new Bundle();
        private CharSequence mLabel;
        private final String mResultKey;

        public Builder(@NonNull String string2) {
            if (string2 != null) {
                this.mResultKey = string2;
                return;
            }
            throw new IllegalArgumentException("Result key can't be null");
        }

        @NonNull
        public Builder addExtras(@NonNull Bundle bundle) {
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            return this;
        }

        @NonNull
        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mExtras, this.mAllowedDataTypes);
        }

        @NonNull
        public Bundle getExtras() {
            return this.mExtras;
        }

        @NonNull
        public Builder setAllowDataType(@NonNull String string2, boolean bl) {
            if (bl) {
                this.mAllowedDataTypes.add(string2);
                return this;
            }
            this.mAllowedDataTypes.remove(string2);
            return this;
        }

        @NonNull
        public Builder setAllowFreeFormInput(boolean bl) {
            this.mAllowFreeFormTextInput = bl;
            return this;
        }

        @NonNull
        public Builder setChoices(@Nullable CharSequence[] arrcharSequence) {
            this.mChoices = arrcharSequence;
            return this;
        }

        @NonNull
        public Builder setLabel(@Nullable CharSequence charSequence) {
            this.mLabel = charSequence;
            return this;
        }
    }

}

