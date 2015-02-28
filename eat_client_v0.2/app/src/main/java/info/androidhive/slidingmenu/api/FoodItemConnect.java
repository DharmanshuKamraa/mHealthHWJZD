package info.androidhive.slidingmenu.api;

import android.util.Log;

import org.json.JSONObject;

import info.androidhive.slidingmenu.interfaces.FoodItemAsyncResponse;

/**
 * Created by Dharmanshu on 2/28/15.
 */
public class FoodItemConnect extends ServerConnect {
    public FoodItemAsyncResponse delegate= null;

    protected void onPostExecute(String result) {
        Log.i("results" , result);
        try {
            JSONObject jsonObj = new JSONObject(result);
            if ((jsonObj.getInt("code") >= 200) && (jsonObj.getInt("code") <= 210)) {
                if (jsonObj.has("object")) {
                    delegate.processItemListFetched(jsonObj.getString("object"));
                } else {
                    delegate.processItemListFetched(jsonObj.getString("objects"));
                }
            } else {
                delegate.processFailed("Could not fetch list.");
            }
        } catch (Exception e) {
            Log.i("exception" , e.toString());
            delegate.processFailed("Server error");
        }



    }
}
