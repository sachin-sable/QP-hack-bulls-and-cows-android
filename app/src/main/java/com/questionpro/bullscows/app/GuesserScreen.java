package com.questionpro.bullscows.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.questionpro.bullscows.app.utils.GlobalData;
import com.questionpro.bullscows.app.utils.Utils;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class GuesserScreen extends Activity{
    private LinearLayout textLayout;
    private GlobalData globalData = GlobalData.getInstance();
    private LayoutInflater layoutInflater ;
    private String selectedWord;
    private Button submitButton;
    private int currentPass= 0;
    private ListView attemptsListView;
    private Map<Integer,String> passes = new LinkedHashMap<>();
    private ResultAdapter resultAdapter;
    private TextView textView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    final DatabaseReference guesserInput = myRef.child("GuesserInput");
    final DatabaseReference chooserInputRef = myRef.child("ChooserInput");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guesser_screen);
        layoutInflater = LayoutInflater.from(this);
        textLayout = findViewById(R.id.textInputLayout);
        submitButton = findViewById(R.id.submitButton);
        attemptsListView = findViewById(R.id.attemptListView);
        selectedWord = globalData.getCurrentWord();
        resultAdapter = new ResultAdapter(this);
        textView = findViewById(R.id.textView);
        attemptsListView.setAdapter(resultAdapter);
        submitButton.setVisibility(View.GONE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    StringBuffer stringBuffer = new StringBuffer();
                    for(int  i=0 ; i<selectedWord.length(); i++) {
                        stringBuffer.append(((EditText) findViewById(i)).getText().toString().toLowerCase());
                    }
                    submitInput(stringBuffer.toString());
                }
            }
        });

        chooserInputRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                selectedWord = dataSnapshot.getValue(String.class);
                if(!selectedWord.isEmpty()) {
                    textView.setVisibility(View.GONE);
                    addInputBoxes(selectedWord);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addInputBoxes(final String selectedWord) {
        textLayout.removeAllViews();
        for(int i=0; i<selectedWord.length(); i++){
            final int index = i;
            EditText editText =(EditText) layoutInflater.inflate(R.layout.edit_text_box, null);
            editText.setId(i);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() == 0 && index > 0){
                        ((EditText) findViewById(index - 1)).requestFocus();
                    }else if(s.length()== 1 && index<selectedWord.length()-1){
                        ((EditText) findViewById(index+ 1)).requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 80);
            params.rightMargin= 5;
            params.leftMargin  = 5;
            params.weight = 1;
            editText.setLayoutParams(params);
            textLayout.addView(editText);
        }
        ((EditText)textLayout.getChildAt(0)).requestFocus();
        submitButton.setVisibility(View.VISIBLE);
    }

    private void submitInput(String text){
        PassAttempt result = Utils.getHint(selectedWord.toLowerCase(), text.toLowerCase());
        result.index = currentPass++;

        JSONObject jsonObject = PassAttempt.toJSON(result);
        guesserInput.setValue(jsonObject.toString());
        //passes.put(currentPass++,result);
        Log.i("Sachin", "Result- Pass-"+currentPass+" Result-"+result);
        resultAdapter.addPassAttempt(result);

        if(result.bullsCount == text.length()) {
            finishGame(currentPass);
        }else
            clearAllEditTexts();

    }

    public void finishGame(final int attempts){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Congratulations!!!");
        alertDialogBuilder.setMessage("You Guess correct word in "+attempts+" Attempts.");
                alertDialogBuilder.setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                resultAdapter.clearData();
                                myRef.child("User").setValue("");
                                myRef.child("ChooserInput").setValue("");
                                myRef.child("GuesserInput").setValue("");
                                GuesserScreen.this.finish();
                            }
                        });

        /*alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void clearAllEditTexts(){
        for(int  i=0 ; i<selectedWord.length(); i++){
            ((EditText) findViewById(i)).setText("");
        }
        ((EditText) findViewById(0)).requestFocus();
    }
    private boolean validateInput(){
        for(int  i=0 ; i<selectedWord.length(); i++){
            if(((EditText) findViewById(i)).getText().toString().length() == 0){
                Toast.makeText(this,"Please enter complete text.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitGame();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exitGame(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Alert!!!");
        alertDialogBuilder.setMessage("Do you want to leave game?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Exit","True");
                            guesserInput.setValue(jsonObject.toString());
                        }catch (Exception e){e.printStackTrace();}
                        GuesserScreen.this.finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
