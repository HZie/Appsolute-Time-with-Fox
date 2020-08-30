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
    ImageView wordbox;
    TextView foxword;
    TextView word;
    TextView message;
    int click = 0;

    public static Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newfox);
        mcontext = this;

        Intent intent = getIntent();
        int data = intent.getIntExtra("foxnum", R.drawable.fox);

        newfox = findViewById(R.id.newfox);
        wordbox = findViewById(R.id.wordBox);
        foxword = findViewById(R.id.foxword);
        word = findViewById(R.id.newfox_text);
        message = findViewById(R.id.endmessage);
        newfox.setBackground(ContextCompat.getDrawable(mcontext, data));

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
                else if(click == 3) {
                    word.setText(R.string.suggestion4);
                    wordbox.setVisibility(View.VISIBLE);
                    foxword.setVisibility(View.VISIBLE);
                    click++;
                }
                else {
                    finish();
                }

            }
        });
    }




}