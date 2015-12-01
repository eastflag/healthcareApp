package kr.co.aura.mtelo.healthcare.certification;

import java.util.UUID;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.GCMIntentService;
import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.childlist.Child_List;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.HCDialog;
import kr.co.aura.mtelo.healthcare.util.MLog;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gcm.GCMRegistrar;


@SuppressLint("HandlerLeak")
public class Terms extends SherlockActivity  {

	
	
	private Context mContext;
	private String mUser_Seq = "";
	private boolean IsTermsOfService = false, IsDetailPrivacy = false, IsConsentToUseOfPrivacy = false;
	
	private PopupWindow mPopup;	
	
	static 
	{
		MLog.set_LogEnable(Define.LOG, "annex");
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms);
		
		mContext = Terms.this;
		
	
		CPreferences cp = new CPreferences(mContext);
		if(cp.getSignUP().equalsIgnoreCase("true"))
			gotoChildList();
		else
			init();
		
	}

	
	private void showSelectDialog(int resid) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_terms, null);
		TextView tx = (TextView)v.findViewById(R.id.terms_text);
		tx.setText(resid);
		
		mPopup = new PopupWindow(v, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, false);
		mPopup.setAnimationStyle(R.style.DialogAnimation);
		mPopup.showAsDropDown(mActionBar.getCustomView());
		
		LinearLayout linear = (LinearLayout)v.findViewById(R.id.outer_layout);
		linear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopup.dismiss();
			}
		}); 
	}
	
	@Override
	public void onBackPressed() {
		if(mPopup !=  null && mPopup.isShowing() )
			mPopup.dismiss();
		else		
			super.onBackPressed();

	}
	
	
	
	private void gotoChildList()
	{
		Intent in = new Intent(Terms.this, Child_List.class);
		startActivity(in);
		finish();
	}
	
	private void gotoSplash() 
	{
//		put_Playapp_Priority(); // 최초가입시 1회입력후 설정이 바뀔떄마다 재입력할것
		Intent in = new Intent(Terms.this, MDN_Certification.class);
		startActivity(in);
		finish();
	}
	
	private ActionBar mActionBar ;
	//액션바 설정
	private void init_ACtionBar(int title)
	{
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(title);
		((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setVisibility(View.GONE);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setVisibility(View.GONE);
	}
	
	
	private  void init()
	{
		init_ACtionBar(R.string.actionbar_title_terms);
		CheckBox box_Thrms_of_Service = (CheckBox)findViewById(R.id.cb_I_agree_to_Terms_of_Service);
		CheckBox box_Privacy = (CheckBox)findViewById(R.id.cb_I_agree_to_Privacy);
		CheckBox box_Use_of_Privacy = (CheckBox)findViewById(R.id.cb_I_agree_to_CONSENT_TO_USE_OF_Privacy);
		
		box_Thrms_of_Service.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				IsTermsOfService = isChecked? true: false;
			}
		});
		
		box_Privacy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				IsDetailPrivacy = isChecked? true: false;
			}
		});
		box_Use_of_Privacy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				IsConsentToUseOfPrivacy = isChecked? true: false;
			}
		});
		
		//GCM ID 등록하기 
		registrationID(mContext);
	}
	public void btnDetailTermsOfServiceClick(View v )
	{
		showSelectDialog(R.string.join__terms_of_service);
	}
	
	
	public void btnDetailPrivacyClick(View v)
	{
		showSelectDialog(R.string.join__privacy_policy);
	}
	
	
	public void btnConsentToUseOfPrivacyClick(View v)
	{
		showSelectDialog(R.string.join__consent_to_use_of_privacy);
	}
	public void btnJoinStarRing(View v)
	{
		if(IsTermsOfService && IsDetailPrivacy && IsConsentToUseOfPrivacy)
		{
			//동의 팝업
			Popup_Manager.Show_Dialog(mContext, R.string.sign_popup_title, R.string.sign_popup_msg, android.R.string.ok, new Popup_Manager.OneButton_Handle() {
				
				@Override
				public void onOK() {
				gotoSplash();
				}
			});
		}
		else 
		{//체크 확인
			 int msg = 0;
			 if(IsTermsOfService == false )
			{
				msg = R.string.sign_popup_msg1;
			}
			 else if(IsDetailPrivacy  == false )
			 {
				 msg = R.string.sign_popup_msg2;
			 }
			 else if(IsConsentToUseOfPrivacy == false ) 
			{
				msg = R.string.sign_popup_msg3;
			}
			
			Popup_Manager.Show_Dialog(mContext, R.string.sign_popup_title, msg, android.R.string.ok, new Popup_Manager.OneButton_Handle() {
				
				@Override
				public void onOK() {
				}
			});
		}
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case HCDialog.DIALOG_DEFAULT:
			MLog.write(Log.ERROR, "" , "onActivityResult");
			gotoSplash();
			break;

		}
	}	
	
	
	//유니크한 디바이스ID를 생성한다
	//사용 안한다
	private String CreateUniqueDeviceID() 
	{
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String tmDevice, tmSerial;//, tmPhone;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		
		UUID deviceUuid = new UUID((long)tmDevice.hashCode() , tmSerial.hashCode() );
		return deviceUuid.toString();
	}
		
	//gcm 등록
	private  void registrationID(Context context) {
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		String regId = GCMRegistrar.getRegistrationId(this);
		MLog.write(Log.ERROR, "", "registration id =====&nbsp; "+regId);

		if (regId.equals("")) {
			MLog.write(Log.ERROR, "", "now registered");
		GCMRegistrar.register(this, GCMIntentService.SEND_ID);
		} else {
			MLog.write(Log.ERROR, "", "Already registered");
		}
	}
		
}
