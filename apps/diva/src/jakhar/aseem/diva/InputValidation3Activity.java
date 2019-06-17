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

public class InputValidation3Activity
extends AppCompatActivity {
    private DivaJni djni;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968610);
        this.djni = new DivaJni();
    }

    public void push(View view) {
        view = (EditText)this.findViewById(2131492997);
        if (this.djni.initiateLaunchSequence(view.getText().toString()) != 0) {
            Toast.makeText((Context)this, (CharSequence)"Launching in T - 10 ...", (int)0).show();
            return;
        }
        Toast.makeText((Context)this, (CharSequence)"Access denied!", (int)0).show();
    }
}

