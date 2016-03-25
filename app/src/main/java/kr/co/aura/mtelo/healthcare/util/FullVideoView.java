package kr.co.aura.mtelo.healthcare.util;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

public class FullVideoView extends VideoView  {

//	private boolean IsFullSize = true;
	
	public FullVideoView(Context context) {
		super(context);
	}

	
	public FullVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	public FullVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Display dis =((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//		setMeasuredDimension(dis.getWidth(), dis.getHeight() );

		int w =getResources().getDisplayMetrics().widthPixels;
		int h = getResources().getDisplayMetrics().heightPixels;
		setMeasuredDimension(w , h);
		
		MLog.write(Log.DEBUG, "FullVideoView" , "onMeasure W= "+w+", h= "+h);

		//16.02.04 비디오 플레이시 화면 하단의 버튼을 안보이게..
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setSystemUiVisibility(VideoView.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
	}

	

}
