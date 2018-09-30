package com.questionpro.bullscows.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
    private TextView labelText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser_screen);
        enteredText = findViewById(R.id.enteredText);
        submitButton = findViewById(R.id.submitButton);
        attemptsListView = findViewById(R.id.attemptListView);
        labelText = findViewById(R.id.textView);
        resultAdapter = new ResultAdapter(this);
        attemptsListView.setAdapter(resultAdapter);
        if(resultAdapter.getCount()>0)
            enteredText.setEnabled(false);

            submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateText()){
                    confirmWord();

                }
            }
        });

        guesserInput.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                try {
                    if(!value.isEmpty()) {
                        labelText.setText("Here is the progress of the other player.");
                        JSONObject jsonObject = new JSONObject(value);
                        PassAttempt passAttempt = PassAttempt.fromJSON(jsonObject);
                        resultAdapter.addPassAttempt(passAttempt);
                        if(passAttempt.bullsCount == GlobalData.getInstance().getCurrentWord().length()){
                            Toast.makeText(ChooserScreen.this.getApplicationContext(), "The other person has guessed the word \""
                                    +GlobalData.getInstance().getCurrentWord()+"\"correctly in "+resultAdapter.getCount()+" attempts.",Toast.LENGTH_LONG).show();
                            myRef.child("User").setValue("");
                            myRef.child("ChooserInput").setValue("");
                            myRef.child("GuesserInput").setValue("");
                            ChooserScreen.this.finish();
                        }

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

    public void confirmWord(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm?");
        alertDialogBuilder.setMessage("Do you confirm this \""+enteredText.getText().toString()+"\" word? ");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try{
                            chooserInputRef.setValue(enteredText.getText().toString());
                        }catch (Exception e){}

                        GlobalData.getInstance().setCurrentWord(enteredText.getText().toString());
                        enteredText.setEnabled(false);
                        labelText.setText("Your word is set. Waiting for the other player to guess.");
                        submitButton.setVisibility(View.GONE);
                        enteredText.setVisibility(View.GONE);
                    }
                });

        alertDialogBuilder.setNegativeButton("No- Want To Change",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        enteredText.requestFocus();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
