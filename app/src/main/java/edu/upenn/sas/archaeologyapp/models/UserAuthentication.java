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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import edu.upenn.sas.archaeologyapp.util.ExtraTypes;
import edu.upenn.sas.archaeologyapp.util.ExtraTypes.StatusFunction;

public class UserAuthentication {


    @FunctionalInterface
    interface Headers {
        void apply(Map<String, String> headers);

    }

    static public SharedPreferences getSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

        return EncryptedSharedPreferences.create(
                "encrypted_preferences", // fileName
                masterKeyAlias, // masterKeyAlias
                context, // context
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
        );

    }

    static public String getToken(Context context) {
        try {
            return UserAuthentication.getSharedPreferences(context).getString("token", "");
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
            UserAuthentication.getSharedPreferences(context).edit().putString("token", token).apply();
            return true;
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

    static public void tryLogin(String userName, String userPassword, Context context, StatusFunction handleSuccess, StatusFunction handleFailure) {

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
                    handleSuccess.apply();
                    System.out.println(response);
                }, error -> {
            handleFailure.apply();

        });
        sendRequest(jsonObjectRequest, context);
    }

    static public void tokenHaveAccess(String token, Context context, ExtraTypes.ChangeActivityFunction changeToLoginActivity,  ExtraTypes.ChangeActivityFunction changeToMainActivity  ) {
        StatusFunction handleSuccess = () -> {

            Toast.makeText(context, "token is valid!", Toast.LENGTH_SHORT).show();
            System.out.println("token is valid");
            changeToMainActivity.apply();
        };
        StatusFunction handleFailure = () -> {
            Toast.makeText(context, "token is invalid", Toast.LENGTH_SHORT).show();
            System.out.println("token is invalid");
            changeToLoginActivity.apply();
        };

        Request jsonArrayRequest = new JsonArrayRequest(TOKEN_ACCESS_TESTING_URL,
                response -> {
                    handleSuccess.apply();
                }, error -> {

            handleFailure.apply();

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
