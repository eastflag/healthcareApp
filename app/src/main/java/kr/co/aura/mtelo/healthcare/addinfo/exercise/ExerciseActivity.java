package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.MLog;

/**
 * Created by young-kchoi on 2016. 2. 11..
 */
public class ExerciseActivity extends SherlockActivity  implements  View.OnClickListener {

    private String mDate, mName, mImg, mCalorie, mStep, mDistance, mBodyType, mClass, mGrade, mExetices,  mAverage, mAverageMax, mUserId;
    private String mExerciseId;

    private TextView mExerciseDate , mExerciseName,  mExerciseCalorie, mExerciseStep, mExerciseDistance,
            mExerciseBodyType, mExerciseClass, mExerciseGrade, mExercicesEntries, mExerciseUser, mExerciseAverage;
    private ImageView mExerciseImg, mExerciseAverImg;
    private ProgressBar mProgressBar ;

    private ExerciseMain exerciseMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_main);

        Intent intent = getIntent();

        exerciseMain = (ExerciseMain)intent.getSerializableExtra("exercise_main");

        init_ACtionBar();
        intiLayout(intent);

        Log.e("!!!!", "!!!!! intent\n " + intent.getExtras() + "\n " + toString());

        ImageButton btnExeriseDetail = (ImageButton) findViewById(R.id.btn_exercise_detail);
        btnExeriseDetail.setOnClickListener(this);

        ImageButton btnExeriseHistory = (ImageButton) findViewById(R.id.btn_exercise_history);
        btnExeriseHistory.setOnClickListener(this);

        //이전 날짜
        ImageButton btnExeriseDatePrev = (ImageButton) findViewById(R.id.exercise_date_prev);
        btnExeriseDatePrev.setOnClickListener(this);

//        이후 날짜
        ImageButton btnExeriseDateNext= (ImageButton) findViewById(R.id.exercise_date_next);
        btnExeriseDateNext.setOnClickListener(this);
    }

    private void intiLayout(Intent intent) {
        mDate        = intent.getStringExtra("date"); //운동날짜
        mName        = intent.getStringExtra("name"); // 이름
        mImg         = intent.getStringExtra("img" ); //이미지 경로
        mCalorie     = intent.getStringExtra("calorie"); //칼로리
        mStep        = intent.getStringExtra("step"); //걸음수
        mDistance    = intent.getStringExtra("distance"); //이동거리
        mBodyType    = intent.getStringExtra("bodyType"); //체형

        mClass       = intent.getStringExtra("class"); // 반랭킹
        mGrade       = intent.getStringExtra("grade"); //학년랭킹
        mExetices    = intent.getStringExtra("exercise"); // 종목랭킹

        mAverage     = intent.getStringExtra("average"); //평군 운동량
        mAverageMax  = intent.getStringExtra("averageMax"); //평군 운동량 맥스
        mUserId      = intent.getStringExtra("userId"); //사용자Id


        if (mImg != null && mName != null && mDate != null) {  //정보가 있을경우 - 정상실행


            // 날짜
            mExerciseDate = (TextView) findViewById(R.id.exercise_date_txt);
            mExerciseDate.setText(mDate);


            //운동종목
            mExerciseName = (TextView) findViewById(R.id.exercise_left_top_txt);
            mExerciseName.setText(mName);


            //운동이미지
            mExerciseImg = (ImageView) findViewById(R.id.exercise_left_img);
            Picasso.with(ExerciseActivity.this).load(mImg).fit().into(mExerciseImg);

            //기본운동정보
            mExerciseCalorie = (TextView) findViewById(R.id.exercise_right_txt_1);  //칼로리
            mExerciseCalorie.setText(mCalorie + " Kcal");

            mExerciseStep = (TextView) findViewById(R.id.exercise_right_txt_2);     //걸음수
            mExerciseStep.setText(mStep + " 보");

            mExerciseDistance = (TextView) findViewById(R.id.exercise_right_txt_3);  //이동거리
            mExerciseDistance.setText(mDistance + " km");

            mExerciseBodyType = (TextView) findViewById(R.id.exercise_right_txt_4);  //체형


            //등수
            mExerciseClass = (TextView) findViewById(R.id.exercise_class_number);    //반 등수
            mExerciseClass.setText(mClass +"등");

            mExerciseGrade = (TextView) findViewById(R.id.exercise_student_number);    // 학년등수
            mExerciseGrade.setText(mGrade +"등");

            mExercicesEntries = (TextView) findViewById(R.id.exercise_entries_number); //종목등수
            mExercicesEntries.setText(mExetices);


            //칼로리 계산
            mExerciseUser = (TextView) findViewById(R.id.exercise_average_text);   //사용자 운동량
            mExerciseUser.setText(mBodyType + " - " + mCalorie + " Kcal"); // 바디타임과 사용자의 운동량을 추가한다


            //평균운동량 계산
            int user = Integer.parseInt(mCalorie);
            int aver = Integer.parseInt(mAverage);
            int result = aver - user;

            mExerciseAverage = (TextView) findViewById(R.id.exercise_average_sub_text);   //평균 운동량
            mExerciseAverage.setText(result + " Kcal");

            mExerciseAverImg = (ImageView) findViewById(R.id.exercise_average_sub_img);
            if (user > aver) {
                mExerciseAverImg.setBackgroundResource(R.drawable.arrow_up
                );
            } else {
                mExerciseAverImg.setBackgroundResource(R.drawable.arrow_down);  //up이미지가 필요하다
            }


            //프로그래스바
            mProgressBar = (ProgressBar) findViewById(R.id.exercise_average_prog);
            mProgressBar.setMax(Integer.parseInt(mAverageMax));
            mProgressBar.setProgress(user);
            mProgressBar.setSecondaryProgress(aver);

        } else { //정보가 없을경우 - 팝업팡 표시

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("정보가 잘못되었습니다 ");
            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.create().show();
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
        ((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public String toString() {
        return "ExerciseActivity{" +
                "mUserId='" + mUserId + '\'' +
                '}';
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_exercise_detail:
                intent = new Intent(ExerciseActivity.this , ExerciseDetailActivity.class);
                intent.putExtra("userId", mUserId);
                intent.putExtra("exerciseId", exerciseMain.getExerciseId());

                startActivity(intent);
                break;

            case R.id.btn_exercise_history:
                intent = new Intent(ExerciseActivity.this , ExerciseHistory.class);
                intent.putExtra("userId", mUserId);
                intent.putExtra("exerciseId", exerciseMain.getExerciseId());
                startActivity(intent);
                break;

            case R.id.exercise_date_prev:
                getDate(mUserId , "");
                break;

            case R.id.exercise_date_next:
                getDate(mUserId , "");
                break;
        }
    }

    private void getDate(String userId, String exerciseId){
        //학교 정보 추출
        JSONNetWork_Manager.request_Get_Exercise_Info(userId, exerciseId, this, new NetWork.Call_Back() {
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

                        data = "[" + data + "]";
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
           Intent mExerciseIntent = new Intent();
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
//           mExerciseIntent.putExtra("exercise", object.getString("rangkingExercise")); // 종목랭킹 16.03.23 해당항목삭

//                mExerciseIntent.putExtra("user", object.getString("user")); //사용자 운동량
            mExerciseIntent.putExtra("average", object.getString("calorieAverage")); //평군 운동량
            mExerciseIntent.putExtra("averageMax", object.getString("calorieMax")); //평군 운동량 맥스


            String mExerciseNext = object.optString("exerciseIdNext");  //다음 운동량
            String mExercisePreiv = object.getString("exerciseIdPrev"); //이전 운동량
            Log.e("!!!!", "!!!! intent 운동량 " + mExerciseIntent.getExtras());

            Message msg = new Message();
            msg.what = 0;
            msg.obj = mExerciseIntent;
            mHandler.sendMessage(msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = (Intent) msg.obj;
            intiLayout(intent);
        }
    };
}
