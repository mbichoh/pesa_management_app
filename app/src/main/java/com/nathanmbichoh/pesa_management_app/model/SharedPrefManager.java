package com.nathanmbichoh.pesa_management_app.model;

import android.content.Context;

public class SharedPrefManager {

    //constants
    private static final String SHARED_PREF_NAME = "pesamanagementappwithnathan";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ID = "keyid";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    //create an instance
    private SharedPrefManager(Context context){
        mCtx = context;
    }

    //get instance


    public static synchronized SharedPrefManager getInstance(Context context) {
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


}
