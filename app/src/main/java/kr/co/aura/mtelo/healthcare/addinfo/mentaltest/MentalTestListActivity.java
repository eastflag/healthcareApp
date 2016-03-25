package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.MLog;

/**
 * Created by young-kchoi on 2016. 2. 18..
 */
public class MentalTestListActivity extends SherlockActivity implements View.OnClickListener{

    private ImageButton mBtn[] = new ImageButton[4];
    private ArrayList<MentalListItem> mListItem = new ArrayList<MentalListItem>();
    private String mUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mental_test_list);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userId");


        init_ACtionBar();
        getDate(mUserId);


        mBtn[0] = (ImageButton) findViewById(R.id.test_list_btn1);
        mBtn[1] = (ImageButton) findViewById(R.id.test_list_btn2);
        mBtn[2]= (ImageButton) findViewById(R.id.test_list_btn3);
        mBtn[3]= (ImageButton) findViewById(R.id.test_list_btn4);


        mBtn[0].setOnClickListener(this);
        mBtn[1].setOnClickListener(this);
        mBtn[2].setOnClickListener(this);
        mBtn[3].setOnClickListener(this);


        initBtn(mListItem);

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

    private void getDate(String userId){
        //학교 정보 추출
        JSONNetWork_Manager.request_Get_Mental_Info( userId , this, new NetWork.Call_Back() {
            @Override
            public void onError(String error) {}

            @Override
            public void onGetResponsData(byte[] data) { }

            @Override
            public void onGetResponsString(String data) {
                Log.e("", "$$$ request_Get_Mental_Info()\n " + data);

                String Result = null, errMsg = null;
                JSONObject jsonObject = null;

                try
                {
                    if ( data != null )
                    {
                        JSONArray array = new JSONArray( data);
                        btnSetting(array);
                    }
                    else
                    {
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRoaming(String message) {}
        });
    }

    public void btnSetting(JSONArray array){
        try {
            if (array.length() == 0) return;
            for (int i = 0; i < array.length(); i++) {
                MLog.write(Log.ERROR, this.toString(), "array= i " + array.get(i));
                JSONObject object = array.getJSONObject(i);  // JSONObject 추출

                MentalListItem item = new MentalListItem();
                item.setIntroType(object.getString("introType"));
                item.setIntroImg(object.getString("introImg"));
                item.setIntroVideo(object.getString("introVideo"));
                item.setIntroType(object.getString("introType"));
                item.setOutroImg(object.getString("outroImg"));
                item.setOutroVideo(object.getString("outroVideo"));
                item.setSimliId(object.getString("simliId"));
                item.setTitle(object.getString("simliNm"));
                item.setUseYN(object.getString("useYN").equals("Y")? true: false);
                mListItem.add(item);
            }
            mHandler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                initBtn(mListItem);
        }
    };

    public void initBtn(ArrayList<MentalListItem> array){
        for (int i = 0; i < array.size(); i++) {

            MentalListItem item = array.get(i);
            Log.e("", "!!!!!! initBtn " + i + item.toString());
            mBtn[i].setTag(item);
//            mBtn[i].setText(item.title);

            if (item.isUseYN()){

                // 인터넷 중독 Gone 처리
                if(i != 2){
                    mBtn[i].setVisibility(View.VISIBLE);
                }
            }
            else {
                mBtn[i].setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        MentalListItem item = (MentalListItem)v.getTag();

        //Intent intent = new Intent(MentalTestListActivity.this, VideoTest.class);
        Intent intent = new Intent(this, VideoTestSelectView.class);
//        intent.putExtra("simliId",      item.getSimliId());
//        intent.putExtra("introType" ,    item.getIntroType());
//        intent.putExtra("introImg" ,    item.getIntroImg());
//        intent.putExtra("introVideo" ,  item.getIntroVideo());
//        intent.putExtra("outroType" ,    item.getOutroType());
//        intent.putExtra("outroImg" ,    item.getIntroImg());
//        intent.putExtra("outroVideo" ,  item.getOutroVideo());
        intent.putExtra("userId", mUserId);

        intent.putExtra("mentalListItem", item);

        switch (v.getId()){
            case R.id.test_list_btn1:
                break;
            case R.id.test_list_btn2:
                break;
            case R.id.test_list_btn3:
                break;
            case R.id.test_list_btn4:
                break;
        }
        startActivity(intent);
    }



}
