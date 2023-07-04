package edu.upenn.sas.archaeologyapp.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

import edu.upenn.sas.archaeologyapp.R;
import edu.upenn.sas.archaeologyapp.util.Constants;
import static edu.upenn.sas.archaeologyapp.models.SecretData.getToken;
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

                //Here check if we have already saved the token. If no, we go to login screen
                System.out.println(1);

                try {
                    System.out.println(getToken(context));
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(2);
                SplashActivity.super.startActivityUsingIntent(LoginActivity.class);
               // SplashActivity.super.startActivityUsingIntent(MainActivity.class);
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