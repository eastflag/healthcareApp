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


    private ArrayList<TestList> mTestList = new ArrayList<TestList>();
    private ArrayList<String> mAnswer = new ArrayList<String>();

    private String mSimliId, mIntroType, mIntroImg ,mIntroVideo, mOutroType, mOutroImg, mOutroVideo;
    private ImageView mBG ;

    private final int REFASH_LAYOUT = 100;

    private final int MODE_QUESTION = 1000;
    private final int MODE_ANSWER   = 1001;
    private int mNowMode = MODE_QUESTION;


    @Override
    public String toString() {
        return "VideoTest{" +
                "mSimliId='" + mSimliId + '\'' +
                ", mIntroType='" + mIntroType + '\'' +
                ", mIntroImg='" + mIntroImg + '\'' +
                ", mIntroVideo='" + mIntroVideo + '\'' +
                ", mOutroType='" + mOutroType + '\'' +
                ", mOutroImg='" + mOutroImg + '\'' +
                ", mOutroVideo='" + mOutroVideo + '\'' +
                '}';
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);

        Intent in = getIntent();
        mSimliId     = in.getStringExtra("simliId");
        mIntroType   = in.getStringExtra("introType");
        mIntroImg    = in.getStringExtra("introImg");
        mIntroVideo  = in.getStringExtra("introVideo");
        mOutroType   = in.getStringExtra("outroType");
        mOutroImg    = in.getStringExtra("outroImg");
        mOutroVideo  = in.getStringExtra("outroVideo");


        Log.e("###", "######" + toString());


        mImgLayout = (LinearLayout)findViewById(R.id.btnLayout);
        //배경이미지
        mBG= (ImageView) findViewById(R.id.image_bg);

        getMenetalTestList(mSimliId);

        if(mIntroType.toLowerCase().equals("video")){
            videoInit();
            VideoPlay(mIntroVideo);
        }else{
            Picasso.with(getApplicationContext()).load(mIntroImg).into(mBG);
        }


    }



    //레이아웃에 이미지 넣기
    private void showQuestion(final TestList item){
        Log.e("$$$", "@@@@@@@ 현재 문제는  "+ item.toString());

//        보기 이미지
        if(item.answers.size() >= 2){
            Picasso.with(VideoTest.this).load(item.answers.get(0).img).fit().into(mImgBtn1);
            Picasso.with(VideoTest.this).load(item.answers.get(1).img).fit().into(mImgBtn2);

            if (item.answers.size() >= 3) {
                Picasso.with(VideoTest.this).load(item.answers.get(2).img).fit().into(mImgBtn3);

                if (item.answers.size() >= 4) {
                    Picasso.with(VideoTest.this).load(item.answers.get(3).img).fit().into(mImgBtn4);
                }
            }
        }

        //배경화면 이미지
        Picasso.with(VideoTest.this).load(item.img).fit().into(mBG);
//        Picasso.with(VideoTest.this).load(item.img).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                mImgLayout.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
//                mImgLayout.bringToFront();
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {}
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {}
//        });

    }

    private void getMenetalTestList(String simliid) {
        JSONNetWork_Manager.request_Get_Mental_TestList(simliid, this, new NetWork.Call_Back() {
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
                item.img = object.getString("img");
                item.questId = object.getString("questId");
                item.video = object.getString("video");
                item.videoC = object.getString("videoC");

                JSONArray answer = new JSONArray(object.getString("answer"));

                for (int j = 0; j < answer.length(); j++) {
                    JSONObject answerObject = answer.getJSONObject(j);
                    TestItemAnswer Answer = new TestItemAnswer();
                    Answer.answerId = answerObject.getString("answerId");
                    Answer.content = answerObject.getString("content");
                    Answer.img = answerObject.getString("img");
                    Answer.score = answerObject.getString("score");
                    Answer.video = answerObject.getString("video");
                    item.answers.add(Answer);
                }
                mTestList.add(item);

                Log.e("!!!!!", "###### " + item.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
//            UI는 통신스레드에서 손댈수 없으니 핸들러를 이용해서 UI를 변경한다
            mHandler.sendEmptyMessage(REFASH_LAYOUT);
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BtnLayuoutInit(mTestList.get(0).answers.size());
        }
    };

    //16.02.01 답변 레이아웃구성
    private void layoutInit(RelativeLayout layout, int count) {
        mImgLayout.removeAllViews();
        mImgLayout.addView(layout);

        mBG= (ImageView) layout.findViewById(R.id.image_bg);
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
            mVideoView.setVideoURI(Uri.parse(url));
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
    }


    //16.02.01 더블버튼 체크로직
    private void checkChkButton(View v){

        if (v.getTag() != null) {
            double id = (Integer) v.getTag();
            if (id == R.drawable.dublebtn_bg_click) {
                Log.e("$$", "@@@@@@ 버튼 더들클릭 " + mTestList.size());


                //문재리스트에서 현재 문제의 동영상 플레이
                TestList item = mTestList.get(0);

                switch (v.getId()) {
                    case R.id.dubleBtn1:
                        VideoPlay(item.answers.get(0).video);
                        mAnswer.add(item.answers.get(0).answerId);
                        break;

                    case R.id.dubleBtn2:
                        VideoPlay(item.answers.get(1).video);
                        mAnswer.add(item.answers.get(1).answerId);
                        break;

                    case R.id.dubleBtn3:
                        VideoPlay(item.answers.get(2).video);
                        mAnswer.add(item.answers.get(2).answerId);
                        break;

                    case R.id.dubleBtn4:
                        VideoPlay(item.answers.get(3).video);
                        mAnswer.add(item.answers.get(3).answerId);
                        break;

                }

                preTestItemDelete();   //선택한 문재를 삭제
            }

            //체크버튼의 상태를 리셋한다
            reserCheckButton();


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

    }

    private void reserCheckButton() {
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
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {

        //문제 레이아웃을 표시한다
        if (mTestList.size() != 0) {
            mHandler.sendEmptyMessage(REFASH_LAYOUT);
        } else {
            //  문제리스트가 0개면 결과창으로
            Toast.makeText(VideoTest.this, "결과창으로 이동", Toast.LENGTH_SHORT).show();
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.setSystemUiVisibility(VideoView.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        mImgLayout.setVisibility(View.GONE);
        mVideoView.setBackgroundColor(Color.TRANSPARENT);
        mVideoView.setVisibility(View.VISIBLE);

//        mp.seekTo(10000);
        mp.start();



    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        //문제를 풀고 결과값을 넘겨준다
        if(mTestList.size() == 0 ) {
            Intent intent = new Intent(VideoTest.this, VideoTestResultList.class);
            intent.putStringArrayListExtra("answer", mAnswer);
            intent.putExtra("simliId", mSimliId);
            startActivity(intent);
            finish();
        }

        reserCheckButton();

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



    private void preTestItemDelete(){
        mTestList.remove(0);/// 리스트 삭제
    }
}
