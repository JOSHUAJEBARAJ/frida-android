/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.Log
 */
package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompatApi21;
import android.support.v4.media.RatingCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public final class MediaMetadataCompat
implements Parcelable {
    public static final Parcelable.Creator<MediaMetadataCompat> CREATOR;
    private static final ArrayMap<String, Integer> METADATA_KEYS_TYPE;
    public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
    public static final String METADATA_KEY_ART = "android.media.metadata.ART";
    public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
    public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
    public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
    public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
    public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
    public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
    public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
    public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
    public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
    private static final int METADATA_TYPE_BITMAP = 2;
    private static final int METADATA_TYPE_LONG = 0;
    private static final int METADATA_TYPE_RATING = 3;
    private static final int METADATA_TYPE_TEXT = 1;
    private static final String[] PREFERRED_BITMAP_ORDER;
    private static final String[] PREFERRED_DESCRIPTION_ORDER;
    private static final String[] PREFERRED_URI_ORDER;
    private static final String TAG = "MediaMetadata";
    private final Bundle mBundle;
    private MediaDescriptionCompat mDescription;
    private Object mMetadataObj;

    static {
        METADATA_KEYS_TYPE = new ArrayMap();
        METADATA_KEYS_TYPE.put("android.media.metadata.TITLE", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", 0);
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.DATE", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", 0);
        METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", 0);
        METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", 0);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", 0);
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.ART", 2);
        METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", 2);
        METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", 3);
        METADATA_KEYS_TYPE.put("android.media.metadata.RATING", 3);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", 2);
        METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", 1);
        METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", 1);
        PREFERRED_DESCRIPTION_ORDER = new String[]{"android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER"};
        PREFERRED_BITMAP_ORDER = new String[]{"android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART"};
        PREFERRED_URI_ORDER = new String[]{"android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI"};
        CREATOR = new Parcelable.Creator<MediaMetadataCompat>(){

            public MediaMetadataCompat createFromParcel(Parcel parcel) {
                return new MediaMetadataCompat(parcel);
            }

            public MediaMetadataCompat[] newArray(int n) {
                return new MediaMetadataCompat[n];
            }
        };
    }

    private MediaMetadataCompat(Bundle bundle) {
        this.mBundle = new Bundle(bundle);
    }

    private MediaMetadataCompat(Parcel parcel) {
        this.mBundle = parcel.readBundle();
    }

    public static MediaMetadataCompat fromMediaMetadata(Object object) {
        if (object == null || Build.VERSION.SDK_INT < 21) {
            return null;
        }
        Object object2 = new Builder();
        block6 : for (String string2 : MediaMetadataCompatApi21.keySet(object)) {
            Integer n = METADATA_KEYS_TYPE.get(string2);
            if (n == null) continue;
            switch (n) {
                default: {
                    continue block6;
                }
                case 0: {
                    object2.putLong(string2, MediaMetadataCompatApi21.getLong(object, string2));
                    continue block6;
                }
                case 2: {
                    object2.putBitmap(string2, MediaMetadataCompatApi21.getBitmap(object, string2));
                    continue block6;
                }
                case 3: {
                    object2.putRating(string2, RatingCompat.fromRating(MediaMetadataCompatApi21.getRating(object, string2)));
                    continue block6;
                }
                case 1: 
            }
            object2.putText(string2, MediaMetadataCompatApi21.getText(object, string2));
        }
        object2 = object2.build();
        object2.mMetadataObj = object;
        return object2;
    }

    public boolean containsKey(String string2) {
        return this.mBundle.containsKey(string2);
    }

    public int describeContents() {
        return 0;
    }

    public Bitmap getBitmap(String string2) {
        try {
            string2 = (Bitmap)this.mBundle.getParcelable(string2);
            return string2;
        }
        catch (Exception exception) {
            Log.w((String)"MediaMetadata", (String)"Failed to retrieve a key as Bitmap.", (Throwable)exception);
            return null;
        }
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public MediaDescriptionCompat getDescription() {
        if (this.mDescription != null) {
            return this.mDescription;
        }
        var7_1 = this.getString("android.media.metadata.MEDIA_ID");
        var8_2 = new CharSequence[3];
        var5_3 = null;
        var6_4 = null;
        var4_5 = this.getText("android.media.metadata.DISPLAY_TITLE");
        if (!TextUtils.isEmpty((CharSequence)var4_5)) {
            var8_2[0] = var4_5;
            var8_2[1] = this.getText("android.media.metadata.DISPLAY_SUBTITLE");
            var8_2[2] = this.getText("android.media.metadata.DISPLAY_DESCRIPTION");
        } else {
            var2_7 = 0;
            for (var1_6 = 0; var2_7 < var8_2.length && var1_6 < MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER.length; ++var1_6) {
                var4_5 = this.getText(MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER[var1_6]);
                var3_8 = var2_7;
                if (!TextUtils.isEmpty((CharSequence)var4_5)) {
                    var8_2[var2_7] = var4_5;
                    var3_8 = var2_7 + 1;
                }
                var2_7 = var3_8;
            }
        }
        var1_6 = 0;
        do {
            var4_5 = var5_3;
            if (var1_6 >= MediaMetadataCompat.PREFERRED_BITMAP_ORDER.length || (var4_5 = this.getBitmap(MediaMetadataCompat.PREFERRED_BITMAP_ORDER[var1_6])) != null) break;
            ++var1_6;
        } while (true);
        var1_6 = 0;
        do {
            var5_3 = var6_4;
            if (var1_6 >= MediaMetadataCompat.PREFERRED_URI_ORDER.length) ** GOTO lbl35
            var5_3 = this.getString(MediaMetadataCompat.PREFERRED_URI_ORDER[var1_6]);
            if (!TextUtils.isEmpty((CharSequence)var5_3)) {
                var5_3 = Uri.parse((String)var5_3);
lbl35: // 2 sources:
                var6_4 = new MediaDescriptionCompat.Builder();
                var6_4.setMediaId(var7_1);
                var6_4.setTitle(var8_2[0]);
                var6_4.setSubtitle(var8_2[1]);
                var6_4.setDescription(var8_2[2]);
                var6_4.setIconBitmap((Bitmap)var4_5);
                var6_4.setIconUri((Uri)var5_3);
                this.mDescription = var6_4.build();
                return this.mDescription;
            }
            ++var1_6;
        } while (true);
    }

    public long getLong(String string2) {
        return this.mBundle.getLong(string2, 0);
    }

    public Object getMediaMetadata() {
        if (this.mMetadataObj != null || Build.VERSION.SDK_INT < 21) {
            return this.mMetadataObj;
        }
        Object object = MediaMetadataCompatApi21.Builder.newInstance();
        block6 : for (String string2 : this.keySet()) {
            Integer n = METADATA_KEYS_TYPE.get(string2);
            if (n == null) continue;
            switch (n) {
                default: {
                    continue block6;
                }
                case 0: {
                    MediaMetadataCompatApi21.Builder.putLong(object, string2, this.getLong(string2));
                    continue block6;
                }
                case 2: {
                    MediaMetadataCompatApi21.Builder.putBitmap(object, string2, this.getBitmap(string2));
                    continue block6;
                }
                case 3: {
                    MediaMetadataCompatApi21.Builder.putRating(object, string2, this.getRating(string2).getRating());
                    continue block6;
                }
                case 1: 
            }
            MediaMetadataCompatApi21.Builder.putText(object, string2, this.getText(string2));
        }
        this.mMetadataObj = MediaMetadataCompatApi21.Builder.build(object);
        return this.mMetadataObj;
    }

    public RatingCompat getRating(String object) {
        try {
            object = (RatingCompat)this.mBundle.getParcelable((String)object);
            return object;
        }
        catch (Exception exception) {
            Log.w((String)"MediaMetadata", (String)"Failed to retrieve a key as Rating.", (Throwable)exception);
            return null;
        }
    }

    public String getString(String charSequence) {
        if ((charSequence = this.mBundle.getCharSequence((String)charSequence)) != null) {
            return charSequence.toString();
        }
        return null;
    }

    public CharSequence getText(String string2) {
        return this.mBundle.getCharSequence(string2);
    }

    public Set<String> keySet() {
        return this.mBundle.keySet();
    }

    public int size() {
        return this.mBundle.size();
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeBundle(this.mBundle);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface BitmapKey {
    }

    public static final class Builder {
        private final Bundle mBundle;

        public Builder() {
            this.mBundle = new Bundle();
        }

        public Builder(MediaMetadataCompat mediaMetadataCompat) {
            this.mBundle = new Bundle(mediaMetadataCompat.mBundle);
        }

        public MediaMetadataCompat build() {
            return new MediaMetadataCompat(this.mBundle);
        }

        public Builder putBitmap(String string2, Bitmap bitmap) {
            if (METADATA_KEYS_TYPE.containsKey(string2) && (Integer)METADATA_KEYS_TYPE.get(string2) != 2) {
                throw new IllegalArgumentException("The " + string2 + " key cannot be used to put a Bitmap");
            }
            this.mBundle.putParcelable(string2, (Parcelable)bitmap);
            return this;
        }

        public Builder putLong(String string2, long l) {
            if (METADATA_KEYS_TYPE.containsKey(string2) && (Integer)METADATA_KEYS_TYPE.get(string2) != 0) {
                throw new IllegalArgumentException("The " + string2 + " key cannot be used to put a long");
            }
            this.mBundle.putLong(string2, l);
            return this;
        }

        public Builder putRating(String string2, RatingCompat ratingCompat) {
            if (METADATA_KEYS_TYPE.containsKey(string2) && (Integer)METADATA_KEYS_TYPE.get(string2) != 3) {
                throw new IllegalArgumentException("The " + string2 + " key cannot be used to put a Rating");
            }
            this.mBundle.putParcelable(string2, (Parcelable)ratingCompat);
            return this;
        }

        public Builder putString(String string2, String string3) {
            if (METADATA_KEYS_TYPE.containsKey(string2) && (Integer)METADATA_KEYS_TYPE.get(string2) != 1) {
                throw new IllegalArgumentException("The " + string2 + " key cannot be used to put a String");
            }
            this.mBundle.putCharSequence(string2, (CharSequence)string3);
            return this;
        }

        public Builder putText(String string2, CharSequence charSequence) {
            if (METADATA_KEYS_TYPE.containsKey(string2) && (Integer)METADATA_KEYS_TYPE.get(string2) != 1) {
                throw new IllegalArgumentException("The " + string2 + " key cannot be used to put a CharSequence");
            }
            this.mBundle.putCharSequence(string2, charSequence);
            return this;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface LongKey {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface RatingKey {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TextKey {
    }

}

