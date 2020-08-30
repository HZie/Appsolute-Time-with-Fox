package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.todolist.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    FloatingActionButton btnAdd;
    Button btnDDay;
    Switch switchShowDDay;
    Button btnOasis;
    Button btnToOasis2;
    Button btnToOasis1;
    ConstraintLayout layoutBackground;
    TextView tvDate;

    DDayFragment d_dayFrag;
    ToDoFragment todoFrag;
    OasisFragment oasisFrag;
    OasisWinterFragment oasiswinterFrag;

    FragmentManager fm;
    FragmentTransaction ftran;

    boolean isDDay = false;
    boolean isOasis = false;
    int currList = 0;
    float pointX; float pointY; float oasisNum;

    private long backKeyPressed = 0;

    // Use Realm DB
    //Realm realm;

    // Gamify 관련 변수
    public static Context mcontext;
    SharedPreferences Log;
    SharedPreferences.Editor editor;
    SimpleDateFormat mFormat;
    ProgressBar prgbar;
    TextView percentV, levelView;
    boolean isFoxGet = false;
    boolean isFinished = false;

    int imageResources[] = {
            R.drawable.fox_swim, R.drawable.fox, R.drawable.fox_sleep, R.drawable.fox_pilot, R.drawable.fox_angry,
            R.drawable.fox_baby,  R.drawable.fox_lifeguard, R.drawable.fox_red, R.drawable.fox_guitar, R.drawable.fox_owl,
            R.drawable.fox_flower, R.drawable.fox_cold, R.drawable.fox_spotato, R.drawable.fox_white, R.drawable.fox_xmas
    };

    int[] stringResources = { R.string.cheerup, R.string.hi, R.string.howdud, R.string.plywthm };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mcontext = this;

        // Realm DB 사용을 위한 초기화
        //Realm.init(this);
        //Realm realm = Realm.getDefaultInstance();

        layoutBackground = binding.layoutBackground;

        // TODO: 사막 background로 설정
        layoutBackground.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.dday_background));


        btnAdd = binding.btnAdd;
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 할 일 추가 화면
                Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                startActivityForResult(intent,1);
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
                        btnToOasis2.setVisibility(View.GONE);
                        btnToOasis1.setVisibility(View.GONE);
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
                        btnToOasis2.setVisibility(View.GONE);
                        btnToOasis1.setVisibility(View.GONE);
                        layoutBackground.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.dday_background));
                    }
                    isDDay = false;
                }


            }
        });

        tvDate = binding.tvDate;
        tvDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime()));

        btnOasis = binding.btnOasis;
        btnToOasis2 = binding.btnToOasis2;
        btnToOasis1 = binding.btnToOasis1;
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
                    btnToOasis2.setVisibility(View.VISIBLE);
                    tvDate.setTextColor(getResources().getColor(R.color.whiteText));
                    setFragment(2);
                }
                else{
                    // TODO: 사막 background로 전환
                    btnOasis.setText("Oasis");
                    btnOasis.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.airplane_oasis_go));
                    isOasis = false;
                    layoutBackground.setBackground(ContextCompat.getDrawable(mcontext, R.drawable.dday_background));
                    btnToOasis2.setVisibility(View.GONE);
                    btnToOasis1.setVisibility(View.GONE);
                    tvDate.setTextColor(getResources().getColor(R.color.blackText));
                    setFragment(currList);
                }

            }
        });

        btnToOasis2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isOasis) {
                    setFragment(3);
                    btnToOasis2.setVisibility(View.GONE);
                    btnToOasis1.setVisibility(View.VISIBLE);
                }
            }
        });

        btnToOasis1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isOasis) {
                    setFragment(2);
                    btnToOasis1.setVisibility(View.GONE);
                    btnToOasis2.setVisibility(View.VISIBLE);
                }

            }
        });

        d_dayFrag = new DDayFragment();
        todoFrag = new ToDoFragment();
        oasisFrag = new OasisFragment();
        oasiswinterFrag = new OasisWinterFragment();

        setFragment(0);
        currList = 0;

        // gamify 관련 코드
        //TODO: 할일 하나 완료시마다 메인화면 속 여우의 대사(TextView hi)를 -->@string/udidit으로 바꾸고 말풍선 보여주기
        final ImageView wordbox = binding.wordBox;
        final TextView hi = binding.hi;
        final ImageButton fox = binding.fox;
        //여우대사
        fox.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordbox.setVisibility(View.VISIBLE);
                hi.setText(stringResources[new Random().nextInt(4)]);
                hi.setVisibility(View.VISIBLE);

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        wordbox.setVisibility(View.INVISIBLE);
                        hi.setVisibility(View.INVISIBLE);
                    }
                }, 2000);

            }
        });
        //앱 킬때 점검하는부분
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();
        int currentFox = Log.getInt("Fox", imageResources[1]);
        fox.setBackground(ContextCompat.getDrawable(mcontext, currentFox));
        levelUp();
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
            case 3:
                ftran.replace(R.id.listFragment, oasiswinterFrag);
                break;
            default:
                break;
        }
        ftran.commit();
    }

    // TODO: 여기부터 gamify 관련 코드 작성
    public String getdate() {
        mFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date nowDate = new Date(System.currentTimeMillis());
        String date = mFormat.format(nowDate);
        return date;
    }

    //현재 날짜 기준 다음주 월요일
    public String getMonday() {
        mFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cur = Calendar.getInstance();
        cur.setFirstDayOfWeek(Calendar.MONDAY);
        cur.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cur.add(Calendar.DATE, 7);
        String date = mFormat.format(cur.getTime());
        return date;
    }

    public void percentView(int percent) {
        prgbar = binding.loveprg;
        percentV = binding.percent;
        percentV.setText(String.valueOf(percent));
        prgbar.setProgress(percent, true);
        if(percent>=70) {
            prgbar.getProgressTintMode();
        }
    }

    public void levelView(int level) {
        levelView = binding.levelNum;
        levelView.setText(String.valueOf(level));
    }

    public void levelupView(int level) {
        levelView(level);
        final ImageView wordbox = binding.wordBox;
        final TextView hi = binding.hi;
        ImageButton fox = binding.fox;

        wordbox.setVisibility(View.VISIBLE);
        hi.setText(R.string.thx);
        hi.setVisibility(View.VISIBLE);
    }

    public int[] getLog() {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        int[] arr = new int[2];
        arr[0] = Log.getInt("Percent", 0);
        arr[1] = Log.getInt("Level", 0);
        return arr;
    }

    public String getDateLog() {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        return Log.getString("Date", "01-01-1999");
    }

    //TODO: 1.매번 할일완료시 2.할일제거시 3.할일추가시 --> finish할일 퍼센트 구해서 로그에 저장
    //TODO: 밑 함수에 itemnum=총할일수, finishtask=끝낸 할일수만 넣어주면 됨
    public void setPercent(int itemnum, int finishTask) {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();
        int percent = (int)((float) finishTask/itemnum*100);
        editor.putInt("Percent", percent);
        editor.putString("Date", getdate());
        editor.commit();
        percentView(percent);
    }

    public void getNewFox(ImageButton fox, boolean isFirst, int curFox) {
        int randomImage;
        if(isFirst == true) {
            randomImage = imageResources[1];
        } else {
            do{
                randomImage = imageResources[new Random().nextInt(imageResources.length)];
            }while(randomImage==curFox);
        }

        Intent intent = new Intent(this, NewfoxActivity.class);
        intent.putExtra("foxnum", randomImage);
        if(isFirst == true) intent.putExtra("isFirst", true);
        startActivity(intent);

        editor.putInt("Fox", randomImage);
        editor.commit();
        fox.setBackground(ContextCompat.getDrawable(mcontext, randomImage));
    }

    public void leaveFox(int lastFox) {
        Intent intent = new Intent(this, GetfoxActivity.class);
        intent.putExtra("foxnum", lastFox);
        intent.putExtra("isGet", isFoxGet);
        startActivity(intent);
    }

    public Boolean[] getFoxLog() {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        Boolean[] arr = new Boolean[imageResources.length];
        arr[0] = Log.getBoolean("FSwim", false); arr[1] = Log.getBoolean("FNormal", false);
        arr[2] = Log.getBoolean("FSleep", false); arr[3] = Log.getBoolean("FPilot", false);
        arr[4] = Log.getBoolean("FAngry", false); arr[5] = Log.getBoolean("FBaby", false);
        arr[6] = Log.getBoolean("FLife", false); arr[7] = Log.getBoolean("FRed", false);
        arr[8] = Log.getBoolean("FGuitar", false); arr[9] = Log.getBoolean("FOwl", false);
        arr[10] = Log.getBoolean("FFlower", false); arr[11] = Log.getBoolean("FCold", false);
        arr[12] = Log.getBoolean("FPotato", false); arr[13] = Log.getBoolean("FWhite", false);
        arr[14] = Log.getBoolean("FXmas", false);
        return arr;
    }

    public void setFoxLog(int i) {
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();
        if(i==0) editor.putBoolean("FSwim", true); if(i==1) editor.putBoolean("FNormal", true);
        if(i==2) editor.putBoolean("FSleep", true); if(i==3) editor.putBoolean("FPilot", true);
        if(i==4) editor.putBoolean("FAngry", true); if(i==5) editor.putBoolean("FBaby", true);
        if(i==6) editor.putBoolean("FLife", true); if(i==7) editor.putBoolean("FRed", true);
        if(i==8) editor.putBoolean("FGuitar", true); if(i==9) editor.putBoolean("FOwl", true);
        if(i==10) editor.putBoolean("FFlower", true); if(i==11) editor.putBoolean("FCold", true);
        if(i==12) editor.putBoolean("FPotato", true); if(i==13) editor.putBoolean("FWhite", true);
        if(i==14) editor.putBoolean("FXmas", true);
        editor.commit();
    }

    public void levelUp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int[] foxLog = getLog();
        String curDate = getdate();
        String logDate = getDateLog();
        Date date1 = null;
        Date date2 = null;

        try {
            date1 = sdf.parse(logDate);
            date2 = sdf.parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar log = Calendar.getInstance();
        Calendar cur = Calendar.getInstance();
        log.setTime(date1); cur.setTime(date2);

        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();
        ImageButton fox = binding.fox;

        if(logDate == "01-01-1999") { //날짜 로그 비어있을시 기본값임
            Toast.makeText(this, "첫방문", Toast.LENGTH_SHORT).show();
            getNewFox(fox, true, Log.getInt("Fox", 0));
            editor.putInt("Fox", imageResources[1]);

            editor.putString("Date", getdate());
            editor.putString("FoxDate", getMonday());
            editor.commit();

            levelView(Log.getInt("Level", 0));
            int percent = Log.getInt("Percent", 0);
            percentView(percent);
        }
        else if(log.before(cur)) {
            if(foxLog[0]>=70) {
                if (foxLog[1] < 7) {
                    int levelDiff = foxLog[1]++;
                    editor.putInt("Level", levelDiff);
                    levelupView(levelDiff);
                } else Toast.makeText(this, "이미 길들이기 LV.7달성!", Toast.LENGTH_LONG).show();
                levelView(foxLog[1]);
            }
            else if(foxLog[0]<70) {
                Toast.makeText(this, "70%달성 실패", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "저번 달성률: "+ foxLog[0], Toast.LENGTH_SHORT).show();
                levelView(foxLog[1]);
            }
            editor.putInt("Finish", 0); editor.putString("Date", getdate()); editor.putInt("Percent", 0);
            editor.commit();
            percentView(Log.getInt("Percent", 0));
            isNextWeek(); //다음주가 됐거나 지났는지(새 여우 지급용 함수)
        }
        else {
            Toast.makeText(this, "오늘방문", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "마지막로그: "+ logDate, Toast.LENGTH_LONG).show();
            levelView(foxLog[1]);
            int percent = Log.getInt("Percent", 0);
            percentView(percent);
        }
    }

    //현재 키우고있는 여우를 받았던 주가 지났는지 여부
    public void isNextWeek(){
        ImageButton foxImage = binding.fox;
        Log = getSharedPreferences("Log", MODE_PRIVATE);
        editor = Log.edit();

        Calendar cur = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String logDate = Log.getString("FoxDate", getMonday()); //여우 받은 주의 다음주 월요일
        Date date = null;
        try {
            date = sdf.parse(logDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if(cal.before(cur)) { //만약 여우 받은 주가 이미 지났다면 (여우 받은 주의 다음주 월요일 이후 날짜라면)
            if(Log.getInt("Level", 0)>=7) { //레벨 7
                Toast.makeText(this, "여우와 약속 지키기 성공!", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "여우와 친구가 됐어요!", Toast.LENGTH_LONG).show();
                for(int i = 0;  i<imageResources.length; i++) {
                    if(imageResources[i]==Log.getInt("Fox", imageResources[1])) setFoxLog(i);
                }
                isFoxGet = true;
            }
            else isFoxGet = false; //레벨 7 아닌경우
            editor.putString("FoxDate", getMonday()); editor.commit();
            int lastFox = Log.getInt("Fox", imageResources[1]);
            getNewFox(foxImage, false, Log.getInt("Fox", imageResources[1]));
            leaveFox(lastFox);
        }

    }

}