package com.bom.sangue.sanguebom.Utils;

/**
 * Created by alan on 29/11/15.
 */
public class Constants {

    // Use this for ADB
//    public static final String HOST = "10.0.2.2";

    // Use this for Genymotion
    public static final String HOST = "10.0.3.2";
    public static final String PORT = "8000";
    public static final String URL_SIGNUP = "http://" + HOST+":"+PORT+"/engine/users/";
    public static final String URL_SIGN = "http://"+HOST+":"+PORT+"/api-auth-token/";
    public static final String URL_LAST_DONATION = "http://" + HOST+":"+PORT+"/engine/users/{id}/lastdonation";
}
