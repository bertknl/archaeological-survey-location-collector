package edu.upenn.sas.archaeologyapp.ui;


import static edu.upenn.sas.archaeologyapp.models.UserAuthentication.tryLogin;
import edu.upenn.sas.archaeologyapp.util.ExtraTypes.StatusFunction;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import edu.upenn.sas.archaeologyapp.R;
import edu.upenn.sas.archaeologyapp.util.ExtraTypes;

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

            //after success login, we save the login token, we switch to the correct page
            StatusFunction handleSuccess = () -> {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                LoginActivity.super.startActivityUsingIntent(MainActivity.class);
            };
            StatusFunction handleFailure = () -> {
                Toast.makeText(context, "Wrong username and/or password ", Toast.LENGTH_SHORT).show();
            };

            if (userNameStr != null && !userNameStr.isEmpty() && userPasswordStr !=null && !userPasswordStr.isEmpty()){
                tryLogin(userNameStr, userPasswordStr, context, handleSuccess, handleFailure);

            }else{
                Toast.makeText(LoginActivity.this, "Both fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
