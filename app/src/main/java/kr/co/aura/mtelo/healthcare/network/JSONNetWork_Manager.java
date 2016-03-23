package kr.co.aura.mtelo.healthcare.network;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.network.NetWork.Call_Back;
import kr.co.aura.mtelo.healthcare.util.MLog;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class JSONNetWork_Manager //implements Call_Back
{
	private final static String LOG_TAG = "JSONNetWork_Manager";
	
	/**
	 * SMS 인증코드획득
	 * @param myMDN
	 * @param context
	 * @param call_back
	 */
	public static void request_SMSCertification (String myMDN, Context context, final Call_Back call_back)
	{
		//mCall_Back = call_back;
//		Resources Resource = context.getResources();
		
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.SMS_CERF);
		String keys[] = {JSONNetWork.KEY_MDN};
		String values[] = { myMDN};
		MLog.write(context, "mdn = "+myMDN );
		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setPostParams(keys, values);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
		
	}
	
	/**
	 * 헬스케어 가입인터페이스
	 * @param myMDN
	 * @param gcm_id gcm
	 * @param context
	 * @param call_back
	 */
	public static void request_SignUP(String myMDN, String gcm_id,  Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.SIGNUP);
		String keys[] = {JSONNetWork.KEY_MDN, JSONNetWork.KEY_GCM_ID};
		String values[] = { myMDN, gcm_id};
		MLog.write(context, "mdn = "+myMDN +",gcm= "+gcm_id);
		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setPostParams(keys, values);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}
	
	/**
	 * 추천운동 리스트 획득
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_GetVideoList  ( String sId, String userId, String grdeId, Context context, final Call_Back call_back)
	{
		if(grdeId == null || grdeId.length() == 0 ) grdeId = "1";
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.VIDEO_LIST);
		String keys[] = {JSONNetWork.KEY_VIDEO_MASTER_ID, JSONNetWork.KEY_VIDEO_SCHOOL_ID, JSONNetWork.KEY_USER_ID};
		String values[] = { grdeId, sId, userId};
		MLog.write(context, "masterGradeId = "+grdeId + " schoolGradeId= "+sId + " userId=" + userId);
		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setPostParams(keys, values);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}

	
	
	/**
	 *	아이들 리스트&정보 획득
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_SimliGetVideoList  ( String userId, Context context, final Call_Back call_back)
	{
		
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.SIMLI_VIDEO_LIST);
		String keys[]= {JSONNetWork.KEY_USER_ID};
		String values[] =  {userId};
		MLog.write(context, "userId = "+userId );
		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setPostParams(keys, values);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}

	////////////////////////////////////////////////////////////////////
	
	/**
	 * 아이들 리스트&정보 획득
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_GetBodyMeasureInfo(String myMDN, String gcm_id,  Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.BODY_MEASURE);
		
		String keys[] = {JSONNetWork.KEY_MDN, JSONNetWork.KEY_GCM_ID};
		String values[] = { myMDN, gcm_id};
		MLog.write(Log.ERROR, "request_GetBodyMeasureInfo",  "mdn = "+myMDN +",gcm= "+gcm_id);
		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setPostParams(keys, values);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}
	
	/**
	 * 13.9.23 신규 
	 * 아이들 리스트 획득
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_Get_Student_List(String myMDN, String gcm_id,  Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.STUDENT_LIST);
		
		String keys[] = {JSONNetWork.KEY_MDN, JSONNetWork.KEY_GCM_ID};
		String values[] = { myMDN, gcm_id};
		MLog.write(Log.ERROR, "request_Get_Student_List",  "mdn = "+myMDN +",gcm= "+gcm_id);
		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setPostParams(keys, values);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}
	
	
	
	/**
	 * 13.9.23 신규
	 * 아이들 인포 획득
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_Get_Student_Info(String userId, String token,  Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.STUDENT_INFO);
		
		String keys[] = {JSONNetWork.KEY_USER_ID, JSONNetWork.KEY_USER_TOKEN};
		String values[] = { userId, token};
		MLog.write(Log.ERROR, "request_Get_Student_Info",  "userId = "+userId +",token= "+token);
		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setPostParams(keys, values);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}
	
	/**
	 * 13.10.08 신규 
	 * MasterGradeId획득
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_Get_MasterGradeId(String userId, String token,  Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.GET_BMI);
		
		String keys[] = {JSONNetWork.KEY_USER_ID, JSONNetWork.KEY_USER_TOKEN};
		String values[] = { userId, token};
		MLog.write(Log.ERROR, "request_Get_MasterGradeId",  "userId = "+userId +",token= "+token);
		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setPostParams(keys, values);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}
	
	
	/**
	 * 13.10.10 신규 
	 * 서비스 실행정보 획득
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_ServiceInfo(Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.GET_SERVICE_INFO);
		
		MLog.write(Log.ERROR, "request_request_ServiceInfo", "");
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}
	
	/**
	 * 13.10.10 신규 
	 * 신규 공지사항 정보를 획득 
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_Notice_Count(Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.GET_NOTICE_COUNT);
		
		MLog.write(Log.ERROR, "request_Notice_Count","");		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}
	
	/**
	 * 13.10.16 비디오리스트 - 파일업로드 인터페이스 추가
	 * @param Seq
	 * @param context
	 * @param call_back
	 */
	public static void request_Update_Content(String mdn, String file, Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.FILE_UPLOAD_CONTENT);

		String keys[] = {JSONNetWork.KEY_MDN};
		String values[] = { mdn};
		String keys_f[] = {JSONNetWork.KEY_FILE };
		String fvalues[] = { file};
		String format[] = {"image/png"};
		MLog.write(Log.ERROR, "request_Update_Content","mdn = "+ mdn +", file = "+ file);	
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_UPLOAD);
		if (file == null || file.equalsIgnoreCase("" ) )
		{
			jn.setPostParams(keys, values);
		}
		else
		{
			jn.setPostParams(keys, values);
			if ( jn.setUpLoadFiles( keys_f, fvalues, format) )
			{
				jn.setMRequestUrl(strbuff.toString());
			}
			else
			{
				MLog.write(LOG_TAG, "ERROR = There's no need to upload files");
			}
		}
		
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}
	
	/**
	 * 14.08.12 신규 
	 * 이벤트 팝업창
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_EventCheck(Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.CHECK_EVENT);
		
		MLog.write(Log.ERROR, "request_request_CHECK_EVENT","");		
		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}



	/**
	 * 16.2.18 신규
	 * 심리검사 list 조회
	 * @param type
	 * @param context
	 * @param call_back
	 */
	public static void request_Get_Mental_Info(String userId,  Context context, final Call_Back call_back)
	{
		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.MENTAL_LIST);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID +"=" + userId);

		MLog.write(Log.ERROR, "request_Get_Mental_Info", "userId = " + userId +", url :"+ strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}


	/**
	 * 16.2.18 신규
	 * 심리검사 뮨재리스트 조회
	 * @param type
	 * @param call_back
	 */
	public static void request_Get_Mental_TestList(String simliId,  Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.MENTAL_TEST_LIST);
		strbuff.append("?" + JSONNetWork.KEY_MENTAL_ID +"=" + simliId);

		MLog.write(Log.ERROR, "request_Get_Mental_TestList", "simliId = " + simliId +", url :"+ strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}



	/**
	 * 16.3.7 신규
	 * 운동량 정보 조회
	 * @param type
	 * @param call_back
	 */
	public static void request_Get_Exercise_Info(String simliId, String exerciseId, Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.EXERCISE_INFO);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID +"=" + simliId +"&" +JSONNetWork.KEY_EXERCISE_ID +"="+ exerciseId);

		MLog.write(Log.ERROR, "request_Get_Exercise_Info", "simliId = " + simliId + ", url :" + strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}


	/**
	 * 16.3.10 신규
	 * 운동량 디테일 정보 조회
	 * @param type
	 * @param call_back
	 */
	public static void request_Get_Exercise_Detail_Info(String simliId, String exerciseId, Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.EXERCISE_DETAIL_INFO);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID +"=" + simliId +"&" +JSONNetWork.KEY_EXERCISE_ID +"="+ exerciseId);

		MLog.write(Log.ERROR, "request_Get_Exercise_Detail_Info", "simliId = " + simliId + ", url :" + strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}


	/**
	 * 16.3.10 신규
	 * 운동량 히스토리  정보 조회
	 * @param type
	 * @param call_back
	 */
	public static void request_Get_Exercise_History(String simliId, String exerciseId, Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.EXERCISE_HISTORY);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID +"=" + simliId +"&" +JSONNetWork.KEY_EXERCISE_ID +"="+ exerciseId);

		MLog.write(Log.ERROR, "request_Get_Exercise_History", "simliId = " + simliId + ", url :" + strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}



	/**
	 * 16.3.10 신규
	 * 운동량 히스토리  정보 조회
	 * @param type
	 * @param call_back
	 */
	public static void request_Get_Simli_AnswerList(String userid, String simliId,  Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.MENTAL_TEST_ANSWER_LIST);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID +"=" + userid+"&" +JSONNetWork.KEY_MENTAL_ID+"="+ simliId);

		MLog.write(Log.ERROR, "request_Get_Simli_AnswerList", "userId= "+userid +" simliId = " + simliId + ", url :" + strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}

	public static void request_Get_Simli_Result(String userid, String simliId,  Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.MENTAL_TEST_RESULT);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID +"=" + userid+"&" +JSONNetWork.KEY_MENTAL_ID+"="+ simliId);

		MLog.write(Log.ERROR, "request_Get_Simli_Result", "userId= "+userid +" simliId = " + simliId + ", url :" + strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}

	public static void request_insert_Simli_Result(String userid, ArrayList<String> listResult, Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
		strbuff.append(Define.TEST_URL);
		strbuff.append(Define.MENTAL_TEST_INSERT);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID + "=" + userid+"&" +JSONNetWork.KEY_MENTAL_ANSWER + "=");

		String answerData = "";
		for (String item:listResult) {
			answerData += item;
			answerData += ",";
		}

		strbuff.append(answerData);

		// TODO result setting

		MLog.write(Log.ERROR, "request_Get_Simli_Result", "url :" + strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();


//		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
//		strbuff.append(Define.SMS_CERF);
//		String keys[] = {JSONNetWork.KEY_MDN};
//		String values[] = { myMDN};
//		MLog.write(context, "mdn = "+myMDN );
//
//		JSONNetWork jn = new JSONNetWork(context);
//		jn.setMRequestType(JSONNetWork.REQUESTTYPE_POST);
//		jn.setPostParams(keys, values);
//		jn.setMRequestUrl(strbuff.toString());
//		jn.set_Call_Back(call_back);
//		jn.start_Request();
	}

	public static void request_Get_Simli_Result_Check(String userid, String simliId, Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.MENTAL_TEST_RESULT_CHECK);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID +"=" + userid+"&" +JSONNetWork.KEY_MENTAL_ID+"="+ simliId);

		MLog.write(Log.ERROR, "request_Get_Simli_Result_check", "url :" + strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}



	/**
	 * 16.3.10 신규
	 * 운동량 히스토리  정보 조회
	 * @param type
	 * @param call_back
	 */
	public static void request_Get_User_Exercise_Data(String userid, String exerciseId, String averageType, String groupType, Context context, final Call_Back call_back)
	{

		StringBuffer strbuff = new StringBuffer();
//		strbuff.append((String)Define.getNetUrl());
		strbuff.append( Define.TEST_URL);
		strbuff.append(Define.EXERCISE_DATA);
		strbuff.append("?" + JSONNetWork.KEY_USER_ID +"=" + userid+ "&" + JSONNetWork.KEY_EXERCISE_ID+"="+ exerciseId +"&"+
				JSONNetWork.KEY_EXERCISE_AVERAGE_TYPE +"="+ averageType +"&"+ JSONNetWork.KEY_EXERCISE_GROUP_TYPE +"="+ groupType);

		MLog.write(Log.ERROR, "request_Get_User_Exercise_Data", "url :" + strbuff);

		JSONNetWork jn = new JSONNetWork(context);
		jn.setMRequestType(JSONNetWork.REQUESTTYPE_GET);
		jn.setMRequestUrl(strbuff.toString());
		jn.set_Call_Back(call_back);
		jn.start_Request();
	}


}
