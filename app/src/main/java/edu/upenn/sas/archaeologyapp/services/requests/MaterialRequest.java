package edu.upenn.sas.archaeologyapp.services.requests;

import static edu.upenn.sas.archaeologyapp.util.Constants.DEFAULT_VOLLEY_TIMEOUT;
import static edu.upenn.sas.archaeologyapp.util.ExtraUtils.putString;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MaterialRequest {



    public static void materialRequest(final String URL, String token, RequestQueue queue, Context context, boolean materialGeneralResponsePreviouslyLoaded, String PREFERENCES) {

        Request jsonArrayRequest = new JsonArrayRequest(URL, getRequestSuccessHandler(context), getRequestFailureHandler(context, materialGeneralResponsePreviouslyLoaded)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonArrayRequest);
    }

    private static Response.Listener<JSONArray> getRequestSuccessHandler(Context context) {

        Listener<JSONArray> successHandler = response -> putString("materialGeneralAPIResponse", response.toString(), context);
        System.out.println("Successful request for material");
        return successHandler;
    }


    private static ErrorListener getRequestFailureHandler(Context context, boolean materialGeneralResponsePreviouslyLoaded) {
        ErrorListener handleFailure = error -> {
            if (!materialGeneralResponsePreviouslyLoaded) {
                Toast.makeText(context, "Could not load general materials (Communication error): "
                        + error, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };
        return handleFailure;
    }

}
