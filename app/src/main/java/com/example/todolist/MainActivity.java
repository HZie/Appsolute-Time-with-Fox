package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.realm.Realm;

// TODO: gamify 관련해서 필요한 코드 작성

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    FloatingActionButton btnAdd;
    Button btnDDay;
    Switch switchShowDDay;
    Button btnOasis;
    ConstraintLayout layoutBackground;

    DDayFragment d_dayFrag;
    ToDoFragment todoFrag;
    OasisFragment oasisFrag;

    FragmentManager fm;
    FragmentTransaction ftran;

    boolean isDDay = false;
    boolean isOasis = false;
    int currList = 0;
    float pointX; float pointY; float oasisNum;

    private long backKeyPressed = 0;

    // Use Realm DB
    Realm realm;

    // Gamify 관련 변수
    public static Context mcontext;
    SharedPreferences Log;
    SharedPreferences.Editor editor;
    SimpleDateFormat mFormat;
    ProgressBar prgbar;
    TextView percentV, levelView;

    int imageResources[] = {
            R.drawable.fox_swim, R.drawable.fox, R.drawable.fox_sleep, R.drawable.fox_red, R.drawable.fox_pilot,
            R.drawable.fox_angry,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mcontext = this;

        // Realm DB 사용을 위한 초기화
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        layoutBackground = binding.layoutBackground;

        // TODO: 사막 background로 설정
        layoutBackground.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.dday_background));


        btnAdd = binding.btnAdd;
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO: 할 일 추가 화면 여기서 띄우기
            }
        });

        btnDDay = binding.btnDDay;
        btnDDay.setText("To D-Day List");
        isDDay = false;
        btnDDay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isDDay){
                    setFragment(1);
                    currList = 1;
                    btnDDay.setText("To to do list");
                    if(isOasis) {
                        btnOasis.setText("Oasis");
                        btnOasis.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.airplane_oasis_go));
                        isOasis = false;
                        layoutBackground.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.dday_background));
                    }
                    isDDay = true;
                }
                else{
                    setFragment(0);
                    currList = 0;
                    btnDDay.setText("To D-Day List");
                    if(isOasis) {
                        btnOasis.setText("Oasis");
                        btnOasis.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.airplane_oasis_go));
                        isOasis = false;
                        layoutBackground.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.dday_background));
                    }
                    isDDay = false;
                }
            }
        });

        btnOasis = binding.btnOasis;
        btnOasis.setText("Oasis");
        isOasis = false;

        btnOasis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isOasis){
                    // TODO: 오아시스 background로 전환
                    btnOasis.setText("Go Back");
                    btnOasis.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.airplane_oasis_back));
                    isOasis = true;
                    layoutBackground.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.oasis_background));
                    oasisNum = (float)1332/(float)986;

                    setFragment(2);
                }
                else{
                    // TODO: 사막 background로 전환
                    btnOasis.setText("Oasis");
                    btnOasis.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.airplane_oasis_go));
                    isOasis = false;
                    layoutBackground.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.dday_background));
                    setFragment(currList);
                }

            }
        });

        d_dayFrag = new DDayFragment();
        todoFrag = new ToDoFragment();
        oasisFrag = new OasisFragment();

        setFragment(0);
        currList = 0;

        // gamify 관련 코드
        final ImageView wordbox = binding.wordBox;
        final TextView hi = binding.hi;
        ImageButton fox = binding.fox;

        fox.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordbox.setVisibility(View.VISIBLE);
                hi.setVisibility(View.VISIBLE);

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        wordbox.setVisibility(View.INVISIBLE);
                        hi.setVisibility(View.INVISIBLE);
                    }
                }, 3000);

            }
        });
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        int currentFox = Log.getInt("Fox", imageResources[1]);
        fox.setBackground(ContextCompat.getDrawable(mcontext, currentFox));
        levelUp();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        View view = findViewById(R.id.listFragment);
        pointX = view.getWidth();
        pointY = view.getHeight();
        Toast.makeText(getApplicationContext(),String.valueOf(pointX) + " " + String.valueOf(pointY),Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressed + 2000){
            backKeyPressed = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"back 버튼을 한 번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(System.currentTimeMillis() <= backKeyPressed + 2000){
            finish();
        }

    }

    // 0: to do list, 1: d-day list, 2: oasis
    public void setFragment(int n){
        fm = getSupportFragmentManager();
        ftran = fm.beginTransaction();

        switch(n){
            case 0:
                ftran.replace(R.id.listFragment, todoFrag);
                break;
            case 1:
                ftran.replace(R.id.listFragment, d_dayFrag);
                break;
            case 2:
                ftran.replace(R.id.listFragment, oasisFrag);
                break;
            default:
                break;
        }
        ftran.commit();
    }

    // TODO: 여기부터 gamify 관련 코드 작성
    public String getdate() {
        mFormat = new SimpleDateFormat("yyyyMMdd");
        Date nowDate = new Date(System.currentTimeMillis());
        String date = mFormat.format(nowDate);
        return date;
    }
    public void levelView(int level) {
        levelView = binding.levelNum;
        levelView.setText(String.valueOf(level));
    }
    public void percentView(String percentS, int percentN) {
        prgbar = binding.loveprg;
        percentV = binding.percent;
        percentV.setText(percentS);
        prgbar.setProgress(percentN, true);
    }
    public void levelupView(int level) {
        levelView(level);
        final ImageView wordbox = binding.wordBox;
        final TextView hi = binding.hi;
        ImageButton fox = binding.fox;

        wordbox.setVisibility(View.VISIBLE);
        hi.setText("축하해!");
        hi.setVisibility(View.VISIBLE);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                hi.setText("안녕!"); //5초후 다시 안녕으로 바꾸고 말풍선 안보이게
                wordbox.setVisibility(View.INVISIBLE);
                hi.setVisibility(View.INVISIBLE);
            }
        }, 5000);
    }

    public int[] getLog() {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        int[] arr = new int[4];
        arr[0] = Log.getInt("Date", Integer.parseInt(getdate()));
        arr[1] = Log.getInt("Percent", 0);
        arr[2] = Log.getInt("Finish", 0);
        arr[3] = Log.getInt("Level", 0);
        return arr;
    }

    public void change_percent(int itemnum) {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();

        int finishTask = Log.getInt("Finish", 0);
        int percent = (int)((float) finishTask/itemnum*100);
        editor.putInt("Percent", percent);
        editor.putInt("Date", Integer.parseInt(getdate()));
        editor.commit();
        percentView(String.valueOf(percent), percent);
    }

    public void taskDone(int itemnum) {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();

        int finishTask = Log.getInt("Finish", 0);
        finishTask++;
        editor.putInt("Finish", finishTask);
        editor.commit();
    }

    public void newfox(ImageButton fox) {
        int randomImage = imageResources[new Random().nextInt(imageResources.length)];
        fox.setBackground(ContextCompat.getDrawable(mcontext, randomImage));
        editor.putInt("Fox", randomImage);
        editor.commit();
    }

    public Boolean[] getFoxLog() {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        Boolean[] arr = new Boolean[imageResources.length];
        arr[0] = Log.getBoolean("FSwim", false);
        arr[1] = Log.getBoolean("FNormal", false);
        arr[2] = Log.getBoolean("FSleep", false);
        arr[3] = Log.getBoolean("FRed", false);
        arr[4] = Log.getBoolean("FPilot", false);
        arr[5] = Log.getBoolean("FAngry", false);
        return arr;
    }

    public void setFoxLog(int i) {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();
        if(i==0) editor.putBoolean("FSwim", true);
        if(i==1) editor.putBoolean("FNormal", true);
        if(i==2) editor.putBoolean("FSleep", true);
        if(i==3) editor.putBoolean("FRed", true);
        if(i==4) editor.putBoolean("FPilot", true);
        if(i==5) editor.putBoolean("FAngry", true);
        editor.commit();
    }

    public void levelUp() {
        int[] arr = getLog();
        String dateLog = String.valueOf(arr[0]);
        String date = getdate(); int dateN = Integer.parseInt(date);
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();

        mFormat = new SimpleDateFormat("EE", Locale.KOREAN);
        Date nowDate = new Date(System.currentTimeMillis());
        String weekday = mFormat.format(nowDate);
        ImageButton fox = binding.fox;

        if((dateN >= arr[0] + 1)||(Integer.parseInt(date.substring(4,5)) > Integer.parseInt(dateLog.substring(4,5)))) {
            if(arr[1]>=70) {
                if (arr[3] < 7) {
                    int levelDiff = arr[3]++;
                    editor.putInt("Level", levelDiff);
                    levelupView(levelDiff);
                } else Toast.makeText(this, "이미 길들이기 LV.7달성!", Toast.LENGTH_LONG).show();
                levelView(arr[3]);
            }
            else if(arr[1]<70) {
                Toast.makeText(this, "70%달성 실패", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "어제 달성률: "+ arr[1], Toast.LENGTH_SHORT).show();
                levelView(arr[3]);
            }
            editor.putInt("Finish", 0); editor.putInt("Date", dateN); editor.putInt("Percent", 0);
            editor.commit();
            percentView(String.valueOf(Log.getInt("Percent", 0)), Log.getInt("Percent", 0));
            if(weekday.equals("월")) {
                if(Log.getInt("Level", 0)>=7) {
                    Toast.makeText(this, "여우와 약속 지키기 성공!", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "여우와 친구가 됐어요!", Toast.LENGTH_LONG).show();
                    for(int i = 0;  i<imageResources.length; i++) {
                        if(imageResources[i]==Log.getInt("Fox", imageResources[1])) setFoxLog(i);
                    }
                }
                Toast.makeText(this, "새로운 여우와 친구되기", Toast.LENGTH_LONG).show();
                newfox(fox);
            }

        }
        else {
            Toast.makeText(this, "오늘방문", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "마지막로그: "+arr[0], Toast.LENGTH_LONG).show();
            levelView(arr[3]);
            int percentN = Log.getInt("Percent", 0);
            String percentS = String.valueOf(percentN);
            percentView(percentS, percentN);
        }
    }

}