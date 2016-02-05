package kr.co.aura.mtelo.healthcare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import kr.co.aura.mtelo.healthcare.addinfo.AddInfoActivity;
import kr.co.aura.mtelo.healthcare.certification.TermsViewActivity;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork.Call_Back;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.LCommonFunction;
import kr.co.aura.mtelo.healthcare.util.MLog;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager;
import kr.co.aura.mtelo.healthcare.video.VideoList;


public class MetroMain extends SherlockActivity implements OnClickListener{	

	private Context mCon;
	private final int ERROR_MESSAGE = 100001; 
	//private String totResult = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_new6); 
		
		mCon = MetroMain.this;
		Log.e("", "gcm = " + GCMRegistrar.getRegistrationId(mCon));
		init_ACtionBar(getIntent());
		initUI();
	}

	
	//GCM등이 들어올경우 실행된다 
	@Override
	protected void onNewIntent(Intent intent) {
		onRestart();
		init_ACtionBar(intent);
		initUI();
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		//chack_nocice();
		super.onResume();
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent resultIntent1 = new Intent();
		setResult(Activity.RESULT_OK, resultIntent1);
		//Toast.makeText(mCon,자녀선택 page 로 이동....... ", Toast.LENGTH_SHORT).show();
		super.onBackPressed();
		
	}


	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	 public static void sender(String regId, String authToken, String msg) throws Exception { 
         StringBuffer postDataBuilder = new StringBuffer();
         postDataBuilder.append("registration_id=" + regId); // 등록ID 
         postDataBuilder.append("&collapse_key=1"); 
         postDataBuilder.append("&delay_while_idle=1"); 
         postDataBuilder.append("&data.msg=" + URLEncoder.encode(msg, "UTF-8"));
         
         
         byte[] postData = postDataBuilder.toString().getBytes("UTF8");
         URL url = new URL("https://android.googleapis.com/gcm/send");
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return false;
			}
		});
         conn.setDoOutput(true); 
         conn.setUseCaches(false); 
         conn.setRequestMethod("POST"); 
         conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
         conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
         conn.setRequestProperty("Authorization", "key=" + authToken);
         OutputStream out = conn.getOutputStream(); 
         out.write(postData); 
         out.close();
         conn.getInputStream();
         
         System.out.println("postData : " + postDataBuilder.toString());
         String reponseLine = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
         System.out.println("responseLine : " + reponseLine);
	}
  
	
	private void request_get_Notice_Count()
	{
		 JSONNetWork_Manager.request_Notice_Count(mCon, new Call_Back() {
			
			@Override
			public void onRoaming(String message) {}
			
			@Override
			public void onGetResponsString(String data) {
				
				MLog.write(Log.ERROR,"" , "request_ServiceInfo() "+ data);	
				
				String Result = null, errMsg = null;
				JSONObject jsonObject = null;
		 		try
				{
				jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
				Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); // 결과값
				errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); // 에러 메세지
				
				if ( Result != null && Result.equalsIgnoreCase("0") )
				{
						
					int count = jsonObject.optInt(JSONNetWork.RETURN_KEY_VALUE);
					int count2 = (new CPreferences(mCon).getEM_Notice_Count());
					if(count > count2)
					{
						new CPreferences(mCon).setEM_Notice_Count(count);
						new CPreferences(mCon).setShowNotice("false");
						//((ImageButton)findViewById(R.id.btn_more)).setBackgroundResource(R.drawable.metro_btn_notice_new);
					}
					else if(count == count2 & new CPreferences(mCon).getShowNotice().equalsIgnoreCase("true"))
					{
						//chack_nocice();
					}
		 			MLog.write(Log.ERROR,"" , "notice Count = "+ count +" count2 ="+ count2);
				}
				else
				{
					Message msg = mHandler.obtainMessage(ERROR_MESSAGE, errMsg);
					mHandler.sendMessage(msg);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			}
			
			@Override
			public void onGetResponsData(byte[] data) {}
			
			@Override
			public void onError(String error) 
			{
				Message msg = mHandler.obtainMessage(ERROR_MESSAGE, error);
				mHandler.sendMessage(msg);
			}
		});
	 }
	
	//private void request_get_EventCheck()
	//{
		 //JSONNetWork_Manager.request_EventCheck(mCon, new Call_Back() {
			
			//@Override
			//public void onRoaming(String message) {}
			
			//@Override
			//public void onGetResponsString(String data) {
				
				//MLog.write(Log.ERROR,"" , "request_request_CHECK_EVENT() "+ data);	
				
				//String Result= null, errMsg = null;
				//JSONObject jsonObject = null;
		 		//try
				//{
				//jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
				//Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); // 결과값
				//errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); // 에러 메세지
				//setTotResult(Result);
				
				//if(getTotResult() != null && getTotResult().equalsIgnoreCase("0"))//서버에서 "0"을 내려줄 경우 이벤트 웹뷰 팝업, "1"일 경우 이벤트 웹뷰 팝업 안함
				//{
					//Intent intent = new Intent(getApplicationContext(), PopEvent.class);
					//startActivity(intent);
					
				//}else {
					
				//}
			//}
		 	
			//catch (Exception e)
			//{
				//e.printStackTrace();
			//}
			//}
			
			//@Override
			//public void onGetResponsData(byte[] data) {}
			
			//@Override
			//public void onError(String error) 
			//{
				//Message msg = mHandler.obtainMessage(ERROR_MESSAGE, error);
				//mHandler.sendMessage(msg);
			//}
		//});
	 //}

	 
	 
	 private Handler mHandler = new Handler()
	 {
		 @Override
		public void dispatchMessage(Message msg) 
		 {
			 switch(msg.what)
			 {
			 case ERROR_MESSAGE:
					Popup_Manager.Show_Error_Dialog(mCon, (String)msg.obj, new Popup_Manager.OneButton_Handle() {
						
						@Override
						public void onOK() {
						}
					});
				 break;
				 
//			 case NEXT_ANIM:
//				 try 
//				 {
//					 int i= msg.getData().getInt(KEY_VALUE);
//					 String str5 = i+"점";
//					 SpannableStringBuilder sp5 = new SpannableStringBuilder(str5);
//					 sp5.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize +10 /*43*/)), 0, str5.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					 sp5.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize +7/*30*/)), str5.length() -1, str5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					 fl7.setText(sp5);//랭킹 버튼과 성장점수 버튼을 교체 
//				 }
//				 catch (Exception err) {
//				 }
//				 break;
//				 
//			 case NEXT_ANIM2:
//				 try 
//				 {
//					 double i= msg.getData().getLong(KEY_VALUE);
//					 String str5 = i+"점";
//					 SpannableStringBuilder sp5 = new SpannableStringBuilder(str5);
//					 sp5.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize +10 /*43*/)), 0, str5.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					 sp5.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize +7/*30*/)), str5.length() -1, str5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//					 fl7.setText(sp5);//랭킹 버튼과 성장점수 버튼을 교체 
//				 }
//				 catch (Exception err) {
//				 }
//				 break;
				 
			case NUMBER_COUNTING_ANIM_CM:  // 신장
				try {
					double i = msg.getData().getDouble(KEY);
					String status = msg.getData().getString(KEY2);
					String str = String.format("%.1f", i) + " cm" + "\n" + status;
					SpannableStringBuilder sp = new SpannableStringBuilder(str);
					sp.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					sp.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize)), str.length() - status.length(), str.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					fl1.setText(sp);
				} catch (Exception err) {
				}
				break;

			case NUMBER_COUNTING_ANIM_KG: // 체중
				try {
					double i = msg.getData().getDouble(KEY);
					String status = msg.getData().getString(KEY2);
					String str = String.format("%.1f", i) + " kg" + "\n" + status;
					SpannableStringBuilder sp = new SpannableStringBuilder(str);
					sp.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					sp.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize)), str.length() - status.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					fl2.setText(sp);
				} catch (Exception err) {
				}
				break;

			case NUMBER_COUNTING_ANIM_BMI: // BMI
				try {
					double i = msg.getData().getDouble(KEY);
					String status = msg.getData().getString(KEY2);
					String str = String.format("%.2f", i) + "\n" + status;
					SpannableStringBuilder sp = new SpannableStringBuilder(str);
					sp.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					sp.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize)), str.length() - status.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					fl3.setText(sp);
				} catch (Exception err) {
				}
				break;

			case NUMBER_COUNTING_ANIM_G_POINT:// 성장점수
				try {
					int i = msg.getData().getInt(KEY);
					String str = i + "점";
					SpannableStringBuilder sp5 = new SpannableStringBuilder(str);
					sp5.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize) + 10), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					sp5.setSpan(new AbsoluteSizeSpan( getDP(SmallFontSize) + 10), str.length() - 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					fl7.setText(sp5);
				} catch (Exception err) {
				}
				break;
			}
		}
	 };

	 
	 
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	 
	 
	private String 
		mName 			= null, 
		mLastDate 		= null, 
		mSex 			= null, 
		mAge 			= null,
		mCM 			= null, 
		mKG 			= null, 
		mUserId 		= null, 
		mBMI 			= null,
		mG_Point 		= null, 
		mPPM 			= null, 
		mCOHD 			= null, 
		mCM_Status 		= null, 
		mKG_Status 		= null, 
		mBMI_Status 	= null, 
		mPPM_Status 	= null, 
		mSchoolId 		= null, 
		mGradeId 		= null;
	
	
	private ActionBar mActionBar ;
	//액션바 설정
	private void init_ACtionBar(Intent in)
	{
		mName 	  	= in.getStringExtra("name");
		mUserId   	= in.getStringExtra("userId");
		mLastDate 	= in.getStringExtra("lastdate");
		mAge 	  	= in.getStringExtra("age");
		mSex 	  	= in.getStringExtra("sex");
		mCM 	  	= in.getStringExtra("cm");
		mKG		  	= in.getStringExtra("kg");
		mBMI	  	= in.getStringExtra("bmi");
		mG_Point  	= in.getStringExtra("g_point");
		mPPM	  	= in.getStringExtra("ppm");
		mCOHD	  	= in.getStringExtra("cohd");
		mCM_Status 	= in.getStringExtra("cm_status");
		mKG_Status	= in.getStringExtra("kg_status");
		mBMI_Status	= in.getStringExtra("bmi_status");
		mPPM_Status	= in.getStringExtra("ppm_status");
		mSchoolId	= in.getStringExtra("schoolGradeId");
		mGradeId 	= in.getStringExtra("mGradeId");
		
		int add_str = mSex.equalsIgnoreCase("m")? R.string.actionbar_title_title_male: R.string.actionbar_title_title_female;
		String str = mName +getResources().getString(add_str);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(str);		//타이틀
		
		mLastDate = mLastDate.substring(0, 7); //TTA월까지 표시요청 으로 인한 수정 예정.. 아우라 컨폼후 2015.02.27아우라 협의 2015.03.02 수정  tharaud
		
		
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).append(mLastDate);	//날짜
		new CPreferences(mCon).setTitleLastDate("update\n"+mLastDate);
		((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Toast.makeText(MetroMain.this,"자녀목록 APP 으로 이동........... ", Toast.LENGTH_SHORT).show();
				
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);

				finish();
			}
		});
	}
	
	
	
	private int BigFontSize = 25;//40;
	private int SmallFontSize = 20;// 25;
	
	//해상도로 스크린 사이즈를 설정
	private void getScreenReSolution()
	{
		int screenX = getResources().getDisplayMetrics().widthPixels;
		if(screenX == 480)
		{
			BigFontSize = 20;
			SmallFontSize = 15;
		}	
	}
	
	//픽셀값을 DP로 전환
	private int getDP(int size)
	{
		DisplayMetrics mex = getResources().getDisplayMetrics();
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mex);
	}
	
	
	private Button fl1, fl2,  fl3, fl4, fl5 , fl6, fl7, fl8 , fl9;
	private void initUI() {
		request_get_Notice_Count();
		getScreenReSolution();		
		
		fl1 = (Button) findViewById(R.id.imageview1); // 신장
		fl2 = (Button) findViewById(R.id.imageview2); //체중
		fl3 = (Button) findViewById(R.id.imageview3); // BMI
		fl4 = (Button) findViewById(R.id.imageview4); //흡연
		fl5 = (Button) findViewById(R.id.imageview5); // 추가정보
		fl6 = (Button) findViewById(R.id.imageview6); // 랭킹
		fl7 = (Button) findViewById(R.id.imageview7); // 성장점수
		fl8 = (Button) findViewById(R.id.imageview8); // 식단
		fl9 = (Button) findViewById(R.id.imageview9); // 추천운동
			
		if(mLastDate.equalsIgnoreCase("null"))
		{
			String str = "미측정";
			SpannableStringBuilder sp = new SpannableStringBuilder(str);
			sp.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			fl1.setText(sp);
			fl2.setText(sp);
			fl3.setText(sp);
			fl4.setText(sp);
			//fl5.setText(sp);//추가정보 주석 tharaud
		}
		else
		{
			//신장,  소수점 한자리 제거 
			String str = mCM + " cm\n" + mCM_Status;
			SpannableStringBuilder sp = new SpannableStringBuilder(str);
			sp.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize)), 0, mCM.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			sp.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize)), mCM.length() , str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			fl1.setText(sp);
			
			//체중
			String str2 = mKG + " kg";
			SpannableStringBuilder sp2 = new SpannableStringBuilder(str2);
			sp2.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize)), 0, mKG.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			sp2.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize)), mKG.length(), str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			fl2.setText(sp2);
			
			//bmi
			String str3 = mBMI + "\n" + mBMI_Status;
			SpannableStringBuilder sp3 = new SpannableStringBuilder(str3);
			sp3.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize)), 0, mBMI.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			sp3.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize)), mBMI.length(), str3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			fl3.setText(sp3);
			
			//흡연
			if(mPPM_Status.equalsIgnoreCase("null") )
			{
				fl4.setBackgroundResource(R.drawable.metro_btn_no_smoking);
//			String str4 = "미측정";
//			SpannableStringBuilder sp4 = new SpannableStringBuilder(str4);
//			sp4.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize)), 0, str4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			fl4.setText(sp4);
//			fl4.setPaintFlags(fl4.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
//			fl4.setHeight(getDP(110));
//			fl5.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			}
			else
			{
				String str4 = mPPM + " ppm\n" + mCOHD + " COHb\n";
				String str41 = mPPM_Status;
				SpannableStringBuilder sp4 = new SpannableStringBuilder(str4);
				SpannableStringBuilder sp41 = new SpannableStringBuilder(str41);
				sp4.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize) - 5), 0 , str4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				sp41.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize)), 0 , str41.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				fl4.setText(sp4);
				fl4.append(sp41);
			}
			
		
			String str5 = "0점"; // mG_Point+"점";
			SpannableStringBuilder sp5 = new SpannableStringBuilder(str5);
			sp5.setSpan(new AbsoluteSizeSpan(getDP(BigFontSize) + 10), 0, str5.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			sp5.setSpan(new AbsoluteSizeSpan(getDP(SmallFontSize) + 10), str5.length() -1, str5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			fl7.setText(sp5);//랭킹 버튼과 성장점수 버튼을 교체 
			
		}
		
		
		fl1.setOnClickListener(this);
		fl2.setOnClickListener(this);
		fl3.setOnClickListener(this);
		fl4.setOnClickListener(this);
		fl5.setOnClickListener(this);
		fl6.setOnClickListener(this);
		fl7.setOnClickListener(this);
		fl8.setOnClickListener(this);
		fl9.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{//9번(추천운동) 버튼은 따로 이벤트를 할당함
				MLog.write(Log.ERROR, this.toString(), "Main shchoolId = "+mSchoolId +" grdeId = "+mGradeId+" userId = "+ mUserId );
				Intent in = new Intent(getApplicationContext(), VideoList.class);
				in.putExtra("schoolGradeId" , mSchoolId);
				in.putExtra("userId" , mUserId);
				in.putExtra("grdeId" , mGradeId);  //mGradeId
				startActivity(in);
			}
		});
		
		//chack_nocice();
		
		makeAnim(); //1: 왼쪽방향 ,2:오른쪽 방향 ,3: 아래방향
       fl1.setAnimation(mAnim1); // 신장  
       fl2.setAnimation(mAnim2); //체중  
       fl3.setAnimation(mAnim2); // BMI 
       fl4.setAnimation(mAnim1); // 흡연
      // fl5.setAnimation(mAnim2); //추가정보
       
       fl5.setAnimation( mAnim3); // 추가정보 2015.04.17 tharaud
       
       fl6.setAnimation(mAnim1); // 랭킹
       fl7.setAnimation(mAnim3); // 성장점수
       fl8.setAnimation(mAnim3); // 식단 
       fl9.setAnimation(mAnim2); // 추천운동
       
    // 신장 애니메이션
		mAnim1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				numberAnimCm(Double.parseDouble(mCM), mCM_Status);
				
				
			}
		});
 
		// 체중, BMI 애니메이션
		mAnim2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				numberAnimKg(Double.parseDouble(mKG), mKG_Status);
				numberAnimBmi(Double.parseDouble(mBMI), mBMI_Status);
			}
		});

		// 성장점수 애니메이션
		mAnim3.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				numberAnimGPoint(Integer.parseInt(mG_Point));
			}
		});

		//이용약관
		findViewById(R.id.tv_termtitle).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MetroMain.this, TermsViewActivity.class);
				startActivity(intent);
			}
		});
		
	};
	
	
//	private static final int toValue = 200;
	private static final int fromValue = 0;
	private static final double fromValue2 = 0.0;
	private static final int FRAME_TIME_MS = 10;
//	private static final int NEXT_ANIM = 1000;
//	private static final int NEXT_ANIM2 = 1001;
	private static final int NUMBER_COUNTING_ANIM_CM      = 2001;
	private static final int NUMBER_COUNTING_ANIM_KG      = 2002;
	private static final int NUMBER_COUNTING_ANIM_BMI     = 2003;
	private static final int NUMBER_COUNTING_ANIM_G_POINT = 2004;
	private static final String KEY = "i";
	private static final String KEY2 = "VALUE";
	
	
	
	boolean isRunning_GPoint = false;
	boolean isRunning_Cm = false;
	boolean isRunning_Kg = false;
	boolean isRunning_Bmi = false;

	
	
	/**
	 *  성장포인트
	 * @param toValue 
	 */
	private void numberAnimGPoint(final int toValue) {
		Thread t_GPoint = new Thread(new Runnable() {
			public void run() {
				try {
					for (int i = fromValue; i <= toValue && isRunning_GPoint; i++) {
						Thread.sleep(FRAME_TIME_MS);
						Bundle data = new Bundle();
						data.putInt(KEY, i);
						Message message = mHandler.obtainMessage(NUMBER_COUNTING_ANIM_G_POINT);
						message.setData(data);
						mHandler.sendMessage(message);
					}
				} catch (Throwable t) {
				}
			}
		});
		isRunning_GPoint = true;
		t_GPoint.start();
	}

	
	
	/**
	 * 신장
	 * @param toValue - 신장 값
	 * @param status - 신장 상태
	 */
	private void numberAnimCm(final double toValue, final String status) {
		Thread t_Cm = new Thread(new Runnable() {
			public void run() {
				try {
					Bundle data = new Bundle();
					double i = fromValue2;
					while (i <= toValue && isRunning_Cm) {
						Thread.sleep(5);
						data.putDouble(KEY, i);
						data.putString(KEY2, status);
						Message message = mHandler.obtainMessage(NUMBER_COUNTING_ANIM_CM);
						message.setData(data);
						mHandler.sendMessage(message);
						i += 1;
					}
					data.putDouble(KEY, toValue);
					data.putString(KEY2, status);
					Message message = mHandler.obtainMessage(NUMBER_COUNTING_ANIM_CM);
					message.setData(data);
					mHandler.sendMessage(message);
				} catch (Throwable t) {
				}
			}
		});
		isRunning_Cm = true;
		t_Cm.start();
	}
	
	
	/**
	 *체중
	 * @param toValue - 체중 값
	 * @param status - 체중 상태
	 */
	private void numberAnimKg(final double toValue, final String status) {
		Thread t_Kg = new Thread(new Runnable() {
			public void run() {
				try {
					Bundle data = new Bundle();
					double i = fromValue2;
					while (i <= toValue && isRunning_Kg) {
						Thread.sleep(5);
						data.putDouble(KEY, i);
						data.putString(KEY2, status);
						Message message = mHandler.obtainMessage(NUMBER_COUNTING_ANIM_KG);
						message.setData(data);
						mHandler.sendMessage(message);
						i += 1;
					}
					data.putDouble(KEY, toValue);
					data.putString(KEY2, status);
					Message message = mHandler.obtainMessage(NUMBER_COUNTING_ANIM_KG);
					message.setData(data);
					mHandler.sendMessage(message);
				} catch (Throwable t) {
				}
			}
		});
		isRunning_Kg = true;
		t_Kg.start();
	}

	
	
	/**
	 * BMI
	 * @param toValue - BMI 값
	 * @param status - BMI 상태
	 */
	private void numberAnimBmi(final double toValue, final String status) {
		Thread t_Bmi = new Thread(new Runnable() {
			public void run() {
				try {
					Bundle data = new Bundle();
					double i = fromValue2;
					while (i <= toValue && isRunning_Bmi) {
						Thread.sleep(5);
						data.putDouble(KEY, i);
						data.putString(KEY2, status);
						Message message = mHandler.obtainMessage(NUMBER_COUNTING_ANIM_BMI);
						message.setData(data);
						mHandler.sendMessage(message);
						i += 1;
					}
					data.putDouble(KEY, toValue);
					data.putString(KEY2, status);
					Message message = mHandler.obtainMessage(NUMBER_COUNTING_ANIM_BMI);
					message.setData(data);
					mHandler.sendMessage(message);
				} catch (Throwable t) {
				}
			}
		});
		isRunning_Bmi = true;
		t_Bmi.start();
	}

	
	
	//13.11.08
	int animTime = 800, anim_Count = 0;
	TranslateAnimation  mAnim1, mAnim2, mAnim3;
	private void makeAnim()
	{
		mAnim1 = new TranslateAnimation( TranslateAnimation.RELATIVE_TO_PARENT, -1f,
 						                 TranslateAnimation.RELATIVE_TO_PARENT, 0f,
						                 TranslateAnimation.ABSOLUTE, 0f,
						                 TranslateAnimation.ABSOLUTE, 0f);
		mAnim1.setDuration(animTime);
		mAnim1.setRepeatCount(anim_Count);
		mAnim1.setRepeatMode(Animation.REVERSE);
		mAnim1.setInterpolator( new AccelerateDecelerateInterpolator() );
		mAnim1.setFillAfter(true);
		

		mAnim2 = new TranslateAnimation(  TranslateAnimation.RELATIVE_TO_PARENT, 1f,
						                  TranslateAnimation.RELATIVE_TO_SELF,   0f,
						                  TranslateAnimation.ABSOLUTE, 0f,
						                  TranslateAnimation.ABSOLUTE, 0f);
		mAnim2.setDuration(animTime);
		mAnim2.setRepeatCount(anim_Count);
		mAnim2.setRepeatMode(Animation.INFINITE);
		mAnim2.setInterpolator(new AccelerateDecelerateInterpolator());
		mAnim2.setFillAfter(true);
		
		mAnim3 = new TranslateAnimation( TranslateAnimation.ABSOLUTE, 0f,
										 TranslateAnimation.ABSOLUTE, 0f,
										 TranslateAnimation.RELATIVE_TO_PARENT, 1f,
										 TranslateAnimation.RELATIVE_TO_PARENT, 0f);
		mAnim3.setDuration(animTime);
		mAnim3.setRepeatCount(anim_Count);
		mAnim3.setRepeatMode(Animation.INFINITE);
		mAnim3.setInterpolator(new AccelerateDecelerateInterpolator());
		mAnim2.setFillAfter(true);
		
	}


	
	
	@Override
	public void onClick(View v) {

	// tharaud 2015.05.26 심리검사 우선 주석

		if(v.getId()==R.id.imageview5){
			//Intent in3 = new Intent(MetroMain.this, SimRiWebActivity.class);
		//	Intent in3 = new Intent(MetroMain.this, SimliTestFrontTempActivity.class);
			//Intent in3 = new Intent(MetroMain.this, SimliTestFrontTempActivity.class);
			Intent in3 = new Intent(MetroMain.this, AddInfoActivity.class);
			in3.putExtra("sex", mSex);
			in3.putExtra("userId" , mUserId);
			in3.putExtra("name", mName);
			//in3.putExtra("menu_value", "menu_list");
			MLog.write("MetroMain", "AddInfoActivity Call ...~ ");
			startActivity(in3);

		}else{

			Intent in2 = new Intent(mCon, WEBActivity.class);
			in2.putExtra("sex", mSex);
			in2.putExtra("name", mName);

			switch (v.getId()) {

				case R.id.imageview1:    //신장
					//Log.i("MetroMain", " => 3 Server URL :::"+ Define.getNetUrl());
					in2.putExtra("url", Define.getNetUrl() + "front-views/view?p=height" + getUserId());
					break;
				case R.id.imageview2:    //체중
					in2.putExtra("url", Define.getNetUrl() + "front-views/view?p=weight" + getUserId());
					break;
				case R.id.imageview3:    //bmi
					in2.putExtra("url", Define.getNetUrl() + "front-views/view?p=bmi" + getUserId());
					break;
				case R.id.imageview4:    //흡연
					in2.putExtra("url", Define.getNetUrl() + "front-views/view?p=smoke" + getUserId());
					break;
				case R.id.imageview5:    //추가정보
					showDialog("알림", "준비중입니다..");  //2015.04.17  추가정보메뉴 즉 심리검사 추가로 인한 수정  //tharaud
					return;
//				in2.putExtra("url", Define.getNetUrl()+"front-views/view?p=add_info"+getUserId());
//				in2.putExtra("menu", "addinfo");
//				in2.putExtra("url", Define.getNetUrl()+"front-views/view?p=add_info"+getUserId());
//				
//				Log.i("MetroMain ","URL :::"+Define.getNetUrl().toString() +"front-views/view?p=add_info"+ "  , UserId ::: "+getUserId());
//				in2.putExtra("url", Define.getNetUrl()+"front-views/view?p=add_info"+getUserId());
//				break;


				case R.id.imageview6:    //랭킹
					in2.putExtra("url", Define.getNetUrl() + "front-views/view?p=rank" + getUserId());
					break;
				case R.id.imageview7:    //성장점수
					in2.putExtra("url", Define.getNetUrl() + "front-views/view?p=score" + getUserId());
					break;
				case R.id.imageview8:    //식단
					in2.putExtra("url", Define.getNetUrl() + "front-views/view?p=food" + getUserId());
					break;
			}
			startActivity(in2);
			overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
		}
		
	}

	
	/** [공지사항] 버튼 클릭 */
	private void chack_nocice() {
		ImageButton notice = (ImageButton)findViewById(R.id.btn_more);
		if(new CPreferences(mCon).getShowNotice().equalsIgnoreCase("false")){
			notice.setBackgroundResource(R.drawable.metro_btn_notice_new);
		}else{
			notice.setBackgroundResource(R.drawable.metro_btn_notice);
		}
	}

	
	
	
	@Deprecated
	public void clickNotice(View v) {
		new CPreferences(mCon).setShowNotice("true");
		
		
		Intent in = new Intent(mCon, WEBActivity.class );
		in.putExtra("url",	Define.getNetUrl()+"notice");
		startActivity(in); 
		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
	}

	
	
	/**
	 * [더보기] 버튼 클릭
	 * @param v
	 */
	public void clickMore(View v) {
		Intent in2 = new Intent(mCon, WEBActivity.class);
		in2.putExtra("sex", mSex);
		in2.putExtra("name", mName);
		in2.putExtra("url", Define.getNetUrl()+"front-views/view?p=more"+getUserId());
		startActivity(in2); 
		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	
	
	/**
	 * [AURA 로고] 버튼 클릭
	 * @param v
	 */
	public void clickAuraLogo(View v) {
		// 특별한 동작 없음
	}
	
	
	@Deprecated //현재 사용하지 않고 있음 
	public void clickQA(View v) {
		Uri uri = Uri.parse("smsto:1544-1284");  
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);  
		it.putExtra("sms_body", "");  
		startActivity(it);  
	}

	
	
	//URL 주소의 끝에 userID를 달아둔다
	public String getUserId() {
		return "&userId="+mUserId;
	}

	
	
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
	}

	
	
	private void showDialog(String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mCon);
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}


	//public String getTotResult() {
		//return totResult;
	//}


	//public void setTotResult(String totResult) {
		//this.totResult = totResult;
	//}
	
}
