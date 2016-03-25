package kr.co.aura.mtelo.healthcare;




import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.actionbarsherlock.app.SherlockActivity;


public class PopEvent extends SherlockActivity implements OnClickListener{	
	ProgressBar progressBar;
	WebView webView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_event);
		
		webView = (WebView)findViewById(R.id.webPopup);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.bringToFront();
		
		webView.setWebViewClient(new myWebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		//WebSettings webSettings = webView.getSettings();
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.clearCache(true);
		webView.setInitialScale(100);
		webView.getSettings().setLoadWithOverviewMode(true);
		
		webView.loadUrl("http://210.127.55.205:82/HealthCare/popup/event.html"); //팝업 웹뷰 컨텐츠 IP 설정 실서버
		//webView.loadUrl("http://192.168.0.8:8081/healthcare/popup/event.html"); /팝업 웹뷰 컨텐츠 IP 설정
		//webView.loadUrl("http:m.naver.com");
		
		ImageButton button01 = (ImageButton) findViewById(R.id.imageButton1);
		button01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				
			}
			
		});
	}
	
	public class myWebViewClient extends WebViewClient 
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.INVISIBLE);	
		}	
	}
	
	public class myWebChromeClient extends WebChromeClient
	{
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			progressBar.setProgress(newProgress);
		}
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}


