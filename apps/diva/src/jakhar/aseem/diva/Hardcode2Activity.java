/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.text.Editable
 *  android.view.View
 *  android.widget.EditText
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import jakhar.aseem.diva.DivaJni;

public class Hardcode2Activity
extends AppCompatActivity {
    private DivaJni djni;

    public void access(View view) {
        view = (EditText)this.findViewById(2131492990);
        if (this.djni.access(view.getText().toString()) != 0) {
            Toast.makeText((Context)this, (CharSequence)"Access granted! See you on the other side :)", (int)0).show();
            return;
        }
        Toast.makeText((Context)this, (CharSequence)"Access denied! See you in hell :D", (int)0).show();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968608);
        this.djni = new DivaJni();
    }
}

