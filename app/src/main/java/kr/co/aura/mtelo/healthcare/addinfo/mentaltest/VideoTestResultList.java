package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;

/**
 * Created by young-kchoi on 16. 3. 16..
 */
public class VideoTestResultList extends SherlockActivity implements  View.OnClickListener{

    private final int BTN_PARENT = R.id.btn_video_test_result_parent ;
    private final int BTN_CHILD = R.id.btn_video_test_result_child ;
    private  String mSimliId = null;
    private String mUserId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_test_result_list);

        init_ACtionBar();

        Intent intent = getIntent();
        ArrayList<String> answerList =  intent.getStringArrayListExtra("answer");
        mSimliId = intent.getStringExtra("simliId");   //심리ID
        mUserId  = new CPreferences(VideoTestResultList.this).getNowPosition();

        Log.e("!!!!!!!! " , "!!!!!! video_testreuslt_list  simliId "+ mSimliId);;

        ImageButton imgBtn = (ImageButton) findViewById(R.id.btn_video_test_result_parent);
        ImageButton imgBtn2 = (ImageButton) findViewById(R.id.btn_video_test_result_child);
        imgBtn.setOnClickListener(this);
        imgBtn2.setOnClickListener(this);




        StringBuilder sb = new StringBuilder();
        sb.append("문제들이 답은 다음과 같다");

        for (int i=0 ; i < answerList.size() ; i++){
            Log.e("!!!!!!" , "!!!!!! answer "+ i +", " + answerList.get(i));
            sb.append( i+ "번 답 " + answerList.get(i) +"\n");
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(VideoTestResultList.this);
        dialog.setMessage(sb);
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();

    }

    private void init_ACtionBar() {
        ActionBar mActionBar;

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.custom_actionbar);

        String str = new CPreferences(this).getUserName();
        ((TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(str);        //타이틀

        String date = new CPreferences(this).getTitleLastDate();
        ((TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setText(date);    //날짜
        ((ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(VideoTestResultList.this, VideoTestResult.class);

        switch (v.getId() ){
            case R.id.btn_video_test_result_parent:
                intent.putExtra("mode" , "parent");  //부모
                break;
            case R.id.btn_video_test_result_child:
                intent.putExtra("mode" , "child");  //부모
                break;
        }
        intent.putExtra("simliId", mSimliId);  //심리ID
        intent.putExtra("userId", mUserId);     //유저ID
        startActivity(intent);
    }
}
