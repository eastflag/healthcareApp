package kr.co.aura.mtelo.healthcare.addinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.addinfo.mentaltest.VideoTest;

/**
 * Created by young-kchoi on 2016. 2. 4..
 */
public class AddInfoActivity extends SherlockActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info);

        Button btn1 = (Button)findViewById(R.id.add_info_btn1);
        Button btn2 = (Button)findViewById(R.id.add_info_btn2);
        Button btn3 = (Button)findViewById(R.id.add_info_btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.add_info_btn1:  //활동량 조회
                break;

            case R.id.add_info_btn2:    //심리검사
                intent = new Intent(AddInfoActivity.this, VideoTest.class);
                break;

            case R.id.add_info_btn3:
                break;
        }

        if(intent != null)
            startActivity(intent);
    }
}
