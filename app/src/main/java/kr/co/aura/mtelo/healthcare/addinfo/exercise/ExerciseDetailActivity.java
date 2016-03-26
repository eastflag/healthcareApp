package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

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
 * Created by young-kchoi on 2016. 2. 24..
 */
public class ExerciseDetailActivity extends SherlockActivity implements View.OnClickListener, OnChartGestureListener, OnChartValueSelectedListener {

    private final int CALORIE = 1000;
    private final int STEP = 1001;
    private final int DISTANCE = 1002;
    private int mChartStatus  = CALORIE;

    private String mUserId;
    private String mExerciseId;

    private LineChart mChart;
    private ArrayList<ChartData> mCheatDatas = new ArrayList<ChartData>();

    private String mCalorie, mStep, mDistance, mSpeed, mBodyType;
    private String mCalorieMax, mStepMax, mDistanceMax, mSpeedMax, mBodyTypeMax;

    private TextView mCalorieText, mStepText, mSpeedText, mBodyTypeText, mDistanceText;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exerics_detail);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userId");
        mExerciseId = intent.getStringExtra("exerciseId");

        init_ACtionBar();

        Button topBtn1 = (Button) findViewById(R.id.exercise_detail_top_tx1);
        topBtn1.setOnClickListener(this);

        Button topBtn2 = (Button) findViewById(R.id.exercise_detail_top_tx2);
        topBtn2.setOnClickListener(this);

        Button topBtn3 = (Button) findViewById(R.id.exercise_detail_top_tx3);
        topBtn3.setOnClickListener(this);

        TextView tv = (TextView) findViewById(R.id.chart1_regend);
        tv.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.chart2_regend);
        tv.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.chart3_regend);
        tv.setOnClickListener(this);


        mTitle  = (TextView) findViewById(R.id.exercise_detail_title);

        //하단 텍스트박스
        mCalorieText  = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx1);
        mStepText     = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx2);
        mDistanceText = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx3);
        mBodyTypeText = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx5);


        mChart = (LineChart) findViewById(R.id.chart1);

        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(false);    // 터치동작 지원X
        mChart.setDescription("");

        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(false);
        mChart.animateXY(3000, 3000);

        //오른쪽 Y축을 삭제한
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        mChart.getXAxis().setTextSize(14f);
        mChart.getXAxis().setAvoidFirstLastClipping(true);

        mChart.setExtraLeftOffset(15);
        mChart.setExtraRightOffset(40);
        //X축 표시하기
        //mChart.getXAxis().setValueFormatter(new MyValueFormatter(mCheatDatas));


        //데이터 획득
        getDate(mUserId, mExerciseId);

    }

    private void setupChart() {
        //Y축 설정
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit linesto avoid overlapping lines
        leftAxis.setAxisMaxValue(Float.parseFloat(mStepMax));
        leftAxis.setAxisMinValue(0f);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setXOffset(20f);
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
        Intent intent = new Intent(ExerciseDetailActivity.this, ExerciseDetailTabActivity.class);
        intent.putExtra("userId", mUserId );
        intent.putExtra("exerciseId", mExerciseId );

        switch (v.getId()) {

            case R.id.exercise_detail_top_tx1:
                intent.putExtra("averageType", "calorie");
                startActivity(intent);
                break;
            case R.id.exercise_detail_top_tx2:
                intent.putExtra("averageType", "step");
                startActivity(intent);
                break;
            case R.id.exercise_detail_top_tx3:
                intent.putExtra("averageType", "distance");
                startActivity(intent);
                break;

            case R.id.chart1_regend:
                DrawChart(CALORIE);
                break;
            case R.id.chart2_regend:
                DrawChart(STEP);
                break;
            case R.id.chart3_regend:
                DrawChart(DISTANCE);
                break;



        }
    }

    private void DrawChart(int type){

        if(mCheatDatas.size() <= 0) return;

        if(mChart.getLineData() != null){
            mChart.getLineData().clearValues();
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        ArrayList<String> xVals = new ArrayList<String>();

        ArrayList<Entry> listEntry = new ArrayList<Entry>();



        for (int i = 0; i < mCheatDatas.size(); i++) {

            ChartData item =  mCheatDatas.get(i);

            //xVals.add(String.format("%s\n(%s)", item.getDate(), item.getExercise()));
            xVals.add(item.getDate());

            String entryData = "0";
            if(type == CALORIE){
                entryData = item.getCalorie();

            } else if (type == STEP) {
                entryData = item.getStep();

            } else if(type == DISTANCE) {
                entryData = item.getDistance();

            }

            listEntry.add(new Entry(Float.parseFloat(entryData), i));
        }

        // create a dataset and give it a type
        LineDataSet lineDataSet = new LineDataSet(listEntry, "");

        YAxis leftAxis = mChart.getAxisLeft();
        int color = Color.WHITE;
        if(type == CALORIE){
            color = Color.RED;
            leftAxis.setAxisMaxValue(Float.parseFloat(mCalorieMax));

        } else if (type == STEP) {
            color = Color.GREEN;
            leftAxis.setAxisMaxValue(Float.parseFloat(mStepMax));

        } else if(type == DISTANCE) {
            color = Color.BLUE;
            leftAxis.setAxisMaxValue(Float.parseFloat(mDistanceMax));
        }

        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setCircleColorHole(color);
        lineDataSet.setLineWidth(4f);
        lineDataSet.setValueTextSize(14f);
        //lineDataSet.setDrawCubic(true);

        dataSets.add(lineDataSet);

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        mChart.setData(data);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }


    private void getDate(String userId, String exerciseId){
        JSONNetWork_Manager.request_Get_Exercise_Detail_Info(userId, exerciseId, this, new NetWork.Call_Back() {
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
                        Log.e("!!!!", "!!! request_Get_Exercise_Detail_Info()\n " + data);

                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.optJSONArray("chart");

                        mCalorie = object.optString("calorie");
                        mStep = object.optString("step");
                        mDistance = object.optString("distance");
                        mBodyType = object.optString("bodyType");

                        mCalorieMax = object.optString("calorieMax");
                        mStepMax = object.optString("stepMax");
                        mDistanceMax = object.optString("distanceMax");
                        mBodyTypeMax = object.optString("bodyTypeMax");
                        mSpeedMax = object.optString("speedMax");

                        buildData(array);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupText();
                            }
                        });

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


    //하단 텍스트상자를 체워넣는다
    private void setupText() {

        mTitle.setText(String.format("체형 : %s : 평균( %s Kcal)", mBodyType, mCalorie));

        mCalorieText.setText(mCalorie +" Kcal");
        mStepText.setText(mStep +" 보");
        mDistanceText.setText(mDistance + " Km");
        mBodyTypeText.setText(mBodyType);
    }

    //JSON데이터를 가공
    public void buildData(JSONArray array){
        try {
            if (array.length() == 0) return;
            for (int i = 0; i < array.length(); i++) {
                MLog.write(Log.ERROR, this.toString(), "array= i " + array.get(i));
                JSONObject object = array.getJSONObject(i);  // JSONObject 추출

                ChartData data = new ChartData(
                        object.getString("date"),
                        object.getString("exerciseName"),
                        object.getString("calorie"),
                        object.getString("step"),
                        object.getString("distance")
                );


                mCheatDatas.add(data);
            }

            DrawChart(CALORIE);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 차크 메소드들
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.e("!!!!", "!!! onChartDoubleTapped "+ this.getPackageName());
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
//        mChart.invalidate();
//        mChart.animateXY(3000, 3000);

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
    }


    class ChartData {
        private String date;
        private String exercise;
        private String calorie;
        private String step;
        private String distance;
        private String speed;

        public ChartData(String date, String exercise, String calorie, String step, String distance, String speed) {
            this.date = date;
            this.exercise = exercise;
            this.calorie = calorie;
            this.step = step;
            this.distance = distance;
            this.speed = speed;
        }

        public ChartData(String date, String exercise, String calorie, String step, String distance) {
            this.date = date;
            this.exercise = exercise;
            this.calorie = calorie;
            this.step = step;
            this.distance = distance;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getExercise() {
            return exercise;
        }

        public void setExercise(String exercise) {
            this.exercise = exercise;
        }

        public String getCalorie() {
            return calorie;
        }

        public void setCalorie(String calorie) {
            this.calorie = calorie;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        @Override
        public String toString() {
            return "ChartData{" +
                    "date='" + date + '\'' +
                    ", exercise='" + exercise + '\'' +
                    ", calorie='" + calorie + '\'' +
                    ", step='" + step + '\'' +
                    ", distance='" + distance + '\'' +
                    ", speed='" + speed + '\'' +
                    '}';
        }
    }


    public class MyValueFormatter implements XAxisValueFormatter{
        private ArrayList<ChartData> xDate = new ArrayList<ChartData>();

        public MyValueFormatter(ArrayList<ChartData> xDate) {
            this.xDate = xDate;
        }

        @Override
        public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
            ChartData data = xDate.get(index);
            return data.date;
        }
    }
}
