package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;

/**
 * Created by young-kchoi on 16. 3. 17..
 */
public class VideoTestResult extends SherlockActivity {

    public static final String Mode_AnswerList = "answerList";
    public static final String Mode_Parent = "parent";
    public static final String Mode_Child = "child";

    private String mSimliId = null;
    private String mUserId = null;
    private String mMode = null;



    private ArrayList<ResultList> mResultList = new ArrayList<ResultList>();


    private final int REFASH_LAYOUT = 100;

    private LinearLayout mBG;

    private TextView mResultView;
    private StringBuilder mResultText;
    private String mResult = ""; // test result

    private ImageView mImageView ;
    private LinearLayout mLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_test_result);

        init_ACtionBar();

        Intent intent = getIntent();
        mSimliId = intent.getStringExtra("simliId");   //심리ID
        mUserId  =  intent.getStringExtra("userId");   //유저ID
        mMode    = intent.getStringExtra("mode");      //부모 자식 모등
        Log.e("!!!!!!!! " , "!!!!!! VideoTestResult{" +
            "mMode='" + mMode + '\'' +
                    ", mUserId='" + mUserId + '\'' +
                    ", mSimliId='" + mSimliId + '\'' +
                    '}') ;


        mBG = (LinearLayout) findViewById(R.id.video_result_bg);
        mLinear = (LinearLayout) findViewById(R.id.video_result_layout);
        mImageView = (ImageView) findViewById(R.id.video_result_img);
        mResultView = (TextView) findViewById(R.id.video_result_text);
        mResultView.setMovementMethod(new ScrollingMovementMethod());

        //문제를 파악한다
        if(mSimliId.toUpperCase().startsWith("AN")){ //불안척도
            setWorryMode();
        }else if(mSimliId.toUpperCase().startsWith("GL")){ //우울척도
            setGloomMode();
        }else if(mSimliId.toUpperCase().startsWith("SE")) { //자아존중감
            setSelfMode();
        }

        if(mMode.equals(Mode_AnswerList)){
            getAnswerList(mUserId, mSimliId);
        } else if(mMode.equals(Mode_Parent) || mMode.equals(Mode_Child)){
            getTestResult(mUserId, mSimliId);
        }


    }



    //우울척도
    private void setGloomMode(){
        mBG.setBackgroundResource(R.color.video_test_result_gloom_bg);
        mImageView.setBackgroundResource(R.drawable.btn_gloom);
        mLinear.setBackgroundResource(R.drawable.btn_video_test_restlt_gloom);
    }

    //불안척도
    private void setWorryMode(){
        mBG.setBackgroundResource(R.color.video_test_result_worry_bg);
        mImageView.setBackgroundResource(R.drawable.btn_worry);
        mLinear.setBackgroundResource(R.drawable.btn_video_test_restlt_worry);
    }

    //자아존중감 모드
    private void setSelfMode(){
        mBG.setBackgroundResource(R.color.video_test_result_self_bg);
        mImageView.setBackgroundResource(R.drawable.btn_self);
        mLinear.setBackgroundResource(R.drawable.btn_video_test_restlt_self);
    }



    private void init_ACtionBar() {
        ActionBar mActionBar;

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.custom_actionbar);

        String str = new CPreferences(this).getUserName();
        ((TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(str);     //타이틀

        String date = new CPreferences(this).getTitleLastDate();
        ((TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setText(date);    //날짜
        ((ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //데이터 획득
    private void getAnswerList(String userId, String simliid) {
        JSONNetWork_Manager.request_Get_Simli_AnswerList(userId, simliid, this, new NetWork.Call_Back() {
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
                        Toast.makeText(VideoTestResult.this, "다시 시도해주세요.", Toast.LENGTH_LONG).show();
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

    private void getTestResult(String userId, String simliid) {
        JSONNetWork_Manager.request_Get_Simli_Result(userId, simliid, this, new NetWork.Call_Back() {
            @Override
            public void onError(String error) {
            }

            @Override
            public void onGetResponsData(byte[] data) {
            }

            @Override
            public void onGetResponsString(String data) {
                String Result = null, errMsg = null;
                JSONObject jsonObject = null;

                try {
                    if (data != null) {
                        JSONObject jsonObj = new JSONObject(data);
                        // parsing 처리
                        if(mMode.equals(Mode_Parent)){
                            mResult = jsonObj.getString("resultParent");
                        } else if(mMode.equals(Mode_Child)) {
                            mResult = jsonObj.getString("resultStudent");
                        }

                        if(mResult != null && mResult.length() > 0){

                            mResult = mResult.replace("<br/>", "\n");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mResultView.setText(mResult);
                                }
                            });
                        }

                    } else {
                        Toast.makeText(VideoTestResult.this, "다시 시도해주세요.", Toast.LENGTH_LONG).show();
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
                ResultList item = new ResultList();
                item.content = object.getString("content");
                item.questId = object.getString("questId");

                JSONArray answer = new JSONArray(object.getString("answer"));

                for (int j = 0; j < answer.length(); j++) {
                    JSONObject answerObject = answer.getJSONObject(j);
                    ResultListAnswer Answer = new ResultListAnswer();
                    Answer.answerId = answerObject.getString("answerId");
                    Answer.content  = answerObject.getString("content");
                    Answer.setectYN = answerObject.getString("selectYN");
                    item.answers.add(Answer);
                }
                mResultList.add(item);

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
            buildBuildText();
        }
    };



    private void buildBuildText(){
        Log.e("!!!!!", "###### start build text" );
        mResultText = new StringBuilder();

        for (int i = 0 ; i < mResultList.size() ; i++){
            ResultList item = mResultList.get(i);

            mResultText.append( (i +1) + ". " + item.getContent()  +"\n");
            ArrayList<ResultListAnswer> answers =  item.answers;


            for (int j = 0 ; j < answers.size() ; j++){
                ResultListAnswer answer = answers.get(j);

                if (answer.setectYN.toUpperCase().equals("Y")) {
                    mResultText.append("■ " + answer.content + "\n");
                } else {
                    mResultText.append("□ " + answer.content + "\n");
                }
            }
            mResultText.append("\n");
        }

        mResultView.setText(mResultText.toString());
        Log.e("!!!!!", "###### start build text \n"+ mResultText.toString());

    }


    class ResultList {
        private  String questId , content;
        private ArrayList<ResultListAnswer> answers = new ArrayList<ResultListAnswer>();

        public ResultList(){}

        public ResultList(String questId, String content, ArrayList<ResultListAnswer> answers) {
            this.questId = questId;
            this.content = content;
            this.answers = answers;
        }

        public String getQuestId() {
            return questId;
        }

        public void setQuestId(String questId) {
            this.questId = questId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public ArrayList<ResultListAnswer> getAnswers() {
            return answers;
        }

        public void setAnswers(ArrayList<ResultListAnswer> answers) {
            this.answers = answers;
        }

        @Override
        public String toString() {
            return "ResultList{" +
                    "questId='" + questId + '\'' +
                    ", content='" + content + '\'' +
                    ", answers=" + answers +
                    '}';
        }
    }

    class ResultListAnswer{
        private String answerId , content , setectYN;

        public ResultListAnswer() {}

        @Override
        public String toString() {
            return "ResultListAnswer{" +
                    "answerId='" + answerId + '\'' +
                    ", content='" + content + '\'' +
                    ", setectYN='" + setectYN + '\'' +
                    '}';

        }

        public ResultListAnswer(String answerId, String content, String setectYN) {
            this.answerId = answerId;
            this.content = content;
            this.setectYN = setectYN;
        }
    }
}
