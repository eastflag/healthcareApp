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
                item.introType      = object.getString("introType");
                item.introImg       = object.getString("introImg");
                item.introVideo     = object.getString("introVideo");
                item.introType      = object.getString("introType");
                item.outroImg       = object.getString("outroImg");
                item.outroVideo     = object.getString("outroVideo");
                item.simliId        = object.getString("simliId");
                item.title          = object.getString("simliNm");
                item.useYN          = object.getString("useYN").equals("Y")? true: false;
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

            if (item.useYN){

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

        Intent intent = new Intent(MentalTestListActivity.this, VideoTest.class);
        intent.putExtra("simliId",      item.simliId);
        intent.putExtra("introType" ,    item.introType);
        intent.putExtra("introImg" ,    item.introImg);
        intent.putExtra("introVideo" ,  item.introVideo);
        intent.putExtra("outroType" ,    item.outroType);
        intent.putExtra("outroImg" ,    item.outroImg);
        intent.putExtra("outroVideo" ,  item.outroVideo);
        intent.putExtra("userId", mUserId);



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


    class MentalListItem {
        String introType =null;
        String introImg = null;
        String introVideo = null;
        String outroType = null;
        String outroImg = null;
        String outroVideo = null;
        String simliId = null;
        String title = null;
        boolean useYN = false;

        public MentalListItem(){

        }

        public MentalListItem(String introType, String introImg, String introVideo, String outroType, String outroImg, String outroVideo, String simliId, String title, boolean useYN) {
            this.introType = introType;
            this.introImg = introImg;
            this.introVideo = introVideo;
            this.outroType = outroType;
            this.outroImg = outroImg;
            this.outroVideo = outroVideo;
            this.simliId = simliId;
            this.title = title;
            this.useYN = useYN;
        }

        @Override
        public String toString() {
            return "MentalListItem{" +
                    "introType='" + introType + '\'' +
                    ", introImg='" + introImg + '\'' +
                    ", introVideo='" + introVideo + '\'' +
                    ", outroType='" + outroType + '\'' +
                    ", outroImg='" + outroImg + '\'' +
                    ", outroVideo='" + outroVideo + '\'' +
                    ", simliId='" + simliId + '\'' +
                    ", title='" + title + '\'' +
                    ", useYN=" + useYN +
                    '}';
        }
    }








}
