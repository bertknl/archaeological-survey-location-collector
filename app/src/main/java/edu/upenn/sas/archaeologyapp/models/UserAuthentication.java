package edu.upenn.sas.archaeologyapp.models;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.upenn.sas.archaeologyapp.ui.LoginActivity;
import edu.upenn.sas.archaeologyapp.util.ExtraTypes;
import edu.upenn.sas.archaeologyapp.util.ExtraTypes.InjectableFunc;

public class UserAuthentication {

    static public SharedPreferences getEncryptedSharedPreferences(Context context)  {

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

    static public String getToken(Context context) {
        try {
            return getToken(getEncryptedSharedPreferences(context));
        } catch (Exception e) {
            return "";
        }
    }

    static public String getToken(SharedPreferences sharedPreferences) {
        try {
            return sharedPreferences.getString("token", "");
        } catch (Exception e) {
            return "";
        }
    }

    static public boolean setToken(String token, Context context)  {
        try {
            return setToken(token, UserAuthentication.getEncryptedSharedPreferences(context));
        } catch (Exception e) {
            return false;
        }
    }

    static public boolean setToken(String token, SharedPreferences sharedPreferences) {
        try {
            sharedPreferences.edit().putString("token", token).apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static public void tryLogin(String userName, String userPassword, Context context, InjectableFunc handleLoginSuccess, InjectableFunc handleLoginFailure) {

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
                        setToken( token, context);
                        handleLoginSuccess.apply();
                    } catch (JSONException e) {
                        Toast.makeText(context, "Unexpected error parsing return JSON", Toast.LENGTH_SHORT).show();
                        System.out.println("Unexpected error parsing return JSON");
                        throw new RuntimeException(e);
                    }

                }, error -> {
            handleLoginFailure.apply();

        });
        sendRequest(jsonObjectRequest, context);
    }

    static public void tokenHaveAccess(String token, Context context, InjectableFunc handleTokenWithSuccess,  InjectableFunc handleTokenWithoutSuccess  ) {

        Request jsonArrayRequest = new JsonArrayRequest(TOKEN_ACCESS_TESTING_URL,
                response -> {
                    handleTokenWithSuccess.apply();
                }, error -> {
            handleTokenWithoutSuccess.apply();
        }) {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        sendRequest(jsonArrayRequest, context);

    }

    static private void sendRequest(Request request, Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }


}
