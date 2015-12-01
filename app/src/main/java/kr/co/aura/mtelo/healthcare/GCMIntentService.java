package kr.co.aura.mtelo.healthcare;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;

import kr.co.aura.mtelo.healthcare.intro.Intro;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork.Call_Back;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.LCommonFunction;
import kr.co.aura.mtelo.healthcare.util.MLog;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String tag = "GCMIntentService";
	public static final String SEND_ID = "1081274531985";//"949256309433"
	public static final String API_KEY = "AIzaSyB2TTsHaCHrj9kufCgwl675kMrKcuslyrI";// "AIzaSyCdYEQUUUIkxzX9rcvsil7IyVCm8U4V62M"

	private final int GET_USER_DATA = 1000;
	private final int GET_USER_DATA_DOWN = 1001;
	private final int GET_USER_DATA_ERROR = 1002;
	
	public GCMIntentService(){
		this(SEND_ID);
		Log.e(tag, "GCMIntentService ");
		}
	
	public GCMIntentService(String senderId) { super(senderId); }

	@Override
	protected void onMessage(Context context, Intent intent) {
		// 가입하지않은경우 GCM의 동작을 막는다
		if (new CPreferences(context).getSignUP().equalsIgnoreCase("false"))
			return;

		Bundle b = intent.getExtras();

		Iterator<String> iterator = b.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = b.get(key).toString();
			MLog.write(tag, "onMessage. " + key + " : " + value);
		}

		String c2dm_msg = intent.getExtras().getString("msg");
		try {
			// 서버에서 보낸 메시지의 타입이 UTF-8로 인코딩 하였으므로 여기서는 디코딩!
			c2dm_msg = URLDecoder.decode(c2dm_msg, "UTF-8"); // 2014.10.10. HYT.
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MLog.write(Log.WARN, c2dm_msg, "c2dm_msg:" + c2dm_msg);

		String userID = intent.getExtras().getString("userID");
		userID = ""; // User ID 값을 서버에서 내려주지 않기로 했다. 그러므로 "" 값을 넣었다. 다른곳 null 처리 하기 힘들어서...
		             // 2014.10.16. HYT.
		GET_GCM(new Message_Data(c2dm_msg, userID));
		
//		 make_noti(c2dm_msg, userID );
		 
//		 Message m = handler.obtainMessage();
//		 m.obj = (Message_Data)new Message_Data(c2dm_msg, userID);
//		 m.arg1 = GET_USER_DATA;
//		 handler.sendMessage(m);
		 
	}


	class Message_Data
	{
		String msg,userID, KG,KG_STATUS, CM, CM_STATUS,BMI ,BMI_STATUS,MASTER_ID, SCHOOL_ID, PPM, PPM_STATUS,COHD,
		G_POINT, LASTDATE, NAME, SEX;
		
		public Message_Data()
		{
		}
		
		public Message_Data(String msg, String userID) {
			super();
			this.msg = msg;
			this.userID = userID;
		}

		public Message_Data(String msg, String userID, String kG,
				String kG_STATUS, String cM, String cM_STATUS, String bMI,
				String bMI_STATUS, String mASTER_ID,String school_id,  String pPM,
				String pPM_STATUS, String cOHD, String g_POINT, String lASTDATE, String name, String sex) {
			super();
			this.msg = msg;
			this.userID = userID;
			KG = kG;
			KG_STATUS = kG_STATUS;
			CM = cM;
			CM_STATUS = cM_STATUS;
			BMI = bMI;
			BMI_STATUS = bMI_STATUS;
			MASTER_ID = mASTER_ID;
			SCHOOL_ID = school_id;
			PPM = pPM;
			PPM_STATUS = pPM_STATUS;
			COHD = cOHD;
			G_POINT = g_POINT;
			LASTDATE = lASTDATE;
			this.NAME 	= name;
			this.SEX	= sex;
		}
		
	}
	
	public void GET_GCM(final Message_Data data)
	{ 
		//get_User_Data(data);

		make_noti2(data);
//		Thread thread = new Thread(new Runnable() { 
//			public void run() {
//				Message m = new Message();
//				m.obj = (Message_Data)data;
//				m.arg1 = GET_USER_DATA;
//				handler.sendMessage(m);
//				
//			} 
//		}); 
//		thread.start(); 
	} 
	
	private Handler handler = new Handler() { 
		public void handleMessage(Message msg) 
		{ 
			switch (msg.what) {
			case GET_USER_DATA:
				get_User_Data((Message_Data) msg.obj);
				break;
			case GET_USER_DATA_DOWN :
				make_noti((Message_Data) msg.obj);
				break;
			}
//			make_noti((String)msg.obj);
		} 
	};
	
	
	private void get_User_Data(Message_Data data)
	{
		final String MSG = data.msg;
		JSONNetWork_Manager.request_Get_Student_Info(data.userID, "", getApplicationContext(), new Call_Back() 
		{
			@Override
			public void onRoaming(String message) {	}
			
			@Override
			public void onGetResponsString(String data) 
			{
				MLog.write(Log.ERROR, "", "request_Get_Student_List()\n " +data );
				String Result = null, errMsg = null;
				JSONObject jsonObject = null;
				try
				{
					jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
					Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); // 결과값
					errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); // 에러 메세지
					
					if ( Result != null && Result.equalsIgnoreCase("0") )
					{
						ContentValues values = new ContentValues();
						JSONObject array = new JSONObject(jsonObject.optString(JSONNetWork.RETURN_KEY_VALUE));
						Message_Data Data = new Message_Data();
						if(array != null)
						{
							MLog.write(Log.ERROR,this.toString(), "array= "+array);
							Data.msg 		= MSG;
							Data.userID 	= array.optString(JSONNetWork.KEY_USER_ID);
							Data.CM 		= array.optString(JSONNetWork.KEY_USER_HEIGHT);
							Data.CM_STATUS	= array.optString(JSONNetWork.KEY_USER_HEIGHT_STATUS);
							Data.KG 		= array.optString(JSONNetWork.KEY_USER_WEIGHT);
							Data.KG_STATUS	= array.optString(JSONNetWork.KEY_USER_WEIGHT_STATUS);
							Data.BMI		= array.optString(JSONNetWork.KEY_USER_BMI);
							Data.BMI_STATUS	= array.optString(JSONNetWork.KEY_USER_BMI_STATUS);
							Data.COHD		= array.optString(JSONNetWork.KEY_USER_COHD);
							Data.G_POINT	= array.optString(JSONNetWork.KEY_USER_G_POINT);
							Data.PPM		= array.optString(JSONNetWork.KEY_USER_PPM);
							Data.PPM_STATUS	= array.optString(JSONNetWork.KEY_USER_PPM_STATUS);
							Data.MASTER_ID	= array.optString(JSONNetWork.KEY_USER_MASTER_GID);
							Data.SCHOOL_ID	= array.optString(JSONNetWork.KEY_USER_SCHOOL_GID);
							Data.LASTDATE	= array.optString(JSONNetWork.KEY_USER_DATE);
							Data.NAME		= array.optString(JSONNetWork.KEY_USER_NAME);
							Data.SEX		= array.optString(JSONNetWork.KEY_USER_SEX);
						}
						Message msg = handler.obtainMessage(GET_USER_DATA_DOWN, (Object)Data);
						handler.sendMessage(msg);
					}
					else //if(Result != null && Result.equalsIgnoreCase("2") )
					{	
						Message msg = handler.obtainMessage(GET_USER_DATA_ERROR, errMsg);
						handler.sendMessage(msg);
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
			public void onError(String error) {
				MLog.write(Log.ERROR, "", "request_Get_Student_Info()\n " +error );
			}
		});
	}
	
	
	private void make_noti(Message_Data data)
	{
		 try 
		 {
			 int ID = (int)SystemClock.currentThreadTimeMillis();
			 MLog.write(Log.ERROR, "", "make noti, put extra msg = " + data.msg);
			 Intent noti_intent = new Intent(getApplicationContext(), MetroMain.class);
			 noti_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			 noti_intent.putExtra("msg", data.msg);
			 noti_intent.putExtra("userId", data.userID);
			 
			 noti_intent.putExtra("name"	, 	data.NAME);
			 noti_intent.putExtra("lastdate"	,  data.LASTDATE);
			 noti_intent.putExtra("sex"		,  	data.SEX);
			 
			 noti_intent.putExtra("cm"		,    data.CM);
			 noti_intent.putExtra("cm_status",   data.CM_STATUS);
			 noti_intent.putExtra("kg"		,  	 data.KG);
			 noti_intent.putExtra("kg_status",	 data.KG_STATUS);
			 noti_intent.putExtra("bmi"		,  	 data.BMI);
			 noti_intent.putExtra("bmi_status",  data.BMI_STATUS);
			 noti_intent.putExtra("g_point"	,  	 data.G_POINT);
			 noti_intent.putExtra("ppm"		,    data.PPM);
			 noti_intent.putExtra("ppm_status",  data.PPM_STATUS);
			 noti_intent.putExtra("cohd"	,	 data.COHD);
			 noti_intent.putExtra("schoolGradeId", data.SCHOOL_ID); //스쿨ID
			 noti_intent.putExtra("mGradeId",  	 data.MASTER_ID);//마스터 그래이드ID
			 
			 
			 
			 noti_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			 PendingIntent pi = PendingIntent.getActivity(this, ID, noti_intent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			 
			 NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			 Notification noti = new NotificationCompat.Builder(getApplicationContext())
			 									.setContentTitle(getResources().getString(R.string.app_name))
			 									.setContentText(data.msg)
			 									.setTicker("이것은 티커")
			 									.setSmallIcon(R.drawable.icon)
			 									.setAutoCancel(true)
			 									.setContentIntent(pi)
			 									.setDefaults(Notification.DEFAULT_ALL)
			 									.build();
			 notificationManager.notify(Integer.parseInt(data.userID), noti);
		 } 
		 catch (Exception e) 
		 {
			 Log.e("",  "[setNotification] Exception : " + e.getMessage());
		 }
	}
	
	
	/**
 * 사용자에게 서버에서 받은 노티를 알립니다.
	 * 
	 * ※  make_noti()와 다른점
	 *   사용자의 아이디(data.userID)의 값을 서버에서 보내주지 않기 때문에,
	 *   서버에서 전달된 null 값을 "" 로 전달되어 사용된 것임. (실제로 "" 값의 변경은 의미가 없다.)
	 *   그리고, 노티를 클릭하면 인트로 화면 부터 시작하게 된다.
	 * 
	 * @author HYT.
	 * @since 2014.10.16.
	 * @param data
	 */
	private void make_noti2(Message_Data data) {
		try {
			int ID = (int) SystemClock.currentThreadTimeMillis();
			MLog.write(Log.ERROR, "", "make noti, put extra msg = " + data.msg);
			Intent noti_intent = new Intent(getApplicationContext(), Intro.class);
			noti_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			noti_intent.putExtra("msg", data.msg);

			noti_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pi = PendingIntent.getActivity(this, ID, noti_intent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Notification noti = new NotificationCompat.Builder(
					getApplicationContext())
					.setContentTitle(getResources().getString(R.string.app_name))
					.setContentText(data.msg).setTicker(getResources().getString(R.string.app_name))    //push header
					.setSmallIcon(R.drawable.icon).setAutoCancel(true)
					.setContentIntent(pi).setDefaults(Notification.DEFAULT_ALL)
					.build();
			notificationManager.notify(0/* Integer.parseInt(data.userID) */, noti);
			
		} catch (Exception e) {
			Log.e("", "[setNotification] Exception : " + e.getMessage());
		}
	}
	
	
	@Override
	protected void onError(Context context, String errorId) {
		Log.e(tag, "onError. errorId : "+errorId);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.e(tag, "onRegistered. regId : "+regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.e(tag, "onUnregistered. regId : "+regId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.e(tag, "onRecoverableError. errorId : "+errorId);
		return super.onRecoverableError(context, errorId);
	}
}