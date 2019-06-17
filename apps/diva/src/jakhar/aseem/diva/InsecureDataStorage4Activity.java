/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Environment
 *  android.text.Editable
 *  android.util.Log
 *  android.view.View
 *  android.widget.EditText
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;

public class InsecureDataStorage4Activity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968614);
    }

    public void saveCredentials(View view) {
        view = (EditText)this.findViewById(2131493010);
        EditText editText = (EditText)this.findViewById(2131493011);
        Object object = Environment.getExternalStorageDirectory();
        try {
            object = new File(object.getAbsolutePath() + "/.uinfo.txt");
            object.setReadable(true);
            object.setWritable(true);
            object = new FileWriter((File)object);
            object.write(view.getText().toString() + ":" + editText.getText().toString() + "\n");
            object.close();
            Toast.makeText((Context)this, (CharSequence)"3rd party credentials saved successfully!", (int)0).show();
            return;
        }
        catch (Exception exception) {
            Toast.makeText((Context)this, (CharSequence)"File error occurred", (int)0).show();
            Log.d((String)"Diva", (String)("File error: " + exception.getMessage()));
            return;
        }
    }
}

