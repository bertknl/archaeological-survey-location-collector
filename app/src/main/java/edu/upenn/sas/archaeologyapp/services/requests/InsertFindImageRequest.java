package edu.upenn.sas.archaeologyapp.services.requests;

import static edu.upenn.sas.archaeologyapp.util.Constants.DEFAULT_VOLLEY_TIMEOUT;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import edu.upenn.sas.archaeologyapp.services.requests.multipartrequests.VolleyMultipartRequest;

/**
 * A class that contains methods for making a request to upload an image onto the server.
 * Notice that we use VolleyMultipartRequest defined in the multipartrequests subfolder.
 */
public class InsertFindImageRequest {

    /**
     * A method that sends a request to upload an image, as an byte array, onto the server.
     * @param URL: The URL to which this request is sent. It is created using getContextURL() in the same class
     * @param token: The token that must exist to authenticate the requests
     * @param imageFile: The byte array of an image
     * @param queue: The request queue to which the created request is put to
     * @param successListener: The method that is going to get executed when the RESTful request returns successfully.
     * @param failureListener: The method that is going to get executed when the requset fails.
     */
    public static void insertFindImageRequest(String token, String URL,byte [] imageFile , RequestQueue queue, Response.Listener<JSONObject> successListener, Response.ErrorListener failureListener ) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL, imageFile, successListener, failureListener) {
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



}
