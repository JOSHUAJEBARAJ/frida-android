/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.os.Build
 *  android.os.Bundle
 */
package sg.vantagepoint.debug;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class MainActivity
extends AppCompatActivity {
    private static Context context;

    private boolean hasEmulatorBuildProp() {
        if (!(Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") || Build.PRODUCT.contains("google_sdk") || Build.PRODUCT.contains("sdk") || Build.HARDWARE.contains("goldfish") || Build.HARDWARE.contains("ranchu") || Build.BOARD.contains("unknown") || Build.ID.contains("FRF91") || Build.MANUFACTURER.contains("unknown") || Build.SERIAL == null || Build.TAGS.contains("test-keys") || Build.USER.contains("android-build"))) {
            return false;
        }
        return true;
    }

    public boolean debug() {
        if (this.hasEmulatorBuildProp()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        this.setContentView(2131296284);
        object = new AlertDialog.Builder((Context)this);
        if (this.debug()) {
            object.setTitle("Oops");
            object.setMessage("Your are ruuning on emulator");
            object.setPositiveButton("ok", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialogInterface, int n) {
                    System.exit(0);
                }
            });
            object.setCancelable(false);
            object.show();
        }
    }

}

