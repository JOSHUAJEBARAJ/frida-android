/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.text.Editable
 *  android.view.View
 *  android.webkit.WebSettings
 *  android.webkit.WebView
 *  android.widget.EditText
 */
package jakhar.aseem.diva;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

public class InputValidation2URISchemeActivity
extends AppCompatActivity {
    public void get(View view) {
        view = (EditText)this.findViewById(2131492993);
        ((WebView)this.findViewById(2131492995)).loadUrl(view.getText().toString());
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968609);
        ((WebView)this.findViewById(2131492995)).getSettings().setJavaScriptEnabled(true);
    }
}

