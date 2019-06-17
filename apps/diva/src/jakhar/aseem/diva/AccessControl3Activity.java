/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Bundle
 *  android.preference.PreferenceManager
 *  android.text.Editable
 *  android.view.View
 *  android.widget.Button
 *  android.widget.EditText
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import jakhar.aseem.diva.AccessControl3NotesActivity;

public class AccessControl3Activity
extends AppCompatActivity {
    public void addPin(View view) {
        view = PreferenceManager.getDefaultSharedPreferences((Context)this).edit();
        String string2 = ((EditText)this.findViewById(2131492976)).getText().toString();
        if (string2 == null || string2.isEmpty()) {
            Toast.makeText((Context)this, (CharSequence)"Please Enter a valid pin!", (int)0).show();
            return;
        }
        Button button = (Button)this.findViewById(2131492978);
        view.putString(this.getString(2131099722), string2);
        view.commit();
        if (button.getVisibility() != 0) {
            button.setVisibility(0);
        }
        Toast.makeText((Context)this, (CharSequence)"PIN Created successfully. Private notes are now protected with PIN", (int)0).show();
    }

    public void goToNotes(View view) {
        this.startActivity(new Intent((Context)this, AccessControl3NotesActivity.class));
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968603);
        if (!PreferenceManager.getDefaultSharedPreferences((Context)this).getString(this.getString(2131099722), "").isEmpty()) {
            ((Button)this.findViewById(2131492978)).setVisibility(0);
        }
    }
}

