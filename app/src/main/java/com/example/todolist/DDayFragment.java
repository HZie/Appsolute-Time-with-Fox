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

import com.example.todolist.databinding.FragmentDDayBinding;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

// TODO: D-Day 관련 코드 여기서 작성

public class DDayFragment extends Fragment {


    private ArrayList<ToDoItem> dDayItems;
    private recyclerAdapter dDayAdapter;

    FragmentDDayBinding binding;
    Realm realm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDDayBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = binding.dDayRecyclerView;
        dDayItems = new ArrayList<>();
        LinearLayoutManager todoLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(todoLayoutManager);
        dDayAdapter = new recyclerAdapter(dDayItems, getContext(), 2);   //어뎁터 안에 array list 넣기
        recyclerView.setAdapter(dDayAdapter);                   // 어뎁터 셋팅

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                if(swipeDir == ItemTouchHelper.LEFT)
                    dDayAdapter.showMenu(position);
                else
                    dDayAdapter.closeMenu();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                dDayAdapter.closeMenu();
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

    public void getDataFromDB(){
        Log.d("getDataFromDB at DDayFragment:","called");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        final Date todayStart = cal.getTime();
        if(dDayItems != null)
            dDayItems.clear();
        else
            dDayItems = new ArrayList<ToDoItem>();
        try{
            realm = Realm.getDefaultInstance();
            final RealmResults<ToDoItem> items = realm.where(ToDoItem.class)
                    .equalTo("isDDay",true)
                    .greaterThanOrEqualTo("dueDate", todayStart)
                    .findAllSorted("dueDate");

            for(int i = 0; i < items.size(); i++){
                dDayItems.add(items.get(i));
            }
            dDayAdapter.notifyDataSetChanged();
        }
        catch(Exception e){
            Log.e("error at getDataFromDB: ",String.valueOf(e));
        }
    }


}