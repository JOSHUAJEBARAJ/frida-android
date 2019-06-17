/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.Log
 *  android.util.SparseArray
 */
package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.BackStackRecord;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManagerImpl;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;

final class BackStackState
implements Parcelable {
    public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator<BackStackState>(){

        public BackStackState createFromParcel(Parcel parcel) {
            return new BackStackState(parcel);
        }

        public BackStackState[] newArray(int n) {
            return new BackStackState[n];
        }
    };
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int mIndex;
    final String mName;
    final int[] mOps;
    final boolean mReorderingAllowed;
    final ArrayList<String> mSharedElementSourceNames;
    final ArrayList<String> mSharedElementTargetNames;
    final int mTransition;
    final int mTransitionStyle;

    public BackStackState(Parcel parcel) {
        this.mOps = parcel.createIntArray();
        this.mTransition = parcel.readInt();
        this.mTransitionStyle = parcel.readInt();
        this.mName = parcel.readString();
        this.mIndex = parcel.readInt();
        this.mBreadCrumbTitleRes = parcel.readInt();
        this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mBreadCrumbShortTitleRes = parcel.readInt();
        this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSharedElementSourceNames = parcel.createStringArrayList();
        this.mSharedElementTargetNames = parcel.createStringArrayList();
        boolean bl = parcel.readInt() != 0;
        this.mReorderingAllowed = bl;
    }

    public BackStackState(BackStackRecord object) {
        int n = object.mOps.size();
        this.mOps = new int[n * 6];
        if (object.mAddToBackStack) {
            int n2 = 0;
            int n3 = 0;
            while (n3 < n) {
                BackStackRecord.Op op = object.mOps.get(n3);
                int[] arrn = this.mOps;
                int n4 = n2 + 1;
                arrn[n2] = op.cmd;
                arrn = this.mOps;
                int n5 = n4 + 1;
                n2 = op.fragment != null ? op.fragment.mIndex : -1;
                arrn[n4] = n2;
                arrn = this.mOps;
                n2 = n5 + 1;
                arrn[n5] = op.enterAnim;
                arrn = this.mOps;
                n4 = n2 + 1;
                arrn[n2] = op.exitAnim;
                arrn = this.mOps;
                n2 = n4 + 1;
                arrn[n4] = op.popEnterAnim;
                this.mOps[n2] = op.popExitAnim;
                ++n3;
                ++n2;
            }
            this.mTransition = object.mTransition;
            this.mTransitionStyle = object.mTransitionStyle;
            this.mName = object.mName;
            this.mIndex = object.mIndex;
            this.mBreadCrumbTitleRes = object.mBreadCrumbTitleRes;
            this.mBreadCrumbTitleText = object.mBreadCrumbTitleText;
            this.mBreadCrumbShortTitleRes = object.mBreadCrumbShortTitleRes;
            this.mBreadCrumbShortTitleText = object.mBreadCrumbShortTitleText;
            this.mSharedElementSourceNames = object.mSharedElementSourceNames;
            this.mSharedElementTargetNames = object.mSharedElementTargetNames;
            this.mReorderingAllowed = object.mReorderingAllowed;
            return;
        }
        object = new IllegalStateException("Not on back stack");
        throw object;
    }

    public int describeContents() {
        return 0;
    }

    public BackStackRecord instantiate(FragmentManagerImpl fragmentManagerImpl) {
        BackStackRecord backStackRecord = new BackStackRecord(fragmentManagerImpl);
        reference var3_3 = 0;
        int n = 0;
        while (var3_3 < this.mOps.length) {
            BackStackRecord.Op op = new BackStackRecord.Op();
            int[] arrn = this.mOps;
            reference var4_5 = var3_3 + 1;
            op.cmd = arrn[var3_3];
            if (FragmentManagerImpl.DEBUG) {
                arrn = new StringBuilder();
                arrn.append("Instantiate ");
                arrn.append(backStackRecord);
                arrn.append(" op #");
                arrn.append(n);
                arrn.append(" base fragment #");
                arrn.append(this.mOps[var4_5]);
                Log.v((String)"FragmentManager", (String)arrn.toString());
            }
            arrn = this.mOps;
            var3_3 = var4_5 + 1;
            op.fragment = (var4_5 = (reference)arrn[var4_5]) >= 0 ? (Fragment)fragmentManagerImpl.mActive.get((int)var4_5) : null;
            arrn = this.mOps;
            var4_5 = (reference)(var3_3 + 1);
            op.enterAnim = arrn[var3_3];
            var3_3 = var4_5 + true;
            op.exitAnim = arrn[var4_5];
            var4_5 = var3_3 + true;
            op.popEnterAnim = arrn[var3_3];
            op.popExitAnim = arrn[var4_5];
            backStackRecord.mEnterAnim = op.enterAnim;
            backStackRecord.mExitAnim = op.exitAnim;
            backStackRecord.mPopEnterAnim = op.popEnterAnim;
            backStackRecord.mPopExitAnim = op.popExitAnim;
            backStackRecord.addOp(op);
            ++n;
            var3_3 = var4_5 + true;
        }
        backStackRecord.mTransition = this.mTransition;
        backStackRecord.mTransitionStyle = this.mTransitionStyle;
        backStackRecord.mName = this.mName;
        backStackRecord.mIndex = this.mIndex;
        backStackRecord.mAddToBackStack = true;
        backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
        backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
        backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
        backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
        backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
        backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
        backStackRecord.mReorderingAllowed = this.mReorderingAllowed;
        backStackRecord.bumpBackStackNesting(1);
        return backStackRecord;
    }

    public void writeToParcel(Parcel parcel, int n) {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

}

