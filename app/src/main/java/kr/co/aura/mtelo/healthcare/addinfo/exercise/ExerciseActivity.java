package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.squareup.picasso.Picasso;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;

/**
 * Created by young-kchoi on 2016. 2. 11..
 */
public class ExerciseActivity extends SherlockActivity  implements  View.OnClickListener {

    private String mDate, mName, mImg, mCalorie, mStep, mDistance, mBodyType, mClass, mGrade, mExetices,  mAverage, mAverageMax;
    private TextView mExerciseDate , mExerciseName,  mExerciseCalorie, mExerciseStep, mExerciseDistance,
            mExerciseBodyType, mExerciseClass, mExerciseGrade, mExercicesEntries, mExerciseUser, mExerciseAverage;
    private ImageView mExerciseImg, mExerciseAverImg;
    private ProgressBar mProgressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_main);

        Intent intent = getIntent();

        init_ACtionBar();

        intiLayout(intent);  // 사용중지

        Log.e("!!!!", "!!!!! intent\n " + intent.getExtras());

        ImageButton btnExeriseDetail = (ImageButton) findViewById(R.id.btn_exercise_detail);
        btnExeriseDetail.setOnClickListener(this);

        ImageButton btnExeriseHistory = (ImageButton) findViewById(R.id.btn_exercise_history);
        btnExeriseHistory.setOnClickListener(this);


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

//        mUser = intent.getStringExtra("user"); //사용자 운동량 , 칼로리로 대체
        mAverage     = intent.getStringExtra("average"); //평군 운동량
        mAverageMax  = intent.getStringExtra("averageMax"); //평군 운동량 맥스

        // 날짜
        mExerciseDate = (TextView) findViewById(R.id.exercise_date_txt);
        mExerciseDate.setText(mDate);


        //운동종목
        mExerciseName = (TextView) findViewById(R.id.exercise_left_top_txt);
        mExerciseName.setText(mName);


        //운동이미지
        mExerciseImg = (ImageView) findViewById(R.id.exercise_left_img);
        Picasso.with(ExerciseActivity.this).load(mImg).into(mExerciseImg);

        //기본운동정보
        mExerciseCalorie = (TextView) findViewById(R.id.exercise_right_txt_1);  //칼로리
        mExerciseCalorie.setText(mCalorie +" Kcal");

        mExerciseStep = (TextView) findViewById(R.id.exercise_right_txt_2);     //걸음수
        mExerciseStep.setText(mStep+" 보");

        mExerciseDistance = (TextView) findViewById(R.id.exercise_right_txt_3);  //이동거리
        mExerciseDistance.setText(mDistance +" km");

        mExerciseBodyType = (TextView) findViewById(R.id.exercise_right_txt_4);  //체형
        mExerciseBodyType.setText(mBodyType);



        //등수
        mExerciseClass = (TextView) findViewById(R.id.exercise_class_number);    //반 등수
        mExerciseClass.setText(mClass);

        mExerciseGrade = (TextView) findViewById(R.id.exercise_student_number);    // 학년등수
        mExerciseGrade.setText(mGrade);

        mExercicesEntries = (TextView) findViewById(R.id.exercise_entries_number); //종목등수
        mExercicesEntries.setText(mExetices);


        //칼로리 계산
        mExerciseUser  = (TextView) findViewById(R.id.exercise_average_text);   //사용자 운동량
        mExerciseUser.setText(mBodyType +" - "+mCalorie + " Kcal"); // 바디타임과 사용자의 운동량을 추가한다


        //평균운동량 계산
        int user = Integer.parseInt(mCalorie);
        int aver = Integer.parseInt(mAverage);
        int result = aver - user;

        mExerciseAverage = (TextView) findViewById(R.id.exercise_average_sub_text);   //평균 운동량
        mExerciseAverage.setText(result +" Kcal" );

        mExerciseAverImg = (ImageView) findViewById(R.id.exercise_average_sub_img);
        if(user > aver){
            mExerciseAverImg.setBackgroundResource(R.drawable.arrow_up
            );
        }else{
            mExerciseAverImg.setBackgroundResource(R.drawable.arrow_down);  //up이미지가 필요하다
        }



        //프로그래스바
        mProgressBar = (ProgressBar) findViewById(R.id.exercise_average_prog);
        mProgressBar.setMax(Integer.parseInt(mAverageMax) );
        mProgressBar.setProgress(user);
        mProgressBar.setSecondaryProgress(aver);

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

                break;
            case R.id.btn_exercise_history:
                intent = new Intent(ExerciseActivity.this , ExerciseHistory.class);
                break;

        }
        startActivity(intent);
    }
}
