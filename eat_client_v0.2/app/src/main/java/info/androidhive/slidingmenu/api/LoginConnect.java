package info.androidhive.slidingmenu.api;

import android.util.Log;

import org.json.JSONObject;

import info.androidhive.slidingmenu.interfaces.LoginAsyncResponse;

/**
 * Created by Dharmanshu on 2/27/15.
 */
public class LoginConnect extends ServerConnect {
    public LoginAsyncResponse delegate= null;

    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            if (jsonObj.get("code").toString().equals("201")) {
                delegate.processLoginSuccessful(jsonObj.getString("object"));
            } else {
                delegate.processFailed("Incorrect login credentials.");
            }
        } catch (Exception e) {
            Log.i("exception" , e.toString());
            delegate.processFailed("Server error");
        }

    }
}
