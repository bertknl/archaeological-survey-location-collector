package edu.upenn.sas.archaeologyapp.ui;


import static edu.upenn.sas.archaeologyapp.util.Constants.LOGIN_SERVER_URL;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upenn.sas.archaeologyapp.R;
import edu.upenn.sas.archaeologyapp.util.Constants;

public class LoginActivity extends BaseActivity{


    //For now, we use
    private void loginAndGetToken(String userName, String userPassword){

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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                }
            }
        });
    }



}
