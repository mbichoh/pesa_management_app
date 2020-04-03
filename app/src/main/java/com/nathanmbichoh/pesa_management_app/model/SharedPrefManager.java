package com.nathanmbichoh.pesa_management_app.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nathanmbichoh.pesa_management_app.Login;

public class SharedPrefManager {

    //constants
    private static final String SHARED_PREF_NAME = "pesamanagementappwithnathan";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ID = "keyid";
    private static final String KEY_CODE = "keycode";

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

    //let user login and hold data in the sharedPrefManager
    public void userLogin(User user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_CODE, user.getCode());
        editor.apply();
    }

    //check if user is logged IN
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences  = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(KEY_NAME, null) != null;
    }

    //get logged in user
    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_CODE, null)
        );
    }

    //logout user
    public void logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, Login.class));
    }
}
