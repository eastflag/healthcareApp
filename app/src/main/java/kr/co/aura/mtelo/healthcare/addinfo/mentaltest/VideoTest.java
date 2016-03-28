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
    private String selectedAnswer = null;

    private String mSimliId, mIntroType, mIntroImg ,mIntroVideo, mOutroType, mOutroImg, mOutroVideo, mUserId;
    private ImageView mBG ;

    private final int REFASH_LAYOUT = 100;

    private final int MODE_QUESTION = 1000;
    private final int MODE_ANSWER   = 1001;
    private int mNowMode = MODE_QUESTION;

    private boolean isOutroVideo = false;


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

        Intent intent = getIntent();
        MentalListItem item = (MentalListItem)intent.getSerializableExtra("mentalListItem");
        mSimliId     = item.getSimliId();
        mIntroType   = item.getIntroType();
        mIntroImg    = item.getIntroImg();
        mIntroVideo  = item.getIntroVideo();
        mOutroType   = item.getOutroType();
        mOutroImg    = item.getOutroImg();
        mOutroVideo  = item.getOutroVideo();
        mUserId      = intent.getStringExtra("userId");

        Log.e("###", "######" + toString());


        mImgLayout = (LinearLayout)findViewById(R.id.btnLayout);
        //배경이미지
        mBG= (ImageView) findViewById(R.id.image_bg);


        int color = 0;
        //각 문제에 맞게 칼라값을 획득, 적용한다
        if(mSimliId.toUpperCase().startsWith("AN")){ //불안척도
            color = Color.parseColor("#a7c787");
        }else if(mSimliId.toUpperCase().startsWith("GL")){ //우울척도
            color = Color.parseColor("#ffce98");
        }else if(mSimliId.toUpperCase().startsWith("SE")) { //자아존중감
            color = Color.parseColor("#70a3c7");
        }
        mBG.setBackgroundColor(color);
        mImgBtn1 = (ImageView) mImgLayout.findViewById(R.id.example_image1);
        mImgBtn2 = (ImageView) mImgLayout.findViewById(R.id.example_image2);
        mImgBtn3 = (ImageView) mImgLayout.findViewById(R.id.example_image3);
        mImgBtn4 = (ImageView) mImgLayout.findViewById(R.id.example_image4);

        if (mImgBtn2 != null) {
            mImgBtn1.setBackgroundColor(color);
            mImgBtn2.setBackgroundColor(color);
            if (mImgBtn3 != null) {
                mImgBtn3.setBackgroundColor(color);
                if (mImgBtn4 != null) {
                    mImgBtn4.setBackgroundColor(color);
                }
            }
        }


        // 데이터를 획득한다
        getMenetalTestList(mSimliId);

        if(mIntroType.toLowerCase().equals("video")){
            //동영상일떄
            videoInit();
            VideoPlay(mIntroVideo);
        }else{
            //이미지일떄
            Picasso.with(getApplicationContext()).load(mIntroImg).into(mBG);
        }

        Picasso.with(getApplicationContext()).setLoggingEnabled(false);
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
            // TODO 임시로 2개만 처리 하도록 구성
            for (int i = 0; i < array.length(); i++) {
            //for (int i = 0; i < 2; i++) {
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
            //변경
//            UI는 통신스레드에서 손댈수 없으니 핸들러를 이용해서 UI를 변경한다
//            mHandler.sendEmptyMessage(REFASH_LAYOUT);
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TestList item =  mTestList.get(0);
            BtnLayuoutInit(item.answers.size());
            VideoPlay(item.video);

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
        if(url == null) return;

        try {
            Log.e("!!!!!!!!", "!!!!! VideoPlay " + url);
            mImgLayout.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            if (mVideoView != null) {
                mVideoView.bringToFront();
                mVideoView.setVideoURI(Uri.parse(url));
                mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.e("!!!!!", "!!!!!! onError " + what + ", extra " + extra);
                        resetVideoView();
                        return true;
                    }
                });

//                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        Log.e("VideoPlay", "PlayCompete : ================== ");
//                        mp.stop();
//
//                        if(isOutroVideo){
//                            //문제를 풀고 결과값을 넘겨준다
//                            Intent intent = new Intent(VideoTest.this, VideoTestResultList.class);
//                            intent.putStringArrayListExtra("answer", mAnswer);
//                            intent.putExtra("simliId", mSimliId);
//                            intent.putExtra("userId", mUserId);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // answer의 수에 따라 레이아웃을 변경한다
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
    private void checkChkButton(View v) {

        double id = 0;
        if (v.getTag() != null) {
            id = (Integer) v.getTag();
        }

        if (id == R.drawable.dublebtn_bg_click) {
            Log.e("$$", "@@@@@@ 버튼 더들클릭 " + mTestList.size());

            if(selectedAnswer != null) {
                mAnswer.add(selectedAnswer);
                selectedAnswer = null;
            }


            preTestItemDelete();   //선택한 문재를 삭제

            if (mTestList.size() != 0) {
                mHandler.sendEmptyMessage(REFASH_LAYOUT);  // 다음문제로 이동
            } else {

                // TODO outro 영상을 display 한다.
                isOutroVideo = true;
                VideoPlay(mOutroVideo);
            }
        }


        if (mTestList.size() != 0) {
            reserCheckButton();    //체크버튼의 상태를 리셋한다
            TestList item = mTestList.get(0);  //문재리스트에서 현재 문제의 동영상 플레이
            Log.e("!!!!!", "!!!!! TestItem size " + mTestList.size() + ", item = " + item.toString());
            String videoUrl = "";
            switch (v.getId()) {
                case R.id.example_image1:
                case R.id.dubleBtn1:
                    mCheckBtn1.setBackgroundResource(R.drawable.dublebtn_bg_click);
                    mCheckBtn1.setTag(R.drawable.dublebtn_bg_click);
                    videoUrl = item.answers.get(0).video;
                    //VideoPlay(item.answers.get(0).video);
                    selectedAnswer = item.answers.get(0).answerId;
                    //mAnswer.add(item.answers.get(0).answerId);
                    break;

                case R.id.example_image2:
                case R.id.dubleBtn2:
                    mCheckBtn2.setBackgroundResource(R.drawable.dublebtn_bg_click);
                    mCheckBtn2.setTag(R.drawable.dublebtn_bg_click);
                    videoUrl = item.answers.get(1).video;
                    //VideoPlay(item.answers.get(1).video);
                    selectedAnswer = item.answers.get(1).answerId;
                    //mAnswer.add(item.answers.get(1).answerId);
                    break;

                case R.id.example_image3:
                case R.id.dubleBtn3:
                    mCheckBtn3.setBackgroundResource(R.drawable.dublebtn_bg_click);
                    mCheckBtn3.setTag(R.drawable.dublebtn_bg_click);
                    //VideoPlay(item.answers.get(2).video);
                    videoUrl = item.answers.get(2).video;
                    selectedAnswer = item.answers.get(2).answerId;
                    //mAnswer.add(item.answers.get(2).answerId);
                    break;

                case R.id.example_image4:
                case R.id.dubleBtn4:
                    mCheckBtn4.setBackgroundResource(R.drawable.dublebtn_bg_click);
                    mCheckBtn4.setTag(R.drawable.dublebtn_bg_click);
                    //VideoPlay(item.answers.get(3).video);
                    videoUrl = item.answers.get(3).video;
                    selectedAnswer = item.answers.get(3).answerId;
                    //mAnswer.add(item.answers.get(3).answerId);
                    break;
            }

            if(videoUrl.length() > 0) {
                VideoPlay(videoUrl);
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

        //인트로후 1회만 동작하도록 한다
        if(mNowMode == MODE_QUESTION){
            mHandler.sendEmptyMessage(REFASH_LAYOUT);
            mNowMode = MODE_ANSWER;
        }

        resetVideoView();
        Log.e("VideoPlay", "PlayCompete : ================== ");

        if(isOutroVideo){
            //문제를 풀고 결과값을 넘겨준다
            Intent intent = new Intent(VideoTest.this, VideoTestResultList.class);
            intent.putStringArrayListExtra("answer", mAnswer);
            intent.putExtra("simliId", mSimliId);
            intent.putExtra("userId", mUserId);
            startActivity(intent);
            finish();
        }
    }

    private void resetVideoView() {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.e("!!!!" , "!!!!!!! onBackPressed");
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
