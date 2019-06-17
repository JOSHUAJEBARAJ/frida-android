/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Bundle
 *  android.preference.PreferenceManager
 *  android.text.Editable
 *  android.view.View
 *  android.widget.Button
 *  android.widget.EditText
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.SimpleCursorAdapter
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import jakhar.aseem.diva.NotesProvider;

public class AccessControl3NotesActivity
extends AppCompatActivity {
    public void accessNotes(View view) {
        view = (EditText)this.findViewById(2131492979);
        Button button = (Button)this.findViewById(2131492980);
        String string2 = PreferenceManager.getDefaultSharedPreferences((Context)this).getString(this.getString(2131099722), "");
        if (view.getText().toString().equals(string2)) {
            ((ListView)this.findViewById(2131492981)).setAdapter((ListAdapter)new SimpleCursorAdapter((Context)this, 2130968630, this.getContentResolver().query(NotesProvider.CONTENT_URI, new String[]{"_id", "title", "note"}, null, null, null), new String[]{"title", "note"}, new int[]{2131493043, 2131493044}, 0));
            view.setVisibility(4);
            button.setVisibility(4);
            return;
        }
        Toast.makeText((Context)this, (CharSequence)"Please Enter a valid pin!", (int)0).show();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968604);
    }
}

