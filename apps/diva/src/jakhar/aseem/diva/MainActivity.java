/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 */
package jakhar.aseem.diva;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import jakhar.aseem.diva.AccessControl1Activity;
import jakhar.aseem.diva.AccessControl2Activity;
import jakhar.aseem.diva.AccessControl3Activity;
import jakhar.aseem.diva.Hardcode2Activity;
import jakhar.aseem.diva.HardcodeActivity;
import jakhar.aseem.diva.InputValidation2URISchemeActivity;
import jakhar.aseem.diva.InputValidation3Activity;
import jakhar.aseem.diva.InsecureDataStorage1Activity;
import jakhar.aseem.diva.InsecureDataStorage2Activity;
import jakhar.aseem.diva.InsecureDataStorage3Activity;
import jakhar.aseem.diva.InsecureDataStorage4Activity;
import jakhar.aseem.diva.LogActivity;
import jakhar.aseem.diva.SQLInjectionActivity;

public class MainActivity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968616);
        this.setSupportActionBar((Toolbar)this.findViewById(2131493015));
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.getMenuInflater().inflate(2131558400, menu2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131493058) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void startChallenge(View view) {
        if (view == this.findViewById(2131493023)) {
            this.startActivity(new Intent((Context)this, LogActivity.class));
            return;
        } else {
            if (view == this.findViewById(2131493024)) {
                this.startActivity(new Intent((Context)this, HardcodeActivity.class));
                return;
            }
            if (view == this.findViewById(2131493025)) {
                this.startActivity(new Intent((Context)this, InsecureDataStorage1Activity.class));
                return;
            }
            if (view == this.findViewById(2131493026)) {
                this.startActivity(new Intent((Context)this, InsecureDataStorage2Activity.class));
                return;
            }
            if (view == this.findViewById(2131493027)) {
                this.startActivity(new Intent((Context)this, InsecureDataStorage3Activity.class));
                return;
            }
            if (view == this.findViewById(2131493028)) {
                this.startActivity(new Intent((Context)this, InsecureDataStorage4Activity.class));
                return;
            }
            if (view == this.findViewById(2131493029)) {
                this.startActivity(new Intent((Context)this, SQLInjectionActivity.class));
                return;
            }
            if (view == this.findViewById(2131493030)) {
                this.startActivity(new Intent((Context)this, InputValidation2URISchemeActivity.class));
                return;
            }
            if (view == this.findViewById(2131493031)) {
                this.startActivity(new Intent((Context)this, AccessControl1Activity.class));
                return;
            }
            if (view == this.findViewById(2131493032)) {
                this.startActivity(new Intent((Context)this, AccessControl2Activity.class));
                return;
            }
            if (view == this.findViewById(2131493033)) {
                this.startActivity(new Intent((Context)this, AccessControl3Activity.class));
                return;
            }
            if (view == this.findViewById(2131493034)) {
                this.startActivity(new Intent((Context)this, Hardcode2Activity.class));
                return;
            }
            if (view != this.findViewById(2131493035)) return;
            {
                this.startActivity(new Intent((Context)this, InputValidation3Activity.class));
                return;
            }
        }
    }
}

