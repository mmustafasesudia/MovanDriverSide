package com.example.admin.movanuser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;


public class ConfigURL {

    public static final String URL_LOGIN_PERSON = "http://itehadmotors.com/Move_Van/v1/login/driver";
    public static final String URL_REGISTER_PERSON = "http://itehadmotors.com/Move_Van/v1/driver/register";
    public static final String URL_LIST_OF_CUSTOMERS = "http://itehadmotors.com/Move_Van/v1/listofcustomer";
    public static final String URL_CREATE_JOURNEY = "http://itehadmotors.com/Move_Van/v1/create/journy";

    public static final String URL_PICKUP = "http://itehadmotors.com/Move_Van/v1/picked/customer";
    public static final String URL_DROP_OFF = "http://itehadmotors.com/Move_Van/v1/complete/customer";
    public static final String URL_COMPLETE_JOURNEY = "http://itehadmotors.com/Move_Van/v1/complete/journy";
    public static final String URL_DRIVER_CURRENT_JOURNEY = "http://itehadmotors.com/Move_Van/v1/driver/currentjourny";
    public static final String URL_UPDATE_CURRENT_LOCATION = "http://itehadmotors.com/Move_Van/v1/current/location";

    public static final String URL_FORGOT_PASS = "http://itehadmotors.com/Move_Van/v1/forgotpass";
    public static final String URL_IS_PERSON_EXIST = "http://itehadmotors.com/Move_Van/v1/isPersonExistCheck";
    public static final String PUSH_NOTIFICATION = "unique_name";
    public static String PICK_UP_LAT = "";
    public static String PICK_UP_LNG = "";
    public static String DROP_OF_LAT = "";
    public static String DROP_OF_LNG = "";
    public static Handler handler = new Handler();
    public static Runnable runnable;

    public static String getLoginState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("LOGIN", "").length() > 0) {
            return prefs.getString("LOGIN", "");
        } else
            return "";
    }


    public static String getName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("Name", "").length() > 0) {
            return prefs.getString("Name", "");
        } else
            return "";
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("Email", "").length() > 0) {
            return prefs.getString("Email", "");
        } else
            return "";
    }

    public static String getMobileNo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("MobileNo", "").length() > 0) {
            return prefs.getString("MobileNo", "");
        } else
            return "";
    }

    public static String isRideIsActive(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("RIDE", "").length() > 0) {
            return prefs.getString("RIDE", "");
        } else
            return "";
    }

    public static String getVanNo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("Van_Number", "").length() > 0) {
            return prefs.getString("Van_Number", "");
        } else
            return "";
    }

    public static void clearshareprefrence(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


}
