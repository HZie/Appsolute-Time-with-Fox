package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todolist.databinding.ActivityAddTodoBinding;
import com.example.todolist.databinding.ActivityHelpBinding;

public class HelpActivity extends Activity {

    ActivityHelpBinding binding;
    ImageButton closeBtn;
    TextView helpTxt;
    String helpStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
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

        setHelpText();
        helpTxt = binding.helpText;
        helpTxt.setText(helpStr);
        helpTxt.setMovementMethod(new ScrollingMovementMethod());
    }


    // 여기에 도움말 텍스트 작성
    void setHelpText(){
        helpStr = "여기에 도움말 텍스트가 갑니다";
    }
}