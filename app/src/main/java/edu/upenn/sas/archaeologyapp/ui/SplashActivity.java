package edu.upenn.sas.archaeologyapp.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Bundle;
import edu.upenn.sas.archaeologyapp.R;
import edu.upenn.sas.archaeologyapp.util.Constants;
import edu.upenn.sas.archaeologyapp.util.ExtraTypes.InjectableFunc;

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

        InjectableFunc handleTokenWithSuccess = () -> SplashActivity.super.startActivityUsingIntent(MainActivity.class);
        InjectableFunc handleTokenWithoutSuccess = () -> SplashActivity.super.startActivityUsingIntent(LoginActivity.class);

        new Handler().postDelayed(new Runnable() {
            /**
             * Run the handler
             */
            @Override
            public void run()
            {
                String token =  (getToken(context));
                tokenHaveAccess(token, context, handleTokenWithSuccess, handleTokenWithoutSuccess );
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