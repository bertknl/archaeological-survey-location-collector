package edu.upenn.sas.archaeologyapp.ui;


import android.content.Context;
import android.os.Handler;
import android.os.Bundle;

import java.io.IOException;
import java.security.GeneralSecurityException;

import edu.upenn.sas.archaeologyapp.R;
import edu.upenn.sas.archaeologyapp.util.Constants;
import edu.upenn.sas.archaeologyapp.util.ExtraTypes;

import static edu.upenn.sas.archaeologyapp.models.UserAuthentication.getToken;
import static edu.upenn.sas.archaeologyapp.models.UserAuthentication.tokenHaveAccess;

/**
 * The splash activity
 * @author Created by eanvith on 24/12/16.
 */
public class SplashActivity extends BaseActivity
{
    private Context context = this;

    /**
     * App is launched
     * @param savedInstanceState - state from memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Make this a fullscreen activity
        super.requestFullScreenActivity();
        // Setting the layout for this activity
        setContentView(R.layout.activity_splash);

        ExtraTypes.ChangeActivityFunction changeToLoginActivity = () ->{SplashActivity.super.startActivityUsingIntent(LoginActivity.class);};
        ExtraTypes.ChangeActivityFunction changeToMainActivity = () ->{SplashActivity.super.startActivityUsingIntent(MainActivity.class);};

        new Handler().postDelayed(new Runnable() {
            /**
             * Run the handler
             */
            @Override
            public void run()
            {
                String token =  (getToken(context));
                token = "***REMOVED***";
                tokenHaveAccess(token, context, changeToLoginActivity, changeToMainActivity );
            }
        }, Constants.SPLASH_TIME_OUT);
    }

    /**
     * Disable on back pressed - this is the splash screen, the user will automatically be taken to the next screen.
     */
    @Override
    public void onBackPressed()
    {
    }
}