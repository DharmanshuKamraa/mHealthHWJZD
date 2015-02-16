package edu.dartmouth.zhenma.eat;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends Activity {

    private EditText userEdit;
    private EditText PasswdEdit;
    private TextView mainTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        userEdit = (EditText)findViewById(R.id.login_user_edit);
        PasswdEdit = (EditText)findViewById(R.id.login_passwd_edit);
        mainTitle = (TextView)findViewById(R.id.main_title);
//
//        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Gabrielle.ttf");
//        mainTitle.setTypeface(type);

//        userEdit.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
//        userEdit.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
        setContentView(R.layout.activity_login);
    }

    public void onSignInBtnClicked(){

    }

    public void onSkipBtnClicked(){

    }
}
