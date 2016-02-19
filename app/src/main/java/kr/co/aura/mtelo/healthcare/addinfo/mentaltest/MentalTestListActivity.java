package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.util.MLog;

/**
 * Created by young-kchoi on 2016. 2. 18..
 */
public class MentalTestListActivity extends Activity implements View.OnClickListener{

    private Button mBtn[] = new Button[4];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mental_test_list);


        mBtn[0] = (Button)findViewById(R.id.test_list_btn1);
        mBtn[1] = (Button)findViewById(R.id.test_list_btn2);
        mBtn[2] = (Button)findViewById(R.id.test_list_btn3);
        mBtn[3] = (Button)findViewById(R.id.test_list_btn4);


        mBtn[0].setOnClickListener(this);
        mBtn[1].setOnClickListener(this);
        mBtn[2].setOnClickListener(this);
        mBtn[3].setOnClickListener(this);
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
                    else //if(Result != null && Result.equalsIgnoreCase("2") )
                    {
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRoaming(String message) {

            }
        });
    }

    public void btnSetting(JSONArray array){
        try {
            for (int i = 0; i < array.length(); i++) {
                if (array.length() == 0) return;
                MLog.write(Log.ERROR, this.toString(), "array= " + array.get(i));
                JSONObject object = array.getJSONObject(i);  // JSONObject 추출
                mBtn[i].setText(object.getString("simliNm"));

                if(object.getString("useYN").equals("Y")){
                    mBtn[i].setVisibility(View.VISIBLE);
                }else{
                    mBtn[i].setVisibility(View.GONE);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
