package kr.co.aura.mtelo.healthcare.mentaltest;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;

import kr.co.aura.mtelo.healthcare.R;

public class VideoTest extends Activity implements MediaPlayer.OnPreparedListener ,MediaPlayer.OnCompletionListener, View.OnClickListener{


    private VideoView mVideoView;
    private LinearLayout mImgLayout ;
    private LinearLayout mImgBtn1 , mImgBtn2, mImgBtn3, mImgBtn4;
    private Button mCheckBtn1, mCheckBtn2, mCheckBtn3, mCheckBtn4, mReplayBtn, mOksBtn;

    private final static int POOLING_INTERVAL_MS = 100;
    private Timer timer;
    private boolean isActive;

    private final String INTRO_VIDEO ="http://210.127.55.205/psychology_contents/sample/an/AN_IN.mp4";
    private final String OUTRO_VIDEO ="http://210.127.55.205/psychology_contents/sample/an/AN_OUT.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);

        videoInit();

        layoutInit();
    }


    //16.02.01 레이아웃구성
    private void layoutInit() {
        mImgLayout = (LinearLayout)findViewById(R.id.btnLayout);
        mImgBtn1 = (LinearLayout)findViewById(R.id.imgbtn1);
        mImgBtn2 = (LinearLayout)findViewById(R.id.imgbtn2);
        mImgBtn3 = (LinearLayout)findViewById(R.id.imgbtn3);
        mImgBtn4 = (LinearLayout)findViewById(R.id.imgbtn4);
        mImgBtn1.setOnClickListener(this);
        mImgBtn2.setOnClickListener(this);
        mImgBtn3.setOnClickListener(this);
        mImgBtn4.setOnClickListener(this);
        mImgLayout.bringToFront();


        mCheckBtn1 = (Button)mImgBtn1.findViewById(R.id.dubleBtn);
        mCheckBtn2 = (Button)mImgBtn2.findViewById(R.id.dubleBtn);
        mCheckBtn3 = (Button)mImgBtn3.findViewById(R.id.dubleBtn);
        mCheckBtn4 = (Button)mImgBtn4.findViewById(R.id.dubleBtn);

        mCheckBtn1.setOnClickListener(this);
        mCheckBtn2.setOnClickListener(this);
        mCheckBtn3.setOnClickListener(this);
        mCheckBtn4.setOnClickListener(this);


        mReplayBtn = (Button) findViewById(R.id.replay_btn);
        mOksBtn    = (Button) findViewById(R.id.ok_btn);

        mReplayBtn.setOnClickListener(this);
        mOksBtn.setOnClickListener(this);
    }


    //16.02.01 비디오뷰 구성
    private void videoInit() {
        Intent in = getIntent();
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.healthcare_video);
        Uri uri = Uri.parse(INTRO_VIDEO);
        mVideoView = (VideoView) findViewById(R.id.myVideo);
        mVideoView.requestFocus();
        mVideoView.setVideoURI(uri);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.setSystemUiVisibility(VideoView.SYSTEM_UI_FLAG_FULLSCREEN);
        }

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dubleBtn) {

        } else {
            switch (v.getId()) {
                case R.id.imgbtn1:
                    checkChkButton(v);
                    break;

                case R.id.imgbtn2:
                    checkChkButton(v);
                    break;
                case R.id.imgbtn3:
                    checkChkButton(v);
                    break;
                case R.id.imgbtn4:
                    checkChkButton(v);
                    break;

                case R.id.replay_btn:
                    mImgLayout.setVisibility(View.GONE);
                    mVideoView.start();
                    break;
                case R.id.ok_btn:
                    Toast.makeText(VideoTest.this, "문항을 선택하였습니다 ", Toast.LENGTH_LONG).show();
                    if(mVideoView == null)
                        videoInit();

                    mVideoView.setVideoURI(Uri.parse(OUTRO_VIDEO));
                    mVideoView.start();

                    mImgLayout.setVisibility(View.GONE);
                    break;

            }
        }
            Log.e("", "!!!!!!! 더블버튼 태그" + v.getId());
    }


    //16.02.01 더블버튼 체크로직
    private void checkChkButton(View v){
        mCheckBtn1.setBackgroundResource(R.drawable.dublebtn_bg);
        mCheckBtn2.setBackgroundResource(R.drawable.dublebtn_bg);
        mCheckBtn3.setBackgroundResource(R.drawable.dublebtn_bg);
        mCheckBtn4.setBackgroundResource(R.drawable.dublebtn_bg);

//        View view = (View) v.getParent();
        v.findViewById(R.id.dubleBtn).setBackgroundResource(R.drawable.dublebtn_bg_click);
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        isActive = true;
        mp.start();
        Log.e("", "@@@@@  onPrepared ");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgLayout.setVisibility(View.VISIBLE);
//        mp.release();
        mVideoView.setVideoURI(null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVideoView.isPlaying()){
            mVideoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mImgLayout.getVisibility() == View.GONE && !mVideoView.isPlaying()){
            mVideoView.resume();
        }
    }

    //    private void initVideoProgressPooling(final int stopAtMsec) {
//        cancelProgressPooling();
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mVideoView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!isActive) {
//                            cancelProgressPooling();
//                            return;
//                        }
//                        if (mVideoView.getCurrentPosition() >= stopAtMsec) {
//                            mVideoView.pause();
//                            cancelProgressPooling();
//                            Toast.makeText(video_test.this, "Video has PAUSED at: " + mVideoView.getCurrentPosition(), Toast.LENGTH_SHORT).show();
//                            mImgLayout.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//            }
//        }, 0, POOLING_INTERVAL_MS);
//    }
//
//    private void cancelProgressPooling() {
//        if(timer != null) {
//            timer.cancel();
//        }
//        timer = null;
//    }

}
