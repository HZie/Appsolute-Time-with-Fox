package com.appsolute.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder> {
    private ArrayList<ToDoItem> mData = null;     // Todo라는 객체를 가진 ArrayList
    Realm realm;
    int checkedNum = 0;
    private Context mContext;
    int showMenuPos = -1;
    int mode = 1;   // mode 1: to do fragment, mode 2: d-day fragment

    // item View를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout itemLayout;
        CheckBox todoItemCheckBox;
        TextView ddayItemText;
        ImageView importantBt;
        ImageView repeatBtn;

        LinearLayout menuLayout;
        ImageButton editBtn;
        ImageButton deleteBtn;

        public ViewHolder(final View itemView){   // itemView와 연결했기 때문에 findViewById 앞에 itemView를 명시한다.
            super(itemView);

            todoItemCheckBox = itemView.findViewById(R.id.todoCheckbox);
            ddayItemText = itemView.findViewById(R.id.ddayTextView);
            importantBt = itemView.findViewById(R.id.importantBtn);
            itemLayout = itemView.findViewById(R.id.main_item_layout);
            menuLayout = itemView.findViewById(R.id.item_menu_layout);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            repeatBtn = itemView.findViewById(R.id.repeatBtn);



            if(mode == 1){
                todoItemCheckBox.setVisibility(View.VISIBLE);
                ddayItemText.setVisibility(View.GONE);
                // 체크박스 리스너
                todoItemCheckBox.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(todoItemCheckBox.getVisibility() == View.VISIBLE){
                            int position = getAdapterPosition();    // 현재 어뎁터가 다루고 있는 리스트의 포지션을 가져온다.
                            if(position != RecyclerView.NO_POSITION){   // 삭제된 포지션이 아닌 경우
                                Log.d("todoItemCheckBox? ", todoItemCheckBox.isChecked()+"");
                                changeChecked(mData.get(position).getId(),todoItemCheckBox.isChecked());
                                notifyDataSetChanged();     // 어뎁터에게 데이터 셋이 변경되었음을 알린다.
                            }
                        }

                    }
                });
          }
            if(mode == 2){
                ddayItemText.setVisibility(View.VISIBLE);
                todoItemCheckBox.setVisibility(View.GONE);

            }



            itemLayout.setOnScrollChangeListener(new View.OnScrollChangeListener(){
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    closeMenu();
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ((MainActivity)mContext).goAddTodoActivity(2, mData.get(position).getId());
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mData.get(position).isChecked())
                            checkedNum--;
                        setPercentage();
                        deleteDB(mData.get(position).getId());
                        mData.remove(position);
                        notifyDataSetChanged();
                        closeMenu();
                    }
                }
            });
        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    recyclerAdapter(ArrayList<ToDoItem> list, Context mContext, int mode){
        mData = list;
        this.mContext = mContext;
        this.mode = mode;
    }
    recyclerAdapter(){}

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public recyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflater.inflate(R.layout.item_recycler, parent, false);
        recyclerAdapter.ViewHolder vh = new recyclerAdapter.ViewHolder(view);
        realm.getDefaultInstance();

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(recyclerAdapter.ViewHolder holder, int position){


        try{
            if (mode == 1){
                holder.todoItemCheckBox.setText(mData.get(position).getContent());       // 직접적으로 binding 해주는 것
                holder.todoItemCheckBox.setChecked(mData.get(position).isChecked());
            }
            else if(mode == 2){
                holder.ddayItemText.setText(new SimpleDateFormat("MM/dd").format(mData.get(position).getDueDate())+": "+mData.get(position).getContent());
            }

            if(position == showMenuPos){
                holder.itemLayout.setVisibility(View.GONE);
                holder.menuLayout.setVisibility(View.VISIBLE);
            }
            else{
                holder.itemLayout.setVisibility(View.VISIBLE);
                holder.menuLayout.setVisibility(View.GONE);

                if(mData.get(position).isImportant())
                    holder.importantBt.setVisibility(View.VISIBLE);
                else
                    holder.importantBt.setVisibility(View.GONE);
                if(mData.get(position).isChecked())
                    holder.todoItemCheckBox.setTextColor(mContext.getResources().getColor(R.color.checkedText));
                else
                    holder.todoItemCheckBox.setTextColor(mContext.getResources().getColor(R.color.blackText));
                if(mData.get(position).isRepeat())
                    holder.repeatBtn.setVisibility(View.VISIBLE);
                else
                    holder.repeatBtn.setVisibility(View.GONE);
            }

            setPercentage();
        }
        catch (Exception e){
            Log.e("Exception:", String.valueOf(e));
        }

        //textview_todo_item.setText("할 일"); 동일
    }

    // getItemCount() - 전체 데이터 갯수 리턴
    @Override
    public int getItemCount(){
        return mData.size();
    }

    public void changeChecked(String id, boolean isChecked){
        Log.d("id: ",id);
        if(isChecked)
            checkedNum++;
        else
            checkedNum--;
        try{
            realm = Realm.getDefaultInstance();
            ToDoItem item = realm.where(ToDoItem.class)
                    .contains("id",id)
                    .findFirst();
            realm.beginTransaction();
            item.setChecked(isChecked);
            Log.d("is checked? ", item.isChecked()+"");
            realm.commitTransaction();
            realm.close();
        }
        catch(Exception e){
            Log.e("error at changeChecked in todo adapter:", String.valueOf(e));
        }
    }

    public void deleteDB(String id){
        final String dId = id;
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                ToDoItem dItem = realm.where(ToDoItem.class)
                        .equalTo("id",dId)
                        .findFirst();
                if(dItem.isValid()){
                    dItem.deleteFromRealm();
                }
            }
        });
        realm.close();
    }

    public void deleteAll(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        final Date beforeTwoYear = cal.getTime();
        Log.d("Date beforeTwoYear: ", String.valueOf(beforeTwoYear));

        Log.d("delete all:", "called");
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                RealmResults<ToDoItem> dItems = realm.where(ToDoItem.class)
                        .lessThan("date",beforeTwoYear)
                        .findAll();

                for(int i = 0; i < dItems.size(); i++){
                    if(dItems.get(i).isValid()){
                        Log.d("delete :", dItems.get(i).getContent());
                    }
                }
                dItems.deleteAllFromRealm();


            }
        });
        realm.close();

    }

    public int getCheckedNum(){ return checkedNum; }
    public void setCheckedNum(int checkedNum){ this.checkedNum = checkedNum; }

    public void setPercentage(){
        int total, checked;
        total = getItemCount();
        checked = getCheckedNum();
        Log.d("total. checked:",total+", " + checked);
        ((MainActivity)mContext).setPercent(total,checked);
    }

    public void showMenu(int position){
        showMenuPos = position;
        notifyDataSetChanged();
    }

    public void closeMenu(){
        showMenuPos = -1;
        notifyDataSetChanged();
    }



    public void setMode(int mode){
        this.mode = mode;
    }

    public int getMode(){ return this.mode; }


}
