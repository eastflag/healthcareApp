package kr.co.aura.mtelo.healthcare;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import kr.co.aura.mtelo.healthcare.certification.Terms;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.FullVideoView;
import kr.co.aura.mtelo.healthcare.util.MLog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("HandlerLeak")
public class VideoSplash extends Activity implements  OnPreparedListener, OnClickListener{
//    private long splashDelay = 1500;

    private Context mContext;
    private final String TAG = "VideoSplash"; 
	private FullVideoView mVideoView ;
    private CPreferences mCP;

    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_splash);
        
        mContext= VideoSplash.this;
        mCP = new CPreferences(mContext);
        
        if(mCP.getFirstSlide().equalsIgnoreCase("false"))
        {
        	init();
			mCP.setFirstSlide("true");
        }
        else
        {
        	gotoMain();
        }
    }
    
	private void init() {
		Intent in = getIntent();
		Uri u = Uri.parse("android.resource://"+ getPackageName()+"/"+R.raw.healthcare_video);
		mVideoView = (FullVideoView)findViewById(R.id.video);
		mVideoView.setOnClickListener(this);
		mVideoView.setVideoURI(u );
		mVideoView.requestFocus();
//		mVideoView.start();
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				gotoMain();
				
				return false;
			}
		});
		
		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
					gotoMain();
			}
		});
	}

	
	@Override
	public void onBackPressed() {
		gotoMain();
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
//		   if(isINTRO_VIDEO_PLAYED == true)
//	        {
//	        	isINTRO_VIDEO_PLAYED = false;
//	        	gotoMain();
//	        }
		super.onResume();
	}


	@Override
	protected void onPause() {
		if(mVideoView != null && mVideoView.isPlaying())
		{
			mVideoView.stopPlayback();
		}
		super.onPause();
	}
	private void gotoMain() {
            Intent hackbookIntent = new Intent(VideoSplash.this, Terms.class);
            startActivity(hackbookIntent);
            finish();
	}
    
    

    
    @Override
	public void onPrepared(MediaPlayer mp) 
	{
		mp.start();
	}



	@Override
	public void onClick(View v) {
		MLog.write(Log.ERROR, "", "onClick(), v.getId() = " + v.getId());
		
		if ( v.getId() == R.id.video )
		{
			if (mVideoView != null &&  mVideoView.isPlaying() )
			{
				mVideoView.stopPlayback();
				mVideoView = null;
			}
			gotoMain();
		}
	}
    

} 
                       