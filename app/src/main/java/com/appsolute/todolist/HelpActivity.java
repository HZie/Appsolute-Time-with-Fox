package com.appsolute.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todolist.databinding.ActivityAddTodoBinding;
import com.example.todolist.databinding.ActivityHelpBinding;

public class HelpActivity extends Activity {

    ActivityHelpBinding binding;
    ImageButton closeBtn;
    TextView helpTxt;
    TextView helpTxt2;
    TextView helpTxt3;
    TextView helpTxt4;
    String helpStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9); // Display 사이즈의 90%
        getWindow().getAttributes().width = width;

        closeBtn = binding.closeBtn;
        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setHelpText();
        helpTxt = binding.helpText;
        helpTxt.setText(helpStr);
        helpTxt.setMovementMethod(new ScrollingMovementMethod());
    }


    // 여기에 도움말 텍스트 작성
    void setHelpText(){
        helpStr = ("1)To Do List\n" +
                "\n" +
                "[할 일 추가는 어떻게 하나요?]\n" +
                "-하단 오른쪽에 있는 + 버튼을 누르면 할 일을 추가할 수 있는 팝업이 생깁니다.\n" +
                "-팝업에는 3가지 옵션, 중요 / 반복 / 디데이가 있습니다. 원하는 옵션을 선택하면 해당 옵션의 세부 설정이 생깁니다. 해당하는 옵션을 선택한 후 저장 버튼을 누르면 팝업이 닫히며 할 일 항목이 생성됩니다. \n" +
                "-옵션으로 ‘중요’를 선택하면 별표시가 나타납니다.\n" +
                "\n" +
                "[할 일 삭제 / 수정은 어떻게 하나요?]\n" +
                "-할 일 리스트를 왼쪽으로 스와이프하면 수정(연필 모양) 혹은 삭제(휴지통 모양)를 선택할 수 있습니다.\n" +
                "-수정을 누르면 팝업이 생성되면서 할 일의 옵션을 수정할 수 있습니다.\n" +
                "-2년 전에 추가한 할 일 목록은 자동으로 삭제됩니다.\n" +
                "\n" +
                "[할 일을 완료하면 어떻게 되나요?]\n" +
                "-할 일을 완료하면 항목 옆에 있는 체크박스를 누르면 됩니다. 누르면 체크표시가 생성되면서 글씨색이 바뀝니다.\n" +
                "\n" +
                "\n" +
                "2)D-Day List\n" +
                "\n" +
                "[D-Day List 화면으로 어떻게 가나요?]\n" +
                "-화면 가운데 상단에 D-Day 항목 중 하나가 띄워집니다. 이 D-Day 버튼을 클릭하면 To Do List와 D-Day List의 전환이 이루어집니다.\n" +
                "\n" +
                "[D-Day 일정 추가 / 수정 / 삭제는 어떻게 하나요?]\n" +
                "-To Do List의 할 일 추가 / 수정 / 삭제하는 것과 방법이 똑같습니다. \n" +
                "-옵션 중 디데이를 클릭하면 마감일을 설정할 수 있습니다. \n" +
                "-만약 중요한 일정의 경우 ‘중요’ 옵션을 선택하면 별표시가 나타납니다. \n" +
                "\n" +
                "[D-Day 목록이 배열되는 규칙이 있나요?]\n" +
                "-마감일에 가까운 순서로 List에 배열됩니다.\n" +
                "-마감일이 지난 항목은 자동적으로 삭제가 됩니다.\n" +
                "\n" +
                "[D-Day 버튼을 숨기는 것이 가능한가요?]\n" +
                "-왼쪽 상단, D-Day 버튼 왼쪽에 위치한 스위치를 누르면 D-Day 버튼을 hide/show할 수 있습니다.\n" +
                "\n" +
                "\n" +
                "3)여우키우기\n" +
                "\n" +
                "[TODO앱에서 여우를 왜 키우는건가요?]\n" +
                "-여우를 키우는것은 유저의 일주일간 TODO 달성도를 알려주는 일종의 척도입니다.\n" +
                "-오늘 투두리스트의 70%이상을 달성한다면, 내일 방문시 여우와의 친밀도가 1LV 오르게됩니다.\n" +
                "-오직 투두리스트만 포함되며, DDAY리스트의 할일들은 달성도에 포함되지 않습니다." +
                "-일주일간 빠짐없이 70%이상을 달성해서 다음주 방문시 여우와의 친밀도가 7LV이 된다면, 여우는 약속을 지켜준 유저에게 고마워하며 떠나지않고 오아시스에 남을것입니다.\n" +
                "-여우와의 약속을 지키며 여우와 친밀도를 차곡차곡 쌓아보는게 어떨까요?\n" +
                "\n" +
                "[여우옆에있는 바하고 레벨은 무엇인가요?]\n" +
                "-바 > 오늘 투두리스트의 달성도를 나타내는 바입니다.\n" +
                "-레벨 > 일주일안에서 총 며칠간 할일의 70%를 달성했는지를 나타내는 레벨입니다.\n" + "ex) 3LV = 일주일중 3일간 70%달성\n" +
                "\n" +
                "[오아시스는 무엇인가요?]\n" +
                "-오아시스는 수많은모습의 여우를 모으는 도감이자 유저의 성실함을 보여주는 트로피입니다.\n" +
                "-유저와 친밀도를 많이 쌓아 7LV이 된 여우들은 일주일 후 떠나지 않고 오아시스로 가게됩니다.\n" +
                "-오아시스는 왼쪽 아래 비행기버튼을 클릭하면 갈 수 있으며, 오아시스에 가면 유저가 모은 여우들이 오아시스에 앉아있습니다.\n" +
                "-오아시스는 총 2개의 맵: 일반 오아시스와, 풀과눈으로 뒤덮힌 오아시스가 존재합니다. 이는 화면 좌우에 있는 화살표로 서로간 이동이 가능합니다.\n" +
                "-여우와의 약속을 지키며 오아시스에 활기를 불어넣어주세요!\n");
    }
}