package edu.upenn.sas.archaeologyapp.services.requests;

import static edu.upenn.sas.archaeologyapp.util.Constants.DEFAULT_VOLLEY_TIMEOUT;
import static edu.upenn.sas.archaeologyapp.util.ExtraUtils.putString;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that contains methods for making a request to get the available materials from the server
 */
public class MaterialRequest {

    /**
     * A method that sends a request to get the available materials from the server
     */
    public static void materialRequest(final String URL, String token, RequestQueue queue, Context context, boolean materialGeneralResponsePreviouslyLoaded) {
        Request jsonArrayRequest = new JsonArrayRequest(URL,
                success_response -> {
                    putString("materialGeneralAPIResponse", success_response.toString(), context);
                    System.out.println("Successful request for material");
                }, error -> {
                    if (!materialGeneralResponsePreviouslyLoaded) {
                        Toast.makeText(context, "Could not load general materials (Communication error): "
                                + error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
                ){
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

}
