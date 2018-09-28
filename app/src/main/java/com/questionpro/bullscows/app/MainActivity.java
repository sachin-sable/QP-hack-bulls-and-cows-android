package com.questionpro.bullscows.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.questionpro.bullscows.app.utils.Utils;

public class MainActivity extends Activity {
     private Button startButton;
     @Override
     public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         startButton = (Button) findViewById(R.id.startButton);
         startButton.setText("START");
         startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Sachin", "Clicker");
                startActivity(new Intent(MainActivity.this, ChooserScreen.class));
            }
        });
    }
}
