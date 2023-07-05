package edu.upenn.sas.archaeologyapp.ui;


import static edu.upenn.sas.archaeologyapp.models.UserAuthentication.loginAndGetToken;
import static edu.upenn.sas.archaeologyapp.models.UserAuthentication.setToken;
import static edu.upenn.sas.archaeologyapp.util.Constants.LOGIN_SERVER_URL;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends BaseActivity{
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.loginButton).setOnClickListener(view -> {
            EditText userName = (findViewById(R.id.userName));
            EditText userPassword = (findViewById(R.id.userPassword));
            String userNameStr = (userName.getText().toString());
            String userPasswordStr =   (userPassword.getText().toString());
            if (userNameStr != null && !userNameStr.isEmpty() && userPasswordStr !=null && !userPasswordStr.isEmpty()){
                loginAndGetToken(userNameStr, userPasswordStr, context);

            }else{
                Toast.makeText(LoginActivity.this, "Both fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
