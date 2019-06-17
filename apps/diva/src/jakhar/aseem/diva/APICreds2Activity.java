/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.Button
 *  android.widget.EditText
 *  android.widget.TextView
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class APICreds2Activity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968606);
        bundle = (TextView)this.findViewById(2131492983);
        EditText editText = (EditText)this.findViewById(2131492984);
        Button button = (Button)this.findViewById(2131492985);
        if (!this.getIntent().getBooleanExtra(this.getString(2131099686), true)) {
            bundle.setText((CharSequence)"TVEETER API Key: secrettveeterapikey\nAPI User name: diva2\nAPI Password: p@ssword2");
            return;
        }
        bundle.setText((CharSequence)"Register yourself at http://payatu.com to get your PIN and then login with that PIN!");
        editText.setVisibility(0);
        button.setVisibility(0);
    }

    public void viewCreds(View view) {
        Toast.makeText((Context)this, (CharSequence)"Invalid PIN. Please try again", (int)0).show();
    }
}

