package com.questionpro.bullscows.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Datta on 10/8/16.
 */
public class SharedPrefManager {
    private static SharedPreferences sharedpreferences;
    private static SharedPrefManager mInstance;
    public static final String MY_PREF="SMS";

    public static SharedPrefManager getInstance(Context mContext){
        if(mInstance == null) {
            mInstance = new SharedPrefManager(mContext);
        }
        return mInstance;
    }

    private SharedPrefManager(Context context){
        sharedpreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
    }

    public void clearSharesPreference()
    {
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void saveGuestDeviceId(String deviceId){
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putString("DeviceId",deviceId);
        editor.commit();
    }
    public String getGuestDeviceId(){
        return sharedpreferences.getString("DeviceId","");
    }


    public void saveHostUserType(String userType){
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putString("HostUserType",userType);
        editor.commit();
    }
    public String getHostUserType(){
        return sharedpreferences.getString("HostUserType","");
    }


}
