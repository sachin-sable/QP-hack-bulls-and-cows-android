package com.questionpro.bullscows.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.questionpro.bullscows.app.utils.GlobalData;
import com.questionpro.bullscows.app.utils.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class GuesserScreen extends Activity{
    private LinearLayout textLayout;
    private GlobalData globalData = GlobalData.getInstance();
    private LayoutInflater layoutInflater ;
    private String selectedWord;
    private Button submitButton;
    private int currentPass= 0;
    private Map<Integer,String> passes = new LinkedHashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guesser_screen);
        layoutInflater = LayoutInflater.from(this);
        textLayout = findViewById(R.id.textInputLayout);
        submitButton = findViewById(R.id.submitButton);
        selectedWord = globalData.getCurrentWord();
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
    }

    private void submitInput(String text){
        String result = Utils.getHint(selectedWord.toLowerCase(), text.toLowerCase());

        passes.put(currentPass++,result);
        Log.i("Sachin", "Result- Pass-"+currentPass+" Result-"+result);
        clearAllEditTexts();

    }

    private void clearAllEditTexts(){
        for(int  i=0 ; i<selectedWord.length(); i++){
            ((EditText) findViewById(i)).setText("");
        }
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

}