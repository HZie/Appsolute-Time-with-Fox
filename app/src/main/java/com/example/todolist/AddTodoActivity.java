package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.todolist.databinding.ActivityAddTodoBinding;
import com.example.todolist.databinding.ActivityMainBinding;

public class AddTodoActivity extends Activity{
    ActivityAddTodoBinding binding;
    ImageButton closeBtn;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityAddTodoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        getWindow().getAttributes().width = width;


        closeBtn = binding.closeBtn;
        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveBtn = binding.saveBtn;
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO: realm에 저장하는 코드 작성

                finish();
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}