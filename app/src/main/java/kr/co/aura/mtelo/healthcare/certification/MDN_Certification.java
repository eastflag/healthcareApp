package kr.co.aura.mtelo.healthcare.certification;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.VideoSplash;
import kr.co.aura.mtelo.healthcare.childlist.Child_List;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork.Call_Back;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.LCommonFunction;
import kr.co.aura.mtelo.healthcare.util.MLog;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager.OneButton_Handle;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gcm.GCMRegistrar;

public class MDN_Certification extends SherlockActivity implements OnClickListener 
{

	private CloseEditText edit1;
	private CloseEditText  edit2;
	
	private Button btn1, btn2;
	private Context mContext;
	
	private final int CERTIFICATION_SUCCESS = 1500;
	private final int CERTIFICATION_FAILED = CERTIFICATION_SUCCESS+1;
	private final int MAIL_CERTIFICATION   = CERTIFICATION_SUCCESS+2;
	private final int SENDMAIL_CERTIFICATION_SUCCESS   = CERTIFICATION_SUCCESS+3;
	private final int SENDMAIL_CERTIFICATION_FAILED   = CERTIFICATION_SUCCESS+4;
	private final int SENDMAIL_UNKWON_ADDRESS   = CERTIFICATION_SUCCESS+5;
	
	private ProgressDialog mProgress;
	private String mMDN;
	private InputMethodManager mIMM;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.certification);
		 mContext = MDN_Certification.this;
		
		 init_ACtionBar();
		mIMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		edit1 = (CloseEditText) findViewById(R.id.editText1);
		edit1.setInputType(InputType.TYPE_CLASS_PHONE);
		
		edit2 = (CloseEditText) findViewById(R.id.editText2);
		edit2.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		
		TelephonyManager mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		mMDN = mTelephonyManager.getLine1Number();
		if(mMDN == null || mMDN.length() == 0 )
		{
			mMDN="";    
			edit1.setHint("전화번호 읽기 실패"); 	// 2014.07.21 @HYT 전화번호 정보를 가져오지 못할 경우 텍스트 추가.
			edit1.setEnabled(false); 			// 2014.07.21 @HYT 전화번호를 수정하지 못하도록 수정
			btn1.setEnabled(false);
			btn2.setEnabled(false); 			// 2014.07.21 @HYT 다음으로 진행하지 못하도록 수정
			btn2.setClickable(false); 			// 2014.07.21 @HYT 다음으로 진행하지 못하도록 수정
			Toast.makeText(mContext,"사용자의 전화번호를 정상적으로 읽어올 수 없습니다.", Toast.LENGTH_LONG).show(); // 2014.07.21 @HYT 토스트 추가
		}
		else
		{
			edit1.setText( mMDN);
			edit1.setEnabled(false); //2013.09.09 전화번호를 수정하지 못하도록 수정  
			btn1.setEnabled(true);
		}
		
		edit1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 7 )
				{
					btn1.setEnabled(true);
				}
				else if(s.length() <= 8 )
				{
					btn1.setEnabled(false);
				}
			}
		});
		
		edit2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {		}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {	}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() >= 4 )
				{
//					btn2.setEnabled(true);
				}
				else if(s.length() == 0)
				{
//					btn2.setEnabled(false);
				}
			}
		});
		
		
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}

	
	private ActionBar mActionBar ;
	//액션바 설정
	private void init_ACtionBar()
	{
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(R.string.actionbar_title_cerf);
		((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setVisibility(View.GONE);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setVisibility(View.GONE);
	}
	
	private String mCode = null;
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.button1: //인증번호 요청
			//13.10.10 인증번호를 무조건 0000으로 입력하여 처리한다 
//			request_Certification_Number(LCommonFunction.remake_PhoneNumber(mContext, edit1.getText().toString()));
			chackNumber("0000");
			break;
			
		case R.id.button2:
//			if(chackMDN(edit1.getText().toString()))
			mCode = "0000"; //13.10.10 SMS인증을 하게될시에 삭제할것 
//			if(mCode != null)
			if(mSignCode != null && mSignCode.equalsIgnoreCase("0")) //13.10.10 SMS인증을 하게될시에 삭제할것
			{
//				chackNumber(edit2.getText().toString().trim());
				signUP_Popup(mSignCode);
			}
			else
			{
				Popup_Manager.Show_Dialog(mContext, R.string.cerf_popup_failed_title,
						R.string.cerf_top_text, android.R.string.ok, new OneButton_Handle() {
							
							@Override
							public void onOK() {
								// TODO Auto-generated method stub
								
							}
						});
			}
			break;
		}
	}
	
	
	private void request_Certification_Number(String number)
	{
		JSONNetWork_Manager.request_SMSCertification(number, mContext, new Call_Back() {
			
			@Override
			public void onRoaming(String message) {	}
			
			@Override
			public void onGetResponsString(String data) 
			{
				MLog.write(this, "request_Certification_Number()\n " +data );
				String Result = null, errMsg = null;
				JSONObject jsonObject = null;
				try
				{
					jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
					Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); // 결과값
					errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); // 에러 메세지
					
					if ( Result != null && Result.equalsIgnoreCase("0") )
					{
						JSONObject array = new JSONObject(jsonObject.optString(JSONNetWork.RETURN_KEY_VALUE));
						if(array != null)
						{
							mCode  = array.optString(JSONNetWork.RETURN_KEY_SMS_CODE); // SMS 인증코드
							Log.e("", "result= "+Result +" "+ mCode);
							runOnUiThread(new Runnable() {
								public void run() {
									edit2.setText(mCode);
									edit2.setEnabled(false);
									Popup_Manager.Show_Dialog(mContext, 
															R.string.cerf_popup_title,
															 R.string.cerf_popup_msg,
															 android.R.string.ok,
															 new Popup_Manager.OneButton_Handle() {
										
										@Override
										public void onOK() {
											
										}
									});
								}
							});
						}
					}
					else //if(Result != null && Result.equalsIgnoreCase("2") )
					{	
						runOnUiThread(new Runnable() {
							public void run() {
								edit2.setText(mCode);
								edit2.setEnabled(false);
								Popup_Manager.Show_Dialog(mContext,
														R.string.cerf_popup_failed_title,
														R.string.cerf_popup_failed2_msg	,
														android.R.string.ok,
														R.string.cerf_btn_1_failed_text,
														new Popup_Manager.TwoButton_Handle() {
									
									@Override
									public void onPostive() {									}
									
									@Override
									public void onNegative() {
										Uri uri = Uri.parse("tel:1544-1284");  
										Intent it = new Intent(Intent.ACTION_CALL, uri);  
										startActivity(it);  
									}
								});
							}
						});
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
			@Override
			public void onGetResponsData(byte[] data) {		}
			
			@Override
			public void onError(String error) {	}
		});
	}
	
	
	String mSignCode;
	private void request_signup()
	{
		String mdn = LCommonFunction.remake_PhoneNumber(mContext, edit1.getText().toString() );
		mdn = mdn.replace("-", "");
		JSONNetWork_Manager.request_SignUP(mdn, GCMRegistrar.getRegistrationId(mContext), mContext, new Call_Back() {
			
			@Override
			public void onRoaming(String message) {	}
			
			@Override
			public void onGetResponsString(String data) 
			{
				MLog.write(this, "request_signup()\n " +data );
				String Result = null, errMsg = null;
				JSONObject jsonObject = null;
				
				try
				{
					jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
					Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); // 결과값
					errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); // 에러 메세지
					mSignCode = Result;  //13.10.10 SMS인증을 하게될시에 삭제할것
					if ( Result != null && Result.equalsIgnoreCase("0") )
					{
						JSONObject array = new JSONObject(jsonObject.optString(JSONNetWork.RETURN_KEY_VALUE));
						if(array != null)
						{
							Log.e("", "result= "+Result +" "+ array);
//							signUP_Popup(Result);

							//13.10.10 SMS인증을 하게될시에 삭제할것
							runOnUiThread(new Runnable() {
								public void run() {
									edit2.setText(mCode);
									edit2.setEnabled(false);
									Popup_Manager.Show_Dialog(mContext, 
															R.string.cerf_popup_title,
															 R.string.cerf_popup_msg,
															 android.R.string.ok,
															 new Popup_Manager.OneButton_Handle() {
										
										@Override
										public void onOK() {
											
										}
									});
								}
							});
						}
					}
					else //if(Result != null && Result.equalsIgnoreCase("2") )
					{	
						signUP_Popup(Result);
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
			public void onError(String error) {}
		});
		
		
	}
	
	private void signUP_Popup(final String flag)
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() 
			{
				if(flag.equalsIgnoreCase("0"))
				{
					Popup_Manager.Show_Dialog(mContext, R.string.cerf_popup_sign_title, R.string.cerf_popup_sign_msg ,android.R.string.ok, new OneButton_Handle() {
						
						@Override
						public void onOK() {
							CPreferences cp = new CPreferences(mContext);
							cp.setSignUP("true");
							cp.setShared_MDN( LCommonFunction.remake_PhoneNumber(mContext, edit1.getText().toString()) );
							//Intent in = new Intent(mContext, VideoSplash.class);
							//인사말 동영상 삭제
							Intent in = new Intent(mContext, Child_List.class);
							startActivity(in);
							finish();
						}
					});
				}
				else
				{
					Popup_Manager.Show_Dialog(mContext,
							R.string.cerf_popup_failed_title,
							R.string.cerf_popup_failed2_msg	,
							android.R.string.ok,
							R.string.cerf_btn_1_failed_text,
							new Popup_Manager.TwoButton_Handle() {
						
						@Override
						public void onPostive() {									}
						
						@Override
						public void onNegative() {
							Uri uri = Uri.parse("tel:1544-1284");  
							Intent it = new Intent(Intent.ACTION_CALL, uri);  
							startActivity(it);  
						}
					});
				}
			}
		});
	}
	
	
	
	
	
	//등록된 전화번호인지 체크  
	private boolean  chackMDN(String mdn)
	{
		//가입자 번호인지 검증하는 코드를 넣을것
		if(mdn.equalsIgnoreCase("0000"))
		{
			return true;
		}
		return false;
	}
	
	//인증번호가 정상적인지 체크
	private void chackNumber(String number)
	{
		
		Log.e("" ,  "chackNumber("+ number+")");
		if(number.equalsIgnoreCase( "0000")) 
		{
			request_signup();
		}
		else
		{
			
		}
	}

}
