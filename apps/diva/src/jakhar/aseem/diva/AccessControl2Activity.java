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
 *  android.widget.RadioButton
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
import android.widget.RadioButton;
import android.widget.Toast;

public class AccessControl2Activity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968602);
    }

    public void viewAPICredentials(View view) {
        view = (RadioButton)this.findViewById(2131492973);
        Intent intent = new Intent();
        boolean bl = view.isChecked();
        intent.setAction("jakhar.aseem.diva.action.VIEW_CREDS2");
        intent.putExtra(this.getString(2131099686), bl);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(intent);
            return;
        }
        Toast.makeText((Context)this, (CharSequence)"Error while getting Tveeter API details", (int)0).show();
        Log.e((String)"Diva-aci1", (String)"Couldn't resolve the Intent VIEW_CREDS2 to our activity");
    }
}

