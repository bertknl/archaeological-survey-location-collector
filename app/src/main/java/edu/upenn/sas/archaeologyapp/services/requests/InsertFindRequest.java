package edu.upenn.sas.archaeologyapp.services.requests;

import static android.provider.Settings.Global.getString;
import static edu.upenn.sas.archaeologyapp.ui.DataEntryActivity.PREFERENCES;
import static edu.upenn.sas.archaeologyapp.ui.DataEntryActivity.parseMaterialGeneralAPIResponse;
import static edu.upenn.sas.archaeologyapp.util.Constants.DEFAULT_VOLLEY_TIMEOUT;
import static edu.upenn.sas.archaeologyapp.util.ExtraUtils.putString;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.upenn.sas.archaeologyapp.R;

public class InsertFindRequest {


    public static void insertFindRequest(final String URL, String token, RequestQueue queue, Context context, boolean materialGeneralResponsePreviouslyLoaded, String PREFERENCES) {

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
        Response.Listener<JSONArray> successHandler = response -> {};
        return successHandler;
    }


    private static Response.ErrorListener getRequestFailureHandler(Context context, boolean materialGeneralResponsePreviouslyLoaded) {
        Response.ErrorListener handleFailure = error -> {};
        return handleFailure;
    }


    private class MaterialParameters{

    }

    public @NonNull MaterialParameters createInsertMaterialParametersObject(Context context, @NonNull String utm_hemisphere, @NonNull int utm_zone, @NonNull int area_utm_easting_meters, @NonNull int area_utm_northing_meters, @NonNull int context_number, String material, String category, String director_notes) {

        //1. Checking the parameters are alright
        Objects.requireNonNull(utm_hemisphere);
        Objects.requireNonNull(utm_zone);
        Objects.requireNonNull(area_utm_easting_meters);
        Objects.requireNonNull(area_utm_northing_meters);
        Objects.requireNonNull(context_number);
        if ((material == null && category != null) || (material != null && category == null)){
            throw new RuntimeException("Material and Category are either both null or both non-null");
        }
        if (material != null && material.length()  > 255){
                throw new RuntimeException("Material cannot be a string with more than 255 characters");
        }
        if (category != null && category.length()  > 255){
                throw new RuntimeException("Category cannot be a string with more than 255 characters");
        }

        if (material != null && category != null){
            SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
            String materialGeneralAPIResponse = settings.getString("materialGeneralAPIResponse", null);
            String materialCategoryOptions[] = parseMaterialGeneralAPIResponse(materialGeneralAPIResponse, context);
            boolean legitMaterialCategory = false;
            for (int i = 0; i < materialCategoryOptions.length; i++){
                if (material +" : " + category == materialCategoryOptions[i]){
                    legitMaterialCategory = true;
                    break;
                }
            }
            if (legitMaterialCategory == false){
                throw new RuntimeException("The material and category is wrong");
            }
        }
        //2. Packing the parameters into a nice JSON Array.

        JSONObject parametersObj = new JSONObject();
        try {
            parametersObj.put("utm_hemisphere",utm_hemisphere);
            parametersObj.put("utm_zone",utm_zone);
            parametersObj.put("area_utm_easting_meters",area_utm_easting_meters);
            parametersObj.put("area_utm_northing_meters",area_utm_northing_meters);
            parametersObj.put("context_number",context_number);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
