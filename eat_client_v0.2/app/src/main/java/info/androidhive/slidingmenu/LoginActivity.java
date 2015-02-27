package info.androidhive.slidingmenu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.slidingmenu.api.LoginConnect;
import info.androidhive.slidingmenu.api.ServerConnect;
import info.androidhive.slidingmenu.db.UserContract;
import info.androidhive.slidingmenu.db.UserReaderDbHelper;
import info.androidhive.slidingmenu.interfaces.ApiAsyncResponse;
import info.androidhive.slidingmenu.interfaces.LoginAsyncResponse;


public class LoginActivity extends Activity implements LoginAsyncResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initializeDatabase();
//        checkAlreadyLoggedIn();
//        checkLogin();
        setContentView(R.layout.activity_login);
    }

    public void initializeDatabase() {
        UserReaderDbHelper mDbHelper = new UserReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(UserContract.UserEntry.COLUMN_NAME_ENTRY_ID , "1");
//        values.put(UserContract.UserEntry.COLUMN_NAME_TOKEN, "abcde");
//        values.put(UserContract.UserEntry.COLUMN_NAME_USERNAME , "Manshu");
//        long newRowId;
//        newRowId = db.insert(UserContract.UserEntry.TABLE_NAME , null, values);
    }

    public void checkLogin() {
    }

    public void checkAlreadyLoggedIn() {
        UserReaderDbHelper mDbHelper = new UserReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                UserContract.UserEntry._ID ,
                UserContract.UserEntry.COLUMN_NAME_USERNAME
        };
        Cursor c = db.query(
                UserContract.UserEntry.TABLE_NAME ,
                projection ,
                null ,
                null ,
                null ,
                null ,
                null
        );
        c.moveToFirst();
        String username = c.getString(c.getColumnIndex(UserContract.UserEntry.COLUMN_NAME_USERNAME));
        Log.i("TAG", username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLoginClicked(View v) {
        Log.i("TAG", "Login Clicked");
        JSONObject obj = new JSONObject();

        EditText username_view = (EditText) findViewById(R.id.username);
        String username = username_view.getText().toString();

        EditText password_view = (EditText) findViewById(R.id.password);
        String password = password_view.getText().toString();

        try {
            obj.put("username" , username);
            obj.put("password" , password);
            LoginConnect s = new LoginConnect();
            s.delegate = this;
            s.execute("POST" , "users/login/" , obj.toString());

        } catch (JSONException e) {
            Log.i("JSON_ERROR", e.getMessage());
        }
    }

    public void onSignUpClicked(View v) {
        Log.i("TAG", "Login Clicked");
        Intent intent = new Intent(this , SignupActivity.class);
        startActivity(intent);
    }

    public void processLoginSuccessful(String s) {
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    };

    public void processFailed(String s) {
        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
    }
}
