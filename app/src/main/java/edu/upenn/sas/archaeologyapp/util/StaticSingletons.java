package edu.upenn.sas.archaeologyapp.util;

import static edu.upenn.sas.archaeologyapp.util.ExtraUtils.createImagePathBucketIDPairConcurrentHashSet;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import java.util.Set;

import edu.upenn.sas.archaeologyapp.util.ExtraUtils.ImagePathBucketIDPair;

/**
 * These singletons allow the initialization of only one time across the whole application.
 *
 * requestQueue: all requests of different types are added to this one queue
 * imagesToIgnore: if we are already sending certain images, we add these images here to block them from being resended.
 */

public class StaticSingletons {


    private static RequestQueue requestQueue;
    private static Set<ImagePathBucketIDPair> imagesToIgnore;


    public static synchronized RequestQueue getRequestQueueSingleton(Context context) {
        return requestQueue != null ? requestQueue : (requestQueue = Volley.newRequestQueue(context.getApplicationContext()));
    }

    public static synchronized Set<ImagePathBucketIDPair> getImagesToIgnore() {
        //Notice we use concurrentHashMap to create a thread-safe hashset
        return imagesToIgnore != null ? imagesToIgnore : (imagesToIgnore = createImagePathBucketIDPairConcurrentHashSet());

    }


}
