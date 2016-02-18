package kr.co.aura.mtelo.healthcare.addinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.addinfo.exercise.ExerciseActivity;
import kr.co.aura.mtelo.healthcare.addinfo.mentaltest.MentalTestListActivity;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;

/**
 * Created by young-kchoi on 2016. 2. 4..
 */
public class AddInfoActivity extends SherlockActivity implements View.OnClickListener {


    private Context mCon = this;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info);

        Button btn1 = (Button) findViewById(R.id.add_info_btn1);
        Button btn2 = (Button) findViewById(R.id.add_info_btn2);
        Button btn3 = (Button) findViewById(R.id.add_info_btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        init_ACtionBar();
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
                intent = new Intent(AddInfoActivity.this, ExerciseActivity.class);
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("활동량 체크")
                        .putContentId("01040239227"));

                break;

            case R.id.add_info_btn2:    //심리검사
                intent = new Intent(AddInfoActivity.this, MentalTestListActivity.class);
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("심리검사 시작")
                        .putContentType("Video")
                        .putContentId("01040239227"));
                break;

            case R.id.add_info_btn3:
                break;
        }

        if (intent != null) {
            startActivity(intent);
            finish();
        }


    }

}
