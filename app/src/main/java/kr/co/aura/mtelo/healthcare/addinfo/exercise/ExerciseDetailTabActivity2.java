package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import org.json.JSONObject;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;

/**
 * Created by harks on 2016-03-26.
 */
public class ExerciseDetailTabActivity2 extends SherlockActivity implements View.OnClickListener {

    private static final String TAG = "tab_exercize";

    private static final String WEIGHT_SMALL = "저체상";
    private static final String WEIGHT_NORMAL = "정상";
    private static final String WEIGHT_BIG = "과체중";
    private static final String WEIGHT_OBESITY = "비만";
    private static final String WEIGHT_OBESITY_BIG = "중도비만";
    private static final String WEIGHT_OBESITY_BIGGER = "고도비만";

    public static final String UNIT_CALORIE = "calorie";
    public static final String UNIT_STEP = "step";
    public static final String UNIT_DISTANCE = "distance";

    private static final String GROUP_TYPE_CLASS = "class";
    private static final String GROUP_TYPE_GRADE = "grade";
    private static final String GROUP_TYPE_ALL = "all";


    private String mUserId;
    private String mExerciseId;
    private String mAverageType;
    private String mAvrGroupType;


    ExerciseTab exerciseTab = null;

    String getUnit(String unitType){
        String unit = "";
        if(unitType.equals(UNIT_CALORIE)){
            unit = "Kcal";
        }else if(unitType.equals(UNIT_STEP)){
            unit = "보";
        }else if(unitType.equals(UNIT_DISTANCE)){
            unit = "Km";
        }

        return  unit;
    }


    String getAvgGroupType(String groupType){
        String type = "";
        if(groupType.equals(GROUP_TYPE_CLASS)){
            type = "반";
        }else if(groupType.equals(GROUP_TYPE_GRADE)){
            type = "학교";
        }else if(groupType.equals(GROUP_TYPE_ALL)){
            type = "전국";
        }

        return  type;
    }


    void setUIContents(ExerciseTab exerciseTab){

        String unit = getUnit(exerciseTab.getValueType());

        TextView tv = null;
        ProgressBar progressBarAvg = null;
        ProgressBar progressBarUser = null;
        LinearLayout linearLayout = null;
        // Title
        tv = (TextView) findViewById(R.id.exercise_tab2_title);

        String s = String.format("나의 체형 : %s (%s %s)"
                , exerciseTab.getBodyType()
                , exerciseTab.getUserValue()
                , unit);
        tv.setText(s);

        // Avg Title 반 평균(35명) 254Kcal
        String avgGroupType = getAvgGroupType(mAvrGroupType);
        tv = (TextView) findViewById(R.id.exercise_tab2_avg_title);
        tv.setText(String.format("%s 평균 (%s 명) %s %s"
                , avgGroupType
                , exerciseTab.getAverageCnt()
                , exerciseTab.getAverageValue()
                , unit));

        // 개별 영역 구성


        // 저체중 구성
        linearLayout = (LinearLayout) findViewById(R.id.layout_type_1);
        tv = (TextView) findViewById(R.id.txt_avg_1);
        progressBarAvg = (ProgressBar) findViewById(R.id.progress_avg_1);
        progressBarUser = (ProgressBar) findViewById(R.id.progress_user_1);

        setProgressItem(linearLayout, tv,
                progressBarAvg, progressBarUser,
                exerciseTab.getBodyType(),
                exerciseTab.getBodyType1(), exerciseTab.getBodyType1Max(),
                exerciseTab.getUserValue(), unit, WEIGHT_SMALL, exerciseTab.getValueType());

        // 정상
        linearLayout = (LinearLayout) findViewById(R.id.layout_type_2);
        tv = (TextView) findViewById(R.id.txt_avg_2);
        progressBarAvg = (ProgressBar) findViewById(R.id.progress_avg_2);
        progressBarUser = (ProgressBar) findViewById(R.id.progress_user_2);

        setProgressItem(linearLayout, tv,
                progressBarAvg, progressBarUser,
                exerciseTab.getBodyType(),
                exerciseTab.getBodyType2(), exerciseTab.getBodyType2Max(),
                exerciseTab.getUserValue(), unit, WEIGHT_NORMAL, exerciseTab.getValueType());

        // 과제중
        linearLayout = (LinearLayout) findViewById(R.id.layout_type_3);
        tv = (TextView) findViewById(R.id.txt_avg_3);
        progressBarAvg = (ProgressBar) findViewById(R.id.progress_avg_3);
        progressBarUser = (ProgressBar) findViewById(R.id.progress_user_3);

        setProgressItem(linearLayout, tv,
                progressBarAvg, progressBarUser,
                exerciseTab.getBodyType(),
                exerciseTab.getBodyType3(), exerciseTab.getBodyType3Max(),
                exerciseTab.getUserValue(), unit, WEIGHT_BIG, exerciseTab.getValueType());

        // 비만
        linearLayout = (LinearLayout) findViewById(R.id.layout_type_4);
        tv = (TextView) findViewById(R.id.txt_avg_4);
        progressBarAvg = (ProgressBar) findViewById(R.id.progress_avg_4);
        progressBarUser = (ProgressBar) findViewById(R.id.progress_user_4);

        setProgressItem(linearLayout, tv,
                progressBarAvg, progressBarUser,
                exerciseTab.getBodyType(),
                exerciseTab.getBodyType4(), exerciseTab.getBodyType4Max(),
                exerciseTab.getUserValue(), unit, WEIGHT_OBESITY, exerciseTab.getValueType());

        // 중도비만
        linearLayout = (LinearLayout) findViewById(R.id.layout_type_5);
        tv = (TextView) findViewById(R.id.txt_avg_5);
        progressBarAvg = (ProgressBar) findViewById(R.id.progress_avg_5);
        progressBarUser = (ProgressBar) findViewById(R.id.progress_user_5);

        setProgressItem(linearLayout, tv,
                progressBarAvg, progressBarUser,
                exerciseTab.getBodyType(),
                exerciseTab.getBodyType5(), exerciseTab.getBodyType5Max(),
                exerciseTab.getUserValue(), unit, WEIGHT_OBESITY_BIG, exerciseTab.getValueType());

        // 고도비만
        linearLayout = (LinearLayout) findViewById(R.id.layout_type_6);
        tv = (TextView) findViewById(R.id.txt_avg_6);
        progressBarAvg = (ProgressBar) findViewById(R.id.progress_avg_6);
        progressBarUser = (ProgressBar) findViewById(R.id.progress_user_6);

        setProgressItem(linearLayout, tv,
                progressBarAvg, progressBarUser,
                exerciseTab.getBodyType(),
                exerciseTab.getBodyType6(), exerciseTab.getBodyType6Max(),
                exerciseTab.getUserValue(), unit, WEIGHT_OBESITY_BIGGER, exerciseTab.getValueType());

    }

    void setProgressItem(LinearLayout linearLayout, TextView tv,
                         ProgressBar progressBarAvg,
                         ProgressBar progressBarUser,
                         String bodyType,
                         String avgVlaue, String avgMax,
                         String userVlaue,
                         String unit, String weghtType,
                         String valueType){

        int progressValue = 0;
        int progressMax = 0;
        int progressUserValue = 0;

        linearLayout.setBackgroundResource(R.color.exercise_tab_item_normal);

        tv.setText(String.format("%s %s"
                , avgVlaue
                , unit));

        if(UNIT_DISTANCE.equals(valueType)){

            float fProgressMax = Float.parseFloat(avgMax);
            float fProgressValue = Float.parseFloat(avgVlaue);
            float fProgressUserValue = Float.parseFloat(userVlaue);

            progressMax = (int)(fProgressMax * 10);
            progressValue = (int)(fProgressValue * 10);
            progressUserValue = (int)(fProgressUserValue * 10);

        } else{
            progressMax = Integer.parseInt(avgMax);
            progressValue = Integer.parseInt(avgVlaue);
            progressUserValue = Integer.parseInt(userVlaue);
        }


        progressBarUser.setVisibility(View.GONE);

        progressBarAvg.setMax(progressMax);
        progressBarAvg.setProgress(progressValue);

        if(bodyType.equals(weghtType)){

            linearLayout.setBackgroundResource(R.color.exercise_tab_item_select);

            // 사용자 정보 구성
            progressBarUser.setMax(progressMax);
            progressBarUser.setProgress(progressUserValue);
            progressBarUser.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exerics_detail_tab2);

        init_ACtionBar();

        exerciseTab = new ExerciseTab();

        mAvrGroupType = GROUP_TYPE_CLASS;

        Intent intent = getIntent();
        mAverageType = intent.getStringExtra("averageType");
        mUserId = intent.getStringExtra("userId");
        mExerciseId = intent.getStringExtra("exerciseId");

        exerciseTab.setUserId(mUserId);
        exerciseTab.setAverageType(mAvrGroupType);
        exerciseTab.setValueType(mAverageType);


        // 버튼 이벤트 구성
        Button btn = (Button) findViewById(R.id.exercise_tab2_btn_class);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.exercise_tab2_btn_grade);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.exercise_tab2_btn_all);
        btn.setOnClickListener(this);


        ((Button) findViewById(R.id.exercise_tab2_btn_class)).performClick();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.exercise_tab2_btn_class:
                exerciseTab.setAverageType(GROUP_TYPE_CLASS);
                getDate(exerciseTab.getUserId(), mExerciseId, exerciseTab.getValueType(), exerciseTab.getAverageType());
                break;

            case R.id.exercise_tab2_btn_grade:
                exerciseTab.setAverageType(GROUP_TYPE_GRADE);
                getDate(exerciseTab.getUserId(), mExerciseId, exerciseTab.getValueType(), exerciseTab.getAverageType());
                break;

            case R.id.exercise_tab2_btn_all:
                exerciseTab.setAverageType(GROUP_TYPE_ALL);
                getDate(exerciseTab.getUserId(), mExerciseId, exerciseTab.getValueType(), exerciseTab.getAverageType());
                break;

        }
    }

    private void getDate(String userId, String exerciseId, String averageType, String groupType) {
        JSONNetWork_Manager.request_Get_User_Exercise_Tab(userId, exerciseId, averageType, groupType, this, new NetWork.Call_Back() {
            @Override
            public void onError(String error) {
            }

            @Override
            public void onGetResponsData(byte[] data) {
            }

            @Override
            public void onGetResponsString(String data) {
                try {
                    if (data != null) {
                        if (data.startsWith("[")) {
                            data = data.substring(1, data.length());
                            data = data.substring(0, data.length() - 1);
                        }
                        Log.e(TAG, data);

                        JSONObject object = new JSONObject(data);


                        exerciseTab.setUserValue(object.optString("user"));
                        exerciseTab.setBodyType(object.optString("bodyType"));

                        exerciseTab.setAverageValue(object.optString("all"));
                        exerciseTab.setAverageCnt(object.optString("averageCnt"));

                        exerciseTab.setBodyType1(object.optString("bodyType1"));
                        exerciseTab.setBodyType1Max(object.optString("bodyType1Max"));
                        exerciseTab.setBodyType2(object.optString("bodyType2"));
                        exerciseTab.setBodyType2Max(object.optString("bodyType2Max"));
                        exerciseTab.setBodyType3(object.optString("bodyType3"));
                        exerciseTab.setBodyType3Max(object.optString("bodyType3Max"));
                        exerciseTab.setBodyType4(object.optString("bodyType4"));
                        exerciseTab.setBodyType4Max(object.optString("bodyType4Max"));
                        exerciseTab.setBodyType5(object.optString("bodyType5"));
                        exerciseTab.setBodyType5Max(object.optString("bodyType5Max"));
                        exerciseTab.setBodyType6(object.optString("bodyType6"));
                        exerciseTab.setBodyType6Max(object.optString("bodyType6Max"));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setUIContents(exerciseTab);
                            }
                        });

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
}
