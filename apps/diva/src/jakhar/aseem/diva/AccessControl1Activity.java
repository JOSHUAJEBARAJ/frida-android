/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.os.Bundle
 *  android.util.Log
 *  android.view.View
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AccessControl1Activity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968601);
    }

    public void viewAPICredentials(View view) {
        view = new Intent();
        view.setAction("jakhar.aseem.diva.action.VIEW_CREDS");
        if (view.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity((Intent)view);
            return;
        }
        Toast.makeText((Context)this, (CharSequence)"Error while getting API details", (int)0).show();
        Log.e((String)"Diva-aci1", (String)"Couldn't resolve the Intent VIEW_CREDS to our activity");
    }
}

