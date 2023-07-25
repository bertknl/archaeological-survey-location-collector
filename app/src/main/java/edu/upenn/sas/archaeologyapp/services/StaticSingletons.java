package edu.upenn.sas.archaeologyapp.services;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashSet;

import edu.upenn.sas.archaeologyapp.util.ExtraUtils;

//The use of the Singleton pattern here ensures that we only create request queue for all the different RESTFUL requests.
public class StaticSingletons {

  private static RequestQueue requestQueue;
  private static HashSet<ExtraUtils.ImagePathBucketIDPair> imagesToIgnore;

    public static synchronized RequestQueue getRequestQueueSingleton(Context context){
      return requestQueue != null ? requestQueue : (requestQueue = Volley.newRequestQueue(context.getApplicationContext()));
  }
    public static synchronized  HashSet<ExtraUtils.ImagePathBucketIDPair> getImagesToIgnore(){
        return imagesToIgnore != null ? imagesToIgnore : (imagesToIgnore = new HashSet<ExtraUtils.ImagePathBucketIDPair>());
    }



}
