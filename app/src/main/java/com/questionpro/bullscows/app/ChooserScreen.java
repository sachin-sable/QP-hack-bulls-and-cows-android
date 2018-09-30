package com.questionpro.bullscows.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.questionpro.bullscows.app.utils.GlobalData;

import org.json.JSONObject;

public class ChooserScreen extends Activity {
    private EditText enteredText;
    private Button submitButton;
    private ListView attemptsListView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    final DatabaseReference guesserInput = myRef.child("GuesserInput");
    final DatabaseReference chooserInputRef = myRef.child("ChooserInput");
    private ResultAdapter resultAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser_screen);
        enteredText = findViewById(R.id.enteredText);
        submitButton = findViewById(R.id.submitButton);
        attemptsListView = findViewById(R.id.attemptListView);
        resultAdapter = new ResultAdapter(this);
        attemptsListView.setAdapter(resultAdapter);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateText()){
                    try{
                        chooserInputRef.setValue(enteredText.getText().toString());
                    }catch (Exception e){}

                    GlobalData.getInstance().setCurrentWord(enteredText.getText().toString());

                }
            }
        });

        guesserInput.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                try {
                    if(!value.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(value);
                        PassAttempt passAttempt = PassAttempt.fromJSON(jsonObject);
                        resultAdapter.addPassAttempt(passAttempt);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private boolean validateText(){
        if(enteredText.getText().toString().isEmpty()){
            Toast.makeText(this,"Please enter text.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(enteredText.getText().toString().length()<3){
            Toast.makeText(this,"Minimum 3 characters please.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
