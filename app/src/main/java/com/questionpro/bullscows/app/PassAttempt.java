package com.questionpro.bullscows.app;

import org.json.JSONException;
import org.json.JSONObject;

public class PassAttempt {
    public int index;
    public int bullsCount;
    public int cowsCount;

    public static PassAttempt fromJSON(JSONObject jsonObject){
        try {
            PassAttempt passAttempt = new PassAttempt();
            passAttempt.index = jsonObject.getInt("index");
            passAttempt.bullsCount = jsonObject.getInt("bullsCount");
            passAttempt.cowsCount = jsonObject.getInt("cowsCount");
            return passAttempt;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject toJSON(PassAttempt passAttempt){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("index", passAttempt.index);
            jsonObject.put("bullsCount", passAttempt.bullsCount);
            jsonObject.put("cowsCount", passAttempt.cowsCount);
            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
