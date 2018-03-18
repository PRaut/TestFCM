package com.prat.testfcm;

/**
 * Created by user on 19/03/2017.
 */
public class Constants {
    //Firebase app url
    public static final String FIREBASE_APP = "https://simplifiedcoding.firebaseio.com/";

    //Constant to store shared preferences
    public static final String SHARED_PREF = "secure_auth_app_pref";

    //To store boolean in shared preferences for if the device is registered to not
    public static final String REGISTERED = "false";

    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";

    //register.php address in your server
    public static final String REGISTER_URL = "http://192.168.94.1/firebasepushnotification/register.php";

    // Variable used for storing the key in the Android Keystore container
    public static final String KEY_NAME = "testFcm_userKey";
    //-----------------------------------------------------------------------

    public static final String KEY_DEVICE_REG = "Device_Reg";
    public static final String KEY_REG_MOBNO = "Reg_mobno";
    public static final String KEY_ICCID = "iccid";
}
