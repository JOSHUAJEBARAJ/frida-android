/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.BackStackState;
import android.support.v4.app.FragmentState;

final class FragmentManagerState
implements Parcelable {
    public static final Parcelable.Creator<FragmentManagerState> CREATOR = new Parcelable.Creator<FragmentManagerState>(){

        public FragmentManagerState createFromParcel(Parcel parcel) {
            return new FragmentManagerState(parcel);
        }

        public FragmentManagerState[] newArray(int n) {
            return new FragmentManagerState[n];
        }
    };
    FragmentState[] mActive;
    int[] mAdded;
    BackStackState[] mBackStack;

    public FragmentManagerState() {
    }

    public FragmentManagerState(Parcel parcel) {
        this.mActive = (FragmentState[])parcel.createTypedArray(FragmentState.CREATOR);
        this.mAdded = parcel.createIntArray();
        this.mBackStack = (BackStackState[])parcel.createTypedArray(BackStackState.CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeTypedArray(this.mActive, n);
        parcel.writeIntArray(this.mAdded);
        parcel.writeTypedArray(this.mBackStack, n);
    }

}

