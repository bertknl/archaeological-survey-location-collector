package edu.upenn.sas.archaeologyapp.services.requests;

import static edu.upenn.sas.archaeologyapp.util.Constants.DEFAULT_VOLLEY_TIMEOUT;
import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import edu.upenn.sas.archaeologyapp.services.requests.multipartrequests.VolleyMultipartRequest;

public class InsertFindImageRequest {

    public static void insertFindImageRequest(Context context, String token, String url,byte [] imageFile , RequestQueue queue) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, imageFile, getRequestSuccessHandler(context), getRequestFailureHandler(context)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(multipartRequest);
    }

    public static void insertFindImageRequest(Context context, String token, String url,byte [] imageFile , RequestQueue queue, Response.Listener<JSONObject> success_listener, Response.ErrorListener failure_listener ) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, imageFile, success_listener, failure_listener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token);
                return params;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(multipartRequest);
    }

    private static Response.Listener<JSONObject> getRequestSuccessHandler(Context context) {
        Response.Listener<JSONObject> successHandler = response -> {
            System.out.println("Success");
            System.out.println("############");
            System.out.println(response);
            System.out.println("############");
        };
        return successHandler;
    }


    private static Response.ErrorListener getRequestFailureHandler(Context context) {
        Response.ErrorListener handleFailure = error -> {
            System.out.println("Failure");
            System.out.println("############");
            System.out.println(error);
            System.out.println("############");
        };
        return handleFailure;
    }

}
