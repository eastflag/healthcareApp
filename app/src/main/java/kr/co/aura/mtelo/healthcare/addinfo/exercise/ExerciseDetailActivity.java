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
    private final int SPEED = 1003;
    private int mChartStatus  = CALORIE;

    private LineChart mChart;
    private ArrayList<ChartData> mCheatDatas = new ArrayList<ChartData>();

    private String mCalorie, mStep, mDistance, mSpeed, mBodyType;
    private String mCalorieMax, mStepMax, mDistanceMax, mSpeedMax, mBodyTypeMax;

    private TextView mCalorieText, mStepText, mSpeedText, mBodyTypeText, mDistanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exerics_detail);


        init_ACtionBar();
        getDate();

        Button topBtn1 = (Button) findViewById(R.id.exercise_detail_top_tx1);
        topBtn1.setOnClickListener(this);

        Button topBtn2 = (Button) findViewById(R.id.exercise_detail_top_tx2);
        topBtn2.setOnClickListener(this);

        Button topBtn3 = (Button) findViewById(R.id.exercise_detail_top_tx3);
        topBtn3.setOnClickListener(this);


        //하단 텍스트박스
        mCalorieText  = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx1);
        mStepText     = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx2);
        mDistanceText = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx3);
//        mSpeedText    = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx4);
        mBodyTypeText = (TextView) findViewById(R.id.exercise_detail_bottom_right_tx5);


        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);    // 터치동작 지원X
        mChart.setDescription("");

        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.animateXY(3000, 3000);

        //오른쪽 Y축을 삭제한
        mChart.getAxisRight().setEnabled(false);



        //테스트 코드
        testCOde();

        //X축 표시하기
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyValueFormatter(mCheatDatas));



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
        Intent intent = null;
        switch (v.getId()){

            case R.id.exercise_detail_top_tx1:
            case R.id.exercise_detail_top_tx2:
            case R.id.exercise_detail_top_tx3:
                intent = new Intent(ExerciseDetailActivity.this, ExerciseDetailTabActivity.class);
        }

        startActivity(intent);
    }




    //테스트코드
    public void testCOde(){

        ChartData data1 = new ChartData("2/21", "축구",  "280", "6000", "4.05", "40");
        ChartData data2 = new ChartData("2/28", "농구", "250" ,"7000",  "5.05" , "50");
        ChartData data3 = new ChartData("3/2", "배구", "350","5000","6.05" , "20");
        ChartData data4 = new ChartData("3/10", "축구","260", "4500","3.05", "40");
        mCheatDatas.add(data1);
        mCheatDatas.add(data2);
        mCheatDatas.add(data3);
        mCheatDatas.add(data4);

    }



    private void getDate(){
        JSONNetWork_Manager.request_Get_Exercise_Detail_Info("123", "", this, new NetWork.Call_Back() {
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
                        data = data.substring(1, data.length());
                        data = data.substring(0, data.length() - 1);
                        Log.e("!!!!", "!!! request_Get_Exercise_Detail_Info()\n " + data);

                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.optJSONArray("cart");

                        mCalorie  = object.optString("calorie");
                        mStep     = object.optString("step");
                        mDistance = object.optString("distance");
                        mBodyType = object.optString("bodyType");

                        mCalorieMax  = object.optString("calorieMax");
                        mStepMax     = object.optString("stepMax");
                        mDistanceMax = object.optString("distanceMax");
                        mBodyTypeMax = object.optString("bodyTypeMax");
                        mSpeedMax    = object.optString("speedMax");

                        buildData(array);
                        setupText();
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
                        object.getString("exercise"),
                        object.getString("calorie"),
                        object.getString("step"),
                        object.getString("distance")
                );


                mCheatDatas.add(data);

            }

            setupChart();
            //데이터 설정
            setData(mCheatDatas.size() , mCheatDatas, mChartStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //데이터를 차트에 추가
    private void setData(int count, ArrayList<ChartData> list, int status) {
        Log.e("!!!!", "!!! setData  status "+ status +", mChartStatus "+ mChartStatus);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        ArrayList<String> xVals = new ArrayList<String>();

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "카운트 " + i);

            ChartData data = list.get(i);

            switch (status) {
                case CALORIE:
                    yVals.add(new Entry(Float.parseFloat(data.calorie), i));
                    break;

                case STEP:
                    yVals.add(new Entry(Float.parseFloat(data.step), i));
                    break;

                case DISTANCE:
                    yVals.add(new Entry(Float.parseFloat(data.distance), i));

                    break;

//                case SPEED:
//                    yVals.add(new Entry(Float.parseFloat(data.speed), i));
//                    break;
            }

        }



            // create a dataset and give it a type
            LineDataSet set1 = new LineDataSet(yVals, "이동거리");
            // set1.setFillAlpha(110);
            // set1.setFillColor(Color.RED);
            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(12f);


        // Y축 최대값을 설정한다
        YAxis leftAxis = mChart.getAxisLeft();
        if (status == CALORIE) {
            leftAxis.setAxisMaxValue(Float.parseFloat(mCalorieMax));
            set1.setLabel("킬로리");
        } else if (status == STEP) {
            leftAxis.setAxisMaxValue(Float.parseFloat(mStepMax));
            set1.setLabel("걸음수");
        } else if (status == DISTANCE) {
            set1.setLabel("걸음수");
            leftAxis.setAxisMaxValue(Float.parseFloat(mDistanceMax));
            set1.setLabel("이동거리");
//        } else if (status == SPEED) {
//            leftAxis.setAxisMaxValue(Float.parseFloat(mSpeedMax));
//            set1.setLabel("속도");
        }



        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

        mChartStatus = status;
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
        Log.e("!!!!", "!!! onChartSingleTapped "+ mChartStatus);
        if(mChartStatus == CALORIE)         setData(mCheatDatas.size() , mCheatDatas , STEP);
        else if(mChartStatus == STEP)       setData(mCheatDatas.size() , mCheatDatas , DISTANCE);
        else if(mChartStatus == DISTANCE)   setData(mCheatDatas.size() , mCheatDatas , CALORIE);
//        else if(mChartStatus == SPEED)      setData(mCheatDatas.size() , mCheatDatas , CALORIE);

        mChart.invalidate();
        mChart.animateXY(3000, 3000);

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

