package edu.upenn.sas.archaeologyapp.util;

import static edu.upenn.sas.archaeologyapp.util.Constants.PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public class ExtraUtils {
    @FunctionalInterface
    public interface InjectableFunc {
        void apply();
    }

    @FunctionalInterface
    public interface VolleyErrorHandler {
        void apply(VolleyError error);
    }

    @FunctionalInterface
    public interface JSONArraySuccessHandler {
        void apply(JSONArray success);
    }

    public static void putString(String key, String value, Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
