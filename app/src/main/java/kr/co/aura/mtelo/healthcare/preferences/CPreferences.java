package kr.co.aura.mtelo.healthcare.preferences;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import kr.co.aura.mtelo.healthcare.util.MLog;

public class CPreferences
{
	private Context mContext;
	private SharedPreferences mSharedPreferences;
//	private Resources mResources;
	private Editor mEditor;
	

	
	public CPreferences(Context context)
	{
		mContext = context;
//		mResources = mContext.getResources();
		mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_APPEND);
		mEditor = mSharedPreferences.edit();
	}
	

	/**
	 *  가입여부를 저장
	 */
	public void setSignUP(String str) 
	{
		mEditor.putString("Sign", str);
		mEditor.commit();
	}
	/**
	 *   가입여부를 반환	 */
	public String getSignUP() 
	{
		return mSharedPreferences.getString("Sign",  "false");
	}
	
	/**
	 *  최초 슬라이드 튜토리얼을 봤는지 확인
	 */
	public void setFirstSlide(String str) 
	{
		mEditor.putString("slide", str);
		mEditor.commit();
	}

	/**
	 *  최초 슬라이드 튜토리얼을 봤는지 확인	 
	 *   */
	public String getFirstSlide() 
	{
		return mSharedPreferences.getString("slide",  "false");
	}
		
	
	/**
	 * 디바이스의 유니크한  ID값을 저장
	 * 
	 * @param id
	 */
	public void setDeviceID(String id) {
		mEditor.putString("UniqueDeviceID", id);
		mEditor.commit();
	}
	
	/**
	 * 디바이스의 유니크한 ID값을 반환
	 * 
	 * @return String 값이 지정되지않은 경우 ""
	 */
	public String getDeviceID() {
		String result =mSharedPreferences.getString("UniqueDeviceID", "");
		return result;
	}

	// 내 MDN을 저장 한다.
	public void setShared_MDN(String str)
	{
		MLog.write(Log.ERROR, "","setShared_MDN("+str+")" );
		mEditor.putString("MDN", str.replace("-", ""));
		mEditor.commit();
	}
	public String getShared_MDN()
	{
		return mSharedPreferences.getString("MDN", "");
	}
	
	// 내 닉네임을 저장 한다.
	public void setShared_NAME(String str)
	{
		mEditor.putString("NAME", str);
		mEditor.commit();
	}
	public String getShared_NAME()
	{
		return mSharedPreferences.getString("NAME", "");
	}
	

	// 내 SEQ을 저장 한다.
	public void setShared_SEQ(String str)
	{
		mEditor.putString( "SEQ", str);
		mEditor.commit();
	}
	public String getShared_SEQ()
	{
		return mSharedPreferences.getString("SEQ", "");
	}

	/**
	 * 설정변경시 필요한 서비스Id 
	 * @param id
	 */
	public void setBasic_ss_seq(String id) {
		mEditor.putString("basic_ss_seq", id);
		mEditor.commit();
	}
	/**
	 * 설정변경시 필요한 서비스ID값을 반환
	 * 
	 * @return String 값이 지정되지않은 경우 ""
	 */
	public String getBasic_ss_seq() {
		return mSharedPreferences.getString("basic_ss_seq", "");
	}
	
	// 스타링의 최초 실행을 저장
	public void setFirstRun(String str)
	{
		mEditor.putString( "FirstRun", str);
		mEditor.commit();
	}
	public String getFirstRun()
	{
		return mSharedPreferences.getString("FirstRun", "false");
	}

	//기존가입자라면 설정한다 
	public void setUsedtRun(String str)
	{
		mEditor.putString( "UsedRun", str);
		mEditor.commit();
	}
	public String getUsedRun()
	{
		return mSharedPreferences.getString("UsedRun", "false");
	}
	
	//사용자의 디스플레이 사이즈를 저장
	public void setDisplaySize(String str)
	{
		mEditor.putString( "Displaysize", str);
		mEditor.commit();
	}
//	public String getDisplaySize()
//	{
//		return mSharedPreferences.getString("Displaysize", LCommonFunction.get_Display_Matrix(mContext));
//	}	
	
	//긴급 공지사항 표시
	public void setShowNotice(String str)
	{
		mEditor.putString( "EM_Notice", str);
		mEditor.commit();
	}
	public String getShowNotice()
	{
		return mSharedPreferences.getString("EM_Notice", "false");
	}
	
	
	//긴급공지사항의 표시시간을 저장 
	public void setEM_Notice_Count(int str)
	{
		mEditor.putInt("Notice_Count", str);
		mEditor.commit();
	}
	public int getEM_Notice_Count()
	{
		return mSharedPreferences.getInt("Notice_Count", 0);
	}
	
	//긴급공지사항의 ID를 저장한다
	public void setEM_Notice_SEQ(String str)
	{
		mEditor.putString("Notice_SEQ", str);
		mEditor.commit();
	}
	public String getEM_Notice_SEQ()
	{
		return mSharedPreferences.getString("Notice_SEQ", "0");
	}
	
	//긴급 공지사항의 데이터
	public void setEM_NoticeData(String str)
	{
		mEditor.putString( "EM_Notice_Data", str);
		mEditor.commit();
	}
	public String getEM_NoticeData()
	{
		return mSharedPreferences.getString("EM_Notice_Data", "");
	}
	
	
	//긴급 공지사항의 종료 
	public void setEM_NoticeExit(String str)
	{
		mEditor.putString( "EM_Notice_Exit", str);
		mEditor.commit();
	}
	public String getEM_NoticeExit()
	{
		return mSharedPreferences.getString("EM_Notice_Exit", "false");
	}
	
	//알람소리 설정
	public void setAlarmSound(String str)
	{
		mEditor.putString( "AlarmSound", str);
		mEditor.commit();
	}
	public String getAlarmSound()
	{
		return mSharedPreferences.getString("AlarmSound", "true");
	}
	
	public void setNowPosition(String str)
	{
		mEditor.putString( "NowPosition", str);
		mEditor.commit();
	}
	public String getNowPosition()
	{
		return mSharedPreferences.getString("NowPosition", "true");
	}
	
	
	public void setTitleLastDate(String str)
	{
		mEditor.putString( "TitleLastDate", str);
		mEditor.commit();
	}
	public String getTitleLastDate()
	{
		return mSharedPreferences.getString("TitleLastDate", "");
	}
	
		
	public void setDataAlertPopup(String str)
	{
		mEditor.putString( "DataAlertPopup", str);
		mEditor.commit();
	}
	public String getDataAlertPopup()
	{
		return mSharedPreferences.getString("DataAlertPopup", "false");
	}


	public void setUserName(String name)
	{
		mEditor.putString( "TempUserName", name);
		mEditor.commit();
	}
	public String getUserName()
	{
		return mSharedPreferences.getString("TempUserName", "user");
	}


}


