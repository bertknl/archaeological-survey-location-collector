package edu.upenn.sas.archaeologyapp.util;
/**
 * Class containing any constants and helpers required throughout the app
 * Created by eanvith on 24/12/16.
 */
public class Constants
{
    // Time for which the splash activity must pause before proceeding to the main activity
    public static int SPLASH_TIME_OUT = 2000;
    public static final String PARAM_KEY_ID = "entry_id", PARAM_KEY_ZONE = "zone", PARAM_KEY_HEMISPHERE = "hemisphere";
    public static final String PARAM_KEY_NORTHING = "northing", PARAM_KEY_EASTING = "easting", PARAM_KEY_SAMPLE = "sample";
    public static final String PARAM_KEY_LATITUDE = "latitude", PARAM_KEY_PRECISE_EASTING = "precise easting";
    public static final String PARAM_KEY_LONGITUDE = "longitude", PARAM_KEY_PRECISE_NORTHING = "precise northing";
    public static final String PARAM_KEY_ALTITUDE = "altitude", PARAM_KEY_STATUS = "status", PARAM_KEY_AR_RATIO = "AR_ratio";
    public static final String PARAM_KEY_MATERIAL = "material", PARAM_KEY_CONTEXT_NUMBER = "context_number", PARAM_KEY_IMAGES = "images", PARAM_KEY_COMMENTS = "comments";

    public static final String PARAM_FIND_UUID = "find_uuid";

    public static final String PARAM_FIND_DELETED = "find_deleted";

    public static final String PARAM_KEY_TEAM_MEMBER = "team_member", PARAM_KEY_BEGIN_LATITUDE = "begin_latitude";
    public static final String PARAM_KEY_END_LATITUDE = "end_latitude", PARAM_KEY_BEGIN_LONGITUDE = "begin_longitude";
    public static final String PARAM_KEY_END_LONGITUDE = "end_longitude", PARAM_KEY_BEGIN_ALTITUDE = "begin_altitude";
    public static final String PARAM_KEY_END_ALTITUDE = "end_altitude", PARAM_KEY_BEGIN_EASTING = "begin_easting";
    public static final String PARAM_KEY_BEGIN_NORTHING = "begin_northing", PARAM_KEY_END_EASTING = "end_easting";
    public static final String PARAM_KEY_END_NORTHING = "end_northing", PARAM_KEY_BEGIN_TIME = "begin_time";
    public static final String PARAM_KEY_END_TIME = "end_time", PARAM_KEY_BEGIN_STATUS = "begin_status";
    public static final String PARAM_KEY_END_STATUS = "end_status", PARAM_KEY_BEGIN_AR_RATIO = "begin_AR_ratio";
    public static final String PARAM_KEY_END_AR_RATIO = "end_AR_ratio", DEFAULT_REACH_HOST = "192.168.43.162";
    public static final int DEFAULT_POSITION_UPDATE_INTERVAL = 2, DEFAULT_VOLLEY_TIMEOUT = 15000;

    public static final String DEFAULT_REACH_PORT = "9001";
    public static final String DEFAULT_SERVER_URL = "https://j20200007.kotsf.com/asl/";
    public static final String LOGIN_SERVER_URL =  DEFAULT_SERVER_URL + "auth-token/";
    public static final String TOKEN_ACCESS_TESTING_URL = DEFAULT_SERVER_URL + "api/area/";

    public static final String TEAM_MEMBERS_URL = DEFAULT_SERVER_URL + "api/team/";

    public static final String MATERIALS_URL = DEFAULT_SERVER_URL + "api/find/mc/";

    public static final String INSERT_PATH_URL = DEFAULT_SERVER_URL + "api/path/";

    public static final String INSERT_FIND_URL = DEFAULT_SERVER_URL + "api/find/";

    public static final String INSERT_FIND_IMAGE_URL = DEFAULT_SERVER_URL + "api/find/<uuid>/photo/";

    public static final String CONTEXT_URL =  DEFAULT_SERVER_URL + "api/context/<hemisphere>/<zone>/<easting>/<northing>/";
    private static final String DEFAULT_WEB_SERVER_URL = "http://40.117.209.97";
    public static final String PREFERENCES = "archaeological-survey-location-collector-preferences";
    public static String globalWebServerURL = DEFAULT_WEB_SERVER_URL;
}


