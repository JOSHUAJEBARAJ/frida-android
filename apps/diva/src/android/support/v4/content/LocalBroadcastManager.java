/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 */
package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap();
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList();
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = new HashMap();

    static {
        mLock = new Object();
    }

    private LocalBroadcastManager(Context context) {
        this.mAppContext = context;
        this.mHandler = new Handler(context.getMainLooper()){

            public void handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        super.handleMessage(message);
                        return;
                    }
                    case 1: 
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
    private void executePendingBroadcasts() {
        block3 : do {
            BroadcastRecord[] arrbroadcastRecord;
            int n;
            Object object = this.mReceivers;
            synchronized (object) {
                n = this.mPendingBroadcasts.size();
                if (n <= 0) {
                    return;
                }
                arrbroadcastRecord = new BroadcastRecord[n];
                this.mPendingBroadcasts.toArray(arrbroadcastRecord);
                this.mPendingBroadcasts.clear();
            }
            n = 0;
            do {
                if (n >= arrbroadcastRecord.length) continue block3;
                object = arrbroadcastRecord[n];
                for (int i = 0; i < object.receivers.size(); ++i) {
                    object.receivers.get((int)i).receiver.onReceive(this.mAppContext, object.intent);
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
    public static LocalBroadcastManager getInstance(Context object) {
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
     */
    public void registerReceiver(BroadcastReceiver arrayList, IntentFilter intentFilter) {
        HashMap<BroadcastReceiver, ArrayList<IntentFilter>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, (BroadcastReceiver)arrayList);
            Object object = this.mReceivers.get(arrayList);
            ArrayList arrayList2 = object;
            if (object == null) {
                arrayList2 = new ArrayList(1);
                this.mReceivers.put((BroadcastReceiver)arrayList, arrayList2);
            }
            arrayList2.add(intentFilter);
            int n = 0;
            while (n < intentFilter.countActions()) {
                object = intentFilter.getAction(n);
                arrayList2 = this.mActions.get(object);
                arrayList = arrayList2;
                if (arrayList2 == null) {
                    arrayList = new ArrayList<ReceiverRecord>(1);
                    this.mActions.put((String)object, arrayList);
                }
                arrayList.add(receiverRecord);
                ++n;
            }
            return;
        }
    }

    /*
     * Exception decompiling
     */
    public boolean sendBroadcast(Intent var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    public void sendBroadcastSync(Intent intent) {
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
    public void unregisterReceiver(BroadcastReceiver var1_1) {
        var6_2 = this.mReceivers;
        // MONITORENTER : var6_2
        var7_3 = this.mReceivers.remove((Object)var1_1);
        if (var7_3 == null) {
            // MONITOREXIT : var6_2
            return;
        }
        var3_5 = 0;
        block3 : do {
            if (var3_5 >= var7_3.size()) {
                // MONITOREXIT : var6_2
                return;
            }
            var8_8 = var7_3.get(var3_5);
            var4_6 = 0;
            do {
                if (var4_6 >= var8_8.countActions()) ** GOTO lbl20
                var9_9 = var8_8.getAction(var4_6);
                var10_10 = this.mActions.get(var9_9);
                if (var10_10 == null) ** GOTO lbl35
                ** GOTO lbl22
lbl20: // 1 sources:
                ++var3_5;
                continue block3;
lbl22: // 1 sources:
                var2_4 = 0;
                do {
                    if (var2_4 < var10_10.size()) {
                        var5_7 = var2_4;
                        if (var10_10.get((int)var2_4).receiver == var1_1) {
                            var10_10.remove(var2_4);
                            var5_7 = var2_4 - 1;
                        }
                    } else {
                        if (var10_10.size() > 0) break;
                        this.mActions.remove(var9_9);
                        break;
                    }
                    var2_4 = var5_7 + 1;
                } while (true);
lbl35: // 3 sources:
                ++var4_6;
            } while (true);
            break;
        } while (true);
    }

    private static class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent intent, ArrayList<ReceiverRecord> arrayList) {
            this.intent = intent;
            this.receivers = arrayList;
        }
    }

    private static class ReceiverRecord {
        boolean broadcasting;
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
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}

