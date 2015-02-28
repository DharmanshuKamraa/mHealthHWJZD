package info.androidhive.slidingmenu.Application;

import android.app.Application;

/**
 * Created by Dharmanshu on 2/27/15.
 */
public class SmartCartApplication extends Application {
    private String logged_in_user_token = "None";
    private Boolean is_logged_in = false;
    private String logged_in_username = "None";

    public String setLoggedInUsername(String s) {
        logged_in_username= s;
        return logged_in_username;
    }

    public String getLoggedInUsername() {
        return logged_in_username;
    }

    public String getUserToken() {
        return logged_in_user_token;
    }

    public String setUserToken(String token) {
        logged_in_user_token = token;
        return logged_in_user_token;
    }

    public Boolean setLoggedIn(Boolean x) {
        is_logged_in = x;
        return is_logged_in;
    }
}
