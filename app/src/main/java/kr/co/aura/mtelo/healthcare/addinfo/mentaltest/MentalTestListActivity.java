package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.util.MLog;

/**
 * Created by young-kchoi on 2016. 2. 18..
 */
public class MentalTestListActivity extends Activity implements View.OnClickListener{

    private Button mBtn[] = new Button[4];
    private ArrayList<MentalListItem> mListItem = new ArrayList<MentalListItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mental_test_list);
        getDate();


        mBtn[0] = (Button)findViewById(R.id.test_list_btn1);
        mBtn[1] = (Button)findViewById(R.id.test_list_btn2);
        mBtn[2]= (Button)findViewById(R.id.test_list_btn3);
        mBtn[3]= (Button)findViewById(R.id.test_list_btn4);


        mBtn[0].setOnClickListener(this);
        mBtn[1].setOnClickListener(this);
        mBtn[2].setOnClickListener(this);
        mBtn[3].setOnClickListener(this);


        initBtn(mListItem);

    }


      private void getDate(){
        //학교 정보 추출
//        String url = Define.getNetUrl() + Define.MENTAL_LIST+"?" +JSONNetWork.KEY_USER_ID+ "=123";;
        String url = "http://210.127.55.205:82/HealthCare/simli/type_list?userId=123";
        Log.e("LDK", "############# url: " + url);

        JSONNetWork_Manager.request_Get_Mental_Info("123", this, new NetWork.Call_Back() {
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
                item.intro = object.getString("intro");
                item.outro = object.getString("outro");
                item.simliId = object.getString("simliId");
                item.title = object.getString("simliNm");
                item.useYN = object.getString("useYN").equals("Y")? true: false;
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
            Log.e("" , "!!!!!! initBtn "+i + item.toString());
            mBtn[i].setTag(item);
            mBtn[i].setText(item.title);

            if (item.useYN)
                mBtn[i].setVisibility(View.VISIBLE);
            else
                mBtn[i].setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        MentalListItem item = (MentalListItem)v.getTag();

        Intent intent = new Intent(MentalTestListActivity.this, VideoTest.class);
        intent.putExtra("simliId", item.simliId);
        intent.putExtra("intro" ,  item.intro);
        intent.putExtra("outro" ,  item.outro);



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
        String intro = null;
        String outro = null;
        String simliId = null;
        String title = null;
        boolean useYN = false;

        @Override
        public String toString() {
            return "MentalListItem{" +
                    "intro='" + intro + '\'' +
                    ", outro='" + outro + '\'' +
                    ", simliId='" + simliId + '\'' +
                    ", title='" + title + '\'' +
                    ", useYN=" + useYN +
                    '}';
        }

        public MentalListItem() { }
    }
}
