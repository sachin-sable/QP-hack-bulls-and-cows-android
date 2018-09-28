package com.questionpro.bullscows.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.questionpro.bullscows.app.utils.GlobalData;

public class ChooserScreen extends Activity {
    private EditText enteredText;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser_screen);
        enteredText = findViewById(R.id.enteredText);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateText()){
                    GlobalData.getInstance().setCurrentWord(enteredText.getText().toString());
                    startActivity(new Intent(ChooserScreen.this,GuesserScreen.class));
                    finish();
                }
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
