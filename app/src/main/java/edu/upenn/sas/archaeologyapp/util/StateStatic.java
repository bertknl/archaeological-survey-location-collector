// Static stuff
// @author: msenol
package edu.upenn.sas.archaeologyapp.util;

import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothAdapter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class StateStatic
{
    // This class holds global state variables. This class should be only used in a static way
    public static final String LOG_TAG = "Ceramic App";
    public static final String LOG_TAG_WIFI_DIRECT = "WIFIDIRECT";
    public static final String LOG_TAG_BLUETOOTH = "BLUETOOTH";
    public static final int GOOGLE_PLAY_SIGN_IN = 301;
    public static final int REQUEST_CODE_CREATE_FILE = 302;
    public static final int MESSAGE_WEIGHT = 501;
    public static final int MESSAGE_STATUS_CHANGE = 502;
    public static final int REQUEST_ENABLE_BT = 503;
    public static final String DEFAULT_WEB_SERVER_URL = "https://object-data-collector-service.herokuapp.com";
    // Default URL to connect to database to send photos back and forth
    public static final String DEFAULT_BUCKET_URL = "s3.console.aws.amazon.com/s3/buckets/pennmuseum/";
    public static final String DEFAULT_CAMERA_MAC = "fe:c2:de:31:0a:e1";
    // 30 minutes
    public static final long DEFAULT_CALIBRATION_INTERVAL = 1800000;
    private static final String DEFAULT_PHOTO_PATH = "Archaeology";
    public static final String THUMBNAIL_EXTENSION_STRING = "thumb.jpg";
    public static final int DEFAULT_VOLLEY_TIMEOUT = 7000;
    public static final String SYNCED = "S";
    public static final String MARKED_AS_ADDED = "A";
    public static final String MARKED_AS_TO_DOWNLOAD = "D";
    // fields in the database
    public static final String EASTING = "easting";
    public static final String NORTHING = "northing";
    public static final String FIND_NUMBER = "find_number";
    public static final String ALL_FIND_NUMBER = "all_available_find_number";
    public static String deviceName = "nutriscale_1910";
    public static String globalWebServerURL = DEFAULT_WEB_SERVER_URL;
    public static String globalBucketURL = DEFAULT_BUCKET_URL;
    public static String cameraMACAddress = DEFAULT_CAMERA_MAC;
    public static long remoteCameraCalibrationInterval = DEFAULT_CALIBRATION_INTERVAL;
    public static long tabletCameraCalibrationInterval = DEFAULT_CALIBRATION_INTERVAL;
    public static final boolean DEFAULT_REMOTE_CAMERA_SELECTED = false;
    public static boolean isRemoteCameraSelected = DEFAULT_REMOTE_CAMERA_SELECTED;
    public static String cameraIPAddress = null;
    public static final boolean DEFAULT_CORRECTION_SELECTION = true;
    public static boolean colorCorrectionEnabled = DEFAULT_CORRECTION_SELECTION;

    /**
     * Get current date
     * @return Returns timestamp
     */
    public static String getTimeStamp()
    {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
    }

    /**
     * Creating a thumbnail for requested image
     * @param inputFileName - image file
     * @return Returns image URI
     */
    public static Uri getThumbnail(String inputFileName)
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Archaeology");
        String originalFilePath = mediaStorageDir.getPath() + File.separator + inputFileName;
        String thumbPath = mediaStorageDir.getPath() + File.separator + THUMBNAIL_EXTENSION_STRING;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(originalFilePath, options);
        int width = options.outWidth;
        int height = options.outHeight;
        File thumbFile = new File(thumbPath);
        // creating a thumbnail image and setting the bounds of the thumbnail
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory
                        .decodeFile(originalFilePath), Math.round(width / 4.1f),
                Math.round(height / 4.1f));
        try
        {
            FileOutputStream fos = new FileOutputStream(thumbFile);
            thumbImage.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        }
        catch (Exception ex)
        {
            // e.getMessage is returning a null value
            ex.printStackTrace();
        }
        return Uri.fromFile(thumbFile);
    }
}