package edu.upenn.sas.archaeologyapp.services;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//The use of the Singleton pattern here ensures that we only create request queue for all the different RESTFUL requests.
public class RequestQueueSingleton {

  private static RequestQueue requestQueue;
  public static synchronized RequestQueue getRequestQueueSingleton(Context context){
      return requestQueue != null ? requestQueue : (requestQueue = Volley.newRequestQueue(context.getApplicationContext()));

  }
}
