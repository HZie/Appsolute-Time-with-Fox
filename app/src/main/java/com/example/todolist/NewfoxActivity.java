package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

//새로운 여우 받는 화면!

public class NewfoxActivity extends AppCompatActivity {
    ImageView newfox;
    TextView word;
    TextView message;
    int click = 0;

    int imageResources[] = {
            R.drawable.fox_swim, R.drawable.fox, R.drawable.fox_sleep, R.drawable.fox_pilot, R.drawable.fox_angry,
            R.drawable.fox_baby,  R.drawable.fox_lifeguard, R.drawable.fox_red, R.drawable.fox_guitar, R.drawable.fox_owl,
            R.drawable.fox_flower, R.drawable.fox_cold, R.drawable.fox_spotato, R.drawable.fox_white, R.drawable.fox_xmas
    };

    public static Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newfox);
        mcontext = this;

        Intent intent = getIntent();
        int data = intent.getIntExtra("foxnum", 1);

        newfox = findViewById(R.id.newfox);
        word = findViewById(R.id.newfox_text);
        message = findViewById(R.id.endmessage);
        newfox.setBackground(ContextCompat.getDrawable(mcontext, imageResources[data]));

        ConstraintLayout popup = (ConstraintLayout) findViewById(R.id.popup);

        popup.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click == 0) {
                    word.setText(R.string.suggestion);
                    click++;
                }
                else if(click == 1) {
                    word.setText(R.string.suggestion2);
                    click++;
                }
                else if(click == 2) {
                    word.setText(R.string.suggestion3);
                    click++;
                }
                else {
                    finish();
                }

            }
        });
    }




}