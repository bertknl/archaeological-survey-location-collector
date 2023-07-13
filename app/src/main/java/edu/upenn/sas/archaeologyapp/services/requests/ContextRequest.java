package edu.upenn.sas.archaeologyapp.services.requests;

import static edu.upenn.sas.archaeologyapp.ui.DataEntryActivity.getMaterialCategoryOptions;
import static edu.upenn.sas.archaeologyapp.util.Constants.CONTEXT_URL;
import static edu.upenn.sas.archaeologyapp.util.Constants.DEFAULT_VOLLEY_TIMEOUT;
import static edu.upenn.sas.archaeologyapp.util.ExtraUtils.putString;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContextRequest {

    public static String getContextURL(String hemisphere, int zone, int easting, int northing){
//
        return CONTEXT_URL.replace("<hemisphere>", hemisphere)
                .replace("<zone>", Integer.toString(zone))
                .replace("<easting>",Integer.toString(easting))
                .replace("<northing>", Integer.toString(northing));

    }

    public static void contextRequest(final String URL, String token, RequestQueue queue, Context context, @Nullable Response.Listener<JSONArray> reRequestHandler  ) {

        if (reRequestHandler == null){
            reRequestHandler = getRequestSuccessHandler(context);
        }

        Request jsonArrayRequest = new JsonArrayRequest(URL, reRequestHandler, getRequestFailureHandler(context )) {
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

        Response.Listener<JSONArray> successHandler = response -> {

            System.out.println(response);
            //putString("contextListForCurrentLocation", response.toString(), context);
        };
        return successHandler;
    }


    private static Response.ErrorListener getRequestFailureHandler(Context context ) {
        Response.ErrorListener handleFailure = error -> {
            System.out.println("####");
            System.out.println(error);
            System.out.println("####");
        };
        return handleFailure;
    }


}
