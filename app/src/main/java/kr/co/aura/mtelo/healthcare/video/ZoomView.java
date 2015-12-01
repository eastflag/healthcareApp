package kr.co.aura.mtelo.healthcare.video;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



public class ZoomView extends Activity {
	
	private String file;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullimageview);
		
		init_ACtionBar();
		String image = Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() +"/temp/tempimage.jpg";
		Intent in = getIntent();
		file = in.getStringExtra("file");
		Log.e("" , "flie = "+ file);
		ImageView ZoomView = (ImageView)findViewById(R.id.full_imageview);
		ZoomView.setImageBitmap(BitmapFactory.decodeFile(file));
		
		PhotoViewAttacher mAttacher = new PhotoViewAttacher(ZoomView);
		mAttacher.update();
	}
	
	
	private ActionBar mActionBar ;
	//�׼ǹ� ����
	private void init_ACtionBar()
	{
		mActionBar = getActionBar();
//		mActionBar.setTitle(정보 동의");
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(R.string.actionbar_title_image);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setVisibility(View.INVISIBLE);	//날짜
		((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public void ZoomOK(View v)
	{
		Intent i = new Intent(this, VideoList.class);
		i.putExtra("file", file);
		setResult(RESULT_OK, i);
		finish();
	}
	
	public void ZoomCancle(View v)
	{
		setResult(RESULT_OK);
		finish();
	}
}
