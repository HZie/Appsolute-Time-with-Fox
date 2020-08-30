package com.example.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.todolist.databinding.ActivityAddTodoBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;


public class AddTodoActivity extends Activity{
    ActivityAddTodoBinding binding;
    ImageButton closeBtn;
    Button saveBtn;

    EditText contentET;
    ToggleButton importantBtn, repeatBtn, dueBtn;
    CheckBox isEveryday;
    ToggleButton[]weekTBtn = new ToggleButton[7];
    DatePicker dueDP;

    ToggleButton weekSingle;

    LinearLayout repeatLayout;
    LinearLayout ddayLayout;

    private String todoIDhead;
    private String todoID;
    private String todoDate;
    private String todoContent;
    private boolean todoIsImportant;
    private boolean todoIsDDay;
    private String todoDueDate;
    private boolean todoIsRepeat;
    private String todoRepeatDate = "";

    Realm realm;


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


        // 바인딩
        closeBtn = binding.closeBtn;
        saveBtn = binding.saveBtn;

        contentET = binding.addTodoET;
        importantBtn = binding.importantBtn;
        repeatBtn = binding.repeatBtn;
        dueBtn = binding.dDayBtn;

        isEveryday = binding.isEveryday;
        weekTBtn[0] = binding.monBtn;
        weekTBtn[1] = binding.tueBtn;
        weekTBtn[2] = binding.wedBtn;
        weekTBtn[3] = binding.thuBtn;
        weekTBtn[4] = binding.friBtn;
        weekTBtn[5] = binding.satBtn;
        weekTBtn[6] = binding.sunBtn;


        dueDP = binding.dueDP;

        repeatLayout = binding.repeatLayout;
        ddayLayout = binding.ddayLayout;

        repeatLayout.setVisibility(View.GONE);
        ddayLayout.setVisibility(View.GONE);

        // 리스너 정의
        closeBtn.setOnClickListener(BtnOnClickListener);
        saveBtn.setOnClickListener(BtnOnClickListener);

        // 모드 버튼 (중요, 반복, 디데이)
        importantBtn.setOnClickListener(BtnOnClickListener);
        repeatBtn.setOnClickListener(BtnOnClickListener);
        dueBtn.setOnClickListener(BtnOnClickListener);

        setWeekListener();

        dueDP.setOnDateChangedListener(dpListener);
        todoIDhead = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public View.OnClickListener BtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.saveBtn:
                    if(addToDoItem()){
                        ToDoFragment tdf = new ToDoFragment();
                        tdf.onResume();
                        finish();
                    }
                    break;
                case R.id.closeBtn:
                    finish();
                    break;
                case R.id.importantBtn:
                    isToggleChecked(importantBtn);
                    break;
                case R.id.repeatBtn:
                    isToggleChecked(repeatBtn);
                    if(repeatBtn.isChecked())
                        repeatLayout.setVisibility(View.VISIBLE);
                    else
                        repeatLayout.setVisibility(View.GONE);
                    break;
                case R.id.dDayBtn:
                    isToggleChecked(dueBtn);
                    if(dueBtn.isChecked())
                        ddayLayout.setVisibility(View.VISIBLE);
                    else
                        ddayLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    DatePicker.OnDateChangedListener dpListener = new DatePicker.OnDateChangedListener(){
        @Override
        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
            String strMonth=(month+1)+"", strDay=day+"";
            if(month+1 < 10){
                strMonth = "0"+(month+1);
            }
            if(day < 10){
                strDay = "0"+day;
            }
            todoDueDate = year+"-"+strMonth+"-"+strDay;
        }
    };

    public void setWeekListener(){
        View.OnClickListener weekTBListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.monBtn:
                        isToggleChecked(weekTBtn[0]);
                        break;
                    case R.id.tueBtn:
                        isToggleChecked(weekTBtn[1]);
                        break;
                    case R.id.wedBtn:
                        isToggleChecked(weekTBtn[2]);
                        break;
                    case R.id.thuBtn:
                        isToggleChecked(weekTBtn[3]);
                        break;
                    case R.id.friBtn:
                        isToggleChecked(weekTBtn[4]);
                        break;
                    case R.id.satBtn:
                        isToggleChecked(weekTBtn[5]);
                        break;
                    case R.id.sunBtn:
                        isToggleChecked(weekTBtn[6]);
                        break;
                }
            }
        };

        for(int i = 0; i < weekTBtn.length; i++)
            weekTBtn[i].setOnClickListener(weekTBListener);

        isEveryday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                for(int i = 0 ; i < weekTBtn.length; i++){
                    if(isEveryday.isChecked()){
                        weekTBtn[i].setChecked(isEveryday.isChecked());
                        weekTBtn[i].setBackground(getResources().getDrawable(R.drawable.circle_btn_checked));
                        weekTBtn[i].setTextColor(getResources().getColor(R.color.whiteText));
                    }
                    else{
                        weekTBtn[i].setChecked(isEveryday.isChecked());
                        weekTBtn[i].setBackground(getResources().getDrawable(R.drawable.circle_btn_unchecked));
                        weekTBtn[i].setTextColor(getResources().getColor(R.color.blackText));
                    }
                }
            }
        });
    }

    public void isToggleChecked(ToggleButton tbtn){
        if(tbtn.isChecked()){
            tbtn.setBackground(getResources().getDrawable(R.drawable.circle_btn_checked));
            tbtn.setTextColor(getResources().getColor(R.color.whiteText));
        }
        else{
            tbtn.setBackground(getResources().getDrawable(R.drawable.circle_btn_unchecked));
            tbtn.setTextColor(getResources().getColor(R.color.blackText));
        }
    }

    public boolean addToDoItem(){
        ToDoItem todo;
        todoDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        todoContent = contentET.getText().toString();

        if(todoContent.compareTo("") == 0){
            Toast.makeText(this, "할 일을 적어주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        todoIsImportant = importantBtn.isChecked();
        todoIsDDay = dueBtn.isChecked();
        todoIsRepeat = repeatBtn.isChecked();
        for(int i = 0; i < weekTBtn.length; i++){
            // true: 1, false: 0
            if(weekTBtn[i].isChecked())
                todoRepeatDate += "1";
            else
                todoRepeatDate += "0";
        }
        getLastID();
        todo = new ToDoItem(todoID, todoDate,todoContent, todoIsImportant,todoIsRepeat,todoIsDDay);
        if(todoIsRepeat)
            todo.setRepeatDate(todoRepeatDate);
        if(todoIsDDay)
            todo.setDueDate(todoDueDate);

        DBTransaction(todo);
        return true;
    }

    public void DBTransaction(ToDoItem todo){
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(todo);
        realm.commitTransaction();
        realm.close();
    }

    public void getLastID(){
        try{
            realm = Realm.getDefaultInstance();
            final RealmResults<ToDoItem> items = realm.where(ToDoItem.class).contains("id", todoIDhead).findAll();
            todoID = todoIDhead + items.size();
        }
        catch(Exception e){
            Log.e("error at getLastID: ",String.valueOf(e));
        }
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