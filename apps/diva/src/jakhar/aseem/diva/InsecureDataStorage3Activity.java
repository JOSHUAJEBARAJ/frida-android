/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.os.Bundle
 *  android.text.Editable
 *  android.util.Log
 *  android.view.View
 *  android.widget.EditText
 *  android.widget.Toast
 */
package jakhar.aseem.diva;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;

public class InsecureDataStorage3Activity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968613);
    }

    public void saveCredentials(View view) {
        view = (EditText)this.findViewById(2131493006);
        EditText editText = (EditText)this.findViewById(2131493007);
        Object object = new File(this.getApplicationInfo().dataDir);
        try {
            object = File.createTempFile("uinfo", "tmp", (File)object);
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

