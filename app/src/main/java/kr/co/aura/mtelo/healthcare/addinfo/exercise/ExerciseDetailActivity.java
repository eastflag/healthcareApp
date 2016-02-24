package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kr.co.aura.mtelo.healthcare.R;

/**
 * Created by young-kchoi on 2016. 2. 24..
 */
public class ExerciseDetailActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exerics_detail);

        Button topBtn1 = (Button) findViewById(R.id.exercise_detail_top_tx1);
        topBtn1.setOnClickListener(this);

        Button topBtn2 = (Button) findViewById(R.id.exercise_detail_top_tx2);
        topBtn1.setOnClickListener(this);

        Button topBtn3 = (Button) findViewById(R.id.exercise_detail_top_tx3);
        topBtn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){

            case R.id.exercise_detail_top_tx1:
                intent = new Intent(ExerciseDetailActivity.this, ExerciseDetailTabActivity.class);
                break;

            case R.id.exercise_detail_top_tx2:
                break;

            case R.id.exercise_detail_top_tx3:
                break;
        }

        startActivity(intent);
    }



}
