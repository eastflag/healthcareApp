package kr.co.aura.mtelo.healthcare;

import android.os.Environment;
import android.util.Log;


public class Define {

	/**
	 * log에 관한 정의
	 */
	//public static boolean LOG =true; //개발자 모드 true , 상용모드 false 로 변경..... 
	//public static boolean LOG =true; //개발자 모드 true , 상용모드 false 로 변경.....
	public static boolean LOG = true; //개발자 모드 true , 상용모드 false 로 변경.....
	public static String LOG_FILTER = "HealthCare";
	
	
	//DB관련정보
	public static String URI_AUTHORITY = "kr.co.aura.mtelo.healthcare";
	public static String URI = "content://"+Define.URI_AUTHORITY+ "/";
	  
	///릴리즈 서버 아이피  변경시 수정... 2015.02.26 tharaud 추가 
	//public static String SERVER_IP = "http://210.96.71.161/HealthCare/";//TTA
	//public static String SERVER_IP = "http://210.127.55.205/HealthCare/"; //Real
	public static String SERVER_IP = "http://210.127.55.205:82/HealthCare/"; //Real
	//public static String SERVER_IP = "http://10.10.106.79:8080/HealthCare/"; //Real
	//public static String SERVER_IP = "http://192.168.0.8:8081/healthcare/"; //
	//public static String SERVER_IP = "http://192.168.0.100:7070/HealthCare/"; //시연
	//public static String SERVER_IP = "http://106.245.237.196:7070/HealthCare/"; //시연

	
	//이미지 로더 관련 
	public static String DISK_CACHE_PATH =Environment.getExternalStorageDirectory() + "/Android/data/"+URI_AUTHORITY + "/temp";
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	 
	
	//네트워크 관련 
	public static int SOCKET_TIMEOUT = 15000;//300000
	public static String ServerAdd;
	public void setNetUri(String add)
	{
		//Log.i("Define", " => 1 Server URL :::"+add);
		ServerAdd = add;
	}

	public static Object getNetUrl() 
	{
		if (LOG) {
			Log.i("Define", " => 2 Server URL :::"+ServerAdd);
			if(ServerAdd == null)
				return SERVER_IP;
			else
				return ServerAdd;

		} else {
			//return "http://210.127.55.205/HealthCare/"; // 실서버
			//return "http://192.168.0.8:8081/healthcare/"; // 테스트 서버
			return SERVER_IP; // TTA 테스트 서버
		}
	}
	 
	 public static String SMS_CERF = "SMSCertification";
	 public static String SIGNUP = "SignUp";
	 public static String VIDEO_LIST = "GetVideoList";
	 public static String BODY_MEASURE = "GetBodyMeasureInfo";
	 public static String STUDENT_LIST = "GetStudent";
	 public static String STUDENT_INFO = "GetBodyMeasureSummary";
	 public static String GET_BMI = "GetRestApiList/GetBmi";
	 public static String GET_SERVICE_INFO ="GetServiceInfo";
	 public static String GET_NOTICE_COUNT ="notice/GetCount";
	 public static String FILE_UPLOAD_CONTENT ="UploadContent";
	 public static String CHECK_EVENT = "CheckEvent";
	 public static String SIMLI_VIDEO_LIST = "GetSimliVideoList"; //Simli Test



	//심리검사
	public static String TEST_URL ="http://210.127.55.205:82/HealthCare/";
	public static String MENTAL_LIST ="simli/type_list";
	public static String MENTAL_TEST_LIST ="simli/qna_list";
	public static String MENTAL_TEST_RESULT ="simli/simli_review";

	//운동량
	public static String EXERCISE_INFO ="exercise/main";
	public static String EXERCISE_DETAIL_INFO ="exercise/view";
	public static String EXERCISE_HISTORY ="exercise/history";

}
