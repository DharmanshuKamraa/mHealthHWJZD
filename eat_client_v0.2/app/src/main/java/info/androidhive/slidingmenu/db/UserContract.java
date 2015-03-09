package info.androidhive.slidingmenu.db;

import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Dharmanshu on 2/24/15.
 */
public final  class UserContract {
    public UserContract(){}

    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_ENTRY_ID = "user_id";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_TOKEN = "token";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UserEntry.TABLE_NAME + " ( " +
                        UserEntry._ID + " INT PRIMARY KEY " + "," +
                        UserEntry.COLUMN_NAME_ENTRY_ID + " TEXT " + "," +
                        UserEntry.COLUMN_NAME_USERNAME + " TEXT " + "," +
                        UserEntry.COLUMN_NAME_TOKEN + " TEXT " +
                        ")";

        private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
        private static final String SQL_DELETE_DATA = "DELETE FROM " + UserEntry.TABLE_NAME;

        public static String getSqlCreateEntries() {
            return SQL_CREATE_ENTRIES;
        }
        public static String getSqlDeleteEntries() {
            return SQL_DELETE_ENTRIES;
        }
        public static String getSqlDeleteData() {
            Log.i("DELETE QUERY" , SQL_DELETE_DATA);
            return SQL_DELETE_DATA;
        }
    }


}
