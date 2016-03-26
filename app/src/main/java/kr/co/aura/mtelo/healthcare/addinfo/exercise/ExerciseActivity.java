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
import android.widget.Toast;

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

/**
 * Created by young-kchoi on 2016. 2. 11..
 */
public class ExerciseActivity extends SherlockActivity  implements  View.OnClickListener {

    private String TAG = "ExerciseActivity";
    private String mUserId;
    private ImageView mExerciseImg;
    private ProgressBar mProgressBar ;

    private ExerciseMain mExerciseMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_main);

        Intent intent = getIntent();

        ExerciseMain exerciseMain = (ExerciseMain)intent.getSerializableExtra("exercise_main");



        init_ACtionBar();

        intiLayout(exerciseMain);

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



    private void intiLayout(ExerciseMain exerciseMain) {
        Log.e(TAG, "ExerciseMain " + exerciseMain.toString());

        if (exerciseMain != null
                && exerciseMain.getUserId() != null && exerciseMain.getUserId().length() > 0
                && exerciseMain.getExerciseImg() != null && exerciseMain.getExerciseImg().length() > 0
                && exerciseMain.getExerciseName() != null && exerciseMain.getExerciseName().length() > 0
                && exerciseMain.getExerciseDate() != null && exerciseMain.getExerciseDate().length() > 0
            ) {  //정보가 있을경우 - 정상실행

            // Data Init
            mExerciseMain = exerciseMain;
            mUserId = exerciseMain.getUserId();

            TextView tv = (TextView) findViewById(R.id.exercise_date_txt);
            tv.setText(exerciseMain.getExerciseDate());

            tv = (TextView) findViewById(R.id.exercise_left_top_txt);
            tv.setText(exerciseMain.getExerciseName());


            //운동이미지
            mExerciseImg = (ImageView) findViewById(R.id.exercise_left_img);
            Picasso.with(ExerciseActivity.this).load(exerciseMain.getExerciseImg()).fit().into(mExerciseImg);

            //기본운동정보
            tv = (TextView) findViewById(R.id.exercise_right_txt_1);  //칼로리
            tv.setText(exerciseMain.getCalorie() + " Kcal");

            tv = (TextView) findViewById(R.id.exercise_right_txt_2);     //걸음수
            tv.setText(exerciseMain.getStep() + " 보");

            tv = (TextView) findViewById(R.id.exercise_right_txt_3);  //이동거리
            tv.setText(exerciseMain.getDistance() + " km");

            tv = (TextView) findViewById(R.id.exercise_right_txt_4);  //체형
            tv.setText(exerciseMain.getBodyType());


            //등수
            tv = (TextView) findViewById(R.id.exercise_class_number);    //반 등수
            tv.setText(exerciseMain.getRangkingClass() +" 등");

            tv = (TextView) findViewById(R.id.exercise_student_number);    // 학년등수
            tv.setText(exerciseMain.getRangkingGrade() +" 등");


            //칼로리 계산
            tv = (TextView) findViewById(R.id.exercise_average_text);   //사용자 운동량
            tv.setText(exerciseMain.getBodyType() + " " + exerciseMain.getCalorie() + " Kcal"); // 바디타임과 사용자의 운동량을 추가한다


            //평균운동량 계산
            int user = Integer.parseInt(exerciseMain.getCalorie());
            int aver = Integer.parseInt(exerciseMain.getCalorieAverage());
            int result = aver - user;

            tv = (TextView) findViewById(R.id.exercise_average_sub_text);   //평균 운동량
            tv.setText(Math.abs(result) + " Kcal");

            ImageView arrow = (ImageView) findViewById(R.id.exercise_average_sub_img);
            if (user > aver) {
                arrow.setBackgroundResource(R.drawable.arrow_up);
            } else {
                arrow.setBackgroundResource(R.drawable.arrow_down);
            }


            //프로그래스바
            mProgressBar = (ProgressBar) findViewById(R.id.exercise_average_prog);
            mProgressBar.setMax(Integer.parseInt(exerciseMain.getCalorieMax()));
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
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_exercise_detail:
                intent = new Intent(ExerciseActivity.this , ExerciseDetailActivity.class);
                intent.putExtra("userId", mUserId);
                intent.putExtra("exerciseId", mExerciseMain.getExerciseId());

                startActivity(intent);
                break;

            case R.id.btn_exercise_history:
                intent = new Intent(ExerciseActivity.this , ExerciseHistory.class);
                intent.putExtra("userId", mUserId);
                intent.putExtra("exerciseId", mExerciseMain.getExerciseId());
                startActivity(intent);
                break;

            case R.id.exercise_date_prev:
                if(mExerciseMain == null|| mExerciseMain.getExerciseIdPrev().length() == 0 ){
                    Toast.makeText(ExerciseActivity.this, "데이터가 없습니다 ", Toast.LENGTH_SHORT).show();
                }else{
                     getDate(mUserId, mExerciseMain.getExerciseIdPrev()); // 이전 운동ID를 얻어올 방법을 찾을것
                }
                break;

            case R.id.exercise_date_next:
                if(mExerciseMain == null || mExerciseMain.getExerciseIdNext().length() == 0 ){
                    Toast.makeText(ExerciseActivity.this, "데이터가 없습니다 ", Toast.LENGTH_SHORT).show();
                }else{
                    getDate(mUserId , mExerciseMain.getExerciseIdNext()); // 이후 운동ID를 얻어올 방법을 찾을것
                }
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
                        parsingExerciseMain(array);
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

    public void parsingExerciseMain(JSONArray array){
        try {

            if (array.length() == 0) return;

//            MLog.write(Log.ERROR, this.toString(), "array= i " + array.get(0));
            JSONObject object = array.getJSONObject(0);  // JSONObject 추출

            ExerciseMain exerciseInfo = new ExerciseMain();

            exerciseInfo.setUserId(mUserId);
            exerciseInfo.setExerciseId(object.getString("exerciseId"));
            exerciseInfo.setExerciseIdNext(object.optString("exerciseIdNext")); //다음 운동 ID
            exerciseInfo.setExerciseIdPrev(object.optString("exerciseIdPrev")); //이전 운동 ID

            exerciseInfo.setExerciseDate(object.getString("exerciseDate")); //운동날짜
            exerciseInfo.setExerciseName(object.getString("exerciseName")); // 이름
            exerciseInfo.setExerciseImg(object.getString("exerciseImg")); //이미지 경로
            exerciseInfo.setCalorie(object.getString("calorie")); //칼로리
            exerciseInfo.setStep(object.getString("step")); //걸음수
            exerciseInfo.setDistance(object.getString("distance")); //이동거리
            exerciseInfo.setBodyType(object.getString("bodyType")); //체형

            exerciseInfo.setRangkingClass(object.getString("rangkingClass")); // 반랭킹
            exerciseInfo.setRangkingGrade(object.getString("rangkingGrade")); //학년랭킹

            exerciseInfo.setCalorieAverage(object.getString("calorieAverage")); //평군 운동량
            exerciseInfo.setCalorieMax(object.getString("calorieMax")); //평군 운동량 맥스


            Log.e("!!!!", "ExerciseMain : " + exerciseInfo.toString());

            Intent intent = new Intent();

            intent.putExtra("exercise_main", exerciseInfo);

            Message msg = new Message();
            msg.what = 0;
            msg.obj = intent;
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

            ExerciseMain exerciseMain = (ExerciseMain)intent.getSerializableExtra("exercise_main");
            intiLayout(exerciseMain);
        }
    };
}
