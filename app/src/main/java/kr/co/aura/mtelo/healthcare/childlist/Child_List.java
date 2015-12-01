package kr.co.aura.mtelo.healthcare.childlist;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.MetroMain;
import kr.co.aura.mtelo.healthcare.PopEvent;
import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.database.DB_Define;
import kr.co.aura.mtelo.healthcare.database.DB_Maneger;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork.Call_Back;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.LCommonFunction;
import kr.co.aura.mtelo.healthcare.util.MLog;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager.OneButton_Handle;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gcm.GCMRegistrar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class Child_List extends SherlockActivity implements OnItemClickListener{

	private ListView mList;
	private Cursor mCursor;
	private Context mCon;
	private DB_Maneger mDBManager;
	private ChildListAdapter mAdapter; 
	
	private final int MENU_ALBUM = 1000;
	private final int MENU_CAMERA = MENU_ALBUM+1;
	private final int MENU_CLAER =  MENU_ALBUM+2;
	
	
	public final int REQ_CODE_PICK_GALLERY = 900001;
	public final int REQ_CODE_PICK_CAMERA = 900002;
	public final int REQ_CODE_PICK_CROP = 900003;
	
	public final int CHILD_CODE=404040;  ///// 자녀코드 추가 
	
	private final int DATA_DOWNLOAD = 30303;
	private final int DATA_ERROR = 30304;
	private final int DATA_ITEM_DOWNLOAD = 30305;
	private final int DATA_EMPTY = 30306;
	private final int DATA_STUDENT_EMPTY = 30307;
	private ProgressBar mProBar;
	private int maxCount , nowCount = 0;
	private String totResult = null;  //팝업 이벤트
	
	private final int ERROR_MESSAGE = 100001;
	
	static 
	{
		MLog.set_LogEnable(Define.LOG, Define.LOG_FILTER);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.child_list);
		mCon =  Child_List.this;
		
		mProBar  = (ProgressBar)findViewById(R.id.video_list_probar);
		mProBar.setVisibility(View.VISIBLE);
		mProBar.bringToFront();
		
		Intent in = getIntent();
		String userID= in.getStringExtra("userID");
		if(userID != null)
		{ //GCM으로 동작
			
		}
		
		init_ACtionBar();
		mDBManager = new DB_Maneger(mCon);
		getCursor();
		
		mCursor.moveToFirst();
//		mMap = new HashMap<String, String>();
//		if(mCursor.getCount() != 0)
//		{
//			do {//해쉬맵을 생성
//				mMap.put(mCursor.getString(DB_Define.COLUMN_CHILD_ID), "false");
//			} while (mCursor.moveToNext());
//		}
//		textInput();
		
		mList = (ListView)findViewById(R.id.child_list);
		mList.setOnItemClickListener(this);
		MLog.write(Log.ERROR, this.toString(), "커서 카운트  "+ mCursor.getCount()+ ", "+ SystemClock.currentThreadTimeMillis());
		
		request_Get_Student_List(new CPreferences(mCon).getShared_MDN());
	}

	//  app Close => onDestroy Call 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//Toast.makeText(Child_List.this,"앱종료....... ", Toast.LENGTH_SHORT).show();
		//finish();
		onDestroy();
	}


	private com.actionbarsherlock.app.ActionBar mActionBar ;
	//액션바 설정
	private void init_ACtionBar()
	{
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(R.string.actionbar_title_childlist);
		((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setVisibility(View.GONE);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setVisibility(View.GONE);
	}
	
	private void getCursor() {
		mCursor  = mDBManager.get_Child_Data(null, DB_Define.KEY_CHILD_ID +" ASC");//DESC
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		mCursor.moveToPosition(pos);
		String userId =  mCursor.getString(DB_Define.COLUMN_CHILD_ID);
		request_Student_Info(userId, "token", pos);
		
//		Intent in = new Intent(mCon, TutorialSlide.class);
//		Intent in = new Intent(mCon, MetroMain.class);
//		in.putExtra("userId"	,  mCursor.getString(DB_Define.COLUMN_CHILD_ID) );
//		in.putExtra("name"		,  mCursor.getString(DB_Define.COLUMN_CHILD_NAME));
//		in.putExtra("lastdate"	,  mCursor.getString(DB_Define.COLUMN_CHILD_LASTDATE));
//		in.putExtra("age"		,  mCursor.getString(DB_Define.COLUMN_CHILD_ANG));
//		in.putExtra("sex"		,  mCursor.getString(DB_Define.COLUMN_CHILD_GENDER));
//		in.putExtra("cm"		,  mCursor.getString(DB_Define.COLUMN_CHILD_CM));
//		in.putExtra("kg"		,  mCursor.getString(DB_Define.COLUMN_CHILD_KG));
//		in.putExtra("bmi"		,  mCursor.getString(DB_Define.COLUMN_CHILD_BMI));
//		in.putExtra("g_point"	,  mCursor.getString(DB_Define.COLUMN_CHILD_G_POINT));
//		in.putExtra("ppm"		,  mCursor.getString(DB_Define.COLUMN_CHILD_PPM));
//		in.putExtra("cohd"		,  mCursor.getString(DB_Define.COLUMN_CHILD_COHD));
//		in.putExtra("cm_status"	,  mCursor.getString(DB_Define.COLUMN_CHILD_CM_STATUS));
//		in.putExtra("kg_status"	,  mCursor.getString(DB_Define.COLUMN_CHILD_KG_STATUS));
//		in.putExtra("bmi_status",  mCursor.getString(DB_Define.COLUMN_CHILD_BMI_STATUS));
//		in.putExtra("ppm_status",  mCursor.getString(DB_Define.COLUMN_CHILD_PPM_STATUS));
//		in.putExtra("schoolGradeId",  mCursor.getString(DB_Define.COLUMN_CHILD_SCHOOL_ID)); //스쿨ID
//		in.putExtra("mGradeId",  	  mCursor.getString(DB_Define.COLUMN_CHILD_MASTER_ID));  //마스터 그래이드ID
//		
//		startActivity(in);
//		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
	}

	@Override
	public void finish() {
		super.finish();
	}
	
	private void request_get_EventCheck()
	{
		 JSONNetWork_Manager.request_EventCheck(mCon, new Call_Back() {
			
			@Override
			public void onRoaming(String message) {}
			
			@Override
			public void onGetResponsString(String data) {
				
				MLog.write(Log.ERROR,"" , "request_request_CHECK_EVENT() "+ data);	
				
				String Result= null, errMsg = null;
				JSONObject jsonObject = null;
		 		try
				{
				jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
				Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT);  // 결과값
				errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); // 에러 메세지
				setTotResult(Result);
				
					if(getTotResult() != null && getTotResult().equalsIgnoreCase("0")) //서버에서 "0"을 내려줄 경우 이벤트 웹뷰 팝업, "1"일 경우 이벤트 웹뷰 팝업 안함
					{
						Intent intent = new Intent(getApplicationContext(), PopEvent.class);
						startActivity(intent);
						
					}else {
					}
				} catch (Exception e)
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
	
	
	private  void request_Get_Student_List(String mdn)
	{
		mdn = mdn.replace("-", "");
		MLog.write(this, "request_Get_Student_List() " +mdn );
		
		JSONNetWork_Manager.request_Get_Student_List(
				mdn,
				GCMRegistrar.getRegistrationId(mCon),
				mCon,
				new Call_Back() {
					@Override
					public void onRoaming(String message) {	}
					
					@Override
					public void onGetResponsString(String data) {
							MLog.write(Log.ERROR, "", "request_Get_Student_List()\n " +data );
							String Result = null, errMsg = null;
							JSONObject jsonObject = null;
							
							try
							{
							jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
							Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); // ���
							errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); // ���� �޼���
							
							if ( Result != null && Result.equalsIgnoreCase("0") )
							{
								ContentValues values = new ContentValues();
								JSONArray array = new JSONArray( jsonObject.optString(JSONNetWork.RETURN_KEY_VALUE));
								String selectionArgs = "";
								for(int i = 0; i < array.length(); i++)
								{
									if(array.length() == 0 ) return;
									MLog.write(Log.ERROR,this.toString(), "array= "+array.get(i) );
									String child_id = array.getJSONObject(i).optString(JSONNetWork.KEY_USER_ID);
									selectionArgs = selectionArgs+ child_id+",";
									values.put(DB_Define.KEY_CHILD_ID,   	child_id);
									values.put(DB_Define.KEY_CHILD_NAME, 	 array.getJSONObject(i).optString(JSONNetWork.KEY_USER_NAME));
									values.put(DB_Define.KEY_CHILD_GENDER, 	 array.getJSONObject(i).optString(JSONNetWork.KEY_USER_SEX));
									values.put(DB_Define.KEY_CHILD_AGE, 	 array.getJSONObject(i).optString(JSONNetWork.KEY_USER_AGE));
									values.put(DB_Define.KEY_CHILD_BIRTHDAY ,array.getJSONObject(i).optString(JSONNetWork.KEY_USER_BIRTHDATE));
									values.put(DB_Define.KEY_CHILD_SCHOOL_ID,array.getJSONObject(i).optString(JSONNetWork.KEY_USER_SCHOOL_GID));
									values.put(DB_Define.KEY_CHILD_LASTDATE, array.getJSONObject(i).optString(JSONNetWork.KEY_USER_DATE));
									
									mCursor = mDBManager.get_Child_Data(DB_Define.KEY_CHILD_ID +" = \'"+ child_id +"\'", null);
									if( mCursor.getCount() == 0)
									{
										mDBManager.insert_Child_Data(values);
									}
									else
									{
										mDBManager.update_Child_Data(values, DB_Define.KEY_CHILD_ID +" = \'"+ child_id +"\'");	
									}
									values.clear();
									
								}
								DB_NotDataDelete(selectionArgs);
								
								maxCount = array.length();
								Message msg = mHandler.obtainMessage(DATA_ITEM_DOWNLOAD);
								mHandler.sendMessage(msg);
							}
							else //if(Result != null && Result.equalsIgnoreCase("2") )
							{	
								Message msg = mHandler.obtainMessage(DATA_ERROR, errMsg);
								mHandler.sendMessage(msg);
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}

					
					@Override
					public void onGetResponsData(byte[] data) {				}
					
					@Override
					public void onError(String error) {
						MLog.write(Log.ERROR, "", "request_Get_Student_List()\n " +error );
						Message msg = mHandler.obtainMessage(DATA_ERROR, error);
						mHandler.sendMessage(msg);
					}
				});
		request_get_EventCheck();
	} 
	

	private void DB_NotDataDelete(String selectionArgs) {
		if(selectionArgs.length() > 0 )
			selectionArgs =  selectionArgs.substring(0, selectionArgs.length()-1);
		
		Uri u= Uri.parse(Define.URI +DB_Define.URI_CHILD_DATA+ DB_Define.DELETE);
		SQLiteDatabase  db = mCon.openOrCreateDatabase("content.db" , Context.MODE_PRIVATE, null);
		String sql ="DELETE FROM "+DB_Define.URI_CHILD_DATA +" WHERE "+DB_Define.KEY_CHILD_ID +" NOT IN ("+ selectionArgs+")"; 
		Log.e("" , sql );
		db.execSQL(sql);
		db.close();
		
	}
	

	private void request_Student_Info(final String userId, String token, final int position)
	{
		JSONNetWork_Manager.request_Get_Student_Info(userId, token, mCon, new Call_Back() 
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
				Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); 
				errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); 
				
				if ( Result != null && Result.equalsIgnoreCase("0") )
				{
						
					ContentValues values = new ContentValues();
					JSONObject array = new JSONObject(jsonObject.optString(JSONNetWork.RETURN_KEY_VALUE));
					if(array != null)
					{
					Intent in = new Intent(mCon, MetroMain.class);
					in.putExtra("userId"	,  userId );
					in.putExtra("name"		,  array.optString(JSONNetWork.KEY_USER_NAME));
					in.putExtra("sex"		,  array.optString(JSONNetWork.KEY_USER_SEX));
					in.putExtra("age"		,  array.optString(JSONNetWork.KEY_USER_AGE));
					in.putExtra("lastdate"	,  array.optString(JSONNetWork.KEY_USER_DATE));
					in.putExtra("cm"		,  array.optString(JSONNetWork.KEY_USER_HEIGHT));
					in.putExtra("cm_status"	,  array.optString(JSONNetWork.KEY_USER_HEIGHT_STATUS));
					in.putExtra("kg"		,  array.optString(JSONNetWork.KEY_USER_WEIGHT));
					in.putExtra("kg_status"	,  array.optString(JSONNetWork.KEY_USER_WEIGHT_STATUS));
					in.putExtra("bmi"		,  array.optString(JSONNetWork.KEY_USER_BMI));
					in.putExtra("bmi_status",  array.optString(JSONNetWork.KEY_USER_BMI_STATUS));
					in.putExtra("ppm"		,  array.optString(JSONNetWork.KEY_USER_PPM));
					in.putExtra("cohd"		,  array.optString(JSONNetWork.KEY_USER_COHD));
					in.putExtra("ppm_status",  array.optString(JSONNetWork.KEY_USER_PPM_STATUS));
					in.putExtra("g_point"	,  array.optString(JSONNetWork.KEY_USER_G_POINT));
					in.putExtra("mGradeId"	,  array.optString(JSONNetWork.KEY_USER_MASTER_GID) );
				
					mCursor.moveToPosition(position);
					in.putExtra("schoolGradeId",  mCursor.getString(DB_Define.COLUMN_CHILD_SCHOOL_ID)); 

					
					// 자녀리스트 앱종료하지 않고도 업데이트 되도록 수정  
					//TTA 1,2차결함 보고 서로 인한 수정 tharaud 
					//2차 2015.03.10 최종본 보낸 후 2015.03.16 GS인증으로 최종컨폼됨..
					//startActivity(in);
					startActivityForResult(in, CHILD_CODE);  
					
					//((Child_List) getParent()).startChildActivity("second", in);
					overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
					
						MLog.write(Log.ERROR,this.toString(), "array= "+array);
//						values.put(DB_Define.KEY_CHILD_MASTER_ID,	array.optString(JSONNetWork.KEY_USER_MASTER_GID));
//						values.put(DB_Define.KEY_CHILD_CM_STATUS, 	array.optString(JSONNetWork.KEY_USER_HEIGHT_STATUS));
//						values.put(DB_Define.KEY_CHILD_KG,		array.optString(JSONNetWork.KEY_USER_WEIGHT));
//						values.put(DB_Define.KEY_CHILD_BMI, 	array.optString(JSONNetWork.KEY_USER_BMI));
//						
//						values.put(DB_Define.KEY_CHILD_PPM_STATUS, 	array.optString(JSONNetWork.KEY_USER_PPM_STATUS));
//						values.put(DB_Define.KEY_CHILD_BMI_STATUS, 	array.optString(JSONNetWork.KEY_USER_BMI_STATUS));
//						values.put(DB_Define.KEY_CHILD_G_POINT,		array.optString(JSONNetWork.KEY_USER_G_POINT));
//						values.put(DB_Define.KEY_CHILD_KG_STATUS	,array.optString(JSONNetWork.KEY_USER_WEIGHT_STATUS));
//						values.put(DB_Define.KEY_CHILD_COHD, 		array.optString(JSONNetWork.KEY_USER_COHD));
//						values.put(DB_Define.KEY_CHILD_CM, 			array.optString(JSONNetWork.KEY_USER_HEIGHT));
//						values.put(DB_Define.KEY_CHILD_PPM, 		array.optString(JSONNetWork.KEY_USER_PPM));
//						String child_id = array.optString(JSONNetWork.KEY_USER_ID);
//						values.put(DB_Define.KEY_CHILD_ID,   	child_id);
//						values.put(DB_Define.KEY_CHILD_LASTDATE,array.optString(JSONNetWork.KEY_USER_DATE));
//						
//						
//						if( mCursor.getCount() == 0)
//						{
//							mDBManager.insert_Child_Data(values);
//						}
//						else
//						{
//							mDBManager.update_Child_Data(values, DB_Define.KEY_CHILD_ID +" = \'"+ child_id +"\'");	
//						}
//						values.clear();
					}
//					nowCount = nowCount +1;
//					Message msg = mHandler.obtainMessage(DATA_DOWNLOAD);
//					mHandler.sendMessage(msg);
				}
				else if(Result.equalsIgnoreCase("6")) 
				{
					Message msg = mHandler.obtainMessage(DATA_EMPTY);
					mHandler.sendMessage(msg);
				}else if(Result.equalsIgnoreCase("3")){ 
					Message msg = mHandler.obtainMessage(DATA_STUDENT_EMPTY);
					mHandler.sendMessage(msg);
				}
				else //if(Result != null && Result.equalsIgnoreCase("2") )
				{	
					Log.e("" , "userId ="+ userId);
					Message msg = mHandler.obtainMessage(DATA_ERROR, errMsg);
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
			public void onError(String error) {
				MLog.write(Log.ERROR, "", "request_Get_Student_Info()\n " +error+ "����2" );
				Message msg = mHandler.obtainMessage(DATA_ERROR, error);
				mHandler.sendMessage(msg);
			}
		});
	}
	
	//public void startChildActivity(Intent intent) {
	 //   Window window = getLocalActivityManager().startActivity(id, intent);
	  //    if (window != null) {
	   //     setContentView(window.getDecorView());
	   // }
	 // }
	
	private void  DB_UpdateOrDelete(ContentValues value, HashMap<String, String> map)
	{
		mCursor.moveToFirst();
		String child_id; 
		do{
			
			  for (Map.Entry<String, String> entry : map.entrySet()) 
			  {
				  child_id  = entry.getKey().toString();
				  mDBManager.delete_Child_Data(DB_Define.KEY_CHILD_ID +" = \'"+ child_id +"\'");
			  } 
                
//			Cursor c = mDBManager.get_Child_Data(DB_Define.KEY_CHILD_ID +" = '"+child_id+"'" , "");
//			if(c.getCount() == 0)
//			{
//				mDBManager.delete_Child_Data(DB_Define.KEY_CHILD_ID +" = \'"+ child_id +"\'");
//			}
//			else 
//			{
//				mDBManager.update_Child_Data(value, DB_Define.KEY_CHILD_ID +" = \'"+ child_id +"\'");
//			}
		}while(mCursor.moveToNext());
	}
	
	
	public Handler mHandler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case DATA_DOWNLOAD:
				if(mAdapter != null )mAdapter.notifyDataSetChanged();
				if(maxCount == nowCount)
				{
					getCursor();
					mAdapter = new ChildListAdapter(mCon, mCursor, true);
					mList.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					mProBar.setVisibility(View.GONE);
				}
				Log.e("" , "COunt NowCount = "+ nowCount+", maxCount ="+ maxCount);
				break;
			case DATA_ITEM_DOWNLOAD:
				getCursor();
				mAdapter = new ChildListAdapter(mCon, mCursor, true);
				mList.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				mProBar.setVisibility(View.GONE);
//				if(mCursor.getCount() > 0 )
//				{
//					mCursor.moveToFirst();
//					do{
//						request_Student_Info( mCursor.getString(DB_Define.COLUMN_CHILD_ID) , "token");
//					}while(mCursor.moveToNext());
//				}
				break;
				
			case DATA_ERROR:
				Popup_Manager.Show_Error_Dialog(mCon, (String)msg.obj, new Popup_Manager.OneButton_Handle() {
					
					@Override
					public void onOK() {  
						Log.i("Chile_List","====DATA_ERROR Show_Error_Dialog Null=====");
						mProBar.setVisibility(View.GONE);
						
					}
				});
				break;
			case DATA_EMPTY:
				Popup_Manager.Show_Dialog(mCon, android.R.string.ok, R.string.child_list_popup_data_empty_msg,  android.R.string.ok, new OneButton_Handle() {
					
					@Override
					public void onOK() {
						
					}
				});
				break;
			case DATA_STUDENT_EMPTY:	
				Popup_Manager.Show_Dialog(mCon, android.R.string.ok, R.string.child_list_popup_data_student_empty_msg,  android.R.string.ok, new OneButton_Handle() {
					
					@Override
					public void onOK() {
						
					}
				});
				break;
			}
		}
	};
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) 
	{
		
        menu.setHeaderTitle("사진선택");
		menu.add(0, MENU_ALBUM, 0, R.string.child_list_menu_1);
		menu.add(0, MENU_CAMERA, 0, R.string.child_list_menu_2);
		menu.add(0, MENU_CLAER, 0, R.string.child_list_menu_3);
//		super.onCreateContextMenu(menu, v, menuInfo);
	}
	 
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch (item.getItemId() ) 
		{
		case MENU_ALBUM:
			Intent i = new Intent(Intent.ACTION_PICK);
			i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, REQ_CODE_PICK_GALLERY);
			break;
		case MENU_CAMERA:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, makeTempImageFile());
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQ_CODE_PICK_CAMERA);
			break;
			
		case MENU_CLAER:
			ContentValues values = new ContentValues();
			values.put(DB_Define.KEY_CHILD_PHOTO_URI,  "");
			mDBManager.update_Child_Data(values,  DB_Define.KEY_CHILD_NAME + " = '"+new CPreferences(mCon).getNowPosition()+"'");
			getCursor();
			mAdapter.changeCursor(mCursor);
			mAdapter.notifyDataSetChanged();
			mList.refreshDrawableState();
			break;
		}
		
		return super.onContextItemSelected(item);
	}
	
	private Uri makeTempImageFile()
	{
		File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/temp/");
		if (!path.exists()) 
			path.mkdirs();
		File file = new File(path, "tempimage.jpg");
		return Uri.fromFile(file);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("" , "onActivityResult("+ requestCode+","+ resultCode+", "+ data+")");
		
		if(resultCode == RESULT_OK)
		{
			if (requestCode == REQ_CODE_PICK_GALLERY)
			{
				cropImage(data.getData());
			}
			else if(requestCode == REQ_CODE_PICK_CAMERA)
			{
				cropImage(makeTempImageFile());
			}
			else if(requestCode == REQ_CODE_PICK_CROP)
			{
				String filename = makeTempImageFile().getPath().replace(".jpg", "")	+SystemClock.currentThreadTimeMillis()+".jpg";
				LCommonFunction.copyFile(new File(makeTempImageFile().getPath()), filename);
				ContentValues values = new ContentValues();
				values.put(DB_Define.KEY_CHILD_PHOTO_URI, filename);
				mDBManager.update_Child_Data(values,  DB_Define.KEY_CHILD_NAME + " = '"+new CPreferences(mCon).getNowPosition()+"'");
				
				getCursor();
				
				if(mAdapter != null)
				{
					mAdapter.changeCursor(mCursor);
					mAdapter.notifyDataSetChanged();
					mList.refreshDrawableState();
				}
				Log.e("", "REQ_CODE_PICK_CROP "+filename);
			}else if(requestCode==CHILD_CODE){
				request_Get_Student_List(new CPreferences(mCon).getShared_MDN());
			}
		}
			
	}
	
	  
	   
	   
	   
	   
	private void cropImage(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> cropToolLists = getPackageManager().queryIntentActivities(intent, 0);
		int size = cropToolLists.size();
		if (size != 0) {
			intent.setData(data);
			intent.putExtra("aspectX", 240);
			intent.putExtra("aspectY", 240);
			intent.putExtra("output",  makeTempImageFile());
			Intent i = new Intent(intent);
			ResolveInfo res = cropToolLists.get(0);
			i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			startActivityForResult(i, REQ_CODE_PICK_CROP);
		}
	}
	
	 private ImageLoader mLoader;
	DisplayImageOptions mDisOption;
	private class ChildListAdapter extends CursorAdapter 
	{
		public ChildListAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			mLoader = ImageLoader.getInstance();
			
			mDisOption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.childlist_default_image)
				.showStubImage(R.drawable.childlist_default_image)
//				.showImageOnFail(R.drawable.btn_close)
				.imageScaleType(ImageScaleType.EXACTLY)
				.cacheInMemory(true)
				.cacheOnDisc(true)
//				.displayer(new RoundedBitmapDisplayer(20)) //이미지에 라운드 효과 주기				
				.build();
			
		}

		@Override
		public void bindView(View view, Context con, final Cursor cur) {
			ChildListItemHolder holder = null;
			final String name ; 
			if(view != null)
			{
				holder = (ChildListItemHolder)view.getTag();
				String uri =  cur.getString(DB_Define.COLUMN_CHILD_PHOTH_URI);
				if(uri != null && uri.length() > 0 )
//					holder.img.setImageURI(Uri.parse(uri)) ;
					mLoader.displayImage("file://"+uri, holder.img, mDisOption);
				else
					mLoader.displayImage("drawable://"+R.drawable.childlist_default_image, holder.img, mDisOption);
//					holder.img.setImageDrawable(mDefault_Box_Image);
				
				holder.name =  cur.getString(DB_Define.COLUMN_CHILD_NAME);
				name = holder.name;
				holder.toptx.setText(holder.name);
				String date = cur.getString(DB_Define.COLUMN_CHILD_LASTDATE);
				if(date.equalsIgnoreCase("null"))
				{
					date = "미측정";
				}
				holder.bottomtx.setText(date);
				holder.middtx.setText(cur.getString(DB_Define.COLUMN_CHILD_ANG) +"세");
				holder.img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//cur.getString(DB_Define.COLUMN_CHILD_NAME);
						if(cur.getString(DB_Define.COLUMN_CHILD_ID)==null 
									|| cur.getString(DB_Define.COLUMN_CHILD_ID).equals("")){
						
						}else{
							
							
						}
						//if(COLUMN_CHILD_ID)
						new CPreferences(mCon).setNowPosition(name);
						registerForContextMenu(v);
						openContextMenu(v);
					}
				});
			}
		}

		@Override
		public View newView(Context context, Cursor arg1, ViewGroup arg2) {
			View v = LayoutInflater.from(context).inflate(R.layout.child_list_item, null );
			ChildListItemHolder holder = new ChildListItemHolder();
			holder.img 		= (ImageView)v.findViewById(R.id.child_list_item_img);
			holder.toptx    = (TextView)v.findViewById(R.id.child_list_item_top_tx);
			holder.middtx   = (TextView)v.findViewById(R.id.child_list_item_mid_tx);
			holder.bottomtx = (TextView)v.findViewById(R.id.child_list_item_bottom_tx);
			v.setTag(holder);
			return v;
		}
	}
	
	class ChildListItemHolder
	{
		ImageView img;
		TextView toptx;
		TextView middtx;
		TextView bottomtx;
		String name;		
	}
	
	public String getTotResult() {
		return totResult; 
	}
	
	public void setTotResult(String totResult) {
		this.totResult = totResult; 
	}
}
