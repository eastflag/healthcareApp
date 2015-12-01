package kr.co.aura.mtelo.healthcare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.LCommonFunction;
import kr.co.aura.mtelo.healthcare.util.MLog;
import kr.co.aura.mtelo.healthcare.video.VideoList;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class WEBActivity extends SherlockActivity {
	/** Called when the activity is first created. */

	WebView web;
	ProgressBar progressBar;

	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 10000;
	private final static int CAMERA_RESULTCODE = 10;

	private String mURL;
	  private int mReturnCode;
	  private int mResultCode;
	  private Intent mResultIntent;
	  private boolean mUploadFileOnLoad = false;
	  
	  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.i("WEBActivity","  mReturnCode ::" + resultCode +"mResultCode" +requestCode );
		 if(null==mUploadMessage)
		 {
	        mReturnCode = requestCode;
	        mResultCode = resultCode;
	        mResultIntent = intent;
	        mUploadFileOnLoad = true;
		    return;
		  }else{
	        mUploadFileOnLoad = false;
		  }
		 Log.i("WEBActivity","  mReturnCode ::" + mReturnCode +"mResultCode" +mResultCode );
		 
		Uri result = null;
		if (requestCode == FILECHOOSER_RESULTCODE) 
		{
			result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
		else if(requestCode == CAMERA_RESULTCODE)
		{
			Uri mypic = picUri;
			result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	private Uri picUri;
	@SuppressLint("JavascriptInterface")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acvibity_webview);

		init_ACtionBar();
		web = (WebView) findViewById(R.id.main_webview);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
//		progressBar = (SquareProgressBar) findViewById(R.id.progressBar1);
		progressBar.bringToFront();
//		progressBar.setImage(R.drawable.icon);
//		progressBar.setColor("#3fc6f3");
//		progressBar.setWidth(8);
//		progressBar.setOpacity(false);
		
		web.getSettings().setLoadWithOverviewMode(true);
		web.getSettings().setUseWideViewPort(true);
		web.getSettings().setJavaScriptEnabled(true);
		//웹뷰의 크기를 화면에 맟춘다. 허나 동작이 이상해짐, 사용중지 deprecated
//		web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			web.getSettings().setAllowUniversalAccessFromFileURLs(true);
			web.getSettings().setAllowFileAccessFromFileURLs(true);
			web.getSettings().setAllowContentAccess(true);
			web.getSettings().setAppCacheEnabled(true);
		}

		web.loadUrl(mURL);
		MLog.write(Log.DEBUG, this.toString(), "WEB URL = "+ mURL);
		
		
		web.setWebChromeClient(	new MyWebChromeViewClient() );
//		web.setWebViewClient(new myWebViewClient() );
		//객체, 노출할 DOM 명칭
////        web.addJavascriptInterface(new AndroidBridge(), "android");
//		
		// 서버에서 보낸 쿠키체크
		checkCookie(mURL);
	}
	
	
	
	/**
	 * 업데이트를 위해 서버에서 내려준 쿠키값을 확인합니다.
	 * @param url
	 * @author HYT. 2014.02.21.
	 */
	private void checkCookie(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(1500);
				
				try {
					// 쿠키를 얻어온다.
					CookieSyncManager.createInstance(getBaseContext());
					CookieManager cookieManager = CookieManager.getInstance();
					cookieManager.setAcceptCookie(true);
					String cookie = cookieManager.getCookie(url);
					cookieManager.hasCookies();
					Log.i("WEBActivity", "cookie==" + cookie + ", cookieManager.hasCookies()==" + cookieManager.hasCookies());
					
					int idx = cookie.indexOf("isUpdate");
					Log.i("WEBActivity", "cookieStr.indexOf:" + idx);
					
					String value = cookie.substring(idx, idx + 10);
					Log.i("WEBActivity", "value:" + value);
					
					String result = value.substring(9, 10);
					Log.i("WEBActivity", "result:" + result);
					
					checkUpdate(result);
					
				} catch (Exception e) {
					e.getStackTrace();
					Log.e("WEBActivity", "서버에서 업데이트 파라미터를 제대로 보냈는지 확인하십시요.");
					
				} finally {
					CookieSyncManager.getInstance().startSync(); 
					CookieSyncManager.getInstance().stopSync();
					
				}
				
			}
		}).start();
	}

	
	
	private void checkUpdate(String result) {
		if (result.equalsIgnoreCase("Y")) {
			Log.i("WEBActivity", "이 앱의 업데이트 버전이 있습니다.");
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
					String dialogTitle = "알림";
					String dialogMessage = "스마트 건강지킴이의 새 업데이트 버전이 있습니다. 업데이트 하시겠습니까?";
					String positiveBtnText = "예";
					String negativeBtnText = "아니오";
					
					AlertDialog.Builder dialog = new AlertDialog.Builder(WEBActivity.this);
					dialog.setCancelable(false);
					dialog.setTitle(dialogTitle);
					dialog.setMessage(dialogMessage);
					dialog.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									String appPackage = "kr.co.aura.mtelo.healthcare";
									goGooglePlayUpdate(appPackage);
									dialog.dismiss();
								}
							});
					dialog.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					dialog.show();
				}
			});

		}
	}

	
	
	/**
	 * 구글 플레이의 해당 패키지 앱 화면으로 이동합니다.
	 * @param appPackage 이동하길 원하는 앱의 패키지명
	 * @author HYT. 2014.02.21.
	 */
	private void goGooglePlayUpdate(String appPackage) {
		String url = "http://play.google.com/store/apps/details?id=" + appPackage;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
	
	
	
	private ActionBar mActionBar ;
	//액션바 설정
	private void init_ACtionBar()
	{
		String str = null;
		Intent in = getIntent();
		String name = in.getStringExtra("name");
		String sex = in.getStringExtra("sex");
		String add_info_param = in.getStringExtra("menu");
		mURL = in.getStringExtra("url");
		mURL = mURL + "&ver=" + LCommonFunction.getAppVersionName(getBaseContext());// 이전 배포된 앱들의 업데이트를 위해 추가
		                                                                             										// 버전 정보를 보낸다. 2014.02.20. HYT.
		if(add_info_param !=null){
			//setRequestedOrientation(Activity.);
			Log.i("action bar", "1111");
		}
		
		if(sex != null)
		{
			int add_str = sex.equalsIgnoreCase("m")? R.string.actionbar_title_title_male: R.string.actionbar_title_title_female;
			str = name + getResources().getString(add_str);
		}
		
		mActionBar = getSupportActionBar();
//		mActionBar.setTitle("정보 동의");
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		if(name == null) {
			str="공지사항";
		}
		
		
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(str);
		((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setText(new CPreferences(getApplicationContext()).getTitleLastDate());	//��¥
		((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				finish();
				onBackPressed();
			}
		});
	}

	
	private boolean mGoBack = false;
	@Override
	public void onBackPressed() {
		if(mGoBack)
		{
			web.loadUrl("javascript:page_close()");
			mGoBack = false;
		}
		else{
			super.onBackPressed();
		}
	}
	
	
	private final Handler handler = new Handler();
	 private class AndroidBridge {
	    	public void callAndroid(final String type) { //반드시 final로 선언
	    		
	    		/*****************************************************************************
	    		 * handler.post() : 메인 스레드의 실행 중인 큐에 집어넣는다.
	    		 *                  나중에 메인 스레드는 기회가 되면 run() 메서드를 호출하고 
	    		 *                  이것은 setText()를 호출해 TextView 객체 내의 텍스트를 변경함
	    		 *****************************************************************************/
	    		MLog.write(Log.ERROR,"", "callAndroid(" + type + ")");
	    		handler.post(new Runnable() {

					@Override
					public void run() {
						Intent in = new Intent(getApplicationContext(), VideoList.class);
						in.putExtra("type", type);
						startActivity(in);
						overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
					}
	    			
	    		});
	    	}
	    }
	 
	private Uri imageUri;
	private void showAttachmentDialog(ValueCallback<Uri> uploadMsg) {
	    this.mUploadMessage = uploadMsg;

	    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "TestApp");
	    if (!imageStorageDir.exists()) {
	        imageStorageDir.mkdirs();
	    }
	    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
	    this.imageUri = Uri.fromFile(file); // save to the private variable

	    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

	    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	    i.addCategory(Intent.CATEGORY_OPENABLE);
	    i.setType("image/*");

	    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
	    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[] { captureIntent });
	    this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
	}


		private class myWebViewClient extends WebViewClient 
	{
//		   public boolean shouldOverrideUrlLoading(WebView view, String url) { 
////			   if(progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);
//	            view.loadUrl(url); 
//	            return true; 
//	        }
//		   
		   @Override
			public void onPageFinished(WebView view, String url) {			}
		   
		   @Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {}
	}
		

		
		private class MyWebChromeViewClient extends WebChromeClient {	
    	@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				// TODO Auto-generated method stub
    		super.onShowCustomView(view, callback);
    	    if (view instanceof FrameLayout){
    	        FrameLayout frame = (FrameLayout) view;
    	        if (frame.getFocusedChild() instanceof VideoView){
    	            VideoView video = (VideoView) frame.getFocusedChild();
    	            frame.removeView(video);
    	            setContentView(video);
    	            video.setOnCompletionListener((OnCompletionListener) this);
    	            video.setOnErrorListener((OnErrorListener) this);
    	            video.start();
    	        }
    	    }

			}


		@Override
    	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
           MLog.write(Log.ERROR, this.toString(), "onJsAlert(!" + view + ", " + url + ", " + message + ", " + result + ")");
           result.confirm();
           if(message.startsWith("detail"))
           {
        	   mGoBack = true;
           }
           else
           {
        	   String userId = mURL.split("userId=")[mURL.split("userId=").length -1 ];
        	   String grdeId = message.split(",")[message.split(",").length -1 ];
        	   Intent in = new Intent(getApplicationContext(), VideoList.class);
        	   in.putExtra("type", message);
        	   in.putExtra("grdeId", grdeId);
        	   in.putExtra("userId", userId);
        	   startActivity(in);
        	   overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
           }
           return true; // I handled it
    	}
    	
    	
    	@Override
    	public void onProgressChanged(WebView view, int newProgress) {
//    		progressBar.setVisibility(View.VISIBLE);
    		progressBar.setProgress(newProgress);
    		if(newProgress == 0 ) progressBar.setVisibility(View.VISIBLE);
    		if(newProgress == 100 ) progressBar.setVisibility(View.GONE);
//    		super.onProgressChanged(view, newProgress);
    	}
    	
    
    	
    	
        //@Override
        public void openFileChooser(ValueCallback uploadMsg, String acceptType )  {      
             File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
            // Create the storage directory if it does not exist
            if (! imageStorageDir.exists()){
                imageStorageDir.mkdirs();                  
            }
            File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");  
            imageUri = Uri.fromFile(file); 

            final List<Intent> cameraIntents = new ArrayList<Intent>();
            final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            final PackageManager packageManager = getPackageManager();
            final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
            for(ResolveInfo res : listCam) {
                final String packageName = res.activityInfo.packageName;
                final Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraIntents.add(intent);
            }

            mUploadMessage = uploadMsg; 
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
            intent.addCategory(Intent.CATEGORY_OPENABLE);  
            intent.setType("image/*"); 
            Intent chooserIntent = Intent.createChooser(intent,"Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
            startActivityForResult(chooserIntent,  FILECHOOSER_RESULTCODE); 
        }
    }


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_left);
	}
}
