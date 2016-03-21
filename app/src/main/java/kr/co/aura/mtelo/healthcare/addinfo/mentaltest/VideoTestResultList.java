package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;

/**
 * Created by young-kchoi on 16. 3. 16..
 */
public class VideoTestResultList extends SherlockActivity implements  View.OnClickListener{

    private final int BTN_PARENT = R.id.btn_video_test_result_parent ;
    private final int BTN_CHILD = R.id.btn_video_test_result_child ;

    private  String mSimliId = null;
    private String mUserId = null;
    private String resultMessage = "";
    private  boolean existResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_test_result_list);

        init_ACtionBar();

        Intent intent = getIntent();
        ArrayList<String> answerList =  intent.getStringArrayListExtra("answer");
        mSimliId = intent.getStringExtra("simliId");   //심리ID
        mUserId  = intent.getStringExtra("userId");   //유저ID
        existResult = intent.getBooleanExtra("existResult", false);

        Log.e("!!!!!!!! " , "!!!!!! video_testreuslt_list  simliId "+ mSimliId);;

        ImageButton imgBtn = (ImageButton) findViewById(R.id.btn_video_test_result_parent);
        ImageButton imgBtn2 = (ImageButton) findViewById(R.id.btn_video_test_result_child);
        imgBtn.setOnClickListener(this);
        imgBtn2.setOnClickListener(this);

        if(!existResult) {
            // TODO 결과 등록 하기
            JSONNetWork_Manager.request_insert_Simli_Result(mUserId, answerList, this, new NetWork.Call_Back(){
                @Override
                public void onError(String error) {
                }

                @Override
                public void onGetResponsData(byte[] data) {

                }

                @Override
                public void onGetResponsString(String data) {

                    Log.e("$$$", "$$$ request_Get_Mental_Info()\n " + data);

                    try {

                        if (data != null) {
                            if(data.equals("0")){
                                resultMessage = "검사결과 등록 성공";

                            } else if(data.equals("1")){
                                resultMessage = "검사결과 등록 실패";
                            } else {
                                resultMessage = "검사결과 등록 실패";
                            }
                        } else {
                            resultMessage = "결과값이 없습니다.";
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onRoaming(String message) {
                }
            } );
        }
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
