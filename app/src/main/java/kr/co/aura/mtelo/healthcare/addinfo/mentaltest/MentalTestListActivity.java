package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;

/**
 * Created by young-kchoi on 2016. 2. 18..
 */
public class MentalTestListActivity extends Activity implements View.OnClickListener{

    private Button mBtn1, mBtn2, mBtn3, mBtn4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mental_test_list);


        mBtn1 = (Button)findViewById(R.id.test_list_btn1);
        mBtn2 = (Button)findViewById(R.id.test_list_btn2);
        mBtn3 = (Button)findViewById(R.id.test_list_btn3);
        mBtn4 = (Button)findViewById(R.id.test_list_btn4);


        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);


        getDate();
    }


    public void jsoncallback(String url, JSONObject json, AjaxStatus status){

        if(json != null) {
            Log.e("", "!!!!!!! url :" + url + ", data :" + json);
        }else{
            Log.e("", "!!!!!!! url :" + url + ", data noooooooooo" );
        }
    }

    private void getDate(){
        AQuery mAq = new AQuery(this);

        //학교 정보 추출
//        String url = Define.getNetUrl() + Define.MENTAL_LIST+"?" +JSONNetWork.KEY_USER_ID+ "=123";;
        String url = "http://210.127.55.205:82/HealthCare/simli/type_list?userId=123";
        Log.e("LDK", "############# url: " + url);
        JSONNetWork_Manager.request_Get_Mental_Info("123", this, new NetWork.Call_Back() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onGetResponsData(byte[] data) {
            }

            @Override
            public void onGetResponsString(String data) {
                Log.e("LDK", "############# data: " + data);
            }

            @Override
            public void onRoaming(String message) {

            }
        });

    }

    @Override
    public void onClick(View v) {
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
    }
}
