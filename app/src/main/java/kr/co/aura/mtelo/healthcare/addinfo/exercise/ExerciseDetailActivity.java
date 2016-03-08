package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

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
import kr.co.aura.mtelo.healthcare.util.MLog;

/**
 * Created by young-kchoi on 2016. 2. 24..
 */
public class ExerciseDetailActivity extends Activity implements View.OnClickListener, OnChartGestureListener, OnChartValueSelectedListener {

    private static final int CALORIE = 1000;
    private static final int STEP = 1001;
    private static final int DISTANCE = 1002;
    private LineChart mChart;
    private ArrayList<ChartData> mCheatDatas = new ArrayList<ChartData>();

    private String mCalorie, mStep, mDistance, mSpeed, mBodyType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exerics_detail);


        Button topBtn1 = (Button) findViewById(R.id.exercise_detail_top_tx1);
        topBtn1.setOnClickListener(this);

        Button topBtn2 = (Button) findViewById(R.id.exercise_detail_top_tx2);
        topBtn2.setOnClickListener(this);

        Button topBtn3 = (Button) findViewById(R.id.exercise_detail_top_tx3);
        topBtn3.setOnClickListener(this);


        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);    // 터치동작 지원X
        mChart.setDescription("");

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.animateXY(3000, 3000);

        //오른쪽 Y축을 삭제한
        mChart.getAxisRight().setEnabled(false);

        //Y축 설정
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaxValue(7f);
        leftAxis.setAxisMinValue(0f);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setXOffset(20f);



        //테스트 코드
        testCOde();

        //X축 표시하기
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyValueFormatter(mCheatDatas));

        //데이터 설정
        setData(mCheatDatas.size() , mCheatDatas);
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

        ChartData data1 = new ChartData("2/21", "축구",  "280", "600", "4.05");
        ChartData data2 = new ChartData("2/28", "농구", "250" ,"700",  "5.05");
        ChartData data3 = new ChartData("3/2", "배구", "350","500","6.05");
        ChartData data4 = new ChartData("3/10", "축구","260", "450","3.05");
        mCheatDatas.add(data1);
        mCheatDatas.add(data2);
        mCheatDatas.add(data3);
        mCheatDatas.add(data4);

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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //데이터를 차트에 추가
    private void setData(int count, ArrayList<ChartData> list) {

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < count ; i++ ){
            ChartData data=  list.get(i);

            yVals.add(new Entry(Float.parseFloat(data.distance ), i));


            // create a dataset and give it a type
            LineDataSet set1 = new LineDataSet(yVals, data.date);
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

            dataSets.add(set1); // add the datasets

        }



        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);
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

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {


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
        Log.e("!!!!", "!!! event "+ this.getPackageName());
    }


    class ChartData {
        private String date, exercise, calorie, step, distance;

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

