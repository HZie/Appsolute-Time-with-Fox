package com.example.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.realm.Realm;

public class ToDoRecyclerAdapter extends RecyclerView.Adapter<ToDoRecyclerAdapter.ViewHolder> {
    private ArrayList<ToDoItem> mData = null;     // Todo라는 객체를 가진 ArrayList
    Realm realm;

    // item View를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox itemCheckBox;
        ImageView importantBt;

        public ViewHolder(final View itemView){   // itemView와 연결했기 때문에 findViewById 앞에 itemView를 명시한다.
            super(itemView);

            itemCheckBox = itemView.findViewById(R.id.itemCheckbox);
            this.importantBt = itemView.findViewById(R.id.importantBtn);


            // 체크박스 리스너
            itemCheckBox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();    // 현재 어뎁터가 다루고 있는 리스트의 포지션을 가져온다.
                    if(position != RecyclerView.NO_POSITION){   // 삭제된 포지션이 아닌 경우
                        changeChecked(mData.get(position).getId(),itemCheckBox.isChecked());
                        notifyDataSetChanged();     // 어뎁터에게 데이터 셋이 변경되었음을 알린다.

                    }
                }
            });
        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ToDoRecyclerAdapter(ArrayList<ToDoItem> list){
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ToDoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_todo_recycler, parent, false);
        ToDoRecyclerAdapter.ViewHolder vh = new ToDoRecyclerAdapter.ViewHolder(view);
        realm.getDefaultInstance();

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ToDoRecyclerAdapter.ViewHolder holder, int position){
        holder.itemCheckBox.setText(mData.get(position).getContent());       // 직접적으로 binding 해주는 것
        holder.itemCheckBox.setChecked(mData.get(position).isChecked());

        if(mData.get(position).isImportant())
            holder.importantBt.setVisibility(View.VISIBLE);
        else
            holder.importantBt.setVisibility(View.GONE);
        //textview_todo_item.setText("할 일"); 동일
    }

    // getItemCount() - 전체 데이터 갯수 리턴
    @Override
    public int getItemCount(){
        return mData.size();
    }

    public void changeChecked(String id, boolean isChecked){
        Log.d("id: ",id);
        try{
            realm = Realm.getDefaultInstance();
            Log.d("realm:", String.valueOf(realm));
            ToDoItem item = realm.where(ToDoItem.class)
                    .contains("id",id)
                    .findFirst();
            realm.beginTransaction();
            item.setChecked(isChecked);
            realm.commitTransaction();
            realm.close();
        }
        catch(Exception e){
            Log.e("error at changeChecked in todo adapter:", String.valueOf(e));
        }

    }


}
