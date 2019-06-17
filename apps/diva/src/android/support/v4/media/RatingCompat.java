/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 */
package android.support.v4.media;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.RatingCompatApi21;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class RatingCompat
implements Parcelable {
    public static final Parcelable.Creator<RatingCompat> CREATOR = new Parcelable.Creator<RatingCompat>(){

        public RatingCompat createFromParcel(Parcel parcel) {
            return new RatingCompat(parcel.readInt(), parcel.readFloat());
        }

        public RatingCompat[] newArray(int n) {
            return new RatingCompat[n];
        }
    };
    public static final int RATING_3_STARS = 3;
    public static final int RATING_4_STARS = 4;
    public static final int RATING_5_STARS = 5;
    public static final int RATING_HEART = 1;
    public static final int RATING_NONE = 0;
    private static final float RATING_NOT_RATED = -1.0f;
    public static final int RATING_PERCENTAGE = 6;
    public static final int RATING_THUMB_UP_DOWN = 2;
    private static final String TAG = "Rating";
    private Object mRatingObj;
    private final int mRatingStyle;
    private final float mRatingValue;

    private RatingCompat(int n, float f) {
        this.mRatingStyle = n;
        this.mRatingValue = f;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static RatingCompat fromRating(Object var0) {
        if (var0 == null) return null;
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        var1_1 = RatingCompatApi21.getRatingStyle(var0);
        if (!RatingCompatApi21.isRated(var0)) ** GOTO lbl21
        switch (var1_1) {
            default: {
                return null;
            }
            case 1: {
                var2_2 = RatingCompat.newHeartRating(RatingCompatApi21.hasHeart(var0));
                ** GOTO lbl22
            }
            case 2: {
                var2_2 = RatingCompat.newThumbRating(RatingCompatApi21.isThumbUp(var0));
                ** GOTO lbl22
            }
            case 3: 
            case 4: 
            case 5: {
                var2_2 = RatingCompat.newStarRating(var1_1, RatingCompatApi21.getStarRating(var0));
                ** GOTO lbl22
            }
            case 6: 
        }
        var2_2 = RatingCompat.newPercentageRating(RatingCompatApi21.getPercentRating(var0));
        ** GOTO lbl22
lbl21: // 1 sources:
        var2_2 = RatingCompat.newUnratedRating(var1_1);
lbl22: // 5 sources:
        var2_2.mRatingObj = var0;
        return var2_2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static RatingCompat newHeartRating(boolean bl) {
        float f;
        if (bl) {
            f = 1.0f;
            do {
                return new RatingCompat(1, f);
                break;
            } while (true);
        }
        f = 0.0f;
        return new RatingCompat(1, f);
    }

    public static RatingCompat newPercentageRating(float f) {
        if (f < 0.0f || f > 100.0f) {
            Log.e((String)"Rating", (String)"Invalid percentage-based rating value");
            return null;
        }
        return new RatingCompat(6, f);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static RatingCompat newStarRating(int n, float f) {
        float f2;
        switch (n) {
            default: {
                Log.e((String)"Rating", (String)("Invalid rating style (" + n + ") for a star rating"));
                return null;
            }
            case 3: {
                f2 = 3.0f;
                break;
            }
            case 4: {
                f2 = 4.0f;
                break;
            }
            case 5: {
                f2 = 5.0f;
            }
        }
        if (f < 0.0f || f > f2) {
            Log.e((String)"Rating", (String)"Trying to set out of range star-based rating");
            return null;
        }
        return new RatingCompat(n, f);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static RatingCompat newThumbRating(boolean bl) {
        float f;
        if (bl) {
            f = 1.0f;
            do {
                return new RatingCompat(2, f);
                break;
            } while (true);
        }
        f = 0.0f;
        return new RatingCompat(2, f);
    }

    public static RatingCompat newUnratedRating(int n) {
        switch (n) {
            default: {
                return null;
            }
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
        }
        return new RatingCompat(n, -1.0f);
    }

    public int describeContents() {
        return this.mRatingStyle;
    }

    public float getPercentRating() {
        if (this.mRatingStyle != 6 || !this.isRated()) {
            return -1.0f;
        }
        return this.mRatingValue;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Object getRating() {
        if (this.mRatingObj != null || Build.VERSION.SDK_INT < 21) {
            return this.mRatingObj;
        }
        if (this.isRated()) {
            switch (this.mRatingStyle) {
                default: {
                    do {
                        return null;
                        break;
                    } while (true);
                }
                case 1: {
                    this.mRatingObj = RatingCompatApi21.newHeartRating(this.hasHeart());
                    do {
                        return this.mRatingObj;
                        break;
                    } while (true);
                }
                case 2: {
                    this.mRatingObj = RatingCompatApi21.newThumbRating(this.isThumbUp());
                    return this.mRatingObj;
                }
                case 3: 
                case 4: 
                case 5: {
                    this.mRatingObj = RatingCompatApi21.newStarRating(this.mRatingStyle, this.getStarRating());
                    return this.mRatingObj;
                }
                case 6: 
            }
            this.mRatingObj = RatingCompatApi21.newPercentageRating(this.getPercentRating());
            return null;
        }
        this.mRatingObj = RatingCompatApi21.newUnratedRating(this.mRatingStyle);
        return this.mRatingObj;
    }

    public int getRatingStyle() {
        return this.mRatingStyle;
    }

    /*
     * Enabled aggressive block sorting
     */
    public float getStarRating() {
        switch (this.mRatingStyle) {
            default: {
                return -1.0f;
            }
            case 3: 
            case 4: 
            case 5: {
                if (!this.isRated()) return -1.0f;
                return this.mRatingValue;
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean hasHeart() {
        boolean bl = true;
        if (this.mRatingStyle != 1) {
            return false;
        }
        if (this.mRatingValue != 1.0f) return false;
        return bl;
    }

    public boolean isRated() {
        if (this.mRatingValue >= 0.0f) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isThumbUp() {
        if (this.mRatingStyle != 2 || this.mRatingValue != 1.0f) {
            return false;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        String string2;
        StringBuilder stringBuilder = new StringBuilder().append("Rating:style=").append(this.mRatingStyle).append(" rating=");
        if (this.mRatingValue < 0.0f) {
            string2 = "unrated";
            do {
                return stringBuilder.append(string2).toString();
                break;
            } while (true);
        }
        string2 = String.valueOf(this.mRatingValue);
        return stringBuilder.append(string2).toString();
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeInt(this.mRatingStyle);
        parcel.writeFloat(this.mRatingValue);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface StarStyle {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Style {
    }

}

