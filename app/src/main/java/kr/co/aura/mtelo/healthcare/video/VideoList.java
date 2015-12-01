package kr.co.aura.mtelo.healthcare.video;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.database.DB_Define;
import kr.co.aura.mtelo.healthcare.database.DB_Maneger;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork.Call_Back;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.Content_Download;
import kr.co.aura.mtelo.healthcare.util.LCommonFunction;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager;
import kr.co.aura.mtelo.healthcare.util.UploadMuitpoartEntity;
import kr.co.aura.mtelo.healthcare.util.UploadMuitpoartEntity.ProgressListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class VideoList extends SherlockActivity implements OnItemClickListener{

	
	private ListView mList;
	private Context mCon;
	private ArrayList<VideoListItem> mItems;
	private VideoListAdapter mAdapter;
	
	private final int DATA_DOWNLOAD = 10000;
	private final int DATA_ERROR = DATA_DOWNLOAD +1;
	private final int FILE_UPLOAD_SUCCESS = DATA_DOWNLOAD +2;
	private final int FILE_UPLOAD_FAILED = DATA_DOWNLOAD +3;
	
	private ProgressBar mProBar;
	public final int REQ_CODE_PICK_GALLERY = 90001;
	public final int REQ_CODE_PICK_IMAGE = 90002;
	public final int REQ_CODE_PICK_CROP = 90003;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_list);
		mCon = VideoList.this;
		init_ACtionBar();
		imageLoaderConfige();
		
		mProBar  = (ProgressBar)findViewById(R.id.video_list_probar);
		mProBar.setVisibility(View.VISIBLE);
		mProBar.bringToFront();
		
		mList = (ListView)findViewById(R.id.child_list);
		mList.setOnItemClickListener(this);
		mItems = new ArrayList<VideoListItem>();
		Intent in = getIntent();
		String schoolId = in.getStringExtra("schoolGradeId");
		String grdeId 	= in.getStringExtra("grdeId");
		String userId 	= in.getStringExtra("userId");
		
		//웹에서 호출시 schoolId가 없고 userId가 들어온다, userId를 이용해서 DB에서 schoolId를 얻어올것
		if(schoolId == null)
		{//웹에서 호출시
			DB_Maneger manager = new DB_Maneger(this);
			Cursor c = manager.get_Child_Data(DB_Define.KEY_CHILD_ID +" = '"+userId+"'", "");
			c.moveToFirst();
			if(c.getCount() > 0 )schoolId = c.getString(DB_Define.COLUMN_CHILD_SCHOOL_ID);
//			MLog.write(Log.ERROR, this.toString(), "shchoolId = "+schoolId +" grdeId = "+grdeId+" userId = "+ userId );
		}
		else
		{//메트로 메인에서 호출시
//			getMasterGradeId(schoolId, userId);
		}
		
		request_video_list(schoolId , grdeId);
	}
	
	
	
	private ActionBar mActionBar ;
	//액션바 설정
	private void init_ACtionBar()
	{
		mActionBar = getSupportActionBar();
//		mActionBar.setTitle("정보 동의");
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(R.string.actionbar_title_videolist);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setText(new CPreferences(getApplicationContext()).getTitleLastDate());	//��¥
		((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, final int pos, long arg3) {
		
		final CPreferences cp = new CPreferences(mCon);
		if(cp.getDataAlertPopup().equalsIgnoreCase("true"))
		{
			VideoPlay(pos);
		}
		else
		{
			Popup_Manager.Show_Dialog(mCon, R.string.videolist_datapopup_tatle,
					R.string.videolist_datapopup_msg, 
					android.R.string.ok,
					R.string.videolist_datapopup_btn2, new Popup_Manager.TwoButton_Handle() {
				
				@Override
				public void onPostive() {
					VideoPlay(pos);		
				}
				
				@Override
				public void onNegative() {
					cp.setDataAlertPopup("true");
					VideoPlay(pos);		
				}
			});
			
		}

	}

	//2013.11.05 비디오 스트리밍이거나 다운로드후 플레이
	private void VideoPlay(int pos) {
		String currentapiVersion = (android.os.Build.VERSION.RELEASE).substring(0, 1);
		if (Integer.parseInt(currentapiVersion) >= 4)
		{
			// 미디어파일 플레이 하기
			Uri uri = Uri.parse(mItems.get(pos).video_url);
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setDataAndType(uri, "video/mp4");
			startActivity(it);
			
			Log.i("VideoList", "currentapiVersion=" +currentapiVersion + ", uri=" + uri.toString());
		}
		else
		{
			new Content_Download(mCon, mItems.get(pos).video_url, Define.DISK_CACHE_PATH+"/" , true) ;
		}
	}
	
	

	//운동사진 컨테스트 클릭 이벤트
	public void clickVideoUpload(View v)
	{
		get_Gelley_Image();
	}

	//갤러리 실핼 인텐츠 
	private void get_Gelley_Image() {
		Intent i = new Intent(Intent.ACTION_PICK);
		i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, REQ_CODE_PICK_GALLERY);
	}
	
	
	private void displayProgressBarNoti(String f)
	{
		UploadAsyc upload = new UploadAsyc(mCon);
		upload.execute(new CPreferences(mCon).getShared_MDN(), f);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	Log.e("" , "onActivityResult : "+ resultCode);
		if(resultCode == RESULT_OK)
		{
			if (requestCode == REQ_CODE_PICK_GALLERY)
			{
//				cropImage(data.getData());
					File f = new File(getPath(data.getData()));
//					LCommonFunction.copyFile(f, Define.DISK_CACHE_PATH+"/atoz.jpg");
//					Log.e("" , "onActivityResult("+ requestCode+","+ resultCode+", "+ data+", "+f.getAbsoluteFile()+")");
					
					Intent i = new Intent(mCon, ZoomView.class);
					i.putExtra("file", f.toString());
					startActivityForResult(i, REQ_CODE_PICK_IMAGE);
//					displayProgressBarNoti(f);
			}
			else if(requestCode == REQ_CODE_PICK_CROP)
			{
				File f = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() +"/temp/tempimage.jpg");
				LCommonFunction.copyFile(f, Define.DISK_CACHE_PATH+"/atoz.jpg");
//				Log.e("" , "onActivityResult("+ requestCode+","+ resultCode+", "+ data+", "+f.getAbsoluteFile()+")");
				displayProgressBarNoti(f.getAbsolutePath().toString());
			}
			else if(requestCode == REQ_CODE_PICK_IMAGE)
			{
				if(data == null)
				{//갤럭리를 재실행
					get_Gelley_Image();
				}
				else
				{//이미지 선택
					displayProgressBarNoti( data.getStringExtra("file"));
				}
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
			intent.putExtra("aspectX", getResources().getDisplayMetrics().widthPixels);
			intent.putExtra("aspectY", getResources().getDisplayMetrics().heightPixels);
			intent.putExtra("output",  makeTempImageFile());
			Intent i = new Intent(intent);
			ResolveInfo res = cropToolLists.get(0);
			i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			startActivityForResult(i, REQ_CODE_PICK_CROP);
		}
	}
	//임시파일을 생성한다
	private Uri makeTempImageFile()
	{
		File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/temp/");
		if (!path.exists()) 
			path.mkdirs();
		File file = new File(path, "tempimage.jpg");
		return Uri.fromFile(file);
	}
	
	
	public String getPath(Uri uri) {
	    String[] projection = {MediaStore.Images.Media.DATA};
	    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
	    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(columnIndex);
	}
	
	public Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case DATA_DOWNLOAD:
				mAdapter = new VideoListAdapter(mCon, mItems);	
				mList.setAdapter(mAdapter);
				mProBar.setVisibility(View.GONE);
				break;
				
			case DATA_ERROR:
				Popup_Manager.Show_Error_Dialog(mCon, (String)msg.obj, new Popup_Manager.OneButton_Handle() {
					
					@Override
					public void onOK() {
						mProBar.setVisibility(View.GONE);
					}
				});
				break;
			case FILE_UPLOAD_SUCCESS:
				Popup_Manager.Show_Dialog(mCon, 0,
												R.string.videolist_fileup_success_msg,
												android.R.string.ok,
												new Popup_Manager.OneButton_Handle() {
													
													@Override
													public void onOK() {
													}
												});
				break;
			case FILE_UPLOAD_FAILED:

				Popup_Manager.Show_Dialog(mCon, 0,
												R.string.videolist_fileup_failed_msg,
												android.R.string.ok,
												new Popup_Manager.OneButton_Handle() {
													
													@Override
													public void onOK() {
													}
												});
				break;	
				
			}
		}
	};
			
			
			
	private void request_video_list(String type, String grdeId)
	{
		
		JSONNetWork_Manager.request_GetVideoList(type, grdeId, mCon, new Call_Back() {
			@Override
			public void onRoaming(String message) {	}
			
			@Override
			public void onGetResponsString(String data) {
				try
				{
					Log.i("VideoList", "request_GetVideoList()\n"+data);
					String Result = null, errMsg = null;
					JSONObject jsonObject = null;
					
					jsonObject = new JSONObject(LCommonFunction.HtmlToJson(data));
					Log.i("VideoList","  === onGetResponsString"+jsonObject);
					Result = jsonObject.optString(JSONNetWork.RETURN_KEY_RESULT); // 결과값
					errMsg = jsonObject.optString(JSONNetWork.RETURN_KEY_ERRMSG); //에러 메세지
					if ( Result != null && Result.equalsIgnoreCase("0") )
					{
						JSONArray array = new JSONArray( jsonObject.optString(JSONNetWork.RETURN_KEY_VALUE));
						for(int i = 0; i < array.length(); i++)
						{
							mItems.add( new VideoListItem(
									array.getJSONObject(i).optString("thumbnailUrl"),
									array.getJSONObject(i).getString("videoUrl"),
									array.getJSONObject(i).optString("title"),
									array.getJSONObject(i).optString("duration"),
									ScaleType.FIT_XY
									));
						}
						Message msg = mHandler.obtainMessage(DATA_DOWNLOAD);
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
			public void onError(String error) 
			{
				Message msg = mHandler.obtainMessage(DATA_ERROR, error);
				mHandler.sendMessage(msg);
			}
		});
	}
	
	
	private String URL_Encoder(String url)
	{
		String encodedString = null; 
		try {
			 encodedString = URLDecoder.decode(url, "EUC-KR");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		Log.e("" , "URL_Encoder = "+ encodedString);
		return encodedString;
	}
	
	private DisplayImageOptions mDisOption;
	private void imageLoaderConfige()
	{
	/*	mLoaderConfig = new ImageLoaderConfiguration.Builder(mCon)
								.threadPoolSize(10)
								.memoryCache( new LruMemoryCache(2*688*286))  //리스트의 이미지 크기
								.discCache(new UnlimitedDiscCache(new File(Define.DISK_CACHE_PATH) ) )
								.discCacheFileCount(100)   //디스크 캐쉬파일의 최대수를 지정
//								.writeDebugLogs()           // 디버그용 로그쓰기
								.build();*/
		
		 mDisOption = new DisplayImageOptions.Builder()
		 						.showStubImage(R.drawable.video_list_empty)
								.showImageOnFail(R.drawable.network_disconnect)
								.imageScaleType(ImageScaleType.EXACTLY)
								.showImageForEmptyUri(R.drawable.icon)
								.cacheInMemory(true)
								.cacheOnDisc(true)
//								.displayer(new RoundedBitmapDisplayer(20))//이미지에 라운드 효과 주기
								.build();
	}
	
	

	class VideoListItem{
		String image_url;
		String video_url;
		String name;
		String time;
		ScaleType scaltype;
		
		public VideoListItem(String image_url, String video_url, String name,String time, ScaleType scaltype) {
			super();
			this.image_url = image_url;
			this.video_url = video_url;
			this.name = name;
			this.time = time;
			this.scaltype = scaltype;
//			MLog.write( Log.ERROR,"",  "image="+ image_url+"\nvideo="+video_url+"\nname="+name+"\ntime"+time );
		}
	}
	
	class VideoListAdapter extends ArrayAdapter<VideoListItem>
	{
		private LayoutInflater mInflater;
		private ImageLoader mLoader;
		
		
		public VideoListAdapter(Context context,  List<VideoListItem> objects) {
			super(context, 0, objects);
			mInflater = LayoutInflater.from(mCon);
			mLoader = ImageLoader.getInstance();		//로더 생성
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			VideoHolder holder ;
			if (convertView == null) 
			{
				holder = new VideoHolder();
				convertView =  mInflater.inflate(R.layout.video_list_item, parent, false);
				holder.image =  ((ImageView) convertView.findViewById(R.id.video_list_img));
				holder.name = (TextView)convertView.findViewById(R.id.video_list_bottom_tx1);
				holder.du = ((TextView)convertView.findViewById(R.id.video_list_bottom_tx2));
				convertView.setTag((VideoHolder) holder);
			}
			else   
			{
				holder = (VideoHolder)convertView.getTag();
			}
			
			VideoListItem item = getItem(position);
			String result;
			int time = Integer.parseInt(item.time) ;
			if( time > 60 )
			{
				int m = Integer.parseInt(item.time) /60;
				int s = Integer.parseInt(item.time) %60;
				result = m+"분"+" "+ s+"초";
			}
			else
			{
				result = time +" 초";
			}
			
			holder.du.setText(result);
			holder.name.setText(item.name);
			holder.image.setScaleType(item.scaltype);
			mLoader.displayImage(URL_Encoder(item.image_url), holder.image, mDisOption);  
			
			return convertView;
		}
	}
	
	class VideoHolder {
		TextView name;
		TextView du;
		ImageView image;
	}
	
	@Override
	public void finish() {
		mItems.clear();
		super.finish();
		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
	}
	
	 
	class UploadAsyc extends AsyncTask<String, Integer, Long>
	{
		ProgressDialog mPDialog  = null;
		long totalSize;
		boolean onCancle = false; 
		
		public UploadAsyc(Context ctx){
			mPDialog  = new ProgressDialog(ctx);
		}
		@Override
		protected Long doInBackground(String... arg0) {
			String url = (String)Define.getNetUrl()+Define.FILE_UPLOAD_CONTENT;
			//파라미터 등록
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("string", arg0[0]);
			
			//파라미터 등록
			Map<String, File> fileParams = new HashMap<String, File>();
			fileParams.put("file", new File(arg0[1]) );
			
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext httpContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(url);
				UploadMuitpoartEntity multipart = new UploadMuitpoartEntity(new ProgressListener() {
					@Override
					public void transferred(long transferred) {
						publishProgress((int)transferred);
					}
				});
				
				//Params 첨부
				for (String strKey : params.keySet()) {
					StringBody body = new StringBody(params.get(strKey).toString());
					multipart.addPart(strKey, body);
				}
				
				//파일첨부
				for (String keys : fileParams.keySet()) {
					multipart.addPart(keys , new FileBody(fileParams.get(keys)));
				}
				
				totalSize = multipart.getContentLength();
				//Time Check
				mPDialog.setMax((int)totalSize);
				httpPost.setEntity(multipart);
				HttpResponse response = httpClient.execute(httpPost,httpContext);
				InputStream is = response.getEntity().getContent();
				/**
				is를 가지고 추가 작업
			 */
			} catch (Exception e) {
				onCancelled();
				return 0L;
			}
			
			return 0L;
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mPDialog.dismiss();
			
			onCancle = true;
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			mPDialog.dismiss();
			if(onCancle)
			{
				Popup_Manager.Show_Dialog(mCon, 0,
						R.string.videolist_fileup_failed_msg,
						android.R.string.ok,
						new Popup_Manager.OneButton_Handle() {
							
							@Override
							public void onOK() {
							}
						});
			}
			else
			{
				Popup_Manager.Show_Dialog(mCon, 0,
						R.string.videolist_fileup_success_msg,
						android.R.string.ok,
						new Popup_Manager.OneButton_Handle() {
					
					@Override
					public void onOK() {
					}
				});
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mPDialog.setCancelable(false);
			mPDialog.setOnCancelListener(cancelListener);
			mPDialog.setMessage(getResources().getString(R.string.videolist_fileuploading));
			mPDialog.show();
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			//Progress 업데이트
			mPDialog.setProgress((int)progress[0]);
			if((int)progress[0] == 99)
				onCancelled();
		}
		
		OnCancelListener cancelListener = new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		};

		
	}
}

