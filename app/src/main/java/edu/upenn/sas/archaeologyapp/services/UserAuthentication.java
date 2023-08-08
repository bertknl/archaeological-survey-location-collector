package edu.upenn.sas.archaeologyapp.services;

import static edu.upenn.sas.archaeologyapp.util.Constants.LOGIN_SERVER_URL;
import static edu.upenn.sas.archaeologyapp.util.Constants.TOKEN_ACCESS_TESTING_URL;


import android.content.SharedPreferences;
import android.content.Context;
import android.widget.Toast;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import edu.upenn.sas.archaeologyapp.util.ExtraUtils.InjectableFunc;


/**
 * This class contains various static methods to authenticate the user via user's token, user names and passwords.
 */
public class UserAuthentication {

    /**
     * This method get the encrypted sharedPreferences. It has to be encrypted for safety.
     *
     * @param context The context of the class that calls this method.
     * @return A sharedPreferences object that is encrypted.
     */
    static public SharedPreferences getEncryptedSharedPreferences(Context context) {

        try {
            return EncryptedSharedPreferences.create(
                    "encrypted_preferences",
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method get the token from the context.
     *
     * @param context The context of the class that calls this method.
     * @return A token string
     */
    static public String getToken(Context context) {
        try {
            return getToken(getEncryptedSharedPreferences(context));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * This method get the token from the context.
     *
     * @param sharedPreferences The shared preferences object from which we get the token.
     * @return A token string
     */
    static public String getToken(SharedPreferences sharedPreferences) {
        try {
            return sharedPreferences.getString("token", "");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * This method get the token from the context.
     *
     * @param token   The token string we want to store
     * @param context The context of the class that calls this method.
     * @return a boolean value indicating success or failure of the operation.
     */
    static public boolean setToken(String token, Context context) {
        try {
            return setToken(token, UserAuthentication.getEncryptedSharedPreferences(context));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method get the token from the context.
     *
     * @param token             The token string we want to store
     * @param sharedPreferences The shared preferences object to which we set the token.
     * @return a boolean value indicating success or failure of the operation.
     */
    static public boolean setToken(String token, SharedPreferences sharedPreferences) {
        try {
            sharedPreferences.edit().putString("token", token).apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method get the token from the context. It does so by sending the userName and userPassword to the login server url to get a token.
     *
     * @param userName           The name of the user
     * @param userPassword       The password of the user
     * @param context            The context of the calling function
     * @param handleLoginSuccess The method to call when the request returns a token
     * @param handleLoginFailure The method to call when it is a bad request
     * @param queue              The request queue to which the created requests is added
     * @return a boolean value indiccating success or failure of the operation.
     */
    static public void tryLogin(String userName, String userPassword, Context context, InjectableFunc handleLoginSuccess, InjectableFunc handleLoginFailure, RequestQueue queue) {

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("username", userName);
            object.put("password", userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_SERVER_URL, object,
                response -> {
                    try {
                        String token = response.getString("token");
                        setToken(token, context);
                        handleLoginSuccess.apply();
                    } catch (JSONException e) {
                        Toast.makeText(context, "Unexpected error parsing return JSON", Toast.LENGTH_SHORT).show();
                        System.out.println("Unexpected error parsing return JSON");
                        throw new RuntimeException(e);
                    }

                }, error -> {
            handleLoginFailure.apply();

        });
        queue.add(jsonObjectRequest);

    }

    /**
     * This is a simplified testing of the validity of the token
     * On the field, we can't always check if the login is valid by connecting to the internet. In case the internet is down, we
     * simply assume a token not null and not disabled_string is one that is valid because it has been set before and not been logged out yet.
     *
     * @param token                  The string token that we want to check if it can be used to access the api
     * @param handleTokenWithSuccess The method to call when the token is valid
     * @param handleTokenWithoutSuccess The method to call when the token is invalid

     */

    static public void simpleTokenHaveAccess(String token, InjectableFunc handleTokenWithSuccess, InjectableFunc handleTokenWithoutSuccess) {

        if (token != null && !token.equals("") && !token.equals("disabled_string")){

            handleTokenWithSuccess.apply();
        }else{
            handleTokenWithoutSuccess.apply();
        }
    }


        /**
         * This method tests the validity of the token by sending a simple request that needs a proper token.
         *
         * @param token                  The string token that we want to check if it can be used to access the api
         * @param handleTokenWithSuccess The method to call when the token is valid
         * @param handleTokenWithoutSuccess The method to call when the token is invalid
         * @param queue                  The request queue to which the created requests is added
         */
    static public void tokenHaveAccess(String token, InjectableFunc handleTokenWithSuccess, InjectableFunc handleTokenWithoutSuccess, RequestQueue queue) {

        Request jsonArrayRequest = new JsonArrayRequest(TOKEN_ACCESS_TESTING_URL,
                response -> {
                    handleTokenWithSuccess.apply();
                }, error -> {
            handleTokenWithoutSuccess.apply();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token);
                return params;
            }
        };

        queue.add(jsonArrayRequest);

    }
}
