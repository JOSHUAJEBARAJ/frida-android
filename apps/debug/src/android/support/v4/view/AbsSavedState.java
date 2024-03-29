/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 */
package android.support.v4.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class AbsSavedState
implements Parcelable {
    public static final Parcelable.Creator<AbsSavedState> CREATOR;
    public static final AbsSavedState EMPTY_STATE;
    private final Parcelable mSuperState;

    static {
        EMPTY_STATE = new AbsSavedState(){};
        CREATOR = new Parcelable.ClassLoaderCreator<AbsSavedState>(){

            public AbsSavedState createFromParcel(Parcel parcel) {
                return this.createFromParcel(parcel, null);
            }

            public AbsSavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                if (parcel.readParcelable(classLoader) == null) {
                    return AbsSavedState.EMPTY_STATE;
                }
                throw new IllegalStateException("superState must be null");
            }

            public AbsSavedState[] newArray(int n) {
                return new AbsSavedState[n];
            }
        };
    }

    private AbsSavedState() {
        this.mSuperState = null;
    }

    protected AbsSavedState(@NonNull Parcel parcel) {
        this(parcel, null);
    }

    protected AbsSavedState(@NonNull Parcel object, @Nullable ClassLoader classLoader) {
        object = object.readParcelable(classLoader);
        if (object == null) {
            object = EMPTY_STATE;
        }
        this.mSuperState = object;
    }

    protected AbsSavedState(@NonNull Parcelable parcelable) {
        if (parcelable != null) {
            if (parcelable == EMPTY_STATE) {
                parcelable = null;
            }
            this.mSuperState = parcelable;
            return;
        }
        throw new IllegalArgumentException("superState must not be null");
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public final Parcelable getSuperState() {
        return this.mSuperState;
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeParcelable(this.mSuperState, n);
    }

}

