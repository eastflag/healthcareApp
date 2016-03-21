package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;

/**
 * Created by young-kchoi on 16. 3. 16..
 */
public class VideoTestSelectView extends SherlockActivity implements  View.OnClickListener{

    private  String mSimliId = null;
    private String mUserId = null;

    private String resultMessage = "";
    private boolean existResult = false;

    MentalListItem item = new MentalListItem();

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_test_select_view);

        init_ACtionBar();

        Intent intent = getIntent();

        item = (MentalListItem)intent.getSerializableExtra("mentalListItem");
        mUserId  = intent.getStringExtra("userId");
        mSimliId = item.getSimliId();


        Log.e("!!!!!!!! " , "!!!!!! video_testreuslt_list  simliId "+ mSimliId);;

        Button btn1 = (Button) findViewById(R.id.btn_test_start);
        TextView btn2 = (Button) findViewById(R.id.btn_test_result);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        mImageView = (ImageButton) findViewById(R.id.test_kind_img);

        existResult = false;
        // TODO 검사 결과 확인 하기
        JSONNetWork_Manager.request_Get_Simli_Result_Check(mUserId, mSimliId, this, new NetWork.Call_Back() {
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
                        if (data.equals("O")) {
                            resultMessage = "검사결과 있음";
                            existResult = true;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    ((Button) findViewById(R.id.btn_test_result)).setVisibility(View.VISIBLE);
                                }
                            });

                        } else {
                            resultMessage = "검사결과 없음";
                        }
                    } else {
                        resultMessage = "결과값이 없습니다.";
                    }
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_LONG).show();
//                        }
//                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRoaming(String message) {
            }
        });

        if(mSimliId.toUpperCase().startsWith("AN")){ //불안척도
            setWorryMode();
        }else if(mSimliId.toUpperCase().startsWith("GL")){ //우울척도
            setGloomMode();
        }else if(mSimliId.toUpperCase().startsWith("SE")) { //자아존중감
            setSelfMode();
        }

    }

    //우울척도
    private void setGloomMode(){
        //mBG.setBackgroundResource(R.color.video_test_result_gloom_bg);
        mImageView.setBackgroundResource(R.drawable.btn_gloom);
        //mLinear.setBackgroundResource(R.drawable.btn_video_test_restlt_gloom);
    }

    //불안척도
    private void setWorryMode(){
        //mBG.setBackgroundResource(R.color.video_test_result_worry_bg);
        mImageView.setBackgroundResource(R.drawable.btn_worry);
        //mLinear.setBackgroundResource(R.drawable.btn_video_test_restlt_worry);
    }

    //자아존중감 모드
    private void setSelfMode(){
        //mBG.setBackgroundResource(R.color.video_test_result_self_bg);
        mImageView.setBackgroundResource(R.drawable.btn_self);
        //mLinear.setBackgroundResource(R.drawable.btn_video_test_restlt_self);
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
        Intent intent = null;

        switch (v.getId() ){
            case R.id.btn_test_start:
                intent = new Intent(this, VideoTest.class);
                intent.putExtra("mentalListItem", item);
                break;

            case R.id.btn_test_result:
                intent = new Intent(VideoTestSelectView.this, VideoTestResultList.class);
                break;
        }

        intent.putExtra("existResult", existResult);
        intent.putExtra("simliId", mSimliId);  //심리ID
        intent.putExtra("userId", mUserId);     //유저ID
        startActivity(intent);
    }
}
