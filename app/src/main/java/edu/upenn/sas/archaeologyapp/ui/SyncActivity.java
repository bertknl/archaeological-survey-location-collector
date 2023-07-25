package edu.upenn.sas.archaeologyapp.ui;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import edu.upenn.sas.archaeologyapp.R;
import edu.upenn.sas.archaeologyapp.models.PathElement;
import edu.upenn.sas.archaeologyapp.models.StringObjectResponseWrapper;
import edu.upenn.sas.archaeologyapp.models.DataEntryElement;
import edu.upenn.sas.archaeologyapp.services.DatabaseHandler;
import edu.upenn.sas.archaeologyapp.util.ExtraUtils;
import edu.upenn.sas.archaeologyapp.util.ExtraUtils.ImagePathBucketIDPair;

import static edu.upenn.sas.archaeologyapp.services.StaticSingletons.getImagesToIgnore;
import static edu.upenn.sas.archaeologyapp.services.StaticSingletons.getRequestQueueSingleton;
import static edu.upenn.sas.archaeologyapp.services.UserAuthentication.getToken;
import static edu.upenn.sas.archaeologyapp.services.VolleyStringWrapper.makeVolleyStringObjectRequest;
import static edu.upenn.sas.archaeologyapp.services.requests.InsertFindImageRequest.insertFindImageRequest;
import static edu.upenn.sas.archaeologyapp.services.requests.InsertFindRequest.createInsertMaterialParametersObject;
import static edu.upenn.sas.archaeologyapp.services.requests.InsertFindRequest.insertFindRequest;
import static edu.upenn.sas.archaeologyapp.util.Constants.INSERT_FIND_URL;
import static edu.upenn.sas.archaeologyapp.util.Constants.globalWebServerURL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This activity is responsible for uploading all the records from the local database onto a server.
 * @author eanvith, Colin Roberts, Christopher Besser.
 */



public class SyncActivity extends AppCompatActivity
{
    // The button the user clicks to initiate the sync process
    Button syncButton;

    Context context;
    ProgressBar progressBar;
    TextView logText;
    AtomicInteger counter = new AtomicInteger();
    int totalImages;
    private HashMap<String, Integer> imageNumbers = new HashMap<>();
    // A list of records populated from the local database, that need to be uploaded onto the server
    ArrayList<DataEntryElement> elementsToUpload;
    // The index of the find currently being uploaded
    int uploadIndex, totalItems, pathUploadIndex, totalPaths;
    // A list of paths populated from the local database, that need to be uploaded onto the server
    ArrayList<PathElement> pathsToUpload;
    // A database helper class object that enables fetching of records from the local database
    DatabaseHandler databaseHandler;

    HashSet<ImagePathBucketIDPair> imagesToIgnore;
    // A request queue to handle python requests
    RequestQueue queue;
    /**
     * Activity is launched
     * @param savedInstanceState - saved state from memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;
        imagesToIgnore = getImagesToIgnore();
        setContentView(R.layout.activity_sync);
        queue = getRequestQueueSingleton(getApplicationContext());
        // Initialize the database helper class object, and read in the records from the local database
        databaseHandler = new DatabaseHandler(this);
        elementsToUpload = databaseHandler.getUnsyncedFindsRows();
        pathsToUpload = databaseHandler.getUnsyncedPathsRows();
        totalItems = elementsToUpload.size();
        uploadIndex = 0;
        totalPaths = pathsToUpload.size();
        pathUploadIndex = 0;
        progressBar = findViewById(R.id.progress);
        logText = findViewById(R.id.logText);
        counter.set(0);
        totalImages = 0;
        for (DataEntryElement e: elementsToUpload) {
            totalImages += e.getImagePaths().size();
        }
        // Attach a click listener to the sync button, and trigger the sync process on click of the button
        syncButton = findViewById(R.id.sync_button_sync_activity);
        syncButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sync records
             * @param v - sync button
             */
            @Override
            public void onClick(View v)
            {
                // totalItems is 0, so nothing to sync
                if (uploadIndex >= totalItems && pathUploadIndex >= totalPaths)
                {
                    Toast.makeText(SyncActivity.this, "There are no records to sync.", Toast.LENGTH_SHORT).show();
                }
//                try {
//                    newUploadFinds();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                // Disable the sync button while the sync is in progress
             //   syncButton.setEnabled(false);
               // qUploadFinds();
                System.out.println(1);

                System.out.println(2);
                // Start uploading unsynced items
                //uploadFinds();
                // Start uploading unsynced paths
                //uploadPaths();

            }
        });
    }


    private void qUploadFinds(){
        for(int i = uploadIndex; i < totalItems; i++) {
            uploadFind(i);
        }
    }




    private void newUploadFinds( ) throws IOException {

        Map <JSONObject, String> insertFindIDPairs = new HashMap<JSONObject, String>();
        Map< JSONObject, ArrayList<byte[]> > insertFindImagePairs = new HashMap<JSONObject, ArrayList<byte[]>>() ;  //Only got sent when it has an id.


        for(int i = uploadIndex; i < totalItems; i++) {
            //Don't think this complicatedly
            final DataEntryElement find = (elementsToUpload.get(i));
            String utm_hemisphere = find.getHemisphere();
            int utm_zone = find.getZone();
            int area_utm_easting_meters = find.getEasting();
            int area_utm_northing_meters = find.getNorthing();
            String context_number_string_stripped = (find.getContextNumber()).replaceAll("[^\\d.]", "");
            ;
            int context_number = Integer.parseInt(context_number_string_stripped);
            String material_category_pair = find.getMaterial();
            String material = material_category_pair.split(" : ")[0].trim();
            String category = material_category_pair.split(" : ")[1].trim();
            String directory_notes = find.getComments();
            JSONObject insertFindRequestParametersObject = createInsertMaterialParametersObject(context, utm_hemisphere, utm_zone, area_utm_easting_meters, area_utm_northing_meters, context_number, material, category, directory_notes);
            insertFindIDPairs.put(insertFindRequestParametersObject, null);
            ArrayList<String> finds = find.getImagePaths();
            ArrayList<byte[]> allPhotosOfAnFind = new ArrayList<byte[]>();
            for (int j = 0; j < finds.size(); j++) {

                File fi = new File(finds.get(j));
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    byte[] fileContent = Files.readAllBytes(fi.toPath());
                    allPhotosOfAnFind.add(fileContent);
                }
            }
            insertFindImagePairs.put(insertFindRequestParametersObject,allPhotosOfAnFind);
        }



    }

    private Response.Listener<JSONObject> getRequestSuccessHandler(Context context, DataEntryElement find) {
        Response.Listener<JSONObject> successHandler = response -> {
            try {
                find.setFindUUID(response.getString("id"));
                System.out.println("We just set the FINDUUID into the find in the database!" + response.getString("id"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            databaseHandler.setFindSynced(find);
        };
        return successHandler;
    }
    private void uploadFind(int index){
        final DataEntryElement find = elementsToUpload.get(index);
        String utm_hemisphere = find.getHemisphere();
        int utm_zone =  find.getZone();
        int area_utm_easting_meters =  find.getEasting() ;
        int area_utm_northing_meters =  find.getNorthing();
        String context_number_string_stripped =  (find.getContextNumber()).replaceAll("[^\\d.]", "");;
        int context_number =  Integer.parseInt(context_number_string_stripped) ;
        //To avoid some complexity, we use a single field material to store and get both the material and category
        String material_category_pair =find.getMaterial();
        String material = material_category_pair.split(" : ")[0].trim();
        String category = material_category_pair.split(" : ")[1].trim();
        String directory_notes = find.getComments();
        JSONObject insertFindRequestParametersObject = createInsertMaterialParametersObject(context, utm_hemisphere, utm_zone, area_utm_easting_meters,area_utm_northing_meters,context_number, material, category, directory_notes);
        insertFindRequest(INSERT_FIND_URL, insertFindRequestParametersObject,getToken(context), queue,context, getRequestSuccessHandler(context, find));

//        ArrayList<String> imagePaths = find.getImagePaths();
//        for (int i = 0; i < imagePaths.size(); i++){
//            File fi = new File(imagePaths.get(0));
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                try {
//                    byte[] fileContent = Files.readAllBytes(fi.toPath());
//
//                    String url = "https://j20200007.kotsf.com/asl/api/find/15b78e84-2136-43d4-aaea-a292605bcca5/photo/";
//                    insertFindImageRequest(context, getToken(context), url , fileContent, queue);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }


    }


    private void filterFinishedOrProcessingImages(HashSet<ImagePathBucketIDPair> ImagePathBucketIDPairs, HashSet<ImagePathBucketIDPair> imagesToIgnore){

        ImagePathBucketIDPairs.removeAll(imagesToIgnore);
    }


    private void uploadImages(){


        //1. Go through all images not synced
        HashSet<ImagePathBucketIDPair> ImagePathBucketIDPairs =  databaseHandler.getAllImagesUnsynched();
        filterFinishedOrProcessingImages(ImagePathBucketIDPairs, imagesToIgnore);

        //2. Filter out the images that are in the table
        //3. Filiter image requests based on uuid and been_synched in table Bucket.
        //4. For loop do each request,
                //Before response: diable in the current image being uploaded table
                //If Response is good, Then set image as synced
                //If response is error, we do nothing(Maybe just show an error)
                //Only reenable the insertion when the request failed.
        //
    }





    /**
     * Upload a find to the database
     */
    private void uploadFinds()
    {
        if (uploadIndex < totalItems)
        {
            final DataEntryElement find = elementsToUpload.get(uploadIndex);
            String zone = Integer.toString(find.getZone());
            String hemisphere = find.getHemisphere();
            String easting = Double.toString(find.getPreciseEasting());
            String northing = Double.toString(find.getPreciseNorthing());
            String sample = Integer.toString(find.getSample());
            String contextEasting = Integer.toString(find.getEasting());
            String contextNorthing = Integer.toString(find.getNorthing());
            String latitude = Double.toString(find.getLatitude());
            String longitude = Double.toString(find.getLongitude());
            String altitude = Double.toString(find.getAltitude());
            String status = find.getStatus();
            String material = find.getMaterial();
            String ARratio = Double.toString(find.getARRatio());
            String locationTimestamp = Double.toString(find.getCreatedTimestamp());
            String comments = find.getComments();
            String encoding = "";
            List<String> imagePaths = find.getImagePaths();
            List<String> imageNames = parseImageNames(imagePaths);
            List<String> imageBase64 = encodeImages(imagePaths);

            try
            {
                encoding = URLEncoder.encode(comments, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            makeVolleyStringObjectRequest(globalWebServerURL + "/insert_find?zone=" + zone
                            + "&hemisphere=" + hemisphere + "&easting=" + easting + "&northing=" + northing
                            + "&contextEasting=" + contextEasting + "&contextNorthing=" + contextNorthing
                            + "&find=" + sample + "&latitude=" + latitude + "&longitude=" + longitude
                            + "&altitude=" + altitude + "&status=" + status + "&material=" + material
                            + "&comments=" + encoding + "&ARratio=" + ARratio + "&timestamp=" + locationTimestamp,
                    queue, new StringObjectResponseWrapper() {
                /**
                 * Response received
                 * @param response - database response
                 */
                @Override
                public void responseMethod(String response)
                {
                    Log.v("Sync", response);
                    if (!response.contains("Error"))
                    {
                        logText.setText(logText.getText().toString() + response);
                        databaseHandler.setFindSynced(find);
                        ArrayList<String> paths = elementsToUpload.get(uploadIndex).getImagePaths();
                        String key = hemisphere + "." + zone + "." + contextEasting + "." + contextNorthing + "." + find;
                        if (imageNumbers.get(key) == null)
                        {
                            imageNumbers.put(key, 0);
                        }
                        for (String path: paths)
                        {
                            imageNumbers.put(key, imageNumbers.get(key) + 1);
                            String newDir = Environment.getExternalStorageDirectory().toString()
                                    + "/Archaeology/" + hemisphere + "/" + zone + "/" + contextEasting
                                    + "/" + contextNorthing + "/" + sample + "/photos/field/";
                            File dir = new File(newDir);
                            if (!dir.exists())
                            {
                                dir.mkdirs();
                            }
                            String newPath = newDir + "/" + imageNumbers.get(key) + ".JPG";
                            File oldImage = new File(path);
                            File newImage = new File(newPath);
                            if (!oldImage.renameTo(newImage))
                            {
                                Log.v("Moving Files", "Failed to move " + oldImage.getAbsolutePath()
                                        + " to " + newImage.getAbsolutePath());
                            }
                            Log.v("Moving Files", oldImage.getAbsolutePath() + " renamed to "
                                    + newImage.getAbsolutePath());
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Upload failed: " + response,
                                Toast.LENGTH_SHORT).show();
                    }
                    // Upload the next find
                    uploadIndex++;
                    //then send images
                    for (int i = 0; i < imageNames.size(); i++) {
                        //imageNames.get(i) and imageBase64.get(i)
                        String currentName = imageNames.get(i);
                        String currentImageBase64 = imageBase64.get(i);
                        String currentIndex = String.valueOf(i + 1);

                        //insertFindImageRequest(context, getToken(context), url , new byte[]{}, queue);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, globalWebServerURL + "/insert_find_image",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("Res++", response);
                                        logText.setText(logText.getText().toString() + response);
                                        if (!response.contains("Error")) {
                                            //count image success
                                            counter.getAndAdd(1);
                                            progressBar.setProgress( (int) ((double)counter.get()/(totalImages) * 100));
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Upload failed: " + response,
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("zone", zone);
                                params.put("hemisphere", hemisphere);
                                params.put("contextEasting", contextEasting);
                                params.put("contextNorthing", contextNorthing);
                                params.put("find", sample);
                                params.put("imageName", currentName);
                                params.put("imageBase64", currentImageBase64);
                                params.put("indexNum", currentIndex);
                                Log.d("cool", String.valueOf(currentImageBase64.length()));
                                return params;
                            }
                        }; //end of defining current POST request
                        queue.add(stringRequest);
                    }//end of for loop
                   // progressBar.setProgress( (int) ((double)(uploadIndex)/(totalItems) * 100));
                    uploadFinds();
                }

                /**
                 * Connection failed
                 * @param error - failure
                 */
                @Override
                public void errorMethod(VolleyError error)
                {

                    Toast.makeText(getApplicationContext(), "Upload failed (Communication error): " + error,
                            Toast.LENGTH_SHORT).show();

                }
            });

        }
        else
        {
            Toast.makeText(SyncActivity.this, "Done syncing finds", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Upload a path to the database
     */
    private void uploadPaths()
    {
        if (pathUploadIndex < totalPaths)
        {
            final PathElement path = pathsToUpload.get(pathUploadIndex);
            String teamMember = path.getTeamMember();
            String beginLatitude = Double.toString(path.getBeginLatitude());
            String beginLongitude = Double.toString(path.getBeginLongitude());
            String beginAltitude = Double.toString(path.getBeginAltitude());
            String beginStatus = path.getBeginStatus();
            String beginARRatio =  Double.toString(path.getBeginARRatio());
            String endLatitude =  Double.toString(path.getEndLatitude());
            String endLongitude = Double.toString(path.getEndLongitude());
            String endAltitude = Double.toString(path.getEndAltitude());
            String endStatus =  path.getEndStatus();
            String endARRatio = Double.toString(path.getEndARRatio());
            String hemisphere = path.getHemisphere();
            String zone = Integer.toString(path.getZone());
            String beginNorthing = Double.toString(path.getBeginNorthing());
            String beginEasting = Double.toString(path.getBeginEasting());
            String endNorthing = Double.toString(path.getEndNorthing());
            String endEasting = Double.toString(path.getEndEasting());
            String beginTime = Double.toString(path.getBeginTime());
            String endTime = Double.toString(path.getEndTime());
            String encoding = "";
            try
            {
                encoding = URLEncoder.encode(teamMember, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            makeVolleyStringObjectRequest(globalWebServerURL + "/insert_path?teamMember=" + encoding
                            + "&hemisphere=" + hemisphere + "&zone=" + zone + "&beginEasting=" + beginEasting
                            + "&beginNorthing=" + beginNorthing + "&endEasting=" + endEasting + "&endNorthing="
                            + endNorthing + "&beginLatitude=" + beginLatitude + "&beginLongitude=" + beginLongitude
                            + "&beginAltitude=" + beginAltitude + "&beginStatus=" + beginStatus + "&beginARRatio="
                            + beginARRatio + "&endLatitude=" + endLatitude + "&endLongitude=" + endLongitude
                            + "&endAltitude=" + endAltitude + "&endStatus=" + endStatus + "&endARRatio="
                            + endARRatio + "&beginTime=" + beginTime + "&endTime=" + endTime, queue,
                    new StringObjectResponseWrapper() {
                /**
                 * Response received
                 * @param response - database response
                 */
                @Override
                public void responseMethod(String response)
                {

                    if (!response.contains("Error"))
                    {
                        databaseHandler.setPathSynced(path);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Upload failed: " + response, Toast.LENGTH_SHORT).show();
                    }
                    pathUploadIndex++;
                    uploadPaths();
                }

                /**
                 * Connection failed
                 * @param error - failure
                 */
                @Override
                public void errorMethod(VolleyError error)
                {
                    Toast.makeText(getApplicationContext(), "Upload unsuccessful (Communication error): " + error,
                            Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
        }
        else
        {
            Toast.makeText(SyncActivity.this, "Done syncing paths", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> parseImageNames(List<String> imagePaths) {
        List<String> imageNames = new ArrayList<>();
        for (String imagePath: imagePaths) {
            Log.d("parseparse++", imagePath);
            String[] parsedResult = imagePath.split("/");
            imageNames.add(parsedResult[parsedResult.length - 1]);  //name is the last one
        }
        return imageNames;
    }

    private List<String> encodeImages(List<String> imagePaths) {
        List<String> encodedImages = new ArrayList<>();
        for (String imagePath: imagePaths) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                encodedImages.add(imageToString(bitmap));
        }
        return encodedImages;
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        byte[] imgBytes = output.toByteArray();
        return android.util.Base64.encodeToString(imgBytes, android.util.Base64.NO_WRAP);

    }

}
