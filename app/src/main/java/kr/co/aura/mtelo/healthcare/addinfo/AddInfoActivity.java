package kr.co.aura.mtelo.healthcare.addinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.addinfo.exercise.ExerciseActivity;
import kr.co.aura.mtelo.healthcare.addinfo.mentaltest.MentalTestListActivity;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.MLog;

/**
 * Created by young-kchoi on 2016. 2. 4..
 */
public class AddInfoActivity extends SherlockActivity implements View.OnClickListener {


    private Context mCon = this;
    private Intent mExerciseIntent ;
    private String mName , mSex, mUserId;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info);

        ImageButton btn1 = (ImageButton) findViewById(R.id.add_info_btn1);
        ImageButton btn2 = (ImageButton) findViewById(R.id.add_info_btn2);
        Button btn3 = (Button) findViewById(R.id.add_info_btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        Intent intent = getIntent();
        mSex    = intent.getStringExtra("sex");
        mUserId = intent.getStringExtra("userId");
        mName   = intent.getStringExtra("name");


        init_ACtionBar();

        //실행후 운동량의 데이터를 가져올것
        getDate();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void init_ACtionBar() {
        ActionBar mActionBar;

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.custom_actionbar);

        String str = new CPreferences(mCon).getUserName();
        ((TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(str);        //타이틀

        String date = new CPreferences(mCon).getTitleLastDate();
        ((TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setText(date);    //날짜
        ((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.add_info_btn1:  //활동량 조회intent = new Intent(AddInfoActivity.this, VideoTest.class);
                intent = new Intent(AddInfoActivity.this, MentalTestListActivity.class);
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("심리검사 시작")
                        .putContentType("Video")
                        .putContentId("01040239227"));
                break;

            case R.id.add_info_btn2:    //심리검사
                intent = mExerciseIntent ;
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("활동량 체크")
                        .putContentId("01040239227"));
                break;

            case R.id.add_info_btn3:
                break;
        }

        if (intent != null) {
            intent.putExtra("userId", mUserId); //userId
            startActivity(intent);
            finish();
        }
    }



    private void getDate(){
        //학교 정보 추출
        JSONNetWork_Manager.request_Get_Exercise_Info(mUserId, "", this, new NetWork.Call_Back() {
            @Override
            public void onError(String error) {
            }

            @Override
            public void onGetResponsData(byte[] data) {
            }

            @Override
            public void onGetResponsString(String data) {
                Log.e("!!!!", "!!! request_Get_Exercise_Info()\n " + data);
                try {
                    if (data != null) {

                        data = "["+data+"]";
                        Log.e("!!!!", "!!! request_Get_Exercise_Info()");
                        JSONArray array = new JSONArray(data);
                        btnSetting(array);
                    } else {
                    }
                } catch (Exception e) {
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
           mExerciseIntent = new Intent(AddInfoActivity.this , ExerciseActivity.class );
            if (array.length() == 0) return;

           MLog.write(Log.ERROR, this.toString(), "array= i " + array.get(0));
           JSONObject object = array.getJSONObject(0);  // JSONObject 추출

           mExerciseIntent.putExtra("date", object.getString("exerciseDate")); //운동날짜
           mExerciseIntent.putExtra("name", object.getString("exerciseName")); // 이름
           mExerciseIntent.putExtra("img", object.getString("exerciseImg")); //이미지 경로
           mExerciseIntent.putExtra("calorie", object.getString("calorie")); //칼로리
           mExerciseIntent.putExtra("step", object.getString("step")); //걸음수
           mExerciseIntent.putExtra("distance", object.getString("distance")); //이동거리
           mExerciseIntent.putExtra("bodyType", object.getString("bodyType")); //체형

           mExerciseIntent.putExtra("class", object.getString("rangkingClass")); // 반랭킹
           mExerciseIntent.putExtra("grade", object.getString("rangkingGrade")); //학년랭킹
           mExerciseIntent.putExtra("exercise", object.getString("rangkingExercise")); // 종목랭킹

//                mExerciseIntent.putExtra("user", object.getString("user")); //사용자 운동량
           mExerciseIntent.putExtra("average", object.getString("calorieAverage")); //평군 운동량
           mExerciseIntent.putExtra("averageMax", object.getString("calorieMax")); //평군 운동량 맥스

            Log.e("!!!!", "!!!! intent " + mExerciseIntent.getExtras());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
