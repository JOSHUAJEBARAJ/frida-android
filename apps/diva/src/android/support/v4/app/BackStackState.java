/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.BackStackRecord;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManagerImpl;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

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
    }

    /*
     * Enabled aggressive block sorting
     */
    public BackStackState(BackStackRecord backStackRecord) {
        int n;
        int n2 = 0;
        BackStackRecord.Op op = backStackRecord.mHead;
        while (op != null) {
            n = n2;
            if (op.removed != null) {
                n = n2 + op.removed.size();
            }
            op = op.next;
            n2 = n;
        }
        this.mOps = new int[backStackRecord.mNumOp * 7 + n2];
        if (!backStackRecord.mAddToBackStack) {
            throw new IllegalStateException("Not on back stack");
        }
        op = backStackRecord.mHead;
        n2 = 0;
        do {
            if (op == null) {
                this.mTransition = backStackRecord.mTransition;
                this.mTransitionStyle = backStackRecord.mTransitionStyle;
                this.mName = backStackRecord.mName;
                this.mIndex = backStackRecord.mIndex;
                this.mBreadCrumbTitleRes = backStackRecord.mBreadCrumbTitleRes;
                this.mBreadCrumbTitleText = backStackRecord.mBreadCrumbTitleText;
                this.mBreadCrumbShortTitleRes = backStackRecord.mBreadCrumbShortTitleRes;
                this.mBreadCrumbShortTitleText = backStackRecord.mBreadCrumbShortTitleText;
                this.mSharedElementSourceNames = backStackRecord.mSharedElementSourceNames;
                this.mSharedElementTargetNames = backStackRecord.mSharedElementTargetNames;
                return;
            }
            int[] arrn = this.mOps;
            n = n2 + 1;
            arrn[n2] = op.cmd;
            arrn = this.mOps;
            int n3 = n + 1;
            n2 = op.fragment != null ? op.fragment.mIndex : -1;
            arrn[n] = n2;
            arrn = this.mOps;
            n2 = n3 + 1;
            arrn[n3] = op.enterAnim;
            arrn = this.mOps;
            n = n2 + 1;
            arrn[n2] = op.exitAnim;
            arrn = this.mOps;
            n2 = n + 1;
            arrn[n] = op.popEnterAnim;
            arrn = this.mOps;
            int n4 = n2 + 1;
            arrn[n2] = op.popExitAnim;
            if (op.removed != null) {
                this.mOps[n4] = n3 = op.removed.size();
                n2 = n4 + 1;
                for (n = 0; n < n3; ++n, ++n2) {
                    this.mOps[n2] = op.removed.get((int)n).mIndex;
                }
            } else {
                arrn = this.mOps;
                n2 = n4 + 1;
                arrn[n4] = 0;
            }
            op = op.next;
        } while (true);
    }

    public int describeContents() {
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    public BackStackRecord instantiate(FragmentManagerImpl fragmentManagerImpl) {
        BackStackRecord backStackRecord = new BackStackRecord(fragmentManagerImpl);
        int n = 0;
        int n2 = 0;
        do {
            if (n >= this.mOps.length) {
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
                backStackRecord.bumpBackStackNesting(1);
                return backStackRecord;
            }
            BackStackRecord.Op op = new BackStackRecord.Op();
            Object object = this.mOps;
            int n3 = n + 1;
            op.cmd = object[n];
            if (FragmentManagerImpl.DEBUG) {
                Log.v((String)"FragmentManager", (String)("Instantiate " + backStackRecord + " op #" + n2 + " base fragment #" + this.mOps[n3]));
            }
            object = this.mOps;
            n = n3 + 1;
            op.fragment = (n3 = object[n3]) >= 0 ? fragmentManagerImpl.mActive.get(n3) : null;
            object = this.mOps;
            n3 = n + 1;
            op.enterAnim = object[n];
            object = this.mOps;
            n = n3 + 1;
            op.exitAnim = object[n3];
            object = this.mOps;
            n3 = n + 1;
            op.popEnterAnim = object[n];
            object = this.mOps;
            int n4 = n3 + 1;
            op.popExitAnim = object[n3];
            object = this.mOps;
            n = n4 + 1;
            int n5 = object[n4];
            n3 = n;
            if (n5 > 0) {
                op.removed = new ArrayList(n5);
                n4 = 0;
                do {
                    n3 = n;
                    if (n4 >= n5) break;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v((String)"FragmentManager", (String)("Instantiate " + backStackRecord + " set remove fragment #" + this.mOps[n]));
                    }
                    object = fragmentManagerImpl.mActive.get(this.mOps[n]);
                    op.removed.add((Fragment)object);
                    ++n4;
                    ++n;
                } while (true);
            }
            n = n3;
            backStackRecord.addOp(op);
            ++n2;
        } while (true);
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeIntArray(this.mOps);
        parcel.writeInt(this.mTransition);
        parcel.writeInt(this.mTransitionStyle);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mIndex);
        parcel.writeInt(this.mBreadCrumbTitleRes);
        TextUtils.writeToParcel((CharSequence)this.mBreadCrumbTitleText, (Parcel)parcel, (int)0);
        parcel.writeInt(this.mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel((CharSequence)this.mBreadCrumbShortTitleText, (Parcel)parcel, (int)0);
        parcel.writeStringList(this.mSharedElementSourceNames);
        parcel.writeStringList(this.mSharedElementTargetNames);
    }

}

