package com.example.appsolute_todolist;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.EditText;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener{

    Button insertButton;
    Button FloatingActionButton;
    EditText todoEdit;
    private ArrayList<Todo> todoArrayList;
    private TodoAdapter todoAdapter;        // 어뎁터를 사용하기 위해 정의




    private ListView listView;                      // 리스트뷰
    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    private List<String> list;                      // String 데이터를 담고있는 리스트
    private ListViewAdapter adapter;                // 리스트뷰의 아답터
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerlist);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        todoArrayList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoArrayList);       // 어뎁터 안에 ArrayList 넣기
        recyclerView.setAdapter(todoAdapter);   // 어뎁터를 셋팅

        insertButton = (Button) findViewById(R.id.button_insert_main) ;
        todoEdit = (EditText) findViewById(R.id.edit_todo_main);

        insertButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Todo newTodo = new Todo(todoEdit.getText().toString());     // 입력한 문자열로 Todo 객체 생성
                todoArrayList.add(newTodo);     // 생성한 객체를 ArrayList<Todo> 타입의  TodoArrayList에 추가
                todoAdapter.notifyDataSetChanged();     // 어뎁터에게 데이터 셋이 변경되었음을 알린다.
                todoEdit.setText(null);
            }
        });






        setContentView(R.layout.listview_paging);

        listView = (ListView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        list = new ArrayList<String>();
        adapter = new ListViewAdapter(this, list);
        listView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);


        listView.setOnScrollListener(this);
        getItem();

    }



    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    private void getItem(){

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        // 다음 20개의 데이터를 불러와서 리스트에 저장한다.
        for(int i = 0; i < 20; i++){
            String label = "Label " + ((page * OFFSET) + i);
            list.add(label);
        }

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },1000);
    }
}