/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.os.Bundle
 *  android.text.Editable
 *  android.util.Log
 *  android.view.View
 *  android.widget.EditText
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsecureDataStorage2Activity
extends AppCompatActivity {
    private SQLiteDatabase mDB;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            this.mDB = this.openOrCreateDatabase("ids2", 0, null);
            this.mDB.execSQL("CREATE TABLE IF NOT EXISTS myuser(user VARCHAR, password VARCHAR);");
        }
        catch (Exception exception) {
            Log.d((String)"Diva", (String)("Error occurred while creating database: " + exception.getMessage()));
        }
        this.setContentView(2130968612);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void saveCredentials(View view) {
        view = (EditText)this.findViewById(2131493003);
        EditText editText = (EditText)this.findViewById(2131493004);
        try {
            this.mDB.execSQL("INSERT INTO myuser VALUES ('" + view.getText().toString() + "', '" + editText.getText().toString() + "');");
            this.mDB.close();
        }
        catch (Exception exception) {
            Log.d((String)"Diva", (String)("Error occurred while inserting into database: " + exception.getMessage()));
        }
        Toast.makeText((Context)this, (CharSequence)"3rd party credentials saved successfully!", (int)0).show();
    }
}

