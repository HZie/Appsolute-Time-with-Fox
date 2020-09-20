package com.appsolute.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.todolist.R;

public class GetfoxActivity extends AppCompatActivity {

    ImageView endfox;
    ImageView wordbox;
    TextView foxword;
    TextView narrator;
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
        setContentView(R.layout.activity_getfox);
        mcontext = this;

        final Intent intent = getIntent();
        int data = intent.getIntExtra("foxnum", 1);
        final boolean get = intent.getBooleanExtra("isGet", false);

        final Intent intentM = new Intent(this, MainActivity.class);

        endfox = findViewById(R.id.endfox);
        wordbox = findViewById(R.id.wordBox);
        foxword = findViewById(R.id.foxword);
        narrator = findViewById(R.id.endfox_text);
        message = findViewById(R.id.endmessage);
        endfox.setBackground(ContextCompat.getDrawable(mcontext, imageResources[data]));

        ConstraintLayout popup = (ConstraintLayout) findViewById(R.id.popup);
        if(get == false) {
            foxword.setText(R.string.fail1);
        }

        popup.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(get == true) {
                    if(click == 0) {
                        foxword.setText(R.string.get2);
                        click++;
                    }
                    else if(click == 1) {
                        foxword.setText(R.string.get3);
                        click++;
                    }
                    else {
                        narrator.setText(R.string.getmessage);
                        message.setVisibility(View.INVISIBLE);
                        foxword.setVisibility(View.INVISIBLE); wordbox.setVisibility(View.INVISIBLE);
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                ((MainActivity)MainActivity.mcontext).isFinished = true;
                                finish();
                            }
                        }, 3000);
                    }
                }
                else {
                    if(click == 0) {
                        foxword.setText(R.string.fail2);
                        click++;
                    }
                    else if(click == 1) {
                        foxword.setText(R.string.fail3);
                        click++;
                    }
                    else {
                        narrator.setText(R.string.failmessage);
                        message.setVisibility(View.INVISIBLE);
                        foxword.setVisibility(View.INVISIBLE); wordbox.setVisibility(View.INVISIBLE);
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                finish();
                            }
                        }, 3000);
                    }
                }

            }
        });
    }
}
