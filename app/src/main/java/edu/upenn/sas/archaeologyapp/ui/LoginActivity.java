package edu.upenn.sas.archaeologyapp.ui;


import static edu.upenn.sas.archaeologyapp.models.SecretData.setToken;
import static edu.upenn.sas.archaeologyapp.util.Constants.LOGIN_SERVER_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;

import edu.upenn.sas.archaeologyapp.R;
import edu.upenn.sas.archaeologyapp.util.Constants;

public class LoginActivity extends BaseActivity{
    Context context = this;



    private void loginAndGetToken(String userName, String userPassword){
        try {
            setToken(userPassword, context);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("username",userName);
            object.put("password",userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_SERVER_URL, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("String Response : "+ response.toString());
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Wrong username and/or password ", Toast.LENGTH_SHORT).show();

                System.out.println(("Error getting response"));
                System.out.println(error);
            }


        });

        RequestQueue queue = Volley.newRequestQueue(this);
//          System.out.println("Whatsupp");
        queue.add(jsonObjectRequest);
//



    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Setting the layout for this activity
        setContentView(R.layout.activity_login);
        // Wait for specified time and start main activity
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userName = (findViewById(R.id.userName));
                EditText userPassword = (findViewById(R.id.userPassword));
                String userNameStr = (userName.getText().toString());
                String userPasswordStr =   (userPassword.getText().toString());
                if (userNameStr != null && !userNameStr.isEmpty() && userPasswordStr !=null && !userPasswordStr.isEmpty()){
                    loginAndGetToken(userNameStr, userPasswordStr);
                }else{
                    Toast.makeText(LoginActivity.this, "Both fields cannot be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



}
