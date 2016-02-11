package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.Timer;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.util.FullVideoView;

public class VideoTest extends Activity implements MediaPlayer.OnPreparedListener ,MediaPlayer.OnCompletionListener, View.OnClickListener{


    private FullVideoView mVideoView;
    private LinearLayout mImgLayout ;
    private ImageView mImgBtn1 , mImgBtn2, mImgBtn3, mImgBtn4;
    private Button mCheckBtn1, mCheckBtn2, mCheckBtn3, mCheckBtn4, mReplayBtn, mOksBtn;
    private RelativeLayout mBtnLayout1 ,mBtnLayout2, mBtnLayout3, mBtnLayout4;

    //보기 모드들
    private final int MODE_2 =0;
    private final int MODE_3 =1;
    private final int MODE_4 =2;
//    private final static int POOLING_INTERVAL_MS = 100;
    private Timer timer;

    private final String INTRO_VIDEO ="http://210.127.55.205/psychology_contents/sample/an/AN_IN.mp4";
    private final String OUTRO_VIDEO ="http://210.127.55.205/psychology_contents/sample/an/AN_OUT.mp4";


    private final String BG_IMAGE ="http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_Q.png";
    private final String EX1_IMAGE ="http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_A_1.png";
    private final String EX2_IMAGE ="http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_A_2.png";

    private final String EX1_VIDEO = "http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_1.mp4";
    private final String EX2_VIDEO = "http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_2.mp4";


    private String mLastPlayVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);

        videoInit();
        layoutInit();
        BtnLayuoutInit();


        ImageView bg = (ImageView) findViewById(R.id.image_bg);
        Picasso.with(VideoTest.this).load(BG_IMAGE).fit().into(bg);

        setLayoutImages(BG_IMAGE, EX1_IMAGE, EX2_IMAGE, null, null);


        VideoPlay(INTRO_VIDEO);
//        mImgLayout.bringToFront();
//        mImgLayout.setVisibility(View.VISIBLE);
    }



    //16.02.01 레이아웃구성
    private void layoutInit() {
        mImgLayout = (LinearLayout)findViewById(R.id.btnLayout);
        mImgBtn1 = (ImageView)findViewById(R.id.example_image1);
        mImgBtn2 = (ImageView) findViewById(R.id.example_image2);
        mImgBtn3 = (ImageView) findViewById(R.id.example_image3);
        mImgBtn4 = (ImageView) findViewById(R.id.example_image4);
        mImgBtn1.setOnClickListener(this);
        mImgBtn2.setOnClickListener(this);
        mImgBtn3.setOnClickListener(this);
        mImgBtn4.setOnClickListener(this);
        mImgLayout.bringToFront();


        mCheckBtn1 = (Button)findViewById(R.id.dubleBtn1);
        mCheckBtn2 = (Button)findViewById(R.id.dubleBtn2);
        mCheckBtn3 = (Button)findViewById(R.id.dubleBtn3);
        mCheckBtn4 = (Button)findViewById(R.id.dubleBtn4);

        mCheckBtn1.setOnClickListener(this);
        mCheckBtn2.setOnClickListener(this);
        mCheckBtn3.setOnClickListener(this);
        mCheckBtn4.setOnClickListener(this);


        mReplayBtn = (Button) findViewById(R.id.replay_btn);
        mOksBtn    = (Button) findViewById(R.id.ok_btn);

        mReplayBtn.setOnClickListener(this);
        mOksBtn.setOnClickListener(this);


    }


    private void setLayoutImages(String bg , String ex1, String ex2 , String ex3 , String ex4){

        //보기 이미지 체크
        Picasso.with(VideoTest.this).load(ex1).into(mImgBtn1);
        Picasso.with(VideoTest.this).load(ex2).fit().into(mImgBtn2);
    }

    //16.02.01 비디오뷰 구성
    private void videoInit() {
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.healthcare_video);
        mVideoView = (FullVideoView) findViewById(R.id.myVideo);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
    }


    private void VideoPlay(String url){
        mImgLayout.setVisibility(View.GONE);
        mVideoView.setVisibility(View.VISIBLE);
        if(mVideoView != null){
            mVideoView.bringToFront();
            mVideoView.setVideoURI( Uri.parse(url));
            mLastPlayVideo = url;
        }
    }

    private void BtnLayuoutInit(){
        mBtnLayout1 = (RelativeLayout) findViewById(R.id.btn_layout1);
        mBtnLayout2 = (RelativeLayout) findViewById(R.id.btn_layout2);
        mBtnLayout3 = (RelativeLayout) findViewById(R.id.btn_layout3);
        mBtnLayout4 = (RelativeLayout) findViewById(R.id.btn_layout4);
    }

    private void setMode2(){
        mBtnLayout1.setVisibility(View.VISIBLE);
        mBtnLayout2.setVisibility(View.VISIBLE);
        mBtnLayout3.setVisibility(View.GONE);
        mBtnLayout4.setVisibility(View.GONE);
    }

    private void setMode3(){
        mBtnLayout1.setVisibility(View.VISIBLE);
        mBtnLayout2.setVisibility(View.VISIBLE);
        mBtnLayout3.setVisibility(View.VISIBLE);
        mBtnLayout4.setVisibility(View.GONE);
    }


    private void setMode4(){
        mBtnLayout1.setVisibility(View.VISIBLE);
        mBtnLayout2.setVisibility(View.VISIBLE);
        mBtnLayout3.setVisibility(View.VISIBLE);
        mBtnLayout4.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.example_image1:
                case R.id.dubleBtn1:
                    checkChkButton(mCheckBtn1);
                    break;

                case R.id.example_image2:
                case R.id.dubleBtn2:
                    checkChkButton(mCheckBtn2);
                    break;

                case R.id.example_image3:
                case R.id.dubleBtn3:
                    checkChkButton(mCheckBtn3);
                    break;

                case R.id.example_image4:
                case R.id.dubleBtn4:
                    checkChkButton(mCheckBtn4);
                    break;

                case R.id.replay_btn:
                    if(mLastPlayVideo != null){
                        VideoPlay(mLastPlayVideo);
                    }

                    break;
                case R.id.ok_btn:
                    VideoPlay(OUTRO_VIDEO);
                    break;

            }
            Log.e("", "!!!!!!! 더블버튼 태그" + v.getId());
    }


    //16.02.01 더블버튼 체크로직
    private void checkChkButton(View v){

        if (v.getTag() != null) {
            double id = (Integer) v.getTag();
            if (id == R.drawable.dublebtn_bg_click) {
                Log.e("!!!", "@@@@@@@@@@@@ same!!! 리소스  " + id);
                switch (v.getId()){
                    case R.id.dubleBtn1:
                        VideoPlay(EX1_VIDEO);
                        break;

                    case R.id.dubleBtn2:
                        VideoPlay(EX1_VIDEO);
                        break;

                    case R.id.dubleBtn3:
                        break;

                    case R.id.dubleBtn4:
                        break;
                }
            }

        }


        mCheckBtn1.setBackgroundResource(R.drawable.dublebtn_bg);
        mCheckBtn2.setBackgroundResource(R.drawable.dublebtn_bg);
        mCheckBtn3.setBackgroundResource(R.drawable.dublebtn_bg);
        mCheckBtn4.setBackgroundResource(R.drawable.dublebtn_bg);

        mCheckBtn1.setTag(R.drawable.dublebtn_bg);
        mCheckBtn2.setTag(R.drawable.dublebtn_bg);
        mCheckBtn3.setTag(R.drawable.dublebtn_bg);
        mCheckBtn4.setTag(R.drawable.dublebtn_bg);


        switch (v.getId()) {
            case R.id.example_image1:
            case R.id.dubleBtn1:
                mCheckBtn1.setBackgroundResource(R.drawable.dublebtn_bg_click);
                mCheckBtn1.setTag(R.drawable.dublebtn_bg_click);
                break;

            case R.id.example_image2:
            case R.id.dubleBtn2:
                mCheckBtn2.setBackgroundResource(R.drawable.dublebtn_bg_click);
                mCheckBtn2.setTag(R.drawable.dublebtn_bg_click);
                break;

            case R.id.example_image3:
            case R.id.dubleBtn3:
                mCheckBtn3.setBackgroundResource(R.drawable.dublebtn_bg_click);
                mCheckBtn3.setTag(R.drawable.dublebtn_bg_click);
                break;

            case R.id.example_image4:
            case R.id.dubleBtn4:
                mCheckBtn4.setBackgroundResource(R.drawable.dublebtn_bg_click);
                mCheckBtn4.setTag(R.drawable.dublebtn_bg_click);
                break;
        }

    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.setSystemUiVisibility(VideoView.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        mImgLayout.setVisibility(View.GONE);
        mVideoView.setBackgroundColor(Color.TRANSPARENT);
        mVideoView.setVisibility(View.VISIBLE);
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgLayout.setVisibility(View.VISIBLE);
        mImgLayout.bringToFront();
        mVideoView.setVisibility(View.GONE);
        mVideoView.seekTo(0);
        mVideoView.setBackgroundColor(Color.BLACK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.setSystemUiVisibility(VideoView.SYSTEM_UI_FLAG_FULLSCREEN);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVideoView != null && mVideoView.isPlaying()){
            mVideoView.pause();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mVideoView != null &&  mImgLayout.getVisibility() == View.GONE && !mVideoView.isPlaying()){
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