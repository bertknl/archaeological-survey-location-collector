package edu.upenn.sas.archaeologyapp.services;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.upenn.sas.archaeologyapp.util.ExtraUtils;

//The use of the Singleton pattern here ensures that we only create request queue for all the different RESTFUL requests.
public class StaticSingletons {

  private static RequestQueue requestQueue;
  private static Set<ExtraUtils.ImagePathBucketIDPair> imagesToIgnore;

    public static synchronized RequestQueue getRequestQueueSingleton(Context context){
      return requestQueue != null ? requestQueue : (requestQueue = Volley.newRequestQueue(context.getApplicationContext()));
  }
    public static synchronized  Set<ExtraUtils.ImagePathBucketIDPair> getImagesToIgnore(){
        //Notice we use concurrentHashMap to create a thread-safe hashset
        return imagesToIgnore != null ? imagesToIgnore : (imagesToIgnore  = createImagePathBucketIDPairConcurrentHashSet());

    }

  public static  Set<ExtraUtils.ImagePathBucketIDPair> createImagePathBucketIDPairConcurrentHashSet(){
    return  Collections.newSetFromMap(new ConcurrentHashMap<ExtraUtils.ImagePathBucketIDPair, Boolean>());
  }

  public static  Set<ExtraUtils.ServerUUIDBucketIDPair> createServerUUIDBucketIDPairConcurrentHashSet(){
    return  Collections.newSetFromMap(new ConcurrentHashMap<ExtraUtils.ServerUUIDBucketIDPair, Boolean>());
  }


}
