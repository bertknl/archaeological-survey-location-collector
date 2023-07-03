package edu.upenn.sas.archaeologyapp.ui;

import android.os.Bundle;
import android.os.Handler;

import edu.upenn.sas.archaeologyapp.R;
import edu.upenn.sas.archaeologyapp.util.Constants;

public class LoginActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Setting the layout for this activity
        setContentView(R.layout.activity_login);
        // Wait for specified time and start main activity
        new Handler().postDelayed(new Runnable() {
            /**
             * Run the handler
             */
            @Override
            public void run()
            {
                // Here' we change the starting screen from Mainactivity to Login.
                //Without a proper login token. We do not allow using the application.
                //SplashActivity.super.startActivityUsingIntent(MainActivity.class);
            }
        }, Constants.SPLASH_TIME_OUT);
    }



}
