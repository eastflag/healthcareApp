package kr.co.aura.mtelo.healthcare.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import kr.co.aura.mtelo.healthcare.R;

/**
 * Created by young-kchoi on 16. 3. 3..
 */
public class AnimatedProgressLinear extends LinearLayout{

    public static final int MODE_ALL = 100;
    public static final int MODE_NO_ANI = 101;
    private int mMode = MODE_ALL;

    private int mProgress = 0;
    private SeekBar mProgressBar;
    private ImageView mAniImage, mUpDownImage;
    private TextView mSubText, mMainText;
    private Context mContext;
    private int Xvalue = 500;
    private final int DEFAULT_XVALUE = 500;
    private CheckTypesTask mTask;

    public AnimatedProgressLinear(Context context) {
        super(context);
        init(context);
    }

    public AnimatedProgressLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimatedProgressLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimatedProgressLinear(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(
//                getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),    //width
//                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));  //height




    }

    private void init(Context context) {

        mContext = context;

        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.animated_porgress_linear , null);

        mProgressBar = (SeekBar) view.findViewById(R.id.ani_progressbar);
        mAniImage = (ImageView) view.findViewById(R.id.ani_move_img);
        mUpDownImage = (ImageView) view.findViewById(R.id.ani_updown_flag_img);
        mSubText = (TextView) view.findViewById(R.id.ani_user_sub_text);
        mMainText = (TextView) view.findViewById(R.id.ani_main_text);

        mProgressBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        switch (mMode) {
            case MODE_ALL:
                mAniImage.setVisibility(View.VISIBLE);
                mUpDownImage.setVisibility(View.VISIBLE);
                mSubText.setVisibility(View.VISIBLE);
                mMainText.setVisibility(View.VISIBLE);
                break;

            case MODE_NO_ANI:
                mAniImage.setVisibility(View.GONE);
                mUpDownImage.setVisibility(View.GONE);
                mSubText.setVisibility(View.GONE);
                mMainText.setVisibility(View.VISIBLE);
                break;
        }
        addView(view);  // 불러온 뷰를 실제로 삽입한다
    }

    public void aniReset(){
        setXValue(DEFAULT_XVALUE);

        if(mTask != null )
            mTask.cancel(false);
    }

    public void setMode(int mode){
        mMode = mode;
    }

    public void setXValue(int value){
        Xvalue = value;
    }

    public void setProgress(int pro){
        mProgress = pro;
        mProgressBar.setProgress(mProgress);
    }
    public void setMax(int proMax){
        mProgressBar.setMax(proMax);
    }

    public int getmProgress(){
        return mProgressBar.getProgress();
    }
    public  int getMax(){
        return mProgressBar.getMax();
    }


    public void setMainMessage(String str ){
        mMainText.setText(str);
    }

    public void setSubMessage(String str){
        mSubText.setText(str);
    }

    public void setAveProgressbar (int sec_pro){
        mProgressBar.setSecondaryProgress(sec_pro);
    }


    public ImageView getAniImage(){
        return  mAniImage;
    }
    public void startImgAnim(){
        //단위는 필셀
        TranslateAnimation anim = new TranslateAnimation
                        (0,   // fromXDelta
                        Xvalue,  // toXDelta
                        0,    // fromYDelta
                        0);// toYDelta
        anim.setDuration(500);
        anim.setFillAfter(true);

        mAniImage.startAnimation(anim);

        if(mTask != null )
            mTask.cancel(false);


//        mTask = new CheckTypesTask();
//        mTask.execute();
        CheckTypesTask task = new CheckTypesTask();
        task.execute();
    }




    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int addCount = 0;
            int max = mProgressBar.getMax();
            if(max > 100){
                 addCount = max /100;
            }
                        try {
                for (int i = 0; i < mProgress; i++) {
                    mProgressBar.setProgress(i + addCount);
                    Thread.sleep(15);
                    i = i+ addCount;
//                    Log.e("!!!!", "!!!! aniPro i "+ i +", addCount "+ addCount +", max "+ max);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}