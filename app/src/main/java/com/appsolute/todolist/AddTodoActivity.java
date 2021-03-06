package com.appsolute.todolist;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.todolist.R;
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
    Button saveBtn, editBtn;

    EditText contentET;
    ToggleButton importantBtn, repeatBtn, dueBtn;
    CheckBox isEveryday;
    ToggleButton[]weekTBtn = new ToggleButton[7];
    DatePicker dueDP;
    TextView dpTextView;

    LinearLayout repeatLayout;
    LinearLayout ddayLayout;

    private String todoIDhead="";
    private String todoID;
    private String todoDate;
    private String todoContent;
    private boolean todoIsImportant;
    private boolean todoIsDDay;
    private Date todoDueDate;
    private boolean todoIsRepeat;
    private String todoRepeatDate = "";
    String editId="";

    Realm realm;
    int mode;
    int currFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityAddTodoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",0);
        currFrag = intent.getIntExtra("fragment",-1);


        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        getWindow().getAttributes().width = width;


        // 바인딩
        closeBtn = binding.closeBtn;
        saveBtn = binding.saveBtn;
        editBtn = binding.editBtn;

        contentET = binding.addTodoET;
        importantBtn = binding.importantBtn;
        repeatBtn = binding.repeatBtn;
        dueBtn = binding.dDayBtn;
        dpTextView = binding.dpTextView;

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
        editBtn.setOnClickListener(BtnOnClickListener);

        // 모드 버튼 (중요, 반복, 디데이)
        importantBtn.setOnClickListener(BtnOnClickListener);
        repeatBtn.setOnClickListener(BtnOnClickListener);
        dueBtn.setOnClickListener(BtnOnClickListener);

        setWeekListener();

        dueDP.setOnDateChangedListener(dpListener);
        dueDP.updateDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dpTextView.setText(new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(Calendar.getInstance().getTime()));
        //TODO: 오늘까지인 디데이 추가시에 스피너 조작을 안한채로 그대로 추가하게 되는데(스피너 처음값이 오늘이니까)
        //TODO: 이렇게 하면 날짜가 null로 뜨길래 처음에 오늘값을 todoDueDate에 초기값으로 줌
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        todoDueDate = cal.getTime();
        todoIDhead = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().getTime());


        switch(mode){
            case 1:
                editBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);
                break;
            case 2:
                editId = intent.getStringExtra("itemID");
                editBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.GONE);
                setEditScreen(editId);
                break;
            default:
                break;
        }
    }

    public View.OnClickListener BtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.editBtn:
                    // ToDo: edit button 관련 메소드
                    if(editToDoItem(editId)){
                        ((MainActivity)MainActivity.mcontext).setFragment(currFrag);
                        finish();
                    }
                    break;
                case R.id.saveBtn:
                    if(addToDoItem()){
                        ((MainActivity)MainActivity.mcontext).setFragment(currFrag);
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
            //TODO: 바꾼 부분
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd",Locale.KOREAN);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            dpTextView.setText(new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN).format(cal.getTime()));
            todoDueDate = cal.getTime();

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
        Date todoDate = Calendar.getInstance().getTime();
        Log.d("addToDoItem - todoDate: ", String.valueOf(todoDate));
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

    public boolean editToDoItem(String id){
        ToDoItem todo = getItem(id);
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

        updateItem(id);
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

    public ToDoItem getItem(String id){
        ToDoItem item = null;
        try{
            realm = Realm.getDefaultInstance();
            item = realm.where(ToDoItem.class)
                                        .equalTo("id",id)
                                        .findFirst();
            realm.close();
        }
        catch(Exception e){
            Log.e("error at getItem(AddToDoActivity): ",String.valueOf(e));
        }
        return item;
    }

    public void updateItem(String id){
        try{
            realm = Realm.getDefaultInstance();
            ToDoItem item = realm.where(ToDoItem.class)
                    .equalTo("id",id)
                    .findFirst();
            realm.beginTransaction();

            Log.d("id, content : ",id+" "+contentET.getText().toString());
            item.setContent(contentET.getText().toString());
            item.setImportant(importantBtn.isChecked());
            item.setDDay(dueBtn.isChecked());
            item.setRepeat(repeatBtn.isChecked());
            if(todoIsRepeat)
                item.setRepeatDate(todoRepeatDate);
            if(todoIsDDay)
                item.setDueDate(todoDueDate);

            realm.commitTransaction();
            realm.close();
        }
        catch(Exception e){
            Log.e("error at changeChecked in AddTodoActivity:", String.valueOf(e));
        }
    }

    public void setEditScreen(String id){
        try{
            realm = Realm.getDefaultInstance();
            ToDoItem item = realm.where(ToDoItem.class)
                    .equalTo("id",id)
                    .findFirst();
            realm.close();

            contentET.setText(item.getContent());
            importantBtn.setChecked(item.isImportant());
            isToggleChecked(importantBtn);

            repeatBtn.setChecked(item.isRepeat());
            isToggleChecked(repeatBtn);
            if(repeatBtn.isChecked())
                repeatLayout.setVisibility(View.VISIBLE);
            else
                repeatLayout.setVisibility(View.GONE);

            dueBtn.setChecked(item.isDDay());
            isToggleChecked(dueBtn);
            if(dueBtn.isChecked())
                ddayLayout.setVisibility(View.VISIBLE);
            else
                ddayLayout.setVisibility(View.GONE);

            String repeatDate = item.getRepeatDate();

            if(repeatDate.equals("1111111"))
                isEveryday.setChecked(true);
            else
                isEveryday.setChecked(false);

            for(int i = 0; i < repeatDate.length(); i++){
                if(repeatDate.charAt(i) == '1')
                    weekTBtn[i].setChecked(true);
                else
                    weekTBtn[i].setChecked(false);
                isToggleChecked(weekTBtn[i]);
            }



        }
        catch(Exception e){
            Log.e("error at setEditScreen in AddTodoActivity: ",String.valueOf(e));
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