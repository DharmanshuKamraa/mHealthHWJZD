package info.androidhive.slidingmenu.model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

import info.androidhive.slidingmenu.db.UserContract;
import info.androidhive.slidingmenu.db.UserReaderDbHelper;

/**
 * Created by Dharmanshu on 2/27/15.
 */
public class User {
    private SQLiteDatabase readable_db;
    private SQLiteDatabase writable_db;
    private Context context;

    public User(Activity c) {
        Log.i("Constructor", "Here");
        UserReaderDbHelper mDbHelper = new UserReaderDbHelper(c);
        this.readable_db = mDbHelper.getReadableDatabase();
        this.writable_db = mDbHelper.getWritableDatabase();
    }

    public Cursor getLoggedInUser() {
        String[] columns= {
                UserContract.UserEntry._ID ,
                UserContract.UserEntry.COLUMN_NAME_USERNAME ,
                UserContract.UserEntry.COLUMN_NAME_TOKEN ,
        };

        Cursor c = this.readable_db.query(
                UserContract.UserEntry.TABLE_NAME ,
                columns,
                "token IS NOT NULL",
                null ,
                null ,
                null ,
                null
        );
        c.moveToFirst();
        return c;
    }
}
