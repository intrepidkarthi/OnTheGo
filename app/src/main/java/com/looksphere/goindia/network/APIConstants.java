package com.looksphere.goindia.network;

/**
 * Created by SPARK on 08/06/15.
 */
public class APIConstants {

    //Endpoint url
    public static final String BASE_URL = "http://52.10.123.111:8080";


    //API paths
    public static final String SIGNIN_URL = "/signin";
    public static final String SIGNUP_URL = "/api/signup";
    public static final String OTP_GENERATION_URL = "/api/generateotp";
    public static final String OTP_VERIFICATION_URL = "/api/verifyotp";
    public static final String FETCH_EVENTS_URL = "/api/ui/events";
    public static final String FETCH_EVENTS_BY_USER_URL = "/api/ui/{apikey}/events";
    public static final String FETCH_USER_INFO_URL = "/api/student/{apikey}";
    public static final String JOIN_EVENT_URL = "/api/protected/joinevent";


}
