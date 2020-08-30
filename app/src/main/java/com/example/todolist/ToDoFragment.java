package com.example.todolist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.todolist.databinding.ActivityMainBinding;
import com.example.todolist.databinding.FragmentTodoBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

// TODO: To Do List관련 코드 여기에 작성

public class ToDoFragment extends Fragment {
    Button insertButton;
    EditText todoEdit;
    private ArrayList<ToDoItem> todoArrayList;
    private ToDoRecyclerAdapter todoAdapter;
    Date curr;
    String date;

    FragmentTodoBinding binding;
    Realm realm;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTodoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = binding.todoRecyclerView;
        todoArrayList = new ArrayList<>();
        LinearLayoutManager todoLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(todoLayoutManager);
        todoAdapter = new ToDoRecyclerAdapter(todoArrayList);   //어뎁터 안에 array list 넣기
        recyclerView.setAdapter(todoAdapter);                   // 어뎁터 셋팅
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromDB();
    }

    public void getDataFromDB(){
       String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
       if(todoArrayList != null)
           todoArrayList.clear();
       try{
           realm = Realm.getDefaultInstance();
           final RealmResults<ToDoItem> items = realm.where(ToDoItem.class).contains("date", date).findAll();
           for(int i = 0; i < items.size(); i++){
               todoArrayList.add(items.get(i));
               Log.d("getFromDB: ",items.get(i).getContent());
           }
       }
       catch(Exception e){
           Log.e("error at getDataFromDB: ",String.valueOf(e));
       }
    }



}