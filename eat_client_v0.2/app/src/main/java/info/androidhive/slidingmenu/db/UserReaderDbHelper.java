package info.androidhive.slidingmenu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dharmanshu on 2/24/15.
 */
public class UserReaderDbHelper extends SQLiteOpenHelper {
    public final static int DATABASE_VERSION = 5;
    public final static String DATABASE_NAME = "smartcart.db";

    public UserReaderDbHelper(Context context) {
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserContract.UserEntry.getSqlCreateEntries());
    }
    public void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion) {
        db.execSQL(UserContract.UserEntry.getSqlDeleteEntries());
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db , int oldVersion , int newVersion) {
        onUpgrade(db , oldVersion , newVersion);
    }

    public void deleteEntries(SQLiteDatabase db) {
        db.execSQL(UserContract.UserEntry.getSqlDeleteData());
    }
}
