package com.example.todolist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.databinding.FragmentTodoBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;


public class ToDoFragment extends Fragment {

    private ArrayList<ToDoItem> todoArrayList;
    private recyclerAdapter todoAdapter;
    RecyclerView recyclerView;

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
        deletePrevData();
        recyclerView = binding.todoRecyclerView;
        todoArrayList = new ArrayList<>();
        LinearLayoutManager todoLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(todoLayoutManager);
        todoAdapter = new recyclerAdapter(todoArrayList, getContext(), 1);   //어뎁터 안에 array list 넣기
        recyclerView.setAdapter(todoAdapter);                   // 어뎁터 셋팅

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                if(swipeDir == ItemTouchHelper.LEFT)
                    todoAdapter.showMenu(position);
                else
                    todoAdapter.closeMenu();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                todoAdapter.closeMenu();
                return true;
            }




        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromDB();
    }

    public boolean deletePrevData(){
        realm = Realm.getDefaultInstance();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        final Date todayStart = cal.getTime();
        System.out.println(todayStart);
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                try {
                    RealmResults<ToDoItem> dItem = realm.where(ToDoItem.class)
                            .lessThan("date", todayStart)
                            .equalTo("isRepeat", false)
                            .equalTo("isDDay",false)
                            .findAll();
                    for (int i = 0; i < dItem.size(); i++) {
                        if (dItem.isValid()) {
                            dItem.get(i).deleteFromRealm();
                        }
                    }
                    System.out.println("deletePrevData");

                    RealmResults<ToDoItem> dItems = realm.where(ToDoItem.class)
                            .lessThan("dueDate", todayStart)
                            .equalTo("isDDay",true)
                            .findAll();

                    for (int i = 0; i < dItem.size(); i++) {
                        if (dItem.isValid()) {
                            System.out.println("dItem: "+dItem.get(i).getContent());
                            dItem.get(i).deleteFromRealm();
                        }
                    }
                    notifyAll();

                }
                catch(Exception e){
                }

            }
        });
        realm.close();

        return true;

    }

    public void getDataFromDB(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        final Date todayStart = cal.getTime();
        int checkedNum = 0;
           if(todoArrayList != null)
               todoArrayList.clear();
           try{
               realm = Realm.getDefaultInstance();
               final RealmResults<ToDoItem> items = realm.where(ToDoItem.class)
                                                            .greaterThanOrEqualTo("date", todayStart)
                                                            .equalTo("isDDay",false)
                                                            .equalTo("isRepeat",false)
                                                            .findAll();

               for(int i = 0; i < items.size(); i++){
                   todoArrayList.add(items.get(i));

                   if(items.get(i).isChecked()){
                       checkedNum++;
                   }
               }


               String queryVal = "";
               String todayWeek = new SimpleDateFormat("EEE",Locale.ENGLISH).format(Calendar.getInstance().getTime());
               int weekNum = getWeekNum(todayWeek);
               for(int i = 0; i < 7; i++){
                   if(weekNum == i)
                       queryVal += "1";
                   else
                       queryVal +="?";
               }

               final RealmResults<ToDoItem> repeatItem = realm.where(ToDoItem.class)
                       .equalTo("isRepeat",true)
                       .like("repeatDate",queryVal)
                       .findAll();
               for(int i = 0; i < repeatItem.size(); i++){
                   todoArrayList.add(repeatItem.get(i));

                   if(repeatItem.get(i).isChecked()){
                       checkedNum++;
                   }
               }
               todoAdapter.setCheckedNum(checkedNum);
               todoAdapter.notifyDataSetChanged();
           }
           catch(Exception e){
               Log.e("error at getDataFromDB: ",String.valueOf(e));
           }
    }

    public int getWeekNum(String tWeek){
        int weekNum = -1;
        switch(tWeek){
            case "Mon":
                weekNum = 0;
                break;
            case "Tue":
                weekNum = 1;
                break;
            case "Wed":
                weekNum = 2;
                break;
            case "Thu":
                weekNum = 3;
                break;
            case "Fri":
                weekNum = 4;
                break;
            case "Sat":
                weekNum = 5;
                break;
            case "Sun":
                weekNum = 6;
                break;
            default:
                break;
        }

        return weekNum;
    }





}