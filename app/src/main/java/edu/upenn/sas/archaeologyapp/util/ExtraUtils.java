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

    public static class ImagePathBucketIDPair{
        private String imagePath;
        private String bucketID;
        public ImagePathBucketIDPair(String imagePath,  String bucketID){
            this.imagePath = imagePath;
            this.bucketID = bucketID;
        }

        public String getImagePath(){
            return this.imagePath;
        }

        public String getBucketID(){
            return this.bucketID;
        }
    }
    public static class ServerUUIDBucketIDPair{
        private String serverUUID;
        private String bucketID;
        public ServerUUIDBucketIDPair(String imagePath,  String bucketID){
            this.serverUUID = imagePath;
            this.bucketID = bucketID;
        }

        public String getServerUUID(){
            return this.serverUUID;
        }

        public String getBucketID(){
            return this.bucketID;
        }
    }


}
