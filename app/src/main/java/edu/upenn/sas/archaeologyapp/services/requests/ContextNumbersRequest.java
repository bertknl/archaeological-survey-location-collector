package edu.upenn.sas.archaeologyapp.services.requests;

import static edu.upenn.sas.archaeologyapp.util.Constants.CONTEXT_URL;
import static edu.upenn.sas.archaeologyapp.util.Constants.DEFAULT_VOLLEY_TIMEOUT;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that contains static functions for making a request to get the context numbers from the server of a given location
 */
public class ContextNumbersRequest {


    /**
     * Making a request to get all the finds of a specific location, so that we can filter their contexts later.
     * @param URL: The URL to which this request is sent. It is created using getContextURL() in the same class
     * @param token: The token that must exist to authenticate the requests
     * @param queue: The request queue to which the created request is put to
     * @param successListener: The method that is going to get executed when the RESTful request returns successfully.
     * @param failureListener: The method that is going to get executed when the requset fails.
     *
     */
    public static void contextNumbersRequest(final String URL, String token, RequestQueue queue,   Response.Listener<JSONArray> successListener, Response.ErrorListener failureListener  ) {

        Request jsonArrayRequest = new JsonArrayRequest(URL, successListener, failureListener) {
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

    /**
     * Create a URL string for contextNumbersRequest()
     * @param hemisphere: The hemisphere (north or south)
     * @param zone: The geographical zone
     * @param easting: The geographical easting coordinate
     * @param northing: The geographical northing coordinate
     *  @return Returns a string of the url for the request.
     */
    public static String getContextURL(String hemisphere, int zone, int easting, int northing){
//
        return CONTEXT_URL.replace("<hemisphere>", hemisphere)
                .replace("<zone>", Integer.toString(zone))
                .replace("<easting>",Integer.toString(easting))
                .replace("<northing>", Integer.toString(northing));

    }

    /**
     * Filter the result of contextNumbersRequests, which is a json array of finds, into an String array of context numbers which the method returns.
     * @param jsonArray The JSON array of all the finds in a location
     * @return Returns a string array of the context numbers of the location
     */
    public static String [] contextJSONArray2ContextStrArray(JSONArray jsonArray){

        List<String> contextNumbersList = new ArrayList<String>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                contextNumbersList.add(String.valueOf(obj.getInt("context_number")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        String [] contextNumbersString = contextNumbersList.toArray(new String[0]);
        return contextNumbersString;
    }

}
