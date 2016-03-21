package kr.co.aura.mtelo.healthcare.network;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.NetWork.Call_Back;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.MLog;

public class JSONNetWork 
{
	// TODO Communication protocols define the parameters. Start
	//private static Call_Back mCall_Back;
	private Call_Back mCall_Back;
	
	public void set_Call_Back(Call_Back obj)
	{
		MLog.write(this, "set_Call_Back("+obj+")");
		mCall_Back = obj;
	}
	// TODO Communication protocols define the parameters. End
	
	
	
	// TODO Define  constants. Start
	public final static int REQUESTTYPE_GET = 10;
	public final static int REQUESTTYPE_POST = REQUESTTYPE_GET + 1;
	public final static int REQUESTTYPE_UPLOAD = REQUESTTYPE_GET + 2;
	
	//json  공통사용
	public final static String RETURN_KEY_RESULT = "result";
	public final static String RETURN_KEY_ERRMSG = "errMsg";
	public final static String RETURN_KEY_VALUE =  "value";
	public final static String KEY_MDN = "mdn";
	
	//SMSCertification
	public final static String RETURN_KEY_SMS_CODE = "certificationNumber";
	
	//GetVideoList
	public final static String RETURN_KEY_VIDEO_LIST = "videoInfos";
	public final static String KEY_VIDEO_MASTER_ID = "masterGradeId";
	public final static String KEY_VIDEO_SCHOOL_ID = "schoolGradeId";
	
	
	//SimliGetVideoList
		public final static String SIMLI_RETURN_KEY_VIDEO_LIST = "simlivideoInfo";
		//public final static String SIMLI_KEY_VIDEO_USER_ID = "userId";
	
	//Sign UP
	public final static String KEY_GCM_ID = "registrationId";
	
	//Child_List
	public final static String RETURN_KEY_CHILD_INFO = "bodyMeasureInfos";
	public final static String KEY_USER_ID= "userId"; //�ɸ� userid�ε� ��뿹��
	public final static String KEY_USER_NAME= "name";
	public final static String KEY_USER_SEX= "sex";
	public final static String KEY_USER_AGE= "age";
	public final static String KEY_USER_BIRTHDATE= "birthDate";
	public final static String KEY_USER_DATE= "measureDate";
	public final static String KEY_USER_HEIGHT= "height";
	public final static String KEY_USER_HEIGHT_STATUS= "heightStatus";
	public final static String KEY_USER_WEIGHT= "weight";
	public final static String KEY_USER_WEIGHT_STATUS= "weigthStatus";
	public final static String KEY_USER_BMI= "bmi";
	public final static String KEY_USER_BMI_STATUS= "bmiStatus";
	public final static String KEY_USER_G_POINT= "growthGrade";
	public final static String KEY_USER_PPM= "ppm";
	public final static String KEY_USER_PPM_STATUS= "smokeStatus";
	public final static String KEY_USER_COHD= "cohd";

	public final static String KEY_USER_TOKEN= "token";
	public final static String KEY_USER_SCHOOL_GID= "schoolGradeId"; //13.10.04 schoolGradeId 추가
	public final static String KEY_USER_MASTER_GID= "bmiGradeId"; //13.10.08 MasterGradeId 추가
	public final static String KEY_SERVICE_INFO= "serviceEnable"; //13.10.10 serviceEnable 추가
	public final static String KEY_SERVICE_MESSAGE= "serviceNotice";  //13.10.10 serviceNotice 추가

	//Video_List
	public final static String KEY_FILE= "file";   //13.10.16 serviceNotice 추가

	//심리검사
	public final static String KEY_MENTAL_ID= "simliId";
	public final static String KEY_MENTAL_ANSWER= "answer";

	//운동량 보기
	public final static String KEY_EXERCISE_ID= "exerciseId";

	private String mServerUrl = null; // 서버 url 정보
	private int mRequestType;
	private HttpClient mHttpClient;
//	private List<NameValuePair> mParams = new ArrayList<NameValuePair>();
	private JSONObject mParams = new JSONObject();
	private HttpPost mHttpPost;
	private HttpGet mHttpGet;
	private MultipartEntity mMultipartEntity;
	private Context mContext;
	private CPreferences mCPreferences;
	private LTimeOut_Kepper mKepper;

	
	public JSONNetWork(Context context)
	{
		mContext = context;
	}

	
	
	private String getMRequestUrl()
	{
		return mServerUrl;
	}


	
	public void setMRequestUrl(String serverUrl)
	{
		mServerUrl = serverUrl;
		
		MLog.write(this, "setMRequestUrl("+serverUrl+" )");
		
		switch ( getMRequestType() )
		{
		case REQUESTTYPE_GET:
			{
				mHttpGet = new HttpGet(getMRequestUrl());
			}
			break;
			
		case REQUESTTYPE_POST:
			{
				//04. 전송할 URL를 설정
				mHttpPost = new HttpPost(mServerUrl);
//				mHttpPost.setHeader("Connection", "Keep-Alive");
//				mHttpPost.setHeader("Accept-Charset", "UTF-8");
//				mHttpPost.setHeader("ENCTYPE", "multipart/form-data");
				
				mHttpPost.setHeader("Accept", "application/json");
				mHttpPost.setHeader("Content-type", "application/json");
				
				// 06. URL에 데이터를 설정
//		        UrlEncodedFormEntity entity;
				StringEntity entity;
				try
				{
//					entity = new UrlEncodedFormEntity(mParams, "UTF-8");
					if(mParams == null )
						 mParams = new JSONObject();
					
					entity = new StringEntity(mParams.toString(), "UTF-8");
					mHttpPost.setEntity(entity);
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}
			break;
			
		case REQUESTTYPE_UPLOAD:
			mHttpPost = new HttpPost(mServerUrl);
			mHttpPost.setEntity(mMultipartEntity);
			break;
		}
	}
	
	
	
	private int getMRequestType()
	{
		return mRequestType;
	}



	public void setMRequestType(int requestType)
	{
		mRequestType = requestType;
		
		init_Variable();
		
		HttpParams hp = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(hp, Define.SOCKET_TIMEOUT);
		
		
		mHttpClient = new DefaultHttpClient(hp);
		
		switch ( mRequestType )
		{
		case REQUESTTYPE_GET:
			break;
			
		case REQUESTTYPE_POST:
			{
				///02. Parameter를 재설정
				mHttpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			}
			break;
			
		case REQUESTTYPE_UPLOAD:
			{
				mMultipartEntity = new MultipartEntity();
			}
			break;
		}
	}



	/**
	 * 
	 */
	private void init_Variable()
	{
		boolean isError = false;
		if (mHttpGet != null && mHttpGet.isAborted() == false )
		{
			isError = true;
		}
		if (mHttpPost != null && mHttpPost.isAborted() == false )
		{
			isError = true;
		}
		
		MLog.write(this, "isError = "+isError);
		
		if ( mHttpClient != null )
		{
			mHttpClient.getConnectionManager().shutdown();
			mHttpClient = null;
		}
		
		if ( mParams != null )
//			mParams.clear();
			mParams = null;
		
		if ( mHttpPost != null )
		{
			mHttpPost.abort();
			mHttpPost = null;
		}
		
		if ( mHttpGet != null )
		{
			
			mHttpGet.abort();
			mHttpGet = null;
		}
		
		if ( mMultipartEntity != null )
			mMultipartEntity = null;
		
		if ( isError )
		{
			mCall_Back.onError("Network Timeout Exception");
		}
	}
	
	
	
	public boolean setUpLoadFiles( String key[], String file[], String format[])
	{
		// 파일셋팅
		int count = key.length;
		String full_path = null;
		
		for ( int i = 0; i < count; i ++ )
		{
			full_path = file[i];
			MLog.write(this, "setUpLoadFiles(), path + file = " + full_path);
			File fp = new File(full_path);
			
			if ( fp.exists() )  // 파일이 존재하는가!?
			{
				mMultipartEntity.addPart(key[i], new FileBody(fp, format[i]));
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	
	
	public void setPostParams(String key[], String value[])
	{
		//03. 전송할 데이터를 구성
		int count = key.length;
		
		switch ( mRequestType )
		{
		case REQUESTTYPE_GET:
			break;
			
		case REQUESTTYPE_POST:
			{
				if(mParams == null) mParams = new JSONObject();
				for ( int i = 0; i < count; i ++ )
				{
					try {
						mParams.put(key[i], value[i]);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//					mParams.add(new BasicNameValuePair(key[i], value[i]));
			}
			break;
			
		case REQUESTTYPE_UPLOAD:
			{
				for ( int i = 0; i < count; i ++ )
				{
					try
					{
						mMultipartEntity.addPart(key[i], new StringBody(value[i]));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			break;
		}
	}



	private String mNetworkErrorMsg;
	public void start_Request()
	{
		MLog.write(this, "start_Request()");
		new Thread(new request_Data_Runnable()).start();
		
		if ( mKepper == null )
			mKepper = new LTimeOut_Kepper();
		
		mKepper.Start_Kepper(Define.SOCKET_TIMEOUT, new LTimeOut_Kepper.Kepper_CallBack()
		{
			@Override
			public void onTimeOut()
			{
				MLog.write(this, "onTimeOut()");
				init_Variable();
			}
		});
	}
	
	
	
	private class request_Data_Runnable implements Runnable
	{
		@Override
		public void run()
		{
			MLog.write(this, "request_Data_Runnable(). run()");
			request_Data();
		}
	}

	
	
	private void request_Data()
	{
		MLog.write(this, "request_Data()");
		
		long sT = System.currentTimeMillis();
		
		String Result = null;
		HttpEntity resEntity = null;
		
		try
		{
			HttpResponse response = null;
			
			switch ( getMRequestType() )
			{
			case REQUESTTYPE_GET:
				response = mHttpClient.execute(mHttpGet);
				break;
				
			case REQUESTTYPE_POST:
			case REQUESTTYPE_UPLOAD:
				response = mHttpClient.execute(mHttpPost);
				break;
			}
			
			resEntity = response.getEntity();
			if (resEntity != null)
			{			
				Result = EntityUtils.toString(resEntity);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
		long eT = System.currentTimeMillis();
		MLog.write(this, "request Time = " + (eT - sT) + " MS");
		
		if ( Result != null )
		{			
			mCall_Back.onGetResponsString(Result);
		}
		else
		{
			//mCall_Back.onError("ERROR");
			mCall_Back.onError(mContext.getResources().getString(R.string.network_failed));
		}
		
		if (mKepper != null)
			mKepper.Stop_Kepper();
		
		if(mHttpClient != null)
		{
			mHttpClient.getConnectionManager().shutdown();
		}
	}

	
	
	
	//와글 연동관련 코드
	public void setWagle_Headers_For_TypeGet(String key, String value)
	{
		mHttpGet.addHeader(key, value);
	}
	
	public void setWagle_Headers_For_TypePost(String key, String value)
	{
		mHttpPost.addHeader(key, value);
	}

	public boolean setWagle_UpLoadFiles(String path, String key, String format)
	{
		// 파일셋팅
		{
			MLog.write(this, "setWagle_UpLoadFiles(), path + file = " + path);
			File fp = new File(path);
			
			if ( fp.exists() )   // 파일이 존재하는가!?
			{
				mMultipartEntity.addPart(key, new FileBody(fp, format));
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
}
