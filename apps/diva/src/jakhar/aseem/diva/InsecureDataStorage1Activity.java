/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Bundle
 *  android.preference.PreferenceManager
 *  android.text.Editable
 *  android.view.View
 *  android.widget.EditText
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsecureDataStorage1Activity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968611);
    }

    public void saveCredentials(View view) {
        view = PreferenceManager.getDefaultSharedPreferences((Context)this).edit();
        EditText editText = (EditText)this.findViewById(2131493000);
        EditText editText2 = (EditText)this.findViewById(2131493001);
        view.putString("user", editText.getText().toString());
        view.putString("password", editText2.getText().toString());
        view.commit();
        Toast.makeText((Context)this, (CharSequence)"3rd party credentials saved successfully!", (int)0).show();
    }
}

