package com.questionpro.bullscows.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.questionpro.bullscows.app.utils.SharedPrefManager;
import com.questionpro.bullscows.app.utils.Utils;

import org.json.JSONObject;

/**
 * Created by Datta Kunde on 28/09/18.
 */

public class SplashScreenActivity extends Activity{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    final DatabaseReference user = myRef.child("User");

    private String userType="CHOOSER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        RadioButton chooser=findViewById(R.id.chooser_radio_button);
        RadioButton guesser=findViewById(R.id.guesser_radio_button);
        guesser.setEnabled(false);
        chooser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userType="CHOOSER";
                }
            }
        });

        guesser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userType= "GUESSER";
                }
            }
        });
        Button submit=findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserType();
            }
        });


        this.user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                try {
                    Log.d("Datta","Chooser feedback:"+value);
                    if(!value.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(value);
                        if (!jsonObject.optString("DEVICE_ID").equals(Utils.getDeviceId(SplashScreenActivity.this))) {
                            SharedPrefManager.getInstance(SplashScreenActivity.this).saveGuestDeviceId(jsonObject.optString("DeviceId"));
                        }

                        if (jsonObject.optString("DEVICE_ID").equals(Utils.getDeviceId(SplashScreenActivity.this))
                                && jsonObject.optString("ROLE").equals("CHOOSER")) {
                            Log.i("Sachin", "Going to Chooser screen");
                            Intent intent = new Intent(SplashScreenActivity.this, ChooserScreen.class);
                            intent.putExtra("json", value);
                            startActivity(intent);
                        } else {
                            Log.i("Sachin", "Going to Guesser screen");
                            Intent intent = new Intent(SplashScreenActivity.this, GuesserScreen.class);
                            intent.putExtra("json", value);
                            startActivity(intent);
                        }
                    }
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("file", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void sendUserType(){
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("DEVICE_ID", Utils.getDeviceId(this));
            jsonObject.put("ROLE", userType);
            user.setValue(jsonObject.toString());
            SharedPrefManager.getInstance(this).saveHostUserType(userType);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        user.setValue("");
        myRef.child("ChooserInput").setValue("");
        myRef.child("GuesserInput").setValue("");
    }
}
