/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.Uri
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 */
package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap();
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList();
    private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = new HashMap();

    static {
        mLock = new Object();
    }

    private LocalBroadcastManager(Context context) {
        this.mAppContext = context;
        this.mHandler = new Handler(context.getMainLooper()){

            public void handleMessage(Message message) {
                if (message.what != 1) {
                    super.handleMessage(message);
                    return;
                }
                LocalBroadcastManager.this.executePendingBroadcasts();
            }
        };
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @NonNull
    public static LocalBroadcastManager getInstance(@NonNull Context object) {
        Object object2 = mLock;
        synchronized (object2) {
            if (mInstance != null) return mInstance;
            mInstance = new LocalBroadcastManager(object.getApplicationContext());
            return mInstance;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    void executePendingBroadcasts() {
        block5 : do {
            Object object = this.mReceivers;
            // MONITORENTER : object
            int n = this.mPendingBroadcasts.size();
            if (n <= 0) {
                // MONITOREXIT : object
                return;
            }
            BroadcastRecord[] arrbroadcastRecord = new BroadcastRecord[n];
            this.mPendingBroadcasts.toArray(arrbroadcastRecord);
            this.mPendingBroadcasts.clear();
            // MONITOREXIT : object
            n = 0;
            do {
                if (n >= arrbroadcastRecord.length) continue block5;
                object = arrbroadcastRecord[n];
                int n2 = object.receivers.size();
                for (int i = 0; i < n2; ++i) {
                    ReceiverRecord receiverRecord = object.receivers.get(i);
                    if (receiverRecord.dead) continue;
                    receiverRecord.receiver.onReceive(this.mAppContext, object.intent);
                }
                ++n;
            } while (true);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void registerReceiver(@NonNull BroadcastReceiver arrayList, @NonNull IntentFilter intentFilter) {
        HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, (BroadcastReceiver)arrayList);
            Object object = this.mReceivers.get(arrayList);
            ArrayList arrayList2 = object;
            if (object == null) {
                arrayList2 = new ArrayList(1);
                this.mReceivers.put((BroadcastReceiver)arrayList, arrayList2);
            }
            arrayList2.add(receiverRecord);
            for (int i = 0; i < intentFilter.countActions(); ++i) {
                object = intentFilter.getAction(i);
                arrayList2 = this.mActions.get(object);
                arrayList = arrayList2;
                if (arrayList2 == null) {
                    arrayList = new ArrayList<ReceiverRecord>(1);
                    this.mActions.put((String)object, arrayList);
                }
                arrayList.add(receiverRecord);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean sendBroadcast(@NonNull Intent intent) {
        HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            Object object;
            ArrayList<ReceiverRecord> arrayList;
            String string2 = intent.getAction();
            String string3 = intent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
            Uri uri = intent.getData();
            String string4 = intent.getScheme();
            Set set = intent.getCategories();
            int n = (intent.getFlags() & 8) != 0 ? 1 : 0;
            if (n != 0) {
                object = new StringBuilder();
                object.append("Resolving type ");
                object.append(string3);
                object.append(" scheme ");
                object.append(string4);
                object.append(" of intent ");
                object.append((Object)intent);
                Log.v((String)"LocalBroadcastManager", (String)object.toString());
            }
            if ((arrayList = this.mActions.get(intent.getAction())) == null) return false;
            if (n != 0) {
                object = new StringBuilder();
                object.append("Action list: ");
                object.append(arrayList);
                Log.v((String)"LocalBroadcastManager", (String)object.toString());
            }
            Object object2 = null;
            int n2 = 0;
            while (n2 < arrayList.size()) {
                ReceiverRecord receiverRecord = arrayList.get(n2);
                if (n != 0) {
                    object = new StringBuilder();
                    object.append("Matching against filter ");
                    object.append((Object)receiverRecord.filter);
                    Log.v((String)"LocalBroadcastManager", (String)object.toString());
                }
                if (receiverRecord.broadcasting) {
                    if (n != 0) {
                        Log.v((String)"LocalBroadcastManager", (String)"  Filter's target already added");
                    }
                } else {
                    object = receiverRecord.filter;
                    Serializable serializable = object2;
                    int n3 = object.match(string2, string3, string4, uri, set, "LocalBroadcastManager");
                    if (n3 >= 0) {
                        if (n != 0) {
                            object = new StringBuilder();
                            object.append("  Filter matched!  match=0x");
                            object.append(Integer.toHexString(n3));
                            Log.v((String)"LocalBroadcastManager", (String)object.toString());
                        }
                        object = serializable;
                        if (serializable == null) {
                            object = new ArrayList();
                        }
                        object.add(receiverRecord);
                        receiverRecord.broadcasting = true;
                        object2 = object;
                    } else if (n != 0) {
                        object = n3 != -4 ? (n3 != -3 ? (n3 != -2 ? (n3 != -1 ? "unknown reason" : "type") : "data") : "action") : "category";
                        serializable = new StringBuilder();
                        serializable.append("  Filter did not match: ");
                        serializable.append((String)object);
                        Log.v((String)"LocalBroadcastManager", (String)serializable.toString());
                    }
                }
                ++n2;
            }
            return false;
        }
    }

    public void sendBroadcastSync(@NonNull Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void unregisterReceiver(@NonNull BroadcastReceiver var1_1) {
        var5_3 = this.mReceivers;
        // MONITORENTER : var5_3
        var6_4 = this.mReceivers.remove((Object)var1_1);
        if (var6_4 == null) {
            // MONITOREXIT : var5_3
            return;
        }
        var2_5 = var6_4.size() - 1;
        block3 : do {
            if (var2_5 < 0) {
                // MONITOREXIT : var5_3
                return;
            }
            var7_8 = var6_4.get(var2_5);
            var7_8.dead = true;
            var3_6 = 0;
            do {
                if (var3_6 >= var7_8.filter.countActions()) ** GOTO lbl26
                var8_9 = var7_8.filter.getAction(var3_6);
                var9_10 = this.mActions.get(var8_9);
                if (var9_10 == null) ** GOTO lbl41
                ** GOTO lbl28
lbl26: // 1 sources:
                --var2_5;
                continue block3;
lbl28: // 1 sources:
                var4_7 = var9_10.size() - 1;
                do {
                    if (var4_7 >= 0) {
                        var10_11 = var9_10.get(var4_7);
                        if (var10_11.receiver == var1_1) {
                            var10_11.dead = true;
                            var9_10.remove(var4_7);
                        }
                    } else {
                        if (var9_10.size() > 0) break;
                        this.mActions.remove(var8_9);
                        break;
                    }
                    --var4_7;
                } while (true);
lbl41: // 3 sources:
                ++var3_6;
            } while (true);
            break;
        } while (true);
    }

    private static final class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent intent, ArrayList<ReceiverRecord> arrayList) {
            this.intent = intent;
            this.receivers = arrayList;
        }
    }

    private static final class ReceiverRecord {
        boolean broadcasting;
        boolean dead;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter intentFilter, BroadcastReceiver broadcastReceiver) {
            this.filter = intentFilter;
            this.receiver = broadcastReceiver;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(128);
            stringBuilder.append("Receiver{");
            stringBuilder.append((Object)this.receiver);
            stringBuilder.append(" filter=");
            stringBuilder.append((Object)this.filter);
            if (this.dead) {
                stringBuilder.append(" DEAD");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}

