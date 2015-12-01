package kr.co.aura.mtelo.healthcare;


import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Scroller;



public class TutorialSlide extends Activity {
	
	private final int COUNT = 4;				//아이템 갯수
	private int mPrevPosition;				//이전에 선택되었던 포지션 값
	private LinearLayout mPageMark;			//현재 몇 페이지 인지 나타내는 뷰
//	private ArrayList<String> mFilelist = null;
//	private String path = Environment.getExternalStorageDirectory().toString()+"/DCIM";
	private Context mContext;
	
	private int mHome_Images[] = {R.drawable.slide_img1,
								  R.drawable.slide_img2,
								  R.drawable.slide_img3,
								  R.drawable.slide_img4
								  };
	
	private	ViewPager pager;
	private String mName= null, mLastDate= null,mSex= null, mAge= null, mCM= null, mKG= null, mUserId = null,
			mBMI= null, mG_Point= null, mPPM= null, mCOHD= null ,mCM_Status, mKG_Status	,mBMI_Status, mPPM_Status, mSchoolId, mGradeId; 
	private boolean mTimerStart = true; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		setContentView(R.layout.tutorialslide);
		Intent in = getIntent();
		mUserId 		= in.getStringExtra("userId");
		mName			= in.getStringExtra("name");
		mLastDate 		= in.getStringExtra("lastdate");
		mAge 	  		= in.getStringExtra("age");
		mSex 	  		= in.getStringExtra("sex");
		mCM 	  		= in.getStringExtra("cm");
		mKG		  		= in.getStringExtra("kg");
		mBMI	  		= in.getStringExtra("bmi");
		mG_Point  		= in.getStringExtra("g_point");
		mPPM	  		= in.getStringExtra("ppm");
		mCOHD	  		= in.getStringExtra("cohd");
		mCM_Status 	  	= in.getStringExtra("cm_status");
		mKG_Status	  	= in.getStringExtra("kg_status");
		mBMI_Status	  	= in.getStringExtra("bmi_status");
		mPPM_Status	  	= in.getStringExtra("ppm_status");
		mSchoolId	  	= in.getStringExtra("schoolGradeId");
		mGradeId		= in.getStringExtra("mGradeId");
		
		if(new CPreferences(mContext).getFirstSlide().equalsIgnoreCase("true"))
		{
			mTimerStart = false;
			moveMain();
		}
		
		
		
		
		mPageMark = (LinearLayout)findViewById(R.id.page_mark);			//상단의 현재 페이지 나타내는 뷰
		mPageMark.bringToFront();
		
		pager = (ViewPager)findViewById(R.id.pager);
		SlidePagerleAdapter samp = new SlidePagerleAdapter(mContext);
		pager.setAdapter(samp);
		pager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//이전 페이지에 해당하는 페이지 표시 이미지 변경
				mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.home_icon_pageflip_off);	
				//현재 페이지에 해당하는 페이지 표시 이미지 변경
				mPageMark.getChildAt(position).setBackgroundResource(R.drawable.home_icon_pageflip_on);
				mPrevPosition = position;				//이전 포지션 값을 현재로 변경
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {	}
			
			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});
		initPageMark();
		
		
		final Timer timer = new Timer();
		if(mTimerStart)
		{
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							pager.setCurrentItem(mPage, true);
							mPage+=1;
//							Log.e("", "mpage = "+ mPage);
							if(mPage > COUNT)
							{
								CPreferences cp = new CPreferences(mContext);
								cp.setFirstSlide("true");
								moveMain();
								timer.cancel();
							}
						}
					});
				}
			}, 0, 2000);
		}
		
		try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true); 
            Interpolator sInterpolator = new AccelerateInterpolator();            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(), sInterpolator);
            // scroller.setFixedDuration(5000);
            mScroller.set(pager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }		
	}
	
	private  int mPage =0;
	private void moveMain()
	{
		String format = null;
		if(mLastDate != null)
		{
			format= mLastDate.replace("-", ".");
			format = format.substring(2, format.length());
		}
		
		Intent in = new Intent(mContext, MetroMain.class);
		in.putExtra("userId", mUserId);
		in.putExtra("name", mName);
		in.putExtra("lastdate", format);
		in.putExtra("age",  mAge);
		in.putExtra("sex", mSex);
		in.putExtra("cm",  mCM);
		in.putExtra("kg", mKG );
		in.putExtra("bmi", mBMI);
		in.putExtra("g_point", mG_Point);
		in.putExtra("ppm",  mPPM);
		in.putExtra("cohd",  mCOHD);
		in.putExtra("cm_status", mCM_Status);
		in.putExtra("kg_status", mKG_Status);
		in.putExtra("bmi_status", mBMI_Status);
		in.putExtra("ppm_status", mPPM_Status);
		in.putExtra("schoolGradeId", mSchoolId);
		in.putExtra("mGradeId", mGradeId);
		startActivity(in);
		finish();
	}
	
	//상단의 현재 페이지 위치 표시하는 뷰 초기화
	private void initPageMark(){
		for(int i=0; i< COUNT; i++)
		{
			ImageView iv = new ImageView(getApplicationContext());	//페이지 표시 이미지 뷰 생성
			LayoutParams params =new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
			params.rightMargin=20;
			params.topMargin = 15;
			
			iv.setLayoutParams(params);

			//첫 페이지 표시 이미지 이면 선택된 이미지로
			if(i==0){
				iv.setBackgroundResource(R.drawable.home_icon_pageflip_on);
			}else	{//나머지는 선택안된 이미지로
				iv.setBackgroundResource(R.drawable.home_icon_pageflip_off);
			}

			//LinearLayout에 추가
			mPageMark.addView(iv);
		}
		mPrevPosition = 0;	//이전 포지션 값 초기화
	}	
	

	
	@Override
	public void finish() {
		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
		super.finish();
	}

public class SlidePagerleAdapter extends PagerAdapter {
//	private Context mContext;
//	private LayoutInflater inflater;
	private BitmapFactory.Options option;
	
	public SlidePagerleAdapter(Context context) {
//		mContext = context;
//		inflater = LayoutInflater.from(context);
		option = new BitmapFactory.Options();
		option.inSampleSize = 2;
	}
	
	@Override
	public void destroyItem(View container, int postion, Object view) {
			   ((ViewPager) container).removeView((View)view);
	}


	@Override
	public int getCount() {
		return COUNT;
	}

	@Override
	public Object instantiateItem(View pager, int position) 
	{
		View v = getLayoutInflater().inflate(R.layout.tutorialslide_item, null);
		ImageView image =(ImageView) v.findViewById(R.id.home_pager_item);
		image.setImageResource(mHome_Images[position]);
		((ViewPager)pager).addView( v);
		return v;
		
//		ImageView image = new ImageView(mContext);
//		image.setScaleType(ScaleType.FIT_XY);
//		Bitmap b = BitmapFactory.decodeFile(path+"/"+mFilelist.get(position) , option);
//		
//		image.setImageBitmap(b);
//		
//		((ViewPager)pager).addView( image);
//		return image;
		
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void startUpdate(View container) {
		// TODO Auto-generated method stub
		super.startUpdate(container);
	}
}

public class FixedSpeedScroller extends Scroller {

    private int mDuration = 400;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }


    @SuppressLint("NewApi")
	@Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}

}
