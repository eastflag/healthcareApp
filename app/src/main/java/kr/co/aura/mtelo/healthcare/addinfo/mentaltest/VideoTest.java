package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.util.FullVideoView;

public class VideoTest extends Activity implements MediaPlayer.OnPreparedListener ,MediaPlayer.OnCompletionListener, View.OnClickListener{


    private FullVideoView mVideoView;
    private LinearLayout mImgLayout ;
    private ImageView mImgBtn1 , mImgBtn2, mImgBtn3, mImgBtn4;
    private Button mCheckBtn1, mCheckBtn2, mCheckBtn3, mCheckBtn4, mReplayBtn, mOksBtn;
    private RelativeLayout mBtnMode4, mBtnMode3, mBtnMode2;

    //보기 모드들
    private final int MODE_2 =0;
    private final int MODE_3 =1;
    private final int MODE_4 =2;
//    private final static int POOLING_INTERVAL_MS = 100;

    private ArrayList<TestList> mTestList = new ArrayList<TestList>();


    private final String INTRO_VIDEO ="http://210.127.55.205/psychology_contents/sample/an/AN_IN.mp4";
    private final String OUTRO_VIDEO ="http://210.127.55.205/psychology_contents/sample/an/AN_OUT.mp4";
    private final String BG_IMAGE ="http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_Q.png";
    private final String EX1_IMAGE ="http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_A_1.png";
    private final String EX2_IMAGE ="http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_A_2.png";

    private final String EX1_VIDEO = "http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_1.mp4";
    private final String EX2_VIDEO = "http://210.127.55.205/psychology_contents/sample/an/AN_E13_01_2.mp4";


    private String mSimliId, mIntro, mOutro;
    private ImageView mBG ;

    private String mLastPlayVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);

        Intent in = getIntent();
        mSimliId = in.getStringExtra("simliId");
        mIntro   = in.getStringExtra("intro");
        mOutro   = in.getStringExtra("outro");

        Log.e("###", "###### msimliId " + mSimliId + ", intro " + mIntro + ", outro " + mOutro);


        mImgLayout = (LinearLayout)findViewById(R.id.btnLayout);
        //배경이미지
        mBG= (ImageView) findViewById(R.id.image_bg);

        getMenetalTestList();

        //test
        mIntro = INTRO_VIDEO;
        videoInit();
        VideoPlay(mIntro);


    }



    private void showQuestion(TestList item){
        //배경화면 이미지
        Picasso.with(VideoTest.this).load(item.img).fit().into(mBG);

//        보기 이미지
        if(item.answers.size() >= 2){
            Picasso.with(VideoTest.this).load(item.answers.get(0).img).into(mImgBtn1);

            Picasso.with(VideoTest.this).load(item.answers.get(1).img).into(mImgBtn2);
            if (item.answers.size() >= 3) {

                Picasso.with(VideoTest.this).load(item.answers.get(2).img).into(mImgBtn3);
                if (item.answers.size() >= 4) {

                    Picasso.with(VideoTest.this).load(item.answers.get(3).img ).into(mImgBtn4);
                }
            }
        }



    }

    private void getMenetalTestList() {
        JSONNetWork_Manager.request_Get_Mental_TestList(mSimliId, this, new NetWork.Call_Back() {
            @Override
            public void onError(String error) {
            }

            @Override
            public void onGetResponsData(byte[] data) {
            }

            @Override
            public void onGetResponsString(String data) {
//                Log.e("$$$", "$$$ request_Get_Mental_Info()\n " + data);
                String Result = null, errMsg = null;
                JSONObject jsonObject = null;

                try {
                    if (data != null) {
                        JSONArray array = new JSONArray(data);
                        makeTestList(array);
                    } else {
                        Toast.makeText(VideoTest.this, "서버가 잘못됬습니다.", Toast.LENGTH_LONG).show();
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


    //전체 문항리스트를 획득하여 ArrayList로 만든다
    private void makeTestList(JSONArray array) {

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                TestList item = new TestList();
                item.content = object.getString("content");
                item.img     = object.getString("img");
                item.questId = object.getString("questId");
                item.video   = object.getString("video");
                item.videoC  = object.getString("videoC");

                JSONArray answer = new JSONArray( object.getString("answer"));

                for (int j = 0; j < answer.length() ; j++){
                    JSONObject answerObject = answer.getJSONObject(j);
                    TestItemAnswer Answer = new TestItemAnswer();
                    Answer.answerId = answerObject.getString("answerId");
                    Answer.content  = answerObject.getString("content");
                    Answer.img      = answerObject.getString("img");
                    Answer.score    = answerObject.getString("score");
                    Answer.video    = answerObject.getString("video");
                    item.answers.add(Answer);
                }

                mTestList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
//            UI는 통신스레드에서 손댈수 없으니 핸들러를 이용해서 UI를 변경한다
            mHandler.sendEmptyMessage(0);
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BtnLayuoutInit(mTestList.get(0).answers.size());
        }
    };

    //16.02.01 레이아웃구성
    private void layoutInit(RelativeLayout layout, int count) {
        mImgLayout.removeAllViews();
        mImgLayout.addView(layout);

        if (count >= 2) {
            mImgBtn1 = (ImageView) layout.findViewById(R.id.example_image1);
            mImgBtn2 = (ImageView) layout.findViewById(R.id.example_image2);
            mImgBtn1.setOnClickListener(this);
            mImgBtn2.setOnClickListener(this);

            mCheckBtn1 = (Button) layout.findViewById(R.id.dubleBtn1);
            mCheckBtn2 = (Button) layout.findViewById(R.id.dubleBtn2);
            mCheckBtn1.setOnClickListener(this);
            mCheckBtn2.setOnClickListener(this);
            if (count >= 3) {
                mImgBtn3 = (ImageView) layout.findViewById(R.id.example_image3);
                mImgBtn3.setOnClickListener(this);

                mCheckBtn3 = (Button) layout.findViewById(R.id.dubleBtn3);
                mCheckBtn3.setOnClickListener(this);
                if (count >= 4) {
                    mImgBtn4 = (ImageView) layout.findViewById(R.id.example_image4);
                    mImgBtn4.setOnClickListener(this);

                    mCheckBtn4 = (Button) layout.findViewById(R.id.dubleBtn4);
                    mCheckBtn4.setOnClickListener(this);
                }
            }
        }

        showQuestion(mTestList.get(0));
        mImgLayout.bringToFront();
//        mReplayBtn = (Button) findViewById(R.id.replay_btn);
//        mOksBtn    = (Button) findViewById(R.id.ok_btn);
//
//        mReplayBtn.setOnClickListener(this);
//        mOksBtn.setOnClickListener(this);
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

    private void BtnLayuoutInit(int count) {
        if (count == 2) {
            mBtnMode2 = (RelativeLayout) getLayoutInflater().inflate(R.layout.video_test_img_btn_list2, null);
            layoutInit(mBtnMode2 , count);

        } else if (count == 3) {
            mBtnMode3 = (RelativeLayout) getLayoutInflater().inflate(R.layout.video_test_img_btn_list3, null);
            layoutInit(mBtnMode3 , count);

        } else if (count == 4) {
            mBtnMode4 = (RelativeLayout) getLayoutInflater().inflate(R.layout.video_test_img_btn_list4, null);
            layoutInit(mBtnMode4, count);
        }
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

//                case R.id.replay_btn:
//                    if(mLastPlayVideo != null){
//                        VideoPlay(mLastPlayVideo);
//                    }
//
//                    break;
//                case R.id.ok_btn:
//                    VideoPlay(OUTRO_VIDEO);
//                    break;

            }
            Log.e("", "!!!!!!! 더블버튼 태그" + v.getId());
    }


    //16.02.01 더블버튼 체크로직
    private void checkChkButton(View v){

        if (v.getTag() != null) {
            double id = (Integer) v.getTag();
            if (id == R.drawable.dublebtn_bg_click) {

                //문재리스트 초기화
                mTestList.remove(0);
                BtnLayuoutInit(mTestList.get(0).answers.size());

                switch (v.getId()){
                    case R.id.dubleBtn1:
                        VideoPlay(EX1_VIDEO);
                        break;

                    case R.id.dubleBtn2:
                        VideoPlay(EX1_VIDEO);
                        break;

                    case R.id.dubleBtn3:
                        Log.e("$$" , "3번 버튼 클릭  ");
                        break;

                    case R.id.dubleBtn4:
                        break;
                }
            }

        }


        mCheckBtn1.setBackgroundResource(R.drawable.dublebtn_bg);
        mCheckBtn2.setBackgroundResource(R.drawable.dublebtn_bg);
        mCheckBtn1.setTag(R.drawable.dublebtn_bg);
        mCheckBtn2.setTag(R.drawable.dublebtn_bg);

        if(mCheckBtn3 != null){
            mCheckBtn3.setBackgroundResource(R.drawable.dublebtn_bg);
            mCheckBtn3.setTag(R.drawable.dublebtn_bg);
        }

        if(mCheckBtn4 !=null){
            mCheckBtn4.setBackgroundResource(R.drawable.dublebtn_bg);
            mCheckBtn4.setTag(R.drawable.dublebtn_bg);
        }


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

    private class TestList {
        private String content;
        private String img;
        private String questId;
        private String video;
        private String videoC;
        private ArrayList<TestItemAnswer> answers = new ArrayList<TestItemAnswer>();

        @Override
        public String toString() {
            return "TestList{" +
                    "content='" + content + '\'' +
                    ", img='" + img + '\'' +
                    ", questId='" + questId + '\'' +
                    ", video='" + video + '\'' +
                    ", videoC='" + videoC + '\'' +
                    ", answers=" + answers +
                    '}';
        }
    }

    private class TestItemAnswer {
        private String answerId;
        private String content;
        private String img;
        private String score;
        private String video = null;

        @Override
        public String toString() {
            return "TestItemAnswer{" +
                    "answerId='" + answerId + '\'' +
                    ", content='" + content + '\'' +
                    ", img='" + img + '\'' +
                    ", socre='" + score + '\'' +
                    ", video='" + video + '\'' +
                    '}';
        }
    }
}
