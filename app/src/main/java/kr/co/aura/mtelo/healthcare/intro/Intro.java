package kr.co.aura.mtelo.healthcare.intro;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.GCMIntentService;
import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.certification.Terms;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork.Call_Back;
import kr.co.aura.mtelo.healthcare.util.LCommonFunction;
import kr.co.aura.mtelo.healthcare.util.MLog;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager;

public class Intro extends Activity {

    // GCM 임시 데이터
    static String gcmURL = "https://android.googleapis.com/gcm/send";
    static String gcmRegID = ""; // 단말의 레지스터 ID
    // static String gcmAuthToken ="GCM 서버 등록한 Access Key";


    static {
        MLog.set_LogEnable(Define.LOG, Define.LOG_FILTER);
    }

    private final int SHOW_SERVICE_INFO = 101;
    private final int SHOW_SERVICE_ERROR = 102;

    private RelativeLayout introLayout;
    private ImageView iv_city;
    private Context mCon;

    private AQuery mAq;

    private final int MY_PERMISSION_GRANTED = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        checkPermission();
    }

    private void init() {
        mAq = new AQuery(this);
        iv_city = (ImageView) findViewById(R.id.logo_city);

        mCon = Intro.this;

        introLayout = (RelativeLayout) findViewById(R.id.intro_layout);
        Animation alphaAnim = (Animation) AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.alpha);
        introLayout.startAnimation(alphaAnim);

        //학교 정보 추출
        String url = Define.getNetUrl() + Define.STUDENT_LIST;
        //Log.e("LDK", "url: " + url);

        JSONObject json = new JSONObject();
        try {
            json.put("mdn", getMdn(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAq.post(url, json, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                Log.e("LDK", "!!!!!! @#!@# result: url "+ url +" , object " + object.toString());
                Log.e("" , "!!!!!! mdn " + getMdn(Intro.this));

                if (status.getCode() != 200) {
                    showLogo(1);
                    return;
                }

                try {
                    JSONArray array = object.getJSONArray("value");
                    if (array != null && array.length() > 0) {
                        JSONObject json = array.getJSONObject(0);
                        if (json.has("address")) {
                            String address = json.getString("address");
                            if (address != null && address.contains("광명")) {
                                //show 광명 로고
                                showLogo(1);
                            } else {
                                //show 오산 로고
                                showLogo(2);
                            }
                        } else {
                            showLogo(1);
                        }
                    } else {
                        showLogo(1);
                    }
                } catch (JSONException e) {
                    showLogo(1);
                    Log.e("LDK", "json exception:" + e.getMessage());
                }
            }
        });

//		if(Define.LOG)
//			select_Dev_Server();
//		else
        start_Timer();
    }


    private void checkPermission() {
        Log.i("", "!!!!! CheckPermission : " + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //사용권한이 없을경우

            //최초권한 요청인지 , 사용자에 의한 재요청인지 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                Log.e("", "@@@@@@@@ permission  재요청");
            }

             //최초로 권한을 요청하는경우(처음실행)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_GRANTED);


        } else {
            //사용 권한이 있는경우
            Log.e("", "@@@@@@@@@@@@@ ermission deny");
            init();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_GRANTED:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 획득", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "권한 허용을 선택하지않은경우 정상동작을 보장할수없습니다.", Toast.LENGTH_LONG).show();
                }
                break;
        }

        init();
    }

    public String getMdn(Context context) {
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //2015-06-29 전화번호가 +82로 시작하는 경우 보정
        String number = tMgr.getLine1Number();
        if (number.startsWith("+82")) {
            number = number.replace("+82", "0");
        }
        return number;
    }

    private void showLogo(int index) {
        iv_city.setVisibility(View.VISIBLE);
        switch (index) {
            case 1: //광명
                iv_city.setImageResource(R.drawable.logo_kwangmeong);
                break;
            case 2: //오산
                iv_city.setImageResource(R.drawable.logo_osan);
                break;
        }

        Animation anim = (Animation) AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.translate_bounce);
        iv_city.startAnimation(anim);
    }


    private void start_Timer() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                registGCM();
                request_ServiceInfo();
            }
        }, 2000);
    }


    private void select_Dev_Server() {
        final String list[] = {"http://106.245.237.196:7070/HealthCare/",// mtelo 헬스케어 개발서버(100)
                "http://192.168.0.48/HealthCare/",        // 유재선 연구원
                //"http://192.168.0.199/HealthCare/", 		// 임은실 연구원 TTA DB서버
                "http://192.168.0.199:7070/HealthCare/",        // 임은실 연구원 REAL DB 서버
                "http://192.168.0.199:5050/HealthCare/",        // 임은실 연구원 TEST DB 서버
                "http://192.168.0.199:4040/HealthCare/",
                "http://192.168.0.199:6060/HealthCare/",
                "http://210.127.55.205/HealthCare/",        // 상용서버
                //"http://192.168.0.28/HealthCare/", 		// TTA 테스트서버 회사IP
                //"http://210.96.71.161/HealthCare/", 		// TTA 테스트서버
                //"http://210.127.55.205:82/HealthCare/"		// 개발서버
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCon, android.R.layout.simple_list_item_1, list);
        AlertDialog.Builder dal = new AlertDialog.Builder(mCon);
        dal.setTitle("선택");
        dal.setInverseBackgroundForced(true);
        dal.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mCon, list[which] + " 입니다 ", Toast.LENGTH_SHORT).show();
                Define.ServerAdd = list[which];
                registGCM();
                request_ServiceInfo();
            }
        });
        dal.show();

    }


    private void registGCM() {
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        gcmRegID = GCMRegistrar.getRegistrationId(this);

        if (gcmRegID != null)  // 구글 가이드에는 regId.equals("")로 되어 있는데 Exception을
            // 피하기 위해 수정
            GCMRegistrar.register(this, GCMIntentService.SEND_ID);
        else
            Log.e("==============", gcmRegID);
    }

    boolean mService_Info = false;
    String mService_Message;

    private void request_ServiceInfo() {
        JSONNetWork_Manager.request_ServiceInfo(this, new Call_Back() {

            @Override
            public void onRoaming(String message) {
            }

            @Override
            public void onGetResponsString(String data) {
                MLog.write(Log.ERROR, "", "request_ServiceInfo() " + data);

                String Result = null, errMsg = null;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
                    Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); // 결과값
                    errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); // 에러 메세지

                    if (Result != null && Result.equalsIgnoreCase("0")) {
                        JSONObject array = new JSONObject(jsonObject.optString(JSONNetWork.RETURN_KEY_VALUE));
                        if (array != null) {
                            mService_Info = array.optBoolean(JSONNetWork.KEY_SERVICE_INFO);
                            mService_Message = array.optString(JSONNetWork.KEY_SERVICE_MESSAGE);
                        }
                    }

                    if (mService_Info) {
                        moveMain(introLayout);
                    } else {
                        Message msg = mHandler.obtainMessage(SHOW_SERVICE_INFO, mService_Message);
                        mHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = mHandler.obtainMessage(SHOW_SERVICE_ERROR, data);
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onGetResponsData(byte[] data) {
            }

            @Override
            public void onError(String error) {
                Message msg = mHandler.obtainMessage(SHOW_SERVICE_ERROR, error);
                mHandler.sendMessage(msg);
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_SERVICE_INFO:
                    Popup_Manager.Show_Dialog(mCon, (String) msg.obj, mService_Message,
                            "확인", new Popup_Manager.OneButton_Handle() {

                                @Override
                                public void onOK() {
                                    finish();
                                }
                            });
                    break;

                case SHOW_SERVICE_ERROR:
                    Popup_Manager.Show_Error_Dialog(mCon, (String) msg.obj, new Popup_Manager.OneButton_Handle() {
                        @Override
                        public void onOK() {
                        }
                    });
                    break;

            }

            super.handleMessage(msg);
        }
    };

    public void moveMain(View v) {
        startActivity(new Intent(getApplicationContext(), Terms.class));
        finish();
    }

    @Override
    public void finish() {
        overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
        super.finish();
    }

    @Override
    protected void onPause() {
        // finish();
        super.onPause();
    }

}
