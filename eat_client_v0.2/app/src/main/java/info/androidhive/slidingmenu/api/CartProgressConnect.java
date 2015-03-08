package info.androidhive.slidingmenu.api;

import android.util.Log;

import org.json.JSONObject;

import info.androidhive.slidingmenu.interfaces.CartProgressAsyncResponse;

/**
 * Created by Dharmanshu on 3/8/15.
 */

public class CartProgressConnect extends ServerConnect {
    public CartProgressAsyncResponse delegate= null;

    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            if ((jsonObj.getInt("code") >= 200) && (jsonObj.getInt("code") <= 210)) {
                if (jsonObj.has("object")) {
                    delegate.processCartProgress(jsonObj.getString("object"));
                } else {
                    delegate.processCartProgress(jsonObj.getString("objects"));
                }
            } else {
                delegate.processFailed("Could not fetch list.");
            }
        } catch (Exception e) {
            Log.i("exception", e.toString());
            delegate.processFailed("Server error");
        }
    }
}
