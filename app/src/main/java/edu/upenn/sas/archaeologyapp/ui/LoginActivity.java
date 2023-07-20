package edu.upenn.sas.archaeologyapp.ui;


import static edu.upenn.sas.archaeologyapp.services.RequestQueueSingleton.getRequestQueueSingleton;
import static edu.upenn.sas.archaeologyapp.services.UserAuthentication.tryLogin;
import edu.upenn.sas.archaeologyapp.util.ExtraUtils.InjectableFunc;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import edu.upenn.sas.archaeologyapp.R;

public class LoginActivity extends BaseActivity{
    Context context = this;
    RequestQueue queue ;
    private InjectableFunc handleLoginSuccess = () -> {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
        LoginActivity.super.startActivityUsingIntent(MainActivity.class);
    };
    private InjectableFunc handleLoginFailure = () -> {
        Toast.makeText(context, "Wrong username and/or password ", Toast.LENGTH_SHORT).show();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        queue =  getRequestQueueSingleton(getApplicationContext());;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.loginButton).setOnClickListener(view -> {
            EditText userName = (findViewById(R.id.userName));
            EditText userPassword = (findViewById(R.id.userPassword));
            String userNameStr = (userName.getText().toString());
            String userPasswordStr =   (userPassword.getText().toString());

            if (userNameStr != null && !userNameStr.isEmpty() && userPasswordStr !=null && !userPasswordStr.isEmpty()){
                tryLogin(userNameStr, userPasswordStr, context, handleLoginSuccess, handleLoginFailure, queue);

            }else{
                Toast.makeText(LoginActivity.this, "Both fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
