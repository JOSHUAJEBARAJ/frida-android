/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.content.ContentProvider
 *  android.content.ContentResolver
 *  android.content.ContentUris
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.UriMatcher
 *  android.database.ContentObserver
 *  android.database.Cursor
 *  android.database.SQLException
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.database.sqlite.SQLiteOpenHelper
 *  android.database.sqlite.SQLiteQueryBuilder
 *  android.net.Uri
 *  android.text.TextUtils
 */
package jakhar.aseem.diva;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class NotesProvider
extends ContentProvider {
    static final String AUTHORITY = "jakhar.aseem.diva.provider.notesprovider";
    static final Uri CONTENT_URI = Uri.parse((String)"content://jakhar.aseem.diva.provider.notesprovider/notes");
    static final String CREATE_TBL_QRY = " CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, note TEXT NOT NULL);";
    static final String C_ID = "_id";
    static final String C_NOTE = "note";
    static final String C_TITLE = "title";
    static final String DBNAME = "divanotes.db";
    static final int DBVERSION = 1;
    static final String DROP_TBL_QRY = "DROP TABLE IF EXISTS notes";
    static final int PATH_ID = 2;
    static final int PATH_TABLE = 1;
    static final String TABLE = "notes";
    static final UriMatcher urimatcher = new UriMatcher(-1);
    SQLiteDatabase mDB;

    static {
        urimatcher.addURI("jakhar.aseem.diva.provider.notesprovider", "notes", 1);
        urimatcher.addURI("jakhar.aseem.diva.provider.notesprovider", "notes/#", 2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public int delete(Uri uri, String string2, String[] arrstring) {
        int n;
        switch (urimatcher.match(uri)) {
            default: {
                throw new IllegalArgumentException("Divanotes(delete): Unsupported URI " + (Object)uri);
            }
            case 1: {
                n = this.mDB.delete("notes", string2, arrstring);
                break;
            }
            case 2: {
                String string3 = uri.getLastPathSegment();
                SQLiteDatabase sQLiteDatabase = this.mDB;
                StringBuilder stringBuilder = new StringBuilder().append("_id = ").append(string3);
                string2 = !TextUtils.isEmpty((CharSequence)string2) ? " AND (" + string2 + ')' : "";
                n = sQLiteDatabase.delete("notes", stringBuilder.append(string2).toString(), arrstring);
            }
        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return n;
    }

    public String getType(Uri uri) {
        switch (urimatcher.match(uri)) {
            default: {
                throw new IllegalArgumentException("Divanotes: Unsupported URI: " + (Object)uri);
            }
            case 1: {
                return "vnd.android.cursor.dir/vnd.jakhar.notes";
            }
            case 2: 
        }
        return "vnd.android.cursor.item/vnd.jakhar.notes";
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        long l = this.mDB.insert("notes", "", contentValues);
        if (l > 0) {
            uri = ContentUris.withAppendedId((Uri)CONTENT_URI, (long)l);
            this.getContext().getContentResolver().notifyChange(uri, null);
            return uri;
        }
        throw new SQLException("Divanotes: Fail to add a new record into " + (Object)uri);
    }

    public boolean onCreate() {
        this.mDB = new DBHelper(this.getContext()).getWritableDatabase();
        if (this.mDB == null) {
            return false;
        }
        return true;
    }

    public Cursor query(Uri uri, String[] cursor, String string2, String[] arrstring, String string3) {
        SQLiteQueryBuilder sQLiteQueryBuilder;
        String string4;
        block6 : {
            sQLiteQueryBuilder = new SQLiteQueryBuilder();
            sQLiteQueryBuilder.setTables("notes");
            switch (urimatcher.match(uri)) {
                default: {
                    throw new IllegalArgumentException("Divanotes(query): Unknown URI " + (Object)uri);
                }
                case 2: {
                    sQLiteQueryBuilder.appendWhere((CharSequence)("_id=" + uri.getLastPathSegment()));
                }
                case 1: 
            }
            if (string3 != null) {
                string4 = string3;
                if (string3 != "") break block6;
            }
            string4 = "title";
        }
        cursor = sQLiteQueryBuilder.query(this.mDB, (String[])cursor, string2, arrstring, null, null, string4);
        cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
        return cursor;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int update(Uri uri, ContentValues contentValues, String string2, String[] arrstring) {
        int n;
        switch (urimatcher.match(uri)) {
            default: {
                throw new IllegalArgumentException("Divanotes(update): Unsupported URI " + (Object)uri);
            }
            case 1: {
                n = this.mDB.update("notes", contentValues, string2, arrstring);
                break;
            }
            case 2: {
                SQLiteDatabase sQLiteDatabase = this.mDB;
                StringBuilder stringBuilder = new StringBuilder().append("_id = ").append(uri.getLastPathSegment());
                string2 = !TextUtils.isEmpty((CharSequence)string2) ? " AND (" + string2 + ')' : "";
                n = sQLiteDatabase.update("notes", contentValues, stringBuilder.append(string2).toString(), arrstring);
            }
        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return n;
    }

    private static class DBHelper
    extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "divanotes.db", null, 1);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
            sQLiteDatabase.execSQL(" CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, note TEXT NOT NULL);");
            sQLiteDatabase.execSQL("INSERT INTO notes(title,note) VALUES ('office', '10 Meetings. 5 Calls. Lunch with CEO');");
            sQLiteDatabase.execSQL("INSERT INTO notes(title,note) VALUES ('home', 'Buy toys for baby, Order dinner');");
            sQLiteDatabase.execSQL("INSERT INTO notes(title,note) VALUES ('holiday', 'Either Goa or Amsterdam');");
            sQLiteDatabase.execSQL("INSERT INTO notes(title,note) VALUES ('Expense', 'Spent too much on home theater');");
            sQLiteDatabase.execSQL("INSERT INTO notes(title,note) VALUES ('Exercise', 'Alternate days running');");
            sQLiteDatabase.execSQL("INSERT INTO notes(title,note) VALUES ('Weekend', 'b333333333333r');");
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
            this.onCreate(sQLiteDatabase);
        }
    }

}

