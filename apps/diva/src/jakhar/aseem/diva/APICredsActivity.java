/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.TextView
 */
package jakhar.aseem.diva;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class APICredsActivity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968605);
        ((TextView)this.findViewById(2131492982)).setText((CharSequence)"API Key: 123secretapikey123\nAPI User name: diva\nAPI Password: p@ssword");
    }
}

