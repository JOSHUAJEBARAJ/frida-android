/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.location.Location
 *  android.location.LocationManager
 *  android.util.Log
 */
package android.support.v7.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.TwilightCalculator;
import android.util.Log;
import java.util.Calendar;

class TwilightManager {
    private static final int SUNRISE = 6;
    private static final int SUNSET = 22;
    private static final String TAG = "TwilightManager";
    private static TwilightManager sInstance;
    private final Context mContext;
    private final LocationManager mLocationManager;
    private final TwilightState mTwilightState = new TwilightState();

    @VisibleForTesting
    TwilightManager(@NonNull Context context, @NonNull LocationManager locationManager) {
        this.mContext = context;
        this.mLocationManager = locationManager;
    }

    static TwilightManager getInstance(@NonNull Context context) {
        if (sInstance == null) {
            context = context.getApplicationContext();
            sInstance = new TwilightManager(context, (LocationManager)context.getSystemService("location"));
        }
        return sInstance;
    }

    @SuppressLint(value={"MissingPermission"})
    private Location getLastKnownLocation() {
        Location location = null;
        Location location2 = null;
        if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            location = this.getLastKnownLocationForProvider("network");
        }
        if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            location2 = this.getLastKnownLocationForProvider("gps");
        }
        if (location2 != null && location != null) {
            if (location2.getTime() > location.getTime()) {
                return location2;
            }
            return location;
        }
        if (location2 != null) {
            return location2;
        }
        return location;
    }

    @RequiresPermission(anyOf={"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    private Location getLastKnownLocationForProvider(String string2) {
        try {
            if (this.mLocationManager.isProviderEnabled(string2)) {
                string2 = this.mLocationManager.getLastKnownLocation(string2);
                return string2;
            }
        }
        catch (Exception exception) {
            Log.d((String)"TwilightManager", (String)"Failed to get last known location", (Throwable)exception);
        }
        return null;
    }

    private boolean isStateValid() {
        if (this.mTwilightState.nextUpdate > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    static void setInstance(TwilightManager twilightManager) {
        sInstance = twilightManager;
    }

    private void updateState(@NonNull Location location) {
        TwilightState twilightState = this.mTwilightState;
        long l = System.currentTimeMillis();
        TwilightCalculator twilightCalculator = TwilightCalculator.getInstance();
        twilightCalculator.calculateTwilight(l - 86400000, location.getLatitude(), location.getLongitude());
        long l2 = twilightCalculator.sunset;
        twilightCalculator.calculateTwilight(l, location.getLatitude(), location.getLongitude());
        int n = twilightCalculator.state;
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        long l3 = twilightCalculator.sunrise;
        long l4 = twilightCalculator.sunset;
        twilightCalculator.calculateTwilight(86400000 + l, location.getLatitude(), location.getLongitude());
        long l5 = twilightCalculator.sunrise;
        if (l3 != -1 && l4 != -1) {
            l = l > l4 ? 0 + l5 : (l > l3 ? 0 + l4 : 0 + l3);
            l += 60000;
        } else {
            l += 43200000;
        }
        twilightState.isNight = bl;
        twilightState.yesterdaySunset = l2;
        twilightState.todaySunrise = l3;
        twilightState.todaySunset = l4;
        twilightState.tomorrowSunrise = l5;
        twilightState.nextUpdate = l;
    }

    boolean isNight() {
        TwilightState twilightState = this.mTwilightState;
        if (this.isStateValid()) {
            return twilightState.isNight;
        }
        Location location = this.getLastKnownLocation();
        if (location != null) {
            this.updateState(location);
            return twilightState.isNight;
        }
        Log.i((String)"TwilightManager", (String)"Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
        int n = Calendar.getInstance().get(11);
        if (n >= 6 && n < 22) {
            return false;
        }
        return true;
    }

    private static class TwilightState {
        boolean isNight;
        long nextUpdate;
        long todaySunrise;
        long todaySunset;
        long tomorrowSunrise;
        long yesterdaySunset;

        TwilightState() {
        }
    }

}

